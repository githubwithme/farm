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
import android.widget.ListView;
import android.widget.TextView;

import com.farm.R;
import com.farm.bean.SelectCmdArea;
import com.farm.bean.commandtab_single;
import com.farm.bean.goodslisttab;
import com.farm.bean.goodslisttab_flsl;
import com.farm.com.custominterface.FragmentCallBack;
import com.farm.common.SqliteDb;
import com.farm.widget.CustomDialog_FLSL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Area_Cmd_Adapter extends BaseAdapter
{
    InputGoodsAdapter inputGoodsAdapter;
    Button btn_sure;
    ListView lv;
    EditText et_flsl;
    TextView tv_dw;
    ListItemView currentlistItemView;
    CustomDialog_FLSL customDialog_flsl;
    private Context context;
    int currentPostion = 0;
    private int position = 0;
    // Holder hold;
    private LayoutInflater listContainer;// 视图容器
    List<goodslisttab> list;
    String firstid;
    String firstType;
    List<String> secondItemid;
    List<String> secondItemName;
    List<String> thirdItemName;
    int acountnumber = 0;

    public Area_Cmd_Adapter(Context context, FragmentCallBack fragmentCallBack, String firstid, String firstType, List<String> secondItemid, List<String> secondItemName, List<String> thirdItemName, List<goodslisttab> list)
    {
        this.listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
        this.list = list;
        this.context = context;
        this.secondItemid = secondItemid;
        this.secondItemName = secondItemName;
        this.thirdItemName = thirdItemName;
        this.firstid = firstid;
        this.firstType = firstType;
    }

    public int getCount()
    {
        return secondItemName.size();
    }

    public Object getItem(int position)
    {
        return secondItemName.get(position);
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
            convertView = listContainer.inflate(R.layout.area_cmd_adapter, null);
            listItemView = new ListItemView();
            listItemView.tv_tip_full = (TextView) convertView.findViewById(R.id.tv_tip_full);
            listItemView.tv_flsl = (TextView) convertView.findViewById(R.id.tv_flsl);
            listItemView.cb_area = (CheckBox) convertView.findViewById(R.id.cb_area);

            listItemView.cb_area.setText(secondItemName.get(arg0));
            Bundle bundle = new Bundle();
            bundle.putString("index", arg0);
            bundle.putString("pi", firstid);
            bundle.putString("pn", firstType);
            listItemView.cb_area.setTag(arg0);
            listItemView.cb_area.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                {
                    Bundle bundle = (Bundle) buttonView.getTag();
                    currentPostion = Integer.valueOf( bundle.getString("index").toString());
                    String pi =bundle.getString("pi");
                    String pn =bundle.getString("pn");
                    currentlistItemView = (ListItemView) lmap.get(currentPostion).getTag();
                    if (isChecked)
                    {
                        showDialog_flsl(currentPostion,pi,pn);
                    } else
                    {
                        deleteSelectRecords(firstType, secondItemName.get(currentPostion));
                        currentlistItemView.tv_flsl.setVisibility(View.GONE);
                    }
                }
            });

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
        TextView tv_tip_full;
        TextView tv_flsl;
        CheckBox cb_area;
    }

    public void saveSelectCmdArea(String firstid, String firsttype, String secondid, String secondType, String goodsnumber)
    {
        SelectCmdArea selectCmdArea = new SelectCmdArea();
        selectCmdArea.setFirstid(firstid);
        selectCmdArea.setFirsttype(firsttype);
        selectCmdArea.setSecondid(secondid);
        selectCmdArea.setSecondtype(secondType);
        selectCmdArea.setThirdid("");
        selectCmdArea.setThirdtype("");
        selectCmdArea.setGoodsnumber(goodsnumber);
        selectCmdArea.setGoodsdw(commandtab_single.getInstance().getNongzidw());
        selectCmdArea.setId(1);
        SqliteDb.save(context, selectCmdArea);
    }

    public void deleteSelectRecords(String firsttype, String secondType)
    {
        SqliteDb.deleteSelectCmdArea(context, SelectCmdArea.class, firsttype, secondType);
    }

    public void showDialog_flsl(final int pos, String pi , String pn)
    {
        final View dialog_layout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.dialog_inputflsl, null);
        customDialog_flsl = new CustomDialog_FLSL(context, R.style.MyDialog, dialog_layout);
        lv = (ListView) dialog_layout.findViewById(R.id.lv);
        inputGoodsAdapter = new InputGoodsAdapter(context, list, pi, pn, secondItemid.get(currentPostion), secondItemName.get(currentPostion));
        lv.setAdapter(inputGoodsAdapter);
        btn_sure = (Button) dialog_layout.findViewById(R.id.btn_sure);
        btn_sure.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String goodsYL = "";
                String goodssum = "";
                List<goodslisttab_flsl> list_goodslisttab_flsl = new ArrayList<goodslisttab_flsl>();
                List<goodslisttab> list_goodslisttab = inputGoodsAdapter.getGoosList();
                for (int i = 0; i < list_goodslisttab.size(); i++)
                {
                    goodslisttab_flsl goodslisttab_flsl = new goodslisttab_flsl();
                    goodslisttab goodslisttab = list_goodslisttab.get(i);

                    goodslisttab_flsl.setId(goodslisttab.getId());
                    goodslisttab_flsl.setParkId(goodslisttab.getParkId());
                    goodslisttab_flsl.setParkName(goodslisttab.getParkName());
                    goodslisttab_flsl.setAreaId(goodslisttab.getAreaId());
                    goodslisttab_flsl.setAreaName(goodslisttab.getAreaName());
                    goodslisttab_flsl.setGoodsSum(goodslisttab.getAreaName());
                    goodsYL = goodsYL + goodslisttab.getId() + ":" + goodslisttab.getYL() + ",";
                    goodssum = goodssum + goodslisttab.getgoodsName() + ":" + goodslisttab.getYL() + ",";
                    goodslisttab_flsl.setYL(goodsYL);
                    goodslisttab_flsl.setGoodsSum(goodssum);
                    goodslisttab_flsl.setgoodsunit(goodslisttab.getgoodsunit());
                    goodslisttab_flsl.setgoodsSpec(goodslisttab.getgoodsSpec());
                    list_goodslisttab_flsl.add(goodslisttab_flsl);
                }
                SqliteDb.saveAll(context, list_goodslisttab_flsl);
