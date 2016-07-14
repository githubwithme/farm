package com.farm.adapter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.farm.R;
import com.farm.bean.OrderPlan;
import com.farm.bean.OrderPlanBean;
import com.farm.ui.RecoveryDetail_;
import com.farm.widget.CustomDialog_CallTip;
import com.farm.widget.CustomDialog_ListView;
import com.farm.widget.MyDialog;
import com.swipelistview.SwipeLayout;

import java.util.HashMap;
import java.util.List;

/**
 * Created by user on 2016/4/8.
 */
public class Adapter_OrderPlan extends BaseExpandableListAdapter
{
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

    public Adapter_OrderPlan(Context context, List<OrderPlanBean> listData, ExpandableListView mainlistview)
    {
        this.mainlistview = mainlistview;
        this.listData = listData;
        this.context = context;
    }

    //得到子item需要关联的数据
    @Override
    public Object getChild(int groupPosition, int childPosition)
    {
        if (listData.get(groupPosition).getOrderList().size()==0)
        {
            return null;
        }
        return listData.get(groupPosition).getOrderList().get(childPosition);
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
        public LinearLayout ll_buyer;
        public TextView tv_buyer;
        public TextView tv_preparestatus;
        public TextView tv_orderstate;
        public TextView tv_product;
        public TextView tv_parkname;
        public Button btn_cancleorder;
        public Button btn_preparework;
        public Button btn_editorder;
        public Button btn_changetime;
    }

    //设置子item的组件
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
    {
        List<OrderPlan> childData = listData.get(groupPosition).getOrderList();
        final OrderPlan orderPlan = childData.get(childPosition);
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
            listItemView.ll_buyer = (LinearLayout) convertView.findViewById(R.id.ll_buyer);
            listItemView.tv_buyer = (TextView) convertView.findViewById(R.id.tv_buyer);
            listItemView.tv_preparestatus = (TextView) convertView.findViewById(R.id.tv_preparestatus);
            listItemView.tv_orderstate = (TextView) convertView.findViewById(R.id.tv_orderstate);
            listItemView.tv_product = (TextView) convertView.findViewById(R.id.tv_product);
            listItemView.tv_parkname = (TextView) convertView.findViewById(R.id.tv_parkname);
            listItemView.btn_cancleorder = (Button) convertView.findViewById(R.id.btn_cancleorder);
            listItemView.btn_preparework = (Button) convertView.findViewById(R.id.btn_preparework);
            listItemView.btn_editorder = (Button) convertView.findViewById(R.id.btn_editorder);
            listItemView.btn_changetime = (Button) convertView.findViewById(R.id.btn_changetime);
            convertView.setTag(listItemView);
//            listItemView.btn_cancleorder.setTag(R.id.tag_danwei,sellOrder);
            listItemView.btn_cancleorder.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
//                    SellOrder_New sellOrder_new = (SellOrder_New) v.getTag(R.id.tag_cash);
//                    showDeleteTip(sellOrder_new.getUuid());
                }
            });
//            listItemView.btn_preparework.setTag(R.id.tag_danwei,sellOrder);
            listItemView.btn_preparework.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
//                    SellOrder_New sellOrder_new = (SellOrder_New) v.getTag(R.id.tag_danwei);
                    Intent intent = new Intent(context, RecoveryDetail_.class);
//                    intent.putExtra("zbstudio", sellOrder_new);
                    context.startActivity(intent);
                }
            });
//            listItemView.btn_changetime.setTag(R.id.tag_kg, listItemView);
//            listItemView.btn_changetime.setTag(R.id.tag_hg, sellOrder);
//            listItemView.btn_changetime.setOnClickListener(new View.OnClickListener()
//            {
//                @Override
//                public void onClick(View view)
//                {
//                    SellOrder_New sellOrders = (SellOrder_New) view.getTag(R.id.tag_hg);
//                    ListItemView listItemView2 = (ListItemView) view.getTag(R.id.tag_kg);
//                    MyDateMaD myDatepicker = new MyDateMaD(context, listItemView2.tv_name, sellOrders, "1");
//                    myDatepicker.getDialog().show();
//                }
//            });
            convertView.setTag(R.id.tag_fi, listData.get(groupPosition).getDate());
            convertView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
//                    Intent intent = new Intent(context, ShowUserInfo_.class);
//                    intent.putExtra("userid", orderPlan.getid());// 因为list中添加了头部,因此要去掉一个
//                    intent.putExtra("type",type);// 因为list中添加了头部,因此要去掉一个
//                    context.startActivity(intent);
                }
            });
            listItemView.ll_buyer.setTag(R.id.tag_fi, listData.get(groupPosition).getDate());
            listItemView.ll_buyer.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    showDialog_addsaleinfo("15989154871");
                }
            });
            map.put(childPosition, convertView);
            lmap.put(groupPosition, map);
            if (isLastChild)
            {
                map = new HashMap<>();
            }

            //数据添加
            listItemView.tv_buyer.setText(orderPlan.getBuyer());
            listItemView.tv_orderstate.setText(orderPlan.getOrderStatus());
            if (orderPlan.getOrderStatus().equals("已付定金"))
            {
                listItemView.tv_orderstate.setTextColor(context.getResources().getColor(R.color.gray));
                listItemView.tv_preparestatus.setTextColor(context.getResources().getColor(R.color.red));
                listItemView.tv_preparestatus.setText(orderPlan.getPrepareStatus());
            }else
            {
                listItemView.tv_preparestatus.setVisibility(View.GONE);
            }
            listItemView.tv_product.setText(orderPlan.getProduct());
            listItemView.tv_parkname.setText(orderPlan.getParkname());
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
        if (listData.get(groupPosition).getOrderList() == null)
        {
            return 0;
        }
        return listData.get(groupPosition).getOrderList().size();
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

    //设置父item组件
    @Override
    public View getGroupView(final int groupPosition, final boolean isExpanded, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.adapter_orderplan_parent, null);
        }
        TextView tv_ordernumber = (TextView) convertView.findViewById(R.id.tv_ordernumber);
        TextView tv_carnumber = (TextView) convertView.findViewById(R.id.tv_carnumber);
        TextView tv_date = (TextView) convertView.findViewById(R.id.tv_date);

        tv_date.setText(listData.get(groupPosition).getDate());
        tv_carnumber.setText(listData.get(groupPosition).getCarNumber()+"车");
        tv_ordernumber.setText(listData.get(groupPosition).getOrderNumber()+"单");
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
        final View dialog_layout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.customdialog_calltip, null);
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

        View dialog_layout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.customdialog_callback, null);
        myDialog = new MyDialog(context, R.style.MyDialog, dialog_layout, "订单", "确定删除吗?", "删除", "取消", new MyDialog.CustomDialogListener()
        {
            @Override
            public void OnClick(View v)
            {
                switch (v.getId())
                {
                    case R.id.btn_sure:
//                        deleteSellOrderAndDetail(uuid);
                        break;
                    case R.id.btn_cancle:
                        myDialog.cancel();
                        break;
                }
            }
        });
        myDialog.show();
    }
}
