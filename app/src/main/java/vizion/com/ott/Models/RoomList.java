package vizion.com.ott.Models;

import android.app.Activity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import vizion.com.ott.Adapters.ListRoomAdapter;
import vizion.com.ott.Utils.Commands;
import vizion.com.ott.Utils.SocketHelper;

/**
 * Created by dthongvl on 11/13/16.
 */
public class RoomList {
    private int currentPage;
    private int loadedPage;
    private int totalPage;
    private ArrayList<Room> listRooms;
    private ArrayList<Room> currentPageRooms;
    private Activity activity;
    private ListRoomAdapter adapter;
    private boolean isUpdate;

    public boolean isUpdate() {
        return isUpdate;
    }

    public void setUpdate(boolean update) {
        isUpdate = update;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    private static RoomList ourInstance = new RoomList();

    public static RoomList getInstance() {
        return ourInstance;
    }

    public ArrayList<Room> getCurrentPageRooms() {
        return currentPageRooms;
    }

    public void setCurrentPageRooms(ArrayList<Room> currentPageRooms) {
        this.currentPageRooms = currentPageRooms;
    }
    public void setFirstPageRooms(ArrayList<Room> firstPageRooms) {
        if(ourInstance.listRooms.size()>0)
            for(int roomIndex =0; roomIndex<firstPageRooms.size();roomIndex++){
                ourInstance.listRooms.set(roomIndex,firstPageRooms.get(roomIndex));
            }
        else {
            ourInstance.listRooms.addAll(firstPageRooms);
            ourInstance.loadedPage=1;
        }

    }

    public int getLoadedPage() {
        return loadedPage;
    }

    public void setLoadedPage(int loadedPage) {
        this.loadedPage = loadedPage;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    private RoomList() {
        listRooms = new ArrayList<>();
        currentPageRooms = new ArrayList<>();
        totalPage=0;
        currentPage=1;
        loadedPage=1;
        isUpdate=false;

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
        ourInstance.listRooms.clear();
        ourInstance.currentPage=0;
        ourInstance.loadedPage=0;
        if (adapter != null) {
            ourInstance.adapter.notifyDataSetChanged();
        }
    }

    public ListRoomAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(ListRoomAdapter adapter) {
        this.adapter = adapter;
    }

    public void updateRoom(ArrayList<Room> roomPage){
        if(ourInstance.isUpdate){
            if(ourInstance.currentPage<ourInstance.loadedPage)
                for(int roomIndex = 0; roomIndex<roomPage.size();roomIndex++){
                    ourInstance.listRooms.set((ourInstance.currentPage-1)*10+roomIndex,roomPage.get(roomIndex));
                }
            else{
                for(int roomIndex = ourInstance.listRooms.size()-1; roomIndex>=(ourInstance.currentPage-1)*10;roomIndex--){
                    ourInstance.listRooms.remove(roomIndex);
                }
                ourInstance.listRooms.addAll(roomPage);
            }
            isUpdate=false;
        }
        else
            ourInstance.listRooms.addAll(roomPage);
        ourInstance.adapter.notifyDataSetChanged();
    }
    public void getRoomPage(int i) {
        JSONObject reqObject = new JSONObject();
        try {
            reqObject.put("page",i);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        SocketHelper.getInstance().sendRequest(Commands.CLIENT_GET_ROOM_BY_PAGE,reqObject);
    }

}
