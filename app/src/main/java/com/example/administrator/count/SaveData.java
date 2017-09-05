package com.example.administrator.count;
/*
*备份数据库
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SaveData extends AppCompatActivity {
    private DialogForAll  dialogAll=new DialogForAll(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_data);
    }
public void  recoveryDB(View v){

    final View view_custom;
    Context mContext;
    AlertDialog alert = null;
    AlertDialog.Builder builder = null;
    mContext = SaveData.this;
    //初始化Builder
    builder = new AlertDialog.Builder(mContext);

    //加载自定义的那个View,同时设置下
    final LayoutInflater inflater = SaveData.this.getLayoutInflater();
    view_custom = inflater.inflate(R.layout.file_dialog, null,false);
    builder.setView(view_custom);
   // builder.setPositiveButton("确定", null);
    builder.setCancelable(true);
    builder.setNegativeButton("取消",null);
    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            Spinner sp=(Spinner) view_custom.findViewById(R.id.filespinner);
            String filename=sp.getSelectedItem().toString();
            //dialogAll.print(filename);
            try {
                doRecovery(filename);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    });
        alert = builder.create();
    alert.show();
    Spinner sp=(Spinner) view_custom.findViewById(R.id.filespinner);
    ArrayList<String> list=new ArrayList<String>();
    ArrayAdapter<String> adapter=null;
    String DATABASE_PATH ="/sdcard/1/DBbackup";
    File dir = new File(DATABASE_PATH);
    String[] a=dir.list();
    for(String b:a){
        if(b.indexOf("count")!=-1){
            list.add(b);
        }
    }
    int k=list.size()/2;
    for(int i=0;i<k;i++){
        String temp;
        temp=list.get(i);

        list.set(i,list.get((list.size()-1-i)));
        list.set((list.size()-1-i),temp);


    }

    //把数组导入到ArrayList中
    adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,list);
   //设置下拉菜单的风格
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    sp.setAdapter(adapter);
}
    //备份数据库
    public void backupDB(View v) throws IOException {
        String DATABASE_PATH = "/data/data/com.example.administrator.count/databases";
        String DATABASE_FILENAME = "count.db";
        String outputPath="/sdcard/1/DBbackup";

        Date date=new Date();
        SimpleDateFormat format=new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
        String time=format.format(date);
        String outputName="count"+time+".db";

        String outputAll=outputPath+"/"+outputName;
        //com.example.administrator.shuidian

//获取数据库路径
        String databaseFilename=getApplicationContext().getDatabasePath(DATABASE_FILENAME).getAbsolutePath();
       // String databaseFilename = DATABASE_PATH + "/" + DATABASE_FILENAME;
        File dir = new File(databaseFilename);


        //要备份的数据库文件是否存在
        if (dir.exists()) {
            File newPath=new File(outputPath);
            //备份到的路径是否存在
            if(!newPath.exists()){
                newPath.mkdirs();
            }
            //创建路径
            if(newPath.exists()) {
                InputStream is = new FileInputStream(databaseFilename);
                FileOutputStream fos = new FileOutputStream(outputAll);
                byte[] buffer = new byte[1024];
                int count = 0;
                while ((count = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                }
                fos.close();
                is.close();
                dialogAll.print("数据库成功备份到：1/DBbackup/" + outputName);
            }else{
                dialogAll.print("创建备份路径失败！");
            }


        }else{
            dialogAll.print("数据库文件不存在！");
        }

    }
    //恢复数据库
    public void doRecovery(String outputName) throws IOException {


        String DATABASE_PATH = "/data/data/com.example.administrator.count/databases";
        String DATABASE_FILENAME = "count.db";
        String outputPath="/sdcard/1/DBbackup";
       // String outputName="count01.db";
        String outputAll=outputPath+"/"+outputName;
        //com.example.administrator.shuidian

//获取数据库路径
        String databaseFilename=getApplicationContext().getDatabasePath(DATABASE_FILENAME).getAbsolutePath();
        //String databaseFilename = DATABASE_PATH + "/" + DATABASE_FILENAME;
        File dir = new File(databaseFilename);
        File newPath=new File(outputAll);

        if (dir.exists()&newPath.exists()) {


            InputStream is=new FileInputStream(outputAll);
            FileOutputStream fos = new FileOutputStream(databaseFilename);
            byte[] buffer = new byte[1024];
            int count = 0;
            while ((count = is.read(buffer)) > 0) {
                fos.write(buffer, 0, count);
            }
            fos.close();
            is.close();
            dialogAll.print("成功从：1/DBbackup/"+outputName+"恢复数据库count.db");


        }else{
            dialogAll.print("路径不存在，恢复失败！");
        }


    }


}
