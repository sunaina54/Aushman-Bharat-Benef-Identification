package com.nhpm.Utility;

/**
 * Created by Anand on 20-09-2016.
 */
public interface AppConstant {

    Boolean isKyCEnabled = false;
    String RSBY_DATE_FORMAT = "yyyy-MM-dd";
    String SUBMIT_MEMBER_ADDITIONAL_DATA="https://pmrssm.gov.in/reportapi/submitMemberAdditionalData";
    String DIST_VILLAGE_LOCATION="DistVillageLocation";
    String MOBILE_PARAM="6";
    String VILLAGE_PARAM="7";
    String RATION_PARAM="2";
    String MATCH_SCORE_STATUS="MATCH_SCORE_STATUS";

    String MATCH_SCORE_STATUS_CONFIRM="C";
    String MATCH_SCORE_STATUS_REJECT="R";

    String SESSION_EXPIRED="N-404";
    String INVALID_TOKEN="N-401";
    String APPLICATION_ID="3";
    String TEST_DOMAIN="https://pmrssm.gov.in/reportapi/";
    int FAMILY_MEMBER_REQUEST_CODE_VALUE = 11;
    String dateTimeFormate = "yyyy-MM-dd";
    String VIEW_DATA = "VIEW_DATA";
    //String AUTO_SUGGEST="https://pmrssm.gov.in/SE/suggest";
    String AUTO_SUGGEST="https://pmrssm.gov.in/reportapi/suggest";
    String FAMILY_MEMBER_RESULT_CODE_NAME="FamilyMember";
    String WORNG_PIN_ENTERED_TIMESTAMP = "wornPinEnteredTimeStamp";
    String TOKEN_VALUE_AADHAAR = "Bearer 1139a002-5856-39e5-b51e-a2ad3d59efd7";
    String AUTHORIZATION = "Authorization";
    String AUTHORIZATIONVALUE = "Bearer 1139a002-5856-39e5-b51e-a2ad3d59efd7";
    String SPLUNK_MINT_ID = "5e33a5e1";

//    String SPLUNK_MINT_ID = "e2d2eff1";//psquikit

    String SELECTED_HEAD_GENDER_ID = "headGenderID";
    String SELECTED_MEMBER_GENDER_ID = "memberGenderID";
    String REGISTER_MODE = "REGISTER_MODE";
    String USER_DATA_TAG = "USER_DATA";
    String AADHAAR_DATA_TAG = "AADHAAR_DATA";
    String MOBILE_DATA = "MOBILE_DATA";

    String RSBY_POLICIES_DATA_DATE_FORMAT = "dd/MM/yyyy";
    String MENUTIA_DETAIL_AVAILABLE = "YES";
    String MENUTIA_DETAIL_NOT_AVAILABLE = "NO";
    String RSBY_POLICIES_TABLE = "rsby_policies_params_main_filter";
    String RSBY_RELATION_TABLE = "rsby_relation_master";
    String USER_DATA_TABLE = "app_credentials";
    String RSBY_ISSUES_TIME_STAMP_DATE_FORMAT = "yyyy-MM-dd hh:mm:ss";
    String DATE_FORMAT = "yyyy-mm-dd";
    String NOTIFICATION_DATE_FORMAT = "MMM dd, yyyy";
    String ACCESS_DENIED_ERROR = "Access denied ! Please registered with NHPS You are not valid use ";
    int COMPARED_YEAR = 2016;
    int cancelSyncService = 90909090;
    String cancelSyncMsg = "CANCELSYNC";
    int ADD_OPR = 0;
    int UPDATE_OPR = 1;
    String NHS_DATA_SYNC_TABLE = "";
    String PROJECT_PREF = "COM.FIELDVERIFY";
    String SESSION_EXPIRE_INVAILD_TOKEN="SessionExpire";
    String LOG_REQUEST="logRequest";
    String APPLICATION_DATA = "APPLICATION_DATA";
    String HOUSEHOLD_TAB_STATUS = "houseHoldTabClick";
    String MEMBER_TAB_STATUS = "memberTabClick";
    String DASHBOARD_TAB_STATUS = "dashboardTabClick";
    String HOUSEHOLD_SORT_STATUS = "householdSortStatus";
    String MEMBER_SORT_STATUS = "memberSortStatus";
    String MASTER_LOC_CONTENT = "masterLoc";
    String AADHAAR_SEARCH = "searchByAadhaar";
    String EKYC = "EKYC";
    String DEMO = "DEMO";
    String MEMBERS_NAME = "membersName";
    String MALE = "Male";
    String MALE_GENDER = "1";
    String FEMALE_GENDER = "2";
    String OTHER_GENDER = "3";
    String FEMALE = "Female";
    String OTHER_GENDER_NAME = "OTHER";
    String SELECTED_MEMBER = "selectedMemberItem";
    String VERIFIER_CONTENT = "verifierContent";
    String VERIFIER_USERNAME_CONTENT = "verifierUserNameContent";
    String SECC_MEMBER_CONTENT = "seccMemberContent";
    String SECC_HOUSE_HOLD_CONTENT = "seccHouseholdContent";
    String SELECTED_BLOCK = "selectedBlock";
    String AADHAAR_CAPTURE_ITEM = "aadhaarCapture";
    String SELECTED_ITEM_FOR_VERIFICATION = "selectedItemForVerification";
    String APPLICATIONLANGUAGE = "applicationLanguage";
    String SELECTED_CARDREADER = "SELECTEDCARDREADER";
    String ACS = "ACS";
    String SCM = "SCM";
    String OMIKEY = "OMIKEY";
    String RSBY_VALIDATION = "rsbyValidation";
    String AADHAAR_AUTH_YES = "Y";
    String AADHAAR_AUTH_NO = "N";
    String OFFLINE_MODE_CODE = "-1";
    String OFFLINE_MODE = "offline mode";
    int WITH_AADHAAR = 0;
    int WITH_OUT_AADHAAR = 1;
    String NEVIGATE = "navigateType";
    String MEMBER_TAB = "dashBordMemTab";
    int PARTIAL_VERIFIED_TAB = 2;
    int VERIFIED_TAB = 3;
    int PARTIAL_RSBY_TAB = 1;
    String RSBY_CONTENT = "rsbyContent";
    String MEMBER_TYPE = "memberType";
    int RSBY_MEMBER = 0;
    int SECC_MEMBER = 1;
    String RURAL = "R", URBUN = "U";

