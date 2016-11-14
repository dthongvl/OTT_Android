package vizion.com.ott.Activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TabHost;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import vizion.com.ott.Adapters.ListRoomAdapter;
import vizion.com.ott.Entities.IActivity;
import vizion.com.ott.Listeners.CreateRoomListener;
import vizion.com.ott.Listeners.GetRoomPageResultListener;
import vizion.com.ott.Listeners.JoinRoomListener;
import vizion.com.ott.Models.MyRoom;
import vizion.com.ott.Models.MyUser;
import vizion.com.ott.Models.Room;
import vizion.com.ott.R;
import vizion.com.ott.Utils.Commands;
import vizion.com.ott.Utils.MyProgressDialog;
import vizion.com.ott.Utils.SocketHelper;

public class RoomsActivity extends AppCompatActivity implements IActivity {
    
    private ImageButton btnCreateRoom;
    private TabHost tabHost;
        GridView gvListRoom;
    ListRoomAdapter adapterRooms;
    private Room createdRoom;

    private boolean isUpdate=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rooms);
        this.mapViewIDs();
        this.addEventListeners();
        

    }

    @Override
    public void mapViewIDs() {
        gvListRoom = (GridView) findViewById(R.id.gvListRoom);
        adapterRooms = new ListRoomAdapter(RoomsActivity.this, MyRoom.getInstance().getListRooms());
        MyRoom.getInstance().setAdapter(adapterRooms);
        gvListRoom.setAdapter(adapterRooms);


        /*tabHost = (TabHost)findViewById(R.id.tabPlay);
        tabHost.setup();

        //Tab 1
        TabHost.TabSpec spec = tabHost.newTabSpec("Rooms");
        spec.setContent(R.id.tabRooms);
        spec.setIndicator("Rooms");
        tabHost.addTab(spec);

        //Tab 2
        spec = tabHost.newTabSpec("Friends");
        spec.setContent(R.id.tabFriends);
        spec.setIndicator("Friends");
        tabHost.addTab(spec);*/
        btnCreateRoom = (ImageButton) findViewById(R.id.btnCreateRoom);
    }

    @Override
    public void addEventListeners() {
        SocketHelper.getInstance().addListener(Commands.CLIENT_CREATE_ROOM_RS, CreateRoomListener.getInstance(RoomsActivity.this));
        SocketHelper.getInstance().addListener(Commands.CLIENT_GET_ROOM_BY_PAGE_RS, GetRoomPageResultListener.getInstance(RoomsActivity.this));
        SocketHelper.getInstance().addListener(Commands.CLIENT_JOIN_ROOM_RS, JoinRoomListener.getInstance(RoomsActivity.this));
        btnCreateRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCreateRoomDialog();
            }
        });

        gvListRoom.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(firstVisibleItem + visibleItemCount >= totalItemCount){
                    if(MyRoom.getInstance().getLoadedPage() <MyRoom.getInstance().getTotalPage() ){
                        MyRoom.getInstance().setLoadedPage(MyRoom.getInstance().getLoadedPage()+1);
                        MyRoom.getInstance().getRoomPage(MyRoom.getInstance().getLoadedPage());
                        Log.d("page", String.valueOf(MyRoom.getInstance().getTotalPage()));
                    }
                }
                if(MyRoom.getInstance().getCurrentPage()<=MyRoom.getInstance().getLoadedPage()) {
                    MyRoom.getInstance().setCurrentPage(((firstVisibleItem + visibleItemCount) / 10) + 1);
                    Log.d("curPage", String.valueOf(MyRoom.getInstance().getCurrentPage()));
                }
            }
        });

        gvListRoom.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                joinRoom(position);
            }
        });

    }

    private void joinRoom(int position) {
        JSONObject reqObject = new JSONObject();
        try {
            reqObject.put("room_id",MyRoom.getInstance().getListRooms().get(position).getId());
            reqObject.put("uid",MyUser.getInstance().getUid());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        SocketHelper.getInstance().sendRequest(Commands.CLIENT_JOIN_ROOM,reqObject);
    }




    private void openCreateRoomDialog() {
        LayoutInflater inflater = LayoutInflater.from(RoomsActivity.this);
        View createRoomView = inflater.inflate(R.layout.create_room, null);


        final EditText txtRoomName = (EditText) createRoomView.findViewById(R.id.txtRoomName);
        final EditText txtCoinBet = (EditText) createRoomView.findViewById(R.id.txtCoinBet);
        final ImageButton btnCreate = (ImageButton) createRoomView.findViewById(R.id.btnCreate);
        final ImageButton btnCancel = (ImageButton) createRoomView.findViewById(R.id.btnCancel);
        final FrameLayout layoutTop = (FrameLayout) createRoomView.findViewById(R.id.layoutTop);
        layoutTop.bringToFront();
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(createRoomView);
        dialog.show();

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });


       btnCreate.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if(isInputValid()){
                   createRoom();
                   dialog.dismiss();
               }

           }

           private void createRoom() {
               MyProgressDialog.getInstance(RoomsActivity.this,getString(R.string.createRoom)).showProgressDialog();
               String roomName = txtRoomName.getText().toString();
               Double coinBet = Double.parseDouble(txtCoinBet.getText().toString());
               createdRoom= new Room();
               createdRoom.setRoomName(roomName);
               createdRoom.setMoneyBet(coinBet);
               JSONObject regObject = new JSONObject();
               try {
                   regObject.put("room_name",roomName);
                   regObject.put("money_bet",coinBet);
                   regObject.put("host_uid", MyUser.getInstance().getUid());
               } catch (JSONException e) {
                   e.printStackTrace();
               }
               SocketHelper.getInstance().sendRequest(Commands.CLIENT_CREATE_ROOM,regObject);
           }
           private boolean isInputValid() {
               boolean result = true;
               if (TextUtils.isEmpty(txtRoomName.getText())) {
                   txtRoomName.setError(getString(R.string.input_error));
                   result = false;
               }
               if (TextUtils.isEmpty(txtCoinBet.toString())) {
                   txtCoinBet.setError(getString(R.string.input_error));
                   result = false;
               }
               return result;
           }
       });

    }


   /* private void updateRoom(JSONArray arrRooms) {
        isUpdate=false;
        try {
            for (int roomOrder = currentPage-1; roomOrder < arrRooms.length(); roomOrder++) {
                listRooms.set(roomOrder,new Room(arrRooms.getJSONObject(roomOrder)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void addNewRoom(JSONArray arrRooms) {

        try {
            for (int roomOrder = 0; roomOrder < arrRooms.length(); roomOrder++) {
                listRooms.add(new Room(arrRooms.getJSONObject(roomOrder)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
*/
}
