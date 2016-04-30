package com.farm.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.farm.R;
import com.farm.app.AppContext;
import com.farm.bean.Today_job;
import com.farm.bean.commembertab;
import com.farm.bean.jobtab;
import com.farm.ui.Common_CommandDetail_Show_;
import com.farm.ui.Common_JobDetail_;
import com.farm.ui.Common_JobDetail_Assess_;
import com.farm.ui.NCZ_Todayjob_Common_;
import com.farm.ui.RecordList_;
import com.farm.widget.CircleImageView;
import com.farm.widget.CustomDialog_ListView;
import com.swipelistview.SimpleSwipeListener;
import com.swipelistview.SwipeLayout;

import java.util.HashMap;
import java.util.List;

/**
 * Created by user on 2016/4/25.
 */
public class NCZ_todaygzExecute_Adapter extends BaseExpandableListAdapter
{
    TextView currentTextView;
    CustomDialog_ListView customDialog_listView;
    private int currentItem = 0;
    ExpandableListView mainlistview;
    private Context context;// 运行上下文
    int currentChildsize = 0;
    private GoodsAdapter adapter;
    List<Today_job> listData;
    ListView list;

    public NCZ_todaygzExecute_Adapter(Context context, List<Today_job> listData, ExpandableListView mainlistview)
    {
        this.mainlistview = mainlistview;
        this.listData = listData;
        this.context = context;
    }

    //得到子item需要关联的数据
    @Override
    public Object getChild(int groupPosition, int childPosition)
    {
        if (listData.get(groupPosition).getJoblist() == null)
        {
            return null;
        }
        return listData.get(groupPosition).getJoblist().get(childPosition);
    }

    static class ListItemView
    {
        public TextView tv_jobname;
        public TextView tv_local;
        public TextView tv_tip_pf;
        public TextView tv_score;
        public TextView tv_importance;
        public CircleImageView circle_img;
        public SwipeLayout swipeLayout;
        public LinearLayout ll_menu;
        public Button btn_connect;
        public TextView tv_new_item;
        public TextView tv_time;
        public TextView tv_type;
        public TextView tv_zf;
        public FrameLayout fl_new;
        public FrameLayout fl_new_item;
        public TextView tv_new;
        public ImageView iv_record;
    }
    //得到子item的ID
    @Override
    public long getChildId(int groupPosition, int childPosition)
    {
        return childPosition;
    }
    HashMap<Integer, HashMap<Integer, View>> lmap = new HashMap<Integer, HashMap<Integer, View>>();
    HashMap<Integer, View> map = new HashMap<>();
    ListItemView listItemView = null;


    //设置子item的组件
    @Override
    public View getChildView( int groupPosition,  int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
    {

        List<jobtab> childData = listData.get(groupPosition).getJoblist();
        final jobtab jobtab1 = childData.get(childPosition);

        View v = null;
        if (lmap.get(groupPosition) != null)
        {
            HashMap<Integer, View> map1 = lmap.get(groupPosition);
            v = lmap.get(groupPosition).get(childPosition);
        }
        if (v == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.ncz_pc_todayjobadater, null);//ncz_pc_todayjobadater-ncz_pc_todayjobadater
            listItemView = new ListItemView();

            // 获取控件对象
            listItemView.tv_local = (TextView) convertView.findViewById(R.id.tv_local);
            listItemView.fl_new_item = (FrameLayout) convertView.findViewById(R.id.fl_new_item);
            listItemView.fl_new = (FrameLayout) convertView.findViewById(R.id.fl_new);
            listItemView.tv_new = (TextView) convertView.findViewById(R.id.tv_new);
            listItemView.tv_jobname = (TextView) convertView.findViewById(R.id.tv_jobname);

            listItemView.iv_record = (ImageView) convertView.findViewById(R.id.iv_record);
            listItemView.tv_tip_pf = (TextView) convertView.findViewById(R.id.tv_tip_pf);
            listItemView.tv_score = (TextView) convertView.findViewById(R.id.tv_score);
            listItemView.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            listItemView.tv_type = (TextView) convertView.findViewById(R.id.tv_type);
            listItemView.tv_new_item = (TextView) convertView.findViewById(R.id.tv_new_item);
            listItemView.tv_zf = (TextView) convertView.findViewById(R.id.tv_zf);
            listItemView.btn_connect = (Button) convertView.findViewById(R.id.btn_connect);
            listItemView.tv_importance = (TextView) convertView.findViewById(R.id.tv_importance);
            listItemView.circle_img = (CircleImageView) convertView.findViewById(R.id.circle_img);
            listItemView.swipeLayout = (SwipeLayout) convertView.findViewById(R.id.swipe);
            listItemView.ll_menu = (LinearLayout) convertView.findViewById(R.id.ll_menu);
            convertView.setTag(listItemView);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent intent = new Intent(context, Common_JobDetail_.class);
//                    Intent intent = new Intent(context, Common_JobDetail_Assess_.class);
                    Intent intent = new Intent(context, NCZ_Todayjob_Common_.class);
                    intent.putExtra("bean", jobtab1);//
                    context.startActivity(intent);

                }
            });
            map.put(childPosition, convertView);
            lmap.put(groupPosition, map);
            if (isLastChild)
            {
                map = new HashMap<>();
            }
