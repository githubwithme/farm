package com.farm.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.farm.R;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.HaveReadNumber;
import com.farm.bean.commembertab;
import com.farm.bean.parktab;
import com.farm.common.BitmapHelper;
import com.farm.common.SqliteDb;
import com.farm.ui.Map_NC_;
import com.farm.ui.NCZ_CZ_ToDayJob_;
import com.farm.ui.NCZ_CZ_ToDayPlant_;
import com.farm.ui.NCZ_CZ_TodayCommand_;
import com.farm.ui.NCZ_ToDayPQ_;
import com.farm.ui.ShowUserInfo_;
import com.farm.ui.WeatherActivity_;
import com.farm.widget.CircleImageView;

import java.util.HashMap;
import java.util.List;

public class NCZ_YQPQAdapter extends BaseAdapter
{
    String audiopath;
    private Context context;// 运行上下文
    private List<parktab> listItems;// 数据集合
    private LayoutInflater listContainer;// 视图容器
    parktab parktab;
    ListItemView listItemView = null;
    commembertab commembertab;

    static class ListItemView
    {
        public TextView tv_pqzl;
        public TextView tv_pqmq;
        public TextView tv_pqgz;
        public TextView tv_yqmq;
        public TextView tv_yqgz;
        public TextView tv_yqzl;
        public TextView tv_yqsq;

        public TextView tv_job;
        public TextView tv_plant;
        public TextView tv_sq;
        public TextView tv_cmd;
        public TextView tv_job_new;
        public TextView tv_plant_new;
        public TextView tv_sq_new;
        public TextView tv_cmd_new;
        public TextView tv_czname;
        public TextView tv_yqname;
        public TextView tv_temph;
        public TextView tv_plantnumber;
        public TextView tv_worknumber;
        public TextView tv_cmdnumber;
        public TextView tv_plantnumber_new;
        public TextView tv_cmdnumber_new;
        public TextView tv_worknumber_new;
        public FrameLayout fl_cmdnumber_new;
        public ImageView iv_img;
        public LinearLayout ll_weather;
        public LinearLayout ll_ask;
        public LinearLayout ll_cmd;
        public LinearLayout ll_job;
        public LinearLayout ll_plant;
        public LinearLayout ll_jrpq;
        public FrameLayout fl_plant_new;
        public FrameLayout fl_cmd_new;
        public FrameLayout fl_worknumber_new;
        public FrameLayout fl_plantnumber_new;
        public FrameLayout fl_job_new;
        public FrameLayout fl_sq_new;
        public CircleImageView circle_img;
    }

