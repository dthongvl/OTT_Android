package vizion.com.ott.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import vizion.com.ott.Entities.IActivity;
import vizion.com.ott.Models.MyUser;
import vizion.com.ott.Models.Room;
import vizion.com.ott.R;
import vizion.com.ott.Utils.Commands;
import vizion.com.ott.Utils.SocketHelper;

public class MenuActivity extends AppCompatActivity implements IActivity {

    private Button btnSignOut;
    private Button btnPlay;
    private Button btnProfile;
    private Button btnAbout;

    private ArrayList<Room> listRooms;
    private int totalPage;

    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        getFirstRoomPage();
        this.mapViewIDs();
        this.addEventListeners();


    }

    private void getFirstRoomPage() {
        Intent intent = getIntent();
        try {
            JSONObject roomdata = new JSONObject(intent.getStringExtra("firstRoomPage"));
            totalPage = roomdata.getInt("total_page");
            JSONArray arrRooms = roomdata.getJSONArray("rooms");
            listRooms = new ArrayList<>();
            for (int roomOrder = 0; roomOrder < arrRooms.length(); ++roomOrder) {
                listRooms.add(new Room(arrRooms.getJSONObject(roomOrder)));
            }

        }
            catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void signOut() {
        JSONObject reqObject = new JSONObject();
        try {
            reqObject.put("uid", MyUser.getInstance().getUid());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        SocketHelper.getInstance().sendRequest(Commands.CLIENT_SIGN_OUT, reqObject);
        Intent intent = new Intent(MenuActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void roomsActivity() {
        Intent intent = new Intent(MenuActivity.this, RoomsActivity.class);
        intent.putExtra("total_page", totalPage);
        intent.putParcelableArrayListExtra("list_rooms", listRooms);
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

    private Emitter.Listener onListRooms = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    try {
                        totalPage = data.getInt("total_page");
                        JSONArray arrRooms = data.getJSONArray("rooms");
                        listRooms.clear();
                        for (int roomOrder = 0; roomOrder < arrRooms.length(); ++roomOrder) {
                            listRooms.add(new Room(arrRooms.getJSONObject(roomOrder)));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };

    @Override
    public void addEventListeners() {
        SocketHelper.getInstance().addListener(Commands.CLIENT_RECEIVE_FIRST_ROOMS, onListRooms);

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
