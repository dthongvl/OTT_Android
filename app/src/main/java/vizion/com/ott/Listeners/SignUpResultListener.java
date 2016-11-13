package vizion.com.ott.Listeners;

import android.app.Activity;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;

import org.json.JSONException;
import org.json.JSONObject;

import vizion.com.ott.Activities.MenuActivity;
import vizion.com.ott.R;
import vizion.com.ott.Utils.MyProgressDialog;

/**
 * Created by dthongvl on 11/13/16.
 */
public class SignUpResultListener implements Emitter.Listener {
    private Activity activity;
    private static SignUpResultListener ourInstance = new SignUpResultListener();

    public static SignUpResultListener getInstance(Activity activity) {
        ourInstance.activity = activity;
        return ourInstance;
    }

    private SignUpResultListener() {
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
                    TextView txtEmail = (TextView) ourInstance.activity.findViewById(R.id.txtEmail);
                    if (isSuccess) {
                        MyProgressDialog.getInstance(ourInstance.activity, "").hideProgressDialog();
                        Intent intent = new Intent(ourInstance.activity, MenuActivity.class);
                        Toast.makeText(ourInstance.activity, data.getString("uid"), Toast.LENGTH_LONG).show();
                        ourInstance.activity.startActivity(intent);
                        ourInstance.activity.finish();
                    } else {
                        //Toast.makeText(MainActivity.this, data.getString("message"), Toast.LENGTH_SHORT).show();
                        txtEmail.setError("Email hoặc mật khẩu không đúng");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                finally {
                    MyProgressDialog.getInstance(ourInstance.activity, "").hideProgressDialog();
                }
            }
        });
    }
}
