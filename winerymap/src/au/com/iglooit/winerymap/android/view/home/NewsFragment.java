package au.com.iglooit.winerymap.android.view.home;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import au.com.iglooit.winerymap.android.R;
import au.com.iglooit.winerymap.android.dbhelper.WineryInfoHelper;
import au.com.iglooit.winerymap.android.entity.WineryInfo;
import com.androidquery.AQuery;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewsFragment extends Fragment
{
    private WineryInfoHelper databaseHelper = null;
    private ViewGroup root;
    AQuery aq;
    private ListView mListView;
    private SearchDetailsBarAdapter mAdapter;
    private List<Map<String, Object>> mList;

    public static Fragment newInstance(Context context)
    {
        NewsFragment f = new NewsFragment();
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

        root = (ViewGroup)inflater.inflate(R.layout.wm_news_layout, null);
        aq = new AQuery(root);
        mListView = (ListView)root.findViewById(R.id.resultList1);

        String[] mFrom = new String[]{"img", "title1", "title2", "time"};
        int[] mTo = new int[]{R.id.img, R.id.title1, R.id.title2, R.id.time};
        // get data from database
        List<WineryInfo> resultList = getDataHelper().findWineryByName("This");

        Map<String, Object> mMap = null;

        mMap = new HashMap<String, Object>();
        mMap.put("img", R.drawable.ic_launcher);
        mMap.put("title1", 0);
        mMap.put("title2", "Remove All");
        mMap.put("time", "2011-08-15 09:00");
        mList.add(mMap);

        mAdapter = new SearchDetailsBarAdapter(root.getContext(), mList, R.layout.wm_home_search_result_list_item,
            mFrom, mTo);
        mListView.setAdapter(mAdapter);

        return root;
    }
}
