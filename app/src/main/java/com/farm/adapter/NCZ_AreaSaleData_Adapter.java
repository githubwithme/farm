package com.farm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

import com.farm.R;
import com.farm.bean.areatab;
import com.farm.bean.contractTab;
import com.farm.common.utils;
import com.farm.widget.CustomDialog_EditSaleInInfo;
import com.farm.widget.CustomDialog_ListView;
import com.farm.widget.CustomListView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by user on 2016/4/25.
 */
public class NCZ_AreaSaleData_Adapter extends BaseExpandableListAdapter
{
    GridViewAdapter_SellOrDetail_NCZ gridViewAdapter_sellOrDetail_ncz;
    TextView currentTextView;
    CustomDialog_ListView customDialog_listView;
    private int currentItem = 0;
    ExpandableListView mainlistview;
    private Context context;// 运行上下文
    int currentChildsize = 0;
    private GoodsAdapter adapter;
    List<areatab> listData;
    ListView list;

    public NCZ_AreaSaleData_Adapter(Context context, List<areatab> listData, ExpandableListView mainlistview)
    {
        this.mainlistview = mainlistview;
        this.listData = listData;
        this.context = context;
    }

    //得到子item需要关联的数据
    @Override
    public Object getChild(int groupPosition, int childPosition)
    {
        if (listData.get(groupPosition).getContractTabList() == null)
        {
            return null;
        }
        return listData.get(groupPosition).getContractTabList().get(childPosition);
    }

    static class ListItemView
    {
        public CustomListView lv;

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

        List<contractTab> childData = listData.get(groupPosition).getContractTabList();
        final contractTab contractTab = childData.get(childPosition);

        View v = null;
        if (lmap.get(groupPosition) != null)
        {
            HashMap<Integer, View> map1 = lmap.get(groupPosition);
            v = lmap.get(groupPosition).get(childPosition);
        }
        if (v == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.ncz_areasaledata_child, null);//ncz_pc_todayjobadater-ncz_pc_todayjobadater
            listItemView = new ListItemView();

            // 获取控件对象
            listItemView.lv = (CustomListView) convertView.findViewById(R.id.lv);
            map.put(childPosition, convertView);
            lmap.put(groupPosition, map);
            if (isLastChild)
            {
                map = new HashMap<>();
            }
            gridViewAdapter_sellOrDetail_ncz = new GridViewAdapter_SellOrDetail_NCZ(context, childData);
            listItemView.lv.setAdapter(gridViewAdapter_sellOrDetail_ncz);
            utils.setListViewHeight(listItemView.lv);
        } else
        {
            convertView = lmap.get(groupPosition).get(childPosition);
            listItemView = (ListItemView) convertView.getTag();
        }
        return convertView;
    }


    @Override
    public void onGroupExpanded(int groupPosition)
    {
        // mExpListView 是列表实例，通过判断它的状态，关闭已经展开的。
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
        if (listData.get(groupPosition).getContractTabList() == null)
        {
            return 0;
        }
        return 1;
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
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            if (groupPosition == 0)
            {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.ncz_areasaledata_parenttopitem, null);
            } else
            {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.ncz_areasaledata_parentitem, null);
            }

        }

        TextView tv_salein = (TextView) convertView.findViewById(R.id.tv_salein);
        TextView tv_saleout = (TextView) convertView.findViewById(R.id.tv_saleout);
        TextView tv_allsale = (TextView) convertView.findViewById(R.id.tv_allsale);
        TextView tv_salefor = (TextView) convertView.findViewById(R.id.tv_salefor);
        TextView tv_contractname = (TextView) convertView.findViewById(R.id.tv_contractname);
        areatab areatab = listData.get(groupPosition);
        int percent = 0;
        float allnumber = Integer.valueOf(areatab.getAllsaleout()) + Integer.valueOf(areatab.getAllsalein()) + Integer.valueOf(areatab.getAllnewsale()) + Integer.valueOf(areatab.getAllsalefor());
        float salenumber = Integer.valueOf(areatab.getAllsaleout()) + Integer.valueOf(areatab.getAllsalein()) + Integer.valueOf(areatab.getAllnewsale());
        if (allnumber != 0)
        {
            percent = (int) ((salenumber / allnumber) * 100);
        }

        tv_contractname.setText(listData.get(groupPosition).getareaName());
        tv_salefor.setText(areatab.getAllsalefor());
        tv_allsale.setText(String.valueOf(allnumber));
        tv_saleout.setText(areatab.getAllsaleout());
        tv_salein.setText(areatab.getAllsalein());
        if (areatab.getAllsalefor().equals("0"))
        {
            if (salenumber != 0)
            {
//                tv_salefor.setText("售完");
                tv_salefor.setText("0");
            } else
            {
//                tv_allsale.setText("未上报");
                tv_allsale.setText("0");
            }
        }
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


    public class GridViewAdapter_SellOrDetail_NCZ extends BaseAdapter
    {
        List<contractTab> list;
        EditText et_number;
        CustomDialog_EditSaleInInfo customDialog_editSaleInInfo;
        private Context context;
        Holder view;

        public GridViewAdapter_SellOrDetail_NCZ(Context context, List<contractTab> list)
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
//                if (position == 0)
//                {
//                    convertView = View.inflate(context, R.layout.ncz_areasaledata_childtopitem, null);
//                } else
//                {
//                    convertView = View.inflate(context, R.layout.ncz_areasaledata_childitem, null);
//                }
                convertView = View.inflate(context, R.layout.ncz_areasaledata_childitem, null);
                view = new Holder(convertView);
                contractTab contractTab = list.get(position);
                int percent = 0;
                float allnumber = Integer.valueOf(contractTab.getAllsaleout()) + Integer.valueOf(contractTab.getAllsalein()) + Integer.valueOf(contractTab.getAllnewsale()) + Integer.valueOf(contractTab.getAllsalefor());
                float salenumber = Integer.valueOf(contractTab.getAllsaleout()) + Integer.valueOf(contractTab.getAllsalein()) + Integer.valueOf(contractTab.getAllnewsale());
                if (allnumber != 0)
                {
                    percent = (int) ((salenumber / allnumber) * 100);
                }
                view.tv_salein.setText(list.get(position).getAllsalein());
                view.tv_saleout.setText(list.get(position).getAllsaleout());
                view.tv_salefor.setText(list.get(position).getAllsalefor());
                view.tv_allsale.setText(String.valueOf(allnumber));
                view.tv_contractname.setText(list.get(position).getContractNum());
                if (contractTab.getAllsalefor().equals("0"))
                {
                    if (salenumber != 0)
                    {
//                        view.tv_salefor.setText("售完");
                        view.tv_salefor.setText("0");
                    } else
                    {
//                        view.tv_allsale.setText("未上报");
                        view.tv_allsale.setText("0");
                    }
                }

                convertView.setTag(view);
            } else
            {
                view = (Holder) convertView.getTag();
            }


            return convertView;
        }

        private class Holder
        {
            private TextView tv_salefor;
            private TextView tv_salein;
            private TextView tv_saleout;
            private TextView tv_allsale;
            private TextView tv_contractname;

            public Holder(View view)
            {
                tv_contractname = (TextView) view.findViewById(R.id.tv_contractname);
                tv_allsale = (TextView) view.findViewById(R.id.tv_allsale);
                tv_saleout = (TextView) view.findViewById(R.id.tv_saleout);
                tv_salein = (TextView) view.findViewById(R.id.tv_salein);
                tv_salefor = (TextView) view.findViewById(R.id.tv_salefor);
            }
        }


    }
}
