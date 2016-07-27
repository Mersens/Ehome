package com.zzu.ehome.activity;


import android.os.Bundle;
import com.zzu.ehome.R;
import com.zzu.ehome.view.HeadView;

/**
 * Created by Administrator on 2016/6/17.
 */
public class InstitutionActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_institution);
        initViews();
        initEnent();
    }
    private void initViews() {
        setLeftWithTitleViewMethod(R.mipmap.icon_arrow_left,"体检机构", new HeadView.OnLeftClickListener() {
            @Override
            public void onClick() {
                finishActivity();

            }
        });


    }
    private void initEnent() {

    }
}
