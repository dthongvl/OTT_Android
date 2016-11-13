package vizion.com.ott.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TabHost;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import vizion.com.ott.Adapters.ListRoomAdapter;
import vizion.com.ott.Entities.IActivity;
import vizion.com.ott.Models.MyUser;
import vizion.com.ott.Models.Room;
import vizion.com.ott.R;
import vizion.com.ott.Utils.Commands;
import vizion.com.ott.Utils.SocketHelper;

public class RoomsActivity extends AppCompatActivity implements IActivity {
    
    private ImageButton btnCreateRoom;
    private ArrayList<Room> listRooms;
    private int totalPage;
    private int currentLoadedPage =1;
    private int currentPage =1;
    private TabHost tabHost;
    private ProgressDialog progressDialog;
    GridView gvListRoom;
    ListRoomAdapter adapterRooms;
    private Room createdRoom;

    private boolean isUpdate=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rooms);
        this.loadRooms();
        this.mapViewIDs();
        this.addEventListeners();
        

    }

    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage(getString(R.string.wait_login));
        }
        progressDialog.show();
    }

    private void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private void loadRooms() {
        Intent intent = getIntent();
        totalPage = intent.getIntExtra("total_page",0);
        listRooms=new ArrayList<>();
        listRooms= intent.getParcelableArrayListExtra("list_rooms");
    }

    @Override
    public void mapViewIDs() {
        gvListRoom = (GridView) findViewById(R.id.gvListRoom);
        adapterRooms = new ListRoomAdapter(RoomsActivity.this,listRooms);
        gvListRoom.setAdapter(adapterRooms);


        tabHost = (TabHost)findViewById(R.id.tabPlay);
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
        tabHost.addTab(spec);
        btnCreateRoom = (ImageButton) findViewById(R.id.btnCreateRoom);
    }

    @Override
    public void addEventListeners() {
        SocketHelper.getInstance().addListener(Commands.CLIENT_CREATE_ROOM_RS, onCreateRoomResult);
        SocketHelper.getInstance().addListener(Commands.CLIENT_GET_ROOM_BY_PAGE_RS, onGetRoomPageResult);
        SocketHelper.getInstance().addListener(Commands.CLIENT_JOIN_ROOM_RS,onJoinRoomResult);
        SocketHelper.getInstance().addListener(Commands.CLIENT_RECEIVE_UPDATE_ROOMS, onRoomUpdate);
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
                    if(currentLoadedPage <totalPage){
                        currentLoadedPage++;
                        getRoomPage(currentLoadedPage);
                        Log.d("page", String.valueOf(currentLoadedPage));
                    }
                }
                if(currentPage<=currentLoadedPage) {
                    currentPage = ((firstVisibleItem + visibleItemCount) / 10) + 1;
                    Log.d("curPage", String.valueOf(currentPage));
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
            reqObject.put("room_id",listRooms.get(position).getId());
            reqObject.put("uid",MyUser.getInstance().getUid());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        SocketHelper.getInstance().sendRequest(Commands.CLIENT_JOIN_ROOM,reqObject);
    }

    private void getRoomPage(int i) {
        JSONObject reqObject = new JSONObject();
        try {
            reqObject.put("page",i);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        SocketHelper.getInstance().sendRequest(Commands.CLIENT_GET_ROOM_BY_PAGE,reqObject);
    }


    private void openCreateRoomDialog() {
        LayoutInflater inflater = LayoutInflater.from(RoomsActivity.this);
        View createRoomView = inflater.inflate(R.layout.create_room, null);


        final EditText txtRoomName = (EditText) createRoomView.findViewById(R.id.txtRoomName);
        final EditText txtCoinBet = (EditText) createRoomView.findViewById(R.id.txtCoinBet);
        final ImageButton btnCreate = (ImageButton) createRoomView.findViewById(R.id.btnCreate);
        final ImageButton btnCancel = (ImageButton) createRoomView.findViewById(R.id.btnCancel);

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(RoomsActivity.this);
        alertDialogBuilder.setView(createRoomView);


        final AlertDialog createRoomDialog = alertDialogBuilder.create();
        createRoomDialog.show();

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createRoomDialog.cancel();
            }
        });


       btnCreate.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if(isInputValid()){
                   createRoom();
                   createRoomDialog.dismiss();
               }

           }

           private void createRoom() {
               showProgressDialog();
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

    private Emitter.Listener onCreateRoomResult = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    try {
                        boolean isSuccess = data.getBoolean("isSuccess");
                        if(isSuccess){
                            createdRoom.setId(data.getString("room_id"));
                            createdRoom.setHostUid(MyUser.getInstance().getUid());
                            createdRoom.setState("joinable");
                            Toast.makeText(RoomsActivity.this,data.getString("room_id"),Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(RoomsActivity.this,"failed",Toast.LENGTH_LONG).show();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    finally {
                        hideProgressDialog();
                    }

                }
            });
        }
    };

    private Emitter.Listener onGetRoomPageResult = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    try {

                        JSONArray arrRooms = null;
                        arrRooms = data.getJSONArray("rooms");
                        if(isUpdate)
                            updateRoom(arrRooms);
                        else
                            addNewRoom(arrRooms);

                        adapterRooms.notifyDataSetChanged();
                        Log.d("abc", data.toString());
                        Toast.makeText(RoomsActivity.this,"ok",Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        }
    };

    private void updateRoom(JSONArray arrRooms) {
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

    private Emitter.Listener onJoinRoomResult = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    try {
                        boolean isSuccess = data.getBoolean("isSuccess");
                        if(isSuccess){
                           Toast.makeText(RoomsActivity.this,"ok",Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(RoomsActivity.this,"failed",Toast.LENGTH_LONG).show();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };

    private Emitter.Listener onRoomUpdate = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    isUpdate=true;
                    JSONObject data = new JSONObject();
                    try {
                        totalPage=data.getInt("total_page");
                        getRoomPage(currentPage);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };
}
