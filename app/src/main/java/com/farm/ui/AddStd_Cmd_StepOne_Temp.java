package com.farm.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextPaint;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.adapter.Attendance_Park_Adapter;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.Dictionary;
import com.farm.bean.Dictionary_wheel;
import com.farm.bean.Result;
import com.farm.bean.commembertab;
import com.farm.com.custominterface.FragmentCallBack;
import com.farm.common.DictionaryHelper;
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
import java.util.List;

/**
 * Created by ${hmj} on 2015/12/15.
 */
@EFragment
public class AddStd_Cmd_StepOne_Temp extends Fragment
{
    PopupWindow pw_command;
    View pv_command;
    commembertab commembertab;
    String[] fn;
    Dictionary_wheel dictionary_wheel;
    Dictionary dic_park;
    Dictionary dic_area;
    FragmentCallBack fragmentCallBack = null;
    SelectorFragment selectorUi;
    Fragment mContent = new Fragment();
    private String[] list;
    private TextView[] tvList;
    private View[] views;
    private LayoutInflater inflater;
    private int currentItem = 0;
    private ShopAdapter shopAdapter;

    @ViewById
    ImageView iv_dowm_tab;
    @ViewById
    HorizontalScrollView cmd_tools_scrlllview;
    @ViewById
    LinearLayout cmd_tools;
    @ViewById
    ViewPager cmd_pager;
    @ViewById
    TextView toptype;
    @ViewById
    RelativeLayout rl_pb;
    @ViewById
    RelativeLayout pb_cmd;
    @ViewById
    LinearLayout ll_tip;
    @ViewById
    RelativeLayout rl_zw;
    @ViewById
    ProgressBar pb;
    @ViewById
    TextView tv_tip;
    @ViewById
    TextView tv_zw;
    @ViewById
    View line;

    @Click
    void rl_zw()
    {
        getZW();
    }

