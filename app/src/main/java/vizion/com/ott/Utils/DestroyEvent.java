package vizion.com.ott.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import vizion.com.ott.Models.MyRoom;
import vizion.com.ott.Models.MyUser;


public class DestroyEvent {
    private static DestroyEvent ourInstance = new DestroyEvent();

    public static DestroyEvent getInstance() {
        return ourInstance;
    }

    private DestroyEvent() {
    }

    public void leaveRoom() {
        JSONObject reqObject = new JSONObject();
        try {
            reqObject.put("room_id",MyRoom.getInstance().getId());
            reqObject.put("uid",MyUser.getInstance().getUid());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        SocketHelper.getInstance().sendRequest(Commands.CLIENT_LEAVE_ROOM,reqObject);
    }
}
