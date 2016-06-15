package com.farm.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.farm.R;
import com.farm.adapter.Adapter_CreateSellOrderDetail_NCZ;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.Purchaser;
import com.farm.bean.Result;
import com.farm.bean.SellOrderDetail;
import com.farm.bean.SellOrderDetail_New;
import com.farm.bean.SellOrder_New;
import com.farm.bean.commembertab;
import com.farm.common.FileHelper;
import com.farm.common.utils;
import com.farm.widget.CustomDialog_ListView;
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
@EActivity(R.layout.ncz_createorder)
public class NCZ_CreateMoreOrder extends Activity
{
    MyDialog myDialog;
    CustomDialog_ListView customDialog_listView;
    String zzsl = "";
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
    TextView et_values;
    @ViewById
    TextView et_name;
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

    List<Purchaser> listNewData = null;



    @Click
    void et_name()
    {

//        listNewData = FileHelper.getAssetsData(NCZ_CreateMoreOrder.this, "getPurchaser", Purchaser.class);

 /*       JSONObject jsonObject = utils.parseJsonFile(NCZ_CreateMoreOrder.this, "dictionary.json");
        JSONArray jsonArray = JSONArray.parseArray(jsonObject.getString("Happen"));
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < jsonArray.size(); i++)
        {
            list.add(jsonArray.getString(i));
        }*/
        List<String> listdata = new ArrayList<String>();
        List<String> listid = new ArrayList<String>();
        for (int i = 0; i < listNewData.size(); i++)
        {
            listdata.add(listNewData.get(i).getName());
            listid.add(listNewData.get(i).getId());
        }
        showDialog_workday(listdata, listid);
    }

    @Click
    void btn_sure()
    {
        commembertab commembertab = AppContext.getUserInfo(NCZ_CreateMoreOrder.this);
        if (et_name.getText().toString().equals(""))
        {
            Toast.makeText(NCZ_CreateMoreOrder.this, "请先填写信息", Toast.LENGTH_SHORT).show();
            return;
        }
        if (et_email.getText().toString().equals(""))
        {
            Toast.makeText(NCZ_CreateMoreOrder.this, "请先填写信息", Toast.LENGTH_SHORT).show();
            return;
        }
        if (et_address.getText().toString().equals(""))
        {
            Toast.makeText(NCZ_CreateMoreOrder.this, "请先填写信息", Toast.LENGTH_SHORT).show();
            return;
        }
        if (et_phone.getText().toString().equals(""))
        {
            Toast.makeText(NCZ_CreateMoreOrder.this, "请先填写信息", Toast.LENGTH_SHORT).show();
            return;
        }
        if (et_price.getText().toString().equals(""))
        {
            Toast.makeText(NCZ_CreateMoreOrder.this, "请先填写信息", Toast.LENGTH_SHORT).show();
            return;
        }
        if (et_weight.getText().toString().equals(""))
        {
            Toast.makeText(NCZ_CreateMoreOrder.this, "请先填写信息", Toast.LENGTH_SHORT).show();
            return;
        }
        if (et_values.getText().toString().equals(""))
        {
            Toast.makeText(NCZ_CreateMoreOrder.this, "请先填写信息", Toast.LENGTH_SHORT).show();
            return;
        }
        //

        SmsManager smsMessage = SmsManager.getDefault();
        smsMessage.sendTextMessage(et_phone.getText().toString(), null, "单价:"+et_price.getText().toString()+"元,重量:"+et_weight.getText().toString()+"斤,总价:"+et_values.getText().toString()+"元", null, null);
        //
        List<String> list_uuid = new ArrayList<>();
        String batchtime = "";
        String producer = "";
        List<String> list_batchtime = new ArrayList<>();
        List<String> list_producer = new ArrayList<>();
        for (int i = 0; i < list_SellOrderDetail.size(); i++)
        {
            list_uuid.add(list_SellOrderDetail.get(i).getUuid());
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
                if (list_SellOrderDetail.get(i).getBatchTime().equals(list_batchtime.get(j)))
                {
                    break;
                } else if (i == list_batchtime.size() - 1)
                {
                    list_batchtime.add(list_SellOrderDetail.get(i).getBatchTime());
                    batchtime = batchtime + list_SellOrderDetail.get(i).getBatchTime() + ";";
                }
            }
            for (int j = 0; j < list_producer.size(); j++)
            {
                if (list_SellOrderDetail.get(i).getparkname().equals(list_producer.get(j)))
                {
                    break;
                } else if (i == list_producer.size() - 1)
                {
                    list_producer.add(list_SellOrderDetail.get(i).getparkname());
                    producer = producer + list_SellOrderDetail.get(i).getparkname() + ";";
                }
            }

        }
        String uuid = java.util.UUID.randomUUID().toString();
        SellOrder_New sellOrder = new SellOrder_New();
        sellOrder.setid("");
        sellOrder.setUid(commembertab.getuId());
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


