package vizion.com.ott.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

import vizion.com.ott.Adapters.ListRoomAdapter;
import vizion.com.ott.Entities.IActivity;
import vizion.com.ott.Models.Room;
import vizion.com.ott.R;

public class RoomsActivity extends AppCompatActivity implements IActivity {
    
    private Button btnCreateRoom;
    private ListView lvRooms;
    private ListRoomAdapter listRoomAdapter;
    private ArrayList<Room> listRooms;
    private int totalPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rooms);
        
        this.mapViewIDs();
        this.addEventListeners();
        
        this.loadRooms();
    }

    private void loadRooms() {
        totalPage = getIntent().getIntExtra("total_page", 0);
        listRooms = getIntent().getParcelableArrayListExtra("list_rooms");
        listRoomAdapter = new ListRoomAdapter(RoomsActivity.this, R.layout.room_item, listRooms);
        lvRooms.setAdapter(listRoomAdapter);
    }

    @Override
    public void mapViewIDs() {
        lvRooms = (ListView) findViewById(R.id.lvRooms);
        btnCreateRoom = (Button) findViewById(R.id.btnCreateRoom);
    }

    @Override
    public void addEventListeners() {
        btnCreateRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createRoom();
            }
        });
    }

    private void createRoom() {
    }
}
