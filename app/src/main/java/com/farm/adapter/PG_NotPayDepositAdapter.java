package com.farm.adapter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.farm.R;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.Result;
import com.farm.bean.SellOrder_New;
import com.farm.bean.SellOrder_New_First;
import com.farm.bean.commembertab;
import com.farm.common.utils;
import com.farm.ui.NCZ_All_OneOrder_Detail_;
import com.farm.ui.PG_EditOrder_;
import com.farm.widget.CircleImageView;
import com.farm.widget.CustomDialog_CallTip;
import com.farm.widget.CustomDialog_ListView;
import com.farm.widget.MyDateMaD;
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
 * Created by hasee on 2016/6/29.
 */
public class PG_NotPayDepositAdapter extends BaseAdapter
{

    CustomDialog_ListView customDialog_listView;
    String zzsl;
    CustomDialog_CallTip custom_calltip;
    MyDialog myDialog;
    String broadcast;
    private Context context;// 运行上下文
    private List<SellOrder_New> listItems;// 数据集合
    private LayoutInflater listContainer;// 视图容器
    SellOrder_New sellOrder;

    static class ListItemView
    {
        //        public CircleImageView circle_img;
        public TextView tv_car;
        public TextView btn_changetime;
        public TextView tv_mainpeople;
        public View view_buyer_call;
        public View view_mainpeople_call;
        public TextView tv_parkname;
        public TextView tv_buyer;
        public TextView tv_orderstate;
        //        public TextView tv_price;
//        public TextView tv_sum;
//        public TextView tv_from;
        public TextView tv_product;
        public Button btn_cancleorder;
        public Button btn_preparework;
        public Button btn_editorder;
        public CircleImageView circleImageView;
        public  Button btn_showdetail;

    }

    public PG_NotPayDepositAdapter(Context context, List<SellOrder_New> data, String broadcast)
    {
        this.context = context;
        this.listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
        this.listItems = data;
        this.broadcast = broadcast;
    }

    public int getCount()
    {
        return listItems.size();
    }

    public Object getItem(int arg0)
    {
        return null;
    }

    public long getItemId(int arg0)
    {
        return 0;
    }

    HashMap<Integer, View> lmap = new HashMap<Integer, View>();

