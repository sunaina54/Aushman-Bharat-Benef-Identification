package com.nhpm.AadhaarUtils;

import org.spongycastle.crypto.InvalidCipherTextException;
import org.spongycastle.crypto.engines.AESEngine;
import org.spongycastle.crypto.modes.GCMBlockCipher;
import org.spongycastle.crypto.paddings.PKCS7Padding;
import org.spongycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.spongycastle.crypto.params.AEADParameters;
import org.spongycastle.crypto.params.KeyParameter;
import org.spongycastle.jce.provider.BouncyCastleProvider;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.Security;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.TimeZone;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import static com.zebra.sdk.util.internal.StringUtilities.byteArrayToHexString;

public class Encrypter
{
	//	private static final String JCE_PROVIDER = "BC";
	private static final String JCE_PROVIDER = "SC";
	private static final String ASYMMETRIC_ALGO = "RSA/ECB/PKCS1Padding";
	private static final int SYMMETRIC_KEY_SIZE = 256;
	private static final String CERTIFICATE_TYPE = "X.509";
	private PublicKey publicKey;
	private Date certExpiryDate;
	// IV length - last 96 bits of ISO format timestamp
	public static final int IV_SIZE_BITS = 96;

	// Authentication tag length - in bits
	public static final int AUTH_TAG_SIZE_BITS = 128;

	// Additional authentication data - last 128 bits of ISO format timestamp
	public static final int AAD_SIZE_BITS = 128;

	X509Certificate cert;

	/**
	 * Hashing Algorithm Used for encryption and decryption
	 */
	private String algorithm = "SHA-256";

	/**
	 * SHA-256 Implementation provider
	 */
	private final static String SECURITY_PROVIDER = "BC";

	/**
	 * Default Size of the HMAC/Hash Value in bytes
	 */
	private int HMAC_SIZE = 32;

	static
	{
		BouncyCastleProvider prov = new BouncyCastleProvider();
		Security.addProvider(prov);
	}





	public Encrypter(byte[] inpubkey) {
		//FileInputStream fileInputStream = null;
		try {
			CertificateFactory certFactory = CertificateFactory.getInstance(CERTIFICATE_TYPE, JCE_PROVIDER);
			//fileInputStream = new FileInputStream(new File(publicKeyFileName));
			ByteArrayInputStream is = new ByteArrayInputStream(inpubkey);
			cert = (X509Certificate) certFactory.generateCertificate(is);
			is.close();
			publicKey = cert.getPublicKey();
			certExpiryDate = cert.getNotAfter();
		} catch (Exception ex)
		{
			ex.printStackTrace();
			//System.out.println("Error:" + ex.getMessage());
		}
	}

	/**
	 * Encrypts given data using a generated session and used ts as for all other needs.
	 * @param inputData - data to encrypt
	 * @param sessionKey  - Session key
	 * @param ts - timestamp as per the PID
	 * @return encrypted data
	 * @throws IllegalStateException
	 * @throws InvalidCipherTextException
	 * @throws Exception
	 */
	public byte[] encrypt(byte[] inputData, byte[] sessionKey, String ts) throws IllegalStateException, InvalidCipherTextException, Exception {
		byte[] iv = this.generateIv(ts);
		byte[] aad = this.generateAad(ts);
		byte[] cipherText = this.encryptDecryptUsingSessionKey(true, sessionKey, iv, aad, inputData);
		byte[] tsInBytes = ts.getBytes("UTF-8");
		byte [] packedCipherData = new byte[cipherText.length + tsInBytes.length];
		System.arraycopy(tsInBytes, 0, packedCipherData, 0, tsInBytes.length);
		System.arraycopy(cipherText, 0, packedCipherData, tsInBytes.length, cipherText.length);
		return packedCipherData;
	}

