package org.sec.ncrypto.security;

import java.security.AlgorithmParameters;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import org.sec.ncrypto.db.ContactDatabaseHelper;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

public class EncryptedMessage {
	private PublicKey rsaPublicKey;
	private SecretKey aesKey;
	private String destination;
	private String message;
	private String iv;
	private Context context;
	
	public EncryptedMessage(String destination, String message, Context context) {
		super();
		this.destination = destination;
		this.message = message;
		this.context = context;
		
		ContactDatabaseHelper helper = new ContactDatabaseHelper(context, "db", null, 0);
		byte[] publicKey = Base64.decode(helper.getPublicKey(destination), Base64.DEFAULT);
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(publicKey);
		try {
			KeyFactory factory = KeyFactory.getInstance("RSA", "BC");
			rsaPublicKey = factory.generatePublic(x509KeySpec);
		} catch (NoSuchAlgorithmException e1) {
			Log.e("org.sec.ncrypto", "No such algorithm");
		} catch (NoSuchProviderException e1) {
			Log.e("org.sec.ncrypto", "No such provider");
		} catch (InvalidKeySpecException e) {
			Log.e("org.sec.ncrypto", "Invalid key in base64!");
		}
		
		
		try {
			KeyGenerator keygen = KeyGenerator.getInstance("AES");
			keygen.init(256);
			aesKey = keygen.generateKey();
		} catch (NoSuchAlgorithmException e) {
			Log.d("org.sec.ncrypto","No AES provider!");
		}
		
		
	}
	
	public String getIv() {
		return iv;
	}
	
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getEncryptedAesKey() {
		return encryptWithRsa(aesKey.getEncoded());
	}
	public String getEncryptedMessage() {
		return encryptWithAes(message.getBytes());
	}
	
	private String encryptWithAes(byte[] toEncrypt) {
		Cipher cipher;
		try {
			cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, aesKey);
			byte[] encryptedMessage = cipher.doFinal(toEncrypt);
			AlgorithmParameters params = cipher.getParameters();
			iv = Base64.encodeToString(params.getParameterSpec(IvParameterSpec.class).getIV(), Base64.DEFAULT);
			return Base64.encodeToString(encryptedMessage, Base64.DEFAULT);
		} 
		catch (NoSuchAlgorithmException e) {
			Log.e("org.sec.ncrypto", "No such algorithm!");
		} 
		catch (NoSuchPaddingException e) {
			Log.e("org.sec.ncrypto", "Invalid padding!");
		} 
		catch (InvalidKeyException e) {
			Log.e("org.sec.ncrypto", "Invalid key!");
		} catch (IllegalBlockSizeException e) {
			Log.e("org.sec.ncrypto", "Illegal block size!");
		} catch (BadPaddingException e) {
			Log.e("org.sec.ncrypto", "Bad padding!");
		} catch (InvalidParameterSpecException e) {
			Log.e("org.sec.ncrypto", "Invalid parameter exception");
		}
		
		return null;
	}
	
	private String encryptWithRsa(byte[] toEncrypt) {
		
        try {
        	Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.ENCRYPT_MODE, rsaPublicKey);
			byte[] key = cipher.doFinal(toEncrypt);
			String result = Base64.encodeToString(key, Base64.DEFAULT);
			return result;
		} 
        catch (InvalidKeyException e) {
        	Log.e("org.sec.ncrypto", "Invalid key!");
		}
        catch(NoSuchPaddingException e) {
        	Log.e("org.sec.ncrypto", "Invalid padding!");
        }
        catch(NoSuchAlgorithmException e) {
        	Log.e("org.sec.ncrypto", "No such algorithm!");
        } 
        catch (IllegalBlockSizeException e) {
        	Log.e("org.sec.ncrypto", "Illegal block size!");
		} 
        catch (BadPaddingException e) {
			Log.e("org.sec.ncrypto", "Bad padding!");
		}
        
		
		return null;
	}
	
	
}
