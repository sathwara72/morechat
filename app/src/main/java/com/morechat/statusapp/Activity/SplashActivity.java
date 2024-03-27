package com.morechat.statusapp.Activity;

import static com.ymg.ads.sdk.util.Constant.ADMOB;
import static com.ymg.ads.sdk.util.Constant.APPLOVIN;
import static com.ymg.ads.sdk.util.Constant.APPLOVIN_MAX;
import static com.ymg.ads.sdk.util.Constant.GOOGLE_AD_MANAGER;
import static com.ymg.ads.sdk.util.Constant.WORTISE;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.res.ResourcesCompat;

import com.morechat.statusapp.Config;
import com.morechat.statusapp.Utils.Anims;
import com.morechat.statusapp.Utils.Constant;
import com.morechat.statusapp.Utils.PrefManager;
import com.morechat.statusapp.Utils.Tools;
import com.morechat.statusapp.R;
import com.morechat.statusapp.Utils.AdsManager;
import com.morechat.statusapp.Utils.AdsPref;
import com.ymg.ymgdevelopers.YmgTools;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;


public class SplashActivity extends com.ymg.ymgdevelopers.SplashActivity {

    private TextView developers;
    private PrefManager prf;
    private AdsPref adsPref;
    private AdsManager adsManager;
    private RelativeLayout parentLayout;
    private AppCompatImageView logo;
    public static final String TAG = "ActivitySplash";
    private Tools tools;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setSplashDeveloperName(getString( R.string.developers_name));
        setSplashImage(R.drawable.logo);

        prf = new PrefManager(this);
        adsPref = new AdsPref(this);
        adsManager = new AdsManager(this);
        adsManager.initializeAd();
        tools = new Tools(this);

        if (prf.getBoolean("first")){
            startActivity(new Intent(SplashActivity.this,OnBoardActivity.class));
            finish();
            return;
        }

        developers = findViewById(R.id.developers);
        parentLayout = findViewById(R.id.parentLayout);

        logo = findViewById(R.id.logo);
        Anims aVar = new Anims(ResourcesCompat.getDrawable(this.getResources(),R.drawable.logo,this.getTheme()));
        aVar.m14932a(true);
        logo.setImageDrawable(aVar);
        logo.setVisibility(View.VISIBLE);


        if (Config.AD_STATUS && Constant.OPEN_ADS_ON_START) {
            if (Constant.FORCE_TO_SHOW_APP_OPEN_AD_ON_START) {
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    switch (Config.AD_NETWORK) {
                        case ADMOB:
                            if (!Config.ADMOB_APP_OPEN_AD_ID.equals("0")) {
                                ((MyApplication) getApplication()).showAdIfAvailable(SplashActivity.this, this::dataIsReadys);
                            } else {
                                dataIsReadys();
                            }
                            break;
                        case GOOGLE_AD_MANAGER:
                            if (!Config.GOOGLE_AD_MANAGER_APP_OPEN_AD_ID.equals("0")) {
                                ((MyApplication) getApplication()).showAdIfAvailable(SplashActivity.this, this::dataIsReadys);
                            } else {
                                dataIsReadys();
                            }
                            break;
                        case APPLOVIN:
                        case APPLOVIN_MAX:
                            if (!Config.APPLOVIN_APP_OPEN_AP_ID.equals("0")) {
                                ((MyApplication) getApplication()).showAdIfAvailable(SplashActivity.this, this::dataIsReadys);
                            } else {
                                dataIsReadys();
                            }
                            break;
                        case WORTISE:
                            if (!Config.WORTISE_APP_OPEN_AD_ID.equals("0")) {
                                ((MyApplication) getApplication()).showAdIfAvailable(SplashActivity.this, this::dataIsReadys);
                            } else {
                                dataIsReadys();
                            }
                            break;
                        default:
                            dataIsReadys();
                            break;
                    }
                }, 1500);
            } else {
                dataIsReadys();
            }
        } else {
            dataIsReadys();
        }

    }

    private void dataIsReadys() {
        adsManager.loadAppOpenAd(true);
        if (YmgTools.isNetworkConnected(this)){
            dataIsReady();
        }else {
            dataIsReady();
            tools.onLoad(true,this);
        }

    }

    @Override
    public void onFinishButtonPressed() {
        getConfig();
//        tools.data();
        tools.onLoad(true,this);
    }


    private void getConfig() {

        adsPref.saveAds(
                Config.AD_STATUS,
                Config.AD_NETWORK,
                Config.BACKUP_AD_NETWORK,
                "",
                "",
                Config.ADMOB_BANNER_ID,
                Config.ADMOB_INTERSTITIAL_ID,
                Config.ADMOB_NATIVE_ID,
                Config.ADMOB_APP_OPEN_AD_ID,
                Config.GOOGLE_AD_MANAGER_BANNER_ID,
                Config.GOOGLE_AD_MANAGER_INTERSTITIAL_ID,
                Config.GOOGLE_AD_MANAGER_NATIVE_ID,
                Config.GOOGLE_AD_MANAGER_APP_OPEN_AD_ID,
                Config.FAN_BANNER_ID,
                Config.FAN_INTERSTITIAL_ID,
                Config.FAN_NATIVE_ID,
                Config.STARTAPP_APP_ID,
                Config.UNITY_GAME_ID,
                Config.UNITY_BANNER_ID,
                Config.UNITY_INTERSTITIAL_ID,
                Config.APPLOVIN_BANNER_ID,
                Config.APPLOVIN_INTERSTITIAL_ID,
                Config.APPLOVIN_NATIVE_MANUAL_ID,
                Config.APPLOVIN_APP_OPEN_AP_ID,
                Config.APPLOVIN_BANNER_ZONE_ID,
                Config.APPLOVIN_BANNER_MREC_ZONE_ID,
                Config.APPLOVIN_INTERSTITIAL_ZONE_ID,
                Config.IRONSOURCE_APP_KEY,
                Config.IRONSOURCE_BANNER_ID,
                Config.IRONSOURCE_INTERSTITIAL_ID,
                Config.WORTISE_APP_ID,
                Config.WORTISE_BANNER_ID,
                Config.WORTISE_INTERSTITIAL_ID,
                Config.WORTISE_NATIVE_ID,
                Config.WORTISE_APP_OPEN_AD_ID,
                Config.INTERSTITIAL_AD_INTERVAL,
                Config.NATIVE_AD_INDEX,
                Config.NATIVE_AD_INDEX
        );
    }



    @Override
    protected void onResume() {
        super.onResume();
        initCheck();
    }

    private void initCheck() {
        if (prf.loadNightModeState()==true){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }
}
