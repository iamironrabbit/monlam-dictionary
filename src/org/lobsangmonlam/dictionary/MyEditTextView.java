package org.lobsangmonlam.dictionary;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.TextView;

public class MyEditTextView extends EditText {

    Context context;


    public MyEditTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

       init();
    }

    private void init() {
        Typeface font = Typeface.createFromAsset(context.getAssets(), MonlamConstants.DEFAULT_FONT);
        setTypeface(font);
    }

    @Override
    public void setTypeface(Typeface tf) {

        super.setTypeface(tf);
    }

}