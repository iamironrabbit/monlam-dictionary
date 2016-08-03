package org.lobsangmonlam.dictionary;

import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.content.SharedPreferencesCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.github.javiersantos.appupdater.AppUpdater;
import com.github.javiersantos.appupdater.enums.Display;
import com.github.javiersantos.appupdater.enums.Duration;
import com.github.javiersantos.appupdater.enums.UpdateFrom;

import org.ironrabbit.type.CustomTypefaceManager;
import org.ironrabbit.type.CustomTypefaceTextView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SearchFragment.OnFragmentInteractionListener, TextWatcher {

    private FragmentTabHost mTabHost;
    private Toolbar mToolbar;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private EditText mSearchBox;

    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CustomTypefaceManager.loadFromAssets(this);

        setContentView(R.layout.main);

        setTitle(getString(R.string.main_title));
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getActionBarTextView().setTypeface(CustomTypefaceManager.getCurrentTypeface(this));

        mToolbar.setLogo(R.drawable.toolbaricon);
        mToolbar.setCollapsible(true);
        mToolbar.setTitle(getTitle());

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(mViewPager);

        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(mViewPager);

        changeTabsFont(mTabLayout);

        mSearchBox = (EditText)findViewById(R.id.searchbox);
        mSearchBox.addTextChangedListener(this);

        View button = findViewById(R.id.searchbtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSearch();
            }
        });

        checkFirstTime();

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        Bundle bundle = new Bundle();
        bundle.putString("db", "tbtb");
        SearchFragment frag = new SearchFragment();
        frag.setArguments(bundle);
        adapter.addFragment( frag, getString(R.string.tab_tb));

        bundle = new Bundle();
        bundle.putString("db", "tben");
        frag = new SearchFragment();
        frag.setArguments(bundle);
        adapter.addFragment( frag, getString(R.string.tab_tbtoen));

        bundle = new Bundle();
        bundle.putString("db", "entotb");
        frag = new SearchFragment();
        frag.setArguments(bundle);
        adapter.addFragment( frag, getString(R.string.tab_entotb));


        viewPager.setAdapter(adapter);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 2)
                    mSearchBox.setHint(getString(R.string.searchhint_en));
                else
                    mSearchBox.setHint(getString(R.string.searchhint_tb));

//                doSearch();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void changeTabsFont(TabLayout tabLayout) {

        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    ((TextView) tabViewChild).setTypeface(CustomTypefaceManager.getCurrentTypeface(this), Typeface.NORMAL);
                }
            }
        }
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    private void checkUpdates ()
    {
        try {
            AppUpdater appUpdater = new AppUpdater(this);
            appUpdater.setDisplay(Display.DIALOG);
            appUpdater.setUpdateFrom(UpdateFrom.XML);
            appUpdater.setUpdateXML(MonlamConstants.URL_UPDATER);
          //  appUpdater.showAppUpdated(true);
            appUpdater.start();
        }
        catch (RuntimeException e)
        {
            Log.d("AppUpdater","error checking app updates",e);
        }
        catch (Exception e)
        {
            Log.d("AppUpdater","error checking app updates",e);
        }
	}

    private void checkFirstTime ()
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        if (prefs.getBoolean("showapps",true))
        {

            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle(getString(R.string.app_prompt_title));
         //   alert.setMessage(getString(R.string.app_prompt));

            final CustomTypefaceTextView input = new CustomTypefaceTextView (this);
            input.setText(getString(R.string.app_prompt));
            input.setPadding(35,10,35,10);
            alert.setView(input);


            alert.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    showApps();
                }
            });

            alert.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // Canceled.
                }
            });
            alert.show();

            prefs.edit().putBoolean("showapps",false).commit();
        }
    }

    private TextView getActionBarTextView() {
        TextView titleTextView = null;

        try {
            Field f = mToolbar.getClass().getDeclaredField("mTitleTextView");
            f.setAccessible(true);
            titleTextView = (TextView) f.get(mToolbar);
        } catch (NoSuchFieldException e) {
        } catch (IllegalAccessException e) {
        }
        return titleTextView;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private void showApps ()
    {
        //show list of recommended apps
        startActivity(new Intent(this,AppListActivity.class));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_apps:
                showApps();
                return true;

            case R.id.action_update:
                checkUpdates();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void afterTextChanged(Editable s) {

//        new QueryTask().execute(s.toString());
        doSearch();

    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {

    }
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    private void doSearch ()
    {

        SearchFragment frag = (SearchFragment)((FragmentPagerAdapter)mViewPager.getAdapter()).getItem(mViewPager.getCurrentItem());
        frag.doSearch(mSearchBox.getText().toString());
    }
}
