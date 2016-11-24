package vizion.com.ott.Listeners;

import android.app.Activity;
import android.util.Log;

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
                try {
                    RoomList.getInstance().setTotalPage(data.getInt("total_page"));
                    JSONArray arrRooms = data.getJSONArray("rooms");
                    Log.d("roompage",data.toString() );
                    ArrayList<Room> firstPage = new ArrayList<Room>();
                    for(int roomIndex =0; roomIndex < arrRooms.length();roomIndex++){
                        firstPage.add(new Room(arrRooms.getJSONObject(roomIndex)));
                    }

                    if(RoomList.getInstance().getListRooms().size()>0){
                        RoomList.getInstance().setUpdate(true);
                        RoomList.getInstance().getRoomPage(RoomList.getInstance().getCurrentPage());
                    }
                    RoomList.getInstance().setFirstPageRooms(firstPage);

                    }
                catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }
}
