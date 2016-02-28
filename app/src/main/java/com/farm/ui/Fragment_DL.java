package com.farm.ui;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.farm.R;
import com.farm.adapter.BreakOffAdapter;
import com.farm.adapter.breakoff_Adapter;
import com.farm.adapter.ListViewProductBatchDetailAdapter;
import com.farm.app.AppContext;
import com.farm.bean.Dictionary;
import com.farm.bean.contractTab;
import com.farm.bean.planttab;
import com.farm.common.SqliteDb;
import com.farm.widget.CustomDialog_EditDLInfor;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EFragment
public class Fragment_DL extends Fragment implements View.OnClickListener
{
    Button btn_cancle;
    Button btn_addBreakOffInfo;
    TextView tv_tip;
    ListView lv_breakofflist;
    CustomDialog_EditDLInfor customdialog_editdlinfor;
    Fragment mContent = new Fragment();
    private ListViewProductBatchDetailAdapter listAdapter;
    private int listSumData;
    private List<planttab> listData = new ArrayList<planttab>();
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
    ExpandableListView expandableListView;
    Dictionary dictionary;
    BreakOffAdapter breakOffAdapter;
    breakoff_Adapter breakoff_adapter;
    @Click
    void btn_add()
    {
        // showPop_addcommand();
        Intent intent = new Intent(getActivity(), AddSpecialCost_.class);
        startActivity(intent);
    }

    @AfterViews
    void afterOncreate()
    {
        List<contractTab> listdata= SqliteDb.getBreakOffListByAreaID(getActivity());
        breakoff_adapter = new breakoff_Adapter(getActivity(), listdata, expandableListView);
        expandableListView.setAdapter(breakoff_adapter);
        for (int i = 0; i < listdata.size(); i++)
        {
            expandableListView.expandGroup(i);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_dl, container, false);
        appContext = (AppContext) getActivity().getApplication();
        IntentFilter intentfilter_update = new IntentFilter(AppContext.BROADCAST_UPDATEBREAKOFFINFO);
        getActivity().registerReceiver(receiver_update, intentfilter_update);
        return rootView;
    }

    BroadcastReceiver receiver_update = new BroadcastReceiver()// 从扩展页面返回信息
    {
        @SuppressWarnings("deprecation")
        @Override
        public void onReceive(Context context, Intent intent)
        {
            List<contractTab> listdata= SqliteDb.getBreakOffListByAreaID(getActivity());
            breakoff_adapter = new breakoff_Adapter(getActivity(), listdata, expandableListView);
            expandableListView.setAdapter(breakoff_adapter);
            for (int i = 0; i < listdata.size(); i++)
            {
                expandableListView.expandGroup(i);
            }
        }
    };

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

    public void showDialog_BreakOffList()
    {
        final View dialog_layout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.customdialog_breakofflist, null);
        customdialog_editdlinfor = new CustomDialog_EditDLInfor(getActivity(), R.style.MyDialog, dialog_layout);
        btn_cancle = (Button) dialog_layout.findViewById(R.id.btn_cancle);
        btn_addBreakOffInfo = (Button) dialog_layout.findViewById(R.id.btn_addBreakOffInfo);
        tv_tip = (TextView) dialog_layout.findViewById(R.id.tv_tip);
        lv_breakofflist = (ListView) dialog_layout.findViewById(R.id.lv_breakofflist);
        btn_cancle.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                customdialog_editdlinfor.dismiss();
            }
        });
        btn_addBreakOffInfo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                customdialog_editdlinfor.dismiss();
            }
        });
        customdialog_editdlinfor.show();
    }

    public void showPop_addcommand()
    {
        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
        pv_command = layoutInflater.inflate(R.layout.pop_addcommand, null);// 外层
        pv_command.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if ((keyCode == KeyEvent.KEYCODE_MENU) && (pw_command.isShowing()))
                {
                    pw_command.dismiss();
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
                }
                return false;
            }
        });
        pw_command = new PopupWindow(pv_command, LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        pw_command.showAsDropDown(line, 0, 0);
        pw_command.setOutsideTouchable(true);
        pv_command.findViewById(R.id.btn_standardprocommand).setOnClickListener(this);
        pv_command.findViewById(R.id.btn_nonstandardprocommand).setOnClickListener(this);
        pv_command.findViewById(R.id.btn_nonprocommand).setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        Intent intent;
        switch (v.getId())
        {
            case R.id.btn_standardprocommand:
                intent = new Intent(getActivity(), AddStandardCommand_.class);
                startActivity(intent);
                break;
            case R.id.btn_nonstandardprocommand:
                intent = new Intent(getActivity(), AddNotStandardCommand_.class);
                startActivity(intent);
                break;
            case R.id.btn_nonprocommand:
                intent = new Intent(getActivity(), AddNotProductCommand_.class);
                startActivity(intent);
                break;

            default:
                break;
        }
    }
}
