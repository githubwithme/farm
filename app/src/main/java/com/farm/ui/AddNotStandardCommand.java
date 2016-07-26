package com.farm.ui;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.farm.R;
import com.farm.adapter.FirstItemAdapter;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.Dictionary;
import com.farm.bean.HaveReadRecord;
import com.farm.bean.Result;
import com.farm.bean.SelectRecords;
import com.farm.bean.commembertab;
import com.farm.bean.jobtab;
import com.farm.common.DictionaryHelper;
import com.farm.common.SqliteDb;
import com.farm.common.utils;
import com.farm.widget.MyDatepicker;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.http.client.entity.FileUploadEntity;
import com.wheel.OneWheel;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.addnotstandardcommand)
public class AddNotStandardCommand extends Activity implements OnClickListener
{
	FirstItemAdapter firstItemAdapter;
	private ListView mainlist;
	private ListView morelist;
	PopupWindow popupWindow_selector;
	View popupWindowView_selector;
	PopupWindow popupWindow_recent;
	View popupWindowView_recent;
	@ViewById
	View line;
	@ViewById
	TextView tv_area;
	@ViewById
	TextView tv_workday;
	@ViewById
	TextView tv_timelimit;
	@ViewById
	EditText et_note;
	@ViewById
	EditText et_nz;
	@ViewById
	TextView tv_importance;
	@ViewById
	EditText et_yl;
	@ViewById
	RadioGroup rg_level;
	@ViewById
	Button btn_save;
	// List<Dictionary> dic_cmdlist = null;
	// List<Dictionary> dic_arealist = null;
	Dictionary dic_comm;
	Dictionary dic_park;
	Dictionary dic_area;
	String level = "";
	String importance = "";
	commembertab commembertab;

	@Click
	void btn_save()
	{
		commandTabAdd();
	}

	@Click
	void tv_importance()
	{
		String[] firstItemid = new String[] { "0", "1", "2" };
		String[] firstItemData = new String[] { "一般", "重要", "非常重要" };
		OneWheel.showWheel(this, firstItemid, firstItemData, tv_importance);
	}

	@Click
	void tv_area()
	{
		if (level.equals("0"))
		{
			if (dic_park.getFirstItemName().size() != 0)
			{
				DictionaryFragment dictionaryFragment = new DictionaryFragment_();
				Bundle bundle = new Bundle();
				bundle.putSerializable("bean", dic_park);
				dictionaryFragment.setArguments(bundle);
				dictionaryFragment.setResultview(tv_area);
				switchContent_top(mContent_top, dictionaryFragment);
			} else
			{
				Toast.makeText(AddNotStandardCommand.this, "获取数据异常！", Toast.LENGTH_SHORT).show();
			}

		} else if (level.equals("1"))
		{
			if (dic_area != null)
			{
				DictionaryFragment dictionaryFragment = new DictionaryFragment_();
				Bundle bundle = new Bundle();
				bundle.putSerializable("bean", dic_area);
				dictionaryFragment.setArguments(bundle);
				dictionaryFragment.setResultview(tv_area);
				switchContent_top(mContent_top, dictionaryFragment);
			} else
			{
				Toast.makeText(AddNotStandardCommand.this, "获取数据异常！", Toast.LENGTH_SHORT).show();
			}

		} else
		{
			Toast.makeText(AddNotStandardCommand.this, "请先选择执行级别！", Toast.LENGTH_SHORT).show();
		}
	}

	@Click
	void tv_workday()
	{
		JSONObject jsonObject = utils.parseJsonFile(AddNotStandardCommand.this, "dictionary.json");
		JSONArray jsonArray = JSONArray.parseArray(jsonObject.getString("number"));
		String[] number = new String[jsonArray.size()];
		String[] firstItemid = new String[jsonArray.size()];
		for (int i = 0; i < jsonArray.size(); i++)
		{
			number[i] = jsonArray.getString(i);
			firstItemid[i] = String.valueOf(i);
		}
		OneWheel.showWheel(AddNotStandardCommand.this, firstItemid, number, tv_workday);
	}

	@Click
	void tv_timelimit()
	{
		MyDatepicker myDatepicker = new MyDatepicker(AddNotStandardCommand.this, tv_timelimit);
		myDatepicker.getDialog().show();
	}

