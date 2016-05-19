package com.farm.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.Result;
import com.farm.bean.SellOrderDetail_New;
import com.farm.bean.commembertab;
import com.farm.widget.CustomDialog_EditOrderDetail;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.HashMap;
import java.util.List;

public class Adapter_EditSellOrderDetail_NCZ extends BaseAdapter
{
    private Activity context;// 运行上下文
    private List<SellOrderDetail_New> listItems;// 数据集合
    private LayoutInflater listContainer;// 视图容器
    SellOrderDetail_New SellOrderDetail;
    CustomDialog_EditOrderDetail customDialog_editOrderDetaill;
    EditText et_number;

    static class ListItemView
    {
        public TextView tv_area;
        public Button btn_plannumber;

        public TextView tv_yq;
        public TextView tv_pq;
    }

    public Adapter_EditSellOrderDetail_NCZ(Activity context, List<SellOrderDetail_New> data)
    {
        this.context = context;
        this.listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
        this.listItems = data;
    }

    public int getCount()
    {
        return listItems.size();
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
        SellOrderDetail = listItems.get(position);
        // 自定义视图
        ListItemView listItemView = null;
        if (lmap.get(position) == null)
        {
            // 获取list_item布局文件的视图
            convertView = listContainer.inflate(R.layout.adapter_editsellorderdetail_ncz, null);
            listItemView = new ListItemView();
            // 获取控件对象
            listItemView.tv_area = (TextView) convertView.findViewById(R.id.tv_area);
            listItemView.btn_plannumber = (Button) convertView.findViewById(R.id.btn_plannumber);
            listItemView.btn_plannumber.setTag(position);
            listItemView.btn_plannumber.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    int pos = (int) v.getTag();
                    showDialog_editNumber(listItems.get(pos));
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
        listItemView.btn_plannumber.setText(SellOrderDetail.getplannumber());
        listItemView.tv_area.setText(SellOrderDetail.getparkname() + SellOrderDetail.getareaname() + SellOrderDetail.getcontractname());
        return convertView;
    }


    private void editPolygon(final SellOrderDetail_New sellOrderDetail, String number_new, String number_difference)
    {
        commembertab commembertab = AppContext.getUserInfo(context);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", sellOrderDetail.getuid());
        params.addQueryStringParameter("contractid", sellOrderDetail.getcontractid());
        params.addQueryStringParameter("year", sellOrderDetail.getYear());
        params.addQueryStringParameter("uuid", sellOrderDetail.getUuid());
        params.addQueryStringParameter("batchTime", sellOrderDetail.getBatchTime());
        params.addQueryStringParameter("number_difference", number_difference);
        params.addQueryStringParameter("number_new", number_new);
        params.addQueryStringParameter("action", "editSellOrderDetail");
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
                        Toast.makeText(context, "修改成功！", Toast.LENGTH_SHORT).show();
                    } else if (rows.equals("0"))
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


    private void showDialog_editNumber(final SellOrderDetail_New sellOrderDetail_new)
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
                editPolygon(sellOrderDetail_new, et_number.getText().toString(), String.valueOf(number_difference));
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