package com.farm.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.farm.R;
import com.farm.bean.jobtab;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.ncz_cz_jobdetail)
public class NCZ_CZ_JobDetail extends Activity
{

	@ViewById
	LinearLayout ll_more;
	@ViewById
	TextView tv_score;
	@ViewById
	TextView tv_importance;
	@ViewById
	TextView tv_scorenote;
	@ViewById
	TextView btn_score;
	@ViewById
	TextView tv_tip_yl;
	@ViewById
	TextView tv_tip_nz;
	@ViewById
	TextView tv_jobname;
	@ViewById
	TextView tv_nz;
	@ViewById
	TextView tv_yl;
	@ViewById
	TextView tv_qx;
	@ViewById
	TextView tv_note;

	jobtab jobtab;
	@ViewById
	ImageButton btn_back;

	@Click
	void btn_score()
	{
		Intent intent = new Intent(this, CZ_PG_Assess_.class);
		intent.putExtra("bean", jobtab);
		startActivity(intent);
	}

	@Click
	void btn_back()
	{
		finish();
	}

	@AfterViews
	void afterOncreate()
	{
		showData(jobtab);
		// if (!jobtab.getaudioJobExecPath().equals(""))
		// {
		// downloadLuYin(AppConfig.baseurl + jobtab.getaudioJobExecPath(),
		// AppConfig.MEDIA_PATH +
		// jobtab.getaudioJobExecPath().substring(jobtab.getaudioJobExecPath().lastIndexOf("/"),
		// jobtab.getaudioJobExecPath().length()));
		// }
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		getActionBar().hide();
		jobtab = getIntent().getParcelableExtra("bean");
	}

	private void showData(jobtab jobtab)
	{
		if (jobtab.getstdJobType().equals("0") || jobtab.getstdJobType().equals("-1"))
		{
			ll_more.setVisibility(View.GONE);
			tv_jobname.setText(jobtab.getnongziName());
		} else
		{
			tv_jobname.setText(jobtab.getstdJobTypeName() + "——" + jobtab.getstdJobName());
			tv_nz.setText(jobtab.getnongziName());
			tv_yl.setText(jobtab.getamount());
			if (jobtab.getImportance().equals("0"))
			{
				tv_importance.setText("一般");
			} else if (jobtab.getImportance().equals("1"))
			{
				tv_importance.setText("重要");
			} else if (jobtab.getImportance().equals("2"))
			{
				tv_importance.setText("非常重要");
			}
		}
		// if (jobtab.getassessScore().equals("-1"))
		// {
		// tv_score.setText("暂无");
		// } else
		// {
		// tv_score.setText(jobtab.getassessScore());
		// }
		if (jobtab.getassessScore().equals("-1"))
		{
			tv_score.setText("暂无");
		} else
		{
			tv_score.setTextColor(NCZ_CZ_JobDetail.this.getResources().getColor(R.color.red));
			if (jobtab.getassessScore().equals("0"))
			{
				tv_score.setText("不合格");
			} else if (jobtab.getassessScore().equals("8"))
			{
				tv_score.setText("合格");
			} else if (jobtab.getassessScore().equals("10"))
			{
				tv_score.setText("优");
			}
		}
		tv_note.setText(jobtab.getjobNote());
		tv_scorenote.setText(jobtab.getassessNote());

	}

}
