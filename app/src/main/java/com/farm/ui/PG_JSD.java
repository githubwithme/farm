package com.farm.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.adapter.CustomArray_cbh_Adapter;
import com.farm.adapter.PG_JSD_Adapter;
import com.farm.adapter.WZ_RKExecute_Adapter;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.Park_AllCBH;
import com.farm.bean.Purchaser;
import com.farm.bean.Result;
import com.farm.bean.SellOrderDetail_New;
import com.farm.bean.SellOrder_New;
import com.farm.bean.SellOrder_New_First;
import com.farm.bean.WZ_CRk;
import com.farm.bean.Wz_Storehouse;
import com.farm.bean.commembertab;
import com.farm.bean.contractTab;
import com.farm.common.utils;
import com.farm.widget.CustomArrayAdapter;
import com.farm.widget.CustomDialog_Expandlistview;
import com.farm.widget.CustomDialog_ListView;
import com.farm.widget.MyDialog;
import com.farm.widget.PullToRefreshListView;
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
import org.apache.http.entity.StringEntity;
import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hasee on 2016/6/21.
 */
@EActivity(R.layout.pg_jsd)
public class PG_JSD extends Activity
{
    CustomDialog_ListView customDialog_listViews;
    String zzsl = "";
    List<Purchaser> listData_BY = new ArrayList<Purchaser>();
    List<Purchaser> listData_BZ = new ArrayList<Purchaser>();
    String byId = "";
    String bzId = "";

    PG_JSD_Adapter pg_jsd_adapter;
    SellOrder_New sellOrder_new;
    String broadcast;
    CustomArray_cbh_Adapter customArray_cbh_adapter;
    //    ArrayAdapter<String> CustomArray_cbh_Adapter = null;  //省级适配器
    private String[] mProvinceDatas = new String[]{"全部分场", "乐丰分场", "双桥分场"};
    private String[] mDatas;
    @ViewById
    RadioButton rb_nc_chose;  //包装费农场请人
    @ViewById
    RadioButton rb_kh_chose; //客户自带
    @ViewById
    RadioButton nc_banyun;   //搬运  农场
    @ViewById
    RadioButton kh_banyun;//客户
    @ViewById
    RadioButton wu_banyun;

    @ViewById
    TextView zp_jingzhong;

    @ViewById
    LinearLayout other_baozhuang;
    @ViewById
    LinearLayout nc_baozhuang;

    @ViewById
    LinearLayout ll_nobanyun;
    @ViewById
    LinearLayout ll_banyun;

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
    //        CustomDialog_ListView customDialog_listView;
    CustomDialog_Expandlistview customDialog_listView;

    List<WZ_CRk> listpeople = new ArrayList<WZ_CRk>();
    List<contractTab> listdata = new ArrayList<contractTab>();

    private String[] areaId = new String[30];
    private String[] contractId = new String[30];
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

    @ViewById
    EditText bz_khnote; //包装客户自带说明
    @ViewById
    EditText bz_nc_danjia;//包装农场  包装单价
    @ViewById
    TextView bz_fzrid;//包装负责人Id

    @ViewById
    EditText by_khnote; //搬运客户自带说明
    @ViewById
    EditText by_nc_danjia;//搬运农场  搬运单价
    @ViewById
    TextView by_fzrid;//搬运负责人Id

    @ViewById
    EditText jsd_zongjianshu;//总件数
    @ViewById
    EditText jsd_zongjingzhong;//总净重
    @ViewById
    EditText jsd_zpjz;//正品总净重
    @ViewById
    EditText jsd_cpzjs;//次品总净重
    @ViewById
    EditText jsd_zpprice;//正品单价
    @ViewById
    EditText jsd_cpprice;//次品单价
    @ViewById
    EditText zp_jianshu;//正品件数
    @ViewById
    EditText cp_jianshu;//次品件数

    @ViewById
    EditText zp_ds_zhong;//正品带水重
    @ViewById
    EditText cp_ds_zhong;//次品带水重
    @ViewById
    EditText zp_bds_zhong;  //正品净重
    @ViewById
    EditText cp_jingzhong;   //次品净重
    @ViewById
    EditText zp_jsje;       //正品结算金额
    @ViewById
    EditText cp_jsje;      //次品 结算金额

