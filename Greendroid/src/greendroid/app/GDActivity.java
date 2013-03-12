/*
 * Copyright (C) 2010 Cyril Mottier (http://www.cyrilmottier.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package greendroid.app;

import com.cyrilmottier.android.greendroid.R;
import greendroid.util.Config;
import greendroid.widget.GDActionBar;
import greendroid.widget.GDActionBarHost;
import greendroid.widget.GDActionBarItem;
import greendroid.widget.GDActionBar.OnActionBarListener;
import greendroid.widget.GDActionBar.Type;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;

/**
 * <p>
 * An GDActivity is a regular Activity that hosts an {@link greendroid.widget.GDActionBar}. It is
 * extremely simple to use as you have nothing particular to do. Indeed, the
 * {@link greendroid.widget.GDActionBar} is automatically added to your own layout when using the
 * {@link #getContentView()} method. You can also use one of the
 * setActionBarContentView utility methods. As a result, a basic GDActivity will
 * often be initialized using the following snippet of code:
 * </p>
 * 
 * <pre>
 * protected void onCreate(Bundle savedInstanceState) {
 *     super.onCreate(savedInstanceState);
 * 
 *     setActionBarContentView(R.layout.main);
 * }
 * </pre>
 * <p>
 * An {@link greendroid.widget.GDActionBar} is a widget that may contains actions items and a title.
 * You can also set the title putting an extra string with the key
 * {@link ActionBarActivity#GD_ACTION_BAR_TITLE} in your Intent:
 * </p>
 * 
 * <pre>
 * Intent intent = new Intent(this, MyGDActivity.class);
 * intent.putExtra(ActionBarActivity.GD_ACTION_BAR_TITLE, &quot;Next screen title&quot;);
 * startActivity(intent);
 * </pre>
 * <p>
 * <em><strong>Note</strong>: An GDActivity automatically handle the type of the {@link greendroid.widget.GDActionBar}
 * (taken from {@link greendroid.widget.GDActionBar.Type}) depending on the value returned by the
 * {@link GDApplication#getHomeActivityClass()} method. However you can force the
 * type of the action bar in your constructor. Make the Activity declared in the AndroidManifest.xml 
 * has at least a constructor with no arguments as required by the Android platform.</em>
 * </p>
 * 
 * <pre>
 * public MyGDActivity() {
 *     super(ActionBar.Type.Dashboard);
 * }
 * </pre>
 * <p>
 * All Activities that inherits from an GDActivity are notified when an action
 * button is tapped in the
 * {@link #onHandleActionBarItemClick(greendroid.widget.GDActionBarItem, int)} method. By default
 * this method does nothing but return false.
 * </p>
 * 
 * @see GDApplication#getHomeActivityClass()
 * @see ActionBarActivity#GD_ACTION_BAR_TITLE
 * @see GDActivity#setActionBarContentView(int)
 * @see GDActivity#setActionBarContentView(View)
 * @see GDActivity#setActionBarContentView(View, LayoutParams)
 * @author Cyril Mottier
 */
public class GDActivity extends Activity implements ActionBarActivity {

    private static final String LOG_TAG = GDActivity.class.getSimpleName();

    private boolean mDefaultConstructorUsed = false;

    private Type mActionBarType;
    private GDActionBarHost mGDActionBarHost;

    /**
     * <p>
     * Default constructor.
     * </p>
     * <p>
     * <em><strong>Note</strong>: This constructor should never be used manually. 
     * In order to instantiate an Activity you should let the Android system do 
     * it for you by calling startActivity(Intent)</em>
     * </p>
     */
    public GDActivity() {
        this(Type.Normal);
        mDefaultConstructorUsed = true;
    }

