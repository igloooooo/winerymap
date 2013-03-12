package au.com.iglooit.winerymap.android.view.home;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import au.com.iglooit.winerymap.android.R;

public class SearchDetailsFragment extends Fragment
{
    public static Fragment newInstance(Context context)
    {
        SearchDetailsFragment f = new SearchDetailsFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        ViewGroup root = (ViewGroup)inflater.inflate(R.layout.wm_home_latest_search_details_page, null);
        return root;
    }
}
