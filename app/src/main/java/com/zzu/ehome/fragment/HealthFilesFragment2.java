package com.zzu.ehome.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zzu.ehome.R;
import com.zzu.ehome.utils.JsonAsyncTaskOnComplete;
import com.zzu.ehome.utils.JsonAsyncTask_Info;
import com.zzu.ehome.utils.RequestMaker;
import com.zzu.ehome.utils.SharePreferenceUtil;
import com.zzu.ehome.utils.ToastUtils;
import com.zzu.ehome.view.FlowLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Mersens on 2016/7/27.
 */
public class HealthFilesFragment2 extends BaseFragment {
    private View mView;
    private TextView tv_maritalStatus;//婚姻状况
    private TextView tv_smokeState;//吸烟状况
    private TextView tv_drinkState;//喝酒状况
    private FlowLayout flowMedicineAllergy;//药物过敏
    private FlowLayout flowPastMedicalHistory;//既往病史
    private FlowLayout flowFamilyMedicalhistory_Father;//家族病史（父亲）
    private FlowLayout flowFamilyMedicalhistory_Mother;//家族病史（母亲）
    private FlowLayout flowFamilyMedicalhistory_Sister;//家族病史（兄弟姐妹）
    private FlowLayout flowFamilyMedicalhistory_Children;//家族病史（子女）
    private FlowLayout flow_GeneticHistory;//遗传病史
    private String userid;
    private RequestMaker requestMaker;
    private String maritalStatusNames = "";//婚姻状况
    private String medicineAllergyNames = "";//药物过敏
    private String pastMedicalHistoryNames = "";//既往病史
    private String familyNames = "";
    private String geneticHistoryNmaes = "";//遗传病史
    private String drinkStateNames = "";//喝酒状况
    private String smokeStateNames = "";//吸烟状况
    private String familyMedicalhistory_fatherNames = "";//家族病史（父亲）
    private String familyMedicalhistory_motherNames = "";//家族病史（母亲）
    private String familyMedicalhistory_sisterNames = "";//家族病史（兄弟姐妹）
    private String familyMedicalhistory_childrenNames = "";//家族病史（子女）
    private ViewGroup.MarginLayoutParams lp;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_healthfiles1, null);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userid = SharePreferenceUtil.getInstance(getActivity()).getUserId();
        requestMaker = RequestMaker.getInstance();
        mView = view;
        initViews();
        initEvent();

    }


    public void initViews() {
        tv_maritalStatus = (TextView) mView.findViewById(R.id.tv_maritalStatus);
        tv_smokeState = (TextView) mView.findViewById(R.id.tv_smokeState);
        tv_drinkState = (TextView) mView.findViewById(R.id.tv_drinkState);
        flowMedicineAllergy = (FlowLayout) mView.findViewById(R.id.flowMedicineAllergy);
        flowPastMedicalHistory = (FlowLayout) mView.findViewById(R.id.flowPastMedicalHistory);
        flowFamilyMedicalhistory_Father = (FlowLayout) mView.findViewById(R.id.flowFamilyMedicalhistory_Father);
        flowFamilyMedicalhistory_Mother = (FlowLayout) mView.findViewById(R.id.flowFamilyMedicalhistory_Mother);
        flowFamilyMedicalhistory_Sister = (FlowLayout) mView.findViewById(R.id.flowFamilyMedicalhistory_Sister);
        flowFamilyMedicalhistory_Children = (FlowLayout) mView.findViewById(R.id.flowFamilyMedicalhistory_Children);
        flow_GeneticHistory = (FlowLayout) mView.findViewById(R.id.flow_GeneticHistory);
    }

    public void initEvent() {
        lp = new ViewGroup.MarginLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        lp.bottomMargin = 8;
        lp.rightMargin = 12;
        lp.topMargin = 8;
    }

    @Override
    public void onResume() {
        super.onResume();
        initDatas();
    }

    public void initDatas() {
        requestMaker.BaseDataInquiry(userid, new JsonAsyncTask_Info(getActivity(), true, new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {
                JSONObject mySO = (JSONObject) result;

                try {
                    JSONArray json = mySO.getJSONArray("BaseDataInquiry");
                    if (json.getJSONObject(0).has("MessageCode")) {
                        String MessageCode = json.getJSONObject(0).getString("MessageCode");
                        if ("2".equals(MessageCode)) {
                            ToastUtils.showMessage(getActivity(), "数据为空！");
                            return;
                        }
                        if ("1".equals(MessageCode)) {
                            ToastUtils.showMessage(getActivity(), "查询失败！");
                            return;
                        }
                    } else {
                        JSONObject jsonobject = json.getJSONObject(0);
                        maritalStatusNames = jsonobject.getString("Marriage");
                        smokeStateNames = jsonobject.getString("Smoking");
                        drinkStateNames = jsonobject.getString("Drinking");
                        medicineAllergyNames = jsonobject.getString("DrugAllergy");
                        pastMedicalHistoryNames = jsonobject.getString("MedicalHistory");
                        geneticHistoryNmaes = jsonobject.getString("GeneticHistory");
                        familyNames = jsonobject.getString("FamilyHistory");
                        setDatas();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }));

    }

    public void setDatas() {
        clearFlowData();
        if (!TextUtils.isEmpty(maritalStatusNames)) {
            tv_maritalStatus.setText(maritalStatusNames);
        }

        if (!TextUtils.isEmpty(medicineAllergyNames)) {
            String names[] = medicineAllergyNames.split(",");
            for (int i = 0; i < names.length; i++) {
                if (!TextUtils.isEmpty(names[i]) && !" ".equals(names[i])) {
                    flowMedicineAllergy.addView(getTextView(names[i]), lp);
                }
            }
        } else {
            flowMedicineAllergy.addView(getTextView("无"), lp);
        }

        if (!TextUtils.isEmpty(pastMedicalHistoryNames)) {
            String names[] = pastMedicalHistoryNames.split(",");
            for (int i = 0; i < names.length; i++) {
                if (!TextUtils.isEmpty(names[i]) && !" ".equals(names[i])) {
                    flowPastMedicalHistory.addView(getTextView(names[i]), lp);
                }
            }
        } else {
            flowPastMedicalHistory.addView(getTextView("无"), lp);
        }

        String nametypes[] = familyNames.split(",");
        for (int i = 0; i < nametypes.length; i++) {
            if (nametypes[i].contains(":")) {
                String names[] = nametypes[i].split(":");
                if ("父亲".equals(names[0])) {
                    familyMedicalhistory_fatherNames = familyMedicalhistory_fatherNames + names[1] + ",";
                } else if ("母亲".equals(names[0])) {
                    familyMedicalhistory_motherNames = familyMedicalhistory_motherNames + names[1] + ",";

                } else if ("兄弟姐妹".equals(names[0])) {
                    familyMedicalhistory_sisterNames = familyMedicalhistory_sisterNames + names[1] + ",";

                } else if ("子女".equals(names[0])) {
                    familyMedicalhistory_childrenNames = familyMedicalhistory_childrenNames + names[1] + ",";

                }
            }

        }
        if (!TextUtils.isEmpty(familyMedicalhistory_fatherNames)) {
            String names[] = familyMedicalhistory_fatherNames.split(",");
            for (int i = 0; i < names.length; i++) {
                if (!TextUtils.isEmpty(names[i]) && !" ".equals(names[i])) {
                    flowFamilyMedicalhistory_Father.addView(getTextView(names[i]), lp);
                }
            }
        } else {
            flowFamilyMedicalhistory_Father.addView(getTextView("无"), lp);
        }

        if (!TextUtils.isEmpty(familyMedicalhistory_motherNames)) {
            String names[] = familyMedicalhistory_motherNames.split(",");
            for (int i = 0; i < names.length; i++) {
                if (!TextUtils.isEmpty(names[i]) && !" ".equals(names[i])) {
                    flowFamilyMedicalhistory_Mother.addView(getTextView(names[i]), lp);
                }
            }
        } else {
            flowFamilyMedicalhistory_Mother.addView(getTextView("无"), lp);
        }

        if (!TextUtils.isEmpty(familyMedicalhistory_sisterNames)) {
            String names[] = familyMedicalhistory_sisterNames.split(",");
            for (int i = 0; i < names.length; i++) {
                if (!TextUtils.isEmpty(names[i]) && !" ".equals(names[i])) {
                    flowFamilyMedicalhistory_Sister.addView(getTextView(names[i]), lp);
                }
            }
        } else {
            flowFamilyMedicalhistory_Sister.addView(getTextView("无"), lp);
        }

        if (!TextUtils.isEmpty(familyMedicalhistory_childrenNames)) {
            String names[] = familyMedicalhistory_childrenNames.split(",");
            for (int i = 0; i < names.length; i++) {
                if (!TextUtils.isEmpty(names[i]) && !" ".equals(names[i])) {
                    flowFamilyMedicalhistory_Children.addView(getTextView(names[i]), lp);
                }
            }
        } else {
            flowFamilyMedicalhistory_Children.addView(getTextView("无"), lp);
        }

        if (!TextUtils.isEmpty(geneticHistoryNmaes)) {
            String names[] = geneticHistoryNmaes.split(",");
            for (int i = 0; i < names.length; i++) {
                if (!TextUtils.isEmpty(names[i]) && !" ".equals(names[i])) {
                    flow_GeneticHistory.addView(getTextView(names[i]), lp);
                }
            }
        } else {
            flow_GeneticHistory.addView(getTextView("无"), lp);
        }

        if (!TextUtils.isEmpty(drinkStateNames)) {
            tv_drinkState.setText(drinkStateNames);
        }
        if (!TextUtils.isEmpty(smokeStateNames)) {
            tv_smokeState.setText(smokeStateNames);
        }
    }


    public void clearFlowData() {
        flowMedicineAllergy.removeAllViews();
        flowPastMedicalHistory.removeAllViews();
        flowFamilyMedicalhistory_Father.removeAllViews();
        flowFamilyMedicalhistory_Mother.removeAllViews();
        flowFamilyMedicalhistory_Sister.removeAllViews();
        flowFamilyMedicalhistory_Children.removeAllViews();
        flow_GeneticHistory.removeAllViews();
    }

    private TextView getTextView(String text) {
        TextView tv = new TextView(getActivity());
        tv.setText(text);
        tv.setTextSize(14);
        tv.setTextColor(getResources().getColor(R.color.text_color1));
        return tv;
    }


    public static Fragment getInstance() {
        return new HealthFilesFragment2();
    }


    @Override
    protected void lazyLoad() {

    }
}