    @ViewById
    EditText carryFee; //总搬运费
    @ViewById
    EditText packFee;// 总包装费
    @ViewById
    EditText totalFee;// 合计金额
    @ViewById
    EditText actualMoney;//实际金额

    @ViewById
    EditText packPec;
   /* @ViewById
    ListView pg_sale;*/

    @ViewById
    PullToRefreshListView frame_listview_news;

    @Click
    void bz_fzrid()
    {
        List<String> listdata = new ArrayList<String>();
        List<String> listid = new ArrayList<String>();
        for (int i = 0; i < listData_BZ.size(); i++)
        {
            listdata.add(listData_BZ.get(i).getName());
            listid.add(listData_BZ.get(i).getId());
        }
        showDialog_bz(listdata, listid);
    }

    @Click
    void by_fzrid()
    {
        List<String> listdata = new ArrayList<String>();
        List<String> listid = new ArrayList<String>();
        for (int i = 0; i < listData_BY.size(); i++)
        {
            listdata.add(listData_BY.get(i).getName());
            listid.add(listData_BY.get(i).getId());
        }
        showDialog_by(listdata, listid);
    }

    @Click
    void nc_banyun()
    {
        ll_nobanyun.setVisibility(View.GONE);
        ll_banyun.setVisibility(View.VISIBLE);
    }

    @Click
    void kh_banyun()
    {
        ll_nobanyun.setVisibility(View.VISIBLE);
        ll_banyun.setVisibility(View.GONE);
    }

    @Click
    void wu_banyun()
    {
        ll_nobanyun.setVisibility(View.GONE);
        ll_banyun.setVisibility(View.GONE);
    }

    @Click
    void rb_nc_chose()
    {
        other_baozhuang.setVisibility(View.GONE);
        nc_baozhuang.setVisibility(View.VISIBLE);
    }

    @Click
    void rb_kh_chose()
    {
        other_baozhuang.setVisibility(View.VISIBLE);
        nc_baozhuang.setVisibility(View.GONE);
    }


