package com.farm.ui;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.adapter.NCZ_DLExecute_Adapter;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.BatchTime;
import com.farm.bean.BreakOff_New;
import com.farm.bean.Result;
import com.farm.bean.commembertab;
import com.farm.bean.parkweathertab;
import com.farm.common.FileHelper;
import com.farm.common.utils;
import com.farm.widget.MyDatepicker;
import com.farm.widget.MyDialog;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by user on 2016/5/1.
 */
@EFragment
public class CZ_DLFragment extends Fragment
{
     commembertab commembertab;
    NCZ_DLExecute_Adapter ncz_dlExecute_adapter;
    MyDialog myDialog;
    Fragment mContent = new Fragment();
    @ViewById
    TextView tv_title;
   @ViewById
   LinearLayout cz_startdl;
    @ViewById
    TextView startdl;
    @ViewById
   TextView tv_timelimit;
    @ViewById
    ExpandableListView expandableListView;
    @ViewById
    RelativeLayout rl_view;

    @Click
    void startdl()
    {
        cz_startdl.setVisibility(View.GONE);
        View dialog_layout = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.customdialog_callback, null);
        myDialog = new MyDialog(getActivity(), R.style.MyDialog, dialog_layout, "断蕾", "是否选择"+tv_timelimit.getText().toString()+"这个时间为开始断蕾？", "确认", "取消", new MyDialog.CustomDialogListener()
        {
            @Override
            public void OnClick(View v)
            {
                switch (v.getId())
                {
                    case R.id.btn_sure:
                        cz_startdl.setVisibility(View.GONE);
                        getcreateBatchTime();
                        myDialog.dismiss();
                        break;
                    case R.id.btn_cancle:

                        myDialog.dismiss();
                        break;
                }
            }
        });
        myDialog.show();
    }
    @Click
    void tv_timelimit()
    {
//        tv_timelimit.setTextColor(getContext().getResources().getColor(R.color.bg_yellow));
        MyDatepicker myDatepicker = new MyDatepicker(getActivity(), tv_timelimit);
        myDatepicker.getDialog().show();
    }
    //刷新
    @Click
    void shuaxin()
    {
        getBatchTimeOfPark();
    }

    @AfterViews
    void afteroncreate()
    {
        tv_title.setText(commembertab.getparkName()+"断蕾情况"  );
        SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);
        tv_timelimit.setText(str);
//        tv_timelimit.setTextColor(getContext().getResources().getColor(R.color.gray));
        getIsStartBreakOff();
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.cz_dlfragment, container, false);
         commembertab = AppContext.getUserInfo(getActivity());
        return rootView;
    }


    public void getIsStartBreakOff()
    {

         commembertab = AppContext.getUserInfo(getActivity());
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("parkid",commembertab.getparkId());
        params.addQueryStringParameter("year",utils.getYear());
        params.addQueryStringParameter("action", "IsStartBreakOff");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String a = responseInfo.result;
                List<BatchTime> listNewData = null;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    if (result.getAffectedRows() > 0)
                    {
                        rl_view.setVisibility(View.GONE);
                        cz_startdl.setVisibility(View.GONE);
                        getBatchTimeOfPark();
//                        getBatchTimeOfPark_temp();
                  /*      listNewData = JSON.parseArray(result.getRows().toJSONString(), BatchTime.class);

                        ncz_dlExecute_adapter=new NCZ_DLExecute_Adapter(getActivity(), listNewData, expandableListView);
                        expandableListView.setAdapter(ncz_dlExecute_adapter);
                        cz_startdl.setVisibility(View.GONE);
//                        utils.setListViewHeight(expandableListView);
                        for (int i = 0; i < listNewData.size(); i++)
                        {
                            expandableListView.expandGroup(i);//展开
//                                  expandableListView.collapseGroup(i);//关闭
                        }*/

                    }else
                    {
                        rl_view.setVisibility(View.GONE);
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
   public void  getcreateBatchTime()
   {

  /*     SimpleDateFormat formatter = new SimpleDateFormat ("yyyy");
       Date curDate = new Date(System.currentTimeMillis());//获取当前时间
       String str = formatter.format(curDate);*/
       commembertab commembertab = AppContext.getUserInfo(getActivity());
       RequestParams params = new RequestParams();
       params.addQueryStringParameter("uid", commembertab.getuId());
       params.addQueryStringParameter("parkid",commembertab.getparkId());
       params.addQueryStringParameter("year",utils.getYear());
       params.addQueryStringParameter("startday",tv_timelimit.getText().toString());
       params.addQueryStringParameter("action", "createBatchTime");
       HttpUtils http = new HttpUtils();
       http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
       {
           @Override
           public void onSuccess(ResponseInfo<String> responseInfo)
           {
               String a = responseInfo.result;
               List<BatchTime> listNewData = null;
               Result result = JSON.parseObject(responseInfo.result, Result.class);
               if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
               {
                   if (result.getAffectedRows() > 0)
                   {
                       cz_startdl.setVisibility(View.GONE);
                       getBatchTimeOfPark();

                   } else
                   {
                       listNewData = new ArrayList<BatchTime>();
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
    //测试数据
   public void getBatchTimeOfPark_temp()
    {
        List<BatchTime> listNewData = null;
        listNewData=FileHelper.getAssetsData(getActivity(), "getDayWeatherAllHour", BatchTime.class);
        ncz_dlExecute_adapter=new NCZ_DLExecute_Adapter(getActivity(), listNewData, expandableListView);
        expandableListView.setAdapter(ncz_dlExecute_adapter);
    }
    public void  getBatchTimeOfPark()
   {

  /*     SimpleDateFormat formatter = new SimpleDateFormat ("yyyy");
       Date curDate = new Date(System.currentTimeMillis());//获取当前时间
       String str = formatter.format(curDate);*/
       commembertab commembertab = AppContext.getUserInfo(getActivity());
       RequestParams params = new RequestParams();
       params.addQueryStringParameter("uid", commembertab.getuId());
       params.addQueryStringParameter("parkid",commembertab.getparkId());
       params.addQueryStringParameter("year",utils.getYear());
       params.addQueryStringParameter("action", "getBatchTimeOfPark");
       HttpUtils http = new HttpUtils();
       http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
       {
           @Override
           public void onSuccess(ResponseInfo<String> responseInfo)
           {
               String a = responseInfo.result;
               List<BatchTime> listNewData = null;
               Result result = JSON.parseObject(responseInfo.result, Result.class);
               if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
               {
                   if (result.getAffectedRows() > 0)
                   {
                       listNewData = JSON.parseArray(result.getRows().toJSONString(), BatchTime.class);
                       ncz_dlExecute_adapter=new NCZ_DLExecute_Adapter(getActivity(), listNewData, expandableListView);
                       expandableListView.setAdapter(ncz_dlExecute_adapter);
                       utils.setListViewHeight(expandableListView);
                        for (int i = 0; i < listNewData.size(); i++)
                        {
                            expandableListView.expandGroup(i);//展开
//                                  expandableListView.collapseGroup(i);//关闭
                        }
                       cz_startdl.setVisibility(View.GONE);

                   } else
                   {
                       listNewData = new ArrayList<BatchTime>();
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

}
