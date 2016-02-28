package com.farm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.farm.R;
import com.farm.bean.BreakOffTab;
import com.farm.bean.ProductBatch;
import com.farm.bean.areatab;
import com.farm.bean.contractTab;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProductBatchGridViewAdapter extends BaseAdapter
{
	ListItemView listItemView = null;
	private LayoutInflater listContainer;// 视图容器
	private List<contractTab> list;
	private List<ProductBatch> list_ProductBatch=new ArrayList<>();
	private Context context;
	areatab areatab;
	public ProductBatchGridViewAdapter(Context context, List<contractTab> list,areatab areatab)
	{
		this.list = list;
		this.areatab = areatab;
		this.context = context;
		this.listContainer = LayoutInflater.from(context);
	}

	@Override
	public int getCount()
	{
		if (list != null && list.size() > 0)
			return list.size();
		else
			return 0;
	}

	@Override
	public Object getItem(int position)
	{
		return list.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return 0;
	}
	HashMap<Integer, View> lmap = new HashMap<Integer, View>();

	public View getView(int position, View convertView, ViewGroup parent)
	{
		// 自定义视图

		if (lmap.get(position) == null)
		{
			// 获取list_item布局文件的视图
			convertView = listContainer.inflate(R.layout.item_batchdetailgridview, null);
			listItemView = new ListItemView();
			// 获取控件对象
			listItemView.cb_productselect = (CheckBox) convertView.findViewById(R.id.cb_productselect);
			listItemView.tv_contractname = (TextView) convertView.findViewById(R.id.tv_contractname);
			listItemView.tv_numberofbreakoff = (TextView) convertView.findViewById(R.id.tv_numberofbreakoff);
			listItemView.tv_output = (TextView) convertView.findViewById(R.id.tv_output);

			listItemView.cb_productselect.setTag(position);
			listItemView.cb_productselect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
			{
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
				{
					if (isChecked)
					{
						int pos=Integer.valueOf(buttonView.getTag().toString());
						ProductBatch productBatch=new ProductBatch();
						productBatch.setparkId(areatab.getparkId());
						productBatch.setparkName(areatab.getparkName());
						productBatch.setAreaId(areatab.getid());
						productBatch.setareaName(areatab.getareaName());
						productBatch.setContractId(list.get(pos).getid());
						productBatch.setContractNum(list.get(pos).getContractNum());
						productBatch.setNumberofproductbatch(list.get(pos).getBreakOffTabList().get(0).getNumberofbreakoff());
						productBatch.setDateofproductbatch("");
						productBatch.setNote("");
						list_ProductBatch.add(productBatch);
//					Toast.makeText(context,areatab.getparkName()+"-"+areatab.getareaName()+"-"+list.get(pos).getContractNum()+"-"+list.get(pos).getBreakOffTabList().get(0).getNumberofbreakoff(),Toast.LENGTH_SHORT).show();
					}else
					{

					}
					int pos=Integer.valueOf(buttonView.getTag().toString());
					ProductBatch productBatch=new ProductBatch();
					productBatch.setparkId(areatab.getparkId());
					productBatch.setparkName(areatab.getparkName());
					productBatch.setAreaId(areatab.getid());
					productBatch.setareaName(areatab.getareaName());
					productBatch.setContractId(list.get(pos).getid());
					productBatch.setContractNum(list.get(pos).getContractNum());
					productBatch.setNumberofproductbatch(list.get(pos).getBreakOffTabList().get(0).getNumberofbreakoff());
					productBatch.setDateofproductbatch("");
					productBatch.setNote("");
					list_ProductBatch.add(productBatch);
//					Toast.makeText(context,areatab.getparkName()+"-"+areatab.getareaName()+"-"+list.get(pos).getContractNum()+"-"+list.get(pos).getBreakOffTabList().get(0).getNumberofbreakoff(),Toast.LENGTH_SHORT).show();
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
		if (list != null && list.size() > 0)
		{
			listItemView.tv_contractname.setText(list.get(position).getContractNum());
			listItemView.tv_output.setText("产量:"+list.get(position).getPlantnumber());
			List<BreakOffTab> list_breakoff=list.get(position).getBreakOffTabList();
			if (list_breakoff != null)
			{
				listItemView.tv_numberofbreakoff.setText("待售:"+list_breakoff.get(0).getNumberofbreakoff());
			}

		}

		return convertView;
	}
//	@Override
//	public View getView(final int position, View convertView, ViewGroup parent)
//	{
//		if (convertView == null)
//		{
//			convertView = View.inflate(context, R.layout.item_batchdetailgridview, null);
//			view = new Holder(convertView);
//			convertView.setTag(view);
//		} else
//		{
//			view = (Holder) convertView.getTag();
//		}
//		if (list != null && list.size() > 0)
//		{
//			view.tv_contractname.setText(list.get(position).getContractNum());
//			view.tv_output.setText("产量:"+list.get(position).getPlantnumber());
//			List<BreakOffTab> list_breakoff=list.get(position).getBreakOffTabList();
//			if (list_breakoff != null)
//			{
//				view.tv_numberofbreakoff.setText("待售:"+list_breakoff.get(0).getNumberofbreakoff());
//			}
//
//		}
//
//		return convertView;
//	}
//
//	private class Holder
//	{
//		private TextView tv_contractname;
//		private TextView tv_numberofbreakoff;
//		private TextView tv_output;
//		private CheckBox cb_productselect;
//
//		public Holder(View view)
//		{
//			cb_productselect = (CheckBox) view.findViewById(R.id.cb_productselect);
//			tv_contractname = (TextView) view.findViewById(R.id.tv_contractname);
//			tv_numberofbreakoff = (TextView) view.findViewById(R.id.tv_numberofbreakoff);
//			tv_output = (TextView) view.findViewById(R.id.tv_output);
//		}
//	}
	static class ListItemView
	{
		private TextView tv_contractname;
		private TextView tv_numberofbreakoff;
		private TextView tv_output;
		private CheckBox cb_productselect;

	}
}
