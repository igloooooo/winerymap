package au.com.iglooit.winerymap.android.core.component;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.*;
import android.widget.AbsListView.OnScrollListener;
import au.com.iglooit.winerymap.android.R;

import java.util.Date;

public class ScrollableListView extends ListView implements OnScrollListener {

    private static final String TAG = "listview";
    private final static int RELEASE_To_REFRESH = 0;
    private final static int PULL_To_REFRESH = 1;
    private final static int REFRESHING = 2;
    private final static int DONE = 3;
    private final static int LOADING = 4;
    // Top padding
    private final static int RATIO = 3;
    private LayoutInflater inflater;
    private LinearLayout headView;
    private TextView tipsTextview;
    private TextView lastUpdatedTextView;
    private ImageView arrowImageView;
    private ProgressBar progressBar;
    private RotateAnimation animation;
    private RotateAnimation reverseAnimation;

    private boolean isRecored;
    private int headContentWidth;
    private int headContentHeight;
    private int startY;
    private int firstItemIndex;
    private int state;
    private boolean isBack;
    private OnRefreshListener refreshListener;
    private boolean isRefreshable;

    public ScrollableListView(Context context) {
        super(context);
        init(context);
    }

    public ScrollableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        setCacheColorHint(context.getResources().getColor(R.color.translucent_bg));
        inflater = LayoutInflater.from(context);

        headView = (LinearLayout) inflater.inflate(R.layout.wm_news_list_header, null);

        arrowImageView = (ImageView) headView
                .findViewById(R.id.head_arrowImageView);
        arrowImageView.setMinimumWidth(70);
        arrowImageView.setMinimumHeight(50);
        progressBar = (ProgressBar) headView
                .findViewById(R.id.head_progressBar);
        tipsTextview = (TextView) headView.findViewById(R.id.head_tipsTextView);
        lastUpdatedTextView = (TextView) headView
                .findViewById(R.id.head_lastUpdatedTextView);

        measureView(headView);
        headContentHeight = headView.getMeasuredHeight();
        headContentWidth = headView.getMeasuredWidth();

        headView.setPadding(0, -1 * headContentHeight, 0, 0);
        headView.invalidate();

        Log.v("size", "width:" + headContentWidth + " height:"
                + headContentHeight);

        addHeaderView(headView, null, false);
        setOnScrollListener(this);

        animation = new RotateAnimation(0, -180,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        animation.setInterpolator(new LinearInterpolator());
        animation.setDuration(250);
        animation.setFillAfter(true);

        reverseAnimation = new RotateAnimation(-180, 0,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        reverseAnimation.setInterpolator(new LinearInterpolator());
        reverseAnimation.setDuration(200);
        reverseAnimation.setFillAfter(true);

        state = DONE;
        isRefreshable = false;
    }

    public void onScroll(AbsListView arg0, int firstVisiableItem, int arg2,
                         int arg3) {
        firstItemIndex = firstVisiableItem;
    }

    public void onScrollStateChanged(AbsListView arg0, int arg1) {
    }

    public boolean onTouchEvent(MotionEvent event) {

        if (isRefreshable) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (firstItemIndex == 0 && !isRecored) {
                        isRecored = true;
                        startY = (int) event.getY();
                        Log.v(TAG, "pull down");
                    }
                    break;

                case MotionEvent.ACTION_UP:

                    if (state != REFRESHING && state != LOADING) {
                        if (state == DONE) {
                            // ʲô������
                        }
                        if (state == PULL_To_REFRESH) {
                            state = DONE;
                            changeHeaderViewByState();

                            Log.v(TAG, "pull to refresh done״̬");
                        }
                        if (state == RELEASE_To_REFRESH) {
                            state = REFRESHING;
                            changeHeaderViewByState();
                            onRefresh();

                            Log.v(TAG, "RELEASE_To_REFRESH done״̬");
                        }
                    }

                    isRecored = false;
                    isBack = false;

                    break;

                case MotionEvent.ACTION_MOVE:
                    int tempY = (int) event.getY();

                    if (!isRecored && firstItemIndex == 0) {
                        Log.v(TAG, "ACTION_MOVE");
                        isRecored = true;
                        startY = tempY;
                    }

                    if (state != REFRESHING && isRecored && state != LOADING) {
                        if (state == RELEASE_To_REFRESH) {
                            setSelection(0);
                            if (((tempY - startY) / RATIO < headContentHeight)
                                    && (tempY - startY) > 0) {
                                state = PULL_To_REFRESH;
                                changeHeaderViewByState();

                                Log.v(TAG, "RELEASE_To_REFRESH̬");
                            }
                            // һ�����Ƶ�����
                            else if (tempY - startY <= 0) {
                                state = DONE;
                                changeHeaderViewByState();

                                Log.v(TAG, "tempY - startY <=̬");
                            }
                            else {
                            }
                        }
                        if (state == PULL_To_REFRESH) {
                            setSelection(0);
                            if ((tempY - startY) / RATIO >= headContentHeight) {
                                state = RELEASE_To_REFRESH;
                                isBack = true;
                                changeHeaderViewByState();

                                Log.v(TAG, "��done��������ˢ��״̬ת�䵽�ɿ�ˢ��");
                            }
                            // ���Ƶ�����
                            else if (tempY - startY <= 0) {
                                state = DONE;
                                changeHeaderViewByState();

                                Log.v(TAG, "��DOne��������ˢ��״̬ת�䵽done״̬");
                            }
                        }

                        // done״̬��
                        if (state == DONE) {
                            if (tempY - startY > 0) {
                                state = PULL_To_REFRESH;
                                changeHeaderViewByState();
                            }
                        }

                        // ����headView��size
                        if (state == PULL_To_REFRESH) {
                            headView.setPadding(0, -1 * headContentHeight
                                    + (tempY - startY) / RATIO, 0, 0);

                        }

                        // ����headView��paddingTop
                        if (state == RELEASE_To_REFRESH) {
                            headView.setPadding(0, (tempY - startY) / RATIO
                                    - headContentHeight, 0, 0);
                        }

                    }

                    break;
            }
        }

