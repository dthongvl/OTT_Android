package vizion.com.ott.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import vizion.com.ott.Entities.IActivity;
import vizion.com.ott.Listeners.FirstRoomPageListener;
import vizion.com.ott.Listeners.SignInResultListener;
import vizion.com.ott.Models.MyUser;
import vizion.com.ott.R;
import vizion.com.ott.Utils.Commands;
import vizion.com.ott.Utils.MyProgressDialog;
import vizion.com.ott.Utils.SocketHelper;

public class MainActivity extends AppCompatActivity implements IActivity {

    public EditText txtEmail;
    private EditText txtPassword;
    private Button btnSignIn;
    private Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.mapViewIDs();
        this.addEventListeners();

        SocketHelper.getInstance().connect();

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        txtEmail.setText(sharedPref.getString("email", ""));
    }



    private void signIn() {
        MyProgressDialog.getInstance(this, getString(R.string.wait_login)).showProgressDialog();
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

    @Override
    public void mapViewIDs() {
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        btnSignIn = (Button) findViewById(R.id.btnSignIn);
        btnSignUp = (Button) findViewById(R.id.btnSignUp);
    }

    @Override
    public void addEventListeners() {
        SocketHelper.getInstance().addListener(Commands.CLIENT_RECEIVE_FIRST_ROOMS, FirstRoomPageListener.getInstance(MainActivity.this));
        SocketHelper.getInstance().addListener(Commands.CLIENT_SIGN_IN_RS, SignInResultListener.getInstance(MainActivity.this));
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        JSONObject reqObject = new JSONObject();
        try {
            reqObject.put("uid", MyUser.getInstance().getUid());
            Log.d("signout", "onDestroy: ");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        SocketHelper.getInstance().sendRequest(Commands.CLIENT_SIGN_OUT, reqObject);
    }
}
