package com.farm.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.adapter.CustomExpandableListAdapter_Goods;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.Dictionary;
import com.farm.bean.Dictionary_wheel;
import com.farm.bean.Result;
import com.farm.common.DictionaryHelper;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.HashMap;
import java.util.List;

/**
 * Created by user on 2016/2/25.
 */
@SuppressLint("NewApi")
@EFragment
public class NCZ_WZ_LOOKFragment extends Fragment
{
    String[] fn;
    Dictionary_wheel dictionary_wheel;
    Dictionary dic;
    com.farm.bean.commembertab commembertab;

    private AppContext appContext;
    String[] parent = null;
    HashMap<String, String[]> map = null;
    @ViewById
    ExpandableListView mainlistview;
    @ViewById
    ListView list_goods;
    @ViewById
    TextView tv_top;
    @ViewById
    RelativeLayout rl_pb;
    @ViewById
    LinearLayout ll_tip;
    @ViewById
    ProgressBar pb;
    @ViewById
    TextView tv_tip;
//@OnClick(R.id.mainlistview)
//void mainlistviews(){
//
//}

    @AfterViews
    void afterOncreate()
    {
        getCommandlist();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.ncz_wz_lookfragment, container, false);
        commembertab = AppContext.getUserInfo(getActivity());
        return view;
    }


    private void getCommandlist()
    {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("name", "getWZ");
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
                if (result.getResultCode() == 1)
                {
                    if (result.getAffectedRows() != 0)
                    {
                        rl_pb.setVisibility(View.GONE);
                        String aa = result.getRows().toJSONString();
                        lsitNewData = JSON.parseArray(result.getRows().toJSONString(), Dictionary.class);
                        if (lsitNewData != null)
                        {
                            dic = lsitNewData.get(0);
                            dictionary_wheel = DictionaryHelper.getDictionary_Command(dic);//提供数据
                            CustomExpandableListAdapter_Goods customExpandableListAdapter = new CustomExpandableListAdapter_Goods(getActivity(), dictionary_wheel, mainlistview, list_goods, tv_top);
                            mainlistview.setAdapter(customExpandableListAdapter);
                            mainlistview.expandGroup(0);
                        }

                    } else
                    {
                        ll_tip.setVisibility(View.VISIBLE);
                        tv_tip.setText("暂无数据！");
                        pb.setVisibility(View.GONE);
                    }
                } else//
                {
                    ll_tip.setVisibility(View.VISIBLE);
                    tv_tip.setText("数据加载异常！");
                    pb.setVisibility(View.GONE);
                    return;
                }
            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                ll_tip.setVisibility(View.VISIBLE);
                tv_tip.setText("网络连接异常！");
                pb.setVisibility(View.GONE);
            }
        });
    }


}
