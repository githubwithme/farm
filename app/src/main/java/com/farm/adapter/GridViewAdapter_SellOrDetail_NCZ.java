package com.farm.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.farm.R;
import com.farm.bean.SellOrderDetail_New;
import com.farm.bean.contractTab;
import com.farm.common.utils;
import com.farm.widget.CustomDialog_EditSaleInInfo;

import java.util.ArrayList;
import java.util.List;

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
            view.cb_select.setTag(position);
            convertView.setTag(view);
        } else
        {
            view = (Holder) convertView.getTag();
        }
        view.cb_select.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if (isChecked)
                {
                    int pos = (int) buttonView.getTag();
                    showDialog_editBreakoffinfo(list.get(pos), (CheckBox) buttonView);
                } else
                {
                    buttonView.setTag(null);
                }
            }
        });
        view.cb_selectall.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if (isChecked)
                {
                    int pos = (int) buttonView.getTag();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("salefor", null);
                    bundle.putParcelable("newsale", null);
                    bundle.putString("uuid", list.get(pos).getUuid());
                    buttonView.setTag(bundle);
                } else
                {
                    buttonView.setTag(null);
                }
            }
        });
        view.tv_name.setText(list.get(position).getplannumber());

        return convertView;
    }

    private class Holder
    {
        private TextView tv_number;
        private TextView tv_name;
        private CheckBox cb_select;
        private CheckBox cb_selectall;

        public Holder(View view)
        {
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            tv_number = (TextView) view.findViewById(R.id.tv_number);
            cb_select = (CheckBox) view.findViewById(R.id.cb_select);
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
