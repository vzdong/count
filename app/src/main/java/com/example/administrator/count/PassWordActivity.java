package com.example.administrator.count;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

public class PassWordActivity extends AppCompatActivity {
    private DialogForAll  dialogAll=new DialogForAll(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_word);
        EditText et=(EditText)findViewById(R.id.pwd);
        //监听密码输入，如果正确自动登入
        et.addTextChangedListener(mTextWatcher);

    }
    TextWatcher mTextWatcher = new TextWatcher() {

        private CharSequence temp;
        private int editStart ;
        private int editEnd ;
        @Override
        public void beforeTextChanged(CharSequence s, int arg1, int arg2, int arg3) {

        }

        @Override
        public void onTextChanged(CharSequence s, int arg1, int arg2,
                                  int arg3) {

        }

        @Override
        public void afterTextChanged(Editable s) {
          check();
        }
    };
    public void check(){
        EditText setpwd=(EditText)findViewById(R.id.pwd);
        PWDset pwd=new PWDset(this);
        if(setpwd.getText().toString().equals(pwd.read("pwd"))){

            Intent intent = new Intent(this, ContentActivity.class);
            startActivity(intent);
            pwd.save("havepass","1");
            finish();

        }
    }
public void checkPWD(View v){
    EditText setpwd=(EditText)findViewById(R.id.pwd);
    PWDset pwd=new PWDset(this);
    if(setpwd.getText().toString().equals(pwd.read("pwd"))){

        Intent intent = new Intent(this, ContentActivity.class);
        startActivity(intent);
        finish();
        pwd.save("havepass","1");
    }else {
        dialogAll.print("密码输入错误！");
    }

}
    public void close(View v){

        Intent MyIntent = new Intent(Intent.ACTION_MAIN);
        MyIntent.addCategory(Intent.CATEGORY_HOME);
        startActivity(MyIntent);
        finish();

    }

}
