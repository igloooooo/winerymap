package au.com.iglooit.winerymap.android.view.home;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.handmark.pulltorefresh.extras.listfragment.PullToRefreshListFragment;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.Arrays;
import java.util.LinkedList;

public class FavoriteListFragment extends PullToRefreshListFragment implements PullToRefreshBase
    .OnRefreshListener<ListView>
{
    private LinkedList<String> mListItems;
    private ArrayAdapter<String> mAdapter;
    private PullToRefreshListView mPullRefreshListView;

    public static Fragment newInstance(Context context)
    {
        FavoriteListFragment f = new FavoriteListFragment(context);
        return f;
    }

    public FavoriteListFragment(Context context)
    {
        super();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        mPullRefreshListView = this.getPullToRefreshListView();

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

        this.setListShown(true);
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
