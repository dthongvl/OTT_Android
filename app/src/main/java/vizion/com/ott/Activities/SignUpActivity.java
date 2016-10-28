package vizion.com.ott.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;

import vizion.com.ott.R;

public class SignUpActivity extends AppCompatActivity {

    private EditText txtEmail;
    private EditText txtPassword;
    private EditText txtNickname;
    
    private Button btnSignUp;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();

        addControls();
        addEvents();
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
    }

    private void addEvents() {
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValid()) {
                    signUp();
                }
            }
        });
    }

    private void addControls() {
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        txtNickname = (EditText) findViewById(R.id.txtNickname);
        btnSignUp = (Button) findViewById(R.id.btnSignUp);
    }
}
