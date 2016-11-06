package vizion.com.ott.Adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import vizion.com.ott.Models.Room;
import vizion.com.ott.R;

public class ListRoomAdapter extends ArrayAdapter{
    private Activity context;
    private int resource;
    private ArrayList<Room> objects;


    public ListRoomAdapter(Activity context, int resource, ArrayList<Room> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = this.context.getLayoutInflater();
            convertView = inflater.inflate(this.resource, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.txtRoomName = (TextView) convertView.findViewById(R.id.txtRoomName);
            viewHolder.txtState = (TextView) convertView.findViewById(R.id.txtState);
            viewHolder.txtMoneyBet = (TextView) convertView.findViewById(R.id.txtMoneyBet);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Room room = this.objects.get(position);
        if (room != null) {
            viewHolder.txtRoomName.setText(room.getRoomName());
            viewHolder.txtState.setText(room.getState());
            viewHolder.txtMoneyBet.setText(String.valueOf(room.getMoneyBet()));
        }

        return convertView;
    }

    private static class ViewHolder {
        private TextView txtRoomName;
        private TextView txtState;
        private TextView txtMoneyBet;
    }
}
