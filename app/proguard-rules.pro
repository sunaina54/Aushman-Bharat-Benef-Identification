# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Users\psqit\AppData\Local\Android\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
   public *;
}
-dontskipnonpubliclibraryclassmembers
-ignorewarnings
-dontwarn org.apache.**
-dontobfuscate
-keep class android.support.v4.app.** { *; }
-keep interface android.support.v4.app.** { *; }
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
   public *;
}

# Database model


#
#
#
#
#
#

-keep class com.nhpm.Models.request.AadhaarAuthRequestItem.**
-keepclassmembers class com.nhpm.Models.request.AadhaarAuthRequestItem {
   private *;
}
-keep class com.nhpm.Models.request.LoginRequest.**
-keepclassmembers class com.nhpm.Models.request.LoginRequest {
   private *;
}

-keep class com.nhpm.Models.request.NotificationRequestItem.**
-keepclassmembers class com.nhpm.Models.request.NotificationRequestItem {
   private *;
}

-keep class com.nhpm.Models.request.PinRequestItem.**
-keepclassmembers class com.nhpm.Models.request.PinRequestItem {
   private *;
}
-keep class com.nhpm.Models.request.TargetStateCodeItem.**
-keepclassmembers class com.nhpm.Models.request.TargetStateCodeItem {
   private *;
}


-keep class com.nhpm.Models.request.VerifyValidator.**
-keepclassmembers class com.nhpm.Models.request.VerifyValidator {
   private *;
}


-keep class com.nhpm.Models.response.AadhaarCaptureDetailItem.**
-keepclassmembers class com.nhpm.Models.response.AadhaarCaptureDetailItem {
   private *;
}
-keep class com.nhpm.Models.response.AadhaarGenderItem.**
-keepclassmembers class com.nhpm.Models.response.AadhaarGenderItem {
   private *;
}

-keep class com.nhpm.Models.response.AadhaarStatusItem.**
-keepclassmembers class com.nhpm.Models.response.AadhaarStatusItem {
   private *;
}


-keep class com.nhpm.Models.ApplicationDataModel.**
-keepclassmembers class com.nhpm.Models.ApplicationDataModel {
   private *;
}

-keep class com.nhpm.Models.response.AadharDataModel.**
-keepclassmembers class com.nhpm.Models.response.AadharDataModel {
   private *;
}
-keep class com.nhpm.Models.response.ApplicationConfigListModel.**
-keepclassmembers class com.nhpm.Models.response.ApplicationConfigListModel {
   private *;
}
-keep class com.nhpm.Models.response.ApplicationConfigurationModel.**
-keepclassmembers class com.nhpm.Models.response.ApplicationConfigurationModel {
   private *;
}
-keep class com.nhpm.Models.response.BlockItem.**
-keepclassmembers class com.nhpm.Models.response.BlockItem {
   private *;
}

-keep class com.nhpm.Models.response.GenericResponse.**
-keepclassmembers class com.nhpm.Models.response.GenericResponse {
   private *;
}

-keep class com.nhpm.Models.response.GovernmentIdItem.**
-keepclassmembers class com.nhpm.Models.response.GovernmentIdItem {
   private *;
}
-keep class com.nhpm.Models.response.HealthSchemeItem.**
-keepclassmembers class com.nhpm.Models.response.HealthSchemeItem {
   private *;
}
-keep class com.nhpm.Models.response.MobileNumberItem.**
-keepclassmembers class com.nhpm.Models.response.MobileNumberItem {
   private *;
}
-keep class com.nhpm.Models.response.MobileOTPResponse.**
-keepclassmembers class com.nhpm.Models.response.MobileOTPResponse {
   private *;
}

-keep class com.nhpm.Models.request.MobileOtpRequest.**
-keepclassmembers class com.nhpm.Models.request.MobileOtpRequest {
   private *;
}


-keep class com.nhpm.Models.response.MobileVerifyStatusItem.**
-keepclassmembers class com.nhpm.Models.response.MobileVerifyStatusItem {
   private *;
}

-keep class com.nhpm.Models.response.NomineeMemberItem.**
-keepclassmembers class com.nhpm.Models.response.NomineeMemberItem {
   private *;
}
-keep class com.nhpm.Models.response.NotificationResponse.**
-keepclassmembers class com.nhpm.Models.response.NotificationResponse {
   private *;
}
-keep class com.nhpm.Models.response.Poa.**
-keepclassmembers class com.nhpm.Models.response.Poa {
   private *;
}
-keep class com.nhpm.Models.response.Poi.**
-keepclassmembers class com.nhpm.Models.response.Poi {
   private *;
}

