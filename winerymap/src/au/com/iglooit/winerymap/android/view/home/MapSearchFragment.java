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
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;
import au.com.iglooit.winerymap.android.R;
import au.com.iglooit.winerymap.android.core.component.poimenu.POIMenuImageView;
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

public class MapSearchFragment extends Fragment implements GoogleMap.OnMarkerClickListener, View.OnClickListener
{
    private String tag = "MapSearchFragment";
    static final LatLng HAMBURG = new LatLng(53.558, 9.927);
    static final LatLng KIEL = new LatLng(53.551, 9.993);
    private GoogleMap mMap;
    private AQuery aq;
    private ProgressDialog progressDialog;
    private Activity parentActivity;

    private POIMenuImageView mImageViewCamera;
    private TranslateAnimation cameraOutTA;//
    private TranslateAnimation cameraOutTA2;
    private TranslateAnimation cameraInTA;

    private POIMenuImageView mImageViewivWith;
    private TranslateAnimation withOutTA;
    private TranslateAnimation withOutTA2;
    private TranslateAnimation withInTA;

    private POIMenuImageView mImageViewivThought;
    private TranslateAnimation thoughtOutTA;
    private TranslateAnimation thoughtOutTA2;
    private TranslateAnimation thoughtInTA;

    private final int mIntDisApear = -1;
    private final int mIntDoDraw = 1;// drawPaint
    private boolean canHideClock = true;
    private int mIntAnimationDuration = 200;

    private int mIntInsWidth;
    private int mIntInsHeight;
    private ImageViewAnimationCallBack mIImageViewAnimationCallBack;
    private boolean isClockwise = true;
    private AnimationSet mAnimationScaleAnimation;
    private Fragment searchBar;

    private ViewGroup root;

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
        mMap.setOnMarkerClickListener(this);
        Marker hamburg = mMap.addMarker(new MarkerOptions().position(HAMBURG)
            .title("Hamburg"));
        hamburg.hideInfoWindow();
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
        SupportMapFragment mapFragment = (SupportMapFragment)getFragmentManager().findFragmentById(R.id.map);
        View zoomControls = mapFragment.getView().findViewById(0x1);

        if (zoomControls != null && zoomControls.getLayoutParams() instanceof RelativeLayout.LayoutParams)
        {
            // ZoomControl is inside of RelativeLayout
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)zoomControls.getLayoutParams();

            // Align it to - parent top|left
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

            // Update margins, set to 10dp
            final int margin = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10,
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

