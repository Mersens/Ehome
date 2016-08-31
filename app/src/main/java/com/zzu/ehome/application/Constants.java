package com.zzu.ehome.application;
import android.os.Environment;

/**
 * Created by Dell on 2016/3/14.
 */
public class Constants {
 //相机常量
 public static final String ACCOUNT_DIR = Environment.getExternalStorageDirectory().getPath();
 public static final int REQUEST_CODE_CAPTURE_CAMEIA = 1458;

 public static final int SHOW_ALL_PICTURE = 0x14;
 public static final int REQUEST_CALENDAR = 1460;
 public static final int ADDTTIME = 0x16;
 public static final int CROP = 2;
 public static final int CROP_PICTURE = 3;
 public static final String IMGPATH = ACCOUNT_DIR;

 public static final String IMAGE_FILE_NAME = "faceImage.jpeg";
 public  static final String TMP_IMAGE_FILE_NAME = "tmp_faceImage.jpeg";
 //性别
 public static final int REQUEST_CODE_SEX = 1460;

 //服务器地址
 public static String EhomeURL = "http://ehome.staging.topmd.cn";
 public static final String	URLIMAGE	= EhomeURL+"/WebServices/News.aspx?id=";
 //名医网地址

 public static String TopmdURL = "http://staging.topmd.cn";
 //添加就诊人
 public static String AddPatient=TopmdURL+"/topmdweixin.asmx";


 //https校验用户名和密码
 public static final String UserName = "netlab";
 public static final String Identify = "n2#4%93142$23Ll";

 public static final String	URL001	= "http://tempuri.org/";
 public static final String	URL002	= EhomeURL+"/WebServices/EhomeWebservice.asmx";

 public static final String NewsIndexInquiry  = "NewsInquiryIndex";
 public static final String DoctorInquiry ="DoctorInquiry";
 //主页小贴士接口
 private final String SOAP_NAMESPACE = Constants.URL001;

 private final String SOAP_NewsIndexInquiryURLMETHODNAME = Constants.NewsIndexInquiry;
 //广告接口
 public static final String Ads ="ADInquiry";
 //登陆接口
 public static final String LOGIN ="UserLogin";
 //名医网头像上传
 public static final String UploadUserPhoto ="UploadUserPhoto";
 //用户注册
 public static final String UserRegister ="UserRegister";
 public static final String SendAuthCode ="SendAuthCode";

 public static final String	URL002Topmd	= "http://staging.topmd.cn/android/TopMD.asmx";



 //头像地址 永远不换
 public static final String	URL001Topmd	= "http://www.topmd.cn/WebServices/";
 //public static final String	JE_BASE_URL3	= "http://staging.topmd.cn";
 public static final String	JE_BASE_URL3	= "http://staging.topmd.cn";
 //心电报告

 public static final String	JE_BASE_URL	= "http://staging.topmd.cn/";
 public static final String	URL003	= JE_BASE_URL3+"/TopmdWeiXin.asmx";

 public static final String	UserInfoChange	= "UserInfoChange";
 public static final String UserInquiry ="UserInquiry";
 public static final String HealthDataSearch ="HealthDataSearch";
 public static final String TemperatureInsert ="TemperatureInsert";
 public static final String WeightInsert ="WeightInsert";
 public static final String BloodPressureInsert ="BloodPressureInsert";
 public static final String Download="http://file.staging.topmd.cn/upload/";
 public static final String BloodSugarInsert ="BloodSugarInsert";

 public static  final String HOSPITALINQUIRY="HospitalInquiry";
 public static  final String DEPARTMENTINQUIRY="DepartmentInquiry";
 public static  final String DOCTORINQUIRY="DoctorInquiry";

 public static  final String OtherTreatmentInsert="OtherTreatmentInsert";

 public static  final String SCHEDULEINQUIRY="ScheduleInquiry";
 public static  final String TREATMENTINSERT="TreatmentInsert";
 public static  final String TreatmentInquirywWithPage="TreatmentInquirywWithPage";
 public static  final String FAVORDOCTORINSERT="FavorDoctorInsert";
 public static  final String FAVORDOCTORINQUIRY="FavorDoctorInquiry";
 public static  final String FEEDBACKINSERT="FeedBackInsert";

 public static final String UserClientIDChange ="UserClientIDChange";
 public static final String HealthDataInquirywWithPage ="HealthDataInquiryWithPage";

 public static  final String USERAUTHCHANGE="UserAuthChange";
 public static  final String UserFindPass="UserFindPass";

 public static  final String TemperatureInquiry="TemperatureInquiry";

 public static  final String TREATMENTSEARCH="TreatmentSearch";
 public static  final String ICON="http://p2d.staging.topmd.cn/Images/headImg/";
 public static  final String TREATMENTCANCEL="TreatmentCancel";
 public static  final String WeightInquiry="WeightInquiry";

 public static  final String FAVORDOCTORDELETE="FavorDoctorDelete";
 public static  final String BloodPressureInquiry="BloodPressureInquiry";
 public static  final String BloodSugarInquiry="BloodSugarInquiry";
 public static final String	DOWNLOADURL	= EhomeURL+"/UpLoadFile/VersionDownload/ehome.apk";
 public static final String	VersionInquiry	= "VersionInquiry";
 public static final String	BaseDataInsert	= "BaseDataInsert";
 public static final String	BASEDATAINQUIRY	= "BaseDataInquiry";
 public static final String	BASEDATAUPDATE	= "BaseDataUpdate";
 public static final String	HolterPDFInquiry	= "HolterPDFInquiry";
 public static final String	BJResultInquiry	= "BJResultInquiry";
 public static final String	MedicalAgencyInquiry	= "MedicalAgencyInquiry";
 public static  final String MedicalReportInsert="MedicalReportInsert";
 public static  final String MeidicalReportInquiry="MeidicalReportInquiry";
 public static  final String RegionInquiry="RegionInquiry";
 public static  final String HospitalInquiryByRegion="HospitalInquiryByRegion";
 public static  final String HealthDataSearchByDate="HealthDataSearchByDate";
 public static  final String MedicationRecordInquiry="MedicationRecordInquiry";
 public static  final String MedicationRecordInsert="MedicationRecordInsert";
 public static  final String StepCounterInsert="StepCounterInsert";
 public static  final String StepCounterInquiry="StepCounterInquiry";
 public static  final String WeatherInquiry="WeatherInquiry";
 public static  final String UserRelationshipInsert="UserRelationshipInsert";
 public static  final String NewsInquiry="NewsInquiry";
 public static  final String HospitalInquiryByTopmd="HospitalInquiryByTopmd";
 public static  final String UserRelationshipInquiry="UserRelationshipInquiry";
 public static  final String HospitalDepertByTopmd ="HospitalDepertByTopmd";
 public static  final String DepertDoctorByTopmd ="DepertDoctorByTopmd";
 public static  final String DoctorSchemaByTopmd ="DoctorSchemaByTopmd";
 public static  final String UserContactorInsert ="UserContactorInsert";



}
