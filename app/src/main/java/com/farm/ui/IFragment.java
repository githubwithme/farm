package com.farm.ui;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.app.AppManager;
import com.farm.bean.Apk;
import com.farm.bean.BreakOff;
import com.farm.bean.Result;
import com.farm.bean.commembertab;
import com.farm.common.SqliteDb;
import com.farm.common.utils;
import com.farm.widget.MyDialog;
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

import java.io.File;
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
    commembertab commembertab;
    public static final int NOTIFICATIN_ID = 100;
    private Notification mNotification;
    private NotificationManager manager;
    private PendingIntent pendingIntent;
    private Intent intent;
    MyDialog myDialog;
    //	TimeThread timethread;
    Fragment mContent = new Fragment();
    @ViewById
    FrameLayout fl_new;
    @ViewById
    TextView tv_exist;
    @ViewById
    TextView tv_makemap;
    @ViewById
    RelativeLayout rl_userinfo;
    //    @ViewById
//    TextView tv_username;
    @ViewById
    LinearLayout ll_startBreakoff;
    //    @ViewById
//    CircleImageView circle_img;
    @ViewById
    TextView tv_changepwd;
//    @ViewById
//    LinearLayout ll_edituser;

//    @Click
//    void tv_detail()
//    {
//        Intent intent = new Intent(getActivity(), ShowUserInfo_.class);
//        startActivity(intent);
//    }

    @Click
    void tv_makemap()
    {
        Intent intent = new Intent(getActivity(), MakeLayer_Farm_.class);
        startActivity(intent);

//		Intent intent = new Intent(getActivity(), MapUtils_.class);
//		startActivity(intent);
    }

    //	@Click
//	void tv_resetMapData()
//	{
//		startBreakoff();
//		commembertab commembertab = AppContext.getUserInfo(getActivity());
//		SqliteDb.resetmapdata(getActivity());
//        SqliteDb.initPark(getActivity());
//        SqliteDb.initArea(getActivity());
//        SqliteDb.initContract(getActivity());
//		Toast.makeText(getActivity(), "重置成功！", Toast.LENGTH_SHORT).show();
//	}
    @Click
    void tv_startBreakoff()
    {
        startBreakoff();
//		commembertab commembertab = AppContext.getUserInfo(getActivity());
//        SqliteDb.startBreakoff(getActivity(), commembertab.getuId());
//		Toast.makeText(getActivity(), "已经初始化断蕾图层成功！", Toast.LENGTH_SHORT).show();
    }

    @Click
    void rl_userinfo()
    {
        Intent intent = new Intent(getActivity(), ShowUserInfo_.class);
        intent.putExtra("type", "0");
        intent.putExtra("userid", commembertab.getId());
        startActivity(intent);
    }

    @Click
    void rl_feedback()
    {
        Intent intent = new Intent(getActivity(), YiJianFanKui_.class);
        startActivity(intent);
    }

    @Click
    void rl_help()
    {
        Intent intent = new Intent(getActivity(), Helper_.class);
        startActivity(intent);
    }

    @Click
    void rl_gy()
    {
        Intent intent = new Intent(getActivity(), GuanYu_.class);
        startActivity(intent);
    }

    @Click
    void rl_share()
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
    void rl_renewversion()
    {
        getListData();
        /*Intent intent = new Intent(getActivity(), UpdateApk.class);
        intent.setAction(UpdateApk.ACTION_NOTIFICATION_CONTROL);
		getActivity().startService(intent);*/

    }

    @Click
    void rl_changepwd()
    {
        Intent intent = new Intent(getActivity(), ChangePwd_.class);
        startActivity(intent);

    }

    @Click
    void rl_exist()
    {
        CleanLoginInfo();
        AppManager.getAppManager().AppExit(getActivity());
        NotificationManager manger = (NotificationManager) getActivity().getSystemService(getActivity().NOTIFICATION_SERVICE);
        manger.cancel(101);
        manger.cancel(100);
    }

    @AfterViews
    void afterOncreate()
    {
        commembertab = AppContext.getUserInfo(getActivity());
//        tv_username.setText(commembertab.getparkName() + commembertab.getareaName() + "-" + commembertab.getuserlevelName() + ":" + commembertab.getrealName());
    /*    if (commembertab.getnlevel().equals("0"))
        {
            ll_startBreakoff.setVisibility(View.VISIBLE);
        }*/
        getNew();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.ifragment, container, false);
        /*IntentFilter intentfilter_update = new IntentFilter(AppContext.BROADCAST_SHOWDIALOG);
        getActivity().registerReceiver(receiver_update, intentfilter_update);*/
        return rootView;
    }

    BroadcastReceiver receiver_update = new BroadcastReceiver()// 从扩展页面返回信息
    {
        @SuppressWarnings("deprecation")
        @Override
        public void onReceive(Context context, Intent intent)
        {
//			intent.getStringArrayExtra()
//			View dialog_layout = (LinearLayout) LayoutInflater.from(UpdateApk.this).inflate(R.layout.customdialog_callback, null);
            View dialog_layout = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.customdialog_callback, null);
            myDialog = new MyDialog(getActivity(), R.style.MyDialog, dialog_layout, "系统更新", "是否系统更新?", "确认", "取消", new MyDialog.CustomDialogListener()
            {
                @Override
                public void OnClick(View v)
                {
                    switch (v.getId())
                    {
                        case R.id.btn_sure:
                            getListData();
                            myDialog.dismiss();
                            break;
                        case R.id.btn_cancle:

                            myDialog.dismiss();
                            break;
                    }
                }
            });
            myDialog.show();
        }
    };

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

