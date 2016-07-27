package com.farm.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.farm.R;
import com.farm.bean.areatab;
import com.farm.bean.contractTab;
import com.farm.common.utils;
import com.farm.widget.CustomDialog_EditSaleInInfo;
import com.farm.widget.CustomDialog_ListView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by user on 2016/4/25.
 */
public class NCZ_BatchDetail_Adapter extends BaseExpandableListAdapter
{
    GridViewAdapter_SellOrDetail_NCZ gridViewAdapter_sellOrDetail_ncz;
    TextView currentTextView;
    CustomDialog_ListView customDialog_listView;
    private int currentItem = 0;
    ExpandableListView mainlistview;
    private Context context;// 运行上下文
    int currentChildsize = 0;
    private GoodsAdapter adapter;
    List<areatab> listData;
    ListView list;

    public NCZ_BatchDetail_Adapter(Context context, List<areatab> listData, ExpandableListView mainlistview)
    {
        this.mainlistview = mainlistview;
        this.listData = listData;
        this.context = context;
    }

    //得到子item需要关联的数据
    @Override
    public Object getChild(int groupPosition, int childPosition)
    {
        if (listData.get(groupPosition).getContractTabList() == null)
        {
            return null;
        }
        return listData.get(groupPosition).getContractTabList().get(childPosition);
    }

    static class ListItemView
    {
        public GridView gv;

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


    //设置子item的组件
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
    {

        List<contractTab> childData = listData.get(groupPosition).getContractTabList();
        final contractTab contractTab = childData.get(childPosition);

        View v = null;
        if (lmap.get(groupPosition) != null)
        {
            HashMap<Integer, View> map1 = lmap.get(groupPosition);
            v = lmap.get(groupPosition).get(childPosition);
        }
        if (v == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.ncz_batchdetail_child, null);//ncz_pc_todayjobadater-ncz_pc_todayjobadater
            listItemView = new ListItemView();

            // 获取控件对象
            listItemView.gv = (GridView) convertView.findViewById(R.id.gv);
//            convertView.setTag(listItemView);
//            convertView.setOnClickListener(new View.OnClickListener()
//            {
//                @Override
//                public void onClick(View v)
//                {
//                    Intent intent = new Intent(context, NCZ_FarmSale_BatchDetail_.class);
//                    intent.putExtra("bean", contractTab);
//                    context.startActivity(intent);
//                }
//            });
            map.put(childPosition, convertView);
            lmap.put(groupPosition, map);
            if (isLastChild)
            {
                map = new HashMap<>();
            }
            gridViewAdapter_sellOrDetail_ncz = new GridViewAdapter_SellOrDetail_NCZ(context, childData);
            listItemView.gv.setAdapter(gridViewAdapter_sellOrDetail_ncz);
            utils.setGridViewHeight(listItemView.gv);
//            listItemView.tv_batchtime.setText(batchTime.getBatchTime() + "  " + batchTime.getBatchColor() + "  " + "已售:" + numberofsaleout + "售中:" + numberofselein + "拟售:" + numberofsalefor + "待售:" + numberofnewsale);
        } else
        {
            convertView = lmap.get(groupPosition).get(childPosition);
            listItemView = (ListItemView) convertView.getTag();
        }
        return convertView;
    }


    @Override
    public void onGroupExpanded(int groupPosition)
    {
        // mExpListView 是列表实例，通过判断它的状态，关闭已经展开的。
        super.onGroupExpanded(groupPosition);

    }

    @Override
    public void onGroupCollapsed(int groupPosition)
    {
        super.onGroupCollapsed(groupPosition);

    }