    String AADHAAR_ALREADY_ALLOCATED = "AADHAAR_ALREADY_EXIST";
    String URI_ALREADY_EXIST = "PRIMARY_KEY_VIOLATION";
    String VERIFIER_AADHAAR_ALLOCATED = "VERIFIER_AADHAAR_EXIST";
    String SERVER_CONNECTION_TIMEOUT = "Server connection timeout";
    String AADHAAR_VALIDATION_ERROR = "Aadhaar Validation Error";
    String INVALID_AADHAAR_MG = "Invalid Aadhaar";
    String AADHAAR_ALREADY_ALLOCATED_MSG = "Duplicate Aadhaar";
    String VERIFIER_AADHAAR_ALLOCATED_MSG = "Verfier Aadhaar Found";
    String SERVER_CONNECTION_TIMEOUT_MSG = "Server connection timeout";

    String INTERNET_ERROR_CODE = "INTERNET_EEROR_CODE";
    String INTERNET_ERROR_MSG = "Internet connection error";
    String SYNCING_ERROR = "Syncing Error";
    String SYNCING_MEMBER_ERROR = "Member Sync Error";
    //TABLES
    String TRANSECT_POPULATE_SECC = "pop_secc_output_idea_schema_main_filter";
    String HOUSE_HOLD_SECC = "secc_household_params_main_filter";
    String AADHAAR_STATUS = "m_aadhaar_status";
    String MEMBER_STATUS = "m_hh_mem_status";
    String HEALTH_SCHEME = "m_state_schemes";
    String RELATION_TABLE = "m_relation";
    String MEMBER_ERROR_TABLE = "member_error";
    String RSBY_POPULATION_TABLE = "pop_rsby_output_idea_schema_main_filter";
    String RSBY_HOUSEHOD_TABLE = "rsby_household_params_main_filter";
    String RSBY_CARD_CAT_MASTER_TABLE = "rsby_category_master";
    String RSBY_POLICY_COMPANY = "rsby_policies_master";
    String NOTIFICATION_TABLE = "t_notification";

    //String PROD_URL="http://164.100.162.62/";

    // API
    // nhps_service/nhps/test/verifier/login
    /*String PROD_DOMAIN_NEW_DEPLOYMENT = "http://164.100.161.62/nhps_service_new/";*/
    String PROD_DOMAIN_NEW_DEPLOYMENT = "http://164.100.161.62/nhps_service/";
    String NEW_PROD_DOMAIN_NEW_DEPLOYMENT = "http://164.100.58.98/";

    String PROD_DOMAIN = "http://164.100.161.62/nhps_service/";
  //  String TEST_DOMAIN = "http://103.241.181.83:8080/" +"nhps_service/";
    String LOCAL_DOMAIN = "http://192.168.2.81:8080/" +
            "service/";
    //String MAIN_DOMAIN=PROD_DOMAIN_NEW_DEPLOYMENT;
    String MAIN_DOMAIN = PROD_DOMAIN_NEW_DEPLOYMENT;
    String NEW_MAIN_DOMAIN = NEW_PROD_DOMAIN_NEW_DEPLOYMENT;
    String NEW_MAIN_DOMAIN_UPDATED = TEST_DOMAIN;

