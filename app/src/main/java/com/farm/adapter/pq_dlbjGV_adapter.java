package com.farm.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.BreakOff;
import com.farm.bean.BreakOff_New;
import com.farm.bean.Result;
import com.farm.bean.SellOrderDetail_New;
import com.farm.bean.contractTab;
import com.farm.common.utils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by user on 2016/5/1.
 */
public class pq_dlbjGV_adapter extends BaseAdapter {
    private Context context;// 运行上下文
    private List<contractTab> listItems;// 数据集合
    private LayoutInflater listContainer;
    contractTab contracttab;
    String batchtime;
    String batchcolor;
    public Button btn_add;
    public ProgressBar bp_upload;
    public pq_dlbjGV_adapter(Context context, List<contractTab> data, String batchtime, String batchcolor)
    {
        this.context = context;
        this.listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
        this.listItems = data;
        this.batchtime=batchtime;
        this.batchcolor=batchcolor;

    }

    static class ListItemView {
        public TextView name;
        public EditText num;
        public Button btn_save;
        public ProgressBar pb_upload;
    }

    @Override
    public int getCount() {
        return listItems.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    HashMap<Integer, View> lmap = new HashMap<Integer, View>();

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        contracttab = listItems.get(i);
        // 自定义视图
        ListItemView listItemView = null;
        if (lmap.get(i) == null) {
            // 获取list_item布局文件的视图
            view = listContainer.inflate(R.layout.pq_dlbj_adapter, null);
            listItemView = new ListItemView();
            // 获取控件对象
            listItemView.name = (TextView) view.findViewById(R.id.name);
            listItemView.num = (EditText) view.findViewById(R.id.num);
            listItemView.btn_save = (Button) view.findViewById(R.id.btn_save);
            listItemView.pb_upload = (ProgressBar) view.findViewById(R.id.pb_upload);

//            view.setTag(R.id.tag_upload ,contracttab);

            listItemView.btn_save.setTag(R.id.tag_contract ,contracttab);
            listItemView.btn_save.setTag(R.id.tag_view, listItemView);
            listItemView.btn_save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
            /*
                List<BreakOff_New> list_breakoff = contracttab.getBreakOffList();
            if (list_breakoff != null && list_breakoff.size() > 0) {
                        listItemView.num.setText(list_breakoff.get(0).getnumberofbreakoff());
                    }*/

                    btn_add= (Button) view;
                    ListItemView listItemView1 = (ListItemView) view.getTag(R.id.tag_view);
                    contractTab contracttab = (contractTab) view.getTag(R.id.tag_contract);
                    List<BreakOff_New> list_BreakOff_New = contracttab.getBreakOffList();

                    String number = listItemView1.num.getText().toString();
                    listItemView1.btn_save.setVisibility(View.GONE);
                    listItemView1.pb_upload.setVisibility(View.VISIBLE);
                    bp_upload=listItemView1.pb_upload;//
                    if (number.equals("")) //没填写数据
                    {
                        listItemView1.btn_save.setVisibility(View.VISIBLE);
                        listItemView1.pb_upload.setVisibility(View.GONE);
                        Toast.makeText(context, "请先填写数量", Toast.LENGTH_SHORT).show();
                    } else if (list_BreakOff_New==null || list_BreakOff_New.size()==0)//新增数据
                    {
                        String uuid=java.util.UUID.randomUUID().toString();
                        BreakOff_New breakoff=new BreakOff_New();
                        breakoff.setUuid(uuid);
                        breakoff.setuid(contracttab.getuId());
                        breakoff.setareaid(contracttab.getAreaId());
                        breakoff.setYear(utils.getYear());
                        breakoff.setparkid(contracttab.getparkId());
                        breakoff.setparkname(contracttab.getparkName());
                        breakoff.setareaname(contracttab.getareaName());
                        breakoff.setBatchColor(batchcolor);
                        breakoff.setBatchTime(batchtime);
                        breakoff.setBreakofftime(utils.getTime());
                        breakoff.setcontractid(contracttab.getid());
                        breakoff.setcontractname(contracttab.getContractNum());
                        breakoff.setLat("");
                        breakoff.setLatlngsize("");
                        breakoff.setLng("");
                        breakoff.setnumberofbreakoff(number);
                        breakoff.setregdate(utils.getTime());
                        breakoff.setStatus("0");
                        breakoff.setWeight("0");
                        breakoff.setXxzt("0");
                        list_BreakOff_New.add(breakoff);


                        SellOrderDetail_New sellorderdetail=new SellOrderDetail_New();
                        sellorderdetail.setYear(utils.getYear());
                        sellorderdetail.setXxzt("0");
                        sellorderdetail.setactuallat("");
                        sellorderdetail.setactuallatlngsize("");
                        sellorderdetail.setactuallng("");
                        sellorderdetail.setactualnote("");
                        sellorderdetail.setactualnumber("");
                        sellorderdetail.setactualprice("");
                        sellorderdetail.setactualweight("");
                        sellorderdetail.setareaid(contracttab.getAreaId());
                        sellorderdetail.setareaname(contracttab.getareaName());
                        sellorderdetail.setcontractid(contracttab.getid());
                        sellorderdetail.setcontractname(contracttab.getContractNum());
                        sellorderdetail.setBatchTime(batchtime);
                        sellorderdetail.setisSoldOut("");
                        sellorderdetail.setparkid(contracttab.getparkId());
                        sellorderdetail.setparkname(contracttab.getparkName());
                        sellorderdetail.setPlanlat("");
                        sellorderdetail.setplanlatlngsize("");
                        sellorderdetail.setplanlng("");
                        sellorderdetail.setplannote("");
                        sellorderdetail.setplanprice("");
                        sellorderdetail.setplanweight("");
                        sellorderdetail.setplannumber(number);
                        sellorderdetail.setreg(utils.getTime());
                        sellorderdetail.setreg(utils.getTime());
                        sellorderdetail.setType("salefor");
                        sellorderdetail.setXxzt("0");
                        sellorderdetail.setstatus("0");
                        sellorderdetail.setUuid(uuid);
                        sellorderdetail.setYear(utils.getYear());
                        sellorderdetail.setsaleid("");

                        StringBuilder builder = new StringBuilder();
                        builder.append("{\"breakoff\": [");
                        builder.append(JSON.toJSONString(breakoff));
                        builder.append("],\"sellorderdetail\": [");
                        builder.append(JSON.toJSONString(sellorderdetail));
                        builder.append("]");
                        builder.append("} ");
                        saveBreakOffList(builder.toString());
                    } else //编辑数据
                    {

                            updateBreakOff(list_BreakOff_New.get(0).getUuid(), number);

                    }

                }
            });

            //数据添加
