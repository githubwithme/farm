package com.farm.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.adapter.Adapter_SellOrderDetail;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.Result;
import com.farm.bean.SellOrderDetail_New;
import com.farm.bean.SellOrder_New;
import com.farm.bean.SellOrder_New_First;
import com.farm.common.utils;
import com.farm.widget.CustomDialog_CallTip;
import com.farm.widget.MyDateMaD;
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
import java.util.List;

/**
 * Created by hasee on 2016/7/1.
 */
@EActivity(R.layout.ncz_dd_sh_detail)
public class NCZ_DD_SH_Detail extends Activity
{

    CustomDialog_CallTip custom_calltip;
    MyDialog myDialog;
    SellOrder_New sellOrder_new;
    @ViewById
    ListView lv;
    @ViewById
    TextView tv_allnumber;

    @ViewById
    TextView chanpin;
    @ViewById
    TextView et_name;
    @ViewById
    TextView dd_time;//采收时间
    @ViewById
    EditText old_time;//旧的时间
    @ViewById
    TextView et_price;
    @ViewById
    EditText old_price;
    @ViewById
    TextView by_danjia;
    @ViewById
    TextView old_danjia;
    @ViewById
    TextView bz_danjia;
    @ViewById
    TextView old_bzdanjia;
    @ViewById
    TextView bz_guige;
    @ViewById
    TextView et_weight;
    @ViewById
    TextView et_values;
    @ViewById
    TextView dingjin;
    @ViewById
    TextView et_address;

    @ViewById
    TextView dd_fzr;
    @ViewById
    TextView dd_cl;
    @ViewById
    TextView dd_bz;
    @ViewById
    TextView dd_by;
    @ViewById
    TextView et_note;
    @ViewById
    TextView old_weight;
    @ViewById
    TextView no_ncz;

    @ViewById
    RelativeLayout no_dingjin;
    @ViewById
    RelativeLayout no_weikuan;
    List<SellOrderDetail_New> list_orderdetail;
    Adapter_SellOrderDetail adapter_sellOrderDetail;
    @ViewById
    ScrollView dd_edit;


    @ViewById
    ImageView ig_view1;
    @ViewById
    ImageView ig_view2;
    @ViewById
    ImageView ig_view3;
    @ViewById
    ImageView ig_view4;
    @ViewById
    ImageView ig_view5;

    @Click
    void btn_back()
    {
        finish();
    }


