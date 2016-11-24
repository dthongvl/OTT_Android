package vizion.com.ott.Listeners;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;

import org.json.JSONException;
import org.json.JSONObject;

import vizion.com.ott.Activities.RoomsActivity;
import vizion.com.ott.Models.MyRoom;
import vizion.com.ott.Models.RoomList;

/**
 * Created by Razor on 20/11/2016.
 */
public class LeaveRoomListener implements Emitter.Listener{

    private Activity activity;

    private static LeaveRoomListener ourInstance = new LeaveRoomListener();

    public static LeaveRoomListener getInstance(Activity activity) {
        ourInstance.activity=activity;
        return ourInstance;
    }

    private LeaveRoomListener() {
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

                        MyRoom.getInstance().clear();
                        ourInstance.activity.finish();
                        Log.d("leave","ok");
                    }
                    else{
                        Log.d("leave","failed");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
