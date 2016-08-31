package org.lobsangmonlam.dictionary;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

/**
 * The DBAdapter class enables program integration with a SQLite database.
 * @author Hanly De Los Santos (http://hdelossantos.com)
 */
public class DBAdapter
{
	
	private static final int DATABASE_VERSION = 1;
    private static final long ASSET_LENGTH = 36041728;
	
	// Index Key column
	public static final String KEY_ID = "_id";
	
	private Context mContext = null;

	private int mDbResourceId = -1;

	// Variable to hold database instant
	private SQLiteDatabase db;
	private File mDbFile;

    public final static String DEFAULT_ASSET = "MLDic.ml";

    private static DBAdapter mInstance = null;

    public static synchronized DBAdapter getInstance (Context context, File dbFile)
    {

        if (mInstance == null)
        {
            mInstance = new DBAdapter(context, dbFile);

        }
		else if (mInstance.getDatabase() == null || (!mInstance.getDatabase().isOpen()))
		{
			mInstance = new DBAdapter(context, dbFile);

		}

        return mInstance;
    }
	/**
	 * Open the database if it exists or create it if it doesn't. Additionally checks if the
	 * table exists and creates it if it doesn't.
	 * @param context Context passed by the parent.
	 */
	@SuppressWarnings("unchecked")
	private DBAdapter(Context context, File dbFile){
		// Start initializing all of the variables
		
		mContext = context;
		mDbFile = dbFile;
    	
		if (mDbFile.exists())
    	{
			open();
		}
	}

	private SQLiteDatabase getDatabase ()
	{
		return db;
	}
	
	public static synchronized void installDb (Context context, File dbFile, String asset)
	{
		boolean doDbInstall = false;

        if (!dbFile.exists())
			doDbInstall = true;
		else if (dbFile.length() != ASSET_LENGTH)
		    doDbInstall = true;

		if (doDbInstall)
		{

            try {
				dbFile.getParentFile().mkdirs();
                copyAssetFile(context, asset, dbFile);
               ;
            } catch (Exception e) {
                Log.e("db", e.getMessage(), e);
            }
        }
		
	}
	

    
	
	private static void copyAssetFile(Context ctx, String asset, File file) throws IOException, InterruptedException
	{
    	
		DataOutputStream out = new DataOutputStream(new FileOutputStream(file));
		InputStream is = ctx.getAssets().open(asset);
		
		byte buf[] = new byte[8172];
		int len;
		while ((len = is.read(buf)) > 0) {
			out.write(buf, 0, len);
		}
		out.close();
		is.close();
	}
	
	
	/**
	 * Open the connection to the database.
	 * @return Returns a DBAdapter.
	 * @throws SQLException
	 */
	private DBAdapter open() throws SQLException {
		db = SQLiteDatabase.openOrCreateDatabase(mDbFile, null);
		
		return this;
	}
	
	/**
	 * Close the connection to the database.
	 */
	public void close() {
		db.close();
	}
	
	/**
	 * Insert a row into the database.
	 * @param key ArrayList of Keys (column headers).
	 * @param value ArrayList of Key values.
	 * @return Returns the number of the row added.
	 */
	public long insertEntry(ArrayList<String> key, ArrayList<String> value, String table) {
		ContentValues contentValues = new ContentValues();
		for(int i = 0; key.size() > i; i++){
			contentValues.put(key.get(i), value.get(i));
		}
		Log.v("Database Add", contentValues.toString());
		return db.insert(table, null, contentValues);
	}
	
	/**
	 * Remove a row from the database.
	 * @param rowIndex Number of the row to remove.
	 * @return Returns TRUE if it was deleted, FALSE if failed.
	 */
	public boolean removeEntry(long rowIndex, String table) {
		return db.delete(table, KEY_ID + "=" + rowIndex, null) > 0;
	}
	
	/**
	 * Get all entries in the database sorted by the given value.
	 * @param columns List of columns to include in the result.
	 * @param selection Return rows with the following string only. Null returns all rows.
	 * @param selectionArgs Arguments of the selection.
	 * @param groupBy Group results by.
	 * @param having A filter declare which row groups to include in the cursor.
	 * @param sortBy Column to sort elements by.
	 * @param sortOption ASC for ascending, DESC for descending.
	 * @return Returns a cursor through the results.
	 */
	public Cursor getAllEntries(String table, String[] columns, String selection, String[] selectionArgs,
			String groupBy, String having, String sortBy, String sortOption) {

		if (db != null)
			return db.query(table, columns, selection, selectionArgs, groupBy,
					having, sortBy + " " + sortOption);
		else
			return null;
	}
	
	/**
	 * Does the SQL UPDATE function on the table with given SQL string
	 * @param sqlQuery an SQL Query starting at SET
	 */
	public void update(String table, String sqlQuery) {
		db.rawQuery("UPDATE " + table + sqlQuery, null);
	}
	
	/**
	 * Get all entries in the database sorted by the given value.
	 * @param columns List of columns to include in the result.
	 * @param selection Return rows with the following string only. Null returns all rows.
	 * @param selectionArgs Arguments of the selection.
	 * @param groupBy Group results by.
	 * @param having A filter declare which row groups to include in the cursor.
	 * @param sortBy Column to sort elements by. tabl
	 * @param sortOption ASC for ascending, DESC for descending.
	 * @param limit limiting number of records to return
	 * @return Returns a cursor through the results.
	 */
	public Cursor getAllEntries(String table, String[] columns, String selection, String[] selectionArgs,
			String groupBy, String having, String sortBy, String sortOption, String limit) {
		return db.query(table, columns, selection, selectionArgs, groupBy,
				having, sortBy + " " + sortOption, limit);
	}
	
	
	/**
	 * This is a function that should only be used if you know what you're doing.
	 * It is only here to clear the appended test data. This clears out all data within
	 * the table specified when the database connection was opened.
	 * @return Returns TRUE if successful. FALSE if not.
	 */
	public boolean clearTable(String table) {
		return db.delete(table, null, null) > 0;
	}
	
	/**
	 * Update the selected row of the open table.
	 * @param rowIndex Number of the row to update.
	 * @param key ArrayList of Keys (column headers).
	 * @param value ArrayList of Key values.
	 * @return Returns an integer.
	 */
	public int updateEntry(String table, long rowIndex, ArrayList<String> key, ArrayList<String> value) {
		String where = KEY_ID + "=" + rowIndex;
		ContentValues contentValues = new ContentValues();
		for(int i = 0; key.size() > i; i++){
			contentValues.put(key.get(i), value.get(i));
		}
		return db.update(table, contentValues, where, null);
	}
	
	

}
