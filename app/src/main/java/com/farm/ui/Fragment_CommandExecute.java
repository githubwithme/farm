package com.farm.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.adapter.CommandExecute_Adapter;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.Result;
import com.farm.bean.commandtab;
import com.farm.bean.commembertab;
import com.farm.bean.jobtab;
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
public class Fragment_CommandExecute extends Fragment
{
    commandtab commandtab;
    CommandExecute_Adapter commandExecute_adapter;
    @ViewById
    TextView tv_zyts;
    @ViewById
    ExpandableListView expandableListView;
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
        getJobs();
//        getTestData("pfbz");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_commandexecute, container, false);
        commandtab = getArguments().getParcelable("bean");
        return rootView;
    }

//    private void getTestData(String from)
//    {
//        JSONObject jsonObject = utils.parseJsonFile(getActivity(), "dictionary.json");
//        Result result = JSON.parseObject(jsonObject.getString(from), Result.class);
//        List<Dictionary> listData = JSON.parseArray(result.getRows().toJSONString(), Dictionary.class);
//
//        commandExecute_adapter = new CommandExecute_Adapter(getActivity(), listData.get(0), expandableListView);
//        expandableListView.setAdapter(commandExecute_adapter);
//        for (int i = 0; i < listData.get(0).getFirstItemName().size(); i++)
//        {
//            expandableListView.expandGroup(i);
//        }
//    }

    private void getJobs()
    {
        commembertab commembertab = AppContext.getUserInfo(getActivity());
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("commandid", commandtab.getId());
        params.addQueryStringParameter("userid", commembertab.getId());
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("username", commembertab.getrealName());
        params.addQueryStringParameter("action", "jobListGetByComID");
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
//                        command_execute_adapter = new Command_Execute_Adapter(getActivity(), expandableListView,listNewData);
//                        expandableListView.setAdapter(command_execute_adapter);
//                        utils.setListViewHeight(expandableListView);

                        List<jobtab> jobtabs = listNewData.get(0).getJobList();
                        commandExecute_adapter = new CommandExecute_Adapter(getActivity(), listNewData, expandableListView);
                        expandableListView.setAdapter(commandExecute_adapter);
                        for (int i = 0; i < listNewData.size(); i++)
                        {
                            expandableListView.expandGroup(i);
                        }
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
                AppContext.makeToast(getActivity(), "error_connectServer");
            }
        });
    }

}
