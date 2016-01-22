package com.farm.ui;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.farm.R;
import com.farm.adapter.SelectorFirstItemAdapter;
import com.farm.adapter.SelectorSecondItemAdapter;
import com.farm.app.AppContext;
import com.farm.bean.Dictionary;
import com.farm.bean.SelectRecords;
import com.farm.common.SqliteDb;
import com.farm.widget.PullToRefreshListView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressLint("NewApi")
@EFragment
public class SelectorFragment extends Fragment implements OnClickListener
{
    String strWhere = "";
    String orderby = "";
    private ListView mainlist;
    private ListView morelist;
    private Button btn_reset;
    private Button btn_sure;
    private List<Map<String, Object>> mainList;
    SelectorFirstItemAdapter mainAdapter;
    SelectorSecondItemAdapter moreAdapter;
    public List<View> list_state = new ArrayList<View>();
    PopupWindow popupWindow_recent;
    View popupWindowView_recent;
    PopupWindow popupWindow_selector;
    View popupWindowView_selector;
    private AppContext appContext;// 全局Context
    @ViewById
    PullToRefreshListView frame_listview_news;
    @ViewById
    HorizontalScrollView hs_selected;
    @ViewById
    LinearLayout ll_selected;
    @ViewById
    RelativeLayout rl_selector;
    @ViewById
    RelativeLayout rl_recent;
    @ViewById
    ImageView iv_up_recent;
    @ViewById
    ImageView iv_up_selector;
    @ViewById
    TextView tv_recent;
    @ViewById
    View line;
    // ListView lv_type;
    // FrameLayout container;
    CheckBox cb_1;
    RadioButton rb_bz;
    int oldpostion = 0;
    List<SelectRecords> list_SelectRecords;
    public Boolean state_hs_selected = false;
    String title;
    Dictionary dictionary;

    @Click
    void rl_selector()
    {
        iv_up_selector.setBackground(getResources().getDrawable(R.drawable.upward));
        showPop_selector();
    }

    @Click
    void rl_recent()
    {
        iv_up_recent.setBackground(getResources().getDrawable(R.drawable.upward));
        showPop_recent();
    }