-keep class com.nhpm.Models.response.verifier.ValidatorVerificationResponse.**
-keepclassmembers class com.nhpm.Models.response.verifier.ValidatorVerificationResponse. {
   private *;
}


-keep class com.nhpm.Models.response.RelationItem.**
-keepclassmembers class com.nhpm.Models.response.RelationItem {
   private *;
}

-keep class com.nhpm.Models.response.UidData.**
-keepclassmembers class com.nhpm.Models.response.UidData {
   private *;
}
-keep class com.nhpm.Models.response.WhoseMobileItem.**
-keepclassmembers class com.nhpm.Models.response.WhoseMobileItem {
   private *;
}
-keep class com.nhpm.Models.AadharAuthItem.**
-keepclassmembers class com.nhpm.Models.AadharAuthItem {
   private *;
}
-keep class com.nhpm.Models.ApplicationLanguageItem.**
-keepclassmembers class com.nhpm.Models.ApplicationLanguageItem {
   private *;
}

-keep class com.nhpm.Models.DataCountRequest.**
-keepclassmembers class com.nhpm.Models.DataCountRequest {
   private *;
}

-keep class com.nhpm.Models.DemoAuthResp.**
-keepclassmembers class com.nhpm.Models.DemoAuthResp {
   private *;
}
-keep class com.nhpm.Models.DownloadedDataCountModel.**
-keepclassmembers class com.nhpm.Models.DownloadedDataCountModel {
   private *;
}
-keep class com.nhpm.Models.KycResponse.**
-keepclassmembers class com.nhpm.Models.KycResponse {
   private *;
}
-keep class com.nhpm.Models.MemberModel.**
-keepclassmembers class com.nhpm.Models.MemberModel {
   private *;
}

-keep class com.nhpm.Models.NotificationModel.**
-keepclassmembers class com.nhpm.Models.NotificationModel {
   private *;
}

-keep class com.nhpm.Models.PrintQrCodeFinalObject.**
-keepclassmembers class com.nhpm.Models.PrintQrCodeFinalObject {
   private *;
}
-keep class com.nhpm.Models.PrintQrCodeHousehold.**
-keepclassmembers class com.nhpm.Models.PrintQrCodeHousehold {
   private *;
}
-keep class com.nhpm.Models.PrintQrCodeMemberDetail.**
-keepclassmembers class com.nhpm.Models.PrintQrCodeMemberDetail {
   private *;
}
-keep class com.nhpm.Models.response.master.request.MasterRequest.**
-keepclassmembers class com.nhpm.Models.response.master.request.MasterRequest {
   private *;
}

-keep class com.nhpm.Models.response.master.response.AadhaarStatusItemResponse.**
-keepclassmembers class com.nhpm.Models.response.master.response.AadhaarStatusItemResponse {
   private *;
}
-keep class com.nhpm.Models.response.master.response.AppUpdatVersionResponse**
-keepclassmembers class com.nhpm.Models.response.master.response.AppUpdatVersionResponse {
   private *;
}
-keep class com.nhpm.Models.response.master.response.HealthSchemeItemResponse.**
-keepclassmembers class com.nhpm.Models.response.master.response.HealthSchemeItemResponse {
   private *;
}
-keep class com.nhpm.Models.response.master.response.HealthSchemeRequest.**
-keepclassmembers class com.nhpm.Models.response.master.response.HealthSchemeRequest {
   private *;
}
-keep class com.nhpm.Models.response.master.response.MasterResponseItem.**
-keepclassmembers class com.nhpm.Models.response.master.response.MasterResponseItem {
   private *;
}
-keep class com.nhpm.Models.response.master.response.MemberStatusItemResponse.**
-keepclassmembers class com.nhpm.Models.response.master.response.MemberStatusItemResponse {
   private *;
}
-keep class com.nhpm.Models.response.master.AadhaarStatusItem.**
-keepclassmembers class com.nhpm.Models.response.master.AadhaarStatusItem {
   private *;
}
-keep class com.nhpm.Models.response.master.MemberRelationItem.**
-keepclassmembers class com.nhpm.Models.response.master.MemberRelationItem {
   private *;
}


