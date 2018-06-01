package com.customComponent.customEditText;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.EditText;

public class CustomEditText extends EditText {

    public CustomEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomEditText(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface font = Typeface.createFromAsset(getContext().getAssets(), "font/OpenSans-Regular.ttf");
            setTypeface(font, Typeface.NORMAL);
        }
    }
  @Override
    public void setError(CharSequence error, Drawable icon) {
        setCompoundDrawables(null, null, icon, null);
        //setBackgroundResource(R.drawable.rounded_shape_edittext_for_validate);
    }
}




