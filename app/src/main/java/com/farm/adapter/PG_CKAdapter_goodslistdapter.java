package com.farm.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.Result;
import com.farm.bean.Wz_Storehouse;
import com.farm.bean.commembertab;
import com.farm.bean.goodslisttab;
import com.farm.common.BitmapHelper;
import com.farm.common.SqliteDb;
import com.farm.widget.CustomDialog_FLSL;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by user on 2016/5/18.
 */
public class PG_CKAdapter_goodslistdapter extends BaseAdapter
{
    String batchNumber;
    String batchName;
    List<Wz_Storehouse> listpeople = new ArrayList<Wz_Storehouse>();
    PG_CKlistAdapter pg_cKlistAdapter;
    PopupWindow pw_tab;
    View pv_tab;
    int currentpos;

    String large_dw;
    String iiduishui;
    String gx;
    com.farm.bean.commembertab commembertab;
    com.farm.bean.goodslisttab currentgoods;
    boolean isrecovery = false;
    View currentparentview;
    TextView tv_tip_gg;
    TextView tv_dw;
    TextView tv_spec;
    TextView tv_tip_number;
    TextView tv_goodsname;
    TextView storehouse;
    Button btn_sure;
    View tv_line;

    EditText et_flsl;
    CustomDialog_FLSL customDialog_flsl;
    HashMap<Integer, View> lmap = new HashMap<Integer, View>();
    private List<goodslisttab> list;
    private goodslisttab goodslisttab;
    private Context context;
    ImageView currentiv_tip = null;
    String plants;
    String x;
    String y;
    String id;

