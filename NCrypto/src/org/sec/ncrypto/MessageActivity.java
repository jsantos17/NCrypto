package org.sec.ncrypto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.sec.ncrypto.security.EncryptedMessage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class MessageActivity extends Activity {
    
	ArrayAdapter<String> adapter;
	private String fingerprint;
	private String username;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        ListView listView = (ListView)findViewById(R.id.messageList);
        TextView fingerprintView = (TextView) findViewById(R.id.fingerprintView);
        TextView usernameView = (TextView) findViewById(R.id.usernameView);
    	String[] items = {"Message 1", "Message 2"};
    	List<String> backingArray = new ArrayList<String>(Arrays.asList(items));
    	adapter = new ArrayAdapter<String>(this, R.layout.list_item, backingArray);
    	listView.setAdapter(adapter);
    	Intent intent = getIntent();
    	
    	fingerprint = intent.getStringExtra("fingerprint");
    	fingerprintView.setText(fingerprint);
    	
    	username = intent.getStringExtra("username");
    	usernameView.setText(username);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    public void sendMessage(View view) {
    	// We need to create an AES key for EVERY message
    	String ownFingerprint = PreferenceManager
    			.getDefaultSharedPreferences(getBaseContext())
    			.getString("KEY_FINGERPRINT", "Key not found");
    	EditText editText = (EditText)findViewById(R.id.edit_message);
    	EncryptedMessage em = new EncryptedMessage(
    			fingerprint, editText.getText().toString(), this);
    	
    	Log.d("MESSAGE submitter", ownFingerprint);
    	Log.d("MESSAGE destination", em.getDestination());
    	Log.d("MESSAGE key", em.getEncryptedAesKey());
    	Log.d("MESSAGE encrypted", em.getEncryptedMessage());
    	Log.d("MESSAGE IV", em.getIv());
    	
    	this.adapter.add(editText.getText().toString());
    	editText.setText("");
    }
    
}