    @Click
    void btn_upload()
    {
        SellOrder_New sellOrder = new SellOrder_New();
//        sellOrder=sellOrder_new;
        sellOrder.setid("");
        sellOrder.setUid(sellOrder_new.getUid());
        sellOrder.setUuid(sellOrder_new.getUuid());
        sellOrder.setBatchTime(sellOrder_new.getBatchTime());
//        sellOrder.setSelltype("交易中");//
        sellOrder.setStatus("0");
//        sellOrder.setBuyers(et_name.getText().toString());
        sellOrder.setBuyers(sellOrder_new.getBuyersId());

        sellOrder.setPrice(sellOrder_new.getPrice());
        sellOrder.setWeight(sellOrder_new.getWeight());
        sellOrder.setSumvalues(sellOrder_new.getSumvalues());
   /*     sellOrder.setActualprice("");
        sellOrder.setActualweight("");
        sellOrder.setActualnumber("");
        sellOrder.setActualsumvalues("");*/
//        sellOrder.setDeposit("0");
        sellOrder.setReg(utils.getTime());
//        sellOrder.setSaletime(utils.getTime());
        sellOrder.setSaletime(sellOrder_new.getSaletime());
        sellOrder.setYear(utils.getYear());
        sellOrder.setXxzt("0");
        sellOrder.setProducer(sellOrder_new.getProducer());
//        sellOrder.setFinalpayment("0");
        sellOrder.setGoodsname(sellOrder_new.getGoodsname());
        sellOrder.setMainPepole(sellOrder_new.getMainPepole());
        sellOrder.setPlateNumber(sellOrder_new.getPlateNumber());
        sellOrder.setContractorId(byId);
        sellOrder.setPickId(bzId);
        sellOrder.setCarryPrice(sellOrder_new.getCarryPrice());
        sellOrder.setPackPrice(sellOrder_new.getPackPrice());
        sellOrder.setPackPec(sellOrder_new.getPackPec());
        sellOrder.setWaitDeposit(sellOrder_new.getWaitDeposit());
        sellOrder.setAddress(sellOrder_new.getAddress());
        sellOrder.setPackPrice(bz_nc_danjia.getText().toString());
        sellOrder.setCarryPrice(by_nc_danjia.getText().toString());
        //
        sellOrder.setActualprice(jsd_zpprice.getText().toString());//  正品单价
        sellOrder.setDefectPrice(jsd_cpprice.getText().toString());//  次品单价
        sellOrder.setActualnumber(zp_jianshu.getText().toString());//  正品件数
        sellOrder.setDefectNum(cp_jianshu.getText().toString());//  次品件数
        sellOrder.setPackPec(packPec.getText().toString());//  包装规格

        sellOrder.setSelltype("审批结算");//  包装规格
        sellOrder.setIsNeedAudit("0");
        sellOrder.setFreeFinalPay("1");
        sellOrder.setFreeDeposit("1");
/*        public String total ;//总件数
        public String qualityTotalWeight;//正品总净重
        public String defectTotalWeight;//次品重净重
        public String TotalWeight;//总净重
        public String packFee;//总包装费
        public String carryFee;//总搬运费
        public String totalFee;//总合计金额
           public String personNote;//搬运说明
    public String actualMoney;//实际金额*/
        //附表2
        SellOrder_New_First sellOrder_new_first = new SellOrder_New_First();
        sellOrder_new_first.setSellOrderId(sellOrder_new.getUuid());
        sellOrder_new_first.setQualityWaterWeight(zp_ds_zhong.getText().toString());
        sellOrder_new_first.setQualityNetWeight(zp_bds_zhong.getText().toString());
        sellOrder_new_first.setQualityBalance(zp_jsje.getText().toString());
        sellOrder_new_first.setDefectWaterWeight(cp_ds_zhong.getText().toString());
        sellOrder_new_first.setDefectNetWeight(cp_jingzhong.getText().toString());
        sellOrder_new_first.setDefectBalance(cp_jsje.getText().toString());

        sellOrder_new_first.setTotal(jsd_zongjianshu.getText().toString());

        sellOrder_new_first.setQualityTotalWeight(jsd_zpjz.getText().toString());
        sellOrder_new_first.setDefectTotalWeight(jsd_cpzjs.getText().toString());

        sellOrder_new_first.setTotalWeight(jsd_zongjingzhong.getText().toString());
        sellOrder_new_first.setPackFee(packFee.getText().toString());
        sellOrder_new_first.setCarryFee(carryFee.getText().toString());
        sellOrder_new_first.setTotalFee(totalFee.getText().toString());
        sellOrder_new_first.setActualMoney(actualMoney.getText().toString());
//        sellOrder_new_first.setPersonNote(totalFee.getText().toString());

        StringBuilder builder = new StringBuilder();
        builder.append("{\"SellOrder_new\":[ ");
        builder.append(JSON.toJSONString(sellOrder));
        builder.append("], \"sellorderlistadd\": [");
        builder.append(JSON.toJSONString(sellOrder_new_first));
        builder.append("]} ");
        newaddOrder(builder.toString());
    }

    @Click
    void btn_save()
    {
        addView();
        curremt++;
    }

    private void addView()
    {
//        View view = inflater.inflate(R.layout.pg_dtcbh, null);
        View view = inflater.inflate(R.layout.pg_jsd_adapter, null);
        view.setId(curremt);
        TextView all_cbh = (TextView) findViewById(R.id.all_cbh);
/*        EditText cipin = (EditText) view.findViewById(R.id.cipin);
        cipin.addTextChangedListener(oncigpin);
        EditText jinzhong = (EditText) view.findViewById(R.id.jinzhong);
        jinzhong.addTextChangedListener(onjinzhong);*/
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT, 1);
        lp.setMargins(0, 0, 0, 0);
        all_cbh.setLayoutParams(lp);
/*        zhushu.setLayoutParams(lp);
        zhengpin.setLayoutParams(lp);
        cipin.setLayoutParams(lp);
        jinzhong.setLayoutParams(lp);
        pg_dts.addView(view);
        zhushus[curremt] = zhushu;
        zhengpins[curremt] = zhengpin;
        cipins[curremt] = cipin;
        zhongliangs[curremt] = jinzhong;*/

