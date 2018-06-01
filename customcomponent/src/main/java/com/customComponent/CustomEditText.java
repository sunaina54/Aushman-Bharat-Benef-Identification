package com.customComponent;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.EditText;

import java.util.Hashtable;

public class CustomEditText extends EditText { 
    private static final String asset = "OpenSansRegular.ttf";
    TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // TODO Auto-generated method stub
            CustomEditText.this.setBackgroundResource(R.drawable.onfocus_edittext_shape);
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // TODO Auto-generated method stub

        }

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub
//			CustomEditText.this.setBackground(getResources().getDrawable(R.drawable.rounded_shape_edittext));

        }
    };

    public CustomEditText(Context context) {
        super(context);
        this.addTextChangedListener(textWatcher);

        // TODO Auto-generated constructor stub
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.addTextChangedListener(textWatcher);
        setCustomFont(context, asset);
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.addTextChangedListener(textWatcher);
        setCustomFont(context, asset);
    }

    public boolean setCustomFont(Context ctx, String asset) {

        setTypeface(TypeFacee.get(ctx, asset));
        return true;
    }

}

class TypeFace {
    private static final String TAG = "Typefaces";

    private static final Hashtable<String, Typeface> cache = new Hashtable<String, Typeface>();

    public static Typeface get(Context c, String assetPath) {
        synchronized (cache) {
            if (!cache.containsKey(assetPath)) {
                try {
                    Typeface t = Typeface.createFromAsset(c.getAssets(),
                            assetPath);
                    cache.put(assetPath, t);
                } catch (Exception e) {
                    Log.e(TAG, "Could not get typeface '" + assetPath
                            + "' because " + e.getMessage());
                    return null;
                }
            }
            return cache.get(assetPath);
        }
    }
}



