package com.farm.ui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.InputType;
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
import com.farm.bean.AllType;
import com.farm.bean.PeopelList;
import com.farm.bean.Purchaser;
import com.farm.bean.Result;
import com.farm.bean.SellOrderDetail;
import com.farm.bean.SellOrderDetail_New;
import com.farm.bean.SellOrder_New;
import com.farm.bean.SellOrder_New_First;
import com.farm.bean.commembertab;
import com.farm.common.utils;
import com.farm.widget.CustomDialog_ListView;
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
@EActivity(R.layout.ncz_createneworder)
public class NCZ_CreateNewOrder extends Activity
{
    //    int num=0;
    String mail;
    String telphone;
    @ViewById
    Button btn_addcg;
    @ViewById
    Button btn_addbz;
    @ViewById
    Button btn_addby;
    List<PeopelList> listpeople = new ArrayList<PeopelList>();
    @ViewById
    EditText bz_danjia;
    @ViewById
    EditText dingjin;
    @ViewById
    EditText by_danjia;
    @ViewById
    EditText bz_guige;
    @ViewById
    EditText dd_cl;
    @ViewById
    TextView dd_time;
    @ViewById
    EditText dd_fzr;
    @ViewById
    TextView dd_bz;
    @ViewById
    TextView dd_by;
    MyDialog myDialog;
    CustomDialog_ListView customDialog_listView;
    String zzsl = "";
    String batchtime = "";
    List<SellOrderDetail_New> list_SellOrderDetail;
    Adapter_CreateSellOrderDetail_NCZ adapter_sellOrderDetail;
    SellOrderDetail SellOrderDetail;
    /*   @ViewById
       LinearLayout ll_flyl;*/
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
    @ViewById
    TextView CR_chanpin;
    @ViewById
    LinearLayout ll_more;
    @ViewById
    RelativeLayout rl_more_tip;

    String uuid = "";
    String cgId = "";
    String byId = "";
    String bzId = "";
    String fzrId = "";
    String cpid = "";
    List<Purchaser> listData_CG = new ArrayList<Purchaser>();
    List<Purchaser> listData_BY = new ArrayList<Purchaser>();
    List<Purchaser> listData_BZ = new ArrayList<Purchaser>();
    List<AllType> listAlltype = new ArrayList<AllType>();

    @ViewById
    TextView cheliang_num;


