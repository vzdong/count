package com.example.administrator.count;

import android.app.AlertDialog;
import android.content.Context;

/**
 * Created by Administrator on 2017/8/23.
 */

public class DialogForAll {
    private Context mContext;
    public DialogForAll(){
    }
    public DialogForAll(Context context){
        this.mContext=context;
    }
    //通用对话框
    public void print(String str){
        //Context mContext;
        AlertDialog alert = null;
        AlertDialog.Builder builder = null;
       // mContext = ContentActivity.this;
        //初始化Builder
        builder = new AlertDialog.Builder(this.mContext);
        builder.setMessage(str);
        builder.setPositiveButton("知道了",null);

        builder.setCancelable(true);
        alert = builder.create();
        alert.show();
    }
}
