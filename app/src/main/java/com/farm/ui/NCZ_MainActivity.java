package com.farm.ui;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.app.AppManager;
import com.farm.bean.ExceptionInfo;
import com.farm.bean.HaveReadRecord;
import com.farm.bean.LogInfo;
import com.farm.bean.Result;
import com.farm.bean.commembertab;
import com.farm.common.FontManager;
import com.farm.common.GetMobilePhoneInfo;
import com.farm.common.SqliteDb;
import com.farm.common.utils;
import com.farm.widget.MyDialog;
import com.farm.widget.MyDialog.CustomDialogListener;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.tips.BaseActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.ncz_activity)
public class NCZ_MainActivity extends BaseActivity
{
	MyDialog myDialog;
//	TimeThread timethread;
	int cmd_videoNum;
	Fragment mContent = new Fragment();
	NCZ_MainFragment mainFragment;
//	NCZ_EventList ncz_eventList;
	NCZ_EventofList ncz_eventofList;
	NCZ_CommandList ncz_CommandList;
	NCZ_CommandOfList ncz_commandOfList;
	// NCZ_SaleFragment ncz_SaleFragment;
	// SaleList saleList;
	ProductBatchList productBatchList;
	IFragment iFragment;
	@ViewById
	FrameLayout fl_new;
	@ViewById
	TextView tv_new;
	commembertab commembertab;
	@ViewById
	ImageButton imgbtn_home;
	@ViewById
	ImageButton imgbtn_me;
	@ViewById
	ImageButton imgbtn_product;
	@ViewById
	ImageButton imgbtn_sale;
	@ViewById
	ImageButton imgbtn_money;

	@ViewById
	TextView tv_home;
	@ViewById
	TextView tv_me;
	@ViewById
	TextView tv_product;
	@ViewById
	TextView tv_sale;
	@ViewById
	TextView tv_money;

	@ViewById
	TableLayout tl_home;
	@ViewById
	TableLayout tl_product;
	@ViewById
	TableLayout tl_sale;
	@ViewById
	TableLayout tl_me;
	@ViewById
	TableLayout tl_money;

	@Click
	void tl_home()
	{
		tv_home.setTextColor(getResources().getColor(R.color.red));
		tv_me.setTextColor(getResources().getColor(R.color.menu_textcolor));
		tv_product.setTextColor(getResources().getColor(R.color.menu_textcolor));
		tv_sale.setTextColor(getResources().getColor(R.color.menu_textcolor));
		tv_money.setTextColor(getResources().getColor(R.color.menu_textcolor));

		tl_home.setSelected(true);
		tl_me.setSelected(false);
		tl_product.setSelected(false);
		tl_sale.setSelected(false);
		tl_money.setSelected(false);
		switchContent(mContent, mainFragment);
	}

	@Click
	void tl_product()
	{
		HaveReadRecord haveReadRecord = SqliteDb.getHaveReadRecord(NCZ_MainActivity.this, AppContext.TAG_NCZ_CMD);
		if (haveReadRecord != null)
		{
			SqliteDb.updateHaveReadRecord(NCZ_MainActivity.this, AppContext.TAG_NCZ_CMD, String.valueOf(cmd_videoNum));
			fl_new.setVisibility(View.GONE);
		} else
		{
			SqliteDb.saveHaveReadRecord(NCZ_MainActivity.this, AppContext.TAG_NCZ_CMD, String.valueOf(cmd_videoNum));
		}
		tv_home.setTextColor(getResources().getColor(R.color.menu_textcolor));
		tv_me.setTextColor(getResources().getColor(R.color.menu_textcolor));
		tv_product.setTextColor(getResources().getColor(R.color.red));
		tv_sale.setTextColor(getResources().getColor(R.color.menu_textcolor));
		tv_money.setTextColor(getResources().getColor(R.color.menu_textcolor));

		tl_home.setSelected(false);
		tl_me.setSelected(false);
		tl_product.setSelected(true);
		tl_sale.setSelected(false);
		tl_money.setSelected(false);
//		switchContent(mContent, ncz_CommandList);
		switchContent(mContent, ncz_commandOfList);
	}

	@Click
	void tl_sale()
	{
		tv_home.setTextColor(getResources().getColor(R.color.menu_textcolor));
		tv_me.setTextColor(getResources().getColor(R.color.menu_textcolor));
		tv_product.setTextColor(getResources().getColor(R.color.menu_textcolor));
		tv_sale.setTextColor(getResources().getColor(R.color.bg_text));
		tv_money.setTextColor(getResources().getColor(R.color.menu_textcolor));

		tl_home.setSelected(false);
		tl_me.setSelected(false);
		tl_product.setSelected(false);
		tl_sale.setSelected(true);
		tl_money.setSelected(false);
		// switchContent(mContent, productBatchList);
	}

