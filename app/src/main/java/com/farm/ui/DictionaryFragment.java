package com.farm.ui;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.CheckBox;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;

import com.farm.R;
import com.farm.adapter.DictionaryFirstItemAdapter;
import com.farm.adapter.DictionarySecondAdapter;
import com.farm.app.AppContext;
import com.farm.bean.Dictionary;
import com.farm.bean.SelectRecords;
import com.farm.common.SqliteDb;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressLint("NewApi")
@EFragment
public class DictionaryFragment extends Fragment implements OnClickListener
{
	private ListView mainlist;
	private ListView morelist;
	TextView resultview;
	private List<Map<String, Object>> mainList;
	DictionaryFirstItemAdapter mainAdapter;
	DictionarySecondAdapter moreAdapter;
	PopupWindow popupWindow_recent;
	View popupWindowView_recent;
	PopupWindow popupWindow_selector;
	View popupWindowView_selector;
	private AppContext appContext;// 全局Context
	@ViewById
	View line;
	CheckBox cb_1;
	RadioButton rb_bz;
	int oldpostion = 0;
	List<SelectRecords> list_SelectRecords;
	public Boolean state_hs_selected = false;
	String title;
	Dictionary dictionary;

	@AfterViews
	void afterOncreate()
	{
		showPop_selector();
	}

	@Override
	public void onDestroyView()
	{
		super.onDestroyView();
		SqliteDb.deleteAllRecordtemp(getActivity(), SelectRecords.class, dictionary.getBELONG());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.dictionaryui, container, false);
		dictionary = (Dictionary) getArguments().getSerializable("bean");
		return rootView;
	}

	public void setResultview(TextView resultview)
	{
		this.resultview = resultview;
	}

	public TextView getResultview()
	{
		return resultview;
	}

	public void showPop_selector()
	{
		LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
		popupWindowView_selector = layoutInflater.inflate(R.layout.pop_selector, null);// 外层
		popupWindowView_selector.setOnKeyListener(new OnKeyListener()
		{
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event)
			{
				if ((keyCode == KeyEvent.KEYCODE_MENU) && (popupWindow_selector.isShowing()))
				{
					popupWindow_selector.dismiss();
					return true;
				}
				return false;
			}
		});
		popupWindowView_selector.setOnTouchListener(new OnTouchListener()
		{
			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				if (popupWindow_selector.isShowing())
				{
					// popupWindow_selector.dismiss();// 点击外部消失
				}
				return false;
			}
		});
		popupWindow_selector = new PopupWindow(popupWindowView_selector, LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, true);
		popupWindow_selector.showAsDropDown(line, 0, 0);
		popupWindow_selector.setOutsideTouchable(false);
		ColorDrawable cd = new ColorDrawable(0x000000);
		popupWindow_selector.setBackgroundDrawable(cd);

		mainlist = (ListView) popupWindowView_selector.findViewById(R.id.classify_mainlist);
		morelist = (ListView) popupWindowView_selector.findViewById(R.id.classify_morelist);
		popupWindowView_selector.findViewById(R.id.btn_reset).setOnClickListener(this);
		popupWindowView_selector.findViewById(R.id.btn_sure).setOnClickListener(this);
		initModle();
		initView(popupWindowView_selector);
	}

	@Override
	public void onClick(View v)
	{

		switch (v.getId())
		{
		case R.id.btn_sure:
			popupWindow_selector.dismiss();
			list_SelectRecords = SqliteDb.getSelectRecordByFirstTypetemp(getActivity(), SelectRecords.class, dictionary.getBELONG().toString());
			String result = "";
			for (int i = 0; i < list_SelectRecords.size(); i++)
			{
				result = result + list_SelectRecords.get(i).getFirsttype() + "——" + list_SelectRecords.get(i).getSecondtype() + " ; ";
			}
			resultview.setText(result);
			break;
		case R.id.btn_reset:
			popupWindow_selector.dismiss();
			SqliteDb.deleteAllRecordtemp(getActivity(), SelectRecords.class, dictionary.getBELONG());
			resultview.setText("请选择指令");
			break;

		default:
			break;
		}
	}

	private void initView(View v)
	{
		mainlist = (ListView) v.findViewById(R.id.classify_mainlist);
		morelist = (ListView) v.findViewById(R.id.classify_morelist);
		mainAdapter = new DictionaryFirstItemAdapter(getActivity(), mainList);
		mainAdapter.setSelectItem(0);
		mainlist.setAdapter(mainAdapter);
		mainlist.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				initAdapter(dictionary.getBELONG(), dictionary.getFirstItemID().get(position), dictionary.getFirstItemName().get(position), dictionary.getSecondItemID().get(position), dictionary.getSecondItemName().get(position));
				mainAdapter.setSelectItem(position);
				mainAdapter.notifyDataSetChanged();
			}
		});
		mainlist.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		// 一定要设置这个属性，否则ListView不会刷新
		initAdapter(dictionary.getBELONG(), dictionary.getFirstItemID().get(0), dictionary.getFirstItemName().get(0), dictionary.getSecondItemID().get(0), dictionary.getSecondItemName().get(0));

		morelist.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				moreAdapter.setSelectItem(position);
				// moreAdapter.notifyDataSetChanged();
			}
		});
	}

	private void initAdapter(String BELONG, String firstid, String firstType, List<String> secondItemid, List<String> secondItemName)
	{
		moreAdapter = new DictionarySecondAdapter(getActivity(), BELONG, firstid, firstType, secondItemid, secondItemName);
		morelist.setAdapter(moreAdapter);
		moreAdapter.notifyDataSetChanged();
	}

	private void initModle()
	{
		mainList = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < dictionary.getFirstItemName().size(); i++)
		{
			Map<String, Object> map = new HashMap<String, Object>();
			// map.put("img", dictionary.getLISTVIEWIMG()[i]);
			map.put("txt", dictionary.getFirstItemName().get(i));
			mainList.add(map);
		}
	}

}
