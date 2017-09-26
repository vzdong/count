package com.example.administrator.count;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorTreeAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.administrator.count.R;

public class LogActivity extends AppCompatActivity {
    private DialogForAll  dialogAll=new DialogForAll(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        final ExpandableListView listView=(ExpandableListView)findViewById(R.id.expandableListView);
        //初始化expandablelistview列表
        refresh();
        //child长按事件
        AdapterView.OnItemLongClickListener onItemLongClickListener = new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,  int position, long id) {
                long packedPosition = listView.getExpandableListPosition(position);
                int groupPosition = ExpandableListView.getPackedPositionGroup(packedPosition);
                int childPosition = ExpandableListView.getPackedPositionChild(packedPosition);


                //长按的是group的时候，childPosition = -1
                if (childPosition != -1) {
                    TextView tv0=(TextView)view.findViewById(R.id.t0);
                    final String dealnumber=tv0.getText().toString();

                    TextView tv5=(TextView)view.findViewById(R.id.t5);
                    TextView tv2=(TextView)view.findViewById(R.id.t2);
                    TextView tv3=(TextView)view.findViewById(R.id.t3);
                    TextView tv4=(TextView)view.findViewById(R.id.t4);
                    String  tv5s=tv5.getText().toString();
                    String  tv2s=tv2.getText().toString();
                    String  tv3s=tv3.getText().toString();
                    String  tv4s=tv4.getText().toString();

                    Context context=LogActivity.this;
                    AlertDialog.Builder builder= new AlertDialog.Builder(context);
                    builder.setTitle("警告");
                    builder.setMessage("确定要删除该条记录吗？\n\n"+tv5s+"--"+tv2s+"--"+tv3s+"--"+tv4s);
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            delChild(dealnumber);

                        }
                    });
                    builder.setNegativeButton("取消",null);
                    builder.show();
                    return true;
                }else{
                    TextView tv2=(TextView)view.findViewById(R.id.t2);
                    TextView tv1=(TextView)view.findViewById(R.id.t1);
                    final String  tv1s=tv1.getText().toString();
                    String  tv2s=tv2.getText().toString();

                    Context context=LogActivity.this;
                    AlertDialog.Builder builder= new AlertDialog.Builder(context);
                    builder.setTitle("警告");
                    builder.setMessage("确定要删除该组全部数据吗？\n\n"+tv1s+"--"+tv2s);
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            delGroup(tv1s);

                        }
                    });
                    builder.setNegativeButton("取消",null);
                    builder.show();
                    return false;
                }

            }
        };
        listView.setOnItemLongClickListener(onItemLongClickListener);
    }
    //长按事件，删除子项条目
    public void delChild(String dealnumber){
        SQLiteDatabase db = openOrCreateDatabase("count.db", Context.MODE_PRIVATE, null);
        db.execSQL("delete from deal where dealnumber=?",new String[]{dealnumber});
        db.close();
        refresh();

    }
    //删除整组数据
    public void delGroup(String code){


        SQLiteDatabase db = openOrCreateDatabase("count.db", Context.MODE_PRIVATE, null);
        db.execSQL("delete from deal where code=?",new String[]{code});
        db.close();

        refresh();



    }
    //刷新列表
    public void refresh(){

        String[] fromParent=new String[]{"_id","name"};
        int[] toParent={R.id.t1,R.id.t2};
        String[] fromChild=new String[]{"_id","flag","price","number","time","dealnumber"};
        int[] toChild={R.id.t1,R.id.t2,R.id.t3,R.id.t4,R.id.t5,R.id.t0};
        ExpandableListAdapter mAdapter;
        ExpandableListView elistview=(ExpandableListView)findViewById(R.id.expandableListView);
        SQLiteDatabase db = openOrCreateDatabase("count.db", Context.MODE_PRIVATE, null);
        Cursor groupCursor=db.rawQuery("select code as _id,name,sum(price*number)/sum(number) as avg_price,sum(number) as total " +
                "from log group by code,name",null);
        mAdapter = new LogActivity.MyExpandableListAdapter(groupCursor, this, R.layout.listview_parent, R.layout.listview,fromParent, toParent, fromChild,
                toChild);
        elistview.setAdapter(mAdapter);
        db.close();

    }
    public class MyExpandableListAdapter extends SimpleCursorTreeAdapter {
        private int mGroupIdColumnIndex;

        public MyExpandableListAdapter(Cursor cursor, Context context, int groupLayout,
                                       int childLayout, String[] groupFrom, int[] groupTo, String[] childrenFrom,
                                       int[] childrenTo) {
            super(context, cursor, groupLayout, groupFrom, groupTo, childLayout, childrenFrom, childrenTo);
            // mGroupIdColumnIndex = cursor.getColumnIndexOrThrow("_id");
        }

        //注意二级项数据在这里取得
        @Override
        protected Cursor getChildrenCursor(Cursor groupCursor) {

            // String code = groupCursor.getString(groupCursor.getColumnIndex("name"));
            String code = groupCursor.getString(mGroupIdColumnIndex);
            SQLiteDatabase db = openOrCreateDatabase("count.db", Context.MODE_PRIVATE, null);
            Cursor childCursor = db.rawQuery("select code as _id,flag,price,number,time,dealnumber from log where code=? " +
                    "order by price", new String[]{code});
            //db.close();
            return childCursor;


        }
    }
    public void back(View v){
        Intent intent=new Intent(this,ContentActivity.class);
        startActivity(intent);
        finish();
    }
    //暂停时保存数据
    @Override
    protected void onPause(){
        super.onPause();
        PWDset havepass=new PWDset(this);
        havepass.save("havepass","0");
    }
    //停止时保存数据
    @Override
    protected void onStop(){
        super.onStop();
        PWDset havepass=new PWDset(this);
        havepass.save("havepass","0");
    }
    //摧毁时保存数据
    @Override
    protected void onDestroy(){


        PWDset havepass=new PWDset(this);
        havepass.save("havepass","0");
        super.onDestroy();
    }
}
