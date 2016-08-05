package com.farm.ui;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.farm.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * 图片左右滑动
 *
 * @author hmj
 */
@SuppressLint("NewApi")
@EFragment
public class DialogFragment_WaitTip extends DialogFragment
{
    @ViewById
    FrameLayout fl_loadingtip;
    @ViewById
    RelativeLayout rl_loadingtip;
    @ViewById
    RelativeLayout rl_errortip;
    @ViewById
    TextView tv_errortip;
    @ViewById
    Button btn_refresh;
    @ViewById
    Button btn_close;
    @ViewById
    TextView tv_note;


    @Click
    void btn_refresh()
    {
        rl_loadingtip.setVisibility(View.VISIBLE);
    }

    @Click
    void btn_close()
    {
        getActivity().finish();
    }

    public void loadingTip(String tip)
    {
        fl_loadingtip.setVisibility(View.VISIBLE);
        rl_loadingtip.setVisibility(View.GONE);
        rl_errortip.setVisibility(View.VISIBLE);
        tv_errortip.setText(tip);
    }

    @AfterViews
    void afterOncreate()
    {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        //无标题栏
        Dialog dialog = getDialog();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = dialog.getWindow();
        View rootView = inflater.inflate(R.layout.dialogfragment_waittip, container, false);
        //点击外部消失
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        //设置背景色白色
        window.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        //全屏
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);

        window.setGravity(Gravity.BOTTOM); //可设置dialog的位置
        window.getDecorView().setPadding(0, 0, 0, 0); //消除边距

        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;   //设置宽度充满屏幕
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);

        dialog.setOnKeyListener(new DialogInterface.OnKeyListener()
        {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent)
            {
                if (i == KeyEvent.KEYCODE_BACK)
                {
                    getActivity().finish();
                    return true;
                }// pretend we've processed it
                else
                {
                    return false;
                }// pass on to be processed as normal

            }
        });
        return rootView;
    }


}