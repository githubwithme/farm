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
		url = getIntent().getStringExtra("url");
	}
}
