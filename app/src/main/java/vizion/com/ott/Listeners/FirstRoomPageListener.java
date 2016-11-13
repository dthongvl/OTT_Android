package vizion.com.ott.Listeners;

import android.app.Activity;

import com.github.nkzawa.emitter.Emitter;

import org.json.JSONObject;

import vizion.com.ott.Models.MyRoom;

/**
 * Created by dthongvl on 11/13/16.
 */
public class FirstRoomPageListener implements Emitter.Listener{
    private Activity activity;
    private static FirstRoomPageListener ourInstance = new FirstRoomPageListener();

    public static FirstRoomPageListener getInstance(Activity activity) {
        ourInstance.activity = activity;
        return ourInstance;
    }

    private FirstRoomPageListener() {
    }

    @Override
    public void call(final Object... args) {
        ourInstance.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                JSONObject data = (JSONObject) args[0];
                MyRoom.getInstance().setRoomData(data.toString());
            }
        });
    }
}
