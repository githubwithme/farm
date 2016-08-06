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
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.farm.R;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.OrderPlanBean;
import com.farm.bean.Result;
import com.farm.bean.SellOrder_New;
import com.farm.bean.SellOrder_New_First;
import com.farm.bean.commembertab;
import com.farm.common.utils;
import com.farm.ui.NCZ_All_OneOrder_Detail_;
import com.farm.ui.NCZ_EditOrder_;
import com.farm.ui.NCZ_NewOrderDetail_;
import com.farm.ui.RecoveryDetail_;
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
import com.swipelistview.SwipeLayout;

import org.apache.http.entity.StringEntity;
import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by user on 2016/4/8.
 */
public class Adapter_OrderPlan extends BaseExpandableListAdapter
{
    TextView tv_car;
    CustomDialog_CallTip custom_calltip;
    MyDialog myDialog;
    SwipeLayout swipeLayout;
    TextView currentTextView;
    CustomDialog_ListView customDialog_listView;
    ExpandableListView mainlistview;
    private Context context;// 运行上下文
    int currentChildsize = 0;
    private GoodsAdapter adapter;
    List<OrderPlanBean> listData;
    ListView list;
    String broadcast;

    public Adapter_OrderPlan(Context context, List<OrderPlanBean> listData, String broadcast, ExpandableListView mainlistview)
    {
        this.mainlistview = mainlistview;
        this.listData = listData;
        this.context = context;
        this.broadcast = broadcast;
    }

    //得到子item需要关联的数据
    @Override
    public Object getChild(int groupPosition, int childPosition)
    {
        if (listData.get(groupPosition).getSellOrderList().size() == 0)
        {
            return null;
        }
        return listData.get(groupPosition).getSellOrderList().get(childPosition);
    }

    //得到子item的ID
    @Override
    public long getChildId(int groupPosition, int childPosition)
    {
        return childPosition;
    }

    HashMap<Integer, HashMap<Integer, View>> lmap = new HashMap<Integer, HashMap<Integer, View>>();
    HashMap<Integer, View> map = new HashMap<>();
    ListItemView listItemView = null;

    static class ListItemView
    {
        public View view_buyer_call;
        public View view_cardetail;
        public View view_mainpeople_call;
        //        public LinearLayout ll_buyer;
//        public LinearLayout ll_mainpeople;
//        public LinearLayout ll_car;
        public TextView tv_buyer;
        public TextView tv_number;
        //        public TextView tv_preparestatus;
        public TextView tv_car;
        public TextView tv_orderstate;
        public TextView tv_mainpeple;
        public TextView tv_product;
        public TextView tv_parkname;
        public Button btn_cancleorder;
        //        public Button btn_preparework;
        public Button btn_orderdetail;
        public Button btn_editorder;
        public Button btn_changetime;
        public CircleImageView circleImageView;
        public TextView tv_readynumber;
        public TextView tv_notreadynumber;
        public  RelativeLayout rl_car;
//        public RelativeLayout weijiux;
    }

