package vizion.com.ott.Listeners;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.TextView;

import com.github.nkzawa.emitter.Emitter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

import vizion.com.ott.Models.MyRoom;
import vizion.com.ott.Models.MyUser;
import vizion.com.ott.R;
import vizion.com.ott.Utils.Commands;
import vizion.com.ott.Utils.SocketHelper;


public class PlayClientReadyListener implements Emitter.Listener {
    private Activity activity;
    private static PlayClientReadyListener ourInstance = new PlayClientReadyListener();

    public static PlayClientReadyListener getInstance(Activity activity) {
        ourInstance.activity = activity;
        return ourInstance;
    }

    private PlayClientReadyListener() {
    }


    @Override
    public void call(final Object... args) {
        ourInstance.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject data = (JSONObject) args[0];
                    TextView txtEnemyReady = (TextView) ourInstance.activity.findViewById(R.id.txtEnemyReady);
                    if (data.getBoolean("their")) {
                        txtEnemyReady.setText(ourInstance.activity.getString(R.string.ready));
                    } else {
                        txtEnemyReady.setText(ourInstance.activity.getString(R.string.unready));
                    }
                    if (!data.getBoolean("their") || !data.getBoolean("self")) return;
                    MyRoom.getInstance().setCouting(true);
                    TimerTask timerTask = new TimerTask((TextView) ourInstance.activity.findViewById(R.id.txtTime));
                    timerTask.execute();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private class TimerTask extends AsyncTask<Void, Integer, Void>
    {
        TextView txtTime;

        public TimerTask(TextView txtTime) {
            this.txtTime = txtTime;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                for (Integer i = 15; i > 0; --i) {
                    if (!MyRoom.getInstance().isCouting()) break;
                    publishProgress(i);
                    Thread.sleep(1000);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(final Integer... values) {
            super.onProgressUpdate(values);
            txtTime.setText("Time: " + values[0].toString());
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (MyRoom.getInstance().isCouting()) {
                Random rnd = new Random();
                int selection = rnd.nextInt(3) + 1;
                JSONObject reqObject = new JSONObject();
                try {
                    reqObject.put("match_id", MyRoom.getInstance().getMatchId());
                    reqObject.put("game_id", MyRoom.getInstance().getGameId() + 1);
                    reqObject.put("uid", MyUser.getInstance().getUid());
                    reqObject.put("selection", selection);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                SocketHelper.getInstance().sendRequest(Commands.CLIENT_SUBMIT_SELECTION, reqObject);
                MyRoom.getInstance().setCouting(false);
            }
        }
    }
}
