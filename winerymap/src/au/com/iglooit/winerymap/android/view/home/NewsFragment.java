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
import android.widget.ListAdapter;
import android.widget.ListView;
import au.com.iglooit.winerymap.android.R;
import au.com.iglooit.winerymap.android.dbhelper.DataHelper;
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

    private void initContent()
    {
        PullToRefreshListView plv = (PullToRefreshListView)root.findViewById(R.id.latest_news_listview);
        ListAdapter adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_list_item_1,
            Arrays.asList(mStrings));
        plv.setAdapter(adapter);

        plv.setOnRefreshListener(this);
    }

    private String[] mStrings = {"Abbaye de Belloc", "Abbaye du Mont des Cats", "Abertam", "Abondance", "Ackawi",
        "Acorn", "Adelost", "Affidelice au Chablis", "Afuega'l Pitu", "Airag", "Airedale", "Aisy Cendre",
        "Allgauer Emmentaler", "Abbaye de Belloc", "Abbaye du Mont des Cats", "Abertam", "Abondance", "Ackawi",
        "Acorn", "Adelost", "Affidelice au Chablis", "Afuega'l Pitu", "Airag", "Airedale", "Aisy Cendre",
        "Allgauer Emmentaler"};
}
