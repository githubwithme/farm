package com.farm.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.farm.R;
import com.farm.bean.commandtab;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.cz_pq_commanddetail)
public class CZ_PQ_CommandDetail extends Activity
{

	@ViewById
	TextView tv_zyts;
	@ViewById
	TextView tv_importance;
	@ViewById
	TextView tv_jobtype;
	@ViewById
	TextView tv_jobname;
	@ViewById
	TextView tv_nz;
	@ViewById
	TextView tv_nz_tip;
	@ViewById
	TextView tv_qx;
	@ViewById
	TextView tv_note;
	@ViewById
	TextView tv_from;
	// @ViewById
	// ImageButton btn_record;
	String filename;
	commandtab commandtab;
	@ViewById
	ImageButton btn_back;
	@ViewById
	LinearLayout ll_nz_tip;
	@ViewById
	RelativeLayout rl_jobtype_tip;
	@ViewById
	RelativeLayout rl_jobname_tip;

	@Click
	void btn_back()
	{
		finish();
	}

	// @Click
	// void btn_record()
	// {
	// if (filename != null)
	// {
	// MediaHelper.playRecord(new File(AppConfig.MEDIA_PATH + filename));
	//
	// } else
	// {
	// Toast.makeText(this, "暂无录音", Toast.LENGTH_SHORT).show();
	// }
	//
	// }

	@AfterViews
	void afterOncreate()
	{

		showData(commandtab);
		// if (commandtab.getcommFromVPath() != null &&
		// !commandtab.getcommFromVPath().equals(""))
		// {
		// filename =
		// commandtab.getcommFromVPath().toString().substring(commandtab.getcommFromVPath().lastIndexOf("/"),
		// commandtab.getcommFromVPath().length());
		// downloadLuYin(AppConfig.testurl + commandtab.getcommFromVPath(),
		// AppConfig.MEDIA_PATH + filename);
		// } else
		// {
		// Toast.makeText(this, "暂无录音", Toast.LENGTH_SHORT).show();
		// }
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		getActionBar().hide();
		commandtab = getIntent().getParcelableExtra("bean");
	}

	private void showData(commandtab commandtab)
	{
		if (commandtab.getstdJobType().equals("0") || commandtab.getstdJobType().equals("-1"))
		{
			ll_nz_tip.setVisibility(View.GONE);
			rl_jobtype_tip.setVisibility(View.GONE);
			rl_jobname_tip.setVisibility(View.GONE);
		} else
		{
			String[] nongzi = commandtab.getnongziName().split(",");
			String[] yl = commandtab.getamount().split(",");
			String flyl = "";
			for (int i = 0; i < nongzi.length; i++)
			{
				flyl = flyl + nongzi[i] + "：" + yl[i] + ";";
			}
			tv_nz.setText(flyl);
			tv_jobtype.setText(commandtab.getstdJobTypeName());
			tv_jobname.setText(commandtab.getstdJobName());
		}
		if (!commandtab.getcommComDate().equals(""))
		{
			if (commandtab.getcommComDate().length() > 10)
			{
				tv_qx.setText(commandtab.getcommComDate().substring(0, commandtab.getcommComDate().lastIndexOf(" ")));
			} else
			{
				tv_qx.setText(commandtab.getcommComDate());
			}
		}
		tv_note.setText(commandtab.getcommNote());
		tv_from.setText(commandtab.getcommFromName());
		if (commandtab.getimportance().equals("0"))
		{
			tv_importance.setText("一般");
		} else if (commandtab.getimportance().equals("1"))
		{
			tv_importance.setText("重要");
		} else if (commandtab.getimportance().equals("2"))
		{
			tv_importance.setText("非常重要");
		}

	}
	// public void downloadLuYin(String path, final String target)
	// {
	// HttpUtils http = new HttpUtils();
	// http.download(path, target, true, true, new RequestCallBack<File>()
	// {
	// @Override
	// public void onFailure(HttpException error, String msg)
	// {
	// if (msg.equals("maybe the file has downloaded completely"))
	// {
	// btn_record.setVisibility(View.VISIBLE);
	// } else
	// {
	// Toast.makeText(Common_CommandDetail.this, "录音下载失败！找不到该文件!",
	// Toast.LENGTH_SHORT).show();
	// }
	// }
	//
	// @Override
	// public void onSuccess(ResponseInfo<File> responseInfo)
	// {
	// btn_record.setVisibility(View.VISIBLE);
	// }
	// });
	//
	// }

}
