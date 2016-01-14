package com.farm.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.farm.R;
import com.farm.bean.goodslisttab;
import com.farm.common.BitmapHelper;
import com.farm.widget.CustomDialog_FLSL;

import java.util.HashMap;
import java.util.List;

public class AddStd_Cmd_StepTwo_Self_goodslistdapter extends BaseAdapter
{
    Button btn_sure;
    EditText et_flsl;
    TextView tv_dw;
    CustomDialog_FLSL customDialog_flsl;
    HashMap<Integer, View> lmap = new HashMap<Integer, View>();
    private List<goodslisttab> list;
    private goodslisttab goodslisttab;
    private Context context;

    public AddStd_Cmd_StepTwo_Self_goodslistdapter(Context context, List<goodslisttab> list)
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
        ListItemView listItemView = null;
        if (lmap.get(position) == null)
        {
            convertView = View.inflate(context, R.layout.item_gridview, null);
            listItemView = new ListItemView();
            listItemView.typeicon = (ImageView) convertView.findViewById(R.id.typeicon);
            listItemView.typename = (TextView) convertView.findViewById(R.id.typename);
            listItemView.tv_gg = (TextView) convertView.findViewById(R.id.tv_gg);
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
            listItemView.cb_fl.setTag(goodslisttab);
            listItemView.cb_fl.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                {
                    goodslisttab goods = (goodslisttab) buttonView.getTag();
//                    if (isChecked)
//                    {
//                        SqliteDb.save(context, goods);
//                    } else
//                    {
//                        SqliteDb.deleteGoods(context, goodslisttab.class, goods.getId());
//                    }

//                    int pos = Integer.valueOf(buttonView.getTag().toString());
//                    currentlistItemView = (ListItemView) lmap.get(pos).getTag();
//                    if (isChecked)
//                    {
//                        showDialog_flsl(pos);
//
//                    } else
//                    {
//                        deleteSelectRecords(firstType, secondItemName[pos]);
//                        currentlistItemView.tv_flsl.setVisibility(View.GONE);
//                    }
                }
            });
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
        CheckBox cb_fl;
    }
    public void showDialog_flsl(final int pos)
    {
//        final View dialog_layout = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.customdialog_flsl_, null);
//        customDialog_flsl = new CustomDialog_FLSL(context, R.style.MyDialog, dialog_layout);
//        tv_dw = (TextView) dialog_layout.findViewById(R.id.tv_dw);
//        et_flsl = (EditText) dialog_layout.findViewById(R.id.et_flsl);
//        btn_sure = (Button) dialog_layout.findViewById(R.id.btn_sure);
//        tv_dw.setText(commandtab_single.getInstance().getNongzidw());
//        btn_sure.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                saveSelectCmdArea(firstid, firstType, secondItemid[pos], secondItemName[pos], et_flsl.getText().toString());
//                List<SelectCmdArea> list_SelectCmdArea = SqliteDb.getSelectCmdArea(context, SelectCmdArea.class);
//                acountnumber = 0;
//                for (int i = 0; i < list_SelectCmdArea.size(); i++)
//                {
//                    acountnumber = acountnumber + Integer.valueOf(list_SelectCmdArea.get(i).getGoodsnumber());
//                }
//                if (acountnumber > goodsNumber)
//                {
//                    currentlistItemView.tv_flsl.setVisibility(View.VISIBLE);
//                    currentlistItemView.tv_flsl.setText(et_flsl.getText().toString() + commandtab_single.getInstance().getNongzidw() + " " + "【不足】");
//
//                    customDialog_flsl.dismiss();
//                } else
//                {
//                    currentlistItemView.tv_flsl.setVisibility(View.VISIBLE);
//                    currentlistItemView.tv_flsl.setText(et_flsl.getText().toString() + commandtab_single.getInstance().getNongzidw());
//                    customDialog_flsl.dismiss();
//                }
//
//            }
//        });
//        customDialog_flsl.show();
    }

}
