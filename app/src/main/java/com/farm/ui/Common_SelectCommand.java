package com.farm.ui;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.Dictionary;
import com.farm.bean.HaveReadRecord;
import com.farm.bean.Result;
import com.farm.bean.commandtab;
import com.farm.bean.commembertab;
import com.farm.bean.jobtab;
import com.farm.bean.plantgrowthtab;
import com.farm.common.DictionaryHelper;
import com.farm.common.SqliteDb;
import com.farm.common.StringUtils;
import com.farm.common.UIHelper;
import com.farm.common.utils;
import com.farm.widget.CircleImageView;
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
import java.util.concurrent.CountDownLatch;

@EActivity(R.layout.pg_commandlist)
public class Common_SelectCommand extends Activity implements OnClickListener
{
	TimeThread timethread;
	private List<jobtab> joblist;
	CountDownLatch latch;
	private List<commandtab> listItems_selected = new ArrayList<commandtab>();// 数据集合
	SelectorFragment selectorUi;
	Fragment mContent = new Fragment();
	private ListViewPGCommandAdapter listAdapter;
	private int listSumData;
	private List<commandtab> listData = new ArrayList<commandtab>();
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
	Button btn_addtowork;
	@ViewById
	PullToRefreshListView frame_listview_news;
	@ViewById
	ImageButton btn_back;
	Dictionary dictionary;

	@Click
	void btn_back()
	{
		finish();
	}

	@Click
	void btn_addtowork()
	{
		if (listItems_selected.size() > 0)
		{
			latch = new CountDownLatch(listItems_selected.size());
			for (int i = 0; i < listItems_selected.size(); i++)
			{
				jobTabAdd(listItems_selected.get(i));
			}
		} else
		{
			Toast.makeText(this, "您还没选择任何指令呢！", Toast.LENGTH_SHORT).show();
		}

	}

