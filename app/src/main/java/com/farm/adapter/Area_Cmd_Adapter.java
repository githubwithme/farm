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
import com.farm.app.AppContext;
import com.farm.bean.SelectRecords;
import com.farm.com.custominterface.FragmentCallBack;
import com.farm.common.SqliteDb;
import com.farm.widget.CustomDialog_FLSL;

import java.util.HashMap;

public class Area_Cmd_Adapter extends BaseAdapter
{
    Button btn_sure;
    EditText et_flsl;
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
                        showDialog_flsl();
                    } else
                    {
                        deleteSelectRecords(AppContext.BELONG_ADD_CMD_AREA, firstType, secondItemName[position]);
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

    public void saveSelectRecords(String BELONG, String firstid, String firsttype, String secondid, String secondType)
    {
        SelectRecords selectRecords = new SelectRecords();
        selectRecords.setBELONG(BELONG);
        selectRecords.setFirstid(firstid);
        selectRecords.setFirsttype(firsttype);
        selectRecords.setSecondid(secondid);
        selectRecords.setSecondtype(secondType);
        selectRecords.setThirdid("");
        selectRecords.setThirdtype("");
        selectRecords.setId(1);
        SqliteDb.save(context, selectRecords);
    }

    public void deleteSelectRecords(String BELONG, String firsttype, String secondType)
    {
        SqliteDb.deleteRecordtemp(context, SelectRecords.class, BELONG, firsttype, secondType);
    }

    public void showDialog_flsl()
    {
        final View dialog_layout = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.customdialog_flsl, null);
        customDialog_flsl = new CustomDialog_FLSL(context, R.style.MyDialog, dialog_layout);
        et_flsl = (EditText) dialog_layout.findViewById(R.id.et_flsl);
        btn_sure = (Button) dialog_layout.findViewById(R.id.btn_sure);
        btn_sure.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                currentlistItemView.tv_flsl.setVisibility(View.VISIBLE);
                currentlistItemView.tv_flsl.setText(et_flsl.getText().toString()+"kg");
                saveSelectRecords(AppContext.BELONG_ADD_CMD_AREA, firstid, firstType, secondItemName[position], secondItemName[position]);
                customDialog_flsl.dismiss();
            }
        });
        customDialog_flsl.show();
    }

}