    // String LOGIN_API = MAIN_DOMAIN + "nhps/test/verifier/login";
    //String LOGIN_API = NEW_MAIN_DOMAIN + "authenticateVerifier/1.0.0/authenticateVerifier";
    String LOGIN_API = NEW_MAIN_DOMAIN_UPDATED + "login";

    String USER_NAME_LOGIN = MAIN_DOMAIN + "nhps/verifier/login";
    //AADHAAR AUTH API OLDER VERSION
    String AADHAR_OTP_AUTH_API = "http://164.100.161.62/health/getOtp/";
    String AADHAAR_KYC_DATA = "http://164.100.161.62/health/getKycData/";

    //AADHAAR AUTH API NEW VERSION
    String AADHAR_OTP_AUTH_API_NEW = "http://164.100.161.62/health_new/getOtp";
    String AADHAAR_KYC_DATA_API_NEW = "http://164.100.161.62/health_new/getKycData";

    String AADHAAR_REQUEST_FOR_OTP = "http://164.100.58.98:80/getOtpResult/1.0.0/getOtpResult";

    String USER_ID = "/nicuser";
    String PASSWORD = "/nicpass";


/*
    String RSBY_MEMBER_DOWNLOAD = "http://localhost:8080/service/nhps/rsby/rsbyMembers";
    String RSBY_HOUSEHOLD_DOWNLOAD = "http://localhost:8080/service/nhps/rsby/rsbyHouseHold";*/

   /* String SECC_HOUSE_HOLD_API=MAIN_DOMAIN+"nhps/secc/houseHold";
    String SECC_MEMBER_API=MAIN_DOMAIN+"nhps/secc/seccMembers";*/
    //  http://103.241.181.83:8080/nhps_service/nhps/app/updatedVersion
//http://164.100.161.62/health/getOtpFromServer/1/mobileno/sequenceNo/Otp/Nic@nhps123/Nhps@123
    //http://164.100.161.62/health/sendRequestForOtp/0/9999341830/Nic@nhps123/Nhps@123


    ///////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////


    //String UPDATE_PIN = MAIN_DOMAIN + "nhps/verifier/updatePin";
   // String UPDATE_PIN = NEW_MAIN_DOMAIN + "updatePin/1.0.0/updatePin";
    String UPDATE_PIN = NEW_MAIN_DOMAIN_UPDATED + "updatePin";

    //String HEALTH_SCHEME_API = MAIN_DOMAIN + "nhps/master/stateHealthScheme";
    String HEALTH_SCHEME_API = NEW_MAIN_DOMAIN + "stateSchemes/1.0.0/stateSchemes";

    //String MEMBER_STATUS_API = MAIN_DOMAIN + "nhps/master/memberStatus";
    String RELATION_MASTER_API = NEW_MAIN_DOMAIN + "relationList/1.0.0/relationList";

    //String MEMBER_STATUS_API = MAIN_DOMAIN + "nhps/master/memberStatus";
    String MEMBER_STATUS_API = NEW_MAIN_DOMAIN + "hhPopMemStatus/1.0.0/hhPopMemStatus";

    //String SECC_HOUSE_HOLD_API = MAIN_DOMAIN + "nhps/secc/houseHold";
    String SECC_HOUSE_HOLD_API = NEW_MAIN_DOMAIN + "householdSeccRead/1.0.0/householdSeccRead";

    //String SECC_MEMBER_API = MAIN_DOMAIN + "nhps/secc/seccMembers";
    String SECC_MEMBER_API_HIERARECY_WISE = "http://164.100.58.98:80/populationSeccReadLevel/1.0.0/populationSeccReadLevel";

    //String SECC_HOUSE_HOLD_API = MAIN_DOMAIN + "nhps/secc/houseHold";
    String SECC_HOUSE_HOLD_API_HIERARECY_WISE = "http://164.100.58.98:80/householdSeccReadLevel/1.0.0/householdSeccReadLevel";

    //String SECC_MEMBER_API = MAIN_DOMAIN + "nhps/secc/seccMembers";
    String SECC_MEMBER_API = NEW_MAIN_DOMAIN + "populationSeccRead/1.0.0/populationSeccRead";

    //String SYNC_MEMBER_API = MAIN_DOMAIN + "nhps/secc/updateSeccMember";
    String SYNC_MEMBER_API = NEW_MAIN_DOMAIN + "popSeccWrite/1.0.0/popSeccWrite";

    //String HOUSE_HOLD_API = MAIN_DOMAIN + "nhps/secc/updateHousehold";
    String HOUSE_HOLD_API = NEW_MAIN_DOMAIN + "houseHoldSeccWrite/1.0.0/houseHoldSeccWrite";