-keep class com.nhpm.Models.response.master.ConfigurationItem.**
-keepclassmembers class com.nhpm.Models.response.master.ConfigurationItem {
   private *;
}
-keep class com.nhpm.Models.response.master.HealthSchemeItem.**
-keepclassmembers class com.nhpm.Models.response.master.HealthSchemeItem {
   private *;
}

-keep class com.nhpm.Models.response.master.MemberStatusItem.**
-keepclassmembers class com.nhpm.Models.response.master.MemberStatusItem {
   private *;
}


-keep class com.nhpm.Models.response.master.RelationMasterItem.**
-keepclassmembers class com.nhpm.Models.response.master.RelationMasterItem {
   private *;
}


-keep class com.nhpm.Models.response.master.StateItem.**
-keepclassmembers class com.nhpm.Models.response.master.StateItem {
   private *;
}

-keep class com.nhpm.Models.response.master.StateItemList.**
-keepclassmembers class com.nhpm.Models.response.master.StateItemList {
   private *;
}
-keep class com.nhpm.Models.response.rsbyMembers.CardCategoryMaster.**
-keepclassmembers class com.nhpm.Models.response.rsbyMembers.CardCategoryMaster {
   private *;
}
-keep class com.nhpm.Models.response.rsbyMembers.RsbyPoliciesCompany.**
-keepclassmembers class com.nhpm.Models.response.rsbyMembers.RsbyPoliciesCompany {
   private *;
}
-keep class com.nhpm.Models.response.rsbyMembers.RSBYPoliciesItem.**
-keepclassmembers class com.nhpm.Models.response.rsbyMembers.RSBYPoliciesItem {
   private *;
}
-keep class com.nhpm.Models.response.rsbyMembers.RsbyRelationItem.**
-keepclassmembers class com.nhpm.Models.response.rsbyMembers.RsbyRelationItem {
   private *;
}
-keep class com.nhpm.Models.response.seccMembersDataCountModel..**
-keepclassmembers class com.nhpm.Models.response.seccMembers.DataCountModel {
   private *;
}
-keep class com.nhpm.Models.response.seccMembers.DataCountObject.**
-keepclassmembers class com.nhpm.Models.response.seccMembers.DataCountObject {
   private *;
}
-keep class com.nhpm.Models.response.seccMembers.ErrorItem.**
-keepclassmembers class com.nhpm.Models.response.seccMembers.ErrorItem {
   private *;
}
-keep class com.nhpm.Models.response.seccMembers.HouseHoldItem.**
-keepclassmembers class com.nhpm.Models.response.seccMembers.HouseHoldItem {
   private *;
}
-keep class com.nhpm.Models.response.seccMembers.HouseHoldRequest.**
-keepclassmembers class com.nhpm.Models.response.seccMembers.HouseHoldRequest {
   private *;
}
-keep class com.nhpm.Models.response.seccMembers.HouseHoldWriteItem.**
-keepclassmembers class com.nhpm.Models.response.seccMembers.HouseHoldWriteItem {
   private *;
}
-keep class com.nhpm.Models.response.seccMembers.PopSeccWriteItem.**
-keepclassmembers class com.nhpm.Models.response.seccMembers.PopSeccWriteItem {
   private *;
}
-keep class com.nhpm.Models.response.seccMembers.SeccHouseholdResponse.**
-keepclassmembers class com.nhpm.Models.response.seccMembers.SeccHouseholdResponse {
   private *;
}
-keep class com.nhpm.Models.response.seccMembers.SeccMemberItem.**
-keepclassmembers class com.nhpm.Models.response.seccMembers.SeccMemberItem {
   private *;
}
-keep class com.nhpm.Models.response.seccMembers.SeccMemberRequest.**
-keepclassmembers class com.nhpm.Models.response.seccMembers.SeccMemberRequest {
   private *;
}
-keep class com.nhpm.Models.response.seccMembers.SeccMemberResponse.**
-keepclassmembers class com.nhpm.Models.response.seccMembers.SeccMemberResponse {
   private *;
}
-keep class com.nhpm.Models.response.seccMembers.SelectedMemberItem.**
-keepclassmembers class com.nhpm.Models.response.seccMembers.SelectedMemberItem {
   private *;
}
-keep class com.nhpm.Models.response.seccMembers.SyncHouseholdResponse.**
-keepclassmembers class com.nhpm.Models.response.seccMembers.SyncHouseholdResponse {
   private *;
}
-keep class com.nhpm.Models.response.seccMembers.SyncSeccMemberResponse.**
-keepclassmembers class com.nhpm.Models.response.seccMembers.SyncSeccMemberResponse {
   private *;
}

