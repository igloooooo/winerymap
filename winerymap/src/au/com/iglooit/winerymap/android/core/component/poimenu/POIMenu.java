package au.com.iglooit.winerymap.android.core.component.poimenu;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;
import au.com.iglooit.winerymap.android.R;

public class POIMenu extends RelativeLayout
{
    private static final String TAG = POIMenu.class.getSimpleName();
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

    public ClickMenuItemListener clickMenuItemListener;

    public POIMenu(Context context)
    {
        super(context);
        initContent();
    }

    public POIMenu(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        initContent();
    }

    public POIMenu(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        initContent();
    }

    private void initContent()
    {
        // Inflate the view from the layout resource.
        String infService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater li;

        li = (LayoutInflater)getContext().getSystemService(infService);
        li.inflate(R.layout.wm_home_poimenu_layout, this, true);

        initAnimationView();
    }

    private void initAnimationView()
    {
        imageViewAnimationCallBack = new ImageViewAnimationCallBack();

        addToMyFavourite = (POIMenuImageView)findViewById(R.id.add_my_favourite_img);
        addToMyFavourite.setAnimationCallBack(imageViewAnimationCallBack);

        addToMyFavouriteOutTA = AnimationFactory.newInstance().getAnimation(MenuItemAnimationType.AddToMyFavouriteOut1);
        addToMyFavouriteOutTA2 = AnimationFactory.newInstance().getAnimation(MenuItemAnimationType.AddToMyFavouriteOut2);
        addToMyFavouriteInTA = AnimationFactory.newInstance().getAnimation(MenuItemAnimationType.AddToMyFavouriteIn);

        navigateTo = (POIMenuImageView)findViewById(R.id.navigate_to_img);
        navigateTo.setAnimationCallBack(imageViewAnimationCallBack);

        navigateToOutTA = AnimationFactory.newInstance().getAnimation(MenuItemAnimationType.NavigateToOut1);
        navigateToOutTA2 = AnimationFactory.newInstance().getAnimation(MenuItemAnimationType.NavigateToOut2);
        navigateToInTA = AnimationFactory.newInstance().getAnimation(MenuItemAnimationType.NavigateToIn);

        showDetails = (POIMenuImageView)findViewById(R.id.show_details_img);
        showDetails.setAnimationCallBack(imageViewAnimationCallBack);

        showDetailsOutTA = AnimationFactory.newInstance().getAnimation(MenuItemAnimationType.ShowDetailsOut1);
        showDetailsOutTA2 = AnimationFactory.newInstance().getAnimation(MenuItemAnimationType.ShowDetailsOut2);
        showDetailsInTA = AnimationFactory.newInstance().getAnimation(MenuItemAnimationType.ShowDetailsIn);
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
                Log.d(TAG, "addToMyFavouriteOutTA");
                addToMyFavourite.startAnimation2(addToMyFavouriteOutTA2, 10);
            }
            else if (animation.equals(navigateToOutTA))
            {
                Log.d(TAG, "navigateToOutTA");
                navigateTo.startAnimation2(navigateToOutTA2, 0);
            }
            else if (animation.equals(showDetailsOutTA))
            {
                Log.d(TAG, "showDetailsOutTA");
                showDetails.startAnimation2(showDetailsOutTA2, 0);
            }
            else if (animation.equals(addToMyFavouriteOutTA2) || animation.equals(navigateToOutTA2)
                || animation.equals(showDetailsOutTA2))
            {
                // don't do anything currently
            }
            else
            {
                if(clickMenuItemListener != null)
                {
                    if(v.equals(addToMyFavourite))
                    {
                        clickMenuItemListener.onAddToMyFavourite(v);
                    }
                    else if (v.equals(navigateTo))
                    {
                        clickMenuItemListener.onNavigationTo(v);
                    }
                    else if (v.equals(showDetails))
                    {
                        clickMenuItemListener.onShowDetails(v);
                    }
                }
            }
        }

        @Override
        public void onRepeatAnimation(Animation animation, View v)
        {
        }

    }

    public void showMenuItems(Point basePoint)
    {
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
    }

    public void hideMenuItems()
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
    }

    public interface ClickMenuItemListener
    {
        void onShowDetails(View v);
        void onAddToMyFavourite(View v);
        void onNavigationTo(View v);
    }

}
