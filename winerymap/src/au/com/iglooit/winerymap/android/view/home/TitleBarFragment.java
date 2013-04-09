package au.com.iglooit.winerymap.android.view.home;

import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import au.com.iglooit.winerymap.android.R;
import au.com.iglooit.winerymap.android.WineryMapHome;
import com.androidquery.AQuery;

public class TitleBarFragment extends Fragment
{
    private AQuery aq;
    private CharSequence title;

    public static TitleBarFragment newInstance(CharSequence title)
    {
        TitleBarFragment f = new TitleBarFragment();
        Bundle b =new Bundle();
        b.putCharSequence("title", title);
        f.setArguments(b);
        return f;

    }

    @Override
    public void onInflate(Activity activity, AttributeSet attrs, Bundle savedInstanceState) {
        super.onInflate(activity, attrs, savedInstanceState);
        TypedArray a = activity.obtainStyledAttributes(attrs, R.styleable.TitleBarFragment);
        title = a.getText(R.styleable.TitleBarFragment_android_label);
        a.recycle();
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle)
    {
        ViewGroup root = (ViewGroup)layoutInflater.inflate(R.layout.wm_home_title_bar_fragment, viewGroup, false);
        aq = new AQuery(root);
        aq.id(R.id.goHomeButton).clicked(this, "onClickHomeButton");
        aq.id(R.id.titleText).text(title==null?"":title);
        return root;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if(args !=null){
            title = args.getCharSequence("title", title);
        }

    }

    public void onClickHomeButton(View view)
    {
        Intent intent = new Intent(this.getActivity(), WineryMapHome.class);
        this.getActivity().startActivity(intent);
    }
}
