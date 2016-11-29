package vizion.com.ott.Models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class Room implements Parcelable {
    private String id;
    private String roomName;
    private String state;
    private double moneyBet;
    private String hostUid;
    private boolean hostReady;
    private String guestUid;
    private boolean guestReady;
    private double bestOf;
    private String matchId;
    private int gameId;
    private boolean couting = false;

    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }

    public boolean isCouting() {
        return couting;
    }

    public void setCouting(boolean couting) {
        this.couting = couting;
    }
    //private

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public Room() {
    }

    public Room(JSONObject room) {
        try {
            this.id = room.getString("id");
            this.roomName = room.getString("room_name");
            this.state = room.getString("state");
            this.moneyBet = room.getDouble("money_bet");
            JSONObject member = room.getJSONObject("host");
            this.hostUid = member.getString("uid");
            this.hostReady = member.getBoolean("ready");
            this.bestOf = room.getDouble("best_of");
            if (!this.state.equals("joinable")) {
                member = room.getJSONObject("guest");
                this.guestUid = member.getString("uid");
                this.guestReady = member.getBoolean("ready");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public double getMoneyBet() {
        return moneyBet;
    }

    public void setMoneyBet(double moneyBet) {
        this.moneyBet = moneyBet;
    }

    public String getHostUid() {
        return hostUid;
    }

    public void setHostUid(String hostUid) {
        this.hostUid = hostUid;
    }

    public boolean isHostReady() {
        return hostReady;
    }

    public void setHostReady(boolean hostReady) {
        this.hostReady = hostReady;
    }

    public String getGuestUid() {
        return guestUid;
    }

    public void setGuestUid(String guestUid) {
        this.guestUid = guestUid;
    }

    public boolean isGuestReady() {
        return guestReady;
    }

    public void setGuestReady(boolean guestReady) {
        this.guestReady = guestReady;
    }

    public double getBestOf() {
        return bestOf;
    }

    public void setBestOf(double bestOf) {
        this.bestOf = bestOf;
    }

    protected Room(Parcel in) {
        id = in.readString();
        roomName = in.readString();
        state = in.readString();
        moneyBet = in.readDouble();
        hostUid = in.readString();
        hostReady = in.readByte() != 0x00;
        guestUid = in.readString();
        guestReady = in.readByte() != 0x00;
        bestOf= in.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(roomName);
        dest.writeString(state);
        dest.writeDouble(moneyBet);
        dest.writeDouble(bestOf);
        dest.writeString(hostUid);
        dest.writeByte((byte) (hostReady ? 0x01 : 0x00));
        dest.writeString(guestUid);
        dest.writeByte((byte) (guestReady ? 0x01 : 0x00));
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Room> CREATOR = new Parcelable.Creator<Room>() {
        @Override
        public Room createFromParcel(Parcel in) {
            return new Room(in);
        }

        @Override
        public Room[] newArray(int size) {
            return new Room[size];
        }
    };
}