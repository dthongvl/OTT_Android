package vizion.com.ott.Activities;

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

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import vizion.com.ott.Entities.IActivity;
import vizion.com.ott.Listeners.SignUpResultListener;
import vizion.com.ott.R;
import vizion.com.ott.Utils.Commands;
import vizion.com.ott.Utils.ImageCompression;
import vizion.com.ott.Utils.MyProgressDialog;
import vizion.com.ott.Utils.SocketHelper;

public class SignUpActivity extends AppCompatActivity implements IActivity {


    private static final int REQUEST_CODE = 1;
    private byte[] byteImage;

    private EditText txtEmail;
    private EditText txtPassword;
    private EditText txtNickname;
    private ImageView imgAvatar;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        this.mapViewIDs();
        this.addEventListeners();
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
        MyProgressDialog.getInstance(this, getString(R.string.wait_sign_up)).showProgressDialog();
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
                    Bitmap bmp = ImageCompression.getInstance().getBitmap(this, selectedImageUri);

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bmp.compress(Bitmap.CompressFormat.PNG, 50, stream);
                    byteImage = stream.toByteArray();
                    stream.close();
                    //byteImage = getBytes(getContentResolver().openInputStream(selectedImageUri));
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
        SocketHelper.getInstance().addListener(Commands.CLIENT_SIGN_UP_RS, SignUpResultListener.getInstance(SignUpActivity.this));

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
