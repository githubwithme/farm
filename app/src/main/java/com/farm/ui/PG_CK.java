package com.farm.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.adapter.AddStd_cmd_StepTwo_self_Adapter;
import com.farm.adapter.PG_CKAdapter;
import com.farm.adapter.PG_CKlistAdapter;
import com.farm.adapter.PeopleAdapter;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.Dictionary;
import com.farm.bean.Dictionary_wheel;
import com.farm.bean.Result;
import com.farm.bean.Wz_Storehouse;
import com.farm.bean.commembertab;
import com.farm.bean.goodslisttab;
import com.farm.com.custominterface.FragmentCallBack;
import com.farm.common.DictionaryHelper;
import com.farm.common.SqliteDb;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by user on 2016/5/17.
 */
@EActivity(R.layout.pg_cklayout)
public class PG_CK extends Activity {

    private String id;
    private String name;
    PopupWindow pw_tab;
    View pv_tab;
    @ViewById
    View line;
    PG_CKlistAdapter pg_cKlistAdapter;
    List<Wz_Storehouse> listpeople = new ArrayList<Wz_Storehouse>();
    //    AddStd_cmd_StepTwo_self_Adapter addStd_cmd_stepTwo_self_adapter;
    PG_CKAdapter pg_ckAdapter;
    String[] fn;
    Dictionary_wheel dictionary_wheel;
    Dictionary dic;
    commembertab commembertab;
    private Context context;// 运行上下文
    String[] parent = null;
    HashMap<String, String[]> map = null;
    FragmentCallBack fragmentCallBack = null;
    @ViewById
    ExpandableListView mainlistview;
    @ViewById
    ListView list_goods;
    @ViewById
    TextView tv_head;
    @ViewById
    RelativeLayout rl_pb;
    @ViewById
    LinearLayout ll_tip;
    @ViewById
    ProgressBar pb;
    @ViewById
    TextView tv_tip;
    @ViewById
    Button btn_next;
    @ViewById
    TextView tv_shuju;
    String aa="";
    List<goodslisttab> list_goodslisttab = new ArrayList<goodslisttab>();

    @Override
    protected void onStart() {
        super.onStart();
        list_goodslisttab = SqliteDb.getSelectCmdArea(PG_CK.this, goodslisttab.class);//拿数据
        if (list_goodslisttab.size()>0)
        {
            for (int i=0;i<list_goodslisttab.size();i++)
            {
                aa+=list_goodslisttab.get(i).getGX()+"-"+list_goodslisttab.get(i).getZS()+"-"+list_goodslisttab.get(i).getId()+list_goodslisttab.get(i).getgoodsName()+
                        "-"+list_goodslisttab.get(i).getYL()+list_goodslisttab.get(i).getDW()+"-"+list_goodslisttab.get(i).getgoodsNote()+"\n";
            }
        }
        tv_shuju.setText(aa);
    }

    @Click
    void btn_next() {

    }

    @Click
    void tv_head() {
        //list_goodslisttab = SqliteDb.getSelectCmdArea(PG_CK.this, goodslisttab.class);//拿数据
//        SqliteDb.deleteAllSelectCmdArea(PG_CK.this, goodslisttab.class);//删除
        showPop_title();
    }

    @AfterViews
    void afssss() {
        SqliteDb.deleteAllSelectCmdArea(PG_CK.this, goodslisttab.class);//删除
        getlistdata();
        getCommandlist();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        commembertab = AppContext.getUserInfo(PG_CK.this);
    }

    private void getCommandlist() {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("name", "getWZ");
        params.addQueryStringParameter("action", "getDict");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String a = responseInfo.result;
                List<Dictionary> lsitNewData = null;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1) {
                    if (result.getAffectedRows() != 0) {
                        rl_pb.setVisibility(View.GONE);
                        String aa = result.getRows().toJSONString();
                        lsitNewData = JSON.parseArray(result.getRows().toJSONString(), Dictionary.class);
                        if (lsitNewData != null) {
                            dic = lsitNewData.get(0);
                            dictionary_wheel = DictionaryHelper.getDictionary_Command(dic);
                            pg_ckAdapter = new PG_CKAdapter(PG_CK.this, dictionary_wheel, mainlistview, list_goods, tv_head, fragmentCallBack,id);
                            mainlistview.setAdapter(pg_ckAdapter);
                            mainlistview.expandGroup(0);
                        }

                    } else {
                        ll_tip.setVisibility(View.VISIBLE);
                        tv_tip.setText("暂无数据！");
                        pb.setVisibility(View.GONE);
                    }
                } else {
                    ll_tip.setVisibility(View.VISIBLE);
                    tv_tip.setText("数据加载异常！");
                    pb.setVisibility(View.GONE);
                    return;
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                ll_tip.setVisibility(View.VISIBLE);
                tv_tip.setText("网络连接异常！");
                pb.setVisibility(View.GONE);
            }
        });
    }


    public void showPop_title() {//LAYOUT_INFLATER_SERVICE
        LayoutInflater layoutInflater = (LayoutInflater) PG_CK.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        pv_tab = layoutInflater.inflate(R.layout.popup_yq, null);// 外层
        pv_tab.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((keyCode == KeyEvent.KEYCODE_MENU) && (pw_tab.isShowing())) {
                    pw_tab.dismiss();
                    return true;
                }
                return false;
            }
        });
        pv_tab.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (pw_tab.isShowing()) {
                    pw_tab.dismiss();
                }
                return false;
            }
        });
        pw_tab = new PopupWindow(pv_tab, LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        pw_tab.showAsDropDown(line, 0, 0);
        pw_tab.setOutsideTouchable(true);


        ListView listview = (ListView) pv_tab.findViewById(R.id.lv_yq);
        pg_cKlistAdapter = new PG_CKlistAdapter(PG_CK.this, listpeople);
        listview.setAdapter(pg_cKlistAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View v, int postion, long arg3) {
                id = listpeople.get(postion).getStorehouseId();
                name = listpeople.get(postion).getStorehouseName();
                pw_tab.dismiss();
                tv_head.setText(listpeople.get(postion).getStorehouseName());

            }
        });
    }

    private void getlistdata() {
        commembertab commembertab = AppContext.getUserInfo(PG_CK.this);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("action", "getGoodsByUid");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String a = responseInfo.result;
                List<Wz_Storehouse> listNewData = null;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    if (result.getAffectedRows() != 0) {
                        listNewData = JSON.parseArray(result.getRows().toJSONString(), Wz_Storehouse.class);
                        tv_head.setText(listNewData.get(0).getStorehouseName());
                        id = listNewData.get(0).getStorehouseId();
                        name = listNewData.get(0).getStorehouseName();
                        listpeople.addAll(listNewData);

                    } else {
                        listNewData = new ArrayList<Wz_Storehouse>();
                    }
                } else {
                    AppContext.makeToast(PG_CK.this, "error_connectDataBase");

                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg) {
                String a = error.getMessage();
                AppContext.makeToast(PG_CK.this, "error_connectServer");

            }
        });

    }
}
