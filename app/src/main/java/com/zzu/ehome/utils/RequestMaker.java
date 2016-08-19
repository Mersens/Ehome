package com.zzu.ehome.utils;


import com.zzu.ehome.application.Constants;
import com.zzu.ehome.bean.Images;
import com.zzu.ehome.view.SexPopupWindows;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xtfgq on 2016/4/5.
 * 统一请求地址
 */
public class RequestMaker {
    private static RequestMaker requestMaker = null;
    private final String SOAP_NAMESPACE = Constants.URL001;
    private final String SOAP_NAMESPACEECG = "http://www.topmd.cn/TopmdWeixin/";
    private final String SOAP_URL = Constants.URL002;

    public static RequestMaker getInstance() {
        if (requestMaker == null) {
            requestMaker = new RequestMaker();
            return requestMaker;
        } else {
            return requestMaker;
        }
    }

    /**
     * 广告位
     *
     * @param date
     * @param task
     */
    public void searchAds(String date, JsonAsyncTask_Info task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><Date>%s</Date><ShowPosition>%s</ShowPosition></Request>";
        str = String.format(str, new Object[]{date, "01"});

        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE +
                        Constants.Ads, Constants.Ads,
                SOAP_URL, paramMap);
    }

    public void getDoctorsRequest(int userid, String IsPMD, String PageIndex, String PageSize, JsonAsyncTask_Info task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><UserID>%s</UserID><DoctorID>%s</DoctorID><HospitalID>%s</HospitalID><IsPMD>%s</IsPMD><PageIndex>%s</PageIndex><PageSize>%s</PageSize></Request>";
        if (userid != 0) {
            str = String.format(str, new Object[]
                    {userid, "", "", IsPMD, PageIndex, PageSize});
        } else {            str = String.format(str, new Object[]
                {"", "", "", IsPMD, PageIndex, PageSize});
        }
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.DoctorInquiry, Constants.DoctorInquiry,
                SOAP_URL, paramMap);
    }

    /**
     * 用户登录
     *
     * @param username
     * @param psd
     * @param ClientID
     * @param task
     */
    public void userLogin(String username, String psd, String ClientID, JsonAsyncTask_Info task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><UserMobile>%s</UserMobile><Password>%s</Password><ClientID>%s</ClientID><PhoneType>%s</PhoneType></Request>";
        str = String.format(str, new Object[]
                {username, psd, ClientID, "0"});
        paramMap.put("str", str);
        // 必须是这5个参数，而且得按照顺序
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.LOGIN, Constants.LOGIN,
                SOAP_URL, paramMap);
    }

    /**
     * 上传头像
     *
     * @param pic
     * @param userid
     * @param filename
     * @param task
     */
    public void uploadPic(String pic, String userid, String filename, JsonAsyncTask_Info task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("UserID", userid);
        paramMap.put("fileName", filename);
        paramMap.put("img", pic);
        task.execute(Constants.URL001Topmd, Constants.URL001Topmd + Constants.UploadUserPhoto, Constants.UploadUserPhoto,
                Constants.URL002Topmd, paramMap);
    }

    public void userRegister(String UserMobile, String password, String ClientID, JsonAsyncTask_Info task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><UserMobile>%s</UserMobile><Password>%s</Password><ClientID>%s</ClientID><FromTo>%s</FromTo><PhoneType>%s</PhoneType></Request>";
        str = String.format(str, new Object[]
                {UserMobile, password, ClientID, "5", "0"});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.UserRegister, Constants.UserRegister,
                SOAP_URL, paramMap);

    }

    public void sendCode(String mobile, JsonAsyncTask_Info task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><UserMobile>%s</UserMobile><Flag>%s</Flag></Request>";
        str = String.format(str, new Object[]
                {mobile, "001"});
        paramMap.put("str", str);

        // 必须是这5个参数，而且得按照顺序
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.SendAuthCode, Constants.SendAuthCode,
                SOAP_URL, paramMap);

    }

    public void userInfo(String userid, String username, JsonAsyncTask_Info task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><UserID>%s</UserID><RealName>%s</RealName></Request>";
        str = String.format(str, new Object[]
                {userid, username});
        paramMap.put("str", str);
        // 必须是这5个参数，而且得按照顺序
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.UserInfoChange, Constants.UserInfoChange,
                SOAP_URL, paramMap);

    }


    public void userInfo(String userid, String username,String sex,String userno,String age, String height, JsonAsyncTask_Info task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><UserID>%s</UserID><RealName>%s</RealName><UserSex>%s</UserSex><UserNO>%s</UserNO><UserAge>%s</UserAge><UserHeight>%s</UserHeight></Request>";
        str = String.format(str, new Object[]
                {userid, username,sex,userno,age,height});
        paramMap.put("str", str);

        // 必须是这5个参数，而且得按照顺序
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.UserInfoChange, Constants.UserInfoChange,
                SOAP_URL, paramMap);

    }
    public void userInfo2(String userid, String height, JsonAsyncTask_Info task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><UserID>%s</UserID><UserHeight>%s</UserHeight></Request>";
        str = String.format(str, new Object[]
                {userid, height});
        paramMap.put("str", str);

        // 必须是这5个参数，而且得按照顺序
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.UserInfoChange, Constants.UserInfoChange,
                SOAP_URL, paramMap);

    }

    /**
     * 用户信息查询
     *
     * @param userid
     * @param task
     */
    public void UserInquiry(String userid, JsonAsyncTask_Info task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><UserID>%s</UserID></Request>";
        str = String.format(str, new Object[]
                {userid});
        paramMap.put("str", str);

        // 必须是这5个参数，而且得按照顺序
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.UserInquiry, Constants.UserInquiry,
                SOAP_URL, paramMap);
    }

    /**
     * 健康数据
     *
     * @param userid
     * @param task
     */
    public void healthInfo(String userid, JsonAsyncTask_Info task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><UserID>%s</UserID></Request>";
        str = String.format(str, new Object[]
                {userid});
        paramMap.put("str", str);

        // 必须是这5个参数，而且得按照顺序
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.HealthDataSearch, Constants.HealthDataSearch,
                SOAP_URL, paramMap);

    }

    public void TemperatureInsert(String userid, String time, String value, JsonAsyncTask_Info task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><UserID>%s</UserID><Value>%s</Value><MonitorTime>%s</MonitorTime></Request>";
        str = String.format(str, new Object[]
                {userid, value, time});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.TemperatureInsert, Constants.TemperatureInsert,
                SOAP_URL, paramMap);

    }

    public void WeightInsert(String userid, String time, String value,JsonAsyncTask_Info task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><UserID>%s</UserID><Weight>%s</Weight><MonitorTime>%s</MonitorTime></Request>";
        str = String.format(str, new Object[]
                {userid, value,time});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.WeightInsert, Constants.WeightInsert,
                SOAP_URL, paramMap);

    }

    public void BloodPressureInsert(String userid, String High, String low, String xinlv, String time, JsonAsyncTask_Info task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><UserID>%s</UserID><High>%s</High><Low>%s</Low><Pulse>%s</Pulse><MonitorTime>%s</MonitorTime></Request>";
        str = String.format(str, new Object[]
                {userid, High, low, xinlv, time});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.BloodPressureInsert, Constants.BloodPressureInsert,
                SOAP_URL, paramMap);

    }
    public void BloodSugarInsert(String userid,String BloodSugarValue,String MonitorPoint,String MonitorTime,String Type,JsonAsyncTask_Info task){
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><UserID>%s</UserID><BloodSugarValue>%s</BloodSugarValue><MonitorPoint>%s</MonitorPoint><MonitorTime>%s</MonitorTime><Type>%s</Type></Request>";
        str = String.format(str, new Object[]
                {userid,BloodSugarValue,MonitorPoint,MonitorTime,Type});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE+ Constants.BloodSugarInsert, Constants.BloodSugarInsert,
                SOAP_URL, paramMap);

    }

    /**
     * 医院信息
     *
     * @param task
     */
    public void HospitalInquiry(JsonAsyncTask_Info task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><HospitalID>%s</HospitalID></Request>";
        str = String.format(str, new Object[]
                {""});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.HOSPITALINQUIRY, Constants.HOSPITALINQUIRY,
                SOAP_URL, paramMap);

    }



    public void DepartmentInquiry(String hosptial_id,JsonAsyncTask_Info task){
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><Department_Id>%s</Department_Id><Hospital_Id>%s</Hospital_Id></Request>";
        str = String.format(str, new Object[]
                {"",hosptial_id});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.DEPARTMENTINQUIRY, Constants.DEPARTMENTINQUIRY,
                SOAP_URL, paramMap);
    }

    public void DoctorInquiry(String department_id,JsonAsyncTask_Info task){
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><Department_Id >%s</Department_Id ><Doctor_Id>%s</Doctor_Id></Request>";
        str = String.format(str, new Object[]
                {department_id,""});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.DOCTORINQUIRY, Constants.DOCTORINQUIRY,
                SOAP_URL, paramMap);
    }

    /**
     * 新建病例
     * @param userid
     * @param AppointmentTime
     * @param Hospital
     * @param Diagnosis
     * @param Opinion
     * @param list
     * @param task
     */
    public void OtherTreatmentInsert(String userid, String AppointmentTime, String Hospital, String Diagnosis, String Opinion, List<Images> list, JsonAsyncTask_Info task){
        Map<String, Object> paramMap = new HashMap<String, Object>();

        String images2="";
        if(list.size()>0) {
            for (int i = 0; i < list.size(); i++) {

                images2 += "<Image>" + "<Code>" + list.get(i).getCode() + "</Code>" + "<FileName>" + list.get(i).getFileName() + "</FileName>" + "</Image>";
            }
        }
        String str = "<Request><User_Id>%s</User_Id><AppointmentTime>%s</AppointmentTime><Hospital>%s</Hospital>" +
                "<Diagnosis>%s</Diagnosis>" +
                "<Opinion>%s</Opinion>%s</Request>";
        str = String.format(str, new Object[]
                {userid,AppointmentTime,Hospital,Diagnosis,Opinion,images2});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.OtherTreatmentInsert, Constants.OtherTreatmentInsert,
                SOAP_URL, paramMap);
    }



    public void ScheduleInquiry(String doctor_id,String PatientId,JsonAsyncTask_Info task){
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><DoctorId>%s</DoctorId ><PatientId>%s</PatientId></Request>";
        str = String.format(str, new Object[]
                {doctor_id,PatientId});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.SCHEDULEINQUIRY, Constants.SCHEDULEINQUIRY,
                SOAP_URL, paramMap);
    }


    public void TreatmentInsert(String user_id,String appointmentTime,String doctor_id,JsonAsyncTask_Info task){
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><Patient_Id>%s</Patient_Id><AppointmentTime>%s</AppointmentTime><Doctor_Id>%s</Doctor_Id></Request>";
        str = String.format(str, new Object[]
                {user_id,appointmentTime,doctor_id});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.TREATMENTINSERT, Constants.TREATMENTINSERT,
                SOAP_URL, paramMap);
    }
    public void TreatmentInquirywWithPage(String usrid,String pagesize,String pageindex,JsonAsyncTask_Info task){
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><UserID>%s</UserID><PageSize>%s</PageSize><PageIndex>%s</PageIndex></Request>";
        str = String.format(str, new Object[]
                {usrid,pagesize,pageindex});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.TreatmentInquirywWithPage, Constants.TreatmentInquirywWithPage,
                SOAP_URL, paramMap);

    }
    public void DoctorInquiry(String department_id,String doctor_id,String userid,JsonAsyncTask_Info task){
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><Department_Id >%s</Department_Id ><Doctor_Id>%s</Doctor_Id><UserID>%s</UserID></Request>";
        str = String.format(str, new Object[]
                {department_id,doctor_id,userid});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.DOCTORINQUIRY, Constants.DOCTORINQUIRY,
                SOAP_URL, paramMap);
    }

    public void FavorDoctorInsert(String UserID ,String DoctorID,JsonAsyncTask_Info task){
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><UserID >%s</UserID ><DoctorID>%s</DoctorID></Request>";
        str = String.format(str, new Object[]
                {UserID,DoctorID});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.FAVORDOCTORINSERT, Constants.FAVORDOCTORINSERT,
                SOAP_URL, paramMap);
    }


    public void FavorDoctorInquiry(String UserID,String pasesize,String pageindex,JsonAsyncTask_Info task){
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><UserID >%s</UserID ><PageSize>%s</PageSize><PageIndex>%s</PageIndex></Request>";
        str = String.format(str, new Object[]
                {UserID,pasesize,pageindex});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.FAVORDOCTORINQUIRY, Constants.FAVORDOCTORINQUIRY,
                SOAP_URL, paramMap);
    }

    public void FeedBackInsert(String UserID ,String Value,JsonAsyncTask_Info task){
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><UserID >%s</UserID ><Value>%s</Value></Request>";
        str = String.format(str, new Object[]
                {UserID,Value});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.FEEDBACKINSERT, Constants.FEEDBACKINSERT,
                SOAP_URL, paramMap);
    }

    public void UserAuthChange(String UserMobile ,String Password,String NewPassword,JsonAsyncTask_Info task){
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><UserMobile >%s</UserMobile ><Password>%s</Password><NewPassword>%s</NewPassword></Request>";
        str = String.format(str, new Object[]
                {UserMobile,Password,NewPassword});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.USERAUTHCHANGE, Constants.USERAUTHCHANGE,
                SOAP_URL, paramMap);
    }
    public void loginOut(String userid,JsonAsyncTask_Info task){
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><UserID>%s</UserID></Request>";
        str = String.format(str, new Object[]
                {userid});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.UserClientIDChange, Constants.UserClientIDChange,
                SOAP_URL, paramMap);

    }
    public void HealthDataInquirywWithPage(String usrid,String pagesize,String pageindex,JsonAsyncTask_Info task){
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><UserID>%s</UserID><PageSize>%s</PageSize><PageIndex>%s</PageIndex></Request>";
        str = String.format(str, new Object[]
                {usrid,pagesize,pageindex});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.HealthDataInquirywWithPage, Constants.HealthDataInquirywWithPage,
                SOAP_URL, paramMap);
    }


    public void UserFindPass(String UserMobile  ,String Password,JsonAsyncTask_Info task){
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><UserMobile >%s</UserMobile ><Password>%s</Password></Request>";
        str = String.format(str, new Object[]
                {UserMobile,Password});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.UserFindPass, Constants.UserFindPass,
                SOAP_URL, paramMap);
    }
    public void TemperatureInquiry(String UserID,String StartTime,String EndTime,JsonAsyncTask_Info task){
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><UserID>%s</UserID><StartTime>%s</StartTime><EndTime>%s</EndTime></Request>";
        str = String.format(str, new Object[]
                {UserID,StartTime,EndTime});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.TemperatureInquiry, Constants.TemperatureInquiry,
                SOAP_URL, paramMap);


    }

    /**
     * 新温度查询
     * @param UserID
     * @param StartTime
     * @param EndTime
     * @param type
     * @param task
     */
    public void TemperatureInquiry(String UserID,String StartTime,String EndTime,String type,JsonAsyncTask_Info task){
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><UserID>%s</UserID><StartTime>%s</StartTime><EndTime>%s</EndTime><Type>%s</Type></Request>";
        str = String.format(str, new Object[]
                {UserID,StartTime,EndTime,type});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.TemperatureInquiry, Constants.TemperatureInquiry,
                SOAP_URL, paramMap);

    }


    public void TreatmentInsert(String DoctorId  ,String PatientId,String DateStr,String TimeSpanStr,String PerTime,JsonAsyncTask_Info task){
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><DoctorId>%s</DoctorId><PatientId>%s</PatientId><DateStr>%s</DateStr><TimeSpanStr>%s</TimeSpanStr><PerTime>%s</PerTime></Request>";
        str = String.format(str, new Object[]
                {DoctorId,PatientId,DateStr,TimeSpanStr,PerTime});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.TREATMENTINSERT, Constants.TREATMENTINSERT,
                SOAP_URL, paramMap);
    }
    public void TreatmentSearch(String PatientId,String start,String end,JsonAsyncTask_Info task){
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><PatientId>%s</PatientId><IntStatus>%s</IntStatus><Start>%s</Start><End>%s</End></Request>";
        str = String.format(str, new Object[]
                {PatientId,"0",start,end});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.TREATMENTSEARCH, Constants.TREATMENTSEARCH,
                SOAP_URL, paramMap);
    }
    public void TreatmentCancel(String ReservationId ,JsonAsyncTask_Info task){
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><ReservationId>%s</ReservationId></Request>";
        str = String.format(str, new Object[]
                {ReservationId});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.TREATMENTCANCEL, Constants.TREATMENTCANCEL,
                SOAP_URL, paramMap);
    }
    public void HealthDataInquirywWithPageType(String usrid,String pagesize,String pageindex,String type,JsonAsyncTask_Info task){
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><UserID>%s</UserID><Type>%s</Type><PageSize>%s</PageSize><PageIndex>%s</PageIndex></Request>";
        str = String.format(str, new Object[]
                {usrid,type,pagesize,pageindex});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.HealthDataInquirywWithPage, Constants.HealthDataInquirywWithPage,
                SOAP_URL, paramMap);
    }
    public void WeightInquiryType(String UserID,String StartTime,String EndTime,JsonAsyncTask_Info task){
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><UserID>%s</UserID><StartTime>%s</StartTime><EndTime>%s</EndTime></Request>";
        str = String.format(str, new Object[]
                {UserID,StartTime,EndTime});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.WeightInquiry, Constants.WeightInquiry,
                SOAP_URL, paramMap);
    }

    /**
     * 新体重查询
     * @param UserID
     * @param StartTime
     * @param EndTime
     * @param type
     * @param task
     */
    public void WeightInquiryType(String UserID,String StartTime,String EndTime,String type,JsonAsyncTask_Info task){
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><UserID>%s</UserID><StartTime>%s</StartTime><EndTime>%s</EndTime><Type>%s</Type></Request>";
        str = String.format(str, new Object[]
                {UserID,StartTime,EndTime,type});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.WeightInquiry, Constants.WeightInquiry,
                SOAP_URL, paramMap);
    }

    public void FavorDoctorDelete(String UserID,String DoctorID,JsonAsyncTask_Info task){
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><UserID>%s</UserID><DoctorID>%s</DoctorID></Request>";
        str = String.format(str, new Object[]
                {UserID,DoctorID});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.FAVORDOCTORDELETE, Constants.FAVORDOCTORDELETE,
                SOAP_URL, paramMap);
    }



    public void BloodPressureInquiryType(String UserID,String StartTime,String EndTime,JsonAsyncTask_Info task){
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><UserID>%s</UserID><StartTime>%s</StartTime><EndTime>%s</EndTime></Request>";
        str = String.format(str, new Object[]
                {UserID,StartTime,EndTime});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.BloodPressureInquiry, Constants.BloodPressureInquiry,
                SOAP_URL, paramMap);
    }

    /**
     * 新血压查询接口
     * @param UserID
     * @param StartTime
     * @param EndTime
     * @param type
     * @param task
     */
    public void BloodPressureInquiryType(String UserID,String StartTime,String EndTime,String type,JsonAsyncTask_Info task){
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><UserID>%s</UserID><StartTime>%s</StartTime><EndTime>%s</EndTime><Type>%s</Type></Request>";
        str = String.format(str, new Object[]
                {UserID,StartTime,EndTime,type});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.BloodPressureInquiry, Constants.BloodPressureInquiry,
                SOAP_URL, paramMap);
    }
    public void BloodSugarInquiryType(String UserID,String StartTime,String EndTime,JsonAsyncTask_Info task){
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><UserID>%s</UserID><StartTime>%s</StartTime><EndTime>%s</EndTime></Request>";
        str = String.format(str, new Object[]
                {UserID,StartTime,EndTime});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.BloodSugarInquiry, Constants.BloodSugarInquiry,
                SOAP_URL, paramMap);
    }

    /**
     *
     * @param UserID
     * @param StartTime
     * @param EndTime
     * @param type
     * @param task
     */
    public void BloodSugarInquiryType(String UserID,String StartTime,String EndTime,String type,JsonAsyncTask_Info task){
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><UserID>%s</UserID><StartTime>%s</StartTime><EndTime>%s</EndTime><Type>%s</Type></Request>";
        str = String.format(str, new Object[]
                {UserID,StartTime,EndTime,type});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.BloodSugarInquiry, Constants.BloodSugarInquiry,
                SOAP_URL, paramMap);
    }
    public void updaateApk(JsonAsyncTask_Info task){

        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request></Request>";

        paramMap.put("str", str);

        // 必须是这5个参数，而且得按照顺序
        task.execute(SOAP_NAMESPACE,  SOAP_NAMESPACE + Constants.VersionInquiry, Constants.VersionInquiry,
                SOAP_URL, paramMap);
    }
    public void BaseDataInsertInsert(String userid, String Marriage, String DrugAllergy,String GeneticHistory,String MedicalHistory,List<String> list,String Smoking,String Drinking, JsonAsyncTask_Info task){
        Map<String, Object> paramMap = new HashMap<String, Object>();

        String familyHistory="";
        if(list!=null && list.size()>0){
        if(list.size()>0) {

            for (int i = 0; i < list.size(); i++) {
                String [] ss=list.get(i).split(":");
                    familyHistory += "<FamilyHistory>" + "<Relation>" + list.get(i).split(":")[0] + "</Relation>" + "<Disease>" + list.get(i).split(":")[1] + "</Disease>" + "</FamilyHistory>";


            }

        }
        }
        String str = "<Request><UserID >%s</UserID><Marriage>%s</Marriage><DrugAllergy>%s</DrugAllergy>" +"<GeneticHistory>%s</GeneticHistory>"+
                "<MedicalHistory>%s</MedicalHistory>" +"%s"+

                "<Smoking>%s</Smoking><Drinking>%s</Drinking></Request>";
        str = String.format(str, new Object[]
                {userid,Marriage,DrugAllergy,GeneticHistory,MedicalHistory,familyHistory,Smoking,Drinking});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.BaseDataInsert, Constants.BaseDataInsert,
                SOAP_URL, paramMap);
    }

    public void BaseDataInquiry(String UserID,JsonAsyncTask_Info task){
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><UserID>%s</UserID></Request>";
        str = String.format(str, new Object[]
                {UserID});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.BASEDATAINQUIRY, Constants.BASEDATAINQUIRY,
                SOAP_URL, paramMap);
    }

    public void BaseDataUpdate(String userid, String Marriage, String DrugAllergy,String GeneticHistory,String MedicalHistory,List<String> list,String Smoking,String Drinking, JsonAsyncTask_Info task){
        Map<String, Object> paramMap = new HashMap<String, Object>();

        String familyHistory="";
        if(list!=null && list.size()>0){
            if(list.size()>0) {
                for (int i = 0; i < list.size(); i++) {

                        familyHistory += "<FamilyHistory>" + "<Relation>" + list.get(i).split(":")[0] + "</Relation>" + "<Disease>" +  list.get(i).split(":")[1] + "</Disease>" + "</FamilyHistory>";

                }
            }
        }
        String str = "<Request><UserID >%s</UserID><Marriage>%s</Marriage><DrugAllergy>%s</DrugAllergy>" +"<GeneticHistory>%s</GeneticHistory>"+
                "<MedicalHistory>%s</MedicalHistory>" +"%s"+

                "<Smoking>%s</Smoking><Drinking>%s</Drinking></Request>";
        str = String.format(str, new Object[]
                {userid,Marriage,DrugAllergy,GeneticHistory,MedicalHistory,familyHistory,Smoking,Drinking});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.BASEDATAUPDATE, Constants.BASEDATAUPDATE,
                SOAP_URL, paramMap);
    }
    public void HolterPDFInquiry(String userid, String startTime, String endTime,JsonAsyncTask_ECGInfo task){
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><UserID>%s</UserID><StartDate></StartDate><EndDate></EndDate></Request>";
        str = String.format(str, new Object[]
                {userid,startTime,endTime});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACEECG, SOAP_NAMESPACEECG + Constants.HolterPDFInquiry, Constants.HolterPDFInquiry,
                Constants.URL003, paramMap);
    }
    public void BJResultInquiry(String userid, String startTime, String endTime,JsonAsyncTask_ECGInfo task){
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><UserID>%s</UserID><StartDate></StartDate><EndDate></EndDate></Request>";
        str = String.format(str, new Object[]
                {userid,startTime,endTime});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACEECG, SOAP_NAMESPACEECG + Constants.BJResultInquiry, Constants.BJResultInquiry,
                Constants.URL003, paramMap);
    }

    /**
     * 指定体检医院查询
     * @param task
     */
    public void MedicalAgencyInquiry(JsonAsyncTask_Info task){
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request></Request>";
        str = String.format(str, new Object[]
                {""});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.MedicalAgencyInquiry, Constants.MedicalAgencyInquiry,
                SOAP_URL, paramMap);
    }
    /**
     * 新建体检

     */
    public void MedicalReportInsert(String userid, String username, String CheckTime, String InstituteName, List<Images> list, JsonAsyncTask_Info task){
        Map<String, Object> paramMap = new HashMap<String, Object>();

        String images2="";
        if(list.size()>0) {
            for (int i = 0; i < list.size(); i++) {

                images2 += "<Image>" + "<Code>" + list.get(i).getCode() + "</Code>" + "<FileName>" + list.get(i).getFileName() + "</FileName>" + "</Image>";
            }
        }
        String str = "<Request><UserID>%s</UserID><UserName>%s</UserName><CheckTime>%s</CheckTime>" +"<InstituteName>%s</InstituteName>"+
                "%s</Request>";
        str = String.format(str, new Object[]
                {userid,username,CheckTime,InstituteName,images2});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.MedicalReportInsert, Constants.MedicalReportInsert,
                SOAP_URL, paramMap);
    }
    /**
     *

     */
    public void MeidicalReportInquiry(String userid, JsonAsyncTask_Info task){
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><UserID>%s</UserID></Request>";
        str = String.format(str, new Object[]
                {userid});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.MeidicalReportInquiry, Constants.MeidicalReportInquiry,
                SOAP_URL, paramMap);
    }

    /**
     * 查询体检详情
     * @param userid
     * @param id
     * @param task
     */
    public void MeidicalReportInquiry(String userid, String id,JsonAsyncTask_Info task){
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><UserID>%s</UserID><ID>%s</ID></Request>";
        str = String.format(str, new Object[]
                {userid,id});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.MeidicalReportInquiry, Constants.MeidicalReportInquiry,
                SOAP_URL, paramMap);
    }
    public void RegionInquiry(JsonAsyncTask_Info task){
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request></Request>";
        str = String.format(str, new Object[]
                {""});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.RegionInquiry, Constants.RegionInquiry,
                SOAP_URL, paramMap);
    }
    public void HospitalInquiryByRegion(String id, JsonAsyncTask_Info task){
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><RegionID>%s</RegionID></Request>";
        str = String.format(str, new Object[]
                {id});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.HospitalInquiryByRegion, Constants.HospitalInquiryByRegion,
                SOAP_URL, paramMap);
    }
    public void HealthDataSearchByDate(String usrid,String date,JsonAsyncTask_Info task){
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><UserID>%s</UserID><Date>%s</Date></Request>";
        str = String.format(str, new Object[]
                {usrid,date});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.HealthDataSearchByDate, Constants.HealthDataSearchByDate,
                SOAP_URL, paramMap);
    }

    public void MedicationRecordInquiry(String usrid,String PageSize,String PageIndex,JsonAsyncTask_Info task){
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><UserID>%s</UserID><PageSize>%s</PageSize><PageIndex>%s</PageIndex></Request>";
        str = String.format(str, new Object[]
                {usrid,PageSize,PageIndex});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.MedicationRecordInquiry, Constants.MedicationRecordInquiry,
                SOAP_URL, paramMap);
    }

    /**
     * 新建用药记录
     */

    public  void MedicationRecordInsert(String UserID,String MedicationTime,String DrugName,String Number,String Remarks,List<Images> list,JsonAsyncTask_Info task){
        Map<String, Object> paramMap = new HashMap<String, Object>();

        String images2="";
        if(list.size()>0) {
            for (int i = 0; i < list.size(); i++) {

                images2 += "<Image>" + "<Code>" + list.get(i).getCode() + "</Code>" + "<FileName>" + list.get(i).getFileName() + "</FileName>" + "</Image>";
            }
        }
        String str = "<Request><UserID>%s</UserID><MedicationTime>%s</MedicationTime><DrugName>%s</DrugName><Number>%s</Number><Remarks>%s</Remarks>" +
                "%s</Request>";
        str = String.format(str, new Object[]
                {UserID,MedicationTime,DrugName,Number,Remarks,images2});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.MedicationRecordInsert, Constants.MedicationRecordInsert,
                SOAP_URL, paramMap);
    }
    public void StepCounterInsert(String usrid,String TotalStep,String TotalDistance ,String TotalTime ,String TotalHeat ,String MonitorTime,JsonAsyncTask_Info task){
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><UserID>%s</UserID><TotalStep>%s</TotalStep><TotalDistance>%s</TotalDistance><TotalTime>%s</TotalTime><TotalHeat>%s</TotalHeat><MonitorTime>%s</MonitorTime></Request>";
        str = String.format(str, new Object[]
                {usrid,TotalStep,TotalDistance,TotalTime,TotalHeat,MonitorTime});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.StepCounterInsert, Constants.StepCounterInsert,
                SOAP_URL, paramMap);
    }
    public void StepCounterInquiry(String usrid,String MonitorTime,String PageSize,String PageIndex,JsonAsyncTask_Info task){
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><UserID>%s</UserID><MonitorTime>%s</MonitorTime><PageSize>%s</PageSize><PageIndex>%s</PageIndex></Request>";
        str = String.format(str, new Object[]
                {usrid,MonitorTime,PageSize,PageIndex});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.StepCounterInquiry, Constants.StepCounterInquiry,
                SOAP_URL, paramMap);
    }
    public void StepCounterInquiry(String usrid,String MonitorTime,JsonAsyncTask_Info task){
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><UserID>%s</UserID><MonitorTime>%s</MonitorTime></Request>";
        str = String.format(str, new Object[]
                {usrid,MonitorTime});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.StepCounterInquiry, Constants.StepCounterInquiry,
                SOAP_URL, paramMap);
    }
    public void WeatherInquiry(String city,JsonAsyncTask_Info task){
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><currentCity>%s</currentCity></Request>";
        str = String.format(str, new Object[]
                {city});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.WeatherInquiry, Constants.WeatherInquiry,
                SOAP_URL, paramMap);
    }
    public void UserRelationshipInsert(String userid,String Relationship,String RUserID,JsonAsyncTask_Info task){
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><UserID>%s</UserID><Relationship>%s</Relationship><RUserID>%s</RUserID></Request>";
        str = String.format(str, new Object[]
                {userid,Relationship,RUserID});
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.UserRelationshipInsert, Constants.UserRelationshipInsert,
                SOAP_URL, paramMap);
    }
    /*
    *录入身高体重 weight
     */
    public void userInfoWeight(String userid, String height,String weight, JsonAsyncTask_Info task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><UserID>%s</UserID><UserHeight>%s</UserHeight><UserWeight>%s</UserWeight></Request>";
        str = String.format(str, new Object[]
                {userid, height,weight});
        paramMap.put("str", str);

        // 必须是这5个参数，而且得按照顺序
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.UserInfoChange, Constants.UserInfoChange,
                SOAP_URL, paramMap);

    }
    public void NewsInquiry(String Pagesize, String PageIndex, JsonAsyncTask_Info task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><PageSize>%s</PageSize><PageIndex>%s</PageIndex></Request>";
        str = String.format(str, new Object[]
                {Pagesize, PageIndex});
        paramMap.put("str", str);

        // 必须是这5个参数，而且得按照顺序
        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.NewsInquiry, Constants.NewsInquiry,
                SOAP_URL, paramMap);

    }
    public void HospitalInquiryByTopmd(JsonAsyncTask_Info task) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request></Request>";

        paramMap.put("str", str);


        task.execute(SOAP_NAMESPACE, SOAP_NAMESPACE + Constants.HospitalInquiryByTopmd, Constants.HospitalInquiryByTopmd,
                SOAP_URL, paramMap);

    }



}
