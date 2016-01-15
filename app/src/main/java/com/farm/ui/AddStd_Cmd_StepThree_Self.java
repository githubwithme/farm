package com.farm.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.adapter.CustomFragmentPagerAdapter;
import com.farm.adapter.EdiGoodsNumberAdapter;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.Dictionary;
import com.farm.bean.Result;
import com.farm.bean.commandtab_single;
import com.farm.bean.commembertab;
import com.farm.bean.goodslisttab;
import com.farm.bean.goodslisttab_flsl;
import com.farm.com.custominterface.FragmentCallBack;
import com.farm.common.SqliteDb;
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
import java.util.HashMap;
import java.util.List;

/**
 * Created by ${hmj} on 2015/12/15.
 */
@EFragment
public class AddStd_Cmd_StepThree_Self extends Fragment
{
    List<goodslisttab> lsitNewData = new ArrayList<>();
    EdiGoodsNumberAdapter ediGoodsNumberAdapter;
    List<Fragment> fragments;
    List<goodslisttab> list_goods;
    commembertab commembertab;
    Dictionary dic_park;
    Dictionary dic_area;
    FragmentCallBack fragmentCallBack = null;
    SelectorFragment selectorUi;
    Fragment mContent = new Fragment();
    HashMap<String, List<goodslisttab>> map_goods;
    private List<String> list;
    private TextView[] tvList;
    private View[] views;
    private int currentItem = 0;
    private CustomFragmentPagerAdapter customFragmentPagerAdapter;

    @ViewById
    ImageView iv_dowm_tab;
    @ViewById
    Button btn_next;
    @ViewById
    ListView lv_goods;
    @ViewById
    HorizontalScrollView cmd_tools_scrlllview;
    @ViewById
    LinearLayout cmd_tools;
    @ViewById
    ViewPager area_pager;

    @Click
    void btn_next()
    {
        Bundle bundle = new Bundle();
        bundle.putInt("INDEX", 2);
        fragmentCallBack.callbackFun2(bundle);
    }


    @AfterViews
    void afterOncreate()
    {
        ediGoodsNumberAdapter = new EdiGoodsNumberAdapter(getActivity(),lsitNewData);
        lv_goods.setAdapter(ediGoodsNumberAdapter);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.add_std__cmd__step_three_self, container, false);
        commembertab = AppContext.getUserInfo(getActivity());
        return rootView;
    }

    private void getGoodsSum()
    {
        String goodsid = "";
        String goodsname = "";
        for (int i = 0; i < list_goods.size(); i++)
        {
            goodsid = goodsid + list_goods.get(i).getId() + ",";
            goodsname = goodsname + list_goods.get(i).getgoodsName() + ",";
        }
        commandtab_single commandtab_single = com.farm.bean.commandtab_single.getInstance();
        commembertab commembertab = AppContext.getUserInfo(getActivity());
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", commembertab.getuId());
        String aa = commandtab_single.getNongziId();
        params.addQueryStringParameter("goodsId", goodsid.substring(0, goodsid.length() - 1));
        params.addQueryStringParameter("action", "getGoodsSum");
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
                        String aa = result.getRows().toJSONString();
                        lsitNewData = JSON.parseArray(result.getRows().toJSONString(), goodslisttab.class);
                        if (lsitNewData != null)
                        {
                            ediGoodsNumberAdapter = new EdiGoodsNumberAdapter(getActivity(),lsitNewData);
                            lv_goods.setAdapter(ediGoodsNumberAdapter);
                        }
                    } else
                    {

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
                String a = error.getMessage();
                AppContext.makeToast(getActivity(), "error_connectServer");
            }
        });
    }

    public void update()
    {
        SqliteDb.deleteAllSelectCmdArea(getActivity(), goodslisttab_flsl.class);
        list_goods = SqliteDb.getGoods(getActivity(), goodslisttab.class);
        getGoodsSum();
    }


    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        fragmentCallBack = (FragmentCallBack) activity;
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        getActivity().finish();
    }
}
