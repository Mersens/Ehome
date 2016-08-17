package com.zzu.ehome.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.zzu.ehome.R;
import com.zzu.ehome.fragment.DoctorListFragment;
import com.zzu.ehome.utils.ToastUtils;
import com.zzu.ehome.view.HeadView;

/**
 * Created by Mersens on 2016/8/15.
 */
public class PMDActivity extends BaseActivity {
    private RelativeLayout layout_all_yiyuan, layout_all_zhicheng, layout_all_manbing;
    private ImageView img_arrow_yy, img_arrow_zc, img_arrow_mb;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.layout_pmd);
        initViews();
        initDatas();
        initEvent();
    }

    public void initViews() {
        layout_all_yiyuan = (RelativeLayout) findViewById(R.id.layout_all_yiyuan);
        layout_all_zhicheng = (RelativeLayout) findViewById(R.id.layout_all_zhicheng);
        layout_all_manbing = (RelativeLayout) findViewById(R.id.layout_all_manbing);
        img_arrow_yy = (ImageView) findViewById(R.id.img_arrow_yy);
        img_arrow_zc = (ImageView) findViewById(R.id.img_arrow_zc);
        img_arrow_mb = (ImageView) findViewById(R.id.img_arrow_mb);
        setLeftWithTitleViewMethod(R.mipmap.icon_arrow_left, "私人医生", new HeadView.OnLeftClickListener() {
            @Override
            public void onClick() {
                finishActivity();
            }
        });
    }

    public void initEvent() {
        layout_all_yiyuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showMessage(PMDActivity.this,"全部医院");

            }
        });
        layout_all_zhicheng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showMessage(PMDActivity.this,"全部职称");
            }
        });
        layout_all_manbing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showMessage(PMDActivity.this,"全部慢病");
            }
        });
    }

    public void initDatas() {
        addFragment(Type.HOSPTIAL);

    }

    public enum Type{
        HOSPTIAL,POSITIONAL_TITLES,DISEASE;
    }

    public void addFragment(Type type){
        FragmentManager fm=getSupportFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        Fragment fragment=fm.findFragmentById(R.id.fragment_container);
        if(fragment==null){
            fragment=creatFragment(type);
        }
        if(fragment.isAdded()){
            ft.show(fragment);
        }else{
            ft.add(R.id.fragment_container,fragment);
        }
        ft.commit();
    }

    public Fragment creatFragment(Type type){
        Fragment mFragment=null;
        switch (type){
            case HOSPTIAL:
                mFragment= DoctorListFragment.getInstance();

                break;
            case POSITIONAL_TITLES:
                mFragment= DoctorListFragment.getInstance();
                break;
            case DISEASE:
                mFragment= DoctorListFragment.getInstance();
                break;
        }
        return mFragment;
    }


}
