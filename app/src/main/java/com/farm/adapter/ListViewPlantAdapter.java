package com.farm.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.farm.R;
import com.farm.app.AppConfig;
import com.farm.bean.plantgrowthtab;
import com.farm.common.BitmapHelper;
import com.farm.ui.GrowthTree_;
import com.farm.ui.ShowPlant_;

import java.util.HashMap;
import java.util.List;

/**
 * 
 */
public class ListViewPlantAdapter extends BaseAdapter
{
	private Context context;// 运行上下文
	private List<plantgrowthtab> listItems;// 数据集合
	private LayoutInflater listContainer;// 视图容器
	plantgrowthtab plantgrowthtab;

	static class ListItemView
	{ // 自定义控件集合
		public ImageView img;
		public TextView tv_name;
		public Button btn_detail;
		public Button btn_tree;
	}

	/**
	 * 实例化Adapter
	 * 
	 * @param context
	 * @param data
	 * @param context
	 */
	public ListViewPlantAdapter(Context context, List<plantgrowthtab> data)
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

	/**
	 * ListView Item设置
	 */
	public View getView(int position, View convertView, ViewGroup parent)
	{
		plantgrowthtab = listItems.get(position);
		// 自定义视图
		ListItemView listItemView = null;
		if (lmap.get(position) == null)
		{
			// 获取list_item布局文件的视图
			convertView = listContainer.inflate(R.layout.listitem_plantgrowth, null);
			listItemView = new ListItemView();
			// 获取控件对象
			listItemView.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			listItemView.btn_tree = (Button) convertView.findViewById(R.id.btn_tree);
			listItemView.btn_detail = (Button) convertView.findViewById(R.id.btn_detail);
			listItemView.img = (ImageView) convertView.findViewById(R.id.img);
			listItemView.btn_detail.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View arg0)
				{
					Intent intent = new Intent(context, ShowPlant_.class);
					intent.putExtra("plant", plantgrowthtab);
					context.startActivity(intent);
				}
			});
			listItemView.btn_tree.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View arg0)
				{
					Intent intent = new Intent(context, GrowthTree_.class);
					intent.putExtra("plant", plantgrowthtab);
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
		listItemView.tv_name.setText(plantgrowthtab.getplantName());
		BitmapHelper.setImageViewBackground(context, listItemView.img, AppConfig.baseurl + plantgrowthtab.getpicPath1());
		return convertView;
	}

}