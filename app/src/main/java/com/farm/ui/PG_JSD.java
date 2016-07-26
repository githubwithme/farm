package com.farm.ui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.farm.adapter.PG_CBF_CLAdapyer;
import com.farm.adapter.PG_JSD_Adapter;
import com.farm.adapter.WZ_RKExecute_Adapter;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.Park_AllCBH;
import com.farm.bean.Purchaser;
import com.farm.bean.Result;
import com.farm.bean.SellOrder;
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

    @ViewById
    View btn_view;
    @ViewById
    Button btn_upload;
    List<SellOrderDetail_New> listSellData = new ArrayList<SellOrderDetail_New>();
    List<SellOrder_New> listSellOrderData = new ArrayList<SellOrder_New>();
    String jsdId = "";
    CustomDialog_ListView customDialog_listViews;
    String zzsl = "";
    List<Purchaser> listData_BY = new ArrayList<Purchaser>();
    List<Purchaser> listData_BZ = new ArrayList<Purchaser>();
    String byId = "";
    String bzId = "";
    String batchTime="";

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

    List<Park_AllCBH> listpeople = new ArrayList<Park_AllCBH>();
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

    @Click
    void button_add()
    {
        showDialog_workday(listpeople, 1);
    }

    @ViewById
    ListView frame_listview_news;


    @ViewById
    LinearLayout goods_xx;   //一文字
    @ViewById
    LinearLayout isgoods_xx;//产品信息

    @ViewById
    LinearLayout ll_cbhlist;//承包户

    @ViewById
    LinearLayout ll_jsd;//结算单统计
    @ViewById
    TextView tv_isgood;
    @ViewById
    EditText plateNumber;

