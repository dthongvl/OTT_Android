package vizion.com.ott.Listeners;

import android.app.Activity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;

import org.json.JSONException;
import org.json.JSONObject;

import vizion.com.ott.Models.MyRoom;
import vizion.com.ott.R;

public class SubmitSelectionListener implements Emitter.Listener {
    private Activity activity;
    private static SubmitSelectionListener ourInstance = new SubmitSelectionListener();

    public static SubmitSelectionListener getInstance(Activity activity) {
        ourInstance.activity = activity;
        return ourInstance;
    }

    private SubmitSelectionListener() {
    }

    @Override
    public void call(final Object... args) {
        ourInstance.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject data = (JSONObject) args[0];

                    ImageView imgEnemyChoice = (ImageView) ourInstance.activity.findViewById(R.id.imgEnemyChoice);
                    switch (data.getInt("their")) {
                        case 1:
                            imgEnemyChoice.setImageResource(R.drawable.hammer);
                            break;
                        case 2:
                            imgEnemyChoice.setImageResource(R.drawable.bag);
                            break;
                        case 3:
                            imgEnemyChoice.setImageResource(R.drawable.scissor);
                            break;
                    }

                    if (data.getBoolean("win")) {
                        TextView txtUserWin = (TextView) ourInstance.activity.findViewById(R.id.txtUserWin);
                        txtUserWin.setText(String.valueOf(Integer.parseInt(txtUserWin.getText().toString()) + 1));
                        Toast.makeText(ourInstance.activity, ourInstance.activity.getString(R.string.you_win), Toast.LENGTH_SHORT).show();
                    } else {
                        TextView txtEnemyWin = (TextView) ourInstance.activity.findViewById(R.id.txtEnemyWin);
                        txtEnemyWin.setText(String.valueOf(Integer.parseInt(txtEnemyWin.getText().toString()) + 1));
                        Toast.makeText(ourInstance.activity, ourInstance.activity.getString(R.string.you_lose), Toast.LENGTH_SHORT).show();
                    }

                    MyRoom.getInstance().setGameId(MyRoom.getInstance().getGameId() + 1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
