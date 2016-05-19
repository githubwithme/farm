package com.farm.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.adapter.Adapter_EditSellOrderDetail_NCZ;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.Result;
import com.farm.bean.SellOrderDetail;
import com.farm.bean.SellOrderDetail_New;
import com.farm.bean.SellOrder_New;
import com.farm.bean.commembertab;
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

/**
 * Created by ${hmj} on 2016/1/20.
 */

/**
 * 1
 */
@EActivity(R.layout.ncz_editorder)
public class NCZ_EditOrder extends Activity
{
    SellOrder_New sellOrder;
    Adapter_EditSellOrderDetail_NCZ adapter_editSellOrderDetail_ncz;
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
    EditText et_number;
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
            Toast.makeText(NCZ_EditOrder.this, "请先填写信息", Toast.LENGTH_SHORT).show();
            return;
        }
        if (et_email.getText().toString().equals(""))
        {
            Toast.makeText(NCZ_EditOrder.this, "请先填写信息", Toast.LENGTH_SHORT).show();
            return;
        }
        if (et_address.getText().toString().equals(""))
        {
            Toast.makeText(NCZ_EditOrder.this, "请先填写信息", Toast.LENGTH_SHORT).show();
            return;
        }
        if (et_phone.getText().toString().equals(""))
        {
            Toast.makeText(NCZ_EditOrder.this, "请先填写信息", Toast.LENGTH_SHORT).show();
            return;
        }
        if (et_price.getText().toString().equals(""))
        {
            Toast.makeText(NCZ_EditOrder.this, "请先填写信息", Toast.LENGTH_SHORT).show();
            return;
        }
        if (et_weight.getText().toString().equals(""))
        {
            Toast.makeText(NCZ_EditOrder.this, "请先填写信息", Toast.LENGTH_SHORT).show();
            return;
        }
        if (et_number.getText().toString().equals(""))
        {
            Toast.makeText(NCZ_EditOrder.this, "请先填写信息", Toast.LENGTH_SHORT).show();
            return;
        }
        if (et_values.getText().toString().equals(""))
        {
            Toast.makeText(NCZ_EditOrder.this, "请先填写信息", Toast.LENGTH_SHORT).show();
            return;
        }
        editOrder(sellOrder.getUuid(), et_name.getText().toString(), et_address.getText().toString(), et_email.getText().toString(), et_phone.getText().toString(), et_price.getText().toString(), et_weight.getText().toString(), et_values.getText().toString(), et_note.getText().toString());

    }

    @AfterViews
    void afterOncreate()
    {
        tv_allnumber.setText("共售" + String.valueOf(countAllNumber()) + "株");
        adapter_editSellOrderDetail_ncz = new Adapter_EditSellOrderDetail_NCZ(NCZ_EditOrder.this, sellOrder.getSellOrderDetailList());
        lv.setAdapter(adapter_editSellOrderDetail_ncz);
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
    }

    public int countAllNumber()
    {
        List<SellOrderDetail_New> list = sellOrder.getSellOrderDetailList();
        int allnumber = 0;
        for (int i = 0; i < list.size(); i++)
        {
            allnumber = allnumber + Integer.valueOf(list.get(i).getplannumber());
        }
        return allnumber;
    }

    private void getListData()
    {
        commembertab commembertab = AppContext.getUserInfo(NCZ_EditOrder.this);
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
                List<SellOrderDetail_New> listNewData = null;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    if (result.getAffectedRows() != 0)
                    {
                        listNewData = JSON.parseArray(result.getRows().toJSONString(), SellOrderDetail_New.class);
                        adapter_editSellOrderDetail_ncz = new Adapter_EditSellOrderDetail_NCZ(NCZ_EditOrder.this, listNewData);
                        lv.setAdapter(adapter_editSellOrderDetail_ncz);
                        utils.setListViewHeight(lv);
                    } else
                    {
                        listNewData = new ArrayList<SellOrderDetail_New>();
                    }
                } else
                {
                    AppContext.makeToast(NCZ_EditOrder.this, "error_connectDataBase");
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
        et_name.setText(sellOrder.getBuyers());
        et_price.setText(sellOrder.getPrice());
//        tv_planweight.setText(sellOrder.getWeight());
//        tv_actualweight.setText(sellOrder.getBuyers());
//        tv_plansumvalues.setText(sellOrder.getBuyers());
//        tv_actualsumvalues.setText(sellOrder.getBuyers());
//        et_deposit.setText(sellOrder.getDeposit());
//        et_finalpayment.setText(sellOrder.getFinalpayment());
        et_phone.setText(sellOrder.getPhone());
        et_address.setText(sellOrder.getAddress());
        et_email.setText(sellOrder.getEmail());
        et_note.setText(sellOrder.getNote());
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
                        Toast.makeText(NCZ_EditOrder.this, "订单创建成功！", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                } else
                {
                    AppContext.makeToast(NCZ_EditOrder.this, "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(NCZ_EditOrder.this, "error_connectServer");
            }
        });
    }

    private void editOrder(String uuid, String buyers, String address, String email, String phone, String price, String weight, String sumvalues, String note)
    {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uuid", uuid);
        params.addQueryStringParameter("buyers", buyers);
        params.addQueryStringParameter("address", address);
        params.addQueryStringParameter("email", email);
        params.addQueryStringParameter("phone", phone);
        params.addQueryStringParameter("price", price);
        params.addQueryStringParameter("weight", weight);
        params.addQueryStringParameter("sumvalues", sumvalues);
        params.addQueryStringParameter("note", note);
        params.addQueryStringParameter("action", "editOrder");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String a = responseInfo.result;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    String rows = result.getRows().get(0).toString();
                    if (rows.equals("1"))
                    {
                        Toast.makeText(NCZ_EditOrder.this, "修改成功！", Toast.LENGTH_SHORT).show();
                    } else if (rows.equals("0"))
                    {
                        Toast.makeText(NCZ_EditOrder.this, "修改失败！", Toast.LENGTH_SHORT).show();
                    }

                } else
                {
                    Toast.makeText(NCZ_EditOrder.this, "修改失败！", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(NCZ_EditOrder.this, "error_connectServer");
            }
        });
    }
}
