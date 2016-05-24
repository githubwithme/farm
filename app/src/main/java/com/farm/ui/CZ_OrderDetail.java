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
import java.util.HashMap;
import java.util.List;

/**
 * Created by ${hmj} on 2016/1/20.
 */

/**
 * 1
 */
@EActivity(R.layout.cz_orderdetail)
public class CZ_OrderDetail extends Activity
{
    String broadcast;
    TextView textview_number;
    TextView textview_weight;
    String batchtime;
    SellOrder_New sellOrder;
    Adapter_SellOrderDetail adapter_sellOrderDetail;
    SellOrderDetail SellOrderDetail;
    List<SellOrderDetail_New> list_orderdetail;
    @ViewById
    LinearLayout ll_flyl;
    @ViewById
    ListView lv;
    @ViewById
    TextView tv_plansumvalues;
    @ViewById
    TextView tv_name;
    @ViewById
    TextView tv_address;
    @ViewById
    TextView tv_planprice;
    @ViewById
    TextView tv_price;
    @ViewById
    TextView tv_planweight;
    @ViewById
    TextView tv_deposit;
    @ViewById
    TextView tv_number;
    @ViewById
    TextView tv_finalpayment;
    @ViewById
    TextView tv_actualweight;
    @ViewById
    TextView tv_actualsumvalues;
    @ViewById
    TextView tv_actualnumber;
    @ViewById
    TextView tv_phone;
    @ViewById
    TextView tv_email;
    @ViewById
    TextView tv_note;


//    @Click
//    void btn_edit()
//    {
//        Intent intent = new Intent(CZ_OrderDetail.this, CZ_EditOrder_.class);
//        intent.putParcelableArrayListExtra("list", (ArrayList<? extends Parcelable>) sellOrder.getSellOrderDetailList());
//        startActivity(intent);
//    }

