package com.nhpm.AadhaarUtils;

import android.util.Log;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * Created by HP on 02-01-2018.
 */

public class UdaiAuthOTPHelper {


    static String licenseKey = "";
    static Encrypter encrypter;
    static HashGenerator hashgenerator;
    static byte[] authpubkey;
    String aua = "public";
    String terminalID = "public";
    String serviceAgency = "public";
    byte[] sessionkey;
    Boolean authpkey = false;
    String auaurl = "";
    Boolean useProxy = false;
    String pidTimeStamp;
    byte[] newRandom = new byte[20];
    private String bioDeviceType;
    private String samsungTS;
    private static SynchronizedKey synchronizedKey = null;

//    public UdaiAuthOTPHelper(byte[] publicKey) {
//        LoadInitial(publicKey);
//    }
//
//    public void LoadInitial(byte[] publicKey) {
//        System.out.println("Encryption Initilized");
//        hashgenerator = new HashGenerator();
//        getAuthPubKey(publicKey);
//
//    }
//
//    public boolean getAuthPubKey(byte[] publicKey) {
//
//        try {
//            authpubkey = publicKey;
//            encrypter = new Encrypter(authpubkey);
//            authpkey = true;
//            System.out.println("Auth Encryption Initilized!");
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            authpkey = false;
//        }
//
//        return authpkey;
//    }


    public UdaiAuthOTPHelper(byte[] publicKey) {
        LoadInitial(publicKey);
    }


    public void LoadInitial(byte[] publicKey) {
        System.out.println("Encryption Initilized");
        hashgenerator = new HashGenerator();
        getAuthPubKey(publicKey);

    }

    public boolean getAuthPubKey(byte[] publicKey) {

        try {
            authpubkey = publicKey;
            encrypter = new Encrypter(authpubkey);
            authpkey = true;
            System.out.println("Auth Encryption Initilized!");

        } catch (Exception e) {
            e.printStackTrace();
            authpkey = false;
        }

        return authpkey;
    }

    public static class SynchronizedKey {
        byte[] seedSkey;
        String keyIdentifier;
        Date seedCreationDate;

        public SynchronizedKey(byte[] seedSkey, String keyIdentifier,
                               Date seedCreationDate) {
            super();
            this.seedSkey = seedSkey;
            this.keyIdentifier = keyIdentifier;
            this.seedCreationDate = seedCreationDate;
        }

        public String getKeyIdentifier() {
            return keyIdentifier;
        }

        public Date getSeedCreationDate() {
            return seedCreationDate;
        }

        public byte[] getSeedSkey() {
            return seedSkey;
        }

    }



