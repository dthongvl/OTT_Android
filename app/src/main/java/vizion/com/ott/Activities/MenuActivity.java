package vizion.com.ott.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import org.json.JSONException;
import org.json.JSONObject;

import vizion.com.ott.Entities.IActivity;
import vizion.com.ott.Models.MyUser;
import vizion.com.ott.Models.RoomList;
import vizion.com.ott.R;
import vizion.com.ott.Utils.Commands;
import vizion.com.ott.Utils.SocketHelper;

public class MenuActivity extends AppCompatActivity implements IActivity {

    private Button btnSignOut;
    private Button btnPlay;
    private Button btnProfile;
    private Button btnAbout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        this.mapViewIDs();
        this.addEventListeners();


    }

    private void signOut() {
        JSONObject reqObject = new JSONObject();
        try {
            reqObject.put("uid", MyUser.getInstance().getUid());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RoomList.getInstance().clearListRooms();
        SocketHelper.getInstance().sendRequest(Commands.CLIENT_SIGN_OUT, reqObject);
        finish();
    }

    private void roomsActivity() {
        Intent intent = new Intent(MenuActivity.this, RoomsActivity.class);
        startActivity(intent);
    }

    private void aboutActivity() {
        Intent intent = new Intent(MenuActivity.this, AboutActivity.class);
        startActivity(intent);
    }

    private void profileActivity() {
        Intent intent = new Intent(MenuActivity.this, ProfileActivity.class);
        startActivity(intent);
    }

    @Override
    public void addEventListeners() {
        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                roomsActivity();
            }
        });

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profileActivity();
            }
        });

        btnAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                aboutActivity();
            }
        });
    }

    @Override
    public void mapViewIDs() {
        btnSignOut = (Button) findViewById(R.id.btnSignOut);
        btnPlay = (Button) findViewById(R.id.btnPlay);
        btnProfile = (Button) findViewById(R.id.btnProfile);
        btnAbout = (Button) findViewById(R.id.btnAbout);
    }
}
