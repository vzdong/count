package com.example.administrator.count;

/*
*存取SharedPreferences里的数据
 */
import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/8/18.
 */
       //mContext = getApplicationContext();
  //      sh = new SharedHelper(mContext);
public class PWDset {
    private Context mContext;
    public PWDset() {
    }

    public PWDset(Context mContext) {
        this.mContext = mContext;
    }

    //定义一个保存数据的方法
    public void save(String key, String value) {
        SharedPreferences sp = mContext.getSharedPreferences("mima", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.commit();

    }

    //定义一个读取SP文件的方法
    public String read(String key) {
       String data;
        SharedPreferences sp = mContext.getSharedPreferences("mima", Context.MODE_PRIVATE);
        data=sp.getString(key,"");

        return data;
    }
}
