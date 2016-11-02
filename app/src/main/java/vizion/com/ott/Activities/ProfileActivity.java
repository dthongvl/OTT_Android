package vizion.com.ott.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import vizion.com.ott.Entities.IActivity;
import vizion.com.ott.R;

public class ProfileActivity extends AppCompatActivity implements IActivity {

    private EditText txtEmail;
    private EditText txtNickname;
    private EditText txtPassword;
    private EditText txtNewpassword;
    private ImageView imgAvatar;
    private Button btnChange;






    private boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        this.mapViewIDs();
        this.addEventListeners();
        this.getUserInfo();
    }

    private void getUserInfo() {
        //Toast.makeText(ProfileActivity.this,MenuActivity.email,Toast.LENGTH_LONG).show();
        txtEmail.setText(MenuActivity.email);

        try {
            txtNickname.setText(MenuActivity.user.getString("name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

//Chưa tìm được solution, imgAvatar.getWidth/Height trả về 0-> lỗi
        try {
            Picasso.with(getBaseContext()).load(MenuActivity.user.getString("avatar"))
                    .resize(156,156).centerCrop().into(imgAvatar);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void addEventListeners() {
        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isEditMode) {
                    btnChange.setBackgroundDrawable(getResources().getDrawable(R.drawable.button12));
                    //txtEmail.setEnabled(false);
                    txtNickname.setEnabled(false);
                    txtPassword.setVisibility(View.GONE);
                    txtNewpassword.setVisibility(View.GONE);
                    isEditMode = false;
                } else {
                    btnChange.setBackgroundDrawable(getResources().getDrawable(R.drawable.button7));
                    //txtEmail.setEnabled(true);
                    txtNickname.setEnabled(true);
                    txtPassword.setEnabled(true);
                    txtNewpassword.setEnabled(true);
                    txtPassword.setVisibility(View.VISIBLE);
                    txtNewpassword.setVisibility(View.VISIBLE);
                    isEditMode = true;
                }
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
        imgAvatar= (ImageView) findViewById(R.id.imgAvatar);
    }
}
