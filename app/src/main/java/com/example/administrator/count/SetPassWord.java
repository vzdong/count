package com.example.administrator.count;

import android.app.AlertDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Set;

public class SetPassWord extends AppCompatActivity {
    private DialogForAll  dialogAll=new DialogForAll(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_pass_word);
        PWDset pwd=new PWDset(this);
        EditText pwd3=(EditText)findViewById(R.id.pwd3);
        if(!(pwd.read("pwd").length()>0)){
            //如果没设置密码，则隐藏旧密码输入框
            pwd3.setVisibility(View.INVISIBLE);
            //pwd3.setEnabled(false);
        }else{
            //pwd3.setEnabled(true);
            pwd3.setVisibility(View.VISIBLE);
        }
    }
    public void close(View v){
        finish();
    }
    //设置密码
    public void set(View v){
        PWDset pwd=new PWDset(this);
        EditText pwd1=(EditText)findViewById(R.id.pwd1);
        EditText pwd2=(EditText)findViewById(R.id.pwd2);
        EditText pwd3=(EditText)findViewById(R.id.pwd3);
        if(!(pwd.read("pwd").length()>0)){
            //旧密码不存在的情况
          if(pwd1.getText().toString().equals(pwd2.getText().toString())){
              pwd.save("pwd",pwd1.getText().toString());
             // dialogAll("密码设置成功！");
              Toast.makeText(SetPassWord.this, "密码设置成功！", Toast.LENGTH_LONG).show();
              finish();
          }else{
              dialogAll.print("两次密码输入不一致！");

          }
        }else{//旧密码存在的情况
            if (pwd.read("pwd").equals(pwd3.getText().toString())){
                if(pwd1.getText().toString().equals(pwd2.getText().toString())){
                    pwd.save("pwd",pwd1.getText().toString());
                    if(pwd1.getText().toString().length()>0){
                    //dialogAll("设置新密码成功！");
                        Toast.makeText(SetPassWord.this, "设置新密码成功！", Toast.LENGTH_LONG).show();
                        finish();
                    }else{
                        //dialogAll("密码清除成功");
                        Toast.makeText(SetPassWord.this, "密码清除成功！", Toast.LENGTH_LONG).show();
                        finish();
                    }
                }else{
                    dialogAll.print("两次密码输入不一致！");
            }
        }else{
                dialogAll.print("旧密码错误！");
            }

    }}


}
