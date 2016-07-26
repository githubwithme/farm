package com.farm.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.farm.R;
import com.farm.bean.ContractAssess;
import com.farm.bean.ContractAssessBean;
import com.farm.common.BitmapHelper;
import com.farm.ui.DialogFragment_AddNewAssess;
import com.farm.ui.DialogFragment_AddNewAssess_;
import com.farm.ui.PictureScrollFragment_DialogFragment;
import com.farm.widget.CustomDialog_EditSaleInInfo;
import com.farm.widget.CustomDialog_ListView;
import com.swipelistview.SwipeLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Adapter_NCZAssess extends BaseExpandableListAdapter
{
    CustomDialog_EditSaleInInfo customDialog_editSaleInInfo;
    int[] color;
    SwipeLayout swipeLayout;
    TextView currentTextView;
    CustomDialog_ListView customDialog_listView;
    ExpandableListView mainlistview;
    private Activity context;// 运行上下文
    int currentChildsize = 0;
    private GoodsAdapter adapter;
    List<ContractAssessBean> listData;
    ListView list;

    public Adapter_NCZAssess(Activity context, List<ContractAssessBean> listData, ExpandableListView mainlistview)
    {
        color = new int[]{R.color.bg_ask, R.color.bg_work, R.color.gray, R.color.green, R.color.bg_job, R.color.gray, R.color.green, R.color.bg_job, R.color.bg_plant, R.color.bg_text_small, R.color.bg_job, R.color.bg_plant, R.color.bg_text_small, R.color.bg_job, R.color.bg_plant, R.color.bg_text_small};
        this.mainlistview = mainlistview;
        this.listData = listData;
        this.context = context;
    }

    //得到子item需要关联的数据
    @Override
    public Object getChild(int groupPosition, int childPosition)
    {
        if (listData.get(groupPosition).getContractAssessList() == null)
        {
            return null;
        }
        return listData.get(groupPosition).getContractAssessList().get(childPosition);
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
        public ImageView image;
        public View view_bottom;
        public View view_top;
        public TextView tv_time;
        public TextView tv_assesstype;
        public TextView tv_number;
    }

    //设置子item的组件
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
    {

        List<ContractAssess> childData = listData.get(groupPosition).getContractAssessList();
        final ContractAssess contractAssess = childData.get(childPosition);
        View v = null;
        if (lmap.get(groupPosition) != null)
        {
            HashMap<Integer, View> map1 = lmap.get(groupPosition);
            v = lmap.get(groupPosition).get(childPosition);
        }
        if (v == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.adapter_nczassess_child, null);
            listItemView = new ListItemView();
            listItemView.image = (ImageView) convertView.findViewById(R.id.image);
            listItemView.view_bottom = convertView.findViewById(R.id.view_bottom);
            listItemView.view_top = convertView.findViewById(R.id.view_top);
            listItemView.tv_assesstype = (TextView) convertView.findViewById(R.id.tv_assesstype);
            listItemView.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            listItemView.tv_number = (TextView) convertView.findViewById(R.id.tv_number);
            if (childPosition == 0)
            {
                listItemView.view_top.setVisibility(View.GONE);
            }
            if (isLastChild)
            {
                listItemView.view_bottom.setVisibility(View.GONE);
            }
            convertView.setTag(listItemView);
            map.put(childPosition, convertView);
            lmap.put(groupPosition, map);
            if (isLastChild)
            {
                map = new HashMap<>();
            }

            //数据添加
            if (inflater == null)
            {

            }
            if (contractAssess.getAssessType().equals("0"))
            {
                listItemView.tv_assesstype.setText("警告");
            } else if (contractAssess.getAssessType().equals("1"))
            {
                listItemView.tv_assesstype.setText("不合格");
            } else if (contractAssess.getAssessType().equals("2"))
            {
                listItemView.tv_assesstype.setText("合格");
            }
//            if (contractAssess.getAssessDate().substring(0, contractAssess.getAssessDate().lastIndexOf(" ")).equals(utils.getToday_MMDD()))
//            {
//                listItemView.tv_time.setText("今日 " + contractAssess.getAssessDate().substring(contractAssess.getAssessDate().lastIndexOf(" "), contractAssess.getAssessDate().length() - 1));
//            } else
//            {
//                listItemView.tv_time.setText(contractAssess.getAssessDate());
//            }
            listItemView.tv_time.setText(contractAssess.getAssessDate());
            if (contractAssess.getUrls().size() != 0)
            {
                BitmapHelper.setImageViewBackground(context, listItemView.image, contractAssess.getUrls().get(0));
            }
            listItemView.image.setTag(contractAssess.getUrls());
            listItemView.image.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    List<String> urls = (List<String>) v.getTag();
                    PictureScrollFragment_DialogFragment dialog = new PictureScrollFragment_DialogFragment();
                    Bundle bundle1 = new Bundle();
                    bundle1.putStringArrayList("imgurl", (ArrayList<String>) urls);
                    dialog.setArguments(bundle1);
                    dialog.show(context.getFragmentManager(), "EditNameDialog");
                }
            });

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
//        super.onGroupExpanded(groupPosition);
    }

    @Override
    public void onGroupCollapsed(int groupPosition)
    {
//        super.onGroupCollapsed(groupPosition);
    }

    //获取当前父item下的子item的个数
    @Override
    public int getChildrenCount(int groupPosition)
    {
        if (listData.get(groupPosition).getContractAssessList().size() == 0)
        {
            return 0;
        }
        return listData.get(groupPosition).getContractAssessList().size();
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
            convertView = inflater.inflate(R.layout.adapter_nczassess_parent, null);
        }
        TextView tv_contractname = (TextView) convertView.findViewById(R.id.tv_contractname);
        Button btn_addGoods = (Button) convertView.findViewById(R.id.btn_addGoods);
        btn_addGoods.setTag("");
        btn_addGoods.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                DialogFragment_AddNewAssess dialog = new DialogFragment_AddNewAssess_();
                Bundle bundle1 = new Bundle();
                dialog.setArguments(bundle1);
                dialog.show(context.getFragmentManager(), "EditNameDialog");
//                showDialog_addNewAssess();
            }
        });
        tv_contractname.setText(listData.get(groupPosition).getContractName());
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

    public void showDialog_addNewAssess()
    {
        final View dialog_layout = LayoutInflater.from(context).inflate(R.layout.customdialog_addnewassess, null);
        customDialog_editSaleInInfo = new CustomDialog_EditSaleInInfo(context, R.style.MyDialog, dialog_layout);
        Button btn_sure = (Button) dialog_layout.findViewById(R.id.btn_sure);
        Button btn_cancle = (Button) dialog_layout.findViewById(R.id.btn_cancle);
        btn_sure.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                customDialog_editSaleInInfo.dismiss();
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