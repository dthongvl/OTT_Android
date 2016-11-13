package vizion.com.ott.Models;

import java.util.ArrayList;

/**
 * Created by dthongvl on 11/13/16.
 */
public class MyRoom {
    private int totalPage;
    private String roomData;
    private ArrayList<Room> listRooms;
    private static MyRoom ourInstance = new MyRoom();

    public static MyRoom getInstance() {
        return ourInstance;
    }

    private MyRoom() {
    }

    public String getRoomData() {
        return roomData;
    }

    public void setRoomData(String roomData) {
        this.roomData = roomData;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public ArrayList<Room> getListRooms() {
        return listRooms;
    }

    public void setListRooms(ArrayList<Room> listRooms) {
        this.listRooms = listRooms;
    }

    public void clearListRooms() {
        this.listRooms.clear();
    }

    public void addRoom(Room room) {
        this.listRooms.add(room);
    }
}