-keep class com.nhpm.Models.response.rsbyMembers.RsbyRelationMasterList.**
-keepclassmembers class com.nhpm.Models.response.rsbyMembers.RsbyRelationMasterList {
   private *;
}
-keep class com.nhpm.Models.response.rsbyMembers.RsbyPoliciesMasterList.**
-keepclassmembers class com.nhpm.Models.response.rsbyMembers.RsbyPoliciesMasterList {
   private *;
}


-keep class com.nhpm.Models.response.rsbyMembers.RsbyHouseholdSyncResponse.**
-keepclassmembers class com.nhpm.Models.response.rsbyMembers.RsbyHouseholdSyncResponse {
   private *;
}



-keep class com.nhpm.Models.response.rsbyMembers.RsbyCardCategoryMasterList.**
-keepclassmembers class com.nhpm.Models.response.rsbyMembers.RsbyCardCategoryMasterList {
   private *;
}
-keep class com.nhpm.Models.response.rsbyMembers.RsbyCardCategoryItem.**
-keepclassmembers class com.nhpm.Models.response.rsbyMembers.RsbyCardCategoryItem {
   private *;
}
-keep class com.nhpm.Models.response.rsbyMembers.RsbyPoliciesCompanyMasterList.**
-keepclassmembers class com.nhpm.Models.response.rsbyMembers.RsbyPoliciesCompanyMasterList {
   private *;
}


#
#
#
#
#
#
#
#
#









-keep class com.nhpm.Models.response.verifier.AadhaarOtpResponse.**
-keepclassmembers class com.nhpm.Models.response.verifier.AadhaarOtpResponse {
   private *;
}
-keep class com.nhpm.Models.response.verifier.AadhaarResponseItem.**
-keepclassmembers class com.nhpm.Models.response.verifier.AadhaarResponseItem {
   private *;
}
-keep class com.nhpm.Models.response.verifier.DemoAuthResponseItem.**
-keepclassmembers class com.nhpm.Models.response.verifier.DemoAuthResponseItem {
   private *;
}
-keep class com.nhpm.Models.response.verifier.FamilyStatusItem.**
-keepclassmembers class com.nhpm.Models.response.verifier.FamilyStatusItem {
   private *;
}

-keep class com.nhpm.Models.response.verifier.MobileNumberOTPValidaationResponse.**
-keepclassmembers class com.nhpm.Models.response.verifier.MobileNumberOTPValidaationResponse {
   private *;
}
-keep class com.nhpm.Models.response.verifier.PinRequestItem.**
-keepclassmembers class com.nhpm.Models.response.verifier.PinRequestItem {
   private *;
}
-keep class com.nhpm.Models.response.verifier.UpdatePinResponse.**
-keepclassmembers class com.nhpm.Models.response.verifier.UpdatePinResponse {
   private *;
}
-keep class com.nhpm.Models.response.verifier.VerifierDetail.**
-keepclassmembers class com.nhpm.Models.response.verifier.VerifierDetail {
   private *;
}

-keep class com.nhpm.Models.response.verifier.VerifierItem.**
-keepclassmembers class com.nhpm.Models.response.verifier.VerifierItem {
   private *;
}
-keep class com.nhpm.Models.response.verifier.VerifierLocationItem.**
-keepclassmembers class com.nhpm.Models.response.verifier.VerifierLocationItem {
   private *;
}
-keep class com.nhpm.Models.response.verifier.VerifierLocationOLD.**
-keepclassmembers class com.nhpm.Models.response.verifier.VerifierLocationOLD {
   private *;
}
-keep class com.nhpm.Models.response.verifier.VerifierLoginResponse.**
-keepclassmembers class com.nhpm.Models.response.verifier.VerifierLoginResponse {
   private *;
}

-keep class com.nhpm.Models.response.verifier.VerifierLoginResponse1.**
-keepclassmembers class com.nhpm.Models.response.verifier.VerifierLoginResponse1 {
   private *;
}




