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
import au.com.iglooit.winerymap.android.dbhelper.DataHelper;
import com.androidquery.AQuery;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.util.List;
import java.util.Map;

public class NewsFragment extends Fragment implements PullRefreshListView.OnRefreshListener
{
    private PullRefreshListView mRefreshListView;
    private List<Map<String, Object>> mDataSource;
    private listViewAdapter mAdapter;
    private int pageSize = 10;

    private DataHelper databaseHelper = null;
    private ViewGroup root;
    AQuery aq;

    //    private List<String> data;
//    private BaseAdapter adapter;
    private Activity parentActivity;
//
//    private LinkedList<String> mListItems;
//    private ArrayAdapter<String> mAdapter;
//
//    private PullToRefreshListFragment mPullRefreshListFragment;
//    private PullToRefreshListView mPullRefreshListView;

    public static Fragment newInstance(Context context)
    {
        NewsFragment f = new NewsFragment();
        return f;
    }

    private DataHelper getDataHelper()
    {
        if (databaseHelper == null)
        {
            databaseHelper =
                OpenHelperManager.getHelper(this.getActivity(), DataHelper.class);
        }
        return databaseHelper;
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if (databaseHelper != null)
        {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        parentActivity = activity;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        root = (ViewGroup)inflater.inflate(R.layout.wm_news_fragment, container, false);
        aq = new AQuery(root);
        initContent();
        return root;
    }

//    @Override
//    public void onRefresh(PullToRefreshBase<ListView> refreshView)
//    {
//        // Do work to refresh the list here.
//        new GetDataTask().execute();
//    }
//
//    private class GetDataTask extends AsyncTask<Void, Void, String[]>
//    {
//
//        @Override
//        protected String[] doInBackground(Void... params)
//        {
//            // Simulates a background job.
//            try
//            {
//                Thread.sleep(4000);
//            }
//            catch (InterruptedException e)
//            {
//            }
//            return mStrings;
//        }
//
//        @Override
//        protected void onPostExecute(String[] result)
//        {
//            mListItems.addFirst("Added after refresh...");
//            mAdapter.notifyDataSetChanged();
//
//            // Call onRefreshComplete when the list has been refreshed.
//            mPullRefreshListView.onRefreshComplete();
//
//            super.onPostExecute(result);
//        }
//    }

    private void initContent()
    {
        mRefreshListView = (PullRefreshListView)root.findViewById(R.id.latest_news_listview);
        mDataSource = DataIniter.initValue(1, 15);
        mAdapter = new listViewAdapter();
        mRefreshListView.setAdapter(mAdapter);
        mRefreshListView.setonRefreshListener(this);
    }

    private String[] mStrings = {"Abbaye de Belloc"};

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
            View view = LayoutInflater.from(NewsFragment.this.getActivity()).inflate(
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