	@AfterViews
	void afterOncreate()
	{
		dictionary = DictionaryHelper.getDictionaryFromAssess(Common_SelectCommand.this, "NCZ_CMD");
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
		joblist = getIntent().getParcelableArrayListExtra("jobtablist");
		appContext = (AppContext) getApplication();
		IntentFilter intentfilter_update = new IntentFilter(AppContext.BROADCAST_UPDATEPLANT);
		registerReceiver(receiver_update, intentfilter_update);
		timethread = new TimeThread();
		timethread.setStop(false);
		timethread.setSleep(false);
		timethread.start();
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

	private void jobTabAdd(commandtab commandtab)
	{
		commembertab commembertab = AppContext.getUserInfo(this);
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("userid", commembertab.getId());
		params.addQueryStringParameter("userName", commembertab.getrealName());
		params.addQueryStringParameter("uid", commembertab.getuId());
		params.addQueryStringParameter("action", "jobTabAdd");
		params.addQueryStringParameter("parkId", commembertab.getparkId());
		params.addQueryStringParameter("parkName", commembertab.getparkName());
		params.addQueryStringParameter("areaId", commembertab.getareaId());
		params.addQueryStringParameter("areaName", commembertab.getareaName());

		params.addQueryStringParameter("importance", commandtab.getimportance());
		params.addQueryStringParameter("nongziName", commandtab.nongziName);
		params.addQueryStringParameter("jobNote", commandtab.getcommNote());
		params.addQueryStringParameter("audioJobAssessPath", commandtab.getcommFromVPath());
		params.addQueryStringParameter("amount", commandtab.getamount());
		params.addQueryStringParameter("jobNature", "1");
		params.addQueryStringParameter("commandId", commandtab.getId());
		params.addQueryStringParameter("stdJobType", commandtab.getstdJobType());
		params.addQueryStringParameter("stdJobId", commandtab.getstdJobId());
		params.addQueryStringParameter("stdJobName", commandtab.getstdJobName());
		params.addQueryStringParameter("stdJobTypeName", commandtab.getstdJobTypeName());

		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
		{
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo)
			{
				String a = responseInfo.result;
				Result result = JSON.parseObject(responseInfo.result, Result.class);
				if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
				{
					if (result.getAffectedRows() != 0)
					{
						showProgress();
					} else
					{
						AppContext.makeToast(Common_SelectCommand.this, "error_connectDataBase");
					}
				} else
				{
					AppContext.makeToast(Common_SelectCommand.this, "error_connectDataBase");
					return;
				}
			}

			@Override
			public void onFailure(HttpException arg0, String arg1)
			{
				AppContext.makeToast(Common_SelectCommand.this, "error_connectServer");
			}
		});
	}

	private void getListData(final int actiontype, final int objtype, final PullToRefreshListView lv, final BaseAdapter adapter, final TextView more, final ProgressBar progressBar, final int PAGESIZE, int PAGEINDEX)
	{
		commembertab commembertab = AppContext.getUserInfo(this);
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("workuserid", commembertab.getId());
		params.addQueryStringParameter("userid", commembertab.getId());
		params.addQueryStringParameter("uid", commembertab.getuId());
		params.addQueryStringParameter("username", commembertab.getuserName());
		params.addQueryStringParameter("orderby", "regDate desc");
		params.addQueryStringParameter("strWhere", "");
		params.addQueryStringParameter("page_size", String.valueOf(PAGESIZE));
		params.addQueryStringParameter("page_index", String.valueOf(PAGEINDEX));
		params.addQueryStringParameter("action", "commandGetList");
		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
		{
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo)
			{
				String a = responseInfo.result;
				List<commandtab> listNewData = null;
				Result result = JSON.parseObject(responseInfo.result, Result.class);
				if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
				{
					if (result.getAffectedRows() != 0)
					{
						listNewData = JSON.parseArray(result.getRows().toJSONString(), commandtab.class);
					} else
					{
						listNewData = new ArrayList<commandtab>();
					}
				} else
				{
					AppContext.makeToast(Common_SelectCommand.this, "error_connectDataBase");
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
								for (commandtab commandtab1 : listNewData)
								{
									boolean b = false;
									for (commandtab commandtab2 : listData)
									{
										if (commandtab1.getId().equals(commandtab2.getId()))
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
							NewDataToast.makeText(Common_SelectCommand.this, getString(R.string.new_data_toast_message, newdata), appContext.isAppSound(), R.raw.newdatatoast).show();
						} else
						{
							// NewDataToast.makeText(Common_CommandList.this,
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
							for (commandtab commandtab1 : listNewData)
							{
								boolean b = false;
								for (commandtab commandtab2 : listData)
								{
									if (commandtab1.getId().equals(commandtab2.getId()))
									{
										b = true;
										break;
									}
								}
								if (!b)
									listData.add(commandtab1);
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
					AppContext.makeToast(Common_SelectCommand.this, "load_error");
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
				String a = error.getMessage();
				AppContext.makeToast(Common_SelectCommand.this, "error_connectServer");
			}
		});
	}

	private void initAnimalListView()
	{
		listAdapter = new ListViewPGCommandAdapter(this, listData);
		list_footer = getLayoutInflater().inflate(R.layout.listview_footer, null);
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
				commandtab commandtab = listData.get(position - 1);
				if (commandtab == null)
					return;
				Intent intent = new Intent(Common_SelectCommand.this, Common_CommandDetail_.class);
				intent.putExtra("bean", commandtab);// 因为list中添加了头部,因此要去掉一个
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

	class ListViewPGCommandAdapter extends BaseAdapter
	{
		private Context context;// 运行上下文
		private List<commandtab> listItems;// 数据集合
		private LayoutInflater listContainer;// 视图容器
		commandtab commandtab;

		class ListItemView
		{
			public ImageView iv_record;
			public ProgressBar pb_jd;
			public CheckBox cb_add;
			public TextView tv_jobtype;
			public TextView tv_importance;
			public TextView tv_jd;
			public TextView tv_time;
			public Button btn_sure;
			public FrameLayout fl_new;
			public CircleImageView circle_img;
			public TextView tv_new;
		}

		public ListViewPGCommandAdapter(Context context, List<commandtab> data)
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
			commandtab = listItems.get(position);
			// 自定义视图
			ListItemView listItemView = null;
			if (lmap.get(position) == null)
			{
				// 获取list_item布局文件的视图
				convertView = listContainer.inflate(R.layout.listitem_pgcommand, null);
				listItemView = new ListItemView();
				// 获取控件对象
				listItemView.fl_new = (FrameLayout) convertView.findViewById(R.id.fl_new);
				listItemView.tv_new = (TextView) convertView.findViewById(R.id.tv_new);
				listItemView.iv_record = (ImageView) convertView.findViewById(R.id.iv_record);
				listItemView.pb_jd = (ProgressBar) convertView.findViewById(R.id.pb_jd);
				listItemView.cb_add = (CheckBox) convertView.findViewById(R.id.cb_add);
				listItemView.btn_sure = (Button) convertView.findViewById(R.id.btn_sure);
				listItemView.tv_jobtype = (TextView) convertView.findViewById(R.id.tv_jobtype);
				listItemView.tv_importance = (TextView) convertView.findViewById(R.id.tv_importance);
				listItemView.tv_jd = (TextView) convertView.findViewById(R.id.tv_jd);
				listItemView.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
				listItemView.circle_img = (CircleImageView) convertView.findViewById(R.id.circle_img);
				listItemView.cb_add.setId(position);
				listItemView.btn_sure.setId(position);
				listItemView.iv_record.setId(position);
				for (int i = 0; i < joblist.size(); i++)
				{
					if (joblist.get(i).getcommandID().equals(listItems.get(position).getId()))
					{
						listItemView.cb_add.setChecked(true);
						listItemView.cb_add.setClickable(false);
					}
				}
				listItemView.iv_record.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						commandtab command = listItems.get(v.getId());
						HaveReadRecord haveReadRecord = SqliteDb.getHaveReadRecord(context, command.getId());
						if (haveReadRecord != null)
						{
							SqliteDb.updateHaveReadRecord(context, command.getId(), command.getComvidioCount());
							FrameLayout fl_new = ((ListItemView) (lmap.get(v.getId()).getTag())).fl_new;
							fl_new.setVisibility(View.GONE);
						} else
						{
							SqliteDb.saveHaveReadRecord(context, command.getId(), command.getComvidioCount());
						}
						Intent intent = new Intent(context, RecordList_.class);
						intent.putExtra("type", "2");
						intent.putExtra("workid", listItems.get(v.getId()).getId());
						String aaa = listItems.get(v.getId()).getStatusid();
						context.startActivity(intent);
					}
				});
				listItemView.cb_add.setOnCheckedChangeListener(new OnCheckedChangeListener()
				{

					@Override
					public void onCheckedChanged(CompoundButton v, boolean ischeck)
					{
						if (ischeck)
						{
							listItems_selected.add(listItems.get(v.getId()));
						} else
						{
							for (int i = 0; i < listItems_selected.size(); i++)
							{
								if (listItems_selected.get(i).getId().toString().equals(listItems.get(v.getId()).getId().toString()))
								{
									listItems_selected.remove(i);
								}
							}
						}
					}
				});
				listItemView.btn_sure.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						commandSetStatus(v, listItems.get(v.getId()));
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
			HaveReadRecord haveReadRecord = SqliteDb.getHaveReadRecord(context, commandtab.getId());
			if (haveReadRecord != null)
			{
				String num = haveReadRecord.getNum();
				if (num != null && !num.equals("") && (Integer.valueOf(num) < Integer.valueOf(commandtab.getComvidioCount())))
				{
					int num_new = Integer.valueOf(commandtab.getComvidioCount()) - Integer.valueOf(num);
					listItemView.fl_new.setVisibility(View.VISIBLE);
					listItemView.tv_new.setText(String.valueOf(num_new));
				}
			} else
			{
				SqliteDb.saveHaveReadRecord(context, commandtab.getId(), commandtab.getComvidioCount());
			}
			int commDays = Integer.valueOf(commandtab.getcommDays());
			int workDay = Integer.valueOf(commandtab.getiCount());
			listItemView.tv_jd.setText(utils.getRate(workDay, commDays) + "%");
			listItemView.pb_jd.setProgress(Integer.valueOf(utils.getRate(workDay, commDays)));
			if (commandtab.getstdJobType().equals("0") || commandtab.getstdJobType().equals("-1"))
			{
				listItemView.tv_jobtype.setText(commandtab.getcommNote().toString());
			} else
			{
				listItemView.tv_jobtype.setText(commandtab.getstdJobTypeName() + "——" + commandtab.getstdJobName());
			}
			// 反馈状态
			if (commandtab.getcommStatus().equals("2") || commandtab.getcommStatus().equals("1"))
			{
				listItemView.btn_sure.setVisibility(View.GONE);
			}
			if (commandtab.getimportance().equals("0"))
			{
				listItemView.tv_importance.setText("一般");
				listItemView.circle_img.setImageResource(R.color.bg_blue);
			} else if (commandtab.getimportance().equals("1"))
			{
				listItemView.tv_importance.setText("重要");
				listItemView.circle_img.setImageResource(R.color.bg_green);
			} else if (commandtab.getimportance().equals("2"))
			{
				listItemView.tv_importance.setText("非常重要");
				listItemView.circle_img.setImageResource(R.color.color_orange);
			}
			listItemView.tv_time.setText(commandtab.getregDate().substring(0, commandtab.getregDate().lastIndexOf(":")));
			return convertView;
		}
	}

	private void saveSelectedCommand(List<plantgrowthtab> listItems_selected)
	{
		for (int i = 0; i < listItems_selected.size(); i++)
		{
			SqliteDb.save(this, listItems_selected.get(i));
		}
	}

	private void showProgress()
	{
		latch.countDown();
		Long l = latch.getCount();
		if (l.intValue() == 0) // 全部线程是否已经结束
		{
			Toast.makeText(this, "选择成功！", Toast.LENGTH_SHORT).show();
			finish();
		}
	}

	private void commandSetStatus(final View v, commandtab commandtab)
	{
		commembertab commembertab = AppContext.getUserInfo(this);
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("userid", commembertab.getId());
		params.addQueryStringParameter("userName", commembertab.getrealName());
		params.addQueryStringParameter("uid", commembertab.getuId());
		params.addQueryStringParameter("action", "commandSetStatus");

		params.addQueryStringParameter("statusid", commandtab.getStatusid());
		params.addQueryStringParameter("commStatus", "1");
		params.addQueryStringParameter("feedbackNote", "");
		params.addQueryStringParameter("feedbackDate", "");
		params.addQueryStringParameter("confirmDate", utils.getToday());
		params.addQueryStringParameter("finishDate", "");
		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
		{
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo)
			{
				String a = responseInfo.result;
				List<jobtab> listData = null;
				Result result = JSON.parseObject(responseInfo.result, Result.class);
				if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
				{
					listData = JSON.parseArray(result.getRows().toJSONString(), jobtab.class);
					if (listData == null)
					{
						AppContext.makeToast(Common_SelectCommand.this, "error_connectDataBase");
					} else
					{
						v.setVisibility(View.GONE);
					}

				} else
				{
					AppContext.makeToast(Common_SelectCommand.this, "error_connectDataBase");
					return;
				}
			}

			@Override
			public void onFailure(HttpException error, String arg1)
			{
				AppContext.makeToast(Common_SelectCommand.this, "error_connectServer");
			}
		});
	}

	class TimeThread extends Thread
	{
		private boolean isSleep = true;
		private boolean stop = false;

		public void run()
		{
			Long starttime = 0l;
			while (!stop)
			{
				if (isSleep)
				{
				} else
				{
					try
					{
						Thread.sleep(AppContext.TIME_REFRESH);
						starttime = starttime + 1000;
						getListData(UIHelper.LISTVIEW_ACTION_REFRESH, UIHelper.LISTVIEW_DATATYPE_NEWS, frame_listview_news, listAdapter, list_foot_more, list_foot_progress, AppContext.PAGE_SIZE, 0);
					} catch (InterruptedException e)
					{
						e.printStackTrace();
					}
				}
			}
		}

		public void setSleep(boolean sleep)
		{
			isSleep = sleep;
		}

		public void setStop(boolean stop)
		{
			this.stop = stop;
		}
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		timethread.setStop(true);
		timethread.interrupt();
		timethread = null;
	}
}
