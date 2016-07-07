package com.farm.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.Result;
import com.farm.bean.SellOrderDetail_New;
import com.farm.bean.commembertab;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by hasee on 2016/7/4.
 */
public class PG_JSD_Adapter extends BaseAdapter
{
    private Context context;
    private List<SellOrderDetail_New> listItems;
    private LayoutInflater listContainer;
    SellOrderDetail_New sellOrderDetail_new;
    String zpzl;
    String cpzl;


    class ListItemView
    {
        public TextView all_cbh;
        public EditText jh_zhushu;
        public EditText zhushu;
        public EditText zhengpin;
        public EditText cipin;
        public EditText jinzhong;
        public Button bianjie;
    }

    public PG_JSD_Adapter(Context context, List<SellOrderDetail_New> data,String zpzl,String cpzl)
    {
        this.context = context;
        this.listContainer = LayoutInflater.from(context);
        this.listItems = data;
        this.zpzl=zpzl;
        this.cpzl=cpzl;

    }

    HashMap<Integer, View> lmap = new HashMap<Integer, View>();

    public View getView(int position, View convertView, ViewGroup parent)
    {
        sellOrderDetail_new = listItems.get(position);
        int num = position + 1;
        ListItemView listItemView = null;
        if (lmap.get(position) == null)
        {
            convertView = listContainer.inflate(R.layout.pg_jsd_adapter, null);
            listItemView = new ListItemView();
//            listItemView.tv_yq = (TextView) convertView.findViewById(R.id.tv_yq);
            listItemView.all_cbh = (TextView) convertView.findViewById(R.id.all_cbh);
            listItemView.jh_zhushu = (EditText) convertView.findViewById(R.id.jh_zhushu);
            listItemView.zhushu = (EditText) convertView.findViewById(R.id.zhushu);
            listItemView.zhengpin = (EditText) convertView.findViewById(R.id.zhengpin);
            listItemView.cipin = (EditText) convertView.findViewById(R.id.cipin);
            listItemView.jinzhong = (EditText) convertView.findViewById(R.id.jinzhong);
            listItemView.bianjie = (Button) convertView.findViewById(R.id.bianjie);
            lmap.put(position, convertView);
            convertView.setTag(listItemView);
        } else
        {
            convertView = lmap.get(position);
            listItemView = (ListItemView) convertView.getTag();
        }



        listItemView.jinzhong.setText(sellOrderDetail_new.getactualweight());
        listItemView.zhushu.setText(sellOrderDetail_new.getactualnumber());
        listItemView.zhengpin.setText(sellOrderDetail_new.getplanprice());
        listItemView.cipin.setText(sellOrderDetail_new.getactualprice());
        listItemView.jh_zhushu.setText(sellOrderDetail_new.getplannumber());
        listItemView.all_cbh.setText(sellOrderDetail_new.getareaname() + "\n" + sellOrderDetail_new.getcontractname());

        listItemView.bianjie.setTag(R.id.tag_cash, sellOrderDetail_new);
        listItemView.bianjie.setTag(R.id.tag_bean, listItemView);
        listItemView.bianjie.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                SellOrderDetail_New sellOrderDetail_new = new SellOrderDetail_New();
                sellOrderDetail_new = (SellOrderDetail_New) view.getTag(R.id.tag_cash);
                ListItemView listItemViews = (ListItemView) view.getTag(R.id.tag_bean);

//                int a = Integer.valueOf(listItemViews.zhushu.getText().toString())-Integer.valueOf(sellOrderDetail_new.getplannumber());
                int number_difference=0;
                if (sellOrderDetail_new.getactualnumber()==null||sellOrderDetail_new.getactualnumber().equals(""))
                {
                     number_difference=Integer.valueOf(sellOrderDetail_new.getplannumber())-Integer.valueOf(listItemViews.zhushu.getText().toString());
                }else
                {
                    number_difference=Integer.valueOf(sellOrderDetail_new.getactualnumber())-Integer.valueOf(listItemViews.zhushu.getText().toString());
                }

                sellOrderDetail_new.setactualnumber(listItemViews.zhushu.getText().toString());
                sellOrderDetail_new.setplanprice(listItemViews.zhengpin.getText().toString());
                sellOrderDetail_new.setactualprice(listItemViews.cipin.getText().toString());
                sellOrderDetail_new.setactualweight(listItemViews.jinzhong.getText().toString());
                feedbackOrderDetail_add(sellOrderDetail_new,number_difference);

            }
        });
//        listItemView.tv_yq.setText(wz_storehouse.getBatchName()+"--"+wz_storehouse.getQuantity());
        return convertView;
    }

    @Override
    public int getCount()
    {
        return listItems.size();
    }

    @Override
    public Object getItem(int arg0)
    {
        return null;
    }

    @Override
    public long getItemId(int arg0)
    {
        return 0;
    }

    /*    number_difference	数量差
    uuid
            uid
    contractid
            year
    batchTime
            number_new
    Weight*/

    private void feedbackOrderDetail_add( SellOrderDetail_New sellOrderDetail_new,  int number_difference)
    {
        commembertab commembertab = AppContext.getUserInfo(context);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("number_difference",number_difference+"");
        params.addQueryStringParameter("uuid",sellOrderDetail_new.getUuid());
        params.addQueryStringParameter("uid",sellOrderDetail_new.getuid());
        params.addQueryStringParameter("contractid",sellOrderDetail_new.getcontractid());
        params.addQueryStringParameter("year",sellOrderDetail_new.getYear());
        params.addQueryStringParameter("number_new", sellOrderDetail_new.getactualnumber());
        params.addQueryStringParameter("batchTime", sellOrderDetail_new.getBatchTime());
        params.addQueryStringParameter("Weight", sellOrderDetail_new.getactualweight());
        params.addQueryStringParameter("qualityNum", sellOrderDetail_new.getplanprice());
        params.addQueryStringParameter("defectNum", sellOrderDetail_new.getactualprice());

   /*     sellOrderDetail_new.setplanprice(listItemViews.zhengpin.getText().toString());
        sellOrderDetail_new.setactualprice(listItemViews.cipin.getText().toString());
        sellOrderDetail_new.setactualweight(listItemViews.jinzhong.getText().toString());*/

        params.addQueryStringParameter("action", "feedbackOrderDetail_add");
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



                    } else
                    {
                        listNewData = new ArrayList<SellOrderDetail_New>();
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
}
