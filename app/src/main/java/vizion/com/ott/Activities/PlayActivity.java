package vizion.com.ott.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import vizion.com.ott.Entities.IActivity;
import vizion.com.ott.R;

public class PlayActivity extends AppCompatActivity implements IActivity {

    private TextView txtEnemyName;
    private TextView txtEnemyReady;
    private ImageView imgEnemyChoice;

    private TextView txtTime;
    private TextView txtWin;
    private TextView txtUserWin;
    private TextView txtBo;
    private TextView txtEnemyWin;

    private ImageButton btnReady;
    private ImageButton btnSubmit;
    private ImageView imgUserChoice;
    private ImageButton btnHammer;
    private ImageButton btnBag;
    private ImageButton btnScissor;
    private TextView txtUserReady;
    private TextView txtUserName;
    private ImageView imgUserAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        this.mapViewIDs();
        this.addEventListeners();
    }

    @Override
    public void mapViewIDs() {
        txtEnemyName = (TextView) findViewById(R.id.txtEnemyName);
        txtEnemyReady = (TextView) findViewById(R.id.txtEnemyReady);
        imgEnemyChoice = (ImageView) findViewById(R.id.imgEnemyChoice);

        txtTime = (TextView) findViewById(R.id.txtTime);
        txtWin = (TextView) findViewById(R.id.txtWin);
        txtUserWin = (TextView) findViewById(R.id.txtUserWin);
        txtBo = (TextView) findViewById(R.id.txtBo);
        txtEnemyWin = (TextView) findViewById(R.id.txtEnemyWin);

        btnReady = (ImageButton) findViewById(R.id.btnReady);
        btnSubmit = (ImageButton) findViewById(R.id.btnSubmit);
        imgUserChoice = (ImageView) findViewById(R.id.imgUserChoice);
        btnHammer = (ImageButton) findViewById(R.id.btnHammer);
        btnBag = (ImageButton) findViewById(R.id.btnBag);
        btnScissor = (ImageButton) findViewById(R.id.btnScissor);
        imgUserAvatar = (ImageView) findViewById(R.id.imgUserAvatar);
        txtUserReady = (TextView) findViewById(R.id.txtUserReady);
        txtUserName = (TextView) findViewById(R.id.txtUserName);
    }

    @Override
    public void addEventListeners() {

    }
}
