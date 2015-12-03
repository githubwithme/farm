package com.farm.widget;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.farm.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SelectorLayout extends RelativeLayout implements OnClickListener
{
	Fragment mContent = new Fragment();
	TypeAdapter typeAdapter;
	View selectorlayout;
	Context context;
	public LinearLayout ll_selected;
	public HorizontalScrollView hs_selected;
	public ListView lv_type;
	public FrameLayout container;
	public Button btn_sure;
	public Button btn_reset;
	List<String> type_Data = new ArrayList<String>();
	List<View> type_laout = new ArrayList<View>();
	HashMap<String, View> hashMap = new HashMap<String, View>();

	public void setType_laout(List<View> type_laout)
	{
		this.type_laout = type_laout;
	}

	public List<View> getType_laout()
	{
		return type_laout;
	}

	public void setTypeData(List<String> typeData)
	{
		this.type_Data = typeData;
	}

	public List<String> getTypeData()
	{
		return type_Data;
	}

	public SelectorLayout(Context context)
	{
		super(context);

	}

	public SelectorLayout(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		this.context = context;
		initUi();
		initData();
	}

	public SelectorLayout(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);

	}

	private void initUi()
	{
		type_Data = new ArrayList<String>();// 类型数据
		type_Data.add("片区");
		type_Data.add("观察时间");
		type_Data.add("植株类型");
		type_Data.add("苗情");
		type_Data.add("其他1");
		type_Data.add("其他2");

		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 0);

		View view1 = LayoutInflater.from(context).inflate(R.layout.selector_pq, null);
		view1.setLayoutParams(lp);
		type_laout.add(view1);

		View view2 = LayoutInflater.from(context).inflate(R.layout.selector_obsertime, null);
		view1.setLayoutParams(lp);
		type_laout.add(view2);

		View view3 = LayoutInflater.from(context).inflate(R.layout.selector_obsertime, null);
		view1.setLayoutParams(lp);
		type_laout.add(view3);

		View view4 = LayoutInflater.from(context).inflate(R.layout.selector_obsertime, null);
		view1.setLayoutParams(lp);
		type_laout.add(view4);

		View view5 = LayoutInflater.from(context).inflate(R.layout.selector_obsertime, null);
		view1.setLayoutParams(lp);
		type_laout.add(view5);

		View view6 = LayoutInflater.from(context).inflate(R.layout.selector_obsertime, null);
		view1.setLayoutParams(lp);
		type_laout.add(view6);

	}

	private void findId()
	{
		hs_selected = (HorizontalScrollView) selectorlayout.findViewById(R.id.hs_selected);
		ll_selected = (LinearLayout) selectorlayout.findViewById(R.id.ll_selected);
		lv_type = (ListView) selectorlayout.findViewById(R.id.lv_type);
		container = (FrameLayout) selectorlayout.findViewById(R.id.container);
		btn_sure = (Button) selectorlayout.findViewById(R.id.btn_sure);
		btn_reset = (Button) selectorlayout.findViewById(R.id.btn_reset);
		btn_sure.setOnClickListener(this);
		btn_reset.setOnClickListener(this);
	}

	private void initData()
	{
		selectorlayout = LayoutInflater.from(context).inflate(R.layout.selectorlayout, null);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 0);
		findId();
		typeAdapter = new TypeAdapter(context, type_Data, R.layout.type_item);
		lv_type.setAdapter(typeAdapter);
		lv_type.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3)
			{
				v.setSelected(true);
				TextView textView = (TextView) v.findViewById(R.id.tv_tpye);
				String type = textView.getText().toString();
				if (type.equals("片区"))
				{
					if (hashMap.get(type) != null)
					{
						container.removeAllViews();
						container.addView(hashMap.get(type));
					} else
					{
						container.removeAllViews();
						container.addView(type_laout.get(position));
						hashMap.put(type, type_laout.get(position));
					}

				} else if (type.equals("观察时间"))
				{
					if (hashMap.get(type) != null)
					{
						container.removeAllViews();
						container.addView(hashMap.get(type));
					} else
					{
						container.removeAllViews();
						container.addView(type_laout.get(position));
						hashMap.put(type, type_laout.get(position));
					}
				} else if (type.equals("植株类型"))
				{
					Toast.makeText(context, type, Toast.LENGTH_SHORT).show();
				} else if (type.equals("苗情"))
				{
					Toast.makeText(context, type, Toast.LENGTH_SHORT).show();
				} else if (type.equals("其他1"))
				{
					Toast.makeText(context, type, Toast.LENGTH_SHORT).show();
				} else if (type.equals("其他2"))
				{
					Toast.makeText(context, type, Toast.LENGTH_SHORT).show();
				} else if (type.equals("其他3"))
				{
					Toast.makeText(context, type, Toast.LENGTH_SHORT).show();
				} else if (type.equals("其他4"))
				{
					Toast.makeText(context, type, Toast.LENGTH_SHORT).show();
				} else if (type.equals("+添加筛选项"))
				{
					Toast.makeText(context, type, Toast.LENGTH_SHORT).show();
				}
			}
		});
		addView(selectorlayout, lp);
	}

	public class TypeAdapter extends BaseAdapter
	{
		private Context context;
		private List<String> listItems;
		private LayoutInflater listContainer;
		private int itemViewResource;
		String type;

		class ListItemView
		{
			public ImageView iv_point;
			public TextView tv_tpye;
		}

		public TypeAdapter(Context context, List<String> data, int resource)
		{
			this.context = context;
			this.listContainer = LayoutInflater.from(context);
			this.itemViewResource = resource;
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
			type = listItems.get(position);
			ListItemView listItemView = null;
			if (lmap.get(position) == null)
			{
				convertView = listContainer.inflate(this.itemViewResource, null);
				listItemView = new ListItemView();
				listItemView.tv_tpye = (TextView) convertView.findViewById(R.id.tv_tpye);
				listItemView.iv_point = (ImageView) convertView.findViewById(R.id.iv_point);
				lmap.put(position, convertView);
				convertView.setTag(listItemView);
			} else
			{
				convertView = lmap.get(position);
				listItemView = (ListItemView) convertView.getTag();
			}
			listItemView.tv_tpye.setText(type);
			return convertView;
		}
	}

	public class PqAdapter extends BaseAdapter
	{
		private Context context;
		private List<String> listItems;
		private LayoutInflater listContainer;
		private int itemViewResource;
		String type;

		class ListItemView
		{
			public ImageView iv_point;
			public TextView tv_tpye;
		}

		public PqAdapter(Context context, List<String> data, int resource)
		{
			this.context = context;
			this.listContainer = LayoutInflater.from(context);
			this.itemViewResource = resource;
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
			type = listItems.get(position);
			ListItemView listItemView = null;
			if (lmap.get(position) == null)
			{
				convertView = listContainer.inflate(this.itemViewResource, null);
				listItemView = new ListItemView();
				listItemView.tv_tpye = (TextView) convertView.findViewById(R.id.tv_tpye);
				listItemView.iv_point = (ImageView) convertView.findViewById(R.id.iv_point);
				lmap.put(position, convertView);
				convertView.setTag(listItemView);
			} else
			{
				convertView = lmap.get(position);
				listItemView = (ListItemView) convertView.getTag();
			}
			listItemView.tv_tpye.setText(type);
			return convertView;
		}
	}

	public void switchContent(Fragment from, Fragment to, int id)
	{
		if (mContent != to)
		{
			mContent = to;
			FragmentTransaction transaction = ((Activity) context).getFragmentManager().beginTransaction();
			if (!to.isAdded())
			{ // 先判断是否被add过
				transaction.hide(from).add(id, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
			} else
			{
				transaction.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
			}
		}
	}

	public class PqFragment extends Fragment
	{
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
		{
			View rootView = inflater.inflate(R.layout.selector_pq, container, false);
			return rootView;
		}

	}

	@Override
	public void onClick(View v)
	{

		switch (v.getId())
		{
		case R.id.btn_sure:

			break;
		case R.id.btn_reset:

			break;

		default:
			break;
		}
	}
}
