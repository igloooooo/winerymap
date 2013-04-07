package au.com.iglooit.winerymap.android.view.myfavorite;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import au.com.iglooit.winerymap.android.R;
import au.com.iglooit.winerymap.android.view.core.WMBaseFragmentActivity;
import com.handmark.pulltorefresh.extras.listfragment.PullToRefreshListFragment;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.Arrays;
import java.util.LinkedList;

public class MyFavoriteActivity extends WMBaseFragmentActivity implements PullToRefreshBase.OnRefreshListener<ListView>
{
    private LinkedList<String> mListItems;
    private ArrayAdapter<String> mAdapter;

    private PullToRefreshListFragment mPullRefreshListFragment;
    private PullToRefreshListView mPullRefreshListView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wm_my_favorite_winery_layout);
        initContent();
    }

    private void initContent()
    {
        mPullRefreshListFragment = (PullToRefreshListFragment)this.getSupportFragmentManager()
            .findFragmentById(R.id.latest_news_list);

        // Get PullToRefreshListView from Fragment
        mPullRefreshListView = mPullRefreshListFragment.getPullToRefreshListView();

        // Set a listener to be invoked when the list should be refreshed.
        mPullRefreshListView.setOnRefreshListener(this);

        // You can also just use mPullRefreshListFragment.getListView()
        ListView actualListView = mPullRefreshListView.getRefreshableView();

        mListItems = new LinkedList<String>();
        mListItems.addAll(Arrays.asList(mStrings));
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mListItems);

        // You can also just use setListAdapter(mAdapter) or
        // mPullRefreshListView.setAdapter(mAdapter)
        actualListView.setAdapter(mAdapter);

        mPullRefreshListFragment.setListShown(true);
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
            // get favorite list
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
