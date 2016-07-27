package com.zzu.ehome.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.zzu.ehome.R;
import com.zzu.ehome.adapter.SwitchAdapter;
import com.zzu.ehome.bean.DiseaseBean;
import com.zzu.ehome.utils.IOUtils;
import com.zzu.ehome.utils.ToastUtils;
import com.zzu.ehome.view.ContainsEmojiEditText;
import com.zzu.ehome.view.HeadView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2016/6/2.
 */
public class FamilyHistoryActivity extends BaseActivity {
    private ListView listview;
    private ContainsEmojiEditText editText;
    private Button btn_save;
    private List<DiseaseBean> mList;
    private SwitchAdapter adapter;
    private RadioGroup radioGroup;
    private RadioButton rb_father, rb_mother, rb_brother_sister, rb_children;
    private int index = 0;
    private String familyhistory=null;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.layout_family_history);
        familyhistory=getIntent().getStringExtra("familyhistory");
        initViews();
        initEvent();
        initDatas();
    }

    public void initViews() {
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        rb_father = (RadioButton) findViewById(R.id.rb_father);
        rb_mother = (RadioButton) findViewById(R.id.rb_mother);
        rb_brother_sister = (RadioButton) findViewById(R.id.rb_brother_sister);
        rb_children = (RadioButton) findViewById(R.id.rb_children);
        listview = (ListView) findViewById(R.id.listview);
        editText = (ContainsEmojiEditText) findViewById(R.id.editText);
        btn_save = (Button) findViewById(R.id.btn_save);
        setLeftWithTitleViewMethod(R.mipmap.icon_arrow_left, "家族病史", new HeadView.OnLeftClickListener() {
            @Override
            public void onClick() {

                finish();
            }
        });
    }

    public void initEvent() {
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doAdd();
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_father:
                        index = 0;
                        serColor(0);
                        break;
                    case R.id.rb_mother:
                        index = 1;
                        serColor(1);
                        break;
                    case R.id.rb_brother_sister:
                        index = 2;
                        serColor(2);
                        break;
                    case R.id.rb_children:
                        index = 3;
                        serColor(3);
                        break;
                }
            }
        });
    }

    private void serColor(int pos) {
        resetColor();
        switch (pos) {
            case 0:
                rb_father.setChecked(true);
                rb_father.setTextColor(getResources().getColor(R.color.white));
                break;
            case 1:
                rb_mother.setChecked(true);
                rb_mother.setTextColor(getResources().getColor(R.color.white));
                break;
            case 2:
                rb_brother_sister.setChecked(true);
                rb_brother_sister.setTextColor(getResources().getColor(R.color.white));
                break;
            case 3:
                rb_children.setChecked(true);
                rb_children.setTextColor(getResources().getColor(R.color.white));
                break;
        }
    }

    public void resetColor() {
        rb_father.setChecked(false);
        rb_father.setTextColor(getResources().getColor(R.color.actionbar_color));

        rb_mother.setChecked(false);
        rb_mother.setTextColor(getResources().getColor(R.color.actionbar_color));

        rb_brother_sister.setChecked(false);
        rb_brother_sister.setTextColor(getResources().getColor(R.color.actionbar_color));

        rb_children.setChecked(false);
        rb_children.setTextColor(getResources().getColor(R.color.actionbar_color));
    }

    public void initDatas() {
        mList = new ArrayList<DiseaseBean>();
        mList.add(new DiseaseBean("高血压",false));
        mList.add(new DiseaseBean("糖尿病",false));
        mList.add(new DiseaseBean("冠心病",false));
        mList.add(new DiseaseBean("结核病",false));
        mList.add(new DiseaseBean("脑卒中",false));
        mList.add(new DiseaseBean("肝炎",false));
        mList.add(new DiseaseBean("恶性肿瘤",false));
        mList.add(new DiseaseBean("先天畸形",false));
        mList.add(new DiseaseBean("重性精神疾病",false));
        mList.add(new DiseaseBean("慢性阻塞性肺疾病",false));
        adapter = new SwitchAdapter(this, mList);
        listview.setAdapter(adapter);
    }

    public void doAdd() {
        String name = editText.getText().toString().trim();
        Map<Integer, Boolean> map = adapter.getDataMap();
        StringBuffer sbf = new StringBuffer();
        String title = getTitleName();
        for (Map.Entry<Integer, Boolean> entry : map.entrySet()) {
            if (entry.getValue()) {
                sbf.append(title + ":" + mList.get(entry.getKey()).getName()).append(",");
            }
        }
        Intent intent = new Intent();
        if (!TextUtils.isEmpty(name)) {
            if (!IOUtils.isName(name)) {
                ToastUtils.showMessage(FamilyHistoryActivity.this, "请输入中文名称");
                return;
            }
            sbf.append(title + ":"+name).append(",");
        }
        intent.putExtra("familyhistory", sbf.toString());
        setResult(BaseFilesActivity.FAMILYHISTORY, intent);
        finish();
    }

    public String getTitleName() {
        String name = null;
        switch (index) {
            case 0:
                name = "父亲";
                break;
            case 1:
                name = "母亲";
                break;
            case 2:
                name = "兄妹";
                break;
            case 3:
                name = "子女";
                break;
        }
        return name;
    }

}
