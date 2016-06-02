package com.farm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.farm.R;
import com.farm.bean.WZ_Detail;
import com.farm.bean.Wz_Storehouse;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by user on 2016/4/6.
 */
public class NCZ_CKWZDetailAdapter extends BaseAdapter
{
    private Context context;// 运行上下文
    private List<com.farm.bean.Wz_Storehouse> listItems;// 数据集合
    private List<com.farm.bean.WZ_Detail> wz_detailList;// 数据集合
    private LayoutInflater listContainer;
    Wz_Storehouse Wz_Storehouse;
    WZ_Detail wz_detail;
    String[] num = new String[]{"0","0","0"};
    String bb;

    public NCZ_CKWZDetailAdapter(Context context, List<Wz_Storehouse> data, List<WZ_Detail> wz_details)
    {
        this.context = context;
        this.listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
        this.listItems = data;
        this.wz_detailList = wz_details;
    }

    static class ListItemView
    {
        public TextView batchName;
        public TextView quantity;
        public TextView inGoodsValue;
        public TextView expDate;
        public TextView inDate;
        public TextView guige;
        public TextView shuliang;
    }

    @Override
    public int getCount()
    {
        return listItems.size();
    }

    @Override
    public Object getItem(int i)
    {
        return null;
    }

    @Override
    public long getItemId(int i)
    {
        return 0;
    }

    HashMap<Integer, View> lmap = new HashMap<Integer, View>();

    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        wz_detail = wz_detailList.get(0);
        Wz_Storehouse = listItems.get(i);
        // 自定义视图
        ListItemView listItemView = null;
        if (lmap.get(i) == null)
        {
            // 获取list_item布局文件的视图
            view = listContainer.inflate(R.layout.ncz_ckwzdetailadapter, null);
            listItemView = new ListItemView();
            // 获取控件对象
            listItemView.batchName = (TextView) view.findViewById(R.id.batchName);
            listItemView.quantity = (TextView) view.findViewById(R.id.quantity);
            listItemView.inGoodsValue = (TextView) view.findViewById(R.id.inGoodsValue);
            listItemView.expDate = (TextView) view.findViewById(R.id.expDate);
            listItemView.inDate = (TextView) view.findViewById(R.id.inDate);
            listItemView.guige = (TextView) view.findViewById(R.id.guige);
            listItemView.shuliang = (TextView) view.findViewById(R.id.shuliang);
            lmap.put(i, view);
            view.setTag(listItemView);
        } else
        {
            view = lmap.get(i);
            listItemView = (ListItemView) view.getTag();
        }

        String a = Wz_Storehouse.getExpDate().substring(0, Wz_Storehouse.getExpDate().length() - 8);
        String b = Wz_Storehouse.getInDate().substring(0, Wz_Storehouse.getInDate().length() - 8);
        listItemView.batchName.setText(Wz_Storehouse.getBatchName());
        if (!wz_detail.getThree().equals(""))
        {
            listItemView.guige.setText(wz_detail.getGoodsStatistical() + wz_detail.getGoodsunit() + "/" + wz_detail.getThree());
        } else if (wz_detail.getThree().equals("") && !wz_detail.getSec().equals(""))
        {
            listItemView.guige.setText(wz_detail.getGoodsStatistical() + wz_detail.getGoodsunit() + "/" + wz_detail.getSec());
        } else
        {
            listItemView.guige.setText(wz_detail.getGoodsStatistical() + wz_detail.getGoodsunit() + "/" + wz_detail.getFirs());
        }


        String aa = Wz_Storehouse.getQuantity();
//        string[] num = message.Split(new char[] { '件', '箱', '盒', '袋', '包', '瓶', '桶'});

        String[] str = {"件", "箱", "盒", "袋", "包", "瓶", "桶"};
//        String[] num =aa.split(new char[] { '件', '箱', '盒', '袋', '包', '瓶', '桶' });
        int l = 0;
        for (int k = 0; k < str.length; k++)
        {

            String[] dd = aa.split(str[k]);
            if (dd.length == 2)
            {
                aa = dd[1];
                num[l] = dd[0];
                l++;

            } else
            {
                aa = dd[0];
                //价格判断是否为数字
                Pattern p = Pattern.compile("[0-9]*");
                Matcher m = p.matcher(aa);
                if (m.matches())
                {
                    num[l] = dd[0];
                }

            }

        }
        if (!num[2].equals("0"))
        {
            bb = num[2];
        } else if (!num[1].equals("0") && num[2].equals("0"))
        {
            bb = num[1];
        } else
        {
            bb = num[0];
        }




  /*      int x=Integer.parseInt(Wz_Storehouse.getQuantity().substring(0,Wz_Storehouse.getQuantity().length()-1));
        int y=Integer.parseInt(wz_detail.getGoodsStatistical());*/
        listItemView.quantity.setText(Integer.parseInt(bb)*Integer.parseInt(wz_detail.getGoodsStatistical())+ Wz_Storehouse.getGoodsunit());
//        listItemView.quantity.setText(Wz_Storehouse.getGoodsStatistical() + Wz_Storehouse.getGoodsunit());
        listItemView.shuliang.setText(Wz_Storehouse.getQuantity());
        listItemView.inGoodsValue.setText(Wz_Storehouse.getInGoodsValue() + "元");
        listItemView.expDate.setText(a);
        listItemView.inDate.setText(b);
        return view;
    }
}
