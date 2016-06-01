package org.ironrabbit.type;


import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

public class CustomTypefaceEditText extends EditText {

    Context context;
    private boolean mInit = false;
    
    private String lastChange = "";
    
    public CustomTypefaceEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

       init();
    }
    
    

    public CustomTypefaceEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		 this.context = context;

	       init();
	}



	public CustomTypefaceEditText(Context context) {
		super(context);
		 this.context = context;

	       init();
	}


	private void init() {
        
    	mInit = true;
    	
    	if (context == null)
    		context = getContext();
    

        setTypeface(CustomTypefaceManager.getCurrentTypeface(getContext()));
    	 
    	if (CustomTypefaceManager.precomposeRequired())
    	{
    		/*
	    	setOnTouchListener(new OnTouchListener ()
	    	{
	
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					
					if (event.getAction() == MotionEvent.ACTION_UP)
					{
						setText(TibConvert.convertUnicodeToPrecomposedTibetan(getText().toString()));
					}
					
					return false;
				}
	    		
	    	});*/
    	}
       
    }

    @Override
	public void setText(CharSequence text, BufferType type) {
		
    	if (!mInit)
    		init();
    	
		String result = text.toString();
		
		if (CustomTypefaceManager.precomposeRequired())
		{
			result = CustomTypefaceManager.handlePrecompose(result);
		
		}
		
		super.setText(result, type);
	}
    
    

}