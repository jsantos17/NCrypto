package org.sec.ncrypto;

import java.util.List;

import org.sec.ncrypto.db.ContactDatabaseHelper;
import org.sec.ncrypto.ui.ContactAdapter;

import android.os.Bundle;
import android.os.StrictMode;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class ContactsActivity extends Activity implements OnItemClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (android.os.Build.VERSION.SDK_INT > 9) {
		      StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		      StrictMode.setThreadPolicy(policy);
		}
		setContentView(R.layout.activity_contacts);
		ListView contactListView = (ListView)findViewById(R.id.contactList);
		contactListView.setOnItemClickListener(this);
		SharedPreferences settings = getPreferences(MODE_PRIVATE);
        Log.w("First launch? ", settings.getBoolean("FIRST_LAUNCH", true)? "yes": "no");
        if(settings.getBoolean("FIRST_LAUNCH", true)) {
        	settings.edit().putBoolean("FIRST_LAUNCH", false).apply();
        	Intent intent = new Intent(this, KeygenActivity.class);
        	startActivity(intent);
        }
        ContactDatabaseHelper helper = new ContactDatabaseHelper(this, "db", null, 0);
        String pubKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAw9987LIBweS/FdItRBGz\n" +
        				"7ljQw+gVfNnVjGRAEK98h0xnWyj+67ZcT/JznqYITO+R6BgUjtMCHEF3E1vWA9D+\n" +
        				"Z7PtXfwWr9oUJ/ZHABqH4EAf94yzW9Wf7jMRpGqYXkh0tdKdP6mW93dpVXqOoSJF\n" +
        				"Q20znYXDi9rrznGLpPTprhNyoKOBDfCjy1wWpNZs5UddyflNB/PA9A6q2srhngRF\n" +
        				"sfc7+7jIG0Y3dTcSdC6oQpQ5sObtBKsv4BopAH5Pd8/7pvlory68vRK0UXX9GTqN\n" +
        				"mmz1bLzmBy47uPy4IJopc1eWmpdYoOaCZ6pR3LrWatEKGc3ZIydhAxO8TPscz1oJ\n" +
        				"dQIDAQAB\n";
        Contact contact2 = new Contact("Mario", "Celi", pubKey);
		helper.insertContact(contact2);
		List<Contact> contacts = helper.getAllContacts();
		
		ContactAdapter contactAdapter = new ContactAdapter(contacts, this); 
		contactListView.setAdapter(contactAdapter);
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.contacts, menu);
		return true;
	}


	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
		ListView contactListView = (ListView)findViewById(R.id.contactList);
		Contact contact = (Contact) contactListView.getAdapter().getItem(position);
		Intent intent = new Intent(this, MessageActivity.class);
		intent.putExtra("fingerprint", contact.getKeyFingerprint());
		intent.putExtra("username", contact.getName() + " " + contact.getLastName());
		startActivity(intent);
	}

}
