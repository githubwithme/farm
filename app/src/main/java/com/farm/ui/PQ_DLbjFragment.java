package com.farm.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.adapter.pq_dlbjGV_adapter;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.BreakOff;
import com.farm.bean.BreakOff_New;
import com.farm.bean.Result;
import com.farm.common.utils;
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
import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import com.farm.bean.contractTab;
import com.farm.bean.commembertab;

/**
 * Created by user on 2016/5/1.
 */
@EActivity(R.layout.pq_dlbjfragment)
public class PQ_DLbjFragment extends Activity {
    List<contractTab> list_contractTab = null;
    commembertab commembertab;
    pq_dlbjGV_adapter pq_dLbjGV_adapter;
    @ViewById
    TextView tv_title;
    @ViewById
    GridView gv_breakoff;
    @ViewById
    Button btn_save;
    String batchtime;
    String batchcolor;
    //    @Click
//    void Btn_save()
//    {
//        List<BreakOff_New> list_breakoff=new ArrayList<>();
//       int count= gv_breakoff.getChildCount();
//        for(int i=0;i<count;i++)
//        {
//            LinearLayout linearLayout= (LinearLayout) gv_breakoff.getChildAt(i);
//            EditText et_number= (EditText) linearLayout.findViewById(R.id.num);
//            if (!et_number.getText().equals("")) // 有填写数量
//            {
//                if (et_number.getTag()==null)//新增数量
//                {
//                    String uuid=java.util.UUID.randomUUID().toString();
//                    BreakOff_New breakoff=new BreakOff_New();
//                    breakoff.setUuid(uuid);
//                    breakoff.setcontractid(list_contractTab.get(i).getid());
//                    breakoff.setcontractname(list_contractTab.get(i).getContractNum());
//                    breakoff.setparkid(list_contractTab.get(i).getparkId());
//                    breakoff.setparkname(list_contractTab.get(i).getparkName());
//                    breakoff.setareaid(list_contractTab.get(i).getAreaId());
//                    breakoff.setareaname(list_contractTab.get(i).getareaName());
//                    breakoff.setnumberofbreakoff(et_number.getText().toString());
////                    breakoff.setBatchColor('');//传过来的批次颜色
////                    breakoff.setBatchTime('');//传过来的批次时间
//                    breakoff.setBreakofftime(utils.getTime());
//                    breakoff.setXxzt("0");
//                    breakoff.setYear(utils.getYear());
//                    list_breakoff.add(breakoff);
//                }else//原来数据
//                {
//                    BreakOff_New breakoff= (BreakOff_New) et_number.getTag();
//                    breakoff.setnumberofbreakoff(et_number.getText().toString());
//                    //
//                    String uuid=java.util.UUID.randomUUID().toString();
//                    breakoff.setUuid(uuid);
//                    breakoff.setcontractid(list_contractTab.get(i).getid());
//                    breakoff.setcontractname(list_contractTab.get(i).getContractNum());
//                    breakoff.setparkid(list_contractTab.get(i).getparkId());
//                    breakoff.setparkname(list_contractTab.get(i).getparkName());
//                    breakoff.setareaid(list_contractTab.get(i).getAreaId());
//                    breakoff.setareaname(list_contractTab.get(i).getareaName());
//                    breakoff.setnumberofbreakoff(et_number.getText().toString());
//                    list_breakoff.add(breakoff);
//                }
//            }
//
//        }
//        StringBuilder builder = new StringBuilder();
//        builder.append("{\"breakOffList\": ");
//        builder.append(JSON.toJSONString(list_breakoff));
//        builder.append("} ");
//        saveBreakOffList(builder.toString());
//    }
    @AfterViews
    void afterview() {
        tv_title.setText(batchtime+"-"+batchcolor);
        getContractList();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        commembertab = AppContext.getUserInfo(PQ_DLbjFragment.this);
        batchtime=getIntent().getStringExtra("batchtime");
        batchcolor=getIntent().getStringExtra("batchcolor");
    }

    public void getContractList() {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("areaid", commembertab.getareaId());
        params.addQueryStringParameter("year", utils.getYear());
        params.addQueryStringParameter("batchTime",batchtime);
        params.addQueryStringParameter("action", "getAllBreakOffByAreaId");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String a = responseInfo.result;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    if (result.getAffectedRows() != 0)
                    {
                        list_contractTab = JSON.parseArray(result.getRows().toJSONString(), contractTab.class);
                        pq_dLbjGV_adapter = new pq_dlbjGV_adapter(PQ_DLbjFragment.this, list_contractTab,batchtime,batchcolor);
                        gv_breakoff.setAdapter(pq_dLbjGV_adapter);
                    }

                } else {
                    AppContext.makeToast(PQ_DLbjFragment.this, "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg) {
                AppContext.makeToast(PQ_DLbjFragment.this, "error_connectServer");
            }
        });
//       getBreakOffList();
    }
//    public void getBreakOffList()
//{
//    RequestParams params = new RequestParams();
//    params.addQueryStringParameter("areaid",commembertab.getareaId() );
//    params.addQueryStringParameter("year",utils.getYear());
//    params.addQueryStringParameter("action", "getBreakOffList");
//    HttpUtils http = new HttpUtils();
//    http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
//    {
//        @Override
//        public void onSuccess(ResponseInfo<String> responseInfo)
//        {
//            String a = responseInfo.result;
//            Result result = JSON.parseObject(responseInfo.result, Result.class);
//            if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
//            {
//
//                    list_BreakOff_New = JSON.parseArray(result.getRows().toJSONString(), BreakOff_New.class);
//                    pq_dLbjGV_adapter=new PQ_DLbjGV_Adapter(PQ_DLbjFragment.this,list_contractTab,list_BreakOff_New);
//                    gv_breakoff.setAdapter(pq_dLbjGV_adapter);
//
//                if (result.getAffectedRows() != 0)
//                {
//                }
//
//            } else
//            {
//                AppContext.makeToast(PQ_DLbjFragment.this, "error_connectDataBase");
//                return;
//            }
//
//        }
//
//        @Override
//        public void onFailure(HttpException error, String msg)
//        {
//            AppContext.makeToast(PQ_DLbjFragment.this, "error_connectServer");
//        }
//    });
//}


}
