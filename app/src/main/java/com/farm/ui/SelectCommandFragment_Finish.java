package com.farm.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.Dictionary;
import com.farm.bean.Result;
import com.farm.bean.commandtab;
import com.farm.bean.commembertab;
import com.farm.bean.jobtab;
import com.farm.bean.plantgrowthtab;
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
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@EFragment
public class SelectCommandFragment_Finish extends Fragment implements OnClickListener
{
    boolean ishidding=false;
    commembertab commembertab;
    TimeThread timethread;
//    private List<jobtab> joblist;
    CountDownLatch latch;
    private List<commandtab> listItems_selected = new ArrayList<commandtab>();// 数据集合
    SelectorFragment_Support selectorUi;
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
    PullToRefreshListView frame_listview_news;
    @ViewById
    ImageButton btn_more;
    Dictionary dictionary;

    @Click
    void btn_more()
    {
        Intent intent = new Intent(getActivity(), Command_more_.class);
        intent.putExtra("workuserid", commembertab.getId());
        startActivity(intent);
    }


    @Override
    public void onHiddenChanged(boolean hidden)
    {
        ishidding=hidden;
        super.onHiddenChanged(hidden);
        if (!hidden)
        {
            if (timethread != null)
            {
                timethread.setSleep(false);
            }
        } else
        {
            if (timethread != null)
            {
                timethread.setSleep(true);
            }
        }
    }

