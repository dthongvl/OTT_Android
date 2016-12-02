package vizion.com.ott.Listeners;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;

import org.json.JSONException;
import org.json.JSONObject;

import vizion.com.ott.Activities.WaitActivity;
import vizion.com.ott.Models.MyRoom;
import vizion.com.ott.Models.Room;
import vizion.com.ott.Utils.MyProgressDialog;

/**
 * Created by dthongvl on 11/13/16.
 */
public class CreateRoomListener implements Emitter.Listener {
    private Activity activity;
    private Room createdRoom;
    private static CreateRoomListener ourInstance = new CreateRoomListener();

    public static CreateRoomListener getInstance(Activity activity) {
        ourInstance.activity = activity;
        ourInstance.createdRoom = new Room();
        return ourInstance;
    }

    private CreateRoomListener() {
    }

    @Override
    public void call(final Object... args) {
        ourInstance.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                JSONObject data = (JSONObject) args[0];
                try {
                    Log.d("DEBUG", data.toString());
                    boolean isSuccess = data.getBoolean("isSuccess");
                    if(isSuccess){
                        MyRoom.getInstance().setId(data.getString("room_id"));
                        Intent intent = new Intent(ourInstance.activity, WaitActivity.class);
                        ourInstance.activity.startActivity(intent);
                    }
                    else{
                        Toast.makeText(ourInstance.activity,"failed",Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                finally {
                    MyProgressDialog.getInstance(ourInstance.activity, "").hideProgressDialog();
                }

            }
        });
    }
}
