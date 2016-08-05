package com.farm.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.farm.R;
import com.farm.adapter.NCZ_Look_JSD_Adapter;
import com.farm.adapter.PG_JSD_Adapter;
import com.farm.bean.SellOrder_New;
import com.farm.common.utils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by hasee on 2016/7/19.
 */

@EActivity(R.layout.ncz_look_jsd_detail)
//@EActivity(R.layout.ncz_look_jsd_newdetail)
public class NCZ_Look_JSD_Detail extends Activity
{

    String goodsnames;
    NCZ_Look_JSD_Adapter ncz_look_jsd_adapter;
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


    @ViewById
    TextView bz_nc_danjia;//包装农场  包装单价
    @ViewById
    TextView bz_fzrid;//包装负责人Id


    @ViewById
    TextView by_nc_danjia;//搬运农场  搬运单价
    @ViewById
    TextView by_fzrid;//搬运负责人Id

    @ViewById
    TextView jsd_zongjianshu;//总件数
    @ViewById
    TextView jsd_zongjingzhong;//总净重
    @ViewById
    TextView jsd_zpjz;//正品总净重
    @ViewById
    TextView jsd_cpzjs;//次品总净重
    @ViewById
    TextView jsd_zpprice;//正品单价
    @ViewById
    TextView jsd_cpprice;//次品单价
    @ViewById
    TextView zp_jianshu;//正品件数
    @ViewById
    TextView cp_jianshu;//次品件数

    @ViewById
    TextView zp_ds_zhong;//正品带水重
    @ViewById
    TextView cp_ds_zhong;//次品带水重
    @ViewById
    TextView zp_bds_zhong;  //正品净重
    @ViewById
    TextView cp_jingzhong;   //次品净重
    @ViewById
    TextView zp_jsje;       //正品结算金额
    @ViewById
    TextView cp_jsje;      //次品 结算金额

    @ViewById
    TextView carryFee; //总搬运费
    @ViewById
    TextView packFee;// 总包装费
    @ViewById
    TextView totalFee;// 合计金额
    @ViewById
    TextView actualMoney;//实际金额

    @ViewById
    TextView packPec;
    @ViewById
    LinearLayout goods_xx;   //一文字
    @ViewById
    LinearLayout isgoods_xx;//产品信息

    @ViewById
    LinearLayout ll_cbhlist;//承包户

    @ViewById
    LinearLayout ll_jsd;//结算单统计
/*    @ViewById
    TextView tv_isgood;*/
    @ViewById
    TextView plateNumber;

    @ViewById
    ListView frame_listview_news;

    SellOrder_New sellOrder_new;

    @ViewById
    LinearLayout cbh_isshow;

    @ViewById
    TextView goodsname;
    @ViewById
    TextView goodsname2;
    @ViewById
    TextView bz_nc_danjia1;
    @ViewById
    TextView jsd_zongjianshu1;
    @ViewById
    TextView all_weight1;
    @ViewById
    TextView by_nc_danjia1;



    @Click
    void ll_cbhlist()
    {
        if (!cbh_isshow.isShown())
        {
            cbh_isshow.setVisibility(View.VISIBLE);
        } else
        {
            cbh_isshow.setVisibility(View.GONE);
        }
    }

/*    @Click
    void is_jsd()
    {
        if (!ll_jsd.isShown())
        {
            ll_jsd.setVisibility(View.VISIBLE);
        } else
        {
            ll_jsd.setVisibility(View.GONE);
        }
    }*/

    @Click
    void isgoods_xx()
    {
        if (!goods_xx.isShown())
        {
            goods_xx.setVisibility(View.VISIBLE);
        } else
        {
            goods_xx.setVisibility(View.GONE);
        }
    }

    @AfterViews
    void afterview()
    {

        showData();
    }

    private void showData()
    {
        plateNumber.setText(sellOrder_new.getPlateNumber());
        bz_nc_danjia.setText(sellOrder_new.getPackPrice() + "元/件");
        by_nc_danjia.setText(sellOrder_new.getCarryPrice() + "元/斤");
        by_fzrid.setText(sellOrder_new.getContractorName());
        bz_fzrid.setText(sellOrder_new.getPickName());
        jsd_zpprice.setText(sellOrder_new.getActualprice());
        actualMoney.setText(sellOrder_new.getActualMoney());
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
        jsd_cpzjs.setText(sellOrder_new.getDefectTotalWeight());
        jsd_zpjz.setText(sellOrder_new.getQualityTotalWeight());
        jsd_zongjingzhong.setText(sellOrder_new.getTotalWeight());
        jsd_zongjianshu.setText(sellOrder_new.getTotal());
        totalFee.setText(sellOrder_new.getTotalFee());


        if (sellOrder_new.getDetailSecLists().size() > 0)
        {
            ncz_look_jsd_adapter = new NCZ_Look_JSD_Adapter(NCZ_Look_JSD_Detail.this, sellOrder_new.getDetailSecLists(), sellOrder_new.getQualityNetWeight(), sellOrder_new.getDefectNetWeight());
            frame_listview_news.setAdapter(ncz_look_jsd_adapter);
            utils.setListViewHeight(frame_listview_news);
        }


        goodsname.setText(goodsnames);
        goodsname2.setText(goodsnames);
        bz_nc_danjia1.setText(sellOrder_new.getPackPrice() + "元/件");
        jsd_zongjianshu1.setText(sellOrder_new.getTotal());
        by_nc_danjia1.setText(sellOrder_new.getCarryPrice() + "元/斤");
        all_weight1.setText(sellOrder_new.getTotalWeight());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        sellOrder_new = getIntent().getParcelableExtra("bean");
        goodsnames = getIntent().getParcelableExtra("goodsname");
    }
}
