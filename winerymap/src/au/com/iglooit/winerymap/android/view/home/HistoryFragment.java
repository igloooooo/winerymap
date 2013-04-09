package au.com.iglooit.winerymap.android.view.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import au.com.iglooit.winerymap.android.R;
import au.com.iglooit.winerymap.android.core.component.listview.DataIniter;
import au.com.iglooit.winerymap.android.core.component.listview.PullRefreshListView;
import com.androidquery.AQuery;

import java.util.List;
import java.util.Map;

public class HistoryFragment extends Fragment implements PullRefreshListView.OnRefreshListener
{
    private Activity parentActivity;
    private ViewGroup root;
    private AQuery aq;

    private PullRefreshListView mRefreshListView;
    private List<Map<String, Object>> mDataSource;
    private listViewAdapter mAdapter;
    private int pageSize = 10;

    public static Fragment newInstance(Context context)
    {
        HistoryFragment f = new HistoryFragment();
        return f;
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        parentActivity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle)
    {
        root = (ViewGroup)layoutInflater.inflate(R.layout.wm_my_favorite_fragment, viewGroup, false);
        aq = new AQuery(root);
        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);    //To change body of overridden methods use File | Settings |
        initContent();
    }

    private void initContent()
    {
        mRefreshListView = (PullRefreshListView)this.getActivity().findViewById(R.id.my_favorite_listview);
        mDataSource = DataIniter.initValue(1, 15);
        mAdapter = new listViewAdapter();
        mRefreshListView.setAdapter(mAdapter);
        mRefreshListView.setonRefreshListener(this);
    }

    private class listViewAdapter extends BaseAdapter
    {
        int count = mDataSource.size();

        @Override
        public int getCount()
        {
            return count;
        }

        @Override
        public Object getItem(int position)
        {
            return position;
        }

        @Override
        public long getItemId(int position)
        {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            View view = LayoutInflater.from(HistoryFragment.this.getActivity()).inflate(
                R.layout.wm_news_listview_item, null);
            TextView title = (TextView)view.findViewById(R.id.item_title);
            TextView text = (TextView)view.findViewById(R.id.item_subtext);
            title.setText(mDataSource.get(position).get("title").toString());
            text.setText(mDataSource.get(position).get("subtext").toString());
            return view;
        }

    }

    private void chageListView(int pageStart, int pageSize)
    {
        List<Map<String, Object>> data = DataIniter.initValue(pageStart,
            pageSize);
        for (Map<String, Object> map : data)
        {
            this.mDataSource.add(map);
        }
        data = null;
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            mAdapter.count += pageSize;
            mAdapter.notifyDataSetChanged();
            mRefreshListView.onRefreshComplete();
            super.handleMessage(msg);
        }
    };

    @Override
    public void onPullUpRefresh()
    {
        new Thread()
        {
            public void run()
            {
                try
                {
                    //模拟网络请求时间
                    Thread.sleep(3 * 1000);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                chageListView(mDataSource.size() + 1, pageSize);
                handler.sendEmptyMessage(0);
            }
        }.start();
    }

    @Override
    public void onPullDownRefresh()
    {
        new Thread()
        {
            public void run()
            {
                try
                {
                    Thread.sleep(3 * 1000);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                chageListView(mDataSource.size() + 1, pageSize);
                handler.sendEmptyMessage(0);
            }
        }.start();
    }
}
