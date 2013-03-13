package au.com.iglooit.winerymap.android.view.home;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import au.com.iglooit.winerymap.android.R;
import au.com.iglooit.winerymap.android.dbhelper.DataHelper;
import au.com.iglooit.winerymap.android.dbhelper.WineryInfoHelper;
import com.j256.ormlite.android.apptools.OpenHelperManager;

public class SearchBarFragment extends Fragment {
    private DataHelper databaseHelper = null;

    public static Fragment newInstance(Context context) {
        SearchBarFragment f = new SearchBarFragment();
        return f;
    }

    private DataHelper getDataHelper() {
        if (databaseHelper == null) {
            databaseHelper =
                    OpenHelperManager.getHelper(this.getActivity(), DataHelper.class);
        }
        return databaseHelper;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.wm_home_search_bar, null);
        SearchBarAdapter adapter = new SearchBarAdapter(root.getContext(), android.R.layout.simple_dropdown_item_1line,
                null, WineryInfoHelper.TITLE, android.R.id.text1);
        // 设置输入一个字符就弹出提示列表(默认输入两个字符时才弹出提示)
        ((AutoCompleteTextView) root.findViewById(R.id.autoCompleteTextView1)).setThreshold(2);
        ((AutoCompleteTextView) root.findViewById(R.id.autoCompleteTextView1)).setAdapter(adapter);
        return root;
    }
}
