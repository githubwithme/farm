package com.farm.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.adapter.WZ_RKExecute_Adapter;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.Result;
import com.farm.bean.WZ_CRk;
import com.farm.bean.Wz_Storehouse;
import com.farm.bean.commembertab;
import com.farm.widget.CustomDialog_Expandlistview;
import com.farm.widget.CustomDialog_ListView;
import com.farm.widget.MyDialog;
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
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hasee on 2016/6/21.
 */
@EActivity(R.layout.pg_jsd)
public class PG_JSD extends Activity
{

    @ViewById
    TextView zp_jingzhong;
    @ViewById
    TextView cp_jingzhong;

    @ViewById
    TextView allnum;
    @ViewById
    TextView all_zhengpin;
    @ViewById
    TextView allcipin;
    @ViewById
    TextView all_jinzhong;
    List<WZ_CRk> listData = null;
    String parkname;
    String cbhname;
    MyDialog myDialog;
    //    CustomDialog_ListView customDialog_listView;
    CustomDialog_Expandlistview customDialog_listView;

    List<WZ_CRk> listpeople = new ArrayList<WZ_CRk>();

    private TextView[] pianqus = new TextView[30];
    private TextView[] chengbaohus = new TextView[30];
    private EditText[] zhushus = new EditText[30];
    private EditText[] zhengpins = new EditText[30];
    private EditText[] cipins = new EditText[30];
    private EditText[] zhongliangs = new EditText[30];
    private View[] views = new View[30];
    int curremt = 0;
    private LayoutInflater inflater;
    @ViewById
    LinearLayout pg_dts;

    @Click
    void btn_upload()
    {

        int num = 0;
        for (int i = 0; i < curremt; i++)
        {
            int currems = curremt;
            num += Integer.valueOf(zhushus[i].getText().toString());
        }
        allnum.setText(num + "");

    }

    @Click
    void btn_save()
    {
        addView();
        curremt++;
    }

    private void addView()
    {
        View view = inflater.inflate(R.layout.pg_dtcbh, null);
        view.setId(curremt);
        TextView painqu = (TextView) view.findViewById(R.id.painqu);
        painqu.setId(curremt);
        painqu.setOnClickListener(toolsItemListener);
        TextView chengbaohu = (TextView) view.findViewById(R.id.chengbaohu);
        EditText zhushu = (EditText) view.findViewById(R.id.zhushu);
        zhushu.addTextChangedListener(onclickText);
        EditText zhengpin = (EditText) view.findViewById(R.id.zhengpin);
        zhengpin.addTextChangedListener(onzhengpin);

        EditText cipin = (EditText) view.findViewById(R.id.cipin);
        cipin.addTextChangedListener(oncigpin);
        EditText jinzhong = (EditText) view.findViewById(R.id.jinzhong);
        jinzhong.addTextChangedListener(onjinzhong);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT, 1);
        lp.setMargins(0, 0, 0, 0);
        painqu.setLayoutParams(lp);
        chengbaohu.setLayoutParams(lp);
        zhushu.setLayoutParams(lp);
        zhengpin.setLayoutParams(lp);
        cipin.setLayoutParams(lp);
        jinzhong.setLayoutParams(lp);
        pg_dts.addView(view);
        pianqus[curremt] = painqu;
        chengbaohus[curremt] = chengbaohu;
        zhushus[curremt] = zhushu;
        zhengpins[curremt] = zhengpin;
        cipins[curremt] = cipin;
        zhongliangs[curremt] = jinzhong;

