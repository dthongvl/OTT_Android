package vizion.com.ott.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import vizion.com.ott.Entities.IActivity;
import vizion.com.ott.R;
import vizion.com.ott.Utils.Commands;
import vizion.com.ott.Utils.SocketHelper;

public class SignUpActivity extends AppCompatActivity implements IActivity {


    private static final int REQUEST_CODE = 1;
    private byte[] byteImage;

    private EditText txtEmail;
    private EditText txtPassword;
    private EditText txtNickname;
    private ImageView imgAvatar;
    private Button btnRegister;

    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        this.mapViewIDs();
        this.addEventListeners();
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
    private boolean isValid() {
        boolean result = true;
        if (TextUtils.isEmpty(txtEmail.getText())) {
            txtEmail.setError(getString(R.string.input_error));
            result = false;
        }
        if (TextUtils.isEmpty(txtPassword.toString())) {
            txtPassword.setError(getString(R.string.input_error));
            result = false;
        }
        if (TextUtils.isEmpty(txtNickname.toString())) {
            txtNickname.setError(getString(R.string.input_error));
            result = false;
        }
        return result;
    }

    private void signUp() {
        showProgressDialog();
        String email = txtEmail.getText().toString();
        String password = txtPassword.getText().toString();
        String nickname = txtNickname.getText().toString();
        JSONObject reqObject = new JSONObject();
        try {
            reqObject.put("email", email);
            reqObject.put("pass", password);
            reqObject.put("displayName",nickname);
            reqObject.put("file", byteImage);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        SocketHelper.getInstance().sendRequest(Commands.CLIENT_SIGN_UP, reqObject);
    }


    private Emitter.Listener onSignUpResult = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    boolean isSuccess;
                    try {
                        isSuccess = data.getBoolean("isSuccess");
                        if (isSuccess) {
                            hideProgressDialog();
                            Intent intent = new Intent(SignUpActivity.this, MenuActivity.class);
                            Toast.makeText(SignUpActivity.this, data.getString("uid"), Toast.LENGTH_LONG).show();
                            startActivity(intent);
                            finish();
                        } else {
                            //Toast.makeText(MainActivity.this, data.getString("message"), Toast.LENGTH_SHORT).show();
                            txtEmail.setError("Email hoặc mật khẩu không đúng");
                        }
                    } catch (JSONException e) {
                        return;
                    }
                    finally {
                        hideProgressDialog();
                    }
                }
            });
        }
    };



    private void getAvatar(){

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE) {
                Uri selectedImageUri = data.getData();
                Picasso.with(this.getBaseContext()).load(selectedImageUri).resize(imgAvatar.getWidth(),imgAvatar.getHeight()).centerCrop().into(imgAvatar);
                try {
                    byteImage = getBytes(getContentResolver().openInputStream(selectedImageUri));
                    if(byteImage != null)
                        Toast.makeText(SignUpActivity.this,"ok",Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

//Convert URI to byte array
    private byte[] getBytes(InputStream inputStream) throws IOException {

        byte[] bytesResult = null;
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];
        try {
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                byteBuffer.write(buffer, 0, len);
            }
            bytesResult = byteBuffer.toByteArray();
        } finally {
            // close the stream
            try{ byteBuffer.close(); } catch (IOException ignored){ /* do nothing */ }
        }
        return bytesResult;
    }


    @Override
    public void addEventListeners() {
        SocketHelper.getInstance().addListener(Commands.CLIENT_SIGN_UP_RS,onSignUpResult);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValid()) {
                    signUp();
                }
            }
        });

        imgAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAvatar();
            }
        });
    }

    @Override
    public void mapViewIDs() {
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        txtNickname = (EditText) findViewById(R.id.txtNickname);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        imgAvatar = (ImageView) findViewById(R.id.imgAvatar);
    }
}
