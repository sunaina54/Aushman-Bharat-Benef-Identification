package com.customComponent;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class CustomFontBold extends TextView {

    private static final String asset = "opensansbold.ttf";

    public CustomFontBold(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public CustomFontBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomFont(context, asset);
    }

    public CustomFontBold(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setCustomFont(context, asset);
    }

    public boolean setCustomFont(Context ctx, String asset) {

        setTypeface(TypeFaceBold.get(ctx, asset));
        return true;
    }

}

/*class TypeFaceBold {
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
}*/

