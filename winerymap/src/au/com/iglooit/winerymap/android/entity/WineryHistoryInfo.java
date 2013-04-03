package au.com.iglooit.winerymap.android.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.Date;

@DatabaseTable(tableName = "History_Info_T")
public class WineryHistoryInfo implements Serializable
{
    @DatabaseField(columnName = "_id", generatedId = true)
    public int id;
    @DatabaseField(canBeNull = false, foreign = true)
    public WineryInfo wineryInfo;
    @DatabaseField
    public Date createDate;

    public WineryHistoryInfo()
    {
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("Id is ").append(id);
        builder.append("; createDate:").append(createDate);
        builder.append("; wineryInfo:").append(wineryInfo.toString());
        return builder.toString();
    }
}
