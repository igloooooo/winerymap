package au.com.iglooit.winerymap.android.dbhelper;

import android.content.Context;
import android.database.Cursor;
import au.com.iglooit.winerymap.android.entity.WineryInfo;
import au.com.iglooit.winerymap.android.exception.AppX;
import com.j256.ormlite.android.AndroidDatabaseResults;
import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.List;

public class WineryInfoHelper extends DataHelper {

    private Dao<WineryInfo, Integer> wineryDao = null;

    public static final String TITLE = "title";

    public WineryInfoHelper(Context context) {
        super(context);
    }

    public Dao<WineryInfo, Integer> getWineryDataDao() throws SQLException {
        if (wineryDao == null) {
            wineryDao = getDao(WineryInfo.class);
        }
        return wineryDao;
    }

    public List<WineryInfo> findWineryByName(String name) {
        try {
            QueryBuilder qb = getWineryDataDao().queryBuilder();
            qb.where().like("title", name + "%");
            return qb.query();
        } catch (SQLException e) {
            throw new AppX(e.getMessage());
        }
    }

    public Cursor findWineryForSearch(String name) {
        CloseableIterator<WineryInfo> iterator = null;
        Cursor cursor = null;
        try {
            QueryBuilder qb = getWineryDataDao().queryBuilder();
            qb.where().like("title", "%" + name + "%");
            iterator = getWineryDataDao().iterator(qb.prepare());
            // get the raw results which can be cast under Android
            AndroidDatabaseResults results = (AndroidDatabaseResults) iterator.getRawResults();
            cursor = results.getRawCursor();
        } catch (SQLException e) {
            throw new AppX(e.getMessage());
        } finally {
//            if (iterator != null)
//                iterator.closeQuietly();
        }
        return cursor;
    }

    public WineryInfo findWineryById(int id)
    {
        try {
            QueryBuilder qb = getWineryDataDao().queryBuilder();
            qb.where().eq("id", id);
            List<WineryInfo> result = qb.query();
            if (result != null && result.size() > 0)
            {
                return result.get(0);
            }
            else
            {
                return null;
            }
        } catch (SQLException e) {
            throw new AppX(e.getMessage());
        } finally {

        }
    }

    @Override
    public void close() {
        super.close();
        wineryDao = null;
    }
}
