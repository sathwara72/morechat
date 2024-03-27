package com.morechat.statusapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatDelegate;

import com.morechat.statusapp.Utils.PrefManager;
import com.morechat.statusapp.R;
import com.ymg.ymgdevelopers.YmgActivity;
import com.ymg.ymgdevelopers.YmgOnboardItem;

import java.util.ArrayList;
import java.util.List;

public class OnBoardActivity extends YmgActivity {

    PrefManager prefManager;
    Button btnSkip;
    Button btnNext;
    Button btnFinish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefManager = new PrefManager(this);
        if (prefManager.loadNightModeState()==true){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        List<YmgOnboardItem> onboardItems = new ArrayList<>();

        YmgOnboardItem onboardItemList1 = new YmgOnboardItem();
        onboardItemList1.setTitle("Explore a Variety of Categories");
        onboardItemList1.setDescription("Discover a vast collection of quotes across numerous categories. Find the inspiration you need from love to motivation.");
        onboardItemList1.setImage(R.drawable.onboard_1);

        YmgOnboardItem onboardItemList2 = new YmgOnboardItem();
        onboardItemList2.setTitle("Customize Your Quotes Images");
        onboardItemList2.setDescription("Personalize your experience by selecting from the ultimate background images and customizing the appearance of your favorite quotes.");
        onboardItemList2.setImage(R.drawable.onboard_2);

        YmgOnboardItem onboardItemList3 = new YmgOnboardItem();
        onboardItemList3.setTitle("Create Your Own Quotes \nand Share");
        onboardItemList3.setDescription("Unlock the creativity within you. Use our quotes maker to craft your unique quotes and share them with the world.");
        onboardItemList3.setImage(R.drawable.onboard_3);


        onboardItems.add(onboardItemList1);
        onboardItems.add(onboardItemList2);
        onboardItems.add(onboardItemList3);

        setOnboardPagesReady(onboardItems);

        //setupOnBoardingItems();
        setupViewIdInit();
        setupIndicators();
        setCurrentIndicator(0);
        setupViewId();

        btnSkip= findViewById(com.ymg.ymgdevelopers.R.id.btnSkip);
        btnSkip.setAllCaps(false);
        btnSkip.setLetterSpacing(0.0f);
        btnNext= findViewById(com.ymg.ymgdevelopers.R.id.btnNext);
        btnNext.setAllCaps(false);
        btnNext.setLetterSpacing(0.0f);
        btnFinish= findViewById(com.ymg.ymgdevelopers.R.id.btnFinish);
        btnFinish.setAllCaps(false);
        btnFinish.setLetterSpacing(0.0f);
    }

    @Override
    public void onFinishButtonPressed() {
        startActivity(new Intent(this,SplashActivity.class));
        prefManager.setBoolean("first",false);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }
}