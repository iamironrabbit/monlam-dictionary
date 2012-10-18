package org.lobsangmonlam.dictionary;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

/**
 * This program adds entries to the database, then pulls them out and
 * uses them to populate a ListView.
 * @author n8fr8
 * @author Hanly De Los Santos (http://hdelossantos.com)
 *
 */
public class DictionarySearchActivity extends Activity implements TextWatcher, MonlamConstants {
	
	private  ListView lv;
	private  TextView tv;
	
	private SimpleCursorAdapter adapter;
	private Cursor cursor;
	
	private DBAdapter database;
      String[] cols = new String[] { COLUMN_WORD, COLUMN_MEANING };
      int[] names = new int[] {R.id.WORD, R.id.MEANING};

	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.main);
        
        String dbName = getIntent().getExtras().getString("db");
        
        try
        { 	
        	File dbPath = new File(Environment.getExternalStorageDirectory(),DB_FOLDER_NAME);
        	
        	// Call the database adapter to create the database
    		database = new DBAdapter(this, dbPath, dbName, dbName);
	  
	        tv = (TextView)findViewById(R.id.SEARCH);
	        tv.addTextChangedListener(this);
	        
	        if (dbName.equals("entotb"))
	        	tv.setHint(getString(R.string.searchhint_en));
	        
	        Button btn = (Button)findViewById(R.id.BTN_SEARCH);
	        btn.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					
					InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(tv.getWindowToken(), 0);
					
					new QueryTask().execute(tv.getText().toString());

				}
	        	
	        });
	        
	        lv = (ListView)findViewById(R.id.mylist);
	        
	        lv.setTextFilterEnabled(true);
        }
        catch (Exception e)
        {
        	Log.e("db",e.getLocalizedMessage(),e);
        }

    }
    
    
    
	@Override
	protected void onResume() {
		super.onResume();
		

	}


	@Override
	public void afterTextChanged(Editable s) {

		new QueryTask().execute(s.toString());

		
	}
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		
	}
	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
	
	}
	
	Handler handler = new Handler() {		
		  public void handleMessage(Message msg) {
		
			  adapter = new SimpleCursorAdapter(DictionarySearchActivity.this, R.layout.listitem ,cursor,cols,names);
		        
	  	        // Set the ListView
	  	        lv.setAdapter(adapter);
	  	        lv.setTextFilterEnabled(true);
		}
	};

	
	private class QueryTask extends AsyncTask<String, Void, Void> {
	 
	      // can use UI thread here
	      protected void onPreExecute() {
	        
	      }
	 
	      // automatically done on worker thread (separate from UI thread)
	      protected Void doInBackground(final String... args) {
	    	  
	    	  try
	          {
	    		 String queryString = args[0];
	    		 
	  	        String queryText = COLUMN_WORD + " LIKE '" + queryString + "%'";
	  	        
	  	        //OR " + COLUMN_MEANING + " LIKE '%" + queryString + "%'";
	  	      
	  	        cursor = database.getAllEntries(new String[] {COLUMN_ID, COLUMN_WORD, COLUMN_MEANING}, queryText, null, null, null, COLUMN_WORD, " ASC LIMIT " + QUERY_LIMIT);
	    		  
	  	        Log.d("db","found items: " + cursor.getCount());
	  	        
	  	        startManagingCursor(cursor);
	  	       
	  	        handler.sendMessage(new Message());

	          }
	          catch (Exception e)
	          {
	          	Log.e("db",e.getLocalizedMessage(),e);
	          }
	    	  
	         return null;
	      }
	 
	      // can use UI thread here
	      protected void onPostExecute(final Void unused) {
	        
	      }
	   }
    
    
   
}