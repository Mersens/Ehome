package com.zzu.ehome.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.Toast;

import com.zzu.ehome.R;
import com.zzu.ehome.view.HeadView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mersens on 2016/7/27.
 */
public class HealthFilesFragment1 extends BaseFragment {
    private View mView;

    private Button btn_save;

    private RadioButton weiHunCheck, jieHunCheck, liHunCheck, sangOuCheck;

    private RadioGroup maritalStatus_group, medicineAllergy_group, pastMedicalHistory_group,
            familyMedicalhistory_father_group, familyMedicalhistory_mother_group,
            familyMedicalhistory_sister_group, familyMedicalhistory_children_group,
            geneticHistory_group, smokeState_group, drink_group;

    private RadioButton medicineAllergy_yesCheck, medicineAllergy_noCheck, pastMedicalHistory_yesCheck,
            pastMedicalHistory_noCheck, familyMedicalhistory_father_yesCheck,
            familyMedicalhistory_father_noCheck, familyMedicalhistory_mother_yesCheck,
            familyMedicalhistory_mother_noCheck, familyMedicalhistory_sister_yesCheck,
            familyMedicalhistory_sister_noCheck, familyMedicalhistory_children_yesCheck,
            familyMedicalhistory_children_noCheck, geneticHistory_yesCheck,
            geneticHistory_noCheck, smokeState_yesCheck, smokeState_noCheck,
            drinkState_yesCheck, drinkState_noCheck;

    private TableLayout medicineAllergy_type, pastMedicalHistory_type, familyMedicalhistory_father_type,
            familyMedicalhistory_mother_type, familyMedicalhistory_sister_type,
            familyMedicalhistory_children_type, geneticHistory_type;

    private LinearLayout smokeState_type, drinkState_type;

    private CheckBox medicineAllergy_checkbox_qingmeisu, medicineAllergy_checkbox_huangan,
            medicineAllergy_checkbox_lianmeisu,
            medicineAllergy_checkbox_qita;

    private CheckBox pastMedicalHistory_checkbox_gaoxueya, pastMedicalHistory_checkbox_tangniaobing,
            pastMedicalHistory_checkbox_guanxinbing, pastMedicalHistory_checkbox_naozuzhong,
            pastMedicalHistory_checkbox_jiehebing, pastMedicalHistory_checkbox_exingzhongliu,
            pastMedicalHistory_checkbox_ganyan, pastMedicalHistory_checkbox_chuanranbing,
            pastMedicalHistory_checkbox_zzjb, pastMedicalHistory_checkbox_mxfjb,
            pastMedicalHistory_checkbox_qita;

    private CheckBox fmh_father_checkbox_gxy, fmh_father_checkbox_tnb, fmh_father_checkbox_gxb,
            fmh_father_checkbox_gy, fmh_father_checkbox_jhb, fmh_father_checkbox_exzl,
            fmh_father_checkbox_xtjx, fmh_father_checkbox_zsxfjb, fmh_father_checkbox_qt;

    private CheckBox fmh_mother_checkbox_gxy, fmh_mother_checkbox_tnb, fmh_mother_checkbox_gxb,
            fmh_mother_checkbox_gy, fmh_mother_checkbox_jhb, fmh_mother_checkbox_exzl,
            fmh_mother_checkbox_xtjx, fmh_mother_checkbox_zsxfjb, fmh_mother_checkbox_qt;

    private CheckBox fmh_children_checkbox_gxy, fmh_children_checkbox_tnb, fmh_children_checkbox_gxb,
            fmh_children_checkbox_gy, fmh_children_checkbox_jhb, fmh_children_checkbox_exzl,
            fmh_children_checkbox_xtjx, fmh_children_checkbox_zsxfjb, fmh_children_checkbox_qt;

    private CheckBox fmh_sister_checkbox_gxy, fmh_sister_checkbox_tnb, fmh_sister_checkbox_gxb,
            fmh_sister_checkbox_gy, fmh_sister_checkbox_jhb, fmh_sister_checkbox_exzl,
            fmh_sister_checkbox_xtjx, fmh_sister_checkbox_zsxfjb, fmh_sister_checkbox_qt;

    private CheckBox gh_checkbox_gxy, gh_checkbox_tnb, gh_checkbox_gxb, gh_checkbox_nzu,
            gh_checkbox_jhb, gh_checkbox_exzl, gh_checkbox_gy, gh_checkbox_crb,
            gh_checkbox_zzjsjb, gh_checkbox_zsxfjb, gh_checkbox_qt;


