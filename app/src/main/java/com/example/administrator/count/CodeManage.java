package com.example.administrator.count;

import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CodeManage extends AppCompatActivity {
    private DialogForAll  dialogAll=new DialogForAll(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_manage);
        //初始化选择框
        spinnerInit();
    }
    //增加代码
    public void add(View v){
        EditText code = (EditText) findViewById(R.id.code);
        EditText name = (EditText) findViewById(R.id.name);

        String code1 = code.getText().toString();
        String name1 = name.getText().toString();


        String[] sql = {code1, name1};

        if (code1.length() > 0 & name1.length() > 0) {

            SQLiteDatabase db = openOrCreateDatabase("count.db", Context.MODE_PRIVATE, null);
            Cursor cursor=db.rawQuery("SELECT distinct  code FROM gu where code=? order by code",new String[]{code1});
            if(cursor.moveToFirst()){
                dialogAll.print("该代码在数据库中已经存在！");
            }else{
            db.execSQL("INSERT INTO gu (code,name) values(?,?)", sql);
            db.close();

            dialogAll.print("成功增加！");
           spinnerInit();
            //清空输入框
            code.setText("");
            name.setText("");}

        } else{
            dialogAll.print("请检查输入是否正确！");
        }
    }
//删除代码
    public void del(View v){
        Spinner sp=(Spinner)findViewById(R.id.sp);
        ArrayList<String> list=new ArrayList<String>();
        ArrayAdapter<String> adapter=null;
        String str=sp.getSelectedItem().toString();
        String code=str.substring(0,str.indexOf("-"));
        if (code.length() > 0) {
            // Toast.makeText(CodeManage.this, code, Toast.LENGTH_SHORT).show();
            SQLiteDatabase db = openOrCreateDatabase("count.db", Context.MODE_PRIVATE, null);
            Cursor cursor=db.rawQuery("SELECT distinct  code FROM deal where code=? order by code",new String[]{code});
            if (cursor.moveToFirst()){
                dialogAll.print(str+"在表deal中还有数据！请先删除该代码下的所有数据！");
            }else {
                db.execSQL("delete from gu where code=?", new String[]{code});
                spinnerInit();
                db.close();
            }
        }else {
            dialogAll.print("没数据了！");
        }

    }
    public void cancel(View v){
        finish();
    }
    //初始化spinner-sp数据
    public  void spinnerInit(){
       Spinner sp=(Spinner) findViewById(R.id.sp);
        ArrayList<String> list=new ArrayList<String>();
        ArrayAdapter<String> adapter=null;
        String code;
        String name;
        SQLiteDatabase db = openOrCreateDatabase("count.db", Context.MODE_PRIVATE, null);
        Cursor cursor=db.rawQuery("SELECT distinct  code,name FROM gu order by code",null);
        while(cursor.moveToNext()){
            code= cursor.getString(cursor.getColumnIndex("code"));
            name= cursor.getString(cursor.getColumnIndex("name"));
            String str=code+"-"+name;
            list.add(str);
        }
        //把数组导入到ArrayList中
        adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,list);
        //设置下拉菜单的风格
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adapter);
        //最后关闭数据库
        db.close();
    }


}
