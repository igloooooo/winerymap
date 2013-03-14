package au.com.iglooit.winerymap.android.view.home;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import au.com.iglooit.winerymap.android.R;

import java.util.List;
import java.util.Map;

public class SearchResultAdapter extends SimpleAdapter {
    public List<Map<String, Object>> mItemList;
    int count = 0;

    public SearchResultAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
        mItemList = (List<Map<String, Object>>) data;
        if (data == null) {
            count = 0;
        } else {
            count = data.size();
        }
    }
    public int getCount() {
        return mItemList.size();
    }

    public Object getItem(int pos) {
        return pos;
    }

    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Map<String ,Object> map = mItemList.get(position);
        View view = super.getView(position, convertView, parent);
        ImageView imageview = (ImageView)view.findViewById(R.id.img);

        TextView tv = (TextView)view.findViewById(R.id.title1);
        tv.setText((String)map.get("title1"));

        return view;
    }
}
