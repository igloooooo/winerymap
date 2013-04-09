package au.com.iglooit.winerymap.android.view.home;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class HomePageViewAdapter extends FragmentPagerAdapter
{

    private Context _context;
    private ArrayList<Fragment> fragments;

    public HomePageViewAdapter(Context context, FragmentManager fm)
    {
        super(fm);
        _context = context;
        fragments = new ArrayList<Fragment>();
        fragments.add(HistoryFragment.newInstance(_context));
        fragments.add(MapSearchFragment.newInstance(_context));
        fragments.add(MyFavoriteFragment.newInstance(_context));
    }

    @Override
    public Fragment getItem(int position)
    {
        return fragments.get(position);
    }

    @Override
    public int getCount()
    {
        return fragments.size();
    }

    @Override
    public int getItemPosition(Object object)
    {
        return super.getItemPosition(object);
    }
}