	@Click
	void tl_money()
	{
		tv_home.setTextColor(getResources().getColor(R.color.menu_textcolor));
		tv_me.setTextColor(getResources().getColor(R.color.menu_textcolor));
		tv_product.setTextColor(getResources().getColor(R.color.menu_textcolor));
		tv_sale.setTextColor(getResources().getColor(R.color.menu_textcolor));
		tv_money.setTextColor(getResources().getColor(R.color.red));

		tl_home.setSelected(false);
		tl_me.setSelected(false);
		tl_product.setSelected(false);
		tl_sale.setSelected(false);
		tl_money.setSelected(true);
//		switchContent(mContent, ncz_eventList);
		switchContent(mContent, ncz_eventofList);

	}

	@Click
	void tl_me()
	{
		tv_home.setTextColor(getResources().getColor(R.color.menu_textcolor));
		tv_me.setTextColor(getResources().getColor(R.color.red));
		tv_product.setTextColor(getResources().getColor(R.color.menu_textcolor));
		tv_sale.setTextColor(getResources().getColor(R.color.menu_textcolor));
		tv_money.setTextColor(getResources().getColor(R.color.menu_textcolor));

		tl_home.setSelected(false);
		tl_me.setSelected(true);
		tl_product.setSelected(false);
		tl_sale.setSelected(false);
		tl_money.setSelected(false);
		switchContent(mContent, iFragment);
	}

	@AfterViews
	void afterOncreate()
	{
		//将错误信息提交
		List<ExceptionInfo> list_exception = SqliteDb.getExceptionInfo(NCZ_MainActivity.this);
		if (list_exception != null)
		{
			for (int i = 0; i < list_exception.size(); i++)
			{
				sendExceptionInfoToServer(list_exception.get(i));
			}
		}
		//将日志信息提交
		List<LogInfo> list_LogInfo = SqliteDb.getLogInfo(NCZ_MainActivity.this);
		if (list_LogInfo != null)
		{
			sendLogInfoToServer(list_LogInfo, GetMobilePhoneInfo.getDeviceUuid(NCZ_MainActivity.this).toString(),utils.getToday());
		}



		List<Integer> guideResourceId = new ArrayList<Integer>();
		guideResourceId.add(R.drawable.yd666);
		guideResourceId.add(R.drawable.yd55555);
		guideResourceId.add(R.drawable.yd555);
		guideResourceId.add(R.drawable.yd444);
		guideResourceId.add(R.drawable.yd333);
		guideResourceId.add(R.drawable.yd222);
		guideResourceId.add(R.drawable.yd001);
		setGuideResId(guideResourceId);// 添加引导页
		tv_home.setTypeface(FontManager.getTypefaceByFontName(NCZ_MainActivity.this, "wsyh.ttf"));
		tv_me.setTypeface(FontManager.getTypefaceByFontName(NCZ_MainActivity.this, "wsyh.ttf"));
		tv_product.setTypeface(FontManager.getTypefaceByFontName(NCZ_MainActivity.this, "wsyh.ttf"));
		tv_sale.setTypeface(FontManager.getTypefaceByFontName(NCZ_MainActivity.this, "wsyh.ttf"));
		tv_money.setTypeface(FontManager.getTypefaceByFontName(NCZ_MainActivity.this, "wsyh.ttf"));
		switchContent(mContent, mainFragment);
		tv_home.setTextColor(getResources().getColor(R.color.red));
		tl_home.setSelected(true);
		getCmdVideoNum();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		getActionBar().hide();
		AppManager.getAppManager().addActivity(NCZ_MainActivity.this);
		commembertab = AppContext.getUserInfo(NCZ_MainActivity.this);

		Bundle bundle = new Bundle();
		bundle.putString("workuserid", commembertab.getId());

		mainFragment = new NCZ_MainFragment_();
		productBatchList = new ProductBatchList_();

//		ncz_CommandList = new NCZ_CommandList_();
//		ncz_CommandList.setArguments(bundle);
		ncz_commandOfList=new NCZ_CommandOfList_();
//		ncz_commandOfList.setArguments(bundle);

//		ncz_eventList = new NCZ_EventList_();
//		ncz_eventList.setArguments(bundle);
		ncz_eventofList=new NCZ_EventofList_();


		iFragment = new IFragment_();

//		timethread = new TimeThread();
//		timethread.setStop(false);
//		timethread.setSleep(false);
//		timethread.start();

//		SqliteDb.InitDbutils(NCZ_MainActivity.this);
	}

