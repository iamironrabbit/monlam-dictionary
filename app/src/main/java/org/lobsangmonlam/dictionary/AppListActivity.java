package org.lobsangmonlam.dictionary;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;

import java.util.List;

public class AppListActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_list);
    }

    public void installZomApp (View view)
    {
        if (hasGooglePlay())
            openURL(getString(R.string.app_zom_play_link));
        else
            openURL(getString(R.string.app_zom_direct_link));
    }

    public void learnMoreZomApp (View view)
    {
        openURL(getString(R.string.app_zom_learn_more));
    }

    public void installKbApp (View view)
    {
        if (hasGooglePlay())
            openURL(getString(R.string.app_kb_play_link));
        else
            openURL(getString(R.string.app_kb_direct_link));

    }

    public void learnMoreKbApp (View view)
    {
        openURL(getString(R.string.app_kb_learn_more));
    }

    public void openURL (String url)
    {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }

    private static final String GooglePlayStorePackageNameNew = "com.android.vending";

    boolean hasGooglePlay() {
        try {
            getApplication().getPackageManager().getPackageInfo(GooglePlayStorePackageNameNew, 0);
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
        return true;


    }
}
