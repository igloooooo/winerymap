package au.com.iglooit.winerymap.android.view.home;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import au.com.iglooit.winerymap.android.R;
import au.com.iglooit.winerymap.android.dbhelper.DataHelper;
import au.com.iglooit.winerymap.android.dbhelper.WineryInfoHelper;
import com.androidquery.AQuery;
import com.handmark.pulltorefresh.extras.listfragment.PullToRefreshListFragment;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class NewsFragment extends Fragment implements PullToRefreshBase.OnRefreshListener<ListView>
{
    private DataHelper databaseHelper = null;
    private ViewGroup root;
    AQuery aq;

    private List<String> data;
    private BaseAdapter adapter;
    private Activity parentActivity;

    private LinkedList<String> mListItems;
    private ArrayAdapter<String> mAdapter;

    private PullToRefreshListFragment mPullRefreshListFragment;
    private PullToRefreshListView mPullRefreshListView;

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
        super.onActivityCreated(savedInstanceState);    //To change body of overridden methods use File | Settings |

        mPullRefreshListFragment = (PullToRefreshListFragment)this.getActivity().getSupportFragmentManager()
            .findFragmentById(R.id.latest_news_list);

        // Get PullToRefreshListView from Fragment
        mPullRefreshListView = mPullRefreshListFragment.getPullToRefreshListView();

        // Set a listener to be invoked when the list should be refreshed.
        mPullRefreshListView.setOnRefreshListener(this);

        // You can also just use mPullRefreshListFragment.getListView()
        ListView actualListView = mPullRefreshListView.getRefreshableView();

        mListItems = new LinkedList<String>();
        mListItems.addAll(Arrays.asList(mStrings));
        mAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_list_item_1, mListItems);

        // You can also just use setListAdapter(mAdapter) or
        // mPullRefreshListView.setAdapter(mAdapter)
        actualListView.setAdapter(mAdapter);

        mPullRefreshListFragment.setListShown(true);

//        data = new ArrayList<String>();
//        data.add("a");
//        data.add("b");
//        data.add("c");
//
//        final ScrollableListView listView = (ScrollableListView)root.findViewById(R.id.resultList_news);
//        adapter = new BaseAdapter()
//        {
//            public View getView(int position, View convertView, ViewGroup parent)
//            {
//                TextView tv = new TextView(getActivity());
//                tv.setText(data.get(position));
//                return tv;
//            }
//
//            public long getItemId(int position)
//            {
//                return 0;
//            }
//
//            public Object getItem(int position)
//            {
//                return null;
//            }
//
//            public int getCount()
//            {
//                return data.size();
//            }
//        };
//        listView.setAdapter(adapter);
//
//        listView.setonRefreshListener(new ScrollableListView.OnRefreshListener()
//        {
//            public void onRefresh()
//            {
//                new AsyncTask<Void, Void, Void>()
//                {
//                    protected Void doInBackground(Void... params)
//                    {
//                        try
//                        {
//                            Thread.sleep(1000);
//                        }
//                        catch (Exception e)
//                        {
//                            e.printStackTrace();
//                        }
//                        data.add("new data after refresh");
//                        return null;
//                    }
//
//                    @Override
//                    protected void onPostExecute(Void result)
//                    {
//                        adapter.notifyDataSetChanged();
//                        listView.onRefreshComplete();
//                    }
//
//                }.execute(null);
//            }
//        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        root = (ViewGroup)inflater.inflate(R.layout.wm_news_fragment, container, false);
        aq = new AQuery(root);
//        mListView = (ListView)root.findViewById(R.id.resultList_news);

//        String[] mFrom = new String[]{"img", "title1", "title2", "time"};
//        int[] mTo = new int[]{R.id.img, R.id.title1, R.id.title2, R.id.time};
//        // get data from database
//        List<WineryInfo> resultList = getDataHelper().findWineryByName("This");
//
//        Map<String, Object> mMap = null;
//
//        mMap = new HashMap<String, Object>();
//        mMap.put("img", R.drawable.ic_launcher);
//        mMap.put("title1", 0);
//        mMap.put("title2", "Remove All");
//        mMap.put("time", "2011-08-15 09:00");
//        mList.add(mMap);
//
//        mAdapter = new SearchDetailsBarAdapter(root.getContext(), mList, R.layout.wm_home_search_result_list_item,
//            mFrom, mTo);
//        mListView.setAdapter(mAdapter);


        return root;
    }

    @Override
    public void onRefresh(PullToRefreshBase<ListView> refreshView)
    {
        // Do work to refresh the list here.
        new GetDataTask().execute();
    }

    private class GetDataTask extends AsyncTask<Void, Void, String[]>
    {

        @Override
        protected String[] doInBackground(Void... params)
        {
            // Simulates a background job.
            try
            {
                Thread.sleep(4000);
            }
            catch (InterruptedException e)
            {
            }
            return mStrings;
        }

        @Override
        protected void onPostExecute(String[] result)
        {
            mListItems.addFirst("Added after refresh...");
            mAdapter.notifyDataSetChanged();

            // Call onRefreshComplete when the list has been refreshed.
            mPullRefreshListView.onRefreshComplete();

            super.onPostExecute(result);
        }
    }

    private String[] mStrings = {"Abbaye de Belloc", "Abbaye du Mont des Cats", "Abertam", "Abondance", "Ackawi",
        "Acorn", "Adelost", "Affidelice au Chablis", "Afuega'l Pitu", "Airag", "Airedale", "Aisy Cendre",
        "Allgauer Emmentaler", "Abbaye de Belloc", "Abbaye du Mont des Cats", "Abertam", "Abondance", "Ackawi",
        "Acorn", "Adelost", "Affidelice au Chablis", "Afuega'l Pitu", "Airag", "Airedale", "Aisy Cendre",
        "Allgauer Emmentaler"};
}
