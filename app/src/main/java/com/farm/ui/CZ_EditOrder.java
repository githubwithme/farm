package com.farm.ui;

import android.app.Activity;
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
import java.util.HashMap;
import java.util.List;

/**
 * Created by ${hmj} on 2016/1/20.
 */

/**
 * 1
 */
@EActivity(R.layout.cz_editorder)
public class CZ_EditOrder extends Activity
{
    Adapter_EditSellOrderDetail adapter_editSellOrderDetail;
    SellOrderDetail SellOrderDetail;
    List<SellOrderDetail_New> list_orderDetail;
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


    @Click
    void btn_sure()
    {
        if (et_name.getText().toString().equals(""))
        {
            Toast.makeText(CZ_EditOrder.this, "请先填写信息", Toast.LENGTH_SHORT).show();
            return;
        }
        if (et_email.getText().toString().equals(""))
        {
            Toast.makeText(CZ_EditOrder.this, "请先填写信息", Toast.LENGTH_SHORT).show();
            return;
        }
        if (et_address.getText().toString().equals(""))
        {
            Toast.makeText(CZ_EditOrder.this, "请先填写信息", Toast.LENGTH_SHORT).show();
            return;
        }
        if (et_phone.getText().toString().equals(""))
        {
            Toast.makeText(CZ_EditOrder.this, "请先填写信息", Toast.LENGTH_SHORT).show();
            return;
        }
        if (et_price.getText().toString().equals(""))
        {
            Toast.makeText(CZ_EditOrder.this, "请先填写信息", Toast.LENGTH_SHORT).show();
            return;
        }
        if (et_weight.getText().toString().equals(""))
        {
            Toast.makeText(CZ_EditOrder.this, "请先填写信息", Toast.LENGTH_SHORT).show();
            return;
        }
        if (et_number.getText().toString().equals(""))
        {
            Toast.makeText(CZ_EditOrder.this, "请先填写信息", Toast.LENGTH_SHORT).show();
            return;
        }
        if (et_values.getText().toString().equals(""))
        {
            Toast.makeText(CZ_EditOrder.this, "请先填写信息", Toast.LENGTH_SHORT).show();
            return;
        }

        StringBuilder builder = new StringBuilder();
        builder.append("{\"listdata\": ");
        builder.append(JSON.toJSONString(list_orderDetail));
        builder.append("} ");
        feedBackOrderDetail(builder.toString());
    }

    @AfterViews
    void afterOncreate()
    {
        adapter_editSellOrderDetail = new Adapter_EditSellOrderDetail(CZ_EditOrder.this);
        lv.setAdapter(adapter_editSellOrderDetail);
        utils.setListViewHeight(lv);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        list_orderDetail = getIntent().getParcelableArrayListExtra("list");
    }


    private void feedBackOrderDetail(String data)
    {
        RequestParams params = new RequestParams();
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
                        Toast.makeText(CZ_EditOrder.this, "订单创建成功！", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                } else
                {
                    AppContext.makeToast(CZ_EditOrder.this, "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(CZ_EditOrder.this, "error_connectServer");
            }
        });
    }

    public class Adapter_EditSellOrderDetail extends BaseAdapter
    {
        private Activity context;// 运行上下文
        private LayoutInflater listContainer;// 视图容器
        SellOrderDetail_New SellOrderDetail;

        class ListItemView
        {
            public TextView tv_batchtime;
            public EditText et_actualnumber;
            public EditText et_actualweight;
            public TextView tv_area;

            public TextView tv_yq;
            public TextView tv_pq;
        }

        public Adapter_EditSellOrderDetail(Activity context)
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
                convertView = listContainer.inflate(R.layout.adapter_editsellorderdetail, null);
                listItemView = new ListItemView();
                // 获取控件对象
                listItemView.et_actualnumber = (EditText) convertView.findViewById(R.id.et_actualnumber);
                listItemView.et_actualweight = (EditText) convertView.findViewById(R.id.et_actualweight);
                listItemView.tv_area = (TextView) convertView.findViewById(R.id.tv_area);
                listItemView.tv_batchtime = (TextView) convertView.findViewById(R.id.tv_batchtime);
                // 设置控件集到convertView
                lmap.put(position, convertView);
                convertView.setTag(listItemView);
            } else
            {
                convertView = lmap.get(position);
                listItemView = (ListItemView) convertView.getTag();
            }
            // 设置文字和图片
            listItemView.et_actualnumber.setText(SellOrderDetail.getactualnumber());
            listItemView.et_actualweight.setText(SellOrderDetail.getactualweight());
            listItemView.tv_batchtime.setText("批次:" + SellOrderDetail.getBatchTime());
            listItemView.tv_area.setText(SellOrderDetail.getparkname() + SellOrderDetail.getareaname() + SellOrderDetail.getcontractname());
            return convertView;
        }


    }
}
