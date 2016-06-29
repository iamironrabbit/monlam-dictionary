package org.lobsangmonlam.dictionary;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.view.Menu;
import android.view.MenuItem;

import org.ironrabbit.type.CustomTypefaceManager;

import java.io.File;
import java.io.FileOutputStream;

public class DrawingActivity extends AppCompatActivity {

    CanvasView mCanvas;
    private StringBuffer mText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawing);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setCollapsible(true);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mCanvas = (CanvasView) findViewById(R.id.canvas);

        String word = getIntent().getStringExtra("word");
        String meaning = getIntent().getStringExtra("meaning");

        mText = new StringBuffer();
        mText.append(word);
        mText.append("\n\n");
        mText.append(meaning);

        mCanvas.setFontFamily(CustomTypefaceManager.getCurrentTypeface(this));
        mCanvas.setMode(CanvasView.Mode.TEXT);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_photo:
                addPhoto();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }


    private ShareActionProvider mShareActionProvider;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_drawing, menu);
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
        shareIntent.setType("image/jpg");

        try {
            Bitmap bitmap = mCanvas.getBitmap();
            File outFolder = new File(Environment.getExternalStorageDirectory(), MonlamConstants.DB_FOLDER_NAME);
            File outFile = new File(outFolder, new java.util.Date().getTime() + ".jpg");
            FileOutputStream fos = new FileOutputStream(outFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 95, fos);

            fos.close();

            shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(outFile));
            return shareIntent;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onResume() {
        super.onResume();

        mCanvas.setText(mText.toString());
    }

    private void addPhoto ()
    {

    }
}