	/**
	 * Encrypts given data using session key, iv, aad
	 * @param cipherOperation - true for encrypt, false otherwise
	 * @param skey	- Session key
	 * @param iv  	- initialization vector or nonce
	 * @param aad 	- additional authenticated data
	 * @param data 	- data to encrypt
	 * @return encrypted data
	 * @throws IllegalStateException
	 * @throws InvalidCipherTextException
	 */
	public byte[] encryptDecryptUsingSessionKey(boolean cipherOperation, byte[] skey, byte[] iv, byte[] aad,
												byte[] data) throws IllegalStateException, InvalidCipherTextException {

		AEADParameters aeadParam = new AEADParameters(new KeyParameter(skey), AUTH_TAG_SIZE_BITS, iv, aad);
		GCMBlockCipher gcmb = new GCMBlockCipher(new AESEngine());

		gcmb.init(cipherOperation, aeadParam);
		int outputSize = gcmb.getOutputSize(data.length);
		byte[] result = new byte[outputSize];
		int processLen = gcmb.processBytes(data, 0, data.length, result, 0);
		gcmb.doFinal(result, processLen);

		return result;
	}

	/**
	 * Decrypts given input data using a sessionKey.
	 * @param inputData - data to decrypt
	 * @param sessionKey  - Session key
	 * @return decrypted data
	 * @throws IllegalStateException
	 * @throws InvalidCipherTextException
	 * @throws Exception
	 */
	public byte[] decrypt(byte[] inputData, byte[] sessionKey, byte[] encSrcHash) throws IllegalStateException, InvalidCipherTextException, Exception {
		byte[] bytesTs = Arrays.copyOfRange(inputData, 0, 19);
		String ts = new String(bytesTs);
		byte[] cipherData = Arrays.copyOfRange(inputData, bytesTs.length, inputData.length);
		byte[] iv = this.generateIv(ts);
		byte[] aad = this.generateAad(ts);
		byte[] plainText = this.encryptDecryptUsingSessionKey(false, sessionKey, iv, aad, cipherData);
		byte[] srcHash = this.encryptDecryptUsingSessionKey(false, sessionKey, iv, aad, encSrcHash);
		System.out.println("Decrypted HAsh in cipher text: "+byteArrayToHexString(srcHash));
		boolean result = this.validateHash(srcHash, plainText);
		if(!result){
			throw new Exception( "Integrity Validation Failed : " + "The original data at client side and the decrypted data at server side is not identical");
		} else{
			System.out.println("Hash Validation is Successful!!!!!");
			return plainText;
		}
	}