//            listItemView.indata.setText(wz_yCxx.getNowDate());
//            Bundle bundle1=new Bundle();
//            bundle1.putString("status","0");
//            bundle1.putParcelable("bean", contracttab);
//            listItemView.num.setTag(bundle1);
//            for (int j = 0; j <list_breakoff.size() ; j++)
//            {
//                if (list_breakoff.get(i).getcontractid().equals(contracttab.getid()))
//                {
//                    listItemView.num.setText(list_breakoff.get(i).getnumberofbreakoff());
//                    Bundle bundle2=new Bundle();
//                    bundle2.putString("status","1");//原有数据
//                    bundle2.putParcelable("bean", list_breakoff.get(i));
//                    listItemView.num.setTag(bundle2);
//                }
//            }
            List<BreakOff_New> list_breakoff = contracttab.getBreakOffList();
//            listItemView.name.setText(contracttab.getareaName() + contracttab.getContractNum());

            listItemView.name.setText(contracttab.getContractNum());
            if (list_breakoff != null && list_breakoff.size() > 0) {
                listItemView.num.setText(list_breakoff.get(0).getnumberofbreakoff());
            }

            lmap.put(i, view);
            view.setTag(listItemView);
        } else {
            view = lmap.get(i);
            listItemView = (ListItemView) view.getTag();
        }


        return view;
    }

    private void saveBreakOffList( String data )
    {
//        listItemView.pb_upload.setTag(R.id.tag_upload ,contracttab);
//        view.setTag(R.id.tag_upload ,contracttab);
//        ProgressBar pb_upload=getTag()
/*View view= listContainer.inflate(R.layout.pq_dlbj_adapter, null);
        ListItemView listItemView = null;
        listItemView =view.getTag(R.id.tag_upload);*/
        String x=data;
        RequestParams params = new RequestParams();
        params.setContentType("application/json");
        try
        {
            params.setBodyEntity(new StringEntity(data, "utf-8"));
        } catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        params.addQueryStringParameter("action", "saveBreakOff");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {

                String a = responseInfo.result;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    if (result.getAffectedRows() != 0) {
                        btn_add.setVisibility(View.VISIBLE);
                        bp_upload.setVisibility(View.GONE);
                        Toast.makeText(context, "保存成功", Toast.LENGTH_SHORT).show();
                    }else
                    {
                        bp_upload.setVisibility(View.GONE);
                        btn_add.setVisibility(View.VISIBLE);
                        Toast.makeText(context, "保存失败", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    bp_upload.setVisibility(View.GONE);
                    btn_add.setVisibility(View.VISIBLE);
                    AppContext.makeToast(context, "error_connectDataBase");
                    Toast.makeText(context, "保存失败，请重试！", Toast.LENGTH_SHORT).show();
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg) {
                AppContext.makeToast(context, "error_connectServer");
            }
        });
    }

    private void updateBreakOff(String uuid,String number)
    {


        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uuid", uuid);
        params.addQueryStringParameter("numberofbreakoff", number);
        params.addQueryStringParameter("action", "updateBreakOff");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String a = responseInfo.result;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    if (result.getAffectedRows() != 0) {
                        bp_upload.setVisibility(View.GONE);
                        btn_add.setVisibility(View.VISIBLE);
                        Toast.makeText(context, "修改成功", Toast.LENGTH_SHORT).show();

                    }else
                    {
                        bp_upload.setVisibility(View.GONE);
                        btn_add.setVisibility(View.VISIBLE);
                        Toast.makeText(context, "修改失败", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    bp_upload.setVisibility(View.GONE);
                    btn_add.setVisibility(View.VISIBLE);
                    AppContext.makeToast(context, "error_connectDataBase");
                    Toast.makeText(context, "修改失败，请重试！", Toast.LENGTH_SHORT).show();
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg) {
                AppContext.makeToast(context, "error_connectServer");
            }
        });
    }

}
