package vizion.com.ott.Listeners;

import android.app.Activity;
import android.widget.TextView;

import com.github.nkzawa.emitter.Emitter;

import org.json.JSONException;
import org.json.JSONObject;

import vizion.com.ott.Models.MyRoom;
import vizion.com.ott.R;
import vizion.com.ott.Utils.MyProgressDialog;

/**
 * Created by Razor on 19/11/2016.
 */
public class UpdateRoomInfoListener implements Emitter.Listener {
    Activity activity;
    private static UpdateRoomInfoListener ourInstance = new UpdateRoomInfoListener();

    public static UpdateRoomInfoListener getInstance(Activity activity) {
        ourInstance.activity=activity;
        return ourInstance;
    }

    private UpdateRoomInfoListener() {
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
                        final TextView txtRoomName = (TextView) ourInstance.activity.findViewById(R.id.txtRoomName);
                        final TextView txtCoinBet = (TextView) ourInstance.activity.findViewById(R.id.txtCoinBet);
                        final TextView txtType = (TextView) ourInstance.activity.findViewById(R.id.txtType);

                        txtRoomName.setText(data.getJSONObject("room_info").getString("room_name"));
                        txtCoinBet.setText(String.valueOf(data.getJSONObject("room_info").getDouble("money_bet")));
                        txtType.setText(String.valueOf(data.getJSONObject("room_info").getDouble("best_of")));

                        MyRoom.getInstance().setRoomName(data.getJSONObject("room_info").getString("room_name"));
                        MyRoom.getInstance().setMoneyBet(data.getJSONObject("room_info").getDouble("money_bet"));
                       // MyRoom.getInstance().setBestOf(data.getJSONObject("room_info").getDouble("best_of"));

                        if(MyRoom.getInstance().isHost())
                            MyProgressDialog.getInstance(ourInstance.activity,"").hideProgressDialog();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }
}
