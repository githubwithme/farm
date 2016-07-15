package com.farm.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.farm.R;
import com.farm.bean.OrderPlan;

import java.util.List;

public class Adapter_OrderPlan_Parentitem extends BaseAdapter
{
    private List<OrderPlan> list;
    private OrderPlan orderPlan;
    private Context context;
    Holder view;

    public Adapter_OrderPlan_Parentitem(Context context, List<OrderPlan> list)
    {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount()
    {
        if (list != null && list.size() > 0) return list.size();
        else return 0;
    }

    @Override
    public Object getItem(int position)
    {
        return list.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            convertView = View.inflate(context, R.layout.adapter_orderplan_parentitem, null);
            view = new Holder(convertView);
            convertView.setTag(view);
        } else
        {
            view = (Holder) convertView.getTag();
        }
        if (list != null && list.size() > 0)
        {
            orderPlan = list.get(position);
            view.tv_parkname.setText(orderPlan.getParkname()+"：");
            view.tv_ordernumber.setText(orderPlan.getOrderNumber() + "单，");
            view.tv_carnumber.setText(orderPlan.getCarNumber() + "车；");
        }

        return convertView;
    }

    private class Holder
    {
        private TextView tv_parkname;
        private TextView tv_ordernumber;
        private TextView tv_carnumber;

        public Holder(View view)
        {
            tv_parkname = (TextView) view.findViewById(R.id.tv_parkname);
            tv_carnumber = (TextView) view.findViewById(R.id.tv_carnumber);
            tv_ordernumber = (TextView) view.findViewById(R.id.tv_ordernumber);
        }
    }

}
