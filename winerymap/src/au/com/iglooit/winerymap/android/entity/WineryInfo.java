package au.com.iglooit.winerymap.android.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable(tableName = "Winery_Info_T")
public class WineryInfo implements Serializable
{
    @DatabaseField(columnName = "_id", generatedId = true)
    public int id;
    @DatabaseField
    public double lat;
    @DatabaseField
    public double lng;
    @DatabaseField
    public String keyValue;
    @DatabaseField
    public String title;

    public WineryInfo()
    {

    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("Id is ").append(id);
        builder.append("; Lat:").append(lat);
        builder.append("; Lng:").append(lng);
        return builder.toString();
    }
}
