package com.farm.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.farm.R;
import com.farm.bean.BreakOffTab;

import java.util.List;

public class BreakOffAdapter extends BaseAdapter
{

	private List<BreakOffTab> list;
	private Context context;
	Holder view;

	public BreakOffAdapter(Context context, List<BreakOffTab> list)
	{
		this.list = list;
		this.context = context;
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

	@Override
	public View getView(final int position, View convertView, ViewGroup parent)
	{
		if (convertView == null)
		{
			convertView = View.inflate(context, R.layout.breakoffadapter, null);
			view = new Holder(convertView);
			convertView.setTag(view);
		} else
		{
			view = (Holder) convertView.getTag();
		}
		if (list != null && list.size() > 0)
		{
			 view.tv_contractname.setText(list.get(position).getContractNum());
			 view.tv_numberofbreakoff.setText(list.get(position).getNumberofbreakoff());
			 view.tv_output.setText(list.get(position).getOutput());
		}

		return convertView;
	}

	private class Holder
	{
		private TextView tv_contractname;
		private TextView tv_numberofbreakoff;
		private TextView tv_output;

		public Holder(View view)
		{
			tv_contractname = (TextView) view.findViewById(R.id.tv_contractname);
			tv_output = (TextView) view.findViewById(R.id.tv_output);
			tv_numberofbreakoff = (TextView) view.findViewById(R.id.tv_numberofbreakoff);
		}
	}

}
