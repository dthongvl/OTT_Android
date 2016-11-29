package vizion.com.ott.Listeners;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;

import org.json.JSONException;
import org.json.JSONObject;

import vizion.com.ott.Activities.PlayActivity;

/**
 * Created by dthongvl on 11/25/16.
 */
public class StartPlayingListener implements Emitter.Listener{
    private Activity activity;
    private static StartPlayingListener ourInstance = new StartPlayingListener();

    public static StartPlayingListener getInstance(Activity activity) {
        ourInstance.activity = activity;
        return ourInstance;
    }

    private StartPlayingListener() {
    }

    @Override
    public void call(final Object... args) {
        ourInstance.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                JSONObject data = (JSONObject) args[0];
                boolean isSuccess;
                try {
                    isSuccess = data.getBoolean("isSuccess");
                    if (isSuccess) {
                        Intent intent = new Intent(ourInstance.activity, PlayActivity.class);
                        intent.putExtra("match_id", data.getString("match_id"));
                        ourInstance.activity.startActivity(intent);
                        ourInstance.activity.finish();
                    } else {
                        Toast.makeText(ourInstance.activity, data.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