/*    @Click
    void tv_isgood()
    {
        if (isgoods_xx.isShown())
        {
            goods_xx.setVisibility(View.GONE);
        } else
        {
            goods_xx.setVisibility(View.VISIBLE);
        }
    }*/

    @Click
    void isgoods_xx()
    {
        if (goods_xx.isShown())
        {
            goods_xx.setVisibility(View.GONE);
        } else
        {
            goods_xx.setVisibility(View.VISIBLE);
        }
    }


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
    void button_save()
    {
        SellOrder_New sellOrder = new SellOrder_New();
        sellOrder= sellOrder_new;
        sellOrder.setid(jsdId);
        sellOrder.setInfoId(sellOrder_new.getUuid());
        sellOrder.setUid(sellOrder_new.getUid());
        sellOrder.setUuid(sellOrder_new.getUuid());
        sellOrder.setBatchTime(sellOrder_new.getBatchTime());
        sellOrder.setBuyers(sellOrder_new.getBuyers());
        sellOrder.setPrice(sellOrder_new.getPrice());
        sellOrder.setWeight(sellOrder_new.getWeight());
        sellOrder.setSumvalues(sellOrder_new.getSumvalues());
        sellOrder.setReg(utils.getTime());
        sellOrder.setSaletime(sellOrder_new.getSaletime());
        sellOrder.setYear(utils.getYear());
        sellOrder.setXxzt("0");
        sellOrder.setProducer(sellOrder_new.getProducer());
        sellOrder.setGoodsname(sellOrder_new.getProduct());
        sellOrder.setMainPepole(sellOrder_new.getMainPepole());
        sellOrder.setActualweight(sellOrder_new.getActualweight());
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

        sellOrder.setQualityWaterWeight(zp_ds_zhong.getText().toString());
        sellOrder.setQualityNetWeight(zp_bds_zhong.getText().toString());
        sellOrder.setQualityBalance(zp_jsje.getText().toString());
        sellOrder.setDefectWaterWeight(cp_ds_zhong.getText().toString());
        sellOrder.setDefectNetWeight(cp_jingzhong.getText().toString());
        sellOrder.setDefectBalance(cp_jsje.getText().toString());

        sellOrder.setTotal(jsd_zongjianshu.getText().toString());

        sellOrder.setQualityTotalWeight(jsd_zpjz.getText().toString());
        sellOrder.setDefectTotalWeight(jsd_cpzjs.getText().toString());

        sellOrder.setTotalWeight(jsd_zongjingzhong.getText().toString());
        sellOrder.setPackFee(packFee.getText().toString());
        sellOrder.setCarryFee(carryFee.getText().toString());
        sellOrder.setTotalFee(totalFee.getText().toString());
        sellOrder.setActualMoney(actualMoney.getText().toString());
        StringBuilder builder = new StringBuilder();
        builder.append("{\"sellOrderSettlementlist\":[ ");
        builder.append(JSON.toJSONString(sellOrder));
        builder.append("]} ");
        updatesellOrderSettlement(builder.toString());

    }

    @Click
    void btn_upload()
    {
        if (plateNumber.getText().toString().equals(""))
        {
            Toast.makeText(PG_JSD.this,"请填写车牌号",Toast.LENGTH_SHORT).show();
            return;
        }
        SellOrder_New sellOrder = new SellOrder_New();
        sellOrder=sellOrder_new;
//        sellOrder.setid("");
        sellOrder.setInfoId(sellOrder_new.getInfoId());
        sellOrder.setUid(sellOrder_new.getUid());
        sellOrder.setUuid(sellOrder_new.getUuid());
        sellOrder.setBatchTime(sellOrder_new.getBatchTime());
        sellOrder.setBuyers(sellOrder_new.getBuyers());

        sellOrder.setPrice(sellOrder_new.getPrice());
        sellOrder.setWeight(sellOrder_new.getWeight());
        sellOrder.setSumvalues(sellOrder_new.getSumvalues());

        sellOrder.setReg(utils.getTime());
        sellOrder.setSaletime(sellOrder_new.getSaletime());
        sellOrder.setYear(utils.getYear());
        sellOrder.setXxzt("0");
        sellOrder.setGoodsname(sellOrder.getProduct());
        sellOrder.setProducer(sellOrder.getParkname());
        sellOrder.setPlateNumber(sellOrder.getCarNumber());

        sellOrder.setMainPepole(sellOrder_new.getMainPepole());
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
//        sellOrder.setFreeFinalPay("1");
//        sellOrder.setFreeDeposit("1");

        sellOrder.setQualityWaterWeight(zp_ds_zhong.getText().toString());
        sellOrder.setQualityNetWeight(zp_bds_zhong.getText().toString());
        sellOrder.setQualityBalance(zp_jsje.getText().toString());
        sellOrder.setDefectWaterWeight(cp_ds_zhong.getText().toString());
        sellOrder.setDefectNetWeight(cp_jingzhong.getText().toString());
        sellOrder.setDefectBalance(cp_jsje.getText().toString());

        sellOrder.setTotal(jsd_zongjianshu.getText().toString());

        sellOrder.setQualityTotalWeight(jsd_zpjz.getText().toString());
        sellOrder.setDefectTotalWeight(jsd_cpzjs.getText().toString());

        sellOrder.setTotalWeight(jsd_zongjingzhong.getText().toString());
        sellOrder.setPackFee(packFee.getText().toString());
        sellOrder.setCarryFee(carryFee.getText().toString());
        sellOrder.setTotalFee(totalFee.getText().toString());
        sellOrder.setActualMoney(actualMoney.getText().toString());
        sellOrder.setActualweight(plateNumber.getText().toString());
        StringBuilder builder = new StringBuilder();
        builder.append("{\"sellOrderSettlementlist\":[ ");
        builder.append(JSON.toJSONString(sellOrder));
        builder.append("]} ");
        addsellOrderSettlement(builder.toString());

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
        btn_upload.setVisibility(View.VISIBLE);
        btn_view.setVisibility(View.VISIBLE);
//        getDetailSecBysettleId();
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


        zp_jianshu.addTextChangedListener(jianshu);//正品件数
        cp_jianshu.addTextChangedListener(jianshu);//次品件数

        zp_bds_zhong.addTextChangedListener(zpZongjingzhong);//正品净重
        zp_jianshu.addTextChangedListener(zpZongjingzhong);//正品件数

        jsd_zpprice.addTextChangedListener(zpJSjiner);//正品单价
        jsd_zpjz.addTextChangedListener(zpJSjiner);//正品总净重


        cp_jingzhong.addTextChangedListener(cpZongjingzhong);//次品净重
        cp_jianshu.addTextChangedListener(cpZongjingzhong);//次品件数

        jsd_cpprice.addTextChangedListener(cpJSjiner);//次品单价
        jsd_cpzjs.addTextChangedListener(cpJSjiner);//次品总净重


        bz_nc_danjia.addTextChangedListener(bzallfee);   //包装单价
//        by_nc_danjia.addTextChangedListener(bzallfee);   //搬运单价
        jsd_zongjianshu.addTextChangedListener(bzallfee);  //总件数

        by_nc_danjia.addTextChangedListener(byallfee);   //搬运单价
        jsd_zongjianshu.addTextChangedListener(byallfee);  //总件数


        jsd_zpjz.addTextChangedListener(all_jingzhong);//正品总净重
        jsd_cpzjs.addTextChangedListener(all_jingzhong);//次品总净重

//        zp_jsje//正品结算金额   cp_jsje  //次品结算金额 carryFee//总搬运费  packFee//总包装费all_jine
        zp_jsje.addTextChangedListener(all_jine);//次品总净重
        cp_jsje.addTextChangedListener(all_jine);//次品总净重
        carryFee.addTextChangedListener(all_jine);//次品总净重
        packFee.addTextChangedListener(all_jine);//次品总净重
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        sellOrder_new = getIntent().getParcelableExtra("bean");
        broadcast = getIntent().getStringExtra("broadcast");
        IntentFilter intentfilter_update = new IntentFilter(AppContext.UPDATEMESSAGE_PG_UPDATE_DELETE);
        registerReceiver(receiver_update, intentfilter_update);
        getActionBar().hide();
    }

    BroadcastReceiver receiver_update = new BroadcastReceiver()// 从扩展页面返回信息
    {
        @SuppressWarnings("deprecation")
        @Override
        public void onReceive(Context context, Intent intent)
        {
            getSellOrderDetailSec();
        }
    };


    //总件数
    private TextWatcher jianshu = new TextWatcher()
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
            if (!zp_jianshu.getText().toString().equals("") && !cp_jianshu.getText().toString().equals(""))
            {
                jsd_zongjianshu.setText(Double.valueOf(zp_jianshu.getText().toString()) + Double.valueOf(cp_jianshu.getText().toString()) + "");
            } else if (!zp_jianshu.getText().toString().equals(""))
            {
                jsd_zongjianshu.setText(zp_jianshu.getText().toString());
            } else if (!cp_jianshu.getText().toString().equals(""))
            {
                jsd_zongjianshu.setText(cp_jianshu.getText().toString());
            }


        }
    };

    //正总净重
    private TextWatcher zpZongjingzhong = new TextWatcher()
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

            if (!zp_bds_zhong.getText().toString().equals("") && !zp_jianshu.getText().toString().equals(""))
            {
                jsd_zpjz.setText(Double.valueOf(zp_bds_zhong.getText().toString()) * Double.valueOf(zp_jianshu.getText().toString()) + "");
            }


        }
    };

    //正品的结算金额
    private TextWatcher zpJSjiner = new TextWatcher()
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

            if (!jsd_zpprice.getText().toString().equals("") && !jsd_zpjz.getText().toString().equals(""))
            {
                zp_jsje.setText(Double.valueOf(jsd_zpprice.getText().toString()) * Double.valueOf(jsd_zpjz.getText().toString()) + "");
            }

        }
    };

    //次品总净重
    private TextWatcher cpZongjingzhong = new TextWatcher()
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

            if (!cp_jianshu.getText().toString().equals("") && !cp_jingzhong.getText().toString().equals(""))
            {
                jsd_cpzjs.setText(Double.valueOf(cp_jianshu.getText().toString()) * Double.valueOf(cp_jingzhong.getText().toString()) + "");
            }

        }
    };

    //次品的结算金额
    private TextWatcher cpJSjiner = new TextWatcher()
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

            if (!jsd_cpzjs.getText().toString().equals("") && !jsd_cpprice.getText().toString().equals(""))
            {
                cp_jsje.setText(Double.valueOf(jsd_cpzjs.getText().toString()) * Double.valueOf(jsd_cpprice.getText().toString()) + "");
            }

        }
    };

    //包装总费
    private TextWatcher bzallfee = new TextWatcher()
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
          /*  bz_nc_danjia.addTextChangedListener(otherjiner);   //包装单价
            by_nc_danjia.addTextChangedListener(otherjiner);   //搬运单价
            jsd_zongjianshu.addTextChangedListener(otherjiner);  //总件数*/
