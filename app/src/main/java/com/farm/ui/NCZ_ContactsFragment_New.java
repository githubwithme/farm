package com.farm.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.adapter.Adapter_ContactsFragment;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.ContactsBean;
import com.farm.bean.Result;
import com.farm.bean.commembertab;
import com.farm.common.FileHelper;
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

import java.util.List;

/**
 * Created by user on 2016/2/26.
 */
@EFragment
public class NCZ_ContactsFragment_New extends Fragment
{
    List<ContactsBean> listNewData = null;
    Adapter_ContactsFragment adapter_contactsFragment;
    String goodsName;
    @ViewById
    ExpandableListView expandableListView;
    @ViewById
    ImageButton imgbtn;
    @ViewById
    TextView et_goodsname;
    DialogFragment_WaitTip dialog;

    public void showDialog_waitTip()
    {
        dialog = new DialogFragment_WaitTip_();
        Bundle bundle1 = new Bundle();
        dialog.setArguments(bundle1);
        dialog.show(getFragmentManager(), "TIP");
    }

    //    dialog.loadingTip(getText(R.string.error_data).toString());
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
        showDialog_waitTip();
        getBreakOffInfoOfContract();
//        getNewSaleList_test();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.ncz_contactsfragment_new, container, false);
        return rootView;
    }

    private void getNewSaleList_test()
    {
        listNewData = FileHelper.getAssetsData(getActivity(), "getUserInfo", ContactsBean.class);
        if (listNewData != null)
        {
            adapter_contactsFragment = new Adapter_ContactsFragment(getActivity(), listNewData, expandableListView);
            expandableListView.setAdapter(adapter_contactsFragment);

            for (int i = 0; i < listNewData.size(); i++)
            {
                expandableListView.expandGroup(i);//展开
            }
        }

    }

    private void getBreakOffInfoOfContract()
    {
        commembertab commembertab = AppContext.getUserInfo(getActivity());
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("action", "getContactsData");
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
                    listNewData = JSON.parseArray(result.getRows().toJSONString(), ContactsBean.class);
                    adapter_contactsFragment = new Adapter_ContactsFragment(getActivity(), listNewData, expandableListView);
                    expandableListView.setAdapter(adapter_contactsFragment);

                    for (int i = 0; i < listNewData.size(); i++)
                    {
                        expandableListView.expandGroup(i);//展开
                    }

                } else
                {
                    AppContext.makeToast(getActivity(), "error_connectDataBase");
                    dialog.loadingTip(getText(R.string.error_data).toString());
                    return;
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(getActivity(), "error_connectServer");
                dialog.loadingTip(getText(R.string.error_network).toString());
            }
        });
    }
}