	public void switchContent(Fragment from, Fragment to)
	{
		if (mContent != to)
		{
			mContent = to;
			FragmentTransaction transaction = getFragmentManager().beginTransaction();
			if (!to.isAdded())
			{ // 先判断是否被add过
				transaction.hide(from).add(R.id.container, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
			} else
			{
				transaction.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
			}
		}
	}

	private void getCmdVideoNum()
	{
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("workuserid", commembertab.getId());
		params.addQueryStringParameter("action", "commandGetVidioCountByNCZ");
		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
		{
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo)
			{
				String aa = responseInfo.result;
				Result result = JSON.parseObject(responseInfo.result, Result.class);
				if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
				{
					if (result.getAffectedRows() != 0)
					{
						cmd_videoNum = result.getAffectedRows();
						HaveReadRecord haveReadRecord = SqliteDb.getHaveReadRecord(NCZ_MainActivity.this, AppContext.TAG_NCZ_CMD);
						if (haveReadRecord != null)
						{
							String num = haveReadRecord.getNum();
							if (num != null && !num.equals("") && (Integer.valueOf(num) < cmd_videoNum))
							{
								int num_new = cmd_videoNum - Integer.valueOf(num);
//								fl_new.setVisibility(View.VISIBLE);
								tv_new.setText(String.valueOf(num_new));
							}
						} else
						{
							SqliteDb.saveHaveReadRecord(NCZ_MainActivity.this, AppContext.TAG_NCZ_CMD, String.valueOf(cmd_videoNum));
						}
					}
				} else
				{
					AppContext.makeToast(NCZ_MainActivity.this, "error_connectDataBase");
					return;
				}

			}

			@Override
			public void onFailure(HttpException error, String msg)
			{
				String a = error.getMessage();
				AppContext.makeToast(NCZ_MainActivity.this, "error_connectServer");
			}
		});
	}

	class TimeThread extends Thread
	{
		private boolean isSleep = true;
		private boolean stop = false;

		public void run()
		{
			Long starttime = 0l;
			while (!stop)
			{
				if (isSleep)
				{
				} else
				{
					try
					{
						Thread.sleep(AppContext.TIME_REFRESH);
						starttime = starttime + 1000;
						getCmdVideoNum();
					} catch (InterruptedException e)
					{
						e.printStackTrace();
					}
				}
			}
		}

		public void setSleep(boolean sleep)
		{
			isSleep = sleep;
		}

		public void setStop(boolean stop)
		{
			this.stop = stop;
		}
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		AppManager.getAppManager().AppExit(NCZ_MainActivity.this);
//		timethread.setStop(true);
//		timethread.interrupt();
//		timethread = null;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			showExistTip();
		}
		return false;

	}

	private void showExistTip()
	{
		View dialog_layout = getLayoutInflater().inflate(R.layout.customdialog_callback, null);
		myDialog = new MyDialog(NCZ_MainActivity.this, R.style.MyDialog, dialog_layout, "确定退出吗？", "确定退出吗？", "退出", "取消", new CustomDialogListener()
		{
			@Override
			public void OnClick(View v)
			{
				switch (v.getId())
				{
				case R.id.btn_sure:
					myDialog.dismiss();
					AppManager.getAppManager().AppExit(NCZ_MainActivity.this);
					NotificationManager manger =  (NotificationManager) NCZ_MainActivity.this.getSystemService(NOTIFICATION_SERVICE);
					manger.cancel(101);
					manger.cancel(100);
					break;
				case R.id.btn_cancle:
					myDialog.dismiss();
					break;
				}
			}
		});
		myDialog.show();
	}
	private void sendExceptionInfoToServer(final ExceptionInfo exception)
	{
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("UUID", exception.getUuid());
		params.addQueryStringParameter("exceptionInfo", exception.getExceptionInfo());
		params.addQueryStringParameter("userid", exception.getUserid());
		params.addQueryStringParameter("username", exception.getUsername());
		params.addQueryStringParameter("action", "saveAppException");
		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
		{
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo)
			{
				String a = responseInfo.result;
				Result result = JSON.parseObject(responseInfo.result, Result.class);
				if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
				{
					if (result.getAffectedRows() != 0)
					{
						SqliteDb.deleteExceptionInfo(NCZ_MainActivity.this, exception.getExceptionid());
					}
				}
			}

			@Override
			public void onFailure(HttpException error, String msg)
			{
				String a = error.getMessage();
			}
		});

	}
	private void sendLogInfoToServer(final List<LogInfo> list,String deviceuuid,String logday)
	{
		StringBuilder builder = new StringBuilder();
		builder.append("{ \"LogInfoList\": ");
		builder.append(JSON.toJSONString(list));
		builder.append("} ");
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("deviceuuid",deviceuuid);
		params.addQueryStringParameter("logday",logday);
		params.addQueryStringParameter("action", "addLogInfo");
		params.setContentType("application/json");
		try
		{
			params.setBodyEntity(new StringEntity(builder.toString(), "utf-8"));
		} catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		HttpUtils http = new HttpUtils();
		http.configTimeout(60000);
		http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
		{
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo)
			{
				String a = responseInfo.result;
				Result result = JSON.parseObject(responseInfo.result, Result.class);
				if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
				{
					String rows=result.getRows().get(0).toString();
					if (rows.equals("1"))
					{
						SqliteDb.updateLogInfo(NCZ_MainActivity.this, list);
					}
				}
			}

			@Override
			public void onFailure(HttpException error, String msg)
			{
				String a = error.getMessage();
			}
		});

	}
}
