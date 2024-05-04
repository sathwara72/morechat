package com.morechat.statusapp.Activity;

import android.Manifest;
import android.app.Activity;;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.bumptech.glide.Glide;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.morechat.statusapp.Model.Quote;
import com.morechat.statusapp.Utils.AdsManager;
import com.morechat.statusapp.Utils.AdsPref;
import com.morechat.statusapp.Utils.DataBaseHandler;
import com.morechat.statusapp.Utils.PrefManager;
import com.morechat.statusapp.R;
import com.ymg.ymgdevelopers.YmgTools;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class QuoteActivity extends AppCompatActivity {

    private int ID;
    private String mode,fav;
    private Quote qte;
    private DataBaseHandler db;
    private ArrayList<Quote> myList = new ArrayList<Quote>();
    private TextView textAuth,textQuote, tv_quotes_watermark , tv_save_quote, tv_like_quote_text;
    private final String TAG = QuoteActivity.class.getSimpleName();
    private ImageView imgIcon , iv_save_quote;

    SharedPreferences sharedPrefs;
    Toolbar toolbar;
    LikeButton favBtn;
    LinearLayout ll_quote_save, ll_copy_quote, ll_quote_share;
    RelativeLayout relativeLayout;
    private int STORAGE_PERMISSION_CODE = 1;
    private int[] images;
    private int imagesIndex = 0;
    View darkView;
    AdsManager adsManager;
    private AdsPref adsPref;
    PrefManager prf;
    private PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quote);

        adsPref = new AdsPref(this);
        adsManager = new AdsManager(this);
        adsManager.loadBannerAd(true);
        adsManager.loadInterstitialAd(true,adsPref.getInterstitialAdInterval());

        prefManager = new PrefManager(this);

        ID = getIntent().getExtras().getInt("id");
        mode = getIntent().getExtras().getString("mode");

        toolbar  = findViewById(R.id.toolbar);

        if(mode.equals("qteday")){
           toolbar.setTitle("Quote of the Day");
        }
        else {
            toolbar.setTitle("Quote of the Day");
        }
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        db = new DataBaseHandler(this);
        prf = new PrefManager(this);

        textAuth = (TextView) findViewById(R.id.textAuth);
        textQuote = (TextView) findViewById(R.id.textQuote);
        imgIcon = (ImageView) findViewById(R.id.imgcon);
        darkView = findViewById(R.id.darkView);

        favBtn = findViewById(R.id.favBtn);
        ll_quote_save = findViewById(R.id.ll_quote_save);
        ll_copy_quote = findViewById(R.id.ll_copy_quote);
        ll_quote_share = findViewById(R.id.ll_quote_share);
        relativeLayout = findViewById(R.id.llBackground);
        tv_quotes_watermark = findViewById(R.id.tv_quotes_watermark);
        tv_save_quote = findViewById(R.id.tv_save_quote);
        iv_save_quote = findViewById(R.id.iv_save_quote);
        tv_like_quote_text = findViewById(R.id.tv_like_quote_text);

        //share image or Text Button
        ll_quote_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup();
                Toast.makeText(QuoteActivity.this, "Share", Toast.LENGTH_SHORT).show();
                startSound();
            }
        });

        //Copy Button
        ll_copy_quote.setOnClickListener(v -> {
            String text = qte.getQuote() + "- " + qte.getName() + "\n" + "https://play.google.com/store/apps/details?id=" + getPackageName();
            YmgTools.copyText(QuoteActivity.this,text);
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.copy_msg), Toast.LENGTH_LONG).show();
            startSound();
            adsManager.showInterstitialAd();
        });

        //Change Random Backgrounds
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int numOfImages = 41;
                images = new int[numOfImages];
                images[ 0 ] = R.drawable.img1;
                images[ 1 ] = R.drawable.img2;
                images[ 2 ] = R.drawable.img3;
                images[ 3 ] = R.drawable.gradient1;
                images[ 4 ] = R.drawable.img4;
                images[ 5 ] = R.drawable.img5;
                images[ 6 ] = R.drawable.img6;
                images[ 7 ] = R.drawable.gradient2;
                images[ 8 ] = R.drawable.img7;
                images[ 9 ] = R.drawable.img8;
                images[ 10 ] = R.drawable.img9;
                images[ 11 ] = R.drawable.gradient3;
                images[ 12 ] = R.drawable.img10;
                images[ 13 ] = R.drawable.img11;
                images[ 14 ] = R.drawable.img12;
                images[ 15 ] = R.drawable.gradient4;
                images[ 16 ] = R.drawable.img13;
                images[ 17 ] = R.drawable.img14;
                images[ 18 ] = R.drawable.img15;
                images[ 19 ] = R.drawable.gradient5;
                images[ 20 ] = R.drawable.img16;
                images[ 21 ] = R.drawable.img17;
                images[ 22 ] = R.drawable.img18;
                images[ 23 ] = R.drawable.gradient6;
                images[ 24 ] = R.drawable.img19;
                images[ 25 ] = R.drawable.img20;
                images[ 26 ] = R.drawable.img21;
                images[ 27 ] = R.drawable.gradient7;
                images[ 28 ] = R.drawable.img22;
                images[ 29 ] = R.drawable.img23;
                images[ 30 ] = R.drawable.img24;
                images[ 31 ] = R.drawable.gradient8;
                images[ 32 ] = R.drawable.img25;
                images[ 33 ] = R.drawable.img26;
                images[ 34 ] = R.drawable.img27;
                images[ 35 ] = R.drawable.gradient9;
                images[ 36 ] = R.drawable.img28;
                images[ 37 ] = R.drawable.img29;
                images[ 38 ] = R.drawable.img30;
                images[ 39 ] = R.drawable.gradient10;
                images[ 40 ] = R.drawable.img31;

                relativeLayout.setBackgroundResource(images[imagesIndex]);
                ++imagesIndex;  // update index, so that next time it points to next resource
                if (imagesIndex == 4 || imagesIndex == 8 || imagesIndex == 12 || imagesIndex == 16 || imagesIndex == 20
                        || imagesIndex == 24 || imagesIndex == 28 || imagesIndex == 32 || imagesIndex == 36 || imagesIndex == 40){
                    darkView.setVisibility(View.GONE);
                }else {
                    darkView.setVisibility(View.VISIBLE);
                }
                if (imagesIndex == images.length - 1)
                    imagesIndex = 0; // if we have reached at last index of array, simply restart from beginning
                allSound();
            }
        });


        //save Image Button
        ll_quote_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_quotes_watermark.setVisibility(View.VISIBLE);
                Bitmap bitmap = Bitmap.createBitmap(relativeLayout.getWidth(), relativeLayout.getHeight(),
                        Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                relativeLayout.draw(canvas);
                YmgTools.saveImage(QuoteActivity.this,bitmap,"best_status_"+System.currentTimeMillis());
                if (ContextCompat.checkSelfPermission(QuoteActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    YmgTools.saveImage(QuoteActivity.this,bitmap,"best_status_"+System.currentTimeMillis());
                }else{
                    requestStoragePermission();
                }
                adsManager.showInterstitialAd();
            }
        });

        Typeface fontQuote = Typeface.createFromAsset(getAssets(),
                "fonts/montserrat_bold.ttf");

        textQuote.setTypeface(fontQuote);


        ID = getIntent().getExtras().getInt("id");
        mode = getIntent().getExtras().getString("mode");

        if(mode.equals("qteday")){
            qte = db.getQuote(ID);

        }
        else {
            myList = (ArrayList<Quote>) getIntent().getSerializableExtra("array");
            qte = myList.get(ID);
        }


        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        if (date.equals(prf.getString("TodaysDate"))){

            textAuth.setText(prf.getString("quoteTodayCat"));
            textQuote.setText(prf.getString("quoteTodays"));

        }else {

            textAuth.setText(qte.getCategory());
            textQuote.setText(qte.getQuote());

            prf.setString("quoteTodays",qte.getQuote());
            prf.setString("quoteTodayCat", qte.getCategory());
            prf.setString("TodaysDate", date);
        }

        checkPicure();


        fav = qte.getFav();

        if (fav.equals("0")) {
            favBtn.setLiked(false);
            tv_like_quote_text.setText("Like");

        }
        if (fav.equals("1")) {
            favBtn.setLiked(true);
            tv_like_quote_text.setText("Liked");

        }

        //Like Button
        favBtn.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                setLikeBtn();
                startSound();
                tv_like_quote_text.setText("Liked");

            }

            @Override
            public void unLiked(LikeButton likeButton) {
                setLikeBtn();
                startSound();
                tv_like_quote_text.setText("Like");
            }
        });

        if(mode.equals("qteday")){
        }

    }


    private void popup() {
            PopupMenu popup = new PopupMenu(QuoteActivity.this, ll_quote_share);
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    int itemId = menuItem.getItemId();
                    if (itemId == R.id.sub_text) {
                        YmgTools.shareText(QuoteActivity.this, qte.getQuote() + "\n https://play.google.com/store/apps/details?id=" + getPackageName());
                        return true;
                    } else if (itemId == R.id.sub_image) {
                        tv_quotes_watermark.setVisibility(View.VISIBLE);
                        Bitmap bitmap = Bitmap.createBitmap(relativeLayout.getWidth(), relativeLayout.getHeight(),
                                Bitmap.Config.ARGB_8888);
                        Canvas canvas = new Canvas(bitmap);
                        relativeLayout.draw(canvas);
                        YmgTools.shareImageWithText(QuoteActivity.this, bitmap, "https://play.google.com/store/apps/details?id=" + getPackageName());
                        return true;
                    }
                    return false;
                }
            });
            popup.inflate(R.menu.menu_item);

            popup.show();
    }

    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale((Activity)this,Manifest.permission.READ_EXTERNAL_STORAGE)){

            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity)QuoteActivity.this,new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},STORAGE_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create().show();

        }else {
            ActivityCompat.requestPermissions((Activity)this,new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},STORAGE_PERMISSION_CODE);
        }
    }

    public void checkPicure(){
        boolean isExist = false;
        InputStream imageStream = null;
        try {
            imageStream = getAssets().open("categories/"+qte.getCategory()+".png");

            isExist =true;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        if (isExist != false){
            Bitmap theImage = BitmapFactory.decodeStream(imageStream);

            Glide.with(this)
                    .load(R.drawable.ic_menu_quotes)
                    .placeholder(R.drawable.ic_menu_quotes)
                    .into(imgIcon);
        }
        else {
            Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.ic_menu_quotes);
            Glide.with(this)
                    .load(bm)
                    .placeholder(R.drawable.ic_menu_quotes)
                    .into(imgIcon);
        }

    }

    //Like , Save , Copy , share - Sound Effect
    private void startSound() {
        MediaPlayer likeSound;
        likeSound = MediaPlayer.create(this, R.raw.water);
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        Boolean speaker = sharedPrefs.getBoolean("prefSpeaker", true);

        if (speaker.equals(true)) {

            likeSound.start();

        }else{

            likeSound.stop();
        }
    }

    //Sound Effect
    private void allSound() {
        MediaPlayer likeSound;
        likeSound = MediaPlayer.create(this, R.raw.all);
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        Boolean speaker = sharedPrefs.getBoolean("prefSpeaker", true);

        if (speaker.equals(true)) {

            likeSound.start();

        }else{

            likeSound.stop();
        }

    }

    private void setLikeBtn() {
        if (qte.getFav().equals("0")) {
            qte.setFav("1");
            db.updateQuote(qte);
            favBtn.setLiked(true);
        } else if (qte.getFav().equals("1")) {
            qte.setFav("0");
            db.updateQuote(qte);
            favBtn.setLiked(false);
        }
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
