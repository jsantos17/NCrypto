package org.sec.ncrypto;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.util.Base64;
import android.util.Log;

public class Contact {
	private String name;
	private String lastName;
	private String keyFingerprint;
	private String pubKey;
	

	public Contact(String name, String lastName, String pubKey) {
		this.name = name;
		this.lastName = lastName;
		this.setPubKey(pubKey); // call setter to generate hash
	}
	
	public String getPubKey() {
		return pubKey;
	}
	public void setPubKey(String pubKey) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			keyFingerprint = new String(Base64.encode(md.digest(pubKey.getBytes()), Base64.DEFAULT));
		} catch (NoSuchAlgorithmException e) {
			Log.d("org.sec.ncrypto", "No SHA-1 provider!");
		}
		this.pubKey = pubKey;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getKeyFingerprint() {
		return keyFingerprint;
	}
	public void setKeyFingerprint(String keyFingerprint) {
		this.keyFingerprint = keyFingerprint;
	}
}
