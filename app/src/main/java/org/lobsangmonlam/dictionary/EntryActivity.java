package org.lobsangmonlam.dictionary;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.os.Environment;
import android.support.v4.app.NavUtils;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.ironrabbit.type.CustomTypefaceManager;

import java.io.File;
import java.io.FileOutputStream;

public class EntryActivity extends AppCompatActivity {

    private String defWord, defMeaning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);

        setTitle("");

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setCollapsible(true);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        findViewById(R.id.defgroup).setDrawingCacheEnabled(true);

        TextView tvWord = (TextView)findViewById(R.id.WORD);
        TextView tvMeaning = (TextView)findViewById(R.id.MEANING);

        defWord = getIntent().getStringExtra("word");
        defMeaning = getIntent().getStringExtra("meaning");

        tvWord.setText(defWord);
        tvMeaning.setText(defMeaning);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
               finish();
                return true;
            case R.id.action_save:
                saveAsBitmap();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private ShareActionProvider mShareActionProvider;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_entry, menu);
        // Get the menu item.
        MenuItem menuItem = menu.findItem(R.id.action_share);
        // Get the provider and hold onto it to set/change the share intent.
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
        // Set share Intent.
        // Note: You can set the share Intent afterwords if you don't want to set it right now.
        mShareActionProvider.setShareIntent(createShareIntent());
        return true;
    }

    // Create and return the Share Intent
    private Intent createShareIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, defWord + "\n" + defMeaning);
        return shareIntent;
    }

    // Sets new share Intent.
    // Use this method to change or set Share Intent in your Activity Lifecycle.
    private void changeShareIntent(Intent shareIntent) {
        mShareActionProvider.setShareIntent(shareIntent);
    }

    private void saveAsBitmap ()
    {

        try {
            int width = 640;
            int height = 480;

            Bitmap  bitmap = Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);

            Paint paintFill = new Paint();
            paintFill.setColor(Color.WHITE);

            // draw a rectangle
            paintFill.setColor(Color.WHITE);
            paintFill.setStyle(Paint.Style.FILL); //fill the background with blue color
            canvas.drawRect(0,0,width,height,paintFill);

            TextPaint tp = new TextPaint();
            tp.setColor(Color.GRAY);
            tp.setTextSize(40);
            tp.setAntiAlias(true);
            tp.setTypeface(CustomTypefaceManager.getCurrentTypeface(this));

            StaticLayout sl = new StaticLayout(defWord + '\n' + defMeaning, tp,
            width, Layout.Alignment.ALIGN_NORMAL, 1f, 0f, false);
            canvas.translate(20, 20);
            sl.draw(canvas);

            File outFolder = new File(Environment.getExternalStorageDirectory(), MonlamConstants.DB_FOLDER_NAME);
            File outFile = new File(outFolder, new java.util.Date().getTime() + ".jpg");
            FileOutputStream fos = new FileOutputStream(outFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 95, fos);

            fos.close();;

            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("image/jpg");
            share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(outFile));
            startActivity(Intent.createChooser(share, "Share Image"));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
