package com.aadhar;

public class Global {
    public static final int startekPID = 33312;
    public static final int startekVID = 3018;
    public static final int fm220U = 33312;
    public static final int mfs100PID = 34323;
    public static final int mfs100VID = 1204;
    public static final int mfs100 = 41;
    public static final int iritechVID = 8035;
    public static final int iritechPID = 61441;
    public static final int iritechDeviceID = 1002;
    public static final int morphoDeviceID = 47;
    public static final int bioenableVID = 2694;
    public static final int bioenablePID = 1616;
    public static final int hfdu08 = 1616;
    public static final int secugenPID = 8704;
    public static final int secugenVID = 4450;
    public static final int pro20 = 8704;
    public static final int EPI_100 = 1003;
    public static final int elkontouchPID = 8214;
    public static final int SCAN_TIME = 60000;
    public static final int TIME_FOR_SCANNING = 60000;
    public static final int TIME_FOR_SCANNING_SAMSUNG = 60000;
    public static final long INTERVAL = 1000;
    public static String imei;
    public static String ipAddress = "ERROR";
    public static String connectionType = "N";
    public static boolean morphoAttached = false;
    public static boolean elkonTouchAttached = false;
    public static String activeCode;
    public static String sessionToken = "";
    public static String connectedDevice = "";
    public static String authType = ""; //"O" for operator, "R" for resident
    public static boolean IsSame = false;
    public static String device_xml = "";
    public static int sequenceNumber;
    public static int connectedDeviceID = -1;
    public static String connectedDeviceNameId = "";
    public static String bioDataType = "FMR";
    public static int bioDeviceType = 0;
    public static String attachedDeviceType = "";
    public static String deviceType = "";
    public static String deviceMake = "", deviceModel = "", serialNumber = "", deviceVendor = "";
    public static int numberOfAttempts = 0;
    public static double latitude = 0.0d;
    public static double longitude = 0.0d;
    public static String deviceid = "";
    public static String hash = "";
    public static String productionPublicKey = "";
    public static String bioType = "N";

    //	make changes here
    //	public static String preproductionPublicKeyFromServer;
    public static String AUTH_AADHAAR = "";
    public static String CHECK_ENTRY = "check_etry";
    public static String PID_TIME_STAMP = "";
    public static String DEVICE_IMEI_NO = "";
    public static float version = 1.0f;
    public static String XML_VERSION = "1.6";
    public static String APP_VER = "v2.2";
    public static String KYC_API_VERSION = "1.0f";
    public static float otp_version = 1.6f;
    //	public static float otp_version= 1.5f;
    public static float AUTH_version = 1.6f;
    public static String serverurl = "";
    public static String otpurl = "";
    public static String KYC_URL = "http://164.100.161.62/health_t/aadhaarBioAuth";
    public static String KYC_URL_NEW = "http://164.100.58.98:80/aadhaarBioAuth/1.0.0/aadhaarBioAuth";
    public static String DEMO_AUTH = "http://164.100.58.98:80/getAuth/1.0.0/getAuth";
    public static String LOG_URL = "";
    public static String STATUS_URL = "";
    public static String KYC_LINCENCE_KEY = "";
    public static String ASA_LICENCE_KEY = "";
    // R stands for Resident
    public static String R_PID_TIME = "";
    public static float MOU_VERSION = 1.0f;
    public static boolean FLAG = false;
    public static String R_AADHAAR = "";
    public static String R_NMN = "";        //Resident new mobile no
    public static String R_OTP = "";
    public static String R_NEM = "";        //Resident new email address
    public static String R_ENCODED_AUTH_XML = "";
    public static String R_AUTH_TYPE = "";
    public static boolean CHECK_CONSENT = false;
    public static boolean MOU = false;
    public static String AADHAAR_NO = "";

    public static boolean morphoMSO1300Attached;
    public static boolean morphoMSO1350Attached;
    public static boolean morphoMSO30xAttached;
    public static boolean morphoMSO35xAttached;
    public static boolean mantraAttached;
    public static boolean iritechAttached;
    public static boolean startekAttached;
    public static boolean bioenableAttached;
    public static boolean secugenAttached;
    public static boolean biometriquesAttached;
    public static boolean precisionElkonTouchAttached;
    public static boolean precisionuru4500Attached;
    public static boolean scannerAttached;
    public static boolean cogentAttached;

    public static boolean biometricInitialized = false;


    public static String NETWORK_TYPE = "";
    public static String NETWORK_NAME = "";
    public static String OPERATOR_NAME = "";

    public static int cid = 0;
    public static int lac = 0;
    public static String mcc = "";
    public static String mnc = "";

    public static String SIGNAL_STRENGTH = "";
    public static String AUTH_USING = "";

    public static String FPSTRINGIMG = "";

    //public static  long startTimeTemp;

    public static String Tempxmldel = "";
    public static String KycXmlForNhps = "";
    public static String LOCATION_ADDRESS = "";

    public static String AADHAAR_NO_FOR_LOG = "";
    public static String OPERATOR_NAME_SET = "";
    public static String CURRENT_LOCATION_SET = "";
    public static int SIM_CHECK;
    public static String TIN_NO = "";
    public static boolean samsungIris = false;
    public static boolean INFOCUS_Iris = false;
    public static String PID_FOR_SAMSUNG = "";
    public static String KYCXML = "";
    public static String TIME_STAMP_FOR_SAVE_DATA = "";
    public static String VALIDATORAADHAR = "";
    public static String KYCIRISXML = "";
    public static String USER_PASSWORD = "";
    public static String USER_NAME = "";
    public static String CURRENT_TIMESTAMP = "";
}

