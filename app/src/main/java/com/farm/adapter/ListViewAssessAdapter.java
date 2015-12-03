package com.farm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.farm.R;
import com.farm.bean.plantgrowthtab;

import java.util.HashMap;
import java.util.List;

public class ListViewAssessAdapter extends BaseAdapter
{
	private Context context;// 运行上下文
	private List<plantgrowthtab> listItems;// 数据集合
	private LayoutInflater listContainer;// 视图容器
	plantgrowthtab plantgrowthtab;

	static class ListItemView
	{
		public TextView tv_cmdname;
		public TextView tv_state;
		public TextView tv_yq;
		public TextView tv_pq;
		public TextView tv_bzzl;
		public TextView tv_zfx;
		public TextView tv_time;
		public TextView tv_day;
	}

	public ListViewAssessAdapter(Context context, List<plantgrowthtab> data)
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
		plantgrowthtab = listItems.get(position);
		// 自定义视图
		ListItemView listItemView = null;
		if (lmap.get(position) == null)
		{
			// 获取list_item布局文件的视图
			convertView = listContainer.inflate(R.layout.listitem_assess, null);
			listItemView = new ListItemView();
			// 获取控件对象
			// listItemView.tv_cmdname = (TextView)
			// convertView.findViewById(R.id.tv_cmdname);
			// listItemView.tv_state = (TextView)
			// convertView.findViewById(R.id.tv_state);
			// listItemView.tv_yq = (TextView)
			// convertView.findViewById(R.id.tv_yq);
			// listItemView.tv_pq = (TextView)
			// convertView.findViewById(R.id.tv_pq);
			// listItemView.tv_bzzl = (TextView)
			// convertView.findViewById(R.id.tv_bzzl);
			// listItemView.tv_zfx = (TextView)
			// convertView.findViewById(R.id.tv_zfx);
			// listItemView.tv_time = (TextView)
			// convertView.findViewById(R.id.tv_time);
			// listItemView.tv_day = (TextView)
			// convertView.findViewById(R.id.tv_day);
			// 设置控件集到convertView
			lmap.put(position, convertView);
			convertView.setTag(listItemView);
		} else
		{
			convertView = lmap.get(position);
			listItemView = (ListItemView) convertView.getTag();
		}
		// 设置文字和图片

		return convertView;
	}

}