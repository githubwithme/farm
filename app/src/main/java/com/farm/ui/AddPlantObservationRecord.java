package com.farm.ui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.farm.R;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.Dictionary;
import com.farm.bean.Result;
import com.farm.bean.commembertab;
import com.farm.bean.jobtab;
import com.farm.bean.planttab;
import com.farm.common.BitmapHelper;
import com.farm.common.utils;
import com.farm.widget.MyDatepicker;
import com.farm.widget.MyDialog;
import com.farm.widget.MyDialog.CustomDialogListener;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.http.client.entity.FileUploadEntity;
import com.media.HomeFragmentActivity;
import com.media.MediaChooser;
import com.wheel.OneWheel;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@EActivity(R.layout.addplantobservationrecord)
public class AddPlantObservationRecord extends Activity
{
	CountDownLatch latch;
	planttab planttab;
	@ViewById
	ProgressBar pb_upload;
	@ViewById
	Button btn_upload;
	@ViewById
	TextView tv_plantName;
	@ViewById
	TextView tv_yNum;
	@ViewById
	EditText et_xNum;
	@ViewById
	EditText et_hNum;
	@ViewById
	EditText et_wNum;
	@ViewById
	TextView tv_yColor;

	@ViewById
	TextView tv_cDate;
	@ViewById
	TextView tv_zDate;
	@ViewById
	EditText et_Ext1;
	@ViewById
	CheckBox cb_plantType;
	List<String> list_picture = new ArrayList<String>();
	MyDialog myDialog;
	@ViewById
	LinearLayout ll_picture;
	@ViewById
	ImageButton imgbtn_addpicture;
	Dictionary dic_comm;

	@Click
	void imgbtn_addpicture()
	{
		Intent intent = new Intent(AddPlantObservationRecord.this, HomeFragmentActivity.class);
		intent.putExtra("type", "picture");
		intent.putExtra("From", "plant");
		startActivity(intent);
	}

	@Click
	void btn_upload()
	{
		btn_upload.setVisibility(View.GONE);
		pb_upload.setVisibility(View.VISIBLE);
		AddData();
	}

	@Click
	void tv_yColor()
	{
		JSONObject jsonObject = utils.parseJsonFile(AddPlantObservationRecord.this, "dictionary.json");
		JSONArray jsonArray = JSONArray.parseArray(jsonObject.getString("color"));
		String[] number = new String[jsonArray.size()];
		String[] firstItemid = new String[jsonArray.size()];
		for (int i = 0; i < jsonArray.size(); i++)
		{
			number[i] = jsonArray.getString(i);
			firstItemid[i] = String.valueOf(i);
		}
		OneWheel.showWheel(AddPlantObservationRecord.this, firstItemid, number, tv_yColor);
	}

	@Click
	void tv_yNum()
	{
		JSONObject jsonObject = utils.parseJsonFile(AddPlantObservationRecord.this, "dictionary.json");
		JSONArray jsonArray = JSONArray.parseArray(jsonObject.getString("number"));
		String[] number = new String[jsonArray.size()];
		String[] firstItemid = new String[jsonArray.size()];
		for (int i = 0; i < jsonArray.size(); i++)
		{
			number[i] = jsonArray.getString(i);
			firstItemid[i] = String.valueOf(i);
		}
		OneWheel.showWheel(AddPlantObservationRecord.this, firstItemid, number, tv_yNum);
	}

	@Click
	void tv_cDate()
	{
		MyDatepicker myDatepicker = new MyDatepicker(AddPlantObservationRecord.this, tv_cDate);
		myDatepicker.getDialog().show();
	}

	@Click
	void tv_zDate()
	{
		MyDatepicker myDatepicker = new MyDatepicker(AddPlantObservationRecord.this, tv_zDate);
		myDatepicker.getDialog().show();
	}

	@AfterViews
	void afterOncreate()
	{
		tv_plantName.setText(planttab.getplantName());
		if (planttab.getplantType().equals("1"))
		{
			cb_plantType.setSelected(true);
		}

		getCommandlist();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		getActionBar().hide();
		planttab = getIntent().getParcelableExtra("bean");
		IntentFilter imageIntentFilter_yz = new IntentFilter(MediaChooser.IMAGE_SELECTED_ACTION_FROM_MEDIA_CHOOSER);
		registerReceiver(imageBroadcastReceiver_yz, imageIntentFilter_yz);
	}

