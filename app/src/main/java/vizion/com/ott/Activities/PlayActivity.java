package vizion.com.ott.Activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import vizion.com.ott.Entities.IActivity;
import vizion.com.ott.Listeners.MatchResultListener;
import vizion.com.ott.Listeners.PlayClientReadyListener;
import vizion.com.ott.Listeners.SubmitSelectionListener;
import vizion.com.ott.Models.MyEnemy;
import vizion.com.ott.Models.MyRoom;
import vizion.com.ott.Models.MyUser;
import vizion.com.ott.R;
import vizion.com.ott.Utils.Commands;
import vizion.com.ott.Utils.DestroyEvent;
import vizion.com.ott.Utils.SocketHelper;

public class PlayActivity extends AppCompatActivity implements IActivity {

    private TextView txtEnemyName;
    private TextView txtEnemyReady;
    private ImageView imgEnemyChoice;
    private ImageView imgEnemyAvatar;

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

    private int selection = 0;
    private int gameId = 1;
    private boolean ready = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        this.mapViewIDs();
        this.addEventListeners();

        MyRoom.getInstance().setGameId(1);

        loadMatch();
    }

    private void loadMatch() {
        Intent intent = getIntent();
        MyRoom.getInstance().setMatchId(intent.getStringExtra("match_id"));

        txtUserName.setText(MyUser.getInstance().getName());
        imgUserAvatar.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                imgUserAvatar.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                Picasso.with(PlayActivity.this).load(MyUser.getInstance().getAvatar()).memoryPolicy(MemoryPolicy.NO_CACHE )
                        .networkPolicy(NetworkPolicy.NO_CACHE).resize(imgUserAvatar.getWidth(),imgUserAvatar.getHeight()).centerCrop().into(imgUserAvatar);

            }
        });

        txtEnemyName.setText(MyEnemy.getInstance().getName());
        imgEnemyAvatar.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                imgEnemyAvatar.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                Picasso.with(PlayActivity.this).load(MyEnemy.getInstance().getAvatar()).memoryPolicy(MemoryPolicy.NO_CACHE )
                        .networkPolicy(NetworkPolicy.NO_CACHE).resize(imgEnemyAvatar.getWidth(),imgEnemyAvatar.getHeight()).centerCrop().into(imgEnemyAvatar);

            }
        });

        txtBo.setText(String.valueOf(MyRoom.getInstance().getBestOf()));
    }

    @Override
    public void mapViewIDs() {
        txtEnemyName = (TextView) findViewById(R.id.txtEnemyName);
        txtEnemyReady = (TextView) findViewById(R.id.txtEnemyReady);
        imgEnemyChoice = (ImageView) findViewById(R.id.imgEnemyChoice);
        imgEnemyAvatar = (ImageView) findViewById(R.id.imgEnemyAvatar);

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
        SocketHelper.getInstance().addListener(Commands.CLIENT_READY_RS, PlayClientReadyListener.getInstance(PlayActivity.this));
        SocketHelper.getInstance().addListener(Commands.CLIENT_SUBMIT_SELECTION_RS, SubmitSelectionListener.getInstance(PlayActivity.this));
        SocketHelper.getInstance().addListener(Commands.SERVER_SEND_MATCH_RESULT, MatchResultListener.getInstance(PlayActivity.this));
        btnBag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (gameId == MyRoom.getInstance().getGameId()) {
                    selection = 2;
                    imgUserChoice.setImageResource(R.drawable.bag);
                }
            }
        });

        btnHammer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (gameId == MyRoom.getInstance().getGameId()) {
                    selection = 1;
                    imgUserChoice.setImageResource(R.drawable.hammer);
                }
            }
        });

        btnScissor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (gameId == MyRoom.getInstance().getGameId()) {
                    selection = 3;
                    imgUserChoice.setImageResource(R.drawable.scissor);
                }
            }
        });

        btnReady.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MyRoom.getInstance().isCouting()) {
                    Toast.makeText(PlayActivity.this, getString(R.string.please_wait), Toast.LENGTH_SHORT).show();
                    return;
                }
                ready = !ready;
                clientReady();
                if (ready) {
                    txtUserReady.setText(getString(R.string.ready));
                } else {
                    txtUserReady.setText(getString(R.string.unready));
                }
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!MyRoom.getInstance().isCouting()) {
                    Toast.makeText(PlayActivity.this, getString(R.string.please_wait_or_ready), Toast.LENGTH_SHORT).show();
                } else if (gameId == MyRoom.getInstance().getGameId()) {
                        clientSubmit();
                        MyRoom.getInstance().setCouting(false);
                        ready = false;
                        txtTime.setText(getString(R.string.waiting));
                    }
                }
        });
    }

    private void clientReady() {
        JSONObject reqObject = new JSONObject();
        try {
            reqObject.put("match_id", MyRoom.getInstance().getMatchId());
            reqObject.put("uid", MyUser.getInstance().getUid());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        SocketHelper.getInstance().sendRequest(Commands.CLIENT_READY,reqObject);
    }

    private void clientSubmit() {
        JSONObject reqObject = new JSONObject();
        try {
            reqObject.put("match_id", MyRoom.getInstance().getMatchId());
            gameId++;
            reqObject.put("game_id", gameId);
            reqObject.put("uid", MyUser.getInstance().getUid());
            reqObject.put("selection", selection);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        SocketHelper.getInstance().sendRequest(Commands.CLIENT_SUBMIT_SELECTION, reqObject);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DestroyEvent.getInstance().leaveRoom();
    }
}
