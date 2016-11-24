package vizion.com.ott.Listeners;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;

import org.json.JSONException;
import org.json.JSONObject;

import vizion.com.ott.Activities.WaitActivity;
import vizion.com.ott.Models.MyRoom;
import vizion.com.ott.Models.MyUser;
import vizion.com.ott.Utils.MyProgressDialog;

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
                        if(MyRoom.getInstance().isHost()){
                            MyRoom.getInstance().setGuestUid(data.getString("uid"));
                            MyRoom.getInstance().setGuestReady(false);
                            Intent intent = new Intent(ourInstance.activity, WaitActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            intent.putExtra("oponent_avatar",data.getString("avatar"));
                            intent.putExtra("oponent_coin",data.getString("coin_card"));
                            intent.putExtra("oponent_name",data.getString("name"));
                            ourInstance.activity.startActivity(intent);
                        }
                        else{
                            MyRoom.getInstance().setHostUid(data.getString("uid"));
                            MyRoom.getInstance().setHostReady(false);
                            MyRoom.getInstance().setGuestUid(MyUser.getInstance().getUid());
                            MyRoom.getInstance().setGuestReady(false);
                            Intent intent = new Intent(ourInstance.activity, WaitActivity.class);
                            intent.putExtra("oponent_avatar",data.getString("avatar"));
                            intent.putExtra("oponent_coin",data.getString("coin_card"));
                            intent.putExtra("oponent_name",data.getString("name"));
                            MyProgressDialog.getInstance(ourInstance.activity,"").hideProgressDialog();
                            ourInstance.activity.startActivity(intent);

                        }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
