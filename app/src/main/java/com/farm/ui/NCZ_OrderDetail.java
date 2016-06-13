package com.farm.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.adapter.Adapter_SellOrderDetail;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.Result;
import com.farm.bean.SellOrderDetail;
import com.farm.bean.SellOrderDetail_New;
import com.farm.bean.SellOrder_New;
import com.farm.common.utils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by ${hmj} on 2016/1/20.
 */

/**
 * 1
 */
@EActivity(R.layout.ncz_orderdetail)
public class NCZ_OrderDetail extends Activity
{
    List<SellOrderDetail_New> list_orderdetail;
    String batchtime;
    SellOrder_New sellOrder;
    Adapter_SellOrderDetail adapter_sellOrderDetail;
    SellOrderDetail SellOrderDetail;
    @ViewById
    LinearLayout ll_flyl;
    @ViewById
    ListView lv;
    @ViewById
    TextView tv_plansumvalues;
    @ViewById
    TextView tv_actualsumvalues;
    @ViewById
    TextView tv_name;
    @ViewById
    TextView tv_address;
    @ViewById
    TextView tv_planprice;
    @ViewById
    TextView tv_actualweight;
    @ViewById
    TextView tv_planweight;
    @ViewById
    TextView tv_number;
    @ViewById
    TextView tv_phone;
    @ViewById
    TextView tv_email;
    @ViewById
    TextView tv_note;
    @ViewById
    TextView tv_deposit;
    @ViewById
    TextView tv_finalpayment;
    @ViewById
    TextView tv_actualnumber;


    @AfterViews
    void afterOncreate()
    {
        adapter_sellOrderDetail = new Adapter_SellOrderDetail(NCZ_OrderDetail.this, list_orderdetail);
        lv.setAdapter(adapter_sellOrderDetail);
        utils.setListViewHeight(lv);
//        getListData();
        showData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        sellOrder = getIntent().getParcelableExtra("bean");
        list_orderdetail = sellOrder.getSellOrderDetailList();
    }


    public int countAllNumber(List<SellOrderDetail_New> list)
    {
        int count_number = 0;
        for (int i = 0; i < list.size(); i++)
        {
            if (list.get(i).getactualnumber() != null && !list.get(i).getactualnumber().equals(""))
            {
                count_number = count_number + Integer.valueOf(list.get(i).getactualnumber());
            }

        }
        tv_actualnumber.setText(String.valueOf(count_number) + "株");
        return count_number;
    }

    public int countAllWeight(List<SellOrderDetail_New> list)
    {
        int count_weight = 0;
        for (int i = 0; i < list.size(); i++)
        {
            if (list.get(i).getactualweight() != null && !list.get(i).getactualweight().equals(""))
            {
                count_weight = count_weight + Integer.valueOf(list.get(i).getactualweight());
            }

        }
        tv_actualsumvalues.setText(count_weight * Float.valueOf(sellOrder.getPrice()) + "元");
        tv_actualweight.setText(String.valueOf(count_weight) + "斤");
        return count_weight;
    }

    private void showData()
    {
        countAllWeight(list_orderdetail);
        countAllNumber(list_orderdetail);
        tv_name.setText(sellOrder.getBuyers());
        tv_planprice.setText(sellOrder.getPrice());
        tv_planweight.setText(sellOrder.getWeight());
        tv_plansumvalues.setText(sellOrder.getSumvalues());
        tv_deposit.setText(sellOrder.getDeposit());
        tv_phone.setText(sellOrder.getPhone());
        tv_address.setText(sellOrder.getAddress());
        tv_email.setText(sellOrder.getEmail());
        tv_note.setText(sellOrder.getNote());
        if (sellOrder.getFinalpayment().equals(""))
        {
            tv_finalpayment.setText("0");
        } else
        {
            tv_finalpayment.setText(sellOrder.getFinalpayment());
        }
    }

    private void addOrder(String uuid, String data)
    {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uuid", uuid);
        params.addQueryStringParameter("action", "addOrder");
        params.setContentType("application/json");
        try
        {
            params.setBodyEntity(new StringEntity(data, "utf-8"));
        } catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        HttpUtils http = new HttpUtils();
        http.configTimeout(60000);
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String a = responseInfo.result;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    if (result.getAffectedRows() != 0)
                    {
                        Toast.makeText(NCZ_OrderDetail.this, "订单创建成功！", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                } else
                {
                    AppContext.makeToast(NCZ_OrderDetail.this, "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(NCZ_OrderDetail.this, "error_connectServer");
            }
        });
    }
}
