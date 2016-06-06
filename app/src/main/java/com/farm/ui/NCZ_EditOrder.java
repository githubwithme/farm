package com.farm.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.Result;
import com.farm.bean.SellOrderDetail;
import com.farm.bean.SellOrderDetail_New;
import com.farm.bean.SellOrder_New;
import com.farm.common.utils;
import com.farm.widget.CustomDialog_EditOrderDetail;
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
import java.util.HashMap;
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
    String broadcast;
    List<SellOrderDetail_New> list_orderDetail;
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
/*    @ViewById
    EditText et_number;*/
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
     /*   if (et_number.getText().toString().equals(""))
        {
            Toast.makeText(NCZ_EditOrder.this, "请先填写信息", Toast.LENGTH_SHORT).show();
            return;
        }*/
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
        adapter_editSellOrderDetail_ncz = new Adapter_EditSellOrderDetail_NCZ(NCZ_EditOrder.this);
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
        broadcast = getIntent().getStringExtra("broadcast");
        list_orderDetail = sellOrder.getSellOrderDetailList();
        if (list_orderDetail == null)
        {
            list_orderDetail = new ArrayList<>();
        }
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
            /*        String rows = result.getRows().get(0).toString();
                    if (rows.equals("1"))
                    {
                        Toast.makeText(NCZ_EditOrder.this, "修改成功！", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        intent.setAction(broadcast);
                        sendBroadcast(intent);
                    } else if (rows.equals("0"))
                    {
                        Toast.makeText(NCZ_EditOrder.this, "修改失败！", Toast.LENGTH_SHORT).show();
                    }
*/
                    finish();
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

    public class Adapter_EditSellOrderDetail_NCZ extends BaseAdapter
    {
        int pos_delete = 0;
        int pos_edit = 0;
        MyDialog myDialog;
        private Activity context;// 运行上下文
        private LayoutInflater listContainer;// 视图容器
        SellOrderDetail_New SellOrderDetail;
        CustomDialog_EditOrderDetail customDialog_editOrderDetaill;
        EditText et_number;

        class ListItemView
        {
            public TextView tv_area;
            public TextView tv_plannumber;
            public Button btn_editorderdetail;
            public Button btn_deleteorderdetail;

            public TextView tv_yq;
            public TextView tv_pq;
        }

        public Adapter_EditSellOrderDetail_NCZ(Activity context)
        {
            this.context = context;
            this.listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
        }

        public int getCount()
        {
            return list_orderDetail.size();
        }

        public Object getItem(int arg0)
        {
            return null;
        }

        public long getItemId(int arg0)
        {
            return 0;
        }

        HashMap<Integer, View> lmap = new HashMap<Integer, View>();

        public View getView(int position, View convertView, ViewGroup parent)
        {
            SellOrderDetail = list_orderDetail.get(position);
            // 自定义视图
            ListItemView listItemView = null;
            if (lmap.get(position) == null)
            {
                // 获取list_item布局文件的视图
                convertView = listContainer.inflate(R.layout.adapter_editsellorderdetail_ncz, null);
                listItemView = new ListItemView();
                // 获取控件对象
                listItemView.tv_area = (TextView) convertView.findViewById(R.id.tv_area);
                listItemView.tv_plannumber = (TextView) convertView.findViewById(R.id.tv_plannumber);
                listItemView.btn_editorderdetail = (Button) convertView.findViewById(R.id.btn_editorderdetail);
                listItemView.btn_deleteorderdetail = (Button) convertView.findViewById(R.id.btn_deleteorderdetail);
                listItemView.btn_editorderdetail.setTag(R.id.tag_tv_number,listItemView.tv_plannumber);
                listItemView.btn_editorderdetail.setTag(R.id.tag_postion, position);
                listItemView.btn_editorderdetail.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        TextView TextView = (TextView) v.getTag(R.id.tag_tv_number);
                        pos_edit = (int) v.getTag(R.id.tag_postion);
                        showDialog_editNumber(list_orderDetail.get(pos_edit), TextView);
                    }
                });
                listItemView.btn_deleteorderdetail.setTag(position);
                listItemView.btn_deleteorderdetail.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        pos_delete = (int) v.getTag();
                        showDeleteTip(list_orderDetail.get(pos_delete));
                    }
                });
                // 设置控件集到convertView
                lmap.put(position, convertView);
                convertView.setTag(listItemView);
            } else
            {
                convertView = lmap.get(position);
                listItemView = (ListItemView) convertView.getTag();
            }
            // 设置文字和图片
            listItemView.tv_plannumber.setText(SellOrderDetail.getplannumber());
            listItemView.tv_area.setText(SellOrderDetail.getparkname() + SellOrderDetail.getareaname() + SellOrderDetail.getcontractname());
            return convertView;
        }


        private void editNumber(final TextView tv_plannumber, final SellOrderDetail_New sellOrderDetail, final String number_new, String number_difference)
        {
            RequestParams params = new RequestParams();
            params.addQueryStringParameter("uid", sellOrderDetail.getuid());
            params.addQueryStringParameter("contractid", sellOrderDetail.getcontractid());
            params.addQueryStringParameter("year", sellOrderDetail.getYear());
            params.addQueryStringParameter("uuid", sellOrderDetail.getUuid());
            params.addQueryStringParameter("batchTime", sellOrderDetail.getBatchTime());
            params.addQueryStringParameter("number_difference", number_difference);
            params.addQueryStringParameter("number_new", number_new);
            params.addQueryStringParameter("action", "editOrderDetail");
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
                        if (result.getAffectedRows()==1)
                        {
                            Toast.makeText(context, "修改成功！", Toast.LENGTH_SHORT).show();
                            tv_plannumber.setText(number_new);
                            list_orderDetail.get(pos_edit).setplannumber(number_new);
                            sellOrder.setSellOrderDetailList(list_orderDetail);
                            adapter_editSellOrderDetail_ncz.notifyDataSetChanged();
                            Intent intent1 = new Intent();
                            intent1.setAction(broadcast);
                            sendBroadcast(intent1);

                            Intent intent2 = new Intent();
                            intent2.setAction(AppContext.BROADCAST_UPDATESELLORDER);
                            sendBroadcast(intent2);
                        } else
                        {
                            Toast.makeText(context, "改承包区该批次剩余株数不足！", Toast.LENGTH_SHORT).show();
                        }

                    } else
                    {
                        Toast.makeText(context, "修改失败！", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(HttpException error, String msg)
                {
                    AppContext.makeToast(context, "error_connectServer");
                }
            });
        }


        private void showDeleteTip(final SellOrderDetail_New sellOrderDetail_new)
        {
            View dialog_layout = (LinearLayout) context.getLayoutInflater().inflate(R.layout.customdialog_callback, null);
            myDialog = new MyDialog(context, R.style.MyDialog, dialog_layout, "订单", "确定删除吗?", "删除", "取消", new MyDialog.CustomDialogListener()
            {
                @Override
                public void OnClick(View v)
                {
                    switch (v.getId())
                    {
                        case R.id.btn_sure:
                            deleteOrderDetail(sellOrderDetail_new.getplannumber(), sellOrderDetail_new.getUuid(), sellOrderDetail_new.getuid(), sellOrderDetail_new.getcontractid(), sellOrderDetail_new.getYear(), sellOrderDetail_new.getBatchTime());
                            break;
                        case R.id.btn_cancle:
                            myDialog.cancel();
                            break;
                    }
                }
            });
            myDialog.show();
        }

        private void deleteOrderDetail(String number_difference, String uuid, String uid, String contractid, String year, String batchTime)
        {
            RequestParams params = new RequestParams();
            params.addQueryStringParameter("number_difference", number_difference);
            params.addQueryStringParameter("uuid", uuid);
            params.addQueryStringParameter("uid", uid);
            params.addQueryStringParameter("contractid", contractid);
            params.addQueryStringParameter("year", year);
            params.addQueryStringParameter("batchTime", batchTime);
            params.addQueryStringParameter("action", "deleteOrderDetail");
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
                            Toast.makeText(context, "删除成功！", Toast.LENGTH_SHORT).show();
                            list_orderDetail.remove(pos_delete);
                            sellOrder.setSellOrderDetailList(list_orderDetail);
                            adapter_editSellOrderDetail_ncz.notifyDataSetChanged();
                            Intent intent = new Intent();
                            intent.setAction(broadcast);
                            sendBroadcast(intent);

                            Intent intent2 = new Intent();
                            intent2.setAction(AppContext.BROADCAST_UPDATESELLORDER);
                            sendBroadcast(intent2);
                            myDialog.cancel();
                        } else
                        {
                            Toast.makeText(context, "删除失败！", Toast.LENGTH_SHORT).show();
                        }
                    } else
                    {
                        AppContext.makeToast(context, "error_connectDataBase");
                        return;
                    }

                }

                @Override
                public void onFailure(HttpException error, String msg)
                {
                    AppContext.makeToast(context, "error_connectServer");
                }
            });
        }

        private void showDialog_editNumber(final SellOrderDetail_New sellOrderDetail_new, final TextView textView)
        {
            final View dialog_layout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.customdialog_editorderdetail, null);
            customDialog_editOrderDetaill = new CustomDialog_EditOrderDetail(context, R.style.MyDialog, dialog_layout);
            et_number = (EditText) dialog_layout.findViewById(R.id.et_number);
            Button btn_sure = (Button) dialog_layout.findViewById(R.id.btn_sure);
            Button btn_cancle = (Button) dialog_layout.findViewById(R.id.btn_cancle);
            btn_sure.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    customDialog_editOrderDetaill.dismiss();
                    int number_difference = Integer.valueOf(sellOrderDetail_new.getplannumber()) - Integer.valueOf(et_number.getText().toString());
                    editNumber(textView, sellOrderDetail_new, et_number.getText().toString(), String.valueOf(number_difference));
                }
            });
            btn_cancle.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    customDialog_editOrderDetaill.dismiss();
                }
            });
            customDialog_editOrderDetaill.show();
        }
    }
}
