package com.farm.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.farm.R;
import com.farm.bean.contractTab;
import com.farm.common.utils;
import com.farm.ui.PG_cbfscore_;

import java.util.HashMap;
import java.util.List;

/**
 * Created by hasee on 2016/6/12.
 */
public class PG_cbhAdapter extends BaseAdapter
{
    private Context context;// 运行上下文
    private List<contractTab> listItems;// 数据集合
    private LayoutInflater listContainer;
    contractTab contracttab;
    public PG_cbhAdapter(Context context, List<contractTab> data)
    {
        this.context = context;
        this.listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
        this.listItems = data;

    }

    static class ListItemView {
        public TextView name;
        public EditText num;
        public Button btn_save;
        public ProgressBar pb_upload;
    }

    @Override
    public int getCount()
    {
        return listItems.size();
    }

    @Override
    public Object getItem(int i)
    {
        return null;
    }

    @Override
    public long getItemId(int i)
    {
        return 0;
    }
    HashMap<Integer, View> lmap = new HashMap<Integer, View>();
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        contracttab = listItems.get(i);
        // 自定义视图
        ListItemView listItemView = null;
        if (lmap.get(i) == null) {
            // 获取list_item布局文件的视图
            view = listContainer.inflate(R.layout.pg_cbhadapter, null);
            listItemView = new ListItemView();
            // 获取控件对象
            listItemView.name = (TextView) view.findViewById(R.id.name);
            listItemView.num = (EditText) view.findViewById(R.id.num);
            listItemView.btn_save = (Button) view.findViewById(R.id.btn_save);

/*            view.setTag(listItemView);
            view.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Intent intent = new Intent(context,PG_cbfscore_.class);
                    intent.putExtra("contracttab", contracttab);
                    context.startActivity(intent);
                }
            });*/

            //数据添加

            listItemView.name.setText(contracttab.getContractNum());

            lmap.put(i, view);
            view.setTag(listItemView);
        } else {
            view = lmap.get(i);
            listItemView = (ListItemView) view.getTag();
        }


        return view;
    }
}
