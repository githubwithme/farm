package com.farm.ui;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.GoodsOfType;
import com.farm.bean.Result;
import com.farm.common.FileHelper;
import com.farm.widget.CustomSpinnerAdapter;
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
 * 图片左右滑动
 *
 * @author hmj
 */
@SuppressLint("NewApi")
@EFragment
public class DialogFragment_SelectGoods extends DialogFragment
{
    @ViewById
    EditText et_number;
    @ViewById
    Button btn_sure;
    @ViewById
    Button btn_cancle;
    @ViewById
    Spinner firstItemSpinner;
    @ViewById
    Spinner secondItemSpinner;
    @ViewById
    Spinner goodsItemSpinner;
    String firstTypeId;
    String secondTypeId;
    List<GoodsOfType> listGoods = new ArrayList<GoodsOfType>();
    List<GoodsOfType> listGoodsOfSec = new ArrayList<GoodsOfType>();
    private List<String> firstTypeData = new ArrayList<String>();
    private List<String> secondTypeData = new ArrayList<String>();
    private List<String> goodsData = new ArrayList<String>();
    ArrayAdapter<String> firstTypeAdapter = null;
    ArrayAdapter<String> secondTypeAdapter = null;
    ArrayAdapter<String> goodsAdapter = null;

    @AfterViews
    void AfterViews()
    {
        getNewSaleList_test();
        getgoodsType();
    }

    @Click
    void btn_cancle()
    {
        this.dismiss();
    }

    @Click
    void btn_sure()
    {
        this.dismiss();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View rootView = inflater.inflate(R.layout.dialogfragment_selectgoods, container, false);
        return rootView;
    }

    private void getNewSaleList_test()
    {
        goodsData = FileHelper.getAssetsData(getActivity(), "getGoods", String.class);
        if (goodsData != null)
        {
            goodsAdapter = new CustomSpinnerAdapter(getActivity(), goodsData);
            goodsItemSpinner.setAdapter(goodsAdapter);
            goodsItemSpinner.setSelection(0, true);
            goodsItemSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
            {

                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3)
                {
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0)
                {

                }
            });
        }

    }

    private void getgoodsType()
    {
        com.farm.bean.commembertab commembertab = AppContext.getUserInfo(getActivity());
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("action", "getgoodsType");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String a = responseInfo.result;
                List<GoodsOfType> listNewData = null;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    firstTypeData = new ArrayList<String>();
                    listNewData = JSON.parseArray(result.getRows().toJSONString(), GoodsOfType.class);
                    GoodsOfType goodsOfType = new GoodsOfType();
                    goodsOfType.setId("");
                    goodsOfType.setGoodsTypeName("不限类型");
                    listGoods.add(goodsOfType);
                    listGoods.addAll(listNewData);
                    for (int i = 0; i < listGoods.size(); i++)
                    {
                        firstTypeData.add(listGoods.get(i).getGoodsTypeName());
                    }
                    firstTypeAdapter = new CustomSpinnerAdapter(getActivity(), firstTypeData);
                    firstItemSpinner.setAdapter(firstTypeAdapter);
                    firstItemSpinner.setSelection(0, true);
                    secondTypeData = new ArrayList<String>();
                    secondTypeData.add("不限类型");
                    secondTypeAdapter = new CustomSpinnerAdapter(getActivity(), secondTypeData);
                    secondItemSpinner.setAdapter(secondTypeAdapter);
                    secondItemSpinner.setSelection(0, true);
                    firstItemSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                    {

                        @Override
                        public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3)
                        {
                            firstTypeId = listGoods.get(position).getId();

                            if (position != 0)
                            {
                                getuserdefgoodstypetab(firstTypeId);

                            } else
                            {
                                secondTypeData = new ArrayList<String>();
                                secondTypeData.add("不限类型");
                                secondTypeAdapter = new CustomSpinnerAdapter(getActivity(), secondTypeData);
                                secondItemSpinner.setAdapter(secondTypeAdapter);
                                secondItemSpinner.setSelection(0, true);
                            }

                            secondTypeId = "";
                            getNewSaleList_test();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> arg0)
                        {

                        }
                    });
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

    private void getuserdefgoodstypetab(String goodsType)
    {
        com.farm.bean.commembertab commembertab = AppContext.getUserInfo(getActivity());
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("goodsType", goodsType);
        params.addQueryStringParameter("action", "getuserdefgoodstypetab");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String a = responseInfo.result;
                List<GoodsOfType> listNewData = null;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    secondTypeData = new ArrayList<String>();
                    listGoodsOfSec = new ArrayList<GoodsOfType>();
                    listNewData = JSON.parseArray(result.getRows().toJSONString(), GoodsOfType.class);
                    GoodsOfType goodsOfType = new GoodsOfType();
                    goodsOfType.setId("");
                    goodsOfType.setTypeName("不限类型");
                    listGoodsOfSec.add(goodsOfType);
                    listGoodsOfSec.addAll(listNewData);

                    for (int i = 0; i < listGoodsOfSec.size(); i++)
                    {
                        secondTypeData.add(listGoodsOfSec.get(i).getTypeName());
                    }
                    secondTypeAdapter = new CustomSpinnerAdapter(getActivity(), secondTypeData);
                    secondItemSpinner.setAdapter(secondTypeAdapter);
                    secondItemSpinner.setSelection(0, true);
                    secondItemSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                    {

                        @Override
                        public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3)
                        {
                            secondTypeId = listGoodsOfSec.get(position).getGoodsType();
                            getNewSaleList_test();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> arg0)
                        {

                        }
                    });
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

//    private void getGoods()
//    {
//        commembertab commembertab = AppContext.getUserInfo(getActivity());
//        RequestParams params = new RequestParams();
//        params.addQueryStringParameter("uid", commembertab.getuId());
//        params.addQueryStringParameter("parkId", "-1");
//        params.addQueryStringParameter("storeId", "-1");
//        params.addQueryStringParameter("goodsType", firstTypeId);
//        params.addQueryStringParameter("userDefType", secondTypeId);
//        params.addQueryStringParameter("action", "getSumOfParkgoods");//jobGetList1
//        HttpUtils http = new HttpUtils();
//        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
//        {
//            @Override
//            public void onSuccess(ResponseInfo<String> responseInfo)
//            {
//                String a = responseInfo.result;
//                List<ParkGoodsBean> listNewData = null;
//                Result result = JSON.parseObject(responseInfo.result, Result.class);
//                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
//                {
//                    listNewData = JSON.parseArray(result.getRows().toJSONString(), ParkGoodsBean.class);
//                    if (listNewData != null)
//                    {
////                        for (int i = 0; i < listNewData.size(); i++)
////                        {
////                            secondTypeData.add(listNewData.get(i).getGoodsStockLists());
////                        }
//                        secondTypeAdapter = new CustomSpinnerAdapter(getActivity(), secondTypeData);
//                        secondItemSpinner.setAdapter(secondTypeAdapter);
//                        secondItemSpinner.setSelection(0, true);
//                        secondItemSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
//                        {
//
//                            @Override
//                            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3)
//                            {
//                                secondTypeId = listGoodsOfSec.get(position).getGoodsType();
//                                getGoods();
//                            }
//
//                            @Override
//                            public void onNothingSelected(AdapterView<?> arg0)
//                            {
//
//                            }
//                        });
//                    }
//                } else
//                {
//                    AppContext.makeToast(getActivity(), "error_connectDataBase");
//                    return;
//                }
//
//            }
//
//            @Override
//            public void onFailure(HttpException error, String msg)
//            {
//                AppContext.makeToast(getActivity(), "error_connectServer");
//            }
//        });
//    }

}