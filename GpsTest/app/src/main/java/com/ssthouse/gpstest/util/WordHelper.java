package com.ssthouse.gpstest.util;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;

import com.ssthouse.gpstest.model.WordItem;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * 关于Word文件处理的Util
 * Created by ssthouse on 2015/7/15.
 */
public class WordHelper {
    private static final String TAG = "WordGenerateUtil";

    /**
     * 获取数据的映射表
     *
     * @param wordItem
     * @return
     */
    private static Map<String, String> getDataMap(WordItem wordItem) {
        Map<String, String> dataMap = new HashMap<>();
        return dataMap;
    }

    /**
     * 生成word文档的方法
     *
     * @param context
     * @param wordItem
     */
    public static void generateWordFile(Context context, WordItem wordItem) {
        Map<String, String> mapData = getDataMap(wordItem);
            //读取word模板
            Resources res = context.getResources();
//            InputStream in = res.openRawResource(R.raw.model);

//            XWPFDocument xwpfDocument = new XWPFDocument(in);
//
//            FileOutputStream fos = new FileOutputStream(FileHelper.getFileParentPath(context));
//
//            xwpfDocument.write(fos);
            //Toasts
            ToastHelper.show(context, "Word文件生成成功!");

    }



    /**
     * 将Word文件分享出去
     * @param context
     * @param wordItem
     */
    public static void sendWordFile(Context context, WordItem wordItem) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        //获取对应TourItem的文件的URL
        File file = new File(FileHelper.getFileParentPath(context) + wordItem.getItem1() +
                "-" + wordItem.getItem2()+".doc");
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        intent.setType("application/msword");
        //调用系统的----发送
        context.startActivity(intent);
    }

    private static final String projectName = "${projectName}";
    private static final String date = "${date}";
    private static final String tourNumber = "${tourNumber}";
    private static final String summary = "${summary}";
    private static final String observer = "${observer}";

}
