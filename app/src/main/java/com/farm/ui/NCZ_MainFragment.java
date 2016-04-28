package com.farm.ui;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.farm.R;
import com.farm.common.utils;
import com.farm.widget.PullToRefreshListView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * @author :hc-sima
 * @version :1.0
 * @createTime：2015-8-14 下午2:19:43
 * @description :主界面
 */
@EFragment
public class NCZ_MainFragment extends Fragment
{
    Fragment mContent = new Fragment();
    Fragment mContent_todayjob = new Fragment();
    NCZ_YQPQ ncz_WorkList;
    NCZ_todaymq ncz_todaymq;
    NCZ_todaygz ncz_todaygz;
    Map_Farm map_farm;
    @ViewById
    TextView tv_gz;
    @ViewById
    TextView tv_mq;
    @ViewById
    TextView tv_farmmap;
    @ViewById
    TextView tv_all;
    @ViewById
    PullToRefreshListView frame_listview_news;
    @ViewById
    Button btn_zg;
    @ViewById
    TextView tv_day;
    @ViewById
    TextView tv_title;

    @Override
    public void onHiddenChanged(boolean hidden)
    {
        super.onHiddenChanged(hidden);
        ncz_WorkList.setThreadStatus(hidden);
        ncz_todaymq.setThreadStatus(hidden);
        ncz_todaygz.setThreadStatus(hidden);
        map_farm.setThreadStatus(hidden);
    }

    @AfterViews
    void afterOncreate()
    {
        ncz_WorkList = new NCZ_YQPQ_();
        ncz_todaymq = new NCZ_todaymq_();
        ncz_todaygz = new NCZ_todaygz_();
        map_farm=new Map_Farm_();
        tv_day.setText(utils.getTodayAndwWeek());
        tv_title.setText("首页");
        switchContent_todayjob(mContent_todayjob, ncz_todaygz);
        setBackground(0);
        setImage(initURL());
        setMenu();
    }

    @Click
    void btn_zg()
    {
        Intent intent = new Intent(getActivity(), DaoGangList_.class);
        getActivity().startActivity(intent);
    }

    @Click
    void tv_gz()
    {
        setBackground(0);
        switchContent_todayjob(mContent_todayjob, ncz_todaygz);
    }

    @Click
    void tv_mq()
    {
        setBackground(1);
        switchContent_todayjob(mContent_todayjob, ncz_todaymq);
    }

    @Click
    void tv_farmmap()
    {
        setBackground(2);
        switchContent_todayjob(mContent_todayjob, map_farm);
    }

    @Click
    void tv_all()
    {
        setBackground(3);
        switchContent_todayjob(mContent_todayjob, ncz_WorkList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.ncz_fragment_main, container, false);
        return rootView;
    }

    public void switchContent_todayjob(Fragment from, Fragment to)
    {
        if (mContent_todayjob != to)
        {
            mContent_todayjob = to;
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            if (!to.isAdded())
            { // 先判断是否被add过
                transaction.hide(from).add(R.id.container_todayjob, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
            } else
            {
                transaction.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
            }
        }
    }

    private List<String> initURL()
    {
        List<String> list = new ArrayList<String>();
        list.add("http://pic4.nipic.com/20090827/3095621_083213047918_2.jpg");
        list.add("http://pica.nipic.com/2008-07-22/2008722162232801_2.jpg");
        list.add("http://pic1a.nipic.com/2008-10-15/2008101517835380_2.jpg");
        return list;
    }

    private void setMenu()
    {
        MenuScrollFragment menuScrollFragment = new MenuScrollFragment();
        getFragmentManager().beginTransaction().replace(R.id.menu_container, menuScrollFragment).commit();
    }

    private void setImage(List<String> imglist)
    {
        PictureScrollFragment pictureScrollFragment = new PictureScrollFragment();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("imgurl", (ArrayList<String>) imglist);
        pictureScrollFragment.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.img_container, pictureScrollFragment).commit();
    }


    private void setBackground(int pos)
    {
        tv_gz.setSelected(false);
        tv_mq.setSelected(false);
        tv_farmmap.setSelected(false);
        tv_all.setSelected(false);

        tv_gz.setBackgroundResource(R.color.white);
        tv_mq.setBackgroundResource(R.color.white);
        tv_farmmap.setBackgroundResource(R.color.white);
        tv_all.setBackgroundResource(R.color.white);

        tv_gz.setTextColor(getResources().getColor(R.color.menu_textcolor));
        tv_mq.setTextColor(getResources().getColor(R.color.menu_textcolor));
        tv_farmmap.setTextColor(getResources().getColor(R.color.menu_textcolor));
        tv_all.setTextColor(getResources().getColor(R.color.menu_textcolor));
        switch (pos)
        {
            case 0:
                tv_gz.setSelected(false);
                tv_gz.setTextColor(getResources().getColor(R.color.bg_blue));
                tv_gz.setBackgroundResource(R.drawable.red_bottom);
                break;
            case 1:
                tv_mq.setSelected(false);
                tv_mq.setTextColor(getResources().getColor(R.color.bg_blue));
                tv_mq.setBackgroundResource(R.drawable.red_bottom);
                break;
            case 2:
                tv_farmmap.setSelected(false);
                tv_farmmap.setTextColor(getResources().getColor(R.color.bg_blue));
                tv_farmmap.setBackgroundResource(R.drawable.red_bottom);
                break;
            case 3:
                tv_all.setSelected(false);
                tv_all.setTextColor(getResources().getColor(R.color.bg_blue));
                tv_all.setBackgroundResource(R.drawable.red_bottom);
                break;
        }

    }
}
