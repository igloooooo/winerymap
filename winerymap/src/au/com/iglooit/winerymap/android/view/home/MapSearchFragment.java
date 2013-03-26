package au.com.iglooit.winerymap.android.view.home;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import au.com.iglooit.winerymap.android.R;
import au.com.iglooit.winerymap.android.core.component.map.IGTZoomControl;
import au.com.iglooit.winerymap.android.core.component.map.NavigationUtil;
import au.com.iglooit.winerymap.android.core.component.poimenu.AnimationFactory;
import au.com.iglooit.winerymap.android.core.component.poimenu.MenuItemAnimationType;
import au.com.iglooit.winerymap.android.core.component.poimenu.POIMenuImageView;
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
import org.json.JSONObject;

import java.util.List;

public class MapSearchFragment extends Fragment implements GoogleMap.OnMarkerClickListener, View.OnClickListener
{
    private String tag = "MapSearchFragment";
    static final LatLng HAMBURG = new LatLng(53.558, 9.927);
    static final LatLng KIEL = new LatLng(53.551, 9.993);

    private GoogleMap mMap;
    private AQuery aq;
    private ProgressDialog progressDialog;
    private Activity parentActivity;

    private POIMenuImageView addToMyFavourite;
    private TranslateAnimation addToMyFavouriteOutTA;//
    private TranslateAnimation addToMyFavouriteOutTA2;
    private TranslateAnimation addToMyFavouriteInTA;

    private POIMenuImageView navigateTo;
    private TranslateAnimation navigateToOutTA;
    private TranslateAnimation navigateToOutTA2;
    private TranslateAnimation navigateToInTA;

    private POIMenuImageView showDetails;
    private TranslateAnimation showDetailsOutTA;
    private TranslateAnimation showDetailsOutTA2;
    private TranslateAnimation showDetailsInTA;

    private ImageViewAnimationCallBack imageViewAnimationCallBack;
    private boolean isClockwise = true;
    private AnimationSet animationScaleAnimation;
    private Fragment searchBar;

