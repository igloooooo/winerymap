package au.com.iglooit.winerymap.android.view.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import au.com.iglooit.winerymap.android.R;
import au.com.iglooit.winerymap.android.WineryMapHome;
import com.androidquery.AQuery;

public class TitleBarFragment extends Fragment
{
    private AQuery aq;

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle)
    {
        ViewGroup root = (ViewGroup)layoutInflater.inflate(R.layout.wm_home_title_bar_fragment, null);
        aq = new AQuery(root);
        aq.id(R.id.goHomeButton).clicked(this, "onClickHomeButton");
        return root;
    }

    public void onClickHomeButton(View view)
    {
        Intent intent = new Intent(this.getActivity(), WineryMapHome.class);
        this.getActivity().startActivity(intent);
    }
}
