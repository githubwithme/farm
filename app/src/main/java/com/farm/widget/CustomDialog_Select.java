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
import android.widget.Spinner;
import android.widget.TextView;

import com.farm.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by hasee on 2016/7/13.
 */
public class CustomDialog_Select extends Dialog
{

    HashMap<Integer, View> lmap = new HashMap<Integer, View>();
    CustomDialogListener cdListener;
    Context context;
    View layout;
    List<String> listdata=new ArrayList<String>();
    List<String> listid=new ArrayList<String>();
    String currentDat = "";
    ListView lv;
    Spinner parkSpinner;
    public CustomDialog_Select(Context context, int theme, View layout, List<String> listdata, List<String> listid, CustomDialogListener cdListener)
    {
        super(context, theme);
        this.context = context;
        this.layout = layout;
        this.listdata = listdata;
        this.listid = listid;
        this.cdListener = cdListener;
    }

    public CustomDialog_Select(Context context, int theme)
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
        parkSpinner= (Spinner) findViewById(R.id.parkSpinner);

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
                convertView = LayoutInflater.from(context).inflate(R.layout.customdialog_listview_item, null);
                listItemView = new ListItemView();
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
            return convertView;
        }
    }

    static class ListItemView
    {
        public TextView tv_name;
    }

    public interface CustomDialogListener
    {
        void OnClick(Bundle bundle);
    }

}
