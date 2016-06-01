package org.ironrabbit.type;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.util.Log;

public class CustomTypefaceManager {

	private static Typeface mTypeface = null;
	
//	public final static int mTypefaceIds[] = {R.raw.jomolhari,R.raw.monlamuniouchan2,R.raw.tcrcunicode,R.raw.tibmachuni};
//	public final static String mTypefaceStrings[] = {"jomolhari.ttf","monlamuniouchan2.ttf","tcrcunicode.ttf","tibmachuni.ttf"};
//	public final static String mTypefaceNames[] = {"Jomolhari","Monlam Uni Ouchan2","TCRC Unicode","Tibetan Machine Uni"};
	
	
	public final static int mTypefaceIds[] = {R.raw.jomolhari};
	public final static String mTypefaceStrings[] = {"jomolhari.ttf"};
	public final static String mTypefaceNames[] = {"Jomolhari"};
	
	private final static String FONT_FOLDER = "Fonts";	
	
	public synchronized static Typeface getCurrentTypeface (Context context)
	{
		if (mTypeface == null)
			loadTypeface(context);
		
		return mTypeface;
	}
	
	public static void loadTypeface (Context context)
	{
    	File fileFolder = new File(context.getExternalFilesDir(null),FONT_FOLDER);
    	fileFolder.mkdirs();
    	
    	for (int i = 0; i < mTypefaceIds.length; i++)
    	{
    		
    		try
    		{
    			File fileTf = new File(fileFolder,mTypefaceStrings[i]);    	
    			if (!fileTf.exists())
    				copyRawFile(context, mTypefaceIds[i], fileTf, true);
    		}
    		catch (Exception e)
    		{
    			Log.e("FontManager","error loading font: " + mTypefaceStrings[i],e);
    		}
    	}
    	
    	  	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
    	   	int typeFaceId = prefs.getInt("pref_typeface",0);
    	
    	mTypeface = Typeface.createFromFile(new File(fileFolder,mTypefaceStrings[typeFaceId]));
	}
	
	/**
	 * Copies a raw resource file, given its ID to the given location
	 * @param ctx context
	 * @param resid resource id
	 * @param file destination file
	 * @param mode file permissions (E.g.: "755")
	 * @throws IOException on error
	 * @throws InterruptedException when interrupted
	 */
	private static void copyRawFile(Context ctx, int resid, File file, boolean isGZipd) throws IOException, InterruptedException
	{
		FileOutputStream out = new FileOutputStream(file);
		InputStream is = ctx.getResources().openRawResource(resid);
	
		if (isGZipd)
    	{
    		is = new GZIPInputStream(is);    		    		
    	}
		
		byte buf[] = new byte[4096];
		int len;
		while ((len = is.read(buf)) > 0) {
			out.write(buf, 0, len);
		}
		out.close();
		is.close();
		
	}
	
	public static boolean precomposeRequired ()
	{
		return true;
	}
	
	public static String handlePrecompose (String text)
	{
		return TibConvert.convertUnicodeToPrecomposedTibetan(text);
	}
}
