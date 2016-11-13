package vizion.com.ott.Adapters;

import android.app.Activity;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import vizion.com.ott.Models.Room;
import vizion.com.ott.R;

public class ListRoomAdapter extends BaseAdapter{
    private Activity context;
    private ArrayList<Room> objects;

    public ListRoomAdapter(Activity context, ArrayList<Room> objects) {
        this.context = context;
        this.objects = objects;
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = this.context.getLayoutInflater();
            convertView = inflater.inflate(R.layout.room_item, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.txtRoomName = (TextView) convertView.findViewById(R.id.txtRoomName);
            viewHolder.layoutRoom = (RelativeLayout) convertView.findViewById(R.id.layoutRoom);
            viewHolder.txtMoneyBet = (TextView) convertView.findViewById(R.id.txtMoneyBet);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Room room = this.objects.get(position);
        if (room != null) {
            viewHolder.txtRoomName.setText(room.getRoomName());
            if(room.getState().equalsIgnoreCase("full"))
                viewHolder.layoutRoom.setBackgroundResource(R.drawable.rooms_full);
            else if(room.getState().equalsIgnoreCase("playing"))
                viewHolder.layoutRoom.setBackgroundResource(R.drawable.rooms_playing);
            else
                viewHolder.layoutRoom.setBackgroundResource(R.drawable.rooms);
            viewHolder.txtMoneyBet.setText(String.valueOf(room.getMoneyBet()));
        }

        return convertView;
    }

    private static class ViewHolder {
        private TextView txtRoomName;
        private TextView txtMoneyBet;
        private RelativeLayout layoutRoom;
    }
}
