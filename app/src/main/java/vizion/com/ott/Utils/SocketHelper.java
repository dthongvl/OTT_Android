package vizion.com.ott.Utils;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONObject;

import java.net.URISyntaxException;

/**
 * Created by Rexviet on 10/29/16.
 */
public class SocketHelper {
    private static SocketHelper ourInstance = new SocketHelper();

    public static SocketHelper getInstance() {
        return ourInstance;
    }

    private SocketHelper() {
    }

    private static final String ADDRESS = "https://oan-tu-ti.herokuapp.com";

    private Socket mSocket;
    {
        try {
            mSocket = IO.socket(ADDRESS);
        } catch (URISyntaxException e) {
        }
    }

    public void AddListener (String event, Emitter.Listener l) {
        mSocket.on(event, l);
    }

    public void Connect () {
        mSocket.connect();
    }

    public void Disconnect () {
        mSocket.disconnect();
    }

    public void SendRequest (String cmd, JSONObject object) {
        mSocket.emit(cmd, object);
    }

    public void sendBytesRequest (String cmd, byte[] data) {
        mSocket.emit(cmd, data);
    }
}
