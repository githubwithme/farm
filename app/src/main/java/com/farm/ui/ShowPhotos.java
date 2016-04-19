package com.farm.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

import com.farm.R;
import com.farm.common.BitmapHelper;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.showphotos)
public class ShowPhotos extends Activity
{
	String url;
	@ViewById
	ImageView iv_photos;

	@AfterViews
	void afterOncreate()
	{
		BitmapHelper.setImageViewBackground(this, iv_photos, url);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		getActionBar().hide();
//		String  temp = getIntent().getStringExtra("url");
//		String name=temp.substring(temp.lastIndexOf("/")+1, temp.length());
//
//		String name1=name.substring(6,name.length());
//		url=temp.substring(0, temp.lastIndexOf("/")+1)+name1;
		url= getIntent().getStringExtra("url");
	}
}
