package com.farm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.farm.R;
import com.farm.bean.Purchaser;

import java.util.HashMap;
import java.util.List;

/**
 * Created by hasee on 2016/6/20.
 */
public class NCZ_CGSAdapter extends BaseAdapter
{
    private Context context;
    private List<Purchaser> listItems;
    private LayoutInflater listContainer;
    Purchaser purchaser;

    class ListItemView
    {
        public TextView tv_yq;
    }

    public NCZ_CGSAdapter(Context context, List<Purchaser> data)
    {
        this.context = context;
        this.listContainer = LayoutInflater.from(context);
        this.listItems = data;
    }

    HashMap<Integer, View> lmap = new HashMap<Integer, View>();

    public View getView(int position, View convertView, ViewGroup parent)
    {
        purchaser = listItems.get(position);
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
        listItemView.tv_yq.setText(purchaser.getName());
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
