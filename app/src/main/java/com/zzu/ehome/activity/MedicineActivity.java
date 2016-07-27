package com.zzu.ehome.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.zzu.ehome.R;
import com.zzu.ehome.adapter.SwitchAdapter;
import com.zzu.ehome.bean.BaseFileBean;
import com.zzu.ehome.bean.DiseaseBean;
import com.zzu.ehome.utils.IOUtils;
import com.zzu.ehome.utils.ToastUtils;
import com.zzu.ehome.view.ContainsEmojiEditText;
import com.zzu.ehome.view.HeadView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2016/6/1.
 */
public class MedicineActivity extends BaseActivity {
    private ListView listview;
    private ContainsEmojiEditText editText;
    private Button btn_save;
    private List<DiseaseBean> mList;
    private SwitchAdapter adapter;
    private String medicine=null;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.layout_medicine);
        medicine=getIntent().getStringExtra("medicine");
        initViews();
        initEvent();
        initDatas();
    }

    public void initViews(){
        listview=(ListView) findViewById(R.id.listview);
        editText=(ContainsEmojiEditText)findViewById(R.id.editText);
        btn_save=(Button) findViewById(R.id.btn_save);
        setLeftWithTitleViewMethod(R.mipmap.icon_arrow_left, "药物过敏", new HeadView.OnLeftClickListener() {
            @Override
            public void onClick() {
                finish();
            }
        });
    }
    public void initEvent(){
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doAdd();
            }
        });
    }
    public void initDatas(){
        mList=new ArrayList<DiseaseBean>();
        mList.add(new DiseaseBean("青霉素",false));
        mList.add(new DiseaseBean("磺胺",false));
        mList.add(new DiseaseBean("链霉素",false));
        if(!TextUtils.isEmpty(medicine)){
            String names[] = medicine.split(",");
            for(int i=0;i<mList.size();i++){
                for(int j=0;j<names.length;j++){
                    if(mList.get(i).getName().equals(names[j])){
                        mList.get(i).setOpen(true);
                    }
                }
            }
        }
        adapter=new SwitchAdapter(this,mList);
        listview.setAdapter(adapter);
    }

    public void doAdd(){
        String name=editText.getText().toString().trim();
        Map<Integer,Boolean> map=adapter.getDataMap();
        StringBuffer sbf=new StringBuffer();
        for(Map.Entry<Integer,Boolean> entry:map.entrySet()){
            if(entry.getValue()){
                sbf.append(mList.get(entry.getKey()).getName()).append(",");
            }
        }
        Log.e("StringBuffer1",sbf.toString());
        Intent intent = new Intent();
        if(!TextUtils.isEmpty(name)){
            if(!IOUtils.isName(name)){
                ToastUtils.showMessage(MedicineActivity.this,"请输入中文名称");
                return;
            }
            sbf.append(name);
        }
        Log.e("StringBuffer2",sbf.toString());
        intent.putExtra("medicine",sbf.toString());
        setResult(BaseFilesActivity.MEDICINE, intent);
        finish();
    }
}
