package com.farm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.farm.R;
import com.farm.bean.ContractCommandProgressBean;

import java.util.HashMap;
import java.util.List;

public class Adapter_PGCommandProgress extends BaseAdapter
{
    private Context context;// 运行上下文
    private List<ContractCommandProgressBean> listItems;// 数据集合
    private LayoutInflater listContainer;// 视图容器
    ContractCommandProgressBean contractCommandProgressBean;

    class ListItemView
    {
        public ProgressBar pb_jd;
        public Button btn_edit;
        public Button btn_add;
        public Button btn_minus;
        public TextView tv_contractname;
        public TextView tv_progress;
        public LinearLayout ll_operateprogress;
    }

    public Adapter_PGCommandProgress(Context context, List<ContractCommandProgressBean> data)
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

    HashMap<Integer, View> lmap = new HashMap<Integer, View>();

    public View getView(int position, View convertView, ViewGroup parent)
    {
        contractCommandProgressBean = listItems.get(position);
        // 自定义视图
        ListItemView listItemView = null;
        if (lmap.get(position) == null)
        {
            // 获取list_item布局文件的视图
            convertView = listContainer.inflate(R.layout.adapter_pgcommandprogress, null);
            listItemView = new ListItemView();
            // 获取控件对象
            listItemView.tv_contractname = (TextView) convertView.findViewById(R.id.tv_contractname);
            listItemView.tv_progress = (TextView) convertView.findViewById(R.id.tv_progress);
            listItemView.pb_jd = (ProgressBar) convertView.findViewById(R.id.pb_jd);
            listItemView.ll_operateprogress = (LinearLayout) convertView.findViewById(R.id.ll_operateprogress);
            listItemView.btn_edit = (Button) convertView.findViewById(R.id.btn_edit);
            listItemView.btn_add = (Button) convertView.findViewById(R.id.btn_add);
            listItemView.btn_minus = (Button) convertView.findViewById(R.id.btn_minus);
            listItemView.btn_edit.setTag(R.id.tag_ll, listItemView.ll_operateprogress);
            listItemView.btn_add.setTag(R.id.tag_pb, listItemView.pb_jd);
            listItemView.btn_add.setTag(R.id.tag_text, listItemView.tv_progress);
            listItemView.btn_minus.setTag(R.id.tag_pb, listItemView.pb_jd);
            listItemView.btn_minus.setTag(R.id.tag_text, listItemView.tv_progress);
            listItemView.btn_edit.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Button button = (Button) v;
                    LinearLayout ll_operateprogress = (LinearLayout) v.getTag(R.id.tag_ll);
                    if (button.getText().equals("确定"))
                    {
                        button.setText("编辑");
                        ll_operateprogress.setVisibility(View.GONE);
                        //进行网络提交操作...
                    } else
                    {
                        button.setText("确定");
                        ll_operateprogress.setVisibility(View.VISIBLE);
                    }
                }
            });
            listItemView.btn_minus.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    ProgressBar pb_jd = (ProgressBar) v.getTag(R.id.tag_pb);
                    TextView tv_progress = (TextView) v.getTag(R.id.tag_text);
                    if (pb_jd.getProgress() <= 100)
                    {
                        pb_jd.setProgress(pb_jd.getProgress() - 10);
                        tv_progress.setText(pb_jd.getProgress() + "%");
                    } else
                    {
                        Toast.makeText(context, "已经0%了", Toast.LENGTH_SHORT).show();
                    }

                }
            });
            listItemView.btn_add.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    ProgressBar pb_jd = (ProgressBar) v.getTag(R.id.tag_pb);
                    TextView tv_progress = (TextView) v.getTag(R.id.tag_text);
                    if (pb_jd.getProgress() <= 100)
                    {
                        pb_jd.setProgress(pb_jd.getProgress() + 10);
                        tv_progress.setText(pb_jd.getProgress() + "%");
                    } else
                    {
                        Toast.makeText(context, "已经100%了", Toast.LENGTH_SHORT).show();
                    }

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
        // 设置文字和图片
        listItemView.tv_progress.setText(contractCommandProgressBean.getProgress()+"%");
        listItemView.tv_contractname.setText(contractCommandProgressBean.getContractName());
        listItemView.pb_jd.setProgress(Integer.valueOf(contractCommandProgressBean.getProgress()));
        return convertView;
    }
}