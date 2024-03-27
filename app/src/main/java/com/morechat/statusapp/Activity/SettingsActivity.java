package com.morechat.statusapp.Activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import com.morechat.statusapp.Config;
import com.morechat.statusapp.Utils.PrefManager;
import com.morechat.statusapp.R;
import com.ymg.ymgdevelopers.YmgTools;


public class SettingsActivity extends AppCompatActivity {


    LinearLayout linearLayoutPolicyPrivacy;
    LinearLayout moreAppsBtn;
    LinearLayout followUsBtn;
    LinearLayout aboutUsBtn;

    Switch switchButtonNotification;
    Switch switchButtonSound;
    Switch switchDarkMode;
    PrefManager prf;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        //This app is Created by Sathwara-InfoTech
        Toolbar toolbar = findViewById(R.id.toolbar);
        setTitle(getResources().getString(R.string.action_settings));
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        prf = new PrefManager(this);


        linearLayoutPolicyPrivacy = findViewById(R.id.linearLayoutPolicyPrivacy);
        moreAppsBtn = findViewById(R.id.moreAppsBtn);
        followUsBtn = findViewById(R.id.followUsBtn);
        aboutUsBtn = findViewById(R.id.aboutUsBtn);

        switchButtonNotification = findViewById(R.id.switchButtonNotification);
        switchButtonSound = findViewById(R.id.switchTapSound);
        switchDarkMode = findViewById(R.id.switchDarkMode);

        TextView tvCurrentVersion = findViewById(R.id.tvCurrentVersion);
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            tvCurrentVersion.setText("V" + version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        moreAppsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                YmgTools.openUrl(SettingsActivity.this, Config.MORE_APP_URL);
            }
        });

        followUsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                YmgTools.openUrl(SettingsActivity.this, Config.SOCIAL_MEDIA_URL);
            }
        });

        aboutUsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAboutDialog();
            }
        });


        if (prf.getBoolean("SOUND")) {
            switchButtonSound.setChecked(true);
        } else {
            switchButtonSound.setChecked(false);
        }

        //Night Mode
        switchDarkMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public final void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked){
                    prf.setNightModeState(true);
                    onResume();
                }else {
                    prf.setNightModeState(false);
                    onResume();
                }
            }
        });

        //notification switch //This app is Created by Sathwara-InfoTech
        switchButtonSound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public final void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked){
                    prf.setBoolean("SOUND",true);
                }else {
                    prf.setBoolean("SOUND",false);
                }
            }
        });

        linearLayoutPolicyPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                startActivity(new Intent(SettingsActivity.this, PrivacyActivity.class));
            }
        });

    }

    private void showAboutDialog() {
        final Dialog dialog = new Dialog(SettingsActivity.this, R.style.DialogCustomTheme);
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
    public void onResume() {
        super.onResume();
       initCheck();
    }

    private void initCheck() {
        if (prf.loadNightModeState()==true){
            switchDarkMode.setChecked(true);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else {
            switchDarkMode.setChecked(false);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
        }
        return true;
    }
}