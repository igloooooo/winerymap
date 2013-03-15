package au.com.iglooit.winerymap.android.view.winerydetails;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import au.com.iglooit.winerymap.android.R;
import au.com.iglooit.winerymap.android.constants.ApplicationConstants;
import com.androidquery.AQuery;

public class WineryDetailsActivity extends FragmentActivity
{
    private AQuery aq;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wm_winery_details);
        aq = new AQuery(this);
        initContent();
    }

    private void initContent()
    {
        Intent intent = getIntent();
        Integer id = intent.getIntExtra(ApplicationConstants.WINERY_ID, ApplicationConstants.INVALID_ID);
        aq.id(R.id.wineryName).text(id.toString());
    }
}
