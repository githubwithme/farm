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
import com.farm.bean.Dictionary_wheel;
import com.farm.com.custominterface.FragmentCallBack;

import java.util.HashMap;

public class AddPlantObservation_StepTwo_Adapter extends BaseAdapter
{
    private Context context;// 运行上下文
    private LayoutInflater listContainer;// 视图容器
    Dictionary_wheel dictionary_wheel;
    ListItemView listItemView = null;
    String[] firstItemName;
    FragmentCallBack fragmentCallBack;
    String item = "";

    static class ListItemView
    {
        public Button btn_zl;

    }

    public AddPlantObservation_StepTwo_Adapter(Context context, Dictionary_wheel data, FragmentCallBack fragmentCallBack)
    {
        this.context = context;
        this.listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
        firstItemName = data.getFirstItemName();
        dictionary_wheel = data;
        this.fragmentCallBack = fragmentCallBack;
    }

    public int getCount()
    {
        return firstItemName.length;
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
        item = firstItemName[position];
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
                    String fn = dictionary_wheel.getFirstItemName()[v.getId()];
                    String[] sn = dictionary_wheel.getSecondItemName().get(fn);
                    Bundle bundle = new Bundle();
                    bundle.putInt("INDEX",0);
                    bundle.putStringArray("SN",sn);
                    fragmentCallBack.callbackFun2(bundle);

//                    Intent intent = new Intent();
//                    intent.setAction(AppContext.ACTION_CURRENTITEM);
//                    intent.putExtra("INDEX", 0);
//                    String fn=dictionary_wheel.getFirstItemName()[v.getId()];
//                    String[] sn=dictionary_wheel.getSecondItemName().get(fn);
//                    intent.putExtra("SN",sn);
//                    context.sendBroadcast(intent);
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
}