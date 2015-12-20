package com.farm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.farm.R;
import com.farm.app.AppContext;
import com.farm.bean.SelectRecords;
import com.farm.bean.commandtab;
import com.farm.com.custominterface.FragmentCallBack;
import com.farm.common.SqliteDb;

import java.util.HashMap;

public class Area_Cmd_Adapter extends BaseAdapter
{
    commandtab commandtab;
    private Context context;
    private int position = 0;
    // Holder hold;
    private LayoutInflater listContainer;// 视图容器
    String firstid;
    String firstType;
    String[] secondItemid;
    String[] secondItemName;

    public Area_Cmd_Adapter(Context context, FragmentCallBack fragmentCallBack, String firstid, String firstType, String[] secondItemid, String[] secondItemName,commandtab commandtab)
    {
        this.commandtab= commandtab;
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
            listItemView.txt = (TextView) convertView.findViewById(R.id.moreitem_txt);
            listItemView.img = (ImageView) convertView.findViewById(R.id.moreitem_img);
            listItemView.ll_sl = (LinearLayout) convertView.findViewById(R.id.ll_sl);
            listItemView.txt.setText(secondItemName[arg0]);

            lmap.put(arg0, convertView);
            convertView.setTag(listItemView);
        } else
        {
            convertView = lmap.get(arg0);
            listItemView = (ListItemView) convertView.getTag();
        }
        return convertView;
    }

    public void setSelectItem(int position)
    {
        this.position = position;
        ListItemView listItemView = (ListItemView) lmap.get(position).getTag();
        Boolean isselected = (Boolean) listItemView.img.getTag();
        if (isselected != null && isselected)// 已选中
        {
            listItemView.txt.setTextColor(0xFF666666);
            listItemView.img.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.item_collection_unselect));
            listItemView.img.setTag(false);
            deleteSelectRecords(AppContext.BELONG_ADD_CMD_AREA, firstType, secondItemName[position]);
            listItemView.ll_sl.setVisibility(View.GONE);
        } else
        // 没选中
        {
            listItemView.txt.setTextColor(0xFFFF8C00);// 设置第一项选择后的颜色
            listItemView.img.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.item_collection_selected));
            listItemView.img.setTag(true);
            saveSelectRecords(AppContext.BELONG_ADD_CMD_AREA, firstid, firstType, secondItemName[position], secondItemName[position]);
            listItemView.ll_sl.setVisibility(View.VISIBLE);
        }
    }

    static class ListItemView
    { // 自定义控件集合
        TextView txt;
        ImageView img;
        LinearLayout ll_sl;
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


}
