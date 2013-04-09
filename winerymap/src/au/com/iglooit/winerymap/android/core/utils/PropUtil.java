package au.com.iglooit.winerymap.android.core.utils;

import android.content.Context;

import java.io.FileInputStream;
import java.util.Properties;

public final class PropUtil
{
    public Properties loadConfig(Context context, String file)
    {
        Properties properties = new Properties();
        try
        {
            FileInputStream s = new FileInputStream(file);
            properties.load(s);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
        return properties;
    }
}
