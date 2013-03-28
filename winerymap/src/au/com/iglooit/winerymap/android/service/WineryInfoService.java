package au.com.iglooit.winerymap.android.service;

import android.database.Cursor;
import au.com.iglooit.winerymap.android.entity.WineryInfo;

import java.util.List;

public interface WineryInfoService
{
    List<WineryInfo> findWineryByName(String name);

    Cursor findWineryForSearch(String name);

    WineryInfo findWineryById(int id);

    List<WineryInfo> findWineryByIds(int[] ids);
}