    @AfterViews
    void afterOncreate()
    {
//        dictionary = DictionaryHelper.getDictionaryFromAssess(getActivity(), "NCZ_CMD");
//        selectorUi = new SelectorFragment_Support_();
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("bean", dictionary);
//        selectorUi.setArguments(bundle);
//        switchContent(mContent, selectorUi);
        initAnimalListView();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.selectcommandfragment_finish, container, false);
        commembertab = AppContext.getUserInfo(getActivity());
        appContext = (AppContext) getActivity().getApplication();
        IntentFilter intentfilter_update = new IntentFilter(AppContext.BROADCAST_UPDATEPLANT);
        getActivity().registerReceiver(receiver_update, intentfilter_update);
        timethread = new TimeThread();
        timethread.setStop(false);
        timethread.setSleep(false);
        timethread.start();
        return rootView;
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
        commembertab commembertab = AppContext.getUserInfo(getActivity());
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("workuserid", commembertab.getId());
        params.addQueryStringParameter("userid", commembertab.getId());
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("username", commembertab.getuserName());
        params.addQueryStringParameter("orderby", "regDate desc");
        params.addQueryStringParameter("strWhere", "zt:2");
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
                    AppContext.makeToast(getActivity(), "error_connectDataBase");
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
                                NewDataToast.makeText(getActivity(), getString(R.string.new_data_toast_message, newdata), appContext.isAppSound(), R.raw.newdatatoast).show();
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
                                        if (!b) listData.add(commandtab1);
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
                    AppContext.makeToast(getActivity(), "load_error");
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
                AppContext.makeToast(getActivity(), "error_connectServer");
            }
        });
    }

    private void initAnimalListView()
    {
        listAdapter = new ListViewPGCommandAdapter(getActivity(), listData);
        list_footer = getActivity().getLayoutInflater().inflate(R.layout.listview_footer, null);
        list_foot_more = (TextView) list_footer.findViewById(R.id.listview_foot_more);
        list_foot_progress = (ProgressBar) list_footer.findViewById(R.id.listview_foot_progress);
        frame_listview_news.addFooterView(list_footer);// 添加底部视图 必须在setAdapter前
        frame_listview_news.setAdapter(listAdapter);
        frame_listview_news.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                // 点击头部、底部栏无效
                if (position == 0 || view == list_footer) return;

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
                if (commandtab == null) return;

                commembertab commembertab = AppContext.getUserInfo(getActivity());
                AppContext.updateStatus(getActivity(), "0", commandtab.getId(), "2", commembertab.getId());

                Intent intent = new Intent(getActivity(), CommandDetail_Edit_.class);
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
                if (listData.isEmpty()) return;

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
                intent = new Intent(getActivity(), AddStandardCommand_.class);
                startActivity(intent);
                break;
            case R.id.btn_nonstandardprocommand:
                intent = new Intent(getActivity(), AddNotStandardCommand_.class);
                startActivity(intent);
                break;
            case R.id.btn_nonprocommand:
                intent = new Intent(getActivity(), AddNotProductCommand_.class);
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
            public TextView tv_jobtype;
            public TextView tv_importance;
            public TextView tv_jd;
            public TextView tv_time;
            public TextView tv_type;
            public TextView tv_zf;
            public Button btn_sure;
            public FrameLayout fl_new;
            public LinearLayout ll_main;
            public FrameLayout fl_new_item;
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
                convertView = listContainer.inflate(R.layout.selectorcommand_finishadapter, null);
                listItemView = new ListItemView();
                // 获取控件对象
                listItemView.ll_main = (LinearLayout) convertView.findViewById(R.id.ll_main);
                listItemView.fl_new = (FrameLayout) convertView.findViewById(R.id.fl_new);
                listItemView.fl_new_item = (FrameLayout) convertView.findViewById(R.id.fl_new_item);
                listItemView.tv_new = (TextView) convertView.findViewById(R.id.tv_new);
                listItemView.iv_record = (ImageView) convertView.findViewById(R.id.iv_record);
                listItemView.pb_jd = (ProgressBar) convertView.findViewById(R.id.pb_jd);
                listItemView.btn_sure = (Button) convertView.findViewById(R.id.btn_sure);
                listItemView.tv_jobtype = (TextView) convertView.findViewById(R.id.tv_jobtype);
                listItemView.tv_importance = (TextView) convertView.findViewById(R.id.tv_importance);
                listItemView.tv_jd = (TextView) convertView.findViewById(R.id.tv_jd);
                listItemView.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
                listItemView.tv_type = (TextView) convertView.findViewById(R.id.tv_type);
                listItemView.tv_zf = (TextView) convertView.findViewById(R.id.tv_zf);
                listItemView.circle_img = (CircleImageView) convertView.findViewById(R.id.circle_img);
                listItemView.btn_sure.setId(position);
                listItemView.iv_record.setId(position);
                listItemView.iv_record.setOnClickListener(new OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        commandtab command = listItems.get(v.getId());
                        commembertab commembertab = AppContext.getUserInfo(context);
                        AppContext.updateStatus(context, "1", command.getId(), "2", commembertab.getId());

                        Intent intent = new Intent(context, RecordList_.class);
                        intent.putExtra("type", "2");
                        intent.putExtra("workid", listItems.get(v.getId()).getId());
                        String aaa = listItems.get(v.getId()).getStatusid();
                        context.startActivity(intent);
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
            if (Integer.valueOf(commandtab.getComCount()) > 0)
            {
                listItemView.fl_new_item.setVisibility(View.VISIBLE);
            } else
            {
                listItemView.fl_new_item.setVisibility(View.GONE);
            }
            if (Integer.valueOf(commandtab.getComvidioCount()) > 0)
            {
                listItemView.fl_new.setVisibility(View.VISIBLE);
            } else
            {
                listItemView.fl_new.setVisibility(View.GONE);
            }
//            int commDays = Integer.valueOf(commandtab.getcommDays());
//            int workDay = Integer.valueOf(commandtab.getiCount());
            if (!commandtab.getfeedbackuserName().equals(""))
            {
                listItemView.tv_jd.setText(commandtab.getfeedbackuserName() + "%");
                listItemView.pb_jd.setProgress(Integer.valueOf(commandtab.getfeedbackuserName()));
            }

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
//                listItemView.circle_img.setImageResource(R.color.bg_blue);
                listItemView.circle_img.setImageResource(R.drawable.yb);
            } else if (commandtab.getimportance().equals("1"))
            {
                listItemView.tv_importance.setText("重要");
//                listItemView.circle_img.setImageResource(R.color.bg_green);
                listItemView.circle_img.setImageResource(R.drawable.zyx);
            } else if (commandtab.getimportance().equals("2"))
            {
                listItemView.tv_importance.setText("非常重要");
//                listItemView.circle_img.setImageResource(R.color.color_orange);
                listItemView.circle_img.setImageResource(R.drawable.fczy);
            }
            listItemView.tv_time.setText(commandtab.getregDate());
            if (commandtab.getcommFromVPath().equals("0"))
            {
                listItemView.tv_zf.setText(commandtab.getcommFromName()+"下发");
            } else
            {
                listItemView.tv_zf.setText(commandtab.getcommFromName()+"自发");
            }
            if (commandtab.getstdJobType().equals("0"))
            {
                listItemView.tv_type.setText("非标准生产指令");
            } else if (commandtab.getstdJobType().equals("-1"))
            {
                listItemView.tv_type.setText("非生产指令");
            } else
            {
                listItemView.tv_type.setText("标准生产指令");
            }

            return convertView;
        }
    }

    private void saveSelectedCommand(List<plantgrowthtab> listItems_selected)
    {
        for (int i = 0; i < listItems_selected.size(); i++)
        {
            SqliteDb.save(getActivity(), listItems_selected.get(i));
        }
    }


    private void commandSetStatus(final View v, commandtab commandtab)
    {
        commembertab commembertab = AppContext.getUserInfo(getActivity());
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
                        AppContext.makeToast(getActivity(), "error_connectDataBase");
                    } else
                    {
                        v.setVisibility(View.GONE);
                    }

                } else
                {
                    AppContext.makeToast(getActivity(), "error_connectDataBase");
                    return;
                }
            }

            @Override
            public void onFailure(HttpException error, String arg1)
            {
                AppContext.makeToast(getActivity(), "error_connectServer");
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
                        timethread.sleep(AppContext.TIME_REFRESH);
                        starttime = starttime + 1000;
                        getListData(UIHelper.LISTVIEW_ACTION_REFRESH, UIHelper.LISTVIEW_DATATYPE_NEWS, frame_listview_news, listAdapter, list_foot_more, list_foot_progress, AppContext.PAGE_SIZE, 0);
                        timethread.setSleep(false);
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
    public void onDestroyView()
    {
        super.onDestroyView();
        timethread.setStop(true);
        timethread.interrupt();
        timethread = null;
    }
}
