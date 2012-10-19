package org.lobsangmonlam.dictionary;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

public class DictionaryTabActivity extends TabActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		TabHost host=getTabHost();
		
		Intent intent = new Intent(this, DictionarySearchActivity.class);
		intent.putExtra("db", "tbtotb");
		intent.putExtra("dbresid", R.raw.tbtotb);
	
		host.addTab(host.newTabSpec("tbtotb") .setIndicator(getString(R.string.tab_tb))
		.setContent(intent));
		
		intent = new Intent(this, DictionarySearchActivity.class);
		intent.putExtra("db", "entotb");
		intent.putExtra("dbresid", R.raw.entotb);
		
		host.addTab(host.newTabSpec("entotb")
		.setIndicator(getString(R.string.tab_entotb))
		.setContent(intent));
		
		intent = new Intent(this, DictionarySearchActivity.class);
		intent.putExtra("db", "tbtoen");
		intent.putExtra("dbresid", R.raw.tbtoen);
		
		TabHost.TabSpec tab = host.newTabSpec("tbtoen") .setIndicator(getString(R.string.tab_tbtoen))
		.setContent(intent);
		
				
		host.addTab(tab);
		
	
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}
    
}
