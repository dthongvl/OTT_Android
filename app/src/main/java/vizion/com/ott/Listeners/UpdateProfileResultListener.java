package vizion.com.ott.Listeners;

import android.app.Activity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;

import org.json.JSONException;
import org.json.JSONObject;

import vizion.com.ott.Models.MyUser;
import vizion.com.ott.R;
import vizion.com.ott.Utils.MyProgressDialog;

/**
 * Created by dthongvl on 11/13/16.
 */
public class UpdateProfileResultListener implements Emitter.Listener{
    private Activity activity;
    private static UpdateProfileResultListener ourInstance = new UpdateProfileResultListener();

    public static UpdateProfileResultListener getInstance(Activity activity) {
        ourInstance.activity = activity;
        return ourInstance;
    }

    private UpdateProfileResultListener() {
    }

    @Override
    public void call(final Object... args) {
        ourInstance.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                JSONObject data = (JSONObject) args[0];
                boolean isSuccess;
                try {
                    Log.d("DEBUG", data.toString());
                    isSuccess = data.getBoolean("isSuccess");
                    TextView txtEmail = (TextView) ourInstance.activity.findViewById(R.id.txtEmail);
                    TextView txtNickname = (TextView) ourInstance.activity.findViewById(R.id.txtNickname);
                    if (isSuccess) {
                        txtEmail.setError(null);
                        MyUser.getInstance().setEmail(txtEmail.getText().toString());
                        MyUser.getInstance().setName(txtNickname.getText().toString());
                        MyUser.getInstance().setAvatar(data.getString("newAvatarUrl"));
                        Log.d("avata",MyUser.getInstance().getAvatar());
                    } else {
                        txtEmail.setError("Email hoặc mật khẩu không đúng");
                        Toast.makeText(ourInstance.activity, "errr",Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    return;
                }
                finally {
                    MyProgressDialog.getInstance(ourInstance.activity, "").hideProgressDialog();

                }
            }
        });
    }
}
