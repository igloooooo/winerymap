package au.com.iglooit.winerymap.android.view.news;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import au.com.iglooit.winerymap.android.R;
import au.com.iglooit.winerymap.android.core.component.ScrollableListView;
import au.com.iglooit.winerymap.android.core.component.listview.DataIniter;
import au.com.iglooit.winerymap.android.core.component.listview.PullRefreshListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NewsActivity extends FragmentActivity implements PullRefreshListView.OnRefreshListener {
    private PullRefreshListView mRefreshListView;
    private List<Map<String, Object>> mDataSource;
    private listViewAdapter mAdapter;
    private int pageSize = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wm_news_layout);
        initContent();
    }

    private void initContent() {
        mRefreshListView = (PullRefreshListView)this.findViewById(R.id.latest_news_listview);
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
                View view = LayoutInflater.from(NewsActivity.this).inflate(
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
