package com.farm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.farm.R;
import com.farm.bean.SelectRecords;
import com.farm.common.SqliteDb;

import java.util.HashMap;
import java.util.List;

public class DictionarySecondAdapter extends BaseAdapter
{

	private Context context;
	private int position = 0;
	// Holder hold;
	private LayoutInflater listContainer;// 视图容器
	String firstid;
	String firstType;
	String BELONG;
	List<String> secondItemid;
	List<String> secondItemName;
	List<SelectRecords> list_SelectRecords;

	public DictionarySecondAdapter(Context context, String BELONG, String firstid, String firstType, List<String> secondItemid, List<String> secondItemName)
	{
		this.listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
		this.context = context;
		this.secondItemid = secondItemid;
		this.secondItemName = secondItemName;
		this.firstid = firstid;
		this.firstType = firstType;
		this.BELONG = BELONG;
		list_SelectRecords = SqliteDb.getSelectRecordTemp(context, SelectRecords.class, BELONG, firstType);
	}

	public int getCount()
	{
		return secondItemName.size();
	}

	public Object getItem(int position)
	{
		return secondItemName.get(position);
	}

	public long getItemId(int position)
	{
		return position;
	}

	HashMap<Integer, View> lmap = new HashMap<Integer, View>();

	public View getView(int arg0, View convertView, ViewGroup viewGroup)
	{
		ListItemView listItemView = null;
		if (lmap.get(arg0) == null)
		{
			convertView = listContainer.inflate(R.layout.dictionaryseconditem, null);
			listItemView = new ListItemView();
			listItemView.txt = (TextView) convertView.findViewById(R.id.moreitem_txt);
			listItemView.img = (ImageView) convertView.findViewById(R.id.moreitem_img);
			listItemView.txt.setText(secondItemName.get(arg0));
			if (list_SelectRecords != null)
			{
				for (int i = 0; i < list_SelectRecords.size(); i++)
				{
					if (list_SelectRecords != null && list_SelectRecords.get(i).getSecondtype().toString().equals(secondItemName.get(arg0)))
					{
						listItemView.txt.setTextColor(0xFFFF8C00);
						listItemView.img.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.item_collection_selected));
						listItemView.img.setTag(true);
						list_SelectRecords.remove(i);
					}
				}
			}
			lmap.put(arg0, convertView);
			convertView.setTag(listItemView);
		} else
		{
			convertView = lmap.get(arg0);
			listItemView = (ListItemView) convertView.getTag();
		}
		return convertView;
	}

	public void setSelectItem(int position)
	{
		this.position = position;
		ListItemView listItemView = (ListItemView) lmap.get(position).getTag();
		Boolean isselected = (Boolean) listItemView.img.getTag();
		if (isselected != null && isselected)// 已选中
		{
			listItemView.txt.setTextColor(0xFF666666);
			listItemView.img.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.item_collection_unselect));
			listItemView.img.setTag(false);
			deleteSelectRecords(BELONG, firstType, secondItemName.get(position));
			list_SelectRecords = SqliteDb.getSelectRecordTemp(context, SelectRecords.class, BELONG, firstType);

		} else
		// 没选中
		{
			listItemView.txt.setTextColor(0xFFFF8C00);// 设置第一项选择后的颜色
			listItemView.img.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.item_collection_selected));
			listItemView.img.setTag(true);
			saveSelectRecords(BELONG, firstid, firstType, secondItemName.get(position), secondItemName.get(position));

		}
	}

	static class ListItemView
	{ // 自定义控件集合
		TextView txt;
		ImageView img;
	}

	public void saveSelectRecords(String BELONG, String firstid, String firsttype, String secondid, String secondType)
	{
		SelectRecords selectRecords = new SelectRecords();
		selectRecords.setBELONG(BELONG);
		selectRecords.setFirstid(firstid);
		selectRecords.setFirsttype(firsttype);
		selectRecords.setSecondid(secondid);
		selectRecords.setSecondtype(secondType);
		selectRecords.setThirdid("");
		selectRecords.setThirdtype("");
		selectRecords.setId(1);
		SqliteDb.save(context, selectRecords);
	}

	public void deleteSelectRecords(String BELONG, String firsttype, String secondType)
	{
		SqliteDb.deleteRecordtemp(context, SelectRecords.class, BELONG, firsttype, secondType);
	}

}
