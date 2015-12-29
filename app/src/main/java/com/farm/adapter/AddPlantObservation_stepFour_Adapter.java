package com.farm.adapter;

import android.content.Context;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

import com.farm.R;
import com.farm.bean.Dictionary;
import com.farm.bean.Dictionary_wheel;
import com.farm.bean.goodslisttab;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${hmj} on 2015/12/17.
 * 适配肥料选择界面的左边导航栏
 */
public class AddPlantObservation_stepFour_Adapter extends BaseExpandableListAdapter
{
    TextView tempParentView;
    TextView tempChildView;
    private int currentItem = 0;
    List<goodslisttab> list_goods = new ArrayList<goodslisttab>();
    Dictionary_wheel dictionary_wheel;
    ExpandableListView mainlistview;
    private Context context;// 运行上下文
    //    String[] parentData = null;
//    String[] parentId = null;
//    HashMap<String, String[]> map = null;
//    HashMap<String, String[]> map_id = null;
    List<String> firstItemName;
    List<String> firstItemID;
    List<List<String>> secondItemName;
    List<List<String>> secondItemID;
    List<List<List<String>>> ThirdItemName;
    List<List<List<String>>> ThirdItemID;
    int currentChildsize = 0;
    private GoodsAdapter adapter;
    ListView list;
    String currentParentId = "";
    String currentParentName = "";
    String currentChildId = "";
    String currentChildName = "";

    public AddPlantObservation_stepFour_Adapter(Context context, Dictionary dictionary, ExpandableListView mainlistview)
    {
        this.mainlistview = mainlistview;
        this.context = context;
        firstItemName = dictionary.getFirstItemName();
        secondItemName = dictionary.getSecondItemName();
        for (int i = 0; i < firstItemName.size(); i++)
        {
            this.mainlistview.expandGroup(i);
        }
    }

    //得到子item需要关联的数据
    @Override
    public Object getChild(int groupPosition, int childPosition)
    {
        String key = firstItemName.get(groupPosition);
        return secondItemName.get(groupPosition).get(childPosition);
//        return (map.get(key)[childPosition]);
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
        String key = firstItemName.get(groupPosition);
        List<String> childData = secondItemName.get(groupPosition);
        String info = childData.get(childPosition);
        if (convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.layout_children_plantobservation, null);
        }
        TextView tv = (TextView) convertView.findViewById(R.id.second_textview);
        if (groupPosition == 0 && childPosition == 0)
        {
//            TextView textView = (TextView) parent.getChildAt(0);
//            textView.setTextColor(0xFFFF5D5E);
//            TextPaint tp = textView.getPaint();
//            tp.setFakeBoldText(true);

            tv.setTextColor(0xFFFF5D5E);
            TextPaint tp = tv.getPaint();
            tp.setFakeBoldText(true);
//            getGoodslist();
            tempChildView = tv;

        }
        tv.setText(info);
        tv.setTag(R.id.tag_fi, firstItemName.get(groupPosition));
        tv.setTag(R.id.tag_fn, key);
        tv.setTag(R.id.tag_si, secondItemName.get(groupPosition).get(childPosition));
        tv.setTag(R.id.tag_sn, info);
        tv.setTag(R.id.tag_childsize, childData.size());
        tv.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                if (tempChildView != null)
                {
                    tempChildView.setTextColor(context.getResources().getColor(R.color.bg_text));
                    TextPaint tp = tempChildView.getPaint();
                    tp.setFakeBoldText(false);
                }

                TextView textView = (TextView) v;
                textView.setTextColor(0xFFFF5D5E);
                TextPaint tp = textView.getPaint();
                tp.setFakeBoldText(true);
                tempChildView = textView;
                currentParentId = (String) v.getTag(R.id.tag_fi);
                currentParentName = (String) v.getTag(R.id.tag_fn);
                currentChildId = (String) v.getTag(R.id.tag_si);
                currentChildName = (String) v.getTag(R.id.tag_sn);
                currentChildsize = (Integer) v.getTag(R.id.tag_childsize);
//                getGoodslist();
            }
        });
        return convertView;
    }

    @Override
    public void onGroupExpanded(int groupPosition)
    {
        // mExpListView 是列表实例，通过判断它的状态，关闭已经展开的。
        for (int i = 0, cnt = getGroupCount(); i < cnt; i++)
        {
            if (groupPosition != i && mainlistview.isGroupExpanded(i))
            {
                mainlistview.collapseGroup(i);
            }
        }
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
        String key = firstItemName.get(groupPosition);
        int size = secondItemName.get(groupPosition).size();
        return size;
    }

    //获取当前父item的数据
    @Override
    public Object getGroup(int groupPosition)
    {
        return firstItemName.get(groupPosition);
    }

    @Override
    public int getGroupCount()
    {
        return firstItemName.size();
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
            convertView = inflater.inflate(R.layout.layout_parent_plantobservation, null);
        }
        TextView tv = (TextView) convertView.findViewById(R.id.parent_textview);

        tv.setTag(groupPosition);
        tv.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mainlistview.expandGroup(Integer.valueOf(v.getTag().toString()));
                if (tempParentView != null)
                {
                    tempParentView.setTextColor(context.getResources().getColor(R.color.bg_text));
                    TextPaint tp = tempParentView.getPaint();
                    tp.setFakeBoldText(false);
                }

                TextView textView = (TextView) v;
                textView.setTextColor(0xFFFF5D5E);
                TextPaint tp = textView.getPaint();
                tp.setFakeBoldText(true);
                tempParentView = textView;
            }
        });
        tv.setText(firstItemName.get(groupPosition));
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

//    public void showDialog()
//    {
//        View dialog_layout = (RelativeLayout) getActivity().getLayoutInflater().inflate(R.layout.customdialog_listview,null);
//        customDialog_listView = new CustomDialog_ListView(getActivity(),R.style.MyDialog,dialog_layout,dic_comm.getFirstItemName(),dic_comm.getFirstItemID(),new CustomDialog_ListView.CustomDialogListener()
//        {
//            @Override
//            public void OnClick(Bundle bundle)
//            {
//                if (from.equals("jj_gjd"))
//                {
//                    jj_gjd_id = bundle.getString("id");
//                    jj_gjd = bundle.getString("name");
//                    tv_jj_gjd.setText(jj_gjd);
//                } else if (from == "jj_gz")
//                {
//                    jj_gz_id = bundle.getString("id");
//                    jj_gz = bundle.getString("name");
//                    tv_jj_gz.setText(jj_gz);
//                } else if (from == "")
//                {
//
//                } else if (from == "")
//                {
//
//                } else if (from == "")
//                {
//
//                } else if (from == "")
//                {
//
//                }
//            }
//        });
//        customDialog_listView.show();
//    }

}
