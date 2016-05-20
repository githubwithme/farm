package com.farm.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.farm.R;
import com.farm.adapter.NCZ_Statistics_Adapter;
import com.farm.adapter.PQ_GV_Adapter;
import com.farm.common.utils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2016/5/19.
 */
@EActivity(R.layout.ncz_statistics)
public class NCZ_Statistics extends Activity
{
    NCZ_Statistics_Adapter  ncz_statistics_adapter;

    @ViewById
    GridView gridView;
@Click
void btn_back()
{
    finish();
}
    @AfterViews
    void oncreateview()
    {
        JSONObject jsonObject = utils.parseJsonFile(NCZ_Statistics.this, "dictionary.json");
        JSONArray jsonArray = JSONArray.parseArray(jsonObject.getString("NCZ_Statistics"));
        final List<String> list = new ArrayList<String>();
        for (int i = 0; i < jsonArray.size(); i++)
        {
            list.add(jsonArray.getString(i));
        }
        ncz_statistics_adapter = new NCZ_Statistics_Adapter(NCZ_Statistics.this, list);
        gridView.setAdapter(ncz_statistics_adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String  aa = list.get(i);

                Intent intent = new Intent(NCZ_Statistics.this, NCZ_StatisticsDetail_.class);
                intent.putExtra("static", i);
                intent.putExtra("statistics", aa);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
    }
}