        return super.onTouchEvent(event);
    }

    // ��״̬�ı�ʱ�򣬵��ø÷������Ը��½���
    private void changeHeaderViewByState() {
        switch (state) {
            case RELEASE_To_REFRESH:
                arrowImageView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                tipsTextview.setVisibility(View.VISIBLE);
                lastUpdatedTextView.setVisibility(View.VISIBLE);

                arrowImageView.clearAnimation();
                arrowImageView.startAnimation(animation);

                tipsTextview.setText("Release to refresh list");

                Log.v(TAG, "��ǰ״̬���ɿ�ˢ��");
                break;
            case PULL_To_REFRESH:
                progressBar.setVisibility(View.GONE);
                tipsTextview.setVisibility(View.VISIBLE);
                lastUpdatedTextView.setVisibility(View.VISIBLE);
                arrowImageView.clearAnimation();
                arrowImageView.setVisibility(View.VISIBLE);
                if (isBack) {
                    isBack = false;
                    arrowImageView.clearAnimation();
                    arrowImageView.startAnimation(reverseAnimation);

                    tipsTextview.setText("Pull down to refresh list");
                } else {
                    tipsTextview.setText("Refresh");
                }
                Log.v(TAG, "��ǰ״̬������ˢ��");
                break;

            case REFRESHING:

                headView.setPadding(0, 0, 0, 0);

                progressBar.setVisibility(View.VISIBLE);
                arrowImageView.clearAnimation();
                arrowImageView.setVisibility(View.GONE);
                tipsTextview.setText("Loading...");
                lastUpdatedTextView.setVisibility(View.VISIBLE);

                Log.v(TAG, "��ǰ״̬,����ˢ��...");
                break;
            case DONE:
                headView.setPadding(0, -1 * headContentHeight, 0, 0);

                progressBar.setVisibility(View.GONE);
                arrowImageView.clearAnimation();
                arrowImageView.setImageResource(R.drawable.arrow);
                tipsTextview.setText("Update finished");
                lastUpdatedTextView.setVisibility(View.VISIBLE);

                Log.v(TAG, "��ǰ״̬��done");
                break;
        }
    }

    public void setonRefreshListener(OnRefreshListener refreshListener) {
        this.refreshListener = refreshListener;
        isRefreshable = true;
    }

    public void onRefreshComplete() {
        state = DONE;
        lastUpdatedTextView.setText("Last updated:" + new Date().toLocaleString());
        changeHeaderViewByState();
    }

    private void onRefresh() {
        if (refreshListener != null) {
            refreshListener.onRefresh();
        }
    }

    // �˷���ֱ���հ��������ϵ�һ������ˢ�µ�demo���˴��ǡ����ơ�headView��width�Լ�height
    private void measureView(View child) {
        ViewGroup.LayoutParams p = child.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
        int lpHeight = p.height;
        int childHeightSpec;
        if (lpHeight > 0) {
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
                    MeasureSpec.EXACTLY);
        } else {
            childHeightSpec = MeasureSpec.makeMeasureSpec(0,
                    MeasureSpec.UNSPECIFIED);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }

    public void setAdapter(BaseAdapter adapter) {
        lastUpdatedTextView.setText("Last updated:" + new Date().toLocaleString());
        super.setAdapter(adapter);
    }

    public interface OnRefreshListener {
        public void onRefresh();
    }

}
