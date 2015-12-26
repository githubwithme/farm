package com.farm.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.farm.R;
import com.farm.adapter.AddPlantObservationAdapter_MakeSure;
import com.farm.bean.Result;
import com.farm.bean.planttab;
import com.farm.common.utils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.List;

/**
 * Created by ${hmj} on 2015/12/15.
 */
@EFragment
public class AddPlantObservation_stepFive extends Fragment
{
    @ViewById
    ListView lv;

    @AfterViews
    void afterOncreate()
    {
        JSONObject jsonObject = utils.parseJsonFile(getActivity(), "dictionary.json");
        List<planttab> lsitNewData = JSON.parseArray(JSON.parseObject(jsonObject.getString("plantlist"), Result.class).getRows().toJSONString(), planttab.class);
        if (lsitNewData != null)
        {
            AddPlantObservationAdapter_MakeSure addPlantObservationAdapter = new AddPlantObservationAdapter_MakeSure(getActivity(), lsitNewData);
            lv.setAdapter(addPlantObservationAdapter);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.addplantobservation_stepfive, container, false);
        return rootView;
    }


}
