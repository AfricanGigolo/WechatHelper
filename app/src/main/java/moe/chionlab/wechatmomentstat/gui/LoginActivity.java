package moe.chionlab.wechatmomentstat.gui;


import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONException;

import moe.chionlab.wechatmomentstat.Model.Manager00;
import moe.chionlab.wechatmomentstat.R;
import moe.chionlab.wechatmomentstat.common.NowUser;

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
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case 1:
                    NowUser.id = msg.obj.toString();
                    NowUser.is_admin = true;
                    Toast.makeText(LoginActivity.this, "管理员 "+NowUser.id+" 登录成功", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                    break;
                case 0:
                    NowUser.id = msg.obj.toString();
                    NowUser.is_admin = false;
                    Toast.makeText(LoginActivity.this, "用户 "+NowUser.id+" 登录成功", Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                    break;
            }
        }
    };

    Manager00 loginManager = new Manager00(LoginActivity.this,handler);


    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.bt_lg_login:
                //写登录逻辑
                final String account = idET.getText().toString();
                final String password = pwET.getText().toString();
                if(account.equals(""))
                {
                    Toast.makeText(this, "请输入账号", Toast.LENGTH_SHORT).show();
                }
                else if(password.equals(""))
                {
                    Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
                }
                else
                {

                   new Thread(new Runnable() {
                       @Override
                       public void run() {
                           try {
                               loginManager.upload(account,password);
                           } catch (JSONException e) {
                               e.printStackTrace();
                               Toast.makeText(LoginActivity.this, "登录错误", Toast.LENGTH_SHORT).show();
                           }
                       }
                   }).start();

                }
                break;

        }
    }
}

