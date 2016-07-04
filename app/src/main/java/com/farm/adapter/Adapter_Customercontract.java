package com.farm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

import com.farm.R;
import com.farm.bean.AgreementBean;
import com.farm.bean.CustomerContractBean;
import com.farm.widget.CircleImageView;
import com.farm.widget.CustomDialog_ListView;
import com.swipelistview.SwipeLayout;

import java.util.HashMap;
import java.util.List;

/**
 * Created by user on 2016/4/8.
 */
public class Adapter_Customercontract extends BaseExpandableListAdapter
{
    int[] color;
    SwipeLayout swipeLayout;
    TextView currentTextView;
    CustomDialog_ListView customDialog_listView;
    ExpandableListView mainlistview;
    private Context context;// 运行上下文
    int currentChildsize = 0;
    private GoodsAdapter adapter;
    List<CustomerContractBean> listData;
    ListView list;

    public Adapter_Customercontract(Context context, List<CustomerContractBean> listData, ExpandableListView mainlistview)
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
        if (listData.get(groupPosition).getAgreementBeanlist() == null)
        {
            return null;
        }
        return listData.get(groupPosition).getAgreementBeanlist().get(childPosition);
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
        public TextView tv_name;
        public CircleImageView circle_img;
        public TextView tv_phone;
    }

    //设置子item的组件
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
    {

        List<AgreementBean> childData = listData.get(groupPosition).getAgreementBeanlist();
        final AgreementBean agreementBean = childData.get(childPosition);
        View v = null;
        if (lmap.get(groupPosition) != null)
        {
            HashMap<Integer, View> map1 = lmap.get(groupPosition);
            v = lmap.get(groupPosition).get(childPosition);
        }
        if (v == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.adapter_customercontract_child, null);
            listItemView = new ListItemView();
            listItemView.circle_img = (CircleImageView) convertView.findViewById(R.id.circle_img);
            listItemView.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            listItemView.tv_phone = (TextView) convertView.findViewById(R.id.tv_phone);
            convertView.setTag(listItemView);
            convertView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
//                    Intent intent = new Intent(context, ShowUserInfo_.class);
//                    intent.putExtra("userid", commembertab.getId());// 因为list中添加了头部,因此要去掉一个
//                    context.startActivity(intent);
                }
            });
            map.put(childPosition, convertView);
            lmap.put(groupPosition, map);
            if (isLastChild)
            {
                map = new HashMap<>();
            }

            //数据添加
            listItemView.circle_img.setImageResource(color[childPosition]);
            listItemView.tv_phone.setText("合同号：" + agreementBean.getAgreementNumber());
            listItemView.tv_name.setText(agreementBean.getYear());

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
        if (listData.get(groupPosition).getAgreementBeanlist() == null)
        {
            return 0;
        }
        return listData.get(groupPosition).getAgreementBeanlist().size();
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
            convertView = inflater.inflate(R.layout.adapter_customercontract_parent, null);
        }
        TextView tv_type = (TextView) convertView.findViewById(R.id.tv_type);
        TextView tv_number = (TextView) convertView.findViewById(R.id.tv_number);

        tv_type.setText(listData.get(groupPosition).getCustomerName());
        tv_number.setText("共" + listData.get(groupPosition).getAgreementBeanlist().size() + "份合同");
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
