package vizion.com.ott.Models;

/**
 * Created by Razor on 20/11/2016.
 */
public class MyRoom extends Room {

    private boolean isHost;
    private static MyRoom ourInstance = new MyRoom();

    public static MyRoom getInstance() {
        return ourInstance;
    }

    private MyRoom() {
    }

    public boolean isHost() {
        return isHost;
    }

    public void setHost(boolean host) {
        isHost = host;
    }

    public void clear(){
        ourInstance.setId(null);
        ourInstance.setHost(false);
        ourInstance.setMoneyBet(0);
        ourInstance.setRoomName(null);
        ourInstance.setHostUid(null);
        ourInstance.setHostReady(false);
        ourInstance.setGuestUid(null);
        ourInstance.setGuestReady(false);
        ourInstance.setState(null);
        ourInstance.setBestOf(3);
    }
}
