package au.com.iglooit.winerymap.android.view.home;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class HomePageViewAdapter extends FragmentPagerAdapter
{

    private Context _context;

    public HomePageViewAdapter(Context context, FragmentManager fm)
    {
        super(fm);
        _context = context;

    }

    @Override
    public Fragment getItem(int position)
    {
        Fragment f = new Fragment();
        switch (position)
        {
            case 0:
                f = MapSearchFragment.newInstance(_context);
                break;
            case 1:
                f = SearchDetailsFragment.newInstance(_context);
                break;
        }
        return f;
    }

    @Override
    public int getCount()
    {
        return 2;
    }
}
