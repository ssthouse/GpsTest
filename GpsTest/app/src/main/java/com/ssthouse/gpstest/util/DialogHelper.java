package com.ssthouse.gpstest.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.ssthouse.gpstest.R;
import com.ssthouse.gpstest.activity.PrjEditActivity;
import com.ssthouse.gpstest.adapter.PrjLvAdapter;
import com.ssthouse.gpstest.model.DBHelper;
import com.ssthouse.gpstest.model.PrjItem;

/**
 * Dialog的工具类
 * Created by ssthouse on 2015/7/18.
 */
public class DialogHelper {

    /**
     * 显示新工程名输入的Dialog
     * @param context
     * @param adapter
     */
    public static void showPrjNameDialog(final Context context, final PrjLvAdapter adapter) {
        LinearLayout llPrjName = (LinearLayout) LayoutInflater.from(context).
                inflate(R.layout.dialog_prj_name, null);
        final EditText etPrjName = (EditText) llPrjName.findViewById(R.id.id_et);


        final NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder.getInstance(context);

        View.OnClickListener cancelListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBuilder.dismiss();
            }
        };

        View.OnClickListener confirmListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String prjName = etPrjName.getText().toString();
                if (prjName.equals("")) {
                    ToastHelper.show(context, "工程名不可为空");
                } else {
                    if (DBHelper.isPrjExist(prjName)) {
                        ToastHelper.show(context, "该工程已存在");
                    } else {
                        //将新的prjItem保存进数据库
                        new PrjItem(prjName).save();
                        //重新加载工程视图
                        adapter.notifyDataSetChanged();
                        //消除Dialog
                        dialogBuilder.dismiss();
                        //Toast 提醒成功
                        ToastHelper.show(context, "工程创建成功!");
                    }
                }
            }
        };
        dialogBuilder.withTitle("工程名")             //.withTitle(null)  no title
                .withTitleColor("#FFFFFF")
                .withDividerColor("#11000000")
                .withMessage(null)//.withMessage(null)  no Msg
                .withMessageColor("#FFFFFFFF")
                .withDialogColor(context.getResources().getColor(R.color.dialog_color))
                .withEffect(Effectstype.Slidetop)       //def Effectstype.Slidetop
                .setCustomView(llPrjName, context)
                .withButton1Text("确认")                 //def gone
                .withButton2Text("取消")                 //def gone
                .isCancelableOnTouchOutside(false)
                .setButton1Click(confirmListener)
                .setButton2Click(cancelListener)
                .show();
    }


    /**
     * 长按显示的Menu的Dialog
     * @param context
     * @param prjItem
     * @param adapter
     */
    public static void showLvLongClickDialog(final Context context, final PrjItem prjItem, final PrjLvAdapter adapter){
        //build出dialog
        final NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder.getInstance(context);
        //inflate出View---配置点击事件
        LinearLayout ll = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.menu_lv_item, null);
        ll.findViewById(R.id.id_menu_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBuilder.dismiss();
                //删除数据库中的数据---prjItem
                prjItem.delete();
                //刷新视图
                adapter.notifyDataSetChanged();
            }
        });
        ll.findViewById(R.id.id_menu_edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBuilder.dismiss();
                //开启编辑PrjItem的Activity
                PrjEditActivity.start(context, prjItem);
            }
        });
        ll.findViewById(R.id.id_menu_rename).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBuilder.dismiss();
                //开启重命名的Dialog
                showChangeNameDialog(context, prjItem);
            }
        });
        dialogBuilder.withTitle(null)             //.withTitle(null)  no title
                .withTitleColor("#FFFFFF")
                .withDividerColor("#11000000")
                .withMessage(null)//.withMessage(null)  no Msg
                .withMessageColor("#FFFFFFFF")
                .withDialogColor(context.getResources().getColor(R.color.dialog_color))
                .withEffect(Effectstype.Slidetop)       //def Effectstype.Slidetop
                .setCustomView(ll, context)
                .isCancelableOnTouchOutside(false)
                .isCancelableOnTouchOutside(true)       //可以点击外面取消
                .show();
    }


    /**
     * 重命名的Dialog
     * @param context
     * @param prjItem
     */
    public static void showChangeNameDialog(final Context context, final PrjItem prjItem) {
        String formername = prjItem.getPrjName();
        //导出View
        LinearLayout llPrjName = (LinearLayout) LayoutInflater.from(context).
                inflate(R.layout.dialog_prj_name, null);
        final EditText etPrjName = (EditText) llPrjName.findViewById(R.id.id_et);
        //导出Dialog
        final NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder.getInstance(context);
        //创建监听事件
        View.OnClickListener cancelListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBuilder.dismiss();
            }
        };
        View.OnClickListener confirmListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String prjName = etPrjName.getText().toString();
                if (prjName.equals("")) {
                    ToastHelper.show(context, "工程名不可为空");
                } else {
                    if (DBHelper.isPrjExist(prjName)) {
                        ToastHelper.show(context, "该工程已存在");
                    } else {
                        //更新数据库中的prjItem
                        prjItem.setPrjName(prjName);
                        prjItem.save();
                        //消除Dialog
                        dialogBuilder.dismiss();
                        //Toast 提醒成功
                        ToastHelper.show(context, "重命名成功!");
                    }
                }
            }
        };
        dialogBuilder.withTitle("工程名")             //.withTitle(null)  no title
                .withTitleColor("#FFFFFF")
                .withDividerColor("#11000000")
                .withMessage(null)//.withMessage(null)  no Msg
                .withMessageColor("#FFFFFFFF")
                .withDialogColor(context.getResources().getColor(R.color.dialog_color))
                .withEffect(Effectstype.Slidetop)       //def Effectstype.Slidetop
                .setCustomView(llPrjName, context)
                .withButton1Text("确认")                 //def gone
                .withButton2Text("取消")                 //def gone
                .isCancelableOnTouchOutside(false)
                .setButton1Click(confirmListener)
                .setButton2Click(cancelListener)
                .show();
    }
}
