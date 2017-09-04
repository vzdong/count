package com.example.administrator.count;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        //save("pwd","123");
        String havepwd = read("pwd");
        if (havepwd.length()>0) {

            Intent intent = new Intent(this, PassWordActivity.class);
            startActivity(intent);
        }else{
            Intent intent = new Intent(this, ContentActivity.class);
            startActivity(intent);
           // Toast.makeText(MainActivity.this, "提示的内容", Toast.LENGTH_LONG).show();

        }
        finish();
    }
    //定义一个保存数据的方法
    public void save(String key, String value) {
        SharedPreferences sp = getSharedPreferences("mima", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.commit();

    }

    //定义一个读取SP文件的方法
    public String read(String key) {
        String data;
        SharedPreferences sp = getSharedPreferences("mima", Context.MODE_PRIVATE);
        data=sp.getString(key, "");

        return data;
    }
}
