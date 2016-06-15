package com.farm.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.GridView;

import com.farm.R;
import com.farm.adapter.PQ_GV_Adapter;
import com.farm.bean.BreakOff_New;
import com.farm.bean.Dictionary;
import com.farm.bean.Dictionary_wheel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by hasee on 2016/6/15.
 */
public class CustomDialog_BZzy extends Dialog
{
    HashMap<Integer, View> lmap = new HashMap<Integer, View>();
    CustomDialogListener cdListener;
    Context context;
//    List<Dictionary> listdata=new ArrayList<Dictionary>();
Dictionary_wheel dictionary_wheel;
    View layout;

   // Dictionary
   public CustomDialog_BZzy(Context context) {
       super(context);
   }
    public CustomDialog_BZzy(Context context, int theme)
    {
        super(context, theme);
        this.context = context;
    }
    public CustomDialog_BZzy(Context context, int theme, View layout,Dictionary_wheel dictionary_wheel, CustomDialogListener cdListener)
    {
        super(context, theme);
        this.context = context;
        this.layout = layout;
        this.dictionary_wheel = dictionary_wheel;
        this.cdListener = cdListener;
    }
    public interface CustomDialogListener
    {
        public void OnClick(Bundle bundle);
    }
   /* public class Dialog_ExpandableListViewAdapter extends BaseExpandableListAdapter
    {
        @Override
        public Object getChild(int groupPosition, int childPosition)
        {
            if (dictionary_wheel.getSecondItemName() == null)
            {
                return null;
            }
//        return listData.get(groupPosition).getBreakOffList().get(childPosition);
            return   dictionary_wheel.getSecondItemName();
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
        public View getChildView( int groupPosition,  int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
        {

            HashMap<String, String[]> childData=dictionary_wheel.getSecondItemName();
//        final BreakOff_New breakOff_new = childData.get(childPosition);
            View v = null;
*//*        if (lmap.get(groupPosition) != null)
        {
            HashMap<Integer, View> map1 = lmap.get(groupPosition);
            v = lmap.get(groupPosition).get(childPosition);
        }*//*
            if (v == null)
            {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.children_dladapter, null);
                listItemView = new ListItemView();
                listItemView.gridView = (GridView) convertView.findViewById(R.id.gridView);

                map.put(childPosition, convertView);
                lmap.put(groupPosition, map);
                if (isLastChild)
                {
                    map = new HashMap<>();
                }

                //数据添加
//            listItemView.goodsname.setText(wz_rKxx.getGoodsname());
                pq_gv_adapter = new PQ_GV_Adapter(context, childData);
                listItemView.gridView.setAdapter(pq_gv_adapter);

            } else
            {
                convertView = lmap.get(groupPosition).get(childPosition);
                listItemView = (ListItemView) convertView.getTag();
            }
            //数据添加  都可以数据加载，不过在上面比较好，这里是返回view



            return convertView;
        }
    }*/
    static class ListItemView
    {
        public GridView gridView;
    }
}
