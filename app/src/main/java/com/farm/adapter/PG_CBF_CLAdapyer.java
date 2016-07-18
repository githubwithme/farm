package com.farm.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.Result;
import com.farm.bean.SellOrderDetail_New;
import com.farm.bean.SellOrder_New;
import com.farm.bean.commembertab;
import com.farm.widget.MyDialog;
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
 * Created by hasee on 2016/7/15.
 */
public class PG_CBF_CLAdapyer extends BaseAdapter
{
    private Context context;
    private List<SellOrder_New> listItems;
    private LayoutInflater listContainer;
    SellOrder_New sellOrder_new;
    String zpzl;
    String cpzl;

    class ListItemView
    {
        public TextView tv_cl;
        public TextView tv_salefor;
        public TextView tv_salefonumr;
        public TextView tv_value;

    }
    public PG_CBF_CLAdapyer(Context context, List<SellOrder_New> data,String zpzl,String cpzl)
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
        sellOrder_new = listItems.get(position);
        int num = position + 1;
        ListItemView listItemView = null;
        if (lmap.get(position) == null)
        {
            if (position == 0)
            {
                convertView = LayoutInflater.from(context).inflate(R.layout.pg_cbf_clperent, null);
            } else
            {
                convertView = LayoutInflater.from(context).inflate(R.layout.pg_cbf_cladapter, null);
            }
            listItemView = new ListItemView();
            listItemView.tv_cl = (TextView) convertView.findViewById(R.id.tv_cl);
            listItemView.tv_salefor = (TextView) convertView.findViewById(R.id.tv_salefor);
            listItemView.tv_salefonumr = (TextView) convertView.findViewById(R.id.tv_salefonumr);
            listItemView.tv_value = (TextView) convertView.findViewById(R.id.tv_value);
            lmap.put(position, convertView);
            convertView.setTag(listItemView);
        } else
        {
            convertView = lmap.get(position);
            listItemView = (ListItemView) convertView.getTag();
        }

        double nums=0;

        listItemView.tv_cl.setText(sellOrder_new.getPlateNumber());
        listItemView.tv_value.setText(sellOrder_new.getActualMoney());
        listItemView.tv_salefonumr.setText(sellOrder_new.getTotal());

        if ( sellOrder_new.getDetailSecLists().size()>0)
        {
               for (int i=0;i<sellOrder_new.getDetailSecLists().size();i++)
               {
                   if (!sellOrder_new.getDetailSecLists().get(i).getactualnumber().equals(""))
                   {
                       nums+=Double.valueOf(sellOrder_new.getDetailSecLists().get(i).getactualnumber());
                   }
               }
            listItemView.tv_salefor.setText(nums+"");

        }else
        {
            listItemView.tv_salefor.setText("0");
        }



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



}
