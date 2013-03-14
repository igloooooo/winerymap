package au.com.iglooit.winerymap.android.view.home;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import au.com.iglooit.winerymap.android.R;
import au.com.iglooit.winerymap.android.entity.WineryInfo;

import java.util.ArrayList;

public class SearchResultAdapter extends ArrayAdapter<WineryInfo> {
    Context context;
    int layoutResourceId;
    ArrayList<WineryInfo> data = null;

    public SearchResultAdapter(Context context, int textViewResourceId, ArrayList<WineryInfo> objects) {
        super(context, textViewResourceId, objects);
        this.context = context;
        this.layoutResourceId = textViewResourceId;
        this.data = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        WineryInfoHolder holder = null;

        if (row == null) {
            View view = super.getView(position, convertView, parent);

            holder = new WineryInfoHolder();
            holder.imgIcon = (ImageView) row.findViewById(R.id.img);
            holder.txtTitle1 = (TextView) row.findViewById(R.id.title1);
            holder.txtTitle2 = (TextView) row.findViewById(R.id.title2);
            row.setTag(holder);
        } else {
            holder = (WineryInfoHolder) row.getTag();
        }

        WineryInfo info = data.get(position);
        holder.txtTitle1.setText(info.id);
        holder.txtTitle2.setText(info.title);
        holder.imgIcon.setImageResource(R.drawable.ic_launcher);

        return row;
    }

    static class WineryInfoHolder {
        ImageView imgIcon;
        TextView txtTitle1;
        TextView txtTitle2;
        TextView time;
    }
}