    //获取当前父item下的子item的个数
    @Override
    public int getChildrenCount(int groupPosition)
    {
        if (listData.get(groupPosition).getContractTabList() == null)
        {
            return 0;
        }
        return 1;
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
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.ncz_batchdetail_parent, null);
        }
        TextView tv_areaname = (TextView) convertView.findViewById(R.id.tv_areaname);
        TextView tv_allnumber = (TextView) convertView.findViewById(R.id.tv_allnumber);
        TextView tv_number = (TextView) convertView.findViewById(R.id.tv_number);
        LinearLayout ll_saleinfo = (LinearLayout) convertView.findViewById(R.id.ll_saleinfo);
        TextView tv_saleinfo = (TextView) convertView.findViewById(R.id.tv_saleinfo);
        ProgressBar pb = (ProgressBar) convertView.findViewById(R.id.pb);
        TextView tv_pb = (TextView) convertView.findViewById(R.id.tv_pb);

        areatab areatab = listData.get(groupPosition);
        tv_areaname.setText(listData.get(groupPosition).getareaName());
        int percent = 0;
        float allnumber = Integer.valueOf(areatab.getAllsaleout()) + Integer.valueOf(areatab.getAllsalein()) + Integer.valueOf(areatab.getAllnewsale()) + Integer.valueOf(areatab.getAllsalefor());
        float salenumber = Integer.valueOf(areatab.getAllsaleout()) + Integer.valueOf(areatab.getAllsalein()) + Integer.valueOf(areatab.getAllnewsale());
        if (allnumber != 0)
        {
            percent = (int) ((salenumber / allnumber) * 100);
        }
        pb.setProgress(percent);
        tv_pb.setText("共售" + percent + "%");
        tv_allnumber.setText("总产量" + allnumber);
        tv_saleinfo.setText("已售" + areatab.getAllsaleout() + "    售中" + areatab.getAllsalein() + "    拟售" + areatab.getAllnewsale());
        if (areatab.getAllsalefor().equals("0"))
        {
            if (salenumber != 0)
            {
                tv_number.setText("已售完");
            } else
            {
                tv_number.setText("产量未上报");
            }
        } else
        {
            tv_number.setText("待售" + areatab.getAllsalefor() + "株");
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


    public class GridViewAdapter_SellOrDetail_NCZ extends BaseAdapter
    {
        List<contractTab> list;
        EditText et_number;
        CustomDialog_EditSaleInInfo customDialog_editSaleInInfo;
        private Context context;
        Holder view;

        public GridViewAdapter_SellOrDetail_NCZ(Context context, List<contractTab> list)
        {
            this.list = list;
            this.context = context;
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

        @Override
        public View getView(final int position, View convertView, ViewGroup parent)
        {
            if (convertView == null)
            {
                convertView = View.inflate(context, R.layout.gridview_sellorderdetail_ncz, null);
                view = new Holder(convertView);
                contractTab contractTab = list.get(position);
                int percent = 0;
                float allnumber = Integer.valueOf(contractTab.getAllsaleout()) + Integer.valueOf(contractTab.getAllsalein()) + Integer.valueOf(contractTab.getAllnewsale()) + Integer.valueOf(contractTab.getAllsalefor());
                float salenumber = Integer.valueOf(contractTab.getAllsaleout()) + Integer.valueOf(contractTab.getAllsalein()) + Integer.valueOf(contractTab.getAllnewsale());
                if (allnumber != 0)
                {
                    percent = (int) ((salenumber / allnumber) * 100);
                }
                view.pb.setProgress(percent);
                view.tv_pb.setText("共售" + percent + "%");
                view.cb_selectall.setTag(R.id.tag_postion, position);
                view.tv_areaname.setText(list.get(position).getContractNum());
                view.tv_saleinfo.setText("总产量" + allnumber);
                view.btn_number.setText(list.get(position).getAllsalefor());
                if (contractTab.getAllsalefor().equals("0"))
                {
                    if (salenumber != 0)
                    {
                        view.tv_number.setText("已售完");
//                        convertView.setBackgroundResource(R.color.gray);
                        view.cb_selectall.setClickable(false);
                        view.btn_number.setClickable(false);
                    } else
                    {
                        view.tv_number.setText("产量未上报");
//                        view.rl_select.setVisibility(View.GONE);
                        view.cb_selectall.setClickable(false);
                        view.btn_number.setClickable(false);
                    }
                } else
                {
                    view.tv_number.setText("待售" + list.get(position).getAllsalefor() + "株");
                    view.btn_number.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            showDialog_editBreakoffinfo(list.get(position), (Button) v);
                        }
                    });
                }


                convertView.setTag(view);
            } else
            {
                view = (Holder) convertView.getTag();
            }
            view.cb_selectall.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                {
                    if (isChecked)
                    {
                        int pos = (int) buttonView.getTag(R.id.tag_postion);
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("bean", list.get(pos));
                        buttonView.setTag(R.id.tag_view, bundle);
                    } else
                    {
                        buttonView.setTag(R.id.tag_view, null);
                    }
                }
            });

            return convertView;
        }

        private class Holder
        {
            private Button btn_number;
            private TextView tv_areaname;
            private TextView tv_saleinfo;
            private TextView tv_number;
            private CheckBox cb_selectall;
            private RelativeLayout rl_select;
            private LinearLayout ll_saleinfo;
            private ProgressBar pb;
            private TextView tv_pb;

            public Holder(View view)
            {
                tv_areaname = (TextView) view.findViewById(R.id.tv_areaname);
                btn_number = (Button) view.findViewById(R.id.btn_number);
                tv_number = (TextView) view.findViewById(R.id.tv_number);
                cb_selectall = (CheckBox) view.findViewById(R.id.cb_selectall);
                ll_saleinfo = (LinearLayout) view.findViewById(R.id.ll_saleinfo);
                tv_saleinfo = (TextView) view.findViewById(R.id.tv_saleinfo);
                pb = (ProgressBar) view.findViewById(R.id.pb);
                tv_pb = (TextView) view.findViewById(R.id.tv_pb);
                rl_select = (RelativeLayout) view.findViewById(R.id.rl_select);
            }
        }

        public void showDialog_editBreakoffinfo(final contractTab contractTab, final Button button)
        {
            final View dialog_layout = LayoutInflater.from(context).inflate(R.layout.customdialog_editcontractsale, null);
            customDialog_editSaleInInfo = new CustomDialog_EditSaleInInfo(context, R.style.MyDialog, dialog_layout);
            et_number = (EditText) dialog_layout.findViewById(R.id.et_number);
            et_number.setText(contractTab.getAllsalefor());
            Button btn_sure = (Button) dialog_layout.findViewById(R.id.btn_sure);
            Button btn_cancle = (Button) dialog_layout.findViewById(R.id.btn_cancle);
            btn_sure.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    int leftnumber = Integer.valueOf(contractTab.getAllsalefor()) - Integer.valueOf(et_number.getText().toString());
                    if (leftnumber < 0)
                    {
                        Toast.makeText(context, "剩余量不足", Toast.LENGTH_SHORT).show();
                    } else
                    {
                        customDialog_editSaleInInfo.dismiss();
                        button.setText(et_number.getText().toString());

                    }

                }
            });
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
}
