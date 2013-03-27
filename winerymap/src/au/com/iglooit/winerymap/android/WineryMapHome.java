package au.com.iglooit.winerymap.android;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import au.com.iglooit.winerymap.android.core.component.ScrollLayout;
import au.com.iglooit.winerymap.android.view.home.HomePageViewAdapter;
import au.com.iglooit.winerymap.android.view.myfavorite.MyFavoriteActivity;
import au.com.iglooit.winerymap.android.view.news.NewsActivity;
import au.com.iglooit.winerymap.android.view.search.SearchWineryActivity;
import com.androidquery.AQuery;

import java.util.ArrayList;

/**
 * test for branch
 */
public class WineryMapHome extends FragmentActivity
{
    public static final String TAG = null;
    /**
     * Called when the activity is first created.
     */
    private ScrollLayout scrollLayout;
    private ViewPager viewpage;
    private LayoutInflater mInflater;
    private ArrayList<View> views;
    private ImageView iv, message, main_mask_bg;
    private RelativeLayout rl1, rl2;
    private MyOnClickListener myOnClickListener;
    private boolean isOpen = false;
    private FragmentManager manager = null;
    private Context context = null;
    private HomePageViewAdapter homePageViewAdapter;
    private AQuery aq;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wm_home_layout);
        context = WineryMapHome.this;
        aq = new AQuery(this);
        initView();
    }

    private void initView()
    {
        mInflater = LayoutInflater.from(this);
        views = new ArrayList<View>();
        myOnClickListener = new MyOnClickListener();

        viewpage = (ViewPager)findViewById(R.id.pager);

        scrollLayout = (ScrollLayout)findViewById(R.id.scrollLayout);

//        View view1 = mInflater.inflate(R.layout.wm_home_latest_search_details_page, null);
//        View view2 = mInflater.inflate(R.layout.wm_home_map_search_page, null);
//    		rl2 = (RelativeLayout) view1.findViewById(R.id.rl_2);
//    		rl1 = (RelativeLayout) view1.findViewById(R.id.rl_1);
//    		rl1.setOnClickListener(myOnClickListener);
//    		rl2.setOnClickListener(myOnClickListener);

        main_mask_bg = (ImageView)findViewById(R.id.main_menu);
        main_mask_bg.setOnClickListener(myOnClickListener);
        // set view adapter
        homePageViewAdapter = new HomePageViewAdapter(getApplicationContext(), getSupportFragmentManager());
        viewpage.setAdapter(homePageViewAdapter);
        viewpage.setCurrentItem(0);
        aq.id(R.id.goto_search_page).clicked(this, "goToSearch");
        aq.id(R.id.goto_news_page).clicked(this, "goToNews");
        aq.id(R.id.goto_my_favorite).clicked(this, "goToMyFavorite");
    }

    public class MyOnClickListener implements View.OnClickListener
    {

        public void onClick(View v)
        {
            switch (v.getId())
            {
                case R.id.rl_1:
                    Toast.makeText(WineryMapHome.this, "DemoActivity rl_1",
                        Toast.LENGTH_SHORT).show();
                    break;
                case R.id.rl_2:
                    Toast.makeText(WineryMapHome.this, "DemoActivity rl_2",
                        Toast.LENGTH_SHORT).show();
                    break;
                case R.id.main_menu:
                    Log.i(TAG, "main_menu");
                    DisplayMetrics metrics = new DisplayMetrics();
                    WineryMapHome.this.getWindowManager().getDefaultDisplay().getMetrics(metrics);
                    int width = metrics.widthPixels;
                    Log.i(TAG, "width=" + width);
                    scrollLayout.scrollToRigth(width);
                    break;
                default:
                    break;
            }
        }
    }

    public class ViewPageAdapter extends PagerAdapter
    {

        @Override
        public int getCount()
        {

            return views.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1)
        {
            return arg0 == arg1;
        }

        @Override
        public int getItemPosition(Object object)
        {
            return super.getItemPosition(object);
        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2)
        {

            ((ViewPager)arg0).removeView(views.get(arg1));
        }

        @Override
        public Object instantiateItem(View container, int position)
        {
            ((ViewPager)container).addView(views.get(position));
            return views.get(position);
        }
    }

    public void goToSearch(View view)
    {
        Intent searchIntent = new Intent(this, SearchWineryActivity.class);
        startActivity(searchIntent);
    }

    public void goToNews(View view)
    {
        Intent searchIntent = new Intent(this, NewsActivity.class);
        startActivity(searchIntent);
    }

    public void goToMyFavorite(View view)
    {
        Intent searchIntent = new Intent(this, MyFavoriteActivity.class);
        startActivity(searchIntent);
    }
}
