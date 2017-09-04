package com.example.administrator.count;

import android.app.AlertDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class feilv extends AppCompatActivity {
    private DialogForAll  dialogAll=new DialogForAll(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feilv);
        PWDset set=new PWDset(this);
        EditText jijinfei=(EditText)findViewById(R.id.jijinfei);
        EditText gupiaofei=(EditText)findViewById(R.id.gupiaofei);
        EditText yinhuashui=(EditText)findViewById(R.id.yinhuashui);
        if (!set.read("jijinfei").equals("")){
            jijinfei.setText(set.read("jijinfei"));
        }
        if (!set.read("gupiaofei").equals("")){
            gupiaofei.setText(set.read("gupiaofei"));
        }
        if (!set.read("yinhuashui").equals("")){
            yinhuashui.setText(set.read("yinhuashui"));
        }
    }
    public void setfei(View v){
        EditText jijinfei=(EditText)findViewById(R.id.jijinfei);
        EditText gupiaofei=(EditText)findViewById(R.id.gupiaofei);
        EditText yinhuashui=(EditText)findViewById(R.id.yinhuashui);
        PWDset set=new PWDset(this);
        set.save("jijinfei",jijinfei.getText().toString());
        set.save("gupiaofei",gupiaofei.getText().toString());
        set.save("yinhuashui",yinhuashui.getText().toString());
        dialogAll.print("设置费率成功！");
    }
    public void cancel(View v){
        finish();
    }

}
