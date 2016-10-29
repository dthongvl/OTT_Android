package vizion.com.ott.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;

import org.json.JSONException;
import org.json.JSONObject;

import vizion.com.ott.Entities.IActivity;
import vizion.com.ott.R;
import vizion.com.ott.Utils.Commands;
import vizion.com.ott.Utils.SocketHelper;

public class MainActivity extends AppCompatActivity implements IActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.mapViewIDs();
        this.addEventListeners();
        
        SocketHelper.getInstance().connect();


        JSONObject reqObject = new JSONObject();
        try {
            reqObject.put("email", "d@gmail.com");
            reqObject.put("pass", "123456");
            reqObject.put("displayName", "D's Name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        SocketHelper.getInstance().sendRequest(Commands.CLIENT_SIGN_UP, reqObject);
    }

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    boolean isSuccess;
                    try {
                        isSuccess = data.getBoolean("isSuccess");
                        if (isSuccess)
                            Toast.makeText(MainActivity.this, data.getString("uid"), Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(MainActivity.this, data.getString("message"), Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        return;
                    }

                }
            });
        }
    };

    @Override
    public void mapViewIDs() {

    }

    @Override
    public void addEventListeners() {
        SocketHelper.getInstance().addListener(Commands.CLIENT_SIGN_UP_RS, onNewMessage);
    }
}
