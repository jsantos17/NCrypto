package org.sec.ncrypto;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ProgressBar;


public class KeygenActivity extends Activity {

	
	String publicKey;
	String privateKey;
	View button;
	ProgressBar spinner;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_keygen);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.keygen, menu);
		return true;
	}
	public void generateKeyPair(View view) {

        button = view;
        view.setEnabled(false);
        spinner = (ProgressBar) findViewById(R.id.spinner);
        spinner.setVisibility(View.VISIBLE);
        try {
        	KeyPairGenerator keygen = KeyPairGenerator.getInstance("RSA","BC");
        	keygen.initialize(2048); // Generate 2048 bits keys
        	new KeyPairGenerationTask().execute(keygen);
        }
        catch(NoSuchAlgorithmException e) {
        	Log.d("org.sec.ncrypto","No such algorithm");
        } catch (NoSuchProviderException e) {
			Log.d("org.sec.ncrypto","Bouncy Castle not working");
		}
	}
	private class KeyPairGenerationTask extends AsyncTask<KeyPairGenerator, Integer, KeyPair> {

		@Override
		protected KeyPair doInBackground(KeyPairGenerator... args) {
			KeyPairGenerator keygen = args[0];
			KeyPair resultKey = keygen.generateKeyPair();
			try {
				FileOutputStream fos = openFileOutput("keys", MODE_PRIVATE);
				ObjectOutputStream oos = new ObjectOutputStream(fos);
				oos.writeObject(resultKey);
				oos.flush();
				oos.close();
				MessageDigest digester = MessageDigest.getInstance("SHA-1");
				String hash = Base64.encodeToString(
						digester.digest(resultKey.getPublic().getEncoded()), Base64.DEFAULT);
				getPreferences(MODE_PRIVATE).edit().putString("KEY_FINGERPRINT", hash).commit();
			} 
			catch (FileNotFoundException e) {
				Log.d("org.sec.ncrypto", "File not found");
				Log.d("org.sec.ncrupto", e.getStackTrace().toString());
				e.printStackTrace();
				
			} 
			catch (IOException e) {
				Log.d("org.sec.ncrypto","IO error");
				Log.d("org.sec.ncrypto",e.getStackTrace().toString());
				e.printStackTrace();
			}
			catch(NoSuchAlgorithmException e) {
				Log.d("org.sec.ncrypto", "SHA-1 not available");
			}
			return resultKey;
		}
		@Override
		protected void onPostExecute(KeyPair resultKey) {
			spinner.setVisibility(View.INVISIBLE);
			button.setEnabled(true);
			finish();
		}
		
	}

}
