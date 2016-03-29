package com.farm.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.adapter.Adapter_SellOrderDetail;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.Result;
import com.farm.bean.SellOrder;
import com.farm.bean.SellOrderDetail;
import com.farm.bean.commembertab;
import com.farm.common.SqliteDb;
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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${hmj} on 2016/1/20.
 */

/**
 * 1
 */
@EActivity(R.layout.createorder)
public class CreateOrder extends Activity
{
    String batchtime;
    List<SellOrderDetail> list_SellOrderDetail;
    Adapter_SellOrderDetail adapter_sellOrderDetail;
    SellOrderDetail SellOrderDetail;
    @ViewById
    LinearLayout ll_flyl;
    @ViewById
    ListView lv;
    @ViewById
    Button btn_sure;
    @ViewById
    EditText et_values;
    @ViewById
    EditText et_name;
    @ViewById
    EditText et_price;
    @ViewById
    EditText et_weight;
    @ViewById
    EditText et_phone;
    @ViewById
    EditText et_email;
    @ViewById
    EditText et_note;


    @Click
    void btn_sure()
    {
        String uuid = java.util.UUID.randomUUID().toString();
        SellOrder sellOrder = new SellOrder();
        sellOrder.setid("");
        sellOrder.setUid("60");
        sellOrder.setUuid(uuid);
        sellOrder.setBatchTime(batchtime);
        sellOrder.setSelltype("0");
        sellOrder.setStatus("0");
        sellOrder.setBuyers(et_name.getText().toString());
        sellOrder.setEmail(et_email.getText().toString());
        sellOrder.setPhone(et_phone.getText().toString());
        sellOrder.setPrice(et_price.getText().toString());
        sellOrder.setNumber(et_weight.getText().toString());
        sellOrder.setWeight("");
        sellOrder.setAllvalues(et_values.getText().toString());
        sellOrder.setActualweight("");
        sellOrder.setActualnumber("");
        sellOrder.setActualallvalues("");
        sellOrder.setReg(utils.getTime());
        sellOrder.setSaletime(utils.getTime());
        boolean issuccess = SqliteDb.save(CreateOrder.this, sellOrder);
        if (issuccess)
        {
            for (int i = 0; i < list_SellOrderDetail.size(); i++)
            {
                SellOrderDetail sellOrderDetail=list_SellOrderDetail.get(i);
                sellOrderDetail.setsaleid(uuid);
                boolean issucc = SqliteDb.save(CreateOrder.this, sellOrderDetail);
                if (!issucc)
                {
                    Toast.makeText(CreateOrder.this, "订单创建失败！", Toast.LENGTH_SHORT).show();
                    break;
                }
                if (i == list_SellOrderDetail.size() - 1)
                {
                    Toast.makeText(CreateOrder.this, "订单创建成功！", Toast.LENGTH_SHORT).show();
                }
            }
        } else
        {
            Toast.makeText(CreateOrder.this, "订单创建失败！", Toast.LENGTH_SHORT).show();
        }
    }

    @AfterViews
    void afterOncreate()
    {
        adapter_sellOrderDetail = new Adapter_SellOrderDetail(CreateOrder.this, list_SellOrderDetail);
        lv.setAdapter(adapter_sellOrderDetail);
        utils.setListViewHeight(lv);
//        getListData();
//        showData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        list_SellOrderDetail = getIntent().getParcelableArrayListExtra("list");
        batchtime = getIntent().getStringExtra("batchtime");
    }


    private void getListData()
    {
        commembertab commembertab = AppContext.getUserInfo(CreateOrder.this);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("comID", SellOrderDetail.getUuid());
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
                List<SellOrderDetail> listNewData = null;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    if (result.getAffectedRows() != 0)
                    {
                        listNewData = JSON.parseArray(result.getRows().toJSONString(), SellOrderDetail.class);
                        adapter_sellOrderDetail = new Adapter_SellOrderDetail(CreateOrder.this, listNewData);
                        lv.setAdapter(adapter_sellOrderDetail);
                        utils.setListViewHeight(lv);
                    } else
                    {
                        listNewData = new ArrayList<SellOrderDetail>();
                    }
                } else
                {
                    AppContext.makeToast(CreateOrder.this, "error_connectDataBase");
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
//        String[] nongzi = SellOrderDetail.getnongziName().split(",");
//        String flyl = "";
//        for (int i = 0; i < nongzi.length; i++)
//        {
//            flyl = flyl + nongzi[i] + "  ;  ";
//        }
//        tv_note.setText(SellOrderDetail.getcommNote());
//        tv_yl.setText(flyl);
//        tv_zyts.setText(SellOrderDetail.getcommDays() + "天");
//        tv_qx.setText(SellOrderDetail.getcommComDate());
//        if (SellOrderDetail.getstdJobType().equals("-1"))
//        {
//            ll_flyl.setVisibility(View.GONE);
//            if (SellOrderDetail.getcommNote().equals(""))
//            {
//                tv_cmdname.setText("暂无说明");
//            } else
//            {
//                tv_cmdname.setText(SellOrderDetail.getcommNote());
//            }
//        } else if (SellOrderDetail.getstdJobType().equals("0"))
//        {
//            if (SellOrderDetail.getcommNote().equals(""))
//            {
//                tv_cmdname.setText("暂无说明");
//            } else
//            {
//                tv_cmdname.setText(SellOrderDetail.getcommNote());
//            }
//        } else
//        {
//            tv_cmdname.setText(SellOrderDetail.getstdJobTypeName() + "——" + SellOrderDetail.getstdJobName());
//        }
    }
}
