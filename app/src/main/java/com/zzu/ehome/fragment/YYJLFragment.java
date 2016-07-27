package com.zzu.ehome.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zzu.ehome.R;
import com.zzu.ehome.activity.ImageAlbumManager;
import com.zzu.ehome.activity.ImageSelectorActivity;
import com.zzu.ehome.activity.SelectDateAct;
import com.zzu.ehome.application.Constants;
import com.zzu.ehome.bean.Images;
import com.zzu.ehome.utils.ImageUtil;
import com.zzu.ehome.utils.ToastUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mersens on 2016/6/28.
 */
public class YYJLFragment extends BaseFragment implements View.OnClickListener {
    private View mView;
    private RelativeLayout layout_fysj;
    private LinearLayout layout_ypmc;
    private EditText edit_num,editText_bz;
    private ImageView imageView_add;
    private Button btn_save;
    private TextView tv_fysj_time;
    private RecyclerView resultRecyclerView;
    private ArrayList<String> images;
    private List<Images> mList=new ArrayList<Images>();
    public static final String EXTRA_IMAGES = "extraImages";
    private GridAdapter mAdapter;
    private String path;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView=inflater.inflate(R.layout.layout_yyjl,null);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        images = (ArrayList<String>) getActivity().getIntent().getSerializableExtra(EXTRA_IMAGES);
        initViews();
        initEvent();
        initDatas();
        return mView;
    }

    public void initViews(){
        layout_fysj=(RelativeLayout)mView.findViewById(R.id.layout_fysj);
        layout_ypmc=(LinearLayout)mView.findViewById(R.id.layout_ypmc);
        edit_num=(EditText)mView.findViewById(R.id.editText);
        editText_bz=(EditText)mView.findViewById(R.id.editText_bz);
        imageView_add=(ImageView) mView.findViewById(R.id.imageView_add);
        btn_save=(Button) mView.findViewById(R.id.btn_save);
        tv_fysj_time=(TextView) mView.findViewById(R.id.tv_fysj_time);
        resultRecyclerView = (RecyclerView) mView.findViewById(R.id.result_recycler);

    }

    public void initEvent(){
        layout_fysj.setOnClickListener(this);
        imageView_add.setOnClickListener(this);
        btn_save.setOnClickListener(this);

    }

    public void initDatas(){
        images=new ArrayList<String>();
        path= ImageUtil.saveResTolocal(getActivity().getResources(),R.mipmap.icon_add_xd,"photo_yao");
        images.add(path);
        mAdapter=new GridAdapter(images,getActivity());
        resultRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        resultRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void lazyLoad() {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout_fysj:
                Intent intenttime = new Intent(getActivity(), SelectDateAct.class);
                startActivityForResult(intenttime, Constants.ADDTTIME);
                break;
            case R.id.imageView_add:
                ImageSelectorActivity.start(getActivity(), 9, ImageSelectorActivity.MODE_MULTIPLE, true,false,false,images.size());

                break;
            case R.id.btn_save:
                doSave();
                break;
        }

    }


    private class GridAdapter extends RecyclerView.Adapter<GridAdapter.ViewHolder> {
        private Context mContext;
        public List<String> getmList() {
            return mList;
        }

        public void setmList(List<String> mList) {
            this.mList = mList;
        }

        private List<String> mList;
        public GridAdapter(List<String> list, Context context){
            super();
            this.mList=list;
            this.mContext=context;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_result, parent, false);

            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            Glide.with(getActivity())
                    .load(new File(images.get(position)))
                    .centerCrop()
                    .into(holder.imageView);


            if(mList.get(position).equals(path)){
                holder.imageClose.setVisibility(View.GONE);
            }else{
                holder.imageClose.setVisibility(View.VISIBLE);
            }
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mList.get(position).equals(path)) {
                        ImageSelectorActivity.start((Activity) mContext, 9, ImageSelectorActivity.MODE_MULTIPLE, true, false, false, images.size() - 1);
                    }else{
                        Intent intent = new Intent(mContext, ImageAlbumManager.class);
                        if(mList.size()==9) {
                            intent.putStringArrayListExtra("imgs", (ArrayList<String>) mList);
                        }else{
                            List<String> imgs=new ArrayList<String>(mList);
                            //imgs=mList;
                            imgs.remove(mList.size()-1);
                            intent.putStringArrayListExtra("imgs", (ArrayList<String>) imgs);
                        }
                        intent.putExtra("position", position);
                        intent.putExtra("type", 2);
                        mContext.startActivity(intent);
                    }
                }
            });
            holder.imageClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mList.remove(position);
//                        images.remove(position);
                    if(position==8){
//                        images.remove(8);
                        images.add(path);
                        setmList(images);
                    }

                    notifyDataSetChanged();

                }
            });
        }

        @Override
        public int getItemCount() {
            return mList==null?0:mList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;
            ImageView imageClose;


            public ViewHolder(View itemView) {
                super(itemView);
                imageView = (ImageView) itemView.findViewById(R.id.image);
                imageClose=(ImageView) itemView.findViewById(R.id.ivclose);
            }
        }
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case Constants.ADDTTIME:
                if(data != null){
                    String time = data.getStringExtra("time");
                    if (!TextUtils.isEmpty(time)) {
                        tv_fysj_time.setText(time);
                    }
                }
                break;
            case ImageSelectorActivity.REQUEST_IMAGE:
                if(data!=null) {
                    ArrayList<String> images1 = (ArrayList<String>) data.getSerializableExtra(ImageSelectorActivity.REQUEST_OUTPUT);
                    if (images1 != null) {
                        for (int i = 0; i < images1.size(); i++) {
                            images.add(0,images1.get(i));
                        }
                        if(images.size()>10){

                            ToastUtils.showMessage(getActivity(),"你最多可以选择9张");
                        }
                        if(images.size()==10){
                            images.remove(9);
                        }
                    }

                    mAdapter.setmList(images);
                    mAdapter.notifyDataSetChanged();
                }
                break;

        }

    }



    //保存
    public void doSave(){



    }
}
