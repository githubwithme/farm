package com.farm.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.farm.R;
import com.farm.bean.FJ_SCFJ;
import com.farm.bean.plantgrowthtab;
import com.farm.common.BitmapHelper;

import java.io.File;
import java.util.List;

/**
 *
 */
public class AddPlantObservationAdapter_MakeSure extends BaseAdapter
{
    List<FJ_SCFJ> list_fj_scfj;
    ListItemView listItemView = null;
    String audiopath;
    private Context context;// 运行上下文
    private List<plantgrowthtab> listItems;// 数据集合
    private LayoutInflater listContainer;// 视图容器
    plantgrowthtab plantgrowthtab;

    static class ListItemView
    { // 自定义控件集合

        public TextView tv_plantname;
        public LinearLayout ll_picture;
        public TextView tv_zg;
        public TextView tv_wj;
        public TextView tv_ys;
        public TextView tv_lys;
        public CheckBox cb_sfyz;
        public CheckBox cb_sfly;
        public CheckBox cb_sfcl;

    }

    /**
     * 实例化Adapter
     *
     * @param context
     * @param data
     * @param context
     */
    public AddPlantObservationAdapter_MakeSure(Context context, List<plantgrowthtab> data, List<FJ_SCFJ> list_fj_scfj)
    {
        this.context = context;
        this.list_fj_scfj = list_fj_scfj;
        this.listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
        this.listItems = data;
    }

    public int getCount()
    {
        return listItems.size();
    }

    public Object getItem(int arg0)
    {
        return listItems.get(arg0);
    }

    public long getItemId(int arg0)
    {
        return arg0;
    }


    /**
     * ListView Item设置
     */
    public View getView(int position, View convertView, ViewGroup parent)
    {
        plantgrowthtab = listItems.get(position);
        convertView = listContainer.inflate(R.layout.addplantobservationadapter_makesure, null);
        listItemView = new ListItemView();
        listItemView.tv_plantname = (TextView) convertView.findViewById(R.id.tv_plantname);
        listItemView.ll_picture = (LinearLayout) convertView.findViewById(R.id.ll_picture);
        listItemView.tv_zg = (TextView) convertView.findViewById(R.id.tv_zg);
        listItemView.tv_wj = (TextView) convertView.findViewById(R.id.tv_wj);
        listItemView.tv_ys = (TextView) convertView.findViewById(R.id.tv_ys);
        listItemView.tv_lys = (TextView) convertView.findViewById(R.id.tv_lys);
        listItemView.cb_sfly = (CheckBox) convertView.findViewById(R.id.cb_sfly);
        listItemView.cb_sfcl = (CheckBox) convertView.findViewById(R.id.cb_sfcl);
        listItemView.cb_sfyz = (CheckBox) convertView.findViewById(R.id.cb_sfyz);
        listItemView.tv_plantname.setText(plantgrowthtab.getplantName());
        listItemView.tv_zg.setText(plantgrowthtab.gethNum());
        listItemView.tv_wj.setText(plantgrowthtab.getwNum());
        listItemView.tv_ys.setText(plantgrowthtab.getyNum());
        listItemView.tv_lys.setText(plantgrowthtab.getxNum());
        if (plantgrowthtab.getplantType().equals("1"))
        {
            listItemView.cb_sfyz.setChecked(true);
        } else
        {
            listItemView.cb_sfyz.setChecked(false);
        }
        if (plantgrowthtab.getcDate().equals("1"))
        {
            listItemView.cb_sfcl.setChecked(true);
        } else
        {
            listItemView.cb_sfcl.setChecked(false);
        }
        if (plantgrowthtab.getzDate().equals("1"))
        {
            listItemView.cb_sfly.setChecked(true);
        } else
        {
            listItemView.cb_sfly.setChecked(false);
        }


        for (int i = 0; i < list_fj_scfj.size(); i++)
        {
            if (list_fj_scfj.get(i).getFJID().equals(plantgrowthtab.getplantId()))
            {
                ImageView imageView = new ImageView(context);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(180, ViewGroup.LayoutParams.MATCH_PARENT, 0);
                lp.setMargins(25, 4, 0, 4);
                imageView.setLayoutParams(lp);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                BitmapHelper.setImageView(context, imageView, list_fj_scfj.get(i).getFJBDLJ());
                imageView.setTag(list_fj_scfj.get(i).getFJBDLJ());
                imageView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        String bdlj = v.getTag().toString();
                        File file = new File(bdlj);
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(Uri.fromFile(file), "image/*");
                        context.startActivity(intent);
                    }
                });
                listItemView.ll_picture.addView(imageView);
            }

        }
        return convertView;
    }
}