package com.farm.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.adapter.ListViewSingleGoodAdapter;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.Dictionary;
import com.farm.bean.Result;
import com.farm.bean.commembertab;
import com.farm.bean.planttab;
import com.farm.common.DictionaryHelper;
import com.farm.common.StringUtils;
import com.farm.common.UIHelper;
import com.farm.widget.NewDataToast;
import com.farm.widget.PullToRefreshListView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@SuppressLint("NewApi")
@EActivity(R.layout.singlegoodlist)
public class SingleGoodList extends Activity implements OnClickListener
{
	SelectorFragment selectorUi;
	Fragment mContent = new Fragment();
	private ListViewSingleGoodAdapter listAdapter;
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
	TextView tv_tab;
	@ViewById
	View line;
	@ViewById
	ImageView iv_dowm_tab;
	@ViewById
	PullToRefreshListView frame_listview_news;
	Dictionary dictionary;

	@Click
	void btn_add()
	{
		// showPop_addcommand();
		Intent intent = new Intent(this, AddSpecialCost_.class);
		startActivity(intent);
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
		dictionary = DictionaryHelper.getDictionaryFromAssess(SingleGoodList.this, "DGJXC");
		selectorUi = new SelectorFragment_();
		Bundle bundle = new Bundle();
		bundle.putSerializable("bean", dictionary);
		selectorUi.setArguments(bundle);
		switchContent(mContent, selectorUi);
		initAnimalListView();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		getActionBar().hide();
		appContext = (AppContext) getApplication();
		IntentFilter intentfilter_update = new IntentFilter(AppContext.BROADCAST_UPDATEPLANT);
		registerReceiver(receiver_update, intentfilter_update);

	}

	BroadcastReceiver receiver_update = new BroadcastReceiver()// 从扩展页面返回信息
	{
		@SuppressWarnings("deprecation")
		@Override
		public void onReceive(Context context, Intent intent)
		{
			getListData(UIHelper.LISTVIEW_ACTION_REFRESH, UIHelper.LISTVIEW_DATATYPE_NEWS, frame_listview_news, listAdapter, list_foot_more, list_foot_progress, AppContext.PAGE_SIZE, 0);
		}
	};

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

