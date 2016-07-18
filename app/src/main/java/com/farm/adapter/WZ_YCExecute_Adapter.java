package com.farm.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.farm.R;
import com.farm.app.AppContext;
import com.farm.bean.ListYc;
import com.farm.bean.WZ_YCxx;
import com.farm.bean.commembertab;
import com.farm.ui.NCZ_NewYc_detail_;
import com.farm.ui.NCZ_WZ_YCDetail_;
import com.farm.widget.CustomDialog_ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by hasee on 2016/7/12.
 */
public class WZ_YCExecute_Adapter extends BaseExpandableListAdapter
{

    TextView currentTextView;
    CustomDialog_ListView customDialog_listView;
    ExpandableListView mainlistview;
    private Context context;// 运行上下文
    int currentChildsize = 0;
    private GoodsAdapter adapter;
    List<ListYc> listData;
    ListView list;

    public WZ_YCExecute_Adapter(Context context, List<ListYc> listData, ExpandableListView mainlistview)
    {
        this.mainlistview = mainlistview;
        this.listData = listData;
        this.context = context;
    }

    //得到子item需要关联的数据
    @Override
    public Object getChild(int groupPosition, int childPosition)
    {
        if (listData.get(groupPosition).getGoodsLists() == null)
        {
            return null;
        }
        return listData.get(groupPosition).getGoodsLists().get(childPosition);
    }

    //得到子item的ID
    @Override
    public long getChildId(int groupPosition, int childPosition)
    {
        return childPosition;
    }

    HashMap<Integer, HashMap<Integer, View>> lmap = new HashMap<Integer, HashMap<Integer, View>>();
    HashMap<Integer, View> map = new HashMap<>();
    ListItemView listItemView = null;

    static class ListItemView
    {
        public TextView goodsname;
        public TextView number;
        public TextView data;
        public TextView storehouse;
        public TextView type;


        public TextView tv_goodsname;
        public TextView tv_number;
        public TextView tv_values;
        public TextView tv_type;
        public TextView tv_gg;
        public TextView tv_type_tip;
        public TextView tv_gg_tip;
    }

    //设置子item的组件
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
    {

        List<WZ_YCxx> childData = listData.get(groupPosition).getGoodsLists();
        final String parkname = listData.get(groupPosition).getParkName();
        final WZ_YCxx wz_YCxx = childData.get(childPosition);

        View v = null;
        if (lmap.get(groupPosition) != null)
        {
            HashMap<Integer, View> map1 = lmap.get(groupPosition);
            v = lmap.get(groupPosition).get(childPosition);
        }
        if (v == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.wz_yc_child_adapter, null);
            listItemView = new ListItemView();
            listItemView.goodsname = (TextView) convertView.findViewById(R.id.goodsname);
            listItemView.number = (TextView) convertView.findViewById(R.id.number);
            listItemView.data = (TextView) convertView.findViewById(R.id.data);
            listItemView.storehouse = (TextView) convertView.findViewById(R.id.storehouse);
            listItemView.type = (TextView) convertView.findViewById(R.id.type);


            listItemView.tv_goodsname = (TextView) convertView.findViewById(R.id.tv_goodsname);
            listItemView.tv_number = (TextView) convertView.findViewById(R.id.tv_number);
            listItemView.tv_values = (TextView) convertView.findViewById(R.id.tv_values);
            listItemView.tv_type = (TextView) convertView.findViewById(R.id.tv_type);
            listItemView.tv_gg = (TextView) convertView.findViewById(R.id.tv_gg);
            listItemView.tv_type_tip = (TextView) convertView.findViewById(R.id.tv_type_tip);
            listItemView.tv_gg_tip = (TextView) convertView.findViewById(R.id.tv_gg_tip);
            convertView.setTag(listItemView);
            convertView.setTag(R.id.tag_bean, wz_YCxx);
            convertView.setTag(R.id.tag_rk, parkname);
            convertView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    String parknames = (String) v.getTag(R.id.tag_rk);
                    WZ_YCxx wz_yCxxList = new WZ_YCxx();
                    wz_yCxxList = (WZ_YCxx) v.getTag(R.id.tag_bean);
                    Intent intent = new Intent(context, NCZ_NewYc_detail_.class);
                    intent.putExtra("wz_rKxx", wz_yCxxList);
                    intent.putExtra("parkname", parknames);
                    context.startActivity(intent);

                }
            });
            map.put(childPosition, convertView);
            lmap.put(groupPosition, map);

