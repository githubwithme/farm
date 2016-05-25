package com.farm.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.farm.R;
import com.farm.bean.goodslisttab;
import com.farm.common.BitmapHelper;
import com.farm.common.SqliteDb;

import java.util.HashMap;
import java.util.List;

public class AddStd_Cmd_goodslistdapter extends BaseAdapter
{
    HashMap<Integer, View> lmap = new HashMap<Integer, View>();
    private List<goodslisttab> list;
    private goodslisttab goodslisttab;
    private Context context;
    ImageView currentiv_tip = null;

    public AddStd_Cmd_goodslistdapter(Context context, ImageView currentiv_tip, List<goodslisttab> list)
    {
        this.list = list;
        this.currentiv_tip = currentiv_tip;
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
        ListItemView listItemView = null;
        if (lmap.get(position) == null)
        {

            convertView = View.inflate(context, R.layout.item_gridview, null);
            listItemView = new ListItemView();
            listItemView.typeicon = (ImageView) convertView.findViewById(R.id.typeicon);
            listItemView.typename = (TextView) convertView.findViewById(R.id.typename);
            listItemView.tv_gg = (TextView) convertView.findViewById(R.id.tv_gg);
            listItemView.cb_fl = (CheckBox) convertView.findViewById(R.id.cb_fl);
            if (list != null && list.size() > 0)
            {
                goodslisttab = list.get(position);
                if (!goodslisttab.getImgurl().equals(""))
                {
                    BitmapHelper.setImageViewBackground(context, listItemView.typeicon, goodslisttab.getImgurl());
                }
                listItemView.typename.setText(goodslisttab.getgoodsName());
                if(!goodslisttab.getThree().equals(""))
                {
//                    listItemView.tv_gg.setText("规格：" +goodslisttab.getThree() + goodslisttab.getgoodsSpec());
                    listItemView.tv_gg.setText("规格：" +goodslisttab.getGoodsStatistical()+goodslisttab.getgoodsunit()+"/"+goodslisttab.getThree() );
                }else if(goodslisttab.getThree().equals("")&&!goodslisttab.getSec().equals(""))
                {
                    listItemView.tv_gg.setText("规格：" +goodslisttab.getGoodsStatistical()+goodslisttab.getgoodsunit()+"/"+goodslisttab.getSec() );
                }else {
                    listItemView.tv_gg.setText("规格：" +goodslisttab.getGoodsStatistical()+goodslisttab.getgoodsunit()+"/"+goodslisttab.getFirs() );
                }
            }
            listItemView.cb_fl.setTag(goodslisttab);
            listItemView.cb_fl.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                {
                    goodslisttab goods = (goodslisttab) buttonView.getTag();
                    if (isChecked)
                    {

                        SqliteDb.save(context, goods);
                        if (currentiv_tip != null)
                        {
                            currentiv_tip.setVisibility(View.VISIBLE);
                        }
                    } else
                    {
                        SqliteDb.deleteGoods(context, goodslisttab.class, goods.getId());
                        currentiv_tip.setVisibility(View.GONE);
                        List<goodslisttab> list_goodslisttab = SqliteDb.getSelectCmdArea(context, goodslisttab.class);
                        for (int i = 0; i < list_goodslisttab.size(); i++)
                        {
                            if (list_goodslisttab.get(i).getId().equals(goodslisttab.getId()))
                            {
                                if (currentiv_tip != null)
                                {
                                    currentiv_tip.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    }
                }
            });
            List<goodslisttab> list_goodslisttab = SqliteDb.getSelectCmdArea(context, goodslisttab.class);
            for (int i = 0; i < list_goodslisttab.size(); i++)
            {
                if (list_goodslisttab.get(i).getId().equals(goodslisttab.getId()))
                {
                    listItemView.cb_fl.setChecked(true);
                    if (currentiv_tip != null)
                    {
                        currentiv_tip.setVisibility(View.VISIBLE);
                    }

                }
            }
            lmap.put(position, convertView);
            convertView.setTag(listItemView);
        } else
        {
            convertView = lmap.get(position);
            listItemView = (ListItemView) convertView.getTag();
        }
        return convertView;
    }


    static class ListItemView
    {
        ImageView typeicon;
        TextView typename;
        TextView tv_gg;
        CheckBox cb_fl;
    }


}
