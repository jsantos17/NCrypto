package org.sec.ncrypto;

import java.util.List;

import org.sec.ncrypto.db.ContactDatabaseHelper;
import org.sec.ncrypto.ui.ContactAdapter;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.widget.ListView;

public class ContactsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contacts);
		
		SharedPreferences settings = getPreferences(MODE_PRIVATE);
        Log.w("First launch? ", settings.getBoolean("FIRST_LAUNCH", true)? "yes": "no");
        if(settings.getBoolean("FIRST_LAUNCH", true)) {
        	settings.edit().putBoolean("FIRST_LAUNCH", false).apply();
        	Intent intent = new Intent(this, KeygenActivity.class);
        	startActivity(intent);
        }
        ContactDatabaseHelper helper = new ContactDatabaseHelper(this, "db", null, 0);
        String pubKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA8P9PouLq6Cue/" +
        		"6oA8mdXkxPPzLpEHTmXkSOyS8BnjCihLcAOd6NhAqTG0314LQ2VjzIibfQ5rTogjd" +
        		"5FQ1XfTi3NMNpPOBjV4vJTmBGIKdCiAkn0MCwxTcG/SDQT0vUCNwXwYZbFWBbM8ND" +
        		"OaQoRulgu8i/qLkFT5HfvbeLbpLsKjaDBjv5mwowX6u/qpgomMy6ewU3H7Q2QtM+V" +
        		"B7F33/IczQuShDwBKMJXOrdld+8HX742/6rJQUbaforQI8Gm+BBY+wt2I+/g5CD89" +
        		"jCB5TvCY70+Y/TvWZres3kiUDOCYyTnf+zn4yYKR2OLaecREdUtxvOm3lvyxx2p6WQ" +
        		"+/QIDAQAB";
        Contact contact2 = new Contact("Mario", "Celi", pubKey);
		helper.insertContact(contact2);
		List<Contact> contacts = helper.getAllContacts();
		ListView contactListView = (ListView)findViewById(R.id.contactList);
		ContactAdapter contactAdapter = new ContactAdapter(contacts, this); 
		contactListView.setAdapter(contactAdapter);
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.contacts, menu);
		return true;
	}

}