    @Click
    void old_time()
    {

        MyDateMaD myDatepicker = new MyDateMaD(NCZ_DD_SH_Detail.this,old_time);
        myDatepicker.getDialog().show();
    }
    @AfterViews
    void afview()
    {
        list_orderdetail = sellOrder_new.getSellOrderDetailList();
        adapter_sellOrderDetail = new Adapter_SellOrderDetail(NCZ_DD_SH_Detail.this, list_orderdetail);
        lv.setAdapter(adapter_sellOrderDetail);
        utils.setListViewHeight(lv);
        int allnumber = 0;
        for (int i = 0; i < list_orderdetail.size(); i++)
        {
            allnumber = allnumber + Integer.valueOf(list_orderdetail.get(i).getplannumber());
        }
        tv_allnumber.setText(allnumber+"");

        if (sellOrder_new.getPrice().equals("") && !sellOrder_new.getCreatorid().equals(""))
        {
            no_ncz.setText("新订单");
        } else if (!sellOrder_new.getCreatorid().equals(""))
        {
            no_ncz.setText("自主生产订单有改变");
        } else
        {
            no_ncz.setText("订单有改变");
        }

        if (sellOrder_new.getFreeDeposit().equals("0"))
        {
            no_dingjin.setVisibility(View.VISIBLE);
        }
        if (sellOrder_new.getFreeFinalPay().equals("0"))
        {
            no_weikuan.setVisibility(View.VISIBLE);
        }

        if (sellOrder_new.getIsNeedAudit().equals("0"))
        {
            dd_edit.setVisibility(View.VISIBLE);
        }
        if (sellOrder_new.getFreeDeposit().equals("2") && sellOrder_new.getFreeFinalPay().equals("2") && sellOrder_new.getIsNeedAudit().equals("2"))
        {
            dd_edit.setVisibility(View.VISIBLE);
        }
        getData();
        old_price.addTextChangedListener(allAcutalvalue);
        old_weight.addTextChangedListener(allAcutalvalue);
    }
    private TextWatcher allAcutalvalue=new TextWatcher()
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
            if (old_price.getText().toString().equals(""))
            {
//                    Toast.makeText(NCZ_CreateMoreOrder.this, "请先填写单价", Toast.LENGTH_SHORT).show();
                return;
            }
            if (old_weight.getText().toString().equals(""))
            {
//                    Toast.makeText(NCZ_CreateMoreOrder.this, "请先填写重量", Toast.LENGTH_SHORT).show();
                return;
            }
            double ss = Double.valueOf(old_price.getText().toString()) * Double.valueOf(old_weight.getText().toString());
            et_values.setText(String.format("%.2f", ss));

        }
    };
    //批准免付尾款
    @Click
    void wk_pz()
    {
        showWk();

    }

    //驳回免付尾款
    @Click
    void wk_bh()
    {
        no_showWk();
    }

    //批准免付定金
    @Click
    void dj_pz()
    {
        showList();
    }

    //驳回免付定金
    @Click
    void dj_bh()
    {
        no_showList();
    }


    //订单申请的修改
    @Click
    void dd_pz()
    {
        showDeleteTip();
    }

    //订单申请的驳回
    @Click
    void dd_bh()
    {
        no_showDeleteTip();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        sellOrder_new = getIntent().getParcelableExtra("SellOrder_New");
        String a = sellOrder_new.getBuyers();
    }




    private void getData()
    {

//        dd_cl.setText(sellOrder_new.getPlateNumber());
        chanpin.setText(sellOrder_new.getGoodsname());

        et_name.setText(sellOrder_new.getPurchaName());
        et_note.setText(sellOrder_new.getNote());
//        dd_fzr.setText(sellOrder_new.getMainPepName());
//        dd_by.setText(sellOrder_new.getContractorName());
//        dd_bz.setText(sellOrder_new.getPickName());
        et_address.setText(sellOrder_new.getAddress());
        dingjin.setText(sellOrder_new.getWaitDeposit());//订金
        et_values.setText(sellOrder_new.getSumvalues());//总价

//        bz_guige.setText(sellOrder_new.getPackPec());//包装规格

        if (!sellOrder_new.getOldsaletime().equals(""))
        {

            old_time.setText(sellOrder_new.getOldsaletime().substring(5, sellOrder_new.getOldsaletime().length() - 8));
            if (!sellOrder_new.saletime.equals(""))
            {
                ig_view1.setVisibility(View.VISIBLE);
                dd_time.setText(sellOrder_new.getSaletime().substring(5, sellOrder_new.getSaletime().length() - 8));
            }

        } else
        {

//            dd_time.setText(sellOrder_new.getSaletime().substring(5, sellOrder_new.getSaletime().length() - 8));
            old_time.setText(sellOrder_new.getSaletime().substring(5, sellOrder_new.getSaletime().length() - 8));
            old_time.setTextColor(getResources().getColor(R.color.bg_text));
        }


        if (!sellOrder_new.getOldPrice().equals(""))
        {

            old_price.setText(sellOrder_new.getOldPrice());
            if (!sellOrder_new.getPrice().equals(""))
            {
                ig_view2.setVisibility(View.VISIBLE);
                et_price.setText(sellOrder_new.getPrice());
            }

        } else
        {
//            et_price.setText(sellOrder_new.getPrice());
            old_price.setText(sellOrder_new.getPrice());
            old_price.setTextColor(getResources().getColor(R.color.bg_text));
        }



 /*       if (!sellOrder_new.getOldCarryPrice().equals(""))
        {

            old_danjia.setText(sellOrder_new.getOldCarryPrice());
            if (!sellOrder_new.getCarryPrice().equals(""))
            {
                ig_view3.setVisibility(View.VISIBLE);
                by_danjia.setText(sellOrder_new.getCarryPrice());
            }

        } else
        {
            old_danjia.setText(sellOrder_new.getCarryPrice());
            old_danjia.setTextColor(getResources().getColor(R.color.bg_text));
        }*/


/*        if (!sellOrder_new.getOldPackPrice().equals(""))
        {

            old_bzdanjia.setText(sellOrder_new.getOldPackPrice());
            if (!sellOrder_new.getPackPrice().equals(""))
            {
                ig_view5.setVisibility(View.VISIBLE);
                bz_danjia.setText(sellOrder_new.getPackPrice());
            }
        } else
        {
            old_bzdanjia.setText(sellOrder_new.getPackPrice());
            old_bzdanjia.setTextColor(getResources().getColor(R.color.bg_text));
        }*/

        if (!sellOrder_new.getOldnumber().equals(""))
        {

            old_weight.setText(sellOrder_new.getOldnumber());//总重量


            if (!sellOrder_new.getWeight().equals(""))
            {
                et_weight.setText(sellOrder_new.getWeight());//总重量
                ig_view4.setVisibility(View.VISIBLE);
            }
        } else
        {
//            et_weight.setText(sellOrder_new.getWeight());//总重量

            old_weight.setText(sellOrder_new.getWeight());//总重量
            old_weight.setTextColor(getResources().getColor(R.color.bg_text));
        }


    }

    private void showDeleteTip()
    {

        View dialog_layout = LayoutInflater.from(NCZ_DD_SH_Detail.this).inflate(R.layout.customdialog_callback, null);
        myDialog = new MyDialog(NCZ_DD_SH_Detail.this, R.style.MyDialog, dialog_layout, "订单", "是否通过申请?", "批准", "取消", new MyDialog.CustomDialogListener()
        {
            @Override
            public void OnClick(View v)
            {
                switch (v.getId())
                {
                    case R.id.btn_sure:
                        upAlldata();
                        myDialog.cancel();
                        break;
                    case R.id.btn_cancle:
                        myDialog.cancel();
                        break;
                }
            }
        });
        myDialog.show();
    }

    public void upAlldata()
    {
        SellOrder_New sellOrder = new SellOrder_New();

        sellOrder = sellOrder_new;

        if (!sellOrder.getOldnumber().equals(""))
        {
            sellOrder.setWeight(sellOrder.getOldnumber());
            sellOrder.setOldnumber("");
        }

   /*     if (!sellOrder.getOldPrice().equals(""))
        {
            sellOrder.setPrice(sellOrder.getOldPrice());
            sellOrder.setOldPrice("");
        }*/
/*        if (!sellOrder.getOldsaletime().equals(""))
        {
            sellOrder.setSaletime(sellOrder.getOldsaletime());
            sellOrder.setOldsaletime("");
        }*/
        sellOrder.setSaletime(old_time.getText().toString());
        sellOrder.setPrice(old_price.getText().toString());
        sellOrder.setSumvalues(et_values.getText().toString());
        if (sellOrder.getSelltype().equals("待审批"))
        {
            sellOrder.setSelltype("待付定金");
        }

  /*      sellOrder.setGoodsname(sellOrder_new.getProduct());
        sellOrder.setProducer(sellOrder_new.getParkname());
        sellOrder.setPlateNumber(sellOrder_new.getCarNumber());*/

        sellOrder.setIsNeedAudit("1");
        SellOrder_New_First sellOrder_new_first = new SellOrder_New_First();
        StringBuilder builder = new StringBuilder();
        builder.append("{\"SellOrder_new\":[ ");
        builder.append(JSON.toJSONString(sellOrder));
        builder.append("], \"sellorderlistadd\": [");
        builder.append(JSON.toJSONString(sellOrder_new_first));
        builder.append("]} ");
        newaddOrder(builder.toString());
    }

    private void newaddOrder(String data)
    {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("action", "editOrder");
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
                        Toast.makeText(NCZ_DD_SH_Detail.this, "订单修改成功！", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
//                        intent.setAction(AppContext.BROADCAST_DD_REFASH);
                        intent.setAction(AppContext.BROADCAST_UPDATEAllORDER);
                        NCZ_DD_SH_Detail.this.sendBroadcast(intent);
                        finish();


                    }

                } else
                {
                    AppContext.makeToast(NCZ_DD_SH_Detail.this, "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(NCZ_DD_SH_Detail.this, "error_connectServer");
            }
        });
    }

    private void showWk()
    {

        View dialog_layout = LayoutInflater.from(NCZ_DD_SH_Detail.this).inflate(R.layout.customdialog_callback, null);
        myDialog = new MyDialog(NCZ_DD_SH_Detail.this, R.style.MyDialog, dialog_layout, "订单", "订单申请免付尾款", "批准", "取消", new MyDialog.CustomDialogListener()
        {
            @Override
            public void OnClick(View v)
            {
                switch (v.getId())
                {
                    case R.id.btn_sure:
                        btnWk();
                        myDialog.cancel();
                        break;
                    case R.id.btn_cancle:
                        myDialog.cancel();
                        break;
                }
            }
        });
        myDialog.show();
    }

    private void btnWk()
    {
        SellOrder_New sellOrders = new SellOrder_New();
        sellOrders = sellOrder_new;
        if (sellOrders.getFreeFinalPay().equals("0"))
        {
            sellOrders.setFreeFinalPay("1");
        }
   /*     sellOrders.setGoodsname(sellOrder_new.getProduct());
        sellOrders.setProducer(sellOrder_new.getParkname());
        sellOrders.setPlateNumber(sellOrder_new.getCarNumber());*/

        SellOrder_New_First sellOrder_new_first = new SellOrder_New_First();
        StringBuilder builder = new StringBuilder();
        builder.append("{\"SellOrder_new\":[ ");
        builder.append(JSON.toJSONString(sellOrders));
        builder.append("], \"sellorderlistadd\": [");
        builder.append(JSON.toJSONString(sellOrder_new_first));
        builder.append("]} ");
        newaddOrder(builder.toString());
    }

    private void showList()
    {

        View dialog_layout = LayoutInflater.from(NCZ_DD_SH_Detail.this).inflate(R.layout.customdialog_callback, null);
        myDialog = new MyDialog(NCZ_DD_SH_Detail.this, R.style.MyDialog, dialog_layout, "订单", "订单申请免付定金?", "批准", "取消", new MyDialog.CustomDialogListener()
        {
            @Override
            public void OnClick(View v)
            {
                switch (v.getId())
                {
                    case R.id.btn_sure:
                        btnSave();
                        myDialog.cancel();
                        break;
                    case R.id.btn_cancle:
                        myDialog.cancel();
                        break;
                }
            }
        });
        myDialog.show();
    }

    private void btnSave()
    {
        SellOrder_New sellOrders = new SellOrder_New();
        sellOrders = sellOrder_new;
        if (sellOrders.getFreeDeposit().equals("0"))
        {
            sellOrders.setFreeDeposit("1");
        }

       /* sellOrders.setGoodsname(sellOrder_new.getProduct());
        sellOrders.setProducer(sellOrder_new.getParkname());
        sellOrders.setPlateNumber(sellOrder_new.getCarNumber());*/

        SellOrder_New_First sellOrder_new_first = new SellOrder_New_First();
        StringBuilder builder = new StringBuilder();
        builder.append("{\"SellOrder_new\":[ ");
        builder.append(JSON.toJSONString(sellOrders));
        builder.append("], \"sellorderlistadd\": [");
        builder.append(JSON.toJSONString(sellOrder_new_first));
        builder.append("]} ");
        newaddOrder(builder.toString());
    }

    private void no_showDeleteTip()
    {

        View dialog_layout = LayoutInflater.from(NCZ_DD_SH_Detail.this).inflate(R.layout.customdialog_callback, null);
        myDialog = new MyDialog(NCZ_DD_SH_Detail.this, R.style.MyDialog, dialog_layout, "订单", "是否通过申请?", "驳回", "取消", new MyDialog.CustomDialogListener()
        {
            @Override
            public void OnClick(View v)
            {
                switch (v.getId())
                {
                    case R.id.btn_sure:
                        no_upAlldata();
                        myDialog.cancel();
                        break;
                    case R.id.btn_cancle:
                        myDialog.cancel();
                        break;
                }
            }
        });
        myDialog.show();
    }

    public void no_upAlldata()
    {
        SellOrder_New sellOrder = new SellOrder_New();

        sellOrder = sellOrder_new;

/*        if (!sellOrder.getOldCarryPrice().equals(""))
        {
            sellOrder.setOldCarryPrice("");
        }*/
        if (!sellOrder.getOldnumber().equals(""))
        {
            sellOrder.setOldnumber("");

        }
/*        if (!sellOrder.getOldPackPrice().equals(""))
        {
            sellOrder.setOldPackPrice("");

        }*/
        if (!sellOrder.getOldPrice().equals(""))
        {
            sellOrder.setOldPrice("");
        }
        if (!sellOrder.getOldsaletime().equals(""))
        {
            sellOrder.setOldsaletime("");
        }
     /*   sellOrder.setGoodsname(sellOrder_new.getProduct());
        sellOrder.setProducer(sellOrder_new.getParkname());
        sellOrder.setPlateNumber(sellOrder_new.getCarNumber());*/
        sellOrder.setIsNeedAudit("-1");
        if (!sellOrder.getSelltype().equals("待审批"))
        {
            SellOrder_New_First sellOrder_new_first = new SellOrder_New_First();
            StringBuilder builder = new StringBuilder();
            builder.append("{\"SellOrder_new\":[ ");
            builder.append(JSON.toJSONString(sellOrder));
            builder.append("], \"sellorderlistadd\": [");
            builder.append(JSON.toJSONString(sellOrder_new_first));
            builder.append("]} ");
            newaddOrder(builder.toString());
        } else
        {
            deleteSellOrderAndDetail(sellOrder.getUuid());
        }

    }

    private void no_showWk()
    {

        View dialog_layout = LayoutInflater.from(NCZ_DD_SH_Detail.this).inflate(R.layout.customdialog_callback, null);
        myDialog = new MyDialog(NCZ_DD_SH_Detail.this, R.style.MyDialog, dialog_layout, "订单", "是否通过申请?", "批准", "取消", new MyDialog.CustomDialogListener()
        {
            @Override
            public void OnClick(View v)
            {
                switch (v.getId())
                {
                    case R.id.btn_sure:
                        no_btnWk();
                        myDialog.cancel();
                        break;
                    case R.id.btn_cancle:
                        myDialog.cancel();
                        break;
                }
            }
        });
        myDialog.show();
    }

    private void no_btnWk()
    {
        SellOrder_New sellOrders = new SellOrder_New();
        sellOrders = sellOrder_new;
        if (sellOrders.getFreeFinalPay().equals("0"))
        {
            sellOrders.setFreeFinalPay("-1");
        }
   /*     sellOrders.setGoodsname(sellOrder_new.getProduct());
        sellOrders.setProducer(sellOrder_new.getParkname());
        sellOrders.setPlateNumber(sellOrder_new.getCarNumber());*/

        SellOrder_New_First sellOrder_new_first = new SellOrder_New_First();
        StringBuilder builder = new StringBuilder();
        builder.append("{\"SellOrder_new\":[ ");
        builder.append(JSON.toJSONString(sellOrders));
        builder.append("], \"sellorderlistadd\": [");
        builder.append(JSON.toJSONString(sellOrder_new_first));
        builder.append("]} ");
        newaddOrder(builder.toString());
    }

    private void no_showList()
    {

        View dialog_layout = LayoutInflater.from(NCZ_DD_SH_Detail.this).inflate(R.layout.customdialog_callback, null);
        myDialog = new MyDialog(NCZ_DD_SH_Detail.this, R.style.MyDialog, dialog_layout, "订单", "是否通过申请?", "批准", "取消", new MyDialog.CustomDialogListener()
        {
            @Override
            public void OnClick(View v)
            {
                switch (v.getId())
                {
                    case R.id.btn_sure:
                        no_btnSave();
                        myDialog.cancel();
                        break;
                    case R.id.btn_cancle:
                        myDialog.cancel();
                        break;
                }
            }
        });
        myDialog.show();
    }

    private void no_btnSave()
    {
        SellOrder_New sellOrders = new SellOrder_New();
        sellOrders = sellOrder_new;
        if (sellOrders.getFreeDeposit().equals("0"))
        {
            sellOrders.setFreeDeposit("-1");
        }
     /*   sellOrders.setGoodsname(sellOrder_new.getProduct());
        sellOrders.setProducer(sellOrder_new.getParkname());
        sellOrders.setPlateNumber(sellOrder_new.getCarNumber());*/

        SellOrder_New_First sellOrder_new_first = new SellOrder_New_First();
        StringBuilder builder = new StringBuilder();
        builder.append("{\"SellOrder_new\":[ ");
        builder.append(JSON.toJSONString(sellOrders));
        builder.append("], \"sellorderlistadd\": [");
        builder.append(JSON.toJSONString(sellOrder_new_first));
        builder.append("]} ");
        newaddOrder(builder.toString());
    }

    private void deleteSellOrderAndDetail(String uuid)
    {

        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uuid", uuid);
        params.addQueryStringParameter("action", "deleteSellOrderAndDetail");//jobGetList1
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

                    Intent intent = new Intent();
                    intent.setAction(AppContext.BROADCAST_UPDATEAllORDER);
                    sendBroadcast(intent);
                } else
                {
                    AppContext.makeToast(NCZ_DD_SH_Detail.this, "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(NCZ_DD_SH_Detail.this, "error_connectServer");

            }
        });
    }


}
