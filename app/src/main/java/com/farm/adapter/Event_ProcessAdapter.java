package com.farm.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.farm.R;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.FJxx;
import com.farm.bean.HandleBean;
import com.farm.bean.RecordBean;
import com.farm.bean.commembertab;
import com.farm.common.BitmapHelper;
import com.farm.common.MediaHelper;
import com.farm.ui.DisplayImage_;
import com.farm.widget.CircleImageView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by user on 2016/5/12.
 */
public class Event_ProcessAdapter extends BaseAdapter {
    private Context context;
    private List<HandleBean> listItems;
    private LayoutInflater listContainer;
    HandleBean handleBean;
    ListItemView listItemView = null;
    List<FJxx> fJxx;

    class ListItemView {
        public LinearLayout rl_left;
        public LinearLayout rl_right;
        public CircleImageView iv_img_left;
        public CircleImageView iv_img_right;
        public ImageView iv_record_right;
        public ImageView iv_record_left;
        public ProgressBar pb_upload_left;
        public ProgressBar pb_upload_right;
        public TextView tv_name_right;
        public TextView tv_name_left;
        public TextView tv_time_right;
        public TextView tv_time_left;
        public TextView tv_txt_right;
        public TextView tv_txt_left;
    }

    public Event_ProcessAdapter(Context context, List<HandleBean> data) {
        this.context = context;
        this.listContainer = LayoutInflater.from(context);
        this.listItems = data;
    }

    HashMap<Integer, View> lmap = new HashMap<Integer, View>();

