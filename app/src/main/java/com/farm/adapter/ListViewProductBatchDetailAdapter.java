package com.farm.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.farm.R;
import com.farm.bean.planttab;
import com.farm.ui.SingleGoodList_;
import com.farm.widget.CustomGridview;

import java.util.HashMap;
import java.util.List;

public class ListViewProductBatchDetailAdapter extends BaseAdapter
{
	private BatchDetailGridViewAdapter adapter;
	private Context context;// 运行上下文
	private List<planttab> listItems;// 数据集合
	private LayoutInflater listContainer;// 视图容器
	planttab planttab;

	static class ListItemView
	{
		public CustomGridview gridview;
		public TextView tv_cmdname;
		public TextView tv_state;
		public TextView tv_yq;
		public TextView tv_pq;
		public TextView tv_bzzl;
		public TextView tv_zfx;
		public TextView tv_time;
		public TextView tv_day;
	}

	public ListViewProductBatchDetailAdapter(Context context, List<planttab> data)
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
		planttab = listItems.get(position);
		// 自定义视图
		ListItemView listItemView = null;
		if (lmap.get(position) == null)
		{
			// 获取list_item布局文件的视图
			convertView = listContainer.inflate(R.layout.listitem_productbatchdetail, null);
			listItemView = new ListItemView();
			// 获取控件对象
			listItemView.gridview = (CustomGridview) convertView.findViewById(R.id.gridview);
			adapter = new BatchDetailGridViewAdapter(context, listItems);
			listItemView.gridview.setAdapter(adapter);
			listItemView.gridview.setOnItemClickListener(new OnItemClickListener()
			{
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
				{
					Intent intent = new Intent(context, SingleGoodList_.class);
					context.startActivity(intent);
				}
			});
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