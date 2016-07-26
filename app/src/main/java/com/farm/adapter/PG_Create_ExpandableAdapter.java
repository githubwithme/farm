package com.farm.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.farm.R;
import com.farm.app.AppContext;
import com.farm.bean.SellOrderDetail_New;
import com.farm.bean.areatab;
import com.farm.bean.contractTab;
import com.farm.common.utils;
import com.farm.widget.CustomDialog_EditSaleInInfo;
import com.farm.widget.CustomDialog_ListView;
import com.farm.widget.CustomListView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by hasee on 2016/7/5.
 */
public class PG_Create_ExpandableAdapter extends BaseExpandableListAdapter
{
    GridViewAdapter_SellOrDetail_NCZ gridViewAdapter_sellOrDetail_ncz;
    TextView currentTextView;
    CustomDialog_ListView customDialog_listView;
    ExpandableListView mainlistview;
    private Context context;// 运行上下文
    int currentChildsize = 0;
    private GoodsAdapter adapter;
    List<areatab> listData;
    ListView list;
    EditText et_number;
    CustomDialog_EditSaleInInfo customDialog_editSaleInInfo;

    public PG_Create_ExpandableAdapter(Context context, List<areatab> listData, ExpandableListView mainlistview)
    {
        this.mainlistview = mainlistview;
        this.listData = listData;
        this.context = context;
    }

    //得到子item需要关联的数据
    @Override
    public Object getChild(int groupPosition, int childPosition)
    {
        if (listData.get(groupPosition).getAreatabList() == null)
        {
            return null;
        }
        return listData.get(groupPosition).getAreatabList().get(childPosition);
    }

    static class ListItemView
    {
        public TextView tv_areaname;
        public TextView tv_number;
        public Button btn_number;
        public CheckBox cb_selectall;
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
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
    {

        final List<SellOrderDetail_New> childData = listData.get(groupPosition).getAreatabList();

        final SellOrderDetail_New sellOrderDetail_new = childData.get(childPosition);

        View v = null;
        if (lmap.get(groupPosition) != null)
        {
            HashMap<Integer, View> map1 = lmap.get(groupPosition);
            v = lmap.get(groupPosition).get(childPosition);
        }
        if (v == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.adapter_selectproduct_childitem, null);//ncz_pc_todayjobadater-ncz_pc_todayjobadater
            listItemView = new ListItemView();

            // 获取控件对象
            listItemView.tv_areaname = (TextView) convertView.findViewById(R.id.tv_areaname);
            listItemView.tv_number = (TextView) convertView.findViewById(R.id.tv_number);
            listItemView.btn_number = (Button) convertView.findViewById(R.id.btn_number);
            listItemView.cb_selectall = (CheckBox) convertView.findViewById(R.id.cb_selectall);
            listItemView.btn_number.setTag(R.id.tag_checkbox, listItemView.cb_selectall);

            listItemView.btn_number.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    showDialog_editBreakoffinfo(childData.get(childPosition), (Button) view);
                }
            });


            listItemView.cb_selectall.setTag(R.id.tag_cash, listItemView.btn_number);
            listItemView.cb_selectall.setTag(R.id.tag_rk, childPosition);
            listItemView.cb_selectall.setTag(R.id.tag_danxuan, Integer.valueOf(sellOrderDetail_new.getplannumber()));
            listItemView.cb_selectall.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b)
                {
                    int pos = (int) compoundButton.getTag(R.id.tag_rk);
                    int salefornumber = (int) compoundButton.getTag(R.id.tag_danxuan);
                    Button btn_number = (Button) compoundButton.getTag(R.id.tag_cash);
                    if (b)
                    {
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("bean", childData.get(pos));
                        compoundButton.setTag(R.id.tag_view, bundle);
                    } else
                    {
                        compoundButton.setTag(R.id.tag_view, null);
                        btn_number.setText(String.valueOf(salefornumber));
                    }
                    Intent intent = new Intent();
                    intent.setAction(AppContext.BROADCAST_UPDATESALENUMBER);
                    context.sendBroadcast(intent);
                }
            });
            map.put(childPosition, convertView);
            lmap.put(groupPosition, map);
            if (isLastChild)
            {
                map = new HashMap<>();
            }
            listItemView.tv_areaname.setText(sellOrderDetail_new.getcontractname());
            listItemView.tv_number.setText(sellOrderDetail_new.getplannumber());
            listItemView.btn_number.setText(sellOrderDetail_new.getplannumber());
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
        if (listData.get(groupPosition).getAreatabList() == null)
        {
            return 0;
        }
        return listData.get(groupPosition).getAreatabList().size();
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
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);//0
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_prents_pg_create, null);
        }
        TextView batchtime = (TextView) convertView.findViewById(R.id.batchtime);
        TextView tv_areaname = (TextView) convertView.findViewById(R.id.tv_areaname);
        TextView tv_number = (TextView) convertView.findViewById(R.id.tv_number);

        batchtime.setText(listData.get(groupPosition).getBatchtime());
//        tv_areaname.setText(listData.get(groupPosition).getareaName());
        int num = 0;
        for (int i = 0; i < listData.get(groupPosition).getAreatabList().size(); i++)
        {
            num += Integer.valueOf(listData.get(groupPosition).getAreatabList().get(i).getplannumber());
        }
        tv_number.setText(num + "");
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


    public void showDialog_editBreakoffinfo(final SellOrderDetail_New sellOrderDetail_new, final Button button)
    {
        final View dialog_layout = LayoutInflater.from(context).inflate(R.layout.customdialog_editcontractsale, null);
        customDialog_editSaleInInfo = new CustomDialog_EditSaleInInfo(context, R.style.MyDialog, dialog_layout);
        et_number = (EditText) dialog_layout.findViewById(R.id.et_number);
        et_number.setText(sellOrderDetail_new.getplannumber());
        Button btn_sure = (Button) dialog_layout.findViewById(R.id.btn_sure);
        Button btn_cancle = (Button) dialog_layout.findViewById(R.id.btn_cancle);
        btn_sure.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                int leftnumber = Integer.valueOf(sellOrderDetail_new.getplannumber()) - Integer.valueOf(et_number.getText().toString());
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
