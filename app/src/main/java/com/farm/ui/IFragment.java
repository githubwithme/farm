package com.farm.ui;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.app.AppManager;
import com.farm.bean.Apk;
import com.farm.bean.Result;
import com.farm.bean.commembertab;
import com.farm.common.SqliteDb;
import com.farm.widget.CircleImageView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.service.UpdateApk;
import com.share.AndroidShare;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * @author :hc-sima
 * @version :1.0
 * @createTime：2015-8-14 下午2:19:43
 * @description :主界面
 */
@EFragment
public class IFragment extends Fragment
{
	TimeThread timethread;
	Fragment mContent = new Fragment();
	@ViewById
	FrameLayout fl_new;
	@ViewById
	TextView tv_exist;
	@ViewById
	TextView tv_makemap;
	@ViewById
	TextView tv_resetMapData;
	@ViewById
	CircleImageView circle_img;
	@ViewById
	TextView tv_changepwd;
	@ViewById
	LinearLayout ll_edituser;

	@Click
	void tv_detail()
	{
		Intent intent = new Intent(getActivity(), ShowUserInfo_.class);
		startActivity(intent);
	}
	@Click
	void tv_makemap()
	{
		Intent intent = new Intent(getActivity(), CZ_MakeMap_MakeLayer_.class);
		startActivity(intent);

//		Intent intent = new Intent(getActivity(), MapUtils_.class);
//		startActivity(intent);
	}
	@Click
	void tv_resetMapData()
	{
		commembertab commembertab = AppContext.getUserInfo(getActivity());
		SqliteDb.resetmapdata(getActivity());
        SqliteDb.initPark(getActivity());
        SqliteDb.initArea(getActivity());
        SqliteDb.initContract(getActivity());
        SqliteDb.startBreakoff(getActivity(), commembertab.getuId());
		Toast.makeText(getActivity(), "重置成功！", Toast.LENGTH_SHORT).show();
	}
	@Click
	void tv_edituser()
	{
		Intent intent = new Intent(getActivity(), EditUserInfo_.class);
		startActivity(intent);
	}

	@Click
	void tv_yj()
	{
		Intent intent = new Intent(getActivity(), YiJianFanKui_.class);
		startActivity(intent);
	}

	@Click
	void tv_bz()
	{
		Intent intent = new Intent(getActivity(), Helper_.class);
		startActivity(intent);
	}

	@Click
	void tv_gy()
	{
		Intent intent = new Intent(getActivity(), GuanYu_.class);
		startActivity(intent);
	}

	@Click
	void tv_share()
	{
		// Intent inte = new Intent(Intent.ACTION_SEND);
		// inte.setType("image/*");
		// inte.putExtra(Intent.EXTRA_SUBJECT, "Share");
		// inte.putExtra(Intent.EXTRA_TEXT,
		// "I would like to share this with you...");
		// inte.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		// startActivity(Intent.createChooser(inte, "good"));
		String imgPath = Environment.getExternalStorageDirectory().getPath() + "/Farm/IMAGE/IMG_20151103_212607.jpg";
		AndroidShare as = new AndroidShare(getActivity(), "这个应用不错喔，快来使用吧!点击下载http://www.farmm.cn/upload/farm.apk", "");
		as.show();
	}

	@Click
	void tv_renewversion()
	{
		Intent intent = new Intent(getActivity(), UpdateApk.class);
		intent.setAction(UpdateApk.ACTION_NOTIFICATION_CONTROL);
		getActivity().startService(intent);
	}

	@Click
	void tv_changepwd()
	{
		Intent intent = new Intent(getActivity(), ChangePwd_.class);
		startActivity(intent);

	}

	@Click
	void tv_exist()
	{
		CleanLoginInfo();
		AppManager.getAppManager().AppExit(getActivity());
		NotificationManager manger =  (NotificationManager) getActivity().getSystemService(getActivity().NOTIFICATION_SERVICE);
		manger.cancel(101);
		manger.cancel(100);
	}

	@AfterViews
	void afterOncreate()
	{
		getListData();
		commembertab commembertab = AppContext.getUserInfo(getActivity());
//		BitmapHelper.setImageViewBackground(getActivity(), circle_img,AppConfig.baseurl+ commembertab.getimgurl());
//		BitmapHelper.setImageViewBackground(getActivity(), circle_img, AppConfig.baseurl + "/upload/201511/02/201511021602504091.jpg");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.ifragment, container, false);
		return rootView;
	}

	public void switchContent(Fragment from, Fragment to)
	{
		if (mContent != to)
		{
			mContent = to;
			FragmentTransaction transaction = getFragmentManager().beginTransaction();
			if (!to.isAdded())
			{ // 先判断是否被add过
				transaction.hide(from).add(R.id.container_weather, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
			} else
			{
				transaction.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
			}
		}
	}

	private void CleanLoginInfo()
	{
		SharedPreferences sp = getActivity().getSharedPreferences("userInfo", getActivity().MODE_PRIVATE);
		String userName = sp.getString("userName", "");
		commembertab commembertab = (com.farm.bean.commembertab) SqliteDb.getCurrentUser(getActivity(), commembertab.class, userName);
		commembertab.setuserPwd("");
		commembertab.setAutoLogin("0");
		SqliteDb.existSystem(getActivity(), commembertab);
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
						Thread.sleep(60000);
						starttime = starttime + 1000;
						if (fl_new.isShown())
						{
						} else
						{
							getListData();
						}
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

	private void getListData()
	{
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("action", "getVersion");
		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
		{
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo)
			{
				List<Apk> listNewData = null;
				Result result = JSON.parseObject(responseInfo.result, Result.class);
				if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
				{
					if (result.getAffectedRows() != 0)
					{
						listNewData = JSON.parseArray(result.getRows().toJSONString(), Apk.class);
						if (listNewData != null)
						{
							Apk apk = listNewData.get(0);
							PackageInfo packageInfo = null;
							try
							{
								packageInfo = getActivity().getApplicationContext().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
							} catch (NameNotFoundException e)
							{
								e.printStackTrace();
							}
							String localVersion = packageInfo.versionName;
							if (localVersion.equals(apk.getVersion()))
							{
							} else
							{
								fl_new.setVisibility(View.VISIBLE);
							}
						}
					} else
					{
						listNewData = new ArrayList<Apk>();
					}
				} else
				{
					AppContext.makeToast(getActivity(), "error_connectDataBase");
					return;
				}
			}

			@Override
			public void onFailure(HttpException error, String msg)
			{
				AppContext.makeToast(getActivity(), "error_connectServer");
			}
		});
	}
}
