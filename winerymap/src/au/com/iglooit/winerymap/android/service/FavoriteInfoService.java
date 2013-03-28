package au.com.iglooit.winerymap.android.service;

import au.com.iglooit.winerymap.android.entity.FavoriteInfo;
import au.com.iglooit.winerymap.android.entity.WineryInfo;

import java.util.List;

public interface FavoriteInfoService
{
    List<WineryInfo> findMyFavorite(Long offset, Long maxRows);

    FavoriteInfo create(FavoriteInfo info);
}
