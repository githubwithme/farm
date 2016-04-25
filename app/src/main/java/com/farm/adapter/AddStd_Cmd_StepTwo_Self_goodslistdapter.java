package com.farm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.farm.R;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.Result;
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

import java.util.HashMap;
import java.util.List;

public class AddStd_Cmd_StepTwo_Self_goodslistdapter extends BaseAdapter
{
    int currentpos;
    String number;
    String small_dw;
    String large_dw;
    String gx;
    commembertab commembertab;
    goodslisttab currentgoods;
    boolean isrecovery = false;
    View currentparentview;
    TextView tv_tip_gg;
    TextView tv_dw;
    TextView tv_spec;
    TextView tv_tip_number;
    TextView tv_goodsname;
    Button btn_sure;
    EditText et_flsl;
    CustomDialog_FLSL customDialog_flsl;
    HashMap<Integer, View> lmap = new HashMap<Integer, View>();
    private List<goodslisttab> list;
    private goodslisttab goodslisttab;
    private Context context;
    ImageView currentiv_tip = null;
    String plants;

    public AddStd_Cmd_StepTwo_Self_goodslistdapter(Context context, ImageView currentiv_tip, List<goodslisttab> list)
    {
        commembertab = AppContext.getUserInfo(context);
        this.list = list;
        this.currentiv_tip = currentiv_tip;
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
                String[] goodsspc = goodslisttab.getgoodsSpec().split("/");
                String number = goodsspc[0];
                String small_dw = goodsspc[1];
                String large_dw = goodsspc[2];
                listItemView.typename.setText(goodslisttab.getgoodsName());
                listItemView.tv_gg.setText("规格：" + number + small_dw + "/" + large_dw);
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
                    String[] goodsspc = list_goodslisttab.get(i).getgoodsSpec().split("/");
                    String number = goodsspc[0];
                    String small_dw = goodsspc[1];
                    String large_dw = goodsspc[2];
                    listItemView.cb_fl.setChecked(true);
                    listItemView.ll_flsl.setVisibility(View.VISIBLE);
                    listItemView.tv_flsl.setText(list_goodslisttab.get(i).getYL() + small_dw + "/株");

//                    listItemView.tv_allnumber.setText(list_goodslisttab.get(i).getGX() + large_dw);

//                    listItemView.tv_allnumber.setText(list_goodslisttab.get(i).getGX() + "");
                 /*   if (large_dw.equals("mL")||large_dw.equals("L"))
                    {
                        currentgoods.setGX("待定");
                    }else
                    {
                        if (!et_flsl.getText().toString().equals(""))
                        {
                            acountnumber = Double.valueOf(et_flsl.getText().toString()) * Integer.valueOf(plants);//株数
//                     neednumber = acountnumber / Double.valueOf(number);
//                    currentgoods.setGX(neednumber.toString());
                            currentgoods.setGX(acountnumber.toString());
                        }

                    }*/
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
        final View dialog_layout = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.customdialog_flsl_self, null);
        customDialog_flsl = new CustomDialog_FLSL(context, R.style.MyDialog, dialog_layout);
        tv_dw = (TextView) dialog_layout.findViewById(R.id.tv_dw);
        tv_tip_number = (TextView) dialog_layout.findViewById(R.id.tv_tip_number);
        tv_tip_gg = (TextView) dialog_layout.findViewById(R.id.tv_tip_gg);
        tv_spec = (TextView) dialog_layout.findViewById(R.id.tv_spec);
        tv_dw = (TextView) dialog_layout.findViewById(R.id.tv_dw);
        tv_goodsname = (TextView) dialog_layout.findViewById(R.id.tv_goodsname);
        et_flsl = (EditText) dialog_layout.findViewById(R.id.et_flsl);
        btn_sure = (Button) dialog_layout.findViewById(R.id.btn_sure);
        String[] goodsspc = list.get(currentpos).getgoodsSpec().split("/");
        number = goodsspc[0];
        small_dw = goodsspc[1];
        large_dw = goodsspc[2];
//        tv_tip_number.setText("剩余：" + goodssum + large_dw);
        tv_tip_number.setText("剩余：" + goodssum);
//        tv_tip_gg.setText(number + small_dw + "/" + large_dw);
        tv_spec.setText(number + small_dw + "/" + large_dw);
        tv_goodsname.setText(list.get(currentpos).getgoodsName());
//        if (small_dw.equals("ml"))
        if (large_dw.equals("mL")||large_dw.equals("L"))
        {

            tv_dw.setText("倍(兑水)");
        }else
        {
            tv_dw.setText( "kg/株");
        }

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
//                if (small_dw.equals("ml"))
                if (large_dw.equals("mL")||large_dw.equals("L"))
                {
                    currentgoods.setGX("待定");
                }else
                {
                    if (!et_flsl.getText().toString().equals(""))
                    {
                        acountnumber = Double.valueOf(et_flsl.getText().toString()) * Integer.valueOf(zzs);//株数
//                     neednumber = acountnumber / Double.valueOf(number);
//                    currentgoods.setGX(neednumber.toString());
                        currentgoods.setGX(acountnumber.toString());
                    }

                }
                SqliteDb.save(context, currentgoods);

                LinearLayout ll_flsl = (LinearLayout) currentparentview.findViewById(R.id.ll_flsl);
                ll_flsl.setVisibility(View.VISIBLE);

                TextView tv_flsl = (TextView) currentparentview.findViewById(R.id.tv_flsl);
                TextView tv_allnumber = (TextView) currentparentview.findViewById(R.id.tv_allnumber);
                TextView tv_zzs = (TextView) currentparentview.findViewById(R.id.tv_zzs);

//                if (small_dw.equals("ml"))
                    if (large_dw.equals("mL")||large_dw.equals("L"))
                {
                    tv_flsl.setText(et_flsl.getText() +"陪(兑水)");
                    tv_allnumber.setText("待定");
                }else
                {
//                    tv_flsl.setText(et_flsl.getText() + small_dw + "/株");
                    tv_flsl.setText(et_flsl.getText() +"kg/株");
                    tv_allnumber.setText(neednumber + large_dw);
                }

                tv_zzs.setText(zzs+"株");
                customDialog_flsl.dismiss();

            }
        });
        customDialog_flsl.show();
    }

}