        views[curremt] = view;
    }

    @AfterViews
    void after()
    {

        byId = sellOrder_new.getContractorId();
        bzId = sellOrder_new.getPickId();

        getpurchaser("");
        showData();
        getsellOrderDetailBySaleId();
        getchengbaohu();
//        getBreakOffInfoOfContract();
        inflater = LayoutInflater.from(PG_JSD.this);

    }

    private void showData()
    {
        bz_nc_danjia.setText(sellOrder_new.getPackPrice());
        by_nc_danjia.setText(sellOrder_new.getCarryPrice());
        by_fzrid.setText(sellOrder_new.getContractorName());
        bz_fzrid.setText(sellOrder_new.getPickName());
        jsd_zpprice.setText(sellOrder_new.getPrice());
        actualMoney.setText(sellOrder_new.getActualMoney());
        packPec.setText(sellOrder_new.getPackPec());

        packFee.setText(sellOrder_new.getPackFee());
        carryFee.setText(sellOrder_new.getCarryFee());
        cp_jsje.setText(sellOrder_new.getDefectBalance());
        zp_jsje.setText(sellOrder_new.getQualityBalance());
        cp_jingzhong.setText(sellOrder_new.getDefectNetWeight());
        zp_bds_zhong.setText(sellOrder_new.getQualityNetWeight());
        cp_ds_zhong.setText(sellOrder_new.getDefectWaterWeight());
        zp_ds_zhong.setText(sellOrder_new.getQualityWaterWeight());
        cp_jianshu.setText(sellOrder_new.getDefectNum());
        zp_jianshu.setText(sellOrder_new.getActualnumber());
        jsd_cpprice.setText(sellOrder_new.getDefectPrice());
//        jsd_zpprice.setText(sellOrder_new.getActualprice());
        jsd_cpzjs.setText(sellOrder_new.getDefectTotalWeight());
        jsd_zpjz.setText(sellOrder_new.getQualityTotalWeight());
        jsd_zongjingzhong.setText(sellOrder_new.getTotalWeight());
        jsd_zongjianshu.setText(sellOrder_new.getTotal());
        totalFee.setText(sellOrder_new.getTotalFee());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        sellOrder_new = getIntent().getParcelableExtra("bean");
        broadcast = getIntent().getStringExtra("broadcast");
        getActionBar().hide();
    }


    //全部净重自动
    private TextWatcher onzidong = new TextWatcher()
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

