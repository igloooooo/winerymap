package au.com.iglooit.winerymap.android.view.home;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Criteria;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import au.com.iglooit.winerymap.android.R;
import au.com.iglooit.winerymap.android.core.component.map.IGTZoomControl;
import au.com.iglooit.winerymap.android.core.component.map.NavigationUtil;
import au.com.iglooit.winerymap.android.core.component.poimenu.POIMenu;
import au.com.iglooit.winerymap.android.dbhelper.DataHelper;
import au.com.iglooit.winerymap.android.entity.FavoriteInfo;
import au.com.iglooit.winerymap.android.entity.WineryInfo;
import au.com.iglooit.winerymap.android.service.FavoriteInfoService;
import au.com.iglooit.winerymap.android.service.FavoriteInfoServiceImpl;
import au.com.iglooit.winerymap.android.service.WineryInfoService;
import au.com.iglooit.winerymap.android.service.WineryInfoServiceImpl;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class MapSearchFragment extends Fragment implements GoogleMap.OnMarkerClickListener
{
    static final LatLng HAMBURG = new LatLng(53.558, 9.927);
    static final LatLng KIEL = new LatLng(53.551, 9.993);
    private String tag = "MapSearchFragment";
    private DataHelper dataHelper = null;
    private GoogleMap mMap;
    private AQuery aq;
    private ProgressDialog progressDialog;
    private Activity parentActivity;
    private boolean isMenuClosed = true;
    private SearchBarFragment searchBar;
    private POIMenu poiMenu;
    private ViewGroup root;
    private Marker currentMarker;
    private HashMap<Marker, WineryInfo> markerMap = new HashMap<Marker, WineryInfo>();
    private WineryInfoService wineryInfoService;
    private FavoriteInfoService favoriteInfoService;

    public static Fragment newInstance(Context context)
    {
        MapSearchFragment f = new MapSearchFragment();
        return f;
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        parentActivity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle)
    {
        root = (ViewGroup)layoutInflater.inflate(R.layout.wm_home_map_search_fragment, viewGroup, false);
        aq = new AQuery(root);
        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);    //To change body of overridden methods use File | Settings |
        searchBar = (SearchBarFragment)this.getActivity().getSupportFragmentManager().findFragmentById(R.id.searchBar);
        poiMenu = (POIMenu)this.getActivity().findViewById(R.id.poi_menu);
        // File Templates.
        setUpMapIfNeeded();
        setUpMenu();
        setUpSearchBar();
        initDao();
    }

    private void initDao()
    {
        wineryInfoService = new WineryInfoServiceImpl(getDataHelper().getWineryInfoDao());
        favoriteInfoService = new FavoriteInfoServiceImpl(getDataHelper().getFavoriteInfoDao());

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
//                initAnimationView();
            }
        }
    }

    private void setUpMap()
    {
        mMap.setOnMarkerClickListener(this);
//        Marker hamburg = mMap.addMarker(new MarkerOptions().position(HAMBURG)
//                .title("Hamburg"));
//        hamburg.hideInfoWindow();
//        Marker kiel = mMap.addMarker(new MarkerOptions()
//                .position(KIEL)
//                .title("Kiel")
//                .icon(BitmapDescriptorFactory
//                        .fromResource(R.drawable.ic_launcher)));

        LocationManager service = (LocationManager)this.getActivity().getSystemService(this.getActivity()
            .LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = service.getBestProvider(criteria, false);
        //            Location location = service.getLastKnownLocation(provider);
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(false);

        // setup zoomcontrol
        setUpZoomControl();

        //Move the camera instantly to hamburg with a zoom of 15.
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(HAMBURG, 15));

        // Zoom in, animating the camera.
        mMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener()
        {
            @Override
            public void onCameraChange(CameraPosition cameraPosition)
            {
                if (!isMenuClosed)
                {
                    currentMarker = null;
                    poiMenu.hideMenuItems();
                    isMenuClosed = true;
                }
            }
        });

        // get driction