    //String APPLICATION_NOTIFICATION_URL = "http://164.100.161.62/nhps_service/nhps/master/notifications";
    //String APPLICATION_NOTIFICATION_URL = "http://164.100.58.98:80/notificationList/1.0.0/notificationList?stateCode=";
    String APPLICATION_NOTIFICATION_URL = NEW_MAIN_DOMAIN_UPDATED + "notification/notificationList?stateCode=";

    String AADHAAR_STATUS_API = MAIN_DOMAIN + "nhps/master/aadhaarStatus";
    String SECC_MEMBER_DOWNLOAD_API = MAIN_DOMAIN + "nhps/secc/members";
    String SECC_HOUSEHOLD_DOWNLOAD_API = MAIN_DOMAIN + "nhps/secc/houseHolds";
    String SEARCH_FAMILY_LIST = NEW_MAIN_DOMAIN_UPDATED+"search";
    String SEARCH_BY_RATION=NEW_MAIN_DOMAIN_UPDATED+"searchByRation";
    String VALIDATE_URN = "https://pmrssm.gov.in/VIEWSTAT/api/validateURN";
    String SEARCH_BY_MOBILE_RATION =  NEW_MAIN_DOMAIN_UPDATED+"searchByMobileRationParameter";
   // String SEARCH_BY_VILLAGE =  "https://pmrssm.gov.in/reportapi/searchByMobileRationParameter";
    String GET_MEMBER_DETAIL="https://pmrssm.gov.in/reportapi/getMemberDetail";

    String VILLAGE_WISE_HOUSEHOLD_DOWNLOADING = MAIN_DOMAIN + "nhps/secc/village/houseHold";
    String VILLAGE_WISE_MEMBER_DOWNLOADING = MAIN_DOMAIN + "nhps/secc/village/seccMembers";
    //String RSBY_MEMBER_DOWNLOAD = MAIN_DOMAIN + "nhps/rsby/rsbyMembers";
    String RSBY_MEMBER_DOWNLOAD = "http://164.100.58.98:80/rsbyPopulationRead/1.0.0/rsbyPopulationRead";
    //String RSBY_HOUSEHOLD_DOWNLOAD = MAIN_DOMAIN + "nhps/rsby/rsbyHouseHold";
    String RSBY_HOUSEHOLD_DOWNLOAD = "http://164.100.58.98:80/rsbyHouseholdRead/1.0.0/rsbyHouseholdRead";
    String SYNC_RSBY_MEMBER = "http://164.100.58.98:80/rsbyPopulationWrite/1.0.0/rsbyPopulationWrite";
    String SYNC_RSBY_HOUSEHOLD = "http://164.100.58.98:80/rsbyHouseholdWrite/1.0.0/rsbyHouseholdWrite";


    String MASTER_DPWNLOAD_API = MAIN_DOMAIN + "nhps/master/masterList";
    String APP_UPDATE_URL = "http://164.100.161.62/NHPS/apospospospospsopso";
    String GET_UPDATE_VERSION = TEST_DOMAIN + "nhps/app/updatedVersion";
    String AADHAAR_DEMO_AUTH_API = "http://164.100.161.62/health/getAuthByName/";
    String AADHAAR_DEMO_AUTH_API_NEW = "http://164.100.161.62/health_new/aadhaarDemoAuth";
    String MOBILE_OTP_REQUEST = "http://164.100.161.62/health/sendRequestForOtp/0/";
    String AUTH_MOBILE_OTP = "http://164.100.161.62/health/getOtpFromServer/1/";
    String GET_DATA_DOWNLOAD_COUNT = "http://164.100.58.98:80/dataCount/1.0.0/dataCount";
  //  String APPLICATION_CONFIGURATION_URL = "http://164.100.58.98:80/appStateConfig/1.0.0/appStateConfig?stateCode=";
    String APPLICATION_CONFIGURATION_URL = NEW_MAIN_DOMAIN_UPDATED + "appStateConfig?stateCode=";
    String GET_STATE_MASTER_DATA = NEW_MAIN_DOMAIN_UPDATED + "state";
    String VERIFY_VALIDATOR = "http://164.100.58.98/authVerifierWithPin/1.0.0/authVerifierWithPin";
   // String GET_NAME_MATCH_SCORE="http://10.247.173.73/NameMatcherHISP/NameMatch/GetTotalScore";
    //String GET_NAME_MATCH_SCORE="https://pmrssm.gov.in/reportapi2/nameMatch/compareString";
    String GET_NAME_MATCH_SCORE="https://pmrssm.gov.in/reportapi/nameMatch/getTotalScore";
    //String GET_FAMILY_MATCH_SCORE="http://10.247.173.73/NameMatcherHISP/NameMatch/ GetFamilyScore";
    String GET_FAMILY_MATCH_SCORE="https://pmrssm.gov.in/reportapi/nameMatch/GetFamilyScore";


