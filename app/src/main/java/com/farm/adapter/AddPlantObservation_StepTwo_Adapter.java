package com.farm.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.farm.R;
import com.farm.bean.Dictionary;
import com.farm.bean.plantgc_single;
import com.farm.com.custominterface.FragmentCallBack_AddPlantObservation;

import java.util.HashMap;
import java.util.List;

public class AddPlantObservation_StepTwo_Adapter extends BaseAdapter
{
    String currentItem = "";
    Button tempButton;
    private Context context;// 运行上下文
    private LayoutInflater listContainer;// 视图容器
    Dictionary dic_comm;
    ListItemView listItemView = null;
    List<String> firstItemName;
    FragmentCallBack_AddPlantObservation fragmentCallBack;
    String item = "";

    static class ListItemView
    {
        public Button btn_zl;

    }

    public AddPlantObservation_StepTwo_Adapter(Context context, Dictionary dic_comm, FragmentCallBack_AddPlantObservation fragmentCallBack)
    {
        this.context = context;
        this.listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
        firstItemName = dic_comm.getFirstItemName();
        this.dic_comm = dic_comm;
        this.fragmentCallBack = fragmentCallBack;
    }

    public int getCount()
    {
        return firstItemName.size();
    }

    public Object getItem(int arg0)
    {
        return null;
    }

    public long getItemId(int arg0)
    {
        return 0;
    }

    HashMap<Integer, View> lmap = new HashMap<Integer, View>();

    public View getView(int position, View convertView, ViewGroup parent)
    {
        item = firstItemName.get(position);
        // 自定义视图

        if (lmap.get(position) == null)
        {
            // 获取list_item布局文件的视图
            convertView = listContainer.inflate(R.layout.addplantobservation_steptwo_item, null);
            listItemView = new ListItemView();
            // 获取控件对象
            listItemView.btn_zl = (Button) convertView.findViewById(R.id.btn_zl);
            listItemView.btn_zl.setId(position);
            listItemView.btn_zl.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {

                    if (tempButton != null)
                    {
                        tempButton.setSelected(false);
                    }
                    tempButton = (Button) v;
                    v.setSelected(true);
                    currentItem = tempButton.getText().toString();
                    plantgc_single plantgc_single = com.farm.bean.plantgc_single.getInstance();
                    plantgc_single.setGcq(tempButton.getText().toString());
                    String fn = dic_comm.getFirstItemName().get(v.getId());
                    Bundle bundle = new Bundle();
                    bundle.putInt("INDEX", 0);
                    fragmentCallBack.callbackFun2(bundle);

                }
            });
            // 设置控件集到convertView
            lmap.put(position, convertView);
            convertView.setTag(listItemView);
        } else
        {
            convertView = lmap.get(position);
            listItemView = (ListItemView) convertView.getTag();
        }
        listItemView.btn_zl.setText(item);

        return convertView;
    }

    public String getCurrentItem()
    {
        return currentItem;
    }
}