package com.morechat.statusapp.Activity;

import static com.ymg.ads.sdk.util.Constant.ADMOB;
import static com.ymg.ads.sdk.util.Constant.APPLOVIN;
import static com.ymg.ads.sdk.util.Constant.APPLOVIN_MAX;
import static com.ymg.ads.sdk.util.Constant.GOOGLE_AD_MANAGER;
import static com.ymg.ads.sdk.util.Constant.WORTISE;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ProcessLifecycleOwner;
import androidx.multidex.MultiDex;

import com.morechat.statusapp.Config;
import com.morechat.statusapp.Utils.Constant;
import com.ymg.ads.sdk.format.AppOpenAdAppLovin;
import com.ymg.ads.sdk.format.AppOpenAdManager;
import com.ymg.ads.sdk.format.AppOpenAdMob;
import com.ymg.ads.sdk.format.AppOpenAdWortise;
import com.ymg.ads.sdk.util.OnShowAdCompleteListener;

@SuppressWarnings("ConstantConditions")
public class MyApplication extends Application {

    private AppOpenAdMob appOpenAdMob;
    private AppOpenAdManager appOpenAdManager;
    private AppOpenAdAppLovin appOpenAdAppLovin;
    private AppOpenAdWortise appOpenAdWortise;
    Activity currentActivity;

    @Override
    public void onCreate() {
        super.onCreate();
        if (Constant.FORCE_TO_SHOW_APP_OPEN_AD_ON_START) {
            registerActivityLifecycleCallbacks(activityLifecycleCallbacks);
            ProcessLifecycleOwner.get().getLifecycle().addObserver(lifecycleObserver);
            appOpenAdMob = new AppOpenAdMob();
            appOpenAdManager = new AppOpenAdManager();
            appOpenAdAppLovin = new AppOpenAdAppLovin();
            appOpenAdWortise = new AppOpenAdWortise();
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    LifecycleObserver lifecycleObserver = new DefaultLifecycleObserver() {
        @Override
        public void onStart(@NonNull LifecycleOwner owner) {
            DefaultLifecycleObserver.super.onStart(owner);
            if (Constant.isAppOpen) {
                if (Constant.OPEN_ADS_ON_RESUME) {
                    if (Config.AD_STATUS) {
                        switch (Config.AD_NETWORK) {
                            case ADMOB:
                                if (!Config.ADMOB_APP_OPEN_AD_ID.equals("0")) {
                                    if (!currentActivity.getIntent().hasExtra("unique_id")) {
                                        appOpenAdMob.showAdIfAvailable(currentActivity, Config.ADMOB_APP_OPEN_AD_ID);
                                    }
                                }
                                break;
                            case GOOGLE_AD_MANAGER:
                                if (!Config.GOOGLE_AD_MANAGER_APP_OPEN_AD_ID.equals("0")) {
                                    if (!currentActivity.getIntent().hasExtra("unique_id")) {
                                        appOpenAdManager.showAdIfAvailable(currentActivity, Config.GOOGLE_AD_MANAGER_APP_OPEN_AD_ID);
                                    }
                                }
                                break;
                            case APPLOVIN:
                            case APPLOVIN_MAX:
                                if (!Config.APPLOVIN_APP_OPEN_AP_ID.equals("0")) {
                                    if (!currentActivity.getIntent().hasExtra("unique_id")) {
                                        appOpenAdAppLovin.showAdIfAvailable(currentActivity, Config.APPLOVIN_APP_OPEN_AP_ID);
                                    }
                                }
                                break;

                            case WORTISE:
                                if (!Config.WORTISE_APP_OPEN_AD_ID.equals("0")) {
                                    if (!currentActivity.getIntent().hasExtra("unique_id")) {
                                        appOpenAdWortise.showAdIfAvailable(currentActivity, Config.WORTISE_APP_OPEN_AD_ID);
                                    }
                                }
                                break;
                        }
                    }
                }
            }
        }
    };

    ActivityLifecycleCallbacks activityLifecycleCallbacks = new ActivityLifecycleCallbacks() {
        @Override
        public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
        }

        @Override
        public void onActivityStarted(@NonNull Activity activity) {
            if (Constant.OPEN_ADS_ON_START) {
                if (Config.AD_STATUS) {
                    switch (Config.AD_NETWORK) {
                        case ADMOB:
                            if (!Config.ADMOB_APP_OPEN_AD_ID.equals("0")) {
                                if (!appOpenAdMob.isShowingAd) {
                                    currentActivity = activity;
                                }
                            }
                            break;
                        case GOOGLE_AD_MANAGER:
                            if (!Config.GOOGLE_AD_MANAGER_APP_OPEN_AD_ID.equals("0")) {
                                if (!appOpenAdManager.isShowingAd) {
                                    currentActivity = activity;
                                }
                            }
                            break;
                        case APPLOVIN:
                        case APPLOVIN_MAX:
                            if (!Config.APPLOVIN_APP_OPEN_AP_ID.equals("0")) {
                                if (!appOpenAdAppLovin.isShowingAd) {
                                    currentActivity = activity;
                                }
                            }
                            break;
                        case WORTISE:
                            if (!Config.WORTISE_APP_OPEN_AD_ID.equals("0")) {
                                if (!appOpenAdWortise.isShowingAd) {
                                    currentActivity = activity;
                                }
                            }
                            break;
                    }
                }
            }
        }

        @Override
        public void onActivityResumed(@NonNull Activity activity) {
        }

        @Override
        public void onActivityPaused(@NonNull Activity activity) {
        }

        @Override
        public void onActivityStopped(@NonNull Activity activity) {
        }

        @Override
        public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
        }

        @Override
        public void onActivityDestroyed(@NonNull Activity activity) {
        }
    };

    public void showAdIfAvailable(@NonNull Activity activity, @NonNull OnShowAdCompleteListener onShowAdCompleteListener) {
        if (Constant.OPEN_ADS_ON_START) {
            if (Config.AD_STATUS) {
                switch (Config.AD_NETWORK) {
                    case ADMOB:
                        if (!Config.ADMOB_APP_OPEN_AD_ID.equals("0")) {
                            appOpenAdMob.showAdIfAvailable(activity, Config.ADMOB_APP_OPEN_AD_ID, onShowAdCompleteListener);
                            Constant.isAppOpen = true;
                        }
                        break;
                    case GOOGLE_AD_MANAGER:
                        if (!Config.GOOGLE_AD_MANAGER_APP_OPEN_AD_ID.equals("0")) {
                            appOpenAdManager.showAdIfAvailable(activity, Config.GOOGLE_AD_MANAGER_APP_OPEN_AD_ID, onShowAdCompleteListener);
                            Constant.isAppOpen = true;
                        }
                        break;
                    case APPLOVIN:
                    case APPLOVIN_MAX:
                        if (!Config.APPLOVIN_APP_OPEN_AP_ID.equals("0")) {
                            appOpenAdAppLovin.showAdIfAvailable(activity, Config.APPLOVIN_APP_OPEN_AP_ID, onShowAdCompleteListener);
                            Constant.isAppOpen = true;
                        }
                        break;
                    case WORTISE:
                        if (!Config.WORTISE_APP_OPEN_AD_ID.equals("0")) {
                            appOpenAdWortise.showAdIfAvailable(activity, Config.WORTISE_APP_OPEN_AD_ID, onShowAdCompleteListener);
                            Constant.isAppOpen = true;
                        }
                        break;
                }
            }
        }
    }

}
