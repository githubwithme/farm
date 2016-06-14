package com.farm.ui;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.adapter.PG_cbhAdapter;
import com.farm.adapter.pq_dlbjGV_adapter;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.Result;
import com.farm.bean.commembertab;
import com.farm.bean.contractTab;
import com.farm.bean.jobtab;
import com.farm.common.utils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import android.widget.AdapterView.OnItemClickListener;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.List;

/**
 * Created by hasee on 2016/6/12.
 */
@EFragment
public class PG_cbhdetail extends Fragment
{
    List<contractTab> list_contractTab = null;
    PG_cbhAdapter pg_cbhAdapter;
    com.farm.bean.commembertab commembertab;
    jobtab jobtab;
    @ViewById
    GridView gv_cbh;

    @AfterViews
    void afterview()
    {
        getContractList();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.pg_cbhdetail, container, false);
        jobtab = getArguments().getParcelable("bean");
        commembertab = AppContext.getUserInfo(getActivity());
        return view;
    }

    public void getContractList()
    {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("areaid", commembertab.getareaId());
//        params.addQueryStringParameter("year", utils.getYear());
//        params.addQueryStringParameter("action", "getAllBreakOffByAreaId");
        params.addQueryStringParameter("action", "getContractList");
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
                        list_contractTab = JSON.parseArray(result.getRows().toJSONString(), contractTab.class);
                        pg_cbhAdapter = new PG_cbhAdapter(getActivity(), list_contractTab);
                        gv_cbh.setAdapter(pg_cbhAdapter);
                        gv_cbh.setOnItemClickListener(new OnItemClickListener()
                        {
                            public void onItemClick(AdapterView<?> parent, View v, int position, long id)
                            {
                                Intent intent = new Intent(getActivity(), PG_cbfscore_.class);
                                intent.putExtra("contracttab", list_contractTab.get(position));
                                getActivity().startActivity(intent);
                            }
                        });
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
//       getBreakOffList();
    }
}
