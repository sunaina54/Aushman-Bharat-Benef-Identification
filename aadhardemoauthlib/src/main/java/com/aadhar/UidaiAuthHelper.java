package com.aadhar;

import android.util.Base64;
import android.util.Log;

import com.sec.biometric.iris.SecIrisConstants;
import com.sec.biometric.iris.SecIrisManager;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.cert.X509Certificate;
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

import static com.aadhar.UidaiAuthHelper.encrypter;
import static com.aadhar.UidaiAuthHelper.hashgenerator;

public class UidaiAuthHelper {

    static String licenseKey = "";
    static Encrypter encrypter;
    static HashGenerator hashgenerator;
    static byte[] authpubkey;
    private static SynchronizedKey synchronizedKey = null;
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

    public UidaiAuthHelper(byte[] publicKey) {
        LoadInitial(publicKey);
    }

    public void LoadInitial(byte[] publicKey) {
        System.out.println("Encryption Initilized");
        hashgenerator = new HashGenerator();
        getAuthPubKey(publicKey);

    }

	/*public String getXMLAttribute(String inxml, String attribute) {
        XmlUtility utility = new XmlUtility();
		return utility.getNodeAttribute(inxml, attribute);
	}*/

    public String getAUAURL() {
        return auaurl;
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

    private void addXmlElementAttribute(String attributeName,
                                        String attributeValue, Document doc, Element rootElement) {
        Attr attr = doc.createAttribute(attributeName);
        attr.setValue(attributeValue);
        rootElement.setAttributeNode(attr);
    }

    private String getPidXmlIris(String uname, boolean bio, boolean pi,
                                 String isotemplate) {
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
        System.out.println("PID TIME STAMP:" + pidTimeStamp);

        String ctime = dfm.format(date).replace(" ", "T");
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

            attr = doc.createAttribute("xmlns");
            attr.setValue("http://www.uidai.gov.in/authentication/uid-auth-request-data/1.0");
            rootElement.setAttributeNode(attr);

            if (pi) {
                Element demo = doc.createElement("Demo");
                rootElement.appendChild(demo);

                Element piNode = doc.createElement("Pi");
                demo.appendChild(piNode);

                attr = doc.createAttribute("name");
                attr.setValue(uname);
                piNode.setAttributeNode(attr);

                attr = doc.createAttribute("mv");
                attr.setValue("100");
                piNode.setAttributeNode(attr);

                attr = doc.createAttribute("ms");
                attr.setValue("E");
                piNode.setAttributeNode(attr);
            }

            // bio Start

            if (bio) {
                // String fpdata= Base64.encodeToString(isotemplate,
                // Base64.DEFAULT);
                String fpdata = isotemplate;
                Element bios = doc.createElement("Bios");
                rootElement.appendChild(bios);
                Element bioElement = doc.createElement("Bio");

                attr = doc.createAttribute("type");
                attr.setValue("IIR");
                bioElement.setAttributeNode(attr);

                attr = doc.createAttribute("posh");
                attr.setValue("UNKNOWN");
                bioElement.setAttributeNode(attr);

                bioElement.appendChild(doc.createTextNode(fpdata));
                bios.appendChild(bioElement);
            }
            // Bio End

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
            return output;
        } catch (Exception ex) {
            System.out.println("Error:" + ex.getMessage());
        }
        return null;
    }

    public String createOTPXML(String aadhaar) {
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
        System.out.println("PID TIME STAMP:" + pidTimeStamp);

        String ctime = dfm.format(date).replace(" ", "T");


        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory
                    .newInstance();
            DocumentBuilder docBuilder;
            docBuilder = docFactory.newDocumentBuilder();

            // root elements
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("otp");
            doc.appendChild(rootElement);

            Attr attrUid = doc.createAttribute("uid");
            attrUid.setValue(aadhaar);
            rootElement.setAttributeNode(attrUid);

            Attr attrTid = doc.createAttribute("tid");
            attrTid.setValue("public");
            rootElement.setAttributeNode(attrTid);

            Attr attrAc = doc.createAttribute("ac");
            attrAc.setValue("public");
            rootElement.setAttributeNode(attrAc);

            Attr attrSa = doc.createAttribute("sa");
            attrSa.setValue("");
            rootElement.setAttributeNode(attrSa);

            Attr attrVer = doc.createAttribute("ver");
            attrVer.setValue(String.valueOf(Global.version));
            rootElement.setAttributeNode(attrVer);

            Attr attrTxn = doc.createAttribute("txn");
            attrTxn.setValue("test:NIC");
            rootElement.setAttributeNode(attrTxn);

            Attr attrLk = doc.createAttribute("lk");
            attrLk.setValue(licenseKey);
            rootElement.setAttributeNode(attrLk);

            Attr attrTs = doc.createAttribute("ts");
            attrTs.setValue(ctime);
            rootElement.setAttributeNode(attrTs);

            Element eleOpts = doc.createElement("opts");
            doc.appendChild(eleOpts);

            Attr attrCh = doc.createAttribute("ch");
            attrCh.setValue("00");
            eleOpts.appendChild(attrCh);

            doc.appendChild(eleOpts);

            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = null;
            try {
                transformer = factory.newTransformer();
            } catch (TransformerConfigurationException e1) {
                e1.printStackTrace();
            }
            DOMSource domSource =
                    new DOMSource(doc.getDocumentElement());
            OutputStream output = new ByteArrayOutputStream();
            StreamResult result = new StreamResult(output);
            try {
                transformer.transform(domSource, result);
            } catch (TransformerException e) {
                e.printStackTrace();
            }
            String xmlString = output.toString();
            Log.e("xml", " = " + xmlString);
            return xmlString;


        } catch (ParserConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        return null;
    }

    private String getPidXmlForMou(String uname, boolean bio, boolean pi, String isotemplate, boolean otp_check, String otp) {
        DateFormat dfm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long now = System.currentTimeMillis();
        String output = null;
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
            attr.setValue(String.valueOf(Global.version));
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
                // String fpdata= Base64.encodeToString(isotemplate,
                // Base64.DEFAULT);
                Log.e("isotemplate", "==" + isotemplate);
                String fpdata = isotemplate;
                Element bios = doc.createElement("Bios");
                rootElement.appendChild(bios);
                Element bioElement = doc.createElement("Bio");
                attr = doc.createAttribute("type");
                if (uname.equalsIgnoreCase("F")) {
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
            output = writer.getBuffer().toString()
                    .replaceAll("\n|\r", "");
            Log.e("PID XML", "=" + output);
            //appendLog(output);
            //return output;
        } catch (Exception ex) {
            System.out.println("Error:" + ex.getMessage());
        }
        return output;
    }

