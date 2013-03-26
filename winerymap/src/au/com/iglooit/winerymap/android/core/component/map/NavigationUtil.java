package au.com.iglooit.winerymap.android.core.component.map;

import au.com.iglooit.winerymap.android.core.navigation.Direction;
import au.com.iglooit.winerymap.android.core.navigation.Kit;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NavigationUtil
{
    public static String urlBuilder(LatLng startPosition, LatLng destination)
    {
        StringBuilder builder = new StringBuilder("http://maps.google.com/maps/api/directions/json?origin=");
        builder.append(convertToParam(startPosition));
        builder.append("&destination=");
        builder.append(convertToParam(destination));
        builder.append("&mode=driving");
        builder.append("&sensor=false");
        return builder.toString();
    }

    private static String convertToParam(LatLng position)
    {
        return "(" + position.latitude + "," + position.longitude + ")";
    }

    public static List<LatLng> convertToPath(JSONObject json)
    {
        Gson gson = new Gson();
        Direction direction = gson.fromJson(json.toString(), Direction.class);
        if (direction.getStatus().equals("OK"))
        {
            return Kit.decodePoly2(direction.getRoutes().get(0).getOverview_polyline().getPoints());
        }
        else
        {
            return new ArrayList<LatLng>();
        }
    }
}