    //设置子item的组件
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
    {
        List<SellOrder_New> childData = listData.get(groupPosition).getSellOrderList();
        final SellOrder_New sellOrder_new = childData.get(childPosition);
        View v = null;
        if (lmap.get(groupPosition) != null)
        {
            HashMap<Integer, View> map1 = lmap.get(groupPosition);
            v = lmap.get(groupPosition).get(childPosition);
        }
        if (v == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.adapter_orderplan_child, null);
            listItemView = new ListItemView();
//            listItemView.ll_car = (LinearLayout) convertView.findViewById(R.id.ll_car);
//            listItemView.ll_mainpeople = (LinearLayout) convertView.findViewById(R.id.ll_mainpeople);
            listItemView.view_cardetail = (View) convertView.findViewById(R.id.view_cardetail);
            listItemView.view_buyer_call = (View) convertView.findViewById(R.id.view_buyer_call);
            listItemView.view_mainpeople_call = (View) convertView.findViewById(R.id.view_mainpeople_call);
//            listItemView.ll_buyer = (LinearLayout) convertView.findViewById(R.id.ll_buyer);
            listItemView.tv_number = (TextView) convertView.findViewById(R.id.tv_number);
            listItemView.tv_buyer = (TextView) convertView.findViewById(R.id.tv_buyer);
//            listItemView.tv_preparestatus = (TextView) convertView.findViewById(R.id.tv_preparestatus);
            listItemView.tv_orderstate = (TextView) convertView.findViewById(R.id.tv_orderstate);
            listItemView.tv_mainpeple = (TextView) convertView.findViewById(R.id.tv_mainpeple);
            listItemView.tv_car = (TextView) convertView.findViewById(R.id.tv_car);
            listItemView.tv_product = (TextView) convertView.findViewById(R.id.tv_product);
            listItemView.tv_parkname = (TextView) convertView.findViewById(R.id.tv_parkname);
            listItemView.btn_cancleorder = (Button) convertView.findViewById(R.id.btn_cancleorder);
//            listItemView.btn_preparework = (Button) convertView.findViewById(R.id.btn_preparework);
            listItemView.btn_orderdetail = (Button) convertView.findViewById(R.id.btn_orderdetail);
            listItemView.btn_editorder = (Button) convertView.findViewById(R.id.btn_editorder);
            listItemView.btn_changetime = (Button) convertView.findViewById(R.id.btn_changetime);
            listItemView.circleImageView = (CircleImageView) convertView.findViewById(R.id.circleImageView);

            listItemView.tv_readynumber= (TextView) convertView.findViewById(R.id.tv_readynumber);
            listItemView.tv_notreadynumber= (TextView) convertView.findViewById(R.id.tv_notreadynumber);
            listItemView.rl_car= (RelativeLayout) convertView.findViewById(R.id.rl_car);
//            listItemView.weijiux = (RelativeLayout) convertView.findViewById(R.id.weijiux);

            listItemView.tv_number.setText(String.valueOf(childPosition + 1));
            listItemView.btn_orderdetail.setTag(R.id.tag_bean, sellOrder_new);
            listItemView.btn_orderdetail.setOnClickListener(new View.OnClickListener()
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
//            listItemView.btn_cancleorder.setTag(R.id.tag_danwei,sellOrder);
            listItemView.btn_cancleorder.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    SellOrder_New sellOrder_new = (SellOrder_New) v.getTag(R.id.tag_cash);
                    showDeleteTip(sellOrder_new.getUuid());
                }
            });


            listItemView.rl_car.setTag(R.id.tag_danwei, sellOrder_new);
            listItemView.rl_car.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    SellOrder_New sellOrder_new = (SellOrder_New) v.getTag(R.id.tag_danwei);
                    Intent intent = new Intent(context, RecoveryDetail_.class);
                    intent.putExtra("uuid", sellOrder_new.getUuid());
                    context.startActivity(intent);
                }
            });
            listItemView.btn_changetime.setTag(R.id.tag_kg, listItemView);
            listItemView.btn_changetime.setTag(R.id.tag_hg, sellOrder_new);
            listItemView.btn_changetime.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    SellOrder_New sellOrders = (SellOrder_New) view.getTag(R.id.tag_hg);
                    ListItemView listItemView2 = (ListItemView) view.getTag(R.id.tag_kg);
                    MyDateMaD myDatepicker = new MyDateMaD(context, sellOrders, "1");
                    myDatepicker.getDialog().show();
                }
            });
            listItemView.btn_cancleorder.setTag(R.id.tag_cash, sellOrder_new);
            listItemView.btn_cancleorder.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    SellOrder_New sellOrder_new = (SellOrder_New) v.getTag(R.id.tag_cash);
                    showDeleteTip(sellOrder_new.getUuid());
//                    deleteSellOrderAndDetail(sellOrder_new.getUuid());
                }
            });

            listItemView.tv_readynumber.setText(sellOrder_new.getReadyPlate() + "车;");
            listItemView.tv_notreadynumber.setText(sellOrder_new.getNotReadyPlate()+"车");
            listItemView.tv_car.setText("共"+(Integer.valueOf(sellOrder_new.getReadyPlate())+Integer.valueOf(sellOrder_new.getNotReadyPlate()))+"车;");


