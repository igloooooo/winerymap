package au.com.iglooit.winerymap.android.core.component.poimenu;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

public class POIMenuImageView extends ImageView implements Animation.AnimationListener
{
    private Animation mAnimation;
    private float degree = 0;
    private Canvas mCanvas;

    private int drawTime = 0;
    private String tag = "POIMenuImageView";
    private boolean isStartCount = false;
    private boolean isAnimation = false;

    private long animationTime = 0;
    private long animationDuration = 0;
    private int rotateOffset = 30;
    private AnimationCallBack mAnimationCallBack;

    public POIMenuImageView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    @Override
    public void startAnimation(Animation animation)
    {
        animationTime = System.currentTimeMillis();
            /*if (isAnimation) {
                return;
    		}*/
    		/*if (null != mAnimation) {
    			mAnimation.reset();
    		}*/
        mAnimation = animation;
        animationDuration = mAnimation.getDuration();
        if (!(animation instanceof TranslateAnimation))
        {
            rotateOffset = 0;
        }
        else
        {
            rotateOffset = (int)(360 / (animationDuration / 20));//20 degree
        }
        super.startAnimation(animation);
        mAnimation.setAnimationListener(this);
    }


    public void startAnimation2(Animation animation, int offset)
    {
        animationTime = System.currentTimeMillis();
    		/*if (isAnimation) {
    			return;
    		}*/
    		/*if (null != mAnimation) {
    			mAnimation.reset();
    		}*/
        mAnimation = animation;
        animationDuration = mAnimation.getDuration();
        if (!(animation instanceof TranslateAnimation))
        {
            rotateOffset = offset;
        }
        else
        {
            rotateOffset = 0;
        }
        super.startAnimation(animation);
        mAnimation.setAnimationListener(this);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        mCanvas = canvas;
        if (0 != rotateOffset)
        {
            canvas.rotate(degree, getWidth() / 2, getHeight() / 2);
        }
        super.onDraw(canvas);
        if (isStartCount)
        {
            degree += rotateOffset;
        }
        drawTime++;
    }

    @Override
    public void onAnimationStart(Animation animation)
    {
        degree = 0;
        drawTime = 0;
        isStartCount = true;
        isAnimation = true;
        mAnimationCallBack.onStartAnimation(animation, this);
    }

    @Override
    public void onAnimationEnd(Animation animation)
    {
        isStartCount = false;
        postInvalidate();
        Log.i(tag, "turn over" + degree + "extral degree:" + (360 - (degree % 360)) + ":"
            + " dration:" + mAnimation.getDuration() + "  drawcount:"
            + drawTime);
        degree = 0;
        isAnimation = false;
        mAnimationCallBack.onEndAnimation(animation, this);
    }

    public void setLocation(int newX, int newY)
    {
        this.setFrame(newX, newY - this.getHeight(), newX + this.getWidth(), newY);
    }


    public void setLocation(Point position)
    {
        this.setFrame(position.x - this.getWidth() / 2, position.y - this.getHeight() / 2,
            position.x + this.getWidth() / 2, position.y + this.getHeight() / 2);
    }

    @Override
    public void onAnimationRepeat(Animation animation)
    {
        mAnimationCallBack.onRepeatAnimation(animation, this);
    }

    public void setAnimationCallBack(AnimationCallBack animationCallBack)
    {
        this.mAnimationCallBack = animationCallBack;
    }


    /**
     * call back function
     *
     * @author nick
     */
    public interface AnimationCallBack
    {
        void onStartAnimation(Animation animation, View v);

        void onEndAnimation(Animation animation, View v);

        void onRepeatAnimation(Animation animation, View v);
    }
}