	private void getListData(final int actiontype, final int objtype, final PullToRefreshListView lv, final BaseAdapter adapter, final TextView more, final ProgressBar progressBar, final int PAGESIZE, int PAGEINDEX)
	{
		commembertab commembertab = AppContext.getUserInfo(SingleGoodList.this);
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("areaid", "4");
		params.addQueryStringParameter("userid", commembertab.getId());
		params.addQueryStringParameter("uid", commembertab.getuId());
		params.addQueryStringParameter("username", commembertab.getuserName());
		params.addQueryStringParameter("orderby", "regDate desc");
		params.addQueryStringParameter("strWhere", "");
		params.addQueryStringParameter("page_size", String.valueOf(PAGESIZE));
		params.addQueryStringParameter("page_index", String.valueOf(PAGEINDEX));
		params.addQueryStringParameter("action", "plantGetList");
		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
		{
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo)
			{
				String a = responseInfo.result;
				List<planttab> listNewData = null;
				Result result = JSON.parseObject(responseInfo.result, Result.class);
				if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
				{
					if (result.getAffectedRows() != 0)
					{
						listNewData = JSON.parseArray(result.getRows().toJSONString(), planttab.class);
					} else
					{
						listNewData = new ArrayList<planttab>();
					}
				} else
				{
					AppContext.makeToast(SingleGoodList.this, "error_connectDataBase");
					return;
				}
				// 数据处理
				int size = listNewData.size();

				switch (actiontype)
				{
				case UIHelper.LISTVIEW_ACTION_INIT:// 初始化
				case UIHelper.LISTVIEW_ACTION_REFRESH:// 顶部刷新
				case UIHelper.LISTVIEW_ACTION_CHANGE_CATALOG:// 页面切换
					int newdata = 0;// 该变量为新加载数据数量-只有顶部刷新才会使用到
					switch (objtype)
					{
					case UIHelper.LISTVIEW_DATATYPE_NEWS:
						listSumData = size;
						if (actiontype == UIHelper.LISTVIEW_ACTION_REFRESH)
						{
							if (listData.size() > 0)// 页面切换时，若之前列表中已有数据，则往上面添加，并判断去除重复
							{
								for (planttab planttab1 : listNewData)
								{
									boolean b = false;
									for (planttab planttab2 : listData)
									{
										if (planttab1.getId().equals(planttab2.getId()))
										{
											b = true;
											break;
										}
									}
									if (!b)// 两个不相等才添加
										newdata++;
								}
							} else
							{
								newdata = size;
							}
						}
						listData.clear();// 先清除原有数据
						listData.addAll(listNewData);
						break;
					case UIHelper.LISTVIEW_DATATYPE_BLOG:
					case UIHelper.LISTVIEW_DATATYPE_COMMENT:
					}
					if (actiontype == UIHelper.LISTVIEW_ACTION_REFRESH)
					{
						// 提示新加载数据
						if (newdata > 0)
						{
							NewDataToast.makeText(SingleGoodList.this, getString(R.string.new_data_toast_message, newdata), appContext.isAppSound(), R.raw.newdatatoast).show();
						} else
						{
							// NewDataToast.makeText(SingleGoodList.this,
							// getString(R.string.new_data_toast_none), false,
							// R.raw.newdatatoast).show();
						}
					}
					break;
				case UIHelper.LISTVIEW_ACTION_SCROLL:// 底部刷新，并且判断去除重复数据
					switch (objtype)
					{
					case UIHelper.LISTVIEW_DATATYPE_NEWS:
						listSumData += size;
						if (listNewData.size() > 0)
						{
							for (planttab planttab1 : listNewData)
							{
								boolean b = false;
								for (planttab planttab2 : listData)
								{
									if (planttab1.getId().equals(planttab2.getId()))
									{
										b = true;
										break;
									}
								}
								if (!b)
									listData.add(planttab1);
							}
						} else
						{
							listData.addAll(listNewData);
						}
						break;
					case UIHelper.LISTVIEW_DATATYPE_BLOG:
						break;
					}
					break;
				}
				// 刷新列表
				if (size >= 0)
				{
					if (size < PAGESIZE)
					{
						lv.setTag(UIHelper.LISTVIEW_DATA_FULL);
						adapter.notifyDataSetChanged();
						more.setText(R.string.load_full);// 已经全部加载完毕
					} else if (size == PAGESIZE)
					{// 还有数据可以加载
						lv.setTag(UIHelper.LISTVIEW_DATA_MORE);
						adapter.notifyDataSetChanged();
						more.setText(R.string.load_more);
					}

				} else if (size == -1)
				{
					// 有异常--显示加载出错 & 弹出错误消息
					lv.setTag(UIHelper.LISTVIEW_DATA_MORE);
					more.setText(R.string.load_error);
					AppContext.makeToast(SingleGoodList.this, "load_error");
				}
				if (adapter.getCount() == 0)
				{
					lv.setTag(UIHelper.LISTVIEW_DATA_EMPTY);
					more.setText(R.string.load_empty);
				}
				progressBar.setVisibility(ProgressBar.GONE);
				// main_head_progress.setVisibility(ProgressBar.GONE);
				if (actiontype == UIHelper.LISTVIEW_ACTION_REFRESH)
				{
					lv.onRefreshComplete(getString(R.string.pull_to_refresh_update) + new Date().toLocaleString());
					lv.setSelection(0);
				} else if (actiontype == UIHelper.LISTVIEW_ACTION_CHANGE_CATALOG)
				{
					lv.onRefreshComplete();
					lv.setSelection(0);
				}
			}

			@Override
			public void onFailure(HttpException error, String msg)
			{
				AppContext.makeToast(SingleGoodList.this, "error_connectServer");
			}
		});
	}

	private void initAnimalListView()
	{
		listAdapter = new ListViewSingleGoodAdapter(SingleGoodList.this, listData);
		list_footer = SingleGoodList.this.getLayoutInflater().inflate(R.layout.listview_footer, null);
		list_foot_more = (TextView) list_footer.findViewById(R.id.listview_foot_more);
		list_foot_progress = (ProgressBar) list_footer.findViewById(R.id.listview_foot_progress);
		frame_listview_news.addFooterView(list_footer);// 添加底部视图 必须在setAdapter前
		frame_listview_news.setAdapter(listAdapter);
		frame_listview_news.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				// 点击头部、底部栏无效
				if (position == 0 || view == list_footer)
					return;

				// Animal animal = null;
				// // 判断是否是TextView
				// if (view instanceof TextView)
				// {
				// animal = (Animal) view.getTag();
				// } else
				// {
				// TextView tv = (TextView)
				// view.findViewById(R.id.news_listitem_title);
				// animal = (Animal) tv.getTag();
				// }
				// if (animal == null)
				// return;
				planttab planttab = listData.get(position - 1);
				if (planttab == null)
					return;
				Intent intent = new Intent(SingleGoodList.this, SingleGoodInvoicingList_.class);
				intent.putExtra("plant", planttab);// 因为list中添加了头部,因此要去掉一个
				startActivity(intent);
			}
		});
		frame_listview_news.setOnScrollListener(new AbsListView.OnScrollListener()
		{
			public void onScrollStateChanged(AbsListView view, int scrollState)
			{
				frame_listview_news.onScrollStateChanged(view, scrollState);

				// 数据为空--不用继续下面代码了
				if (listData.isEmpty())
					return;

				// 判断是否滚动到底部
				boolean scrollEnd = false;
				try
				{
					if (view.getPositionForView(list_footer) == view.getLastVisiblePosition())
						scrollEnd = true;
				} catch (Exception e)
				{
					scrollEnd = false;
				}

				int lvDataState = StringUtils.toInt(frame_listview_news.getTag());
				if (scrollEnd && lvDataState == UIHelper.LISTVIEW_DATA_MORE)
				{
					frame_listview_news.setTag(UIHelper.LISTVIEW_DATA_LOADING);
					list_foot_more.setText(R.string.load_ing);// 之前显示为"完成"加载
					list_foot_progress.setVisibility(View.VISIBLE);
					// 当前pageIndex
					int pageIndex = listSumData / AppContext.PAGE_SIZE;// 总数里面包含几个PAGE_SIZE
					getListData(UIHelper.LISTVIEW_ACTION_SCROLL, UIHelper.LISTVIEW_DATATYPE_NEWS, frame_listview_news, listAdapter, list_foot_more, list_foot_progress, AppContext.PAGE_SIZE, pageIndex);
					// loadLvNewsData(curNewsCatalog, pageIndex, lvNewsHandler,
					// UIHelper.LISTVIEW_ACTION_SCROLL);
				}
			}

			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
			{
				frame_listview_news.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
			}
		});
		frame_listview_news.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener()
		{
			public void onRefresh()
			{
				// loadLvNewsData(curNewsCatalog, 0, lvNewsHandler,
				// UIHelper.LISTVIEW_ACTION_REFRESH);
				getListData(UIHelper.LISTVIEW_ACTION_REFRESH, UIHelper.LISTVIEW_DATATYPE_NEWS, frame_listview_news, listAdapter, list_foot_more, list_foot_progress, AppContext.PAGE_SIZE, 0);
			}
		});
		// 加载资讯数据
		if (listData.isEmpty())
		{
			getListData(UIHelper.LISTVIEW_ACTION_INIT, UIHelper.LISTVIEW_DATATYPE_NEWS, frame_listview_news, listAdapter, list_foot_more, list_foot_progress, AppContext.PAGE_SIZE, 0);
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
		data.add("所有仓库");
		data.add("武鸣园区1号仓库");
		data.add("武鸣园区2号仓库");
		data.add("武鸣园区3号仓库");
		data.add("横县园区1号仓库");
		data.add("横县园区2号仓库");
		data.add("横县园区3号仓库");
		ListView listview = (ListView) pv_tab.findViewById(R.id.lv_yq);
		listview.setAdapter(new TitleAdapter(SingleGoodList.this, data));
		listview.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int postion, long arg3)
			{
				tv_tab.setText(data.get(postion));
				tv_tab.setTextColor(getResources().getColor(R.color.white));
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
