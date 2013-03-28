package au.com.iglooit.winerymap.android.service;

import android.database.Cursor;
import au.com.iglooit.winerymap.android.entity.WineryInfo;
import au.com.iglooit.winerymap.android.exception.AppX;
import com.j256.ormlite.android.AndroidDatabaseResults;
import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WineryInfoServiceImpl implements WineryInfoService
{
    private Dao<WineryInfo, Integer> wineryInfoDao = null;

    public WineryInfoServiceImpl(Dao<WineryInfo, Integer> wineryInfoDao)
    {
        this.wineryInfoDao = wineryInfoDao;
    }

    public final List<WineryInfo> findWineryByName(final String name)
    {
        try
        {
            QueryBuilder qb = wineryInfoDao.queryBuilder();
            qb.where().like("title", name + "%");
            return qb.query();
        }
        catch (SQLException e)
        {
            throw new AppX(e.getMessage());
        }
    }

    public final Cursor findWineryForSearch(final String name)
    {
        CloseableIterator<WineryInfo> iterator = null;
        Cursor cursor = null;
        try
        {
            QueryBuilder qb = wineryInfoDao.queryBuilder();
            qb.where().like("title", "%" + name + "%");
            iterator = wineryInfoDao.iterator(qb.prepare());
            // get the raw results which can be cast under Android
            AndroidDatabaseResults results = (AndroidDatabaseResults)iterator.getRawResults();
            cursor = results.getRawCursor();
        }
        catch (SQLException e)
        {
            throw new AppX(e.getMessage());
        }
        finally
        {
            //            if (iterator != null)
            //                iterator.closeQuietly();
        }
        return cursor;
    }

    public final WineryInfo findWineryById(final int id)
    {
        try
        {
            QueryBuilder qb = wineryInfoDao.queryBuilder();
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
        }
        catch (SQLException e)
        {
            throw new AppX(e.getMessage());
        }
        finally
        {

        }
    }

    public final List<WineryInfo> findWineryByIds(int[] ids)
    {
        try
        {
            QueryBuilder qb = wineryInfoDao.queryBuilder();
            qb.where().in("id", ids);
            List<WineryInfo> result = qb.query();
            if (result != null && result.size() > 0)
            {
                return result;
            }
            else
            {
                return new ArrayList<WineryInfo>();
            }
        }
        catch (SQLException e)
        {
            throw new AppX(e.getMessage());
        }
        finally
        {

        }
    }
}
