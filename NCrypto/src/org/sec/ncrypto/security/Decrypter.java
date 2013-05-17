package org.sec.ncrypto.security;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import android.util.Base64;
import android.util.Log;

public class Decrypter {
	private EncryptedMessage encryptedMessage;

	public PrivateKey privateKey;
	
	public Decrypter(EncryptedMessage encryptedMessage, PrivateKey privateKey) {
		this.encryptedMessage = encryptedMessage;
		this.privateKey = privateKey;
	}
	
	public String getMessage() {
		// We need to decrypt the key
		try {
			Cipher asymmetricCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			asymmetricCipher.init(Cipher.DECRYPT_MODE, privateKey);
			byte[] aesKey = asymmetricCipher.doFinal(
					Base64.decode(encryptedMessage.getEncryptedAesKey(), 
					Base64.DEFAULT));
			SecretKeySpec AesKeySpec = new SecretKeySpec(aesKey, "AES");
			IvParameterSpec iv = new IvParameterSpec(
					Base64.decode(encryptedMessage.getIv(), Base64.DEFAULT));
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, AesKeySpec, iv);
			
			byte[] plainBytes = cipher.doFinal(
					Base64.decode(encryptedMessage.getEncryptedMessage(), Base64.DEFAULT));
			return new String(plainBytes);
		}
		catch(Exception e) {
			Log.d("Decrypter", e.getMessage());
		}
		return null;
	}
	
	
}
