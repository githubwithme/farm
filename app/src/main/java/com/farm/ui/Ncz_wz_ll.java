package com.farm.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.adapter.NCZ_DLAdapter;
import com.farm.adapter.ViewPagerAdapter_GcdDetail;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.ReportedBean;
import com.farm.bean.Result;
import com.farm.bean.Wz_Storehouse;
import com.farm.bean.commembertab;
import com.farm.widget.CustomViewPager;
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
import java.util.Iterator;
import java.util.List;

/**
 * Created by user on 2016/2/24.
 */
@EActivity(R.layout.ncz_wzgl)
public class Ncz_wz_ll extends FragmentActivity
{
    NCZ_DLAdapter ncz_dlAdapter;
    private String id;
    private String name;
    List<Wz_Storehouse> listpeople = new ArrayList<Wz_Storehouse>();
    PopupWindow pw_tab;
    View pv_tab;
    @ViewById
    View line;

    com.farm.bean.commembertab commembertab;
    String wzgl;
    ViewPagerAdapter_GcdDetail viewPagerAdapter_gcdDetail;
    NCZ_WZ_LOOKFragment nec_wz_lookFragment;
    //    NCZ_WZ_CKXXFragment ncz_wz_ckxxFragment;
    NCZ_WZ_RKFragment ncz_wz_rkFragment;
    NCZ_WZ_CKFRagment ncz_wz_ckfRagment;
    NCZ_WZ_YCFragment ncz_wz_ycFragment;
    int currentItem = 0;
    List<Fragment> fragmentList;
    Fragment mContent = new Fragment();
    @ViewById
    ImageButton imgbtn_back;
    @ViewById
    TextView wz_ll;
    @ViewById
    TextView wzck;
    @ViewById
    TextView wz_rk;
    @ViewById
    TextView wz_ck;
    @ViewById
    TextView wz_yc;
    @ViewById
    View view_cangku;
    @ViewById
    View view_ck;
    @ViewById
    View view_rk;
    @ViewById
    View view_wz;
    @ViewById
    View view_yc;
    @ViewById
    CustomViewPager cvPager;

    @ViewById
    TextView tv_title;

    @Click
    void tv_title()
    {
        showPop_title();
    }

    @Click
    void imgbtn_back()
    {
        finish();
    }

    @Click
    void wz_ll()
    {
        cvPager.setCurrentItem(3);
    }

  /*  @Click
    void wzck()
    {
        cvPager.setCurrentItem(3);
    }*/

    @Click
    void wz_rk()
    {
        cvPager.setCurrentItem(1);
    }

    @Click
    void wz_ck()
    {
        cvPager.setCurrentItem(0);
    }

    @Click
    void wz_yc()
    {
        cvPager.setCurrentItem(2);
    }

    @AfterViews
    void afterOncreat()
    {
        commembertab = AppContext.getUserInfo(Ncz_wz_ll.this);
        getlistdata();
        refeash(id, name);
/*        setBackground(0);
        cvPager.setOffscreenPageLimit(0);
        cvPager.setIsScrollable(true);
        viewPagerAdapter_gcdDetail = new ViewPagerAdapter_GcdDetail(Ncz_wz_ll.this.getSupportFragmentManager(), cvPager, fragmentList);
        viewPagerAdapter_gcdDetail.setOnExtraPageChangeListener(new ViewPagerAdapter_GcdDetail.OnExtraPageChangeListener()
        {
            @Override
            public void onExtraPageSelected(int i)
            {
                currentItem = i;
                setBackground(i);
            }
        });*/
    }

