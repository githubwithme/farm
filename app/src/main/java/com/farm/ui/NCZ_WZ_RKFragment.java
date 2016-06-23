package com.farm.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.adapter.WZ_RKExecute_Adapter;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.Result;
import com.farm.bean.WZ_CRk;
import com.farm.bean.commembertab;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2016/4/7.
 */
@EFragment
public class NCZ_WZ_RKFragment extends Fragment
{

    String id;
    String name;

    String goodsName;
    String indate;
    WZ_RKExecute_Adapter wz_rkExecute_adapter;
    @ViewById
    ExpandableListView expandableListView;
    @ViewById
    ImageButton imgbtn;
    @ViewById
    TextView et_goodsname;

    @Click
    void et_goodsname()
    {
        et_goodsname.setText("");
    }

    @Click
    void imgbtn()
    {


        goodsName = et_goodsname.getText().toString();
        getBreakOffInfoOfContract();
    }

    @AfterViews
    void afterOncreate()
    {
       /* IntentFilter intentfilter_update = new IntentFilter(AppContext.BROADCAST_NCZ_WZ_ParkId);
        getActivity().registerReceiver(receiver_update, intentfilter_update);*/
        getBreakOffInfoOfContract();
    }

/*    BroadcastReceiver receiver_update = new BroadcastReceiver()// 从扩展页面返回信息
    {
        @SuppressWarnings("deprecation")
        @Override
        public void onReceive(Context context, Intent intent)
        {
           String a=intent.getStringExtra("id");
           String b=intent.getStringExtra("name");
        }
    };*/
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.ncz_wz_crlayout, container, false);
        id = getArguments().getString("id");
        name = getArguments().getString("name");
        return rootView;
    }

    private void getBreakOffInfoOfContract()
    {
        commembertab commembertab = AppContext.getUserInfo(getActivity());
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("goodsName", goodsName);
        params.addQueryStringParameter("parkId", id);
        params.addQueryStringParameter("action", "getGoodsInByUid");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String a = responseInfo.result;
                List<WZ_CRk> listNewData = null;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    /*if (result.getAffectedRows() != 0)
                    {*/
                        listNewData = JSON.parseArray(result.getRows().toJSONString(), WZ_CRk.class);
                        wz_rkExecute_adapter = new WZ_RKExecute_Adapter(getActivity(), listNewData, expandableListView);
                        expandableListView.setAdapter(wz_rkExecute_adapter);

//                        for (int i = 0; i < listNewData.size(); i++)
//                        {
////                            expandableListView.expandGroup(i);//展开
//                            expandableListView.collapseGroup(i);//关闭
//                        }

                /*    } else
                    {
                        listNewData = new ArrayList<WZ_CRk>();
                    }*/

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
