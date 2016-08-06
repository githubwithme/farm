package com.farm.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.farm.R;
import com.farm.bean.DynamicBean;
import com.farm.bean.SellOrder_New;
import com.farm.common.utils;
import com.farm.ui.NCZ_CommandListActivity_;
import com.farm.ui.NCZ_DLdatail_;
import com.farm.ui.NCZ_JobActivity_;
import com.farm.ui.NCZ_MQActivity_;
import com.farm.ui.NCZ_OrderManager_;
import com.farm.ui.NCZ_SJActivity_;
import com.farm.ui.NCZ__NeedOrder_Detail_;
import com.farm.ui.Ncz_wz_ll_;
import com.farm.widget.CircleImageView;

import java.util.HashMap;
import java.util.List;

@SuppressLint("NewApi")
public class Adapter_NCZApproveSettlement extends BaseAdapter
{
    private Context context;// 运行上下文
    private List<SellOrder_New> listItems;// 数据集合
    private LayoutInflater listContainer;// 视图容器
    SellOrder_New sellOrder_new;

    static class ListItemView
    {
        public TextView tv_settlementnumber;
        public TextView tv_carnumber;
        public TextView tv_packagesnumber;
        public TextView tv_packagesnumber_good;
        public TextView tv_packagesnumber_notgood;
        public TextView tv_price_good;
        public TextView tv_price_notgood;
        public TextView tv_price_packages;
        public TextView tv_price_carry;
        public TextView tv_allvalues;
        public Button btn_showSettlementDetail;
        public Button btn_reject;
        public Button btn_Approval;
    }

    public Adapter_NCZApproveSettlement(Context context, List<SellOrder_New> data)
    {
        this.context = context;
        this.listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
        this.listItems = data;
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
        sellOrder_new = listItems.get(position);
        // 自定义视图
        ListItemView listItemView = null;
        if (lmap.get(position) == null)
        {
            // 获取list_item布局文件的视图
            convertView = listContainer.inflate(R.layout.adapter_nczapprovesettelment, null);
            listItemView = new ListItemView();
            // 获取控件对象
            listItemView.tv_settlementnumber = (TextView) convertView.findViewById(R.id.tv_settlementnumber);
            listItemView.tv_carnumber = (TextView) convertView.findViewById(R.id.tv_carnumber);
            listItemView.tv_packagesnumber = (TextView) convertView.findViewById(R.id.tv_packagesnumber);
            listItemView.tv_packagesnumber_good = (TextView) convertView.findViewById(R.id.tv_packagesnumber_good);
            listItemView.tv_packagesnumber_notgood = (TextView) convertView.findViewById(R.id.tv_packagesnumber_notgood);
            listItemView.tv_price_good = (TextView) convertView.findViewById(R.id.tv_price_good);
            listItemView.tv_price_notgood = (TextView) convertView.findViewById(R.id.tv_price_notgood);
            listItemView.tv_price_packages = (TextView) convertView.findViewById(R.id.tv_price_packages);
            listItemView.tv_price_carry = (TextView) convertView.findViewById(R.id.tv_price_carry);
            listItemView.tv_allvalues = (TextView) convertView.findViewById(R.id.tv_allvalues);
            listItemView.btn_Approval = (Button) convertView.findViewById(R.id.btn_Approval);
            listItemView.btn_reject = (Button) convertView.findViewById(R.id.btn_reject);
            listItemView.btn_showSettlementDetail = (Button) convertView.findViewById(R.id.btn_showSettlementDetail);
            // 设置控件集到convertView
            lmap.put(position, convertView);
            convertView.setTag(listItemView);

        } else
        {
            convertView = lmap.get(position);
            listItemView = (ListItemView) convertView.getTag();
        }
        listItemView.tv_settlementnumber.setText("结算单" + (position + 1));
        listItemView.tv_carnumber.setText(sellOrder_new.getPlateNumber());
        listItemView.tv_packagesnumber.setText(sellOrder_new.getTotal());
        listItemView.tv_packagesnumber_good.setText(sellOrder_new.getActualnumber());
        listItemView.tv_packagesnumber_notgood.setText(sellOrder_new.getDefectNum());
        listItemView.tv_price_good.setText(sellOrder_new.getActualprice());
        listItemView.tv_price_notgood.setText(sellOrder_new.getDefectPrice());
        listItemView.tv_price_packages.setText(sellOrder_new.getPackPrice());
        listItemView.tv_price_carry.setText(sellOrder_new.getCarryPrice());
        listItemView.tv_allvalues.setText(sellOrder_new.getTotalFee());

        listItemView.btn_Approval.setTag(sellOrder_new);
        listItemView.btn_Approval.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

            }
        });
        listItemView.btn_reject.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

            }
        });
        listItemView.btn_showSettlementDetail.setTag(sellOrder_new);
        listItemView.btn_showSettlementDetail.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                SellOrder_New sellOrder_neww = (SellOrder_New) view.getTag();
                Intent intent = new Intent(context, NCZ__NeedOrder_Detail_.class);
                intent.putExtra("bean", sellOrder_neww);
                intent.putExtra("sellOrder_new", sellOrder_new);
                context.startActivity(intent);
            }
        });
        return convertView;
    }


}