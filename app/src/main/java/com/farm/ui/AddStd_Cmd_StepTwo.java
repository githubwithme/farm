package com.farm.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.farm.R;
import com.farm.com.custominterface.FragmentCallBack;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ${hmj} on 2015/12/15.
 */
@EFragment
public class AddStd_Cmd_StepTwo extends Fragment
{
    List<String> parent = null;
    Map<String, List<String>> map = null;
    FragmentCallBack fragmentCallBack = null;
    @ViewById
    ExpandableListView mainlistview ;


    @AfterViews
    void afterOncreate()
    {
        initData();
        mainlistview.setAdapter(new MyAdapter());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.add_std__cmd__step_two, container, false);
        return rootView;
    }

    // 初始化数据
    public void initData()
    {
        parent = new ArrayList<String>();
        parent.add("parent1");
        parent.add("parent2");
        parent.add("parent3");

        map = new HashMap<String, List<String>>();

        List<String> list1 = new ArrayList<String>();
        list1.add("child1-1");
        list1.add("child1-2");
        list1.add("child1-3");
        map.put("parent1", list1);

        List<String> list2 = new ArrayList<String>();
        list2.add("child2-1");
        list2.add("child2-2");
        list2.add("child2-3");
        map.put("parent2", list2);

        List<String> list3 = new ArrayList<String>();
        list3.add("child3-1");
        list3.add("child3-2");
        list3.add("child3-3");
        map.put("parent3", list3);

    }

    class MyAdapter extends BaseExpandableListAdapter
    {

        //得到子item需要关联的数据
        @Override
        public Object getChild(int groupPosition, int childPosition)
        {
            String key = parent.get(groupPosition);
            return (map.get(key).get(childPosition));
        }

        //得到子item的ID
        @Override
        public long getChildId(int groupPosition, int childPosition)
        {
            return childPosition;
        }

        //设置子item的组件
        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
        {
            String key = AddStd_Cmd_StepTwo.this.parent.get(groupPosition);
            String info = map.get(key).get(childPosition);
            if (convertView == null)
            {
                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.layout_children_goods, null);
            }
            TextView tv = (TextView) convertView.findViewById(R.id.second_textview);
            tv.setText(info);
            return convertView;
        }

        //获取当前父item下的子item的个数
        @Override
        public int getChildrenCount(int groupPosition)
        {
            String key = parent.get(groupPosition);
            int size = map.get(key).size();
            return size;
        }

        //获取当前父item的数据
        @Override
        public Object getGroup(int groupPosition)
        {
            return parent.get(groupPosition);
        }

        @Override
        public int getGroupCount()
        {
            return parent.size();
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
                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.layout_parent_goods, null);
            }
            TextView tv = (TextView) convertView.findViewById(R.id.parent_textview);
            tv.setText(AddStd_Cmd_StepTwo.this.parent.get(groupPosition));
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

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        fragmentCallBack = (AddStd_Cmd) activity;
    }

}
