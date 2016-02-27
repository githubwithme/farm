package com.farm.ui;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.farm.R;
import com.farm.adapter.DL_ZS_Adapter;
import com.farm.adapter.ListViewProductBatchDetailAdapter;
import com.farm.adapter.ProductBatch_Adapter;
import com.farm.adapter.SelectorFirstItemAdapter;
import com.farm.adapter.SelectorSecondItemAdapter;
import com.farm.app.AppContext;
import com.farm.bean.Dictionary;
import com.farm.bean.Result;
import com.farm.bean.ZS;
import com.farm.bean.commembertab;
import com.farm.bean.planttab;
import com.farm.common.SqliteDb;
import com.farm.common.utils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@EFragment
public class Fragment_Sale extends Fragment  implements View.OnClickListener
{
    ProductBatch_Adapter productBatch_adapter;
    public List<View> list_state = new ArrayList<View>();
    SelectorFirstItemAdapter mainAdapter;
    SelectorSecondItemAdapter moreAdapter;
    private List<Map<String, Object>> mainList;
    private ListView mainlist;
    private ListView morelist;
    PopupWindow popupWindow_selector;
    View popupWindowView_selector;
    DL_ZS_Adapter dl_zs_adapter;
    List<ZS> list_zs;
    ListView lv_zs;
    commembertab commembertab;
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
    TextView tv_title;
    @ViewById
    View line;
    @ViewById
    TextView tv_select_pc;
    @ViewById
ImageView iv_up_selector;
    @ViewById
    ExpandableListView expandableListView;
    Dictionary dictionary;


    @Click
    void rl_selector()
    {
        showPop_addcommand();
    }
    @AfterViews
    void afterOncreate()
    {
        getTestData("procducts");
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_sale, container, false);
        commembertab = AppContext.getUserInfo(getActivity());
        appContext = (AppContext) getActivity().getApplication();
        return rootView;
    }



    private void getTestData(String from)
    {
        JSONObject jsonObject = utils.parseJsonFile(getActivity(), "dictionary.json");
        Result result = JSON.parseObject(jsonObject.getString(from), Result.class);
        listData= JSON.parseArray(result.getRows().toJSONString(), planttab.class);
//        listAdapter = new ListViewProductBatchDetailAdapter(getActivity(), listData);
//        lv.setAdapter(listAdapter);

//        productBatch_adapter = new ProductBatch_Adapter(getActivity(), listData, expandableListView);
//        expandableListView.setAdapter(productBatch_adapter);
//        for (int i = 0; i < listNewData.size(); i++)
//        {
//            expandableListView.expandGroup(i);
//        }
    }


    public void switchContent(Fragment from, Fragment to)
    {
        if (mContent != to)
        {
            mContent = to;
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            if (!to.isAdded())
            { // 先判断是否被add过
                transaction.hide(from).add(R.id.top_container_sale, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
            } else
            {
                transaction.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
            }
        }
    }

    public void showPop_addcommand()
    {
        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
        pv_command = layoutInflater.inflate(R.layout.pop_zs, null);// 外层
        pv_command.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if ((keyCode == KeyEvent.KEYCODE_MENU) && (pw_command.isShowing()))
                {
                    pw_command.dismiss();
//                    WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
//                    lp.alpha = 1f;
//                    getActivity().getWindow().setAttributes(lp);
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
//                    WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
//                    lp.alpha = 1f;
//                    getActivity().getWindow().setAttributes(lp);
                }
                return false;
            }
        });
        pw_command = new PopupWindow(pv_command, 250, LinearLayout.LayoutParams.MATCH_PARENT, true);
        pw_command.setAnimationStyle(R.style.leftinleftout);
        pw_command.showAsDropDown(line, 0, 0);
//        pw_command.showAtLocation(fl_map, Gravity.LEFT, 0, 500);
        pw_command.setOutsideTouchable(true);

//        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
//        lp.alpha = 0.7f;
//        getActivity().getWindow().setAttributes(lp);
        lv_zs = (ListView) pv_command.findViewById(R.id.lv_zs);
        list_zs = SqliteDb.getZS(getActivity(), ZS.class, commembertab.getareaId());
        dl_zs_adapter = new DL_ZS_Adapter(getActivity(), list_zs);
        lv_zs.setAdapter(dl_zs_adapter);

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
