package org.ironrabbit.type;


import android.content.Context;
import android.graphics.Typeface;
import android.text.ClipboardManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class CustomTypefaceTextView extends TextView {

    Context mContext;
    private boolean mDidInit = false;
    
    public CustomTypefaceTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;

       init();
    }

    
    
    public CustomTypefaceTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		  mContext = context;

	       init();
	}



	public CustomTypefaceTextView(Context context) {
		super(context);
		  mContext = context;

	       init();
	}



	private void init() {
    	mDidInit = true;
    	

    	if (mContext == null)
    		mContext = getContext();
    	
    
        setTypeface(CustomTypefaceManager.getCurrentTypeface(getContext()));
        
        setOnLongClickListener(new View.OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				  ClipboardManager cm = (ClipboardManager)mContext.getSystemService(Context.CLIPBOARD_SERVICE);
				  
				  String shareText = getText().toString();
				  
				  if (CustomTypefaceManager.precomposeRequired())
					  shareText = TibConvert.convertPrecomposedTibetanToUnicode(shareText,0,shareText.length());
	               
				  cm.setText(shareText);
	              Toast.makeText(mContext, "Text copied", Toast.LENGTH_SHORT).show();
	            return true;
			}
        });
        
    }
    


	@Override
	public void setText(CharSequence text, BufferType type) {
		if (!mDidInit)
        	init();
		
		String result = text.toString();
		
		if (CustomTypefaceManager.precomposeRequired())
		{
			result = CustomTypefaceManager.handlePrecompose(result);
		
			super.setText(result, type);
		}
		
		super.setText(result,type);
	}
	
	

}