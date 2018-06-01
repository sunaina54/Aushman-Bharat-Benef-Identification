package com.customComponent;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;

public class CustomDialog extends Dialog {


    public CustomDialog(Context context, int resourceId) {
        super(context);
        this.setContentView(resourceId);
        this.setCancelable(false);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

    }

}
