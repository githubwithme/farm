package com.farm.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import com.farm.R;
import com.farm.adapter.Adapter_PGGoodsUsedInCommand;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by ${hmj} on 2016/7/2.
 */
@EActivity(R.layout.pg_goodsusedincommand)
public class PG_GoodsUsedInCommand extends Activity
{
//    ContractGoodsUsedBean
    Adapter_PGGoodsUsedInCommand adapter_pgGoodsUsedInCommand;
    @ViewById
    Button btn_addgoods;
    @ViewById
    ListView lv;

    @AfterViews
    void afterOncreate()
    {
//        adapter_pgGoodsUsedInCommand = new Adapter_PGGoodsUsedInCommand();
        lv.setAdapter(adapter_pgGoodsUsedInCommand);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }
}
