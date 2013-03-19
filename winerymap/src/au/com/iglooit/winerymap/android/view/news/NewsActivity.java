package au.com.iglooit.winerymap.android.view.news;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import au.com.iglooit.winerymap.android.R;
import au.com.iglooit.winerymap.android.core.component.ScrollableListView;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends FragmentActivity {
    private List<String> data;
    private BaseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wm_news_layout);
        initContent();
    }

    private void initContent() {
        data = new ArrayList<String>();
        data.add("a");
        data.add("b");
        data.add("c");

        final ScrollableListView listView = (ScrollableListView) findViewById(R.id.resultList_news);
        adapter = new BaseAdapter() {
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView tv = new TextView(getApplicationContext());
                tv.setText(data.get(position));
                return tv;
            }

            public long getItemId(int position) {
                return 0;
            }

            public Object getItem(int position) {
                return null;
            }

            public int getCount() {
                return data.size();
            }
        };
        listView.setAdapter(adapter);

        listView.setonRefreshListener(new ScrollableListView.OnRefreshListener() {
            public void onRefresh() {
                new AsyncTask<Void, Void, Void>() {
                    protected Void doInBackground(Void... params) {
                        try {
                            Thread.sleep(1000);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        data.add("new data after refresh");
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        adapter.notifyDataSetChanged();
                        listView.onRefreshComplete();
                    }

                }.execute(null);
            }
        });
    }
}