-keep class com.nhpm.Models.response.verifier.AadhaarDemoAuthResponse.**
-keepclassmembers class com.nhpm.Models.response.verifier.AadhaarDemoAuthResponse {
   private *;
}






























































#-keep class com.nhpm.LocalDataBase.DatabaseHelpers.**
-keep class com.nhpm.Models.DownloadedDataCountModel.**
-keep class com.nhpm.Models.NotificationModel.**
-keep class com.nhpm.Models.response.RelationItem.**
-keep class com.nhpm.Models.response.master.AadhaarStatusItem.**
-keep class com.nhpm.Models.response.master.ConfigurationItem.**
-keep class com.nhpm.Models.response.master.HealthSchemeItem.**
-keep class com.nhpm.Models.response.master.MemberStatusItem.**
-keep class com.nhpm.Models.response.master.StateItem.**
-keep class com.nhpm.Models.response.rsbyMembers.CardCategoryMaster.**
-keep class com.nhpm.Models.response.rsbyMembers.RSBYItem.**
-keep class com.nhpm.Models.response.rsbyMembers.RSBYPoliciesItem.**
-keep class com.nhpm.Models.response.rsbyMembers.RsbyHouseholdItem.**
-keep class com.nhpm.Models.response.rsbyMembers.RsbyPoliciesCompany.**
-keep class com.nhpm.Models.response.rsbyMembers.RsbyRelationItem.**
-keep class com.nhpm.Models.response.seccMembers.ErrorItem.**
-keep class com.nhpm.Models.response.seccMembers.HouseHoldItem.**
-keep class com.nhpm.Models.response.seccMembers.SeccMemberItem.**
-keep class com.nhpm.Models.response.verifier.FamilyStatusItem.**
-keep class com.nhpm.Models.response.verifier.VerifierLocationItem.**
-keep class com.nhpm.Utility.AppConstant.**
-keep class com.nhpm.Utility.AppUtility.**
-keep class com.nhpm.Utility.SyncUtility.**
-keep class com.nhpm.Models.response.master.StateItemList.**


#
#
#
#
#
#


-keep class in.kdms.irislib.** { *; }
-keep class com.aadhar.Global.** { *; }
-keep class com.aadhar.CommonMethods.** { *; }
-keep class com.sec.biometric.license.** {
*;
}
-keep class com.example.cogentsdk.MainActivity.**
-keepclassmembers class com.example.cogentsdk.MainActivity {
   public *;
}
-dontwarn com.example.cogentsdk.**

-keep class com.aadhar.MainActivity.**
-keepclassmembers class com.aadhar.MainActivity {
   public *;
}
-keepclassmembers class com.aadhar.MainActivity {
   private *;
}
-dontwarn com.aadhar.**
-keep class com.aadhar.** { *; }
-keep class com.example.cogentsdk.** { *; }
-dontwarn com.example.cogentsdk.**
-keep class in.kdms.irislib.** { *;}
-keep class in.kdms.irislib.IRISCaptureActivity.** { *;}
-keep class com.jaredrummler.materialspinner.MaterialSpinner.** { *;}
-dontwarn com.jaredrummler.**
-keep class org.spongycastle.** { *; }
-dontwarn org.spongycastle.**
-keepattributes SourceFile,LineNumberTable
-keep class com.sun.jna** { *; }
-dontwarn com.sun.jna.**
-keep class org.apache** { *; }
-dontwarn org.apache.**
-keep class com.bioenable.** { *; }
-dontwarn com.bioenable.**
-keep class com.sec.biometric.iris.** { *; }
-keep class com.sec.biometric.license.** { *; }
-keep class com.aadhar.Global.**
-keepclassmembers class com.aadhar.Global {
   public *;
}
-dontwarn okio.**
-dontwarn retrofit2.Platform$Java8
-keep class com.aadhar.CommonMethods.**
-keepclassmembers class com.aadhar.CommonMethods {
   public *;
}
-keep interface com.aadhar.commonapi.HelperInterface.** { *; }
-keepclassmembers interface com.aadhar.commonapi.HelperInterface {
   public *;
}
-keep class com.aadhar.commonapi.mfs100api.** { *; }
-keepclassmembers class com.aadhar.commonapi.mfs100api {
   public *;
}
-keep class com.aadhar.VerhoeffAadhar.** { *; }
-keepclassmembers class com.aadhar.VerhoeffAadhar {
   public *;
}
-keep class com.aadhar.commonapi.BiometricDeviceHandler.** { *; }
-keepclassmembers class com.aadhar.commonapi.BiometricDeviceHandler {
   public *;
}
-keep class com.aadhar.commonapi.DeviceRecognizer.** { *; }
-keepclassmembers class com.aadhar.commonapi.DeviceRecognizer {
   public *;
}

