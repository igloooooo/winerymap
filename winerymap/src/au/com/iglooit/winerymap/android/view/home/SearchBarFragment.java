package au.com.iglooit.winerymap.android.view.home;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import au.com.iglooit.winerymap.android.R;
import au.com.iglooit.winerymap.android.dbhelper.DataHelper;
import au.com.iglooit.winerymap.android.dbhelper.WineryInfoHelper;
import com.j256.ormlite.android.apptools.OpenHelperManager;

public class SearchBarFragment extends Fragment
{
    private DataHelper databaseHelper = null;
    public SearchBarFragmentListener listener;
    private AutoCompleteTextView textView;
    private Button searchButton;

    public static Fragment newInstance(Context context)
    {
        SearchBarFragment f = new SearchBarFragment();
        return f;
    }

    private DataHelper getDataHelper()
    {
        if (databaseHelper == null)
        {
            databaseHelper =
                OpenHelperManager.getHelper(this.getActivity(), DataHelper.class);
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
        ViewGroup root = (ViewGroup)inflater.inflate(R.layout.wm_home_search_bar, null);
        SearchBarAdapter adapter = new SearchBarAdapter(root.getContext(), android.R.layout.simple_dropdown_item_1line,
            null, WineryInfoHelper.TITLE, android.R.id.text1);
        // two chars to fire searching
        textView = (AutoCompleteTextView)root.findViewById(R.id.autoCompleteTextView1);
        textView.setThreshold(2);
        textView.setAdapter(adapter);
        searchButton = (Button)root.findViewById(R.id.searchButton);
        setListeners();
        return root;
    }

    private void setListeners()
    {
        textView.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent)
            {
                if (keyCode == KeyEvent.KEYCODE_ENTER)
                {
                    if (listener != null)
                    {
                        listener.onTextViewEnter(textView.getText().toString());
                    }
                    return true;
                }
                return false;
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (listener != null)
                {
                    listener.onSearchButton();
                }
            }
        });

    }

    public interface SearchBarFragmentListener
    {
        void onSearchButton();

        void onTextViewEnter(String content);
    }
}
