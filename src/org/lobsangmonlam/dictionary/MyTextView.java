package org.lobsangmonlam.dictionary;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;
import android.widget.TextView.BufferType;

public class MyTextView extends TextView {

    Context context;
    private static Typeface font;

    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        
       init();
    }

    private void init() {
    	
    	if (font == null)
    		 font = Typeface.createFromAsset(context.getAssets(),  MonlamConstants.DEFAULT_FONT);
    	
        setTypeface(font);
        
    }
    

	@Override
    public void setTypeface(Typeface tf) {

        super.setTypeface(tf);
        
    }

}