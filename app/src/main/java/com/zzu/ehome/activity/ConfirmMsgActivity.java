package com.zzu.ehome.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.zzu.ehome.R;
import com.zzu.ehome.view.HeadView;

/**
 * Created by Mersens on 2016/8/10.
 */
public class ConfirmMsgActivity extends BaseActivity {
    private Button btn_yuyue;
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.layout_confirm_msg);
        initViews();
        initEvent();
        initDatas();
    }


    public void initViews(){
        setLeftWithTitleViewMethod(R.mipmap.icon_arrow_left, "确认挂号信息", new HeadView.OnLeftClickListener() {
            @Override
            public void onClick() {
                finishActivity();

            }
        });
        btn_yuyue=(Button) findViewById(R.id.btn_yuyue);

    }


    public void initEvent(){
        btn_yuyue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ConfirmMsgActivity.this,YuYueSuccessActivity.class));

            }
        });

    }


    public void initDatas(){}

}
