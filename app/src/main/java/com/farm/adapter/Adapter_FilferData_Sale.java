package com.farm.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.farm.R;
import com.farm.app.AppContext;
import com.farm.bean.FilferBean_Sale;
import com.farm.bean.contractTab;
import com.farm.common.utils;
import com.farm.widget.CustomDialog_EditSaleInInfo;
import com.farm.widget.CustomDialog_ListView;
import com.farm.widget.CustomGridview;

import java.util.HashMap;
import java.util.List;

/**
 * Created by user on 2016/4/25.
 */
public class Adapter_FilferData_Sale extends BaseExpandableListAdapter
{
    GridViewAdapter_SellOrDetail_NCZ gridViewAdapter_sellOrDetail_ncz;
    TextView currentTextView;
    CustomDialog_ListView customDialog_listView;
    private int currentItem = 0;
    ExpandableListView mainlistview;
    private Context context;// 运行上下文
    int currentChildsize = 0;
    private GoodsAdapter adapter;
    List<FilferBean_Sale> listData;
    ListView list;

    public Adapter_FilferData_Sale(Context context, List<FilferBean_Sale> listData, ExpandableListView mainlistview)
    {
        this.mainlistview = mainlistview;
        this.listData = listData;
        this.context = context;
    }

    //得到子item需要关联的数据
    @Override
    public Object getChild(int groupPosition, int childPosition)
    {
        if (listData.get(groupPosition).getListdata() == null)
        {
            return null;
        }
        return listData.get(groupPosition).getListdata().get(childPosition);
    }

    static class ListItemView
    {
        public CustomGridview gv;

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

        List<String> childData = listData.get(groupPosition).getListdata();

        View v = null;
        if (lmap.get(groupPosition) != null)
        {
            HashMap<Integer, View> map1 = lmap.get(groupPosition);
            v = lmap.get(groupPosition).get(childPosition);
        }
        if (v == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.adapter_salefilferdata_child, null);//ncz_pc_todayjobadater-ncz_pc_todayjobadater
            listItemView = new ListItemView();

            // 获取控件对象
            listItemView.gv = (CustomGridview) convertView.findViewById(R.id.gv);
            map.put(childPosition, convertView);
            lmap.put(groupPosition, map);
            if (isLastChild)
            {
                map = new HashMap<>();
            }
            gridViewAdapter_sellOrDetail_ncz = new GridViewAdapter_SellOrDetail_NCZ(context, childData);
            listItemView.gv.setAdapter(gridViewAdapter_sellOrDetail_ncz);
            utils.setGridViewHeight(listItemView.gv);
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
        if (listData.get(groupPosition).getListdata() == null)
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
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_salefilferdata_parent, null);
        }
        TextView tv_type = (TextView) convertView.findViewById(R.id.tv_type);
        tv_type.setText(listData.get(groupPosition).getType());
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
        List<String> list;
        EditText et_number;
        CustomDialog_EditSaleInInfo customDialog_editSaleInInfo;
        private Context context;
        Holder view;

        public GridViewAdapter_SellOrDetail_NCZ(Context context, List<String> list)
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
                convertView = View.inflate(context, R.layout.adapter_salefilferdata_gvitem, null);
                view = new Holder(convertView);
                view.tv_data.setText(list.get(position));
                convertView.setTag(view);
            } else
            {
                view = (Holder) convertView.getTag();
            }

            return convertView;
        }

        private class Holder
        {
            private TextView tv_data;

            public Holder(View view)
            {
                tv_data = (TextView) view.findViewById(R.id.tv_data);
            }
        }

        public void showDialog_editBreakoffinfo(final contractTab contractTab, final Button button)
        {
            final View dialog_layout = LayoutInflater.from(context).inflate(R.layout.customdialog_editcontractsale, null);
            customDialog_editSaleInInfo = new CustomDialog_EditSaleInInfo(context, R.style.MyDialog, dialog_layout);
            et_number = (EditText) dialog_layout.findViewById(R.id.et_number);
            et_number.setText(contractTab.getAllsalefor());
            Button btn_sure = (Button) dialog_layout.findViewById(R.id.btn_sure);
            Button btn_cancle = (Button) dialog_layout.findViewById(R.id.btn_cancle);
            btn_sure.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    int leftnumber = Integer.valueOf(contractTab.getAllsalefor()) - Integer.valueOf(et_number.getText().toString());
                    if (leftnumber < 0)
                    {
                        Toast.makeText(context, "剩余量不足", Toast.LENGTH_SHORT).show();
                    } else
                    {
                        customDialog_editSaleInInfo.dismiss();
                        button.setText(et_number.getText().toString());
                        CheckBox checkBox = (CheckBox) button.getTag(R.id.tag_checkbox);
                        if (checkBox.isChecked())
                        {
                            Intent intent = new Intent();
                            intent.setAction(AppContext.BROADCAST_UPDATESALENUMBER);
                            context.sendBroadcast(intent);
                        } else
                        {
                            checkBox.setChecked(true);
                        }

                    }

                }
            });
            btn_cancle.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    customDialog_editSaleInInfo.dismiss();
                }
            });
            customDialog_editSaleInInfo.show();
        }


    }
}
