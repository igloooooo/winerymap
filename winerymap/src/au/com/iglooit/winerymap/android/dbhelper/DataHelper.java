package au.com.iglooit.winerymap.android.dbhelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import au.com.iglooit.winerymap.android.core.LOG.LogConstants;
import au.com.iglooit.winerymap.android.entity.FavoriteInfo;
import au.com.iglooit.winerymap.android.entity.WineryInfo;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

public class DataHelper extends OrmLiteSqliteOpenHelper {


    private static final String DATABASE_NAME = "WineryBase.db";
    private static final int DATABASE_VERSION = 1;


    public DataHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, WineryInfo.class);
            TableUtils.createTable(connectionSource, FavoriteInfo.class);
            // insert init data
            db.beginTransaction();
            db.execSQL("INSERT INTO Winery_Info_T (lat, lng, keyValue, title) VALUES(53.558, 9.927,'try1','This is test 1')");
            db.execSQL("INSERT INTO Winery_Info_T (lat, lng, keyValue, title) VALUES(53.551, 9.993,'try2','This is test 2')");
            db.setTransactionSuccessful();
            db.endTransaction();
        } catch (SQLException e) {
            Log.e(DataHelper.class.getName(), "Create database failed!", e);
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        super.close();
    }

    /**
     * update db to version 1
     *
     * @param db
     */
    private void upgradeDatabaseToVersion1(SQLiteDatabase db, ConnectionSource connectionSource) throws SQLException {
        TableUtils.dropTable(connectionSource, WineryInfo.class, true);
    }

    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int currentVersion) {
        Log.w(LogConstants.TAG, "Upgrading database from version " + oldVersion
                + " to " + currentVersion + ".");

        switch (oldVersion) {
            case 0:
                if (currentVersion <= 1) {
                    return;
                }

                db.beginTransaction();
                try {
                    upgradeDatabaseToVersion1(db, connectionSource);
                    db.setTransactionSuccessful();
                } catch (Throwable ex) {
                    Log.e(LogConstants.TAG, ex.getMessage(), ex);
                    break;
                } finally {
                    db.endTransaction();
                }

                return;
        }

        Log.e(LogConstants.TAG, "Destroying all old data.");
        // should drop old data in here
        onCreate(db);

    }
}