package com.farm.ui;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.farm.R;
import com.farm.adapter.ViewPagerAdapter_GcdDetail;
import com.farm.app.AppContext;
import com.farm.bean.Dictionary;
import com.farm.bean.PlantGcd;
import com.farm.bean.jobtab;
import com.farm.common.DictionaryHelper;
import com.farm.widget.CustomArrayAdapter;
import com.farm.widget.CustomViewPager;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${hmj} on 2016/7/12.
 */
@EActivity(R.layout.pg_commandactivity)
public class PG_CommandActivity extends FragmentActivity
{
    Dictionary dictionary;
    SelectorFragment selectorUi;
    private List<jobtab> joblist;
    com.farm.bean.commembertab commembertab;
    PlantGcd plantGcd;
    com.farm.bean.areatab areatab;
    int currentItem = 0;
    List<android.support.v4.app.Fragment> fragmentList;
    ViewPagerAdapter_GcdDetail viewPagerAdapter_gcdDetail;
    Fragment mContent = new Fragment();
    PG_CommandFragment pg_commandFragment;
    PG_CommandFragment_Finish pg_commandFragment_finish;
    @ViewById
    ImageButton btn_back;
    @ViewById
    CustomViewPager vPager;
    @ViewById
    TextView tv_ongoing;
    @ViewById
    TextView tv_waitforexecute;
    @ViewById
    TextView tv_finish;
    @ViewById
    Spinner provinceSpinner;
    @ViewById
    Spinner citySpinner;
    @ViewById
    Spinner countySpinner;
    @ViewById
    Spinner spinner_date;
    @ViewById
    Spinner workerSpinner;
    ArrayAdapter<String> provinceAdapter = null;  //省级适配器
    ArrayAdapter<String> cityAdapter = null;    //地级适配器
    ArrayAdapter<String> countyAdapter = null;    //县级适配器
    ArrayAdapter<String> workerAdapter = null;    //县级适配器
    ArrayAdapter<String> dateAdapter = null;    //县级适配器
    static int provincePosition = 3;
    private String[] mProvinceDatas = new String[]{"全部分场", "乐丰分场", "双桥分场"};
    private String[] mCitisDatasMap = new String[]{"全部片区", "一号片区", "二号片区", "三号片区"};
    private String[] mAreaDatasMap = new String[]{"全部考核", "警告", "不合格", "合格"};
    private String[] mWorkerDatasMap = new String[]{"全部类型", "植保", "施肥"};
    private String[] mDateDatasMap = new String[]{"不限时间", "昨天", "今天", "明天"};
    @Click
    void btn_addcommand()
    {
        Intent intent = new Intent(PG_CommandActivity.this, NCZ_AddNewCommand_.class);
        startActivity(intent);
    }

    @Click
    void btn_add()
    {
        Intent intent = new Intent(PG_CommandActivity.this, AddPlantObservation_.class);
        intent.putExtra("gcdid", plantGcd.getId());
        startActivity(intent);
    }

    @Click
    void btn_back()
    {
        finish();
    }

    @Click
    void tv_finish()
    {
        vPager.setCurrentItem(1);
    }

    @Click
    void tv_ongoing()
    {
        vPager.setCurrentItem(0);
    }

    @AfterViews
    void afterOncreate()
    {
        dictionary = DictionaryHelper.getDictionaryFromAssess(PG_CommandActivity.this, "NCZ_CMD");
        selectorUi = new SelectorFragment_();
        Bundle bundle_dic = new Bundle();
        bundle_dic.putSerializable("bean", dictionary);
        selectorUi.setArguments(bundle_dic);
        switchContent(mContent, selectorUi);
        setSpinner();
        commembertab = AppContext.getUserInfo(PG_CommandActivity.this);
        joblist = getIntent().getParcelableArrayListExtra("jobtablist");
        fragmentList = new ArrayList<>();
        pg_commandFragment = new PG_CommandFragment_();
        pg_commandFragment_finish=new PG_CommandFragment_Finish_();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("jobtablist", (ArrayList<? extends Parcelable>) joblist);
        pg_commandFragment.setArguments(bundle);
        pg_commandFragment_finish.setArguments(bundle);
        fragmentList.add(pg_commandFragment);
        fragmentList.add(pg_commandFragment_finish);

        setBackground(0);
        vPager.setOffscreenPageLimit(1);
        vPager.setIsScrollable(true);
        viewPagerAdapter_gcdDetail = new ViewPagerAdapter_GcdDetail(PG_CommandActivity.this.getSupportFragmentManager(), vPager, fragmentList);
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
    private void setSpinner()
    {
        //绑定适配器和值
        provinceAdapter = new CustomArrayAdapter(PG_CommandActivity.this, mProvinceDatas);
        provinceSpinner.setAdapter(provinceAdapter);
        provinceSpinner.setSelection(0, true);  //设置默认选中项，此处为默认选中第4个值

        cityAdapter = new CustomArrayAdapter(PG_CommandActivity.this, mCitisDatasMap);
        citySpinner.setAdapter(cityAdapter);
        citySpinner.setSelection(0, true);  //默认选中第0个

        countyAdapter = new CustomArrayAdapter(PG_CommandActivity.this, mAreaDatasMap);
        countySpinner.setAdapter(countyAdapter);
        countySpinner.setSelection(0, true);

        workerAdapter = new CustomArrayAdapter(PG_CommandActivity.this, mWorkerDatasMap);
        workerSpinner.setAdapter(workerAdapter);
        workerSpinner.setSelection(0, true);
        dateAdapter = new CustomArrayAdapter(PG_CommandActivity.this, mDateDatasMap);
        spinner_date.setAdapter(dateAdapter);
        spinner_date.setSelection(0, true);

        //省级下拉框监听
        provinceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            // 表示选项被改变的时候触发此方法，主要实现办法：动态改变地级适配器的绑定值
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3)
            {
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0)
            {
            }

        });


        //地级下拉监听
        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3)
            {
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0)
            {

            }
        });
    }

    private void setBackground(int pos)
    {
        tv_finish.setBackgroundResource(R.color.white);
        tv_ongoing.setBackgroundResource(R.color.white);
        switch (pos)
        {
            case 0:
                tv_ongoing.setBackgroundResource(R.drawable.red_bottom);
                break;
            case 1:
                tv_finish.setBackgroundResource(R.drawable.red_bottom);
                break;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
    }
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

}
