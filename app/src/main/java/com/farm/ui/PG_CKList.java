package com.farm.ui;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.adapter.PG_CKExcute_adapter;
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
 * Created by user on 2016/5/17.
 */
@EFragment
public class PG_CKList extends Fragment
{


    String goodsName;
    String indate;
//    WZ_RKExecute_Adapter wz_rkExecute_adapter;
    PG_CKExcute_adapter pg_ckExcute_adapter;
    @ViewById
    ExpandableListView expandableListView;
    @ViewById
    ImageButton imgbtn;
    @ViewById
    TextView et_goodsname;
    @ViewById
    TextView tv_title;
    @ViewById
    ImageButton btn_add;
    com.farm.bean.commembertab commembertab;
    private AppContext appContext;
    @Click
    void btn_add()
    {
//        Intent intent = new Intent(getActivity(), PG_AddEvent_.class);
        Intent intent = new Intent(getActivity(), PG_CK_.class);
        startActivity(intent);
    }
    @Click
    void imgbtn()
    {


        goodsName=et_goodsname.getText().toString();
        getBreakOffInfoOfContract();
    }

    @Override
    public void onResume() {
        super.onResume();
        getBreakOffInfoOfContract();
    }

    @AfterViews
    void afssss()
    {
        getBreakOffInfoOfContract();
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.pg_cklistlayout, container, false);
        appContext = (AppContext) getActivity().getApplication();
//        commembertab commembertab = AppContext.getUserInfo(getActivity());
        commembertab = AppContext.getUserInfo(getActivity());

        IntentFilter intentfilter_update = new IntentFilter(AppContext.BROADCAST_PG_REFASH);
        getActivity().registerReceiver(receiver_update, intentfilter_update);
        return rootView;
    }
    BroadcastReceiver receiver_update = new BroadcastReceiver()// 从扩展页面返回信息
    {
        @SuppressWarnings("deprecation")
        @Override
        public void onReceive(Context context, Intent intent)
        {
            getBreakOffInfoOfContract();
        }
    };
    private void getBreakOffInfoOfContract()
    {
        commembertab commembertab = AppContext.getUserInfo(getActivity());
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("userId", commembertab.getId());
//        params.addQueryStringParameter("goodsName",goodsName);
        params.addQueryStringParameter("action", "getGoodsOutByPG");
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
                    if (result.getAffectedRows() != 0)
                    {
                        listNewData = JSON.parseArray(result.getRows().toJSONString(), WZ_CRk.class);
                        pg_ckExcute_adapter = new PG_CKExcute_adapter(getActivity(), listNewData, expandableListView);
                        expandableListView.setAdapter(pg_ckExcute_adapter);

    /*                    for (int i = 0; i < listNewData.size(); i++)
                        {
                            expandableListView.expandGroup(i);//展开
//                                  expandableListView.collapseGroup(i);//关闭
                        }*/

                    } else
                    {
                        listNewData = new ArrayList<WZ_CRk>();
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