        mIImageViewAnimationCallBack = new ImageViewAnimationCallBack();
        mAnimationScaleAnimation = (AnimationSet)AnimationUtils.loadAnimation(
            parentActivity, R.anim.scaleset);
    }

    private String urlBuilder(LatLng startPosition, LatLng destination)
    {
        StringBuilder builder = new StringBuilder("http://maps.google.com/maps/api/directions/json?origin=");
        builder.append(convertToParam(startPosition));
        builder.append("&destination=");
        builder.append(convertToParam(destination));
        builder.append("&mode=driving");
        builder.append("&sensor=false");
        return builder.toString();
    }

    private String convertToParam(LatLng position)
    {
        return "(" + position.latitude + "," + position.longitude + ")";
    }

    @Override
    public boolean onMarkerClick(Marker marker)
    {
        Point position = mMap.getProjection().toScreenLocation(marker.getPosition());
        if (isClockwise)
        {
            Point basePoint = position;
            // parent position?
            cameraOutTA = new TranslateAnimation(0, 0, 100, 20);
            cameraOutTA.setDuration(mIntAnimationDuration);

            cameraOutTA2 = new TranslateAnimation(0, 0, 20, 0);
            cameraOutTA2.setDuration(100);


            cameraInTA = new TranslateAnimation(0, 0, 0, 100);
            cameraInTA.setDuration(mIntAnimationDuration);
            position.y = position.y + searchBar.getView().getHeight() - 100;
            mImageViewCamera.setLocation(position);
            mImageViewCamera.startAnimation(cameraOutTA);
            mImageViewCamera.setVisibility(View.VISIBLE);

            withOutTA.setStartOffset(20);
            mImageViewivWith.startAnimation(withOutTA);
            mImageViewivWith.setVisibility(View.VISIBLE);

            thoughtOutTA.setStartOffset(80);
            mImageViewivThought.startAnimation(thoughtOutTA);
            mImageViewivThought.setVisibility(View.VISIBLE);
            isClockwise = false;

        }
        else
        {
            thoughtInTA.setStartOffset(20);
            mImageViewivThought.startAnimation(thoughtInTA);
            mImageViewivThought.setVisibility(View.GONE);

            withInTA.setStartOffset(80);
            mImageViewivWith.startAnimation(withInTA);
            mImageViewivWith.setVisibility(View.GONE);

            cameraInTA.setStartOffset(100);
            mImageViewCamera.startAnimation(cameraInTA);
            mImageViewCamera.setVisibility(View.GONE);
            isClockwise = true;
        }
        return true;
    }

    private void initAnimationView()
    {

        // am = new RotateAnimation ( 0, 360, 13, 13 );

        Drawable dm = getResources().getDrawable(R.drawable.composer_camera);

        mIntInsHeight = dm.getIntrinsicHeight();
        mIntInsWidth = dm.getIntrinsicWidth();

        // float flexOffset=5.0f;

        mImageViewCamera = (POIMenuImageView)root.findViewById(R.id.ivCamera);
        mImageViewCamera.setOnClickListener(this);
        mImageViewCamera.setAnimationCallBack(mIImageViewAnimationCallBack);

        cameraOutTA = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
            Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 40.0f,
            Animation.RELATIVE_TO_SELF, 10.0f);
        cameraOutTA.setDuration(mIntAnimationDuration);

        cameraOutTA2 = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
            Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 10f,
            Animation.RELATIVE_TO_SELF, 0.0f);
        cameraOutTA2.setDuration(100);


        cameraInTA = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
            Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
            Animation.RELATIVE_TO_SELF, 240.0f + mIntInsHeight);
        cameraInTA.setDuration(mIntAnimationDuration);

        mImageViewivWith = (POIMenuImageView)root.findViewById(R.id.ivWith);
        mImageViewivWith.setOnClickListener(this);
        mImageViewivWith.setAnimationCallBack(mIImageViewAnimationCallBack);

        withOutTA = new TranslateAnimation(Animation.ABSOLUTE, -75f,
            Animation.ABSOLUTE, 0.0f, Animation.ABSOLUTE, 225.0f,
            Animation.ABSOLUTE, 10.0f);
        withOutTA.setDuration(mIntAnimationDuration);

        withOutTA2 = new TranslateAnimation(Animation.ABSOLUTE, 10.0f,
            Animation.ABSOLUTE, 0.0f, Animation.ABSOLUTE, -10.0f,
            Animation.ABSOLUTE, 0.0f);
        withOutTA2.setDuration(100);

        withInTA = new TranslateAnimation(Animation.ABSOLUTE, 0.0f,
            Animation.ABSOLUTE, -75f, Animation.ABSOLUTE, 10.0f,
            Animation.ABSOLUTE, 225.0f);
        withInTA.setDuration(mIntAnimationDuration);

        mImageViewivThought = (POIMenuImageView)root.findViewById(R.id.ivThought);
        mImageViewivThought.setOnClickListener(this);
        mImageViewivThought.setAnimationCallBack(mIImageViewAnimationCallBack);

        thoughtOutTA = new TranslateAnimation(Animation.ABSOLUTE, -205f,
            Animation.ABSOLUTE, -10.0f, Animation.ABSOLUTE, 70f,
            Animation.ABSOLUTE, 10.0f);
        thoughtOutTA.setDuration(mIntAnimationDuration);

        thoughtOutTA2 = new TranslateAnimation(Animation.ABSOLUTE, 10.0f,
            Animation.ABSOLUTE, 0.0f, Animation.ABSOLUTE, -10.0f,
            Animation.ABSOLUTE, 0.0f);
        thoughtOutTA2.setDuration(100);


        thoughtInTA = new TranslateAnimation(Animation.ABSOLUTE, -10.0f,
            Animation.ABSOLUTE, -205f, Animation.ABSOLUTE, 10.0f,
            Animation.ABSOLUTE, 70f);
        thoughtInTA.setDuration(mIntAnimationDuration);


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
            if (animation.equals(cameraOutTA))
            {
                Log.i(tag, "cameraOutTA");
                mImageViewCamera.startAnimation2(cameraOutTA2, 10);
            }
            if (animation.equals(withOutTA))
            {
                Log.i(tag, "withOutTA");
                mImageViewivWith.startAnimation2(withOutTA2, 0);
            }
            if (animation.equals(thoughtOutTA))
            {
                Log.i(tag, "thoughtOutTA");
                mImageViewivThought.startAnimation2(thoughtOutTA2, 0);

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
        switch (v.getId())
        {
            case R.id.ivCamera:
                mImageViewCamera.startAnimation(mAnimationScaleAnimation);
                break;
            case R.id.ivWith:
                mImageViewivWith.startAnimation(mAnimationScaleAnimation);
                break;
            case R.id.ivThought:
                mImageViewivThought.startAnimation(mAnimationScaleAnimation);
                break;

        }
    }

}
