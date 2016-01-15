package com.farm.adapter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.farm.R;
import com.farm.bean.FJ_SCFJ;
import com.farm.bean.goodslisttab;
import com.farm.bean.goodslisttab_flsl;
import com.farm.common.BitmapHelper;
import com.farm.widget.MyDialog;
import com.media.HomeFragmentActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 */
public class EdiGoodsNumberAdapter extends BaseAdapter
{
    LinearLayout ll_picture_onclick;
    int index_ll = 0;
    int index_imageview = 0;
    List<FJ_SCFJ> list_FJ_SCFJ = new ArrayList<>();
    HashMap<Integer, View> lmap = new HashMap<Integer, View>();
    int currentItem = 0;
    LinearLayout current_ll_picture;
    MyDialog myDialog;
    ListItemView listItemView = null;
    String audiopath;
    private Context context;// 运行上下文
    private List<goodslisttab> listItems;// 数据集合
    private List<goodslisttab_flsl> list_goodslisttab_flsl;// 数据集合
    private LayoutInflater listContainer;// 视图容器
    goodslisttab goodslisttab;

    static class ListItemView
    { // 自定义控件集合

        public TextView tv_plantname;
        public EditText et_zg;
        public EditText et_wj;
        public EditText et_ys;
        public EditText et_lys;
        public CheckBox cb_sfyz;
        public CheckBox cb_sfly;
        public CheckBox cb_sfcl;
        public ImageButton imgbtn_addpicture;
        public LinearLayout ll_picture;

    }

    /**
     * 实例化Adapter
     *
     * @param context
     * @param data
     * @param context
     */
    public EdiGoodsNumberAdapter(Context context, List<goodslisttab> data)
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
        goodslisttab = listItems.get(position);
        if (lmap.get(position) == null)
        {
            convertView = listContainer.inflate(R.layout.editgoodsnumberadapter, null);
            listItemView = new ListItemView();
            listItemView.tv_plantname = (TextView) convertView.findViewById(R.id.tv_plantname);
            listItemView.et_zg = (EditText) convertView.findViewById(R.id.et_zg);
            listItemView.et_wj = (EditText) convertView.findViewById(R.id.et_wj);
            listItemView.et_ys = (EditText) convertView.findViewById(R.id.et_ys);
            listItemView.et_lys = (EditText) convertView.findViewById(R.id.et_lys);
            listItemView.cb_sfly = (CheckBox) convertView.findViewById(R.id.cb_sfly);
            listItemView.cb_sfyz = (CheckBox) convertView.findViewById(R.id.cb_sfyz);
            listItemView.cb_sfcl = (CheckBox) convertView.findViewById(R.id.cb_sfcl);
            listItemView.imgbtn_addpicture = (ImageButton) convertView.findViewById(R.id.imgbtn_addpicture);
            listItemView.ll_picture = (LinearLayout) convertView.findViewById(R.id.ll_picture);

            listItemView.imgbtn_addpicture.setTag(position);
            listItemView.imgbtn_addpicture.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    currentItem = Integer.valueOf(v.getTag().toString());
                    current_ll_picture = ((ListItemView) (lmap.get(currentItem).getTag())).ll_picture;
                    Intent intent = new Intent(context, HomeFragmentActivity.class);
                    intent.putExtra("type", "picture");
                    intent.putExtra("From", "plant");
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
        return convertView;
    }

    public List<FJ_SCFJ> getFJ_SCFJList()
    {
        return list_FJ_SCFJ;
    }

