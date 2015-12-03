package com.farm.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.farm.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment
public class SpeedFragment extends Fragment implements OnClickListener
{
	ImageView iv_status;
	TextView tv_status;
	TextView tv_roles;
	@ViewById
	LinearLayout ll_container;
	@ViewById
	HorizontalScrollView hs_container;
	@ViewById
	ImageView iv_start;
	@ViewById
	TextView tv_startroles;

	@AfterViews
	void afterOncreate()
	{
		addStatus("已审核", "会计");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.speedfragment, container, false);
		return rootView;
	}

	@Override
	public void onClick(View v)
	{

		switch (v.getId())
		{
		default:
			break;
		}
	}

	private void addStatus(String status, String roles)
	{
		for (int i = 0; i < 5; i++)
		{
			LayoutInflater listContainer = LayoutInflater.from(getActivity());
			View statusView = listContainer.inflate(R.layout.statuslayout, null);
			findView(statusView);
			LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
			statusView.setLayoutParams(p);
			ll_container.addView(statusView);
		}

	}

	private void findView(View statusView)
	{
		iv_status = (ImageView) statusView.findViewById(R.id.iv_status);
		tv_status = (TextView) statusView.findViewById(R.id.tv_status);
		tv_roles = (TextView) statusView.findViewById(R.id.tv_roles);
	}
}
