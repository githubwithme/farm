package com.farm.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.adapter.Adapter_CreateSellOrderDetail_NCZ;
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
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ${hmj} on 2016/1/20.
 */

/**
 * 1
 */
@EActivity(R.layout.ncz_createorder)
public class NCZ_DirectCreateOrder extends Activity
{
    List<HashMap<String, String>> uuids;
    String batchtime = "";
    List<SellOrderDetail_New> list_SellOrderDetail;
    Adapter_CreateSellOrderDetail_NCZ adapter_sellOrderDetail;
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
    EditText et_address;
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
    @ViewById
    TextView tv_allnumber;

    @Click
    void btn_sure()
    {
        if (et_name.getText().toString().equals(""))
        {
            Toast.makeText(NCZ_DirectCreateOrder.this, "请先填写信息", Toast.LENGTH_SHORT).show();
            return;
        }
        if (et_email.getText().toString().equals(""))
        {
            Toast.makeText(NCZ_DirectCreateOrder.this, "请先填写信息", Toast.LENGTH_SHORT).show();
            return;
        }
        if (et_address.getText().toString().equals(""))
        {
            Toast.makeText(NCZ_DirectCreateOrder.this, "请先填写信息", Toast.LENGTH_SHORT).show();
            return;
        }
        if (et_phone.getText().toString().equals(""))
        {
            Toast.makeText(NCZ_DirectCreateOrder.this, "请先填写信息", Toast.LENGTH_SHORT).show();
            return;
        }
        if (et_price.getText().toString().equals(""))
        {
            Toast.makeText(NCZ_DirectCreateOrder.this, "请先填写信息", Toast.LENGTH_SHORT).show();
            return;
        }
        if (et_weight.getText().toString().equals(""))
        {
            Toast.makeText(NCZ_DirectCreateOrder.this, "请先填写信息", Toast.LENGTH_SHORT).show();
            return;
        }
        if (et_values.getText().toString().equals(""))
        {
            Toast.makeText(NCZ_DirectCreateOrder.this, "请先填写信息", Toast.LENGTH_SHORT).show();
            return;
        }

        String batchtime = "";
        String producer = "";
        List<String> list_batchtime = new ArrayList<>();
        List<String> list_producer = new ArrayList<>();
        for (int i = 0; i < list_SellOrderDetail.size(); i++)
        {
            if (i == 0)
            {
                list_batchtime.add(list_SellOrderDetail.get(0).getBatchTime());
                batchtime = batchtime + list_SellOrderDetail.get(0).getBatchTime() + ";";
            }
            if (i == 0)
            {
                list_producer.add(list_SellOrderDetail.get(0).getparkname());
                producer = producer + list_SellOrderDetail.get(0).getparkname() + ";";
            }
            for (int j = 0; j < list_batchtime.size(); j++)
            {
                if (!list_SellOrderDetail.get(i).getBatchTime().equals(list_batchtime.get(j)))
                {
                    list_batchtime.add(list_SellOrderDetail.get(i).getBatchTime());
                    batchtime = batchtime + list_SellOrderDetail.get(i).getBatchTime() + ";";
                    break;
                }
            }
            for (int j = 0; j < list_producer.size(); j++)
            {
                if (!list_SellOrderDetail.get(i).getparkname().equals(list_producer.get(j)))
                {
                    list_producer.add(list_SellOrderDetail.get(i).getparkname());
                    producer = producer + list_SellOrderDetail.get(i).getparkname() + ";";
                    break;
                }
            }

        }

        String uuid = java.util.UUID.randomUUID().toString();
        SellOrder_New sellOrder = new SellOrder_New();
        sellOrder.setid("");
        sellOrder.setUid("60");
        sellOrder.setUuid(uuid);
        sellOrder.setBatchTime(batchtime);
        sellOrder.setSelltype("0");
        sellOrder.setStatus("0");
        sellOrder.setBuyers(et_name.getText().toString());
        sellOrder.setAddress(et_address.getText().toString());
        sellOrder.setEmail(et_email.getText().toString());
        sellOrder.setPhone(et_phone.getText().toString());
        sellOrder.setPrice(et_price.getText().toString());
        sellOrder.setNumber(String.valueOf(countAllNumber()));
        sellOrder.setWeight(et_weight.getText().toString());
        sellOrder.setSumvalues(et_values.getText().toString());
        sellOrder.setActualprice("");
        sellOrder.setActualweight("");
        sellOrder.setActualnumber("");
        sellOrder.setActualsumvalues("");
        sellOrder.setDeposit("0");
        sellOrder.setReg(utils.getTime());
        sellOrder.setSaletime(utils.getTime());
        sellOrder.setYear(utils.getYear());
        sellOrder.setNote(et_note.getText().toString());
        sellOrder.setXxzt("0");
        sellOrder.setProducer(producer);

        for (int i = 0; i < list_SellOrderDetail.size(); i++)
        {
            list_SellOrderDetail.get(i).setsaleid(uuid);
            list_SellOrderDetail.get(i).setType("salein");
        }

        List<SellOrder_New> SellOrderList = new ArrayList<>();
        SellOrderList.add(sellOrder);
        StringBuilder builder = new StringBuilder();
        builder.append("{\"SellOrderList\": ");
        builder.append(JSON.toJSONString(SellOrderList));
        builder.append(", \"SellOrderDetailLists\": ");
        builder.append(JSON.toJSONString(list_SellOrderDetail));
        builder.append(", \"uuids\": ");
        builder.append(JSON.toJSONString(uuids));
        builder.append("} ");
        addOrder(uuid, builder.toString());
    }

    @AfterViews
    void afterOncreate()
    {
        tv_allnumber.setText("共售" + String.valueOf(countAllNumber()) + "株");
        adapter_sellOrderDetail = new Adapter_CreateSellOrderDetail_NCZ(NCZ_DirectCreateOrder.this, list_SellOrderDetail);
        lv.setAdapter(adapter_sellOrderDetail);
        utils.setListViewHeight(lv);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        list_SellOrderDetail = getIntent().getParcelableArrayListExtra("list");
        Bundle bundle = getIntent().getExtras();
        ArrayList arraylist = bundle.getParcelableArrayList("list_uuid");
        uuids = (List<HashMap<String, String>>) arraylist.get(0);
    }

    public int countAllNumber()
    {
        int allnumber = 0;
        for (int i = 0; i < list_SellOrderDetail.size(); i++)
        {
            allnumber = allnumber + Integer.valueOf(list_SellOrderDetail.get(i).getplannumber());
        }
        return allnumber;
    }


    private void addOrder(String uuid, String data)
    {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uuid", uuid);
        params.addQueryStringParameter("action", "directCreateOrder");
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
                        Toast.makeText(NCZ_DirectCreateOrder.this, "订单创建成功！", Toast.LENGTH_SHORT).show();
                        Intent intent1 = new Intent();
                        intent1.setAction(AppContext.BROADCAST_FINISH);
                        sendBroadcast(intent1);

                        Intent intent2 = new Intent();
                        intent2.setAction(AppContext.BROADCAST_UPDATESELLORDER);
                        sendBroadcast(intent2);
                        finish();
                    }

                } else
                {
                    AppContext.makeToast(NCZ_DirectCreateOrder.this, "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(NCZ_DirectCreateOrder.this, "error_connectServer");
            }
        });
    }
}
