package com.farm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.farm.R;
import com.farm.bean.PG_CKDWbean;
import com.farm.bean.PeopelList;
import com.farm.bean.Wz_Storehouse;

import java.util.HashMap;
import java.util.List;

/**
 * Created by user on 2016/5/18.
 */
public class PG_CKlistAdapter extends BaseAdapter
{

    private Context context;
    private List<PG_CKDWbean> listItems;
    private LayoutInflater listContainer;
    PG_CKDWbean pg_ckdWbean;

    class ListItemView
    {
        public TextView tv_yq;
    }

    public PG_CKlistAdapter(Context context, List<PG_CKDWbean> data)
    {
        this.context = context;
        this.listContainer = LayoutInflater.from(context);
        this.listItems = data;
    }

    HashMap<Integer, View> lmap = new HashMap<Integer, View>();

    public View getView(int position, View convertView, ViewGroup parent)
    {
        pg_ckdWbean = listItems.get(position);
        int num=position+1;
        ListItemView listItemView = null;
        if (lmap.get(position) == null)
        {
            convertView = listContainer.inflate(R.layout.yq_item, null);
            listItemView = new ListItemView();
            listItemView.tv_yq = (TextView) convertView.findViewById(R.id.tv_yq);
            lmap.put(position, convertView);
            convertView.setTag(listItemView);
        } else
        {
            convertView = lmap.get(position);
            listItemView = (ListItemView) convertView.getTag();
        }
        if(!pg_ckdWbean.getThree().equals(""))
        {
            listItemView.tv_yq.setText("第"+num+"批次:" + pg_ckdWbean.getThreeNum() + pg_ckdWbean.getThree());
        }else if(pg_ckdWbean.getThree().equals("")&&!pg_ckdWbean.getSec().equals(""))
        {
            listItemView.tv_yq.setText("第"+num+"批次:" + pg_ckdWbean.getSecNum() + pg_ckdWbean.getSec());
        }else {
            listItemView.tv_yq.setText("第"+num+"批次:"+pg_ckdWbean.getFirsNum()+pg_ckdWbean.getFirs());
        }

        //"批次号:" +
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
}
