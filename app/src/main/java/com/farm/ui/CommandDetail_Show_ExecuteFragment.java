package com.farm.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.adapter.Common_CommandExecute_Adapter;
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

@EFragment
public class CommandDetail_Show_ExecuteFragment extends Fragment
{
    commandtab commandtab;
    @ViewById
    ListView lv;
    @ViewById
    RelativeLayout rl_pb;
    @ViewById
    LinearLayout ll_tip;
    @ViewById
    ProgressBar pb;
    @ViewById
    TextView tv_tip;


    @AfterViews
    void afterOncreate()
    {
        getJobList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.commanddetail_show_execute, null);
        commandtab = getArguments().getParcelable("bean");
        return view;
    }


    private void getJobList()
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
                        rl_pb.setVisibility(View.GONE);
                        listNewData = JSON.parseArray(result.getRows().toJSONString(), commandtab.class);
                        List<jobtab> jobtabs = listNewData.get(0).getJobList();
                        if (jobtabs == null)
                        {

                        } else
                        {
                            Common_CommandExecute_Adapter common_commandExecute_adapter = new Common_CommandExecute_Adapter(getActivity(), jobtabs);
                            lv.setAdapter(common_commandExecute_adapter);
                        }

                    } else
                    {
                        listNewData = new ArrayList<commandtab>();
                        ll_tip.setVisibility(View.VISIBLE);
                        tv_tip.setText("暂无数据！");
                        pb.setVisibility(View.GONE);
                    }
                } else
                {
                    ll_tip.setVisibility(View.VISIBLE);
                    tv_tip.setText("数据加载异常！");
                    pb.setVisibility(View.GONE);
                    return;
                }
            }

            @Override
            public void onFailure(HttpException e, String s)
            {
                ll_tip.setVisibility(View.VISIBLE);
                tv_tip.setText("网络连接异常！");
                pb.setVisibility(View.GONE);
            }
        });
    }

}
