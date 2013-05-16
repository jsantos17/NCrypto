package org.sec.ncrypto.ui;

import java.util.List;

import org.sec.ncrypto.Contact;
import org.sec.ncrypto.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ContactAdapter extends BaseAdapter {
	
	private List<Contact> contacts;
	private LayoutInflater inflater;
	
	public ContactAdapter(List<Contact> contacts, Context context) {
		super();
		this.contacts = contacts;
		this.inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return contacts.size();
	}

	@Override
	public Object getItem(int position) {
		return contacts.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {		
		convertView = inflater.inflate(R.layout.list_multi_item, null);
		TextView nameView = (TextView) convertView.findViewById(R.id.contact_name);
		TextView fingerprintView = (TextView) convertView.findViewById(R.id.contact_fingerprint);
		nameView.setText(contacts.get(position).getName() + 
				" " + contacts.get(position).getLastName());
		fingerprintView.setText(contacts.get(position).getKeyFingerprint());
		return convertView;
	}

}