    private void setBackground(int pos)
    {
        view_ck.setVisibility(View.GONE);
        view_rk.setVisibility(View.GONE);
        view_yc.setVisibility(View.GONE);
        view_wz.setVisibility(View.GONE);
//        view_cangku.setVisibility(View.GONE);
        switch (pos)
        {
            case 0:
                view_ck.setVisibility(View.VISIBLE);
                break;
            case 1:
                view_rk.setVisibility(View.VISIBLE);
                break;
            case 2:
                view_yc.setVisibility(View.VISIBLE);
                break;
            case 3:
                view_wz.setVisibility(View.VISIBLE);
                break;
        /*    case 4:
                view_cangku.setVisibility(View.VISIBLE);
                break;*/
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
    }

    private void getlistdata()
    {
        com.farm.bean.commembertab commembertab = AppContext.getUserInfo(Ncz_wz_ll.this);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", commembertab.getuId());
//        params.addQueryStringParameter("parkId", "16");
//        params.addQueryStringParameter("action", "getGoodsByUid");
        params.addQueryStringParameter("action", "getcontractByUid");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String a = responseInfo.result;
                List<Wz_Storehouse> listNewData = null;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    if (result.getAffectedRows() != 0)
                    {
                        listNewData = JSON.parseArray(result.getRows().toJSONString(), Wz_Storehouse.class);
                        listpeople.addAll(listNewData);
                        Wz_Storehouse wz_storehouses=new Wz_Storehouse();
                        wz_storehouses.setParkId("");
                        wz_storehouses.setParkName("全部");
                        listpeople.add(wz_storehouses);
            /*            id = listNewData.get(0).getId();
                        name = listNewData.get(0).getParkName();
                        tv_title.setText(listNewData.get(0).getParkName());
                        getIsStartBreakOff(id);*/
                    } else
                    {
                        listNewData = new ArrayList<Wz_Storehouse>();
                    }
                } else
                {
                    AppContext.makeToast(Ncz_wz_ll.this, "error_connectDataBase");

                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                String a = error.getMessage();
                AppContext.makeToast(Ncz_wz_ll.this, "error_connectServer");

            }
        });

    }

    public void showPop_title()
    {//LAYOUT_INFLATER_SERVICE
        LayoutInflater layoutInflater = (LayoutInflater) Ncz_wz_ll.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        pv_tab = layoutInflater.inflate(R.layout.popup_yq, null);// 外层
        pv_tab.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if ((keyCode == KeyEvent.KEYCODE_MENU) && (pw_tab.isShowing()))
                {
                    pw_tab.dismiss();
//                    iv_dowm_tab.setImageResource(R.drawable.ic_down);
                    return true;
                }
                return false;
            }
        });
        pv_tab.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if (pw_tab.isShowing())
                {
                    pw_tab.dismiss();
//                    iv_dowm_tab.setImageResource(R.drawable.ic_down);
                }
                return false;
            }
        });
        pw_tab = new PopupWindow(pv_tab, LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        pw_tab.showAsDropDown(line, 0, 0);
        pw_tab.setOutsideTouchable(true);


        ListView listview = (ListView) pv_tab.findViewById(R.id.lv_yq);
        ncz_dlAdapter = new NCZ_DLAdapter(Ncz_wz_ll.this, listpeople);
        listview.setAdapter(ncz_dlAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View v, int postion, long arg3)
            {
                id = listpeople.get(postion).getId();
                name = listpeople.get(postion).getParkName();
//                viewPagerAdapter_gcdDetail.notifyDataSetChanged();

                getSupportFragmentManager().getFragments().clear();
                refeash(id, name);
/*                Bundle bundle = new Bundle();
                bundle.putString("id", id);
                bundle.putString("name", name);
                nec_wz_lookFragment.setArguments(bundle);
                ncz_wz_rkFragment.setArguments(bundle);
                ncz_wz_ckfRagment.setArguments(bundle);
                ncz_wz_ycFragment.setArguments(bundle);*/
      /*          Intent intent = new Intent();
                intent.putExtra("id", id);
                intent.putExtra("name", name);
                intent.setAction(AppContext.BROADCAST_NCZ_WZ_ParkId);*/
                pw_tab.dismiss();
//                iv_dowm_tab.setImageResource(R.drawable.ic_down);
                tv_title.setText(listpeople.get(postion).getParkName());
//                getIsStartBreakOff(listpeople.get(postion).getId());

            }
        });
    }

    public void refeash(String id, String name)
    {
        fragmentList = new ArrayList<>();
        nec_wz_lookFragment = new NCZ_WZ_LOOKFragment_();
        ncz_wz_rkFragment = new NCZ_WZ_RKFragment_();
        ncz_wz_ckfRagment = new NCZ_WZ_CKFRagment_();
        ncz_wz_ycFragment = new NCZ_WZ_YCFragment_();
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        bundle.putString("name", name);
        nec_wz_lookFragment.setArguments(bundle);
        ncz_wz_rkFragment.setArguments(bundle);
        ncz_wz_ckfRagment.setArguments(bundle);
        ncz_wz_ycFragment.setArguments(bundle);
        fragmentList.add(ncz_wz_ckfRagment);
        fragmentList.add(ncz_wz_rkFragment);
        fragmentList.add(ncz_wz_ycFragment);
        fragmentList.add(nec_wz_lookFragment);
        currentItem = 0;
        setBackground(0);
        cvPager.setOffscreenPageLimit(1);
        cvPager.setIsScrollable(true);
        viewPagerAdapter_gcdDetail = new ViewPagerAdapter_GcdDetail(Ncz_wz_ll.this.getSupportFragmentManager(), cvPager, fragmentList);
        viewPagerAdapter_gcdDetail.setOnExtraPageChangeListener(new ViewPagerAdapter_GcdDetail.OnExtraPageChangeListener()
        {
            @Override
            public void onExtraPageSelected(int i)
            {
                currentItem = i;
                setBackground(i);
            }
        });
    }

}
