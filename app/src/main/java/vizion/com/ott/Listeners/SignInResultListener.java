package vizion.com.ott.Listeners;

import android.app.Activity;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;

import org.json.JSONException;
import org.json.JSONObject;

import vizion.com.ott.Activities.MenuActivity;
import vizion.com.ott.Models.MyRoom;
import vizion.com.ott.Models.MyUser;
import vizion.com.ott.R;
import vizion.com.ott.Utils.MyProgressDialog;

/**
 * Created by dthongvl on 11/13/16.
 */
public class SignInResultListener implements Emitter.Listener{
    private Activity activity;
    private static SignInResultListener ourInstance = new SignInResultListener();

    public static SignInResultListener getInstance(Activity activity) {
        ourInstance.activity = activity;
        return ourInstance;
    }

    private SignInResultListener() {
    }

    @Override
    public void call(final Object... args) {
        ourInstance.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                JSONObject data = (JSONObject) args[0];

                try {

                    boolean isSuccess = data.getBoolean("isSuccess");
                    if (isSuccess) {

                        JSONObject userJSON = data.getJSONObject("user");
                        MyUser.getInstance().setUid(userJSON.getString("uid"));
                        MyUser.getInstance().setName(userJSON.getString("name"));
                        TextView txtEmail = (TextView) ourInstance.activity.findViewById(R.id.txtEmail);
                        MyUser.getInstance().setEmail(txtEmail.getText().toString());
                        MyUser.getInstance().setCoinCard(userJSON.getDouble("coin_card"));
                        MyUser.getInstance().setNotiToken(userJSON.getString("noti_token"));
                        MyUser.getInstance().setAvatar(userJSON.getString("avatar"));
                        MyUser.getInstance().setHistories(userJSON.getString("histories"));
                        MyUser.getInstance().setSocketId(userJSON.getString("socket_id"));
                        MyUser.getInstance().setWins(userJSON.getJSONObject("statistics").getDouble("wins"));
                        MyUser.getInstance().setLoses(userJSON.getJSONObject("statistics").getDouble("loses"));
                        MyProgressDialog.getInstance(ourInstance.activity, "").hideProgressDialog();

                        Intent intent = new Intent(ourInstance.activity, MenuActivity.class);
                        intent.putExtra("firstRoomPage", MyRoom.getInstance().getRoomData());
                        ourInstance.activity.startActivity(intent);
                        ourInstance.activity.finish();
                    } else {
                        MyProgressDialog.getInstance(ourInstance.activity, "").hideProgressDialog();
                        Toast.makeText(ourInstance.activity, data.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
