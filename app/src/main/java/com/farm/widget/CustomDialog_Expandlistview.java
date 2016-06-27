package com.farm.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.farm.R;
import com.farm.app.AppContext;
import com.farm.bean.WZ_CRk;
import com.farm.bean.WZ_RKxx;
import com.farm.bean.commembertab;
import com.farm.ui.NCZ_WZ_RKDetail_;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by hasee on 2016/6/24.
 */
public class CustomDialog_Expandlistview extends Dialog
{
    HashMap<Integer, View> lmap = new HashMap<Integer, View>();
    CustomDialogListener cdListener;
    Context context;
    View layout;
    List<WZ_CRk> listData;
    String currentDat = "";
    ExpandableListView expandableListView;

    public CustomDialog_Expandlistview(Context context, int theme, View layout, List<WZ_CRk> listData, CustomDialogListener cdListener)
    {
        super(context, theme);
        this.context = context;
        this.layout = layout;
        this.listData = listData;
        this.cdListener = cdListener;
    }

    public CustomDialog_Expandlistview(Context context, int theme)
    {
        super(context, theme);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.setContentView(layout);
        this.setCanceledOnTouchOutside(true);
//        lv=findViewById(R.id.action_settings);
        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        expandableListView.setAdapter(new Cbaohudechose());
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener()
        {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l)
            {
                Bundle bundle = new Bundle();
                bundle.putString("parkname", listData.get(i).getBatchName());
                bundle.putString("cbhname", listData.get(i).getWzcrkxx().get(i1).getGoodsname());
                cdListener.OnClick(bundle);
                dismiss();
                return true;
            }
        });
/*        lv.setAdapter(new Dialog_ListViewAdapter());
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Bundle bundle = new Bundle();
                bundle.putString("id", listid.get(position));
                bundle.putString("name", listdata.get(position));
                cdListener.OnClick(bundle);
                dismiss();
            }
        });*/
     /*    expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener()
        {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id)
            {
                parktab parktab = listNewData.get(groupPosition);
                BatchTime batchTime = parktab.getBatchTimeList().get(childPosition);
                Intent intent = new Intent(NCZ_FarmSale.this, NCZ_FarmSale_BatchDetail_.class);
                intent.putExtra("parkid", parktab.getid());
                intent.putExtra("batchTime", batchTime.getBatchTime());
                NCZ_FarmSale.this.startActivity(intent);
                return true;
            }
        });*/
    }


    public class Cbaohudechose extends BaseExpandableListAdapter
    {


        //得到子item需要关联的数据
        @Override
        public Object getChild(int groupPosition, int childPosition)
        {
            if (listData.get(groupPosition).getWzcrkxx() == null)
            {
                return null;
            }
            return listData.get(groupPosition).getWzcrkxx().get(childPosition);
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
        //设置子item的组件
        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
        {

            List<WZ_RKxx> childData = listData.get(groupPosition).getWzcrkxx();
            final WZ_RKxx wz_rKxx = childData.get(childPosition);

            final String batchname = listData.get(groupPosition).getBatchName();
            final String indate = listData.get(groupPosition).getInDate();
            View v = null;
            if (lmap.get(groupPosition) != null)
            {
                HashMap<Integer, View> map1 = lmap.get(groupPosition);
                v = lmap.get(groupPosition).get(childPosition);
            }
            if (v == null)
            {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.layout_children_rkexecute, null);
                listItemView = new ListItemView();
                listItemView.goodsname = (TextView) convertView.findViewById(R.id.goodsname);
                listItemView.quantity = (TextView) convertView.findViewById(R.id.quantity);
/*                convertView.setTag(listItemView);
                convertView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Intent intent = new Intent(context, NCZ_WZ_RKDetail_.class);

                        intent.putExtra("wz_rKxx", wz_rKxx);
                        intent.putExtra("batchname", batchname);
                        intent.putExtra("indate", indate);
                        context.startActivity(intent);

                    }
                });*/
                map.put(childPosition, convertView);
                lmap.put(groupPosition, map);
                if (isLastChild)
                {
                    map = new HashMap<>();
                }

                //数据添加
                listItemView.goodsname.setText(wz_rKxx.getGoodsname());
//            listItemView.local.setText(wz_rKxx.getParkName() + "-" + wz_rKxx.getStorehouseName());
                listItemView.quantity.setText( wz_rKxx.getQuantity());
//            listItemView.inGoodsvalue.setText("总值:" + wz_rKxx.getInGoodsvalue() + "元");
//            listItemView.zhongliangs.setText("总量:" + wz_rKxx.getSumWeight() );
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
            super.onGroupExpanded(groupPosition);
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
            if (listData.get(groupPosition).getWzcrkxx() == null)
            {
                return 0;
            }
            return listData.get(groupPosition).getWzcrkxx().size();
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
            TextView inDate = (TextView) convertView.findViewById(R.id.inDate);
            TextView batchName = (TextView) convertView.findViewById(R.id.batchName);
            TextView loadingFee = (TextView) convertView.findViewById(R.id.loadingFee);
            TextView shippingFee = (TextView) convertView.findViewById(R.id.shippingFee);
            TextView inGoodsValue = (TextView) convertView.findViewById(R.id.inGoodsValue);
            FrameLayout fl_new_item = (FrameLayout) convertView.findViewById(R.id.fl_new_item);
            RelativeLayout groupExpand = (RelativeLayout) convertView.findViewById(R.id.groupExpand);



            if (listData.get(groupPosition).getFlashStr().equals("1"))
            {
                fl_new_item.setVisibility(View.VISIBLE);
            } else
            {
                fl_new_item.setVisibility(View.GONE);
            }
/*        groupExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isExpanded) {
                    mainlistview.collapseGroup(groupPosition);
                } else {
                    mainlistview.expandGroup(groupPosition);
                }
            }
        });*/
            inDate.setText(listData.get(groupPosition).getInDate());
            batchName.setText("批次号:" + listData.get(groupPosition).getBatchName());
            loadingFee.setText("装卸费:" + listData.get(groupPosition).getLoadingFee() + "元");
            shippingFee.setText("运费:" + listData.get(groupPosition).getShippingFee() + "元");
            double a= Double.valueOf(listData.get(groupPosition).getInGoodsValue())+Double.valueOf(listData.get(groupPosition).getLoadingFee())+Double.valueOf(listData.get(groupPosition).getShippingFee());
            inGoodsValue.setText("总值"+String.format("%.2f",a) + "元");
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

    static class ListItemView
    {
        public TextView goodsname;
        public TextView quantity;
    }

    public interface CustomDialogListener
    {
        public void OnClick(Bundle bundle);
    }

}
