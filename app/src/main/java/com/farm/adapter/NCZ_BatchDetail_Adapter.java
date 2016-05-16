package com.farm.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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
import android.widget.TextView;
import android.widget.Toast;

import com.farm.R;
import com.farm.bean.SellOrderDetail_New;
import com.farm.bean.areatab;
import com.farm.bean.contractTab;
import com.farm.common.utils;
import com.farm.ui.NCZ_FarmSale_BatchDetail_;
import com.farm.widget.CustomDialog_EditSaleInInfo;
import com.farm.widget.CustomDialog_ListView;

import java.util.ArrayList;
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
        if (listData.get(groupPosition).getSellOrderDetail_NewList() == null)
        {
            return null;
        }
        return listData.get(groupPosition).getSellOrderDetail_NewList().get(childPosition);
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

        List<SellOrderDetail_New> childData = listData.get(groupPosition).getSellOrderDetail_NewList();
        final SellOrderDetail_New sellOrderDetail_new = childData.get(childPosition);

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
            convertView.setTag(listItemView);
            convertView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Intent intent = new Intent(context, NCZ_FarmSale_BatchDetail_.class);
                    intent.putExtra("bean", sellOrderDetail_new);
                    context.startActivity(intent);
                }
            });
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
        if (listData.get(groupPosition).getSellOrderDetail_NewList() == null)
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
        TextView tv_number = (TextView) convertView.findViewById(R.id.tv_number);

        int numberofsaleout = 0;
        int numberofselein = 0;
        int numberofsalefor = 0;
        int numberofnewsale = 0;
        List<SellOrderDetail_New> list = listData.get(groupPosition).getSellOrderDetail_NewList();
        for (int i = 0; i < list.size(); i++)
        {
            if (list.get(i).getType().equals("saleout"))
            {
                numberofsaleout = numberofsaleout + Integer.valueOf(list.get(i).getplannumber());
            } else if (list.get(i).getType().equals("salein"))
            {
                numberofselein = numberofselein + Integer.valueOf(list.get(i).getplannumber());
            } else if (list.get(i).getType().equals("salefor"))
            {
                numberofsalefor = numberofsalefor + Integer.valueOf(list.get(i).getplannumber());
            } else if (list.get(i).getType().equals("newsale"))
            {
                numberofnewsale = numberofnewsale + Integer.valueOf(list.get(i).getplannumber());
            }
        }
        String number = "" + "已售:" + numberofsaleout + "售中:" + numberofselein + "拟售:" + numberofnewsale + "待售:" + numberofsalefor;
        tv_areaname.setText(listData.get(groupPosition).getareaName());
