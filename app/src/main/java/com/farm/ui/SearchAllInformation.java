package com.farm.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.adapter.Adapter_Search;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.Result;
import com.farm.bean.SearchEntity;
import com.farm.bean.commembertab;
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

import java.util.List;

/**
 * Created by ${hmj} on 2016/5/26.
 */
@EActivity(R.layout.searchallinformation)
public class SearchAllInformation extends Activity
{

    Adapter_Search adapter_search;
    List<SearchEntity> listData;
    @ViewById
    EditText et_search;
    @ViewById
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Click
    void btn_search()
    {
        if (et_search.getText().toString().equals(""))
        {
            Toast.makeText(SearchAllInformation.this, "请输入搜索内容", Toast.LENGTH_SHORT).show();
        } else
        {
            getDynamicData(et_search.getText().toString());
        }
    }

    @Click
    void btn_back()
    {
        finish();
    }


    @AfterViews
    void afterOncrete()
    {


    }

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState)
    {
        super.onCreate(savedInstanceState, persistentState);
        getActionBar().hide();
    }

    private void getDynamicData(String keyword)
    {
        commembertab commembertab = AppContext.getUserInfo(SearchAllInformation.this);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("keyword", keyword);
        params.addQueryStringParameter("action", "SearchAllInformation");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String aa = responseInfo.result;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    if (result.getAffectedRows() != 0)
                    {
                        listData = JSON.parseArray(result.getRows().toJSONString(), SearchEntity.class);
                        adapter_search = new Adapter_Search(SearchAllInformation.this, listData);
                        lv.setAdapter(adapter_search);
                        lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
                        {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                            {
                                Intent intent = null;
                                String type = listData.get(position).getType();
                                if (type.equals("ZL"))
                                {
                                    intent = new Intent(SearchAllInformation.this, NCZ_CommandListActivity_.class);
                                } else if (type.equals("GZ"))
                                {
                                    intent = new Intent(SearchAllInformation.this, NCZ_GZActivity_.class);
                                } else if (type.equals("MQ"))
                                {
                                    intent = new Intent(SearchAllInformation.this, NCZ_MQActivity_.class);
                                } else if (type.equals("XS"))
                                {
                                    intent = new Intent(SearchAllInformation.this, NCZ_FarmSale_.class);
                                } else if (type.equals("KC"))
                                {
                                    intent = new Intent(SearchAllInformation.this, Ncz_wz_ll_.class);
                                } else if (type.equals("SP"))
                                {
                                    intent = new Intent(SearchAllInformation.this, NCZ_CommandListActivity_.class);
                                } else if (type.equals("SJ"))
                                {
                                    intent = new Intent(SearchAllInformation.this, NCZ_SJActivity_.class);
                                } else if (type.equals("DL"))
                                {
                                    intent = new Intent(SearchAllInformation.this, NCZ_CommandListActivity_.class);
                                }
                                SearchAllInformation.this.startActivity(intent);
                            }
                        });
                    }
                } else
                {
                    AppContext.makeToast(SearchAllInformation.this, "error_connectDataBase");
                    return;
                }


            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(SearchAllInformation.this, "error_connectServer");
            }
        });
    }
}