//

            // 当隐藏的删除menu被打开的时候的回调函数
            listItemView.swipeLayout.addSwipeListener(new SimpleSwipeListener() {
                @Override
                public void onOpen(SwipeLayout layout) {
//                    Toast.makeText(context, "Open", Toast.LENGTH_SHORT).show();
                }
            });
            // 双击的回调函数
            listItemView.swipeLayout.setOnDoubleClickListener(new SwipeLayout.DoubleClickListener() {
                @Override
                public void onDoubleClick(SwipeLayout layout, boolean surface) {
//                    Toast.makeText(context, "DoubleClick", Toast.LENGTH_SHORT).show();
                }
            });
            // 添加删除布局的点击事件    下发，自发
            listItemView.ll_menu.setId(childPosition);
            listItemView.ll_menu.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
//                    Toast.makeText(context, "delete", Toast.LENGTH_SHORT).show();
                    // 点击完成之后，关闭删除menu
                    listItemView.swipeLayout.close();

                    Intent intent = new Intent(context, Common_CommandDetail_Show_.class);
                    intent.putExtra("cmdid", jobtab1.getcommandID());
                    context.startActivity(intent);
                }
            });
            listItemView.iv_record.setId(childPosition);
            listItemView.iv_record.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    commembertab commembertab = AppContext.getUserInfo(context);
                    AppContext.updateStatus(context, "1", jobtab1.getId(), "1", commembertab.getId());
                    Intent intent = new Intent(context, RecordList_.class);
                    intent.putExtra("type", "1");
                    intent.putExtra("workid", jobtab1.getId());
                    context.startActivity(intent);
                }
            });

            //下发
            listItemView.btn_connect.setId(childPosition);
            listItemView.btn_connect.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {

                    Intent intent = new Intent(context, Common_CommandDetail_Show_.class);
                    intent.putExtra("cmdid", jobtab1.getcommandID());
                    context.startActivity(intent);
                }
            });

            if (jobtab1.getstdJobType().equals("0") || jobtab1.getstdJobType().equals("-1"))
            {
                if (jobtab1.getjobNote().equals(""))
                {
                    listItemView.tv_jobname.setText("暂无说明");
                } else
                {
                    listItemView.tv_jobname.setText(jobtab1.getjobNote());
                }
            } else
            {
                listItemView.tv_jobname.setText(jobtab1.getstdJobTypeName() + "-" + jobtab1.getstdJobName());
            }

            if (Integer.valueOf(jobtab1.getJobCount()) > 0)
            {
                listItemView.fl_new_item.setVisibility(View.VISIBLE);
                listItemView.tv_new_item.setText(jobtab1.getJobCount());
            } else
            {
                listItemView.fl_new_item.setVisibility(View.GONE);
            }
            if (Integer.valueOf(jobtab1.getJobvidioCount()) > 0)
            {
                listItemView.fl_new.setVisibility(View.VISIBLE);
                listItemView.tv_new.setText(jobtab1.getJobvidioCount());
            } else
            {
                listItemView.fl_new.setVisibility(View.GONE);
            }
            if (jobtab1.getImportance().equals("0"))
            {
                listItemView.tv_importance.setText("一般");
//            listItemView.circle_img.setImageResource(R.color.bg_blue);
                listItemView.circle_img.setImageResource(R.drawable.yb);
            } else if (jobtab1.getImportance().equals("1"))
            {
                listItemView.tv_importance.setText("重要");
//            listItemView.circle_img.setImageResource(R.color.bg_green);
                listItemView.circle_img.setImageResource(R.drawable.zyx);
            } else if (jobtab1.getImportance().equals("2"))
            {
                listItemView.tv_importance.setText("非常重要");
//            listItemView.circle_img.setImageResource(R.color.color_orange);
                listItemView.circle_img.setImageResource(R.drawable.fczy);
            } else if (jobtab1.getImportance().equals("3"))
            {
                listItemView.tv_importance.setText("自");
                listItemView.circle_img.setImageResource(R.color.bg_job);
            }
            if (jobtab1.getFrom().equals("0"))
            {
                listItemView.tv_zf.setText( "下发");
            } else
            {
                listItemView.tv_zf.setText("自发");
            }
            if (jobtab1.getstdJobType().equals("0"))
            {
                listItemView.tv_type.setText("非标准生产指令");
            } else  if (jobtab1.getstdJobType().equals("-1"))
            {
                listItemView.tv_type.setText("非生产指令");
            } else
            {
                listItemView.tv_type.setText("标准生产指令");
            }
            listItemView.tv_time.setText(jobtab1.getregDate().substring(0, jobtab1.getregDate().length()-8));
            if (jobtab1.getaudioJobExecPath().equals("-1"))
            {
                listItemView.tv_score.setText("暂无");
            } else
            {
                listItemView.tv_score.setTextColor(context.getResources().getColor(R.color.red));
                if (jobtab1.getaudioJobExecPath().equals("0"))
                {
                    listItemView.tv_score.setText("0分");
                } else if (jobtab1.getaudioJobExecPath().equals("8"))
                {
                    listItemView.tv_score.setText("8分");
                } else if (jobtab1.getaudioJobExecPath().equals("10"))
                {
                    listItemView.tv_score.setText("10分");
                }
            }

 //
            //数据添加