    String GET_RSBY_CATEGORY_MASTER = "http://164.100.58.98/rsbyCategory/1.0.0/rsbyCategory";
    String GET_RSBY_POLICIES_MASTER = "http://164.100.58.98/rsbyPolicies/1.0.0/rsbyPolicies";
    String GET_RSBY_INSURANCE_COMPANY_MASTER = "http://164.100.58.98/rsbyInsuranceCompany/1.0.0/rsbyInsuranceCompany";
    String GET_RSBY_RELATION_MASTER = "http://164.100.58.98/rsbyRelation/1.0.0/rsbyRelation";
    String REQUEST_FOR_MOBILE_OTP = "http://164.100.58.98:80/sendMessage/1.0.0/sendMessage";
    String REQUEST_FOR_OTP_VERIFICATION = "http://164.100.58.98:80/verifyOtp/1.0.0/verifyOtp";
    String REQUEST_FOR_OTP_VERIFICATION_GATEWAY = NEW_MAIN_DOMAIN_UPDATED + "verifyLogin";
    String REQUEST_FOR_OTP_EKYC = "http://164.100.58.98:80/getOtpKyc/1.0.0/getOtpKyc";
    String REQUEST_FOR_OTP_EKYC1 = "http://164.100.58.98:80/getOtpKycRD/1.0.0/getOtpKycRD";
    String REQUEST_FOR_OTP_AUTH_OLD = "http://164.100.58.98:80/getAuth/1.0.0/getAuth";
    String REQUEST_FOR_OTP_AUTH = "http://164.100.58.98:80/getDemoAuthRD/1.0.0/getDemoAuthRD";
    String REQUEST_FOR_KYC_VIA_RD_SERVICES = "http://164.100.58.98/aadhaarKycRD/1.0.0/aadhaarKycRD";
    String RSBY_URN_SEARCH = "http://rsby.gov.in/UrnSearch/MyRESTService.svc/Dependent/rsbyurn/17e636a5bc2769c5418104508c3d6def/";
    //    String SYNC_RSBY_MEMBER = MAIN_DOMAIN + "nhps/secc/addRsbyMember";
//    String SYNC_RSBY_HOUSEHOLD = MAIN_DOMAIN + "nhps/secc/addRsbyHouseHold";
    //String GET_EBLOCK_COUNT = MAIN_DOMAIN + "nhps/secc/block/downloadCount";
    //  String GET_VILLAGE_COUNT = MAIN_DOMAIN + "nhps/secc/village/downloadCount";
    // String GET_VILLAGE_COUNT = MAIN_DOMAIN + "nhps/secc/rsby/downloadCount";
    // String GET_EBLOCK_COUNT = MAIN_DOMAIN + "nhps/secc/rsby/downloadCount";
    ///////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////

/*

    String UPDATE_PIN = MAIN_DOMAIN + "nhps/verifier/updatePin";
    String HEALTH_SCHEME_API = MAIN_DOMAIN + "nhps/master/stateHealthScheme";
    String MEMBER_STATUS_API = MAIN_DOMAIN + "nhps/master/memberStatus";
    String AADHAAR_STATUS_API = MAIN_DOMAIN + "nhps/master/aadhaarStatus";
    String VILLAGE_WISE_HOUSEHOLD_DOWNLOADING = MAIN_DOMAIN + "nhps/secc/village/houseHold";
    String VILLAGE_WISE_MEMBER_DOWNLOADING = MAIN_DOMAIN + "nhps/secc/village/seccMembers";
    String RSBY_MEMBER_DOWNLOAD = MAIN_DOMAIN + "nhps/rsby/rsbyMembers";
    String RSBY_HOUSEHOLD_DOWNLOAD = MAIN_DOMAIN + "nhps/rsby/rsbyHouseHold";
    String SECC_HOUSE_HOLD_API = MAIN_DOMAIN + "nhps/secc/houseHold";
    String SECC_MEMBER_API = MAIN_DOMAIN + "nhps/secc/seccMembers";
    String MASTER_DPWNLOAD_API = MAIN_DOMAIN + "nhps/master/masterList";
    String APP_UPDATE_URL = "http://164.100.161.62/NHPS/apospospospospsopso";
    String GET_UPDATE_VERSION = TEST_DOMAIN + "nhps/app/updatedVersion";
    String AADHAAR_DEMO_AUTH_API = "http://164.100.161.62/health/getAuthByName/";
    String AADHAAR_DEMO_AUTH_API_NEW = "http://164.100.161.62/health_new/aadhaarDemoAuth";
    String MOBILE_OTP_REQUEST = "http://164.100.161.62/health/sendRequestForOtp/0/";
    String AUTH_MOBILE_OTP = "http://164.100.161.62/health/getOtpFromServer/1/";
    String APPLICATION_NOTIFICATION_URL = "http://164.100.161.62/nhps_service/nhps/master/notifications";
    String SYNC_MEMBER_API = MAIN_DOMAIN + "nhps/secc/updateSeccMember";
    String HOUSE_HOLD_API = MAIN_DOMAIN + "nhps/secc/updateHousehold";
    String SYNC_RSBY_MEMBER = MAIN_DOMAIN + "nhps/secc/addRsbyMember";
    String SYNC_RSBY_HOUSEHOLD = MAIN_DOMAIN + "nhps/secc/addRsbyHouseHold";
    String RSBY_URN_SEARCH = "http://rsby.gov.in/UrnSearch/MyRESTService.svc/Dependent/rsbyurn/17e636a5bc2769c5418104508c3d6def/";

*/