//            packFee 包装  carryFee搬运
            if (!bz_nc_danjia.getText().toString().equals("") && !jsd_zongjianshu.getText().toString().equals(""))
            {
                packFee.setText(Double.valueOf(bz_nc_danjia.getText().toString()) * Double.valueOf(jsd_zongjianshu.getText().toString()) + "");
            }

   /*         if (!by_nc_danjia.getText().toString().equals("") && !jsd_zongjianshu.getText().toString().equals(""))
            {
                carryFee.setText(Double.valueOf(by_nc_danjia.getText().toString())*Double.valueOf(jsd_zongjianshu.getText().toString())+"");
            }*/
        }
    };
    //搬运总费
    private TextWatcher byallfee = new TextWatcher()
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
          /*  bz_nc_danjia.addTextChangedListener(otherjiner);   //包装单价
            by_nc_danjia.addTextChangedListener(otherjiner);   //搬运单价
            jsd_zongjianshu.addTextChangedListener(otherjiner);  //总件数*/
//            packFee 包装  carryFee搬运


            if (!by_nc_danjia.getText().toString().equals("") && !jsd_zongjianshu.getText().toString().equals(""))
            {
                carryFee.setText(Double.valueOf(by_nc_danjia.getText().toString()) * Double.valueOf(jsd_zongjianshu.getText().toString()) + "");
            }
        }
    };
    //总净重
    private TextWatcher all_jingzhong = new TextWatcher()
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


            if (!jsd_zpjz.getText().toString().equals("") && !jsd_cpzjs.getText().toString().equals(""))
            {
                jsd_zongjingzhong.setText(Double.valueOf(jsd_zpjz.getText().toString()) + Double.valueOf(jsd_cpzjs.getText().toString()) + "");
            }
        }
    };
    //总净重
    private TextWatcher all_jine = new TextWatcher()
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
//        zp_jsje//正品结算金额   cp_jsje  //次品结算金额 carryFee//总搬运费  packFee//总包装费all_jine

            double allvalue = 0;
            if (!zp_jsje.getText().toString().equals(""))
            {
                allvalue += Double.valueOf(zp_jsje.getText().toString());
            }
            if (!cp_jsje.getText().toString().equals(""))
            {
                allvalue += Double.valueOf(cp_jsje.getText().toString());
            }
            if (!carryFee.getText().toString().equals(""))
            {
                allvalue += Double.valueOf(carryFee.getText().toString());
            }
            if (!packFee.getText().toString().equals(""))
            {
                allvalue += Double.valueOf(packFee.getText().toString());
            }
            totalFee.setText(allvalue + "");
        }
    };
    //区域选择
    private View.OnClickListener toolsItemListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            int g = v.getId();
            showDialog_workday(listpeople, g);

        }
    };


    public void showDialog_workday(List<Park_AllCBH> listData, final int g)
    {
        View dialog_layout = PG_JSD.this.getLayoutInflater().inflate(R.layout.customdialog_explistview, null);

        customDialog_listView = new CustomDialog_Expandlistview(PG_JSD.this, R.style.cut_expand, dialog_layout, listData, new CustomDialog_Expandlistview.CustomDialogListener()
        {
            @Override
            public void OnClick(Bundle bundle)
            {
                String areaId = bundle.getString("areaId");
                String areaName = bundle.getString("areaName");
                String contractId = bundle.getString("contractId");
                String contractNum = bundle.getString("contractNum");

                String parkId = bundle.getString("parkId");
                String parkName = bundle.getString("parkName");

//                sellOrder_new
                String uuid = java.util.UUID.randomUUID().toString();
                SellOrderDetail_New sellOrderDetail_new=new SellOrderDetail_New();
                sellOrderDetail_new.setUuid(uuid);
                sellOrderDetail_new.setuid(sellOrder_new.getUid());
                sellOrderDetail_new.setInfoId(uuid);
                sellOrderDetail_new.setSettlementId(jsdId);
                sellOrderDetail_new.setBatchTime(batchTime);
                sellOrderDetail_new.setYear(utils.getYear());

                sellOrderDetail_new.setparkid(parkId);
                sellOrderDetail_new.setparkname(parkName);
                sellOrderDetail_new.setareaid(areaId);
                sellOrderDetail_new.setareaname(areaName);
                sellOrderDetail_new.setcontractid(contractId);
                sellOrderDetail_new.setcontractname(contractNum);

                sellOrderDetail_new.setactualnumber("");
                sellOrderDetail_new.setplanprice("");
                sellOrderDetail_new.setactualprice("");
                sellOrderDetail_new.setactualweight("");
                StringBuilder builder = new StringBuilder();
                builder.append("{\"SellOrderDetailSeclist\": [");
                builder.append(JSON.toJSONString(sellOrderDetail_new));
                builder.append("]} ");
                addSellOrderDetailSec(builder.toString());
                StringBuilder builder1 = new StringBuilder();
                builder1.append("{\"SellOrderDetail_newlist\": [");
                builder1.append(JSON.toJSONString(sellOrderDetail_new));
                builder1.append("]} ");
                addSellOrderDetail_new(builder1.toString());

            }
        });
        customDialog_listView.show();
    }


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
                        listpeople.addAll(listNewData);
          /*              for (int i = 0; i < listNewData.size(); i++)
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
                        }*/


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
                        if (listNewData.size()>0)
                        {
                            batchTime=listNewData.get(0).getBatchTime();
                        }

                        listSellData.addAll(listNewData);



       /*      pg_jsd_adapter = new PG_JSD_Adapter(PG_JSD.this, listNewData, sellOrder_new.getQualityNetWeight(), sellOrder_new.getDefectNetWeight());
                        frame_listview_news.setAdapter(pg_jsd_adapter);
                        utils.setListViewHeight(frame_listview_news);
                        shouData(listNewData);*/
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
                        Toast.makeText(PG_JSD.this, "订单保存成功！", Toast.LENGTH_SHORT).show();

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

    //承包户
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
        View dialog_layout = PG_JSD.this.getLayoutInflater().inflate(R.layout.customdialog_listview, null);
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
        View dialog_layout = PG_JSD.this.getLayoutInflater().inflate(R.layout.customdialog_listview, null);
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

    public void shouData(List<SellOrderDetail_New> JSD_listData)
    {

        double zpjianshu = 0;
        double cpjianshu = 0;
        if (JSD_listData.size() > 0)
        {
            for (int i = 0; i < JSD_listData.size(); i++)
            {
                if (!JSD_listData.get(i).getplanprice().equals(""))
                {
                    cpjianshu += Double.valueOf(JSD_listData.get(i).getplanprice());
                }
                if (!JSD_listData.get(i).getactualprice().equals(""))
                {
                    zpjianshu += Double.valueOf(JSD_listData.get(i).getactualprice());
                }

            }
            zp_jianshu.setText(zpjianshu + "");
            cp_jianshu.setText(cpjianshu + "");
//            jsd_zongjianshu.setText(zpjianshu+cpjianshu+"");
        }

    }

    private void isnewaddOrder(String data)
    {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("action", "addsellOrderSettlement");
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

                        jsdId = result.getAffectedRows() + "";
                        List<SellOrderDetail_New> listAddSellData = new ArrayList<SellOrderDetail_New>();

                        for (int i = 0; i < listSellData.size(); i++)
                        {
                            SellOrderDetail_New sellOrderDetail_new = new SellOrderDetail_New();
                            sellOrderDetail_new.setSettlementId(jsdId);
                            sellOrderDetail_new.setInfoId(listSellData.get(i).getUuid());
                            sellOrderDetail_new.setuid(listSellData.get(i).getuid());
                            sellOrderDetail_new.setBatchTime(listSellData.get(i).getBatchTime());
                            sellOrderDetail_new.setYear(listSellData.get(i).getYear());
                            sellOrderDetail_new.setparkid(listSellData.get(i).getparkid());
                            sellOrderDetail_new.setparkname(listSellData.get(i).getparkname());
                            sellOrderDetail_new.setareaid(listSellData.get(i).getareaid());
                            sellOrderDetail_new.setareaname(listSellData.get(i).getareaname());
                            sellOrderDetail_new.setcontractid(listSellData.get(i).getcontractid());
                            sellOrderDetail_new.setcontractname(listSellData.get(i).getcontractname());
                            listAddSellData.add(sellOrderDetail_new);
                        }
                        StringBuilder builder = new StringBuilder();
                        builder.append("{\"SellOrderDetailSeclist\": ");
                        builder.append(JSON.toJSONString(listAddSellData));
                        builder.append("} ");
                        addSellOrderDetailSec(builder.toString());


                   /*     Toast.makeText(PG_JSD.this, "订单保存成功！", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        intent.setAction(AppContext.UPDATEMESSAGE_FARMMANAGER);
                        sendBroadcast(intent);
                        finish();*/
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


    private void addSellOrderDetailSec(String data)
    {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("action", "addSellOrderDetailSec");
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

                        getSellOrderDetailSec();
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
    private void addSellOrderDetail_new(String data)
    {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("action", "addSellOrderDetail_new");
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
    public void getSellOrderDetailSec()
    {
        commembertab commembertab = AppContext.getUserInfo(PG_JSD.this);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("strWhere", "uid=" + commembertab.getuId() + " and settlementId=" + jsdId);
        params.addQueryStringParameter("action", "getSellOrderDetailSec");
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
                        btn_upload.setVisibility(View.GONE);
                        ll_cbhlist.setVisibility(View.VISIBLE);
                        ll_jsd.setVisibility(View.VISIBLE);
                        listNewData = JSON.parseArray(result.getRows().toJSONString(), SellOrderDetail_New.class);


                        pg_jsd_adapter = new PG_JSD_Adapter(PG_JSD.this, listNewData, sellOrder_new.getQualityNetWeight(), sellOrder_new.getDefectNetWeight());
                        frame_listview_news.setAdapter(pg_jsd_adapter);
                        utils.setListViewHeight(frame_listview_news);
                        shouData(listNewData);
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

    private void updatesellOrderSettlement(String data)
    {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("action", "updatesellOrderSettlement");
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

                        Toast.makeText(PG_JSD.this,"保存成功",Toast.LENGTH_SHORT).show();
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
    private void addsellOrderSettlement(String data)
    {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("action", "updatesellOrderSettlement");
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


                        List<SellOrderDetail_New> listAddSellData = new ArrayList<SellOrderDetail_New>();

                        for (int i = 0; i < listSellData.size(); i++)
                        {
                            SellOrderDetail_New sellOrderDetail_new = new SellOrderDetail_New();
                            sellOrderDetail_new.setSettlementId(jsdId);
                            sellOrderDetail_new.setInfoId(listSellData.get(i).getUuid());
                            sellOrderDetail_new.setuid(listSellData.get(i).getuid());
                            sellOrderDetail_new.setBatchTime(listSellData.get(i).getBatchTime());
                            sellOrderDetail_new.setYear(listSellData.get(i).getYear());
                            sellOrderDetail_new.setparkid(listSellData.get(i).getparkid());
                            sellOrderDetail_new.setparkname(listSellData.get(i).getparkname());
                            sellOrderDetail_new.setareaid(listSellData.get(i).getareaid());
                            sellOrderDetail_new.setareaname(listSellData.get(i).getareaname());
                            sellOrderDetail_new.setcontractid(listSellData.get(i).getcontractid());
                            sellOrderDetail_new.setcontractname(listSellData.get(i).getcontractname());
                            listAddSellData.add(sellOrderDetail_new);
                        }
                        StringBuilder builder = new StringBuilder();
                        builder.append("{\"SellOrderDetailSeclist\": ");
                        builder.append(JSON.toJSONString(listAddSellData));
                        builder.append("} ");
                        addSellOrderDetailSec(builder.toString());



                  /*      Intent intent = new Intent();
                        intent.setAction(AppContext.UPDATEMESSAGE_PGDETAIL_UPDATE_DELETE);
                        sendBroadcast(intent);
                        Toast.makeText(PG_JSD_Detail.this,"保存成功",Toast.LENGTH_SHORT).show();
                        finish();*/
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
}
