package com.farm.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.adapter.DictionaryFirstItemAdapter;
import com.farm.adapter.DictionarySecondAdapter;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.Dictionary;
import com.farm.bean.Result;
import com.farm.bean.SelectRecords;
import com.farm.bean.commembertab;
import com.farm.common.SqliteDb;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressLint("NewApi")
@EFragment
public class AddStd_Cmd_Step_Area extends Fragment implements OnClickListener
{
    TextView resultview;
    private List<Map<String, Object>> mainList;
    DictionaryFirstItemAdapter mainAdapter;
    DictionarySecondAdapter moreAdapter;
    private AppContext appContext;// 全局Context
    CheckBox cb_1;
    RadioButton rb_bz;
    int oldpostion = 0;
    List<SelectRecords> list_SelectRecords;
    public Boolean state_hs_selected = false;
    String title;
    Dictionary dictionary;

    @ViewById
    ListView mainlist;
    @ViewById
    ListView morelist;

    @AfterViews
    void afterOncreate()
    {
        getArealist();
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        SqliteDb.deleteAllRecordtemp(getActivity(), SelectRecords.class, dictionary.getBELONG());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.addstd_cmd_area, container, false);
        return rootView;
    }

    public void setResultview(TextView resultview)
    {
        this.resultview = resultview;
    }

    public TextView getResultview()
    {
        return resultview;
    }


    @Override
    public void onClick(View v)
    {

        switch (v.getId())
        {

            default:
                break;
        }
    }

    private void initView()
    {
        mainAdapter = new DictionaryFirstItemAdapter(getActivity(), mainList);
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

    private void initAdapter(String BELONG, String firstid, String firstType, List<String> secondItemid, List<String> secondItemName)
    {
        moreAdapter = new DictionarySecondAdapter(getActivity(), BELONG, firstid, firstType, secondItemid, secondItemName);
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
                            dictionary = lsitNewData.get(0);
                            initModle();
                            initView();
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
}
