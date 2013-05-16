package org.sec.ncrypto.db;

import java.util.ArrayList;
import java.util.List;

import org.sec.ncrypto.Contact;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;


public class ContactDatabaseHelper extends SQLiteOpenHelper {

	static final String DB_NAME = "contacts";
	static final String TABLE_NAME = "contact";
	static final String COL_NAME = "name";
	static final String COL_LASTNAME = "lastName";
	static final String COL_FINGERPRINT = "fingerprint";
	static final String COL_PUBKEY = "pubkey";

	
	public ContactDatabaseHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, null, 37);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String query = "CREATE TABLE %s(" +
				"id integer primary key," +
				"%s varchar(255)," +
				"%s varchar(255)," +
				"%s text," +
				"%s varchar(255))";
		db.execSQL(String.format(query,TABLE_NAME, 
				COL_NAME, COL_LASTNAME, COL_PUBKEY, COL_FINGERPRINT));		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(String.format("DROP TABLE IF EXISTS %s",TABLE_NAME));
		onCreate(db);
	}
	
	public void insertContact(Contact contact) {
		String query = "INSERT INTO %s VALUES (NULL, '%s', '%s', '%s', '%s')";
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL(String.format(query, TABLE_NAME, 
				contact.getName(),
				contact.getLastName(),
				contact.getPubKey(),
				contact.getKeyFingerprint()));
	}
	
	public void deleteContact(String fingerprint) {
		String query = "DELETE FROM %s WHERE %s = '%s'";
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL(String.format(query, TABLE_NAME, COL_FINGERPRINT, fingerprint));
	}
	
	public List<Contact> getAllContacts() {
		String query = "SELECT %s, %s, %s FROM contact";
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(String.format
				(query,COL_NAME, COL_LASTNAME, COL_PUBKEY), new String[]{});
		List<Contact> contactList = new ArrayList<Contact>();
		if(cursor.getCount() == 0) {
			return contactList; // Return an empty list if there are no contacts
		}
		while(cursor.moveToNext()) {
			contactList.add(new Contact(
					cursor.getString(0), 
					cursor.getString(1), 
					cursor.getString(2)));
		}
		return contactList;
	}
	
	public String getPublicKey(String fingerprint) {
		String query = "SELECT pubkey FROM contact WHERE fingerprint = '%s'";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(String.format(query, fingerprint), new String[]{});
		cursor.moveToFirst();
		return cursor.getString(0);
	}

}
