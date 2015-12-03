package com.farm.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.Result;
import com.farm.bean.plantgrowthtab;
import com.farm.common.ConnectionHelper;
import com.farm.common.ResultDeal;
import com.farm.widget.swipelistview.ExpandAniLinearLayout;
import com.farm.widget.swipelistview.SwipeListViewImpl_RW;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author :hc-sima
 * @version :1.0
 * @createTime：2015-8-23 下午3:37:11
 * @description :
 */
@EFragment
public class TaskFragment extends Fragment
{
	@ViewById
	ExpandAniLinearLayout swipe_list_ani;
	@ViewById
	ListView swipe_list;

	@AfterViews
	void afterOncreate()
	{
		getTask(20, 0);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.taskfragment, container, false);
		return rootView;
	}

	private void getTask(final int PAGESIZE, int PAGEINDEX)
	{
		HashMap<String, String> hashMap = new HashMap<String, String>();
		hashMap.put("PAGESIZE", String.valueOf(PAGESIZE));
		hashMap.put("PAGEINDEX", String.valueOf(PAGEINDEX));
		String params = ConnectionHelper.setParams("APP.GetplantgrowthtabList", "0", hashMap);
		new HttpUtils().send(HttpRequest.HttpMethod.POST, AppConfig.testurl, ConnectionHelper.getParas(params), new RequestCallBack<String>()
		{
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo)
			{
				String a = responseInfo.result;
				List<plantgrowthtab> listNewData = null;
				Result result = JSON.parseObject(responseInfo.result, Result.class);
				if (result.getResultCode() == 200)// 连接数据库成功
				{
					listNewData = JSON.parseArray(ResultDeal.getAllRow(result), plantgrowthtab.class);
					plantgrowthtab b = listNewData.get(0);
					if (listNewData == null)
					{
						listNewData = new ArrayList<plantgrowthtab>();
					} else
					{
						SwipeListViewImpl_RW swipeListViewImpl_RW = new SwipeListViewImpl_RW();
						swipeListViewImpl_RW.setMyadapter("CYR", getActivity(), swipe_list_ani, listNewData, swipe_list);
					}
				} else
				{
					AppContext.makeToast(getActivity(), "error_connectDataBase");
					return;
				}
			}

			@Override
			public void onFailure(HttpException arg0, String arg1)
			{

			}
		});
	}
}
