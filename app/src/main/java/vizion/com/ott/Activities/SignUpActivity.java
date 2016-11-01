package vizion.com.ott.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import vizion.com.ott.Entities.IActivity;
import vizion.com.ott.R;

public class SignUpActivity extends AppCompatActivity implements IActivity {

    private EditText txtEmail;
    private EditText txtPassword;
    private EditText txtNickname;
    
    private Button btnSignUp;

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
        String email = txtEmail.getText().toString();
        String password = txtPassword.getText().toString();
        String nickname = txtNickname.getText().toString();
    }

    @Override
    public void addEventListeners() {
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValid()) {
                    signUp();
                }
            }
        });
    }

    @Override
    public void mapViewIDs() {
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        txtNickname = (EditText) findViewById(R.id.txtNickname);
        btnSignUp = (Button) findViewById(R.id.btnSignUp);
    }
}
