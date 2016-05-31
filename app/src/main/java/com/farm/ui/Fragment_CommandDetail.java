package com.farm.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.adapter.Command_ExecuteArea_Adapter;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.Result;
import com.farm.bean.commandtab;
import com.farm.bean.commembertab;
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

/**
 * Created by ${hmj} on 2016/1/20.
 */
@EFragment
public class Fragment_CommandDetail extends Fragment
{
    Command_ExecuteArea_Adapter listAdapter;
    commandtab commandtab;
    @ViewById
    LinearLayout ll_flyl;
    @ViewById
    ListView lv;
    @ViewById
    TextView tv_zyts;
    @ViewById
    TextView tv_cmdname;
    @ViewById
    TextView tv_yl;
    @ViewById
    TextView tv_note;
    @ViewById
    TextView tv_qx;

    @AfterViews
    void afterOncreate()
    {
        commandtab = getArguments().getParcelable("bean");
        getListData();
        showData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_commanddetail, container, false);
        return rootView;
    }

    private void getListData()
    {
        commembertab commembertab = AppContext.getUserInfo(getActivity());
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("comID", commandtab.getId());
        params.addQueryStringParameter("userid", commembertab.getId());
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("username", commembertab.getuserName());
        params.addQueryStringParameter("page_size", "10");
        params.addQueryStringParameter("page_index", "10");
        params.addQueryStringParameter("action", "commandGetListBycomID");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String a = responseInfo.result;
                List<commandtab> listNewData = null;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    if (result.getAffectedRows() != 0)
                    {
                        listNewData = JSON.parseArray(result.getRows().toJSONString(), commandtab.class);
                        listAdapter = new Command_ExecuteArea_Adapter(getActivity(), listNewData);
                        lv.setAdapter(listAdapter);
                        utils.setListViewHeight(lv);
                    } else
                    {
                        listNewData = new ArrayList<commandtab>();
                    }
                } else
                {
                    AppContext.makeToast(getActivity(), "error_connectDataBase");
                    return;
                }
            }

            @Override
            public void onFailure(HttpException e, String s)
            {

            }
        });
    }

    private void showData()
    {
        String[] nongzi = commandtab.getnongziName().split(",");
        String flyl = "";
        for (int i = 0; i < nongzi.length; i++)
        {
            flyl = flyl + nongzi[i] + "  ;  ";
        }
        tv_note.setText(commandtab.getcommNote());
        tv_yl.setText(flyl);
        tv_zyts.setText(commandtab.getcommDays() + "天");
        tv_qx.setText(commandtab.getcommComDate());
        if (commandtab.getstdJobType().equals("-1"))
        {
            ll_flyl.setVisibility(View.GONE);
            if (commandtab.getcommNote().equals(""))
            {
                tv_cmdname.setText("暂无说明");
            } else
            {
                tv_cmdname.setText(commandtab.getcommNote());
            }
        } else if (commandtab.getstdJobType().equals("0"))
        {
            if (commandtab.getcommNote().equals(""))
            {
                tv_cmdname.setText("暂无说明");
            } else
            {
                tv_cmdname.setText(commandtab.getcommNote());
            }
        } else
        {
            tv_cmdname.setText(commandtab.getstdJobTypeName() + "——" + commandtab.getstdJobName());
        }
    }
}
