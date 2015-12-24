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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.farm.R;
import com.farm.bean.SelectCmdArea;
import com.farm.bean.commandtab_single;
import com.farm.com.custominterface.FragmentCallBack;
import com.farm.common.SqliteDb;
import com.farm.widget.CustomDialog_FLSL;

import java.util.HashMap;

public class Area_Cmd_Adapter extends BaseAdapter
{
    Button btn_sure;
    EditText et_flsl;
    TextView tv_dw;
    ListItemView currentlistItemView;
    CustomDialog_FLSL customDialog_flsl;
    private Context context;
    private int position = 0;
    // Holder hold;
    private LayoutInflater listContainer;// 视图容器
    String firstid;
    String firstType;
    String[] secondItemid;
    String[] secondItemName;

    public Area_Cmd_Adapter(Context context, FragmentCallBack fragmentCallBack, String firstid, String firstType, String[] secondItemid, String[] secondItemName)
    {
        this.listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
        this.context = context;
        this.secondItemid = secondItemid;
        this.secondItemName = secondItemName;
        this.firstid = firstid;
        this.firstType = firstType;
    }

    public int getCount()
    {
        return secondItemName.length;
    }

    public Object getItem(int position)
    {
        return secondItemName[position];
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
            listItemView.tv_flsl = (TextView) convertView.findViewById(R.id.tv_flsl);
            listItemView.cb_area = (CheckBox) convertView.findViewById(R.id.cb_area);

            listItemView.cb_area.setText(secondItemName[arg0]);
            listItemView.cb_area.setTag(arg0);
            listItemView.cb_area.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                {
                    int pos = Integer.valueOf(buttonView.getTag().toString());
                    currentlistItemView = (ListItemView) lmap.get(pos).getTag();
                    if (isChecked)
                    {
                        showDialog_flsl(pos);

                    } else
                    {
                        deleteSelectRecords(firstType, secondItemName[pos]);
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
        selectCmdArea.setId(1);
        SqliteDb.save(context, selectCmdArea);
    }

    public void deleteSelectRecords(String firsttype, String secondType)
    {
        SqliteDb.deleteSelectCmdArea(context, SelectCmdArea.class, firsttype, secondType);
    }

    public void showDialog_flsl(final int pos)
    {
        final View dialog_layout = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.customdialog_flsl, null);
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
//                SqliteDb.
                currentlistItemView.tv_flsl.setVisibility(View.VISIBLE);
                currentlistItemView.tv_flsl.setText(et_flsl.getText().toString() + commandtab_single.getInstance().getNongzidw());
                saveSelectCmdArea(firstid, firstType, secondItemid[pos], secondItemName[pos], et_flsl.getText().toString());
                customDialog_flsl.dismiss();
            }
        });
        customDialog_flsl.show();
    }

}
