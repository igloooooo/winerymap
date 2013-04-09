package au.com.iglooit.winerymap.android.entity;

import java.io.Serializable;
import java.util.Date;

public class NewsInfo implements Serializable
{
    public String title;
    public Date time;
    public String content;
    public String description;

    public NewsInfo()
    {

    }
}