    public List<goodslisttab_flsl> getPlanttabList()
    {
        list_goodslisttab_flsl = new ArrayList<>();
        for (int i = 0; i < lmap.size(); i++)
        {
            goodslisttab_flsl  goodslisttab_flsl  = new goodslisttab_flsl ();
            EditText et_zg = ((ListItemView) (lmap.get(i).getTag())).et_zg;
            EditText et_wj = ((ListItemView) (lmap.get(i).getTag())).et_wj;
            EditText et_ys = ((ListItemView) (lmap.get(i).getTag())).et_ys;
            EditText et_lys = ((ListItemView) (lmap.get(i).getTag())).et_lys;
            CheckBox cb_sfly = ((ListItemView) (lmap.get(i).getTag())).cb_sfly;
            CheckBox cb_sfcl = ((ListItemView) (lmap.get(i).getTag())).cb_sfcl;
            CheckBox cb_sfyz = ((ListItemView) (lmap.get(i).getTag())).cb_sfyz;

//            plantgrowthtab.setplantId(listItems.get(i).getId());
//            plantgrowthtab.setplantName(listItems.get(i).getplantName());
//
//            plantgrowthtab.sethNum(et_zg.getText().toString());
//            plantgrowthtab.setyNum(et_ys.getText().toString());
//            plantgrowthtab.setwNum(et_wj.getText().toString());
//            plantgrowthtab.setxNum(et_lys.getText().toString());

            list_goodslisttab_flsl.add(goodslisttab_flsl );
        }
        return list_goodslisttab_flsl;
    }

    BroadcastReceiver imageBroadcastReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(final Context context, Intent intent)
        {
            List<String> list = intent.getStringArrayListExtra("list");
            for (int i = 0; i < list.size(); i++)
            {
                String FJBDLJ = list.get(i);
                ImageView imageView = new ImageView(context);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(180, ViewGroup.LayoutParams.MATCH_PARENT, 0);
                lp.setMargins(25, 4, 0, 4);
                imageView.setLayoutParams(lp);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                BitmapHelper.setImageView(context, imageView, FJBDLJ);
                Bundle bundle = new Bundle();
                bundle.putInt("index_ll", currentItem);
                bundle.putInt("index_imageview", current_ll_picture.getChildCount() + 1);
                imageView.setTag(bundle);

                FJ_SCFJ fj_SCFJ = new FJ_SCFJ();
                fj_SCFJ.setFJBDLJ(FJBDLJ);
                fj_SCFJ.setFJLX("1");
                fj_SCFJ.setFJID(listItems.get(currentItem).getId());
                fj_SCFJ.setGLID(currentItem + "-" + (current_ll_picture.getChildCount() + 1));

                list_FJ_SCFJ.add(fj_SCFJ);
                current_ll_picture.addView(imageView);

                imageView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Bundle bundle = (Bundle) v.getTag();
                        index_ll = bundle.getInt("index_ll");
                        index_imageview = bundle.getInt("index_imageview");
                        ll_picture_onclick = ((ListItemView) (lmap.get(index_ll).getTag())).ll_picture;
                        final int index_zp = ll_picture_onclick.indexOfChild(v);
                        View dialog_layout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.customdialog_callback, null);
                        myDialog = new MyDialog(context, R.style.MyDialog, dialog_layout, "图片", "查看该图片?", "查看", "删除", new MyDialog.CustomDialogListener()
                        {
                            @Override
                            public void OnClick(View v)
                            {
                                switch (v.getId())
                                {
                                    case R.id.btn_sure:
                                        for (int i = 0; i < list_FJ_SCFJ.size(); i++)
                                        {
                                            if (list_FJ_SCFJ.get(i).getGLID().equals(index_ll + "-" + index_imageview))
                                            {
                                                File file = new File(list_FJ_SCFJ.get(i).getFJBDLJ());
                                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                                intent.setDataAndType(Uri.fromFile(file), "image/*");
                                                context.startActivity(intent);
                                            }
                                        }


                                        break;
                                    case R.id.btn_cancle:
                                        ll_picture_onclick.removeViewAt(index_zp);
                                        for (int i = 0; i < list_FJ_SCFJ.size(); i++)
                                        {
                                            if (list_FJ_SCFJ.get(i).getGLID().equals(index_ll + "-" + index_imageview))
                                            {
                                                list_FJ_SCFJ.remove(index_zp);
                                            }
                                        }

                                        myDialog.dismiss();
                                        break;
                                }
                            }
                        });
                        myDialog.show();
                    }
                });
            }
        }
    };
}