package com.farm.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.adapter.Adapter_AreaSaleFragment;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.Result;
import com.farm.bean.SellOrderDetail_New;
import com.farm.bean.commembertab;
import com.farm.bean.parktab;
import com.farm.common.utils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by ${hmj} on 2016/6/57.
 */
@EFragment
public class NCZ_AreaSaleFragment extends Fragment
{
    List<Map<String, String>> uuids;
    List<SellOrderDetail_New> list_sell;
    Adapter_AreaSaleFragment adapter_areaSaleFragment;
    @ViewById
    ExpandableListView expandableListView;
    @ViewById
    TextView tv_note;


    @AfterViews
    void afterOncreate()
    {
        tv_note.setText("各分场销售情况");
        getSaleDataOfArea();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.ncz_areasalefragment, container, false);
        return rootView;
    }


    private void getSaleDataOfArea()
    {
        commembertab commembertab = AppContext.getUserInfo(getActivity());
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("year", utils.getYear());
        params.addQueryStringParameter("action", "getAreaSaleData");//jobGetList1
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String a = responseInfo.result;
                List<parktab> listNewData = null;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    if (result.getAffectedRows() != 0)
                    {
                        listNewData = JSON.parseArray(result.getRows().toJSONString(), parktab.class);
                        adapter_areaSaleFragment = new Adapter_AreaSaleFragment(getActivity(), listNewData, expandableListView);
                        expandableListView.setAdapter(adapter_areaSaleFragment);
                        utils.setListViewHeight(expandableListView);
//                        for (int i = 0; i < listNewData.size(); i++)
//                        {
//                            expandableListView.expandGroup(i);//展开
//                        }

                    } else
                    {
                        listNewData = new ArrayList<parktab>();
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
