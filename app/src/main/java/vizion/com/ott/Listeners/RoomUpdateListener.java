package vizion.com.ott.Listeners;

import android.app.Activity;

import com.github.nkzawa.emitter.Emitter;

import org.json.JSONException;
import org.json.JSONObject;

import vizion.com.ott.Models.MyRoom;

/**
 * Created by dthongvl on 11/13/16.
 */
public class RoomUpdateListener implements Emitter.Listener {
    private Activity activity;
    private static RoomUpdateListener ourInstance = new RoomUpdateListener();

    public static RoomUpdateListener getInstance(Activity activity) {
        ourInstance.activity = activity;
        return ourInstance;
    }

    private RoomUpdateListener() {
    }

    @Override
    public void call(final Object... args) {
        ourInstance.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                isUpdate=true;
                JSONObject data = new JSONObject();
                try {
                    MyRoom.getInstance().setTotalPage(data.getInt("total_page"));
                    ourInstance.activity.getRoomPage(currentPage);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