//新数据

            listItemView.tv_goodsname.setText(wz_YCxx.getGoodsName());



            listItemView.tv_values.setText(wz_YCxx.getStorehouseName());





            //旧数据
            if (isLastChild)
            {
                map = new HashMap<>();
            }

            String danwei = "";
            if (!wz_YCxx.getThree().equals(""))
            {
                listItemView.number.setText("库存数量:" + wz_YCxx.getThreeNum() + wz_YCxx.getThree());
                listItemView.tv_number.setText( wz_YCxx.getThreeNum() + wz_YCxx.getThree());
                danwei = wz_YCxx.getThree();
            } else if (wz_YCxx.getThree().equals("") && !wz_YCxx.getSec().equals(""))
            {
                listItemView.number.setText("库存数量:" + wz_YCxx.getSecNum() + wz_YCxx.getSec());
                listItemView.tv_number.setText(wz_YCxx.getSecNum() + wz_YCxx.getSec());
                danwei = wz_YCxx.getSec();
            } else
            {
                listItemView.number.setText("库存数量:" + wz_YCxx.getFirsNum() + wz_YCxx.getFirs());
                listItemView.tv_number.setText( wz_YCxx.getFirsNum() + wz_YCxx.getFirs());
                danwei = wz_YCxx.getFirs();
            }

            //数据添加
            listItemView.goodsname.setText(wz_YCxx.getGoodsName());


            listItemView.storehouse.setText(wz_YCxx.getStorehouseName());
            if (wz_YCxx.getType().equals("0"))
            {
                listItemView.type.setText("即将过期");
                listItemView.tv_type.setText("即将过期");
                listItemView.tv_gg_tip.setText("过期时间:");
                listItemView.data.setText("过期时间:" + wz_YCxx.getExpDate1().substring(0, wz_YCxx.getExpDate1().length() - 8));
                listItemView.tv_gg.setText( wz_YCxx.getExpDate1().substring(0, wz_YCxx.getExpDate1().length() - 8));

            } else if (wz_YCxx.getType().equals("1"))
            {
                listItemView.type.setText("库存过低");
                listItemView.tv_type.setText("库存过低");
                listItemView.tv_gg_tip.setText("警戒数量:");
                listItemView.data.setText("警戒数量:" + wz_YCxx.getLevelOfWarning() + danwei);
                listItemView.tv_gg.setText(wz_YCxx.getLevelOfWarning() + danwei);
            } else if (wz_YCxx.getType().equals("2"))
            {
                listItemView.type.setText("即将过期|库存过低");
                listItemView.tv_type.setText("即将过期");
                listItemView.tv_gg_tip.setText("过期时间:");
                listItemView.data.setText("过期时间:" + wz_YCxx.getExpDate1().substring(0, wz_YCxx.getExpDate1().length() - 8));
                listItemView.tv_gg.setText( wz_YCxx.getExpDate1().substring(0, wz_YCxx.getExpDate1().length() - 8));
            } else if (wz_YCxx.getType().equals("3"))
            {
                listItemView.type.setText("已经过期");
                listItemView.tv_type.setText("已经过期");
                listItemView.tv_gg_tip.setText("过期时间:");
                listItemView.data.setText("过期时间:" + wz_YCxx.getExpDate1().substring(0, wz_YCxx.getExpDate1().length() - 8));
                listItemView.tv_gg.setText( wz_YCxx.getExpDate1().substring(0, wz_YCxx.getExpDate1().length() - 8));
            }


        } else
        {
            convertView = lmap.get(groupPosition).get(childPosition);
            listItemView = (ListItemView) convertView.getTag();
        }
        //数据添加  都可以数据加载，不过在上面比较好，这里是返回view
        return convertView;
    }

    @Override
    public void onGroupExpanded(int groupPosition)
    {
        // mExpListView 是列表实例，通过判断它的状态，关闭已经展开的。
        super.onGroupExpanded(groupPosition);
        //只展开一项
/*        int len = this.getGroupCount();

        for (int i = 0; i < len; i++) {
            if (i != groupPosition) {
                mainlistview.collapseGroup(i);
            }
        }*/
//        mainlistview  WZ_RKExecute_Adapter

    }

    @Override
    public void onGroupCollapsed(int groupPosition)
    {
        super.onGroupCollapsed(groupPosition);

    }

    //获取当前父item下的子item的个数
    @Override
    public int getChildrenCount(int groupPosition)
    {
        if (listData.get(groupPosition).getGoodsLists() == null)
        {
            return 0;
        }
        return listData.get(groupPosition).getGoodsLists().size();
    }

    //获取当前父item的数据
    @Override
    public Object getGroup(int groupPosition)
    {
        return listData.get(groupPosition);
    }

    @Override
    public int getGroupCount()
    {
        return listData.size();
    }

    @Override
    public long getGroupId(int groupPosition)
    {
        return groupPosition;
    }

    //设置父item组件
    @Override
    public View getGroupView(final int groupPosition, final boolean isExpanded, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.layout_parent_rkexecute, null);
        }
        TextView batchName = (TextView) convertView.findViewById(R.id.batchName);
        TextView inGoodsValue = (TextView) convertView.findViewById(R.id.inGoodsValue);
        FrameLayout fl_new_item = (FrameLayout) convertView.findViewById(R.id.fl_new_item);
        RelativeLayout groupExpand = (RelativeLayout) convertView.findViewById(R.id.groupExpand);
        convertView.setTag(R.id.tag_rk, listData.get(groupPosition));
        convertView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (isExpanded)
                {
                    mainlistview.collapseGroup(groupPosition);
                } else
                {
                    mainlistview.expandGroup(groupPosition);
                }
 /*               ListYc listYc = (ListYc) view.getTag(R.id.tag_rk);
                if (ListYc.getFlashStr().equals("1"))
                {
                    commembertab commembertab = AppContext.getUserInfo(context);
                    AppContext.eventStatus(context, "4", wz_cRk2.getBatchNumber(), commembertab.getId());
                }*/


            }
        });
        batchName.setTextSize(16);
        batchName.setText(listData.get(groupPosition).getParkName());
        inGoodsValue.setTextSize(12);
        inGoodsValue.setText(listData.get(groupPosition).getParkCount() + "条");

/*
        double a= Double.valueOf(listData.get(groupPosition).getInGoodsValue())+Double.valueOf(listData.get(groupPosition).getLoadingFee())+Double.valueOf(listData.get(groupPosition).getShippingFee());
        inGoodsValue.setText("总值"+String.format("%.2f",a) + "元");
 */
        return convertView;
    }


    @Override
    public boolean hasStableIds()
    {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition)
    {
        return true;
    }
}