        List<SellOrder_New> SellOrderList = new ArrayList<>();
        SellOrderList.add(sellOrder);
        StringBuilder builder = new StringBuilder();
        builder.append("{\"SellOrderList\": ");
        builder.append(JSON.toJSONString(SellOrderList));
        builder.append(", \"SellOrderDetailLists\": ");
        builder.append(JSON.toJSONString(list_uuid));
        builder.append("} ");
        addOrder(uuid, builder.toString());
    }

    @AfterViews
    void afterOncreate()
    {
        getpurchaser();
        et_price.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void afterTextChanged(Editable editable)
            {
                if (et_price.getText().toString().equals(""))
                {
//                    Toast.makeText(NCZ_CreateMoreOrder.this, "请先填写单价", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (et_weight.getText().toString().equals(""))
                {
//                    Toast.makeText(NCZ_CreateMoreOrder.this, "请先填写重量", Toast.LENGTH_SHORT).show();
                    return;
                }
                double ss = Double.valueOf(et_price.getText().toString()) * Double.valueOf(et_weight.getText().toString());
                et_values.setText(String.format("%.2f",ss));
            }
        });
        et_weight.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void afterTextChanged(Editable editable)
            {
                if (et_price.getText().toString().equals(""))
                {
//                    Toast.makeText(NCZ_CreateMoreOrder.this, "请先填写单价", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (et_weight.getText().toString().equals(""))
                {
//                    Toast.makeText(NCZ_CreateMoreOrder.this, "请先填写重量", Toast.LENGTH_SHORT).show();
                    return;
                }
                double ss = Double.valueOf(et_price.getText().toString()) * Double.valueOf(et_weight.getText().toString());
                et_values.setText(String.format("%.2f",ss));
            }
        });
        getNewSaleList();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
//        list_SellOrderDetail = getIntent().getParcelableArrayListExtra("list");
//        Bundle bundle = getIntent().getExtras();
//        ArrayList arraylist = bundle.getParcelableArrayList("list_uuid");
//        uuids = (List<HashMap<String, String>>) arraylist.get(0);
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

    private void getNewSaleList()
    {
        commembertab commembertab = AppContext.getUserInfo(NCZ_CreateMoreOrder.this);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("year", utils.getYear());
        params.addQueryStringParameter("action", "getSellOrderDetailList");//jobGetList1
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
                    if (result.getAffectedRows() != 0)
                    {
                        list_SellOrderDetail = JSON.parseArray(result.getRows().toJSONString(), SellOrderDetail_New.class);
                        adapter_sellOrderDetail = new Adapter_CreateSellOrderDetail_NCZ(NCZ_CreateMoreOrder.this, list_SellOrderDetail);
                        lv.setAdapter(adapter_sellOrderDetail);
                        utils.setListViewHeight(lv);
                        tv_allnumber.setText("共售" + String.valueOf(countAllNumber()) + "株");
                    } else
                    {
                        list_SellOrderDetail = new ArrayList<SellOrderDetail_New>();
                    }

                } else
                {
                    AppContext.makeToast(NCZ_CreateMoreOrder.this, "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(NCZ_CreateMoreOrder.this, "error_connectServer");
            }
        });
    }

    private void addOrder(String uuid, String data)
    {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("saleid", uuid);
        params.addQueryStringParameter("action", "createOrder");
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
                        Toast.makeText(NCZ_CreateMoreOrder.this, "订单创建成功！", Toast.LENGTH_SHORT).show();
                        Intent intent1 = new Intent();
                        intent1.setAction(AppContext.BROADCAST_UPDATESELLORDER);
                        sendBroadcast(intent1);
                        finish();
                    }

                } else
                {
                    AppContext.makeToast(NCZ_CreateMoreOrder.this, "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(NCZ_CreateMoreOrder.this, "error_connectServer");
            }
        });
    }

    public void showDialog_workday(List<String> listdata, List<String> listid)
    {
        View dialog_layout = (RelativeLayout) NCZ_CreateMoreOrder.this.getLayoutInflater().inflate(R.layout.customdialog_listview, null);
        customDialog_listView = new CustomDialog_ListView(NCZ_CreateMoreOrder.this, R.style.MyDialog, dialog_layout, listdata, listid, new CustomDialog_ListView.CustomDialogListener()
        {
            @Override
            public void OnClick(Bundle bundle)
            {
                //id也是有的
                zzsl = bundle.getString("name");
                et_name.setText(zzsl);

                for (int i = 0; i < listNewData.size(); i++)
                {
                    if (listNewData.get(i).getName().equals(zzsl))
                    {
                        et_phone.setText(listNewData.get(i).getTelephone());
                        et_address.setText(listNewData.get(i).getAddress());
                        et_email.setText(listNewData.get(i).getMailbox());
                    }
                }


            }
        });
        customDialog_listView.show();
    }

    private void getpurchaser()
    {
        commembertab commembertab = AppContext.getUserInfo(NCZ_CreateMoreOrder.this);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("action", "getpurchaser");//jobGetList1
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
                    if (result.getAffectedRows() != 0)
                    {
                        if (result.getAffectedRows() != 0)
                        {
                            listNewData=JSON.parseArray(result.getRows().toJSONString(), Purchaser.class);
                        } else
                        {
                            listNewData = new ArrayList<Purchaser>();
                        }

                    } else
                    {
                        listNewData = new ArrayList<Purchaser>();
                    }

                } else
                {
                    AppContext.makeToast(NCZ_CreateMoreOrder.this, "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(NCZ_CreateMoreOrder.this, "error_connectServer");
            }
        });
    }
}
