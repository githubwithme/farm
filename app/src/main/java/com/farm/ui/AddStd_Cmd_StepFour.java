package com.farm.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.farm.R;
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
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${hmj} on 2015/12/15.
 */
@EFragment
public class AddStd_Cmd_StepFour extends Fragment
{
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

    @ViewById ImageView iv_dowm_tab;
    @ViewById ScrollView area_tools_scrlllview;
    @ViewById LinearLayout area_tools;
    @ViewById ViewPager area_pager;

    @AfterViews
    void afterOncreate()
    {
        shopAdapter = new ShopAdapter(getActivity().getSupportFragmentManager());
        inflater = LayoutInflater.from(getActivity());
        getArealist();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.add_std__cmd__step_four, container, false);
        return rootView;
    }

    public void switchContent(Fragment from, Fragment to)
    {
        if (mContent != to)
        {
            mContent = to;
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            if (!to.isAdded())
            { // 先判断是否被add过
                transaction.hide(from).add(R.id.area_top_container, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
            } else
            {
                transaction.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
            }
        }
    }

    /**
     * 动态生成显示items中的textview
     */
    private void showToolsView(String[] data)
    {
        list = data;
        tvList = new TextView[list.length];
        views = new View[list.length];

        for (int i = 0; i < list.length; i++)
        {
            View view = inflater.inflate(R.layout.item_addstd_cmd_area, null);
            view.setId(i);
            view.setOnClickListener(toolsItemListener);
            TextView textView = (TextView) view.findViewById(R.id.text);
            textView.setText(list[i]);
            area_tools.addView(view);
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
            area_pager.setCurrentItem(v.getId());
        }
    };

    /**
     * initPager<br/>
     * 初始化ViewPager控件相关内容
     */
    private void initPager()
    {
        area_pager.setAdapter(shopAdapter);
        area_pager.setOnPageChangeListener(onPageChangeListener);
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
            if (area_pager.getCurrentItem() != arg0) area_pager.setCurrentItem(arg0);
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
            Fragment fragment = new AreaList_Cmd_Fragment();
            Bundle bundle = new Bundle();
            bundle.putInt("index", index);
            bundle.putString("FN", fn[index]);
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
                tvList[i].setBackgroundColor(0x00000000);
                tvList[i].setTextColor(0xFF000000);
            }
        }
        tvList[id].setBackgroundColor(0xFFFFFFFF);
        tvList[id].setTextColor(0xFFFF5D5E);
    }

    /**
     * 改变栏目位置
     *
     * @param clickPosition
     */
    private void changeTextLocation(int clickPosition)
    {
        int x = (views[clickPosition].getTop());
        area_tools_scrlllview.smoothScrollTo(0, x);
    }

    private void getArealist()
    {
        commembertab commembertab = AppContext.getUserInfo(getActivity());
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("name", "getstdPark");
        params.addQueryStringParameter("action", "getDict");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                List<Dictionary> lsitNewData = null;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    if (result.getAffectedRows() != 0)
                    {
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
                        lsitNewData = new ArrayList<Dictionary>();
                    }
                } else
                {
                    AppContext.makeToast(getActivity(), "error_connectDataBase");
                    return;
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

    @Override
    public void onAttach(Activity activity)
    {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        fragmentCallBack = (AddStd_Cmd) activity;
    }
}
