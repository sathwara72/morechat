package com.morechat.statusapp.Activity;

//This app is Created by Sathwara-InfoTech
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.morechat.statusapp.Adapter.PhotosAdapter;
import com.morechat.statusapp.Model.Quote;
import com.morechat.statusapp.Utils.AdsManager;
import com.morechat.statusapp.Utils.AdsPref;
import com.morechat.statusapp.Utils.DataBaseHandler;
import com.morechat.statusapp.Utils.PrefManager;
        import com.morechat.statusapp.R;

        import java.util.ArrayList;
import java.util.List;


public class QuotesActivity extends AppCompatActivity {

    private ArrayList<Quote> imageArry;
    private PhotosAdapter adapter;
    private String Activitytype;
    private DataBaseHandler db;
    private RecyclerView dataList;
    private ImageView noQuotes;
    private TextView NoQuotesText;
    private RelativeLayout noQuotesLayout;
    private Toolbar toolbar;
    private PrefManager prefManager;
    private AdsManager adsManager;
    private AdsPref adsPref;

    private final String TAG = QuotesActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quotes);
        //This app is Created by Sathwara-InfoTech
        prefManager = new PrefManager(this);

        adsPref = new AdsPref(this);
        adsManager = new AdsManager(this);
        adsManager.loadBannerAd(true);

        Toast.makeText(this, "Tap to Change Background", Toast.LENGTH_SHORT).show();

        String categoryValue = getIntent().getExtras()
                .getString("category");
        Activitytype = getIntent().getExtras().getString("mode");

        toolbar = findViewById(R.id.toolbar);
        if (Activitytype.equals("isFavorite")) {
            toolbar.setTitle(getResources().getText(R.string.menu_favorite));
        } else
            toolbar.setTitle(categoryValue);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        db = new DataBaseHandler(this);
        noQuotes = (ImageView)findViewById(R.id.NoQuotes);
        NoQuotesText = findViewById(R.id.NoQuotesText);
        noQuotesLayout = findViewById(R.id.noQuotesLayout);

        imageArry = new ArrayList<>();

        dataList = findViewById(R.id.quotesList);
        Button btnLoadMore = new Button(this);

        btnLoadMore.setBackgroundResource(R.drawable.ic_btn_bg);
        btnLoadMore.setText(getResources().getText(R.string.app_name));
        btnLoadMore.setTextColor(0xffffffff);
        Activitytype = getIntent().getExtras().getString("mode");

        if (Activitytype.equals("isCategory")) {
            categoryValue = getIntent().getExtras()
                    .getString("category");
            List<Quote> contacts = db.getQuotesByCategory(categoryValue);
            for (Quote cn : contacts) {

                imageArry.add(cn);

            }
        }

        if (Activitytype.equals("isAuthor")) {
            String authorValue = getIntent().getExtras().getString("name");
            List<Quote> contacts = db.getQuotesByAuthor(authorValue);
            for (Quote cn : contacts) {

                imageArry.add(cn);

            }
        }

        if (Activitytype.equals("isFavorite")) {
                toolbar.setTitle(getResources().getText(R.string.menu_favorite));
                List<Quote> contacts = db.getFavorites();
                for (Quote cn : contacts) {

                    imageArry.add(cn);
                }
            ;
            if (imageArry.isEmpty()){

                noQuotes.setVisibility(View.VISIBLE);
                NoQuotesText.setVisibility(View.VISIBLE);
                noQuotesLayout.setVisibility(View.VISIBLE);
            }

        }
        if (Activitytype.equals("allQuotes")) {

            List<Quote> contacts = db.getAllQuotes(" LIMIT 50");
            for (Quote cn : contacts) {

                imageArry.add(cn);

            }
            ;
            //This app is Created by Sathwara-InfoTech
        }


        dataList.setHasFixedSize(true);
        dataList.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PhotosAdapter(this, imageArry);

        dataList.setAdapter(adapter);


    }

    @Override
    public void onBackPressed()
    {
        finish();
        super.onBackPressed();  // optional depending on your needs
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        //This app is Created by Sathwara-InfoTech
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
