package moe.chionlab.wechatmomentstat.gui;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import moe.chionlab.wechatmomentstat.R;

/**
 * Created by chenjunfan on 2017/2/20.
 */

public class Fm3AddActivity extends Activity implements View.OnClickListener {
    ImageButton reBT;
    Button selectallBT,selectnonBT,yesBT,cancleBT;
    ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fm3add);
        initView();

    }

    private void initView()
    {
        reBT = (ImageButton) findViewById(R.id.bt_fm3add_re);
        selectallBT = (Button) findViewById(R.id.bt_fm3add_selectall);
        selectnonBT = (Button) findViewById(R.id.bt_fm3add_selectnon);
        yesBT = (Button) findViewById(R.id.bt_fm3add_yes);
        cancleBT = (Button) findViewById(R.id.bt_fm3add_cancle);
        listView = (ListView) findViewById(R.id.lv_fm3add);

        reBT.setOnClickListener(this);
        selectnonBT.setOnClickListener(this);
        selectallBT.setOnClickListener(this);
        yesBT.setOnClickListener(this);
        cancleBT.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.bt_fm3add_re:
                finish();
                break;
            case R.id.bt_fm3add_selectall:
                break;
            case R.id.bt_fm3add_selectnon:
                break;
            case R.id.bt_fm3add_yes:
                break;
            case R.id.bt_fm3add_cancle:
                finish();
                break;
        }
    }
}
