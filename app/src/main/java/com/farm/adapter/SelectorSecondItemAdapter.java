package com.farm.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.farm.R;
import com.farm.bean.SelectRecords;
import com.farm.common.SqliteDb;
import com.farm.ui.SelectorFragment;

import java.util.HashMap;
import java.util.List;

public class SelectorSecondItemAdapter extends BaseAdapter
{

	private Context context;
	private int position = 0;
	private LayoutInflater listContainer;// 视图容器
	SelectorFragment selectorFragment;
	String firstItemId;
	String firstItemName;
	List<String> secondItemid;
	List<String> secondItemName;
	String BELONG;
	List<SelectRecords> list_SelectRecords;

	public SelectorSecondItemAdapter(SelectorFragment selectorFragment, Context context, String BELONG, String firstItemId, String firstItemName, List<String> secondItemid, List<String> secondItemName, List<View> list_state)
	{
		this.listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
		this.context = context;
		this.secondItemid = secondItemid;
		this.secondItemName = secondItemName;
		this.selectorFragment = selectorFragment;
		this.firstItemId = firstItemId;
		this.firstItemName = firstItemName;
		this.BELONG = BELONG;
		list_SelectRecords = SqliteDb.getSelectRecordTemp(context, SelectRecords.class, BELONG, firstItemName);
	}

	public int getCount()
	{
		return secondItemName.size() - 1;
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
			convertView = listContainer.inflate(R.layout.selectorseconditem, null);
			listItemView = new ListItemView();
			listItemView.txt = (TextView) convertView.findViewById(R.id.moreitem_txt);
			listItemView.img = (ImageView) convertView.findViewById(R.id.moreitem_img);
			listItemView.txt.setText(secondItemName.get(arg0));
			if (arg0 == 0 && list_SelectRecords.size() == 0)
			{
				listItemView.txt.setTextColor(0xFFFF8C00);
				listItemView.img.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.item_collection_selected));
				listItemView.img.setTag(true);
			}
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
			if (secondItemName.get(secondItemName.size() - 1).equals("多选"))
			{
				listItemView.txt.setTextColor(0xFF666666);
				listItemView.img.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.item_collection_unselect));
				listItemView.img.setTag(false);
				removeText(firstItemName + "\n" + secondItemName.get(position));
				deleteSelectRecords(BELONG, firstItemName, secondItemName.get(position));
				list_SelectRecords = SqliteDb.getSelectRecordTemp(context, SelectRecords.class, BELONG, firstItemName);
				if (selectorFragment.list_state.size() == 0)
				{
					selectorFragment.hidehs_selected();
				}
				if (list_SelectRecords.size() == 0)
				{
					// 当更多列表中没有选择时，切换为默认选项
					View v = lmap.get(0);
					ListItemView listItem = (ListItemView) v.getTag();
					listItem.txt.setTextColor(0xFFFF8C00);
					listItem.img.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.item_collection_selected));
					listItem.img.setTag(false);
				}
			}

		} else
		// 没选中
		{
			if (secondItemName.get(secondItemName.size() - 1).equals("单选") || (position == 0 && secondItemName.get(secondItemName.size() - 1).equals("多选")))
			{
				for (int i = 0; i < lmap.size(); i++)
				{
					ListItemView listItem = (ListItemView) lmap.get(i).getTag();
					Boolean issel = (Boolean) listItem.img.getTag();
					if (issel != null && issel)
					{
						listItem.txt.setTextColor(0xFF666666);// 默认颜色
						listItem.img.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.item_collection_unselect));
						listItem.img.setTag(false);
						removeText(firstItemName + "\n" + secondItemName.get(i));
						deleteSelectRecords(BELONG, firstItemName, secondItemName.get(i));
					}
				}
			}
			listItemView.txt.setTextColor(0xFFFF8C00);// 选择后的颜色
			listItemView.img.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.item_collection_selected));
			listItemView.img.setTag(true);
			if (position != 0)
			{
				if (!selectorFragment.state_hs_selected)
				{
					selectorFragment.showhs_selected();
				}
				addText(firstItemName + "\n" + secondItemName.get(position));
				saveSelectRecords(BELONG, firstItemId, firstItemName, secondItemid.get(position), secondItemName.get(position));
				// 当更多列表中有选择时，去掉默认选项
				View v = lmap.get(0);
				ListItemView listItem = (ListItemView) v.getTag();
				listItem.txt.setTextColor(0xFF666666);// 默认颜色
				listItem.img.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.item_collection_unselect));
				listItem.img.setTag(false);
			}

		}
	}

	static class ListItemView
	{ // 自定义控件集合
		TextView txt;
		ImageView img;
	}

	private void addText(String txt)
	{
		TextView textView = new TextView(context);
		textView.setBackgroundColor(context.getResources().getColor(R.color.bg_selectitem));
		LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(120, LinearLayout.LayoutParams.MATCH_PARENT);
		p.setMargins(15, 15, 0, 15);
		textView.setLayoutParams(p);
		textView.setText(txt);
		textView.setTextSize(10f);
		textView.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
		textView.setGravity(Gravity.CENTER);
		selectorFragment.addTextiew(textView);
	}

	private void removeText(String str)
	{
		selectorFragment.removeTextiew(str);
	}

	public void saveSelectRecords(String BELONG, String firstItemId, String firstItemName, String secondItemId, String secondItemName)
	{
		SelectRecords selectRecords = new SelectRecords();
		selectRecords.setBELONG(BELONG);
		selectRecords.setFirstid(firstItemId);
		selectRecords.setFirsttype(firstItemName);
		selectRecords.setSecondid(secondItemId);
		selectRecords.setSecondtype(secondItemName);
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
