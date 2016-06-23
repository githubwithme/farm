package com.farm.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.adapter.Attendance_Park_Adapter;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.AllType;
import com.farm.bean.Dictionary;
import com.farm.bean.Dictionary_wheel;
import com.farm.bean.Result;
import com.farm.bean.commembertab;
import com.farm.common.DictionaryHelper;
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
import java.util.List;

/**
 * Created by hasee on 2016/6/14.
 */
@EActivity(R.layout.pg_workplan)
public class PG_Workplan extends FragmentActivity
{
    @ViewById
    EditText work_nr;
    @ViewById
    Button btn_nr;

    private int currentItem = 0;
    @ViewById
    ViewPager cmd_pager;
    private LayoutInflater inflater;
    private View[] views;
    private String[] list;
    private TextView[] tvList;
    private ShopAdapter shopAdapter;
    @ViewById
    HorizontalScrollView cmd_tools_scrlllview;
    @ViewById
    LinearLayout cmd_tools;
    String[] fn;
    Dictionary_wheel dictionary_wheel;
    Dictionary dic_park;
    Dictionary dic_area;
    @ViewById
    RelativeLayout pb_cmd;
    @ViewById
    RelativeLayout rl_zw;
    @ViewById
    TextView tv_zw;
    @ViewById
    View line;
    PopupWindow pw_command;
    View pv_command;
    commembertab commembertab;
    String zyid;
    String zyname;
    static String zynames;
    List<AllType> listdata = new ArrayList<AllType>();

    @Click
    void rl_zw()
    {
        getZW();
    }
    @AfterViews
    void afview()
    {
        inflater = LayoutInflater.from(PG_Workplan.this);
        commembertab = AppContext.getUserInfo(PG_Workplan.this);
        getlistdata();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
    }

    private void getlistdata()
    {
        commembertab commembertab = AppContext.getUserInfo(PG_Workplan.this);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("action", "getProduct");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String a = responseInfo.result;
                List<AllType> listNewData = null;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    if (result.getAffectedRows() != 0)
                    {
                        listNewData = JSON.parseArray(result.getRows().toJSONString(), AllType.class);
                        zyid = listNewData.get(0).getId();
                        zyname = listNewData.get(0).getProductName();
                        listdata.addAll(listNewData);
                        tv_zw.setText(zyname);
                        zynames = listNewData.get(0).getProductName();
                        getCommandlist(zyid, zyname);
                    } else
                    {
                        listNewData = new ArrayList<AllType>();
                    }
                } else
                {
                    AppContext.makeToast(PG_Workplan.this, "error_connectDataBase");

                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                String a = error.getMessage();
                AppContext.makeToast(PG_Workplan.this, "error_connectServer");

            }
        });
    }
    private void getZW()
    {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < listdata.size(); i++)
        {
            list.add(listdata.get(i).getProductName());
        }
        showPop_user(list);
    }
    public void showPop_user(final List<String> list)
    {
        LayoutInflater layoutInflater = (LayoutInflater)PG_Workplan.this.getSystemService(PG_Workplan.this.LAYOUT_INFLATER_SERVICE);
        pv_command = layoutInflater.inflate(R.layout.pop_attendance, null);// 外层
        pv_command.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if ((keyCode == KeyEvent.KEYCODE_MENU) && (pw_command.isShowing()))
                {
                    pw_command.dismiss();
                    WindowManager.LayoutParams lp = PG_Workplan.this.getWindow().getAttributes();
                    lp.alpha = 1f;
                    PG_Workplan.this.getWindow().setAttributes(lp);
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
                    WindowManager.LayoutParams lp = PG_Workplan.this.getWindow().getAttributes();
                    lp.alpha = 1f;
                    PG_Workplan.this.getWindow().setAttributes(lp);
                }
                return false;
            }
        });
        pw_command = new PopupWindow(pv_command, LinearLayout.LayoutParams.MATCH_PARENT, 600, true);
        pw_command.showAsDropDown(line, 0, 0);
        pw_command.setOutsideTouchable(true);
        ListView lv = (ListView) pv_command.findViewById(R.id.lv);
        Attendance_Park_Adapter attendance_park_adapter = new Attendance_Park_Adapter(PG_Workplan.this, list);
        lv.setAdapter(attendance_park_adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                pb_cmd.setVisibility(View.VISIBLE);
                tv_zw.setText(list.get(position));
                getCommandlist(listdata.get(position).getId(), listdata.get(position).getProductName());
                zyid = listdata.get(position).getId();
                zyname = listdata.get(position).getProductName();
                pw_command.dismiss();
                WindowManager.LayoutParams lp = PG_Workplan.this.getWindow().getAttributes();
                lp.alpha = 1f;
                PG_Workplan.this.getWindow().setAttributes(lp);
            }
        });
        WindowManager.LayoutParams lp = PG_Workplan.this.getWindow().getAttributes();
        lp.alpha = 0.7f;
        PG_Workplan.this.getWindow().setAttributes(lp);
    }

    private void getCommandlist(String zwid, String zwname)
    {
        zynames = zwname;
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("productid", zwid);
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
//                            cmd_pager.setAdapter(null);
                            if (shopAdapter == null)
                            {
                                initPager();
                            } else
                            {
                                PG_Workplan.this.getSupportFragmentManager().getFragments().clear();
                                initPager();
                            }

                        }

                    } else
                    {

                    }
                } else
                {

                    return;
                }
            }

            @Override
            public void onFailure(HttpException error, String msg)
            {

            }
        });
    }
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
                tvList[i].setTextColor(0xFF000000);
                TextPaint tp = tvList[i].getPaint();
                tp.setFakeBoldText(false);
            }
        }
        tvList[id].setTextColor(0xFFFF5D5E);
        TextPaint tp = tvList[id].getPaint();
        tp.setFakeBoldText(true);
    }
    private class ShopAdapter extends FragmentPagerAdapter
    {
        List<Fragment> list_fragment = new ArrayList<>();

        public ShopAdapter(FragmentManager fm)
        {
            super(fm);
        }

        @Override
        public Fragment getItem(int index)
        {

//            Fragment fragment = new CmdList_Cmd_Fragment();
            Fragment fragment = new Pg_work_fragment();
            Bundle bundle = new Bundle();
            bundle.putInt("index", index);
            bundle.putString("FN", dictionary_wheel.getFirstItemName()[index]);
            bundle.putString("FI", dictionary_wheel.getFirstItemID()[index]);
            bundle.putStringArray("SI", dictionary_wheel.getSecondItemID().get(dictionary_wheel.getFirstItemID()[index]));
            bundle.putStringArray("SN", dictionary_wheel.getSecondItemName().get(fn[index]));
            bundle.putString("ZY", zynames);
            fragment.setArguments(bundle);
            list_fragment.add(fragment);
            return fragment;
        }

        public List<Fragment> getFragment()
        {
            return list_fragment;
        }

        @Override
        public int getCount()
        {
            return list.length;
        }
    }

    /**
     * initPager<br/>
     * 初始化ViewPager控件相关内容
     */
    private void initPager()
    {
        shopAdapter = new ShopAdapter(PG_Workplan.this.getSupportFragmentManager());
        cmd_pager.setAdapter(shopAdapter);
        cmd_pager.setOnPageChangeListener(onPageChangeListener);
        shopAdapter.notifyDataSetChanged();
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
     * 改变栏目位置
     *
     * @param clickPosition
     */
    private void changeTextLocation(int clickPosition)
    {
        int x = (views[clickPosition].getTop());
        cmd_tools_scrlllview.smoothScrollTo(0, x);
    }
}