	/**
	 * Returns true / false value based on Hash comparison between source and generated
	 * @param srcHash
	 * @param plainTextWithTS
	 * @return hash value
	 * @throws Exception
	 */
	private boolean validateHash(byte[] srcHash, byte[] plainTextWithTS) throws Exception {
		byte[] actualHash = this.generateHash(plainTextWithTS);
		System.out.println("Hash of actual plain text in cipher hex:--->"+byteArrayToHexString(actualHash));
//		boolean tr =  Arrays.equals(srcHash, actualHash);
		if (new String(srcHash, "UTF-8").equals(new String(actualHash, "UTF-8"))) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Returns the 256 bit hash value of the message
	 *
	 * @param message
	 *            full plain text
	 *
	 * @return hash value
	 * @throws //HashingException
	 * @throws //HashingException
	 *             I/O errors
	 */
	public byte[] generateHash(byte[] message) throws Exception {
		byte[] hash = null;
		try {
			MessageDigest digest = MessageDigest.getInstance(algorithm, SECURITY_PROVIDER);
			digest.reset();
			HMAC_SIZE = digest.getDigestLength();
			hash = digest.digest(message);
		} catch (GeneralSecurityException e) {
			throw new Exception(
					"SHA-256 Hashing algorithm not available");
		}
		return hash;
	}




	public X509Certificate getCertificateChain() {
		return cert;
	}

	/**
	 * Creates a AES key that can be used as session key (skey)
	 *
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchProviderException
	 */
	public byte[] generateSessionKey() throws NoSuchAlgorithmException, NoSuchProviderException {
		KeyGenerator kgen = KeyGenerator.getInstance("AES", JCE_PROVIDER);
		kgen.init(SYMMETRIC_KEY_SIZE);
		SecretKey key = kgen.generateKey();
		byte[] symmKey = key.getEncoded();
		return symmKey;
	}

	/**
	 * Encrypts given data using UIDAI public key
	 *
	 * @param data Data to encrypt
	 * @return Encrypted data
	 * @throws IOException
	 * @throws GeneralSecurityException
	 */
	public byte[] encryptUsingPublicKey(byte[] data) throws IOException, GeneralSecurityException {
		// encrypt the session key with the public key
		Cipher pkCipher = Cipher.getInstance(ASYMMETRIC_ALGO, JCE_PROVIDER);
		pkCipher.init(Cipher.ENCRYPT_MODE, publicKey);
		byte[] encSessionKey = pkCipher.doFinal(data);
		return encSessionKey;
	}

	/**
	 * Encrypts given data using session key
	 *
	 * @param skey Session key
	 * @param data Data to encrypt
	 * @return Encrypted data
	 * @throws InvalidCipherTextException
	 */
	public byte[] encryptUsingSessionKey(byte[] skey, byte[] data) throws InvalidCipherTextException {
		PaddedBufferedBlockCipher cipher = new PaddedBufferedBlockCipher(new AESEngine(), new PKCS7Padding());
		String str = null;
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
			str = new String(data, StandardCharsets.UTF_8);
		}
		else
		{
			str=new String(data, Charset.forName("UTF-8"));
		}
		System.out.print(str);
		cipher.init(true, new KeyParameter(skey));

		int outputSize = cipher.getOutputSize(data.length);

		byte[] tempOP = new byte[outputSize];
		int processLen = cipher.processBytes(data, 0, data.length, tempOP, 0);
		int outputLen = cipher.doFinal(tempOP, processLen);

		byte[] result = new byte[processLen + outputLen];
		System.arraycopy(tempOP, 0, result, 0, result.length);

		return result;

	}


	public String getCertificateIdentifier()
	{
		String certificateIdentifier=null;
		//try {
			SimpleDateFormat ciDateFormat = new SimpleDateFormat("yyyyMMdd");
			ciDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
			certificateIdentifier = ciDateFormat.format(this.certExpiryDate);
		/*}catch (Exception e){

		}*/
		return certificateIdentifier;
	}


	/**
	 * Returns public certificate's expiry date. It can be used as certificate
	 * identifier
	 *
	 * @return Expiry date of UIDAI public certificate
	 */
	public Date getCertExpiryDate() {
		return certExpiryDate;
	}


	/**
	 * Generate IV using timestamp
	 * @param ts - timestamp string
	 * @return 12 bytes array
	 * @throws UnsupportedEncodingException
	 */
	public byte[] generateIv(String ts) throws UnsupportedEncodingException {
		return getLastBits(ts, IV_SIZE_BITS / 8);
	}

	/**
	 * Fetch specified last bits from String
	 * @param ts - timestamp string
	 * @param bits - no of bits to fetch
	 * @return byte array of specified length
	 * @throws UnsupportedEncodingException
	 */
	private byte[] getLastBits(String ts, int bits) throws UnsupportedEncodingException {
		byte[] tsInBytes = ts.getBytes("UTF-8");
		return Arrays.copyOfRange(tsInBytes, tsInBytes.length - bits, tsInBytes.length);
	}

	/**
	 * Generate AAD using timestamp
	 * @param ts - timestamp string
	 * @return 16 bytes array
	 * @throws UnsupportedEncodingException
	 */
	public byte[] generateAad(String ts) throws UnsupportedEncodingException {
		return getLastBits(ts, AAD_SIZE_BITS / 8);
	}

}
