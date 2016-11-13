package vizion.com.ott.Listeners;

import android.app.Activity;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by dthongvl on 11/13/16.
 */
public class JoinRoomListener implements Emitter.Listener {
    private Activity activity;
    private static JoinRoomListener ourInstance = new JoinRoomListener();

    public static JoinRoomListener getInstance(Activity activity)
    {
        ourInstance.activity = activity;
        return ourInstance;
    }

    private JoinRoomListener() {
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
                        Toast.makeText(ourInstance.activity,"ok",Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(ourInstance.activity,"failed",Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
