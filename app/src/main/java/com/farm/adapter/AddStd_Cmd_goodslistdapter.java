package com.farm.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.farm.R;
import com.farm.bean.goodslisttab;
import com.farm.common.BitmapHelper;

import java.util.List;

public class AddStd_Cmd_goodslistdapter extends BaseAdapter
{

    private List<goodslisttab> list;
    private goodslisttab goodslisttab;
    private Context context;
    Holder view;

    public AddStd_Cmd_goodslistdapter(Context context, List<goodslisttab> list)
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
            convertView = View.inflate(context, R.layout.item_gridview, null);
            view = new Holder(convertView);
            convertView.setTag(view);
        } else
        {
            view = (Holder) convertView.getTag();
        }
        if (list != null && list.size() > 0)
        {
            goodslisttab = list.get(position);
            BitmapHelper.setImageViewBackground(context, view.icon,"http://pic4.nipic.com/20090827/3095621_083213047918_2.jpg");
            view.name.setText(goodslisttab.getgoodsName());
        }

        return convertView;
    }

    private class Holder
    {
        private ImageView icon;
        private TextView name;

        public Holder(View view)
        {
            icon = (ImageView) view.findViewById(R.id.typeicon);
            name = (TextView) view.findViewById(R.id.typename);
        }
    }

}