    ///////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////


    /* String SYNC_RSBY_MEMBER="http://192.168.0.23:8080/service/nhps/secc/addRsbyMember";
     String SYNC_RSBY_HOUSEHOLD="http://192.168.0.23:8080/service/nhps/secc/addRsbyHouseHold";
 */
    String MOBILE_OTP_USERID = "/Nic@nhps123";
    String MOBILE_OTP_PASS = "/Nhps@123";

    /* String SYNC_MEMBER_API=MAIN_DOMAIN+"nhps/secc/updateSeccMember";
     String HOUSE_HOLD_API=MAIN_DOMAIN+"nhps/secc/updateHousehold";*/
    // http://rsby.gov.in/UrnSearch/MyRESTService.svc/Dependent/rsbyurn/17e636a5bc2769c5418104508c3d6def/15081202216000092
    //9999341830;
    //399734158175/152467/nicuser/nicpass
    //399734158175

    String PENDING_STATUS = "P";
    String INVALID_STATUS = "N";
    String VALID_STATUS = "Y";
    String MANUAL_MODE = "M";
    String QR_CODE_MODE = "Q";
    String IRIS_MODE = "I";
    String FINGER_MODE = "F";

    String DEMO_AUTH = "1";
    String OTP_AUTH = "2";
    String FPD_AUTH = "3";
    String IRISH_AUTH = "4";

    int LOCKED = 1;
    int SAVE = 2;
    int YET_TOBE_SURVEYED = 1;
    int UNDER_VALIDATION = 2;
    int SYNCED = 3;
    int VALDATED = 4;
    int UNDER_SURVEYED = 5;

    int SURVEYED = 1;
    int NOT_SURVEYED = 0;
    String SYNCED_STATUS = "1";
    /*  String SYNCED_STATUS_MEMBER = "Y";*/
    String SYNCED_STATUS_MEMBER = "1";

    String AADHAAR_STAT = "1";
    String GOVT_ID_STAT = "2";
    String GOVT_STATUS = "govt_id";
    String AADHAR_STATUS = "aadhar";



    String SECC_SOURCE = "001";
    String SECC_SOURCE_NEW = "01";
    String SECC_SOURCE_NAME = "SECC";
    String RSBY_SOURCE = "002";
    String RSBY_SOURCE_NEW = "02";
    String RSBY_SOURCE_NAME = "RSBY";
    String SCHEME_CODE = "001"; //-NHPS
    // String HOUSEHOLD_FOUND="1";
    String MEMBER_FOUND = "4";
    String ENROLLMENT_ID = "1";
    String VOTER_ID = "2";
    String RASHAN_CARD = "3";
    String AUTH_TOKEN = "authToken";
    String RELEASE_DATE = "June 26, 2018";
    String NEW_HEAD_RELATION_CODE = "01";
    String NO_INFO_AVAIL = "7";
    String invalid_user = "INVALID_USER";
    String invalid_imei = "INVALID_IMEI_NUMBER";
    String user_status = "Validator";
    boolean LOG_STATUS = true;
    int rsbyDataCode = 999;
    int rsbyDataPreviewCode = 888;
    String RSBYCARDRESPONSE = "CardResponse";
    String RSBYCARDPREVIEWRESPONSE = "CardPreviewResponse";
    String RESPONSE_BODY = "response";
    String HOUSEHOLD_FOUND_BUTNOT_INTERESTED = "13";
    String NO_FAMILY_LIVING = "2";
    String DEFAULT_HOUSEHOLD = "0";
    String HOUSEHOLD_FOUND = "1";
    String HOUSEHOLD_LOCKED = "3";
    String FAMILY_MIGRATED = "8";
    String HOUSEHOLD_TO_ENROLL_RSBY = "9";
    //String HOUSEHOLD_NOT_FOUND="2";
    String NO_INFO_AVAILABLE = "7";
    String DEFAULT_MEMBER_STATUS = "0";
    String MEMBERFOUND_BUT_NOT_PRESENT = "10";
    String MEMBERFOUND_AND_PRESENT = "4";
    String MEMBER_NOT_FOUND = "11";
    String MEMBER_MIGRATED = "5";
    String MEMBER_ENROL_THROUGH_RSBY = "12";
    String OTHER_RELATION = "19";
    String DEFAULT_RELATION = "00";
    String HEAD_RELATION = "01";
    String UNKNOWN_RELATION = "95";
    String NON_AADHAAR_SEARCH="NON_AADHAAR_SERACH";
    String PROJECT_NAME = "PM-RSSM";
    String SEARCH_OPTION = "SEARCH-OPTION-DATA";
    String SYNC_DATE_TIME = "yyyy-MM-dd HH:mm:ss";
    String SELF = "0", OTHER = "2", FAMILY = "1";
    String RESET = "reset", UNLOCK = "edit";
    int YET_TO_SURVEY = 0;
    int capturingModePhoto = 1;
    int capturingModeGovId = 2;
    String sendingPrintData = "SENDINGPRINTDATA";
    String homeNavigation = "HOMENAVIGATION";
    String downloadActivityNavigation = "DOWNLOADSCREENNAVIGATION";
    String blockDetailActivityNavigation = "BLOCKDETAILNAVIGATION";
    String dataDownloaded = "DATADOWNLOADED";
    // String SYNC_RSBY_MEMBER="http://192.168.0.19:8080/service/nhps/secc/addRsbyMember";
    // String SYNC_RSBY_HOUSEHOLD="http://192.168.0.19:8080/service/nhps/secc/addRsbyHouseHold";

