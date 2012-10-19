package org.ironrabbit.type;

import org.lobsangmonlam.dictionary.TibConvert;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.BufferType;

public class CustomTypefaceEditTextView extends EditText {

    Context context;
    private boolean mInit = false;
    
    public CustomTypefaceEditTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

       init();
    }
    
    

    public CustomTypefaceEditTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		 this.context = context;

	       init();
	}



	public CustomTypefaceEditTextView(Context context) {
		super(context);
		 this.context = context;

	       init();
	}


	private void init() {
        
    	mInit = true;
    	
    	if (context == null)
    		context = getContext();
    
        setTypeface(CustomTypefaceManager.getCurrentTypeface(getContext()));
    	    
    }

    @Override
	public void setText(CharSequence text, BufferType type) {
		
    	if (!mInit)
    		init();
    	
		String newText = text.toString().trim();
		
		newText = TibConvert.convertUnicodeToPrecomposedTibetan(text.toString());
		super.setText(newText, type);
	}

}