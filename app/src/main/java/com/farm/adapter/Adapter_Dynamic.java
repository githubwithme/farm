package com.farm.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.farm.R;
import com.farm.bean.DynamicBean;
import com.farm.common.utils;
import com.farm.ui.NCZ_BreakOffActivity_;
import com.farm.ui.NCZ_CommandListActivity_;
import com.farm.ui.NCZ_DLdatail_;
import com.farm.ui.NCZ_JobActivity_;
import com.farm.ui.NCZ_MQActivity_;
import com.farm.ui.NCZ_OrderManager_;
import com.farm.ui.NCZ_SJActivity_;
import com.farm.ui.Ncz_wz_ll_;
import com.farm.widget.CircleImageView;

import java.util.HashMap;
import java.util.List;

@SuppressLint("NewApi")
public class Adapter_Dynamic extends BaseAdapter
{
    private Context context;// 运行上下文
    private List<DynamicBean> listItems;// 数据集合
    private LayoutInflater listContainer;// 视图容器
    DynamicBean dynamicBean;

    static class ListItemView
    {
        public TextView tv_note;
        public FrameLayout fl_new_item;
        public TextView tv_newnumber;
        public TextView tv_date;
        public TextView tv_new_item;
        public TextView tv_title;
        public CircleImageView circle_img;
        public LinearLayout ll_select;
    }

    public Adapter_Dynamic(Context context, List<DynamicBean> data)
    {
        this.context = context;
        this.listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
        this.listItems = data;
    }

    public int getCount()
    {
        return listItems.size();
    }

    public Object getItem(int arg0)
    {
        return null;
    }

    public long getItemId(int arg0)
    {
        return 0;
    }

    HashMap<Integer, View> lmap = new HashMap<Integer, View>();