    /*String LOGIN_CONFIG = "1";
    String RSBY_DATA_SOURCE_CONFIG = "2";
    String VALIDATION_MODE_CONFIG = "3";
    String PRINT_CARD = "4";
    String ADDITIONAL_SCHEME = "5";
    String AADHAR_AUTH = "6";
    String BIOGRAPHIC_AUTH = "12";
    String DATA_DOWNLOAD = "7";
    String PHOTO_COLLECT = "8";
    String NOMINEE_COLLECT = "9";
    String SECC_DOWNLOAD = "10";
    String APPLICATION_ZOOM = "11";*/

    String SECC_DOWNLOAD = "1";
    String RSBY_DATA_SOURCE_CONFIG = "2";
    String DATA_DOWNLOAD = "3";
    String PHOTO_COLLECT = "4";
    String VALIDATION_MODE_CONFIG = "5";
    String AADHAR_AUTH = "6";
    String BIOGRAPHIC_AUTH = "7";
    String NOMINEE_COLLECT = "8";
    String ADDITIONAL_SCHEME = "9";
    String PRINT_CARD = "10";
    String APPLICATION_ZOOM = "11";
    String LOGIN_CONFIG = "12";
    String DATA_CATEGORY = "13";
    String LOGIN_STATUS = "N";
    String EKYC_SOURCE_CONFIG = "14";
    String DEMOGRAPHIC_SOURCE_CONFIG = "15";
    String m_state = "m_state";
    //String app_config = "app_config";
    String new_application_configuration = "application_configuration";
    String flow_log="l_flowlog";
    String SELECTED_STATE = "selectedState";
    String SELECTED_STATE_SEARCH = "selectedStateSearch";
    String LOGIN_TYPE_AADHAR = "1";
    String LOGIN_TYPE_EMAIL = "4";
    String LOGIN_TYPE_MOBILE_NUMBER = "";
    String LOGIN_TYPE_USERNAME = "3";
    String SESSIONTIMEOUT = "sessionTimeOut";
    String DEMOAUTH_AADHAR_DATE_FORMAT = "YYYY-MM-DD";
    String AUTHITEM = "AUTHITEM";
    String SESSIONTIME = "30";
    String DOWNLOADINGSOURCE = "DOWNLOADINGSOURCE";
    long TIMMERTIME = 3000;
    String AUTHTYPESELECTED = "AUTHTYPESELETED";
    String IRIS = "IRIS";
    String FINGER = "FINGER";
    String OTP = "OTP";
    String QRCODE = "QRCODE";
    String MANUALAUTH = "MANUAL";
    String MEMBER_DOWNLOADED_COUNT = "MEMBERCOUNT";
    String HOUSEHOLD_DOWNLOADED_COUNT = "HOUSEHOLDCOUNT";
    String DATA_COUNT_TABLE = "data_count";


    String VillageWiseDownloading = "V";
    String EbWiseDownloading = "E";
    String WardWiseDownloading = "W";
    String SubBlockWiseDownloading = "S";
    String AadharNumber = "AadharNumber";