        views[curremt] = view;
    }

    @AfterViews
    void after()
    {
//        getchengbaohu();
        getBreakOffInfoOfContract();
        inflater = LayoutInflater.from(PG_JSD.this);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
    }


    //全部净重自动
    private TextWatcher onzidong  = new TextWatcher()
    {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
        {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
        {

        }

        @Override
        public void afterTextChanged(Editable editable)
        {
/*            zp_jingzhong
                    cp_jingzhong
            all_zhongpin
                    allcipin*/
            double num = 0;
            if (!zp_jingzhong.equals("")&&!cp_jingzhong.equals(""))
            {

                if (!all_zhengpin.equals(""))
                {
                    num += Double.valueOf(all_zhengpin.getText().toString());
                }
            }

            for (int i = 0; i < curremt; i++)
            {
                if (!zhongliangs[i].getText().toString().equals(""))
                {
                    num += Double.valueOf(zhongliangs[i].getText().toString());
                }


            }
            all_jinzhong.setText(num + "");
        }
    };
    //全部净重手动
    private TextWatcher onjinzhong = new TextWatcher()
    {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
        {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
        {

        }

        @Override
        public void afterTextChanged(Editable editable)
        {


            double num = 0;
            for (int i = 0; i < curremt; i++)
            {
                if (!zhongliangs[i].getText().toString().equals(""))
                {
                    num += Double.valueOf(zhongliangs[i].getText().toString());
                }


            }
            all_jinzhong.setText(num + "");
        }
    };
    //次品个数
    private TextWatcher oncigpin = new TextWatcher()
    {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
        {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
        {

        }

        @Override
        public void afterTextChanged(Editable editable)
        {
            float num = 0;
            for (int i = 0; i < curremt; i++)
            {
                if (!cipins[i].getText().toString().equals(""))
                {
                    num += Float.valueOf(cipins[i].getText().toString());
                }


            }
            allcipin.setText(num + "");
        }
    };

    //正品个数
    private TextWatcher onzhengpin = new TextWatcher()
    {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
        {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
        {

        }

        @Override
        public void afterTextChanged(Editable editable)
        {
            float num = 0;
            for (int i = 0; i < curremt; i++)
            {
                if (!zhengpins[i].getText().toString().equals(""))
                {
                    num += Float.valueOf(zhengpins[i].getText().toString());
                }
            }
            all_zhengpin.setText(num + "");


        }
    };
    //总株树
    private TextWatcher onclickText = new TextWatcher()
    {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
        {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
        {

        }

        @Override
        public void afterTextChanged(Editable editable)
        {
            int num = 0;
            for (int i = 0; i < curremt; i++)
            {
                if (!zhushus[i].getText().toString().equals(""))
                {
                    num += Integer.valueOf(zhushus[i].getText().toString());
                }


            }
            allnum.setText(num + "");
        }
    };

    //区域选择
    private View.OnClickListener toolsItemListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            int g = v.getId();
        /*    int g = v.getId();
            List<String> list = new ArrayList<String>();
            for (int i = 0; i < listpeople.size(); i++)
            {
                list.add(listpeople.get(i).getParkName());
            }
            showDialog_workday(list,g);*/
            showDialog_workday(listpeople, g);

        }
    };


    public void showDialog_workday(List<WZ_CRk> listData, final int g)
    {
//        View dialog_layout = (RelativeLayout) PG_JSD.this.getLayoutInflater().inflate(R.layout.customdialog_listview, null);
        View dialog_layout = (RelativeLayout) PG_JSD.this.getLayoutInflater().inflate(R.layout.customdialog_explistview, null);
/*        customDialog_listView = new CustomDialog_Expandlistview(PG_JSD.this, R.style.MyDialog, dialog_layout, list, list, new CustomDialog_ListView.CustomDialogListener()
        {
            @Override
            public void OnClick(Bundle bundle)
            {
                zzsl = bundle.getString("name");
                pianqus[g].setText(zzsl);
            }
        });*/
        customDialog_listView = new CustomDialog_Expandlistview(PG_JSD.this, R.style.mystyle, dialog_layout, listData, new CustomDialog_Expandlistview.CustomDialogListener()
        {
            @Override
            public void OnClick(Bundle bundle)
            {
                parkname = bundle.getString("parkname");
                cbhname = bundle.getString("cbhname");
                pianqus[g].setText(parkname);
                chengbaohus[g].setText(cbhname);
            }
        });
        customDialog_listView.show();
    }

    private void getBreakOffInfoOfContract()
    {
        commembertab commembertab = AppContext.getUserInfo(PG_JSD.this);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("action", "getGoodsInByUid");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String a = responseInfo.result;
                List<WZ_CRk> listNewData = null;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    /*if (result.getAffectedRows() != 0)
                    {*/
                    listNewData = JSON.parseArray(result.getRows().toJSONString(), WZ_CRk.class);
                    listpeople.addAll(listNewData);
//                        for (int i = 0; i < listNewData.size(); i++)
//                        {
////                            expandableListView.expandGroup(i);//展开
//                            expandableListView.collapseGroup(i);//关闭
//                        }

                /*    } else
                    {
                        listNewData = new ArrayList<WZ_CRk>();
                    }*/

                } else
                {
                    AppContext.makeToast(PG_JSD.this, "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(PG_JSD.this, "error_connectServer");
            }
        });
    }

/*    private void getchengbaohu()
    {
        commembertab commembertab = AppContext.getUserInfo(PG_JSD.this);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("parkId", commembertab.getparkId());
        params.addQueryStringParameter("action", "getAreaAndContract");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String a = responseInfo.result;
                List<WZ_CRk> listNewData = null;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    *//*if (result.getAffectedRows() != 0)
                    {*//*
                    listNewData = JSON.parseArray(result.getRows().toJSONString(), WZ_CRk.class);
                    listpeople.addAll(listNewData);
//                        for (int i = 0; i < listNewData.size(); i++)
//                        {
////                            expandableListView.expandGroup(i);//展开
//                            expandableListView.collapseGroup(i);//关闭
//                        }

                *//*    } else
                    {
                        listNewData = new ArrayList<WZ_CRk>();
                    }*//*

                } else
                {
                    AppContext.makeToast(PG_JSD.this, "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(PG_JSD.this, "error_connectServer");
            }
        });
    }*/
}
