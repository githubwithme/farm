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

import com.farm.R;
import com.farm.app.AppContext;
import com.farm.bean.commandtab_single;
import com.farm.bean.commembertab;
import com.farm.bean.goodslisttab;
import com.farm.common.BitmapHelper;
import com.farm.common.SqliteDb;
import com.farm.widget.CustomDialog_FLSL;

import java.util.HashMap;
import java.util.List;

public class AddStd_Cmd_StepTwo_Self_goodslistdapter extends BaseAdapter
{
    commembertab commembertab;
    goodslisttab currentgoods;
    boolean isrecovery = false;
    View currentparentview;
    TextView tv_dw;
    Button btn_sure;
    EditText et_flsl;
    CustomDialog_FLSL customDialog_flsl;
    HashMap<Integer, View> lmap = new HashMap<Integer, View>();
    private List<goodslisttab> list;
    private goodslisttab goodslisttab;
    private Context context;
    ImageView currentiv_tip = null;

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
                listItemView.tv_gg.setText("规格：" + goodslisttab.getgoodsSpec());
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
                    int currentpos = (Integer) buttonView.getTag(R.id.tag_postion);
                    if (isChecked && !isrecovery)
                    {
                        if (currentiv_tip != null)
                        {
                            currentiv_tip.setVisibility(View.VISIBLE);
                        }
                        showDialog_flsl(currentpos);
                    } else
                    {
                        SqliteDb.deleteGoods(context, goodslisttab.class, currentgoods.getId());
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
                    isrecovery = true;
                    listItemView.cb_fl.setChecked(true);
                    listItemView.ll_flsl.setVisibility(View.VISIBLE);
                    listItemView.tv_flsl.setText(list_goodslisttab.get(i).getYL());
                    listItemView.tv_allnumber.setText("");
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
        CheckBox cb_fl;
        LinearLayout ll_flsl;
    }

    public void showDialog_flsl(final int pos)
    {
        final View dialog_layout = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.customdialog_flsl_self, null);
        customDialog_flsl = new CustomDialog_FLSL(context, R.style.MyDialog, dialog_layout);
        tv_dw = (TextView) dialog_layout.findViewById(R.id.tv_dw);
        et_flsl = (EditText) dialog_layout.findViewById(R.id.et_flsl);
        btn_sure = (Button) dialog_layout.findViewById(R.id.btn_sure);
        tv_dw.setText(commandtab_single.getInstance().getNongzidw());
        btn_sure.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                currentgoods.setParkId(commembertab.getparkId());
                currentgoods.setParkName(commembertab.getparkName());
                currentgoods.setAreaId(commembertab.getareaId());
                currentgoods.setAreaName(commembertab.getareaName());
                currentgoods.setYL(et_flsl.getText().toString());
                SqliteDb.save(context, currentgoods);

                LinearLayout ll_flsl = (LinearLayout) currentparentview.findViewById(R.id.ll_flsl);
                ll_flsl.setVisibility(View.VISIBLE);

                TextView tv_flsl = (TextView) currentparentview.findViewById(R.id.tv_flsl);
                TextView tv_allnumber = (TextView) currentparentview.findViewById(R.id.tv_allnumber);

                tv_flsl.setText(et_flsl.getText());
                tv_allnumber.setText("");

                customDialog_flsl.dismiss();

            }
        });
        customDialog_flsl.show();
    }

}