    String aadhaarCertificate = "MIIFVDCCBDygAwIBAgIEAMwvlTANBgkqhkiG9w0BAQsFADCBkDELMAkGA1UEBhMC\n" +
            "SU4xKjAoBgNVBAoTIWVNdWRocmEgQ29uc3VtZXIgU2VydmljZXMgTGltaXRlZDEd\n" +
            "MBsGA1UECxMUQ2VydGlmeWluZyBBdXRob3JpdHkxNjA0BgNVBAMTLWUtTXVkaHJh\n" +
            "IFN1YiBDQSBmb3IgQ2xhc3MgMyBPcmdhbmlzYXRpb24gMjAxNDAeFw0xNjEyMzAx\n" +
            "MTA2MTlaFw0xOTEyMzAxMTA2MTlaMIGCMQswCQYDVQQGEwJJTjEOMAwGA1UEChMF\n" +
            "VUlEQUkxGjAYBgNVBAsTEVRlY2hub2xvZ3kgQ2VudHJlMQ8wDQYDVQQREwY1NjAw\n" +
            "OTIxEjAQBgNVBAgTCUthcm5hdGFrYTEiMCAGA1UEAxMZU2lyaXNoIENob3VkaGFy\n" +
            "eSBKYXJ1YnVsYTCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBAN9Hh/lI\n" +
            "m/V7eP8Tk9p27BKhHvITxsr7lJTO0TFaeDgNjuec8UPHsasIkPBpWZw9mOqwbC8A\n" +
            "1uwQviyBTpBamk2HIKyF6NlKMg3Ihdot3+z3k3iT9E6PbJGTmzT1JrEK4eZ2ULn2\n" +
            "TmXzRofkZCMV77F/263e1AJtVyJLWA3ZwtPfYBpBDAGUYQSOddQKdzcEgMlANo7U\n" +
            "Ha2jzuMc8doPDV2EF/YuQfedru9gyvtHZMVHNiyFIIonABI5A6BZbspNY/JDv7iw\n" +
            "68vfmqYlLMIbV3c5ca6cFOH8gGyAAA6jeRFu90rKWiwKoa3tkgoQM/x4lhGu6dG+\n" +
            "fKZ1VHCWWWHPjbsCAwEAAaOCAcAwggG8MCEGA1UdEQQaMBiBFnNpcmlzaC5qc0B1\n" +
            "aWRhaS5uZXQuaW4wEwYDVR0jBAwwCoAITNG9KhFIBNMwHQYDVR0OBBYEFIa84oX1\n" +
            "K3hof37+oh3sTHXGT47/MA4GA1UdDwEB/wQEAwIFIDCBjAYDVR0gBIGEMIGBMC0G\n" +
            "BmCCZGQCAzAjMCEGCCsGAQUFBwICMBUaE0NsYXNzIDMgQ2VydGlmaWNhdGUwUAYH\n" +
            "YIJkZAEIAjBFMEMGCCsGAQUFBwIBFjdodHRwOi8vd3d3LmUtbXVkaHJhLmNvbS9y\n" +
            "ZXBvc2l0b3J5L2Nwcy9lLU11ZGhyYV9DUFMucGRmMHsGCCsGAQUFBwEBBG8wbTAk\n" +
            "BggrBgEFBQcwAYYYaHR0cDovL29jc3AuZS1tdWRocmEuY29tMEUGCCsGAQUFBzAC\n" +
            "hjlodHRwOi8vd3d3LmUtbXVkaHJhLmNvbS9yZXBvc2l0b3J5L2NhY2VydHMvQzNP\n" +
            "U0NBMjAxNC5jcnQwRwYDVR0fBEAwPjA8oDqgOIY2aHR0cDovL3d3dy5lLW11ZGhy\n" +
            "YS5jb20vcmVwb3NpdG9yeS9jcmxzL0MzT1NDQTIwMTQuY3JsMA0GCSqGSIb3DQEB\n" +
            "CwUAA4IBAQCtIELoC2dLA9cP4FSV426Pda0hdhWEnOTAnF9DA0RUfq47ujUB/edq\n" +
            "g8CvEl5KxyMftsNaXIEUoh4myToUATR1ybxpq6hKKhOM3JYdq3u+5S8D4wXd1HIn\n" +
            "rNgBnkta0af578yIfRjt6KDyHZUVcG57zxbKbRQvWqnQy/00abLwpwAeEvjoDc7f\n" +
            "5Re9Soz6RsUVckNztdZDaXetYQ/Mu/e6qCToH5A4Vgbu0sseQsfBOKZtVZA5p6Op\n" +
            "MvIbDwwdec97cVeoEd2lI+8aGY6zm9ttXvTxsEJtxiNmTJKc0077IaLcpqhDte6V\n" +
            "TSAK0qOiiOMa4Od2jhg6syGENFbMTiXg";


}

