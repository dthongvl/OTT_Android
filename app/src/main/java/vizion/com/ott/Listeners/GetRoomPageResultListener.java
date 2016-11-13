package vizion.com.ott.Listeners;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import vizion.com.ott.Activities.RoomsActivity;

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
                    if(isUpdate)
                        updateRoom(arrRooms);
                    else
                        addNewRoom(arrRooms);

                    adapterRooms.notifyDataSetChanged();
                    Log.d("abc", data.toString());
                    Toast.makeText(ourInstance.activity,"ok",Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }
}
