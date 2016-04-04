package com.farm.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

/**
 * context layout button
 *
 * @author Administrator
 */
public class CustomDialog_EditSaleInInfo extends Dialog
{
    Context context;
    View layout;

    public CustomDialog_EditSaleInInfo(Context context, int theme, View layout)
    {
        super(context, theme);
        this.context = context;
        this.layout = layout;
    }

    public CustomDialog_EditSaleInInfo(Context context, int theme)
    {
        super(context, theme);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.setContentView(layout);
        this.setCanceledOnTouchOutside(false);
    }


}