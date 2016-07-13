package com.farm.adapter;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.farm.R;
import com.farm.app.AppConfig;
import com.farm.app.AppContext;
import com.farm.bean.Result;
import com.farm.bean.commandtab;
import com.farm.common.BitmapHelper;
import com.farm.ui.PictureScrollFragment_DialogFragment;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.http.client.entity.FileUploadEntity;
import com.media.MediaChooser;
import com.media.MediaChooserConstants;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Adapter_PGAssessContract extends BaseAdapter
{
    ImageView currentimageView;
    Uri fileUri;
    private Activity context;// 运行上下文
    private List<com.farm.bean.commandtab> listItems;// 数据集合
    private LayoutInflater listContainer;// 视图容器
    commandtab commandtab;

    class ListItemView
    {
        public ImageView iv;
        public Button btn_warn;
        public Button btn_pass;
        public Button btn_fail;
        public Button btn_camara;
        public TextView tv_history;
    }

    public Adapter_PGAssessContract(Activity context, List<commandtab> data)
    {
        this.context = context;
        this.listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
        this.listItems = data;
        IntentFilter imageIntentFilter_yz = new IntentFilter(MediaChooser.IMAGE_SELECTED_ACTION_FROM_MEDIA_CHOOSER);
        context.registerReceiver(imageBroadcastReceiver, imageIntentFilter_yz);
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
        commandtab = listItems.get(position);
        // 自定义视图
        ListItemView listItemView = null;
        if (lmap.get(position) == null)
        {
            // 获取list_item布局文件的视图
            convertView = listContainer.inflate(R.layout.adapter_pgassesscontract, null);
            listItemView = new ListItemView();
            // 获取控件对象
            listItemView.iv = (ImageView) convertView.findViewById(R.id.iv);
            listItemView.btn_warn = (Button) convertView.findViewById(R.id.btn_warn);
            listItemView.btn_fail = (Button) convertView.findViewById(R.id.btn_fail);
            listItemView.btn_pass = (Button) convertView.findViewById(R.id.btn_pass);
            listItemView.btn_camara = (Button) convertView.findViewById(R.id.btn_camara);
            listItemView.tv_history = (TextView) convertView.findViewById(R.id.tv_history);
            listItemView.btn_camara.setTag(R.id.tag_postion, position);
            listItemView.btn_camara.setTag(R.id.tag_view, listItemView.iv);
            listItemView.btn_warn.setTag(position);
            listItemView.btn_fail.setTag(position);
            listItemView.btn_pass.setTag(position);
            listItemView.tv_history.setTag(position);
            listItemView.iv.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Integer pos = (Integer) v.getTag();
                    List<String> urls = (List<String>) v.getTag();
                    PictureScrollFragment_DialogFragment dialog = new PictureScrollFragment_DialogFragment();
                    Bundle bundle1 = new Bundle();
                    bundle1.putStringArrayList("imgurl", (ArrayList<String>) urls);
                    dialog.setArguments(bundle1);
                    dialog.show(context.getFragmentManager(), "EditNameDialog");
                }
            });
            listItemView.btn_warn.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Integer pos = (Integer) v.getTag();
                    //1进行网络提交 2提交完毕刷新当前页面
                }
            });
            listItemView.btn_fail.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Integer pos = (Integer) v.getTag();
                    //1进行网络提交 2提交完毕刷新当前页面
                }
            });
            listItemView.btn_pass.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Integer pos = (Integer) v.getTag();
                    //1进行网络提交 2提交完毕刷新当前页面
                }
            });
            listItemView.btn_camara.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Integer pos = (Integer) v.getTag(R.id.tag_postion);
                    currentimageView = (ImageView) v.getTag(R.id.tag_view);
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    fileUri = getOutputMediaFileUri(MediaChooserConstants.MEDIA_TYPE_IMAGE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                    context.startActivityForResult(intent, MediaChooserConstants.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
                }
            });
            listItemView.tv_history.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Integer pos = (Integer) v.getTag();
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
//        listItemView.tv_time.setText(commandtab.getregDate());
        return convertView;
    }

    BroadcastReceiver imageBroadcastReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(final Context context, Intent intent)
        {
            String FJBDLJ = fileUri.toString().replaceFirst("file:///", "/").trim();
            BitmapHelper.setImageView(context, currentimageView, FJBDLJ);
            //1进行网络提交 2提交完毕刷新当前页面


//            Bundle bundle = new Bundle();
//            bundle.putInt("index_ll", currentItem);
//            bundle.putInt("index_imageview", current_ll_picture.getChildCount() + 1);
//            imageView.setTag(bundle);

        }
    };

    private Uri getOutputMediaFileUri(int type)
    {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /**
     * Create a File for saving an image or video
     */
    private static File getOutputMediaFile(int type)
    {

        File mediaStorageDir = new File(AppConfig.MEDIA_PATH);
        if (!mediaStorageDir.exists())
        {
            if (!mediaStorageDir.mkdirs())
            {
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile = null;
        if (type == MediaChooserConstants.MEDIA_TYPE_IMAGE)
        {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
        } else if (type == MediaChooserConstants.MEDIA_TYPE_VIDEO)
        {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "VID_" + timeStamp + ".mp4");
        } else if (type == MediaChooserConstants.MEDIA_TYPE_LUYIN)
        {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "AUD_" + timeStamp + ".mp3");
        }
        return mediaFile;
    }

    private void uploadMedia(String plantgrowthId, String path, String plantId)
    {
        File file = new File(path);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("action", "UpLoadFilePlantImg");
        params.addQueryStringParameter("plantgrowthId", plantgrowthId);
        params.addQueryStringParameter("plantId", plantId);
        params.addQueryStringParameter("file", file.getName());
        params.setBodyEntity(new FileUploadEntity(file, "text/html"));
        HttpUtils http = new HttpUtils();
        http.configTimeout(60000);
        http.configSoTimeout(60000);
        http.send(HttpRequest.HttpMethod.POST, AppConfig.uploadurl, params, new RequestCallBack<String>()
        {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String a = responseInfo.result;
                Result result = JSON.parseObject(responseInfo.result, Result.class);
                if (result.getResultCode() == 1)// -1出错；0结果集数量为0；结果列表
                {
                    if (result.getAffectedRows() != 0)
                    {
//                        showProgress();
                    } else
                    {
                        AppContext.makeToast(context, "error_connectDataBase");
                    }
                } else
                {
                    AppContext.makeToast(context, "error_connectDataBase");
                    return;
                }

            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                String a = error.getMessage();
                AppContext.makeToast(context, "error_connectServer");
            }
        });
    }

}