/*    private void getBreakOffInfoOfContract()
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

    private void getchengbaohu()
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
                List<Park_AllCBH> listNewData = null;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    if (result.getAffectedRows() != 0)
                    {
                        listNewData = JSON.parseArray(result.getRows().toJSONString(), Park_AllCBH.class);

                        for (int i = 0; i < listNewData.size(); i++)
                        {
                            for (int j = 0; j < listNewData.get(i).getContractList().size(); j++)
                            {
                                contractTab contractTab = new contractTab();
                                contractTab = listNewData.get(i).getContractList().get(j);
                                contractTab.setparkName(listNewData.get(i).getParkName());
                                contractTab.setareaName(listNewData.get(i).getAreaName());
                                listdata.add(contractTab);
                            }
                        }
                        int xxx = listdata.size();
                        mDatas = new String[listdata.size()];
                        for (int k = 0; k < listdata.size(); k++)
                        {
                            mDatas[k] = listdata.get(k).getareaName() + "\n" + listdata.get(k).getContractNum();
                        }
//                        for (int i = 0; i < listNewData.size(); i++)
//                        {
////                            expandableListView.expandGroup(i);//展开
//                            expandableListView.collapseGroup(i);//关闭
//                        }

                    } else
                    {
                        listNewData = new ArrayList<Park_AllCBH>();
                    }

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


    private void getsellOrderDetailBySaleId()
    {


        commembertab commembertab = AppContext.getUserInfo(PG_JSD.this);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("saleId", sellOrder_new.getUuid());
        params.addQueryStringParameter("action", "getsellOrderDetailBySaleId");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String a = responseInfo.result;
                List<SellOrderDetail_New> listNewData = null;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    if (result.getAffectedRows() != 0)
                    {


                        listNewData = JSON.parseArray(result.getRows().toJSONString(), SellOrderDetail_New.class);
                        pg_jsd_adapter = new PG_JSD_Adapter(PG_JSD.this, listNewData, sellOrder_new.getQualityNetWeight(), sellOrder_new.getDefectNetWeight());
                        frame_listview_news.setAdapter(pg_jsd_adapter);
                        utils.setListViewHeight(frame_listview_news);

                    } else
                    {
                        listNewData = new ArrayList<SellOrderDetail_New>();
                    }

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

    private void newaddOrder(String data)
    {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("action", "editOrder");
        params.setContentType("application/json");
        try
        {
            params.setBodyEntity(new StringEntity(data, "utf-8"));
        } catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        HttpUtils http = new HttpUtils();
        http.configTimeout(60000);
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String a = responseInfo.result;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    if (result.getAffectedRows() != 0)
                    {
                        Toast.makeText(PG_JSD.this, "订单修改成功！", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent();
                        intent.setAction(AppContext.UPDATEMESSAGE_FARMMANAGER);
                        sendBroadcast(intent);

                        finish();
                    }

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

    private void getpurchaser(String name)
    {
        commembertab commembertab = AppContext.getUserInfo(PG_JSD.this);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("action", "getpurchaser");//jobGetList1
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String a = responseInfo.result;
                List<Purchaser> listNewData = null;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    if (result.getAffectedRows() != 0)
                    {
                        if (result.getAffectedRows() != 0)
                        {
                            listNewData = JSON.parseArray(result.getRows().toJSONString(), Purchaser.class);
                            for (int i = 0; i < listNewData.size(); i++)
                            {
                                if (listNewData.get(i).userType.equals("采购商"))
                                {
                                } else if (listNewData.get(i).userType.equals("包装工头"))
                                {
                                    listData_BZ.add(listNewData.get(i));
                                } else
                                {
                                    listData_BY.add(listNewData.get(i));
                                }
                            }


                  /*          String[] str = new String[listData_CG.size()];
                            for (int i = 0; i < listData_CG.size(); i++)
                            {
                                str[i] = listData_CG.get(i).getName();
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(NCZ_CreateNewOrder.this,
                                    android.R.layout.simple_dropdown_item_1line, str);
                            et_name.setAdapter(adapter);
                            et_name.setOnItemClickListener(new AdapterView.OnItemClickListener()
                            {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
                                {
                                    Object obj = adapterView.getItemAtPosition(i);
                                    String ss = obj.toString();
                                    for (int j = 0; j < listData_CG.size(); j++)
                                    {
                                        if (listData_CG.get(j).getName().equals(ss))
                                        {
                                            et_phone.setText(listData_CG.get(j).getTelephone());
                                            et_address.setText(listData_CG.get(j).getAddress());
                                            et_email.setText(listData_CG.get(j).getMailbox());
                                        }
                                    }
                                }
                            });*/

                        } else
                        {
                            listNewData = new ArrayList<Purchaser>();
                        }

                    } else
                    {
                        listNewData = new ArrayList<Purchaser>();
                    }

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

    //包装工
    public void showDialog_bz(List<String> listdata, List<String> listid)
    {
        View dialog_layout = (RelativeLayout) PG_JSD.this.getLayoutInflater().inflate(R.layout.customdialog_listview, null);
        customDialog_listViews = new CustomDialog_ListView(PG_JSD.this, R.style.MyDialog, dialog_layout, listdata, listid, new CustomDialog_ListView.CustomDialogListener()
        {
            @Override
            public void OnClick(Bundle bundle)
            {
                //id也是有的
                zzsl = bundle.getString("name");
                bz_fzrid.setText(zzsl);
                bzId = bundle.getString("id");

            }
        });
        customDialog_listViews.show();
    }

    //搬运工
    public void showDialog_by(List<String> listdata, List<String> listid)
    {
        View dialog_layout = (RelativeLayout) PG_JSD.this.getLayoutInflater().inflate(R.layout.customdialog_listview, null);
        customDialog_listViews = new CustomDialog_ListView(PG_JSD.this, R.style.MyDialog, dialog_layout, listdata, listid, new CustomDialog_ListView.CustomDialogListener()
        {
            @Override
            public void OnClick(Bundle bundle)
            {
                //id也是有的
                zzsl = bundle.getString("name");
                by_fzrid.setText(zzsl);
                byId = bundle.getString("id");

            }
        });
        customDialog_listViews.show();
    }
}
