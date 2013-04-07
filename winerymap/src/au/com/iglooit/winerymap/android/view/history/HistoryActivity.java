package au.com.iglooit.winerymap.android.view.history;

import android.os.Bundle;
import au.com.iglooit.winerymap.android.R;
import au.com.iglooit.winerymap.android.view.core.WMBaseFragmentActivity;

public class HistoryActivity extends WMBaseFragmentActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wm_history_layout);
        initContent();
    }

    private void initContent()
    {

    }
}