    public View getView(int position, View convertView, ViewGroup parent)
    {
        sellOrder = listItems.get(position);
        // 自定义视图
        ListItemView listItemView = null;
        if (lmap.get(position) == null)
        {
            // 获取list_item布局文件的视图
            convertView = listContainer.inflate(R.layout.adapter_pgnotpaydeposit, null);
            listItemView = new ListItemView();
            // 获取控件对象
            listItemView.view_buyer_call = (View) convertView.findViewById(R.id.view_buyer_call);
            listItemView.view_mainpeople_call = (View) convertView.findViewById(R.id.view_mainpeople_call);
            listItemView.tv_parkname = (TextView) convertView.findViewById(R.id.tv_parkname);
            listItemView.tv_buyer = (TextView) convertView.findViewById(R.id.tv_buyer);
            listItemView.tv_orderstate = (TextView) convertView.findViewById(R.id.tv_orderstate);
            listItemView.tv_product = (TextView) convertView.findViewById(R.id.tv_product);
            listItemView.btn_cancleorder = (Button) convertView.findViewById(R.id.btn_cancleorder);
            listItemView.btn_preparework = (Button) convertView.findViewById(R.id.btn_preparework);
            listItemView.btn_editorder = (Button) convertView.findViewById(R.id.btn_editorder);
            listItemView.btn_showdetail = (Button) convertView.findViewById(R.id.btn_showdetail);
            listItemView.tv_mainpeople = (TextView) convertView.findViewById(R.id.tv_mainpeople);
            listItemView.circleImageView = (CircleImageView) convertView.findViewById(R.id.circleImageView);
            listItemView.tv_car = (TextView) convertView.findViewById(R.id.tv_car);
            listItemView.btn_changetime = (TextView) convertView.findViewById(R.id.btn_changetime);
            // 设置控件集到convertView
            lmap.put(position, convertView);
            convertView.setTag(listItemView);
            listItemView.tv_orderstate.setText("待付定金" + sellOrder.getWaitDeposit() + "元");
            listItemView.tv_product.setText(sellOrder.getProduct());
            listItemView.tv_buyer.setText(sellOrder.getBuyersName());
            listItemView.tv_parkname.setText(sellOrder.getParkname());
            listItemView.tv_mainpeople.setText(sellOrder.getMainPeople());
            if (sellOrder.getCarNumber().equals(""))
            {
                listItemView.tv_car.setText("0辆");
            } else
            {
                listItemView.tv_car.setText(sellOrder.getCarNumber());
            }
            listItemView.btn_showdetail.setTag(R.id.tag_bean, sellOrder);
            listItemView.btn_showdetail.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    SellOrder_New sellOrder_new = (SellOrder_New) v.getTag(R.id.tag_bean);
                    commembertab commembertab = AppContext.getUserInfo(context);
                    AppContext.eventStatus(context, "8", sellOrder_new.getUuid(), commembertab.getId());
                    Intent intent = new Intent(context, NCZ_All_OneOrder_Detail_.class);
                    intent.putExtra("bean", sellOrder_new);
                    context.startActivity(intent);
                }
            });
            listItemView.btn_changetime.setTag(R.id.tag_kg, listItemView);
            listItemView.btn_changetime.setTag(R.id.tag_hg, sellOrder);
            listItemView.btn_changetime.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    SellOrder_New sellOrders = (SellOrder_New) view.getTag(R.id.tag_hg);
                    ListItemView listItemView2 = (ListItemView) view.getTag(R.id.tag_kg);
                    MyDateMaD myDatepicker = new MyDateMaD(context, sellOrders, "2");
                    myDatepicker.getDialog().show();
                }
            });
