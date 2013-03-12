package au.com.iglooit.winerymap.android.view.home;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.location.Criteria;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import au.com.iglooit.winerymap.android.R;
import au.com.iglooit.winerymap.android.core.navigation.Direction;
import au.com.iglooit.winerymap.android.core.navigation.Kit;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import org.json.JSONObject;

import java.util.List;

public class MapSearchFragment extends Fragment
{
    static final LatLng HAMBURG = new LatLng(53.558, 9.927);
    static final LatLng KIEL = new LatLng(53.551, 9.993);
    private GoogleMap mMap;
    private AQuery aq;
    private ProgressDialog progressDialog;

    private final String URL = "http://maps.google.com/maps/api/directions/json?origin=" + HAMBURG
        + "&destination=" + KIEL.toString()
        + "&mode=driving"
        + "&sensor=false";

    public static Fragment newInstance(Context context)
    {
        MapSearchFragment f = new MapSearchFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle)
    {
        ViewGroup root = (ViewGroup)layoutInflater.inflate(R.layout.wm_home_map_search_page, null);
        aq = new AQuery(root);
        setUpMapIfNeeded();
        return root;
    }

    @Override
    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);

    }

    private void setUpMapIfNeeded()
    {
        // Do a null check to confirm that we have not already instantiated the mMap.
        if (mMap == null)
        {
            // Try to obtain the mMap from the SupportMapFragment.
            mMap = ((SupportMapFragment)this.getActivity().getSupportFragmentManager().findFragmentById(R.id.map))
                .getMap();
            // Check if we were successful in obtaining the mMap.
            if (mMap != null)
            {
                setUpMap();
            }
        }
    }

    public void jsonCallback(String url, JSONObject json, AjaxStatus status)
    {

        if (json != null)
        {
            Gson gson = new Gson();
            Direction direction = gson.fromJson(json.toString(), Direction.class);
            if (direction.getStatus().equals("OK"))
            {
                List<LatLng> path = Kit.decodePoly2(direction.getRoutes().get(0).getOverview_polyline().getPoints());
                mMap.addPolyline((new PolylineOptions())
                    .add(path.toArray(new LatLng[path.size()]))
                    .width(5)
                    .color(Color.BLUE)
                    .geodesic(true));
            }
        }
        else
        {
            //ajax error
        }
        progressDialog.dismiss();

    }

    private void setUpMap()
    {
        Marker hamburg = mMap.addMarker(new MarkerOptions().position(HAMBURG)
            .title("Hamburg"));
        Marker kiel = mMap.addMarker(new MarkerOptions()
            .position(KIEL)
            .title("Kiel")
            .snippet("Kiel is cool")
            .icon(BitmapDescriptorFactory
                .fromResource(R.drawable.ic_launcher)));

        LocationManager service = (LocationManager)this.getActivity().getSystemService(this.getActivity()
            .LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = service.getBestProvider(criteria, false);
        //            Location location = service.getLastKnownLocation(provider);
        mMap.setMyLocationEnabled(true);

        // change button position
        // Find ZoomControl view
        SupportMapFragment mapFragment = (SupportMapFragment) getFragmentManager().findFragmentById(R.id.map);
        View zoomControls = mapFragment.getView().findViewById(0x1);

        if (zoomControls != null && zoomControls.getLayoutParams() instanceof RelativeLayout.LayoutParams) {
            // ZoomControl is inside of RelativeLayout
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) zoomControls.getLayoutParams();

            // Align it to - parent top|left
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

            // Update margins, set to 10dp
            final int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10,
                getResources().getDisplayMetrics());
            params.setMargins(margin, margin, margin, margin);
        }

        //Move the camera instantly to hamburg with a zoom of 15.
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(HAMBURG, 15));

        // Zoom in, animating the camera.
        mMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);

        // get driction
        aq.ajax(urlBuilder(HAMBURG, KIEL), JSONObject.class, this, "jsonCallback");
        this.progressDialog = ProgressDialog.show(this.getActivity(), "working . . .", "performing HTTP request");
    }

    public String urlBuilder(LatLng startPosition, LatLng destination)
    {
        StringBuilder builder = new StringBuilder("http://maps.google.com/maps/api/directions/json?origin=");
        builder.append(convertToParam(startPosition));
        builder.append("&destination=");
        builder.append(convertToParam(destination));
        builder.append("&mode=driving");
        builder.append("&sensor=false");
        return builder.toString();
    }

    public String convertToParam(LatLng position)
    {
        return "(" + position.latitude + "," + position.longitude + ")";
    }
}
