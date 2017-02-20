package com.example.chenjunfan.wechathelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created by chenjunfan on 2017/2/20.
 */

public class NewkeywordActivity extends Activity implements View.OnClickListener {
    TextView titleTV;
    EditText keywordET,rightET;
    Button yesBT,cancleBT;
    ImageButton reBT;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newkeyword);
        initView();
    }

    private void initView()
    {
        titleTV = (TextView) findViewById(R.id.tv_nkw_title);
        keywordET = (EditText) findViewById(R.id.et_nkw_keyword);
        rightET = (EditText) findViewById(R.id.et_nkw_right);
        yesBT = (Button) findViewById(R.id.bt_nkw_yes);
        cancleBT = (Button) findViewById(R.id.bt_nkw_cancle);
        reBT = (ImageButton) findViewById(R.id.bt_nkw_re);

        yesBT.setOnClickListener(this);
        cancleBT.setOnClickListener(this);
        reBT.setOnClickListener(this);
        Intent intent = getIntent();
        titleTV.setText(intent.getStringExtra("title"));

    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.bt_nkw_re:
                finish();
                break;
            case R.id.bt_nkw_cancle:
                finish();
                break;
            case R.id.bt_nkw_yes:
                break;
        }
    }
}
