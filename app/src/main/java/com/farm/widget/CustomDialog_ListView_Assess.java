package com.farm.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.farm.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * context layout button
 *
 * @author Administrator
 */
public class CustomDialog_ListView_Assess extends Dialog
{
    HashMap<Integer, View> lmap = new HashMap<Integer, View>();
    CustomDialogListener cdListener;
    Context context;
    View layout;
    List<String> listdata = new ArrayList<String>();
    List<String> listid = new ArrayList<String>();
    List<String> listvalues = new ArrayList<String>();
    String currentDat = "";
    ListView lv;

    public CustomDialog_ListView_Assess(Context context, int theme, View layout, List<String> listdata, List<String> listid, List<String> listvalues, CustomDialogListener cdListener)
    {
        super(context, theme);
        this.context = context;
        this.layout = layout;
        this.listdata = listdata;
        this.listid = listid;
        this.listvalues = listvalues;
        this.cdListener = cdListener;
    }

    public CustomDialog_ListView_Assess(Context context, int theme)
    {
        super(context, theme);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.setContentView(layout);
        this.setCanceledOnTouchOutside(true);
        lv = (ListView) findViewById(R.id.lv);
        lv.setAdapter(new Dialog_ListViewAdapter());
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Bundle bundle = new Bundle();
                bundle.putString("id", listid.get(position));
                bundle.putString("name", listdata.get(position));
                cdListener.OnClick(bundle);
                dismiss();
            }
        });

    }

    public class Dialog_ListViewAdapter extends BaseAdapter
    {
        @Override
        public int getCount()
        {
            return listdata.size();
        }

        @Override
        public Object getItem(int position)
        {
            return listdata.get(position);
        }

        @Override
        public long getItemId(int position)
        {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            currentDat = listdata.get(position);
            // 自定义视图
            ListItemView listItemView = null;
            if (lmap.get(position) == null)
            {
                convertView = LayoutInflater.from(context).inflate(R.layout.customdialog_listview_assess_item, null);
                listItemView = new ListItemView();
                listItemView.tv_scroe = (TextView) convertView.findViewById(R.id.tv_scroe);
                listItemView.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                lmap.put(position, convertView);
                convertView.setTag(listItemView);
            } else
            {
                convertView = lmap.get(position);
                listItemView = (ListItemView) convertView.getTag();
            }
            // 设置文字和图片
            listItemView.tv_name.setText(listdata.get(position));
            listItemView.tv_scroe.setText(listvalues.get(position)+"分");
            return convertView;
        }
    }

    static class ListItemView
    {
        public TextView tv_name;
        public TextView tv_scroe;
    }

    public interface CustomDialogListener
    {
        public void OnClick(Bundle bundle);
    }

}