package com.farm.ui;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.farm.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * Created by ${hmj} on 2016/5/26.
 */
@EFragment
public class FarmManagerFragment extends Fragment
{
    PopupWindow pw_command;
    View pv_command;
    @ViewById
    Button btn_add;
    @ViewById
    Button btn_search;
    @ViewById
    GridView gv;
    @ViewById
    View view;

    @AfterViews
    void afterOncrete()
    {
    }

    @Click
    void btn_add()
    {
        showPop_add();
    }

    @Click
    void btn_search()
    {
        Intent intent = new Intent(getActivity(), SearchAllInformation_.class);
        getActivity().startActivity(intent);
    }

//    @Click
//    void ce_tq()//测试天气
//    {
//        Intent intent = new Intent(getActivity(), NC_Weater_.class);
//        getActivity().startActivity(intent);
//    }
    @Click
    void ll_zl()
    {
//        Intent intent = new Intent(getActivity(), NCZ_CommandListActivity_.class);
        Intent intent = new Intent(getActivity(), NCZ_CommandActivity_.class);
//        Intent intent = new Intent(getActivity(), PG_CommandActivity_.class);
        getActivity().startActivity(intent);
    }

    @Click
    void ll_gz()
    {
        Intent intent = new Intent(getActivity(), NCZ_JobActivity_.class);
        getActivity().startActivity(intent);
    }

    @Click
    void ll_mq()
    {
        Intent intent = new Intent(getActivity(), NCZ_MQActivity_.class);
        getActivity().startActivity(intent);
    }

    @Click
    void ll_sj()
    {
        Intent intent = new Intent(getActivity(), NCZ_SJActivity_.class);
        getActivity().startActivity(intent);
    }

    @Click
    void ll_kc()
    {
//        Intent intent = new Intent(getActivity(), Ncz_wz_ll_.class);
        Intent intent = new Intent(getActivity(), NCZ_GoogdsManagerActivity_.class);
        getActivity().startActivity(intent);
    }

    @Click
    void ll_xs()
    {
//        Intent intent = new Intent(getActivity(), NCZ_SaleChart_.class);
//        getActivity().startActivity(intent);
        Intent intent = new Intent(getActivity(), NCZ_SaleInfor_.class);
//        Intent intent = new Intent(getActivity(), NCZ_FarmSaleData_.class);
//        Intent intent = new Intent(getActivity(), NCZ_SaleModuleActivity_.class);
        getActivity().startActivity(intent);
    }

    @Click
    void ll_cw()
    {
        Intent intent = new Intent(getActivity(), NCZ_CostModule_.class);
        getActivity().startActivity(intent);
    }

    @Click
    void ll_dl()
    {
        Intent intent = new Intent(getActivity(), NCZ_BreakOffActivity_.class);
//        Intent intent = new Intent(getActivity(), NCZ_DLdatail_.class);
        getActivity().startActivity(intent);
    }

    @Click
    void ll_sp()
    {
        Intent intent = new Intent(getActivity(), NCZ_CostModule_.class);
        getActivity().startActivity(intent);
    }

    @Click
    void ll_tj()
    {
//        Intent intent = new Intent(getActivity(), NCZ_Statistics_.class);
        Intent intent = new Intent(getActivity(), NCZ_AnalysisModule_.class);
        getActivity().startActivity(intent);
    }

    @Click
    void ll_tq()
    {
        Intent intent = new Intent(getActivity(), WeatherActivity_.class);
        intent.putExtra("parkid", "80");
        getActivity().startActivity(intent);
    }

    @Click
    void ll_sk()
    {
        Intent intent = new Intent(getActivity(), NCZ_FarmMapActivity_.class);
        intent.putExtra("parkid", "80");
        getActivity().startActivity(intent);
    }

    @Click
    void ll_more()
    {
        Intent intent = new Intent(getActivity(), NCZ_MoreModule_.class);
        intent.putExtra("parkid", "80");
        getActivity().startActivity(intent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.farmmanagerfragment, container, false);
        return rootView;
    }

    public void showPop_add()
    {
        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
        pv_command = layoutInflater.inflate(R.layout.dynamicfragment_add, null);// 外层
        pv_command.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if ((keyCode == KeyEvent.KEYCODE_MENU) && (pw_command.isShowing()))
                {
                    pw_command.dismiss();
                    WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                    lp.alpha = 1f;
                    getActivity().getWindow().setAttributes(lp);
                    return true;
                }
                return false;
            }
        });
        pv_command.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if (pw_command.isShowing())
                {
                    pw_command.dismiss();
                    WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                    lp.alpha = 1f;
                    getActivity().getWindow().setAttributes(lp);
                }
                return false;
            }
        });
        pw_command = new PopupWindow(pv_command, 500, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        pw_command.showAsDropDown(view, 0, 0);
//        int[] location = new int[2];
//        btn_add.getLocationOnScreen(location);
//        pw_command.showAtLocation(btn_add, Gravity.NO_GRAVITY, location[0]+line.getWidth(), location[1]);
        pw_command.setOutsideTouchable(true);
        LinearLayout ll_addcost = (LinearLayout) pv_command.findViewById(R.id.ll_addcost);
        LinearLayout ll_addcommand = (LinearLayout) pv_command.findViewById(R.id.ll_addcommand);


        ll_addcost.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                pw_command.dismiss();
                WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                lp.alpha = 1f;
                getActivity().getWindow().setAttributes(lp);
                Intent intent = new Intent(getActivity(), NCZ_CostModule_.class);
                getActivity().startActivity(intent);
            }
        });
        ll_addcommand.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                pw_command.dismiss();
                WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                lp.alpha = 1f;
                getActivity().getWindow().setAttributes(lp);
                Intent intent = new Intent(getActivity(), NCZ_CommandListActivity_.class);
                getActivity().startActivity(intent);
            }
        });

        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = 0.7f;
        getActivity().getWindow().setAttributes(lp);
    }
}