    private ViewGroup root;

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
        searchBar = this.getActivity().getSupportFragmentManager().findFragmentById(R.id.searchBar);
        // File Templates.
        setUpMapIfNeeded();
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
                initAnimationView();
            }
        }
    }

    private void setUpMap()
    {
        mMap.setOnMarkerClickListener(this);
        Marker hamburg = mMap.addMarker(new MarkerOptions().position(HAMBURG)
            .title("Hamburg"));
        hamburg.hideInfoWindow();
        Marker kiel = mMap.addMarker(new MarkerOptions()
            .position(KIEL)
            .title("Kiel")
            .icon(BitmapDescriptorFactory
                .fromResource(R.drawable.ic_launcher)));

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

        // get driction
        aq.ajax(NavigationUtil.urlBuilder(HAMBURG, KIEL), JSONObject.class, this, "navigationCallback");
        this.progressDialog = ProgressDialog.show(this.getActivity(), "Get navigation data . . .",
            "performing HTTP request");

        imageViewAnimationCallBack = new ImageViewAnimationCallBack();
        animationScaleAnimation = (AnimationSet)AnimationUtils.loadAnimation(
            parentActivity, R.anim.scaleset);
        animationScaleAnimation.setAnimationListener(new Animation.AnimationListener()
        {
            @Override
            public void onAnimationStart(Animation animation)
            {

            }

            @Override
            public void onAnimationEnd(Animation animation)
            {

            }

            @Override
            public void onAnimationRepeat(Animation animation)
            {

            }
        });
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
    public boolean onMarkerClick(Marker marker)
    {
        Point position = mMap.getProjection().toScreenLocation(marker.getPosition());
        if (isClockwise)
        {
            Point basePoint = new Point(position.x, position.y + searchBar.getView().getHeight());


            Point cameraPoint = new Point(basePoint.x, basePoint.y - POIMenuImageView.RADIUS);
            addToMyFavourite.setLocation(cameraPoint);
            addToMyFavourite.startAnimation(addToMyFavouriteOutTA);
            addToMyFavourite.setVisibility(View.VISIBLE);

            Point ivWith = new Point(basePoint.x + POIMenuImageView.getOffset(),
                basePoint.y - POIMenuImageView.getOffset());
            navigateToOutTA.setStartOffset(20);
            navigateTo.setLocation(ivWith);
            navigateTo.startAnimation(navigateToOutTA);
            navigateTo.setVisibility(View.VISIBLE);

            showDetailsOutTA.setStartOffset(80);
            Point ivThoughtPoint = new Point(basePoint.x + POIMenuImageView.RADIUS, basePoint.y);
            showDetails.setLocation(ivThoughtPoint);
            showDetails.startAnimation(showDetailsOutTA);
            showDetails.setVisibility(View.VISIBLE);
            isClockwise = false;

        }
        else
        {
            showDetailsInTA.setStartOffset(20);
            showDetails.startAnimation(showDetailsInTA);
            showDetails.setVisibility(View.GONE);

            navigateToInTA.setStartOffset(80);
            navigateTo.startAnimation(navigateToInTA);
            navigateTo.setVisibility(View.GONE);

            addToMyFavouriteInTA.setStartOffset(100);
            addToMyFavourite.startAnimation(addToMyFavouriteInTA);
            addToMyFavourite.setVisibility(View.GONE);
            isClockwise = true;
        }
        return true;
    }

    private void initAnimationView()
    {
        addToMyFavourite = (POIMenuImageView)root.findViewById(R.id.ivCamera);
//        addToMyFavourite.setOnClickListener(this);
        addToMyFavourite.setAnimationCallBack(imageViewAnimationCallBack);

        addToMyFavouriteOutTA = AnimationFactory.newInstance().getAnimation(MenuItemAnimationType.CameraOut1);
        addToMyFavouriteOutTA2 = AnimationFactory.newInstance().getAnimation(MenuItemAnimationType.CameraOut2);
        addToMyFavouriteInTA = AnimationFactory.newInstance().getAnimation(MenuItemAnimationType.CameraIn);

        navigateTo = (POIMenuImageView)root.findViewById(R.id.ivWith);
//        navigateTo.setOnClickListener(this);
        navigateTo.setAnimationCallBack(imageViewAnimationCallBack);

        navigateToOutTA = AnimationFactory.newInstance().getAnimation(MenuItemAnimationType.PlaceOut1);
        navigateToOutTA2 = AnimationFactory.newInstance().getAnimation(MenuItemAnimationType.PlaceOut2);
        navigateToInTA = AnimationFactory.newInstance().getAnimation(MenuItemAnimationType.PlaceIn);

        showDetails = (POIMenuImageView)root.findViewById(R.id.ivThought);
//        showDetails.setOnClickListener(this);
        showDetails.setAnimationCallBack(imageViewAnimationCallBack);

        showDetailsOutTA = AnimationFactory.newInstance().getAnimation(MenuItemAnimationType.ThoughtOut1);
        showDetailsOutTA2 = AnimationFactory.newInstance().getAnimation(MenuItemAnimationType.ThoughtOut2);
        showDetailsInTA = AnimationFactory.newInstance().getAnimation(MenuItemAnimationType.ThoughtIn);
    }

    class ImageViewAnimationCallBack implements POIMenuImageView.AnimationCallBack
    {
        @Override
        public void onStartAnimation(Animation animation, View v)
        {
        }

        @Override
        public void onEndAnimation(Animation animation, View v)
        {
            if (animation.equals(addToMyFavouriteOutTA))
            {
                Log.i(tag, "addToMyFavouriteOutTA");
                addToMyFavourite.startAnimation2(addToMyFavouriteOutTA2, 10);
            }
            else if (animation.equals(navigateToOutTA))
            {
                Log.i(tag, "navigateToOutTA");
                navigateTo.startAnimation2(navigateToOutTA2, 0);
            }
            else if (animation.equals(showDetailsOutTA))
            {
                Log.i(tag, "showDetailsOutTA");
                showDetails.startAnimation2(showDetailsOutTA2, 0);
            }
            else
            {
                Log.i(tag, animation.toString());
                Log.i(tag, v.toString());
            }
        }

        @Override
        public void onRepeatAnimation(Animation animation, View v)
        {
        }

    }

    @Override
    public void onClick(View v)
    {
//        switch (v.getId())
//        {
//            case R.id.ivCamera:
//                addToMyFavourite.startAnimation(animationScaleAnimation);
//                break;
//            case R.id.ivWith:
//                navigateTo.startAnimation(animationScaleAnimation);
//                break;
//            case R.id.ivThought:
//                showDetails.startAnimation(animationScaleAnimation);
//                break;
//
//        }
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
}