    public View getView(int position, View convertView, ViewGroup parent)
    {
        dynamicBean = listItems.get(position);
        // 自定义视图
        ListItemView listItemView = null;
        if (lmap.get(position) == null)
        {
            // 获取list_item布局文件的视图
            convertView = listContainer.inflate(R.layout.adapter_dynamic, null);
            listItemView = new ListItemView();
            // 获取控件对象
            listItemView.tv_note = (TextView) convertView.findViewById(R.id.tv_note);
            listItemView.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
            listItemView.tv_new_item = (TextView) convertView.findViewById(R.id.tv_new_item);
            listItemView.fl_new_item = (FrameLayout) convertView.findViewById(R.id.fl_new_item);
            listItemView.tv_newnumber = (TextView) convertView.findViewById(R.id.tv_newnumber);
            listItemView.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            listItemView.circle_img = (CircleImageView) convertView.findViewById(R.id.circle_img);
            listItemView.ll_select = (LinearLayout) convertView.findViewById(R.id.ll_select);
            // 设置控件集到convertView
            lmap.put(position, convertView);
            convertView.setTag(listItemView);

        } else
        {
            convertView = lmap.get(position);
            listItemView = (ListItemView) convertView.getTag();
        }
//        listItemView.tv_new_item.setText(String.valueOf(dynamicBean.getListdata().size()));
        if (dynamicBean.getListdata().size() > 0)
        {
            String date = dynamicBean.getListdata().get(0).getDate();
            date = date.replace("/", "-");
            listItemView.tv_date.setText(utils.OffSetOfDate(utils.getToday(), date));
        } else
        {
            listItemView.tv_date.setText("");
        }
        String type = dynamicBean.getType();
        if (type.equals("ZL"))
        {
            listItemView.tv_title.setText("指令");
            if (dynamicBean.getListdata().size() > 0)
            {
                listItemView.tv_note.setText(dynamicBean.getListdata().get(0).getNote());
            } else
            {
                listItemView.tv_note.setText("暂无新内容");
            }

            listItemView.circle_img.setBackgroundResource(R.drawable.icon_instruction);
        } else if (type.equals("GZ"))
        {
            listItemView.tv_title.setText("工作");
            if (dynamicBean.getListdata().size() > 0)
            {
                listItemView.tv_note.setText(dynamicBean.getListdata().get(0).getNote());
            } else
            {
                listItemView.tv_note.setText("无");
            }
            listItemView.circle_img.setBackgroundResource(R.drawable.icon_instruction);
        } else if (type.equals("MQ"))
        {
            listItemView.tv_title.setText("苗情");
//            listItemView.tv_note.setText(dynamicBean.getListdata().get(0).getNote());
            if (dynamicBean.getListdata().size() > 0)
            {
                listItemView.tv_note.setText(dynamicBean.getListdata().get(0).getNote());
            } else
            {
                listItemView.tv_note.setText("无");
            }
            listItemView.circle_img.setBackgroundResource(R.drawable.icon_seedlings);
        } else if (type.equals("XS"))
        {
            listItemView.tv_title.setText("销售");
//            listItemView.tv_note.setText(dynamicBean.getListdata().get(0).getNote());
            if (dynamicBean.getListdata().size() > 0)
            {
                listItemView.tv_note.setText(dynamicBean.getListdata().get(0).getNote());
            } else
            {
                listItemView.tv_note.setText("无");
            }
            listItemView.circle_img.setBackgroundResource(R.drawable.icon_sales_management);
        } else if (type.equals("KC"))
        {
            listItemView.tv_title.setText("物资");
//            listItemView.tv_note.setText(dynamicBean.getListdata().get(0).getNote());
            if (dynamicBean.getListdata().size() > 0)
            {
                listItemView.tv_note.setText(dynamicBean.getListdata().get(0).getNote());
            } else
            {
                listItemView.tv_note.setText("无");
            }
            listItemView.circle_img.setBackgroundResource(R.drawable.icon_warehouse_supplies);
        } else if (type.equals("SP"))
        {
            listItemView.tv_title.setText("审批");
//            listItemView.tv_note.setText(dynamicBean.getListdata().get(0).getNote());
            if (dynamicBean.getListdata().size() > 0)
            {
                listItemView.tv_note.setText(dynamicBean.getListdata().get(0).getNote());
            } else
            {
                listItemView.tv_note.setText("无");
            }
            listItemView.circle_img.setBackgroundResource(R.drawable.newicon_money);
        } else if (type.equals("SJ"))
        {
            listItemView.tv_title.setText("事件");
//            listItemView.tv_note.setText(dynamicBean.getListdata().get(0).getNote());
            if (dynamicBean.getListdata().size() > 0)
            {
                listItemView.tv_note.setText(dynamicBean.getListdata().get(0).getNote());
            } else
            {
                listItemView.tv_note.setText("无");
            }
            listItemView.circle_img.setBackgroundResource(R.drawable.icon_emergencies);
        } else if (type.equals("DL"))
        {
            listItemView.tv_title.setText("断蕾");
//            listItemView.tv_note.setText(dynamicBean.getListdata().get(0).getNote());
            if (dynamicBean.getListdata().size() > 0)
            {
                listItemView.tv_note.setText(dynamicBean.getListdata().get(0).getNote());
            } else
            {
                listItemView.tv_note.setText("无");
            }
            listItemView.circle_img.setBackgroundResource(R.drawable.icon_bud_break);
        }

        if (dynamicBean.getListdata().size() > 0)
        {
            if (dynamicBean.getListdata().get(0).getFlashStr().equals("0"))
            {
                listItemView.fl_new_item.setVisibility(View.GONE);
            } else
            {
                listItemView.fl_new_item.setVisibility(View.VISIBLE);
            }
        }

        listItemView.ll_select.setTag(R.id.tag_dt, dynamicBean);
        listItemView.ll_select.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                DynamicBean dynamicBean1 = (DynamicBean) view.getTag(R.id.tag_dt);
                Intent intent = null;
                String type = dynamicBean1.getType();
                if (type.equals("ZL"))
                {
                    intent = new Intent(context, NCZ_CommandListActivity_.class);
                    return;
                } else if (type.equals("GZ"))
                {
                    intent = new Intent(context, NCZ_JobActivity_.class);
                    return;
                } else if (type.equals("MQ"))
                {
                    intent = new Intent(context, NCZ_MQActivity_.class);
                    return;
                } else if (type.equals("XS"))
                {
//                                intent = new Intent(getActivity(), NCZ_FarmSale_.class);
                    intent = new Intent(context, NCZ_OrderManager_.class);
                } else if (type.equals("KC"))
                {
                    intent = new Intent(context, Ncz_wz_ll_.class);
                    return;
                } else if (type.equals("SP"))
                {
                    intent = new Intent(context, NCZ_CommandListActivity_.class);
                    return;
                } else if (type.equals("SJ"))
                {
                    intent = new Intent(context, NCZ_SJActivity_.class);
                    return;
                } else if (type.equals("DL"))
                {
                    intent = new Intent(context, NCZ_BreakOffActivity_.class);
                }
                context.startActivity(intent);


//

            }
        });
        return convertView;
    }


}