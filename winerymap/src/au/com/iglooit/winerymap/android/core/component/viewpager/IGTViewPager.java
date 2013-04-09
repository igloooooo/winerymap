package au.com.iglooit.winerymap.android.core.component.viewpager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.WindowManager;

public class IGTViewPager extends ViewPager
{
    private int childVPHeight = 0;

    public IGTViewPager(Context context)
    {
        super(context);
        init(context);
    }

    public IGTViewPager(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(context);
    }

    private void init(Context context)
    {
        WindowManager windowManager = (WindowManager)context.getSystemService(context.WINDOW_SERVICE);
        int disWidth = windowManager.getDefaultDisplay().getWidth();
        childVPHeight = (int)(context.getResources().getDisplayMetrics().density * disWidth + 0.5f);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0)
    {
        if (getCurrentItem() == 1 && arg0.getY() < childVPHeight)
        {
            return false;
        }
        return super.onInterceptTouchEvent(arg0);
    }
}
