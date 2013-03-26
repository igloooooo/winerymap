package au.com.iglooit.winerymap.android.view.home;


import android.content.Context;
import android.database.Cursor;
import android.widget.SimpleCursorAdapter;
import au.com.iglooit.winerymap.android.dbhelper.WineryInfoHelper;

public class SearchBarAdapter extends SimpleCursorAdapter {

    private WineryInfoHelper dbHelper = null;
    private Context context;
    // query field
    private String queryField;
    public SearchBarAdapter(Context context, int layout, Cursor c,String from, int to) {
        super(context, layout, c, new String[] { from },new int[] { to });
        this.context = context;
        this.queryField = from;
    }
    /**
     * query db
     */
    @Override
    public Cursor runQueryOnBackgroundThread(CharSequence constraint) {
        if (constraint != null) {
            return getDbHelper().findWineryForSearch((String) constraint);
        } else {
            return null;
        }
    }
    /**
     * return value
     */
    @Override
    public CharSequence convertToString(Cursor cursor) {
        return cursor.getString(cursor.getColumnIndex(queryField));
    }
    public WineryInfoHelper getDbHelper() {
        if (dbHelper == null) {
            dbHelper = new WineryInfoHelper(this.context);
        }
        return dbHelper;
    }
}
