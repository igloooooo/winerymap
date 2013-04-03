package au.com.iglooit.winerymap.android.service;

import au.com.iglooit.winerymap.android.entity.WineryHistoryInfo;

import java.util.List;

public interface WineryHistoryInfoService
{
    List<WineryHistoryInfo> findAll();
}