    private CheckBox smokeState_checkbox_cb, smokeState_checkbox_oer, smokeState_checkbox_jc,
            smokeState_checkbox_mt, smokeState_checkbox_yjy;

    private CheckBox drinkState_checkbox_cb, drinkState_checkbox_oer, drinkState_checkbox_jc,
            drinkState_checkbox_mt, drinkState_checkbox_yjj;

    private Map<String, String> map = null;
    public static final Integer MEDICINEALLERGY_CODE = 1;
    public static final Integer PASTMEDICALHISTORY_CODE = 2;
    public static final Integer FAMILYMEDICALHISTORY_FATHER_CODE = 3;
    public static final Integer FAMILYMEDICALHISTORY_MOTHER_CODE = 4;
    public static final Integer FAMILYMEDICALHISTORY_SISTER_CODE = 5;
    public static final Integer FAMILYMEDICALHISTORY_CHILDREN_CODE = 6;
    public static final Integer GENETICHISTORY_CODE = 7;
    public static final Integer SMOKESTATE_CODE = 8;
    public static final Integer DRINKSTATE_CODE = 9;
    public static final Integer MARITALSTATUS = 10;

    private boolean isCheck=false;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_healthfiles2, null);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mView = view;
        initViews();
        initEvent();
        initDatas();
    }


    public void initViews() {
        btn_save = (Button) mView.findViewById(R.id.btn_save);
        //婚姻状况
        maritalStatus_group = (RadioGroup) mView.findViewById(R.id.maritalStatus_group);
        weiHunCheck = (RadioButton) mView.findViewById(R.id.weiHunCheck);
        jieHunCheck = (RadioButton) mView.findViewById(R.id.jieHunCheck);
        liHunCheck = (RadioButton) mView.findViewById(R.id.liHunCheck);
        sangOuCheck = (RadioButton) mView.findViewById(R.id.sangOuCheck);
        //药物过敏
        medicineAllergy_group = (RadioGroup) mView.findViewById(R.id.medicineAllergy_group);
        medicineAllergy_yesCheck = (RadioButton) mView.findViewById(R.id.medicineAllergy_yesCheck);
        medicineAllergy_noCheck = (RadioButton) mView.findViewById(R.id.medicineAllergy_noCheck);
        medicineAllergy_type = (TableLayout) mView.findViewById(R.id.medicineAllergy_type);
        medicineAllergy_checkbox_qingmeisu = (CheckBox) mView.findViewById(R.id.medicineAllergy_checkbox_qingmeisu);
        medicineAllergy_checkbox_huangan = (CheckBox) mView.findViewById(R.id.medicineAllergy_checkbox_huangan);
        medicineAllergy_checkbox_lianmeisu = (CheckBox) mView.findViewById(R.id.medicineAllergy_checkbox_lianmeisu);
        medicineAllergy_checkbox_qita = (CheckBox) mView.findViewById(R.id.medicineAllergy_checkbox_qita);
        //既往病史
        pastMedicalHistory_group = (RadioGroup) mView.findViewById(R.id.pastMedicalHistory_group);
        pastMedicalHistory_yesCheck = (RadioButton) mView.findViewById(R.id.pastMedicalHistory_yesCheck);
        pastMedicalHistory_noCheck = (RadioButton) mView.findViewById(R.id.pastMedicalHistory_noCheck);
        pastMedicalHistory_type = (TableLayout) mView.findViewById(R.id.pastMedicalHistory_type);
        pastMedicalHistory_checkbox_gaoxueya = (CheckBox) mView.findViewById(R.id.pastMedicalHistory_checkbox_gaoxueya);
        pastMedicalHistory_checkbox_tangniaobing = (CheckBox) mView.findViewById(R.id.pastMedicalHistory_checkbox_tangniaobing);
        pastMedicalHistory_checkbox_guanxinbing = (CheckBox) mView.findViewById(R.id.pastMedicalHistory_checkbox_guanxinbing);
        pastMedicalHistory_checkbox_naozuzhong = (CheckBox) mView.findViewById(R.id.pastMedicalHistory_checkbox_naozuzhong);
        pastMedicalHistory_checkbox_jiehebing = (CheckBox) mView.findViewById(R.id.pastMedicalHistory_checkbox_jiehebing);
        pastMedicalHistory_checkbox_exingzhongliu = (CheckBox) mView.findViewById(R.id.pastMedicalHistory_checkbox_exingzhongliu);
        pastMedicalHistory_checkbox_ganyan = (CheckBox) mView.findViewById(R.id.pastMedicalHistory_checkbox_ganyan);
        pastMedicalHistory_checkbox_chuanranbing = (CheckBox) mView.findViewById(R.id.pastMedicalHistory_checkbox_chuanranbing);
        pastMedicalHistory_checkbox_zzjb = (CheckBox) mView.findViewById(R.id.pastMedicalHistory_checkbox_zzjb);
        pastMedicalHistory_checkbox_mxfjb = (CheckBox) mView.findViewById(R.id.pastMedicalHistory_checkbox_mxfjb);
        pastMedicalHistory_checkbox_qita = (CheckBox) mView.findViewById(R.id.pastMedicalHistory_checkbox_qita);
        //家族病史(父亲)
        familyMedicalhistory_father_group = (RadioGroup) mView.findViewById(R.id.familyMedicalhistory_father_group);
        familyMedicalhistory_father_yesCheck = (RadioButton) mView.findViewById(R.id.familyMedicalhistory_father_yesCheck);
        familyMedicalhistory_father_noCheck = (RadioButton) mView.findViewById(R.id.familyMedicalhistory_father_noCheck);
        familyMedicalhistory_father_type = (TableLayout) mView.findViewById(R.id.familyMedicalhistory_father_type);
        fmh_father_checkbox_gxy = (CheckBox) mView.findViewById(R.id.fmh_father_checkbox_gxy);
        fmh_father_checkbox_tnb = (CheckBox) mView.findViewById(R.id.fmh_father_checkbox_tnb);
        fmh_father_checkbox_gxb = (CheckBox) mView.findViewById(R.id.fmh_father_checkbox_gxb);
        fmh_father_checkbox_gy = (CheckBox) mView.findViewById(R.id.fmh_father_checkbox_gy);
        fmh_father_checkbox_jhb = (CheckBox) mView.findViewById(R.id.fmh_father_checkbox_jhb);
        fmh_father_checkbox_exzl = (CheckBox) mView.findViewById(R.id.fmh_father_checkbox_exzl);
        fmh_father_checkbox_xtjx = (CheckBox) mView.findViewById(R.id.fmh_father_checkbox_xtjx);
        fmh_father_checkbox_zsxfjb = (CheckBox) mView.findViewById(R.id.fmh_father_checkbox_zsxfjb);
        fmh_father_checkbox_qt = (CheckBox) mView.findViewById(R.id.fmh_father_checkbox_qt);
        //家族病史(母亲)
        familyMedicalhistory_mother_group = (RadioGroup) mView.findViewById(R.id.familyMedicalhistory_mother_group);
        familyMedicalhistory_mother_yesCheck = (RadioButton) mView.findViewById(R.id.familyMedicalhistory_mother_yesCheck);
        familyMedicalhistory_mother_noCheck = (RadioButton) mView.findViewById(R.id.familyMedicalhistory_mother_noCheck);
        familyMedicalhistory_mother_type = (TableLayout) mView.findViewById(R.id.familyMedicalhistory_mother_type);
        fmh_mother_checkbox_gxy = (CheckBox) mView.findViewById(R.id.fmh_mother_checkbox_gxy);
        fmh_mother_checkbox_tnb = (CheckBox) mView.findViewById(R.id.fmh_mother_checkbox_tnb);
        fmh_mother_checkbox_gxb = (CheckBox) mView.findViewById(R.id.fmh_mother_checkbox_gxb);
        fmh_mother_checkbox_gy = (CheckBox) mView.findViewById(R.id.fmh_mother_checkbox_gy);
        fmh_mother_checkbox_jhb = (CheckBox) mView.findViewById(R.id.fmh_mother_checkbox_jhb);
        fmh_mother_checkbox_exzl = (CheckBox) mView.findViewById(R.id.fmh_mother_checkbox_exzl);
        fmh_mother_checkbox_xtjx = (CheckBox) mView.findViewById(R.id.fmh_mother_checkbox_xtjx);
        fmh_mother_checkbox_zsxfjb = (CheckBox) mView.findViewById(R.id.fmh_mother_checkbox_zsxfjb);
        fmh_mother_checkbox_qt = (CheckBox) mView.findViewById(R.id.fmh_mother_checkbox_qt);
        //家族病史(兄弟姐妹)
        familyMedicalhistory_sister_group = (RadioGroup) mView.findViewById(R.id.familyMedicalhistory_sister_group);
        familyMedicalhistory_sister_yesCheck = (RadioButton) mView.findViewById(R.id.familyMedicalhistory_sister_yesCheck);
        familyMedicalhistory_sister_noCheck = (RadioButton) mView.findViewById(R.id.familyMedicalhistory_sister_noCheck);
        familyMedicalhistory_sister_type = (TableLayout) mView.findViewById(R.id.familyMedicalhistory_sister_type);
        fmh_sister_checkbox_gxy = (CheckBox) mView.findViewById(R.id.fmh_sister_checkbox_gxy);
        fmh_sister_checkbox_tnb = (CheckBox) mView.findViewById(R.id.fmh_sister_checkbox_tnb);
        fmh_sister_checkbox_gxb = (CheckBox) mView.findViewById(R.id.fmh_sister_checkbox_gxb);
        fmh_sister_checkbox_gy = (CheckBox) mView.findViewById(R.id.fmh_sister_checkbox_gy);
        fmh_sister_checkbox_jhb = (CheckBox) mView.findViewById(R.id.fmh_sister_checkbox_jhb);
        fmh_sister_checkbox_exzl = (CheckBox) mView.findViewById(R.id.fmh_sister_checkbox_exzl);
        fmh_sister_checkbox_xtjx = (CheckBox) mView.findViewById(R.id.fmh_sister_checkbox_xtjx);
        fmh_sister_checkbox_zsxfjb = (CheckBox) mView.findViewById(R.id.fmh_sister_checkbox_zsxfjb);
        fmh_sister_checkbox_qt = (CheckBox) mView.findViewById(R.id.fmh_sister_checkbox_qt);
        //家族病史(子女)
        familyMedicalhistory_children_group = (RadioGroup) mView.findViewById(R.id.familyMedicalhistory_children_group);
        familyMedicalhistory_children_yesCheck = (RadioButton) mView.findViewById(R.id.familyMedicalhistory_children_yesCheck);
        familyMedicalhistory_children_noCheck = (RadioButton) mView.findViewById(R.id.familyMedicalhistory_children_noCheck);
        familyMedicalhistory_children_type = (TableLayout) mView.findViewById(R.id.familyMedicalhistory_children_type);
        fmh_children_checkbox_gxy = (CheckBox) mView.findViewById(R.id.fmh_children_checkbox_gxy);
        fmh_children_checkbox_tnb = (CheckBox) mView.findViewById(R.id.fmh_children_checkbox_tnb);
        fmh_children_checkbox_gxb = (CheckBox) mView.findViewById(R.id.fmh_children_checkbox_gxb);
        fmh_children_checkbox_gy = (CheckBox) mView.findViewById(R.id.fmh_children_checkbox_gy);
        fmh_children_checkbox_jhb = (CheckBox) mView.findViewById(R.id.fmh_children_checkbox_jhb);
        fmh_children_checkbox_exzl = (CheckBox) mView.findViewById(R.id.fmh_children_checkbox_exzl);
        fmh_children_checkbox_xtjx = (CheckBox) mView.findViewById(R.id.fmh_children_checkbox_xtjx);
        fmh_children_checkbox_zsxfjb = (CheckBox) mView.findViewById(R.id.fmh_children_checkbox_zsxfjb);
        fmh_children_checkbox_qt = (CheckBox) mView.findViewById(R.id.fmh_children_checkbox_qt);
        //遗传病史
        geneticHistory_group = (RadioGroup) mView.findViewById(R.id.geneticHistory_group);
        geneticHistory_yesCheck = (RadioButton) mView.findViewById(R.id.geneticHistory_yesCheck);
        geneticHistory_noCheck = (RadioButton) mView.findViewById(R.id.geneticHistory_noCheck);
        geneticHistory_type = (TableLayout) mView.findViewById(R.id.geneticHistory_type);
        gh_checkbox_gxy = (CheckBox) mView.findViewById(R.id.gh_checkbox_gxy);
        gh_checkbox_tnb = (CheckBox) mView.findViewById(R.id.gh_checkbox_tnb);
        gh_checkbox_gxb = (CheckBox) mView.findViewById(R.id.gh_checkbox_gxb);
        gh_checkbox_nzu = (CheckBox) mView.findViewById(R.id.gh_checkbox_nzu);
        gh_checkbox_jhb = (CheckBox) mView.findViewById(R.id.gh_checkbox_jhb);
        gh_checkbox_exzl = (CheckBox) mView.findViewById(R.id.gh_checkbox_exzl);
        gh_checkbox_gy = (CheckBox) mView.findViewById(R.id.gh_checkbox_gy);
        gh_checkbox_crb = (CheckBox) mView.findViewById(R.id.gh_checkbox_crb);
        gh_checkbox_zzjsjb = (CheckBox) mView.findViewById(R.id.gh_checkbox_zzjsjb);
        gh_checkbox_zsxfjb = (CheckBox) mView.findViewById(R.id.gh_checkbox_zsxfjb);
        gh_checkbox_qt = (CheckBox) mView.findViewById(R.id.gh_checkbox_qt);
        //吸烟状况
        smokeState_group = (RadioGroup) mView.findViewById(R.id.smokeState_group);
        smokeState_yesCheck = (RadioButton) mView.findViewById(R.id.smokeState_yesCheck);
        smokeState_noCheck = (RadioButton) mView.findViewById(R.id.smokeState_noCheck);
        smokeState_type = (LinearLayout) mView.findViewById(R.id.smokeState_type);
        smokeState_checkbox_cb = (CheckBox) mView.findViewById(R.id.smokeState_checkbox_cb);
        smokeState_checkbox_oer = (CheckBox) mView.findViewById(R.id.smokeState_checkbox_oer);
        smokeState_checkbox_jc = (CheckBox) mView.findViewById(R.id.smokeState_checkbox_jc);
        smokeState_checkbox_mt = (CheckBox) mView.findViewById(R.id.smokeState_checkbox_mt);
        smokeState_checkbox_yjy = (CheckBox) mView.findViewById(R.id.smokeState_checkbox_yjy);

        //喝酒状况
        drink_group = (RadioGroup) mView.findViewById(R.id.drinkState_group);
        drinkState_yesCheck = (RadioButton) mView.findViewById(R.id.drinkState_yesCheck);
        drinkState_noCheck = (RadioButton) mView.findViewById(R.id.drinkState_noCheck);
        drinkState_type = (LinearLayout) mView.findViewById(R.id.drinkState_type);
        drinkState_checkbox_cb = (CheckBox) mView.findViewById(R.id.drinkState_checkbox_cb);
        drinkState_checkbox_oer = (CheckBox) mView.findViewById(R.id.drinkState_checkbox_oer);
        drinkState_checkbox_jc = (CheckBox) mView.findViewById(R.id.drinkState_checkbox_jc);
        drinkState_checkbox_mt = (CheckBox) mView.findViewById(R.id.drinkState_checkbox_mt);
        drinkState_checkbox_yjj = (CheckBox) mView.findViewById(R.id.drinkState_checkbox_yjj);


    }

    public void initEvent() {
        map = new HashMap<String, String>();
        maritalStatus_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                isCheck=true;
                if (checkedId == weiHunCheck.getId()) {
                    map.put(MARITALSTATUS+"", weiHunCheck.getText().toString());
                } else if (checkedId == jieHunCheck.getId()) {
                    map.put(MARITALSTATUS + "", jieHunCheck.getText().toString());
                } else if (checkedId == liHunCheck.getId()) {
                    map.put(MARITALSTATUS + "" , liHunCheck.getText().toString());
                } else if (checkedId == sangOuCheck.getId()) {
                    map.put(MARITALSTATUS + "" , sangOuCheck.getText().toString());
                }
            }
        });
        medicineAllergy_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == medicineAllergy_yesCheck.getId()) {
                    medicineAllergy_type.setVisibility(View.VISIBLE);
                } else if (checkedId == medicineAllergy_noCheck.getId()) {
                    medicineAllergy_type.setVisibility(View.GONE);
                }
            }
        });
        pastMedicalHistory_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == pastMedicalHistory_yesCheck.getId()) {
                    pastMedicalHistory_type.setVisibility(View.VISIBLE);
                } else if (checkedId == pastMedicalHistory_noCheck.getId()) {
                    pastMedicalHistory_type.setVisibility(View.GONE);
                }
            }
        });
        familyMedicalhistory_father_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == familyMedicalhistory_father_yesCheck.getId()) {
                    familyMedicalhistory_father_type.setVisibility(View.VISIBLE);
                } else if (checkedId == familyMedicalhistory_father_noCheck.getId()) {
                    familyMedicalhistory_father_type.setVisibility(View.GONE);
                }
            }
        });
        familyMedicalhistory_mother_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == familyMedicalhistory_mother_yesCheck.getId()) {
                    familyMedicalhistory_mother_type.setVisibility(View.VISIBLE);
                } else if (checkedId == familyMedicalhistory_mother_noCheck.getId()) {
                    familyMedicalhistory_mother_type.setVisibility(View.GONE);
                }
            }
        });
        familyMedicalhistory_sister_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == familyMedicalhistory_sister_yesCheck.getId()) {
                    familyMedicalhistory_sister_type.setVisibility(View.VISIBLE);
                } else if (checkedId == familyMedicalhistory_sister_noCheck.getId()) {
                    familyMedicalhistory_sister_type.setVisibility(View.GONE);
                }
            }
        });
        familyMedicalhistory_children_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == familyMedicalhistory_children_yesCheck.getId()) {
                    familyMedicalhistory_children_type.setVisibility(View.VISIBLE);
                } else if (checkedId == familyMedicalhistory_children_noCheck.getId()) {
                    familyMedicalhistory_children_type.setVisibility(View.GONE);
                }
            }
        });
        geneticHistory_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == geneticHistory_yesCheck.getId()) {
                    geneticHistory_type.setVisibility(View.VISIBLE);
                } else if (checkedId == geneticHistory_noCheck.getId()) {
                    geneticHistory_type.setVisibility(View.GONE);
                }
            }
        });
        smokeState_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == smokeState_yesCheck.getId()) {
                    smokeState_type.setVisibility(View.VISIBLE);
                } else if (checkedId == smokeState_noCheck.getId()) {
                    smokeState_type.setVisibility(View.GONE);
                }
            }
        });
        drink_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == drinkState_yesCheck.getId()) {
                    drinkState_type.setVisibility(View.VISIBLE);
                } else if (checkedId == drinkState_noCheck.getId()) {
                    drinkState_type.setVisibility(View.GONE);
                }
            }
        });


        medicineAllergy_checkbox_qingmeisu.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.MEDICINEALLERGY));
        medicineAllergy_checkbox_huangan.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.MEDICINEALLERGY));
        medicineAllergy_checkbox_lianmeisu.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.MEDICINEALLERGY));
        medicineAllergy_checkbox_qita.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.MEDICINEALLERGY));

        pastMedicalHistory_checkbox_gaoxueya.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.PASTMEDICALHISTORY));
        pastMedicalHistory_checkbox_tangniaobing.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.PASTMEDICALHISTORY));
        pastMedicalHistory_checkbox_guanxinbing.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.PASTMEDICALHISTORY));
        pastMedicalHistory_checkbox_naozuzhong.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.PASTMEDICALHISTORY));
        pastMedicalHistory_checkbox_jiehebing.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.PASTMEDICALHISTORY));
        pastMedicalHistory_checkbox_exingzhongliu.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.PASTMEDICALHISTORY));
        pastMedicalHistory_checkbox_ganyan.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.PASTMEDICALHISTORY));
        pastMedicalHistory_checkbox_chuanranbing.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.PASTMEDICALHISTORY));
        pastMedicalHistory_checkbox_zzjb.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.PASTMEDICALHISTORY));
        pastMedicalHistory_checkbox_mxfjb.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.PASTMEDICALHISTORY));
        pastMedicalHistory_checkbox_qita.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.PASTMEDICALHISTORY));

        fmh_father_checkbox_gxy.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.FAMILYMEDICALHISTORY_FATHER));
        fmh_father_checkbox_tnb.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.FAMILYMEDICALHISTORY_FATHER));
        fmh_father_checkbox_gxb.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.FAMILYMEDICALHISTORY_FATHER));
        fmh_father_checkbox_gy.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.FAMILYMEDICALHISTORY_FATHER));
        fmh_father_checkbox_jhb.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.FAMILYMEDICALHISTORY_FATHER));
        fmh_father_checkbox_exzl.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.FAMILYMEDICALHISTORY_FATHER));
        fmh_father_checkbox_xtjx.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.FAMILYMEDICALHISTORY_FATHER));
        fmh_father_checkbox_zsxfjb.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.FAMILYMEDICALHISTORY_FATHER));
        fmh_father_checkbox_qt.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.FAMILYMEDICALHISTORY_FATHER));

        fmh_mother_checkbox_gxy.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.FAMILYMEDICALHISTORY_MOTHER));
        fmh_mother_checkbox_tnb.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.FAMILYMEDICALHISTORY_MOTHER));
        fmh_mother_checkbox_gxb.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.FAMILYMEDICALHISTORY_MOTHER));
        fmh_mother_checkbox_gy.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.FAMILYMEDICALHISTORY_MOTHER));
        fmh_mother_checkbox_jhb.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.FAMILYMEDICALHISTORY_MOTHER));
        fmh_mother_checkbox_exzl.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.FAMILYMEDICALHISTORY_MOTHER));
        fmh_mother_checkbox_xtjx.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.FAMILYMEDICALHISTORY_MOTHER));
        fmh_mother_checkbox_zsxfjb.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.FAMILYMEDICALHISTORY_MOTHER));
        fmh_mother_checkbox_qt.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.FAMILYMEDICALHISTORY_MOTHER));

        fmh_sister_checkbox_gxy.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.FAMILYMEDICALHISTORY_SISTER));
        fmh_sister_checkbox_tnb.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.FAMILYMEDICALHISTORY_SISTER));
        fmh_sister_checkbox_gxb.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.FAMILYMEDICALHISTORY_SISTER));
        fmh_sister_checkbox_gy.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.FAMILYMEDICALHISTORY_SISTER));
        fmh_sister_checkbox_jhb.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.FAMILYMEDICALHISTORY_SISTER));
        fmh_sister_checkbox_exzl.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.FAMILYMEDICALHISTORY_SISTER));
        fmh_sister_checkbox_xtjx.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.FAMILYMEDICALHISTORY_SISTER));
        fmh_sister_checkbox_zsxfjb.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.FAMILYMEDICALHISTORY_SISTER));
        fmh_sister_checkbox_qt.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.FAMILYMEDICALHISTORY_SISTER));

        fmh_children_checkbox_gxy.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.FAMILYMEDICALHISTORY_CHILDREN));
        fmh_children_checkbox_tnb.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.FAMILYMEDICALHISTORY_CHILDREN));
        fmh_children_checkbox_gxb.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.FAMILYMEDICALHISTORY_CHILDREN));
        fmh_children_checkbox_gy.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.FAMILYMEDICALHISTORY_CHILDREN));
        fmh_children_checkbox_jhb.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.FAMILYMEDICALHISTORY_CHILDREN));
        fmh_children_checkbox_exzl.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.FAMILYMEDICALHISTORY_CHILDREN));
        fmh_children_checkbox_xtjx.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.FAMILYMEDICALHISTORY_CHILDREN));
        fmh_children_checkbox_zsxfjb.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.FAMILYMEDICALHISTORY_CHILDREN));
        fmh_children_checkbox_qt.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.FAMILYMEDICALHISTORY_CHILDREN));

        gh_checkbox_gxy.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.GENETICHISTORY));
        gh_checkbox_tnb.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.GENETICHISTORY));
        gh_checkbox_gxb.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.GENETICHISTORY));
        gh_checkbox_nzu.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.GENETICHISTORY));
        gh_checkbox_jhb.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.GENETICHISTORY));
        gh_checkbox_exzl.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.GENETICHISTORY));
        gh_checkbox_gy.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.GENETICHISTORY));
        gh_checkbox_crb.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.GENETICHISTORY));
        gh_checkbox_zzjsjb.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.GENETICHISTORY));
        gh_checkbox_zsxfjb.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.GENETICHISTORY));
        gh_checkbox_qt.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.GENETICHISTORY));

        smokeState_checkbox_cb.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.SMOKESTATE));
        smokeState_checkbox_oer.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.SMOKESTATE));
        smokeState_checkbox_jc.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.SMOKESTATE));
        smokeState_checkbox_mt.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.SMOKESTATE));
        smokeState_checkbox_yjy.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.SMOKESTATE));

        drinkState_checkbox_cb.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.DRINKSTATE));
        drinkState_checkbox_oer.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.DRINKSTATE));
        drinkState_checkbox_jc.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.DRINKSTATE));
        drinkState_checkbox_mt.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.DRINKSTATE));
        drinkState_checkbox_yjj.setOnCheckedChangeListener(new MyCheckedChangeListener(Type.DRINKSTATE));

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Size", map.size() + "-------------------");
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    Log.e("TAG", entry.getKey() + "==============" + entry.getValue());
                }
            }
        });
    }

    public void initDatas() {

    }

    public class MyCheckedChangeListener implements CompoundButton.OnCheckedChangeListener {
        private Type type = null;

        public MyCheckedChangeListener(Type type) {
            this.type = type;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch (type) {
                case MARITALSTATUS:
                    // addData(MEDICINEALLERGY_CODE,buttonView,isChecked);
                    break;
                case MEDICINEALLERGY:
                    addData(MEDICINEALLERGY_CODE, buttonView, isChecked);
                    break;
                case PASTMEDICALHISTORY:
                    addData(PASTMEDICALHISTORY_CODE, buttonView, isChecked);
                    break;
                case FAMILYMEDICALHISTORY_FATHER:
                    addData(FAMILYMEDICALHISTORY_FATHER_CODE, buttonView, isChecked);
                    break;
                case FAMILYMEDICALHISTORY_MOTHER:
                    addData(FAMILYMEDICALHISTORY_MOTHER_CODE, buttonView, isChecked);
                    break;
                case FAMILYMEDICALHISTORY_SISTER:
                    addData(FAMILYMEDICALHISTORY_SISTER_CODE, buttonView, isChecked);
                    break;
                case FAMILYMEDICALHISTORY_CHILDREN:
                    addData(FAMILYMEDICALHISTORY_CHILDREN_CODE, buttonView, isChecked);
                    break;
                case GENETICHISTORY:
                    addData(GENETICHISTORY_CODE, buttonView, isChecked);
                    break;
                case DRINKSTATE:
                    resetCheck(Type.DRINKSTATE, buttonView, isChecked);
                    addData(DRINKSTATE_CODE, buttonView, isChecked);
                    break;
                case SMOKESTATE:
                    resetCheck(Type.SMOKESTATE, buttonView, isChecked);
                    addData(SMOKESTATE_CODE, buttonView, isChecked);
                    break;
            }

        }
    }

    public enum Type {
        MARITALSTATUS, MEDICINEALLERGY, PASTMEDICALHISTORY,
        FAMILYMEDICALHISTORY_FATHER, FAMILYMEDICALHISTORY_MOTHER,
        FAMILYMEDICALHISTORY_SISTER, FAMILYMEDICALHISTORY_CHILDREN,
        GENETICHISTORY, SMOKESTATE, DRINKSTATE;
    }


    public void addData(Integer type, CompoundButton buttonView, boolean isChecked) {
        CheckBox box = (CheckBox) buttonView;
        if (isChecked) {
            map.put(type + ":" + box.getText().toString(), box.getText().toString());
        } else {
            if (map.containsKey(type + ":" + box.getText().toString()) && map.containsValue(box.getText().toString())) {
                map.remove(type + ":" + box.getText().toString());
            }
        }
    }

    public void resetCheck(Type type, CompoundButton buttonView, boolean isChecked) {
        CheckBox box = (CheckBox) buttonView;
        int id = box.getId();

        switch (type) {
            case SMOKESTATE:
                if (isChecked) {
                    if (id == smokeState_checkbox_cb.getId()) {
                        smokeState_checkbox_cb.setChecked(true);
                    } else {
                        smokeState_checkbox_cb.setChecked(false);
                    }
                    if (id == smokeState_checkbox_oer.getId()) {
                        smokeState_checkbox_oer.setChecked(true);
                    } else {
                        smokeState_checkbox_oer.setChecked(false);
                    }
                    if (id == smokeState_checkbox_jc.getId()) {
                        smokeState_checkbox_jc.setChecked(true);
                    } else {
                        smokeState_checkbox_jc.setChecked(false);
                    }
                    if (id == smokeState_checkbox_mt.getId()) {
                        smokeState_checkbox_mt.setChecked(true);
                    } else {
                        smokeState_checkbox_mt.setChecked(false);
                    }
                    if (id == smokeState_checkbox_yjy.getId()) {
                        smokeState_checkbox_yjy.setChecked(true);
                    } else {
                        smokeState_checkbox_yjy.setChecked(false);
                    }
                } else {
                    id = 0;
                }
                break;
            case DRINKSTATE:
                if (isChecked) {
                    if (id == drinkState_checkbox_cb.getId()) {
                        drinkState_checkbox_cb.setChecked(true);
                    } else {
                        drinkState_checkbox_cb.setChecked(false);
                    }
                    if (id == drinkState_checkbox_oer.getId()) {
                        drinkState_checkbox_oer.setChecked(true);
                    } else {
                        drinkState_checkbox_oer.setChecked(false);
                    }
                    if (id == drinkState_checkbox_jc.getId()) {
                        drinkState_checkbox_jc.setChecked(true);
                    } else {
                        drinkState_checkbox_jc.setChecked(false);
                    }
                    if (id == drinkState_checkbox_mt.getId()) {
                        drinkState_checkbox_mt.setChecked(true);
                    } else {
                        drinkState_checkbox_mt.setChecked(false);
                    }
                    if (id == drinkState_checkbox_yjj.getId()) {
                        drinkState_checkbox_yjj.setChecked(true);
                    } else {
                        drinkState_checkbox_yjj.setChecked(false);
                    }

                } else {
                    id = 0;
                }
                break;
        }

    }


    public static Fragment getInstance() {
        return new HealthFilesFragment1();
    }

    @Override
    protected void lazyLoad() {

    }
}