//            listItemView.tv_jobname.setText(jobtab1.getstdJobTypeName());
            listItemView.tv_local.setText(jobtab1.getparkName()+"-"+jobtab1.getareaName());

            //
        } else
        {
            convertView = lmap.get(groupPosition).get(childPosition);
            listItemView = (ListItemView) convertView.getTag();
        }
        //数据添加  都可以数据加载，不过在上面比较好，这里是返回view
        return convertView;
    }


    @Override
    public void onGroupExpanded(int groupPosition)
    {
        // mExpListView 是列表实例，通过判断它的状态，关闭已经展开的。
        super.onGroupExpanded(groupPosition);

    }

    @Override
    public void onGroupCollapsed(int groupPosition)
    {
        super.onGroupCollapsed(groupPosition);

    }

    //获取当前父item下的子item的个数
    @Override
    public int getChildrenCount(int groupPosition)
    {
        if (listData.get(groupPosition).getJoblist() == null)
        {
            return 0;
        }
        return listData.get(groupPosition).getJoblist().size();
    }

    //获取当前父item的数据
    @Override
    public Object getGroup(int groupPosition)
    {
        return listData.get(groupPosition);
    }

    @Override
    public int getGroupCount()
    {
        return listData.size();
    }
    @Override
    public long getGroupId(int groupPosition)
    {
        return groupPosition;
    }

    //设置父item组件
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.ncz_todayjobperent, null);
        }
        TextView regisdate = (TextView) convertView.findViewById(R.id.regisdate);
        TextView tv_gznum = (TextView) convertView.findViewById(R.id.tv_gznum);

        String dates=listData.get(groupPosition).getRegisdate().substring(0,listData.get(groupPosition).getRegisdate().length()-8);
//        regisdate.setText(listData.get(groupPosition).getRegisdate());
        regisdate.setText(dates);
//        tv_gznum.setText(listData.get(groupPosition).getJoblist().size());
        String gznum=listData.get(groupPosition).getJoblist().size()+"条";
        tv_gznum.setText(gznum);
        return convertView;
    }
    @Override
    public boolean hasStableIds()
    {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition)
    {
        return true;
    }
}