-keep class com.aadhar.UidaiAuthHelper.** { *; }
-keepclassmembers class com.aadhar.UidaiAuthHelper {
   public *;
}

-keep class com.aadhar.CheckConnection.** { *; }
-keepclassmembers class com.aadhar.CheckConnection {
   public *;
}

-keep class com.aadhar.Logs.** { *; }
-keepclassmembers class com.aadhar.Logs {
   public *;
}
-keep class com.aadhar.UidaiAuthHelper.** { *; }
-keepclassmembers class com.aadhar.UidaiAuthHelper {
   public *;
}

-keep class com.aadhar.UdaiAuthOTPHelper.** { *; }
-keepclassmembers class com.aadhar.UdaiAuthOTPHelper {
   public *;
   private *;
}

#-keep class com.nhpm.AadhaarUtils.** { *; }
#-keepclassmembers com.nhpm.AadhaarUtils {
#   public *;
#   private *;
#}

-keep class com.aadhar.MainActivity.** { *; }

-keepclassmembers class com.aadhar.MainActivity {
   public *;
}
#-keepclassmembers class javax.xml.bind.** {
#   public *;
#   private *;
#}

-keepclassmembers class ae.javax.xml.bind.** {
   public *;
   private *;
}


-keepclassmembers class com.aadhar.MainActivity {
   private *;
}
-keep class com.bioenable.androidwrapper.BioEnableWrapper.**
-keepclassmembers class com.bioenable.androidwrapper.BioEnableWrapper {
   public *;
}
-keepclassmembers class com.bioenable.androidwrapper.BioEnableWrapper {
   private *;
}
-dontwarn com.bioenable.**
-keep interface com.aadhar.commonapi.HelperInterface.** { *; }
-keepattributes Signature

# For using GSON @Expose annotation
-keepattributes *Annotation*

# Gson specific classes
-keep class sun.misc.Unsafe { *; }
#-keep class com.google.gson.stream.** { *; }





#-keepclassmembers com.nhpm.LocalDataBase.DatabaseHelpers{
#                                                                                private *;
#                                                                             }
#-keepclassmembers com.nhpm.Models.DownloadedDataCountModel{    private *; }
#-keepclassmembers com.nhpm.Models.NotificationModel{    private *; }
#-keepclassmembers com.nhpm.Models.response.RelationItem{    private *; }
#-keepclassmembers com.nhpm.Models.response.master.AadhaarStatusItem{    private *; }
#-keepclassmembers com.nhpm.Models.response.master.ConfigurationItem{    private *; }
#-keepclassmembers com.nhpm.Models.response.master.HealthSchemeItem{    private *; }
#-keepclassmembers com.nhpm.Models.response.master.MemberStatusItem{    private *; }
#-keepclassmembers com.nhpm.Models.response.master.StateItem{    private *; }
#-keepclassmembers com.nhpm.Models.response.rsbyMembers.CardCategoryMaster{    private *; }
#-keepclassmembers com.nhpm.Models.response.rsbyMembers.RSBYItem{    private *; }
#-keepclassmembers com.nhpm.Models.response.rsbyMembers.RSBYPoliciesItem{    private *; }
#-keepclassmembers com.nhpm.Models.response.rsbyMembers.RsbyHouseholdItem{    private *; }
#-keepclassmembers com.nhpm.Models.response.rsbyMembers.RsbyPoliciesCompany{    private *; }
#-keepclassmembers com.nhpm.Models.response.rsbyMembers.RsbyRelationItem{    private *; }
#-keepclassmembers com.nhpm.Models.response.seccMembers.ErrorItem{    private *; }
#-keepclassmembers com.nhpm.Models.response.seccMembers.HouseHoldItem{    private *; }
#-keepclassmembers com.nhpm.Models.response.seccMembers.SeccMemberItem{    private *; }
#-keepclassmembers com.nhpm.Models.response.verifier.FamilyStatusItem{    private *; }
#-keepclassmembers com.nhpm.Models.response.verifier.VerifierLocationItem{    private *; }
#-keepclassmembers com.nhpm.Utility.AppConstant{    private *; }
#-keepclassmembers com.nhpm.Utility.AppUtility{    private *; }
#-keepclassmembers com.nhpm.Utility.SyncUtility{    private *; }





