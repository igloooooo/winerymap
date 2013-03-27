package au.com.iglooit.winerymap.android.dbhelper;

import android.content.Context;
import au.com.iglooit.winerymap.android.entity.FavoriteInfo;
import au.com.iglooit.winerymap.android.entity.WineryInfo;
import au.com.iglooit.winerymap.android.exception.AppX;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * winerymap
 * User: Nicholas Zhu
 * Date: 13-3-27
 * Time: 8:18PM
 */
public final class FavoriteInfoHelper extends DataHelper {
    private Dao<FavoriteInfo, Integer> favoriteInfoDao = null;

    public FavoriteInfoHelper(Context context) {
        super(context);
    }

    public final Dao<FavoriteInfo, Integer> getFavoriteDataDao() throws SQLException {
        if (favoriteInfoDao == null) {
            favoriteInfoDao = getDao(FavoriteInfo.class);
        }
        return favoriteInfoDao;
    }

    public final List<WineryInfo> findMyFavorite(Long offset, Long maxRows) {
        try {
            QueryBuilder qb = getFavoriteDataDao().queryBuilder();
            if (offset != null) {
                qb.offset(offset);
            }
            if (maxRows != null) {
                qb.limit(maxRows);
            }
            List<FavoriteInfo> result = qb.query();
            if (result != null && result.size() > 0) {
                List<WineryInfo> wineryInfoList = new ArrayList<WineryInfo>();
                for (FavoriteInfo info : result) {
                    wineryInfoList.add(info.wineryInfo);
                }
                return wineryInfoList;
            } else
                return new ArrayList<WineryInfo>();
        } catch (SQLException e) {
            throw new AppX(e.getMessage());
        }
    }
}
