package au.com.iglooit.winerymap.android.view.home;


import android.content.Context;
import android.database.Cursor;
import android.widget.SimpleCursorAdapter;
import au.com.iglooit.winerymap.android.dbhelper.DataHelper;
import au.com.iglooit.winerymap.android.service.WineryInfoService;
import au.com.iglooit.winerymap.android.service.WineryInfoServiceImpl;
import com.j256.ormlite.android.apptools.OpenHelperManager;

public class SearchBarAdapter extends SimpleCursorAdapter
{

    private DataHelper dbHelper = null;
    private Context context;
    // query field
    private String queryField;
    private WineryInfoService wineryInfoService;

    public SearchBarAdapter(Context context, int layout, Cursor c, String from, int to)
    {
        super(context, layout, c, new String[]{from}, new int[]{to});
        this.context = context;
        this.queryField = from;
        wineryInfoService = new WineryInfoServiceImpl(getDbHelper().getWineryInfoDao());
    }

    /**
     * query db
     */
    @Override
    public Cursor runQueryOnBackgroundThread(CharSequence constraint)
    {
        if (constraint != null)
        {

            return wineryInfoService.findWineryForSearch((String)constraint);
        }
        else
        {
            return null;
        }
    }

    /**
     * return value
     */
    @Override
    public CharSequence convertToString(Cursor cursor)
    {
        return cursor.getString(cursor.getColumnIndex(queryField));
    }

    public DataHelper getDbHelper()
    {
        if (dbHelper == null)
        {
            dbHelper =
                OpenHelperManager.getHelper(context, DataHelper.class);
        }
        return dbHelper;
    }


}