//        tv_number.setText(number);
        tv_number.setText("共剩" + numberofsalefor + "株");
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
        List<SellOrderDetail_New> list;
        List<String> list_uuid_update = new ArrayList<>();
        List<SellOrderDetail_New> list_newsale = new ArrayList<>();
        List<String> list_uuid_delete = new ArrayList<>();
        EditText et_number;
        CustomDialog_EditSaleInInfo customDialog_editSaleInInfo;
        private contractTab contracttab;
        private Context context;
        Holder view;

        public GridViewAdapter_SellOrDetail_NCZ(Context context, List<SellOrderDetail_New> list)
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
                view.cb_selectall.setTag(R.id.tag_postion, position);
                view.et_number.setTag(position);
                if (view.et_number != null)
                {
                    view.et_number.setOnTouchListener(new View.OnTouchListener()
                    {
                        public boolean onTouch(View v, MotionEvent event)
                        {
                            view.et_number.setInputType(InputType.TYPE_CLASS_NUMBER); //关闭软键盘
                            return false;
                        }
                    });
                    view.et_number.setInputType(InputType.TYPE_CLASS_NUMBER);//输入类型
                    view.et_number.setFocusableInTouchMode(true);
                    view.et_number.setFocusable(true);
                    view.et_number.requestFocus();
                    view.et_number.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            int pos = (int) v.getTag();
                            Toast.makeText(context, "dd", Toast.LENGTH_SHORT).show();
                            view.et_number.setFocusableInTouchMode(true);
                            view.et_number.requestFocus();
                            InputMethodManager inputManager = (InputMethodManager) view.et_number.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputManager.showSoftInput(view.et_number, 0);
                        }
                    });
                }
                convertView.setTag(view);
            } else
            {
                view = (Holder) convertView.getTag();
                if (view.et_number != null)
                {
                    view.et_number.setOnTouchListener(new View.OnTouchListener()
                    {
                        public boolean onTouch(View v, MotionEvent event)
                        {
                            view.et_number.setInputType(InputType.TYPE_CLASS_NUMBER); //关闭软键盘
                            return false;
                        }
                    });
                    view.et_number.setInputType(InputType.TYPE_CLASS_NUMBER);//输入类型
                    view.et_number.setFocusableInTouchMode(true);
                    view.et_number.setFocusable(true);
                    view.et_number.requestFocus();
                    view.et_number.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            int pos = (int) v.getTag();
                            Toast.makeText(context, "dd", Toast.LENGTH_SHORT).show();
                            view.et_number.setFocusableInTouchMode(true);
                            view.et_number.requestFocus();
                            InputMethodManager inputManager = (InputMethodManager) view.et_number.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputManager.showSoftInput(view.et_number, 0);
                        }
                    });
                }
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
                        bundle.putParcelable("salefor", null);
                        bundle.putParcelable("newsale", null);
                        bundle.putString("uuid", list.get(pos).getUuid());
                        buttonView.setTag(R.id.tag_view, bundle);
                    } else
                    {
                        buttonView.setTag(R.id.tag_view, null);
                    }
                }
            });
            view.tv_areaname.setText(list.get(position).getcontractname());
            view.et_number.setText(list.get(position).getplannumber());
            view.tv_number.setText("剩" + list.get(position).getplannumber() + "株");
            return convertView;
        }

        private class Holder
        {
            private EditText et_number;
            private TextView tv_areaname;
            private TextView tv_number;
            private CheckBox cb_selectall;

            public Holder(View view)
            {
                tv_areaname = (TextView) view.findViewById(R.id.tv_areaname);
                et_number = (EditText) view.findViewById(R.id.et_number);
                tv_number = (TextView) view.findViewById(R.id.tv_number);
                cb_selectall = (CheckBox) view.findViewById(R.id.cb_selectall);
            }
        }

        public void showDialog_editBreakoffinfo(final SellOrderDetail_New sellorderdetail, final CheckBox cb_select)
        {
            final View dialog_layout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.customdialog_editsale, null);
            customDialog_editSaleInInfo = new CustomDialog_EditSaleInInfo(context, R.style.MyDialog, dialog_layout);
            et_number = (EditText) dialog_layout.findViewById(R.id.et_number);
            et_number.setText(sellorderdetail.getplannumber());
            Button btn_sure = (Button) dialog_layout.findViewById(R.id.btn_sure);
            Button btn_cancle = (Button) dialog_layout.findViewById(R.id.btn_cancle);
            btn_sure.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    customDialog_editSaleInInfo.dismiss();
                    int leftnumber = Integer.valueOf(sellorderdetail.getplannumber()) - Integer.valueOf(et_number.getText().toString());
                    if (leftnumber < 0)
                    {

                    } else
                    {
                        String uuid = java.util.UUID.randomUUID().toString();
                        SellOrderDetail_New sellorderdetail_newsale = new SellOrderDetail_New();
                        sellorderdetail_newsale.setuid(sellorderdetail.getuid());
                        sellorderdetail_newsale.setUuid(uuid);
                        sellorderdetail_newsale.setactuallat("");
                        sellorderdetail_newsale.setactuallatlngsize("");
                        sellorderdetail_newsale.setactuallng("");
                        sellorderdetail_newsale.setactualnote("");
                        sellorderdetail_newsale.setactualnumber("");
                        sellorderdetail_newsale.setactualprice("");
                        sellorderdetail_newsale.setactualweight("");
                        sellorderdetail_newsale.setareaid(sellorderdetail.getareaid());
                        sellorderdetail_newsale.setareaname(sellorderdetail.getareaname());
                        sellorderdetail_newsale.setBatchTime(sellorderdetail.getBatchTime());
                        sellorderdetail_newsale.setcontractid(sellorderdetail.getcontractid());
                        sellorderdetail_newsale.setcontractname(sellorderdetail.getcontractname());
                        sellorderdetail_newsale.setisSoldOut("0");
                        sellorderdetail_newsale.setparkid(sellorderdetail.getparkid());
                        sellorderdetail_newsale.setparkname(sellorderdetail.getparkname());
                        sellorderdetail_newsale.setPlanlat("");
                        sellorderdetail_newsale.setplanlng("");
                        sellorderdetail_newsale.setplanlatlngsize("");
                        sellorderdetail_newsale.setplannote("");
                        sellorderdetail_newsale.setplannumber(et_number.getText().toString());
                        sellorderdetail_newsale.setplanprice("");
                        sellorderdetail_newsale.setplanweight("");
                        sellorderdetail_newsale.setreg(utils.getTime());
                        sellorderdetail_newsale.setstatus("0");
                        sellorderdetail_newsale.setType("newsale");
                        sellorderdetail_newsale.setsaleid("");
                        sellorderdetail_newsale.setXxzt("0");
                        sellorderdetail_newsale.setYear(utils.getYear());

                        String uuid_left = java.util.UUID.randomUUID().toString();
                        SellOrderDetail_New sellorderdetail_left = new SellOrderDetail_New();
                        sellorderdetail_left.setuid(sellorderdetail.getuid());
                        sellorderdetail_left.setUuid(uuid_left);
                        sellorderdetail_left.setactuallat("");
                        sellorderdetail_left.setactuallatlngsize("");
                        sellorderdetail_left.setactuallng("");
                        sellorderdetail_left.setactualnote("");
                        sellorderdetail_left.setactualnumber("");
                        sellorderdetail_left.setactualprice("");
                        sellorderdetail_left.setactualweight("");
                        sellorderdetail_left.setareaid(sellorderdetail.getareaid());
                        sellorderdetail_left.setareaname(sellorderdetail.getareaname());
                        sellorderdetail_left.setBatchTime(sellorderdetail.getBatchTime());
                        sellorderdetail_left.setcontractid(sellorderdetail.getcontractid());
                        sellorderdetail_left.setcontractname(sellorderdetail.getcontractname());
                        sellorderdetail_left.setisSoldOut("0");
                        sellorderdetail_left.setparkid(sellorderdetail.getparkid());
                        sellorderdetail_left.setparkname(sellorderdetail.getparkname());
                        sellorderdetail_left.setPlanlat("");
                        sellorderdetail_left.setplanlng("");
                        sellorderdetail_left.setplanlatlngsize("");
                        sellorderdetail_left.setplannote("");
                        sellorderdetail_left.setplannumber(et_number.getText().toString());
                        sellorderdetail_left.setplanprice("");
                        sellorderdetail_left.setplanweight("");
                        sellorderdetail_left.setreg(utils.getTime());
                        sellorderdetail_left.setstatus("0");
                        sellorderdetail_left.setType("salefor");
                        sellorderdetail_left.setsaleid("");
                        sellorderdetail_left.setXxzt("0");
                        sellorderdetail_left.setYear(utils.getYear());

                        Bundle bundle = new Bundle();
                        bundle.putParcelable("salefor", sellorderdetail_left);
                        bundle.putParcelable("newsale", sellorderdetail_newsale);
                        bundle.putString("uuid", sellorderdetail.getUuid());
                        cb_select.setTag(bundle);
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

//    private void createNewSale(final SellOrderDetail_New sellorderdetail, String number_new)
//    {
//        commembertab commembertab = AppContext.getUserInfo(getActivity());
//        RequestParams params = new RequestParams();
//        params.addQueryStringParameter("uid", commembertab.getuId());
//        params.addQueryStringParameter("uuid", breakOff.getUuid());
//        params.addQueryStringParameter("contractid", breakOff.getcontractid());
//        params.addQueryStringParameter("batchTime", breakOff.getBatchTime());
//        params.addQueryStringParameter("year", breakOff.getYear());
//        params.addQueryStringParameter("number_new", number_new);
//        params.addQueryStringParameter("number_difference", number_difference);
//        params.addQueryStringParameter("action", "editBreakOff");
//        HttpUtils http = new HttpUtils();
//        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
//        {
//            @Override
//            public void onSuccess(ResponseInfo<String> responseInfo)
//            {
//                String a = responseInfo.result;
//                Result result = JSON.parseObject(responseInfo.result, Result.class);
//                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
//                {
//                    String rows = result.getRows().get(0).toString();
//                    if (rows.equals("1"))
//                    {
//                        Toast.makeText(getActivity(), "修改成功！", Toast.LENGTH_SHORT).show();
//                    } else if (rows.equals("0"))
//                    {
//                        Toast.makeText(getActivity(), "该批次已经在出售，不能修改了！", Toast.LENGTH_SHORT).show();
//                    }
//
//                } else
//                {
//                    Toast.makeText(getActivity(), "修改失败！", Toast.LENGTH_SHORT).show();
//                }
//
//            }
//
//            @Override
//            public void onFailure(HttpException error, String msg)
//            {
//                AppContext.makeToast(getActivity(), "error_connectServer");
//            }
//        });
//    }
    }
}
