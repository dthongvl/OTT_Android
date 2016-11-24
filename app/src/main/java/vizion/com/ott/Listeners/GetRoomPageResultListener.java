package vizion.com.ott.Listeners;

import android.app.Activity;

import com.github.nkzawa.emitter.Emitter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import vizion.com.ott.Models.RoomList;
import vizion.com.ott.Models.Room;

/**
 * Created by dthongvl on 11/13/16.
 */
public class GetRoomPageResultListener implements Emitter.Listener {
    private Activity activity;
    private static GetRoomPageResultListener ourInstance = new GetRoomPageResultListener();

    public static GetRoomPageResultListener getInstance(Activity activity) {
        ourInstance.activity = activity;
        return ourInstance;
    }

    private GetRoomPageResultListener() {
    }

    @Override
    public void call(final Object... args) {
        ourInstance.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                JSONObject data = (JSONObject) args[0];
                try {

                    JSONArray arrRooms = null;
                    arrRooms = data.getJSONArray("rooms");
                    ArrayList<Room> roomPage = new ArrayList<Room>();
                    for(int roomIndex =0; roomIndex < arrRooms.length();roomIndex++){
                        roomPage.add(new Room(arrRooms.getJSONObject(roomIndex)));
                    }
                    RoomList.getInstance().updateRoom(roomPage);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }
}
