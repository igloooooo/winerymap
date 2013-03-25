package au.com.iglooit.winerymap.android.core.component.poimenu;

import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import au.com.iglooit.winerymap.android.exception.AppX;

public class AnimationFactory {
    private static AnimationFactory instance;
    private static int animationDuration1 = 200;
    private static int animationDuration2 = 100;
    private static int step1 = 20;

    private AnimationFactory() {

    }

    public static AnimationFactory newInstance() {
        if (instance == null) {
            instance = new AnimationFactory();
        }
        return instance;
    }

    public TranslateAnimation getAnimation(MenuItemAnimationType type) {
        switch (type) {
            case CameraOut1:
                TranslateAnimation cameraOutTA = new TranslateAnimation(0, 0, POIMenuImageView.RADIUS, POIMenuImageView.RADIUS - step1);
                cameraOutTA.setDuration(animationDuration1);
                return cameraOutTA;
            case CameraOut2:
                TranslateAnimation cameraOutTA2 = new TranslateAnimation(0, 0, POIMenuImageView.RADIUS - step1, 0);
                cameraOutTA2.setDuration(animationDuration2);
                return cameraOutTA2;
            case CameraIn:
                TranslateAnimation cameraInTA = new TranslateAnimation(0, 0, 0, POIMenuImageView.RADIUS);
                cameraInTA.setDuration(animationDuration1);
                return cameraInTA;

            case PlaceOut1:
                TranslateAnimation placeOut1 = new TranslateAnimation(-POIMenuImageView.getOffset(),
                        step1 - POIMenuImageView.getOffset(), POIMenuImageView.getOffset(), POIMenuImageView.getOffset() - step1);
                placeOut1.setDuration(animationDuration1);
                return placeOut1;
            case PlaceOut2:
                TranslateAnimation placeOut2 = new TranslateAnimation(step1 - POIMenuImageView.getOffset(), 0,
                        POIMenuImageView.getOffset() - step1, 0);
                placeOut2.setDuration(animationDuration2);
                return placeOut2;
            case PlaceIn:
                TranslateAnimation placeIn = new TranslateAnimation(0, -POIMenuImageView.getOffset(),
                        0, POIMenuImageView.getOffset());
                placeIn.setDuration(animationDuration1);
                return placeIn;

            case ThoughtOut1:
                TranslateAnimation thoughtOut1 = new TranslateAnimation(-POIMenuImageView.RADIUS,
                        step1 -POIMenuImageView.RADIUS, 0, 0);
                thoughtOut1.setDuration(animationDuration1);
                return thoughtOut1;
            case ThoughtOut2:
                TranslateAnimation thoughtOut2 = new TranslateAnimation(step1 -POIMenuImageView.RADIUS, 0,
                        0, 0);
                thoughtOut2.setDuration(animationDuration2);
                return thoughtOut2;
            case ThoughtIn:
                TranslateAnimation thoughtIn = new TranslateAnimation(0, step1 -POIMenuImageView.RADIUS,
                        0, 0);
                thoughtIn.setDuration(animationDuration1);
                return thoughtIn;
            default:
                throw new AppX("Unsupported type");
        }
    }
}