    @Click
    void cheliang_num()
    {
        JSONObject jsonObject = utils.parseJsonFile(NCZ_CreateNewOrder.this, "dictionary.json");
        JSONArray jsonArray = JSONArray.parseArray(jsonObject.getString("number"));
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < jsonArray.size(); i++)
        {
            list.add(jsonArray.getString(i));
        }
        showDialog_carNumber(list);
    }

    public void showDialog_carNumber(List<String> list)
    {
        View dialog_layout = NCZ_CreateNewOrder.this.getLayoutInflater().inflate(R.layout.customdialog_listview, null);
        customDialog_listView = new CustomDialog_ListView(NCZ_CreateNewOrder.this, R.style.MyDialog, dialog_layout, list, list, new CustomDialog_ListView.CustomDialogListener()
        {
            @Override
            public void OnClick(Bundle bundle)
            {
                String workday = bundle.getString("name");
                cheliang_num.setText(workday);

            }
        });
        customDialog_listView.show();
    }

    @Click
    void add_jsd()
    {

        Intent intent = new Intent(NCZ_CreateNewOrder.this, RecoveryDetail_.class);
        intent.putExtra("uuid", uuid);
        SellOrder_New sellOrder_new = new SellOrder_New();
        sellOrder_new.setIsReady("True");
        intent.putExtra("bean", sellOrder_new);
        startActivity(intent);
    }

    @Click
    void btn_save()
    {

    }

    @Click
    void rl_more_tip()
    {
        if (ll_more.isShown())
        {
            ll_more.setVisibility(View.GONE);
        } else
        {
            ll_more.setVisibility(View.VISIBLE);
        }

    }

    @Click
    void btn_addcg()
    {
        Intent intent = new Intent(NCZ_CreateNewOrder.this, Add_workPeopel_.class);
        intent.putExtra("type", "采购商");
        startActivity(intent);
    }

    @Click
    void btn_addby()
    {
        Intent intent = new Intent(NCZ_CreateNewOrder.this, Add_workPeopel_.class);
        intent.putExtra("type", "搬运工头");
        startActivity(intent);
    }

    @Click
    void btn_addbz()
    {
        Intent intent = new Intent(NCZ_CreateNewOrder.this, Add_workPeopel_.class);
        intent.putExtra("type", "包装工头");
        startActivity(intent);
    }

    @Click
    void dd_time()
    {
        MyDatepicker myDatepicker = new MyDatepicker(NCZ_CreateNewOrder.this, dd_time);
        myDatepicker.getDialog().show();
    }

    @Click
    void CR_chanpin()
    {
        List<String> listdata = new ArrayList<String>();
        List<String> listid = new ArrayList<String>();
        for (int i = 0; i < listAlltype.size(); i++)
        {
            listdata.add(listAlltype.get(i).getProductName());
            listid.add(listAlltype.get(i).getId());
        }
        showDialog_fcp(listdata, listid);
    }

    @Click
    void dd_fzr()
    {
        List<String> listdata = new ArrayList<String>();
        List<String> listid = new ArrayList<String>();
        for (int i = 0; i < listpeople.size(); i++)
        {
            listdata.add(listpeople.get(i).getRealName());
            listid.add(listpeople.get(i).getId());
        }
        showDialog_fzr(listdata, listid);
    }

    @Click
    void et_name()
    {

//        listNewData = FileHelper.getAssetsData(NCZ_CreateNewOrder.this, "getPurchaser", Purchaser.class);

//        JSONObject jsonObject = utils.parseJsonFile(NCZ_CreateNewOrder.this, "dictionary.json");
//        JSONArray jsonArray = JSONArray.parseArray(jsonObject.getString("Happen"));
/*        List<String> list = new ArrayList<String>();
        for (int i = 0; i < listNewData.size(); i++)
        {
            list.add(listNewData.get(i).getName());
        }*/
        List<String> listdata = new ArrayList<String>();
        List<String> listid = new ArrayList<String>();
        for (int i = 0; i < listData_CG.size(); i++)
        {
            listdata.add(listData_CG.get(i).getName());
            listid.add(listData_CG.get(i).getId());
        }
        showDialog_workday(listdata, listid);
//        showDialog_workday(list, list);
    }

    @Click
    void dd_bz()
    {
        List<String> listdata = new ArrayList<String>();
        List<String> listid = new ArrayList<String>();
        for (int i = 0; i < listData_BZ.size(); i++)
        {
            listdata.add(listData_BZ.get(i).getName());
            listid.add(listData_BZ.get(i).getId());
        }
        showDialog_bz(listdata, listid);
    }

    @Click
    void dd_by()
    {
        List<String> listdata = new ArrayList<String>();
        List<String> listid = new ArrayList<String>();
        for (int i = 0; i < listData_BY.size(); i++)
        {
            listdata.add(listData_BY.get(i).getName());
            listid.add(listData_BY.get(i).getId());
        }
        showDialog_by(listdata, listid);
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        getNewSaleList();
    }
