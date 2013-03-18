package au.com.iglooit.winerymap.android.view.home;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import au.com.iglooit.winerymap.android.R;

import java.util.List;
import java.util.Map;

public class SearchDetailsBarAdapter extends SimpleAdapter
{
    public List<Map<String, Object>> data;
    public Context mContext;

    public SearchDetailsBarAdapter(Context context, List<Map<String, Object>> data, int resource, String[] from,
                                   int[] to)
    {
        super(context, data, resource, from, to);
        this.mContext = mContext;
        this.data = data;
    }

    @Override
    public int getCount()
    {
        return data.size();
    }

    @Override
    public Object getItem(int arg0)
    {
        return data.get(arg0);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        Map<String, Object> map = data.get(position);
        View view = super.getView(position, convertView, parent);
        TextView tv = (TextView)view.findViewById(R.id.title1);
        tv.setText(((Integer)map.get("title1")).toString());
        return view;
    }
}