/*
            listItemView.tv_car.setTag(R.id.tag_bean, sellOrder_new);
            listItemView.tv_car.setTag(R.id.tag_text, listItemView.tv_car);
            listItemView.tv_car.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    SellOrder_New sellOrder_new = (SellOrder_New) v.getTag(R.id.tag_bean);
                    tv_car = (TextView) v.getTag(R.id.tag_text);
                    JSONObject jsonObject = utils.parseJsonFile(context, "dictionary.json");
                    JSONArray jsonArray = JSONArray.parseArray(jsonObject.getString("number"));
                    List<String> list = new ArrayList<String>();
                    for (int i = 0; i < jsonArray.size(); i++)
                    {
                        list.add(jsonArray.getString(i));
                    }
                    showDialog_carNumber(list, sellOrder_new);
                }
            });*/
            listItemView.btn_editorder.setTag(R.id.tag_postion, childPosition);
            listItemView.btn_editorder.setTag(R.id.tag_bean, sellOrder_new);
            listItemView.btn_editorder.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    int pos = (int) v.getTag(R.id.tag_postion);
                    SellOrder_New sellOrder = (SellOrder_New) v.getTag(R.id.tag_bean);
                    Intent intent = new Intent(context, NCZ_EditOrder_.class);
                    intent.putExtra("bean", sellOrder);
                    intent.putExtra("broadcast", broadcast);
                    context.startActivity(intent);
                }
            });

            listItemView.view_buyer_call.setTag(R.id.tag_fi, sellOrder_new.getBuyersPhone());
            listItemView.view_buyer_call.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    String phone = (String) v.getTag(R.id.tag_fi);
                    showDialog_addsaleinfo(phone);
                }
            });
            listItemView.view_mainpeople_call.setTag(R.id.tag_czdl, sellOrder_new.getMainPeoplePhone());
            listItemView.view_mainpeople_call.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    String phone = (String) v.getTag(R.id.tag_czdl);
                    showDialog_addsaleinfo(phone);
                }
            });
            map.put(childPosition, convertView);
            lmap.put(groupPosition, map);
            if (isLastChild)
            {
                map = new HashMap<>();
            }

            //数据添加

