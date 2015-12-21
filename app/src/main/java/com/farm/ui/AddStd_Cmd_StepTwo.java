package com.farm.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.adapter.CustomExpandableListAdapter;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.Dictionary;
import com.farm.bean.Dictionary_wheel;
import com.farm.bean.Result;
import com.farm.bean.commembertab;
import com.farm.com.custominterface.FragmentCallBack;
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
 * Created by ${hmj} on 2015/12/15.
 */
@EFragment
public class AddStd_Cmd_StepTwo extends Fragment
{
    String[] fn;
    Dictionary_wheel dictionary_wheel;
    Dictionary dic;
    commembertab commembertab;
    private Context context;// 运行上下文
    String[] parent = null;
    HashMap<String, String[]> map = null;
    FragmentCallBack fragmentCallBack = null;
    @ViewById
    ExpandableListView mainlistview;
    @ViewById
    GridView gridview_goods;
    @ViewById
    TextView tv_head;


    @AfterViews
    void afterOncreate()
    {
        getCommandlist();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.add_std__cmd__step_two, container, false);
        commembertab = AppContext.getUserInfo(getActivity());
        return rootView;
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
                        String aa = result.getRows().toJSONString();
                        lsitNewData = JSON.parseArray(result.getRows().toJSONString(), Dictionary.class);
                        if (lsitNewData != null)
                        {
                            dic = lsitNewData.get(0);
                            dictionary_wheel = DictionaryHelper.getDictionary_Command(dic);

//                            String[] parentData = dictionary_wheel.getFirstItemName();
//                            HashMap<String, String[]> map = dictionary_wheel.getSecondItemName();
//                            setHeadText(parentData[0]+map.get(parentData[0])[0]);
                            CustomExpandableListAdapter customExpandableListAdapter = new CustomExpandableListAdapter(getActivity(), dictionary_wheel, mainlistview, gridview_goods, tv_head, fragmentCallBack);
                            mainlistview.setAdapter(customExpandableListAdapter);
                            mainlistview.expandGroup(0);
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
                AppContext.makeToast(getActivity(), "error_connectServer");
            }
        });
    }

    public void setHeadText(String string)
    {
        tv_head.setText(string);
        tv_head.setTextColor(0xFFFF5D5E);
//        tv_head.setTextSize(getActivity().getResources().getDimension(R.dimen.size_sp_9));
        TextPaint tp = tv_head.getPaint();
        tp.setFakeBoldText(true);
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        fragmentCallBack = (AddStd_Cmd) activity;
    }

}