    @AfterViews
    void afterOncreate()
    {
        countAllWeight(list_orderdetail);
        countAllNumber(list_orderdetail);
        adapter_sellOrderDetail = new Adapter_SellOrderDetail(CZ_OrderDetail.this);
        lv.setAdapter(adapter_sellOrderDetail);
        utils.setListViewHeight(lv);
        showData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        sellOrder = getIntent().getParcelableExtra("bean");
        broadcast = getIntent().getStringExtra("broadcast");
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
        tv_name.setText(sellOrder.getBuyers());
        tv_planprice.setText(sellOrder.getPrice());
//        tv_planweight.setText(sellOrder.getWeight());
//        tv_actualweight.setText(sellOrder.getBuyers());
//        tv_plansumvalues.setText(sellOrder.getBuyers());
//        tv_actualsumvalues.setText(sellOrder.getBuyers());
        tv_deposit.setText(sellOrder.getDeposit());
        tv_finalpayment.setText(sellOrder.getFinalpayment());
        tv_phone.setText(sellOrder.getPhone());
        tv_address.setText(sellOrder.getAddress());
        tv_email.setText(sellOrder.getEmail());
        tv_note.setText(sellOrder.getNote());
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
                        Toast.makeText(CZ_OrderDetail.this, "订单创建成功！", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                } else
                {
                    AppContext.makeToast(CZ_OrderDetail.this, "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(CZ_OrderDetail.this, "error_connectServer");
            }
        });
    }

    public class Adapter_SellOrderDetail extends BaseAdapter
    {
        private Activity context;// 运行上下文
        private LayoutInflater listContainer;// 视图容器
        SellOrderDetail_New SellOrderDetail;
        CustomDialog_EditOrderDetail customDialog_editOrderDetaill;
        EditText et_actualnumber;
        EditText et_actualweight;

        class ListItemView
        {
            public TextView tv_area;
            public TextView tv_actualweight;
            public TextView tv_actualnumber;
            public TextView tv_plannumber;
            public Button btn_feedback;

            public TextView tv_yq;
            public TextView tv_pq;
        }

        public Adapter_SellOrderDetail(Activity context)
        {
            this.context = context;
            this.listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
        }

        public int getCount()
        {
            return list_orderdetail.size();
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
            SellOrderDetail = list_orderdetail.get(position);
            // 自定义视图
            ListItemView listItemView = null;
            if (lmap.get(position) == null)
            {
                // 获取list_item布局文件的视图
                convertView = listContainer.inflate(R.layout.adapter_czorderdetail, null);
                listItemView = new ListItemView();
                // 获取控件对象
                listItemView.tv_area = (TextView) convertView.findViewById(R.id.tv_area);
                listItemView.tv_plannumber = (TextView) convertView.findViewById(R.id.tv_plannumber);
                listItemView.tv_actualnumber = (TextView) convertView.findViewById(R.id.tv_actualnumber);
                listItemView.tv_actualweight = (TextView) convertView.findViewById(R.id.tv_actualweight);
                listItemView.btn_feedback = (Button) convertView.findViewById(R.id.btn_feedback);
                listItemView.btn_feedback.setTag(R.id.tag_tv_number, listItemView.tv_actualnumber);
                listItemView.btn_feedback.setTag(R.id.tag_tv_weight, listItemView.tv_actualweight);
                listItemView.btn_feedback.setTag(R.id.tag_postion, position);
                listItemView.btn_feedback.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        int pos = (int) v.getTag(R.id.tag_postion);
                        textview_number = (TextView) v.getTag(R.id.tag_tv_number);
                        textview_weight = (TextView) v.getTag(R.id.tag_tv_weight);
                        showDialog_editNumber(pos, list_orderdetail.get(pos));
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
            listItemView.tv_plannumber.setText("拟售" + SellOrderDetail.getplannumber() + "株");
            listItemView.tv_actualnumber.setText("实售" + SellOrderDetail.getactualnumber() + "株");
            listItemView.tv_actualweight.setText("实重" + SellOrderDetail.getactualweight() + "斤");
            listItemView.tv_area.setText(SellOrderDetail.getparkname() + SellOrderDetail.getareaname() + SellOrderDetail.getcontractname());
            return convertView;
        }

        private void showDialog_editNumber(final int pos, final SellOrderDetail_New sellOrderDetail_new)
        {
            final View dialog_layout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.customdialog_czeditorderdetail, null);
            customDialog_editOrderDetaill = new CustomDialog_EditOrderDetail(context, R.style.MyDialog, dialog_layout);
            et_actualnumber = (EditText) dialog_layout.findViewById(R.id.et_actualnumber);
            et_actualweight = (EditText) dialog_layout.findViewById(R.id.et_actualweight);
            Button btn_sure = (Button) dialog_layout.findViewById(R.id.btn_sure);
            Button btn_cancle = (Button) dialog_layout.findViewById(R.id.btn_cancle);
            btn_sure.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    customDialog_editOrderDetaill.dismiss();
                    textview_number.setText("实售" + et_actualnumber.getText().toString() + "株");
                    textview_weight.setText("实重" + et_actualweight.getText().toString() + "斤");
                    int number_difference = 0;
                    if ( sellOrderDetail_new.getactualnumber().equals(""))
                    {
                        number_difference = Integer.valueOf(sellOrderDetail_new.getplannumber()) - Integer.valueOf(et_actualnumber.getText().toString());
                    } else
                    {
                        number_difference = Integer.valueOf(sellOrderDetail_new.getactualnumber()) - Integer.valueOf(et_actualnumber.getText().toString());
                    }
                    list_orderdetail.get(pos).setactualnumber(et_actualnumber.getText().toString());
                    list_orderdetail.get(pos).setactualweight(et_actualweight.getText().toString());
                    countAllWeight(list_orderdetail);
                    countAllNumber(list_orderdetail);
                    editNumber(sellOrderDetail_new, et_actualnumber.getText().toString(), et_actualweight.getText().toString(), String.valueOf(number_difference));
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

        private void editNumber(final SellOrderDetail_New sellOrderDetail, String number_new, String weight, String number_difference)
        {
            RequestParams params = new RequestParams();
            params.addQueryStringParameter("uid", sellOrderDetail.getuid());
            params.addQueryStringParameter("contractid", sellOrderDetail.getcontractid());
            params.addQueryStringParameter("year", sellOrderDetail.getYear());
            params.addQueryStringParameter("uuid", sellOrderDetail.getUuid());
            params.addQueryStringParameter("batchTime", sellOrderDetail.getBatchTime());
            params.addQueryStringParameter("number_difference", number_difference);
            params.addQueryStringParameter("weight", weight);
            params.addQueryStringParameter("number_new", number_new);
            params.addQueryStringParameter("action", "feedbackOrderDetail");
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
                        if (result.getAffectedRows() == 1)
                        {
                            Toast.makeText(context, "修改成功！", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent();
                            intent.setAction(broadcast);
                            sendBroadcast(intent);
                        } else
                        {
                            Toast.makeText(context, "修改失败！", Toast.LENGTH_SHORT).show();
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

    }
}
