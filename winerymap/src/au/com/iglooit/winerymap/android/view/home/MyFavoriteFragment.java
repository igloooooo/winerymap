package au.com.iglooit.winerymap.android.view.home;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import au.com.iglooit.winerymap.android.R;
import au.com.iglooit.winerymap.android.dbhelper.WineryInfoHelper;
import com.androidquery.AQuery;
import com.j256.ormlite.android.apptools.OpenHelperManager;

public class MyFavoriteFragment extends Fragment
{
    private WineryInfoHelper databaseHelper = null;
    private ViewGroup root;
    AQuery aq;

    public static Fragment newInstance(Context context)
    {
        MyFavoriteFragment f = new MyFavoriteFragment();
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

        root = (ViewGroup)inflater.inflate(R.layout.wm_my_favorite_winery_layout, null);
        aq = new AQuery(root);
        return root;
    }
}
