package com.farm.ui;

import android.app.Activity;
import android.os.Bundle;

import com.farm.R;

import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.editplant)
public class EditPlant extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		getActionBar().hide();
	}
}