    private String getPidXml(String uname, boolean bio, boolean pi,
                             String isotemplate, boolean otp_check, String otp) {
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

        System.out.println("PID TIME STAMP:" + pidTimeStamp);
        String ctime = dfm.format(date).replace(" ", "T");
        Global.R_PID_TIME = pidTimeStamp;
        if (!Global.FLAG) {
            Global.R_PID_TIME = pidTimeStamp;
            appendLogInvalidXmlAWithoutRad(Global.R_PID_TIME, "NormalPidTime");
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
            // attr.setValue(ctime);
            attr.setValue(pidTimeStamp);
            rootElement.setAttributeNode(attr);

            attr = doc.createAttribute("ver");
            attr.setValue(String.valueOf(Global.version));
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
                // String fpdata= Base64.encodeToString(isotemplate,
                // Base64.DEFAULT);
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

    private String getPidXmlForDemoAuth(boolean pi, boolean pa, boolean pfa, String pi_ms, String pi_mv, String pi_name
            , String pi_gender, String pi_dob, String pi_dobt, String pi_age, String pi_ph, String pi_mail, String pa_co, String pa_house
            , String pa_street, String pa_landmark, String pa_loc, String pa_vill, String pa_subdist, String pa_dist, String pa_state, String pa_pc
            , String pa_po, String pfa_ms, String pfa_mv, String pfa_av) {

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
            attr.setValue(String.valueOf(Global.version));
            rootElement.setAttributeNode(attr);

            Element demo = doc.createElement("Demo");
            rootElement.appendChild(demo);

            attr = doc.createAttribute("lang");
            attr.setValue("06");
//			demo.setAttributeNode(attr);


            if (pi) {


                Element piNode = doc.createElement("Pi");
                demo.appendChild(piNode);

                attr = doc.createAttribute("name");
                attr.setValue(pi_name);
                if (!pi_name.equalsIgnoreCase("")) {
                    piNode.setAttributeNode(attr);
                }

                attr = doc.createAttribute("mv");
                attr.setValue(pi_mv);
                if (!pi_mv.equalsIgnoreCase("")) {
                    piNode.setAttributeNode(attr);
                }

                attr = doc.createAttribute("ms");
                attr.setValue(pi_ms);
                if (!pi_ms.equalsIgnoreCase("")) {
                    piNode.setAttributeNode(attr);
                }

				/*attr = doc.createAttribute("lmv");
            attr.setValue("");
			piNode.setAttributeNode(attr);*/

				/*attr = doc.createAttribute("lname");
            attr.setValue("");
			piNode.setAttributeNode(attr);*/

                attr = doc.createAttribute("gender");
                attr.setValue(pi_gender);
                if (!pi_gender.equalsIgnoreCase("")) {
                    piNode.setAttributeNode(attr);
                }

                attr = doc.createAttribute("dob");
                attr.setValue(pi_dob);
                if (!pi_dob.equalsIgnoreCase("")) {
                    piNode.setAttributeNode(attr);
                }

                attr = doc.createAttribute("dobt");
                attr.setValue(pi_dobt);
                if (!pi_dobt.equalsIgnoreCase("")) {
                    piNode.setAttributeNode(attr);
                }

                attr = doc.createAttribute("age");
                attr.setValue(pi_age);
                if (!pi_age.equalsIgnoreCase("")) {
                    piNode.setAttributeNode(attr);
                }

                attr = doc.createAttribute("phone");
                attr.setValue(pi_ph);
                if (!pi_ph.equalsIgnoreCase("")) {
                    piNode.setAttributeNode(attr);
                }

                attr = doc.createAttribute("email");
                attr.setValue(pi_mail);
                if (!pi_mail.equalsIgnoreCase("")) {
                    piNode.setAttributeNode(attr);
                }

            }


            if (pa) {


                Element paNode = doc.createElement("Pa");
                demo.appendChild(paNode);

                attr = doc.createAttribute("ms");
                attr.setValue("E");
                paNode.setAttributeNode(attr);

                attr = doc.createAttribute("co");
                attr.setValue(pa_co);
                if (!pa_co.equalsIgnoreCase("")) {
                    paNode.setAttributeNode(attr);
                }

                attr = doc.createAttribute("house");
                attr.setValue(pa_house);
                if (!pa_house.equalsIgnoreCase("")) {
                    paNode.setAttributeNode(attr);
                }

                attr = doc.createAttribute("street");
                attr.setValue(pa_street);
                if (!pa_street.equalsIgnoreCase("")) {
                    paNode.setAttributeNode(attr);
                }

                attr = doc.createAttribute("lm");
                attr.setValue(pa_landmark);
                if (!pa_landmark.equalsIgnoreCase("")) {
                    paNode.setAttributeNode(attr);
                }

                attr = doc.createAttribute("loc");
                attr.setValue(pa_loc);
                if (!pa_loc.equalsIgnoreCase("")) {
                    paNode.setAttributeNode(attr);
                }

                attr = doc.createAttribute("vtc");
                attr.setValue(pa_vill);
                if (!pa_vill.equalsIgnoreCase("")) {
                    paNode.setAttributeNode(attr);
                }

                attr = doc.createAttribute("subdist");
                attr.setValue(pa_subdist);
                if (!pa_subdist.equalsIgnoreCase("")) {
                    paNode.setAttributeNode(attr);
                }

                attr = doc.createAttribute("dist");
                attr.setValue(pa_dist);
                if (!pa_dist.equalsIgnoreCase("")) {
                    paNode.setAttributeNode(attr);
                }

                attr = doc.createAttribute("state");
                attr.setValue(pa_state);
                if (!pa_state.equalsIgnoreCase("")) {
                    paNode.setAttributeNode(attr);
                }

                attr = doc.createAttribute("pc");
                attr.setValue(pa_pc);
                if (!pa_pc.equalsIgnoreCase("")) {
                    paNode.setAttributeNode(attr);
                }

                attr = doc.createAttribute("po");
                attr.setValue(pa_po);
                if (!pa_po.equalsIgnoreCase("")) {
                    paNode.setAttributeNode(attr);
                }

            }

            if (pfa) {

                Element pfaNode = doc.createElement("Pfa");
                demo.appendChild(pfaNode);

                attr = doc.createAttribute("ms");
                attr.setValue(pfa_ms);
                if (!pfa_ms.equalsIgnoreCase("")) {
                    pfaNode.setAttributeNode(attr);
                }

                attr = doc.createAttribute("mv");
                attr.setValue(pfa_mv);
                if (!pfa_mv.equalsIgnoreCase("")) {
                    pfaNode.setAttributeNode(attr);
                }

                attr = doc.createAttribute("av");
                attr.setValue(pfa_av);
                if (!pfa_av.equalsIgnoreCase("")) {
                    pfaNode.setAttributeNode(attr);
                }

            }


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

    public String GenerateAuthXMLForFingerPrint(String uid, String name,
                                                String timeout, boolean bio, boolean pi, String isotemplate,
                                                String imei) {

        byte[] syncSessionKey = null;
        byte[] encryptedSessionKey = null;
        SessionKeyDetails sessionKeyDetails = null;

        Date date = new Date();

        try {
            synchronizedKey = new SynchronizedKey(
                    encrypter.generateSessionKey(), UUID.randomUUID()
                    .toString(), date);
            syncSessionKey = synchronizedKey.getSeedSkey();
            encryptedSessionKey = encrypter
                    .encryptUsingPublicKey(syncSessionKey);
            sessionKeyDetails = SessionKeyDetails
                    .createSkeyToInitializeSynchronizedKey(
                            synchronizedKey.getKeyIdentifier(),
                            encryptedSessionKey);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        byte[] xmlPidBytes = getPidXml(name, bio, pi, isotemplate, false, "").getBytes();
        byte[] hmac = hashgenerator.generateSha256Hash(xmlPidBytes);
        String encryptedHmac = null;
        String encdata = null;

        try {
            encdata = Base64.encodeToString(encrypter.encryptUsingSessionKey(
                    syncSessionKey, xmlPidBytes), Base64.DEFAULT);
            encryptedHmac = Base64.encodeToString(
                    encrypter.encryptUsingSessionKey(syncSessionKey, hmac),
                    Base64.DEFAULT);
        } catch (Exception ex) {
            System.out.println("Error:" + ex.getMessage());
        }

        try {

            DocumentBuilderFactory docFactory = DocumentBuilderFactory
                    .newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // root elements
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("Auth");
            doc.appendChild(rootElement);

            // Adding additional attributes for India Post
            Attr attr = null;

            addXmlElementAttribute("uid", uid, doc, rootElement);
            addXmlElementAttribute("tid", "public", doc, rootElement);
            addXmlElementAttribute("sa", "", doc, rootElement);
            addXmlElementAttribute("txn", "uidai", doc, rootElement);
            addXmlElementAttribute(
                    "xmlns",
                    "http://www.uidai.gov.in/authentication/uid-auth-request/1.0",
                    doc, rootElement);

            Element uses = doc.createElement("Uses");
            rootElement.appendChild(uses);

            addXmlElementAttribute("otp", "n", doc, uses);
            addXmlElementAttribute("pin", "n", doc, uses);
            addXmlElementAttribute("pfa", "n", doc, uses);
            addXmlElementAttribute("pa", "n", doc, uses);

            if (pi) {
                addXmlElementAttribute("pi", "y", doc, uses);
            } else {
                addXmlElementAttribute("pi", "n", doc, uses);
            }

            if (bio) {
                addXmlElementAttribute("bio", "y", doc, uses);
                addXmlElementAttribute("bt", "FMR", doc, uses);
            } else {
                addXmlElementAttribute("bio", "n", doc, uses);
            }

            Element meta = doc.createElement("Meta");
            rootElement.appendChild(meta);

            addXmlElementAttribute("udc", imei, doc, meta);
            addXmlElementAttribute("fdc", "NC", doc, meta);
            addXmlElementAttribute("idc", "NA", doc, meta);
            addXmlElementAttribute("pip", "127.0.0.1", doc, meta);

            addXmlElementAttribute("lot", "P", doc, meta);
            addXmlElementAttribute("lov", "834001", doc, meta);

            Element skey = doc.createElement("Skey");
            rootElement.appendChild(skey);
            addXmlElementAttribute("ci", encrypter.getCertificateIdentifier(),
                    doc, skey);
            skey.appendChild(doc.createTextNode(Base64.encodeToString(
                    sessionKeyDetails.getSkeyValue(), Base64.DEFAULT)));

            Element data = doc.createElement("Data");
            attr = doc.createAttribute("type");
            attr.setValue("X");
            data.setAttributeNode(attr);
            data.appendChild(doc.createTextNode(encdata));
            rootElement.appendChild(data);

            Element mac = doc.createElement("Hmac");
            mac.appendChild(doc.createTextNode(encryptedHmac));
            rootElement.appendChild(mac);

            TransformerFactory transformerFactory = TransformerFactory
                    .newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);

            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);
            transformer.transform(source, result);
            String output = writer.getBuffer().toString()
                    .replaceAll("\n|\r", "");

            return output;

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public void appendLog(String text) {
        File logFile = new File("sdcard/log1.text");
        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        try {
            //BufferedWriter for performance, true to set append to file flag
            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
            buf.append(text);
            buf.newLine();
            buf.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public String createXmlForAuth(String aadhaarNo, String uname, boolean bio, boolean demo, boolean otp, String isoTemplate, String pin) {
        Log.e("Global.MOU", "=== " + Global.MOU);
        Log.e("Global.FLAG", "== " + Global.FLAG);
        //appendLog(Global.MOU+"");
        //appendLog(Global.FLAG+"");
        Log.e("in aadhar", "===+" + aadhaarNo);
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
                +
                (String.valueOf(localCalendar.get(Calendar.HOUR_OF_DAY)).length() < 2 ? "0" + String.valueOf(localCalendar.get(Calendar.HOUR_OF_DAY)) : String.valueOf(localCalendar.get(Calendar.HOUR_OF_DAY)))
                + ":" +
                (String.valueOf(localCalendar.get(Calendar.MINUTE)).length() < 2 ? "0" + String.valueOf(localCalendar.get(Calendar.MINUTE)) : String.valueOf(localCalendar.get(Calendar.MINUTE)))
                + ":" +
                (String.valueOf(localCalendar.get(Calendar.SECOND)).length() < 2 ? "0" + String.valueOf(localCalendar.get(Calendar.SECOND)) : String.valueOf(localCalendar.get(Calendar.SECOND)));

        String txn = null;

        txn = aadhaarNo + pidTimeStamp + "TEST";
        Log.e("txn ", "==" + txn);

        String xmlString = null;
        byte[] syncSessionKey = null;
        byte[] encryptedSessionKey = null;
        SessionKeyDetails sessionKeyDetails = null;

        try {
            synchronizedKey = new SynchronizedKey(
                    encrypter.generateSessionKey(), UUID.randomUUID()
                    .toString(), new Date());
            Log.e("synchronizedKey", "==" + synchronizedKey);

            syncSessionKey = synchronizedKey.getSeedSkey();
            Log.e("syncSessionKey", "==" + syncSessionKey);

            encryptedSessionKey = encrypter.encryptUsingPublicKey(syncSessionKey);

            sessionKeyDetails = SessionKeyDetails
                    .createSkeyToInitializeSynchronizedKey(
                            synchronizedKey.getKeyIdentifier(),
                            encryptedSessionKey);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            Log.e("Exception", "==" + e);
        }

        Log.e("encryptedSessionKey", "==" + encryptedSessionKey);
        Log.e("sessionKeyDetails", "==" + sessionKeyDetails);


        byte[] xmlPidBytes = getPidXml(uname, bio, demo, isoTemplate, otp, pin).getBytes();
        byte[] hmac = hashgenerator.generateSha256Hash(xmlPidBytes);
        String encryptedHmac = null;
        String encdata = null;

        try {
            encdata = Base64.encodeToString(encrypter.encryptUsingSessionKey(
                    syncSessionKey, xmlPidBytes), Base64.DEFAULT);
            encryptedHmac = Base64.encodeToString(
                    encrypter.encryptUsingSessionKey(syncSessionKey, hmac),
                    Base64.DEFAULT);
        } catch (Exception ex) {
            System.out.println("Error:" + ex.getMessage());
        }

        try {
            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
            org.w3c.dom.Document document = documentBuilder.newDocument();
            document.setXmlStandalone(true);

            //			<Auth uid="614422050245" tid="public" sa="NICAPI" txn="614422050245201508061702049979"
            //			xmlns="http://www.uidai.gov.in/authentication/uid-auth-request/1.0">

            // Root Element
            Element rootElement = document.createElement("Auth");
            document.appendChild(rootElement);
            // setting attribute to element
            Attr attrUid = document.createAttribute("uid");
            attrUid.setValue(aadhaarNo);
            Attr attrTid = document.createAttribute("tid");
            attrTid.setValue("public");
            Attr attrSa = document.createAttribute("sa");
            attrSa.setValue("");
            Attr attrTxn = document.createAttribute("txn");
            if (Global.MOU) {
                if (Global.FLAG) {
                    attrTxn.setValue("UMN:O:" + txn.replace("-", "").replace(":", ""));

                } else if (!Global.FLAG) {
                    attrTxn.setValue("UMN:R:" + txn.replace("-", "").replace(":", ""));

                }
            } else {
                attrTxn.setValue(txn.replace("-", "").replace(":", ""));

            }


            Attr attrXmlns = document.createAttribute("xmlns");
            attrXmlns.setValue("http://www.uidai.gov.in/authentication/uid-auth-request/1.0");

            Attr rootLk = document.createAttribute("lk");
            rootLk.setValue(licenseKey);

            Attr rootVer = document.createAttribute("ver");
            rootVer.setValue(String.valueOf(Global.AUTH_version));

            rootElement.setAttributeNode(attrUid);
            rootElement.setAttributeNode(attrTid);
            rootElement.setAttributeNode(attrSa);
            rootElement.setAttributeNode(attrTxn);
            rootElement.setAttributeNode(rootLk);
            rootElement.setAttributeNode(rootVer);
            rootElement.setAttributeNode(attrXmlns);

            //			<Uses pi="n" pa="n" pfa="n" bio="y" bt="FMR" pin="n" otp="n" />
            String bio_check = "n", pi_check = "n", otp_check = "n", bt = "";
            if (bio) {
                bio_check = "y";
                if (Global.iritechAttached) {
                    bt = "IIR";
                } else {
                    bt = "FMR";
                }
            } else if (demo) {
                pi_check = "y";

            } else if (otp) {
                otp_check = "y";
            }

            Element elementUses = document.createElement("Uses");

            Attr attrPi = document.createAttribute("pi");
            attrPi.setValue(pi_check);
            Attr attrPa = document.createAttribute("pa");
            attrPa.setValue("n");
            Attr attrPfa = document.createAttribute("pfa");
            attrPfa.setValue("n");
            Attr attrBio = document.createAttribute("bio");
            attrBio.setValue(bio_check);
            Attr attrBt = document.createAttribute("bt");
            attrBt.setValue(bt);
            Attr attrPin = document.createAttribute("pin");
            attrPin.setValue("n");
            Attr attrOtp = document.createAttribute("otp");
            attrOtp.setValue(otp_check);
            elementUses.setAttributeNode(attrPi);
            elementUses.setAttributeNode(attrPa);
            elementUses.setAttributeNode(attrPfa);
            elementUses.setAttributeNode(attrBio);
            elementUses.setAttributeNode(attrBt);
            elementUses.setAttributeNode(attrPin);
            elementUses.setAttributeNode(attrOtp);
            rootElement.appendChild(elementUses);

            Element elementMeta = document.createElement("Meta");

            Attr attrUdc = document.createAttribute("udc");
            if (Global.MOU) {
                attrUdc.setValue("NICTEST");
            } else {

                attrUdc.setValue(Global.DEVICE_IMEI_NO);
            }
            Attr attrPip = document.createAttribute("pip");
            attrPip.setValue("10.249.34.242");
            Attr attrFdc = document.createAttribute("fdc");
            attrFdc.setValue("NC");
            Attr attrIdc = document.createAttribute("idc");
            attrIdc.setValue("NA");
            Attr attrLot = document.createAttribute("lot");
            attrLot.setValue("P");
            Attr attrLov = document.createAttribute("lov");
            attrLov.setValue("110011");
            elementMeta.setAttributeNode(attrUdc);
            elementMeta.setAttributeNode(attrPip);
            elementMeta.setAttributeNode(attrFdc);
            elementMeta.setAttributeNode(attrIdc);
            elementMeta.setAttributeNode(attrLot);
            elementMeta.setAttributeNode(attrLov);
            rootElement.appendChild(elementMeta);

            Element elementSkey = document.createElement("Skey");

            Attr attrCi = document.createAttribute("ci");
            if (Global.MOU) {
                attrCi.setValue("20170227");
            } else {
                attrCi.setValue(encrypter.getCertificateIdentifier());
            }
            attrCi.setValue(encrypter.getCertificateIdentifier());
            elementSkey.setAttributeNode(attrCi);

            elementSkey.appendChild(document.createTextNode(Base64.encodeToString(
                    sessionKeyDetails.getSkeyValue(), Base64.DEFAULT)));

            rootElement.appendChild(elementSkey);

            Element elementData = document.createElement("Data");

            Attr attrtype = document.createAttribute("type");
            attrtype.setValue("X");
            elementData.setAttributeNode(attrtype);
            elementData.appendChild(document.createTextNode(encdata));
            rootElement.appendChild(elementData);

            Element elementHmac = document.createElement("Hmac");
            elementHmac.appendChild(document.createTextNode(encryptedHmac));
            rootElement.appendChild(elementHmac);

            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = null;

            try {
                transformer = factory.newTransformer();
            } catch (TransformerConfigurationException e1) {
                e1.printStackTrace();
            }

            DOMSource domSource =
                    new DOMSource(document.getDocumentElement());
            OutputStream output = new ByteArrayOutputStream();
            StreamResult result = new StreamResult(output);
            try {
                transformer.transform(domSource, result);
            } catch (TransformerException e) {
                e.printStackTrace();
            }
            //			transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            xmlString = output.toString();
            Log.e("xml", " = " + xmlString);
            //appendLog(xmlString);
            return xmlString;

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            Log.e("ParserConfig", "=" + e);
        }
        return xmlString;


    }

    public String createXmlForAuthForMOU(String aadhaarNo, String uname, boolean bio, boolean demo, boolean otp, String isoTemplate, String pin) {
        Log.e("Global.MOU", "=== " + Global.MOU);
        Log.e("Global.FLAG", "== " + Global.FLAG);
        //appendLog(Global.MOU+"");
        //appendLog(Global.FLAG+"");
        Log.e("in aadhar", "===+" + aadhaarNo);
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
                +
                (String.valueOf(localCalendar.get(Calendar.HOUR_OF_DAY)).length() < 2 ? "0" + String.valueOf(localCalendar.get(Calendar.HOUR_OF_DAY)) : String.valueOf(localCalendar.get(Calendar.HOUR_OF_DAY)))
                + ":" +
                (String.valueOf(localCalendar.get(Calendar.MINUTE)).length() < 2 ? "0" + String.valueOf(localCalendar.get(Calendar.MINUTE)) : String.valueOf(localCalendar.get(Calendar.MINUTE)))
                + ":" +
                (String.valueOf(localCalendar.get(Calendar.SECOND)).length() < 2 ? "0" + String.valueOf(localCalendar.get(Calendar.SECOND)) : String.valueOf(localCalendar.get(Calendar.SECOND)));

        String txn = null;

        txn = aadhaarNo + pidTimeStamp + "TEST";
        Log.e("txn ", "==" + txn);

        String xmlString = null;
        byte[] syncSessionKey = null;
        byte[] encryptedSessionKey = null;
        SessionKeyDetails sessionKeyDetails = null;

        try {
            synchronizedKey = new SynchronizedKey(
                    encrypter.generateSessionKey(), UUID.randomUUID()
                    .toString(), new Date());
            Log.e("synchronizedKey", "==" + synchronizedKey);

            syncSessionKey = synchronizedKey.getSeedSkey();
            Log.e("syncSessionKey", "==" + syncSessionKey);

            encryptedSessionKey = encrypter.encryptUsingPublicKey(syncSessionKey);

            sessionKeyDetails = SessionKeyDetails
                    .createSkeyToInitializeSynchronizedKey(
                            synchronizedKey.getKeyIdentifier(),
                            encryptedSessionKey);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            Log.e("Exception", "==" + e);
        }

        Log.e("encryptedSessionKey", "==" + encryptedSessionKey);
        Log.e("sessionKeyDetails", "==" + sessionKeyDetails);


        byte[] xmlPidBytes = getPidXmlForMou(uname, bio, demo, isoTemplate, otp, pin).getBytes();
        byte[] hmac = hashgenerator.generateSha256Hash(xmlPidBytes);
        String encryptedHmac = null;
        String encdata = null;

        try {
            encdata = Base64.encodeToString(encrypter.encryptUsingSessionKey(
                    syncSessionKey, xmlPidBytes), Base64.DEFAULT);
            encryptedHmac = Base64.encodeToString(
                    encrypter.encryptUsingSessionKey(syncSessionKey, hmac),
                    Base64.DEFAULT);
        } catch (Exception ex) {
            System.out.println("Error:" + ex.getMessage());
        }

        try {
            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
            org.w3c.dom.Document document = documentBuilder.newDocument();
            document.setXmlStandalone(true);

            //			<Auth uid="614422050245" tid="public" sa="NICAPI" txn="614422050245201508061702049979"
            //			xmlns="http://www.uidai.gov.in/authentication/uid-auth-request/1.0">

            // Root Element
            Element rootElement = document.createElement("Auth");
            document.appendChild(rootElement);
            // setting attribute to element
            Attr attrUid = document.createAttribute("uid");
            attrUid.setValue(aadhaarNo);
            Attr attrTid = document.createAttribute("tid");
            attrTid.setValue("public");
            Attr attrSa = document.createAttribute("sa");
            attrSa.setValue("");
            Attr attrTxn = document.createAttribute("txn");
            if (Global.MOU) {
                if (Global.FLAG) {
                    attrTxn.setValue("UMN:O:" + txn.replace("-", "").replace(":", ""));

                } else if (!Global.FLAG) {
                    attrTxn.setValue("UMN:R:" + txn.replace("-", "").replace(":", ""));

                }
            } else {
                attrTxn.setValue(txn.replace("-", "").replace(":", ""));

            }


            Attr attrXmlns = document.createAttribute("xmlns");
            attrXmlns.setValue("http://www.uidai.gov.in/authentication/uid-auth-request/1.0");

            Attr rootLk = document.createAttribute("lk");
            rootLk.setValue(licenseKey);

            Attr rootVer = document.createAttribute("ver");
            rootVer.setValue(String.valueOf(Global.AUTH_version));

            rootElement.setAttributeNode(attrUid);
            rootElement.setAttributeNode(attrTid);
            rootElement.setAttributeNode(attrSa);
            rootElement.setAttributeNode(attrTxn);
            rootElement.setAttributeNode(rootLk);
            rootElement.setAttributeNode(rootVer);
            rootElement.setAttributeNode(attrXmlns);

            //			<Uses pi="n" pa="n" pfa="n" bio="y" bt="FMR" pin="n" otp="n" />
            String bio_check = "n", pi_check = "n", otp_check = "n", bt = "";
            if (bio) {
                bio_check = "y";
                if (Global.iritechAttached) {
                    bt = "IIR";
                } else {
                    bt = "FMR";
                }
            } else if (demo) {
                pi_check = "y";

            } else if (otp) {
                otp_check = "y";
            }

            Element elementUses = document.createElement("Uses");

            Attr attrPi = document.createAttribute("pi");
            attrPi.setValue(pi_check);
            Attr attrPa = document.createAttribute("pa");
            attrPa.setValue("n");
            Attr attrPfa = document.createAttribute("pfa");
            attrPfa.setValue("n");
            Attr attrBio = document.createAttribute("bio");
            attrBio.setValue(bio_check);
            Attr attrBt = document.createAttribute("bt");
            attrBt.setValue(bt);
            Attr attrPin = document.createAttribute("pin");
            attrPin.setValue("n");
            Attr attrOtp = document.createAttribute("otp");
            attrOtp.setValue(otp_check);
            elementUses.setAttributeNode(attrPi);
            elementUses.setAttributeNode(attrPa);
            elementUses.setAttributeNode(attrPfa);
            elementUses.setAttributeNode(attrBio);
            elementUses.setAttributeNode(attrBt);
            elementUses.setAttributeNode(attrPin);
            elementUses.setAttributeNode(attrOtp);
            rootElement.appendChild(elementUses);

            Element elementMeta = document.createElement("Meta");

            Attr attrUdc = document.createAttribute("udc");
            if (Global.MOU) {
                attrUdc.setValue("NICTEST");
            } else {

                attrUdc.setValue(Global.DEVICE_IMEI_NO);
            }
            Attr attrPip = document.createAttribute("pip");
            attrPip.setValue("10.249.34.242");
            Attr attrFdc = document.createAttribute("fdc");
            attrFdc.setValue("NC");
            Attr attrIdc = document.createAttribute("idc");
            attrIdc.setValue("NA");
            Attr attrLot = document.createAttribute("lot");
            attrLot.setValue("P");
            Attr attrLov = document.createAttribute("lov");
            attrLov.setValue("110011");
            elementMeta.setAttributeNode(attrUdc);
            elementMeta.setAttributeNode(attrPip);
            elementMeta.setAttributeNode(attrFdc);
            elementMeta.setAttributeNode(attrIdc);
            elementMeta.setAttributeNode(attrLot);
            elementMeta.setAttributeNode(attrLov);
            rootElement.appendChild(elementMeta);

            Element elementSkey = document.createElement("Skey");

            Attr attrCi = document.createAttribute("ci");
            if (Global.MOU) {
                attrCi.setValue("20170227");
            } else {
                attrCi.setValue(encrypter.getCertificateIdentifier());
            }
            attrCi.setValue(encrypter.getCertificateIdentifier());
            elementSkey.setAttributeNode(attrCi);

            elementSkey.appendChild(document.createTextNode(Base64.encodeToString(
                    sessionKeyDetails.getSkeyValue(), Base64.DEFAULT)));

            rootElement.appendChild(elementSkey);

            Element elementData = document.createElement("Data");

            Attr attrtype = document.createAttribute("type");
            attrtype.setValue("X");
            elementData.setAttributeNode(attrtype);
            elementData.appendChild(document.createTextNode(encdata));
            rootElement.appendChild(elementData);

            Element elementHmac = document.createElement("Hmac");
            elementHmac.appendChild(document.createTextNode(encryptedHmac));
            rootElement.appendChild(elementHmac);

            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = null;

            try {
                transformer = factory.newTransformer();
            } catch (TransformerConfigurationException e1) {
                e1.printStackTrace();
            }

            DOMSource domSource =
                    new DOMSource(document.getDocumentElement());
            OutputStream output = new ByteArrayOutputStream();
            StreamResult result = new StreamResult(output);
            try {
                transformer.transform(domSource, result);
            } catch (TransformerException e) {
                e.printStackTrace();
            }
            //			transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            xmlString = output.toString();
            Log.e("xml", " = " + xmlString);
            //appendLog(xmlString);
            return xmlString;

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            Log.e("ParserConfin", "=" + e);
        }
        return xmlString;


    }


    public String GenerateAuthXMLForIris(String uid, String name,
                                         String timeout, boolean bio, boolean pi, String isotemplate,
                                         String imei) {

        byte[] syncSessionKey = null;
        byte[] encryptedSessionKey = null;
        SessionKeyDetails sessionKeyDetails = null;

        try {

            synchronizedKey = new SynchronizedKey(
                    encrypter.generateSessionKey(), UUID.randomUUID()
                    .toString(), new Date());
            syncSessionKey = synchronizedKey.getSeedSkey();
            encryptedSessionKey = encrypter
                    .encryptUsingPublicKey(syncSessionKey);
            sessionKeyDetails = SessionKeyDetails
                    .createSkeyToInitializeSynchronizedKey(
                            synchronizedKey.getKeyIdentifier(),
                            encryptedSessionKey);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        byte[] xmlPidBytes = getPidXmlIris(name, bio, pi, isotemplate)
                .getBytes();
        byte[] hmac = hashgenerator.generateSha256Hash(xmlPidBytes);
        String encryptedHmac = null;
        String encdata = null;

        try {
            encdata = Base64.encodeToString(encrypter.encryptUsingSessionKey(
                    syncSessionKey, xmlPidBytes), Base64.DEFAULT);
            encryptedHmac = Base64.encodeToString(
                    encrypter.encryptUsingSessionKey(syncSessionKey, hmac),
                    Base64.DEFAULT);
        } catch (Exception ex) {
            System.out.println("Error:" + ex.getMessage());
        }
        String output = null;
        try {

            DocumentBuilderFactory docFactory = DocumentBuilderFactory
                    .newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // root elements
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("Auth");
            doc.appendChild(rootElement);

            // Adding additional attributes for India Post
            Attr attr = null;

            addXmlElementAttribute("uid", uid, doc, rootElement);
            addXmlElementAttribute("mac", imei, doc, rootElement);
            addXmlElementAttribute("tid", "public", doc, rootElement);
            addXmlElementAttribute("sa", "", doc, rootElement);
            // addXmlElementAttribute("ver", "2.7", doc, rootElement);
            addXmlElementAttribute("txn", "uidai", doc, rootElement);
            addXmlElementAttribute(
                    "xmlns",
                    "http://www.uidai.gov.in/authentication/uid-auth-request/1.0",
                    doc, rootElement);
            addXmlElementAttribute(
                    "xmlns",
                    "http://www.uidai.gov.in/authentication/uid-bfd-response/1.0",
                    doc, rootElement);

            Element uses = doc.createElement("Uses");
            rootElement.appendChild(uses);

            addXmlElementAttribute("otp", "n", doc, uses);
            addXmlElementAttribute("pin", "n", doc, uses);
            addXmlElementAttribute("pfa", "n", doc, uses);
            addXmlElementAttribute("pa", "n", doc, uses);

            if (pi) {
                addXmlElementAttribute("pi", "y", doc, uses);
            } else {
                addXmlElementAttribute("pi", "n", doc, uses);
            }

            if (bio) {
                addXmlElementAttribute("bio", "y", doc, uses);
                addXmlElementAttribute("bt", "IIR", doc, uses);
            } else {
                addXmlElementAttribute("bio", "n", doc, uses);
            }

            Element meta = doc.createElement("Meta");
            rootElement.appendChild(meta);

            addXmlElementAttribute("udc", imei, doc, meta);
            addXmlElementAttribute("fdc", "NA", doc, meta);
            addXmlElementAttribute("idc", "NC", doc, meta);
            addXmlElementAttribute("pip", "127.0.0.1", doc, meta);

            addXmlElementAttribute("lot", "P", doc, meta);
            addXmlElementAttribute("lov", "834004", doc, meta);

            Element skey = doc.createElement("Skey");
            rootElement.appendChild(skey);
            addXmlElementAttribute("ci", encrypter.getCertificateIdentifier(),
                    doc, skey);
            addXmlElementAttribute("ki", sessionKeyDetails.getKeyIdentifier(),
                    doc, skey);
            skey.appendChild(doc.createTextNode(Base64.encodeToString(
                    sessionKeyDetails.getSkeyValue(), Base64.DEFAULT)));

            Element data = doc.createElement("Data");
            attr = doc.createAttribute("type");
            attr.setValue("X");
            data.setAttributeNode(attr);
            data.appendChild(doc.createTextNode(encdata));
            rootElement.appendChild(data);

            Element mac = doc.createElement("Hmac");
            mac.appendChild(doc.createTextNode(encryptedHmac));
            rootElement.appendChild(mac);

            TransformerFactory transformerFactory = TransformerFactory
                    .newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);

            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);
            transformer.transform(source, result);
            output = writer.getBuffer().toString()
                    .replaceAll("\n|\r", "");


        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return output;
    }

    public String GenerateRegistrationXMLForFingerPrint(String name,
                                                        String timeout, boolean bio, boolean pi, String isotemplate,
                                                        String imei, String email) {

        byte[] syncSessionKey = null;
        byte[] encryptedSessionKey = null;
        SessionKeyDetails sessionKeyDetails = null;

        Date date = new Date();
        try {
            synchronizedKey = new SynchronizedKey(
                    encrypter.generateSessionKey(), UUID.randomUUID()
                    .toString(), date);
            syncSessionKey = synchronizedKey.getSeedSkey();
            encryptedSessionKey = encrypter
                    .encryptUsingPublicKey(syncSessionKey);
            sessionKeyDetails = SessionKeyDetails
                    .createSkeyToInitializeSynchronizedKey(
                            synchronizedKey.getKeyIdentifier(),
                            encryptedSessionKey);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        byte[] xmlPidBytes = getPidXml(name, bio, pi, isotemplate, false, "").getBytes();
        byte[] hmac = hashgenerator.generateSha256Hash(xmlPidBytes);
        String encryptedHmac = null;
        String encdata = null;

        try {
            encdata = Base64.encodeToString(encrypter.encryptUsingSessionKey(
                    syncSessionKey, xmlPidBytes), Base64.DEFAULT);
            encryptedHmac = Base64.encodeToString(
                    encrypter.encryptUsingSessionKey(syncSessionKey, hmac),
                    Base64.DEFAULT);
        } catch (Exception ex) {
            System.out.println("Error:" + ex.getMessage());
        }

        try {

            DocumentBuilderFactory docFactory = DocumentBuilderFactory
                    .newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // root elements
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("xml");
            doc.appendChild(rootElement);

            Element seqnoNode = doc.createElement("seq_no");
            rootElement.appendChild(seqnoNode);
            seqnoNode.appendChild(doc.createTextNode(String
                    .valueOf(Global.sequenceNumber)));

            Element activeCode = doc.createElement("active_code");
            rootElement.appendChild(activeCode);
            activeCode.appendChild(doc.createTextNode(Global.activeCode));

            Element sessionNode = doc.createElement("session_token");
            rootElement.appendChild(sessionNode);
            sessionNode.appendChild(doc
                    .createTextNode(Global.sessionToken));

            Element deviceTypeNode = doc.createElement("device_type");
            rootElement.appendChild(deviceTypeNode);
            deviceTypeNode.appendChild(doc.createTextNode("A"));

            Element nameNode = doc.createElement("name");
            rootElement.appendChild(nameNode);
            nameNode.appendChild(doc.createTextNode(name));

            Element emailNode = doc.createElement("email");
            rootElement.appendChild(emailNode);
            emailNode.appendChild(doc.createTextNode(email));

            Element cpuNode = doc.createElement("cpu_id");
            rootElement.appendChild(cpuNode);
            cpuNode.appendChild(doc.createTextNode("NONE"));

            Element deviceNode = doc.createElement("device_mac");
            deviceNode.appendChild(doc.createTextNode(imei));
            rootElement.appendChild(deviceNode);

            Element biometricIdNode = doc.createElement("bio_id");
            biometricIdNode.appendChild(doc
                    .createTextNode(Global.deviceType + "$" + Global.serialNumber));
            //.createTextNode(com.aadhaar.life.Global.deviceType + "$" +com.aadhaar.life.Global.biometricDeviceID));
            rootElement.appendChild(biometricIdNode);

            Element biometricTypeNode = doc.createElement("bio_type");
            biometricTypeNode.appendChild(doc.createTextNode("F"));
            rootElement.appendChild(biometricTypeNode);

            Element clientVersionNode = doc.createElement("client_version");
            clientVersionNode.appendChild(doc.createTextNode(String
                    .valueOf(Global.version)));
            rootElement.appendChild(clientVersionNode);

            Element skeyNode = doc.createElement("skey");
            rootElement.appendChild(skeyNode);
            skeyNode.appendChild(doc.createTextNode(Base64.encodeToString(
                    sessionKeyDetails.getSkeyValue(), Base64.DEFAULT)));

            Element dataNode = doc.createElement("pid_data");
            dataNode.appendChild(doc.createTextNode(encdata));
            rootElement.appendChild(dataNode);

            Element hmacNode = doc.createElement("hmac");
            hmacNode.appendChild(doc.createTextNode(encryptedHmac));
            rootElement.appendChild(hmacNode);

            Element ciNode = doc.createElement("ci");
            rootElement.appendChild(ciNode);
            ciNode.appendChild(doc.createTextNode(encrypter
                    .getCertificateIdentifier()));

            Element btNode = doc.createElement("bt");
            rootElement.appendChild(btNode);
            btNode.appendChild(doc.createTextNode("FMR"));

            Element fdcNode = doc.createElement("fdc");
            rootElement.appendChild(fdcNode);
            fdcNode.appendChild(doc.createTextNode("NC"));

            Element idcNode = doc.createElement("idc");
            rootElement.appendChild(idcNode);
            idcNode.appendChild(doc.createTextNode("NA"));

            TransformerFactory transformerFactory = TransformerFactory
                    .newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);

            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);
            transformer.transform(source, result);
            String output = writer.getBuffer().toString()
                    .replaceAll("\n|\r", "");
            // System.out.println("###############################"+output);
            return output;

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public String GenerateRegistrationXMLForIris(String name, String timeout,
                                                 boolean bio, boolean pi, String isotemplate, String imei,
                                                 String email) {

        byte[] syncSessionKey = null;
        byte[] encryptedSessionKey = null;
        SessionKeyDetails sessionKeyDetails = null;

        Date date = new Date();

        try {
            synchronizedKey = new SynchronizedKey(
                    encrypter.generateSessionKey(), UUID.randomUUID()
                    .toString(), date);
            syncSessionKey = synchronizedKey.getSeedSkey();
            encryptedSessionKey = encrypter
                    .encryptUsingPublicKey(syncSessionKey);
            sessionKeyDetails = SessionKeyDetails
                    .createSkeyToInitializeSynchronizedKey(
                            synchronizedKey.getKeyIdentifier(),
                            encryptedSessionKey);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        byte[] xmlPidBytes = getPidXmlIris(name, bio, pi, isotemplate)
                .getBytes();
        byte[] hmac = hashgenerator.generateSha256Hash(xmlPidBytes);
        String encryptedHmac = null;
        String encdata = null;

        try {
            encdata = Base64.encodeToString(encrypter.encryptUsingSessionKey(
                    syncSessionKey, xmlPidBytes), Base64.DEFAULT);
            encryptedHmac = Base64.encodeToString(
                    encrypter.encryptUsingSessionKey(syncSessionKey, hmac),
                    Base64.DEFAULT);
        } catch (Exception ex) {
            System.out.println("Error:" + ex.getMessage());
        }

        try {

            DocumentBuilderFactory docFactory = DocumentBuilderFactory
                    .newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // root elements
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("xml");
            doc.appendChild(rootElement);

            Element seqnoNode = doc.createElement("seq_no");
            rootElement.appendChild(seqnoNode);
            seqnoNode.appendChild(doc.createTextNode(String
                    .valueOf(Global.sequenceNumber)));

            Element activeCode = doc.createElement("active_code");
            rootElement.appendChild(activeCode);
            activeCode.appendChild(doc.createTextNode(Global.activeCode));

            Element sessionNode = doc.createElement("session_token");
            rootElement.appendChild(sessionNode);
            sessionNode.appendChild(doc
                    .createTextNode(Global.sessionToken));

            Element deviceTypeNode = doc.createElement("device_type");
            rootElement.appendChild(deviceTypeNode);
            deviceTypeNode.appendChild(doc.createTextNode("A"));

            Element nameNode = doc.createElement("name");
            rootElement.appendChild(nameNode);
            nameNode.appendChild(doc.createTextNode(name));

            Element emailNode = doc.createElement("email");
            rootElement.appendChild(emailNode);
            emailNode.appendChild(doc.createTextNode(email));

            Element cpuNode = doc.createElement("cpu_id");
            rootElement.appendChild(cpuNode);
            cpuNode.appendChild(doc.createTextNode("NONE"));

            Element deviceNode = doc.createElement("device_mac");
            deviceNode.appendChild(doc.createTextNode(imei));
            rootElement.appendChild(deviceNode);

            Element biometricIdNode = doc.createElement("bio_id");
            biometricIdNode.appendChild(doc
                    .createTextNode(Global.deviceType + "$" + Global.serialNumber));
            //.createTextNode(com.aadhaar.life.Global.deviceType + "$" +com.aadhaar.life.Global.biometricDeviceID));
            rootElement.appendChild(biometricIdNode);

            Element biometricTypeNode = doc.createElement("bio_type");
            biometricTypeNode.appendChild(doc.createTextNode("I"));
            rootElement.appendChild(biometricTypeNode);

            Element clientVersionNode = doc.createElement("client_version");
            clientVersionNode.appendChild(doc.createTextNode(String
                    .valueOf(Global.version)));
            rootElement.appendChild(clientVersionNode);

            Element skeyNode = doc.createElement("skey");
            rootElement.appendChild(skeyNode);
            skeyNode.appendChild(doc.createTextNode(Base64.encodeToString(
                    sessionKeyDetails.getSkeyValue(), Base64.DEFAULT)));

            Element dataNode = doc.createElement("pid_data");
            dataNode.appendChild(doc.createTextNode(encdata));
            rootElement.appendChild(dataNode);

            Element hmacNode = doc.createElement("hmac");
            hmacNode.appendChild(doc.createTextNode(encryptedHmac));
            rootElement.appendChild(hmacNode);

            Element ciNode = doc.createElement("ci");
            rootElement.appendChild(ciNode);
            ciNode.appendChild(doc.createTextNode(encrypter
                    .getCertificateIdentifier()));

            Element btNode = doc.createElement("bt");
            rootElement.appendChild(btNode);
            btNode.appendChild(doc.createTextNode("IIR"));

            Element fdcNode = doc.createElement("fdc");
            rootElement.appendChild(fdcNode);
            fdcNode.appendChild(doc.createTextNode("NA"));

            Element idcNode = doc.createElement("idc");
            rootElement.appendChild(idcNode);
            idcNode.appendChild(doc.createTextNode("NC"));

            TransformerFactory transformerFactory = TransformerFactory
                    .newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);

            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);
            transformer.transform(source, result);
            String output = writer.getBuffer().toString()
                    .replaceAll("\n|\r", "");

            return output;

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public String createAliveXMLForIris(String name,
                                        String timeout, boolean bio, boolean pi, String isotemplate,
                                        String ppo, String email, String bankCode, String bankAccount, boolean reMarriage, boolean reEmployed) {

        byte[] syncSessionKey = null;
        byte[] encryptedSessionKey = null;
        SessionKeyDetails sessionKeyDetails = null;

        try {
            synchronizedKey = new SynchronizedKey(
                    encrypter.generateSessionKey(), UUID.randomUUID()
                    .toString(), new Date());
            syncSessionKey = synchronizedKey.getSeedSkey();
            encryptedSessionKey = encrypter
                    .encryptUsingPublicKey(syncSessionKey);
            sessionKeyDetails = SessionKeyDetails
                    .createSkeyToInitializeSynchronizedKey(
                            synchronizedKey.getKeyIdentifier(),
                            encryptedSessionKey);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        byte[] xmlPidBytes = getPidXmlIris(name, bio, pi, isotemplate)
                .getBytes();
        byte[] hmac = hashgenerator.generateSha256Hash(xmlPidBytes);
        String encryptedHmac = null;
        String encdata = null;

        try {
            encdata = Base64.encodeToString(encrypter.encryptUsingSessionKey(
                    syncSessionKey, xmlPidBytes), Base64.DEFAULT);
            encryptedHmac = Base64.encodeToString(
                    encrypter.encryptUsingSessionKey(syncSessionKey, hmac),
                    Base64.DEFAULT);
        } catch (Exception ex) {
            System.out.println("Error:" + ex.getMessage());
        }

        try {

            String marriage = reMarriage == true ? "Y" : "N";
            String employed = reEmployed == true ? "Y" : "N";

            DocumentBuilderFactory docFactory = DocumentBuilderFactory
                    .newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // root elements
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("xml");
            doc.appendChild(rootElement);

            Element deviceIDNode = doc.createElement("device_id");
            rootElement.appendChild(deviceIDNode);
            deviceIDNode.appendChild(doc.createTextNode(Global.deviceid));

            Element hashNode = doc.createElement("hash_key");
            rootElement.appendChild(hashNode);
            hashNode.appendChild(doc.createTextNode(Global.hash));

            Element seqnoNode = doc.createElement("seq_no");
            rootElement.appendChild(seqnoNode);
            seqnoNode.appendChild(doc.createTextNode(String
                    .valueOf(Global.sequenceNumber)));

            Element activeCode = doc.createElement("active_code");
            rootElement.appendChild(activeCode);
            activeCode.appendChild(doc.createTextNode(Global.activeCode));

            Element sessionNode = doc.createElement("session_token");
            rootElement.appendChild(sessionNode);
            sessionNode.appendChild(doc
                    .createTextNode(Global.sessionToken));

            Element nameNode = doc.createElement("resident_name");
            rootElement.appendChild(nameNode);
            nameNode.appendChild(doc.createTextNode(name));

            Element emailNode = doc.createElement("email");
            rootElement.appendChild(emailNode);
            emailNode.appendChild(doc.createTextNode(email));

            Element ppoNode = doc.createElement("ppo_number");
            rootElement.appendChild(ppoNode);
            ppoNode.appendChild(doc.createTextNode(ppo));

            Element bankNode = doc.createElement("bank_code");
            rootElement.appendChild(bankNode);
            bankNode.appendChild(doc.createTextNode(bankCode));

            Element accountNode = doc.createElement("bank_account");
            rootElement.appendChild(accountNode);
            accountNode.appendChild(doc.createTextNode(bankAccount));


            Element marriageNode = doc.createElement("re_marriage");
            rootElement.appendChild(marriageNode);
            marriageNode.appendChild(doc.createTextNode(marriage));

            Element employedeNode = doc.createElement("re_employed");
            rootElement.appendChild(employedeNode);
            employedeNode.appendChild(doc.createTextNode(employed));

            Element skeyNode = doc.createElement("skey");
            rootElement.appendChild(skeyNode);
            skeyNode.appendChild(doc.createTextNode(Base64.encodeToString(
                    sessionKeyDetails.getSkeyValue(), Base64.DEFAULT)));

            Element dataNode = doc.createElement("pid_data");
            dataNode.appendChild(doc.createTextNode(encdata));
            rootElement.appendChild(dataNode);

            Element hmacNode = doc.createElement("hmac");
            hmacNode.appendChild(doc.createTextNode(encryptedHmac));
            rootElement.appendChild(hmacNode);

            Element ciNode = doc.createElement("ci");
            rootElement.appendChild(ciNode);
            ciNode.appendChild(doc.createTextNode(encrypter
                    .getCertificateIdentifier()));

            Element btNode = doc.createElement("bt");
            rootElement.appendChild(btNode);
            btNode.appendChild(doc.createTextNode("IIR"));

            Element fdcNode = doc.createElement("fdc");
            rootElement.appendChild(fdcNode);
            fdcNode.appendChild(doc.createTextNode("NA"));

            Element idcNode = doc.createElement("idc");
            rootElement.appendChild(idcNode);
            idcNode.appendChild(doc.createTextNode("NC"));

            TransformerFactory transformerFactory = TransformerFactory
                    .newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);

            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);
            transformer.transform(source, result);
            String output = writer.getBuffer().toString()
                    .replaceAll("\n|\r", "");

            return output;

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public String createAliveXMLForFP(String name, String timeout,
                                      boolean bio, boolean pi, String isotemplate, String ppo,
                                      String email, String bankCode, String bankAccount, boolean reMarriage, boolean reEmployed) {

        byte[] syncSessionKey = null;
        byte[] encryptedSessionKey = null;
        SessionKeyDetails sessionKeyDetails = null;

        try {
            synchronizedKey = new SynchronizedKey(
                    encrypter.generateSessionKey(), UUID.randomUUID()
                    .toString(), null);
            syncSessionKey = synchronizedKey.getSeedSkey();
            encryptedSessionKey = encrypter
                    .encryptUsingPublicKey(syncSessionKey);
            sessionKeyDetails = SessionKeyDetails
                    .createSkeyToInitializeSynchronizedKey(
                            synchronizedKey.getKeyIdentifier(),
                            encryptedSessionKey);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        byte[] xmlPidBytes = getPidXml(name, bio, pi, isotemplate, false, "").getBytes();
        byte[] hmac = hashgenerator.generateSha256Hash(xmlPidBytes);
        String encryptedHmac = null;
        String encdata = null;

        try {
            encdata = Base64.encodeToString(encrypter.encryptUsingSessionKey(
                    syncSessionKey, xmlPidBytes), Base64.DEFAULT);
            encryptedHmac = Base64.encodeToString(
                    encrypter.encryptUsingSessionKey(syncSessionKey, hmac),
                    Base64.DEFAULT);
        } catch (Exception ex) {
            System.out.println("Error:" + ex.getMessage());
        }

        try {
            String marriage = reMarriage == true ? "Y" : "N";
            String employed = reEmployed == true ? "Y" : "N";

            DocumentBuilderFactory docFactory = DocumentBuilderFactory
                    .newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // root elements
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("xml");
            doc.appendChild(rootElement);

            Element deviceIDNode = doc.createElement("device_id");
            rootElement.appendChild(deviceIDNode);
            deviceIDNode.appendChild(doc.createTextNode(Global.deviceid));

            Element hashNode = doc.createElement("hash_key");
            rootElement.appendChild(hashNode);
            hashNode.appendChild(doc.createTextNode(Global.hash));

            Element seqnoNode = doc.createElement("seq_no");
            rootElement.appendChild(seqnoNode);
            seqnoNode.appendChild(doc.createTextNode(String
                    .valueOf(Global.sequenceNumber)));

            Element activeCode = doc.createElement("active_code");
            rootElement.appendChild(activeCode);
            activeCode.appendChild(doc.createTextNode(Global.activeCode));

            Element sessionNode = doc.createElement("session_token");
            rootElement.appendChild(sessionNode);
            sessionNode.appendChild(doc
                    .createTextNode(Global.sessionToken));

            Element nameNode = doc.createElement("resident_name");
            rootElement.appendChild(nameNode);
            nameNode.appendChild(doc.createTextNode(name));

            Element emailNode = doc.createElement("email");
            rootElement.appendChild(emailNode);
            emailNode.appendChild(doc.createTextNode(email));

            Element ppoNode = doc.createElement("ppo_number");
            rootElement.appendChild(ppoNode);
            ppoNode.appendChild(doc.createTextNode(ppo));

            Element bankNode = doc.createElement("bank_code");
            rootElement.appendChild(bankNode);
            bankNode.appendChild(doc.createTextNode(bankCode));

            Element accountNode = doc.createElement("bank_account");
            rootElement.appendChild(accountNode);
            accountNode.appendChild(doc.createTextNode(bankAccount));

            Element marriageNode = doc.createElement("re_marriage");
            rootElement.appendChild(marriageNode);
            marriageNode.appendChild(doc.createTextNode(marriage));

            Element employedeNode = doc.createElement("re_employed");
            rootElement.appendChild(employedeNode);
            employedeNode.appendChild(doc.createTextNode(employed));


            Element skeyNode = doc.createElement("skey");
            rootElement.appendChild(skeyNode);
            skeyNode.appendChild(doc.createTextNode(Base64.encodeToString(
                    sessionKeyDetails.getSkeyValue(), Base64.DEFAULT)));

            Element dataNode = doc.createElement("pid_data");
            dataNode.appendChild(doc.createTextNode(encdata));
            rootElement.appendChild(dataNode);

            Element hmacNode = doc.createElement("hmac");
            hmacNode.appendChild(doc.createTextNode(encryptedHmac));
            rootElement.appendChild(hmacNode);

            Element ciNode = doc.createElement("ci");
            rootElement.appendChild(ciNode);
            ciNode.appendChild(doc.createTextNode(encrypter
                    .getCertificateIdentifier()));

            Element btNode = doc.createElement("bt");
            rootElement.appendChild(btNode);
            btNode.appendChild(doc.createTextNode("FMR"));

            Element fdcNode = doc.createElement("fdc");
            rootElement.appendChild(fdcNode);
            fdcNode.appendChild(doc.createTextNode("NC"));

            Element idcNode = doc.createElement("idc");
            rootElement.appendChild(idcNode);
            idcNode.appendChild(doc.createTextNode("NA"));

            DOMSource domSource = new DOMSource(doc);
            StringWriter writer_ = new StringWriter();
            StreamResult result_ = new StreamResult(writer_);
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer_ = tf.newTransformer();
            transformer_.transform(domSource, result_);

            String o = writer_.toString();
            System.out.println("AUth XML DONE");
            return o;

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public String createAliveXMLForIris_EKYC(String name,
                                             String timeout, boolean bio, boolean pi, String isotemplate,
                                             String ppo, String email, String bankCode, String bankAccount, boolean reMarriage, boolean reEmployed, String pensionCode, String categoryCode, String disbursingCode) {

        byte[] syncSessionKey = null;
        byte[] encryptedSessionKey = null;
        SessionKeyDetails sessionKeyDetails = null;

        try {
            synchronizedKey = new SynchronizedKey(
                    encrypter.generateSessionKey(), UUID.randomUUID()
                    .toString(), new Date());
            syncSessionKey = synchronizedKey.getSeedSkey();
            encryptedSessionKey = encrypter
                    .encryptUsingPublicKey(syncSessionKey);
            sessionKeyDetails = SessionKeyDetails
                    .createSkeyToInitializeSynchronizedKey(
                            synchronizedKey.getKeyIdentifier(),
                            encryptedSessionKey);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        byte[] xmlPidBytes = getPidXmlIris(name, bio, pi, isotemplate)
                .getBytes();
        byte[] hmac = hashgenerator.generateSha256Hash(xmlPidBytes);
        String encryptedHmac = null;
        String encdata = null;

        try {
            encdata = Base64.encodeToString(encrypter.encryptUsingSessionKey(
                    syncSessionKey, xmlPidBytes), Base64.DEFAULT);
            encryptedHmac = Base64.encodeToString(
                    encrypter.encryptUsingSessionKey(syncSessionKey, hmac),
                    Base64.DEFAULT);
        } catch (Exception ex) {
            System.out.println("Error:" + ex.getMessage());
        }

        try {

            String marriage = reMarriage == true ? "Y" : "N";
            String employed = reEmployed == true ? "Y" : "N";

            DocumentBuilderFactory docFactory = DocumentBuilderFactory
                    .newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // root elements
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("xml");
            doc.appendChild(rootElement);

            Element deviceIDNode = doc.createElement("device_id");
            rootElement.appendChild(deviceIDNode);
            deviceIDNode.appendChild(doc.createTextNode(Global.deviceid));

            Element hashNode = doc.createElement("hash_key");
            rootElement.appendChild(hashNode);
            hashNode.appendChild(doc.createTextNode(Global.hash));

            Element seqnoNode = doc.createElement("seq_no");
            rootElement.appendChild(seqnoNode);
            seqnoNode.appendChild(doc.createTextNode(String
                    .valueOf(Global.sequenceNumber)));

            Element activeCode = doc.createElement("active_code");
            rootElement.appendChild(activeCode);
            activeCode.appendChild(doc.createTextNode(Global.activeCode));

            Element sessionNode = doc.createElement("session_token");
            rootElement.appendChild(sessionNode);
            sessionNode.appendChild(doc
                    .createTextNode(Global.sessionToken));

            Element nameNode = doc.createElement("resident_name");
            rootElement.appendChild(nameNode);
            nameNode.appendChild(doc.createTextNode(name));

            Element emailNode = doc.createElement("email");
            rootElement.appendChild(emailNode);
            emailNode.appendChild(doc.createTextNode(email));

            Element ppoNode = doc.createElement("ppo_number");
            rootElement.appendChild(ppoNode);
            ppoNode.appendChild(doc.createTextNode(ppo));

            Element bankNode = doc.createElement("bank_code");
            rootElement.appendChild(bankNode);
            bankNode.appendChild(doc.createTextNode(bankCode));


            Element pensionNode = doc.createElement("pension_code");
            rootElement.appendChild(pensionNode);
            pensionNode.appendChild(doc.createTextNode(pensionCode));


            Element categoryNode = doc.createElement("category_code");
            rootElement.appendChild(categoryNode);
            categoryNode.appendChild(doc.createTextNode(categoryCode));


            Element disbursingNode = doc.createElement("disbursing_code");
            rootElement.appendChild(disbursingNode);
            disbursingNode.appendChild(doc.createTextNode(disbursingCode));

            Element accountNode = doc.createElement("bank_account");
            rootElement.appendChild(accountNode);
            accountNode.appendChild(doc.createTextNode(bankAccount));

            Element marriageNode = doc.createElement("re_marriage");
            rootElement.appendChild(marriageNode);
            marriageNode.appendChild(doc.createTextNode(marriage));

            Element employedeNode = doc.createElement("re_employed");
            rootElement.appendChild(employedeNode);
            employedeNode.appendChild(doc.createTextNode(employed));

            Element skeyNode = doc.createElement("skey");
            rootElement.appendChild(skeyNode);
            skeyNode.appendChild(doc.createTextNode(Base64.encodeToString(
                    sessionKeyDetails.getSkeyValue(), Base64.DEFAULT)));

            Element dataNode = doc.createElement("pid_data");
            dataNode.appendChild(doc.createTextNode(encdata));
            rootElement.appendChild(dataNode);

            Element hmacNode = doc.createElement("hmac");
            hmacNode.appendChild(doc.createTextNode(encryptedHmac));
            rootElement.appendChild(hmacNode);

            Element ciNode = doc.createElement("ci");
            rootElement.appendChild(ciNode);
            ciNode.appendChild(doc.createTextNode(encrypter
                    .getCertificateIdentifier()));

            Element pidTSNode = doc.createElement("pid_ts");
            rootElement.appendChild(pidTSNode);
            pidTSNode.appendChild(doc.createTextNode(pidTimeStamp));

            Element btNode = doc.createElement("bt");
            rootElement.appendChild(btNode);
            btNode.appendChild(doc.createTextNode("IIR"));

            Element fdcNode = doc.createElement("fdc");
            rootElement.appendChild(fdcNode);
            fdcNode.appendChild(doc.createTextNode("NA"));

            Element idcNode = doc.createElement("idc");
            rootElement.appendChild(idcNode);
            idcNode.appendChild(doc.createTextNode("NC"));

            TransformerFactory transformerFactory = TransformerFactory
                    .newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);

            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);
            transformer.transform(source, result);
            String output = writer.getBuffer().toString()
                    .replaceAll("\n|\r", "");

            return output;

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public String createAliveXMLForFP_EKYC(String name, String timeout,
                                           boolean bio, boolean pi, String isotemplate, String ppo,
                                           String email, String bankCode, String bankAccount, boolean reMarriage, boolean reEmployed, String pensionCode, String categoryCode, String disbursingCode) {

        byte[] syncSessionKey = null;
        byte[] encryptedSessionKey = null;
        SessionKeyDetails sessionKeyDetails = null;

        try {
            synchronizedKey = new SynchronizedKey(
                    encrypter.generateSessionKey(), UUID.randomUUID()
                    .toString(), null);
            syncSessionKey = synchronizedKey.getSeedSkey();
            encryptedSessionKey = encrypter
                    .encryptUsingPublicKey(syncSessionKey);
            sessionKeyDetails = SessionKeyDetails
                    .createSkeyToInitializeSynchronizedKey(
                            synchronizedKey.getKeyIdentifier(),
                            encryptedSessionKey);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        byte[] xmlPidBytes = getPidXml(name, bio, pi, isotemplate, false, "").getBytes();
        byte[] hmac = hashgenerator.generateSha256Hash(xmlPidBytes);
        String encryptedHmac = null;
        String encdata = null;

        try {
            encdata = Base64.encodeToString(encrypter.encryptUsingSessionKey(
                    syncSessionKey, xmlPidBytes), Base64.DEFAULT);
            encryptedHmac = Base64.encodeToString(
                    encrypter.encryptUsingSessionKey(syncSessionKey, hmac),
                    Base64.DEFAULT);
        } catch (Exception ex) {
            System.out.println("Error:" + ex.getMessage());
        }

        try {

            String marriage = reMarriage == true ? "Y" : "N";
            String employed = reEmployed == true ? "Y" : "N";

            DocumentBuilderFactory docFactory = DocumentBuilderFactory
                    .newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // root elements
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("xml");
            doc.appendChild(rootElement);

            Element deviceIDNode = doc.createElement("device_id");
            rootElement.appendChild(deviceIDNode);
            deviceIDNode.appendChild(doc.createTextNode(Global.deviceid));

            Element hashNode = doc.createElement("hash_key");
            rootElement.appendChild(hashNode);
            hashNode.appendChild(doc.createTextNode(Global.hash));

            Element seqnoNode = doc.createElement("seq_no");
            rootElement.appendChild(seqnoNode);
            seqnoNode.appendChild(doc.createTextNode(String
                    .valueOf(Global.sequenceNumber)));

            Element activeCode = doc.createElement("active_code");
            rootElement.appendChild(activeCode);
            activeCode.appendChild(doc.createTextNode(Global.activeCode));

            Element sessionNode = doc.createElement("session_token");
            rootElement.appendChild(sessionNode);
            sessionNode.appendChild(doc
                    .createTextNode(Global.sessionToken));

            Element nameNode = doc.createElement("resident_name");
            rootElement.appendChild(nameNode);
            nameNode.appendChild(doc.createTextNode(name));

            Element emailNode = doc.createElement("email");
            rootElement.appendChild(emailNode);
            emailNode.appendChild(doc.createTextNode(email));

            Element ppoNode = doc.createElement("ppo_number");
            rootElement.appendChild(ppoNode);
            ppoNode.appendChild(doc.createTextNode(ppo));

            Element bankNode = doc.createElement("bank_code");
            rootElement.appendChild(bankNode);
            bankNode.appendChild(doc.createTextNode(bankCode));


            Element pensionNode = doc.createElement("pension_code");
            rootElement.appendChild(pensionNode);
            pensionNode.appendChild(doc.createTextNode(pensionCode));


            Element categoryNode = doc.createElement("category_code");
            rootElement.appendChild(categoryNode);
            categoryNode.appendChild(doc.createTextNode(categoryCode));


            Element disbursingNode = doc.createElement("disbursing_code");
            rootElement.appendChild(disbursingNode);
            disbursingNode.appendChild(doc.createTextNode(disbursingCode));

            Element accountNode = doc.createElement("bank_account");
            rootElement.appendChild(accountNode);
            accountNode.appendChild(doc.createTextNode(bankAccount));

            Element marriageNode = doc.createElement("re_marriage");
            rootElement.appendChild(marriageNode);
            marriageNode.appendChild(doc.createTextNode(marriage));

            Element employedeNode = doc.createElement("re_employed");
            rootElement.appendChild(employedeNode);
            employedeNode.appendChild(doc.createTextNode(employed));

            Element skeyNode = doc.createElement("skey");
            rootElement.appendChild(skeyNode);
            skeyNode.appendChild(doc.createTextNode(Base64.encodeToString(
                    sessionKeyDetails.getSkeyValue(), Base64.DEFAULT)));

            Element dataNode = doc.createElement("pid_data");
            dataNode.appendChild(doc.createTextNode(encdata));
            rootElement.appendChild(dataNode);

            Element hmacNode = doc.createElement("hmac");
            hmacNode.appendChild(doc.createTextNode(encryptedHmac));
            rootElement.appendChild(hmacNode);

            Element ciNode = doc.createElement("ci");
            rootElement.appendChild(ciNode);
            ciNode.appendChild(doc.createTextNode(encrypter
                    .getCertificateIdentifier()));


            Element pidTSNode = doc.createElement("pid_ts");
            rootElement.appendChild(pidTSNode);
            pidTSNode.appendChild(doc.createTextNode(pidTimeStamp));

            Element btNode = doc.createElement("bt");
            rootElement.appendChild(btNode);
            btNode.appendChild(doc.createTextNode("FMR"));

            Element fdcNode = doc.createElement("fdc");
            rootElement.appendChild(fdcNode);
            fdcNode.appendChild(doc.createTextNode("NC"));

            Element idcNode = doc.createElement("idc");
            rootElement.appendChild(idcNode);
            idcNode.appendChild(doc.createTextNode("NA"));

            DOMSource domSource = new DOMSource(doc);
            StringWriter writer_ = new StringWriter();
            StreamResult result_ = new StreamResult(writer_);
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer_ = tf.newTransformer();
            transformer_.transform(domSource, result_);

            String o = writer_.toString();
            System.out.println("AUth XML DONE");
            return o;

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public String createCDACXMLForFP(String isotemplate, String uid) {

        byte[] syncSessionKey = null;
        byte[] encryptedSessionKey = null;
        SessionKeyDetails sessionKeyDetails = null;

        try {
            synchronizedKey = new SynchronizedKey(
                    encrypter.generateSessionKey(), UUID.randomUUID()
                    .toString(), null);
            syncSessionKey = synchronizedKey.getSeedSkey();
            encryptedSessionKey = encrypter
                    .encryptUsingPublicKey(syncSessionKey);
            sessionKeyDetails = SessionKeyDetails
                    .createSkeyToInitializeSynchronizedKey(
                            synchronizedKey.getKeyIdentifier(),
                            encryptedSessionKey);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        byte[] xmlPidBytes = getPidXml("", true, false, isotemplate, false, "").getBytes();
        byte[] hmac = hashgenerator.generateSha256Hash(xmlPidBytes);
        String encryptedHmac = null;
        String encdata = null;

        try {
            encdata = Base64.encodeToString(encrypter.encryptUsingSessionKey(
                    syncSessionKey, xmlPidBytes), Base64.DEFAULT);
            encryptedHmac = Base64.encodeToString(
                    encrypter.encryptUsingSessionKey(syncSessionKey, hmac),
                    Base64.DEFAULT);
        } catch (Exception ex) {
            System.out.println("Error:" + ex.getMessage());
        }

        try {

            //
            //			String reqXML="<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><KycRequest ver=\"1.0\" uid=\""+ uid+
            //					"\" ci=\""+encrypter.getCertificateIdentifier() +"\" key=\""+Base64.encodeToString(
            //							sessionKeyDetails.getSkeyValue(), Base64.DEFAULT) +"\" pidType=\"X\" pid=\""+ encdata+"\" pidTs=\""+ pidTimeStamp +"\" terminalId=\"public\" fdc=\"NC\" idc=\"NA\" lot=\"P\" lov=\"636103\" udc=\"NICTSIM00000001\" pip=\"127.0.0.1\" biometricType=\"FMR\" />";


            DocumentBuilderFactory docFactory = DocumentBuilderFactory
                    .newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // root elements
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("KycRequest");
            doc.appendChild(rootElement);

            // Adding additional attributes for India Post
            Attr attr = null;
            addXmlElementAttribute("ver", "1.0", doc, rootElement);
            addXmlElementAttribute("uid", uid, doc, rootElement);
            addXmlElementAttribute("ci", encrypter.getCertificateIdentifier(), doc, rootElement);
            addXmlElementAttribute("key", Base64.encodeToString(sessionKeyDetails.getSkeyValue(), Base64.DEFAULT), doc, rootElement);
            addXmlElementAttribute("pidType", "X", doc, rootElement);
            addXmlElementAttribute("pid", encdata, doc, rootElement);
            addXmlElementAttribute("hmac", encryptedHmac, doc, rootElement);
            addXmlElementAttribute("pidTs", pidTimeStamp, doc, rootElement);
            addXmlElementAttribute("terminalId", "public", doc, rootElement);
            addXmlElementAttribute("fdc", "NC", doc, rootElement);
            addXmlElementAttribute("idc", "NA", doc, rootElement);
            addXmlElementAttribute("lot", "P", doc, rootElement);
            addXmlElementAttribute("lov", "834001", doc, rootElement);
            addXmlElementAttribute("udc", Global.imei, doc, rootElement);
            addXmlElementAttribute("pip", "127.0.0.1", doc, rootElement);
            addXmlElementAttribute("biometricType", "FMR", doc, rootElement);


            TransformerFactory transformerFactory = TransformerFactory
                    .newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);

            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);
            transformer.transform(source, result);
            String output = writer.getBuffer().toString()
                    .replaceAll("\n|\r", "");

            return output;


        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public String createCustomXmlForAuth(String xml, String atype) {
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

        Element rootElement = document.createElement("Kyc");
        document.appendChild(rootElement);

        Element appvertype = document.createElement("APP_VER");
        appvertype.appendChild(document.createTextNode(Global.APP_VER));
        rootElement.appendChild(appvertype);

        Element opcode = document.createElement("OP_CODE");
        opcode.appendChild(document.createTextNode(Global.LOCATION_ADDRESS.replaceAll(",", "_") + "#" + Global.CURRENT_LOCATION_SET.replace(",", "_")));
        rootElement.appendChild(opcode);

        String encodedKYCXml = Base64.encodeToString(xml.getBytes(), Base64.NO_WRAP);

        Element skeyElement = document.createElement("Rad");
        skeyElement.appendChild(document.createTextNode(encodedKYCXml));
        rootElement.appendChild(skeyElement);

        Element uidElement = document.createElement("UID");
        uidElement.appendChild(document.createTextNode(Global.AUTH_AADHAAR));
        rootElement.appendChild(uidElement);

        Element atypeElement = document.createElement("AuthType");
        atypeElement.appendChild(document.createTextNode(atype));
        rootElement.appendChild(atypeElement);

        Element imeiElement = document.createElement("imei");
        imeiElement.appendChild(document.createTextNode(Global.DEVICE_IMEI_NO));
        rootElement.appendChild(imeiElement);

        Element ntypeElement = document.createElement("NETTYPE");
        ntypeElement.appendChild(document.createTextNode(Global.NETWORK_TYPE));
        rootElement.appendChild(ntypeElement);

        Element nnameElement = document.createElement("NETNAME");
        if (Global.connectionType.equalsIgnoreCase("Wifi"))
            nnameElement.appendChild(document.createTextNode(Global.NETWORK_NAME));
        else
            nnameElement.appendChild(document.createTextNode(Global.OPERATOR_NAME + "#" + Global.OPERATOR_NAME_SET));
        rootElement.appendChild(nnameElement);

        //Cell information

        Element latElement = document.createElement("LAT");
        latElement.appendChild(document.createTextNode(String.valueOf(Global.latitude)));
        rootElement.appendChild(latElement);

        Element longElement = document.createElement("LONGIT");
        longElement.appendChild(document.createTextNode(String.valueOf(Global.longitude)));
        rootElement.appendChild(longElement);

        Element sigstrenElement = document.createElement("SIGSTRENGTH");
        sigstrenElement.appendChild(document.createTextNode(String.valueOf(Global.SIGNAL_STRENGTH)));
        rootElement.appendChild(sigstrenElement);

        Element cidElement = document.createElement("CID");
        cidElement.appendChild(document.createTextNode(String.valueOf(Global.cid)));
        rootElement.appendChild(cidElement);

        Element lacElement = document.createElement("LAC");
        lacElement.appendChild(document.createTextNode(String.valueOf(Global.lac)));
        rootElement.appendChild(lacElement);

        Element mncElement = document.createElement("MNC");
        mncElement.appendChild(document.createTextNode(Global.mnc));
        rootElement.appendChild(mncElement);

        Element mccElement = document.createElement("MCC");
        mccElement.appendChild(document.createTextNode(Global.mcc));
        rootElement.appendChild(mccElement);

        Element tinElement = document.createElement("TIN");
        tinElement.appendChild(document.createTextNode(Global.TIN_NO));
        rootElement.appendChild(tinElement);


        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = null;
        try {
            transformer = factory.newTransformer();
        } catch (TransformerConfigurationException e1) {
            e1.printStackTrace();
        }
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
        Log.e("Bapuji auth ", "==" + KycAuthXml);

        return KycAuthXml;

    }

    private byte[] getPIDXMLSamsung(String name) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        //		sdf.setTimeZone(TimeZone.getTimeZone("UTC+5.30"));
        //		sdf.setTimeZone(TimeZone.getTimeZone("UTC+5.30"));
        String ts = sdf.format(new Date());
        samsungTS = ts;
        Global.R_PID_TIME = ts;
        Global.TIME_STAMP_FOR_SAVE_DATA = ts;
        //		Global.PID_FOR_SAMSUNG = ts;
        Log.e("Global.PID_TIME_STAMP", "in samsung pid " + Global.PID_TIME_STAMP);

        String pid = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
                + "<ns2:Pid xmlns:ns2=\"http://www.uidai.gov.in/authentication/uid-auth-request/1.0\" " +
                "ts=\"" + ts + "\" >" +
                "<Bios>" +
                //"<Demo>" +"<Pi " + "name=\""+ name + "\"/>" + "</Demo>" + "<Bios>" +
                "</Bios>" + "</ns2:Pid>";

        byte[] piddata = pid.getBytes();
        return piddata;
    }

    public String createXmlForSamsungKycAuth(String deviceType, boolean otp_check, String aadhaarNo) {

        String xml = "";

        SecIrisManager secIrisManager = SecIrisManager.getInstance();
        //		for single eye
        byte[] encXMLPIDData = secIrisManager.getEncryptedPid
                (getPIDXMLSamsung(""), SecIrisConstants.PIDTYPE_XML,
                        SecIrisConstants.BIOTYPE_UNKNOWN_IRIS,
                        new X509Certificate[]{encrypter.getCertificateChain()});

		/*// for double eye
        byte[] encXMLPIDData = secIrisManager.getEncryptedPid
				(getPIDXMLSamsung(name), SecIrisConstants.PIDTYPE_XML,
						SecIrisConstants.BIOTYPE_BOTH_IRIS,
						new X509Certificate[] {encrypter.getCertificateChain()});*/

        String pid = Base64.encodeToString(encXMLPIDData,
                Base64.DEFAULT);
        Log.d("SAMSUNG", "pid data : " + pid);

        String encryptedHmac = Base64.encodeToString(secIrisManager.getEncryptedHMAC(), Base64.DEFAULT);
        String sKey = Base64.encodeToString(secIrisManager.getEncryptedSessionKey(), Base64.DEFAULT);

        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = null;


        try {
            documentBuilder = documentFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        org.w3c.dom.Document document = documentBuilder.newDocument();


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
        Log.e("Global.PID_TIME", "--->" + Global.PID_TIME_STAMP);

        //  String txn = "NIC:" + aadhaarNo + pidTimeStamp.replace("T", "").replace("-", "").replace(":", "") + "NHPS";
        String txn = "UKC:" + aadhaarNo + pidTimeStamp.replace("T", "").replace("-", "").replace(":", "");
        Element mainElement = document.createElement("AuthData");
        document.appendChild(mainElement);
        Element rootElement = document.createElement("Auth");
        mainElement.appendChild(rootElement);
        Attr rootUid = document.createAttribute("uid");
        rootUid.setValue(aadhaarNo);
        Attr rootTid = document.createAttribute("tid");
        rootTid.setValue("public");
        Attr rootAc = document.createAttribute("ac");
        rootAc.setValue("00000000");
        Attr rootSa = document.createAttribute("sa");
        rootSa.setValue("");
        Attr rootLk = document.createAttribute("lk");
        rootLk.setValue("");
        Attr rootTxn = document.createAttribute("txn");
        rootTxn.setValue(txn);
        Attr rootVer = document.createAttribute("ver");
        rootVer.setValue("1.6");
        Attr rootTs = document.createAttribute("ts");
        rootTs.setValue(Global.R_PID_TIME);
        Attr rootXmlns = document.createAttribute("xmlns");
        rootXmlns.setValue("http://www.uidai.gov.in/authentication/uid-auth-request/1.0");
        rootElement.setAttributeNode(rootTid);
        rootElement.setAttributeNode(rootAc);
        rootElement.setAttributeNode(rootSa);
        rootElement.setAttributeNode(rootLk);
        rootElement.setAttributeNode(rootTxn);
        rootElement.setAttributeNode(rootVer);
        rootElement.setAttributeNode(rootXmlns);
        rootElement.setAttributeNode(rootUid);
        rootElement.setAttributeNode(rootTs);
        Element usesElement = document.createElement("Uses");
        Attr usesBio = document.createAttribute("bio");
        if (bio) {
            usesBio.setValue("y");
        } else {
            usesBio.setValue("n");
        }
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
        rootElement.appendChild(usesElement);
        Element metaElement = document.createElement("Meta");
        Attr metaUdc = document.createAttribute("udc");
        metaUdc.setValue("UIDAIADGYASH");
        Attr metaFdc = document.createAttribute("fdc");
        if ((deviceType.equalsIgnoreCase("F"))) {
            metaFdc.setValue("NC");
        } else {
            metaFdc.setValue("NA");
        }
        Attr metaIdc = document.createAttribute("idc");
        if ((deviceType.equalsIgnoreCase("I"))) {
            metaIdc.setValue("NC");
        } else {
            metaIdc.setValue("NA");
        }
        Attr metaPip = document.createAttribute("pip");
        metaPip.setValue("10.249.34.242");
        Attr metaLot = document.createAttribute("lot");
        metaLot.setValue("P");
        Attr metaLov = document.createAttribute("lov");
        metaLov.setValue("110011");

        metaElement.setAttributeNode(metaUdc);
        metaElement.setAttributeNode(metaFdc);
        metaElement.setAttributeNode(metaIdc);
        metaElement.setAttributeNode(metaPip);
        metaElement.setAttributeNode(metaLot);
        metaElement.setAttributeNode(metaLov);
        rootElement.appendChild(metaElement);
        Element skeyElement = document.createElement("Skey");
        Attr skeyCi = document.createAttribute("ci");
        skeyCi.setValue(encrypter.getCertificateIdentifier());
        skeyElement.setAttributeNode(skeyCi);
        skeyElement.appendChild(document.createTextNode(sKey));
        rootElement.appendChild(skeyElement);
        Element dataElement = document.createElement("Data");
        Attr dataType = document.createAttribute("type");
        dataType.setValue("X");
        dataElement.appendChild(document.createTextNode(pid));
        dataElement.setAttributeNode(dataType);
        rootElement.appendChild(dataElement);
        Element hmcaElement = document.createElement("Hmac");
        hmcaElement.appendChild(document.createTextNode(encryptedHmac));
        rootElement.appendChild(hmcaElement);
        //Edited by saurabh
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

       /* String txn = "UKC:" + aadhaarNo + pidTimeStamp.replace("T", "").replace("-", "").replace(":", "");
        Element rootElement = document.createElement("Auth");
        document.appendChild(rootElement);
        Attr rootUid = document.createAttribute("uid");
        rootUid.setValue(aadhaarNo);
        Attr rootTid = document.createAttribute("tid");
        rootTid.setValue("public");
        Attr rootAc = document.createAttribute("ac");
        //rootAc.setValue("0000650000");
        rootAc.setValue("STGNIC0011");
        Attr rootSa = document.createAttribute("sa");
        rootSa.setValue("");
        Attr rootLk = document.createAttribute("lk");
        rootLk.setValue("");
        Attr rootTxn = document.createAttribute("txn");
        rootTxn.setValue(txn);
        Attr rootVer = document.createAttribute("ver");
        //rootVer.setValue(String.valueOf(Global.AUTH_version));
        rootVer.setValue("1.6");

        Attr rootXmlns = document.createAttribute("xmlns");
        rootXmlns.setValue("http://www.uidai.gov.in/authentication/uid-auth-request/1.0");


        rootElement.setAttributeNode(rootTid);
        rootElement.setAttributeNode(rootAc);
        rootElement.setAttributeNode(rootSa);
        rootElement.setAttributeNode(rootLk);
        rootElement.setAttributeNode(rootTxn);
        rootElement.setAttributeNode(rootVer);
        rootElement.setAttributeNode(rootXmlns);
        rootElement.setAttributeNode(rootUid);

        //		<Uses bio="y"  bt="'.$data['bt'].'" pi="n" pa="n" pfa="n" pin="n" otp="n" />

        Element usesElement = document.createElement("Uses");

        Attr usesBio = document.createAttribute("bio");
        if (bio) {
            usesBio.setValue("y");
        } else {
            usesBio.setValue("n");
        }

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

        rootElement.appendChild(usesElement);

        //		<Meta udc="AIIMSTEST" fdc="'.$data['fdc'].'" idc="'.$data['idc'].'" pip="127.0.0.1" lot="P" lov="110092" />

        Element metaElement = document.createElement("Meta");

        Attr metaUdc = document.createAttribute("udc");
        // metaUdc.setValue(Global.connectedDeviceNameId);
        metaUdc.setValue("UIDAIADGYASH");

        Attr metaFdc = document.createAttribute("fdc");
        if ((deviceType.equalsIgnoreCase("F"))) {
            metaFdc.setValue("NC");
        } else {
            metaFdc.setValue("NA");
        }
        //  metaFdc.setValue("NC");

        Attr metaIdc = document.createAttribute("idc");
        if ((deviceType.equalsIgnoreCase("I"))) {
            metaIdc.setValue("NC");
        } else {
            metaIdc.setValue("NA");
        }

        Attr metaPip = document.createAttribute("pip");
        metaPip.setValue("10.249.34.242");
        Attr metaLot = document.createAttribute("lot");
        metaLot.setValue("P");
        Attr metaLov = document.createAttribute("lov");
        metaLov.setValue("110011");

        metaElement.setAttributeNode(metaUdc);
        metaElement.setAttributeNode(metaFdc);
        metaElement.setAttributeNode(metaIdc);
        metaElement.setAttributeNode(metaPip);
        metaElement.setAttributeNode(metaLot);
        metaElement.setAttributeNode(metaLov);

        rootElement.appendChild(metaElement);

        //		<Skey ci="'.$data['ci'].'">';
        //				$authXml .= $data['skey'];
        //				$authXml .= '</Skey>

        Element skeyElement = document.createElement("Skey");

        Attr skeyCi = document.createAttribute("ci");
        skeyCi.setValue(encrypter.getCertificateIdentifier());

        skeyElement.setAttributeNode(skeyCi);
        skeyElement.appendChild(document.createTextNode(sKey));

        rootElement.appendChild(skeyElement);
        //		<Data type="X">';
        //				$authXml .= $data['pid_data'];
        //				$authXml .= '</Data>

        Element dataElement = document.createElement("Data");

        Attr dataType = document.createAttribute("type");
        dataType.setValue("X");
        dataElement.appendChild(document.createTextNode(pid));
        dataElement.setAttributeNode(dataType);

        rootElement.appendChild(dataElement);
        //		<Hmac>';
        //				$authXml .= $data['hmac'];
        //				$authXml .= '</Hmac>

        Element hmcaElement = document.createElement("Hmac");
        hmcaElement.appendChild(document.createTextNode(encryptedHmac));

        rootElement.appendChild(hmcaElement);
        // Edited by saurabh

        //Edited by saurabh
        Element elementUserData = document.createElement("UserData");
        Attr imeiNo = document.createAttribute("imeiNo");
        imeiNo.setValue(Global.imei);
        //Log.e("imei no", Global.imei);
        Attr projectInfo = document.createAttribute("projectInfo");
        projectInfo.setValue("NHPS-FVS");
        Attr macAddress = document.createAttribute("macAddress");
        macAddress.setValue("");
        Attr uid = document.createAttribute("uid");
        uid.setValue(Global.VALIDATORAADHAR);
        Attr ts = document.createAttribute("ts");
        ts.setValue(Global.R_PID_TIME);

        elementUserData.setAttributeNode(imeiNo);
        elementUserData.setAttributeNode(projectInfo);
        elementUserData.setAttributeNode(macAddress);
        elementUserData.setAttributeNode(uid);
        elementUserData.setAttributeNode(ts);
        rootElement.appendChild(elementUserData);
*/

        Element signatureElement = document.createElement("Signature");
        signatureElement.appendChild(document.createTextNode(""));
        //		rootElement.appendChild(signatureElement);


        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = null;
        try {
            transformer = factory.newTransformer();
        } catch (TransformerConfigurationException e1) {
            e1.printStackTrace();
        }
        DOMSource domSource = new DOMSource(document);
        StringWriter writer = new StringWriter();
        //			OutputStream output = new ByteArrayOutputStream();
        StreamResult result = new StreamResult(writer);
        try {
            transformer.transform(domSource, result);
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        xml = writer.getBuffer().toString();


        return xml;
    }

    public String createXmlForKycAuth(String deviceType, String aadhaarNo, String fpImgString, boolean otp_check, String otp) {
        // Saurabh Bhandari
        bioDeviceType = deviceType;
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

        try {
            synchronizedKey = new SynchronizedKey(
                    encrypter.generateSessionKey(), UUID.randomUUID()
                    .toString(), new Date());
            syncSessionKey = synchronizedKey.getSeedSkey();
            encryptedSessionKey = encrypter
                    .encryptUsingPublicKey(syncSessionKey);
            sessionKeyDetails = SessionKeyDetails
                    .createSkeyToInitializeSynchronizedKey(
                            synchronizedKey.getKeyIdentifier(),
                            encryptedSessionKey);

        } catch (Exception e) {
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
        byte[] hmac = hashgenerator.generateSha256Hash(xmlPidBytes);
        String encryptedHmac = null;
        String encdata = null;

        try {
            encdata = Base64.encodeToString(encrypter.encryptUsingSessionKey(
                    syncSessionKey, xmlPidBytes), Base64.DEFAULT);
            encryptedHmac = Base64.encodeToString(
                    encrypter.encryptUsingSessionKey(syncSessionKey, hmac),
                    Base64.DEFAULT);
        } catch (Exception ex) {
            System.out.println("Error:" + ex.getMessage());
        }

        String txn = "UKC:" + aadhaarNo + pidTimeStamp.replace("T", "").replace("-", "").replace(":", "");
        Element rootElement = document.createElement("Auth");
        document.appendChild(rootElement);
        Attr rootUid = document.createAttribute("uid");
        rootUid.setValue(aadhaarNo);
        Attr rootTid = document.createAttribute("tid");
        rootTid.setValue("public");
        Attr rootAc = document.createAttribute("ac");
        //rootAc.setValue("0000650000");
        rootAc.setValue("STGNIC0011");
        Attr rootSa = document.createAttribute("sa");
        rootSa.setValue("");
        Attr rootLk = document.createAttribute("lk");
        rootLk.setValue("");
        Attr rootTxn = document.createAttribute("txn");
        rootTxn.setValue(txn);
        Attr rootVer = document.createAttribute("ver");
        Attr rootTs = document.createAttribute("ts");
        //rootVer.setValue(String.valueOf(Global.AUTH_version));
        rootVer.setValue("1.6");
        rootTs.setValue(Global.R_PID_TIME);

        Attr rootXmlns = document.createAttribute("xmlns");
        rootXmlns.setValue("http://www.uidai.gov.in/authentication/uid-auth-request/1.0");


        rootElement.setAttributeNode(rootTid);
        rootElement.setAttributeNode(rootAc);
        rootElement.setAttributeNode(rootSa);
        rootElement.setAttributeNode(rootLk);
        rootElement.setAttributeNode(rootTxn);
        rootElement.setAttributeNode(rootVer);
        rootElement.setAttributeNode(rootXmlns);
        rootElement.setAttributeNode(rootUid);
        rootElement.setAttributeNode(rootTs);

        //		<Uses bio="y"  bt="'.$data['bt'].'" pi="n" pa="n" pfa="n" pin="n" otp="n" />

        Element usesElement = document.createElement("Uses");

        Attr usesBio = document.createAttribute("bio");
        if (bio) {
            usesBio.setValue("y");
        } else {
            usesBio.setValue("n");
        }

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

        rootElement.appendChild(usesElement);

        //		<Meta udc="AIIMSTEST" fdc="'.$data['fdc'].'" idc="'.$data['idc'].'" pip="127.0.0.1" lot="P" lov="110092" />

        Element metaElement = document.createElement("Meta");

        Attr metaUdc = document.createAttribute("udc");
        // metaUdc.setValue(Global.connectedDeviceNameId);

        metaUdc.setValue("UIDAIADGYASH");

        Attr metaFdc = document.createAttribute("fdc");
        if ((deviceType.equalsIgnoreCase("F"))) {
            metaFdc.setValue("NC");
        } else {
            metaFdc.setValue("NA");
        }
        //  metaFdc.setValue("NC");

        Attr metaIdc = document.createAttribute("idc");
        if ((deviceType.equalsIgnoreCase("I"))) {
            metaIdc.setValue("NC");
        } else {
            metaIdc.setValue("NA");
        }

        Attr metaPip = document.createAttribute("pip");
        metaPip.setValue("10.249.34.242");
        Attr metaLot = document.createAttribute("lot");
        metaLot.setValue("P");
        Attr metaLov = document.createAttribute("lov");
        metaLov.setValue("110011");

        metaElement.setAttributeNode(metaUdc);
        metaElement.setAttributeNode(metaFdc);
        metaElement.setAttributeNode(metaIdc);
        metaElement.setAttributeNode(metaPip);
        metaElement.setAttributeNode(metaLot);
        metaElement.setAttributeNode(metaLov);

        rootElement.appendChild(metaElement);

        //		<Skey ci="'.$data['ci'].'">';
        //				$authXml .= $data['skey'];
        //				$authXml .= '</Skey>

        Element skeyElement = document.createElement("Skey");

        Attr skeyCi = document.createAttribute("ci");
        skeyCi.setValue(encrypter.getCertificateIdentifier());

        skeyElement.setAttributeNode(skeyCi);
        skeyElement.appendChild(document.createTextNode(Base64.encodeToString(
                sessionKeyDetails.getSkeyValue(), Base64.DEFAULT)));

        rootElement.appendChild(skeyElement);
        //		<Data type="X">';
        //				$authXml .= $data['pid_data'];
        //				$authXml .= '</Data>

        Element dataElement = document.createElement("Data");

        Attr dataType = document.createAttribute("type");
        dataType.setValue("X");
        dataElement.appendChild(document.createTextNode(encdata));
        dataElement.setAttributeNode(dataType);

        rootElement.appendChild(dataElement);
        //		<Hmac>';
        //				$authXml .= $data['hmac'];
        //				$authXml .= '</Hmac>

        Element hmcaElement = document.createElement("Hmac");
        hmcaElement.appendChild(document.createTextNode(encryptedHmac));

        rootElement.appendChild(hmcaElement);

        //Edited by saurabh
        Element elementUserData = document.createElement("UserData");
        Attr imeiNo = document.createAttribute("imeiNo");
        imeiNo.setValue(Global.imei);
        //Log.e("imei no", Global.imei);
        Attr projectInfo = document.createAttribute("projectInfo");
        projectInfo.setValue("NHPS-FVS");
        Attr macAddress = document.createAttribute("macAddress");
        macAddress.setValue("");
        Attr uid = document.createAttribute("uid");
        uid.setValue(Global.VALIDATORAADHAR);
        Attr ts = document.createAttribute("ts");
        ts.setValue(Global.R_PID_TIME);

        elementUserData.setAttributeNode(imeiNo);
        elementUserData.setAttributeNode(projectInfo);
        elementUserData.setAttributeNode(macAddress);
        elementUserData.setAttributeNode(uid);
        elementUserData.setAttributeNode(ts);
        rootElement.appendChild(elementUserData);

        Element signatureElement = document.createElement("Signature");
        signatureElement.appendChild(document.createTextNode(""));
        //		rootElement.appendChild(signatureElement);


        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = null;
        try {
            transformer = factory.newTransformer();
        } catch (TransformerConfigurationException e1) {
            e1.printStackTrace();
        }
        DOMSource domSource = new DOMSource(document);
        StringWriter writer = new StringWriter();
        //			OutputStream output = new ByteArrayOutputStream();
        StreamResult result = new StreamResult(writer);
        try {
            try {
                transformer.transform(domSource, result);
            } catch (TransformerException e) {
                e.printStackTrace();
            }
        } catch (Exception ex) {
            appendLogInvalidXmlAWithoutRad(ex.getCause().toString() + "\n\n\n" + ex.getMessage().toString(), "ErrorGeneratingXml");
        }
        KycAuthXml = writer.getBuffer().toString();
        Log.e("kyc auth ", "==" + KycAuthXml);

        return KycAuthXml;

    }


    public String createXmlForKycNew(String deviceType, String aadhaarNo, String fpImgString, boolean otp_check, String otp) {
        // Saurabh Bhandari


        bioDeviceType = deviceType;
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

        try {
            synchronizedKey = new SynchronizedKey(
                    encrypter.generateSessionKey(), UUID.randomUUID()
                    .toString(), new Date());
            syncSessionKey = synchronizedKey.getSeedSkey();
            encryptedSessionKey = encrypter
                    .encryptUsingPublicKey(syncSessionKey);
            sessionKeyDetails = SessionKeyDetails
                    .createSkeyToInitializeSynchronizedKey(
                            synchronizedKey.getKeyIdentifier(),
                            encryptedSessionKey);

        } catch (Exception e) {
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
        byte[] hmac = hashgenerator.generateSha256Hash(xmlPidBytes);
        String encryptedHmac = null;
        String encdata = null;

        try {
            encdata = Base64.encodeToString(encrypter.encryptUsingSessionKey(
                    syncSessionKey, xmlPidBytes), Base64.DEFAULT);
            encryptedHmac = Base64.encodeToString(
                    encrypter.encryptUsingSessionKey(syncSessionKey, hmac),
                    Base64.DEFAULT);
        } catch (Exception ex) {
            System.out.println("Error:" + ex.getMessage());
        }

        String txn = "UKC:" + aadhaarNo + pidTimeStamp.replace("T", "").replace("-", "").replace(":", "");
        Element mainElement = document.createElement("AuthData");
        document.appendChild(mainElement);
        Element rootElement = document.createElement("Auth");
        mainElement.appendChild(rootElement);

        Attr rootUid = document.createAttribute("uid");
        rootUid.setValue(aadhaarNo);
        Attr rootTid = document.createAttribute("tid");
        rootTid.setValue("public");
        Attr rootAc = document.createAttribute("ac");
        //rootAc.setValue("0000650000");
        rootAc.setValue("00000000");
        Attr rootSa = document.createAttribute("sa");
        rootSa.setValue("");
        Attr rootLk = document.createAttribute("lk");
        rootLk.setValue("");
        Attr rootTxn = document.createAttribute("txn");
        rootTxn.setValue(txn);
        Attr rootVer = document.createAttribute("ver");
        //rootVer.setValue(String.valueOf(Global.AUTH_version));
        rootVer.setValue("1.6");
        Attr rootTs = document.createAttribute("ts");
        rootTs.setValue(Global.R_PID_TIME);
        Attr rootXmlns = document.createAttribute("xmlns");
        rootXmlns.setValue("http://www.uidai.gov.in/authentication/uid-auth-request/1.0");


        rootElement.setAttributeNode(rootTid);
        rootElement.setAttributeNode(rootAc);
        rootElement.setAttributeNode(rootSa);
        rootElement.setAttributeNode(rootLk);
        rootElement.setAttributeNode(rootTxn);
        rootElement.setAttributeNode(rootVer);
        rootElement.setAttributeNode(rootTs);
        rootElement.setAttributeNode(rootXmlns);
        rootElement.setAttributeNode(rootUid);

        //		<Uses bio="y"  bt="'.$data['bt'].'" pi="n" pa="n" pfa="n" pin="n" otp="n" />

        Element usesElement = document.createElement("Uses");

        Attr usesBio = document.createAttribute("bio");
        if (bio) {
            usesBio.setValue("y");
        } else {
            usesBio.setValue("n");
        }

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

        rootElement.appendChild(usesElement);

        //		<Meta udc="AIIMSTEST" fdc="'.$data['fdc'].'" idc="'.$data['idc'].'" pip="127.0.0.1" lot="P" lov="110092" />

        Element metaElement = document.createElement("Meta");

        Attr metaUdc = document.createAttribute("udc");
        // metaUdc.setValue(Global.connectedDeviceNameId);

        metaUdc.setValue("UIDAIADGYASH");

        Attr metaFdc = document.createAttribute("fdc");
        if ((deviceType.equalsIgnoreCase("F"))) {
            metaFdc.setValue("NC");
        } else {
            metaFdc.setValue("NA");
        }
        //  metaFdc.setValue("NC");

        Attr metaIdc = document.createAttribute("idc");
        if ((deviceType.equalsIgnoreCase("I"))) {
            metaIdc.setValue("NC");
        } else {
            metaIdc.setValue("NA");
        }

        Attr metaPip = document.createAttribute("pip");
        metaPip.setValue("10.249.34.242");
        Attr metaLot = document.createAttribute("lot");
        metaLot.setValue("P");
        Attr metaLov = document.createAttribute("lov");
        metaLov.setValue("110011");

        metaElement.setAttributeNode(metaUdc);
        metaElement.setAttributeNode(metaFdc);
        metaElement.setAttributeNode(metaIdc);
        metaElement.setAttributeNode(metaPip);
        metaElement.setAttributeNode(metaLot);
        metaElement.setAttributeNode(metaLov);

        rootElement.appendChild(metaElement);

        //		<Skey ci="'.$data['ci'].'">';
        //				$authXml .= $data['skey'];
        //				$authXml .= '</Skey>

        Element skeyElement = document.createElement("Skey");

        Attr skeyCi = document.createAttribute("ci");
        skeyCi.setValue(encrypter.getCertificateIdentifier());

        skeyElement.setAttributeNode(skeyCi);
        skeyElement.appendChild(document.createTextNode(Base64.encodeToString(
                sessionKeyDetails.getSkeyValue(), Base64.DEFAULT)));

        rootElement.appendChild(skeyElement);
        //		<Data type="X">';
        //				$authXml .= $data['pid_data'];
        //				$authXml .= '</Data>

        Element dataElement = document.createElement("Data");

        Attr dataType = document.createAttribute("type");
        dataType.setValue("X");
        dataElement.appendChild(document.createTextNode(encdata));
        dataElement.setAttributeNode(dataType);

        rootElement.appendChild(dataElement);
        //		<Hmac>';
        //				$authXml .= $data['hmac'];
        //				$authXml .= '</Hmac>

        Element hmcaElement = document.createElement("Hmac");
        hmcaElement.appendChild(document.createTextNode(encryptedHmac));

        rootElement.appendChild(hmcaElement);

        //Edited by saurabh

        Element elementUserData = document.createElement("UserData");
        Attr imeiNo = document.createAttribute("imeiNo");
        imeiNo.setValue(Global.imei);
        //Log.e("imei no", Global.imei);
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








       /* Element rootElement = document.createElement("Auth");
        document.appendChild(rootElement);
        Attr rootUid = document.createAttribute("uid");
        rootUid.setValue(aadhaarNo);
        Attr rootTid = document.createAttribute("tid");
        rootTid.setValue("public");
        Attr rootAc = document.createAttribute("ac");
        //rootAc.setValue("0000650000");
        rootAc.setValue("STGNIC0011");
        Attr rootSa = document.createAttribute("sa");
        rootSa.setValue("");
        Attr rootLk = document.createAttribute("lk");
        rootLk.setValue("");
        Attr rootTxn = document.createAttribute("txn");
        rootTxn.setValue(txn);
        Attr rootVer = document.createAttribute("ver");
        Attr rootTs = document.createAttribute("ts");
        //rootVer.setValue(String.valueOf(Global.AUTH_version));
        rootVer.setValue("1.6");
        rootTs.setValue(Global.R_PID_TIME);

        Attr rootXmlns = document.createAttribute("xmlns");
        rootXmlns.setValue("http://www.uidai.gov.in/authentication/uid-auth-request/1.0");


        rootElement.setAttributeNode(rootTid);
        rootElement.setAttributeNode(rootAc);
        rootElement.setAttributeNode(rootSa);
        rootElement.setAttributeNode(rootLk);
        rootElement.setAttributeNode(rootTxn);
        rootElement.setAttributeNode(rootVer);
        rootElement.setAttributeNode(rootXmlns);
        rootElement.setAttributeNode(rootUid);
        rootElement.setAttributeNode(rootTs);

        //		<Uses bio="y"  bt="'.$data['bt'].'" pi="n" pa="n" pfa="n" pin="n" otp="n" />

        Element usesElement = document.createElement("Uses");

        Attr usesBio = document.createAttribute("bio");
        if (bio) {
            usesBio.setValue("y");
        } else {
            usesBio.setValue("n");
        }

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

        rootElement.appendChild(usesElement);

        //		<Meta udc="AIIMSTEST" fdc="'.$data['fdc'].'" idc="'.$data['idc'].'" pip="127.0.0.1" lot="P" lov="110092" />

        Element metaElement = document.createElement("Meta");

        Attr metaUdc = document.createAttribute("udc");
        // metaUdc.setValue(Global.connectedDeviceNameId);

        metaUdc.setValue("UIDAIADGYASH");

        Attr metaFdc = document.createAttribute("fdc");
        if ((deviceType.equalsIgnoreCase("F"))) {
            metaFdc.setValue("NC");
        } else {
            metaFdc.setValue("NA");
        }
        //  metaFdc.setValue("NC");

        Attr metaIdc = document.createAttribute("idc");
        if ((deviceType.equalsIgnoreCase("I"))) {
            metaIdc.setValue("NC");
        } else {
            metaIdc.setValue("NA");
        }

        Attr metaPip = document.createAttribute("pip");
        metaPip.setValue("10.249.34.242");
        Attr metaLot = document.createAttribute("lot");
        metaLot.setValue("P");
        Attr metaLov = document.createAttribute("lov");
        metaLov.setValue("110011");

        metaElement.setAttributeNode(metaUdc);
        metaElement.setAttributeNode(metaFdc);
        metaElement.setAttributeNode(metaIdc);
        metaElement.setAttributeNode(metaPip);
        metaElement.setAttributeNode(metaLot);
        metaElement.setAttributeNode(metaLov);

        rootElement.appendChild(metaElement);

        //		<Skey ci="'.$data['ci'].'">';
        //				$authXml .= $data['skey'];
        //				$authXml .= '</Skey>

        Element skeyElement = document.createElement("Skey");

        Attr skeyCi = document.createAttribute("ci");
        skeyCi.setValue(encrypter.getCertificateIdentifier());

        skeyElement.setAttributeNode(skeyCi);
        skeyElement.appendChild(document.createTextNode(Base64.encodeToString(
                sessionKeyDetails.getSkeyValue(), Base64.DEFAULT)));

        rootElement.appendChild(skeyElement);
        //		<Data type="X">';
        //				$authXml .= $data['pid_data'];
        //				$authXml .= '</Data>

        Element dataElement = document.createElement("Data");

        Attr dataType = document.createAttribute("type");
        dataType.setValue("X");
        dataElement.appendChild(document.createTextNode(encdata));
        dataElement.setAttributeNode(dataType);

        rootElement.appendChild(dataElement);
        //		<Hmac>';
        //				$authXml .= $data['hmac'];
        //				$authXml .= '</Hmac>

        Element hmcaElement = document.createElement("Hmac");
        hmcaElement.appendChild(document.createTextNode(encryptedHmac));

        rootElement.appendChild(hmcaElement);

        //Edited by saurabh
        Element elementUserData = document.createElement("UserData");
        Attr imeiNo = document.createAttribute("imeiNo");
        imeiNo.setValue(Global.imei);
        //Log.e("imei no", Global.imei);
        Attr projectInfo = document.createAttribute("projectInfo");
        projectInfo.setValue("NHPS-FVS");
        Attr macAddress = document.createAttribute("macAddress");
        macAddress.setValue("");
        Attr uid = document.createAttribute("uid");
        uid.setValue(Global.VALIDATORAADHAR);
        Attr ts = document.createAttribute("ts");
        ts.setValue(Global.R_PID_TIME);

        elementUserData.setAttributeNode(imeiNo);
        elementUserData.setAttributeNode(projectInfo);
        elementUserData.setAttributeNode(macAddress);
        elementUserData.setAttributeNode(uid);
        elementUserData.setAttributeNode(ts);
        rootElement.appendChild(elementUserData);
*/
        Element signatureElement = document.createElement("Signature");
        signatureElement.appendChild(document.createTextNode(""));
        //		rootElement.appendChild(signatureElement);


        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = null;
        try {
            transformer = factory.newTransformer();
        } catch (TransformerConfigurationException e1) {
            e1.printStackTrace();
        }
        DOMSource domSource = new DOMSource(document);
        StringWriter writer = new StringWriter();
        //			OutputStream output = new ByteArrayOutputStream();
        StreamResult result = new StreamResult(writer);
        try {
            try {
                transformer.transform(domSource, result);
            } catch (TransformerException e) {
                e.printStackTrace();
            }
        } catch (Exception ex) {
            appendLogInvalidXmlAWithoutRad(ex.getCause().toString() + "\n\n\n" + ex.getMessage().toString(), "ErrorGeneratingXml");
        }
        KycAuthXml = writer.getBuffer().toString();
        Log.e("kyc auth ", "==" + KycAuthXml);

        return KycAuthXml;

    }

    private String getPidXmlforINFOCUS(String uname, boolean bio, boolean pi,
                                       String isotemplate, boolean otp_check, String otp) {
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
            attr.setValue(String.valueOf(Global.version));
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
                // String fpdata= Base64.encodeToString(isotemplate,
                // Base64.DEFAULT);
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

    public String createXmlForKycAuthForINFOCUS(String deviceType, String aadhaarNo, String fpImgString, boolean otp_check, String otp) {
// need to change
        // 0000
        bioDeviceType = deviceType;
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
        Global.R_PID_TIME = pidTimeStamp;

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

        try {
            synchronizedKey = new SynchronizedKey(
                    encrypter.generateSessionKey(), UUID.randomUUID()
                    .toString(), new Date());
            syncSessionKey = synchronizedKey.getSeedSkey();
            encryptedSessionKey = encrypter
                    .encryptUsingPublicKey(syncSessionKey);
            sessionKeyDetails = SessionKeyDetails
                    .createSkeyToInitializeSynchronizedKey(
                            synchronizedKey.getKeyIdentifier(),
                            encryptedSessionKey);

        } catch (Exception e) {
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
        byte[] xmlPidBytes = getPidXmlforINFOCUS("", bio, pi, fpImgString, otp_check, otp).getBytes();
        byte[] hmac = hashgenerator.generateSha256Hash(xmlPidBytes);
        String encryptedHmac = null;
        String encdata = null;

        try {
            encdata = Base64.encodeToString(encrypter.encryptUsingSessionKey(
                    syncSessionKey, xmlPidBytes), Base64.DEFAULT);
            encryptedHmac = Base64.encodeToString(
                    encrypter.encryptUsingSessionKey(syncSessionKey, hmac),
                    Base64.DEFAULT);
        } catch (Exception ex) {
            System.out.println("Error:" + ex.getMessage());
        }

        //  String txn = "NIC:" + aadhaarNo + pidTimeStamp.replace("T", "").replace("-", "").replace(":", "") + "NHPS";
        String txn = "UKC:" + aadhaarNo + pidTimeStamp.replace("T", "").replace("-", "").replace(":", "");
        Element mainElement = document.createElement("AuthData");
        document.appendChild(mainElement);
        Element rootElement = document.createElement("Auth");
        mainElement.appendChild(rootElement);
        Attr rootUid = document.createAttribute("uid");
        rootUid.setValue(aadhaarNo);
        Attr rootTid = document.createAttribute("tid");
        rootTid.setValue("public");
        Attr rootAc = document.createAttribute("ac");
        rootAc.setValue("00000000");
        Attr rootSa = document.createAttribute("sa");
        rootSa.setValue("");
        Attr rootLk = document.createAttribute("lk");
        rootLk.setValue("");
        Attr rootTxn = document.createAttribute("txn");
        rootTxn.setValue(txn);
        Attr rootVer = document.createAttribute("ver");
        rootVer.setValue("1.6");
        Attr rootTs = document.createAttribute("ts");
        rootTs.setValue(Global.R_PID_TIME);

        Attr rootXmlns = document.createAttribute("xmlns");
        rootXmlns.setValue("http://www.uidai.gov.in/authentication/uid-auth-request/1.0");
        rootElement.setAttributeNode(rootTid);
        rootElement.setAttributeNode(rootAc);
        rootElement.setAttributeNode(rootSa);
        rootElement.setAttributeNode(rootLk);
        rootElement.setAttributeNode(rootTxn);
        rootElement.setAttributeNode(rootVer);
        rootElement.setAttributeNode(rootTs);
        rootElement.setAttributeNode(rootXmlns);
        rootElement.setAttributeNode(rootUid);
        Element usesElement = document.createElement("Uses");
        Attr usesBio = document.createAttribute("bio");
        if (bio) {
            usesBio.setValue("y");
        } else {
            usesBio.setValue("n");
        }
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
        rootElement.appendChild(usesElement);
        Element metaElement = document.createElement("Meta");
        Attr metaUdc = document.createAttribute("udc");
        metaUdc.setValue("UIDAIADGYASH");
        Attr metaFdc = document.createAttribute("fdc");
        if ((deviceType.equalsIgnoreCase("F"))) {
            metaFdc.setValue("NC");
        } else {
            metaFdc.setValue("NA");
        }
        Attr metaIdc = document.createAttribute("idc");
        if ((deviceType.equalsIgnoreCase("I"))) {
            metaIdc.setValue("NC");
        } else {
            metaIdc.setValue("NA");
        }
        Attr metaPip = document.createAttribute("pip");
        metaPip.setValue("10.249.34.242");
        Attr metaLot = document.createAttribute("lot");
        metaLot.setValue("P");
        Attr metaLov = document.createAttribute("lov");
        metaLov.setValue("110011");

        metaElement.setAttributeNode(metaUdc);
        metaElement.setAttributeNode(metaFdc);
        metaElement.setAttributeNode(metaIdc);
        metaElement.setAttributeNode(metaPip);
        metaElement.setAttributeNode(metaLot);
        metaElement.setAttributeNode(metaLov);
        rootElement.appendChild(metaElement);
        Element skeyElement = document.createElement("Skey");
        Attr skeyCi = document.createAttribute("ci");
        skeyCi.setValue(encrypter.getCertificateIdentifier());
        skeyElement.setAttributeNode(skeyCi);
        skeyElement.appendChild(document.createTextNode(Base64.encodeToString(
                sessionKeyDetails.getSkeyValue(), Base64.DEFAULT)));
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
        //Edited by saurabh
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



      /*  Attr rootUid = document.createAttribute("uid");
        rootUid.setValue(aadhaarNo);
        Attr rootTid = document.createAttribute("tid");
        rootTid.setValue("public");
        Attr rootAc = document.createAttribute("ac");
        //rootAc.setValue("0000650000");
        rootAc.setValue("STGNIC0011");
        Attr rootSa = document.createAttribute("sa");
        rootSa.setValue("");
        Attr rootLk = document.createAttribute("lk");
        rootLk.setValue("");
        Attr rootTxn = document.createAttribute("txn");
        rootTxn.setValue(txn);
        Attr rootVer = document.createAttribute("ver");
        //rootVer.setValue(String.valueOf(Global.AUTH_version));
        rootVer.setValue("1.6");

        Attr rootXmlns = document.createAttribute("xmlns");
        rootXmlns.setValue("http://www.uidai.gov.in/authentication/uid-auth-request/1.0");


        rootElement.setAttributeNode(rootTid);
        rootElement.setAttributeNode(rootAc);
        rootElement.setAttributeNode(rootSa);
        rootElement.setAttributeNode(rootLk);
        rootElement.setAttributeNode(rootTxn);
        rootElement.setAttributeNode(rootVer);
        rootElement.setAttributeNode(rootXmlns);
        rootElement.setAttributeNode(rootUid);

        //		<Uses bio="y"  bt="'.$data['bt'].'" pi="n" pa="n" pfa="n" pin="n" otp="n" />

        Element usesElement = document.createElement("Uses");

        Attr usesBio = document.createAttribute("bio");
        if (bio) {
            usesBio.setValue("y");
        } else {
            usesBio.setValue("n");
        }

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

        rootElement.appendChild(usesElement);

        //		<Meta udc="AIIMSTEST" fdc="'.$data['fdc'].'" idc="'.$data['idc'].'" pip="127.0.0.1" lot="P" lov="110092" />

        Element metaElement = document.createElement("Meta");

        Attr metaUdc = document.createAttribute("udc");
        // metaUdc.setValue(Global.connectedDeviceNameId);
        metaUdc.setValue("UIDAIADGYASH");

        Attr metaFdc = document.createAttribute("fdc");
        if ((deviceType.equalsIgnoreCase("F"))) {
            metaFdc.setValue("NC");
        } else {
            metaFdc.setValue("NA");
        }
        //taFdc.setValue("NC");

        Attr metaIdc = document.createAttribute("idc");
        if ((deviceType.equalsIgnoreCase("I"))) {
            metaIdc.setValue("NC");
        } else {
            metaIdc.setValue("NA");
        }

        Attr metaPip = document.createAttribute("pip");
        metaPip.setValue("10.249.34.242");
        Attr metaLot = document.createAttribute("lot");
        metaLot.setValue("P");
        Attr metaLov = document.createAttribute("lov");
        metaLov.setValue("110011");

        metaElement.setAttributeNode(metaUdc);
        metaElement.setAttributeNode(metaFdc);
        metaElement.setAttributeNode(metaIdc);
        metaElement.setAttributeNode(metaPip);
        metaElement.setAttributeNode(metaLot);
        metaElement.setAttributeNode(metaLov);

        rootElement.appendChild(metaElement);

        //		<Skey ci="'.$data['ci'].'">';
        //				$authXml .= $data['skey'];
        //				$authXml .= '</Skey>

        Element skeyElement = document.createElement("Skey");

        Attr skeyCi = document.createAttribute("ci");
        skeyCi.setValue(encrypter.getCertificateIdentifier());

        skeyElement.setAttributeNode(skeyCi);
        skeyElement.appendChild(document.createTextNode(Base64.encodeToString(
                sessionKeyDetails.getSkeyValue(), Base64.DEFAULT)));

        rootElement.appendChild(skeyElement);
        //		<Data type="X">';
        //				$authXml .= $data['pid_data'];
        //				$authXml .= '</Data>

        Element dataElement = document.createElement("Data");

        Attr dataType = document.createAttribute("type");
        dataType.setValue("X");
        dataElement.appendChild(document.createTextNode(encdata));
        dataElement.setAttributeNode(dataType);

        rootElement.appendChild(dataElement);
        //		<Hmac>';
        //				$authXml .= $data['hmac'];
        //				$authXml .= '</Hmac>

        Element hmcaElement = document.createElement("Hmac");
        hmcaElement.appendChild(document.createTextNode(encryptedHmac));

        rootElement.appendChild(hmcaElement);
        Element elementUserData = document.createElement("UserData");
        Attr imeiNo = document.createAttribute("imeiNo");
        imeiNo.setValue(Global.imei);
        //Log.e("imei no", Global.imei);
        Attr projectInfo = document.createAttribute("projectInfo");
        projectInfo.setValue("NHPS-FVS");
        Attr macAddress = document.createAttribute("macAddress");
        macAddress.setValue("");
        Attr uid = document.createAttribute("uid");
        uid.setValue(Global.VALIDATORAADHAR);
        Attr ts = document.createAttribute("ts");
        ts.setValue(Global.R_PID_TIME);

        elementUserData.setAttributeNode(imeiNo);
        elementUserData.setAttributeNode(projectInfo);
        elementUserData.setAttributeNode(macAddress);
        elementUserData.setAttributeNode(uid);
        elementUserData.setAttributeNode(ts);
        rootElement.appendChild(elementUserData);

*/
        Element signatureElement = document.createElement("Signature");
        signatureElement.appendChild(document.createTextNode(""));
        //		rootElement.appendChild(signatureElement);

        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = null;
        try {
            transformer = factory.newTransformer();
        } catch (TransformerConfigurationException e1) {
            e1.printStackTrace();
        }
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

    public String createXmlForDemoAuth(String aadhaarNo, boolean pi, boolean pa, boolean pfa, String pi_ms, String pi_mv, String pi_name
            , String pi_gender, String pi_dob, String pi_dobt, String pi_age, String pi_ph, String pi_mail, String pa_co, String pa_house,
                                       String pa_street, String pa_landmark, String pa_loc, String pa_vill, String pa_subdist, String pa_dist, String pa_state, String pa_pc
            , String pa_po, String pfa_ms, String pfa_mv, String pfa_av) {
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

        try {
            synchronizedKey = new SynchronizedKey(
                    encrypter.generateSessionKey(), UUID.randomUUID()
                    .toString(), new Date());
            syncSessionKey = synchronizedKey.getSeedSkey();
            encryptedSessionKey = encrypter
                    .encryptUsingPublicKey(syncSessionKey);
            sessionKeyDetails = SessionKeyDetails
                    .createSkeyToInitializeSynchronizedKey(
                            synchronizedKey.getKeyIdentifier(),
                            encryptedSessionKey);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


        boolean otp_check = false;

		/*boolean pi , boolean pa , boolean pfa ,String pi_ms ,String pi_mv ,String pi_name
        , String pi_gender , String pi_dob , String pi_dobt , String pi_age,String pi_ph,String pi_mail,String pa_co,String pa_house,
		String pa_street,String pa_landmark,String pa_loc,String pa_vill ,String pa_subdist,String pa_dist,String pa_state , String pa_pc
		,String pa_po , String pfa_ms,String pfa_mv,String pfa_av*/

        byte[] xmlPidBytes = getPidXmlForDemoAuth(pi, pa, pfa, pi_ms, pi_mv, pi_name, pi_gender, pi_dob, pi_dobt, pi_age, pi_ph, pi_mail,
                pa_co, pa_house, pa_street, pa_landmark, pa_loc, pa_vill, pa_subdist, pa_dist, pa_state, pa_pc, pa_po, pfa_ms, pfa_mv, pfa_av).getBytes();
        byte[] hmac = hashgenerator.generateSha256Hash(xmlPidBytes);
        String encryptedHmac = null;
        String encdata = null;

        try {
            encdata = Base64.encodeToString(encrypter.encryptUsingSessionKey(
                    syncSessionKey, xmlPidBytes), Base64.DEFAULT);
            encryptedHmac = Base64.encodeToString(
                    encrypter.encryptUsingSessionKey(syncSessionKey, hmac),
                    Base64.DEFAULT);
        } catch (Exception ex) {
            System.out.println("Error:" + ex.getMessage());
        }

        String txn = "UKC:" + aadhaarNo + pidTimeStamp.replace("T", "").replace("-", "").replace(":", "");
        Element rootElement = document.createElement("Auth");
        document.appendChild(rootElement);
        Attr rootUid = document.createAttribute("uid");
        rootUid.setValue(aadhaarNo);
        Attr rootTid = document.createAttribute("tid");
        rootTid.setValue("public");
        Attr rootAc = document.createAttribute("ac");
        //rootAc.setValue("0000650000");
        rootAc.setValue("");
        Attr rootSa = document.createAttribute("sa");
        rootSa.setValue("");
        Attr rootLk = document.createAttribute("lk");
        rootLk.setValue("");
        Attr rootTxn = document.createAttribute("txn");
        rootTxn.setValue(txn);
        Attr rootVer = document.createAttribute("ver");
        //rootVer.setValue(String.valueOf(Global.AUTH_version));
        rootVer.setValue("1.6");

        Attr rootXmlns = document.createAttribute("xmlns");
        rootXmlns.setValue("http://www.uidai.gov.in/authentication/uid-auth-request/1.0");


        rootElement.setAttributeNode(rootTid);
        rootElement.setAttributeNode(rootAc);
        rootElement.setAttributeNode(rootSa);
        rootElement.setAttributeNode(rootLk);
        rootElement.setAttributeNode(rootTxn);
        rootElement.setAttributeNode(rootVer);
        rootElement.setAttributeNode(rootXmlns);
        rootElement.setAttributeNode(rootUid);

        //		<Uses bio="y"  bt="'.$data['bt'].'" pi="n" pa="n" pfa="n" pin="n" otp="n" />

        Element usesElement = document.createElement("Uses");

        Attr usesBio = document.createAttribute("bio");

        usesBio.setValue("n");

        Attr usesBt = document.createAttribute("bt");


        Attr usesPi = document.createAttribute("pi");
        if (pi) {
            usesPi.setValue("y");
        } else {
            usesPi.setValue("n");
        }

        Attr usesPa = document.createAttribute("pa");
        if (pa) {
            usesPa.setValue("y");
        } else {
            usesPa.setValue("n");
        }

        Attr usesPfa = document.createAttribute("pfa");
        if (pfa) {
            usesPfa.setValue("y");

        } else {

            usesPfa.setValue("n");
        }
        Attr usesPin = document.createAttribute("pin");
        usesPin.setValue("n");
        Attr usesOtp = document.createAttribute("otp");
        if (otp_check) {
            usesOtp.setValue("y");
        } else {
            usesOtp.setValue("n");
        }

        usesElement.setAttributeNode(usesBio);
        /*if (deviceType.equalsIgnoreCase("F") | deviceType.equalsIgnoreCase("I")) {
            usesElement.setAttributeNode(usesBt);
		}*/
        usesElement.setAttributeNode(usesPi);
        usesElement.setAttributeNode(usesPa);
        usesElement.setAttributeNode(usesPfa);
        usesElement.setAttributeNode(usesPin);
        usesElement.setAttributeNode(usesOtp);

        rootElement.appendChild(usesElement);

        //		<Meta udc="AIIMSTEST" fdc="'.$data['fdc'].'" idc="'.$data['idc'].'" pip="127.0.0.1" lot="P" lov="110092" />

        Element metaElement = document.createElement("Meta");

        Attr metaUdc = document.createAttribute("udc");
        // metaUdc.setValue(Global.connectedDeviceNameId);
        metaUdc.setValue("UIDAIADGYASH");

        Attr metaFdc = document.createAttribute("fdc");
        //		if ((deviceType.equalsIgnoreCase("F"))) {
        //			metaFdc.setValue("NA");
        //		}else {
        //			metaFdc.setValue("NC");
        //		}
        metaFdc.setValue("NC");

        Attr metaIdc = document.createAttribute("idc");

        metaIdc.setValue("NA");

        Attr metaPip = document.createAttribute("pip");
        metaPip.setValue("10.249.34.242");
        Attr metaLot = document.createAttribute("lot");
        metaLot.setValue("P");
        Attr metaLov = document.createAttribute("lov");
        metaLov.setValue("110011");

        metaElement.setAttributeNode(metaUdc);
        metaElement.setAttributeNode(metaFdc);
        metaElement.setAttributeNode(metaIdc);
        metaElement.setAttributeNode(metaPip);
        metaElement.setAttributeNode(metaLot);
        metaElement.setAttributeNode(metaLov);

        rootElement.appendChild(metaElement);

        //		<Skey ci="'.$data['ci'].'">';
        //				$authXml .= $data['skey'];
        //				$authXml .= '</Skey>

        Element skeyElement = document.createElement("Skey");

        Attr skeyCi = document.createAttribute("ci");
        skeyCi.setValue(encrypter.getCertificateIdentifier());

        skeyElement.setAttributeNode(skeyCi);
        skeyElement.appendChild(document.createTextNode(Base64.encodeToString(
                sessionKeyDetails.getSkeyValue(), Base64.DEFAULT)));

        rootElement.appendChild(skeyElement);
        //		<Data type="X">';
        //				$authXml .= $data['pid_data'];
        //				$authXml .= '</Data>

        Element dataElement = document.createElement("Data");

        Attr dataType = document.createAttribute("type");
        dataType.setValue("X");
        dataElement.appendChild(document.createTextNode(encdata));
        dataElement.setAttributeNode(dataType);

        rootElement.appendChild(dataElement);
        //		<Hmac>';
        //				$authXml .= $data['hmac'];
        //				$authXml .= '</Hmac>

        Element hmcaElement = document.createElement("Hmac");
        hmcaElement.appendChild(document.createTextNode(encryptedHmac));

        rootElement.appendChild(hmcaElement);

        Element signatureElement = document.createElement("Signature");
        signatureElement.appendChild(document.createTextNode(""));
        //		rootElement.appendChild(signatureElement);


        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = null;
        try {
            transformer = factory.newTransformer();
        } catch (TransformerConfigurationException e1) {
            e1.printStackTrace();
        }
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
        Log.e("kyc auth xml", "==" + KycAuthXml);

        return KycAuthXml;

    }

    public String createXmlForKYC(String aadhaarNo, String deviceType, String encodedXml, String fpImgString, String imei, String authtpe) {
        String kycXml = "";
        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = null;
        try {
            documentBuilder = documentFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        org.w3c.dom.Document document = documentBuilder.newDocument();

		/*
         <Kyc ver=�� ts=�� ra=�� rc=�� mec=�� lr=�� de=��>
		<Rad>base64 encoded fully valid Auth XML for resident</Rad>
		</Kyc>
		 */
        Log.e("Global.PID_TIME_STAMP", " == " + Global.PID_TIME_STAMP);
        Element rootElement = document.createElement("Kyc");

        // setting attribute to element
        Attr rootXmlns = document.createAttribute("xmlns");
        rootXmlns.setValue("http://www.uidai.gov.in/kyc/uid-kyc-request/1.0");
        Attr rootVer = document.createAttribute("ver");
        rootVer.setValue(Global.KYC_API_VERSION);
        Attr rootTs = document.createAttribute("ts");
        Log.e("Global.R_PID_TIME", "===" + Global.R_PID_TIME);
        rootTs.setValue(Global.R_PID_TIME);
        appendLogInvalidXmlAWithoutRad(Global.R_PID_TIME, "TIME");
        Attr rootRa = document.createAttribute("ra");
        rootRa.setValue(deviceType);
        Attr rootRc = document.createAttribute("rc");
        rootRc.setValue("Y");
        Attr rootMec = document.createAttribute("mec");
        rootMec.setValue("Y");
        Attr rootLr = document.createAttribute("lr");
        rootLr.setValue("Y");
        Attr rootDe = document.createAttribute("de");
        rootDe.setValue("N");

		/*rootElement.setAttributeNode(rootXmlns);
        rootElement.setAttributeNode(rootVer);
		rootElement.setAttributeNode(rootTs);
		rootElement.setAttributeNode(rootRa);
		rootElement.setAttributeNode(rootRc);
		rootElement.setAttributeNode(rootMec);
		rootElement.setAttributeNode(rootLr);
		rootElement.setAttributeNode(rootDe);*/

        document.appendChild(rootElement);

		/*Element xmlns = document.createElement("xmlns");
        xmlns.appendChild(document.createTextNode("http://www.uidai.gov.in/kyc/uid-kyc-request/1.0"));
		rootElement.appendChild(xmlns);
		Element ver = document.createElement("ver");
		ver.appendChild(document.createTextNode(Global.KYC_API_VERSION));
		rootElement.appendChild(ver);
		Element ra = document.createElement("ra");
		ra.appendChild(document.createTextNode(deviceType));
		rootElement.appendChild(ra);
		Element rc = document.createElement("rc");
		rc.appendChild(document.createTextNode("Y"));
		rootElement.appendChild(rc);
		Element mec = document.createElement("mec");
		mec.appendChild(document.createTextNode("Y"));
		rootElement.appendChild(mec);
		Element lr = document.createElement("lr");
		lr.appendChild(document.createTextNode("Y"));
		rootElement.appendChild(lr);
		Element de = document.createElement("de");
		de.appendChild(document.createTextNode("N"));
		rootElement.appendChild(de);*/

        Element appvertype = document.createElement("APP_VER");
        appvertype.appendChild(document.createTextNode(Global.APP_VER));
        rootElement.appendChild(appvertype);

        Element opcode = document.createElement("OP_CODE");
        opcode.appendChild(document.createTextNode(Global.LOCATION_ADDRESS.replaceAll(",", "_") + "#" + Global.CURRENT_LOCATION_SET.replace(",", "_")));
        rootElement.appendChild(opcode);

        Element ts = document.createElement("ts");
        ts.appendChild(document.createTextNode(Global.R_PID_TIME));
        rootElement.appendChild(ts);

        Element ra = document.createElement("ra");
        ra.appendChild(document.createTextNode(deviceType));
        rootElement.appendChild(ra);

        Element uidElement = document.createElement("UID");
        uidElement.appendChild(document.createTextNode(Global.AUTH_AADHAAR));
        rootElement.appendChild(uidElement);

        Element radElement = document.createElement("Rad");
        radElement.appendChild(document.createTextNode(encodedXml));
        rootElement.appendChild(radElement);

        Element atypeElement = document.createElement("AuthType");
        atypeElement.appendChild(document.createTextNode(authtpe));
        rootElement.appendChild(atypeElement);

        Element imeiElement = document.createElement("imei");
        imeiElement.appendChild(document.createTextNode(Global.DEVICE_IMEI_NO));
        rootElement.appendChild(imeiElement);

        Element ntypeElement = document.createElement("NETTYPE");
        ntypeElement.appendChild(document.createTextNode(Global.NETWORK_TYPE));
        rootElement.appendChild(ntypeElement);

        Element nnameElement = document.createElement("NETNAME");
        if (Global.connectionType.equalsIgnoreCase("Wifi"))
            nnameElement.appendChild(document.createTextNode(Global.NETWORK_NAME));
        else
            nnameElement.appendChild(document.createTextNode(Global.OPERATOR_NAME + "#" + Global.OPERATOR_NAME_SET));
        rootElement.appendChild(nnameElement);

        //Cell information

        Element latElement = document.createElement("LAT");
        latElement.appendChild(document.createTextNode(String.valueOf(Global.latitude)));
        rootElement.appendChild(latElement);

        Element longElement = document.createElement("LONGIT");
        longElement.appendChild(document.createTextNode(String.valueOf(Global.longitude)));
        rootElement.appendChild(longElement);

        Element sigstrenElement = document.createElement("SIGSTRENGTH");
        sigstrenElement.appendChild(document.createTextNode(String.valueOf(Global.SIGNAL_STRENGTH)));
        rootElement.appendChild(sigstrenElement);

        Element cidElement = document.createElement("CID");
        cidElement.appendChild(document.createTextNode(String.valueOf(Global.cid)));
        rootElement.appendChild(cidElement);

        Element lacElement = document.createElement("LAC");
        lacElement.appendChild(document.createTextNode(String.valueOf(Global.lac)));
        rootElement.appendChild(lacElement);

        Element mncElement = document.createElement("MNC");
        mncElement.appendChild(document.createTextNode(Global.mnc));
        rootElement.appendChild(mncElement);

        Element mccElement = document.createElement("MCC");
        mccElement.appendChild(document.createTextNode(Global.mcc));
        rootElement.appendChild(mccElement);

        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = null;
        try {
            transformer = factory.newTransformer();
        } catch (TransformerConfigurationException e1) {
            e1.printStackTrace();
        }
        DOMSource domSource = new DOMSource(document);
        StringWriter writer = new StringWriter();
        //			OutputStream output = new ByteArrayOutputStream();
        StreamResult result = new StreamResult(writer);
        try {
            transformer.transform(domSource, result);
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        kycXml = writer.getBuffer().toString();
        //		Log.e("kyc xml", " = "+kycXml);

        return kycXml;

    }

    public String createXmlForKYCSamsung(String aadhaarNo, String deviceType, String encodedXml, String imei, String authtpe) {
        String kycXml = "";
        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = null;
        try {
            documentBuilder = documentFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        org.w3c.dom.Document document = documentBuilder.newDocument();

		/*
         <Kyc ver=�� ts=�� ra=�� rc=�� mec=�� lr=�� de=��>
		<Rad>base64 encoded fully valid Auth XML for resident</Rad>
		</Kyc>
		 */
        Log.e("Global.PID_TIME_STAMP", " == " + Global.PID_TIME_STAMP);
        Element rootElement = document.createElement("Kyc");

        // setting attribute to element
        Attr rootXmlns = document.createAttribute("xmlns");
        rootXmlns.setValue("http://www.uidai.gov.in/kyc/uid-kyc-request/1.0");
        Attr rootVer = document.createAttribute("ver");
        rootVer.setValue(Global.KYC_API_VERSION);
        Attr rootTs = document.createAttribute("ts");
        Log.e("Global.R_PID_TIME", "===" + Global.R_PID_TIME);
        rootTs.setValue(Global.R_PID_TIME);
        Attr rootRa = document.createAttribute("ra");
        rootRa.setValue(deviceType);
        Attr rootRc = document.createAttribute("rc");
        rootRc.setValue("Y");
        Attr rootMec = document.createAttribute("mec");
        rootMec.setValue("Y");
        Attr rootLr = document.createAttribute("lr");
        rootLr.setValue("Y");
        Attr rootDe = document.createAttribute("de");
        rootDe.setValue("N");


        document.appendChild(rootElement);


        Element appvertype = document.createElement("APP_VER");
        appvertype.appendChild(document.createTextNode(Global.APP_VER));
        rootElement.appendChild(appvertype);

        Element opcode = document.createElement("OP_CODE");
        opcode.appendChild(document.createTextNode(Global.LOCATION_ADDRESS.replaceAll(",", "_") + "#" + Global.CURRENT_LOCATION_SET.replace(",", "_")));
        rootElement.appendChild(opcode);

        Element ts = document.createElement("ts");
        ts.appendChild(document.createTextNode(Global.R_PID_TIME));
        rootElement.appendChild(ts);

        Element ra = document.createElement("ra");
        ra.appendChild(document.createTextNode(deviceType));
        rootElement.appendChild(ra);

        Element uidElement = document.createElement("UID");
        uidElement.appendChild(document.createTextNode(Global.AUTH_AADHAAR));
        rootElement.appendChild(uidElement);

        Element radElement = document.createElement("Rad");
        radElement.appendChild(document.createTextNode(encodedXml));
        rootElement.appendChild(radElement);

        Element atypeElement = document.createElement("AuthType");
        atypeElement.appendChild(document.createTextNode(authtpe));
        rootElement.appendChild(atypeElement);

        Element imeiElement = document.createElement("imei");
        imeiElement.appendChild(document.createTextNode(Global.DEVICE_IMEI_NO));
        rootElement.appendChild(imeiElement);

        Element ntypeElement = document.createElement("NETTYPE");
        ntypeElement.appendChild(document.createTextNode(Global.NETWORK_TYPE));
        rootElement.appendChild(ntypeElement);

        Element nnameElement = document.createElement("NETNAME");
        if (Global.connectionType.equalsIgnoreCase("Wifi"))
            nnameElement.appendChild(document.createTextNode(Global.NETWORK_NAME));
        else
            nnameElement.appendChild(document.createTextNode(Global.OPERATOR_NAME + "#" + Global.OPERATOR_NAME_SET));
        rootElement.appendChild(nnameElement);

        //Cell information

        Element latElement = document.createElement("LAT");
        latElement.appendChild(document.createTextNode(String.valueOf(Global.latitude)));
        rootElement.appendChild(latElement);

        Element longElement = document.createElement("LONGIT");
        longElement.appendChild(document.createTextNode(String.valueOf(Global.longitude)));
        rootElement.appendChild(longElement);

        Element sigstrenElement = document.createElement("SIGSTRENGTH");
        sigstrenElement.appendChild(document.createTextNode(String.valueOf(Global.SIGNAL_STRENGTH)));
        rootElement.appendChild(sigstrenElement);

        Element cidElement = document.createElement("CID");
        cidElement.appendChild(document.createTextNode(String.valueOf(Global.cid)));
        rootElement.appendChild(cidElement);

        Element lacElement = document.createElement("LAC");
        lacElement.appendChild(document.createTextNode(String.valueOf(Global.lac)));
        rootElement.appendChild(lacElement);

        Element mncElement = document.createElement("MNC");
        mncElement.appendChild(document.createTextNode(Global.mnc));
        rootElement.appendChild(mncElement);

        Element mccElement = document.createElement("MCC");
        mccElement.appendChild(document.createTextNode(Global.mcc));
        rootElement.appendChild(mccElement);

        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = null;
        try {
            transformer = factory.newTransformer();
        } catch (TransformerConfigurationException e1) {
            e1.printStackTrace();
        }
        DOMSource domSource = new DOMSource(document);
        StringWriter writer = new StringWriter();
        //			OutputStream output = new ByteArrayOutputStream();
        StreamResult result = new StreamResult(writer);
        try {
            transformer.transform(domSource, result);
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        kycXml = writer.getBuffer().toString();
        //		Log.e("kyc xml", " = "+kycXml);

        return kycXml;

    }

    public String createXmlForDemoAuthNew(String aadhaarNo, boolean pi, boolean pa, boolean pfa, String name, String dob, String gender) {
        Log.e("aadhaarNo", "==2" + aadhaarNo);
        DateFormat dfm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        Calendar localCalendar = GregorianCalendar
                .getInstance();
        localCalendar.setTime(date);
        String pidTimeStamp = String.valueOf(localCalendar
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
        //Global.DEVICE_IMEI_NO = imeiNo;
        // Log.e("imei", imeiNo);
        //Log.e("global_imei", Global.DEVICE_IMEI_NO);
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
        Document document = documentBuilder.newDocument();

        byte[] syncSessionKey = null;
        byte[] encryptedSessionKey = null;
        SessionKeyDetails sessionKeyDetails = null;

        try {
            UidaiAuthHelper.SynchronizedKey synchronizedKey = new UidaiAuthHelper.SynchronizedKey(
                    encrypter.generateSessionKey(), UUID.randomUUID()
                    .toString(), new Date());
            syncSessionKey = synchronizedKey.getSeedSkey();
            encryptedSessionKey = encrypter
                    .encryptUsingPublicKey(syncSessionKey);
            sessionKeyDetails = SessionKeyDetails
                    .createSkeyToInitializeSynchronizedKey(
                            synchronizedKey.getKeyIdentifier(),
                            encryptedSessionKey);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


        boolean otp_check = false;

		/*boolean pi , boolean pa , boolean pfa ,String pi_ms ,String pi_mv ,String pi_name
        , String pi_gender , String pi_dob , String pi_dobt , String pi_age,String pi_ph,String pi_mail,String pa_co,String pa_house,
		String pa_street,String pa_landmark,String pa_loc,String pa_vill ,String pa_subdist,String pa_dist,String pa_state , String pa_pc
		,String pa_po , String pfa_ms,String pfa_mv,String pfa_av*/

        byte[] xmlPidBytes = getPidXmlForDemoAuthNew(pi, pa, pfa, name, dob, gender).getBytes();
        // byte[] out = getPidXmlForDemoAuthNew(pi, pa, pfa, name, dob, gender).getBytes(StandardCharsets.UTF_8);
        /*String s1=new String(xmlPidBytes);
        Log.e("xmlpidbytes",s1);*/
        byte[] hmac = hashgenerator.generateSha256Hash(xmlPidBytes);
        String encryptedHmac = null;
        String encdata = null;


        try {

            String str = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                str = new String(xmlPidBytes, StandardCharsets.UTF_8);
            } else {
                //byte[] postData = getPidXmlForDemoAuthNew(pi, pa, pfa, name, dob, gender).getBytes(Charset.forName("UTF-8"));
                str = new String(xmlPidBytes, Charset.forName("UTF-8"));
            }

            System.out.print(str);
          /*  str = str.replaceAll("","");
            str = str.replaceAll("","");*/
            encdata = Base64.encodeToString(encrypter.encryptUsingSessionKey(
                    syncSessionKey, xmlPidBytes), Base64.DEFAULT);
            encryptedHmac = Base64.encodeToString(
                    encrypter.encryptUsingSessionKey(syncSessionKey, hmac),
                    Base64.DEFAULT);
        } catch (Exception ex) {

            System.out.println("Error:" + ex.getMessage());
        }
        //encdata=getPidXmlForDemoAuth(pi, pa, pfa, name, dob, gender);

        // String txn = "UKC:" + aadhaarNo +pidTimeStamp.replace("T", "").replace("-", "").replace(":", "");
        String txn = "NIC" + aadhaarNo + pidTimeStamp.replace("T", "").replace("-", "").replace(":", "") + "NHPS";
        Log.e("txn", txn);
        Element mainElement = document.createElement("AuthBioData");
        document.appendChild(mainElement);
        Element rootElement = document.createElement("Auth");
        mainElement.appendChild(rootElement);
        Attr rootUid = document.createAttribute("uid");
        rootUid.setValue(aadhaarNo);
        Attr rootTid = document.createAttribute("tid");
        rootTid.setValue("public");
        Attr rootAc = document.createAttribute("ac");
        //rootAc.setValue("0000650000");
        rootAc.setValue("00000000");
        Attr rootSa = document.createAttribute("sa");
        rootSa.setValue("");
        Attr rootLk = document.createAttribute("lk");
        rootLk.setValue("");
        Attr rootTxn = document.createAttribute("txn");
        rootTxn.setValue(txn);
        Attr rootVer = document.createAttribute("ver");
        //rootVer.setValue(String.valueOf(Global.AUTH_version));
        rootVer.setValue(Global.XML_VERSION);

        Attr rootXmlns = document.createAttribute("xmlns");
        rootXmlns.setValue("http://www.uidai.gov.in/authentication/uid-auth-request/1.0");


        rootElement.setAttributeNode(rootTid);
        rootElement.setAttributeNode(rootAc);
        rootElement.setAttributeNode(rootSa);
        rootElement.setAttributeNode(rootLk);
        rootElement.setAttributeNode(rootTxn);
        rootElement.setAttributeNode(rootVer);
        rootElement.setAttributeNode(rootXmlns);
        rootElement.setAttributeNode(rootUid);

        //		<Uses bio="y"  bt="'.$data['bt'].'" pi="n" pa="n" pfa="n" pin="n" otp="n" />

        Element usesElement = document.createElement("Uses");

        Attr usesBio = document.createAttribute("bio");

        usesBio.setValue("n");

        Attr usesBt = document.createAttribute("bt");


        Attr usesPi = document.createAttribute("pi");
        if (pi) {
            usesPi.setValue("y");
        } else {
            usesPi.setValue("n");
        }

        Attr usesPa = document.createAttribute("pa");
        if (pa) {
            usesPa.setValue("y");
        } else {
            usesPa.setValue("n");
        }

        Attr usesPfa = document.createAttribute("pfa");
        if (pfa) {
            usesPfa.setValue("y");

        } else {

            usesPfa.setValue("n");
        }
        Attr usesPin = document.createAttribute("pin");
        usesPin.setValue("n");
        Attr usesOtp = document.createAttribute("otp");
        if (otp_check) {
            usesOtp.setValue("y");
        } else {
            usesOtp.setValue("n");
        }

        usesElement.setAttributeNode(usesBio);
        /*if (deviceType.equalsIgnoreCase("F") | deviceType.equalsIgnoreCase("I")) {
            usesElement.setAttributeNode(usesBt);
		}*/
        usesElement.setAttributeNode(usesPi);
        usesElement.setAttributeNode(usesPa);
        usesElement.setAttributeNode(usesPfa);
        usesElement.setAttributeNode(usesPin);
        usesElement.setAttributeNode(usesOtp);

        rootElement.appendChild(usesElement);

        //		<Meta udc="AIIMSTEST" fdc="'.$data['fdc'].'" idc="'.$data['idc'].'" pip="127.0.0.1" lot="P" lov="110092" />

        Element metaElement = document.createElement("Meta");

        Attr metaUdc = document.createAttribute("udc");
        //metaUdc.setValue("");
        metaUdc.setValue("public");

        Attr metaFdc = document.createAttribute("fdc");
        //		if ((deviceType.equalsIgnoreCase("F"))) {
        //			metaFdc.setValue("NA");
        //		}else {
        //			metaFdc.setValue("NC");
        //		}
        metaFdc.setValue("NC");

        Attr metaIdc = document.createAttribute("idc");

        metaIdc.setValue("NA");

        Attr metaPip = document.createAttribute("pip");
        metaPip.setValue("");
        Attr metaLot = document.createAttribute("lot");
        metaLot.setValue("P");
        Attr metaLov = document.createAttribute("lov");
        metaLov.setValue("");

        metaElement.setAttributeNode(metaUdc);
        metaElement.setAttributeNode(metaFdc);
        metaElement.setAttributeNode(metaIdc);
        metaElement.setAttributeNode(metaPip);
        metaElement.setAttributeNode(metaLot);
        metaElement.setAttributeNode(metaLov);

        rootElement.appendChild(metaElement);

        Element skeyElement = document.createElement("Skey");

        Attr skeyCi = document.createAttribute("ci");
        skeyCi.setValue(encrypter.getCertificateIdentifier());

        skeyElement.setAttributeNode(skeyCi);
        String str = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            str = new String(sessionKeyDetails.getSkeyValue(), StandardCharsets.UTF_8);
        } else {
            str = new String(sessionKeyDetails.getSkeyValue(), Charset.forName("UTF-8"));
        }
        System.out.print(str);
        skeyElement.appendChild(document.createTextNode(Base64.encodeToString(
                sessionKeyDetails.getSkeyValue(), Base64.DEFAULT)));

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

        Element elementUserData = document.createElement("UserData");
        Attr imeiNo = document.createAttribute("imeiNo");
        imeiNo.setValue(Global.imei);
        //Log.e("imei no", Global.imei);
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
        //   mainElement.appendChild(rootElement);

        Element signatureElement = document.createElement("Signature");
        signatureElement.appendChild(document.createTextNode(""));
        //		rootElement.appendChild(signatureElement);


        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = null;
        try {
            transformer = factory.newTransformer();
        } catch (TransformerConfigurationException e1) {
            e1.printStackTrace();
        }
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
        Log.e("kyc auth xml", "==" + KycAuthXml);
        /*KycAuthXml =  KycAuthXml.replaceAll("&lt;", "<");
       KycAuthXml = KycAuthXml.replaceAll("&gt;",">");*/
        appendLogInvalidXmlAWithoutRad(KycAuthXml, "NHPS-demoAuth createXmlForDemoAuthNew");
        return KycAuthXml;

    }

    public String getPidXmlForDemoAuthNew(boolean pi, boolean pa, boolean pfa, String name, String dob, String gender) {

        DateFormat dfm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        Calendar localCalendar = GregorianCalendar
                .getInstance();
        localCalendar.setTime(date);
        String pidTimeStamp = String.valueOf(localCalendar
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
        //if (!Global.FLAG) {
        Global.R_PID_TIME = ctime;
        // }
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
            attr.setValue(String.valueOf(Global.version));
            rootElement.setAttributeNode(attr);

            Element demo = doc.createElement("Demo");
            rootElement.appendChild(demo);

            attr = doc.createAttribute("lang");
            attr.setValue("06");
//			demo.setAttributeNode(attr);


            if (pi) {


                Element piNode = doc.createElement("Pi");
                demo.appendChild(piNode);

                attr = doc.createAttribute("name");
                attr.setValue(name);
                if (!name.equalsIgnoreCase("")) {
                    piNode.setAttributeNode(attr);
                }

				/*attr = doc.createAttribute("mv");
				attr.setValue(pi_mv);
				if (!pi_mv.equalsIgnoreCase("")) {
					piNode.setAttributeNode(attr);
				}

				attr = doc.createAttribute("ms");
				attr.setValue(pi_ms);
				if (!pi_ms.equalsIgnoreCase("")) {
					piNode.setAttributeNode(attr);
				}*/

				/*attr = doc.createAttribute("lmv");
			attr.setValue("");
			piNode.setAttributeNode(attr);*/

				/*attr = doc.createAttribute("lname");
			attr.setValue("");
			piNode.setAttributeNode(attr);*/

                attr = doc.createAttribute("gender");
                attr.setValue(gender);
                if (!gender.equalsIgnoreCase("")) {
                    piNode.setAttributeNode(attr);
                }

                attr = doc.createAttribute("dob");
                attr.setValue(dob);
                if (!dob.equalsIgnoreCase("")) {
                    piNode.setAttributeNode(attr);
                }

				/*attr = doc.createAttribute("dobt");
				attr.setValue(pi_dobt);
				if (!pi_dobt.equalsIgnoreCase("")) {
					piNode.setAttributeNode(attr);
				}

				attr = doc.createAttribute("age");
				attr.setValue(pi_age);
				if (!pi_age.equalsIgnoreCase("")) {
					piNode.setAttributeNode(attr);
				}

				attr = doc.createAttribute("phone");
				attr.setValue(pi_ph);
				if (!pi_ph.equalsIgnoreCase("")) {
					piNode.setAttributeNode(attr);
				}

				attr = doc.createAttribute("email");
				attr.setValue(pi_mail);
				if (!pi_mail.equalsIgnoreCase("")) {
					piNode.setAttributeNode(attr);
				}
*/
            }


			/*if (pa) {


				Element paNode = doc.createElement("Pa");
				demo.appendChild(paNode);

				attr = doc.createAttribute("ms");
				attr.setValue("E");
				paNode.setAttributeNode(attr);

				attr = doc.createAttribute("co");
				attr.setValue(pa_co);
				if (!pa_co.equalsIgnoreCase("")) {
					paNode.setAttributeNode(attr);
				}

				attr = doc.createAttribute("house");
				attr.setValue(pa_house);
				if (!pa_house.equalsIgnoreCase("")) {
					paNode.setAttributeNode(attr);
				}

				attr = doc.createAttribute("street");
				attr.setValue(pa_street);
				if (!pa_street.equalsIgnoreCase("")) {
					paNode.setAttributeNode(attr);
				}

				attr = doc.createAttribute("lm");
				attr.setValue(pa_landmark);
				if (!pa_landmark.equalsIgnoreCase("")) {
					paNode.setAttributeNode(attr);
				}

				attr = doc.createAttribute("loc");
				attr.setValue(pa_loc);
				if (!pa_loc.equalsIgnoreCase("")) {
					paNode.setAttributeNode(attr);
				}

				attr = doc.createAttribute("vtc");
				attr.setValue(pa_vill);
				if (!pa_vill.equalsIgnoreCase("")) {
					paNode.setAttributeNode(attr);
				}

				attr = doc.createAttribute("subdist");
				attr.setValue(pa_subdist);
				if (!pa_subdist.equalsIgnoreCase("")) {
					paNode.setAttributeNode(attr);
				}

				attr = doc.createAttribute("dist");
				attr.setValue(pa_dist);
				if (!pa_dist.equalsIgnoreCase("")) {
					paNode.setAttributeNode(attr);
				}

				attr = doc.createAttribute("state");
				attr.setValue(pa_state);
				if (!pa_state.equalsIgnoreCase("")) {
					paNode.setAttributeNode(attr);
				}

				attr = doc.createAttribute("pc");
				attr.setValue(pa_pc);
				if (!pa_pc.equalsIgnoreCase("")) {
					paNode.setAttributeNode(attr);
				}

				attr = doc.createAttribute("po");
				attr.setValue(pa_po);
				if (!pa_po.equalsIgnoreCase("")) {
					paNode.setAttributeNode(attr);
				}

			}

			if (pfa) {

				Element pfaNode = doc.createElement("Pfa");
				demo.appendChild(pfaNode);

				attr = doc.createAttribute("ms");
				attr.setValue(pfa_ms);
				if (!pfa_ms.equalsIgnoreCase("")) {
					pfaNode.setAttributeNode(attr);
				}

				attr = doc.createAttribute("mv");
				attr.setValue(pfa_mv);
				if (!pfa_mv.equalsIgnoreCase("")) {
					pfaNode.setAttributeNode(attr);
				}

				attr = doc.createAttribute("av");
				attr.setValue(pfa_av);
				if (!pfa_av.equalsIgnoreCase("")) {
					pfaNode.setAttributeNode(attr);
				}

			}*/


            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory
                    .newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            // doc.setXmlStandalone(true);
            DOMSource source = new DOMSource(doc);

            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);
      /*      transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "no");*/
            transformer.transform(source, result);
            String output = writer.getBuffer().toString()
                    .replaceAll("\n|\r", "");
            Log.e("PID XML", "=" + output);
            //appendLog(output);
            output = output.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "");
            appendLogInvalidXmlAWithoutRad(output, "NHPS-demoAuth getPixXmlForDemoAuthNew");
            return output;

        } catch (Exception ex) {
            System.out.println("Error:" + ex.getMessage());
        }
        return null;
    }

    public void appendLogInvalidXmlAWithoutRad(String text, String loc) {
        //File logFile = new File("sdcard/BIO_KYC"+ctime+".txt");
        File logFile = new File("sdcard/" + loc + ".txt");
        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        try {
            //BufferedWriter for performance, true to set append to file flag
            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, false));
            buf.append(text);
            buf.newLine();
            buf.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            Log.e("IOException", "appendlog***** " + e.getMessage());
            e.printStackTrace();
        }
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


    public String createXmlForOtpAuth(String aadhaarNo, boolean otp_check, String otp) {

        DateFormat dfm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        Calendar localCalendar = GregorianCalendar
                .getInstance();
        localCalendar.setTime(date);
        String pidTimeStamp = String.valueOf(localCalendar
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

        try {
            UidaiAuthHelper.SynchronizedKey synchronizedKey = new UidaiAuthHelper.SynchronizedKey(
                    encrypter.generateSessionKey(), UUID.randomUUID()
                    .toString(), new Date());
            syncSessionKey = synchronizedKey.getSeedSkey();
            encryptedSessionKey = encrypter
                    .encryptUsingPublicKey(syncSessionKey);
            sessionKeyDetails = SessionKeyDetails
                    .createSkeyToInitializeSynchronizedKey(
                            synchronizedKey.getKeyIdentifier(),
                            encryptedSessionKey);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        boolean bio, pi;

        bio = false;
        pi = false;
        otp_check = true;

        byte[] xmlPidBytes = getPidOtpXml("", bio, pi, null, otp_check, otp).getBytes();
        byte[] hmac = hashgenerator.generateSha256Hash(xmlPidBytes);
        String encryptedHmac = null;
        String encdata = null;

        try {
            encdata = Base64.encodeToString(encrypter.encryptUsingSessionKey(
                    syncSessionKey, xmlPidBytes), Base64.DEFAULT);
            encryptedHmac = Base64.encodeToString(
                    encrypter.encryptUsingSessionKey(syncSessionKey, hmac),
                    Base64.DEFAULT);
        } catch (Exception ex) {
            System.out.println("Error:" + ex.getMessage());
        }

        String txn = "NIC" + aadhaarNo + pidTimeStamp.replace("T", "").replace("-", "").replace(":", "");
        Element mainElement = document.createElement("AuthBioData");
        document.appendChild(mainElement);

        Element rootElement = document.createElement("Auth");
        mainElement.appendChild(rootElement);
        Attr rootUid = document.createAttribute("uid");
        rootUid.setValue(aadhaarNo);
        Attr rootTid = document.createAttribute("tid");
        rootTid.setValue("public");
        Attr rootAc = document.createAttribute("ac");
        //rootAc.setValue("0000650000");
        rootAc.setValue("000000");
        Attr rootSa = document.createAttribute("sa");
        rootSa.setValue("");
        Attr rootLk = document.createAttribute("lk");
        rootLk.setValue("");
        Attr rootTxn = document.createAttribute("txn");
        rootTxn.setValue(txn);
        Attr rootVer = document.createAttribute("ver");
        //rootVer.setValue(String.valueOf(Global.AUTH_version));
        rootVer.setValue("1.6");

        Attr rootXmlns = document.createAttribute("xmlns");
        rootXmlns.setValue("http://www.uidai.gov.in/authentication/uid-auth-request/1.0");


        rootElement.setAttributeNode(rootTid);
        rootElement.setAttributeNode(rootAc);
        rootElement.setAttributeNode(rootSa);
        rootElement.setAttributeNode(rootLk);
        rootElement.setAttributeNode(rootTxn);
        rootElement.setAttributeNode(rootVer);
        rootElement.setAttributeNode(rootXmlns);
        rootElement.setAttributeNode(rootUid);
        Element usesElement = document.createElement("Uses");

        Attr usesBio = document.createAttribute("bio");
        if (bio) {
            usesBio.setValue("y");
        } else {
            usesBio.setValue("n");
        }

        Attr usesBt = document.createAttribute("bt");
        Attr usesPi = document.createAttribute("pi");
        if (pi) {
            usesPi.setValue("y");
        } else {
            usesPi.setValue("n");
        }

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
        usesElement.setAttributeNode(usesPi);
        usesElement.setAttributeNode(usesPa);
        usesElement.setAttributeNode(usesPfa);
        usesElement.setAttributeNode(usesPin);
        usesElement.setAttributeNode(usesOtp);

        rootElement.appendChild(usesElement);
        Element metaElement = document.createElement("Meta");

        Attr metaUdc = document.createAttribute("udc");
        metaUdc.setValue("public");

        Attr metaFdc = document.createAttribute("fdc");
        metaFdc.setValue("NA");

        Attr metaIdc = document.createAttribute("idc");

        metaIdc.setValue("NA");

        Attr metaPip = document.createAttribute("pip");
        metaPip.setValue("10.249.34.242");
        Attr metaLot = document.createAttribute("lot");
        metaLot.setValue("P");
        Attr metaLov = document.createAttribute("lov");
        metaLov.setValue("110011");

        metaElement.setAttributeNode(metaUdc);
        metaElement.setAttributeNode(metaFdc);
        metaElement.setAttributeNode(metaIdc);
        metaElement.setAttributeNode(metaPip);
        metaElement.setAttributeNode(metaLot);
        metaElement.setAttributeNode(metaLov);

        rootElement.appendChild(metaElement);
        Element skeyElement = document.createElement("Skey");

        Attr skeyCi = document.createAttribute("ci");
        skeyCi.setValue(encrypter.getCertificateIdentifier());

        skeyElement.setAttributeNode(skeyCi);
        skeyElement.appendChild(document.createTextNode(Base64.encodeToString(
                sessionKeyDetails.getSkeyValue(), Base64.DEFAULT)));

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

        Element signatureElement = document.createElement("Signature");
        signatureElement.appendChild(document.createTextNode(""));
        //		rootElement.appendChild(signatureElement);
        Element elementUserData = document.createElement("UserData");
        Attr imeiNo = document.createAttribute("imeiNo");
        imeiNo.setValue(Global.imei);
        //Log.e("imei no", Global.imei);
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

        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = null;
        try {
            transformer = factory.newTransformer();
        } catch (TransformerConfigurationException e1) {
            e1.printStackTrace();
        }
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


    private String getPidOtpXml(String uname, boolean bio, boolean pi,
                                String isotemplate, boolean otp_check, String otp) {
        DateFormat dfm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        Calendar localCalendar = GregorianCalendar
                .getInstance();
        localCalendar.setTime(date);
        String pidTimeStamp = String.valueOf(localCalendar
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
            attr.setValue(String.valueOf(Global.version));
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
                // String fpdata= Base64.encodeToString(isotemplate,
                // Base64.DEFAULT);
                Log.e("isotemplate", "==" + isotemplate);
                String fpdata = isotemplate;
                Element bios = doc.createElement("Bios");
                rootElement.appendChild(bios);
                Element bioElement = doc.createElement("Bio");
                attr = doc.createAttribute("type");
             /*   if (bioDeviceType.equalsIgnoreCase("F")) {
                    attr.setValue("FMR");
                }else if (bioDeviceType.equalsIgnoreCase("I")) {
                    attr.setValue("IIR");
                }*/

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


    public String createXmlForRequestOtpAuth(String aadhaar) {

        long now = System.currentTimeMillis();
        Date date = new Date(now);
        Calendar localCalendar = GregorianCalendar
                .getInstance();
        localCalendar.setTime(date);
        String pidTimeStamp = String.valueOf(localCalendar
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
        document.setXmlStandalone(true);
        String txn = "NIC" + aadhaar + pidTimeStamp.replace("T", "").replace("-", "").replace(":", "");
        Element mainElement = document.createElement("OtpData");
        document.appendChild(mainElement);
        Element rootElement = document.createElement("Otp");
        mainElement.appendChild(rootElement);

        Attr rootUid = document.createAttribute("uid");
        rootUid.setValue(aadhaar);
        Attr rootTid = document.createAttribute("tid");
        rootTid.setValue("public");
        Attr rootAc = document.createAttribute("ac");
        rootAc.setValue("000000");
        Attr rootSa = document.createAttribute("sa");
        rootSa.setValue("");
        Attr rootTxn = document.createAttribute("txn");
        rootTxn.setValue(txn);
        Attr rootLk = document.createAttribute("lk");
        rootLk.setValue("");
        Attr rootVer = document.createAttribute("ver");
        rootVer.setValue("1.6");
        Attr rootType = document.createAttribute("type");
        rootType.setValue("A");
        Attr rootTs = document.createAttribute("ts");
        rootTs.setValue(Global.PID_TIME_STAMP);

        Attr rootXmlns = document.createAttribute("xmlns");
        rootXmlns.setValue("http://www.uidai.gov.in/authentication/uid-auth-request/1.0");
        rootElement.setAttributeNode(rootTid);
        rootElement.setAttributeNode(rootAc);
        rootElement.setAttributeNode(rootSa);
        rootElement.setAttributeNode(rootLk);
        rootElement.setAttributeNode(rootTxn);
        rootElement.setAttributeNode(rootVer);
        rootElement.setAttributeNode(rootType);
        rootElement.setAttributeNode(rootXmlns);
        rootElement.setAttributeNode(rootTs);
        rootElement.setAttributeNode(rootUid);

        Element usesElement = document.createElement("Opts");

        Attr ch = document.createAttribute("ch");
        ch.setValue("00");
        usesElement.setAttributeNode(ch);
        rootElement.appendChild(usesElement);

        Element elementUserData = document.createElement("UserData");
        Attr imeiNo = document.createAttribute("imeiNo");
        imeiNo.setValue(Global.imei);
        //Log.e("imei no", Global.imei);
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

        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = null;
        try {
            transformer = factory.newTransformer();
        } catch (TransformerConfigurationException e1) {
            e1.printStackTrace();
        }
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

    public String createXmlForKycAuthNew(String deviceType, String aadhaarNo, String fpImgString, boolean otp_check, String otp) {
        // Saurabh Bhandari
        bioDeviceType = deviceType;
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

        try {
            synchronizedKey = new SynchronizedKey(
                    encrypter.generateSessionKey(), UUID.randomUUID()
                    .toString(), new Date());
            syncSessionKey = synchronizedKey.getSeedSkey();
            encryptedSessionKey = encrypter
                    .encryptUsingPublicKey(syncSessionKey);
            sessionKeyDetails = SessionKeyDetails
                    .createSkeyToInitializeSynchronizedKey(
                            synchronizedKey.getKeyIdentifier(),
                            encryptedSessionKey);

        } catch (Exception e) {
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
        byte[] hmac = hashgenerator.generateSha256Hash(xmlPidBytes);
        String encryptedHmac = null;
        String encdata = null;

        try {
            encdata = Base64.encodeToString(encrypter.encryptUsingSessionKey(
                    syncSessionKey, xmlPidBytes), Base64.DEFAULT);
            encryptedHmac = Base64.encodeToString(
                    encrypter.encryptUsingSessionKey(syncSessionKey, hmac),
                    Base64.DEFAULT);
        } catch (Exception ex) {
            System.out.println("Error:" + ex.getMessage());
        }

      /*  String txn = "UKC:" + aadhaarNo + pidTimeStamp.replace("T", "").replace("-", "").replace(":", "");*/
        String txn = "NIC:" + aadhaarNo + pidTimeStamp.replace("T", "").replace("-", "").replace(":", "") + "NHPS";

        Element mainElement = document.createElement("AuthData");
        document.appendChild(mainElement);
        Element rootElement = document.createElement("Auth");
        mainElement.appendChild(rootElement);

        Attr rootUid = document.createAttribute("uid");
        rootUid.setValue(aadhaarNo);
        Attr rootTid = document.createAttribute("tid");
        rootTid.setValue("public");
        Attr rootAc = document.createAttribute("ac");
        //rootAc.setValue("0000650000");
        rootAc.setValue("00000000");
        Attr rootSa = document.createAttribute("sa");
        rootSa.setValue("");
        Attr rootLk = document.createAttribute("lk");
        rootLk.setValue("");
        Attr rootTxn = document.createAttribute("txn");
        rootTxn.setValue(txn);
        Attr rootVer = document.createAttribute("ver");
        //rootVer.setValue(String.valueOf(Global.AUTH_version));
        rootVer.setValue("1.6");

        Attr rootXmlns = document.createAttribute("xmlns");
        rootXmlns.setValue("http://www.uidai.gov.in/authentication/uid-auth-request/1.0");


        rootElement.setAttributeNode(rootTid);
        rootElement.setAttributeNode(rootAc);
        rootElement.setAttributeNode(rootSa);
        rootElement.setAttributeNode(rootLk);
        rootElement.setAttributeNode(rootTxn);
        rootElement.setAttributeNode(rootVer);
        rootElement.setAttributeNode(rootXmlns);
        rootElement.setAttributeNode(rootUid);

        //		<Uses bio="y"  bt="'.$data['bt'].'" pi="n" pa="n" pfa="n" pin="n" otp="n" />

        Element usesElement = document.createElement("Uses");

        Attr usesBio = document.createAttribute("bio");
        if (bio) {
            usesBio.setValue("y");
        } else {
            usesBio.setValue("n");
        }

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

        rootElement.appendChild(usesElement);

        //		<Meta udc="AIIMSTEST" fdc="'.$data['fdc'].'" idc="'.$data['idc'].'" pip="127.0.0.1" lot="P" lov="110092" />

        Element metaElement = document.createElement("Meta");

        Attr metaUdc = document.createAttribute("udc");
        // metaUdc.setValue(Global.connectedDeviceNameId);

        metaUdc.setValue("UIDAIADGYASH");

        Attr metaFdc = document.createAttribute("fdc");
        if ((deviceType.equalsIgnoreCase("F"))) {
            metaFdc.setValue("NC");
        } else {
            metaFdc.setValue("NA");
        }
        //  metaFdc.setValue("NC");

        Attr metaIdc = document.createAttribute("idc");
        if ((deviceType.equalsIgnoreCase("I"))) {
            metaIdc.setValue("NC");
        } else {
            metaIdc.setValue("NA");
        }

        Attr metaPip = document.createAttribute("pip");
        metaPip.setValue("10.249.34.242");
        Attr metaLot = document.createAttribute("lot");
        metaLot.setValue("P");
        Attr metaLov = document.createAttribute("lov");
        metaLov.setValue("110011");

        metaElement.setAttributeNode(metaUdc);
        metaElement.setAttributeNode(metaFdc);
        metaElement.setAttributeNode(metaIdc);
        metaElement.setAttributeNode(metaPip);
        metaElement.setAttributeNode(metaLot);
        metaElement.setAttributeNode(metaLov);

        rootElement.appendChild(metaElement);

        //		<Skey ci="'.$data['ci'].'">';
        //				$authXml .= $data['skey'];
        //				$authXml .= '</Skey>

        Element skeyElement = document.createElement("Skey");

        Attr skeyCi = document.createAttribute("ci");
        skeyCi.setValue(encrypter.getCertificateIdentifier());

        skeyElement.setAttributeNode(skeyCi);
        skeyElement.appendChild(document.createTextNode(Base64.encodeToString(
                sessionKeyDetails.getSkeyValue(), Base64.DEFAULT)));

        rootElement.appendChild(skeyElement);
        //		<Data type="X">';
        //				$authXml .= $data['pid_data'];
        //				$authXml .= '</Data>

        Element dataElement = document.createElement("Data");

        Attr dataType = document.createAttribute("type");
        dataType.setValue("X");
        dataElement.appendChild(document.createTextNode(encdata));
        dataElement.setAttributeNode(dataType);

        rootElement.appendChild(dataElement);
        //		<Hmac>';
        //				$authXml .= $data['hmac'];
        //				$authXml .= '</Hmac>

        Element hmcaElement = document.createElement("Hmac");
        hmcaElement.appendChild(document.createTextNode(encryptedHmac));

        rootElement.appendChild(hmcaElement);

        //Edited by saurabh

        Element elementUserData = document.createElement("UserData");
        Attr imeiNo = document.createAttribute("imeiNo");
        imeiNo.setValue(Global.imei);
        //Log.e("imei no", Global.imei);
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
        //		rootElement.appendChild(signatureElement);


        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = null;
        try {
            transformer = factory.newTransformer();
        } catch (TransformerConfigurationException e1) {
            e1.printStackTrace();
        }
        DOMSource domSource = new DOMSource(document);
        StringWriter writer = new StringWriter();
        //			OutputStream output = new ByteArrayOutputStream();
        StreamResult result = new StreamResult(writer);
        try {
            try {
                transformer.transform(domSource, result);
            } catch (TransformerException e) {
                e.printStackTrace();
            }
        } catch (Exception ex) {
            appendLogInvalidXmlAWithoutRad(ex.getCause().toString() + "\n\n\n" + ex.getMessage().toString(), "ErrorGeneratingXml");
        }
        KycAuthXml = writer.getBuffer().toString();
        Log.e("kyc auth ", "==" + KycAuthXml);

        return KycAuthXml;

    }


    public String createXmlForOtpAuthNew(String deviceType, String aadhaarNo, String fpImgString, boolean otp_check, String otp) {
        // modified by Saurabh Bhandari for Otp Auth
        bioDeviceType = deviceType;
        Log.e("deviceType", "==" + deviceType);
        Log.e("aadhaarNo", "==2" + aadhaarNo);
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        Calendar localCalendar = GregorianCalendar
                .getInstance();
        localCalendar.setTime(date);
        pidTimeStamp = String.valueOf(localCalendar.get(Calendar.YEAR))
                + "-" +
                (String.valueOf(localCalendar.get(Calendar.MONTH) + 1).length() < 2 ? "0" + String.valueOf(localCalendar.get(Calendar.MONTH) + 1) : String.valueOf(localCalendar.get(Calendar.MONTH) + 1))
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
            e.printStackTrace();
        }
        org.w3c.dom.Document document = documentBuilder.newDocument();
        byte[] syncSessionKey = null;
        byte[] encryptedSessionKey = null;
        SessionKeyDetails sessionKeyDetails = null;
        try {
            synchronizedKey = new SynchronizedKey(
                    encrypter.generateSessionKey(), UUID.randomUUID()
                    .toString(), new Date());
            syncSessionKey = synchronizedKey.getSeedSkey();
            encryptedSessionKey = encrypter
                    .encryptUsingPublicKey(syncSessionKey);
            sessionKeyDetails = SessionKeyDetails
                    .createSkeyToInitializeSynchronizedKey(
                            synchronizedKey.getKeyIdentifier(),
                            encryptedSessionKey);
        } catch (Exception e) {
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
        byte[] hmac = hashgenerator.generateSha256Hash(xmlPidBytes);
        String encryptedHmac = null;
        String encdata = null;
        try {
            encdata = Base64.encodeToString(encrypter.encryptUsingSessionKey(
                    syncSessionKey, xmlPidBytes), Base64.DEFAULT);
            encryptedHmac = Base64.encodeToString(
                    encrypter.encryptUsingSessionKey(syncSessionKey, hmac),
                    Base64.DEFAULT);
        } catch (Exception ex) {
            System.out.println("Error:" + ex.getMessage());
        }
        String txn = "NIC:" + aadhaarNo + pidTimeStamp.replace("T", "").replace("-", "").replace(":", "") + "NHPS";
        Element mainElement = document.createElement("AuthBioData");
        document.appendChild(mainElement);
        Element rootElement = document.createElement("Auth");
        mainElement.appendChild(rootElement);
        Attr rootUid = document.createAttribute("uid");
        rootUid.setValue(aadhaarNo);
        Attr rootTid = document.createAttribute("tid");
        rootTid.setValue("public");
        Attr rootAc = document.createAttribute("ac");
        rootAc.setValue("00000000");
        Attr rootSa = document.createAttribute("sa");
        rootSa.setValue("");
        Attr rootLk = document.createAttribute("lk");
        rootLk.setValue("");
        Attr rootTxn = document.createAttribute("txn");
        rootTxn.setValue(txn);
        Attr rootVer = document.createAttribute("ver");
        rootVer.setValue("1.6");
        Attr rootXmlns = document.createAttribute("xmlns");
        rootXmlns.setValue("http://www.uidai.gov.in/authentication/uid-auth-request/1.0");
        rootElement.setAttributeNode(rootTid);
        rootElement.setAttributeNode(rootAc);
        rootElement.setAttributeNode(rootSa);
        rootElement.setAttributeNode(rootLk);
        rootElement.setAttributeNode(rootTxn);
        rootElement.setAttributeNode(rootVer);
        rootElement.setAttributeNode(rootXmlns);
        rootElement.setAttributeNode(rootUid);
        Element usesElement = document.createElement("Uses");
        Attr usesBio = document.createAttribute("bio");
        if (bio) {
            usesBio.setValue("y");
        } else {
            usesBio.setValue("n");
        }
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
        rootElement.appendChild(usesElement);
        Element metaElement = document.createElement("Meta");
        Attr metaUdc = document.createAttribute("udc");
        metaUdc.setValue("UIDAIADGYASH");
        Attr metaFdc = document.createAttribute("fdc");
        if ((deviceType.equalsIgnoreCase("F"))) {
            metaFdc.setValue("NC");
        } else {
            metaFdc.setValue("NA");
        }
        Attr metaIdc = document.createAttribute("idc");
        if ((deviceType.equalsIgnoreCase("I"))) {
            metaIdc.setValue("NC");
        } else {
            metaIdc.setValue("NA");
        }
        Attr metaPip = document.createAttribute("pip");
        metaPip.setValue("10.249.34.242");
        Attr metaLot = document.createAttribute("lot");
        metaLot.setValue("P");
        Attr metaLov = document.createAttribute("lov");
        metaLov.setValue("110011");

        metaElement.setAttributeNode(metaUdc);
        metaElement.setAttributeNode(metaFdc);
        metaElement.setAttributeNode(metaIdc);
        metaElement.setAttributeNode(metaPip);
        metaElement.setAttributeNode(metaLot);
        metaElement.setAttributeNode(metaLov);
        rootElement.appendChild(metaElement);
        Element skeyElement = document.createElement("Skey");
        Attr skeyCi = document.createAttribute("ci");
        skeyCi.setValue(encrypter.getCertificateIdentifier());
        skeyElement.setAttributeNode(skeyCi);
        skeyElement.appendChild(document.createTextNode(Base64.encodeToString(
                sessionKeyDetails.getSkeyValue(), Base64.DEFAULT)));
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
        //Edited by saurabh
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
        //		rootElement.appendChild(signatureElement);


        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = null;
        try {
            transformer = factory.newTransformer();
        } catch (TransformerConfigurationException e1) {
            e1.printStackTrace();
        }
        DOMSource domSource = new DOMSource(document);
        StringWriter writer = new StringWriter();
        //OutputStream output = new ByteArrayOutputStream();
        StreamResult result = new StreamResult(writer);
        try {
            try {
                transformer.transform(domSource, result);
            } catch (TransformerException e) {
                e.printStackTrace();
            }
        } catch (Exception ex) {
            appendLogInvalidXmlAWithoutRad(ex.getCause().toString() + "\n\n\n" + ex.getMessage().toString(), "ErrorGeneratingXml");
        }
        KycAuthXml = writer.getBuffer().toString();
        Log.e("kyc auth ", "==" + KycAuthXml);

        return KycAuthXml;

    }


    public String createXmlForOtpKycNew(String deviceType, String aadhaarNo, String fpImgString, boolean otp_check, String otp) {
        // modified by Saurabh Bhandari for Otp Auth
        bioDeviceType = deviceType;
        Log.e("deviceType", "==" + deviceType);
        Log.e("aadhaarNo", "==2" + aadhaarNo);
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        Calendar localCalendar = GregorianCalendar
                .getInstance();
        localCalendar.setTime(date);
        pidTimeStamp = String.valueOf(localCalendar.get(Calendar.YEAR))
                + "-" +
                (String.valueOf(localCalendar.get(Calendar.MONTH) + 1).length() < 2 ? "0" + String.valueOf(localCalendar.get(Calendar.MONTH) + 1) : String.valueOf(localCalendar.get(Calendar.MONTH) + 1))
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
            e.printStackTrace();
        }
        org.w3c.dom.Document document = documentBuilder.newDocument();
        byte[] syncSessionKey = null;
        byte[] encryptedSessionKey = null;
        SessionKeyDetails sessionKeyDetails = null;
        try {
            synchronizedKey = new SynchronizedKey(
                    encrypter.generateSessionKey(), UUID.randomUUID()
                    .toString(), new Date());
            syncSessionKey = synchronizedKey.getSeedSkey();
            encryptedSessionKey = encrypter
                    .encryptUsingPublicKey(syncSessionKey);
            sessionKeyDetails = SessionKeyDetails
                    .createSkeyToInitializeSynchronizedKey(
                            synchronizedKey.getKeyIdentifier(),
                            encryptedSessionKey);
        } catch (Exception e) {
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
        byte[] hmac = hashgenerator.generateSha256Hash(xmlPidBytes);
        String encryptedHmac = null;
        String encdata = null;
        try {
            encdata = Base64.encodeToString(encrypter.encryptUsingSessionKey(
                    syncSessionKey, xmlPidBytes), Base64.DEFAULT);
            encryptedHmac = Base64.encodeToString(
                    encrypter.encryptUsingSessionKey(syncSessionKey, hmac),
                    Base64.DEFAULT);
        } catch (Exception ex) {
            System.out.println("Error:" + ex.getMessage());
        }
        //   String txn = "NIC:" + aadhaarNo + pidTimeStamp.replace("T", "").replace("-", "").replace(":", "") + "NHPS";
        String txn = "UKC:" + aadhaarNo + Global.PID_TIME_STAMP.replace("T", "").replace("-", "").replace(":", "");
        Element mainElement = document.createElement("AuthData");
        document.appendChild(mainElement);
        Element rootElement = document.createElement("Auth");
        mainElement.appendChild(rootElement);
        Attr rootUid = document.createAttribute("uid");
        rootUid.setValue(aadhaarNo);
        Attr rootTid = document.createAttribute("tid");
        rootTid.setValue("public");
        Attr rootAc = document.createAttribute("ac");
        rootAc.setValue("00000000");
        Attr rootSa = document.createAttribute("sa");
        rootSa.setValue("");
        Attr rootLk = document.createAttribute("lk");
        rootLk.setValue("");
        Attr rootTxn = document.createAttribute("txn");
        rootTxn.setValue(txn);
        Attr rootVer = document.createAttribute("ver");
        rootVer.setValue("1.6");
        Attr rootTs = document.createAttribute("ts");
        rootTs.setValue(Global.R_PID_TIME);


        Attr rootXmlns = document.createAttribute("xmlns");
        rootXmlns.setValue("http://www.uidai.gov.in/authentication/uid-auth-request/1.0");
        rootElement.setAttributeNode(rootTid);
        rootElement.setAttributeNode(rootAc);
        rootElement.setAttributeNode(rootSa);
        rootElement.setAttributeNode(rootLk);
        rootElement.setAttributeNode(rootTxn);
        rootElement.setAttributeNode(rootVer);
        rootElement.setAttributeNode(rootTs);
        rootElement.setAttributeNode(rootXmlns);
        rootElement.setAttributeNode(rootUid);
        Element usesElement = document.createElement("Uses");
        Attr usesBio = document.createAttribute("bio");
        if (bio) {
            usesBio.setValue("y");
        } else {
            usesBio.setValue("n");
        }
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
        rootElement.appendChild(usesElement);
        Element metaElement = document.createElement("Meta");
        Attr metaUdc = document.createAttribute("udc");
        metaUdc.setValue("UIDAIADGYASH");
        Attr metaFdc = document.createAttribute("fdc");
        if ((deviceType.equalsIgnoreCase("F"))) {
            metaFdc.setValue("NC");
        } else {
            metaFdc.setValue("NA");
        }
        Attr metaIdc = document.createAttribute("idc");
        if ((deviceType.equalsIgnoreCase("I"))) {
            metaIdc.setValue("NC");
        } else {
            metaIdc.setValue("NA");
        }
        Attr metaPip = document.createAttribute("pip");
        metaPip.setValue("10.249.34.242");
        Attr metaLot = document.createAttribute("lot");
        metaLot.setValue("P");
        Attr metaLov = document.createAttribute("lov");
        metaLov.setValue("110011");

        metaElement.setAttributeNode(metaUdc);
        metaElement.setAttributeNode(metaFdc);
        metaElement.setAttributeNode(metaIdc);
        metaElement.setAttributeNode(metaPip);
        metaElement.setAttributeNode(metaLot);
        metaElement.setAttributeNode(metaLov);
        rootElement.appendChild(metaElement);
        Element skeyElement = document.createElement("Skey");
        Attr skeyCi = document.createAttribute("ci");
        skeyCi.setValue(encrypter.getCertificateIdentifier());
        skeyElement.setAttributeNode(skeyCi);
        skeyElement.appendChild(document.createTextNode(Base64.encodeToString(
                sessionKeyDetails.getSkeyValue(), Base64.DEFAULT)));
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
        //Edited by saurabh
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
        //		rootElement.appendChild(signatureElement);


        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = null;
        try {
            transformer = factory.newTransformer();
        } catch (TransformerConfigurationException e1) {
            e1.printStackTrace();
        }
        DOMSource domSource = new DOMSource(document);
        StringWriter writer = new StringWriter();
        //OutputStream output = new ByteArrayOutputStream();
        StreamResult result = new StreamResult(writer);
        try {
            try {
                transformer.transform(domSource, result);
            } catch (TransformerException e) {
                e.printStackTrace();
            }
        } catch (Exception ex) {
            appendLogInvalidXmlAWithoutRad(ex.getCause().toString() + "\n\n\n" + ex.getMessage().toString(), "ErrorGeneratingXml");
        }
        KycAuthXml = writer.getBuffer().toString();
        Log.e("kyc auth ", "==" + KycAuthXml);

        return KycAuthXml;

    }

}


class HashGenerator {
    public byte[] generateSha256Hash(byte[] message) {
        String algorithm = "SHA-256";
        String SECURITY_PROVIDER = "BC";

        byte[] hash = null;

        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance(algorithm, SECURITY_PROVIDER);
            digest.reset();
            hash = digest.digest(message);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return hash;
    }


}
