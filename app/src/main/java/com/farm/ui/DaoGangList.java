package com.farm.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.farm.R;
import com.farm.adapter.ListViewDaoGangAdapter;
import com.farm.app.AppContext;
import com.farm.bean.Dictionary;
import com.farm.bean.planttab;
import com.farm.common.DictionaryHelper;
import com.farm.common.FileHelper;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SuppressLint("NewApi")
@EActivity(R.layout.daoganglist)
public class DaoGangList extends Activity implements OnClickListener
{
	SelectorFragment selectorUi;
	TimeSeLectorFragment timeSeLectorFragment;
	Fragment mContent = new Fragment();
	Fragment mContent_timefragment = new Fragment();
	private ListViewDaoGangAdapter listAdapter;
	private int listSumData;
	private List<planttab> listData = new ArrayList<planttab>();
	private AppContext appContext;
	private View list_footer;
	private TextView list_foot_more;
	private ProgressBar list_foot_progress;
	PopupWindow pw_tab;
	View pv_tab;
	PopupWindow pw_command;
	View pv_command;
	@ViewById
	TextView tv_title;
	@ViewById
	View line;
	@ViewById
	ImageView iv_dowm_tab;
	@ViewById
	ListView lv;
	Dictionary dictionary;

	@Click
	void btn_account(){
		finish();
	}
	@Click
	void ll_tab()
	{
		iv_dowm_tab.setBackground(getResources().getDrawable(R.drawable.ic_up));
		showPop_title();
	}

	@AfterViews
	void afterOncreate()
	{
		dictionary = DictionaryHelper.getDictionaryFromAssess(DaoGangList.this, "PG_MQ");
		selectorUi = new SelectorFragment_();
		Bundle bundle = new Bundle();
		bundle.putSerializable("bean", dictionary);
		selectorUi.setArguments(bundle);
		switchContent(mContent, selectorUi);
		switchContent_timefragment(mContent_timefragment, timeSeLectorFragment);

		List<planttab> list=FileHelper.getAssetsData(this, "plantgrowthlist", planttab.class);
		listAdapter = new ListViewDaoGangAdapter(DaoGangList.this, list);
		lv.setAdapter(listAdapter);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		getActionBar().hide();
		appContext = (AppContext) getApplication();
		timeSeLectorFragment = new TimeSeLectorFragment_();
	}



	public void switchContent(Fragment from, Fragment to)
	{
		if (mContent != to)
		{
			mContent = to;
			FragmentTransaction transaction = getFragmentManager().beginTransaction();
			if (!to.isAdded())
			{ // 先判断是否被add过
				transaction.hide(from).add(R.id.top_container, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
			} else
			{
				transaction.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
			}
		}
	}

	public void switchContent_timefragment(Fragment from, Fragment to)
	{
		if (mContent_timefragment != to)
		{
			mContent_timefragment = to;
			FragmentTransaction transaction = getFragmentManager().beginTransaction();
			if (!to.isAdded())
			{ // 先判断是否被add过
				transaction.hide(from).add(R.id.time_container, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
			} else
			{
				transaction.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
			}
		}
	}





	public void showPop_title()
	{
		LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		pv_tab = layoutInflater.inflate(R.layout.popup_yq, null);// 外层
		pv_tab.setOnKeyListener(new OnKeyListener()
		{
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event)
			{
				if ((keyCode == KeyEvent.KEYCODE_MENU) && (pw_tab.isShowing()))
				{
					pw_tab.dismiss();
					iv_dowm_tab.setBackground(getResources().getDrawable(R.drawable.ic_down));
					return true;
				}
				return false;
			}
		});
		pv_tab.setOnTouchListener(new OnTouchListener()
		{
			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				if (pw_tab.isShowing())
				{
					pw_tab.dismiss();
					iv_dowm_tab.setBackground(getResources().getDrawable(R.drawable.ic_down));
				}
				return false;
			}
		});
		pw_tab = new PopupWindow(pv_tab, LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, true);
		pw_tab.showAsDropDown(line, 0, 0);
		pw_tab.setOutsideTouchable(true);
		final List<String> data = new ArrayList<String>();
		data.add("所有到岗情况");
		data.add("武鸣园区到岗情况");
		data.add("横县园区到岗情况");
		ListView listview = (ListView) pv_tab.findViewById(R.id.lv_yq);
		listview.setAdapter(new TitleAdapter(DaoGangList.this, data));
		listview.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int postion, long arg3)
			{
				tv_title.setText(data.get(postion));
				tv_title.setTextColor(getResources().getColor(R.color.white));
				iv_dowm_tab.setBackground(getResources().getDrawable(R.drawable.ic_down));
				pw_tab.dismiss();
			}
		});
	}

	public void showPop_addcommand()
	{
		LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		pv_command = layoutInflater.inflate(R.layout.pop_addcommand, null);// 外层
		pv_command.setOnKeyListener(new OnKeyListener()
		{
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event)
			{
				if ((keyCode == KeyEvent.KEYCODE_MENU) && (pw_command.isShowing()))
				{
					pw_command.dismiss();
					return true;
				}
				return false;
			}
		});
		pv_command.setOnTouchListener(new OnTouchListener()
		{
			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				if (pw_command.isShowing())
				{
					pw_command.dismiss();
				}
				return false;
			}
		});
		pw_command = new PopupWindow(pv_command, LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, true);
		pw_command.showAsDropDown(line, 0, 0);
		pw_command.setOutsideTouchable(true);
		pv_command.findViewById(R.id.btn_standardprocommand).setOnClickListener(this);
		pv_command.findViewById(R.id.btn_nonstandardprocommand).setOnClickListener(this);
		pv_command.findViewById(R.id.btn_nonprocommand).setOnClickListener(this);
	}

	public class TitleAdapter extends BaseAdapter
	{
		private Context context;
		private List<String> listItems;
		private LayoutInflater listContainer;
		String type;

		class ListItemView
		{
			public TextView tv_yq;
		}

		public TitleAdapter(Context context, List<String> data)
		{
			this.context = context;
			this.listContainer = LayoutInflater.from(context);
			this.listItems = data;
		}

		HashMap<Integer, View> lmap = new HashMap<Integer, View>();

		public View getView(int position, View convertView, ViewGroup parent)
		{
			type = listItems.get(position);
			ListItemView listItemView = null;
			if (lmap.get(position) == null)
			{
				convertView = listContainer.inflate(R.layout.yq_item, null);
				listItemView = new ListItemView();
				listItemView.tv_yq = (TextView) convertView.findViewById(R.id.tv_yq);
				lmap.put(position, convertView);
				convertView.setTag(listItemView);
			} else
			{
				convertView = lmap.get(position);
				listItemView = (ListItemView) convertView.getTag();
			}
			listItemView.tv_yq.setText(type);
			return convertView;
		}

		@Override
		public int getCount()
		{
			return listItems.size();
		}

		@Override
		public Object getItem(int arg0)
		{
			return null;
		}

		@Override
		public long getItemId(int arg0)
		{
			return 0;
		}
	}

	@Override
	public void onClick(View v)
	{
		Intent intent;
		switch (v.getId())
		{
		case R.id.btn_standardprocommand:
			intent = new Intent(this, AddStandardCommand_.class);
			startActivity(intent);
			break;
		case R.id.btn_nonstandardprocommand:
			intent = new Intent(this, AddNotStandardCommand_.class);
			startActivity(intent);
			break;
		case R.id.btn_nonprocommand:
			intent = new Intent(this, AddNotProductCommand_.class);
			startActivity(intent);
			break;

		default:
			break;
		}
	}

}
