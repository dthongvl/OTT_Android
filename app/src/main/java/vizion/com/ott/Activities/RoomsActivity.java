package vizion.com.ott.Activities;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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

import org.json.JSONException;
import org.json.JSONObject;

import vizion.com.ott.Adapters.ListRoomAdapter;
import vizion.com.ott.Entities.IActivity;
import vizion.com.ott.Listeners.CreateRoomListener;
import vizion.com.ott.Listeners.GetRoomPageResultListener;
import vizion.com.ott.Listeners.JoinRoomListener;
import vizion.com.ott.Models.MyRoom;
import vizion.com.ott.Models.RoomList;
import vizion.com.ott.Models.MyUser;
import vizion.com.ott.Models.Room;
import vizion.com.ott.R;
import vizion.com.ott.Utils.Commands;
import vizion.com.ott.Utils.MyProgressDialog;
import vizion.com.ott.Utils.SocketHelper;

public class RoomsActivity extends AppCompatActivity implements IActivity {
    
    private ImageButton btnCreateRoom;
    GridView gvListRoom;
    ListRoomAdapter adapterRooms;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rooms);
        this.mapViewIDs();
        this.addEventListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapterRooms.notifyDataSetChanged();
    }

    @Override
    public void mapViewIDs() {
        gvListRoom = (GridView) findViewById(R.id.gvListRoom);
        adapterRooms = new ListRoomAdapter(RoomsActivity.this, RoomList.getInstance().getListRooms());
        RoomList.getInstance().setAdapter(adapterRooms);
        gvListRoom.setAdapter(adapterRooms);
        btnCreateRoom = (ImageButton) findViewById(R.id.btnCreateRoom);
    }

    @Override
    public void addEventListeners() {
        SocketHelper.getInstance().addListener(Commands.CLIENT_CREATE_ROOM_RS, CreateRoomListener.getInstance(RoomsActivity.this));
        SocketHelper.getInstance().addListener(Commands.CLIENT_GET_ROOM_BY_PAGE_RS, GetRoomPageResultListener.getInstance(RoomsActivity.this));
        SocketHelper.getInstance().addListener(Commands.CLIENT_JOIN_ROOM_RS,JoinRoomListener.getInstance(RoomsActivity.this));
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

                Log.d("item", String.valueOf(firstVisibleItem)+"a");
                Log.d("item", String.valueOf(visibleItemCount)+"b");

                if(firstVisibleItem + visibleItemCount >= totalItemCount){
                    if(RoomList.getInstance().getLoadedPage() < RoomList.getInstance().getTotalPage()
                            &&RoomList.getInstance().getCurrentPage()==RoomList.getInstance().getLoadedPage()){
                        RoomList.getInstance().setLoadedPage(RoomList.getInstance().getLoadedPage()+1);
                        RoomList.getInstance().getRoomPage(RoomList.getInstance().getLoadedPage());
                        Log.d("page", String.valueOf(RoomList.getInstance().getLoadedPage()));
                    }
                }
                if(RoomList.getInstance().getCurrentPage()<= RoomList.getInstance().getLoadedPage()) {

                    RoomList.getInstance().setCurrentPage(((firstVisibleItem + visibleItemCount) / 10)+1);
                    Log.d("curPage", String.valueOf(RoomList.getInstance().getCurrentPage()));
                }


            }
        });

        gvListRoom.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(RoomList.getInstance().getListRooms().get(position).getState().equalsIgnoreCase("joinable"))
                    joinRoom(position);
                else{
                    Toast.makeText(RoomsActivity.this,"Phòng đầy",Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void joinRoom(int position) {
        JSONObject reqObject = new JSONObject();
        MyRoom.getInstance().setId(RoomList.getInstance().getListRooms().get(position).getId());
        MyRoom.getInstance().setRoomName(RoomList.getInstance().getListRooms().get(position).getRoomName());
        MyRoom.getInstance().setMoneyBet(RoomList.getInstance().getListRooms().get(position).getMoneyBet());
        MyRoom.getInstance().setHost(false);
        try {
            reqObject.put("room_id", RoomList.getInstance().getListRooms().get(position).getId());
            reqObject.put("uid",MyUser.getInstance().getUid());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        SocketHelper.getInstance().sendRequest(Commands.CLIENT_JOIN_ROOM,reqObject);
        MyProgressDialog.getInstance(RoomsActivity.this,"Đang vào phòng").showProgressDialog();
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
               MyRoom.getInstance().setRoomName(roomName);
               MyRoom.getInstance().setMoneyBet(coinBet);
               MyRoom.getInstance().setHostUid(MyUser.getInstance().getUid());
               MyRoom.getInstance().setHostReady(false);
               MyRoom.getInstance().setHost(true);
               MyRoom.getInstance().setBestOf(5);
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
}
