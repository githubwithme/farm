package com.farm.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.farm.R;

/**
 * context layout button
 * 
 * @author Administrator
 * 
 */
public class MyDialog extends Dialog implements OnClickListener
{
	CustomDialogListener cdListener;
	Context context;
	Button btn_sure, btn_cancle;
	TextView tv_tips, tv_content;
	View layout;
	String tips, content, btntext_sure, btntext_cancle;

	public MyDialog(Context context, int theme, View layout, String tips, String content, String btntext_sure, String btntext_cancle, CustomDialogListener cdListener)
	{
		super(context, theme);
		this.context = context;
		this.layout = layout;
		this.tips = tips;
		this.content = content;
		this.btntext_sure = btntext_sure;
		this.btntext_cancle = btntext_cancle;
		this.cdListener = cdListener;
	}

	public MyDialog(Context context, int theme)
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
		InitViews();
	}

	private void InitViews()
	{
		btn_sure = (Button) findViewById(R.id.btn_sure);
		btn_sure.setOnClickListener(this);
		btn_cancle = (Button) findViewById(R.id.btn_cancle);
		btn_cancle.setOnClickListener(this);
		tv_tips = (TextView) findViewById(R.id.tv_tips);
		tv_content = (TextView) findViewById(R.id.tv_content);
		tv_tips.setText(tips);
		tv_content.setText(content);
		btn_sure.setText(btntext_sure);
		btn_cancle.setText(btntext_cancle);
	}

	public interface CustomDialogListener
	{
		void OnClick(View view);
	}

	@Override
	public void onClick(View v)
	{
		cdListener.OnClick(v);
		dismiss();
	}
}