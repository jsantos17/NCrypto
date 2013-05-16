package org.sec.ncrypto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class MessageActivity extends Activity {
    
	ArrayAdapter<String> adapter;
	final String PREFS_NAME = "org.sec.ncrypto.SHARED_PREFS";
	
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        ListView listView = (ListView)findViewById(R.id.messageList);
    	String[] items = {"Message 1", "Message 2"};
    	List<String> backingArray = new ArrayList<String>(Arrays.asList(items));
    	adapter = new ArrayAdapter<String>(this, R.layout.list_item, backingArray);
    	listView.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    public void sendMessage(View view) {
    	EditText editText = (EditText)findViewById(R.id.edit_message);
    	this.adapter.add(editText.getText().toString());
    	editText.setText("");
    }
    
}