//        aq.ajax(NavigationUtil.urlBuilder(HAMBURG, KIEL), JSONObject.class, this, "navigationCallback");
//        this.progressDialog = ProgressDialog.show(this.getActivity(), "Get navigation data . . .",
//                "performing HTTP request");
    }

    private void setUpZoomControl()
    {
        IGTZoomControl zoomControl = (IGTZoomControl)this.getActivity().findViewById(R.id.map_controller);
        zoomControl.addListeners(new IGTZoomControl.IGTZoomControlListener()
        {
            @Override
            public void onClickZoomIn(View view)
            {
                mMap.moveCamera(CameraUpdateFactory.zoomIn());
            }

            @Override
            public void onClickZoomOut(View view)
            {
                mMap.moveCamera(CameraUpdateFactory.zoomOut());
            }

            @Override
            public void onClickShowWinery(View view)
            {

            }
        });
    }

    @Override
    public boolean onMarkerClick(final Marker marker)
    {
        final Point position = mMap.getProjection().toScreenLocation(marker.getPosition());
        if (isMenuClosed)
        {
            currentMarker = marker;
            if (mMap.getCameraPosition().target.latitude != marker.getPosition().latitude
                && mMap.getCameraPosition().target.longitude != marker.getPosition().longitude)
            {
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                    CameraPosition.fromLatLngZoom(marker.getPosition(), 10)), new GoogleMap.CancelableCallback()
                {
                    @Override
                    public void onFinish()
                    {
                        getActivity().runOnUiThread(new Runnable()
                        {
                            public void run()
                            {
                                Point basePoint = new Point(position.x, position.y + searchBar.getView().getHeight());
                                poiMenu.showMenuItems(basePoint);
                                isMenuClosed = false;
                            }
                        });
                    }

                    @Override
                    public void onCancel()
                    {
                    }
                });
            }
            else
            {
                Point basePoint = new Point(position.x, position.y + searchBar.getView().getHeight());
                poiMenu.showMenuItems(basePoint);
                isMenuClosed = false;
            }


        }
        else
        {
            currentMarker = null;
            poiMenu.hideMenuItems();
            isMenuClosed = true;
        }
        return true;
    }

    public void navigationCallback(String url, JSONObject json, AjaxStatus status)
    {

        if (json != null)
        {
            List<LatLng> path = NavigationUtil.convertToPath(json);
            if (path.size() > 0)
            {
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

    private void setUpMenu()
    {
        poiMenu.clickMenuItemListener = new POIMenu.ClickMenuItemListener()
        {
            @Override
            public void onShowDetails(View v)
            {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void onAddToMyFavourite(View v)
            {
                if (currentMarker != null)
                {
                    new AlertDialog.Builder(MapSearchFragment.this.getActivity()).setIcon(
                        R.drawable.main_about_normal).setTitle("Add into favorite")
                        .setMessage("The winery will be added into favorite").setNegativeButton("I'm sure.",
                        new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                WineryInfo wineryInfo = markerMap.get(currentMarker);
                                FavoriteInfo favoriteInfo = new FavoriteInfo();
                                favoriteInfo.wineryInfo = wineryInfo;
                                favoriteInfoService.create(favoriteInfo);
                            }
                        }).setPositiveButton("No",
                        new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int whichButton)
                            {

                            }
                        }).show();
                }
                else
                {

                }
            }

            @Override
            public void onNavigationTo(View v)
            {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        };
    }

    private void setUpSearchBar()
    {
        searchBar.listener = new SearchBarFragment.SearchBarFragmentListener()
        {
            @Override
            public void onSearchButton(String content)
            {
                markerMap.clear();
                List<WineryInfo> resultList = wineryInfoService.findWineryByName(content);
                if (resultList != null && resultList.size() > 0)
                {
                    WineryInfo info = resultList.get(0);
                    Marker marker = mMap.addMarker(new MarkerOptions().position(new LatLng(info.lat,
                        info.lng)).title(info.title));
                    markerMap.put(marker, info);
                }

            }

            @Override
            public void onTextViewEnter(String content)
            {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        };
    }

    private DataHelper getDataHelper()
    {
        if (dataHelper == null)
        {
            dataHelper =
                OpenHelperManager.getHelper(parentActivity, DataHelper.class);
        }
        return dataHelper;
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if (dataHelper != null)
        {
            OpenHelperManager.releaseHelper();
            dataHelper = null;
        }
    }

}
