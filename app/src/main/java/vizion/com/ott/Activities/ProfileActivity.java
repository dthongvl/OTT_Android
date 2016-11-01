package vizion.com.ott.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import vizion.com.ott.R;

public class ProfileActivity extends AppCompatActivity {

    private EditText txtEmail;
    private EditText txtNickname;
    private EditText txtPassword;
    private EditText txtNewpassword;
    private Button btnChange;

    private boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        addControls();
        addEvents();
    }

    private void addEvents() {
        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isEditMode) {
                    btnChange.setBackgroundDrawable(getResources().getDrawable(R.drawable.button12));
                    txtEmail.setEnabled(false);
                    txtNickname.setEnabled(false);
                    txtPassword.setEnabled(false);
                    txtNewpassword.setEnabled(false);
                    isEditMode = false;
                } else {
                    btnChange.setBackgroundDrawable(getResources().getDrawable(R.drawable.button7));
                    txtEmail.setEnabled(true);
                    txtNickname.setEnabled(true);
                    txtPassword.setEnabled(true);
                    txtNewpassword.setEnabled(true);
                    isEditMode = true;
                }
            }
        });
    }

    private void addControls() {
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtNickname = (EditText) findViewById(R.id.txtNickname);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        txtNewpassword = (EditText) findViewById(R.id.txtNewpassword);
        btnChange = (Button) findViewById(R.id.btnChange);
    }
}