    // version 2.0 new code
    public String createXmlForKycAuthByRaj(String deviceType, String aadhaarNo, String fpImgString, boolean otp_check, String otp, String txn, String consent) {
        bioDeviceType = deviceType;

//       set Global TXN  txn recived from otp response
        Global.TXN_FOR_AUTH = txn;

        Log.e("deviceType", "==" + deviceType);
        Log.e("aadhaarNo", "==2" + aadhaarNo);
        DateFormat dfm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        Calendar localCalendar = GregorianCalendar
                .getInstance();
        localCalendar.setTime(date);
        pidTimeStamp = String.valueOf(localCalendar
                .get(Calendar.YEAR)) + "-" +
                (String.valueOf(localCalendar.get(Calendar.MONTH) + 1).length() < 2 ? "0" + String.valueOf(localCalendar
                        .get(Calendar.MONTH) + 1) : String.valueOf(localCalendar
                        .get(Calendar.MONTH) + 1))
                + "-" +
                (String.valueOf(localCalendar.get(Calendar.DATE)).length() < 2 ? "0" + String.valueOf(localCalendar.get(Calendar.DATE)) : String.valueOf(localCalendar.get(Calendar.DATE)))
                + "T" +
                (String.valueOf(localCalendar.get(Calendar.HOUR_OF_DAY)).length() < 2 ? "0" + String.valueOf(localCalendar.get(Calendar.HOUR_OF_DAY)) : String.valueOf(localCalendar.get(Calendar.HOUR_OF_DAY)))
                + ":" +
                (String.valueOf(localCalendar.get(Calendar.MINUTE)).length() < 2 ? "0" + String.valueOf(localCalendar.get(Calendar.MINUTE)) : String.valueOf(localCalendar.get(Calendar.MINUTE)))
                + ":" +
                (String.valueOf(localCalendar.get(Calendar.SECOND)).length() < 2 ? "0" + String.valueOf(localCalendar.get(Calendar.SECOND)) : String.valueOf(localCalendar.get(Calendar.SECOND)));

        Global.PID_TIME_STAMP = pidTimeStamp;
        Global.TIME_STAMP_FOR_SAVE_DATA = pidTimeStamp;
        Log.e("Global.PID_TIME_STAMP", "==" + Global.PID_TIME_STAMP);
        Log.e("pidTimeStamp", "==" + pidTimeStamp);

        String KycAuthXml = "";
        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = null;
        try {
            documentBuilder = documentFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        org.w3c.dom.Document document = documentBuilder.newDocument();

        byte[] syncSessionKey = null;
        byte[] encryptedSessionKey = null;
        SessionKeyDetails sessionKeyDetails = null;
        byte[] sessionKey = null;

        try {

            sessionKey = encrypter.generateSessionKey();

            synchronizedKey = new SynchronizedKey(
                    sessionKey, UUID.randomUUID()
                    .toString(), new Date());
            syncSessionKey = synchronizedKey.getSeedSkey();
            encryptedSessionKey = encrypter
                    .encryptUsingPublicKey(syncSessionKey);
            sessionKeyDetails = SessionKeyDetails
                    .createSkeyToInitializeSynchronizedKey(
                            synchronizedKey.getKeyIdentifier(),
                            encryptedSessionKey);

        } catch (Exception e) {
            Log.e("session Exception", "-->" + e);
            System.out.println(e.getMessage());
        }

        boolean bio, pi;
        if (deviceType.equalsIgnoreCase("F")) {
            bio = true;
            pi = false;
            otp_check = false;
        } else if (deviceType.equalsIgnoreCase("I")) {
            bio = true;
            pi = false;
            otp_check = false;
        } else {
            bio = false;
            pi = false;
            otp_check = true;
        }
        byte[] xmlPidBytes = getPidXml("", bio, pi, fpImgString, otp_check, otp).getBytes();
        byte[] cipherTextWithTS = null;
        byte[] encSrcHash = null;
        try {


            cipherTextWithTS = encrypter.encrypt(xmlPidBytes, sessionKey, pidTimeStamp);

            byte[] iv = encrypter.generateIv(pidTimeStamp);
            byte[] aad = encrypter.generateAad(pidTimeStamp);
            byte[] srcHash = encrypter.generateHash(xmlPidBytes);
            encSrcHash = encrypter.encryptDecryptUsingSessionKey(true, sessionKey, iv, aad, srcHash);
            byte[] decryptedText = encrypter.decrypt(cipherTextWithTS, sessionKey, encSrcHash);
            Log.e("decryptedText bytea", "==>" + decryptedText);
            Log.e("decryptedText", "==>" + decryptedText.toString());

        } catch (Exception e) {
            Log.e("session Exception", "-->" + e);
            System.out.println(e.getMessage());
        }
        Log.e("pid", "--->");
        byte[] hmac = hashgenerator.generateSha256Hash(xmlPidBytes);
        String encryptedHmac = null;
        String encdata = null;

        try {
            encdata = android.util.Base64.encodeToString(cipherTextWithTS, android.util.Base64.DEFAULT);
            encryptedHmac = android.util.Base64.encodeToString(encSrcHash, android.util.Base64.DEFAULT);
        } catch (Exception ex) {
            System.out.println("Error:" + ex.getMessage());
        }

        Element mainElement = document.createElement("AuthBioData");
        document.appendChild(mainElement);

        Element rootElement = document.createElement("Auth");
        mainElement.appendChild(rootElement);
        Attr rootUid = document.createAttribute("uid");
        rootUid.setValue(aadhaarNo);
        Attr rootTid = document.createAttribute("tid");
        rootTid.setValue("");
        Attr rootTidrc = document.createAttribute("rc");
        rootTidrc.setValue(consent);

        Attr rootAc = document.createAttribute("ac");
        rootAc.setValue("");
        Attr rootSa = document.createAttribute("sa");
        rootSa.setValue("");

        Attr rootLk = document.createAttribute("lk");
        rootLk.setValue("");


        Log.e("pid", "1111111--->" + Global.TXN_FOR_AUTH);
        Attr rootTxn = document.createAttribute("txn");
        rootTxn.setValue(Global.TXN_FOR_AUTH);
        Attr rootVer = document.createAttribute("ver");
        rootVer.setValue("2.0");

        Attr rootXmlns = document.createAttribute("xmlns");
        rootXmlns.setValue("http://www.uidai.gov.in/authentication/uid-auth-request/2.0");

        Log.e("pid", "2222--->");
        rootElement.setAttributeNode(rootXmlns);
        rootElement.setAttributeNode(rootTid);
        rootElement.setAttributeNode(rootTidrc);
        rootElement.setAttributeNode(rootAc);
        rootElement.setAttributeNode(rootSa);
        rootElement.setAttributeNode(rootLk);
        rootElement.setAttributeNode(rootTxn);
        rootElement.setAttributeNode(rootVer);
//        rootElement.setAttributeNode(rootXmlns);
        rootElement.setAttributeNode(rootUid);

        //		<Uses bio="y"  bt="'.$data['bt'].'" pi="n" pa="n" pfa="n" pin="n" otp="n" />

        Element usesElement = document.createElement("Uses");

        Log.e("pid", "3333--->");
        Attr usesBio = document.createAttribute("bio");
        if (bio) {
            usesBio.setValue("y");
        } else {
            usesBio.setValue("n");
        }
        Log.e("pid", "4444--->");
        Attr usesBt = document.createAttribute("bt");
        if (deviceType.equalsIgnoreCase("F")) {
            usesBt.setValue("FMR");
        } else if (deviceType.equalsIgnoreCase("I")) {
            usesBt.setValue("IIR");
        }

        Attr usesPi = document.createAttribute("pi");
        if (pi) {
            usesPi.setValue("y");
        } else {
            usesPi.setValue("n");
        }
        Log.e("pid", "5555--->");
        Attr usesPa = document.createAttribute("pa");
        usesPa.setValue("n");
        Attr usesPfa = document.createAttribute("pfa");
        usesPfa.setValue("n");
        Attr usesPin = document.createAttribute("pin");
        usesPin.setValue("n");
        Attr usesOtp = document.createAttribute("otp");
        if (otp_check) {
            usesOtp.setValue("y");
        } else {
            usesOtp.setValue("n");
        }

        usesElement.setAttributeNode(usesBio);
        if (deviceType.equalsIgnoreCase("F") | deviceType.equalsIgnoreCase("I")) {
            usesElement.setAttributeNode(usesBt);
        }
        usesElement.setAttributeNode(usesPi);
        usesElement.setAttributeNode(usesPa);
        usesElement.setAttributeNode(usesPfa);
        usesElement.setAttributeNode(usesPin);
        usesElement.setAttributeNode(usesOtp);
        Log.e("pid", "6666--->");
        rootElement.appendChild(usesElement);


        Element metaElement = document.createElement("Meta");

        Attr metaUdc = document.createAttribute("udc");
        //metaUdc.setValue("");
        metaUdc.setValue("UDC000000001");

        Attr metardsId = document.createAttribute("rdsId");
        metardsId.setValue("");

        Attr metardsVer = document.createAttribute("rdsVer");
        metardsVer.setValue("");

        Attr metadpId = document.createAttribute("dpId");
        metadpId.setValue("");

        Attr metadc = document.createAttribute("dc");
        metadc.setValue("");

        Attr metami = document.createAttribute("mi");
        metami.setValue("");

        Attr metamc = document.createAttribute("mc");
        metamc.setValue("");


        metaElement.setAttributeNode(metardsId);
        metaElement.setAttributeNode(metardsVer);
        metaElement.setAttributeNode(metadpId);
        metaElement.setAttributeNode(metadc);
        metaElement.setAttributeNode(metami);
        metaElement.setAttributeNode(metamc);

        metaElement.setAttributeNode(metaUdc);

        rootElement.appendChild(metaElement);
        //Log.e("pid", "7777--->" + encrypter.getCertificateIdentifier());


        Element skeyElement = document.createElement("Skey");

        Attr skeyCi = document.createAttribute("ci");
        skeyCi.setValue(encrypter.getCertificateIdentifier());
       // Log.e("pid", "34634534545--->" + encrypter.getCertificateIdentifier());
        skeyElement.setAttributeNode(skeyCi);
        skeyElement.appendChild(document.createTextNode(android.util.Base64.encodeToString(
                sessionKeyDetails.getSkeyValue(), android.util.Base64.DEFAULT)));

        rootElement.appendChild(skeyElement);


        Element dataElement = document.createElement("Data");
        Attr dataType = document.createAttribute("type");
        dataType.setValue("X");
        dataElement.appendChild(document.createTextNode(encdata));
        dataElement.setAttributeNode(dataType);

        rootElement.appendChild(dataElement);


        Element hmcaElement = document.createElement("Hmac");
        hmcaElement.appendChild(document.createTextNode(encryptedHmac));

        rootElement.appendChild(hmcaElement);

        //Edited by rajesh
        Element elementUserData = document.createElement("UserData");
        Attr imeiNo = document.createAttribute("imeiNo");
        imeiNo.setValue(Global.imei);
        Attr projectInfo = document.createAttribute("projectInfo");
        projectInfo.setValue("NHPS-FVS");
        Attr macAddress = document.createAttribute("macAddress");
        macAddress.setValue("10.247.47.79");
        Attr uid = document.createAttribute("uid");
        uid.setValue(Global.VALIDATORAADHAR);
        Attr userName = document.createAttribute("userName");
        userName.setValue(Global.USER_NAME);
        Attr userPwd = document.createAttribute("userPass");
        userPwd.setValue(Global.USER_PASSWORD);

        elementUserData.setAttributeNode(imeiNo);
        elementUserData.setAttributeNode(projectInfo);
        elementUserData.setAttributeNode(macAddress);
        elementUserData.setAttributeNode(uid);
        elementUserData.setAttributeNode(userName);
        elementUserData.setAttributeNode(userPwd);
        mainElement.appendChild(elementUserData);

        Element signatureElement = document.createElement("Signature");
        signatureElement.appendChild(document.createTextNode(""));


        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = null;
        try {
            transformer = factory.newTransformer();
        } catch (TransformerConfigurationException e1) {
            e1.printStackTrace();
        }
        Log.e("pid", "0000--->");
        DOMSource domSource = new DOMSource(document);
        StringWriter writer = new StringWriter();
        //			OutputStream output = new ByteArrayOutputStream();
        StreamResult result = new StreamResult(writer);
        try {
            transformer.transform(domSource, result);
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        KycAuthXml = writer.getBuffer().toString();
        Log.e("kyc auth ", "==" + KycAuthXml);

        return KycAuthXml;

    }


    private String getPidXml(String uname, boolean bio, boolean pi,
                             String isotemplate, boolean otp_check, String otp) {
        DateFormat dfm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


//		String wadh = mywadh();
        String wadh = "";
        Log.e("mywadh", "--->" + wadh);


        long now = System.currentTimeMillis();
        Date date = new Date(now);
        Calendar localCalendar = GregorianCalendar
                .getInstance();
        localCalendar.setTime(date);
        pidTimeStamp = String.valueOf(localCalendar
                .get(Calendar.YEAR)) + "-" +
                (String.valueOf(localCalendar.get(Calendar.MONTH) + 1).length() < 2 ? "0" + String.valueOf(localCalendar
                        .get(Calendar.MONTH) + 1) : String.valueOf(localCalendar
                        .get(Calendar.MONTH) + 1))
                + "-" +
                (String.valueOf(localCalendar.get(Calendar.DATE)).length() < 2 ? "0" + String.valueOf(localCalendar.get(Calendar.DATE)) : String.valueOf(localCalendar.get(Calendar.DATE)))
                + "T" +
                (String.valueOf(localCalendar.get(Calendar.HOUR_OF_DAY)).length() < 2 ? "0" + String.valueOf(localCalendar.get(Calendar.HOUR_OF_DAY)) : String.valueOf(localCalendar.get(Calendar.HOUR_OF_DAY)))
                + ":" +
                (String.valueOf(localCalendar.get(Calendar.MINUTE)).length() < 2 ? "0" + String.valueOf(localCalendar.get(Calendar.MINUTE)) : String.valueOf(localCalendar.get(Calendar.MINUTE)))
                + ":" +
                (String.valueOf(localCalendar.get(Calendar.SECOND)).length() < 2 ? "0" + String.valueOf(localCalendar.get(Calendar.SECOND)) : String.valueOf(localCalendar.get(Calendar.SECOND)));
        System.out.println("PID TIME STAMP:" + pidTimeStamp);
        String ctime = dfm.format(date).replace(" ", "T");
        if (!Global.FLAG) {
            Global.R_PID_TIME = ctime;
        }
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory
                    .newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // root elements
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("Pid");
            doc.appendChild(rootElement);

            Attr attr = doc.createAttribute("ts");
            attr.setValue(ctime);
            rootElement.setAttributeNode(attr);

            attr = doc.createAttribute("ver");
            attr.setValue(String.valueOf("2.0"));
            rootElement.setAttributeNode(attr);

            attr = doc.createAttribute("wadh");
            attr.setValue(wadh);
            rootElement.setAttributeNode(attr);


            if (pi) {

                Element demo = doc.createElement("Demo");
                rootElement.appendChild(demo);

                attr = doc.createAttribute("lang");
                attr.setValue("06");
                demo.setAttributeNode(attr);

                Element piNode = doc.createElement("Pi");
                demo.appendChild(piNode);

                attr = doc.createAttribute("name");
                attr.setValue(uname);
                piNode.setAttributeNode(attr);

                attr = doc.createAttribute("mv");
                attr.setValue("100");
                piNode.setAttributeNode(attr);

                attr = doc.createAttribute("ms");
                attr.setValue("P");
                piNode.setAttributeNode(attr);
            }

            // bio Start

            if (bio) {

                Log.e("isotemplate", "==" + isotemplate);
                String fpdata = isotemplate;
                Element bios = doc.createElement("Bios");
                rootElement.appendChild(bios);
                Element bioElement = doc.createElement("Bio");
                attr = doc.createAttribute("type");
                if (bioDeviceType.equalsIgnoreCase("F")) {
                    attr.setValue("FMR");
                } else if (bioDeviceType.equalsIgnoreCase("I")) {
                    attr.setValue("IIR");
                }

                bioElement.setAttributeNode(attr);

                attr = doc.createAttribute("posh");
                attr.setValue("UNKNOWN");
                bioElement.setAttributeNode(attr);

                bioElement.appendChild(doc.createTextNode(fpdata));
                bios.appendChild(bioElement);
            }
            // Bio End

            //	OTP Start

            if (otp_check) {

                Element eleOtp = doc.createElement("Pv");

                attr = doc.createAttribute("otp");
                attr.setValue(otp);
                eleOtp.setAttributeNode(attr);

                attr = doc.createAttribute("pin");
                attr.setValue("");
                //				eleOtp.setAttributeNode(attr);

                rootElement.appendChild(eleOtp);


            }

            // OTP End

            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory
                    .newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);

            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);

            transformer.transform(source, result);
            String output = writer.getBuffer().toString()
                    .replaceAll("\n|\r", "");
            Log.e("PID XML", "=" + output);
            //appendLog(output);
            return output;
        } catch (Exception ex) {
            System.out.println("Error:" + ex.getMessage());
        }
        return null;
    }

}