	BroadcastReceiver imageBroadcastReceiver_yz = new BroadcastReceiver()
	{
		@Override
		public void onReceive(Context context, Intent intent)
		{
			for (int i = 0; i < intent.getStringArrayListExtra("list").size(); i++)
			{
				String FJBDLJ = intent.getStringArrayListExtra("list").get(i);
				list_picture.add(FJBDLJ);
				ImageView imageView = new ImageView(AddPlantObservationRecord.this);
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(180, LayoutParams.MATCH_PARENT, 0);
				lp.setMargins(25, 4, 0, 4);
				imageView.setLayoutParams(lp);
				imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
				BitmapHelper.setImageView(AddPlantObservationRecord.this, imageView, FJBDLJ);

				imageView.setTag(FJBDLJ);
				ll_picture.addView(imageView);
				imageView.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						final int index_zp = ll_picture.indexOfChild(v);
						View dialog_layout = (LinearLayout) getLayoutInflater().inflate(R.layout.customdialog_callback, null);
						myDialog = new MyDialog(AddPlantObservationRecord.this, R.style.MyDialog, dialog_layout, "图片", "查看该图片?", "查看", "删除", new CustomDialogListener()
						{
							@Override
							public void OnClick(View v)
							{
								switch (v.getId())
								{
								case R.id.btn_sure:
									// File file = new
									// File(list_SJ_SBXXFJ_picture.get(index_zp).getFJBDLJ());
									// Intent intent = new
									// Intent(Intent.ACTION_VIEW);
									// intent.setDataAndType(Uri.fromFile(file),
									// "image/*");
									// startActivity(intent);
									break;
								case R.id.btn_cancle:
									ll_picture.removeViewAt(index_zp);
									list_picture.remove(index_zp);
									myDialog.dismiss();
									break;
								}
							}
						});
						myDialog.show();
					}
				});
			}
		}
	};

	private void AddData()
	{
		commembertab commembertab = AppContext.getUserInfo(this);
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("userid", commembertab.getId());
		params.addQueryStringParameter("userName", commembertab.getrealName());
		params.addQueryStringParameter("uid", commembertab.getuId());
		params.addQueryStringParameter("parkId", commembertab.getparkId());
		params.addQueryStringParameter("parkName", commembertab.getparkName());
		params.addQueryStringParameter("areaId", planttab.getareaId());
		params.addQueryStringParameter("areaName", planttab.getareaName());
		params.addQueryStringParameter("action", "plantGrowthTabAdd");

		params.addQueryStringParameter("yNum", tv_yNum.getText().toString());
		params.addQueryStringParameter("wNum", et_wNum.getText().toString());
		params.addQueryStringParameter("hNum", et_hNum.getText().toString());
		params.addQueryStringParameter("xNum", et_xNum.getText().toString());
		params.addQueryStringParameter("yColor", tv_yColor.getText().toString());
		params.addQueryStringParameter("Ext1", et_Ext1.getText().toString());
		params.addQueryStringParameter("cDate", tv_cDate.getText().toString());
		params.addQueryStringParameter("zDate", tv_zDate.getText().toString());
		params.addQueryStringParameter("plantId", planttab.getId());
		params.addQueryStringParameter("plantName", tv_plantName.getText().toString());
		if (cb_plantType.isChecked())
		{
			params.addQueryStringParameter("plantType", "1");
		} else
		{
			params.addQueryStringParameter("plantType", "0");
		}
		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
		{
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo)
			{
				String a = responseInfo.result;
				List<jobtab> listData = null;
				Result result = JSON.parseObject(responseInfo.result, Result.class);
				if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
				{
					listData = JSON.parseArray(result.getRows().toJSONString(), jobtab.class);
					if (listData == null)
					{
						AppContext.makeToast(AddPlantObservationRecord.this, "error_connectDataBase");
					} else
					{
						if (list_picture.size() > 0)
						{
							latch = new CountDownLatch(list_picture.size());
							for (int j = 0; j < list_picture.size(); j++)
							{
								uploadMedia(listData.get(0).getId(), list_picture.get(j));
							}
						} else
						{
							Toast.makeText(AddPlantObservationRecord.this, "保存成功！", Toast.LENGTH_SHORT).show();
							finish();
						}

					}

				} else
				{
					AppContext.makeToast(AddPlantObservationRecord.this, "error_connectDataBase");
					return;
				}
			}

			@Override
			public void onFailure(HttpException error, String arg1)
			{
				String a = error.getMessage();
				AppContext.makeToast(AddPlantObservationRecord.this, "error_connectServer");
			}
		});
	}

	private void uploadMedia(String id, String path)
	{
		File file = new File(path);
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("action", "UpLoadFilePlantImg");
		params.addQueryStringParameter("plantgrowthId", id);
		params.addQueryStringParameter("file", file.getName());
		params.setBodyEntity(new FileUploadEntity(file, "text/html"));
		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, AppConfig.uploadurl, params, new RequestCallBack<String>()
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
						showProgress();
					} else
					{
						AppContext.makeToast(AddPlantObservationRecord.this, "error_connectDataBase");
					}
				} else
				{
					AppContext.makeToast(AddPlantObservationRecord.this, "error_connectDataBase");
					return;
				}

			}

			@Override
			public void onFailure(HttpException error, String msg)
			{
				String a = error.getMessage();
				AppContext.makeToast(AddPlantObservationRecord.this, "error_connectServer");
			}
		});
	}

	private void getCommandlist()
	{
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("name", "Zuoye");
		params.addQueryStringParameter("action", "getDict");
		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
		{
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo)
			{
				String a = responseInfo.result;
				List<Dictionary> listNewData = null;
				Result result = JSON.parseObject(responseInfo.result, Result.class);
				if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
				{
					if (result.getAffectedRows() != 0)
					{
						String aa = result.getRows().toJSONString();
						listNewData = JSON.parseArray(result.getRows().toJSONString(), Dictionary.class);
						dic_comm = listNewData.get(0);
					} else
					{
						listNewData = new ArrayList<Dictionary>();
					}
				} else
				{
					AppContext.makeToast(AddPlantObservationRecord.this, "error_connectDataBase");
					return;
				}
			}

			@Override
			public void onFailure(HttpException error, String msg)
			{
				String a = error.getMessage();
				AppContext.makeToast(AddPlantObservationRecord.this, "error_connectServer");
			}
		});
	}

	private void showProgress()
	{
		latch.countDown();
		Long l = latch.getCount();
		if (l.intValue() == 0) // 全部线程是否已经结束
		{
			Toast.makeText(AddPlantObservationRecord.this, "保存成功！", Toast.LENGTH_SHORT).show();
			finish();
		}
	}
}
