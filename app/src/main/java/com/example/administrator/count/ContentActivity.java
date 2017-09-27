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

public class ContentActivity extends AppCompatActivity {
    Context context=ContentActivity.this;
    private DialogForAll  dialogAll=new DialogForAll(this);
    // String time1=null;
    private long time1=0;
    //基金的费率，默认单位万分之几
    private int jijinfei=0;
    //股票的费率，默认单位万分之几
    private int gupiaofei=0;
    //印花税，默认单位万分之几
    private int yinhuashui=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        //申请运行时读写权限
        int REQUEST_CODE_ASK_PERMISSIONS = 122;
        int hasWriteContactsPermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_CODE_ASK_PERMISSIONS);
        }

       //读取费率参数
        PWDset set=new PWDset(this);
        if (!set.read("jijinfei").equals("")){
            jijinfei= Integer.parseInt(set.read("jijinfei"));
        }
        if (!set.read("gupiaofei").equals("")){
            gupiaofei= Integer.parseInt(set.read("gupiaofei"));
        }
        if (!set.read("yinhuashui").equals("")){
            yinhuashui= Integer.parseInt(set.read("yinhuashui"));
        }


        final ExpandableListView listView=(ExpandableListView)findViewById(R.id.expandableListView);
        //ExpandableListView ListView=(ExpandableListView)findViewById(R.id.expandableListView) ;


        //初始化数据库，创建表gu
        SQLiteDatabase db = openOrCreateDatabase("count.db", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS gu (codenumber INTEGER PRIMARY KEY AUTOINCREMENT,code varchar(10),name varchar(10))");
        db.execSQL("CREATE TABLE IF NOT EXISTS deal (dealnumber INTEGER PRIMARY KEY AUTOINCREMENT,code varchar(10),name varchar(20)," +
                "price REAL,number integer,time varchar(20))");
        db.execSQL("CREATE TABLE IF NOT EXISTS log (dealnumber INTEGER PRIMARY KEY AUTOINCREMENT,code varchar(10),name varchar(20)," +
                "price REAL,number integer,time varchar(20),flag varchar(10))");
        db.close();

        //初始化expandablelistview列表
        refresh();
        //初始化下拉框
        spinnerInit();

        //添加child项点击事件,两次点击后，将child项数据加到输入框


        ExpandableListView.OnChildClickListener childListener = new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                Date date=new Date();
                //判断是不是第一点击
                if(time1!=0){
                    long time2=date.getTime();
                    long i=time2-time1;
                        if (i<1000){
                            TextView code1=(TextView)v.findViewById(R.id.t1);
                            TextView name1=(TextView)v.findViewById(R.id.t2);
                            TextView price1=(TextView)v.findViewById(R.id.t3);
                            TextView number1=(TextView)v.findViewById(R.id.t4);
                           // TextView dealnumber1=(TextView)v.findViewById(R.id.t0);

                           // EditText code=(EditText)findViewById(R.id.code) ;
                           // EditText name=(EditText)findViewById(R.id.name) ;
                            EditText price=(EditText)findViewById(R.id.price) ;
                            EditText number=(EditText)findViewById(R.id.number) ;
                            Spinner sp=(Spinner)findViewById(R.id.code);
                           // TextView dealnumber=(TextView)findViewById(R.id.dealnumber);

                           String code=code1.getText().toString();
                           String name=name1.getText().toString();
                            String str=code+"~~~~"+name;
                            setSpinnerItemSelectedByValue(sp,str);

                            price.setText(price1.getText().toString());
                            number.setText(number1.getText().toString());
                           // dealnumber.setText(dealnumber1.getText().toString());
                        }


                }
                    //不是第一次点击就记下第一次点击时间
                    time1= date.getTime();


                //Toast.makeText(ContentActivity.this,tv.getText().toString(),Toast.LENGTH_SHORT).show();
                return true;
            }
        };

        listView.setOnChildClickListener(childListener);

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

                    String str="确定要删除该条记录吗？\n\n"+tv5s+"--"+tv2s+"--"+tv3s+"--"+tv4s;
                    dialogShow(str,dealnumber,0,tv3s);
                    return true;
                }else{
                    TextView tv2=(TextView)view.findViewById(R.id.t2);
                    TextView tv1=(TextView)view.findViewById(R.id.t1);
                    TextView tv3=(TextView)view.findViewById(R.id.t3);
                    final String  tv1s=tv1.getText().toString();
                    String  tv2s=tv2.getText().toString();
                    String  tv3s=tv3.getText().toString();

                    String str="确定要删除该组全部数据吗？\n\n"+tv1s+"--"+tv2s;
                    dialogShow(str,tv1s,1,tv3s);
                    return false;
                }

            }
        };
        listView.setOnItemLongClickListener(onItemLongClickListener);

    }


    //长按事件，删除子项条目
    public void delChild(String dealnumber,String price) {
        if (price.length() > 0) {
            String sql = "insert into log(code,name,price,number,time,flag) " +
                    "select code,name,?,number,time,'卖出'  from deal  where dealnumber=?";
            SQLiteDatabase db = openOrCreateDatabase("count.db", Context.MODE_PRIVATE, null);
            db.execSQL(sql, new String[]{price, dealnumber});
            db.execSQL("delete from deal where dealnumber=?", new String[]{dealnumber});
            //String[] sql2 = {code1, name1, price1, number1, time,"买入"};
            //db.execSQL("INSERT INTO log (code,name,price,number,time,flag) values(?,?,?,?,?,?)", sql2);
            db.close();
            refresh();

        } else {
                dialogAll.print("请输入价格!");
        }
    }

    //删除整组数据
    public void delGroup(String code,String price){
        if (price.length() > 0) {
                String sql="insert into log(code,name,price,number,time,flag) " +
                "select code,name,?,number,time,'卖出'  from deal  where code=?";

                    SQLiteDatabase db = openOrCreateDatabase("count.db", Context.MODE_PRIVATE, null);
                    db.execSQL(sql,new String[]{price,code});
                    db.execSQL("delete from deal where code=?",new String[]{code});
                    db.close();

                    refresh();

        } else {
            dialogAll.print("请输入价格!");
        }

    }
    public void dialogShow(String str, final String tv1s, final int i,String price){
        final AlertDialog.Builder builder= new AlertDialog.Builder(context);
        //加载自定义的那个View,同时设置下
        final View view_custom;
        final LayoutInflater inflater = ContentActivity.this.getLayoutInflater();
        view_custom = inflater.inflate(R.layout.dialog_del_child_show, null,false);
        builder.setView(view_custom);

        builder.setTitle("警告");
       // builder.setMessage(str);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                EditText priceShow=(EditText)view_custom.findViewById(R.id.price);
                String price2=priceShow.getText().toString();
                if(i==1) {
                    delGroup(tv1s,price2);
                }else{
                    delChild(tv1s,price2);
                }

            }
        });
        builder.setNegativeButton("取消",null);
        builder.show();

        //设置对话框内容
        TextView showStr= (TextView) view_custom.findViewById(R.id.stringShow);
        showStr.setText(str);
        EditText priceShow=(EditText)view_custom.findViewById(R.id.price);
        priceShow.setText(price);
    }

    //重写onCreateOptionMenu(Menu menu)方法，当菜单第一次被加载时调用
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //填充选项菜单（读取XML文件、解析、加载到Menu组件上）
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    //重写OptionsItemSelected(MenuItem item)来响应菜单项(MenuItem)的点击事件（根据id来区分是哪个item）
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.password:

                Intent intent = new Intent(this,SetPassWord.class);
                startActivity(intent);
                break;
            case R.id.data:
                Intent intent2 = new Intent(this,SaveData.class);
                startActivity(intent2);
                break;
            case R.id.fei:
                Intent intent3 = new Intent(this,feilv.class);
                startActivity(intent3);
                break;
            case R.id.log:
                Intent intent4=new Intent(this,LogActivity.class);
                startActivity(intent4);
                break;


            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onResume(){
        super.onResume();
        PWDset havepass=new PWDset(this);
        if(!(havepass.read("havepass").equals("1"))&havepass.read("pwd").length()>0) {
            Intent intent = new Intent(this, PassWordActivity.class);
            startActivity(intent);
        }else {
           spinnerInit();
        }
        PWDset set=new PWDset(this);

        if (!set.read("jijinfei").equals("")){
            jijinfei= Integer.parseInt(set.read("jijinfei"));
        }
        if (!set.read("gupiaofei").equals("")){
            gupiaofei= Integer.parseInt(set.read("gupiaofei"));
        }
        if (!set.read("yinhuashui").equals("")){
            yinhuashui= Integer.parseInt(set.read("yinhuashui"));
        }
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





    //增加交易数据到数据库
    public void add(View v) {

        EditText price = (EditText) findViewById(R.id.price);
        EditText number = (EditText) findViewById(R.id.number);

         Spinner sp=(Spinner)findViewById(R.id.code);
        //检查输入是否完整
        if(sp.getSelectedItem()!=null&price.getText().length() > 0 & number.getText().length() > 0) {
            String spStr = sp.getSelectedItem().toString();
            String code1 = spStr.substring(0, spStr.indexOf("~"));
            String name1 = spStr.substring((spStr.indexOf("~") + 4), spStr.length());
            double temp = Double.parseDouble(price.getText().toString());
            DecimalFormat format = new DecimalFormat("#.0000");
            String price2 = format.format(temp);

            String lei=spStr.substring(0,1);
            //判断是股票还是基金
            if(lei.equals("0")|lei.equals("3")|lei.equals("6")) {
                //计算持仓成本
                temp =(temp * (1 + ( gupiaofei*2 + yinhuashui) * 0.0001));
            }else {
                temp=(temp * (1 + (jijinfei*2) * 0.0001));
            }

            //DecimalFormat format = new DecimalFormat("#.0000");
            String price1 = format.format(temp);

            String number1 = number.getText().toString();
            Date date = new Date();
            SimpleDateFormat df = new SimpleDateFormat("MM月dd日");
            String time = df.format(date);
            String[] sql = {code1, name1, price1, number1, time};
            String[] sql2 = {code1, name1, price2, number1, time,"买入"};

           // if ( price1.length() > 0 & number1.length() > 0) {
                SQLiteDatabase db = openOrCreateDatabase("count.db", Context.MODE_PRIVATE, null);
                db.execSQL("INSERT INTO deal (code,name,price,number,time) values(?,?,?,?,?)", sql);
                db.execSQL("INSERT INTO log (code,name,price,number,time,flag) values(?,?,?,?,?,?)", sql2);
                db.close();
                refresh();
                dialogAll.print("成功增加！");
                // spinnerInit();
                //清空输入框
                // code.setText("");
                // name.setText("");
                price.setText("");
                number.setText("");
                // dealnumber.setText("");
          //  } else {
              //  dialogAll("请检查输入是否正确！");
          //  }
        }else{
            dialogAll.print("请检查输入是否正确！");
        }

    }
    //清空输入框
    public void clear(View v){
        //EditText code = (EditText) findViewById(R.id.code);
      //  EditText name = (EditText) findViewById(R.id.name);
        EditText price = (EditText) findViewById(R.id.price);
        EditText number = (EditText) findViewById(R.id.number);
       // TextView dealnumber=(TextView)findViewById(R.id.dealnumber);
       // code.setText("");
      //  name.setText("");
        price.setText("");
        number.setText("");
       // dealnumber.setText("");
        }
    public void clear(){
       // EditText code = (EditText) findViewById(R.id.code);
      //  EditText name = (EditText) findViewById(R.id.name);
        EditText price = (EditText) findViewById(R.id.price);
        EditText number = (EditText) findViewById(R.id.number);
       // TextView dealnumber=(TextView)findViewById(R.id.dealnumber);
       // code.setText("");
      //  name.setText("");
        price.setText("");
        number.setText("");
       // dealnumber.setText("");
        }
    //跳到代码管理页
    public void codeMange(View v){
        Intent intent = new Intent(this, CodeManage.class);
        startActivity(intent);
    }



    //刷新列表
    public void refresh(){

        String[] fromParent=new String[]{"_id","name","avg_price","total"};
        int[] toParent={R.id.t1,R.id.t2,R.id.t3,R.id.t4};
        String[] fromChild=new String[]{"_id","name","price","number","time","dealnumber"};
        int[] toChild={R.id.t1,R.id.t2,R.id.t3,R.id.t4,R.id.t5,R.id.t0};
        ExpandableListAdapter mAdapter;
        ExpandableListView elistview=(ExpandableListView)findViewById(R.id.expandableListView);
        SQLiteDatabase db = openOrCreateDatabase("count.db", Context.MODE_PRIVATE, null);
        Cursor groupCursor=db.rawQuery("select code as _id,name,sum(price*number)/sum(number) as avg_price,sum(number) as total " +
                "from deal group by code,name ",null);
        mAdapter = new MyExpandableListAdapter(groupCursor, this, R.layout.listview_parent, R.layout.listview,fromParent, toParent, fromChild,
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
            Cursor childCursor=db.rawQuery("select code as _id,name,price,number,time,dealnumber from deal where code=? order by price",new String[]{code});
            //db.close();
            return  childCursor;


        }
    }





//暂时不用的
    //只列出spiner-sp选中的item的数据
    public void chaxun(View v){
       /* ListView listView=(ListView)findViewById(R.id.expandableListView) ;
        Spinner sp=(Spinner)findViewById(R.id.sp);
        String str=sp.getSelectedItem().toString();
        String[] from=new String[]{"_id","name","price","number","time"};
        int[] to={R.id.t1,R.id.t2,R.id.t3,R.id.t4,R.id.t5};
        SQLiteDatabase db = openOrCreateDatabase("count.db", Context.MODE_PRIVATE, null);
        Cursor cursor=db.rawQuery("SELECT code as _id ,name,price,number,time FROM deal where code=? order by code",new String[]{str});
        SimpleCursorAdapter adapter1 = new SimpleCursorAdapter(this, R.layout.listview,cursor,from, to,1);
        listView.setAdapter(adapter1);
        db.close();*/
    }
    //删除spiner-sp选中的全部子项
    public void del(View v){
       /* Spinner sp=(Spinner)findViewById(R.id.sp);
        String str=sp.getSelectedItem().toString();
        SQLiteDatabase db = openOrCreateDatabase("count.db", Context.MODE_PRIVATE, null);
        db.execSQL("delete  from deal where code=?",new String[]{str});
        db.close();
        refresh();
        spinnerInit();*/
    }
    //初始化spinner-sp数据
    public  void spinnerInit(){
        Spinner sp=(Spinner) findViewById(R.id.code);
        ArrayList<String> list=new ArrayList<String>();
        ArrayAdapter<String> adapter=null;
        String code;
        String name;
        SQLiteDatabase db = openOrCreateDatabase("count.db", Context.MODE_PRIVATE, null);
        Cursor cursor=db.rawQuery("SELECT distinct  code,name FROM gu order by code",null);
        while(cursor.moveToNext()){
            code= cursor.getString(cursor.getColumnIndex("code"));
            name= cursor.getString(cursor.getColumnIndex("name"));

            String str=code+"~~~~"+name;
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
    /**
     * 根据值, 设置spinner默认选中:
     * @param spinner
     * @param value
     */
    public  void setSpinnerItemSelectedByValue(Spinner spinner,String value){
        SpinnerAdapter apsAdapter= spinner.getAdapter(); //得到SpinnerAdapter对象
        int k= apsAdapter.getCount();
        for(int i=0;i<k;i++){
            if(value.equals(apsAdapter.getItem(i).toString())){
                spinner.setSelection(i,true);// 默认选中项
                break;
            }
        }
    }

}
