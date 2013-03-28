package au.com.iglooit.winerymap.android.view.search;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import au.com.iglooit.winerymap.android.R;
import au.com.iglooit.winerymap.android.constants.ApplicationConstants;
import au.com.iglooit.winerymap.android.dbhelper.DataHelper;
import au.com.iglooit.winerymap.android.dbhelper.WineryInfoHelper;
import au.com.iglooit.winerymap.android.entity.WineryInfo;
import au.com.iglooit.winerymap.android.service.WineryInfoService;
import au.com.iglooit.winerymap.android.service.WineryInfoServiceImpl;
import au.com.iglooit.winerymap.android.view.home.SearchDetailsBarAdapter;
import au.com.iglooit.winerymap.android.view.winerydetails.WineryDetailsActivity;
import com.androidquery.AQuery;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchWineryActivity extends FragmentActivity
{
    private AQuery aq;
    private ProgressDialog progressDialog;
    private ListView mListView;
    private SearchDetailsBarAdapter mAdapter;
    private List<Map<String, Object>> mList;
    private String content;
    private DataHelper databaseHelper = null;
    private WineryInfoService wineryInfoService;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wm_search_winery_layout);
        aq = new AQuery(this);
        wineryInfoService = new WineryInfoServiceImpl(getDataHelper().getWineryInfoDao());
        initContent();
    }

    private DataHelper getDataHelper()
    {
        if (databaseHelper == null)
        {
            databaseHelper =
                OpenHelperManager.getHelper(this, DataHelper.class);
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

    private void initContent()
    {
        mList = new ArrayList<Map<String, Object>>();
        mListView = (ListView)findViewById(R.id.resultList1);

        String[] mFrom = new String[]{"img", "title1", "title2", "time"};
        int[] mTo = new int[]{R.id.img, R.id.title1, R.id.title2, R.id.time};
        // get data from database
        List<WineryInfo> resultList = wineryInfoService.findWineryByName("This");

        Map<String, Object> mMap = null;

        mMap = new HashMap<String, Object>();
        mMap.put("img", R.drawable.ic_launcher);
        mMap.put("title1", 0);
        mMap.put("title2", "Remove All");
        mMap.put("time", "2011-08-15 09:00");
        mList.add(mMap);

        mAdapter = new SearchDetailsBarAdapter(this, mList, R.layout.wm_home_search_result_list_item,
            mFrom, mTo);
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
                Intent intent = new Intent(SearchWineryActivity.this, WineryDetailsActivity.class);
                intent.putExtra(ApplicationConstants.WINERY_ID, (Integer)(mList.get(position).get("id")));
                SearchWineryActivity.this.startActivity(intent);
            }
        });
        mListView.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View view)
            {
                return false;
            }
        });

//        aq.id(R.id.searchText).textChanged(this, "onTextViewEnter1");
        aq.id(R.id.searchButton).clicked(this, "onClickSearchButton");
    }

    public void onClickSearchButton(View view)
    {
        if (aq.id(R.id.searchText).getText().length() > 1)
        {
            content = aq.id(R.id.searchText).getText().toString();
//            new myAsyncTask().execute(null);
                        onTextViewEnter(content);
        }
    }

    public void onTextViewEnter(final String content)
    {
        runOnUiThread(new Runnable()
        {
            public void run()
            {
                List<WineryInfo> resultList = wineryInfoService.findWineryByName(content);
                Map<String, Object> mMap = null;
                mList.clear();
                for (WineryInfo info : resultList)
                {
                    mMap = new HashMap<String, Object>();
                    mMap.put("img", R.drawable.ic_launcher);
                    mMap.put("id", info.id);
                    mMap.put("title1", info.id);
                    mMap.put("title2", info.title);
                    mMap.put("time", "2011-08-15 09:00");
                    mList.add(mMap);
                }
                String[] mFrom = new String[]{"img", "title1", "title2", "time"};
                int[] mTo = new int[]{R.id.img, R.id.title1, R.id.title2, R.id.time};
                mAdapter.data = mList;
                mAdapter.notifyDataSetChanged();
                Toast.makeText(SearchWineryActivity.this, "Refreshing...", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void onTextViewEnter1(final CharSequence s, int start, int before, int count)
    {
        String content1 = s.toString();
        if (content1 != null && content1.length() >= 2)
        {
            //            onTextViewEnter(content);

        }
    }

    private class myAsyncTask extends AsyncTask<Void, Void, Void>
    {

        @Override
        protected Void doInBackground(Void... params)
        {
            List<WineryInfo> resultList = wineryInfoService.findWineryByName(content);
            Map<String, Object> mMap = null;
            mList = new ArrayList<Map<String, Object>>();
            for (WineryInfo info : resultList)
            {
                mMap = new HashMap<String, Object>();
                mMap.put("img", R.drawable.ic_launcher);
                mMap.put("id", info.id);
                mMap.put("title1", info.id);
                mMap.put("title2", info.title);
                mMap.put("time", "2011-08-15 09:00");
                mList.add(mMap);
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void result)
        {
            super.onPostExecute(result);
            mAdapter.data = mList;
            mAdapter.notifyDataSetChanged();
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

    }
}