//            listItemView.ll_car.setTag(R.id.tag_contract, sellOrder);
//            listItemView.ll_car.setTag(R.id.tag_batchtime, listItemView);
//            listItemView.ll_car.setOnClickListener(new View.OnClickListener()
//            {
//                @Override
//                public void onClick(View v)
//                {
//                    SellOrder_New sellOrder_new = (SellOrder_New) v.getTag(R.id.tag_contract);
//                    JSONObject jsonObject = utils.parseJsonFile(context, "dictionary.json");
//                    JSONArray jsonArray = null;
//                    try
//                    {
//                        jsonArray = JSONArray.parseArray(jsonObject.getString("number"));
//                    } catch (Exception e)
//                    {
//
//                    }
//                    List<String> list = new ArrayList<String>();
//                    for (int i = 0; i < jsonArray.size(); i++)
//                    {
//                        list.add(jsonArray.getString(i));
//                    }
//                    showDialog_workday(list, sellOrder_new);
//                }
//            });
            listItemView.view_buyer_call.setTag(sellOrder.getBuyersPhone());
            listItemView.view_buyer_call.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    String phone = (String) v.getTag();
                    showDialog_addsaleinfo(phone);
                }
            });
            listItemView.view_mainpeople_call.setTag(sellOrder.getMainPeoplePhone());
            listItemView.view_mainpeople_call.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    String phone = (String) v.getTag();
                    showDialog_addsaleinfo(phone);
                }
            });
            listItemView.btn_cancleorder.setTag(R.id.tag_cash, sellOrder);
            listItemView.btn_cancleorder.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    SellOrder_New sellOrder_new = (SellOrder_New) v.getTag(R.id.tag_cash);
                    showDeleteTip(sellOrder_new.getUuid());
                }
            });
            listItemView.btn_editorder.setTag(R.id.tag_postion, position);
            listItemView.btn_editorder.setTag(R.id.tag_bean, sellOrder);
            listItemView.btn_editorder.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    int pos = (int) v.getTag(R.id.tag_postion);
                    SellOrder_New sellOrder = (SellOrder_New) v.getTag(R.id.tag_bean);
                    Intent intent = new Intent(context, PG_EditOrder_.class);
                    intent.putExtra("bean", sellOrder);
                    intent.putExtra("broadcast", broadcast);
                    context.startActivity(intent);
                }
            });
        } else
        {
            convertView = lmap.get(position);
            listItemView = (ListItemView) convertView.getTag();
        }
        if (listItems.get(position).getFlashStr().equals("0"))
        {
            listItemView.circleImageView.setVisibility(View.INVISIBLE);
        } else
        {
            listItemView.circleImageView.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    private void deleteSellOrderAndDetail(String uuid)
    {

        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uuid", uuid);
        params.addQueryStringParameter("action", "deleteSellOrderAndDetail");//jobGetList1
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String a = responseInfo.result;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {

                    Intent intent = new Intent();
                    intent.setAction(AppContext.UPDATEMESSAGE_FARMMANAGER);
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

    public void showDialog_addsaleinfo(final String phone)
    {
        final View dialog_layout = LayoutInflater.from(context).inflate(R.layout.customdialog_calltip, null);
        custom_calltip = new CustomDialog_CallTip(context, R.style.MyDialog, dialog_layout);
        TextView tv_tips = (TextView) dialog_layout.findViewById(R.id.tv_tips);
        tv_tips.setText(phone + "拨打这个电话吗?");
        Button btn_sure = (Button) dialog_layout.findViewById(R.id.btn_sure);
        Button btn_cancle = (Button) dialog_layout.findViewById(R.id.btn_cancle);
        btn_sure.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                custom_calltip.dismiss();
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
                {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                context.startActivity(intent);
            }
        });
        btn_cancle.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                custom_calltip.dismiss();
            }
        });
        custom_calltip.show();
    }

    private void showDeleteTip(final String uuid)
    {

        View dialog_layout = LayoutInflater.from(context).inflate(R.layout.customdialog_callback, null);
        myDialog = new MyDialog(context, R.style.MyDialog, dialog_layout, "订单", "确定删除吗?", "删除", "取消", new MyDialog.CustomDialogListener()
        {
            @Override
            public void OnClick(View v)
            {
                switch (v.getId())
                {
                    case R.id.btn_sure:
                        deleteSellOrderAndDetail(uuid);
                        break;
                    case R.id.btn_cancle:
                        myDialog.cancel();
                        break;
                }
            }
        });
        myDialog.show();
    }


    public void showDialog_workday(List<String> list, final SellOrder_New sellOrder_new)
    {
        View dialog_layout = LayoutInflater.from(context).inflate(R.layout.customdialog_listview, null);
        customDialog_listView = new CustomDialog_ListView(context, R.style.MyDialog, dialog_layout, list, list, new CustomDialog_ListView.CustomDialogListener()
        {
            @Override
            public void OnClick(Bundle bundle)
            {
                zzsl = bundle.getString("name");
                SellOrder_New_First sellOrder_new_first = new SellOrder_New_First();
                sellOrder_new.setActualweight(zzsl);
                sellOrder_new.setGoodsname(sellOrder_new.getProduct());
                sellOrder_new.setProducer(sellOrder_new.getParkname());
                StringBuilder builder = new StringBuilder();
                builder.append("{\"SellOrder_new\":[ ");
                builder.append(JSON.toJSONString(sellOrder_new));
                builder.append("], \"sellorderlistadd\": [");
                builder.append(JSON.toJSONString(sellOrder_new_first));
                builder.append("]} ");
                newaddOrder(builder.toString());
            }
        });
        customDialog_listView.show();
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
                        Toast.makeText(context, "订单修改成功！", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent();
                        intent.setAction(AppContext.UPDATEMESSAGE_FARMMANAGER);
                        context.sendBroadcast(intent);

                    }

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
}
