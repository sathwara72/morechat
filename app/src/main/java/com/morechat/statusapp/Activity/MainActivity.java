package com.morechat.statusapp.Activity;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.ferfalk.simplesearchview.SimpleSearchView;
import com.google.android.material.navigation.NavigationView;
import com.morechat.statusapp.Adapter.CategoryAdapter;
import com.morechat.statusapp.Config;
import com.morechat.statusapp.Model.Category;
import com.morechat.statusapp.Utils.AdsManager;
import com.morechat.statusapp.Utils.AdsPref;
import com.morechat.statusapp.Utils.Constant;
import com.morechat.statusapp.Utils.DataBaseHandler;
import com.morechat.statusapp.Utils.PrefManager;
import com.onesignal.OneSignal;
import com.morechat.statusapp.Notification.NotificationScheduler;
import com.morechat.statusapp.R;
import com.ymg.ymgdevelopers.YmgTools;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{


    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private PrefManager prefManager;
    private static final String TAG = "MainActivity";
    private SimpleSearchView simpleSearchView;
    private CategoryAdapter categoryAdapter;
    private final ArrayList<Category> imageArry = new ArrayList<Category>();
    private GridView dataList;
    private DataBaseHandler dataBaseHandler;
    private AdsManager adsManager;
    private AdsPref adsPref;
    private static final String ONESIGNAL_APP_ID = "733542c8-0218-4742-b075-7f68cc6a8b0c";
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        prefManager = new PrefManager(this);
        dataBaseHandler = new DataBaseHandler(this);
        dataBaseHandler.openDataBase();

        adsPref = new AdsPref(this);
        adsManager = new AdsManager(this);
        adsManager.initializeAd();
        adsManager.updateConsentStatus();
        adsManager.loadBannerAd(true);
        adsManager.loadInterstitialAd(true,adsPref.getInterstitialAdInterval());
        NotificationScheduler.scheduleDailyNotification(this);

        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);
        OneSignal.initWithContext(this);
        OneSignal.setAppId(ONESIGNAL_APP_ID);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();
        toolbar.setNavigationIcon(R.drawable.ic_menu_action);

        final List<Category> categories = dataBaseHandler.getAllCategories("");
        for (Category cat : categories) {
            imageArry.add(cat);
        }
        //Adapter Code
        categoryAdapter = new CategoryAdapter(this, R.layout.item_category,imageArry);
        dataList = findViewById(R.id.categoryList);
        dataList.setAdapter(categoryAdapter);
        dataList.setTextFilterEnabled(true);
        dataList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked,
                                    int position, long idInDB) {

                Category srr = imageArry.get(position);
                Intent i = new Intent(getApplicationContext(),
                        QuotesActivity.class);
                i.putExtra("category", srr.getName());
                i.putExtra("mode", "isCategory");
                startActivity(i);
                adsManager.showInterstitialAd();


            }
        });

        //search
        simpleSearchView = findViewById(R.id.search_view);
        simpleSearchView.setOnQueryTextListener(new SimpleSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("SimpleSearchView", "Submit:" + query);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d("SimpleSearchView", "Text changed:" + newText);
                imageArry.clear();
                final List<Category> categories = dataBaseHandler.getAllCategories(newText);
                for (Category cat : categories) {
                    imageArry.add(cat);
                }
                dataList.setAdapter(categoryAdapter);
                return false;
            }
            @Override
            public boolean onQueryTextCleared() {
                Log.d("SimpleSearchView", "Text cleared");
                return false;
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        simpleSearchView.setMenuItem(item);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }//This app is Created by Sathwara-InfoTech
        if (item.getItemId() == R.id.action_prime) {
            startActivity(new Intent(this,PrimeActivity.class));
        }//This app is Created by Sathwara-InfoTech
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            showExitDialog();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        drawerLayout.closeDrawer(GravityCompat.START);
        if (menuItem.getItemId() == R.id.nav_maker){
            Intent favorites = new Intent(this,
                    MakerActivity.class);
            favorites.putExtra("quote", "");
            startActivity(favorites);
        }
        if (menuItem.getItemId() == R.id.nav_today){
            preferences = PreferenceManager.getDefaultSharedPreferences(this);
            Intent qteDay = new Intent(MainActivity.this,
                    QuoteActivity.class);
            qteDay.putExtra("id", preferences.getInt("id", prefManager.getInt("quoteToday")));
            qteDay.putExtra("mode", "qteday");
            startActivity(qteDay);
        }
        if (menuItem.getItemId() == R.id.nav_favotite){
            Intent favorites = new Intent(MainActivity.this, QuotesActivity.class);
            favorites.putExtra("mode", "isFavorite");
            startActivity(favorites);
        }
        if (menuItem.getItemId() == R.id.nav_rate){
            YmgTools.rateApp(this,getPackageName());
        }
        if (menuItem.getItemId() == R.id.nav_share){
            YmgTools.shareApp(this,getPackageName());
        }
        if (menuItem.getItemId() == R.id.nav_contact){
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("message/rfc822");
            i.putExtra(Intent.EXTRA_EMAIL  , new String[]{Config.contactUsEmail});
            i.putExtra(Intent.EXTRA_SUBJECT, Config.emailSubject);
            i.putExtra(Intent.EXTRA_TEXT   , Config.emailBodyText);
            try {
                startActivity(Intent.createChooser(i, "Send mail..."));
            } catch (ActivityNotFoundException ex) {
                Toast.makeText(MainActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
            }
        }
        if (menuItem.getItemId() == R.id.nav_about){
            showAboutDialog();
        }
        if (menuItem.getItemId() == R.id.nav_settings){
            startActivity(new Intent(MainActivity.this,SettingsActivity.class));
        }
        if (menuItem.getItemId() == R.id.nav_insta){
            YmgTools.openUrl(this, Config.SOCIAL_MEDIA_URL);
        }
        if (menuItem.getItemId() == R.id.nav_night){
            return false;
        }
        return false;
    }

    private void showExitDialog() {
        final Dialog dialog = new Dialog(MainActivity.this, R.style.DialogCustomTheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.dialog_exit);

        LinearLayout dialog_btn=dialog.findViewById(R.id.mbtnYes);
        LinearLayout mbtnNo=dialog.findViewById(R.id.mbtnNo);
        dialog_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mbtnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void showAboutDialog() {
        final Dialog dialog = new Dialog(MainActivity.this, R.style.DialogCustomTheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.dialog_about);

        AppCompatButton btn_done=dialog.findViewById(R.id.btn_done);
        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (!prefManager.getString("VDN").equals(Config.DEVELOPER_NAME)){
//            finish();
//        }
        initCheck();
    }

    private void initCheck() {
        if (prefManager.loadNightModeState()==true){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

}