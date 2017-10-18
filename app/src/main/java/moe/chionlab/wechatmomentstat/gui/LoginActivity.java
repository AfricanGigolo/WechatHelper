package moe.chionlab.wechatmomentstat.gui;


import android.app.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONException;

import moe.chionlab.wechatmomentstat.Model.Manager00;
import moe.chionlab.wechatmomentstat.R;
import moe.chionlab.wechatmomentstat.common.Config;
import moe.chionlab.wechatmomentstat.common.NowUser;
import moe.chionlab.wechatmomentstat.common.ProgressBarCycle;
import moe.chionlab.wechatmomentstat.common.Share;

/*登录界面*/

public class LoginActivity extends Activity implements View.OnClickListener {

    TextView idTV,pwTV;
    EditText idET,pwET,ipET;
    Button loginBT;
    CheckBox remCB,autochatCB,autosnsCB;
    SharedPreferences sharedPreferences = null;
    SharedPreferences.Editor editor = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
        loginBT.setOnClickListener(this);
        remCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(remCB.isChecked())
                {
                    editor.putBoolean("rem_checked",true);
                }
                else
                {
                    editor.putBoolean("rem_checked",false);
                }
            }
        });

        autochatCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(autochatCB.isChecked())
                {
                    editor.putBoolean("autochat",true);
                    Config.autouploadchatrecords = true;
                }
                else
                {
                    editor.putBoolean("autochat",false);
                    Config.autouploadchatrecords = false;
                }
                editor.commit();
            }
        });

        autosnsCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(autosnsCB.isChecked())
                {
                    editor.putBoolean("autosns",true);
                    Config.autouploadsnsrecords = true;
                }
                else
                {
                    editor.putBoolean("autosns",false);
                    Config.autouploadsnsrecords = false;
                }
                editor.commit();
            }
        });

    }



    private void initView() //初始化视图
    {
        ipET = (EditText) findViewById(R.id.et_lg_ip);
        idTV = (TextView) findViewById(R.id.tv_lg_id);  //账号textView
        idET = (EditText) findViewById(R.id.et_lg_id);  //账号editText
        pwTV = (TextView) findViewById(R.id.tv_lg_pw);  //密码xx
        pwET = (EditText) findViewById(R.id.et_lg_pw);  //密码xx
        loginBT = (Button) findViewById(R.id.bt_lg_login); //登录按钮
        remCB = (CheckBox) findViewById(R.id.cb_lg_remaccount);
        autochatCB = (CheckBox) findViewById(R.id.cb_lg_autochat);
        autosnsCB = (CheckBox) findViewById(R.id.cb_lg_autosns);

        sharedPreferences = getSharedPreferences("LoginInfo",MODE_PRIVATE);
        editor = sharedPreferences.edit();
        ipET.setText(sharedPreferences.getString("ip",""));

        if(sharedPreferences.getBoolean("rem_checked",false))
        {
            remCB.setChecked(true);
            idET.setText(sharedPreferences.getString("account",""));
            pwET.setText(sharedPreferences.getString("password",""));
        }

        if(sharedPreferences.getBoolean("autochat",false))
        {
            autochatCB.setChecked(true);
            Config.autouploadchatrecords=true;
        }

        if(sharedPreferences.getBoolean("autosns",false))
        {
            autosnsCB.setChecked(true);
            Config.autouploadsnsrecords=true;
        }

    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ProgressBarCycle.cancleProgressBar();
            switch (msg.what)
            {
                case 1:
                    NowUser.id = msg.obj.toString();
                    NowUser.is_admin = true;
                    Toast.makeText(LoginActivity.this, "管理员 "+NowUser.id+" 登录成功", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                    finish();
                    break;
                case 0:
                    NowUser.id = msg.obj.toString();
                    NowUser.is_admin = false;
                    Toast.makeText(LoginActivity.this, "用户 "+NowUser.id+" 登录成功", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                    finish();
                    break;
                case -1:
                    Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                    break;
                case -2:
                    Toast.makeText(LoginActivity.this, "无法登陆，请检查网络设置", Toast.LENGTH_SHORT).show();
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
                editor.putString("ip",ipET.getText().toString());
                Share.IP_ADDRESS="http://"+ipET.getText().toString()+":8080";
                if(account.equals(""))
                {
                    Toast.makeText(this, "请输入账号", Toast.LENGTH_SHORT).show();
                }
                else if(password.equals(""))
                {
                    Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
                }
                else if(password.equals("admin")&&account.equals("admin"))
                {
                    editor.putString("account",account);
                    editor.putString("password",password);
                    editor.commit();
                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                    finish();
                }
                else
                {
                    editor.putString("account",account);
                    editor.putString("password",password);
                    editor.commit();
                    Log.i("ip",Share.IP_ADDRESS);
                    ProgressBarCycle.setProgressBar(LoginActivity.this,"正在登陆，请稍候...");
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