    public PG_CKAdapter_goodslistdapter(Context context, ImageView currentiv_tip, List<goodslisttab> list,String x,String y,String id)
    {
        commembertab = AppContext.getUserInfo(context);
        this.list = list;
        this.currentiv_tip = currentiv_tip;
        this.context = context;
        this.x=x;
        this.y=y;
        this.id=id;

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
        ListItemView listItemView = null;
        if (lmap.get(position) == null)
        {
            convertView = View.inflate(context, R.layout.item_gridview_self, null);
            listItemView = new ListItemView();
            listItemView.typeicon = (ImageView) convertView.findViewById(R.id.typeicon);
            listItemView.typename = (TextView) convertView.findViewById(R.id.typename);
            listItemView.tv_gg = (TextView) convertView.findViewById(R.id.tv_gg);
            listItemView.tv_allnumber = (TextView) convertView.findViewById(R.id.tv_allnumber);
            listItemView.tv_zzs = (TextView) convertView.findViewById(R.id.tv_zzs);
            listItemView.tv_flsl = (TextView) convertView.findViewById(R.id.tv_flsl);
            listItemView.ll_flsl = (LinearLayout) convertView.findViewById(R.id.ll_flsl);
            listItemView.cb_fl = (CheckBox) convertView.findViewById(R.id.cb_fl);
            if (list != null && list.size() > 0)
            {
                goodslisttab = list.get(position);
                if (!goodslisttab.getImgurl().equals(""))
                {
                    BitmapHelper.setImageViewBackground(context, listItemView.typeicon, goodslisttab.getImgurl());
                }

                listItemView.typename.setText(goodslisttab.getgoodsName());
                if(!goodslisttab.getThree().equals(""))
                {
                    listItemView.tv_gg.setText("规格：" +goodslisttab.getThree() + goodslisttab.getgoodsSpec());
                }else if(goodslisttab.getThree().equals("")&&!goodslisttab.getSec().equals(""))
                {
                    listItemView.tv_gg.setText("规格：" +goodslisttab.getSec() + goodslisttab.getgoodsSpec());
                }else {
                    listItemView.tv_gg.setText("规格：" + goodslisttab.getFirs() + goodslisttab.getgoodsSpec());
                }
            }
            listItemView.cb_fl.setTag(R.id.tag_bean, goodslisttab);
            listItemView.cb_fl.setTag(R.id.tag_postion, position);
            listItemView.cb_fl.setTag(R.id.tag_parentview, convertView);
            listItemView.cb_fl.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                {
                    currentparentview = (View) buttonView.getTag(R.id.tag_parentview);
                    currentgoods = (goodslisttab) buttonView.getTag(R.id.tag_bean);
                    currentpos = (Integer) buttonView.getTag(R.id.tag_postion);
                    if (isChecked && !isrecovery)
                    {
                        if (currentiv_tip != null)
                        {
                            currentiv_tip.setVisibility(View.VISIBLE);
                        }
                        getGoodsSum(list.get(currentpos));
                    } else
                    {
                        if (!isrecovery)
                        {

                            SqliteDb.deleteGoods(context, goodslisttab.class, currentgoods.getId());
                        }
                        currentiv_tip.setVisibility(View.GONE);
                        LinearLayout ll_flsl = (LinearLayout) currentparentview.findViewById(R.id.ll_flsl);
                        ll_flsl.setVisibility(View.GONE);
                        List<goodslisttab> list_goodslisttab = SqliteDb.getSelectCmdArea(context, goodslisttab.class);
                        for (int i = 0; i < list_goodslisttab.size(); i++)
                        {
                            if (list_goodslisttab.get(i).getId().equals(goodslisttab.getId()))
                            {
                                if (currentiv_tip != null)
                                {
                                    currentiv_tip.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    }
                }
            });
            List<goodslisttab> list_goodslisttab = SqliteDb.getSelectCmdArea(context, goodslisttab.class);
            for (int i = 0; i < list_goodslisttab.size(); i++)
            {
                if (list_goodslisttab.get(i).getId().equals(goodslisttab.getId()))
                {
                    Double acountnumber=0d;
                    isrecovery = true;



                    listItemView.cb_fl.setChecked(true);
                    listItemView.ll_flsl.setVisibility(View.VISIBLE);
                    listItemView.tv_flsl.setText(list_goodslisttab.get(i).getYL() + list_goodslisttab.get(i).getDW());

                    isrecovery = false;
                    if (currentiv_tip != null)
                    {
                        currentiv_tip.setVisibility(View.VISIBLE);
                    }

                }
            }
            lmap.put(position, convertView);
            convertView.setTag(listItemView);
        } else
        {
            convertView = lmap.get(position);
            listItemView = (ListItemView) convertView.getTag();
        }
        return convertView;
    }


    static class ListItemView
    {

        ImageView typeicon;
        TextView typename;
        TextView tv_gg;
        TextView tv_flsl;
        TextView tv_allnumber;
        TextView tv_zzs;
        CheckBox cb_fl;
        LinearLayout ll_flsl;
        LinearLayout tv_spec;
    }

    public void getGoodsSum(goodslisttab goodslisttab)
    {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("goodsId", goodslisttab.getId());
//        params.addQueryStringParameter("parkId", commembertab.getparkId());
        params.addQueryStringParameter("parkId", commembertab.getparkId());
        params.addQueryStringParameter("areaId", commembertab.getareaId());
//        params.addQueryStringParameter("action", "getGoodsSum");
        params.addQueryStringParameter("action", "getGoodsSumAndPlants");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String a = responseInfo.result;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)
                {
                    if (result.getAffectedRows() != 0)
                    {
                        String parkId = result.getRows().getJSONObject(0).getString("parkId");
                        String parkName = result.getRows().getJSONObject(0).getString("parkName");
                        String areaPlants = result.getRows().getJSONObject(0).getString("areaPlants");
                        String jsonarray = result.getRows().getJSONObject(0).getString("goodsSum");
                        plants=areaPlants;
//                        JSONArray jsonarray = result.getRows().getJSONObject(0).getJSONArray("goodsSum");
                        showDialog_flsl(areaPlants,jsonarray.toString());

                    } else
                    {
//                        lsitNewData = new ArrayList<Dictionary>();
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

    public void showDialog_flsl(final String zzs,String goodssum)
    {
        final View dialog_layout = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.customdialog_pg_ck, null);
        customDialog_flsl = new CustomDialog_FLSL(context, R.style.MyDialog, dialog_layout);
        tv_dw = (TextView) dialog_layout.findViewById(R.id.tv_dw);
        tv_tip_number = (TextView) dialog_layout.findViewById(R.id.tv_tip_number);
        tv_tip_gg = (TextView) dialog_layout.findViewById(R.id.tv_tip_gg);
        tv_spec = (TextView) dialog_layout.findViewById(R.id.tv_spec);
        tv_dw = (TextView) dialog_layout.findViewById(R.id.tv_dw);
        tv_goodsname = (TextView) dialog_layout.findViewById(R.id.tv_goodsname);
        et_flsl = (EditText) dialog_layout.findViewById(R.id.et_flsl);
        btn_sure = (Button) dialog_layout.findViewById(R.id.btn_sure);
        storehouse = (TextView) dialog_layout.findViewById(R.id.storehouse);
        tv_line= (View) dialog_layout.findViewById(R.id.tv_line);


//        large_dw= list.get(currentpos).getgoodsSpec().substring(1, list.get(currentpos).getgoodsSpec().length());

        tv_tip_number.setText("剩余：" + goodssum);


//        tv_spec.setText(list.get(currentpos).getFirs()+ "/" + list.get(currentpos).getgoodsunit());
        tv_goodsname.setText(list.get(currentpos).getgoodsName());
        if(!list.get(currentpos).getThree().equals(""))
        {
            tv_spec.setText(list.get(currentpos).getThree() + list.get(currentpos).getgoodsunit());
        }else if(list.get(currentpos).getThree().equals("")&&!list.get(currentpos).getSec().equals(""))
        {
            tv_spec.setText( list.get(currentpos).getSec() + list.get(currentpos).getgoodsunit());
        }else {
            tv_spec.setText( list.get(currentpos).getFirs() + list.get(currentpos).getgoodsunit());
        }



        //原来的
        if (!list.get(currentpos).getThree().equals(""))
        {

            tv_dw.setText(list.get(currentpos).getThree());

        }else if(!list.get(currentpos).getSec().equals("") &&list.get(currentpos).getThree().equals(""))
        {
            tv_dw.setText(list.get(currentpos).getSec());
        }else
        {
            tv_dw.setText(list.get(currentpos).getFirs());
        }
        getlistdata();
        storehouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (listpeople.size()>0) {
                    showPop_title();
                }
            }
        });

