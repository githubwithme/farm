package com.farm.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.farm.R;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.Dictionary;
import com.farm.bean.Result;
import com.farm.bean.commembertab;
import com.farm.common.utils;
import com.farm.widget.CustomDialog_ListView;
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
 * Created by ${hmj} on 2015/12/15.
 */
@EFragment
public class AddPlantObservation_stepfour extends Fragment
{
    String from = "";
    Dictionary dic_comm;
    CustomDialog_ListView customDialog_listView;
    String jj_gjd_id = "";
    String jj_gjd = "";
    String jj_gz_id = "";
    String jj_gz= "";

    @ViewById
    TextView tv_jj_gjd;
    @ViewById
    TextView tv_jj_gz;
    @ViewById
    TextView tv_jj_zd;
    @ViewById
    TextView tv_jj_yswbl;
    @ViewById
    TextView tv_y_ys;
    @ViewById
    TextView tv_y_yd;


    @Click
    void tv_jj_gjd()
    {
        from = "jj_gjd";
        getTestData(from);
    }

    @Click
    void tv_jj_gz()
    {
        from = "jj_gz";
        getTestData(from);
    }

    @Click
    void tv_jj_zd()
    {
        from = "jj_gjd";
        getTestData(from);
    }

    @Click
    void tv_jj_yswbl()
    {
        from = "jj_gjd";
        getTestData(from);
    }

    @AfterViews
    void afterOncreate()
    {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.addplantobservation_stepfour, container, false);
        return rootView;
    }

    private void getTestData(String from)
    {
        JSONObject jsonObject = utils.parseJsonFile(getActivity(), "dictionary.json");
        Result result = JSON.parseObject(jsonObject.getString(from), Result.class);
        List<Dictionary> lsitNewData = JSON.parseArray(result.getRows().toJSONString(), Dictionary.class);
        dic_comm = lsitNewData.get(0);
        showDialog();
    }

    private void getCommandlist(String from)
    {
        commembertab commembertab = AppContext.getUserInfo(getActivity());
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("name", from);
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
                            dic_comm = lsitNewData.get(0);
                            showDialog();
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


    public void showDialog()
    {
        View dialog_layout = (RelativeLayout) getActivity().getLayoutInflater().inflate(R.layout.customdialog_listview, null);
        customDialog_listView = new CustomDialog_ListView(getActivity(), R.style.MyDialog, dialog_layout, dic_comm.getFirstItemName(), dic_comm.getFirstItemID(), new CustomDialog_ListView.CustomDialogListener()
        {
            @Override
            public void OnClick(Bundle bundle)
            {
                if (from.equals("jj_gjd"))
                {
                    jj_gjd_id = bundle.getString("id");
                    jj_gjd = bundle.getString("name");
                    tv_jj_gjd.setText(jj_gjd);
                } else if (from == "jj_gz")
                {
                    jj_gz_id = bundle.getString("id");
                    jj_gz = bundle.getString("name");
                    tv_jj_gz.setText(jj_gz);
                } else if (from == "")
                {

                } else if (from == "")
                {

                } else if (from == "")
                {

                } else if (from == "")
                {

                }
            }
        });
        customDialog_listView.show();
    }
}
