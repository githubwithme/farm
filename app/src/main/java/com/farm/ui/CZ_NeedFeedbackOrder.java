package com.farm.ui;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.farm.R;
import com.farm.adapter.CZ_OrderAdapter;
import com.farm.app.AppContext;
import com.farm.bean.SelectRecords;
import com.farm.bean.SellOrder_New;
import com.farm.common.FileHelper;
import com.farm.common.SqliteDb;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("NewApi")
@EFragment
public class CZ_NeedFeedbackOrder extends Fragment
{
    private CZ_OrderAdapter listAdapter;
    private int listSumData;
    private List<SellOrder_New> listData = new ArrayList<SellOrder_New>();
    private AppContext appContext;
    private View list_footer;
    private TextView list_foot_more;
    private ProgressBar list_foot_progress;
    PopupWindow pw_tab;
    View pv_tab;
    PopupWindow pw_command;
    View pv_command;
    @ViewById
    View line;
    @ViewById
    ListView lv;


    @Override
    public void onResume()
    {
        super.onResume();
    }

    @AfterViews
    void afterOncreate()
    {
        SqliteDb.deleteAllRecordtemp(getActivity(), SelectRecords.class, "NCZ_CMD");
        getNewSaleList_test();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.ncz_allorderfragment, container, false);
        appContext = (AppContext) getActivity().getApplication();
        return rootView;
    }


    private void getNewSaleList_test()
    {
        listData = FileHelper.getAssetsData(getActivity(), "getOrderList", SellOrder_New.class);
        if (listData != null)
        {
            listAdapter = new CZ_OrderAdapter(getActivity(), listData);
            lv.setAdapter(listAdapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                {
                    Intent intent = new Intent(getActivity(), CZ_OrderDetail_.class);
                    intent.putExtra("bean", listData.get(position));
                    getActivity().startActivity(intent);
                }
            });
        }

    }


    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
    }

}
