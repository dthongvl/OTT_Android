package vizion.com.ott.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import vizion.com.ott.Entities.IActivity;
import vizion.com.ott.Listeners.ClientReadyListener;
import vizion.com.ott.Listeners.LeaveRoomListener;
import vizion.com.ott.Listeners.PlayerLeaveRoomListener;
import vizion.com.ott.Listeners.StartPlayingListener;
import vizion.com.ott.Listeners.UpdateRoomInfoListener;
import vizion.com.ott.Models.MyEnemy;
import vizion.com.ott.Models.MyRoom;
import vizion.com.ott.Models.MyUser;
import vizion.com.ott.Models.User;
import vizion.com.ott.R;
import vizion.com.ott.Utils.Commands;
import vizion.com.ott.Utils.MyProgressDialog;
import vizion.com.ott.Utils.SocketHelper;

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
    private ImageButton btnStart;
    private ImageButton btnReady;
    private TextView txtReady;
    private TextView txtWaiting;
    private ImageView imgGuestCoin;
    private ImageButton btnLeave;
    private ImageButton btnUpdateRoom;
    private int boType;

    private User oponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait);

        this.mapViewIDs();
        this.addEventListeners();
        loadContent();

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        final Intent intentFinal= intent;
        imgGuestAvatar.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                imgGuestAvatar.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                Picasso.with(WaitActivity.this).load(intentFinal.getStringExtra("oponent_avatar")).memoryPolicy(MemoryPolicy.NO_CACHE )
                        .networkPolicy(NetworkPolicy.NO_CACHE).resize(imgGuestAvatar.getWidth(),imgGuestAvatar.getHeight()).centerCrop().into(imgGuestAvatar);

            }
        });

        txtGuestName.setText(intent.getStringExtra("oponent_name"));
        txtGuestCoin.setText(String.valueOf(intent.getDoubleExtra("oponent_coin",0)));
        txtWaiting.setVisibility(View.INVISIBLE);
        txtGuestName.setVisibility(View.VISIBLE);
        txtGuestCoin.setVisibility(View.VISIBLE);
        txtReady.setVisibility(View.VISIBLE);
        imgGuestAvatar.setVisibility(View.VISIBLE);
        imgGuestCoin.setVisibility(View.VISIBLE);
        Toast.makeText(WaitActivity.this,"okok",Toast.LENGTH_LONG).show();
    }

    private void loadContent() {

        txtRoomName.setText(MyRoom.getInstance().getRoomName());
        txtCoinBet.setText(String.valueOf(MyRoom.getInstance().getMoneyBet()));
        txtType.setText("Best of :" + String.valueOf(MyRoom.getInstance().getBestOf()));
        txtHostName.setText(MyUser.getInstance().getName());
        txtHostCoin.setText(String.valueOf(MyUser.getInstance().getCoinCard()));
        imgHostAvatar.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                imgHostAvatar.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                Picasso.with(WaitActivity.this).load(MyUser.getInstance().getAvatar())
                        .resize(imgHostAvatar.getWidth(),imgHostAvatar.getHeight()).centerCrop().into(imgHostAvatar);
            }
        });
        if(MyRoom.getInstance().isHost()){
            btnReady.setVisibility(View.INVISIBLE);
            btnReady.setEnabled(false);
            btnStart.setEnabled(true);
            btnStart.setVisibility(View.VISIBLE);
            txtReady.setVisibility(View.VISIBLE);
            btnUpdateRoom.setEnabled(true);
        }
        else{
            btnReady.setVisibility(View.VISIBLE);
            btnReady.setEnabled(true);
            btnStart.setEnabled(false);
            btnStart.setVisibility(View.INVISIBLE);
            txtReady.setVisibility(View.INVISIBLE);
            btnUpdateRoom.setEnabled(false);


        }
        if(MyRoom.getInstance().getGuestUid()==null){

            txtWaiting.setVisibility(View.VISIBLE);
            txtGuestName.setVisibility(View.INVISIBLE);
            txtGuestCoin.setVisibility(View.INVISIBLE);
            txtReady.setVisibility(View.INVISIBLE);
            imgGuestAvatar.setVisibility(View.INVISIBLE);
            imgGuestCoin.setVisibility(View.INVISIBLE);
        }
        else {
            imgGuestAvatar.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onGlobalLayout() {
                    imgGuestAvatar.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    Picasso.with(WaitActivity.this).load(MyEnemy.getInstance().getAvatar()).memoryPolicy(MemoryPolicy.NO_CACHE )
                            .networkPolicy(NetworkPolicy.NO_CACHE).resize(imgGuestAvatar.getWidth(),imgGuestAvatar.getHeight()).centerCrop().into(imgGuestAvatar);

                }
            });

            txtGuestName.setText(MyEnemy.getInstance().getName());
            txtGuestCoin.setText(String.valueOf(MyEnemy.getInstance().getCoinCard()));
        }
        btnStart.setEnabled(false);
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
        btnStart = (ImageButton) findViewById(R.id.btnStart);
        btnReady = (ImageButton) findViewById(R.id.btnReady);
        txtReady = (TextView) findViewById(R.id.txtReady);
        txtWaiting= (TextView) findViewById(R.id.txtWaiting);
        imgGuestCoin = (ImageView) findViewById(R.id.imgGuestCoin);
        btnUpdateRoom = (ImageButton) findViewById(R.id.btnUpdateRoom);
        btnLeave = (ImageButton) findViewById(R.id.btnLeave);
    }

    @Override
    public void addEventListeners() {
        SocketHelper.getInstance().addListener(Commands.CLIENT_LEAVE_ROOM_RS, LeaveRoomListener.getInstance(WaitActivity.this));
        SocketHelper.getInstance().addListener(Commands.PLAYER_LEAVE_ROOM, PlayerLeaveRoomListener.getInstance(WaitActivity.this));
        SocketHelper.getInstance().addListener(Commands.CLIENT_READY_RS, ClientReadyListener.getInstance(WaitActivity.this));
        SocketHelper.getInstance().addListener(Commands.CLIENT_UPDATE_ROOM_INFO_RS, UpdateRoomInfoListener.getInstance(WaitActivity.this));
        SocketHelper.getInstance().addListener(Commands.CLIENT_START_PLAYING_RS, StartPlayingListener.getInstance(WaitActivity.this));
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startGame();
            }
        });

        btnLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leaveRoom();
            }
        });

        btnReady.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clientReady();
            }
        });

        btnUpdateRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUpdateDialog();

            }
        });
    }

    private void openUpdateDialog() {



        LayoutInflater inflater = LayoutInflater.from(WaitActivity.this);
        View createUpdateView = inflater.inflate(R.layout.update_room, null);


        final EditText txtRoomName = (EditText) createUpdateView.findViewById(R.id.txtRoomName);
        final EditText txtCoinBet = (EditText) createUpdateView.findViewById(R.id.txtCoinBet);
        final ImageButton btnCreate = (ImageButton) createUpdateView.findViewById(R.id.btnCreate);
        final ImageButton btnCancel = (ImageButton) createUpdateView.findViewById(R.id.btnCancel);
        final FrameLayout layoutTop = (FrameLayout) createUpdateView.findViewById(R.id.layoutTop);
        final RadioGroup rdgroupBo = (RadioGroup) createUpdateView.findViewById(R.id.rdgroupBo);
        layoutTop.bringToFront();
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(createUpdateView);
        dialog.show();




        rdgroupBo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rdBo1:
                        boType=1;
                        break;
                    case R.id.rdBo3:
                        boType=3;
                        break;
                    case R.id.rdBo5:
                        boType=5;
                        break;
                    case R.id.rdBo7:
                        boType=7;
                        break;
                }
            }
        });

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
                    updateRoom();
                    dialog.dismiss();
                }

            }

            private void updateRoom() {
                MyProgressDialog.getInstance(WaitActivity.this,getString(R.string.update)).showProgressDialog();
                String roomName = txtRoomName.getText().toString();
                Double coinBet = Double.parseDouble(txtCoinBet.getText().toString());
                JSONObject regObject = new JSONObject();
                try {
                    regObject.put("room_id",MyRoom.getInstance().getId());
                    regObject.put("room_name",roomName);
                    regObject.put("money_bet",coinBet);
                    regObject.put("best_of", boType);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                SocketHelper.getInstance().sendRequest(Commands.CLIENT_UPDATE_ROOM_INFO,regObject);
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

    private void startGame() {
        JSONObject reqObject = new JSONObject();
        try {
            reqObject.put("room_id",MyRoom.getInstance().getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        SocketHelper.getInstance().sendRequest(Commands.CLIENT_START_PLAYING,reqObject);
    }

    private void clientReady() {
        JSONObject reqObject = new JSONObject();
        try {
            reqObject.put("room_id",MyRoom.getInstance().getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        SocketHelper.getInstance().sendRequest(Commands.CLIENT_READY,reqObject);
    }

    private void leaveRoom() {
        JSONObject reqObject = new JSONObject();
        if(MyRoom.getInstance().getHostUid().equalsIgnoreCase(MyUser.getInstance().getUid())
                &&MyRoom.getInstance().getGuestUid()==null){
            finish();
        }
        try {
            reqObject.put("room_id",MyRoom.getInstance().getId());
            reqObject.put("uid",MyUser.getInstance().getUid());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        SocketHelper.getInstance().sendRequest(Commands.CLIENT_LEAVE_ROOM,reqObject);
    }
}
