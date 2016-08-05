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
import com.farm.bean.commembertab;
import com.farm.widget.MyDialog;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by hasee on 2016/7/19.
 */
public class NCZ_Look_JSD_Adapter extends BaseAdapter
{
    MyDialog myDialog;
    private Context context;
    private List<SellOrderDetail_New> listItems;
    private LayoutInflater listContainer;
    SellOrderDetail_New sellOrderDetail_new;
    String zpzl;
    String cpzl;
    TextView shengyuliang;


    class ListItemView
    {
        public TextView all_cbh;
        public TextView zhushu;
        public TextView zhengpin;
        public TextView cipin;
        public TextView jinzhong;
    }

    public NCZ_Look_JSD_Adapter(Context context, List<SellOrderDetail_New> data,String zpzl,String cpzl)
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
            convertView = listContainer.inflate(R.layout.ncz_look_jsd_adapter_item, null);
            listItemView = new ListItemView();
            listItemView.all_cbh = (TextView) convertView.findViewById(R.id.all_cbh);
            listItemView.zhushu = (TextView) convertView.findViewById(R.id.zhushu);
            listItemView.zhengpin = (TextView) convertView.findViewById(R.id.zhengpin);
            listItemView.cipin = (TextView) convertView.findViewById(R.id.cipin);
            listItemView.jinzhong = (TextView) convertView.findViewById(R.id.jinzhong);
            lmap.put(position, convertView);
            convertView.setTag(listItemView);
        } else
        {
            convertView = lmap.get(position);
            listItemView = (ListItemView) convertView.getTag();
        }

        if (position % 2 == 0)
        {
            convertView.setBackgroundResource(R.color.light_gray);
        } else
        {
            convertView.setBackgroundResource(R.color.white);
        }

        listItemView.jinzhong.setText(sellOrderDetail_new.getactualweight());
        listItemView.zhushu.setText(sellOrderDetail_new.getactualnumber());
        listItemView.cipin.setText(sellOrderDetail_new.getplanprice());
        listItemView.zhengpin.setText(sellOrderDetail_new.getactualprice());
        listItemView.all_cbh.setText(sellOrderDetail_new.getareaname() + "\n" + sellOrderDetail_new.getcontractname());

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

}
