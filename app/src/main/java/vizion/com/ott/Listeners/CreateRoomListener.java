package vizion.com.ott.Listeners;

import android.app.Activity;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;

import org.json.JSONException;
import org.json.JSONObject;

import vizion.com.ott.Activities.RoomsActivity;
import vizion.com.ott.Models.MyRoom;
import vizion.com.ott.Models.MyUser;
import vizion.com.ott.Utils.MyProgressDialog;

/**
 * Created by dthongvl on 11/13/16.
 */
public class CreateRoomListener implements Emitter.Listener {
    private Activity activity;
    private static CreateRoomListener ourInstance = new CreateRoomListener();

    public static CreateRoomListener getInstance(Activity activity) {
        ourInstance.activity = activity;
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
                    boolean isSuccess = data.getBoolean("isSuccess");
                    if(isSuccess){
                        createdRoom.setId(data.getString("room_id"));
                        createdRoom.setHostUid(MyUser.getInstance().getUid());
                        createdRoom.setState("joinable");
                        Toast.makeText(ourInstance.activity,data.getString("room_id"),Toast.LENGTH_LONG).show();
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
