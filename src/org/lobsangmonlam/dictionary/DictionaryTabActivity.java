package org.lobsangmonlam.dictionary;

import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

public class DictionaryTabActivity extends TabActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		TabHost host=getTabHost();
		
		Typeface font = Typeface.createFromAsset(getAssets(),  MonlamConstants.DEFAULT_FONT);
	
		Intent intent = new Intent(this, DictionarySearchActivity.class);
		intent.putExtra("db", "tbtotb");
	
		/*
		MyTextView txtTab = new MyTextView(this, null);
		txtTab.setText(getString(R.string.tab_tb));
		txtTab.setPadding(8, 9, 8, 9);
	    txtTab.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
	    */	    
		host.addTab(host.newTabSpec("tbtotb") .setIndicator(getString(R.string.tab_tb))
		.setContent(intent));
		
		intent = new Intent(this, DictionarySearchActivity.class);
		intent.putExtra("db", "entotb");
		
		host.addTab(host.newTabSpec("entotb")
		.setIndicator(getString(R.string.tab_entotb))
		.setContent(intent));
		
		intent = new Intent(this, DictionarySearchActivity.class);
		intent.putExtra("db", "tbtoen");
		
		
		TabHost.TabSpec tab = host.newTabSpec("tbtoen") .setIndicator(getString(R.string.tab_tbtoen))
		.setContent(intent);
		
				
		host.addTab(tab);
		
		TabWidget tw = host.getTabWidget();
		// for changing the text size of first tab
		 RelativeLayout rllf = (RelativeLayout) tw.getChildAt(0);
		 TextView lf = (TextView) rllf.getChildAt(1);
		 lf.setTypeface(font);		 
		 lf.setSingleLine(false);
		 lf.setLineSpacing(0.0f, 1.2f);
		 
		 rllf = (RelativeLayout) tw.getChildAt(1);
		 lf = (TextView) rllf.getChildAt(1);
		 lf.setTypeface(font);		 
		 lf.setSingleLine(false);
		 lf.setLineSpacing(0.0f, 1.2f);
		 
		 rllf = (RelativeLayout) tw.getChildAt(2);
		 lf = (TextView) rllf.getChildAt(1);
		 lf.setTypeface(font);		 
		 lf.setSingleLine(false);
		 lf.setLineSpacing(0.0f, 1.2f);
	}
}
