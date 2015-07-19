package com.ssthouse.gpstest.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.io.File;

/**
 * 处理图片大小的工具类
 * Created by ssthouse on 2015/7/18.
 */
public class PictureHelper {

    /**
     * 显示当前图库的所有图片---按文件名顺序
     * @param context
     * @param path
     */
    public static void showPictureInAlbum(Context context, String path) {
        //使用Intent
        Intent intent = new Intent(Intent.ACTION_VIEW);
        //Uri mUri = Uri.parse("file://" + picFile.getPath());
        //TODO--Android3.0以后最好不要通过该方法，存在一些小Bug
        intent.setDataAndType(Uri.fromFile(new File(path)), "image/*");
        context.startActivity(intent);
    }


}