        btn_sure.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Double acountnumber=0d;
                Double neednumber = 0d;
                currentgoods.setParkId(commembertab.getparkId());
                currentgoods.setParkName(commembertab.getparkName());
                currentgoods.setAreaId(commembertab.getareaId());
                currentgoods.setAreaName(commembertab.getareaName());
                currentgoods.setYL(et_flsl.getText().toString());
                currentgoods.setDW(tv_dw.getText().toString());
                currentgoods.setGX(x);
                currentgoods.setZS(y);
                currentgoods.setgoodsNote(batchNumber);

                Intent intent = new Intent();
                intent.setAction(AppContext.BROADCAST_PG_DATA);
               context.sendBroadcast(intent);

                SqliteDb.save(context, currentgoods);

                LinearLayout ll_flsl = (LinearLayout) currentparentview.findViewById(R.id.ll_flsl);
                ll_flsl.setVisibility(View.VISIBLE);

                TextView tv_flsl = (TextView) currentparentview.findViewById(R.id.tv_flsl);
                TextView tv_allnumber = (TextView) currentparentview.findViewById(R.id.tv_allnumber);
                TextView tv_zzs = (TextView) currentparentview.findViewById(R.id.tv_zzs);


                tv_flsl.setText(et_flsl.getText()+tv_dw.getText().toString());
                customDialog_flsl.dismiss();

            }
        });
        customDialog_flsl.show();
    }


    public void showPop_title() {//LAYOUT_INFLATER_SERVICE
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        pv_tab = layoutInflater.inflate(R.layout.popup_yq, null);// 外层
        pv_tab.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((keyCode == KeyEvent.KEYCODE_MENU) && (pw_tab.isShowing())) {
                    pw_tab.dismiss();
                    return true;
                }
                return false;
            }
        });
        pv_tab.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (pw_tab.isShowing()) {
                    pw_tab.dismiss();
                }
                return false;
            }
        });
        pw_tab = new PopupWindow(pv_tab, LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        pw_tab.showAsDropDown(tv_line, 0, 0);
        pw_tab.setOutsideTouchable(true);


        ListView listview = (ListView) pv_tab.findViewById(R.id.lv_yq);
        pg_cKlistAdapter = new PG_CKlistAdapter(context, listpeople);
        listview.setAdapter(pg_cKlistAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View v, int postion, long arg3) {
                batchNumber = listpeople.get(postion).getBatchNumber();
                batchName = listpeople.get(postion).getBatchName();
                currentgoods.setgoodsNote(listpeople.get(postion).getBatchNumber());
                storehouse.setText(listpeople.get(postion).getBatchName()+"-"+listpeople.get(postion).getQuantity());
                pw_tab.dismiss();


            }
        });
    }
//
    private void getlistdata() {
        commembertab commembertab = AppContext.getUserInfo(context);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", commembertab.getuId());
        params.addQueryStringParameter("goodsId",goodslisttab.getId());
        params.addQueryStringParameter("storehouseId",id);
        params.addQueryStringParameter("action", "getPCSLByWzIdCKId");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AppConfig.testurl, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String a = responseInfo.result;
                List<Wz_Storehouse> listNewData = null;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    if (result.getAffectedRows() != 0) {
                        listNewData = JSON.parseArray(result.getRows().toJSONString(), Wz_Storehouse.class);
                        batchNumber = listNewData.get(0).getBatchNumber();
                        batchName = listNewData.get(0).getBatchName();
                        storehouse.setText(listNewData.get(0).getBatchName()+"-"+listNewData.get(0).getQuantity());
                        listpeople.addAll(listNewData);

                    } else {
                        listNewData = new ArrayList<Wz_Storehouse>();
                    }
                } else {
                    AppContext.makeToast(context, "error_connectDataBase");

                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg) {
                String a = error.getMessage();
                AppContext.makeToast(context, "error_connectServer");

            }
        });

    }
}
