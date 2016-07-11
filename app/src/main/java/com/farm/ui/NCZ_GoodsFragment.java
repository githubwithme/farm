package com.farm.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.Spinner;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.adapter.Adapter_Goods_NCZ;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.Dictionary;
import com.farm.bean.ParkGoodsBean;
import com.farm.bean.Result;
import com.farm.bean.commembertab;
import com.farm.common.DictionaryHelper;
import com.farm.common.FileHelper;
import com.farm.common.utils;
import com.farm.widget.CustomSpinnerAdapter;
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

/**
 * Created by ${hmj} on 2016/7/11.
 */
@EFragment
public class NCZ_GoodsFragment extends Fragment
{
    @ViewById
    Spinner parkSpinner;
    @ViewById
    Spinner storeSpinner;
    @ViewById
    Spinner firstTypeSpinner;
    @ViewById
    Spinner secondTypeSpinner;

    CustomSpinnerAdapter parkAdapter = null;
    CustomSpinnerAdapter storeAdapter = null;
    CustomSpinnerAdapter firstTypeAdapter = null;
    CustomSpinnerAdapter secondTypeAdapter = null;
    private List<String> parkData;
    private List<String> storeData;
    private List<String> firstTypeData;
    private List<String> secondTypeData;
    @ViewById
    ExpandableListView expandableListView;
    Adapter_Goods_NCZ adapter_goods_ncz;
    Dictionary dictionary_park;
    Dictionary dictionary_type;
    String parkId;
    String storeId;
    String firstTypeId;
    String secondTypeId;

    @AfterViews
    void afterOncreate()
    {
        setTypeSpinner();
        setParkSpinner();
//        getGoods();
        getNewSaleList_test();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.ncz_goodsfragment, container, false);
        return rootView;
    }

    private void getNewSaleList_test()
    {
        List<ParkGoodsBean> listNewData = FileHelper.getAssetsData(getActivity(), "getgoods", ParkGoodsBean.class);
        if (listNewData != null)
        {
            adapter_goods_ncz = new Adapter_Goods_NCZ(getActivity(), listNewData, expandableListView);
            expandableListView.setAdapter(adapter_goods_ncz);
            utils.setListViewHeight(expandableListView);
        }
        for (int i = 0; i < listNewData.size(); i++)
        {
            expandableListView.expandGroup(i);//展开
        }

    }

    private void getGoods()
    {
        commembertab commembertab = AppContext.getUserInfo(getActivity());
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("parkId", parkId);
        params.addQueryStringParameter("storeId", storeId);
        params.addQueryStringParameter("firstTypeId", firstTypeId);
        params.addQueryStringParameter("secondTypeId", secondTypeId);
        params.addQueryStringParameter("action", "getBatchTimeByUid");//jobGetList1
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String a = responseInfo.result;
                List<ParkGoodsBean> listNewData = null;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    if (result.getAffectedRows() != 0)
                    {
                        listNewData = JSON.parseArray(result.getRows().toJSONString(), ParkGoodsBean.class);
                        adapter_goods_ncz = new Adapter_Goods_NCZ(getActivity(), listNewData, expandableListView);
                        expandableListView.setAdapter(adapter_goods_ncz);
                        utils.setListViewHeight(expandableListView);
//                        for (int i = 0; i < listNewData.size(); i++)
//                        {
//                            expandableListView.expandGroup(i);//展开
//                        }

                    } else
                    {
                        listNewData = new ArrayList<ParkGoodsBean>();
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


    private void setTypeSpinner()
    {
        dictionary_type = DictionaryHelper.getDictionaryFromAssess(getActivity(), "getGoodType");
        //绑定适配器和值
        firstTypeAdapter = new CustomSpinnerAdapter(getActivity(), dictionary_type.getFirstItemName());
        firstTypeSpinner.setAdapter(firstTypeAdapter);
        firstTypeSpinner.setSelection(0, true);
        secondTypeAdapter = new CustomSpinnerAdapter(getActivity(), dictionary_type.getSecondItemName().get(0));
        secondTypeSpinner.setAdapter(secondTypeAdapter);
        secondTypeSpinner.setSelection(0, true);

        firstTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3)
            {
                secondTypeAdapter = new CustomSpinnerAdapter(getActivity(), dictionary_type.getSecondItemName().get(position));
                secondTypeSpinner.setAdapter(secondTypeAdapter);
                secondTypeSpinner.setSelection(0, true);
                getNewSaleList_test();//重新获取数据
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0)
            {

            }
        });
        secondTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3)
            {
                getNewSaleList_test();//重新获取数据
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0)
            {

            }
        });
    }

    private void setParkSpinner()
    {
        dictionary_park = DictionaryHelper.getDictionaryFromAssess(getActivity(), "getParkStore");
        //绑定适配器和值
        parkAdapter = new CustomSpinnerAdapter(getActivity(), dictionary_park.getFirstItemName());
        parkSpinner.setAdapter(parkAdapter);
        parkSpinner.setSelection(0, true);  //设置默认选中项，此处为默认选中第4个值
        storeAdapter = new CustomSpinnerAdapter(getActivity(), dictionary_park.getSecondItemName().get(0));
        storeSpinner.setAdapter(storeAdapter);
        storeSpinner.setSelection(0, true);  //默认选中第0个

        //省级下拉框监听
        parkSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            // 表示选项被改变的时候触发此方法，主要实现办法：动态改变地级适配器的绑定值
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3)
            {
                storeAdapter = new CustomSpinnerAdapter(getActivity(), dictionary_park.getSecondItemName().get(position));
                storeSpinner.setAdapter(storeAdapter);
                storeSpinner.setSelection(0, true);  //默认选中第0个
                getNewSaleList_test();//重新获取数据
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0)
            {
            }

        });


        //地级下拉监听
        storeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3)
            {
                getNewSaleList_test();//重新获取数据
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0)
            {

            }
        });
    }


}