//	class TimeThread extends Thread
//	{
//		private boolean isSleep = true;
//		private boolean stop = false;
//
//		public void run()
//		{
//			Long starttime = 0l;
//			while (!stop)
//			{
//				if (isSleep)
//				{
//				} else
//				{
//					try
//					{
//						Thread.sleep(60000);
//						starttime = starttime + 1000;
//						if (fl_new.isShown())
//						{
//						} else
//						{
//							getListData();
//						}
//					} catch (InterruptedException e)
//					{
//						e.printStackTrace();
//					}
//				}
//			}
//		}
//
//		public void setSleep(boolean sleep)
//		{
//			isSleep = sleep;
//		}
//
//		public void setStop(boolean stop)
//		{
//			this.stop = stop;
//		}
//	}

	/*private void getListData()
    {
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("action", "getVersion");
		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				List<Apk> listNewData = null;
				Result result = JSON.parseObject(responseInfo.result, Result.class);
				if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
				{
					if (result.getAffectedRows() != 0) {
						listNewData = JSON.parseArray(result.getRows().toJSONString(), Apk.class);
						if (listNewData != null) {
							Apk apk = listNewData.get(0);
							PackageInfo packageInfo = null;
							try {
								packageInfo = getActivity().getApplicationContext().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
							} catch (NameNotFoundException e) {
								e.printStackTrace();
							}
							String localVersion = packageInfo.versionName;
							if (localVersion.equals(apk.getVersion())) {
							} else {
								fl_new.setVisibility(View.VISIBLE);
							}
						}
					} else {
						listNewData = new ArrayList<Apk>();
					}
				} else {
					AppContext.makeToast(getActivity(), "error_connectDataBase");
					return;
				}
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				AppContext.makeToast(getActivity(), "error_connectServer");
			}
		});
	}*/

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
                String a = responseInfo.result;
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
                                Toast.makeText(getActivity(), "当前版本已经是最新版本!", Toast.LENGTH_SHORT).show();
                            } else
                            {
                                View dialog_layout = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.customdialog_callback, null);
                                myDialog = new MyDialog(getActivity(), R.style.MyDialog, dialog_layout, "系统更新", "是否系统更新?", "确认", "取消", new MyDialog.CustomDialogListener()
                                {
                                    @Override
                                    public void OnClick(View v)
                                    {
                                        switch (v.getId())
                                        {
                                            case R.id.btn_sure:
                                                Intent intent = new Intent(getActivity(), UpdateApk.class);
                                                intent.setAction(UpdateApk.ACTION_NOTIFICATION_CONTROL);
                                                getActivity().startService(intent);
                                                myDialog.dismiss();
                                                break;
                                            case R.id.btn_cancle:

                                                myDialog.dismiss();
                                                break;
                                        }
                                    }
                                });
                                myDialog.show();
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

    private void getNew()
    {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("action", "getVersion");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String a = responseInfo.result;
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
                                fl_new.setVisibility(View.GONE);
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

    public void downloadApk(String path, final String target)
    {
        String sss = path;
        HttpUtils http = new HttpUtils();
        http.download(path, target, true, true, new RequestCallBack<File>()
        {
            @Override
            public void onStart()
            {
                super.onStart();
            }

            @Override
            public void onLoading(long total, long current, boolean isUploading)
            {
                super.onLoading(total, current, isUploading);
                int jd = Integer.valueOf(utils.getRateoffloat(current, total));
                mNotification.contentView.setTextViewText(R.id.content_view_text1, jd + "%");
                mNotification.contentView.setProgressBar(R.id.content_view_progress, 100, jd, false);
//          startForeground(NOTIFICATIN_ID, mNotification);
            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                if (msg.equals("maybe the file has downloaded completely"))
                {
                    Toast.makeText(getActivity(), "下载成功!", Toast.LENGTH_SHORT).show();
                } else
                {
                    Toast.makeText(getActivity(), "更新失败!" + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onSuccess(ResponseInfo<File> responseInfo)
            {
                File file = new File(target);
                if (!file.exists())
                {
                }
                mNotification.contentView.setTextViewText(R.id.content_view_text1, "下载成功！请点击安装！");
                mNotification.defaults = Notification.DEFAULT_SOUND;
                Toast.makeText(getActivity(), "下载完成，请在通知栏点击安装！", Toast.LENGTH_LONG).show();
                Intent updateIntent = new Intent(Intent.ACTION_VIEW);
                updateIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                updateIntent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                startActivity(updateIntent);
                pendingIntent = PendingIntent.getActivity(getActivity(), 0, updateIntent, 0);
                mNotification.contentIntent = pendingIntent;
//          startForeground(NOTIFICATIN_ID, mNotification);
            }
        });
    }

    public void createNotification()
    {
        if (mNotification == null)
        {
            mNotification = new Notification();
            mNotification.icon = R.drawable.logo;
            mNotification.flags |= Notification.FLAG_AUTO_CANCEL;// 表示正处于活动中
            mNotification.flags |= Notification.FLAG_SHOW_LIGHTS;
            intent = new Intent(getActivity(), UpdateApk.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            pendingIntent = PendingIntent.getActivity(getActivity(), 0, intent, 0);
            mNotification.contentIntent = pendingIntent;

            RemoteViews contentView = new RemoteViews(getActivity().getPackageName(), R.layout.notification_layout);

            contentView.setImageViewResource(R.id.content_view_image, R.drawable.logo);
            mNotification.contentView = contentView;
        }
//    startForeground(NOTIFICATIN_ID, mNotification);

    }

    public void startBreakoff()
    {
        commembertab commembertab = AppContext.getUserInfo(getActivity());
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("Year", utils.getYear());
        params.addQueryStringParameter("Starttime", utils.getToday());
        params.addQueryStringParameter("action", "startBreakOff");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String a = responseInfo.result;
                List<BreakOff> listNewData = null;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    String rows = result.getRows().get(0).toString();
                    if (rows.equals("1"))
                    {
                        Toast.makeText(getActivity(), "已经初始化断蕾图层成功！", Toast.LENGTH_SHORT).show();
                    } else if (rows.equals("0"))
                    {
                        Toast.makeText(getActivity(), "今年已经开始断蕾了", Toast.LENGTH_SHORT).show();
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
