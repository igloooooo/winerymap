package au.com.iglooit.winerymap.android.core.component.poimenu;

import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import au.com.iglooit.winerymap.android.exception.AppX;

public class AnimationFactory
{
    private static AnimationFactory instance;
    private static int animationDuration1 = 200;
    private static int animationDuration2 = 100;

    private AnimationFactory()
    {

    }

    public static AnimationFactory newInstance()
    {
        if (instance == null)
        {
            instance = new AnimationFactory();
        }
        return instance;
    }

    public Animation getAnimation(MenuItemAnimationType type)
    {
        switch (type)
        {
            case CameraOut1:
                TranslateAnimation cameraOutTA = new TranslateAnimation(0, 0, 100, 20);
                cameraOutTA.setDuration(animationDuration1);
                return cameraOutTA;
            case CameraOut2:
                TranslateAnimation cameraOutTA2 = new TranslateAnimation(0, 0, 20, 0);
                cameraOutTA2.setDuration(animationDuration2);
                return cameraOutTA2;
            case CameraIn:
                TranslateAnimation cameraInTA = new TranslateAnimation(0, 0, 0, 100);
                cameraInTA.setDuration(animationDuration1);
                return cameraInTA;
            default:
                throw new AppX("Unsupported type");
        }
    }
}
