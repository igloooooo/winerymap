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
package greendroid.widget;

import android.view.LayoutInflater;
import android.view.View;

import com.cyrilmottier.android.greendroid.R;

/**
 * An extension of a {@link NormalGDActionBarItem} that supports a loading states.
 * When in loading state, a {@link LoaderGDActionBarItem} display an indeterminate
 * circular ProgressBar. This item is very handful with application fetching
 * data from the network or performing long background tasks.
 * 
 * @author Cyril Mottier
 */
public class LoaderGDActionBarItem extends NormalGDActionBarItem
{

    private boolean mLoading;
    private View mButton;
    private View mProgressBar;

    public LoaderGDActionBarItem() {
        mLoading = false;
    }

    @Override
    protected View createItemView() {
        return LayoutInflater.from(mContext).inflate(R.layout.gd_action_bar_item_loader, mGDTActionBar, false);
    }

    @Override
    protected void prepareItemView() {
        super.prepareItemView();
        mButton = mItemView.findViewById(R.id.gd_action_bar_item);
        mProgressBar = mItemView.findViewById(R.id.gd_action_bar_item_progress_bar);
    }

    @Override
    protected void onItemClicked() {
        super.onItemClicked();
        setLoading(true);
    }

    /**
     * Sets the loading state of this {@link LoaderGDActionBarItem}.
     * 
     * @param loading The new loading state. If true, an indeterminate
     *            ProgressBar is displayed. When false (default value) the
     *            {@link GDActionBarItem} behaves exactly like a regular
     *            {@link NormalGDActionBarItem}.
     */
    public void setLoading(boolean loading) {
        if (loading != mLoading) {
            mProgressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
            mButton.setVisibility(loading ? View.GONE : View.VISIBLE);
            mLoading = loading;
        }
    }
}
