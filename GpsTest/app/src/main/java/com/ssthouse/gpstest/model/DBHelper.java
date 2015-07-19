package com.ssthouse.gpstest.model;

import com.activeandroid.query.Select;

import java.util.List;

/**
 * 数据库处理的工具类
 * Created by ssthouse on 2015/7/17.
 */
public class DBHelper {


    /**
     * 判断工程是否在---数据库---中已存在
     *
     * @param prjName
     * @return
     */
    public static boolean isPrjExist(String prjName) {
        List<PrjItem> prjImteList = new Select()
                .from(PrjItem.class)
                .where("prjName = " + "'" + prjName + "'")
                .execute();
        if (prjImteList == null || prjImteList.size() == 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 按照创建的Id的顺序获取PrjItm的列表
     */
    public static List<PrjItem> getPrjItemList() {
        List<PrjItem> prjItemList = new Select()
                .from(PrjItem.class)
                .orderBy("Id ASC")
                .execute();
        return prjItemList;
    }
}
