package vizion.com.ott.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;

import org.json.JSONException;
import org.json.JSONObject;

import vizion.com.ott.Entities.IActivity;
import vizion.com.ott.Models.User;
import vizion.com.ott.R;
import vizion.com.ott.Utils.Commands;
import vizion.com.ott.Utils.SocketHelper;

public class MainActivity extends AppCompatActivity implements IActivity {

    private EditText txtEmail;
    private EditText txtPassword;
    private Button btnSignIn;
    private Button btnSignUp;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.mapViewIDs();
        this.addEventListeners();

        SocketHelper.getInstance().connect();
/*
        JSONObject reqObject = new JSONObject();
        try {
            reqObject.put("email", "d@gmail.com");
            reqObject.put("pass", "123456");
            reqObject.put("displayName", "D's Name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        SocketHelper.getInstance().sendRequest(Commands.CLIENT_SIGN_UP, reqObject);
        */
    }

    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage(getString(R.string.wait_login));
        }
        progressDialog.show();
    }

    private void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private void signIn() {
        showProgressDialog();
        String email = txtEmail.getText().toString();
        String password = txtPassword.getText().toString();
        JSONObject reqObject = new JSONObject();
        try {
            reqObject.put("email", email);
            reqObject.put("pass", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        SocketHelper.getInstance().sendRequest(Commands.CLIENT_SIGN_IN, reqObject);
    }

    private boolean isInputValid() {
        boolean result = true;
        if (TextUtils.isEmpty(txtEmail.getText())) {
            txtEmail.setError(getString(R.string.input_error));
            result = false;
        }
        if (TextUtils.isEmpty(txtPassword.toString())) {
            txtPassword.setError(getString(R.string.input_error));
            result = false;
        }
        return result;
    }

    private Emitter.Listener onSignInResult = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    try {
                        boolean isSuccess = data.getBoolean("isSuccess");
                        if (isSuccess) {
                            JSONObject userJSON = data.getJSONObject("user");
                            User.getInstance().setUid(userJSON.getString("uid"));
                            User.getInstance().setName(userJSON.getString("name"));
                            User.getInstance().setEmail(txtEmail.getText().toString());
                            User.getInstance().setCoinCard(userJSON.getDouble("coin_card"));
                            User.getInstance().setNotiToken(userJSON.getString("noti_token"));
                            User.getInstance().setAvatar(userJSON.getString("avatar"));
                            User.getInstance().setHistories(userJSON.getString("histories"));
                            User.getInstance().setSocketId(userJSON.getString("socket_id"));

                            hideProgressDialog();

                            Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            hideProgressDialog();
                            Toast.makeText(MainActivity.this, data.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };

   /*private Emitter.Listener onNewMessage = new Emitter.Listener() {
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
                            //Toast.makeText(MainActivity.this, data.getString("message"), Toast.LENGTH_LONG).show();
                            txtEmail.setError("Email hoặc mật khẩu không đúng");

                    } catch (JSONException e) {
                        return;
                    }

                }
            });
        }
    };*/

    @Override
    public void mapViewIDs() {
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        btnSignIn = (Button) findViewById(R.id.btnSignIn);
        btnSignUp = (Button) findViewById(R.id.btnSignUp);
    }

    @Override
    public void addEventListeners() {
        SocketHelper.getInstance().addListener(Commands.CLIENT_SIGN_IN_RS, onSignInResult);
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isInputValid()) {
                    signIn();
                }
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }
}
