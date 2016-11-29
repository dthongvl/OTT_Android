package vizion.com.ott.Listeners;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.nkzawa.emitter.Emitter;

import org.json.JSONException;
import org.json.JSONObject;

import vizion.com.ott.Models.MyUser;
import vizion.com.ott.R;

/**
 * Created by dthongvl on 11/25/16.
 */
public class MatchResultListener implements Emitter.Listener {
    private Activity activity;
    private static MatchResultListener ourInstance = new MatchResultListener();

    public static MatchResultListener getInstance(Activity activity) {
        ourInstance.activity = activity;
        return ourInstance;
    }

    private MatchResultListener() {
    }

    @Override
    public void call(final Object... args) {
        ourInstance.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject data = (JSONObject) args[0];
                    final Dialog dialog = new Dialog(ourInstance.activity);
                    dialog.setContentView(R.layout.result_dialog);
                    TextView txtResult = (TextView) dialog.findViewById(R.id.txtResult);
                    Button btnDone = (Button) dialog.findViewById(R.id.btnDone);

                    btnDone.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });

                    if (data.getBoolean("win")) {
                        txtResult.setText(ourInstance.activity.getString(R.string.you_win));
                    } else {
                        txtResult.setText(ourInstance.activity.getString(R.string.you_lose));
                    }
                    MyUser.getInstance().setCoinCard(data.getDouble("rest_coin_card"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
