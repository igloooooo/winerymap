package au.com.iglooit.winerymap.android.core.component.map;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class IGTZoomControl extends LinearLayout
{
    public Button zoomOutButton;
    public Button zoomInButton;
    public Button showWineryButton;
    private static final int HEIGHT = 20;

    public IGTZoomControl(Context context)
    {
        super(context);
        this.setOrientation(HORIZONTAL);
        initContent();
    }

    public IGTZoomControl(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        this.setOrientation(HORIZONTAL);
        initContent();
    }

    public IGTZoomControl(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        this.setOrientation(HORIZONTAL);
        initContent();
    }

    private void initContent()
    {
        zoomOutButton = new Button(this.getContext());
        zoomOutButton.setText("+");
        zoomOutButton.setHeight(10);
        zoomOutButton.setGravity(Gravity.CENTER);
        addView(zoomOutButton, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        zoomInButton = new Button(this.getContext());
        zoomInButton.setText("-");
        zoomInButton.setHeight(10);
        zoomInButton.setGravity(Gravity.CENTER);
        addView(zoomInButton, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        showWineryButton = new Button(this.getContext());
        showWineryButton.setText("?");
        showWineryButton.setHeight(10);
        showWineryButton.setGravity(Gravity.CENTER);
        addView(showWineryButton, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
    }

    public void addListeners(final IGTZoomControlListener listener)
    {
        zoomOutButton.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                listener.onClickZoomOut(view);
            }
        });
        zoomInButton.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                listener.onClickZoomIn(view);
            }
        });
        showWineryButton.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                listener.onClickShowWinery(view);
            }
        });
    }

    public interface IGTZoomControlListener
    {
        public void onClickZoomIn(View view);
        public void onClickZoomOut(View view);
        public void onClickShowWinery(View view);
    }
}
