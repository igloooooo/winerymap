package au.com.iglooit.winerymap.android.view.home;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import au.com.iglooit.winerymap.android.R;
import au.com.iglooit.winerymap.android.dbhelper.WineryInfoHelper;
import au.com.iglooit.winerymap.android.entity.WineryInfo;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchDetailsFragment extends Fragment implements SearchBarFragment.SearchBarFragmentListener
{
    private WineryInfoHelper databaseHelper = null;
    private SearchBarFragment searchBar;
    private ViewGroup root;
    private ListView mListView;
    private SearchResultAdapter mAdapter;
    private List<Map<String, Object>> mList = new ArrayList<Map<String, Object>>();

    public static Fragment newInstance(Context context)
    {
        SearchDetailsFragment f = new SearchDetailsFragment();
        return f;
    }

    private WineryInfoHelper getDataHelper()
    {
        if (databaseHelper == null)
        {
            databaseHelper =
                OpenHelperManager.getHelper(this.getActivity(), WineryInfoHelper.class);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        root = (ViewGroup)inflater.inflate(R.layout.wm_home_latest_search_details_page, null);
        searchBar = (SearchBarFragment)this.getActivity().getSupportFragmentManager()
            .findFragmentById(R.id.searchBar1);
        searchBar.listener = this;
        mListView = (ListView)root.findViewById(R.id.resultList1);

        String[] mFrom = new String[]{"img", "title1", "title2", "time"};
        int[] mTo = new int[]{R.id.img, R.id.title1, R.id.title2, R.id.time};
        // get data from database
        List<Map<String, Object>> mList = new ArrayList<Map<String, Object>>();

        mAdapter =  new SearchResultAdapter(root.getContext(), R.layout.wm_home_search_result_list_item, new ArrayList<WineryInfo>());
        mListView.setAdapter(mAdapter);

        // listener
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id)
            {
                @SuppressWarnings("unchecked")
                HashMap<String, Object> map = (HashMap<String, Object>)adapterView.getItemAtPosition(position);
                // goto another activity
            }
        });
        return root;
    }

    @Override
    public void onSearchButton()
    {

    }

    @Override
    public void onTextViewEnter(String content)
    {
        String[] mFrom = new String[]{"img", "title1", "title2", "time"};
        int[] mTo = new int[]{R.id.img, R.id.title1, R.id.title2, R.id.time};
        List<WineryInfo> resultList = getDataHelper().findWineryByName(content);
        Map<String, Object> mMap = null;
        mList = new ArrayList<Map<String, Object>>();
        for (WineryInfo info : resultList)
        {
            mMap = new HashMap<String, Object>();
            mMap.put("img", R.drawable.ic_launcher);
            mMap.put("title1", info.id);
            mMap.put("title2", info.title);
            mMap.put("time", "2011-08-15 09:00");
            mList.add(mMap);
        }
        mAdapter.clear();
        mAdapter.addAll(resultList);
        mListView.invalidateViews();
    }
}