    /**
     * <p>
     * Create a new Activity with an {@link greendroid.widget.GDActionBar} of the given type.
     * </p>
     * <p>
     * <em><strong>Note</strong>: This constructor should never be used manually. 
     * In order to instantiate an Activity you should let the Android system do 
     * it for you by calling startActivity(Intent)</em>
     * </p>
     * 
     * @param actionBarType The {@link greendroid.widget.GDActionBar.Type} for this Activity
     */
    public GDActivity(GDActionBar.Type actionBarType) {
        super();
        mActionBarType = actionBarType;
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        ensureLayout();
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (mDefaultConstructorUsed) {
            // HACK cyril: This should have been done in the default
            // constructor. Unfortunately, the getApplication() method returns
            // null there. Hence, this has to be done here.
            if (getClass().equals(getGDApplication().getHomeActivityClass())) {
                mActionBarType = Type.Dashboard;
            }
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        ensureLayout();
    }

    /**
     * The current {@link greendroid.widget.GDActionBar.Type} of the hosted {@link greendroid.widget.GDActionBar}
     * 
     * @return The current {@link greendroid.widget.GDActionBar.Type} of the hosted
     *         {@link greendroid.widget.GDActionBar}
     */
    public GDActionBar.Type getActionBarType() {
        return mActionBarType;
    }

    public int createLayout() {
        switch (mActionBarType) {
            case Dashboard:
                return R.layout.gd_content_dashboard;
            case Empty:
                return R.layout.gd_content_empty;
            case Normal:
            default:
                return R.layout.gd_content_normal;
        }
    }

    /**
     * Call this method to ensure a layout has already been inflated and
     * attached to the top-level View of this Activity.
     */
    protected void ensureLayout() {
        if (!verifyLayout()) {
            setContentView(createLayout());
        }
    }

    /**
     * Verify the given layout contains everything needed by this Activity. A
     * GDActivity, for instance, manages an {@link greendroid.widget.GDActionBarHost}. As a result
     * this method will return true of the current layout contains such a
     * widget.
     * 
     * @return true if the current layout fits to the current Activity widgets
     *         requirements
     */
    protected boolean verifyLayout() {
        return mGDActionBarHost != null;
    }

    public GDApplication getGDApplication() {
        return (GDApplication) getApplication();
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();

        onPreContentChanged();
        onPostContentChanged();
    }

    public void onPreContentChanged() {
        mGDActionBarHost = (GDActionBarHost) findViewById(R.id.gd_action_bar_host);
        if (mGDActionBarHost == null) {
            throw new RuntimeException("Your content must have an ActionBarHost whose id attribute is R.id.gd_action_bar_host");
        }
        mGDActionBarHost.getGDActionBar().setOnActionBarListener(mActionBarListener);
    }

    public void onPostContentChanged() {

        boolean titleSet = false;

        final Intent intent = getIntent();
        if (intent != null) {
            String title = intent.getStringExtra(ActionBarActivity.GD_ACTION_BAR_TITLE);
            if (title != null) {
                titleSet = true;
                setTitle(title);
            }
        }

        if (!titleSet) {
            // No title has been set via the Intent. Let's look in the
            // ActivityInfo
            try {
                final ActivityInfo activityInfo = getPackageManager().getActivityInfo(getComponentName(), 0);
                if (activityInfo.labelRes != 0) {
                    setTitle(activityInfo.labelRes);
                }
            } catch (NameNotFoundException e) {
                // Do nothing
            }
        }

        final int visibility = intent.getIntExtra(ActionBarActivity.GD_ACTION_BAR_VISIBILITY, View.VISIBLE);
        getGDActionBar().setVisibility(visibility);
    }

    @Override
    public void setTitle(CharSequence title) {
        getGDActionBar().setTitle(title);
    }

    @Override
    public void setTitle(int titleId) {
        setTitle(getString(titleId));
    }

    public GDActionBar getGDActionBar() {
        ensureLayout();
        return mGDActionBarHost.getGDActionBar();
    }

    public GDActionBarItem addActionBarItem(GDActionBarItem item) {
        return getGDActionBar().addItem(item);
    }

    public GDActionBarItem addActionBarItem(GDActionBarItem item, int itemId) {
        return getGDActionBar().addItem(item, itemId);
    }

    public GDActionBarItem addActionBarItem(GDActionBarItem.Type actionBarItemType) {
        return getGDActionBar().addItem(actionBarItemType);
    }

    public GDActionBarItem addActionBarItem(GDActionBarItem.Type actionBarItemType, int itemId) {
        return getGDActionBar().addItem(actionBarItemType, itemId);
    }

    public FrameLayout getContentView() {
        ensureLayout();
        return mGDActionBarHost.getContentView();
    }

    public GDActionBarHost getActionBarHost()
    {
        return mGDActionBarHost;
    }

    /**
     * <p>
     * Set the activity content from a layout resource. The resource will be
     * inflated, adding all top-level views to the activity.
     * </p>
     * <p>
     * This method is an equivalent to setContentView(int) that automatically
     * wraps the given layout in an {@link greendroid.widget.GDActionBarHost} if needed..
     * </p>
     * 
     * @param resID Resource ID to be inflated.
     * @see #setActionBarContentView(View)
     * @see #setActionBarContentView(View, LayoutParams)
     */
    public void setActionBarContentView(int resID) {
        final FrameLayout contentView = getContentView();
        contentView.removeAllViews();
        LayoutInflater.from(this).inflate(resID, contentView);
    }

    /**
     * <p>
     * Set the activity content to an explicit view. This view is placed
     * directly into the activity's view hierarchy. It can itself be a complex
     * view hierarchy.
     * </p>
     * <p>
     * This method is an equivalent to setContentView(View, LayoutParams) that
     * automatically wraps the given layout in an {@link greendroid.widget.GDActionBarHost} if
     * needed.
     * </p>
     * 
     * @param view The desired content to display.
     * @param params Layout parameters for the view.
     * @see #setActionBarContentView(View)
     * @see #setActionBarContentView(int)
     */
    public void setActionBarContentView(View view, LayoutParams params) {
        final FrameLayout contentView = getContentView();
        contentView.removeAllViews();
        contentView.addView(view, params);
    }

    /**
     * <p>
     * Set the activity content to an explicit view. This view is placed
     * directly into the activity's view hierarchy. It can itself be a complex
     * view hierarchy.
     * </p>
     * <p>
     * This method is an equivalent to setContentView(View) that automatically
     * wraps the given layout in an {@link greendroid.widget.GDActionBarHost} if needed.
     * </p>
     * 
     * @param view The desired content to display.
     * @see #setActionBarContentView(int)
     * @see #setActionBarContentView(View, LayoutParams)
     */
    public void setActionBarContentView(View view) {
        final FrameLayout contentView = getContentView();
        contentView.removeAllViews();
        contentView.addView(view);
    }

    public boolean onHandleActionBarItemClick(GDActionBarItem item, int position) {
        return false;
    }

    private OnActionBarListener mActionBarListener = new OnActionBarListener() {
        public void onActionBarItemClicked(int position) {
            if (position == OnActionBarListener.HOME_ITEM) {

                final GDApplication app = getGDApplication();
                switch (mActionBarType) {
                    case Normal:
                        final Class<?> klass = app.getHomeActivityClass();
                        if (klass != null && !klass.equals(GDActivity.this.getClass())) {
                            if (Config.GD_INFO_LOGS_ENABLED) {
                                Log.i(LOG_TAG, "Going back to the home activity");
                            }
                            Intent homeIntent = new Intent(GDActivity.this, klass);
                            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(homeIntent);
                        }
                        break;
                    case Dashboard:
                        final Intent appIntent = app.getMainApplicationIntent();
                        if (appIntent != null) {
                            if (Config.GD_INFO_LOGS_ENABLED) {
                                Log.i(LOG_TAG, "Launching the main application Intent");
                            }
                            startActivity(appIntent);
                        }
                        break;
                }

            } else {
                if (!onHandleActionBarItemClick(getGDActionBar().getItem(position), position)) {
                    if (Config.GD_WARNING_LOGS_ENABLED) {
                        Log.w(LOG_TAG, "Click on item at position " + position + " dropped down to the floor");
                    }
                }
            }
        }
    };

}
