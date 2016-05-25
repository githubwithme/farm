package com.farm.ui;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.farm.R;
import com.farm.adapter.NCZ_NotPayCost_adapter;
import com.farm.app.AppContext;
import com.farm.bean.SellOrder_New;
import com.farm.common.FileHelper;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${hmj} on 2016/5/25.
 */
@EFragment
public class NCZ_NotPayCostFragment extends Fragment
{
    private NCZ_NotPayCost_adapter ncz_notPayCost_adapter;
    private List<SellOrder_New> listData = new ArrayList<SellOrder_New>();
    @ViewById
    ListView lv;
    @ViewById
    ImageButton btn_add;

    @Click
    void btn_add()
    {
        Intent intent = new Intent(getActivity(), AddSpecialCost_.class);
        startActivity(intent);
    }

    @AfterViews
    void afterOncreate()
    {
        getNewSaleList_test();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.ncz_notpaycostfragment, container, false);
        return rootView;
    }

    private void getNewSaleList_test()
    {
        listData = FileHelper.getAssetsData(getActivity(), "getOrderList", SellOrder_New.class);
        if (listData != null)
        {
            ncz_notPayCost_adapter = new NCZ_NotPayCost_adapter(getActivity(), listData, AppContext.BROADCAST_UPDATEAllORDER);
            lv.setAdapter(ncz_notPayCost_adapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                {
                    Intent intent = new Intent(getActivity(), NCZ_OrderDetail_.class);
                    intent.putExtra("bean", listData.get(position));
                    getActivity().startActivity(intent);
                }
            });
        }

    }
}