    @AfterViews
    void afterOncreate()
    {
        inflater = LayoutInflater.from(getActivity());
//        getCommandlist();
        getZW_first();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.add_std__cmd__step_one_temp, container, false);
        commembertab = AppContext.getUserInfo(getActivity());
        return rootView;
    }

    public void setTopType(String st)
    {
        toptype.setText(st);
        toptype.setTextColor(0xFFFF5D5E);
//        toptype.setTextSize(getActivity().getResources().getDimension(R.dimen.size_sp_9));
        TextPaint tp = toptype.getPaint();
        tp.setFakeBoldText(true);
    }

    /**
     * 动态生成显示items中的textview
     */
    private void showToolsView(String[] data)
    {
        cmd_tools.removeAllViews();
        list = data;
        tvList = new TextView[list.length];
        views = new View[list.length];

        for (int i = 0; i < list.length; i++)
        {
            View view = inflater.inflate(R.layout.item_addstd_cmdlist, null);
            view.setId(i);
            view.setOnClickListener(toolsItemListener);
            TextView textView = (TextView) view.findViewById(R.id.text);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT, 1);
            lp.setMargins(40, 1, 40, 1);
            textView.setLayoutParams(lp);
            textView.setText(list[i]);
            cmd_tools.addView(view);
            tvList[i] = textView;
            views[i] = view;
        }
        changeTextColor(0);
    }

    private View.OnClickListener toolsItemListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            cmd_pager.setCurrentItem(v.getId());
        }
    };

    /**
     * initPager<br/>
     * 初始化ViewPager控件相关内容
     */
    private void initPager()
    {
        shopAdapter = new ShopAdapter(getActivity().getSupportFragmentManager());
        cmd_pager.setAdapter(shopAdapter);
        cmd_pager.setOnPageChangeListener(onPageChangeListener);
    }

    /**
     * OnPageChangeListener<br/>
     * 监听ViewPager选项卡变化事的事件
     */
    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener()
    {
        @Override
        public void onPageSelected(int arg0)
        {
            if (cmd_pager.getCurrentItem() != arg0) cmd_pager.setCurrentItem(arg0);
            if (currentItem != arg0)
            {
                changeTextColor(arg0);
                changeTextLocation(arg0);
            }
            currentItem = arg0;
        }

        @Override
        public void onPageScrollStateChanged(int arg0)
        {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2)
        {
        }
    };

    /**
     * ViewPager 加载选项卡
     *
     * @author Administrator
     */
    private class ShopAdapter extends FragmentPagerAdapter
    {
        public ShopAdapter(FragmentManager fm)
        {
            super(fm);
        }

        @Override
        public Fragment getItem(int index)
        {
            Fragment fragment = new CmdList_Cmd_Fragment();
            Bundle bundle = new Bundle();
            bundle.putInt("index", index);
            bundle.putString("FN", dictionary_wheel.getFirstItemName()[index]);
            bundle.putString("FI", dictionary_wheel.getFirstItemID()[index]);
            bundle.putStringArray("SI", dictionary_wheel.getSecondItemID().get(dictionary_wheel.getFirstItemID()[index]));
            bundle.putStringArray("SN", dictionary_wheel.getSecondItemName().get(fn[index]));
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public int getCount()
        {
            return list.length;
        }
    }

    /**
     * 改变textView的颜色
     *
     * @param id
     */
    private void changeTextColor(int id)
    {
        for (int i = 0; i < tvList.length; i++)
        {
            if (i != id)
            {
//                tvList[i].setBackgroundColor(0x00000000);
                tvList[i].setTextColor(0xFF000000);
                TextPaint tp = tvList[i].getPaint();
//                tvList[i].setTextSize(getActivity().getResources().getDimension(R.dimen.size_sp_7));
                tp.setFakeBoldText(false);
            }
        }

//        tvList[id].setBackgroundColor(0xFFFFFFFF);
        tvList[id].setTextColor(0xFFFF5D5E);
//        tvList[id].setTextSize(getActivity().getResources().getDimension(R.dimen.size_sp_7));
        TextPaint tp = tvList[id].getPaint();
        tp.setFakeBoldText(true);
    }

    /**
     * 改变栏目位置
     *
     * @param clickPosition
     */
    private void changeTextLocation(int clickPosition)
    {
        int x = (views[clickPosition].getTop());
        cmd_tools_scrlllview.smoothScrollTo(0, x);
    }

    private void getZW()
    {
        List<String> list = new ArrayList<>();
        list.add("香蕉");
        list.add("芒果");
        list.add("柑橘");
        showPop_user(list);
    }

    private void getZW_first()
    {
        rl_pb.setVisibility(View.VISIBLE);
        List<String> list = new ArrayList<>();
        list.add("香蕉");
        list.add("芒果");
        list.add("柑橘");
        rl_pb.setVisibility(View.GONE);
        getCommandlist(list.get(0));
    }

    private void getCommandlist(String zw)
    {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("zw", "");
        params.addQueryStringParameter("name", "Zuoye");
        params.addQueryStringParameter("action", "getDict");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String a = responseInfo.result;
                List<Dictionary> lsitNewData = null;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)
                {
                    if (result.getAffectedRows() != 0)
                    {
                        pb_cmd.setVisibility(View.GONE);
                        String aa = result.getRows().toJSONString();
                        lsitNewData = JSON.parseArray(result.getRows().toJSONString(), Dictionary.class);
                        if (lsitNewData != null)
                        {
                            dic_area = lsitNewData.get(0);
                            dic_area.setBELONG("片区执行");
                            dictionary_wheel = DictionaryHelper.getDictionary_Command(dic_area);
                            fn = dictionary_wheel.getFirstItemName();
                            showToolsView(fn);
                            initPager();
                        }

                    } else
                    {
                        ll_tip.setVisibility(View.VISIBLE);
                        tv_tip.setText("暂无数据！");
                        pb.setVisibility(View.GONE);
                    }
                } else
                {
                    ll_tip.setVisibility(View.VISIBLE);
                    tv_tip.setText("数据加载异常！");
                    pb.setVisibility(View.GONE);
                    return;
                }
            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                ll_tip.setVisibility(View.VISIBLE);
                tv_tip.setText("网络连接异常！");
                pb.setVisibility(View.GONE);
            }
        });
    }

    public void showPop_user(final List<String> list)
    {
        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
        pv_command = layoutInflater.inflate(R.layout.pop_attendance, null);// 外层
        pv_command.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if ((keyCode == KeyEvent.KEYCODE_MENU) && (pw_command.isShowing()))
                {
                    pw_command.dismiss();
                    WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                    lp.alpha = 1f;
                    getActivity().getWindow().setAttributes(lp);
                    return true;
                }
                return false;
            }
        });
        pv_command.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if (pw_command.isShowing())
                {
                    pw_command.dismiss();
                    WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                    lp.alpha = 1f;
                    getActivity().getWindow().setAttributes(lp);
                }
                return false;
            }
        });
        pw_command = new PopupWindow(pv_command, LinearLayout.LayoutParams.MATCH_PARENT, 600, true);
        pw_command.showAsDropDown(line, 0, 0);
        pw_command.setOutsideTouchable(true);
        ListView lv = (ListView) pv_command.findViewById(R.id.lv);
        Attendance_Park_Adapter attendance_park_adapter = new Attendance_Park_Adapter(getActivity(), list);
        lv.setAdapter(attendance_park_adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                pb_cmd.setVisibility(View.VISIBLE);
                tv_zw.setText(list.get(position));
                getCommandlist(list.get(position));
                pw_command.dismiss();
                WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                lp.alpha = 1f;
                getActivity().getWindow().setAttributes(lp);
            }
        });
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = 0.7f;
        getActivity().getWindow().setAttributes(lp);
    }


    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        fragmentCallBack = (FragmentCallBack) activity;
    }
}
