package com.farm.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.farm.R;
import com.farm.bean.planttab;

import java.util.List;

/**
 *
 */
public class AddPlantObservationAdapter extends BaseAdapter
{
    ListItemView listItemView = null;
    String audiopath;
    private Context context;// 运行上下文
    private List<planttab> listItems;// 数据集合
    private LayoutInflater listContainer;// 视图容器
    planttab planttab;

    static class ListItemView
    { // 自定义控件集合

        public LinearLayout ll;

    }

    /**
     * 实例化Adapter
     *
     * @param context
     * @param data
     * @param context
     */
    public AddPlantObservationAdapter(Context context, List<planttab> data)
    {
        this.context = context;
        this.listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
        this.listItems = data;
    }

    public int getCount()
    {
        return listItems.size();
    }

    public Object getItem(int arg0)
    {
        return null;
    }

    public long getItemId(int arg0)
    {
        return 0;
    }


    /**
     * ListView Item设置
     */
    public View getView(int position, View convertView, ViewGroup parent)
    {
        planttab = listItems.get(position);
        convertView = listContainer.inflate(R.layout.addplantobservationadapter, null);
        listItemView = new ListItemView();
        listItemView.ll = (LinearLayout) convertView.findViewById(R.id.ll);

        for (int i = 0; i < 9; i++)
        {
            if (i == 0)
            {
                TextView tv = new TextView(context);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(120, 120, 0);
                lp.setMargins(0, 0, 0, 0);
                lp.gravity = Gravity.CENTER_HORIZONTAL;
                tv.setLayoutParams(lp);
                listItemView.ll.addView(tv);
                tv.setText(planttab.getplantName());
                tv.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bg_city_search_normal));
            } else
            {
                EditText et = new EditText(context);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(120, 120, 0);
                lp.setMargins(0, 0, 0, 0);
                lp.gravity = Gravity.CENTER_HORIZONTAL;
                et.setLayoutParams(lp);
                int inputType = InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL;
                et.setInputType(inputType);
                et.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bg_city_search_normal));

//                et.setTag(R.id.tag_fn, key);
                listItemView.ll.addView(et);

                et.addTextChangedListener(new TextWatcher()
                {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after)
                    {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count)
                    {

                    }

                    @Override
                    public void afterTextChanged(Editable s)
                    {
                        //此处即监听EditText输入
                        String input = s.toString();
                        if (!TextUtils.isEmpty(input))
                        {
//                           String currentParentId = (String) v.getTag(R.id.tag_fi);
                        }
                    }
                });
            }

        }
        return convertView;
    }
}