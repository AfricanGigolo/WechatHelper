package com.example.chenjunfan.wechathelper;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/*登录界面*/

public class LoginActivity extends Activity implements View.OnClickListener {

    TextView idTV,pwTV;
    EditText idET,pwET;
    Button loginBT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
        loginBT.setOnClickListener(this);

    }

    private void initView() //初始化视图
    {
        idTV = (TextView) findViewById(R.id.tv_lg_id);  //账号textView
        idET = (EditText) findViewById(R.id.et_lg_id);  //账号editText
        pwTV = (TextView) findViewById(R.id.tv_lg_pw);  //密码xx
        pwET = (EditText) findViewById(R.id.et_lg_pw);  //密码xx
        loginBT = (Button) findViewById(R.id.bt_lg_login); //登录按钮
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.bt_lg_login:
                //写登录逻辑
                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}

