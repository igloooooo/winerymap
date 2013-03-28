package au.com.iglooit.winerymap.android.service;

import au.com.iglooit.winerymap.android.entity.FavoriteInfo;
import au.com.iglooit.winerymap.android.entity.WineryInfo;
import au.com.iglooit.winerymap.android.exception.AppX;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FavoriteInfoServiceImpl implements FavoriteInfoService
{
    private Dao<FavoriteInfo, Integer> favoriteInfoDao = null;

    public FavoriteInfoServiceImpl(Dao<FavoriteInfo, Integer> favoriteInfoDao)
    {
        this.favoriteInfoDao = favoriteInfoDao;
    }

    public final List<WineryInfo> findMyFavorite(final Long offset, final Long maxRows)
    {
        try
        {
            QueryBuilder qb = favoriteInfoDao.queryBuilder();
            if (offset != null)
            {
                qb.offset(offset);
            }
            if (maxRows != null)
            {
                qb.limit(maxRows);
            }
            List<FavoriteInfo> result = qb.query();
            if (result != null && result.size() > 0)
            {
                List<WineryInfo> wineryInfoList = new ArrayList<WineryInfo>();
                for (FavoriteInfo info : result)
                {
                    wineryInfoList.add(info.wineryInfo);
                }
                return wineryInfoList;
            }
            else
                return new ArrayList<WineryInfo>();
        }
        catch (SQLException e)
        {
            throw new AppX(e.getMessage());
        }
    }

    public final FavoriteInfo create(FavoriteInfo info)
    {
        try
        {
            favoriteInfoDao.create(info);
        }
        catch (SQLException e)
        {
            throw new AppX(e.getMessage());
        }
        return info;
    }
}
