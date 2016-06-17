package org.lobsangmonlam.dictionary;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import org.ironrabbit.type.CustomTypefaceManager;

public class DictionaryTabActivity extends AppCompatActivity implements SearchFragment.OnFragmentInteractionListener {

    private FragmentTabHost mTabHost;

    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		CustomTypefaceManager.loadFromAssets(this);

        setContentView(R.layout.tabs);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        setSupportActionBar(toolbar);

        toolbar.setLogo(R.drawable.toolbaricon);
        toolbar.setCollapsible(true);
        toolbar.setTitle(getTitle());

        mTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);


		Bundle bundle = new Bundle();
        bundle.putString("db", "tbtotb");
        bundle.putInt("dbresid", R.raw.tbtotb);
        mTabHost.addTab(mTabHost.newTabSpec("tbtotb").setIndicator(getString(R.string.tab_tb)),
                SearchFragment.class, bundle);


        bundle = new Bundle();
        bundle.putString("db", "entotb");
        bundle.putInt("dbresid", R.raw.entotb);
        mTabHost.addTab(mTabHost.newTabSpec("entotb").setIndicator(getString(R.string.tab_entotb)),
                SearchFragment.class, bundle);


        bundle = new Bundle();
        bundle.putString("db", "tbtoen");
        bundle.putInt("dbresid", R.raw.tbtoen);
        mTabHost.addTab(mTabHost.newTabSpec("tbtoen").setIndicator(getString(R.string.tab_tbtoen)),
                SearchFragment.class, bundle);
	
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
