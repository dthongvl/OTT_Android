package vizion.com.ott.Listeners;

import android.app.Activity;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;

import com.github.nkzawa.emitter.Emitter;

import org.json.JSONException;
import org.json.JSONObject;

import vizion.com.ott.Models.MyRoom;
import vizion.com.ott.R;

/**
 * Created by Razor on 24/11/2016.
 */
public class ClientReadyListener implements Emitter.Listener {
    Activity activity;
    private static ClientReadyListener ourInstance = new ClientReadyListener();

    public static ClientReadyListener getInstance(Activity activity) {
        ourInstance.activity=activity;
        return ourInstance;
    }

    private ClientReadyListener() {
    }

    @Override
    public void call(final Object... args) {
        ourInstance.activity.runOnUiThread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void run() {
                JSONObject data = (JSONObject) args[0];
                try {
                    Log.d("DEBUG", data.toString());
                    boolean guestReady = data.getBoolean("ready");
                    if(guestReady){
                        MyRoom.getInstance().setGuestReady(true);
                        if(MyRoom.getInstance().isHost()){
                            TextView txtReady = (TextView) ourInstance.activity.findViewById(R.id.txtReady);
                            ImageButton btnStart = (ImageButton) ourInstance.activity.findViewById(R.id.btnStart);

                            txtReady.setText("Ready");
                            btnStart.setEnabled(true);

                        }
                        else{
                            ImageButton btnReady = (ImageButton) ourInstance.activity.findViewById(R.id.btnReady);
                            btnReady.setBackgroundResource(R.drawable.unready);
                        }
                    }
                    else{
                        MyRoom.getInstance().setGuestReady(false);
                        if(MyRoom.getInstance().isHost()){
                            TextView txtReady = (TextView) ourInstance.activity.findViewById(R.id.txtReady);
                            ImageButton btnStart = (ImageButton) ourInstance.activity.findViewById(R.id.btnStart);

                            txtReady.setText("Unready");
                            btnStart.setEnabled(false);

                        }
                        else{
                            ImageButton btnReady = (ImageButton) ourInstance.activity.findViewById(R.id.btnReady);
                            btnReady.setBackgroundResource(R.drawable.ready);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
