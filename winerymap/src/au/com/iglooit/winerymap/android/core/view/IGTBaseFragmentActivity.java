package au.com.iglooit.winerymap.android.core.view;

import android.support.v4.app.FragmentActivity;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;

public abstract class IGTBaseFragmentActivity<T extends OrmLiteSqliteOpenHelper> extends FragmentActivity implements
    IIGTBaseActivity<T>
{
    protected T databaseHelper;

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
}
