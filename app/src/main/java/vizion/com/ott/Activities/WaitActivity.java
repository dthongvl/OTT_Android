package vizion.com.ott.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import vizion.com.ott.Entities.IActivity;
import vizion.com.ott.R;

public class WaitActivity extends AppCompatActivity implements IActivity {

    private TextView txtHostName;
    private TextView txtHostCoin;
    private ImageView imgHostAvatar;
    private TextView txtGuestName;
    private TextView txtGuestCoin;
    private ImageView imgGuestAvatar;
    private TextView txtRoomName;
    private TextView txtCoinBet;
    private TextView txtType;
    private ImageButton btnAction;
    private TextView txtReady;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait);

        this.mapViewIDs();
        this.addEventListeners();
    }

    @Override
    public void mapViewIDs() {
        txtHostName = (TextView) findViewById(R.id.txtHostName);
        txtHostCoin = (TextView) findViewById(R.id.txtHostCoin);
        imgHostAvatar = (ImageView) findViewById(R.id.imgHostAvatar);
        txtGuestName = (TextView) findViewById(R.id.txtGuestName);
        txtGuestCoin = (TextView) findViewById(R.id.txtGuestCoin);
        imgGuestAvatar = (ImageView) findViewById(R.id.imgGuestAvatar);
        txtRoomName = (TextView) findViewById(R.id.txtRoomName);
        txtCoinBet = (TextView) findViewById(R.id.txtCoinBet);
        txtType = (TextView) findViewById(R.id.txtType);
        btnAction = (ImageButton) findViewById(R.id.btnAction);
        txtReady = (TextView) findViewById(R.id.txtReady);
    }

    @Override
    public void addEventListeners() {
        btnAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
