package vizion.com.ott.Listeners;

import android.app.Activity;
import android.hardware.camera2.CameraManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.nkzawa.emitter.Emitter;

import org.json.JSONException;
import org.json.JSONObject;

import vizion.com.ott.Models.MyRoom;
import vizion.com.ott.R;

/**
 * Created by Razor on 21/11/2016.
 */
public class PlayerLeaveRoomListener implements Emitter.Listener{

    private Activity activity;

    private static PlayerLeaveRoomListener ourInstance = new PlayerLeaveRoomListener();

    public static PlayerLeaveRoomListener getInstance(Activity activity) {
        ourInstance.activity=activity;
        return ourInstance;
    }

    private PlayerLeaveRoomListener() {
    }

    @Override
    public void call(final Object... args) {
        ourInstance.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                JSONObject data = (JSONObject) args[0];
                Log.d("player",data.toString());
                try {

                TextView txtGuestName = (TextView) ourInstance.activity.findViewById(R.id.txtGuestName);
                TextView txtGuestCoin = (TextView) ourInstance.activity.findViewById(R.id.txtGuestCoin);
                ImageView imgGuestAvatar = (ImageView) ourInstance.activity.findViewById(R.id.imgGuestAvatar);
                ImageButton btnStart = (ImageButton) ourInstance.activity.findViewById(R.id.btnStart);
                ImageButton btnReady = (ImageButton) ourInstance.activity.findViewById(R.id.btnReady);
                TextView txtReady = (TextView) ourInstance.activity.findViewById(R.id.txtReady);
                TextView txtWaiting= (TextView) ourInstance.activity.findViewById(R.id.txtWaiting);
                ImageView imgGuestCoin = (ImageView) ourInstance.activity.findViewById(R.id.imgGuestCoin);


                txtWaiting.setVisibility(View.VISIBLE);
                txtGuestName.setVisibility(View.INVISIBLE);
                txtGuestCoin.setVisibility(View.INVISIBLE);
                txtReady.setVisibility(View.INVISIBLE);
                imgGuestAvatar.setVisibility(View.INVISIBLE);
                imgGuestCoin.setVisibility(View.INVISIBLE);

                if(MyRoom.getInstance().isHost()){
                    MyRoom.getInstance().setGuestUid(null);
                    MyRoom.getInstance().setGuestReady(false);
                }
                else{
                    MyRoom.getInstance().setHost(true);
                    MyRoom.getInstance().setHostUid(data.getJSONObject("host").getString("uid"));
                    MyRoom.getInstance().setHostReady(data.getJSONObject("host").getBoolean("ready"));
                    MyRoom.getInstance().setGuestUid(null);
                    MyRoom.getInstance().setGuestReady(false);
                    btnReady.setVisibility(View.INVISIBLE);
                    btnReady.setEnabled(false);
                    btnStart.setEnabled(true);
                    btnStart.setVisibility(View.VISIBLE);

                }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }
}
