package vizion.com.ott.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.SocketHandler;

import vizion.com.ott.Entities.IActivity;
import vizion.com.ott.Models.MyUser;
import vizion.com.ott.R;
import vizion.com.ott.Utils.Commands;
import vizion.com.ott.Utils.SocketHelper;

public class ProfileActivity extends AppCompatActivity implements IActivity {

    private EditText txtEmail;
    private EditText txtNickname;
    private EditText txtPassword;
    private EditText txtNewpassword;
    private ImageView imgAvatar;
    private Button btnChange;


    private static final int REQUEST_CODE = 1;
    private byte[] byteImage;


    private boolean isEditMode = false;
    private ProgressDialog progressDialog;

    private JSONObject newUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        this.mapViewIDs();
        this.addEventListeners();
        this.getUserInfo();

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
    private void getUserInfo() {


        txtEmail.setText(MyUser.getInstance().getEmail());
        txtNickname.setText(MyUser.getInstance().getName());



        imgAvatar.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                imgAvatar.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                Picasso.with(ProfileActivity.this).load(MyUser.getInstance().getAvatar())
                        .memoryPolicy(MemoryPolicy.NO_CACHE )
                        .networkPolicy(NetworkPolicy.NO_CACHE).resize(imgAvatar.getWidth(), imgAvatar.getHeight()).centerCrop().into(imgAvatar);

            }
        });


    }

    private void getAvatar() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, REQUEST_CODE);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE) {
                Uri selectedImageUri = data.getData();
                Picasso.with(ProfileActivity.this).load(selectedImageUri).resize(imgAvatar.getWidth(), imgAvatar.getHeight()).centerCrop().into(imgAvatar);
                try {
                    byteImage = getBytes(getContentResolver().openInputStream(selectedImageUri));


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
            try {
                byteBuffer.close();
            } catch (IOException ignored) { /* do nothing */ }
        }
        return bytesResult;
    }
    private void updateProfile() {
        showProgressDialog();
        String email = txtEmail.getText().toString();
        String password = txtPassword.getText().toString();
        String newPassword = txtNewpassword.getText().toString();
        String nickname = txtNickname.getText().toString();
        JSONObject reqObject = new JSONObject();
        try {
            reqObject.put("email", email);
            reqObject.put("oldPass", password);
            reqObject.put("displayName",nickname);
            reqObject.put("newPass",newPassword);
            reqObject.put("file", byteImage);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        SocketHelper.getInstance().sendRequest(Commands.CLIENT_UPDATE_PROFILE, reqObject);
    }

    private Emitter.Listener onUpdateResult = new Emitter.Listener() {
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

                            MyUser.getInstance().setEmail(txtEmail.getText().toString());
                            MyUser.getInstance().setName(txtNickname.getText().toString());
                            MyUser.getInstance().setAvatar(data.getString("newAvatarUrl"));
                            Log.d("avata",MyUser.getInstance().getAvatar());
                        } else {
                            Toast.makeText(getBaseContext(),"errr",Toast.LENGTH_LONG).show();
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

    @Override
    public void addEventListeners() {

        SocketHelper.getInstance().addListener(Commands.CLIENT_UPDATE_PROFILE_RS,onUpdateResult);

        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isEditMode) {
                    btnChange.setBackgroundDrawable(getResources().getDrawable(R.drawable.button12));
                    txtEmail.setEnabled(false);
                    txtNickname.setEnabled(false);
                    txtPassword.setVisibility(View.GONE);
                    txtNewpassword.setVisibility(View.GONE);
                    imgAvatar.setEnabled(false);
                    isEditMode = false;
                    updateProfile();
                } else {
                    btnChange.setBackgroundDrawable(getResources().getDrawable(R.drawable.button7));
                    txtEmail.setEnabled(true);
                    txtNickname.setEnabled(true);
                    txtPassword.setEnabled(true);
                    txtNewpassword.setEnabled(true);
                    txtPassword.setVisibility(View.VISIBLE);
                    txtNewpassword.setVisibility(View.VISIBLE);
                    imgAvatar.setEnabled(true);
                    isEditMode = true;

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
        txtNickname = (EditText) findViewById(R.id.txtNickname);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        txtNewpassword = (EditText) findViewById(R.id.txtNewpassword);
        btnChange = (Button) findViewById(R.id.btnChange);
        imgAvatar = (ImageView) findViewById(R.id.imgAvatar);
        imgAvatar.setEnabled(false);
    }




}