-keep class org.spongycastle.** { *; }
-dontwarn org.spongycastle.**

-keep class org.bouncycastle.** { *; }
-dontwarn org.bouncycastle.**

#-keep class javax.xml.bind.** { *; }
#-dontwarn javax.xml.bind.**
#
#-keep class javax.xml.bind.** { *; }
#-dontwarn javax.xml.bind.**
#
#-keep class com.sun.activation.** { *; }
#-dontwarn com.sun.activation.**
#
#-keep class javax.activation.** { *; }
#-dontwarn   javax.activation.**


-keepattributes SourceFile,LineNumberTable
-keep class com.parse.*{ *; }
-dontwarn com.parse.**
-dontwarn com.squareup.picasso.**
-keepclasseswithmembernames class * {
    native <methods>;
}

-keep class com.sun.jna** { *; }
-dontwarn com.sun.jna.**
-keep class com.zebra.sdk** { *; }
-dontwarn com.zebra.sdk.**
-keep class org.apache** { *; }
-dontwarn org.apache.**

-dontwarn org.joda.convert.**
-dontwarn org.joda.time.**
-keep class org.joda.time.** { *; }
-keep interface org.joda.time.** { *;}

-dontwarn org.kobjects.**
-dontwarn org.ksoap2.**
-dontwarn org.kxml2.**
-dontwarn org.xmlpull.v1.**

-keep class org.kobjects.** { *; }
-keep class org.ksoap2.** { *; }
-keep class org.kxml2.** { *; }
-keep class org.xmlpull.** { *; }
-keep class com.sun.xml.bind.v2.** { *; }
#-keep class javax.xml.bind.JAXBException { *; }

-dontwarn org.apache.commons.**
-keep class org.apache.http.** { *; }
-dontwarn org.apache.http.**

-keep class org.dom4j.** { *; }
-dontwarn org.dom4j.**
-dontwarn org.apache.**

-keep class android.support.v4.app.** { *; }
-keep interface android.support.v4.app.** { *; }


#
#
#
#
#


-optimizations !code/simplification/arithmetic,!code/simplification/cast,!field/*,!class/merging/*
-optimizationpasses 5
-allowaccessmodification
-dontpreverify

# The remainder of this file is identical to the non-optimized version
# of the Proguard configuration file (except that the other file has
# flags to turn off optimization).

-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-verbose

-keepattributes *Annotation*
-keep public class com.google.vending.licensing.ILicensingService
-keep public class com.android.vending.licensing.ILicensingService

# For native methods, see http://proguard.sourceforge.net/manual/examples.html#native
-keepclasseswithmembernames class * {
    native <methods>;
}

# keep setters in Views so that animations can still work.
# see http://proguard.sourceforge.net/manual/examples.html#beans
-keepclassmembers public class * extends android.view.View {
   void set*(***);
   *** get*();
}

# We want to keep methods in Activity that could be used in the XML attribute onClick
-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

# For enumeration classes, see http://proguard.sourceforge.net/manual/examples.html#enumerations
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keepclassmembers class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator CREATOR;
}

-keepclassmembers class **.R$* {
    public static <fields>;
}

# The support library contains references to newer platform versions.
# Don't warn about those in case this app is linking against an older
# platform version.  We know about them, and they are safe.
-dontwarn android.support.**

# Understand the @Keep support annotation.
-keep class android.support.annotation.Keep

-keep @android.support.annotation.Keep class * {*;}

-keepclasseswithmembers class * {
    @android.support.annotation.Keep <methods>;
}

-keepclasseswithmembers class * {
    @android.support.annotation.Keep <fields>;
}

-keepclasseswithmembers class * {
    @android.support.annotation.Keep <init>(...);
}




#
#
#
#



-dontwarn java.nio.file.Files
-dontwarn java.nio.file.Path
-dontwarn java.nio.file.OpenOption
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
