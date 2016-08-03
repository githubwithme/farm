package com.farm.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.BreakOff_New;
import com.farm.bean.Result;
import com.farm.bean.SellOrderDetail_New;
import com.farm.bean.SellOrder_New;
import com.farm.bean.commembertab;
import com.farm.widget.CustomDialog_EditSaleInInfo;
import com.farm.widget.MyDialog;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by hasee on 2016/7/27.
 */
public class Charge_Settlement_Adapter extends BaseAdapter
{
    CustomDialog_EditSaleInInfo customDialog_editSaleInInfo;
    EditText et_number;
    MyDialog myDialog;
    private Context context;
    private List<SellOrderDetail_New> listItems;
    private LayoutInflater listContainer;
    SellOrderDetail_New sellOrderDetail_new;
    String zpzl;
    String cpzl;
    TextView shengyuliang;
    String isSave;


    class ListItemView
    {
        public TextView all_cbh;
        public TextView jh_zhushu;
        public TextView zhushu;
        public TextView zhengpin;
        public TextView cipin;
        public TextView other_value;

    }

    public Charge_Settlement_Adapter(Context context, List<SellOrderDetail_New> data, String zpzl, String cpzl, String isSave)
    {
        this.context = context;
        this.listContainer = LayoutInflater.from(context);
        this.listItems = data;
        this.zpzl = zpzl;
        this.cpzl = cpzl;
        this.isSave = isSave;

    }

    HashMap<Integer, View> lmap = new HashMap<Integer, View>();

    public View getView(int position, View convertView, ViewGroup parent)
    {
        sellOrderDetail_new = listItems.get(position);
        int num = position + 1;
        ListItemView listItemView = null;
        if (lmap.get(position) == null)
        {
            convertView = listContainer.inflate(R.layout.charge_sellement_item, null);
            listItemView = new ListItemView();
            listItemView.all_cbh = (TextView) convertView.findViewById(R.id.all_cbh);
            listItemView.jh_zhushu = (TextView) convertView.findViewById(R.id.jh_zhushu);
            listItemView.zhushu = (TextView) convertView.findViewById(R.id.zhushu);
            listItemView.zhengpin = (TextView) convertView.findViewById(R.id.zhengpin);
            listItemView.cipin = (TextView) convertView.findViewById(R.id.cipin);
            listItemView.other_value = (TextView) convertView.findViewById(R.id.other_value);

            lmap.put(position, convertView);
            convertView.setTag(listItemView);
        } else
        {
            convertView = lmap.get(position);
            listItemView = (ListItemView) convertView.getTag();
        }
        listItemView.zhushu.setTag(R.id.tag_cash, sellOrderDetail_new);
        listItemView.zhushu.setTag(R.id.tag_number, "1");
        listItemView.zhushu.setOnClickListener(clickListener);

        listItemView.zhengpin.setTag(R.id.tag_cash, sellOrderDetail_new);
        listItemView.zhengpin.setTag(R.id.tag_number, "2");
        listItemView.zhengpin.setOnClickListener(clickListener);

        listItemView.cipin.setTag(R.id.tag_cash, sellOrderDetail_new);
        listItemView.cipin.setTag(R.id.tag_number, "3");
        listItemView.cipin.setOnClickListener(clickListener);

        listItemView.other_value.setTag(R.id.tag_cash, sellOrderDetail_new);
        listItemView.other_value.setTag(R.id.tag_number, "4");
        listItemView.other_value.setOnClickListener(clickListener);


/*        SellOrderDetail_New sellOrderDetail_new = (SellOrderDetail_New) v.getTag(R.id.tag_cash);
        String type = (String) v.getTag(R.id.tag_number);*/


        listItemView.cipin.setText(sellOrderDetail_new.getplanprice());
        listItemView.zhengpin.setText(sellOrderDetail_new.getactualprice());
        listItemView.zhushu.setText(sellOrderDetail_new.getactualnumber());
        listItemView.other_value.setText(sellOrderDetail_new.getCutBananaFee());


        shengyuliang = (TextView) convertView.findViewById(R.id.jh_zhushu);

        listItemView.all_cbh.setText(sellOrderDetail_new.getareaname() + "\n" + sellOrderDetail_new.getcontractname());

        return convertView;
    }


    @Override
    public int getCount()
    {
        return listItems.size();
    }

    @Override
    public Object getItem(int arg0)
    {
        return null;
    }

    @Override
    public long getItemId(int arg0)
    {
        return 0;
    }

    private void ChangeSellOrderDetailSec(SellOrderDetail_New sellOrderDetail_new,String actualnumber,String actualprice, String planprice,String cutBananaFee )
    {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("id", sellOrderDetail_new.getid());
        params.addQueryStringParameter("actualnumber", actualnumber);
        params.addQueryStringParameter("actualprice",actualprice);
        params.addQueryStringParameter("planprice", planprice);
        params.addQueryStringParameter("cutBananaFee", cutBananaFee);
        params.addQueryStringParameter("action", "ChangeSellOrderDetailSec");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String a = responseInfo.result;
                List<SellOrder_New> listData = new ArrayList<SellOrder_New>();
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    Intent intent = new Intent();
                    intent.setAction(AppContext.UPDATEMESSAGE_NEW_JSD);
                    context.sendBroadcast(intent);

                } else
                {
                    AppContext.makeToast(context, "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                AppContext.makeToast(context, "error_connectServer");

            }
        });
    }


    // 测试点击的事件
    protected View.OnClickListener clickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            v.setBackgroundResource(R.drawable.linearlayout_green_round_selector);
            SellOrderDetail_New sellOrderDetail_new = (SellOrderDetail_New) v.getTag(R.id.tag_cash);
            String type = (String) v.getTag(R.id.tag_number);

            showDialog_editActualnumber(sellOrderDetail_new, type);




        }
    };


    public void showDialog_editActualnumber(final SellOrderDetail_New sellOrderDetail_new, final String type)
    {
        final View dialog_layout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.customdialog_editcontractsale, null);
        customDialog_editSaleInInfo = new CustomDialog_EditSaleInInfo(context, R.style.MyDialog, dialog_layout);
        et_number = (EditText) dialog_layout.findViewById(R.id.et_number);
        Button btn_sure = (Button) dialog_layout.findViewById(R.id.btn_sure);
        Button btn_cancle = (Button) dialog_layout.findViewById(R.id.btn_cancle);
        btn_sure.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                customDialog_editSaleInInfo.dismiss();

                if (isSave.equals("0"))
                {
                    Toast.makeText(context, "请先填写上述数据", Toast.LENGTH_SHORT).show();
                } else if (et_number.equals(""))
                {
                    Toast.makeText(context, "请先填写数量", Toast.LENGTH_SHORT).show();
                } else
                {
                      if (type.equals("1"))
                      {
                          ChangeSellOrderDetailSec(sellOrderDetail_new, et_number.getText().toString(), "", "","");
                      }else if (type.equals("2"))
                      {
                          ChangeSellOrderDetailSec(sellOrderDetail_new, "", et_number.getText().toString(), "","");
                      }
                      else if (type.equals("3"))
                      {
                          ChangeSellOrderDetailSec(sellOrderDetail_new,"","",et_number.getText().toString(),"");
                      }else if(type.equals("4"))
                      {
                          ChangeSellOrderDetailSec(sellOrderDetail_new,"","","",et_number.getText().toString());
                      }

                }
            }
        });
        //zhushu zhengpin cipin
        btn_cancle.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                customDialog_editSaleInInfo.dismiss();
            }
        });
        customDialog_editSaleInInfo.show();
    }
}
