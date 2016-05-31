package com.farm.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.farm.R;
import com.farm.app.AppConfig;
import com.farm.bean.plantgrowthtab;
import com.farm.common.BitmapHelper;
import com.farm.ui.DisplayImage_;

import java.util.List;

/**
 *
 */
public class PlantObservationAdapter extends BaseAdapter
{
    ListItemView listItemView = null;
    String audiopath;
    private Context context;// 运行上下文
    private List<plantgrowthtab> listItems;// 数据集合
    private LayoutInflater listContainer;// 视图容器
    plantgrowthtab plantgrowthtab;

    static class ListItemView
    { // 自定义控件集合

        public TextView tv_plantname;
        public TextView tv_zg;
        public TextView tv_wj;
        public TextView tv_ys;
        public TextView tv_lys;
        public CheckBox cb_sfcl;
        public CheckBox cb_sfly;
        public CheckBox cb_sfyz;
        public LinearLayout ll_picture;

    }

    /**
     * 实例化Adapter
     *
     * @param context
     * @param data
     * @param context
     */
    public PlantObservationAdapter(Context context, List<plantgrowthtab> data)
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
        plantgrowthtab = listItems.get(position);
        convertView = listContainer.inflate(R.layout.plantobservationadapter, null);
        listItemView = new ListItemView();
        listItemView.ll_picture = (LinearLayout) convertView.findViewById(R.id.ll_picture);
        listItemView.tv_plantname = (TextView) convertView.findViewById(R.id.tv_plantname);
        listItemView.tv_zg = (TextView) convertView.findViewById(R.id.tv_zg);
        listItemView.tv_wj = (TextView) convertView.findViewById(R.id.tv_wj);
        listItemView.tv_ys = (TextView) convertView.findViewById(R.id.tv_ys);
        listItemView.tv_lys = (TextView) convertView.findViewById(R.id.tv_lys);
        listItemView.cb_sfcl = (CheckBox) convertView.findViewById(R.id.cb_sfcl);
        listItemView.cb_sfly = (CheckBox) convertView.findViewById(R.id.cb_sfly);
        listItemView.cb_sfyz = (CheckBox) convertView.findViewById(R.id.cb_sfyz);

        listItemView.tv_plantname.setText(plantgrowthtab.getplantName());
        listItemView.tv_zg.setText(plantgrowthtab.gethNum());
        listItemView.tv_wj.setText(plantgrowthtab.getwNum());
        listItemView.tv_ys.setText(plantgrowthtab.getyNum());
        listItemView.tv_lys.setText(plantgrowthtab.getxNum());
        if (plantgrowthtab.getcDate().equals("1"))
        {
            listItemView.cb_sfcl.setChecked(true);
        }
        if (plantgrowthtab.getzDate().equals("1"))
        {
            listItemView.cb_sfly.setChecked(true);
        }
        if (plantgrowthtab.getplantType().equals("1"))
        {
            listItemView.cb_sfyz.setChecked(true);
        }

        List<String> list_img = plantgrowthtab.getImgUrl();
        if (list_img != null)
        {
            for (int i = 0; i < list_img.size(); i++)
            {
                ImageView imageView = new ImageView(context);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(180, ViewGroup.LayoutParams.MATCH_PARENT, 0);
                lp.setMargins(25, 4, 0, 4);
                imageView.setLayoutParams(lp);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                BitmapHelper.setImageViewBackground(context, imageView, AppConfig.baseurl + list_img.get(i));
                imageView.setTag(AppConfig.baseurl + list_img.get(i));
                imageView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        String url = v.getTag().toString();
                        Intent intent = new Intent(context, DisplayImage_.class);
                        intent.putExtra("url", url);
                        context.startActivity(intent);
                    }
                });
                listItemView.ll_picture.addView(imageView);
            }
        }


        return convertView;
    }
}