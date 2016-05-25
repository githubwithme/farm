package com.farm.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.farm.R;
import com.farm.bean.goodslisttab;

import java.util.HashMap;
import java.util.List;

public class InputGoodsAdapter extends BaseAdapter
{
    List<goodslisttab> list;
    private Context context;
    String parkid = "";
    String parkName = "";
    String areaid = "";
    String areaName = "";
    private LayoutInflater listContainer;// 视图容器

    public InputGoodsAdapter(Context context, List<goodslisttab> list, String parkid, String parkName, String areaid, String areaName)
    {
        this.listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
        this.context = context;
        this.list = list;
        this.parkid = parkid;
        this.parkName = parkName;
        this.areaid = areaid;
        this.areaName = areaName;
    }

    public int getCount()
    {
        return list.size();
    }

    public Object getItem(int position)
    {
        return list.get(position);
    }

    public long getItemId(int position)
    {
        return position;
    }

    HashMap<Integer, View> lmap = new HashMap<Integer, View>();

    public View getView(int arg0, View convertView, ViewGroup viewGroup)
    {
        ListItemView listItemView = null;
        if (lmap.get(arg0) == null)
        {
            convertView = listContainer.inflate(R.layout.inputgoodsnumberadapter, null);
            listItemView = new ListItemView();
            listItemView.tv_fl = (TextView) convertView.findViewById(R.id.tv_fl);
            listItemView.et_number = (TextView) convertView.findViewById(R.id.et_number);
            listItemView.tv_dw = (TextView) convertView.findViewById(R.id.tv_dw);
            listItemView.tv_syl = (TextView) convertView.findViewById(R.id.tv_syl);
            listItemView.tv_spec = (TextView) convertView.findViewById(R.id.tv_spec);

            listItemView.rg_select = (RadioGroup) convertView.findViewById(R.id.rg_select);
            listItemView.dw_duishui = (RadioButton) convertView.findViewById(R.id.dw_duishui);
            listItemView.dw_gzhu = (RadioButton) convertView.findViewById(R.id.dw_gzhu);



            Bundle bundle = new Bundle();
            bundle.putString("pi", parkid);
            bundle.putString("pn", parkName);
            bundle.putString("ai", areaid);
            bundle.putString("an", areaName);
            listItemView.tv_fl.setTag(bundle);
            listItemView.tv_fl.setText(list.get(arg0).getgoodsName());

            listItemView.dw_duishui.setTag(R.id.tag_upload, listItemView);
            listItemView.dw_duishui.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ListItemView listItemView1 = (ListItemView) view.getTag(R.id.tag_upload);
                    String number = listItemView1.dw_duishui.getText().toString();
                    listItemView1.tv_dw.setText(number);
                }
            });

            listItemView.dw_gzhu.setTag(R.id.tag_danxuan, listItemView);
            listItemView.dw_gzhu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ListItemView listItemView1 = (ListItemView) view.getTag(R.id.tag_danxuan);
                    String number = listItemView1.dw_gzhu.getText().toString();
                    listItemView1.tv_dw.setText(number);
                }
            });
      /*      //方法三
            listItemView.dw_duishui.setTag(R.id.tag_upload, listItemView);
            listItemView.rg_select.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                     ListItemView listItemView1 = (ListItemView) radioGroup.getTag(R.id.tag_upload);
                    if(listItemView1.dw_duishui.getId()==checkedId) {
                        listItemView1.tv_dw.setText(listItemView1.dw_duishui.getText().toString());
                    }
                    if(listItemView1.dw_gzhu.getId()==checkedId) {
                        listItemView1.tv_dw.setText(listItemView1.dw_gzhu.getText().toString());
                    }
                }
            });*/




            //原来的
            if ( list.get(arg0).getIsExchange().equals("兑水"))
            {
                listItemView.dw_duishui.setChecked(true);
//                listItemView.dw_gzhu.setClickable(false);
                listItemView.dw_gzhu.setVisibility(View.GONE);
                listItemView.tv_dw.setText("倍(兑水)");
            }else if( list.get(arg0).getIsExchange().equals("不兑水"))
            {
                listItemView.dw_gzhu.setChecked(true);
//                listItemView.dw_duishui.setClickable(false);
                listItemView.dw_duishui.setVisibility(View.GONE);
                listItemView.tv_dw.setText("g/株");
            }else
            {
                listItemView.dw_duishui.setChecked(true);
                listItemView.tv_dw.setText("倍(兑水)");
            }
//            listItemView.tv_syl.setText( list.get(arg0).getGoodsSum()+large_dw);
            listItemView.tv_syl.setText(list.get(arg0).getGoodsSum());

            if (!list.get(arg0).getThree().equals(""))
            {
//                listItemView.tv_spec.setText(  list.get(arg0).getThree()+list.get(arg0).getgoodsSpec());
                listItemView.tv_spec.setText(list.get(arg0).getGoodsStatistical()+list.get(arg0).getgoodsunit()+"/"+  list.get(arg0).getThree());
            }else if (list.get(arg0).getThree().equals("")&&!list.get(arg0).getSec().equals("")){
                listItemView.tv_spec.setText(list.get(arg0).getGoodsStatistical()+list.get(arg0).getgoodsunit()+"/"+  list.get(arg0).getSec());
            }else
            {
                listItemView.tv_spec.setText(list.get(arg0).getGoodsStatistical()+list.get(arg0).getgoodsunit()+"/"+  list.get(arg0).getFirs());
            }


            lmap.put(arg0, convertView);
            convertView.setTag(listItemView);
        } else
        {
            convertView = lmap.get(arg0);
            listItemView = (ListItemView) convertView.getTag();
        }
        return convertView;
    }


    static class ListItemView
    { // 自定义控件集合
        TextView tv_fl;
        TextView et_number;
        TextView tv_dw;
        TextView tv_syl;
        TextView tv_spec;
        RadioGroup rg_select;
        RadioButton dw_duishui;
        RadioButton dw_gzhu;

    }

    public List<goodslisttab> getGoosList()
    {
        for (int i = 0; i < list.size(); i++)
        {
            ListItemView listItemView = (ListItemView) lmap.get(i).getTag();
            String aa=listItemView.et_number.getText().toString();
            String bb=listItemView.tv_dw.getText().toString();
            if (listItemView.et_number.getText().toString().equals(""))
            {
                return null;
            } else
            {
                Bundle bundle = (Bundle) listItemView.tv_fl.getTag();
                list.get(i).setYL(listItemView.et_number.getText().toString());
                list.get(i).setDW(listItemView.tv_dw.getText().toString());
                list.get(i).setParkId(bundle.get("pi").toString());
                list.get(i).setParkName(bundle.get("pn").toString());
                list.get(i).setAreaId(bundle.get("ai").toString());
                list.get(i).setAreaName(bundle.get("an").toString());
            }

        }
        return list;
    }

}
