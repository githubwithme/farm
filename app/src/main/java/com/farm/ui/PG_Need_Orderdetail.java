package com.farm.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.adapter.Adapter_New_SellDetail;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.Result;
import com.farm.bean.SellOrderDetail_New;
import com.farm.bean.SellOrder_New;
import com.farm.bean.commembertab;
import com.farm.common.utils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hasee on 2016/7/8.
 */
@EActivity(R.layout.pg_need_orderdetail)
public class PG_Need_Orderdetail extends Activity
{

    List<SellOrderDetail_New> list_orderdetail;
    String batchtime;
    SellOrder_New sellOrder;
    Adapter_New_SellDetail adapter_sellOrderDetail;
    @ViewById
    ListView lv;
    @ViewById
    TextView tv_cbh;

/*    @ViewById
    TextView goodsName;//产品名字
    //表头*/

    @ViewById
    TextView price;//单价
    @ViewById
    TextView saletime;//采收时间
    @ViewById
    TextView tv_planweight;//重量
    @ViewById
    TextView tv_plansumvalues;//总价
    @ViewById
    TextView tv_deposit;//定金


    @ViewById
    TextView packPec;//包装规格


    @ViewById
    TextView packPrice;//包装单价

    @ViewById
    TextView carryPrice;//搬运价格

    @AfterViews
    void afterview()
    {
        showData();
        getsellOrderDetailBySaleId();
    }

    private void showData()
    {
        if (!sellOrder.getOldsaletime().equals(""))
        {
            saletime.setText(sellOrder.getOldsaletime().substring(0,sellOrder.getSaletime().length()-8));
        }else if (sellOrder.getOldsaletime().equals("")&&!sellOrder.getSaletime().equals(""))
        {
            saletime.setText(sellOrder.getSaletime().substring(0,sellOrder.getSaletime().length()-8));
        }

        if (!sellOrder.getOldPrice().equals(""))
        {
            price.setText(sellOrder.getOldPrice());
        }else
        {
            price.setText(sellOrder.getPrice());
        }
        if (!sellOrder.getOldCarryPrice().equals(""))
        {
            carryPrice.setText(sellOrder.getOldCarryPrice());
        }else
        {
            carryPrice.setText(sellOrder.getCarryPrice());
        }
        if (!sellOrder.getOldnumber().equals(""))
        {
            tv_planweight.setText(sellOrder.getOldnumber());
        }else
        {
            tv_planweight.setText(sellOrder.getWeight());
        }
        if (!sellOrder.getOldPackPrice().equals(""))
        {
            tv_planweight.setText(sellOrder.getOldPackPrice());
        }else
        {
            packPrice.setText(sellOrder.getPackPrice());
        }



        tv_plansumvalues.setText(sellOrder.getPurchaName());
        tv_plansumvalues.setText(sellOrder.getSumvalues());
        tv_deposit.setText(sellOrder.getWaitDeposit());
//        goodsName.setText(sellOrder.getGoodsname());
        packPec.setText(sellOrder.getPackPec());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        sellOrder = getIntent().getParcelableExtra("bean");
        list_orderdetail = sellOrder.getSellOrderDetailList();
    }

    private void getsellOrderDetailBySaleId()
    {



        commembertab commembertab = AppContext.getUserInfo(PG_Need_Orderdetail.this);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("saleId", sellOrder.getUuid());
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
                        adapter_sellOrderDetail = new Adapter_New_SellDetail(PG_Need_Orderdetail.this, list_orderdetail);
                        lv.setAdapter(adapter_sellOrderDetail);
                        utils.setListViewHeight(lv);
                        if (list_orderdetail.size()>0)
                        {
                            tv_cbh.setText(list_orderdetail.get(0).getparkname()+"承包户产量");
                        }

                    } else
                    {
                        listNewData = new ArrayList<SellOrderDetail_New>();
                    }

                } else
                {
                    AppContext.makeToast(PG_Need_Orderdetail.this, "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(PG_Need_Orderdetail.this, "error_connectServer");
            }
        });
    }
}