/*
            if (sellOrder_new.getCarNumber().equals(""))
            {
                listItemView.tv_car.setText("共0车；");
            } else
            {
                listItemView.tv_car.setText("共"+sellOrder_new.getCarNumber() + "车；");
            }
*/

            listItemView.tv_buyer.setText(sellOrder_new.getBuyersName());
            listItemView.tv_orderstate.setText(sellOrder_new.getSelltype());
            listItemView.tv_mainpeple.setText(sellOrder_new.getMainPeople());
            if (sellOrder_new.getSelltype().equals("已付定金"))
            {
//                listItemView.tv_orderstate.setTextColor(context.getResources().getColor(R.color.gray));
//                listItemView.tv_preparestatus.setTextColor(context.getResources().getColor(R.color.red));
                if (sellOrder_new.getIsReady().equals("True"))
                {
//                    listItemView.tv_preparestatus.setText("已就绪");
//                    listItemView.weijiux.setVisibility(View.GONE);
                } else
                {

//                    listItemView.tv_preparestatus.setText("未就绪");
//                    listItemView.weijiux.setVisibility(View.INVISIBLE);
                }

            } else
            {
//                listItemView.tv_preparestatus.setVisibility(View.GONE);
            }
            listItemView.tv_product.setText(sellOrder_new.getProduct());
            listItemView.tv_parkname.setText(sellOrder_new.getParkname());
        } else
        {
            convertView = lmap.get(groupPosition).get(childPosition);
            listItemView = (ListItemView) convertView.getTag();
        }
        //数据添加  都可以数据加载，不过在上面比较好，这里是返回view
        return convertView;
    }

    @Override
    public void onGroupExpanded(int groupPosition)
    {
//        super.onGroupExpanded(groupPosition);
    }

    @Override
    public void onGroupCollapsed(int groupPosition)
    {
//        super.onGroupCollapsed(groupPosition);
    }

    //获取当前父item下的子item的个数
    @Override
    public int getChildrenCount(int groupPosition)
    {
        if (listData.get(groupPosition).getSellOrderList().size() == 0)
        {
            return 0;
        }
        return listData.get(groupPosition).getSellOrderList().size();
    }

    //获取当前父item的数据
    @Override
    public Object getGroup(int groupPosition)
    {
        return listData.get(groupPosition);
    }

    @Override
    public int getGroupCount()
    {
        return listData.size();
    }

    @Override
    public long getGroupId(int groupPosition)
    {
        return groupPosition;
    }


    HashMap<Integer, HashMap<Integer, View>> lmap_parent = new HashMap<Integer, HashMap<Integer, View>>();
    HashMap<Integer, View> map_parent = new HashMap<>();
    ListItemView listItemView_parent = null;

    //设置父item组件
    @Override
    public View getGroupView(final int groupPosition, final boolean isExpanded, View convertView, ViewGroup parent)
    {
//        View v = null;
//        if (lmap_parent.get(groupPosition) != null)
//        {
//            HashMap<Integer, View> map1 = lmap_parent.get(groupPosition);
//            v = lmap_parent.get(groupPosition).get(childPosition);
//        }
//        if (v == null)
//        {
//            map_parent.put(childPosition, convertView);
//            lmap_parent.put(groupPosition, map_parent);
//            if (isLastChild)
//            {
//                map_parent = new HashMap<>();
//            }
//        } else
//        {
//            convertView = lmap_parent.get(groupPosition).get(childPosition);
//            listItemView = (ListItemView) convertView.getTag();
//        }


        if (convertView == null)
        {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.adapter_orderplan_parent, null);
            TextView tv_ordernumber = (TextView) convertView.findViewById(R.id.tv_ordernumber);
            FrameLayout fl_year = (FrameLayout) convertView.findViewById(R.id.fl_year);
            TextView tv_year = (TextView) convertView.findViewById(R.id.tv_year);
//            GridView gv = (GridView) convertView.findViewById(R.id.gv);
//            TextView tv_datenote = (TextView) convertView.findViewById(R.id.tv_datenote);
            TextView tv_carnumber = (TextView) convertView.findViewById(R.id.tv_carnumber);
            View view_dottedline = (View) convertView.findViewById(R.id.view_dottedline);
            TextView tv_notPayDepositNumber = (TextView) convertView.findViewById(R.id.tv_notPayDepositNumber);
            TextView tv_paidDepositNumber = (TextView) convertView.findViewById(R.id.tv_paidDepositNumber);
            TextView tv_notreadyNumber = (TextView) convertView.findViewById(R.id.tv_notreadyNumber);
            TextView tv_readyNumber = (TextView) convertView.findViewById(R.id.tv_readyNumber);
            TextView tv_day = (TextView) convertView.findViewById(R.id.tv_day);
            TextView tv_month = (TextView) convertView.findViewById(R.id.tv_month);
//            TextView tv_date = (TextView) convertView.findViewById(R.id.tv_date);
            view_dottedline.setLayerType(View.LAYER_TYPE_SOFTWARE, null);//关闭硬件加速，显示虚线正常
            if (groupPosition == 0)
            {
                fl_year.setVisibility(View.VISIBLE);
                tv_year.setText(utils.getYear());
            }

            String date = listData.get(groupPosition).getDate();
            tv_month.setText(date.substring(5, 7) + "月");
            tv_day.setText(date.substring(date.lastIndexOf("-") + 1, date.length()));

            tv_notPayDepositNumber.setText(listData.get(groupPosition).getNotPayDepositNumber());
            tv_paidDepositNumber.setText(listData.get(groupPosition).getPaidDepositNumber());
            tv_notreadyNumber.setText(listData.get(groupPosition).getNotreadyNumber());


            if (listData.get(groupPosition).getOrderPlanList().size() > 0)
            {
                tv_carnumber.setText(String.valueOf((Integer.valueOf(listData.get(groupPosition).getOrderPlanList().get(0).getReadyPlate()) + Integer.valueOf(listData.get(groupPosition).getOrderPlanList().get(0).getNotReadyPlate()))));
                tv_readyNumber.setText(listData.get(groupPosition).getOrderPlanList().get(0).getReadyPlate());
                tv_notreadyNumber.setText(listData.get(groupPosition).getOrderPlanList().get(0).getNotReadyPlate());
            }


            tv_ordernumber.setText(listData.get(groupPosition).getOrderNumber());
//            Adapter_OrderPlan_Parentitem adapter_orderPlan_parentitem = new Adapter_OrderPlan_Parentitem(context, listData.get(groupPosition).getOrderPlanList());
//            gv.setAdapter(adapter_orderPlan_parentitem);
//            utils.setGridViewHeight(gv);
        }

        return convertView;
    }

    @Override
    public boolean hasStableIds()
    {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition)
    {
        return true;
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

    public void showDialog_carNumber(List<String> list, final SellOrder_New sellOrder_new)
    {
        View dialog_layout = LayoutInflater.from(context).inflate(R.layout.customdialog_listview, null);
        customDialog_listView = new CustomDialog_ListView(context, R.style.MyDialog, dialog_layout, list, list, new CustomDialog_ListView.CustomDialogListener()
        {
            @Override
            public void OnClick(Bundle bundle)
            {
                String workday = bundle.getString("name");
                SellOrder_New_First sellOrder_new_first = new SellOrder_New_First();
                sellOrder_new.setActualweight(workday);
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
//                        intent.setAction(AppContext.BROADCAST_DD_REFASH);
                        intent.setAction(AppContext.BROADCAST_UPDATEAllORDER);
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
                    intent.setAction(AppContext.BROADCAST_UPDATEAllORDER);
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
}
