package au.com.iglooit.winerymap.android.view.winerydetails;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import au.com.iglooit.winerymap.android.R;
import au.com.iglooit.winerymap.android.constants.ApplicationConstants;
import au.com.iglooit.winerymap.android.entity.dto.WineryInfoDTO;
import au.com.iglooit.winerymap.android.exception.AppX;
import au.com.iglooit.winerymap.android.view.core.WMBaseFragmentActivity;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.google.gson.Gson;
import org.json.JSONObject;

public class WineryDetailsActivity extends WMBaseFragmentActivity
{
    private AQuery aq;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wm_winery_details_layout);
        aq = new AQuery(this);
        initContent();
    }

    private void initContent()
    {
        Intent intent = getIntent();
        Integer id = intent.getIntExtra(ApplicationConstants.WINERY_ID, ApplicationConstants.INVALID_ID);
        String key = intent.getStringExtra(ApplicationConstants.WINERY_KEY);
        aq.id(R.id.wineryName).text("Test Winery Name");
        aq.id(R.id.wineryDescription).text("This is the description for test winery. (id is " + id + ", " +
            "key is " + key + ")");

//        aq.ajax(urlBuilder(key), JSONObject.class, this, "jsonCallback");
//        this.progressDialog = ProgressDialog.show(this, "Getting Winery Details", "Connecting to the database...");
    }

    public void jsonCallback(String url, JSONObject json, AjaxStatus status)
    {

        if (json != null)
        {
            Gson gson = new Gson();
            WineryInfoDTO wineryInfoDTO = gson.fromJson(json.toString(), WineryInfoDTO.class);
            aq.id(R.id.wineryName).text(wineryInfoDTO.companyName);
            aq.id(R.id.wineryDescription).text(wineryInfoDTO.description);
        }
        else
        {
            throw new AppX("Network Error!");
        }
        progressDialog.dismiss();

    }

    private String urlBuilder(String key)
    {
        return ApplicationConstants.WINERY_MAP_WS_URL + ApplicationConstants.WINERY_MAP_WS_DETAILS + key;
    }
}
