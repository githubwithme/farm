package com.farm.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.farm.R;
import com.farm.app.AppContext;
import com.farm.bean.commembertab;
import com.farm.bean.jobtab;
import com.farm.ui.Common_JobDetail_Show_;

import java.util.HashMap;
import java.util.List;

/**
 * Created by ${hmj} on 2015/12/17.
 * 适配肥料选择界面的左边导航栏
 */
public class Common_CommandExecute_Adapter extends BaseAdapter
{
    String audiopath;
    private Context context;// 运行上下文
    private List<jobtab> listItems;// 数据集合
    private LayoutInflater listContainer;// 视图容器
    jobtab jobtab;
    ListItemView listItemView = null;
    commembertab commembertab;

    static class ListItemView
    {
        public TextView tv_time;
        public TextView tv_note;
        public TextView tv_pf;
        public TextView tv_jd;
    }

    public Common_CommandExecute_Adapter(Context context, List<jobtab> data)
    {
        this.context = context;
        this.listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
        this.listItems = data;
        commembertab = AppContext.getUserInfo(context);
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
        jobtab = listItems.get(position);
        // 自定义视图
        if (lmap.get(position) == null)
        {
            // 获取list_item布局文件的视图
            convertView = listContainer.inflate(R.layout.common_commanddetail_execute, null);
            listItemView = new ListItemView();
            // 获取控件对象

            listItemView.tv_jd = (TextView) convertView.findViewById(R.id.tv_jd);
            listItemView.tv_pf = (TextView) convertView.findViewById(R.id.tv_pf);
            listItemView.tv_note = (TextView) convertView.findViewById(R.id.tv_note);
            listItemView.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            // 设置控件集到convertView
            lmap.put(position, convertView);
            convertView.setTag(listItemView);
            convertView.setTag(R.id.tag_fi,position);
            convertView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Intent intent = new Intent(context, Common_JobDetail_Show_.class);
                    intent.putExtra("bean",  listItems.get(Integer.valueOf(v.getTag(R.id.tag_fi).toString())));
                    context.startActivity(intent);
                }
            });

        } else
        {
            convertView = lmap.get(position);
            listItemView = (ListItemView) convertView.getTag();
        }
        listItemView.tv_time.setText(listItems.get(position).getregDate());
        listItemView.tv_jd.setText(listItems.get(position).getjobNote());
        listItemView.tv_pf.setText(listItems.get(position).getjobNote());
        listItemView.tv_note.setText(listItems.get(position).getjobNote());
        return convertView;
    }



}
