package au.com.iglooit.winerymap.android.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * winerymap
 * User: Nicholas Zhu
 * Date: 13-3-27
 * Time: 8:11PM
 */
@DatabaseTable(tableName = "Favorite_Info_T")
public class FavoriteInfo implements Serializable {
    @DatabaseField(columnName = "_id", generatedId = true)
    public int id;
    @DatabaseField(canBeNull = false, foreign = true)
    public WineryInfo wineryInfo;

    public FavoriteInfo()
    {

    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("Id is ").append(id);
        builder.append("; winery id is ").append(wineryInfo.toString());
        return builder.toString();
    }
}
