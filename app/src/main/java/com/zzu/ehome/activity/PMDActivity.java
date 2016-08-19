package com.zzu.ehome.activity;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zzu.ehome.R;
import com.zzu.ehome.adapter.BaseListAdapter;
import com.zzu.ehome.fragment.DoctorListFragment;
import com.zzu.ehome.view.HeadView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mersens on 2016/8/15.
 */
public class PMDActivity extends BaseActivity {
    private RelativeLayout layout_all_yiyuan, layout_all_zhicheng, layout_all_manbing;
    private ImageView img_arrow_yy, img_arrow_zc, img_arrow_mb;
    private PopupWindow pop;

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
                showPop(Type.HOSPTIAL);

            }
        });
        layout_all_zhicheng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPop(Type.POSITIONAL_TITLES);
            }
        });
        layout_all_manbing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPop(Type.DISEASE);
            }
        });
    }

    public void initDatas() {
        addFragment(Type.HOSPTIAL);

    }

    public enum Type {
        HOSPTIAL, POSITIONAL_TITLES, DISEASE;
    }

    public void showPop(Type t) {
        View mView = LayoutInflater.from(PMDActivity.this).inflate(R.layout.pop_listview, null);
        ListView listView = (ListView) mView.findViewById(R.id.listView);
        List<String> mList = getListDatas(t);
        listView.setAdapter(new MyAdapter(PMDActivity.this, mList));
        pop = new PopupWindow(mView);
        pop.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        pop.setHeight(WindowManager.LayoutParams.MATCH_PARENT);
        pop.setTouchable(true);
        pop.setFocusable(true);
        pop.setOutsideTouchable(true);
        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                pop=null;
            }
        });
        pop.setBackgroundDrawable(new BitmapDrawable());
        switch (t) {
            case HOSPTIAL:
                pop.showAsDropDown(layout_all_yiyuan);
                break;
            case POSITIONAL_TITLES:
                pop.showAsDropDown(layout_all_zhicheng);
                break;

            case DISEASE:
                pop.showAsDropDown(layout_all_manbing);
                break;
        }


    }
    public void popDismiss(){
        if(pop!=null && pop.isShowing()){
            pop.dismiss();
            pop=null;
        }

    }



    class MyAdapter extends BaseListAdapter<String> {
        private List<String> mList;

        public MyAdapter(Context context, List objects) {
            super(context, objects);
            this.mList = objects;
        }

        @Override
        public View getGqView(int position, View convertView, ViewGroup parent) {
            View mView=getInflater().inflate(R.layout.pop_item,null);
            TextView tv_name=(TextView)mView.findViewById(R.id.tv_name);
            tv_name.setText(mList.get(position));
            return mView;
        }
    }


    public void addFragment(Type type) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        if (fragment == null) {
            fragment = creatFragment(type);
        }
        if (fragment.isAdded()) {
            ft.show(fragment);
        } else {
            ft.add(R.id.fragment_container, fragment);
        }
        ft.commit();
    }

    public Fragment creatFragment(Type type) {
        Fragment mFragment = null;
        switch (type) {
            case HOSPTIAL:
                mFragment = DoctorListFragment.getInstance();

                break;
            case POSITIONAL_TITLES:
                mFragment = DoctorListFragment.getInstance();
                break;
            case DISEASE:
                mFragment = DoctorListFragment.getInstance();
                break;
        }
        return mFragment;
    }


    private static List getListDatas(Type t) {
        List list = new ArrayList();
        switch (t) {
            case HOSPTIAL:
                list.add("郑州大学第一附属医院");
                list.add("河南省人民医院");
                list.add("河南中医学院第一附属医院");
                list.add("郑州大学第二附属医院");

                break;
            case POSITIONAL_TITLES:
                list.add("知名专家");
                list.add("主任医师");
                list.add("副主任医师");
                list.add("医师");
                break;

            case DISEASE:
                list.add("心血管");
                list.add("糖尿病");
                list.add("高血压");
                list.add("呼吸疾病");
                break;
        }

        return list;

    }

}