    public NCZ_YQPQAdapter(Context context, List<parktab> data)
    {
        this.context = context;
        this.listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
        this.listItems = data;
        commembertab = AppContext.getUserInfo(context);
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

    HashMap<Integer, View> lmap = new HashMap<Integer, View>();

    public View getView(int position, View convertView, ViewGroup parent)
    {
        parktab = listItems.get(position);
        // 自定义视图
        if (lmap.get(position) == null)
        {
            // 获取list_item布局文件的视图
            convertView = listContainer.inflate(R.layout.ncz_yqpqadapter, null);
            listItemView = new ListItemView();
            // 获取控件对象
            listItemView.fl_cmd_new = (FrameLayout) convertView.findViewById(R.id.fl_cmd_new);
            listItemView.fl_plant_new = (FrameLayout) convertView.findViewById(R.id.fl_plant_new);
            listItemView.fl_worknumber_new = (FrameLayout) convertView.findViewById(R.id.fl_worknumber_new);
            listItemView.fl_plantnumber_new = (FrameLayout) convertView.findViewById(R.id.fl_plantnumber_new);
            listItemView.fl_job_new = (FrameLayout) convertView.findViewById(R.id.fl_job_new);
            listItemView.fl_sq_new = (FrameLayout) convertView.findViewById(R.id.fl_sq_new);

            listItemView.ll_jrpq = (LinearLayout) convertView.findViewById(R.id.ll_jrpq);
            listItemView.ll_weather = (LinearLayout) convertView.findViewById(R.id.ll_weather);
            listItemView.ll_cmd = (LinearLayout) convertView.findViewById(R.id.ll_cmd);
            listItemView.ll_ask = (LinearLayout) convertView.findViewById(R.id.ll_ask);
            listItemView.ll_job = (LinearLayout) convertView.findViewById(R.id.ll_job);
            listItemView.ll_plant = (LinearLayout) convertView.findViewById(R.id.ll_plant);
            listItemView.tv_temph = (TextView) convertView.findViewById(R.id.tv_temph);
            listItemView.tv_yqname = (TextView) convertView.findViewById(R.id.tv_yqname);
            listItemView.tv_czname = (TextView) convertView.findViewById(R.id.tv_czname);
            listItemView.tv_cmdnumber = (TextView) convertView.findViewById(R.id.tv_cmdnumber);
            listItemView.fl_cmdnumber_new = (FrameLayout) convertView.findViewById(R.id.fl_cmdnumber_new);
            listItemView.tv_sq = (TextView) convertView.findViewById(R.id.tv_sq);
            listItemView.tv_job = (TextView) convertView.findViewById(R.id.tv_job);
            listItemView.tv_cmd = (TextView) convertView.findViewById(R.id.tv_cmd);
            listItemView.tv_plant = (TextView) convertView.findViewById(R.id.tv_plant);
            listItemView.tv_sq_new = (TextView) convertView.findViewById(R.id.tv_sq_new);
            listItemView.tv_job_new = (TextView) convertView.findViewById(R.id.tv_job_new);
            listItemView.tv_cmd_new = (TextView) convertView.findViewById(R.id.tv_cmd_new);
            listItemView.tv_plant_new = (TextView) convertView.findViewById(R.id.tv_plant_new);
            listItemView.tv_cmdnumber_new = (TextView) convertView.findViewById(R.id.tv_cmdnumber_new);
            listItemView.tv_worknumber = (TextView) convertView.findViewById(R.id.tv_worknumber);
            listItemView.tv_plantnumber = (TextView) convertView.findViewById(R.id.tv_plantnumber);
            listItemView.tv_worknumber_new = (TextView) convertView.findViewById(R.id.tv_worknumber_new);
            listItemView.tv_plantnumber_new = (TextView) convertView.findViewById(R.id.tv_plantnumber_new);

            listItemView.tv_pqzl = (TextView) convertView.findViewById(R.id.tv_pqzl);
            listItemView.tv_pqmq = (TextView) convertView.findViewById(R.id.tv_pqmq);
            listItemView.tv_pqgz = (TextView) convertView.findViewById(R.id.tv_pqgz);
            listItemView.tv_yqzl = (TextView) convertView.findViewById(R.id.tv_yqzl);
            listItemView.tv_yqgz = (TextView) convertView.findViewById(R.id.tv_yqgz);
            listItemView.tv_yqmq = (TextView) convertView.findViewById(R.id.tv_yqmq);
            listItemView.tv_yqsq = (TextView) convertView.findViewById(R.id.tv_yqsq);

            listItemView.iv_img = (ImageView) convertView.findViewById(R.id.iv_img);
            listItemView.circle_img = (CircleImageView) convertView.findViewById(R.id.circle_img);
            listItemView.ll_jrpq.setId(position);
            listItemView.tv_yqname.setId(position);
            listItemView.ll_weather.setId(position);
            listItemView.ll_plant.setId(position);
            listItemView.ll_job.setId(position);
            listItemView.ll_ask.setId(position);
            listItemView.ll_cmd.setId(position);
            listItemView.circle_img.setTag(position);
            listItemView.circle_img.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Intent intent = new Intent(context, ShowUserInfo_.class);
                    intent.putExtra("workuserid", listItems.get(Integer.valueOf(v.getTag().toString())).getWorkuserid());
                    context.startActivity(intent);
                }
            });
            listItemView.ll_cmd.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Intent intent = new Intent(context, NCZ_CZ_TodayCommand_.class);
                    intent.putExtra("workuserid", listItems.get(v.getId()).getWorkuserid());
                    context.startActivity(intent);
                }
            });
            listItemView.ll_ask.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                }
            });
            listItemView.ll_job.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {

                    Intent intent = new Intent(context, NCZ_CZ_ToDayJob_.class);
                    intent.putExtra("workuserid", listItems.get(v.getId()).getWorkuserid());
                    context.startActivity(intent);
                }
            });
            listItemView.ll_plant.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Intent intent = new Intent(context, NCZ_CZ_ToDayPlant_.class);
                    intent.putExtra("parkid", listItems.get(v.getId()).getid());
                    intent.putExtra("workuserid", listItems.get(v.getId()).getWorkuserid());
                    context.startActivity(intent);
                }
            });
            listItemView.tv_yqname.setOnClickListener(new OnClickListener()
            {

                @Override
                public void onClick(View v)
                {
                    Intent intent = new Intent(context, Map_NC_.class);
                    intent.putExtra("parkid", listItems.get(v.getId()).getid());
                    context.startActivity(intent);
                }
            });
            listItemView.ll_weather.setOnClickListener(new OnClickListener()
            {

                @Override
                public void onClick(View v)
                {
                    Intent intent = new Intent(context, WeatherActivity_.class);
                    intent.putExtra("parkid", listItems.get(v.getId()).getid());
                    context.startActivity(intent);
                }
            });
            listItemView.ll_jrpq.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Intent intent = new Intent(context, NCZ_ToDayPQ_.class);
                    intent.putExtra("parkid", listItems.get(v.getId()).getid());
                    context.startActivity(intent);
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


        listItemView.tv_czname.setText(parktab.getRealName());
        listItemView.tv_yqname.setText(parktab.getparkName());

        if (Integer.valueOf(parktab.getCommandCount()) > 0)
        {
            listItemView.fl_cmd_new.setVisibility(View.VISIBLE);
        } else
        {
            listItemView.fl_cmd_new.setVisibility(View.GONE);
        }
        if (Integer.valueOf(parktab.getJobCount()) > 0)
        {
            listItemView.fl_job_new.setVisibility(View.VISIBLE);
        } else
        {
            listItemView.fl_job_new.setVisibility(View.GONE);
        }
        if (Integer.valueOf(parktab.getPlantGrowCount()) > 0)
        {
            listItemView.fl_plant_new.setVisibility(View.VISIBLE);
        } else
        {
            listItemView.fl_plant_new.setVisibility(View.GONE);
        }
        if (Integer.valueOf(parktab.getAreajobCount()) > 0)
        {
            listItemView.fl_worknumber_new.setVisibility(View.VISIBLE);
        } else
        {
            listItemView.fl_worknumber_new.setVisibility(View.GONE);
        }
        if (Integer.valueOf(parktab.getAreaplantGrowCount()) > 0)
        {
            listItemView.fl_plantnumber_new.setVisibility(View.VISIBLE);
        } else
        {
            listItemView.fl_plantnumber_new.setVisibility(View.GONE);
        }
        if (Integer.valueOf(parktab.getAreacommandCount()) > 0)
        {
            listItemView.fl_cmdnumber_new.setVisibility(View.VISIBLE);
        } else
        {
            listItemView.fl_cmdnumber_new.setVisibility(View.GONE);
        }
        listItemView.tv_plant.setText("园区苗情");
        listItemView.tv_sq.setText("场长申请");
        listItemView.tv_cmd.setText("场长指令");
        listItemView.tv_job.setText("场长工作");
        listItemView.tv_plantnumber.setText("片区苗情");
        listItemView.tv_worknumber.setText("片管工作");
        listItemView.tv_cmdnumber.setText("片管指令");

        listItemView.tv_temph.setText(parktab.getTMPL() + "-" + parktab.getTMPH());
        BitmapHelper.setImageViewBackground(context, listItemView.circle_img, AppConfig.baseurl + parktab.getImgurl());
        BitmapHelper.setImageViewBackground(context, listItemView.iv_img, AppConfig.baseurl + parktab.getWeatherimg());
        return convertView;
    }

    private void saveHaveReadData(String id, String pq_cmdnum, String pq_plantnum, String pq_jobnum, String plantnum, String jobnum, String asknum, String cmdnum)
    {
        HaveReadNumber haveReadNumber = new HaveReadNumber();
        haveReadNumber.setId(commembertab.getId() + id);
        haveReadNumber.setAsknum(asknum);
        haveReadNumber.setJobnum(jobnum);
        haveReadNumber.setPlantnum(plantnum);
        haveReadNumber.setCmdnum(cmdnum);
        haveReadNumber.setPq_jobnum(pq_jobnum);
        haveReadNumber.setPq_plantnum(pq_plantnum);
        haveReadNumber.setPq_cmdnum(pq_cmdnum);
        SqliteDb.save(context, haveReadNumber);
    }

    private HaveReadNumber getHaveReadData(String id)
    {
        HaveReadNumber haveReadNumber = (HaveReadNumber) SqliteDb.getHaveReadData(context, HaveReadNumber.class, id);
        return haveReadNumber;
    }

    private void updateHaveReadData(String id, String columnname, String columvalues)
    {
        HaveReadNumber haveReadNumber = new HaveReadNumber();
        haveReadNumber.setId(commembertab.getId() + id);
        if (columnname.equals("plantnum"))
        {
            haveReadNumber.setPlantnum(columvalues);
        } else if (columnname.equals("jobnum"))
        {
            haveReadNumber.setJobnum(columvalues);
        } else if (columnname.equals("cmdnum"))
        {
            haveReadNumber.setCmdnum(columvalues);
        } else if (columnname.equals("asknum"))
        {
            haveReadNumber.setAsknum(columvalues);
        } else if (columnname.equals("pq_plantnum"))
        {
            haveReadNumber.setPq_plantnum(columvalues);
        } else if (columnname.equals("pq_jobnum"))
        {
            haveReadNumber.setPq_jobnum(columvalues);
        } else if (columnname.equals("pq_cmdnum"))
        {
            haveReadNumber.setPq_cmdnum(columvalues);
        }
        SqliteDb.updateHaveReadData(context, haveReadNumber, id, columnname);
    }
}