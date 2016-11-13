package vizion.com.ott.Listeners;

import android.app.Activity;

import com.github.nkzawa.emitter.Emitter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import vizion.com.ott.Models.MyRoom;
import vizion.com.ott.Models.Room;

/**
 * Created by dthongvl on 11/13/16.
 */
public class ListRoomListener implements Emitter.Listener{
    private Activity activity;
    private static ListRoomListener ourInstance = new ListRoomListener();

    public static ListRoomListener getInstance(Activity activity) {
        ourInstance.activity = activity;
        return ourInstance;
    }

    private ListRoomListener() {
    }

    @Override
    public void call(final Object... args) {
        ourInstance.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                JSONObject data = (JSONObject) args[0];
                try {
                    MyRoom.getInstance().setTotalPage(data.getInt("total_page"));
                    JSONArray arrRooms = data.getJSONArray("rooms");
                    MyRoom.getInstance().clearListRooms();
                    for (int roomOrder = 0; roomOrder < arrRooms.length(); ++roomOrder) {
                        MyRoom.getInstance().addRoom(new Room(arrRooms.getJSONObject(roomOrder)));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
