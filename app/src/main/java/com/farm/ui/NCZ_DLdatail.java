package com.farm.ui;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.adapter.NCZ_DLAdapter;
import com.farm.adapter.NCZ_DLExecute_Adapter;
import com.farm.adapter.PG_CKofListAdapter;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.BatchTime;
import com.farm.bean.Result;
import com.farm.bean.Wz_Storehouse;
import com.farm.bean.commembertab;
import com.farm.common.utils;
import com.farm.widget.MyDialog;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2016/5/26.
 */
@EActivity(R.layout.ncz_dldetail)
public class NCZ_DLdatail extends Activity{

    private String id;
    private String name;
    List<Wz_Storehouse> listpeople = new ArrayList<Wz_Storehouse>();
    PopupWindow pw_tab;
    View pv_tab;
    @ViewById
    View line;
//    PG_CKofListAdapter pg_cKlistAdapter;
    NCZ_DLAdapter ncz_dlAdapter;
    com.farm.bean.commembertab commembertab;
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
    void btn_back()
    {
        finish();
    }
    @Click
    void tv_title()
    {
        showPop_title();
    }
@AfterViews
void aftercreat()
{
//    getIsStartBreakOff();
    getlistdata();
}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        commembertab = AppContext.getUserInfo(NCZ_DLdatail.this);
    }
    public void getIsStartBreakOff(final String parkid)
    {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("parkid",parkid);
        params.addQueryStringParameter("year", utils.getYear());
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
                        getBatchTimeOfPark(parkid);

                    }else
                    {
                        rl_view.setVisibility(View.GONE);
                    }

                } else
                {
                    AppContext.makeToast(NCZ_DLdatail.this, "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(NCZ_DLdatail.this, "error_connectServer");
            }
        });
    }

    private void getlistdata() {
        commembertab commembertab = AppContext.getUserInfo(NCZ_DLdatail.this);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", commembertab.getuId());
//        params.addQueryStringParameter("parkId", "16");
//        params.addQueryStringParameter("action", "getGoodsByUid");
        params.addQueryStringParameter("action", "getcontractByUid");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String a = responseInfo.result;
                List<Wz_Storehouse> listNewData = null;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    if (result.getAffectedRows() != 0) {
                        listNewData = JSON.parseArray(result.getRows().toJSONString(), Wz_Storehouse.class);
                        listpeople.addAll(listNewData);
                        id = listNewData.get(0).getId();
                        name = listNewData.get(0).getParkName();
                        tv_title.setText(listNewData.get(0).getParkName() );
                        getIsStartBreakOff(id);
                    } else {
                        listNewData = new ArrayList<Wz_Storehouse>();
                    }
                } else {
                    AppContext.makeToast(NCZ_DLdatail.this, "error_connectDataBase");

                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg) {
                String a = error.getMessage();
                AppContext.makeToast(NCZ_DLdatail.this, "error_connectServer");

            }
        });

    }

    public void showPop_title() {//LAYOUT_INFLATER_SERVICE
        LayoutInflater layoutInflater = (LayoutInflater) NCZ_DLdatail.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        pv_tab = layoutInflater.inflate(R.layout.popup_yq, null);// 外层
        pv_tab.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((keyCode == KeyEvent.KEYCODE_MENU) && (pw_tab.isShowing())) {
                    pw_tab.dismiss();
//                    iv_dowm_tab.setImageResource(R.drawable.ic_down);
                    return true;
                }
                return false;
            }
        });
        pv_tab.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (pw_tab.isShowing()) {
                    pw_tab.dismiss();
//                    iv_dowm_tab.setImageResource(R.drawable.ic_down);
                }
                return false;
            }
        });
        pw_tab = new PopupWindow(pv_tab, LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        pw_tab.showAsDropDown(line, 0, 0);
        pw_tab.setOutsideTouchable(true);


        ListView listview = (ListView) pv_tab.findViewById(R.id.lv_yq);
        ncz_dlAdapter = new NCZ_DLAdapter(NCZ_DLdatail.this, listpeople);
        listview.setAdapter(ncz_dlAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View v, int postion, long arg3) {
                id = listpeople.get(postion).getId();
                name = listpeople.get(postion).getParkName();
                pw_tab.dismiss();
//                iv_dowm_tab.setImageResource(R.drawable.ic_down);
                tv_title.setText(listpeople.get(postion).getParkName() );
                getIsStartBreakOff(listpeople.get(postion).getId());

            }
        });
    }

    public void  getBatchTimeOfPark(String parkid)
    {

  /*     SimpleDateFormat formatter = new SimpleDateFormat ("yyyy");
       Date curDate = new Date(System.currentTimeMillis());//获取当前时间
       String str = formatter.format(curDate);*/
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("parkid", parkid);
        params.addQueryStringParameter("year",utils.getYear());
        params.addQueryStringParameter("action", "getBatchTimeOfPark");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String a = responseInfo.result;
                List<BatchTime> listNewData = null;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    if (result.getAffectedRows() > 0) {
                        listNewData = JSON.parseArray(result.getRows().toJSONString(), BatchTime.class);
                        ncz_dlExecute_adapter = new NCZ_DLExecute_Adapter(NCZ_DLdatail.this, listNewData, expandableListView);
                        expandableListView.setAdapter(ncz_dlExecute_adapter);
                        utils.setListViewHeight(expandableListView);
                        for (int i = 0; i < listNewData.size(); i++) {
                            expandableListView.expandGroup(i);//展开
//                                  expandableListView.collapseGroup(i);//关闭
                        }
                        cz_startdl.setVisibility(View.GONE);

                    } else {
                        listNewData = new ArrayList<BatchTime>();
                    }

                } else {
                    AppContext.makeToast(NCZ_DLdatail.this, "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg) {
                AppContext.makeToast(NCZ_DLdatail.this, "error_connectServer");
            }
        });
    }
}