    @AfterViews
    void afterOncreate()
    {
        initState();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.selectorui, container, false);
        dictionary = (Dictionary) getArguments().getSerializable("bean");
        return rootView;
    }

    public void showPop_recent()
    {
        // rl_recent.setBackgroundColor(getResources().getColor(R.color.line_color));
        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
        popupWindowView_recent = layoutInflater.inflate(R.layout.popup_recent, null);// 外层
        popupWindowView_recent.setOnKeyListener(new OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if ((keyCode == KeyEvent.KEYCODE_MENU) && (popupWindow_recent.isShowing()))
                {
                    popupWindow_recent.dismiss();
                    rl_recent.setBackgroundColor(getResources().getColor(R.color.white));
                    WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                    lp.alpha = 1f;
                    getActivity().getWindow().setAttributes(lp);
                    return true;
                }
                return false;
            }
        });
        popupWindowView_recent.setOnTouchListener(new OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if (popupWindow_recent.isShowing())
                {
                    popupWindow_recent.dismiss();
                    rl_recent.setBackgroundColor(getResources().getColor(R.color.white));
                    iv_up_recent.setBackground(getResources().getDrawable(R.drawable.downward));
                    WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                    lp.alpha = 1f;
                    getActivity().getWindow().setAttributes(lp);
                }
                return false;
            }
        });
        popupWindow_recent = new PopupWindow(popupWindowView_recent, LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, true);
        popupWindow_recent.showAsDropDown(rl_recent, 0, 0);
        popupWindow_recent.setOutsideTouchable(true);

        final List<String> data = new ArrayList<String>();
        for (int i = 0; i < dictionary.getSORT().size(); i++)
        {
            data.add(dictionary.getSORT().get(i));
        }
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = 0.7f;
        getActivity().getWindow().setAttributes(lp);
        ListView listview = (ListView) popupWindowView_recent.findViewById(R.id.lv_recent);
        listview.setAdapter(new recentAdapter(getActivity(), data));
        listview.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View v, int postion, long arg3)
            {
                if (postion == 0)
                {
                    orderby = "0";
                } else
                {
                    orderby = "1";
                }
                tv_recent.setText(data.get(postion));
                tv_recent.setTextColor(getResources().getColor(R.color.light_blue));
                iv_up_recent.setBackground(getResources().getDrawable(R.drawable.downward));
                popupWindow_recent.dismiss();
                Intent intent = new Intent();
                intent.setAction(AppContext.BROADCAST_UPDATEPCMD_SORT);
                getActivity().sendBroadcast(intent);
            }
        });
    }

    public class recentAdapter extends BaseAdapter
    {
        private Context context;
        private List<String> listItems;
        private LayoutInflater listContainer;
        String type;

        class ListItemView
        {
            public TextView tv_yq;
        }

        public recentAdapter(Context context, List<String> data)
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
                    frame_listview_news.setAlpha(1f);
                    rl_selector.setBackgroundColor(getResources().getColor(R.color.white));
                    WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                    lp.alpha = 1f;
                    getActivity().getWindow().setAttributes(lp);
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
                    popupWindow_selector.dismiss();
                    frame_listview_news.setAlpha(1f);
                    rl_selector.setBackgroundColor(getResources().getColor(R.color.white));
                    iv_up_selector.setBackground(getResources().getDrawable(R.drawable.downward));
                    WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                    lp.alpha = 1f;
                    getActivity().getWindow().setAttributes(lp);
                }
                return false;
            }
        });
        popupWindow_selector = new PopupWindow(popupWindowView_selector, LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, true);
        if (hs_selected.isShown())
        {
            popupWindow_selector.showAsDropDown(hs_selected, 0, 0);
        } else
        {
            popupWindow_selector.showAsDropDown(rl_recent, 0, 0);
        }
        popupWindow_selector.setOutsideTouchable(true);
        ColorDrawable cd = new ColorDrawable(0x000000);
        popupWindow_selector.setBackgroundDrawable(cd);
        frame_listview_news.setAlpha(0.2f);
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = 0.7f;
        getActivity().getWindow().setAttributes(lp);
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
        WindowManager.LayoutParams lp;
        Intent intent = null;
        switch (v.getId())
        {
            case R.id.cb_1:
                // addTextiew(cb_1.getText().toString());
                break;
            case R.id.rb_bz:
                // addTextiew(rb_bz.getText().toString());
                break;
            case R.id.btn_sure:
                popupWindow_selector.dismiss();
                rl_selector.setBackgroundColor(getResources().getColor(R.color.white));
                iv_up_selector.setBackground(getResources().getDrawable(R.drawable.downward));
                list_SelectRecords = SqliteDb.getSelectRecordByFirstTypetemp(getActivity(), SelectRecords.class, dictionary.getBELONG().toString());
                intent = new Intent();
                intent.setAction(AppContext.BROADCAST_UPDATEPLANT);
                getActivity().sendBroadcast(intent);
                lp = getActivity().getWindow().getAttributes();
                lp.alpha = 1f;
                getActivity().getWindow().setAttributes(lp);
                break;
            case R.id.btn_reset:
                popupWindow_selector.dismiss();
                rl_selector.setBackgroundColor(getResources().getColor(R.color.white));
                iv_up_selector.setBackground(getResources().getDrawable(R.drawable.downward));
                SqliteDb.deleteAllRecordtemp(getActivity(), SelectRecords.class, dictionary.getBELONG());
                ll_selected.removeAllViews();
                hs_selected.setVisibility(View.GONE);
                intent = new Intent();
                intent.setAction(AppContext.BROADCAST_UPDATEPLANT);
                getActivity().sendBroadcast(intent);
                lp = getActivity().getWindow().getAttributes();
                lp.alpha = 1f;
                getActivity().getWindow().setAttributes(lp);
                break;

            default:
                break;
        }
    }

    public void addTextiew(TextView textView)
    {
        if (!hs_selected.isShown())
        {
            hs_selected.setVisibility(View.VISIBLE);
            popupWindow_selector.showAsDropDown(hs_selected, 0, 0);
            popupWindow_selector.update(hs_selected, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        }
        ll_selected.addView(textView);
        list_state.add(textView);
    }

    public void removeTextiew(String str)
    {
        if (!hs_selected.isShown())
        {
            hs_selected.setVisibility(View.VISIBLE);
            popupWindow_selector.showAsDropDown(hs_selected, 0, 0);
            popupWindow_selector.update(hs_selected, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        }
        for (int i = 0; i < ll_selected.getChildCount(); i++)
        {
            TextView textView = (TextView) (ll_selected.getChildAt(i));
            if (textView.getText().toString().equals(str))
            {
                ll_selected.removeViewAt(i);
                list_state.remove(textView);
            }
        }

    }

    public class TypeAdapter extends BaseAdapter
    {
        private Context context;
        private List<String> listItems;
        private LayoutInflater listContainer;
        String type;

        class ListItemView
        {
            public ImageView iv_point;
            public TextView tv_tpye;
        }

        public TypeAdapter(Context context, List<String> data)
        {
            this.context = context;
            this.listContainer = LayoutInflater.from(context);
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
                convertView = listContainer.inflate(R.layout.type_item, null);
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

    private void initView(View v)
    {
        mainlist = (ListView) v.findViewById(R.id.classify_mainlist);
        morelist = (ListView) v.findViewById(R.id.classify_morelist);
        mainAdapter = new SelectorFirstItemAdapter(getActivity(), mainList);
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

    private void initAdapter(String BELONG, String fitstItemId, String firstItemName, List<String> secondItemid, List<String> secondItemName)
    {
        moreAdapter = new SelectorSecondItemAdapter(this, getActivity(), BELONG, fitstItemId, firstItemName, secondItemid, secondItemName, list_state);
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

    private void initState()
    {
        list_SelectRecords = SqliteDb.getSelectRecordByFirstTypetemp(getActivity(), SelectRecords.class, dictionary.getBELONG().toString());
        if (list_SelectRecords.size() != 0)
        {
            hs_selected.setVisibility(View.VISIBLE);
        }
        for (int i = 0; i < list_SelectRecords.size(); i++)
        {
            String str = list_SelectRecords.get(i).getFirsttype() + "\n" + list_SelectRecords.get(i).getSecondtype();
            TextView textView = new TextView(getActivity());
            textView.setBackgroundColor(getActivity().getResources().getColor(R.color.bg_selectitem));
            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(140, LinearLayout.LayoutParams.MATCH_PARENT);
            p.setMargins(15, 15, 0, 15);
            textView.setLayoutParams(p);
            textView.setText(str);
            textView.setTextSize(10f);
            textView.setTextColor(getActivity().getResources().getColor(android.R.color.holo_red_dark));
            textView.setGravity(Gravity.CENTER);
            ll_selected.addView(textView);
            list_state.add(textView);
        }
    }

    public void hidehs_selected()
    {
        hs_selected.setVisibility(View.GONE);
        state_hs_selected = false;
        popupWindow_selector.showAsDropDown(rl_recent, 0, 0);
        popupWindow_selector.update(rl_recent, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
    }

    public void showhs_selected()
    {
        hs_selected.setVisibility(View.VISIBLE);
        state_hs_selected = true;
        popupWindow_selector.showAsDropDown(hs_selected, 0, 0);
        popupWindow_selector.update(hs_selected, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
    }

    public void setStrWhere(String strWhere)
    {
        this.strWhere = strWhere;
    }

    public String getStrWhere()
    {
        return strWhere;
    }

    public void setOrderby(String orderby)
    {
        this.orderby = orderby;
    }

    public String getOrderby()
    {
        return orderby;
    }
}
