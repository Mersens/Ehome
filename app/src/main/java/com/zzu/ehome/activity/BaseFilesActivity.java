package com.zzu.ehome.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zzu.ehome.R;
import com.zzu.ehome.utils.JsonAsyncTaskOnComplete;
import com.zzu.ehome.utils.JsonAsyncTask_Info;
import com.zzu.ehome.utils.RequestMaker;
import com.zzu.ehome.utils.SharePreferenceUtil;
import com.zzu.ehome.utils.ToastUtils;
import com.zzu.ehome.view.FlowLayout;
import com.zzu.ehome.view.HeadView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2016/5/24.
 */
public class BaseFilesActivity extends BaseActivity implements View.OnClickListener {
    private ImageView icon_add_maritalstatus;
    private ImageView icon_add_medicine;
    private ImageView icon_add_medical_history;
    private ImageView icon_add_Geneticdisease;
    private ImageView icon_add_familyhistory;
    private ImageView icon_add_smokehistory;
    private ImageView icon_add_drinkhistory;
    private LinearLayout lladd_maritalstatus;
    private LinearLayout lladd_medicine;
    private LinearLayout lladd_medical_history;
    private LinearLayout lladd_Geneticdisease;
    private LinearLayout lladd_familyhistory;
    private LinearLayout lladd_smokehistory;
    private LinearLayout lladd_drinkhistory;
    private ViewGroup.MarginLayoutParams lp;
    private FlowLayout flowmaritalstatus, flowsmoke, flowdrink, flowmedicinestatus, flowmedicalhistory, flowGeneticdisease, flowfamily_history;
    private String marriage = "";
    private String smoke = "";
    private String drink = "";
    private String medicine = "";
    private String medical = "";
    private String geneticdisease = "";
    private String familyhistory = "";
    public static final int MARRIAGE_STATE = 0x000;
    public static final int SMOKE_STATE = 0x001;
    public static final int MEDICINE = 0x011;
    public static final int DRINK_STATE = 0x111;
    public static final int MEDICAL = 0x010;
    public static final int GENETICDISEASE = 0x110;
    public static final int FAMILYHISTORY = 0x101;
    private RequestMaker requestMaker;
    private String userid;
    private boolean isUpdate = false;
    private boolean isEmpty = false;
    private boolean isError = false;


    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_base_file);
        requestMaker = RequestMaker.getInstance();
        userid = SharePreferenceUtil.getInstance(this).getUserId();
        initViews();
        initEvent();
        initDatas();
    }

    private void initViews() {
        icon_add_maritalstatus = (ImageView) findViewById(R.id.icon_add_maritalstatus);
        icon_add_medicine = (ImageView) findViewById(R.id.icon_add_medicine);
        icon_add_medical_history = (ImageView) findViewById(R.id.icon_add_medical_history);
        icon_add_Geneticdisease = (ImageView) findViewById(R.id.icon_add_Geneticdisease);
        icon_add_familyhistory = (ImageView) findViewById(R.id.icon_add_familyhistory);
        icon_add_smokehistory = (ImageView) findViewById(R.id.icon_add_smokehistory);
        icon_add_drinkhistory = (ImageView) findViewById(R.id.icon_add_drinkhistory);
        lladd_maritalstatus = (LinearLayout) findViewById(R.id.lladd_maritalstatus);
        lladd_medicine = (LinearLayout) findViewById(R.id.lladd_medicine);
        lladd_medical_history = (LinearLayout) findViewById(R.id.lladd_medical_history);
        lladd_Geneticdisease = (LinearLayout) findViewById(R.id.lladd_Geneticdisease);
        lladd_familyhistory = (LinearLayout) findViewById(R.id.lladd_familyhistory);
        lladd_smokehistory = (LinearLayout) findViewById(R.id.lladd_smokehistory);
        lladd_drinkhistory = (LinearLayout) findViewById(R.id.lladd_drinkhistory);
        flowmaritalstatus = (FlowLayout) findViewById(R.id.flowmaritalstatus);
        flowsmoke = (FlowLayout) findViewById(R.id.flowsmoke);
        flowdrink = (FlowLayout) findViewById(R.id.flowdrink);
        flowmedicinestatus = (FlowLayout) findViewById(R.id.flowmedicinestatus);
        flowmedicalhistory = (FlowLayout) findViewById(R.id.flowmedicalhistory);
        flowGeneticdisease = (FlowLayout) findViewById(R.id.flowGeneticdisease);
        flowfamily_history = (FlowLayout) findViewById(R.id.flowfamily_history);
        setDefaultTXViewMethod(R.mipmap.icon_arrow_left, "基础档案", "保存", new HeadView.OnLeftClickListener() {
            @Override
            public void onClick() {
                finishActivity();

            }
        }, new HeadView.OnRightClickListener() {
            @Override
            public void onClick() {
                String text = getRightText();
                if (!TextUtils.isEmpty(text)) {
                    if ("编辑".equals(text)) {
                        setSave();
                    } else {
                        doSave();
                    }
                }
            }
        });
    }

    private void initEvent() {
        lladd_maritalstatus.setOnClickListener(this);
        lladd_medicine.setOnClickListener(this);
        lladd_medical_history.setOnClickListener(this);
        lladd_Geneticdisease.setOnClickListener(this);
        lladd_familyhistory.setOnClickListener(this);
        lladd_smokehistory.setOnClickListener(this);
        lladd_drinkhistory.setOnClickListener(this);

        lp = new ViewGroup.MarginLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        lp.bottomMargin = 8;
        lp.leftMargin = 8;
        lp.rightMargin = 8;
        lp.topMargin = 8;
    }

    public void initDatas() {
        startProgressDialog();
        requestMaker.BaseDataInquiry(userid, new JsonAsyncTask_Info(BaseFilesActivity.this, true, new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {
                stopProgressDialog();
                JSONObject mySO = (JSONObject) result;
                Log.e("TAG", result.toString());
                try {
                    JSONArray json = mySO.getJSONArray("BaseDataInquiry");
                    if (json.getJSONObject(0).has("MessageCode")) {
                        String MessageCode = json.getJSONObject(0).getString("MessageCode");
                        if ("2".equals(MessageCode)) {
                            ToastUtils.showMessage(BaseFilesActivity.this, "数据为空！");
                            isEmpty = true;
                            return;
                        }
                        if ("1".equals(MessageCode)) {
                            ToastUtils.showMessage(BaseFilesActivity.this, "查询失败！");
                            isError = true;
                            return;
                        }
                    } else {
                        JSONObject jsonobject = json.getJSONObject(0);
                        marriage = jsonobject.getString("Marriage");
                        smoke = jsonobject.getString("Smoking");
                        drink = jsonobject.getString("Drinking");
                        medicine = jsonobject.getString("DrugAllergy");
                        medical = jsonobject.getString("MedicalHistory");
                        geneticdisease = jsonobject.getString("GeneticHistory");
                        familyhistory = jsonobject.getString("FamilyHistory");
                        setEdit();
                        setData();
                        isUpdate = true;
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {




            case R.id.lladd_maritalstatus:
                Intent maritalintent = new Intent(BaseFilesActivity.this, SelectMarriageActivity.class);
                //maritalintent.putExtra("marriage", marriage);
                startActivityForResult(maritalintent, MARRIAGE_STATE);
                break;
            case R.id.lladd_medicine:
                String medicine=getCheckName(flowmedicinestatus);
                Intent medicineintent = new Intent(BaseFilesActivity.this, MedicineActivity.class);
                medicineintent.putExtra("medicine",medicine);
                startActivityForResult(medicineintent, MEDICINE);
                break;
            case R.id.lladd_medical_history:
                Intent medicalintent = new Intent(BaseFilesActivity.this, MedicalActivity.class);
                String medical=getCheckName(flowmedicalhistory);
                medicalintent.putExtra("medical", medical);
                startActivityForResult(medicalintent, MEDICAL);
                break;
            case R.id.lladd_Geneticdisease:
                String geneticdisease=getCheckName(flowGeneticdisease);
                Intent geneticdiseaseintent = new Intent(BaseFilesActivity.this, GeneticDiseaseActivity.class);
                geneticdiseaseintent.putExtra("geneticdisease", geneticdisease);
                startActivityForResult(geneticdiseaseintent, GENETICDISEASE);
                break;
            case R.id.lladd_familyhistory:
                String familyhistory = getCheckName(flowfamily_history);
                Intent familyhistoryintent = new Intent(BaseFilesActivity.this, FamilyHistoryActivity.class);
                familyhistoryintent.putExtra("familyhistory", familyhistory);
                startActivityForResult(familyhistoryintent, FAMILYHISTORY);
                break;
            case R.id.lladd_smokehistory:
                Intent smokeintent = new Intent(BaseFilesActivity.this, SmokeStateActivity.class);
                smokeintent.putExtra("smoke", smoke);
                startActivityForResult(smokeintent, SMOKE_STATE);
                break;
            case R.id.lladd_drinkhistory:
                Intent drinkintent = new Intent(BaseFilesActivity.this, DrinkStateActivity.class);
                drinkintent.putExtra("drink", drink);
                startActivityForResult(drinkintent, DRINK_STATE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MARRIAGE_STATE && resultCode == MARRIAGE_STATE && data != null) {
            marriage = data.getStringExtra("marriage");
            setMarriage();
        }

        if (requestCode == SMOKE_STATE && resultCode == SMOKE_STATE && data != null) {
            smoke = data.getStringExtra("smoke");
            setSmoke();
        }
        if (requestCode == DRINK_STATE && resultCode == DRINK_STATE && data != null) {
            drink = data.getStringExtra("drink");
            setDrink();
        }
        if (requestCode == MEDICINE && resultCode == MEDICINE && data != null) {
            medicine = data.getStringExtra("medicine");
            setMedicine();
        }
        if (requestCode == MEDICAL && resultCode == MEDICAL && data != null) {
            medical = data.getStringExtra("medical");
            setMedical();
        }
        if (requestCode == GENETICDISEASE && resultCode == GENETICDISEASE && data != null) {
            geneticdisease = data.getStringExtra("geneticdisease");
            setGeneticdisease();
        }

        if (requestCode == FAMILYHISTORY && resultCode == FAMILYHISTORY && data != null) {
            familyhistory = data.getStringExtra("familyhistory");
            setFamilyhistory();
        }
    }

    private TextView getTextView(String text) {
        TextView tv = new TextView(BaseFilesActivity.this);
        tv.setText(text);
        tv.setTextSize(14);
        tv.setTextColor(getResources().getColor(R.color.actionbar_color));
        tv.setBackground(getResources().getDrawable(R.drawable.base_textselector));
        return tv;
    }

    public void doSave() {
        if (isUpdate) {
            update();
        } else {
            if (isEmpty) {
                add();
            }
        }
    }

    public void update() {
        medicine = getCheckName(flowmedicinestatus);
        medical = getCheckName(flowmedicalhistory);
        geneticdisease = getCheckName(flowGeneticdisease);
        familyhistory = getCheckName(flowfamily_history);

        if (TextUtils.isEmpty(familyhistory) || "".equals(familyhistory)||familyhistory.contains("无")) {
            familyhistory = " " + ":" + " " + ",";
        }
        String names[] = familyhistory.split(",");
/*        for (int i = 0; i < names.length; i++) {
            if (TextUtils.isEmpty(names[i])) {
                names[i] = " " + ":" + " ";
            }
        }*/
        List<String> list = Arrays.asList(names);
        startProgressDialog();
        requestMaker.BaseDataUpdate(userid, marriage, medicine, geneticdisease, medical, list, smoke, drink, new JsonAsyncTask_Info(BaseFilesActivity.this, true, new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {
                stopProgressDialog();
                Log.e("TAG", familyhistory);
                String value = result.toString();
                try {
                    JSONObject mySO = (JSONObject) result;
                    JSONArray array = mySO.getJSONArray("BaseDataUpdate");
                    if (array.getJSONObject(0).getString("MessageCode")
                            .equals("0")) {
                        setEdit();
                        ToastUtils.showMessage(BaseFilesActivity.this, array.getJSONObject(0).getString("MessageContent"));
                    } else
                        ToastUtils.showMessage(BaseFilesActivity.this, array.getJSONObject(0).getString("MessageContent"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }));
    }

    public void add() {
        medicine = getCheckName(flowmedicinestatus);
        medical = getCheckName(flowmedicalhistory);
        geneticdisease = getCheckName(flowGeneticdisease);
        familyhistory = getCheckName(flowfamily_history);
        if (TextUtils.isEmpty(familyhistory) || "".equals(familyhistory)||familyhistory.contains("无")) {
            familyhistory = " " + ":" + " " + "," + " " + ":" + " ";
        }
        String names[] = familyhistory.split(",");
//        for (int i = 0; i < names.length; i++) {
//            if (TextUtils.isEmpty(names[i])) {
//                names[i] = " " + ":" + " ";
//            }
//        }

        List<String> list = Arrays.asList(names);
        startProgressDialog();
        requestMaker.BaseDataInsertInsert(userid, marriage, medicine, geneticdisease, medical, list, smoke, drink, new JsonAsyncTask_Info(BaseFilesActivity.this, true, new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {
                stopProgressDialog();
                String value = result.toString();
                try {
                    JSONObject mySO = (JSONObject) result;
                    JSONArray array = mySO.getJSONArray("BaseDataInsert");
                    if (array.getJSONObject(0).getString("MessageCode")
                            .equals("0")) {
                        ToastUtils.showMessage(BaseFilesActivity.this, array.getJSONObject(0).getString("MessageContent"));
                        finish();
                    } else
                        ToastUtils.showMessage(BaseFilesActivity.this, array.getJSONObject(0).getString("MessageContent"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }));
    }

    public void setData() {
        setMarriage();
        setSmoke();
        setDrink();
        setMedicine();
        setMedical();
        setGeneticdisease();
        setFamilyhistory();
    }

    public void setMarriage() {

        flowmaritalstatus.removeAllViews();
        if (!TextUtils.isEmpty(marriage)) {
            flowmaritalstatus.addView(getTextView(marriage), lp);
        } else {
            flowmaritalstatus.addView(getTextView("无"), lp);
        }
    }

    public void setSmoke() {
        flowsmoke.removeAllViews();
        if (!TextUtils.isEmpty(smoke)) {
            flowsmoke.addView(getTextView(smoke), lp);
        } else {
            flowsmoke.addView(getTextView("无"), lp);
        }
    }

    public void setDrink() {
        flowdrink.removeAllViews();
        if (!TextUtils.isEmpty(drink)) {
            flowdrink.addView(getTextView(drink), lp);
        } else {
            flowdrink.addView(getTextView("无"), lp);
        }
    }

    public void setMedicine() {
        flowmedicinestatus.removeAllViews();
        int count = flowmedicinestatus.getChildCount();
        if (count == 0) {
            if (!TextUtils.isEmpty(medicine)) {
                String names[] = medicine.split(",");
                for (int i = 0; i < names.length; i++) {
                    if (!TextUtils.isEmpty(names[i]) && !" ".equals(names[i])) {
                        flowmedicinestatus.addView(getTextView(names[i]), lp);
                    }
                }
            } else {
                flowmedicinestatus.addView(getTextView("无"), lp);
            }
        } else {
            TextView tv = (TextView) flowmedicinestatus.getChildAt(0);
            String name = tv.getText().toString();
            if (!TextUtils.isEmpty(medicine)) {
                if ("无".equals(name)) {
                    flowmedicinestatus.removeViewAt(0);
                }
                String names[] = medicine.split(",");
                String adds[] = getCheckName(flowmedicinestatus).split(",");
                Set<String> set = getNames(names, adds);
                flowmedicinestatus.removeAllViews();
                for (String str : set) {
                    if (!TextUtils.isEmpty(str) && !" ".equals(str)) {
                        flowmedicinestatus.addView(getTextView(str), lp);
                    }
                }
            }
        }
    }

    public void setMedical() {
        flowmedicalhistory.removeAllViews();
        int count = flowmedicalhistory.getChildCount();
        if (count == 0) {
            if (!TextUtils.isEmpty(medical)) {
                String names[] = medical.split(",");
                for (int i = 0; i < names.length; i++) {
                    if (!TextUtils.isEmpty(names[i]) && !" ".equals(names[i])) {
                        flowmedicalhistory.addView(getTextView(names[i]), lp);
                    }
                }
            } else {
                flowmedicalhistory.addView(getTextView("无"), lp);
            }
        } else {
            TextView tv = (TextView) flowmedicalhistory.getChildAt(0);
            String name = tv.getText().toString();
            if (!TextUtils.isEmpty(medical)) {
                if ("无".equals(name)) {
                    flowmedicalhistory.removeViewAt(0);
                }
                String names[] = medical.split(",");
                String adds[] = getCheckName(flowmedicalhistory).split(",");
                Set<String> set = getNames(names, adds);

                flowmedicalhistory.removeAllViews();
                for (String str : set) {
                    if (!TextUtils.isEmpty(str) && !" ".equals(str)) {
                        flowmedicalhistory.addView(getTextView(str), lp);
                    }
                }
            }
        }
    }

    public void setGeneticdisease() {
        flowGeneticdisease.removeAllViews();
        int count = flowGeneticdisease.getChildCount();
        if (count == 0) {
            if (!TextUtils.isEmpty(geneticdisease)) {
                String names[] = geneticdisease.split(",");
                for (int i = 0; i < names.length; i++) {
                    if (!TextUtils.isEmpty(names[i]) && !" ".equals(names[i])) {
                        flowGeneticdisease.addView(getTextView(names[i]), lp);
                    }
                }
            } else {
                flowGeneticdisease.addView(getTextView("无"), lp);
            }
        } else {

            TextView tv = (TextView) flowGeneticdisease.getChildAt(0);
            String name = tv.getText().toString();
            if (!TextUtils.isEmpty(geneticdisease)) {
                if ("无".equals(name)) {
                    flowGeneticdisease.removeViewAt(0);
                }
                String names[] = geneticdisease.split(",");
                String adds[] = getCheckName(flowGeneticdisease).split(",");
                Set<String> set = getNames(names, adds);
                flowGeneticdisease.removeAllViews();
                for (String str : set) {
                    if (!TextUtils.isEmpty(str) && !" ".equals(str)) {
                        flowGeneticdisease.addView(getTextView(str), lp);
                    }
                }
            }
        }
    }

    public void setFamilyhistory() {
        //flowfamily_history.removeAllViews();
        int count = flowfamily_history.getChildCount();
        if (count == 0) {
            if (!TextUtils.isEmpty(familyhistory)) {
                String names[] = familyhistory.split(",");
                if (names.length > 0) {
                    for (int i = 0; i < names.length; i++) {
                        if (!TextUtils.isEmpty(names[i]) && !" ".equals(names[i])) {
                            flowfamily_history.addView(getTextView(names[i]), lp);
                        }
                    }
                } else {
                    flowfamily_history.addView(getTextView("无"), lp);
                }

            } else {
                flowfamily_history.addView(getTextView("无"), lp);
            }
        } else {
            TextView tv = (TextView) flowfamily_history.getChildAt(0);
            String name = tv.getText().toString();
            if (!TextUtils.isEmpty(familyhistory)) {
                if ("无".equals(name)) {
                    flowfamily_history.removeViewAt(0);
                }
                String names[] = familyhistory.split(",");
                String adds[] = getCheckName(flowfamily_history).split(",");
                Set<String> set = getFamilyNames(adds,names);
                flowfamily_history.removeAllViews();
                for (String str : set) {
                    if (!TextUtils.isEmpty(str) && !" ".equals(str)) {
                        flowfamily_history.addView(getTextView(str), lp);
                    }
                }
            }
        }
    }

    public void setSave() {
        setRightText("保存");
        icon_add_maritalstatus.setVisibility(View.VISIBLE);
        icon_add_medicine.setVisibility(View.VISIBLE);
        icon_add_medical_history.setVisibility(View.VISIBLE);
        icon_add_Geneticdisease.setVisibility(View.VISIBLE);
        icon_add_familyhistory.setVisibility(View.VISIBLE);
        icon_add_smokehistory.setVisibility(View.VISIBLE);
        icon_add_drinkhistory.setVisibility(View.VISIBLE);
        lladd_maritalstatus.setClickable(true);
        lladd_medicine.setClickable(true);
        lladd_medical_history.setClickable(true);
        lladd_Geneticdisease.setClickable(true);
        lladd_familyhistory.setClickable(true);
        lladd_smokehistory.setClickable(true);
        lladd_drinkhistory.setClickable(true);

    }

    public void setEdit() {
        setRightText("编辑");
        icon_add_maritalstatus.setVisibility(View.GONE);
        icon_add_medicine.setVisibility(View.GONE);
        icon_add_medical_history.setVisibility(View.GONE);
        icon_add_Geneticdisease.setVisibility(View.GONE);
        icon_add_familyhistory.setVisibility(View.GONE);
        icon_add_smokehistory.setVisibility(View.GONE);
        icon_add_drinkhistory.setVisibility(View.GONE);
        lladd_maritalstatus.setClickable(false);
        lladd_medicine.setClickable(false);
        lladd_medical_history.setClickable(false);
        lladd_Geneticdisease.setClickable(false);
        lladd_familyhistory.setClickable(false);
        lladd_smokehistory.setClickable(false);
        lladd_drinkhistory.setClickable(false);

    }

    public String getCheckName(FlowLayout flowlayout) {
        int count = flowlayout.getChildCount();
        StringBuffer sbf = new StringBuffer();
        for (int i = 0; i < count; i++) {
            TextView tv = (TextView) flowlayout.getChildAt(i);
            String name = tv.getText().toString();
            sbf.append(name).append(",");
        }
        return sbf.toString();
    }


    public Set<String> getNames(String[] str1, String[] str2) {
        Set<String> set = new HashSet<String>();
        Set<String> set1 = new HashSet<String>();
        for (String st1 : str1) {
            set.add(st1);
        }
        for (String st2 : str2) {
            set.add(st2);
        }
        for(String s:set){
            for(String st4:str2){
                if(s.equals(st4)){
                    set1.add(s);
                }
            }
        }
        return set1;
    }

    public Set<String> getFamilyNames(String[] str1, String[] str2) {
        Set<String> set = new HashSet<String>();
        for (String st1 : str1) {
            set.add(st1);
        }
        for (String st2 : str2) {
            set.add(st2);
        }

        return set;
    }
}