    public View getView(int position, View convertView, ViewGroup parent) {
        handleBean = listItems.get(position);

        if (lmap.get(position) == null) {
            convertView = listContainer.inflate(R.layout.event_item, null);
            listItemView = new ListItemView();
            listItemView.tv_txt_left = (TextView) convertView.findViewById(R.id.tv_txt_left);
            listItemView.tv_txt_right = (TextView) convertView.findViewById(R.id.tv_txt_right);
            listItemView.tv_time_left = (TextView) convertView.findViewById(R.id.tv_time_left);
            listItemView.tv_time_right = (TextView) convertView.findViewById(R.id.tv_time_right);
            listItemView.tv_name_left = (TextView) convertView.findViewById(R.id.tv_name_left);
            listItemView.tv_name_right = (TextView) convertView.findViewById(R.id.tv_name_right);
            listItemView.pb_upload_right = (ProgressBar) convertView.findViewById(R.id.pb_upload_right);
            listItemView.pb_upload_left = (ProgressBar) convertView.findViewById(R.id.pb_upload_left);
            listItemView.rl_left = (LinearLayout) convertView.findViewById(R.id.rl_left);
            listItemView.rl_right = (LinearLayout) convertView.findViewById(R.id.rl_right);
            listItemView.iv_img_left = (CircleImageView) convertView.findViewById(R.id.iv_img_left);
            listItemView.iv_img_right = (CircleImageView) convertView.findViewById(R.id.iv_img_right);
            listItemView.iv_record_right = (ImageView) convertView.findViewById(R.id.iv_record_right);
            listItemView.iv_record_left = (ImageView) convertView.findViewById(R.id.iv_record_left);
            listItemView.iv_record_right.setId(position);
            listItemView.iv_record_left.setId(position);
/*            listItemView.iv_record_left.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.setVisibility(View.GONE);
                    ProgressBar pb = ((ListItemView) (lmap.get(v.getId()).getTag())).pb_upload_left;
                    pb.setVisibility(View.VISIBLE);
                }
            });
            listItemView.iv_record_right.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.setVisibility(View.GONE);
                    ProgressBar pb = ((ListItemView) (lmap.get(v.getId()).getTag())).pb_upload_left;
                    pb.setVisibility(View.VISIBLE);
                }
            });*/

            lmap.put(position, convertView);
            convertView.setTag(listItemView);
        } else {
            convertView = lmap.get(position);
            listItemView = (ListItemView) convertView.getTag();
        }
        commembertab commembertab = AppContext.getUserInfo(context);
        if (handleBean.getSolveId().equals(commembertab.getId())) {
            listItemView.rl_right.setVisibility(View.VISIBLE);
            listItemView.tv_time_right.setText(handleBean.getRegistime().substring(0, handleBean.getRegistime().length() - 8));
            listItemView.tv_name_right.setText(handleBean.getSolveName());
            String aa = handleBean.getResult();
            if (!handleBean.getResult().equals("")) {//如果有文字
                listItemView.iv_record_right.setVisibility(View.GONE);
                listItemView.tv_txt_right.setVisibility(View.VISIBLE);
                listItemView.tv_txt_right.setText(handleBean.getResult());
            } else {//图片

                fJxx = new ArrayList<FJxx>();
                fJxx = handleBean.getFjxx();
                if (!fJxx.equals("") && fJxx.size() > 0) {

                    listItemView.iv_record_right.setTag(R.id.tag_rightpicture, listItemView.pb_upload_left);
                    listItemView.iv_record_right.setTag(R.id.tag_rightpicture, fJxx.get(0));
                    if (fJxx.get(0).getFJLX().equals("1")) {
                        listItemView.iv_record_right.setLayoutParams(new LinearLayout.LayoutParams(400, 400));
                        BitmapHelper.setImageView(context, listItemView.iv_record_right, AppConfig.baseurl + fJxx.get(0).getLSTLJ());
                    } else {
                        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) listItemView.iv_record_right.getLayoutParams();
                        lp.width = 150;
                        lp.height = LinearLayout.LayoutParams.WRAP_CONTENT;
                        listItemView.iv_record_right.setLayoutParams(lp);


                    }
                }

                listItemView.iv_record_right.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FJxx fjxxa = (FJxx) view.getTag(R.id.tag_rightpicture);
                        String aa = fjxxa.getLSTLJ();
                        String filename = aa.substring(aa.lastIndexOf("/"), aa.length());
                        String bb = aa;
                        if (fjxxa.getFJLX().equals("1")) {
                            Intent intent = new Intent(context, DisplayImage_.class);
                            intent.putExtra("url", AppConfig.baseurl + fjxxa.getLSTLJ());
                            context.startActivity(intent);
                        } else {
                            downloadLuYin(AppConfig.baseurl + fjxxa.getFJLJ(), AppConfig.audiopath +  fjxxa.getLSTLJ());
                        }
                    }
                });

            }

        } else {
            listItemView.rl_left.setVisibility(View.VISIBLE);

            listItemView.tv_time_left.setText(handleBean.getRegistime().substring(0, handleBean.getRegistime().length() - 8));
            listItemView.tv_name_left.setText(handleBean.getSolveName());
            if (!handleBean.getResult().equals("")) {
                listItemView.iv_record_left.setVisibility(View.GONE);
                listItemView.tv_txt_left.setVisibility(View.VISIBLE);
                listItemView.tv_txt_left.setText(handleBean.getResult());
            } else {
                fJxx = new ArrayList<FJxx>();
                fJxx = handleBean.getFjxx();
                if (!fJxx.equals("") && fJxx.size() > 0) {
                    listItemView.iv_record_left.setTag(R.id.tag_leftpicture, fJxx.get(0));
                    if (fJxx.get(0).getFJLX().equals("1")) {
                        listItemView.iv_record_left.setLayoutParams(new LinearLayout.LayoutParams(400, 400));
                        BitmapHelper.setImageView(context, listItemView.iv_record_left, AppConfig.baseurl + fJxx.get(0).getLSTLJ());
                    } else {

                    }
                }


                listItemView.iv_record_left.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FJxx fjxxa = (FJxx) view.getTag(R.id.tag_leftpicture);
                        Intent intent = new Intent(context, DisplayImage_.class);
//                        intent.putExtra("url", AppConfig.baseurl +fJxx.get(0).getLSTLJ());
                        intent.putExtra("url", AppConfig.baseurl + fjxxa.getLSTLJ());
                        context.startActivity(intent);
                    }
                });
            }

        }

        return convertView;
    }

    @Override
    public int getCount() {
        return listItems.size();
    }

    @Override
    public Object getItem(int arg0) {
        return listItems.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }


    public void downloadLuYin( String path, final String target) {
//    public void downloadLuYin(final ProgressBar pb, final View v, String path, final String target) {
        HttpUtils http = new HttpUtils();
        http.download(path, target, true, true, new RequestCallBack<File>() {
            @Override
            public void onFailure(HttpException error, String msg) {
                if (msg.equals("maybe the file has downloaded completely")) {
//                    v.setVisibility(View.VISIBLE);
//                    pb.setVisibility(View.GONE);
                    MediaHelper.playAudio(context, target);

                } else {
//                    pb.setVisibility(View.GONE);
//                    v.setVisibility(View.VISIBLE);
                    Toast.makeText(context, "语音可能已被删除!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onSuccess(ResponseInfo<File> responseInfo) {
//                v.setVisibility(View.VISIBLE);
//                pb.setVisibility(View.GONE);
                MediaHelper.playAudio(context, target);
            }
        });

    }
}