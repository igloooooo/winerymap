package au.com.iglooit.winerymap.android.view.core;

import au.com.iglooit.winerymap.android.core.view.IGTBaseFragmentActivity;
import au.com.iglooit.winerymap.android.dbhelper.DataHelper;
import com.j256.ormlite.android.apptools.OpenHelperManager;

public class WMBaseFragmentActivity extends IGTBaseFragmentActivity<DataHelper>
{

    @Override
    public DataHelper getDataHelper()
    {
        if (databaseHelper == null)
        {
            databaseHelper =
                OpenHelperManager.getHelper(this, DataHelper.class);
        }
        return databaseHelper;
    }
}