//                acountnumber = 0;
//                String number = "";
//                String small_dw = "";
//                String large_dw = "";
//                for (int i = 0; i < list.size(); i++)
//                {
//                    goodslisttab goodslisttab = list.get(i);
//                    String[] goodsspc = goodslisttab.getgoodsSpec().split("/");
//                    number = goodsspc[0];
//                    small_dw = goodsspc[1];
//                    large_dw = goodsspc[2];
//                    acountnumber = acountnumber + Integer.valueOf(list.get(i).getYL()) * Integer.valueOf(thirdItemName.get(currentPostion));
//                }
//                int neednumber = acountnumber / Integer.valueOf(small_dw);
//                int allnumber = Integer.valueOf(list.get(currentPostion).getGoodsSum());
//                if (neednumber > allnumber)
//                {
//                    currentlistItemView.tv_tip_full.setText(list.get + " " + "【不足】");
//                } else
//                {
//                    currentlistItemView.tv_flsl.setText(et_flsl.getText().toString() + commandtab_single.getInstance().getNongzidw());
//                    customDialog_flsl.dismiss();
//                }
                String result = "";
                for (int i = 0; i < list_goodslisttab.size(); i++)
                {
                    goodslisttab goodslisttab = list_goodslisttab.get(i);
                    int acountnumber = 0;
                    String[] goodsspc = goodslisttab.getgoodsSpec().split("/");
                    String number = goodsspc[0];
                    String small_dw = goodsspc[1];
                    String large_dw = goodsspc[2];
                    String a = list_goodslisttab.get(i).getYL();
                    String b = thirdItemName.get(currentPostion);
                    acountnumber = acountnumber + Integer.valueOf(list_goodslisttab.get(i).getYL()) * Integer.valueOf(thirdItemName.get(currentPostion));
                    int neednumber = acountnumber / Integer.valueOf(number);
                    currentlistItemView.tv_flsl.setVisibility(View.VISIBLE);
                    result = result + goodslisttab.getgoodsName() + ":" + goodslisttab.getYL() + small_dw + "/株" + "共需" + neednumber + goodslisttab.getgoodsunit() + "\n\n";
                }
                currentlistItemView.tv_flsl.setText(result);
                customDialog_flsl.dismiss();
            }
        });
        customDialog_flsl.show();
    }

}
