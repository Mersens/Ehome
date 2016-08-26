package com.zzu.ehome.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zzu.ehome.R;
import com.zzu.ehome.activity.LoginActivity1;
import com.zzu.ehome.application.Constants;
import com.zzu.ehome.bean.RelationDes;

/**
 * Created by Mersens on 2016/8/25.
 */
public class MyHomeFragment extends BaseFragment {
    private View mView;
    public static final String RES = "res";
    public static final int HASDATA = 0x11;
    public static final int NONEDATA = 0x10;
    private int t = 0;
    private Button btnadd;
    private ImageView iv_head;
    private TextView tv_rl, tv_phone;
    RelationDes res;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = null;
        res = (RelationDes) getArguments().getSerializable(RES);
        if (res != null) {
            t = res.getType();
        }
        if (t == NONEDATA) {
            v = inflater.inflate(R.layout.layout_add_parent, null);
        } else if (t == HASDATA) {
            v = inflater.inflate(R.layout.layout_already_parent, null);
        }
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mView = view;
        initViews();
    }

    public void initViews() {
        if (t == NONEDATA) {
            btnadd = (Button) mView.findViewById(R.id.btnadd);
            btnadd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getActivity(), LoginActivity1.class);
                    i.putExtra("relation", "rela");
                    startActivity(i);
                }
            });
        } else if (t == HASDATA) {
            iv_head = (ImageView) mView.findViewById(R.id.iv_head);
            tv_rl = (TextView) mView.findViewById(R.id.tv_rl);
            tv_phone = (TextView) mView.findViewById(R.id.tv_phone);
            if (res != null) {
                if (TextUtils.isEmpty(res.getUser_Icon().replace("~", "").replace("\\", "/"))) {
                    iv_head.setBackgroundResource(R.mipmap.pic_photo2);
                } else {
                    Glide.with(getActivity()).load(Constants.JE_BASE_URL3 + res.getUser_Icon().replace("~", "").replace("\\", "/")).into(iv_head);
                }

                tv_rl.setText(res.getRelationship());
                if (TextUtils.isEmpty(res.getUser_FullName())) {
                    tv_phone.setText(res.getUser_Name());
                } else {
                    tv_phone.setText(res.getUser_FullName());
                }
            }
        }

    }


    public static Fragment getInstance(RelationDes relationDes) {
        Bundle b = new Bundle();
        b.putSerializable(RES, relationDes);
        MyHomeFragment fragment = new MyHomeFragment();
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    protected void lazyLoad() {

    }
}
