package au.com.iglooit.winerymap.android.dbhelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import au.com.iglooit.winerymap.android.core.LOG.LogConstants;
import au.com.iglooit.winerymap.android.core.view.IIGTDataHelper;
import au.com.iglooit.winerymap.android.entity.FavoriteInfo;
import au.com.iglooit.winerymap.android.entity.WineryHistoryInfo;
import au.com.iglooit.winerymap.android.entity.WineryInfo;
import au.com.iglooit.winerymap.android.exception.AppX;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.HashMap;

public class DataHelper extends OrmLiteSqliteOpenHelper implements IIGTDataHelper
{


    private static final String DATABASE_NAME = "WineryBase.db";
    private static final int DATABASE_VERSION = 1;
    public static final String WINERY_INFO_DAO = "WINERY_INFO_DAO";
    public static final String FAVORITE_INFO_DAO = "FAVORITE_INFO_DAO";
    public static final String WINERY_HISTORY_INFO_DAO = "WINERY_HISTORY_INFO_DAO";
    private HashMap<String, Dao> daoMaps = null;


    public DataHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        initDaoMaps();
    }

    public void initDaoMaps()
    {
        daoMaps = new HashMap<String, Dao>();
        daoMaps.put(WINERY_INFO_DAO, null);
        daoMaps.put(FAVORITE_INFO_DAO, null);
        daoMaps.put(WINERY_HISTORY_INFO_DAO, null);
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource)
    {
        try
        {
            TableUtils.createTable(connectionSource, WineryInfo.class);
            TableUtils.createTable(connectionSource, FavoriteInfo.class);
            TableUtils.createTable(connectionSource, WineryHistoryInfo.class);
            // insert init data
            db.beginTransaction();
            db.execSQL("INSERT INTO Winery_Info_T (lat, lng, keyValue, title) VALUES(53.558, 9.927,'try1'," +
                "'This is test 1')");
            db.execSQL("INSERT INTO Winery_Info_T (lat, lng, keyValue, title) VALUES(53.551, 9.993,'try2'," +
                "'This is test 2')");
            db.setTransactionSuccessful();
            db.endTransaction();
        }
        catch (SQLException e)
        {
            Log.e(DataHelper.class.getName(), "Create database failed!", e);
            e.printStackTrace();
        }
    }

    @Override
    public void close()
    {
        super.close();
        daoMaps.clear();
    }

    /**
     * update db to version 1
     *
     * @param db
     */
    private void upgradeDatabaseToVersion1(SQLiteDatabase db, ConnectionSource connectionSource) throws SQLException
    {
        TableUtils.dropTable(connectionSource, WineryInfo.class, true);
    }

    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int currentVersion)
    {
        Log.w(LogConstants.TAG, "Upgrading database from version " + oldVersion
            + " to " + currentVersion + ".");

        switch (oldVersion)
        {
            case 0:
                if (currentVersion <= 1)
                {
                    return;
                }

                db.beginTransaction();
                try
                {
                    upgradeDatabaseToVersion1(db, connectionSource);
                    db.setTransactionSuccessful();
                }
                catch (Throwable ex)
                {
                    Log.e(LogConstants.TAG, ex.getMessage(), ex);
                    break;
                }
                finally
                {
                    db.endTransaction();
                }

                return;
        }

        Log.e(LogConstants.TAG, "Destroying all old data.");
        // should drop old data in here
        onCreate(db);

    }

    @SuppressWarnings("unchecked")
    public final Dao<WineryInfo, Integer> getWineryInfoDao()
    {
        Dao<WineryInfo, Integer> wineryInfoDao = daoMaps.get(WINERY_INFO_DAO);
        if (wineryInfoDao == null)
        {
            try
            {
                wineryInfoDao = getDao(WineryInfo.class);
                daoMaps.put(WINERY_INFO_DAO, wineryInfoDao);
            }
            catch (SQLException e)
            {
                throw new AppX(e.getMessage());
            }
        }
        return wineryInfoDao;
    }

    @SuppressWarnings("unchecked")
    public final Dao<FavoriteInfo, Integer> getFavoriteInfoDao()
    {
        Dao<FavoriteInfo, Integer> favoriteInfoDao = daoMaps.get(FAVORITE_INFO_DAO);
        if (favoriteInfoDao == null)
        {
            try
            {
                favoriteInfoDao = getDao(FavoriteInfo.class);
                daoMaps.put(FAVORITE_INFO_DAO, favoriteInfoDao);
            }
            catch (SQLException e)
            {
                throw new AppX(e.getMessage());
            }
        }
        return favoriteInfoDao;
    }

    @SuppressWarnings("unchecked")
    public final Dao<WineryHistoryInfo, Integer> getWineryHistoryInfoDao()
    {
        Dao<WineryHistoryInfo, Integer> wineryHistoryInfoDao = daoMaps.get(WINERY_HISTORY_INFO_DAO);
        if (wineryHistoryInfoDao == null)
        {
            try
            {
                wineryHistoryInfoDao = getDao(WineryHistoryInfo.class);
                daoMaps.put(FAVORITE_INFO_DAO, wineryHistoryInfoDao);
            }
            catch (SQLException e)
            {
                throw new AppX(e.getMessage());
            }
        }
        return wineryHistoryInfoDao;
    }
}