//
//    @Click
//    void btn_addproduct()
//    {
//        Intent intent = new Intent(NCZ_CreateNewOrder.this, NCZ_CreateOrder_SelectBatchTime_.class);
//        startActivity(intent);
//    }

    @Click
    void btn_back()
    {
//        cancleOrder();
        finish();
    }

    @Click
    void btn_cancleorder()
    {
        cancleOrder();
    }

    @Click
    void btn_sure()
    {
        commembertab commembertab = AppContext.getUserInfo(NCZ_CreateNewOrder.this);
        if (CR_chanpin.getText().toString().equals(""))
        {
            Toast.makeText(NCZ_CreateNewOrder.this, "请先填写信息", Toast.LENGTH_SHORT).show();
            return;
        }
        if (dd_fzr.getText().toString().equals(""))
        {
            Toast.makeText(NCZ_CreateNewOrder.this, "请先填写信息", Toast.LENGTH_SHORT).show();
            return;
        }
        if (dd_time.getText().toString().equals(""))
        {
            Toast.makeText(NCZ_CreateNewOrder.this, "请先填写信息", Toast.LENGTH_SHORT).show();
            return;
        }
        if (et_name.getText().toString().equals(""))
        {
            Toast.makeText(NCZ_CreateNewOrder.this, "请先填写信息", Toast.LENGTH_SHORT).show();
            return;
        }
   /*     if (et_email.getText().toString().equals(""))
        {
            Toast.makeText(NCZ_CreateNewOrder.this, "请先填写信息", Toast.LENGTH_SHORT).show();
            return;
        }*/

  /*      if (et_phone.getText().toString().equals(""))
        {
            Toast.makeText(NCZ_CreateNewOrder.this, "请先填写信息", Toast.LENGTH_SHORT).show();
            return;
        }*/
        if (et_price.getText().toString().equals(""))
        {
            Toast.makeText(NCZ_CreateNewOrder.this, "请先填写信息", Toast.LENGTH_SHORT).show();
            return;
        }
        if (et_weight.getText().toString().equals(""))
        {
            Toast.makeText(NCZ_CreateNewOrder.this, "请先填写信息", Toast.LENGTH_SHORT).show();
            return;
        }
        if (et_values.getText().toString().equals(""))
        {
            Toast.makeText(NCZ_CreateNewOrder.this, "请先填写信息", Toast.LENGTH_SHORT).show();
            return;
        }

        //
/*        String phone = phoneEt.getText().toString();
        String context = contextEt.getText().toString();
        SmsManager manager = SmsManager.getDefault();
        ArrayList<String> list = manager.divideMessage(context);  //因为一条短信有字数限制，因此要将长短信拆分
        for(String text:list){
            manager.sendTextMessage(phone, null, text, null, null);
        }*/

        //短信
        SmsManager smsMessage = SmsManager.getDefault();
//        List<String> divideContents = smsMessage.divideMessage(message);
        smsMessage.sendTextMessage(telphone, null, "单价:" + et_price.getText().toString() + "元,重量:" + et_weight.getText().toString() + "斤,总价:" + et_values.getText().toString() + "元,定金：" + dingjin.getText().toString() + "元，采收时间：" + dd_time.getText().toString(), null, null);
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
            {   //园区名字，注释的是多个园区   目前是单园区，所有另写
//                list_producer.add(list_SellOrderDetail.get(0).getparkname());
//                producer = producer + list_SellOrderDetail.get(0).getparkname() + ";";
                producer = list_SellOrderDetail.get(0).getparkname();
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

        SellOrder_New sellOrder = new SellOrder_New();
        sellOrder.setid("");
        sellOrder.setUid(commembertab.getuId());
        sellOrder.setUuid(uuid);
        sellOrder.setBatchTime(batchtime);
        sellOrder.setSelltype("待付定金");
        sellOrder.setStatus("0");
        sellOrder.setIsReady("False");
//        sellOrder.setBuyers(et_name.getText().toString());
        sellOrder.setBuyers(cgId);
        sellOrder.setAddress(et_address.getText().toString());
        sellOrder.setGoodsname(CR_chanpin.getText().toString());
        sellOrder.setEmail(et_email.getText().toString());
        sellOrder.setPhone(et_phone.getText().toString());
        sellOrder.setPrice(et_price.getText().toString());
        sellOrder.setNumber(String.valueOf(countAllNumber()));
        sellOrder.setWeight(et_weight.getText().toString());
        sellOrder.setSumvalues(et_values.getText().toString());
        sellOrder.setActualweight(cheliang_num.getText().toString());
        sellOrder.setActualprice("");
        sellOrder.setActualnumber("");
        sellOrder.setActualsumvalues("");
        sellOrder.setDefectNum("");
        sellOrder.setDeposit("");
        sellOrder.setReg(utils.getTime());
//        sellOrder.setSaletime(utils.getTime());
        sellOrder.setSaletime(dd_time.getText().toString());
        sellOrder.setYear(utils.getYear());
        sellOrder.setNote(et_note.getText().toString());
        sellOrder.setXxzt("0");
        sellOrder.setProducer(producer);
//        sellOrder.setFinalpayment("");
        sellOrder.setMainPepole(fzrId);
//        sellOrder.setPlateNumber(dd_cl.getText().toString());
        sellOrder.setActualweight(dd_cl.getText().toString());

        sellOrder.setContractorId(bzId);
        sellOrder.setPickId(byId);
        sellOrder.setCarryPrice(by_danjia.getText().toString());
        sellOrder.setPackPrice(bz_danjia.getText().toString());
        sellOrder.setPackPec(bz_guige.getText().toString());
        sellOrder.setWaitDeposit(dingjin.getText().toString());
        sellOrder.setIsNeedAudit("1");
        sellOrder.setFreeFinalPay("2");
        sellOrder.setFreeDeposit("2");
        List<SellOrder_New> SellOrderList = new ArrayList<>();
        SellOrderList.add(sellOrder);
        SellOrder_New_First sellOrder_new_first = new SellOrder_New_First();
        sellOrder_new_first.setSellOrderId(uuid);
        sellOrder_new_first.setQualityWaterWeight("");
        sellOrder_new_first.setQualityNetWeight("");
        sellOrder_new_first.setQualityBalance("");
        sellOrder_new_first.setDefectWaterWeight("");
        sellOrder_new_first.setDefectNetWeight("");
        sellOrder_new_first.setDefectBalance("");
        sellOrder_new_first.setTotal("");
        sellOrder_new_first.setActualMoney("");
        sellOrder_new_first.setQualityTotalWeight("");
        sellOrder_new_first.setDefectTotalWeight("");
        sellOrder_new_first.setTotalWeight("");
        sellOrder_new_first.setPackFee("");
        sellOrder_new_first.setCarryFee("");
        sellOrder_new_first.setTotalFee("");
        sellOrder_new_first.setPersonNote("");

        StringBuilder builder = new StringBuilder();
        builder.append("{\"SellOrderList\": ");
        builder.append(JSON.toJSONString(SellOrderList));

        builder.append(", \"sellorderlistadd\": [");
        builder.append(JSON.toJSONString(sellOrder_new_first));

        builder.append("], \"SellOrderDetailLists\": ");
        builder.append(JSON.toJSONString(list_uuid));
        builder.append("} ");
        addOrder(uuid, builder.toString());
    }

    @AfterViews
    void afterOncreate()
    {
/*        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);
        dd_time.setText(str);*/
        dd_bz.setInputType(InputType.TYPE_NULL);
        dd_by.setInputType(InputType.TYPE_NULL);
        dd_fzr.setInputType(InputType.TYPE_NULL);
        getpurchaser("");
        getchanpin();
        getlistdata();
//        deleNewSaleAddsalefor();
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
                et_values.setText(String.format("%.2f", ss));
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
                et_values.setText(String.format("%.2f", ss));
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        uuid = getIntent().getStringExtra("uuid");
//        uuid = java.util.UUID.randomUUID().toString();

        IntentFilter intentfilter_update = new IntentFilter(AppContext.UPDATEMESSAGE_CHE_LIANG);
        registerReceiver(receiver_update, intentfilter_update);
    }

    BroadcastReceiver receiver_update = new BroadcastReceiver()// 从扩展页面返回信息
    {
        @SuppressWarnings("deprecation")
        @Override
        public void onReceive(Context context, Intent intent)
        {
        /* String    num=getIntent().getStringExtra("num");
            cheliang_num.setText(num);*/
            getDetailSecBysettleId();
        }
    };

    public int countAllNumber()
    {
        int allnumber = 0;
        for (int i = 0; i < list_SellOrderDetail.size(); i++)
        {
            allnumber = allnumber + Integer.valueOf(list_SellOrderDetail.get(i).getplannumber());
        }
        return allnumber;
    }

    private void deleNewSaleAddsalefor()
    {
        commembertab commembertab = AppContext.getUserInfo(this);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("creatorId", commembertab.getId());
        params.addQueryStringParameter("action", "deleNewSaleAddsalefor");//jobGetList1
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
               /* if (result.getAffectedRows() != 0)
                {
                    listData = JSON.parseArray(result.getRows().toJSONString(), SellOrder_New.class);

                } else
                {
                    listData = new ArrayList<SellOrder_New>();
                }*/

                } else
                {
                    AppContext.makeToast(NCZ_CreateNewOrder.this, "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(NCZ_CreateNewOrder.this, "error_connectServer");

            }
        });
    }

    private void getNewSaleList()
    {
        commembertab commembertab = AppContext.getUserInfo(NCZ_CreateNewOrder.this);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("year", utils.getYear());
        params.addQueryStringParameter("creatorId", commembertab.getId());
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
                        adapter_sellOrderDetail = new Adapter_CreateSellOrderDetail_NCZ(NCZ_CreateNewOrder.this, list_SellOrderDetail);
                        lv.setAdapter(adapter_sellOrderDetail);
                        utils.setListViewHeight(lv);
                        tv_allnumber.setText("共售" + String.valueOf(countAllNumber()) + "株");
                    } else
                    {
                        list_SellOrderDetail = new ArrayList<SellOrderDetail_New>();
                    }

                } else
                {
                    AppContext.makeToast(NCZ_CreateNewOrder.this, "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(NCZ_CreateNewOrder.this, "error_connectServer");
            }
        });
    }

    private void addOrder(String uuid, String data)
    {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("saleid", uuid);
        params.addQueryStringParameter("createOrderTye", "1");
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
                        Toast.makeText(NCZ_CreateNewOrder.this, "订单创建成功！", Toast.LENGTH_SHORT).show();
                        Intent intent1 = new Intent();
                        intent1.setAction(AppContext.BROADCAST_FINISHSELECTBATCHTIME);
                        sendBroadcast(intent1);

                        Intent intent2 = new Intent();
                        intent2.setAction(AppContext.BROADCAST_FINISH);
                        sendBroadcast(intent2);
                        finish();
                    }

                } else
                {
                    AppContext.makeToast(NCZ_CreateNewOrder.this, "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(NCZ_CreateNewOrder.this, "error_connectServer");
            }
        });
    }

    //采购商的弹窗
    public void showDialog_workday(List<String> listdata, List<String> listid)
    {
        View dialog_layout = NCZ_CreateNewOrder.this.getLayoutInflater().inflate(R.layout.customdialog_listview, null);
        customDialog_listView = new CustomDialog_ListView(NCZ_CreateNewOrder.this, R.style.MyDialog, dialog_layout, listdata, listid, new CustomDialog_ListView.CustomDialogListener()
        {
            @Override
            public void OnClick(Bundle bundle)
            {
                //id也是有的
                zzsl = bundle.getString("name");
                et_name.setText(zzsl);
                cgId = bundle.getString("id");

                for (int i = 0; i < listData_CG.size(); i++)
                {
                    if (listData_CG.get(i).getName().equals(zzsl))
                    {
                        telphone = listData_CG.get(i).getTelephone();
                        mail = listData_CG.get(i).getMailbox();
                    }
                }


            }
        });
        customDialog_listView.show();
    }

    //包装工
    public void showDialog_bz(List<String> listdata, List<String> listid)
    {
        View dialog_layout = NCZ_CreateNewOrder.this.getLayoutInflater().inflate(R.layout.customdialog_listview, null);
        customDialog_listView = new CustomDialog_ListView(NCZ_CreateNewOrder.this, R.style.MyDialog, dialog_layout, listdata, listid, new CustomDialog_ListView.CustomDialogListener()
        {
            @Override
            public void OnClick(Bundle bundle)
            {
                //id也是有的
                zzsl = bundle.getString("name");
                dd_bz.setText(zzsl);
                bzId = bundle.getString("id");

            }
        });
        customDialog_listView.show();
    }

    //搬运工
    public void showDialog_by(List<String> listdata, List<String> listid)
    {
        View dialog_layout = NCZ_CreateNewOrder.this.getLayoutInflater().inflate(R.layout.customdialog_listview, null);
        customDialog_listView = new CustomDialog_ListView(NCZ_CreateNewOrder.this, R.style.MyDialog, dialog_layout, listdata, listid, new CustomDialog_ListView.CustomDialogListener()
        {
            @Override
            public void OnClick(Bundle bundle)
            {
                //id也是有的
                zzsl = bundle.getString("name");
                dd_by.setText(zzsl);
                byId = bundle.getString("id");

            }
        });
        customDialog_listView.show();
    }

    //产品
    public void showDialog_fcp(List<String> listdata, List<String> listid)
    {
        View dialog_layout = NCZ_CreateNewOrder.this.getLayoutInflater().inflate(R.layout.customdialog_listview, null);
        customDialog_listView = new CustomDialog_ListView(NCZ_CreateNewOrder.this, R.style.MyDialog, dialog_layout, listdata, listid, new CustomDialog_ListView.CustomDialogListener()
        {
            @Override
            public void OnClick(Bundle bundle)
            {
                //id也是有的
                zzsl = bundle.getString("name");
                CR_chanpin.setText(zzsl);
                cpid = bundle.getString("id");

            }
        });
        customDialog_listView.show();
    }

    //负责人
    public void showDialog_fzr(List<String> listdata, List<String> listid)
    {
        View dialog_layout = NCZ_CreateNewOrder.this.getLayoutInflater().inflate(R.layout.customdialog_listview, null);
        customDialog_listView = new CustomDialog_ListView(NCZ_CreateNewOrder.this, R.style.MyDialog, dialog_layout, listdata, listid, new CustomDialog_ListView.CustomDialogListener()
        {
            @Override
            public void OnClick(Bundle bundle)
            {
                //id也是有的
                zzsl = bundle.getString("name");
                dd_fzr.setText(zzsl);
                fzrId = bundle.getString("id");

            }
        });
        customDialog_listView.show();
    }

    private void getpurchaser(String name)
    {
        commembertab commembertab = AppContext.getUserInfo(NCZ_CreateNewOrder.this);
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
                List<Purchaser> listNewData = null;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    if (result.getAffectedRows() != 0)
                    {
                        if (result.getAffectedRows() != 0)
                        {
                            listNewData = JSON.parseArray(result.getRows().toJSONString(), Purchaser.class);
                            for (int i = 0; i < listNewData.size(); i++)
                            {
                                if (listNewData.get(i).userType.equals("采购商"))
                                {
                                    listData_CG.add(listNewData.get(i));
                                } else if (listNewData.get(i).userType.equals("包装工头"))
                                {
                                    listData_BZ.add(listNewData.get(i));
                                } else
                                {
                                    listData_BY.add(listNewData.get(i));
                                }
                            }


                  /*          String[] str = new String[listData_CG.size()];
                            for (int i = 0; i < listData_CG.size(); i++)
                            {
                                str[i] = listData_CG.get(i).getName();
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(NCZ_CreateNewOrder.this,
                                    android.R.layout.simple_dropdown_item_1line, str);
                            et_name.setAdapter(adapter);
                            et_name.setOnItemClickListener(new AdapterView.OnItemClickListener()
                            {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
                                {
                                    Object obj = adapterView.getItemAtPosition(i);
                                    String ss = obj.toString();
                                    for (int j = 0; j < listData_CG.size(); j++)
                                    {
                                        if (listData_CG.get(j).getName().equals(ss))
                                        {
                                            et_phone.setText(listData_CG.get(j).getTelephone());
                                            et_address.setText(listData_CG.get(j).getAddress());
                                            et_email.setText(listData_CG.get(j).getMailbox());
                                        }
                                    }
                                }
                            });*/

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
                    AppContext.makeToast(NCZ_CreateNewOrder.this, "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(NCZ_CreateNewOrder.this, "error_connectServer");
            }
        });
    }

    private void cancleOrder()
    {
        View dialog_layout = getLayoutInflater().inflate(R.layout.customdialog_callback, null);
        myDialog = new MyDialog(NCZ_CreateNewOrder.this, R.style.MyDialog, dialog_layout, "取消订单", "取消订单吗？", "取消", "不取消", new MyDialog.CustomDialogListener()
        {
            @Override
            public void OnClick(View v)
            {
                switch (v.getId())
                {
                    case R.id.btn_sure:
//                        myDialog.dismiss();
//                        deleNewSaleAddsalefor();
                        DeletesellOrderSettlement();
                        Toast.makeText(NCZ_CreateNewOrder.this, "已取消", Toast.LENGTH_SHORT).show();
                        Intent intent1 = new Intent();
                        intent1.setAction(AppContext.BROADCAST_FINISHSELECTBATCHTIME);
                        sendBroadcast(intent1);

                        finish();
                        break;
                    case R.id.btn_cancle:
                        myDialog.dismiss();
                        break;
                }
            }
        });
        myDialog.show();
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event)
//    {
//        if (keyCode == KeyEvent.KEYCODE_BACK)
//        {
//            cancleOrder();
//        }
//        return false;
//
//    }

    //获取人员列表
    private void getlistdata()
    {
        commembertab commembertab = AppContext.getUserInfo(NCZ_CreateNewOrder.this);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("nlevel", "1,2");
        params.addQueryStringParameter("action", "getUserlisttByUID");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String a = responseInfo.result;
                List<PeopelList> listNewData = null;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    if (result.getAffectedRows() != 0)
                    {
                        listNewData = JSON.parseArray(result.getRows().toJSONString(), PeopelList.class);

                        listpeople.addAll(listNewData);
                        //方法一
                     /*   List<String> list = new ArrayList<String>();
                        for (int i = 0; i < listNewData.size(); i++)
                        {
                            list.add(listNewData.get(i).getUserlevelName()+"-"+listNewData.get(i).getRealName());
//                            list.add(jsonArray.getString(i));
                        }
                        showDialog_workday(list);*/
                    } else
                    {
                        listNewData = new ArrayList<PeopelList>();
                    }
                } else
                {
                    AppContext.makeToast(NCZ_CreateNewOrder.this, "error_connectDataBase");

                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                String a = error.getMessage();
                AppContext.makeToast(NCZ_CreateNewOrder.this, "error_connectServer");

            }
        });
    }


    private void getchanpin()
    {
        commembertab commembertab = AppContext.getUserInfo(NCZ_CreateNewOrder.this);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("action", "getProduct");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String a = responseInfo.result;
                List<AllType> listNewData = null;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    if (result.getAffectedRows() != 0)
                    {
                        listNewData = JSON.parseArray(result.getRows().toJSONString(), AllType.class);

                        listAlltype.addAll(listNewData);

                    } else
                    {
                        listNewData = new ArrayList<AllType>();
                    }
                } else
                {
                    AppContext.makeToast(NCZ_CreateNewOrder.this, "error_connectDataBase");

                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                String a = error.getMessage();
                AppContext.makeToast(NCZ_CreateNewOrder.this, "error_connectServer");

            }
        });

    }

    private void DeletesellOrderSettlement()
    {
        commembertab commembertab = AppContext.getUserInfo(NCZ_CreateNewOrder.this);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("strWhere", "infoId='" + uuid + "'");
        params.addQueryStringParameter("action", "DeletesellOrderSettlement");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String a = responseInfo.result;
                List<PeopelList> listNewData = null;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    if (result.getAffectedRows() != 0)
                    {
                        listNewData = JSON.parseArray(result.getRows().toJSONString(), PeopelList.class);

                    } else
                    {
                        listNewData = new ArrayList<PeopelList>();
                    }
                } else
                {
                    AppContext.makeToast(NCZ_CreateNewOrder.this, "error_connectDataBase");

                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                String a = error.getMessage();
                AppContext.makeToast(NCZ_CreateNewOrder.this, "error_connectServer");

            }
        });
    }

    public void getDetailSecBysettleId()
    {
        commembertab commembertab = AppContext.getUserInfo(NCZ_CreateNewOrder.this);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uuid", uuid);
        params.addQueryStringParameter("action", "getDetailSecBysettleId");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {

                String a = responseInfo.result;
                List<SellOrder_New> listNewData = null;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
//                    if (result.getAffectedRows() != 0)
//                    {

                    listNewData = JSON.parseArray(result.getRows().toJSONString(), SellOrder_New.class);

                    if (listNewData.size() > 0)
                    {
                        cheliang_num.setText(listNewData.size() + "");
                    }
                    {
                        listNewData = new ArrayList<SellOrder_New>();
                    }

                } else
                {
                    AppContext.makeToast(NCZ_CreateNewOrder.this, "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(NCZ_CreateNewOrder.this, "error_connectServer");
            }
        });
    }
}
