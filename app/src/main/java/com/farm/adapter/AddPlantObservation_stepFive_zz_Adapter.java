package com.farm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.farm.R;
import com.farm.bean.Dictionary;
import com.farm.bean.FJ_SCFJ;
import com.farm.bean.goodslisttab;
import com.farm.bean.plantgrowthtab;
import com.farm.common.BitmapHelper;
import com.farm.widget.CustomDialog_ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ${hmj} on 2015/12/17.
 * 适配肥料选择界面的左边导航栏
 */
public class AddPlantObservation_stepFive_zz_Adapter extends BaseExpandableListAdapter
{
    plantgrowthtab plantgrowthtab;
    TextView currentTextView;
    CustomDialog_ListView customDialog_listView;
    private int currentItem = 0;
    List<goodslisttab> list_goods = new ArrayList<goodslisttab>();
    Dictionary tempDic = new Dictionary();
    private Context context;// 运行上下文
    ListView list;
    ExpandableListView expanded_zz;
    List<plantgrowthtab> list_plantgrowthtab = new ArrayList<>();
    List<FJ_SCFJ> list_fj_scfj = new ArrayList<>();
    List<String> parentData = new ArrayList<>();
    List<List<plantgrowthtab>> secondItemName = new ArrayList<>();

    public AddPlantObservation_stepFive_zz_Adapter(Context context, ExpandableListView expanded_zz, List<plantgrowthtab> list_plantgrowthtab, List<FJ_SCFJ> list_fj_scfj)
    {
        this.expanded_zz = expanded_zz;
        this.list_plantgrowthtab = list_plantgrowthtab;
        this.list_fj_scfj = list_fj_scfj;
        this.context = context;

        for (int i = 0; i < list_plantgrowthtab.size(); i++)
        {
            parentData.add(list_plantgrowthtab.get(i).getplantName());
            List<plantgrowthtab> list = new ArrayList<>();
            list.add(list_plantgrowthtab.get(i));
            secondItemName.add(list);
        }
    }

    //得到子item需要关联的数据
    @Override
    public Object getChild(int groupPosition, int childPosition)
    {
        return secondItemName.get(groupPosition).get(childPosition);
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
        public LinearLayout ll_picture;
        public TextView tv_zg;
        public TextView tv_wj;
        public TextView tv_ys;
        public TextView tv_lys;
        public CheckBox cb_sfyz;
        public CheckBox cb_sfly;
        public CheckBox cb_sfcl;
    }

    //设置子item的组件
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
    {
        View v = null;
        if (lmap.get(groupPosition) != null)
        {
            HashMap<Integer, View> map1 = lmap.get(groupPosition);
            v = lmap.get(groupPosition).get(childPosition);
        }
        if (v == null)
        {
            plantgrowthtab = list_plantgrowthtab.get(groupPosition);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.layout_children_zz, null);
            listItemView = new ListItemView();
            listItemView.ll_picture = (LinearLayout) convertView.findViewById(R.id.ll_picture);
            listItemView.tv_zg = (TextView) convertView.findViewById(R.id.tv_zg);
            listItemView.tv_wj = (TextView) convertView.findViewById(R.id.tv_wj);
            listItemView.tv_ys = (TextView) convertView.findViewById(R.id.tv_ys);
            listItemView.tv_lys = (TextView) convertView.findViewById(R.id.tv_lys);
            listItemView.cb_sfly = (CheckBox) convertView.findViewById(R.id.cb_sfly);
            listItemView.cb_sfcl = (CheckBox) convertView.findViewById(R.id.cb_sfcl);
            listItemView.cb_sfyz = (CheckBox) convertView.findViewById(R.id.cb_sfyz);


            convertView.setTag(listItemView);
            map.put(childPosition, convertView);
            lmap.put(groupPosition, map);
            if (isLastChild)
            {
                map = new HashMap<>();
            }

            listItemView.tv_zg.setText(plantgrowthtab.gethNum());
            listItemView.tv_wj.setText(plantgrowthtab.getwNum());
            listItemView.tv_ys.setText(plantgrowthtab.getyNum());
            listItemView.tv_lys.setText(plantgrowthtab.getxNum());
            if (plantgrowthtab.getplantType().equals("1"))
            {
                listItemView.cb_sfyz.setChecked(true);
            } else
            {
                listItemView.cb_sfyz.setChecked(false);
            }
            if (plantgrowthtab.getcDate().equals("1"))
            {
                listItemView.cb_sfcl.setChecked(true);
            } else
            {
                listItemView.cb_sfcl.setChecked(false);
            }
            if (plantgrowthtab.getzDate().equals("1"))
            {
                listItemView.cb_sfly.setChecked(true);
            } else
            {
                listItemView.cb_sfly.setChecked(false);
            }
            for (int i = 0; i < list_fj_scfj.size(); i++)
            {
                if (list_fj_scfj.get(i).getFJID().equals(plantgrowthtab.getplantId()))
                {
                    ImageView imageView = new ImageView(context);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(180, ViewGroup.LayoutParams.MATCH_PARENT, 0);
                    lp.setMargins(25, 4, 0, 4);
                    imageView.setLayoutParams(lp);
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    BitmapHelper.setImageView(context, imageView, list_fj_scfj.get(i).getFJBDLJ());
                    listItemView.ll_picture.addView(imageView);
                }

            }
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
//        for (int i = 0, cnt = getGroupCount(); i < cnt; i++)
//        {
//            if (groupPosition != i && expanded_zz.isGroupExpanded(i))
//            {
//                expanded_zz.collapseGroup(i);
//            }
//        }
//        for (int i = 0; i < firstItemName.size(); i++)
//        {
//            mainlistview.expandGroup(i);
//        }
//        mainlistview.expandGroup(groupPosition);
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
        return secondItemName.get(groupPosition).size();
    }

    //获取当前父item的数据
    @Override
    public Object getGroup(int groupPosition)
    {
        return parentData.get(groupPosition);
    }

    @Override
    public int getGroupCount()
    {
        return parentData.size();
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
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.layout_parent_zz, null);
        }
        TextView tv = (TextView) convertView.findViewById(R.id.parent_textview);

        tv.setTag(groupPosition);
        tv.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (expanded_zz.isGroupExpanded(Integer.valueOf(v.getTag().toString())))
                {
                    expanded_zz.collapseGroup(Integer.valueOf(v.getTag().toString()));
                } else
                {
                    int aa = Integer.valueOf(v.getTag().toString());
                    expanded_zz.expandGroup(Integer.valueOf(v.getTag().toString()));
                }
            }
        });
        tv.setText(parentData.get(groupPosition));
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