	@AfterViews
	void afterOncreate()
	{
		rg_level.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1)
			{

				RadioButton radioButton = (RadioButton) findViewById(arg0.getCheckedRadioButtonId());
				if (radioButton.getText().equals("园区"))
				{
					level = "0";
					if (dic_area != null)
					{
						SqliteDb.deleteAllRecordtemp(AddNotStandardCommand.this, SelectRecords.class, dic_area.getBELONG());
					} else
					{
						Toast.makeText(AddNotStandardCommand.this, "获取数据异常！", Toast.LENGTH_SHORT).show();
					}

				} else
				{
					level = "1";
					if (dic_park != null)
					{
						SqliteDb.deleteAllRecordtemp(AddNotStandardCommand.this, SelectRecords.class, dic_park.getBELONG());
					} else
					{
						Toast.makeText(AddNotStandardCommand.this, "获取数据异常！", Toast.LENGTH_SHORT).show();
					}

				}
				tv_area.setText("请选择区域");
			}
		});
		getCommandlist();
		getArealist();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		getActionBar().hide();
		commembertab = AppContext.getUserInfo(AddNotStandardCommand.this);
	}

	private void commandTabAdd()
	{
		List<SelectRecords> list_SelectRecords;
		commembertab commembertab = AppContext.getUserInfo(this);
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("userid", commembertab.getId());
		params.addQueryStringParameter("userName", commembertab.getrealName());
		params.addQueryStringParameter("uid", commembertab.getuId());
		params.addQueryStringParameter("action", "commandTabAdd");

		if (level.equals("0"))
		{
			list_SelectRecords = SqliteDb.getSelectRecordByFirstTypetemp(this, SelectRecords.class, dic_park.getBELONG().toString());
			String tempid = "";
			String tempname = "";
			for (int i = 0; i < list_SelectRecords.size(); i++)
			{
				tempid = tempid + list_SelectRecords.get(i).getSecondid() + ",";
				tempname = tempname + list_SelectRecords.get(i).getSecondtype() + ",";
			}
			params.addQueryStringParameter("parkId", tempid.substring(0, tempid.length() - 1));
			params.addQueryStringParameter("parkName", tempname.substring(0, tempname.length() - 1));
		} else if (level.equals("1"))
		{
			list_SelectRecords = SqliteDb.getSelectRecordByFirstTypetemp(this, SelectRecords.class, dic_area.getBELONG().toString());
			String tempid = "";
			String tempname = "";
			for (int i = 0; i < list_SelectRecords.size(); i++)
			{
				tempid = tempid + list_SelectRecords.get(i).getFirstid() + ":" + list_SelectRecords.get(i).getSecondid() + ",";
				tempname = tempname + list_SelectRecords.get(i).getFirsttype() + ":" + list_SelectRecords.get(i).getSecondtype() + ",";
			}
			params.addQueryStringParameter("areaId", tempid.substring(0, tempid.length() - 1));
			params.addQueryStringParameter("areaName", tempname.substring(0, tempname.length() - 1));
		} else
		{
			Toast.makeText(AddNotStandardCommand.this, "请选择区域！", Toast.LENGTH_SHORT).show();
			return;
		}
		if (tv_importance.getTag() != null)
		{
			importance = ((Bundle) tv_importance.getTag()).getString("FI");// 濒危度
		} else
		{
			Toast.makeText(AddNotStandardCommand.this, "请选择重要性！", Toast.LENGTH_SHORT).show();
			return;
		}
		if (tv_workday.getText().toString().equals(""))
		{
			Toast.makeText(AddNotStandardCommand.this, "请选择作业天数！", Toast.LENGTH_SHORT).show();
			return;
		}
		if (tv_timelimit.getText().toString().equals(""))
		{
			Toast.makeText(AddNotStandardCommand.this, "请选择期限！", Toast.LENGTH_SHORT).show();
			return;
		}
		params.addQueryStringParameter("nongziName", et_nz.getText().toString());
		params.addQueryStringParameter("amount", et_yl.getText().toString());
		params.addQueryStringParameter("commNote", et_note.getText().toString());
		params.addQueryStringParameter("commDays", tv_workday.getText().toString());
		params.addQueryStringParameter("commComDate", tv_timelimit.getText().toString());
		params.addQueryStringParameter("stdJobType", "0");
		params.addQueryStringParameter("stdJobTypeName", "");
		params.addQueryStringParameter("stdJobId", "0");
		params.addQueryStringParameter("stdJobName", "");
		params.addQueryStringParameter("importance", importance);
		params.addQueryStringParameter("execLevel", level);
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
						AppContext.makeToast(AddNotStandardCommand.this, "error_connectDataBase");
					} else
					{
						HaveReadRecord haveReadRecord = SqliteDb.getHaveReadRecord(AddNotStandardCommand.this, AppContext.TAG_NCZ_CMD);
						if (haveReadRecord != null)
						{
							SqliteDb.updateHaveReadRecord(AddNotStandardCommand.this, AppContext.TAG_NCZ_CMD, String.valueOf((Integer.valueOf(haveReadRecord.getNum()) + 1)));
						} else
						{
							SqliteDb.saveHaveReadRecord(AddNotStandardCommand.this, AppContext.TAG_NCZ_CMD, "1");
						}
						Toast.makeText(AddNotStandardCommand.this, "保存成功！", Toast.LENGTH_SHORT).show();
						finish();
					}

				} else
				{
					AppContext.makeToast(AddNotStandardCommand.this, "error_connectDataBase");
					return;
				}
			}

			@Override
			public void onFailure(HttpException error, String arg1)
			{
				String a = error.getMessage();
				AppContext.makeToast(AddNotStandardCommand.this, "error_connectServer");
			}
		});
	}

	private void uploadAudio(String id, File audioFile)
	{
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("action", "UpLoadFileCommAudio");
		params.addQueryStringParameter("commandid", id);
		params.addQueryStringParameter("workuserid", commembertab.getId());
		params.addQueryStringParameter("workusername", commembertab.getrealName());
		params.addQueryStringParameter("file", audioFile.getName());
		params.setBodyEntity(new FileUploadEntity(audioFile, "text/html"));
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
						Toast.makeText(AddNotStandardCommand.this, "保存成功！", Toast.LENGTH_SHORT).show();
						finish();
					} else
					{
						AppContext.makeToast(AddNotStandardCommand.this, "error_connectDataBase");
					}
				} else
				{
					AppContext.makeToast(AddNotStandardCommand.this, "error_connectDataBase");
					return;
				}

			}

			@Override
			public void onFailure(HttpException error, String msg)
			{
				String a = error.getMessage();
				AppContext.makeToast(AddNotStandardCommand.this, "error_connectServer");
			}
		});
	}

	private void getCommandlist()
	{

		RequestParams params = new RequestParams();
		params.addQueryStringParameter("uid", commembertab.getuId());
		params.addQueryStringParameter("name", "Zuoye");
		params.addQueryStringParameter("action", "getDict");
		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
		{
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo)
			{
				String a = responseInfo.result;
				List<Dictionary> lsitNewData = null;
				Result result = JSON.parseObject(responseInfo.result, Result.class);
				if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
				{
					if (result.getAffectedRows() != 0)
					{
						String aa = result.getRows().toJSONString();
						lsitNewData = JSON.parseArray(result.getRows().toJSONString(), Dictionary.class);
						if (lsitNewData != null)
						{
							dic_comm = lsitNewData.get(0);
						}

					} else
					{
						lsitNewData = new ArrayList<Dictionary>();
					}
				} else
				{
					AppContext.makeToast(AddNotStandardCommand.this, "error_connectDataBase");
					return;
				}
			}

			@Override
			public void onFailure(HttpException error, String msg)
			{
				String a = error.getMessage();
				AppContext.makeToast(AddNotStandardCommand.this, "error_connectServer");
			}
		});
	}

	private void getArealist()
	{
		commembertab commembertab = AppContext.getUserInfo(AddNotStandardCommand.this);
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("uid", commembertab.getuId());
		params.addQueryStringParameter("name", "getstdPark");
		params.addQueryStringParameter("action", "getDict");
		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
		{
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo)
			{
				String a = responseInfo.result;
				List<Dictionary> lsitNewData = null;
				Result result = JSON.parseObject(responseInfo.result, Result.class);
				if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
				{
					if (result.getAffectedRows() != 0)
					{
						String aa = result.getRows().toJSONString();
						lsitNewData = JSON.parseArray(result.getRows().toJSONString(), Dictionary.class);
						if (lsitNewData != null)
						{
							dic_area = lsitNewData.get(0);
							dic_area.setBELONG("片区执行");
							dic_park = DictionaryHelper.getParkDictionary(dic_area);
						}
					} else
					{
						lsitNewData = new ArrayList<Dictionary>();
					}
				} else
				{
					AppContext.makeToast(AddNotStandardCommand.this, "error_connectDataBase");
					return;
				}
			}

			@Override
			public void onFailure(HttpException error, String msg)
			{
				String a = error.getMessage();
				AppContext.makeToast(AddNotStandardCommand.this, "error_connectServer");
			}
		});
	}

	@Override
	public void onClick(View arg0)
	{

	}

	Fragment mContent = new Fragment();

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

	Fragment mContent_top = new Fragment();

	public void switchContent_top(Fragment from, Fragment to)
	{
		if (mContent_top != to)
		{
			mContent_top = to;
			FragmentTransaction transaction = getFragmentManager().beginTransaction();
			if (!to.isAdded())
			{ // 先判断是否被add过
				transaction.hide(from).add(R.id.top_container, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
			} else
			{
				transaction.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
			}
		}
	}
}
