package com.farm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.farm.R;
import com.farm.bean.breakofftab;
import com.farm.bean.ProductBatch;
import com.farm.bean.areatab;
import com.farm.bean.contractTab;
import com.farm.bean.sellOrderDetailTab;
import com.farm.common.SqliteDb;
import com.farm.common.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProductBatchGridViewAdapter extends BaseAdapter
{
    ListItemView listItemView = null;
    private LayoutInflater listContainer;// 视图容器
    private List<contractTab> list;
    private List<ProductBatch> list_ProductBatch = new ArrayList<>();
    private List<sellOrderDetailTab> list_sellOrderDetailTab = new ArrayList<>();
    private Context context;
    areatab areatab;

    public ProductBatchGridViewAdapter(Context context, List<contractTab> list, areatab areatab)
    {
        this.list = list;
        this.areatab = areatab;
        this.context = context;
        this.listContainer = LayoutInflater.from(context);
    }

    @Override
    public int getCount()
    {
        if (list != null && list.size() > 0) return list.size();
        else return 0;
    }

    @Override
    public Object getItem(int position)
    {
        return list.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }

    HashMap<Integer, View> lmap = new HashMap<Integer, View>();

    public View getView(int position, View convertView, ViewGroup parent)
    {
        // 自定义视图

        if (lmap.get(position) == null)
        {
            // 获取list_item布局文件的视图
            convertView = listContainer.inflate(R.layout.item_batchdetailgridview, null);
            listItemView = new ListItemView();
            // 获取控件对象
            listItemView.ll_contract = (LinearLayout) convertView.findViewById(R.id.ll_contract);
            listItemView.cb_productselect = (CheckBox) convertView.findViewById(R.id.cb_productselect);
            listItemView.tv_contractname = (TextView) convertView.findViewById(R.id.tv_contractname);
            listItemView.tv_numberofbreakoff = (TextView) convertView.findViewById(R.id.tv_numberofbreakoff);
            listItemView.tv_output = (TextView) convertView.findViewById(R.id.tv_output);

            listItemView.cb_productselect.setTag(position);
            listItemView.cb_productselect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                {//1记录销售订单2记录已销售批次
                    int pos = Integer.valueOf(buttonView.getTag().toString());
                    if (isChecked)
                    {
                        ProductBatch productBatch = new ProductBatch();
                        productBatch.setparkId(areatab.getparkId());
                        productBatch.setparkName(areatab.getparkName());
                        productBatch.setAreaId(areatab.getid());
                        productBatch.setareaName(areatab.getareaName());
                        productBatch.setContractId(list.get(pos).getid());
                        productBatch.setContractNum(list.get(pos).getContractNum());
                        productBatch.setNumberofproductbatch(list.get(pos).getbreakofftabList().get(0).getnumberofbreakoff());
                        productBatch.setDateofproductbatch("");
                        productBatch.setNote("");
                        productBatch.setIsSell("1");

                        sellOrderDetailTab sellOrderDetailTab = new sellOrderDetailTab();
                        sellOrderDetailTab.setuId(areatab.getuId());
                        sellOrderDetailTab.setparkId(areatab.getparkId());
                        sellOrderDetailTab.setparkName(areatab.getparkName());
                        sellOrderDetailTab.setAreaId(areatab.getid());
                        sellOrderDetailTab.setareaName(areatab.getareaName());
                        sellOrderDetailTab.setContractId(list.get(pos).getid());
                        sellOrderDetailTab.setContractNum(list.get(pos).getContractNum());
                        sellOrderDetailTab.setProductBatch("第一批次");
                        sellOrderDetailTab.setOrderId("");
                        sellOrderDetailTab.setActualsellPrice("");
                        sellOrderDetailTab.setOutput("6000");
                        sellOrderDetailTab.setSellDate(utils.getTime());
                        SqliteDb.save(context, sellOrderDetailTab);
                    } else
                    {
                        SqliteDb.deletesellOrderDetailTab(context,sellOrderDetailTab.class,"第一批次",list.get(pos).getid());
                    }
                }
            });

            // 设置控件集到convertView
            lmap.put(position, convertView);
            convertView.setTag(listItemView);
        } else
        {
            convertView = lmap.get(position);
            listItemView = (ListItemView) convertView.getTag();
        } if (list != null && list.size() > 0)
    {
        listItemView.tv_contractname.setText(list.get(position).getContractNum());
        listItemView.tv_output.setText("产量:" + list.get(position).getPlantnumber());
        List<breakofftab> list_breakoff = list.get(position).getbreakofftabList();
        if (list_breakoff != null && list_breakoff.size() > 0)
        {
            listItemView.tv_numberofbreakoff.setText("待售:" + list_breakoff.get(0).getnumberofbreakoff());
        } else
        {
            listItemView.cb_productselect.setClickable(false);
            listItemView.tv_numberofbreakoff.setText("售完");
            listItemView.tv_numberofbreakoff.setTextSize(18f);
            listItemView.tv_numberofbreakoff.setTextColor(context.getResources().getColor(R.color.red));
            listItemView.ll_contract.setBackgroundResource(R.color.line_color);
        }

    }

        return convertView;
    }

    //	@Override
//	public View getView(final int position, View convertView, ViewGroup parent)
//	{
//		if (convertView == null)
//		{
//			convertView = View.inflate(context, R.layout.item_batchdetailgridview, null);
//			view = new Holder(convertView);
//			convertView.setTag(view);
//		} else
//		{
//			view = (Holder) convertView.getTag();
//		}
//		if (list != null && list.size() > 0)
//		{
//			view.tv_contractname.setText(list.get(position).getContractNum());
//			view.tv_output.setText("产量:"+list.get(position).getPlantnumber());
//			List<breakofftab> list_breakoff=list.get(position).getbreakofftabList();
//			if (list_breakoff != null)
//			{
//				view.tv_numberofbreakoff.setText("待售:"+list_breakoff.get(0).getNumberofbreakoff());
//			}
//
//		}
//
//		return convertView;
//	}
//
//	private class Holder
//	{
//		private TextView tv_contractname;
//		private TextView tv_numberofbreakoff;
//		private TextView tv_output;
//		private CheckBox cb_productselect;
//
//		public Holder(View view)
//		{
//			cb_productselect = (CheckBox) view.findViewById(R.id.cb_productselect);
//			tv_contractname = (TextView) view.findViewById(R.id.tv_contractname);
//			tv_numberofbreakoff = (TextView) view.findViewById(R.id.tv_numberofbreakoff);
//			tv_output = (TextView) view.findViewById(R.id.tv_output);
//		}
//	}
    static class ListItemView
    {
        private TextView tv_contractname;
        private TextView tv_numberofbreakoff;
        private TextView tv_output;
        private CheckBox cb_productselect;
        private LinearLayout ll_contract;

    }
}
