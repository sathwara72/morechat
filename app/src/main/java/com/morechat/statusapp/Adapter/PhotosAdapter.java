package com.morechat.statusapp.Adapter;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.morechat.statusapp.Model.Quote;
import com.morechat.statusapp.Utils.Constant;
import com.morechat.statusapp.Utils.PrefManager;
import com.ymg.ads.sdk.ui.MediumNativeAdView;
import com.morechat.statusapp.Activity.MakerActivity;
import com.morechat.statusapp.R;
import com.morechat.statusapp.Utils.AdsManager;
import com.morechat.statusapp.Utils.AdsPref;
import com.morechat.statusapp.Utils.DataBaseHandler;
import com.ymg.ymgdevelopers.YmgTools;
import java.util.List;
import java.util.Random;


public class PhotosAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ActivityCompat.OnRequestPermissionsResultCallback, AdsManager.RewardedAdListener{

    private Context context;
    private List<Quote> wallpaperList;
    private int[] images;
    private int[] font;
    private int imagesIndex = 0;
    private int fontIndex = 0;
    private int STORAGE_PERMISSION_CODE = 1;
    SharedPreferences sharedPrefs;
    PrefManager prf;
    private Boolean isAdLoaded = false;
    private final int VIEW_AD = 2;
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    AdsManager adsManager;
    AdsPref adsPref;
    int clickPos = -1;
    int backPos = -1;
    private Dialog dialog;


    public PhotosAdapter(Context context, List<Quote> wallpaperList) {
        this.context = context;
        this.wallpaperList = wallpaperList;
        adsPref = new AdsPref(context);
        adsManager = new AdsManager((Activity) context);
        adsManager.loadInterstitialAd(true,adsPref.getInterstitialAdInterval());
        adsManager.loadRewardedAd();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_quotes, parent, false);
            vh = new WallpaperViewHolder(v);
        } else if (viewType == VIEW_AD) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_ads, parent, false);
            vh = new ADViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_load_more, parent, false);
            vh = new ProgressViewHolder(v);
        }
        return vh;

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        if (holder instanceof WallpaperViewHolder) {


            final Quote picture = wallpaperList.get(position);

            if (clickPos == position){
                ((WallpaperViewHolder) holder).watermarkIcon.setVisibility(View.GONE);
                ((WallpaperViewHolder) holder).watermarkRelative.setVisibility(View.GONE);
            }else {
                ((WallpaperViewHolder) holder).watermarkIcon.setVisibility(View.VISIBLE);
                ((WallpaperViewHolder) holder).watermarkRelative.setVisibility(View.VISIBLE);
            }

            ((WallpaperViewHolder) holder).db = new DataBaseHandler(context);
            ((WallpaperViewHolder) holder).prf = new PrefManager(context);
            prf = new PrefManager(context);
            ((WallpaperViewHolder) holder).fav = picture.getFav();

            ((WallpaperViewHolder) holder).txtQuote.setText(picture.getQuote());

            ((WallpaperViewHolder) holder).fontChange.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int numOfImages = 16;
                    font = new int[numOfImages];
                    font[0] = 1;
                    font[1] = 2;
                    font[2] = 3;
                    font[3] = 4;
                    font[4] = 5;
                    font[5] = 6;
                    font[6] = 7;
                    font[7] = 8;
                    font[8] = 9;
                    font[9] = 10;
                    font[10] = 11;
                    font[11] = 12;
                    font[12] = 13;
                    font[13] = 14;
                    font[14] = 15;
                    font[15] = 16;

                    final Typeface fonts = Typeface.createFromAsset(context.getAssets(), "fonts/" + font[fontIndex] +".ttf");
                    ((WallpaperViewHolder) holder).txtQuote.setTypeface(fonts);
                    ++fontIndex;
                    allSound();
                    if (fontIndex == font.length - 1)
                        fontIndex = 0;
                }
            });


            //Remove watermark
            ((WallpaperViewHolder) holder).watermarkRelative.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick (View v) {
                    clickPos = position;
                    showRemoveDialog();
                }
                private void showRemoveDialog() {
                    dialog = new Dialog(context, R.style.DialogCustomTheme);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                    dialog.setContentView(R.layout.dialog_watermark);

                    LinearLayout mbtnWatch =dialog.findViewById(R.id.mbtnWatch);
                    LinearLayout mbtnNo = dialog.findViewById(R.id.mbtnNo);
                    mbtnWatch.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            adsManager.showRewardedAd(context, ((WallpaperViewHolder) holder).watermarkRelative,((WallpaperViewHolder) holder).watermarkIcon, dialog);
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
            });

            //Remove watermark
//            ((WallpaperViewHolder) holder).watermarkIcon.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick (View v) {
//
//                }
//            });


            //Quote Maker Button
            ((WallpaperViewHolder) holder).quote_maker.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick (View v) {
                    Intent favorites = new Intent(context,
                            MakerActivity.class);
                    favorites.putExtra("quote", picture.getQuote());
                    context.startActivity(favorites);
                    adsManager.showInterstitialAd();
                }
            });

            //Change Random Backgrounds
            ((WallpaperViewHolder) holder).relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick (View v) {
                    backPos = position;
                    int numOfImages = 41;
                    images = new int[ numOfImages ];
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

                    if (backPos == position){
                        Glide.with(context)
                                .load(images[ imagesIndex ])
                                .into(((WallpaperViewHolder) holder).llBackgroundImage);
                    }else {

                    }

                    ++imagesIndex;  // update index, so that next time it points to next resource

                    if (imagesIndex == 4 || imagesIndex == 8 || imagesIndex == 12 || imagesIndex == 16 || imagesIndex == 20
                            || imagesIndex == 24 || imagesIndex == 28 || imagesIndex == 32 || imagesIndex == 36 || imagesIndex == 40){
                        ((WallpaperViewHolder) holder).darkView.setVisibility(View.GONE);
                    }else {
                        ((WallpaperViewHolder) holder).darkView.setVisibility(View.VISIBLE);
                    }

                    if (imagesIndex == images.length - 1)
                        imagesIndex = 0; // app is created by Sathwara InfoTech
                    allSound();
                }
            });

            if (((WallpaperViewHolder) holder).fav.equals("0")) {
                //finalHolder.favBtn.setImageResource(R.mipmap.not_fav);
                ((WallpaperViewHolder) holder).favBtn.setImageResource(R.drawable.ic_menu_heart_black);
                ((WallpaperViewHolder) holder).likeText.setText("Like");

            }
            if (((WallpaperViewHolder) holder).fav.equals("1")) {
                //finalHolder.favBtn.setImageResource(R.mipmap.fav);
                ((WallpaperViewHolder) holder).favBtn.setImageResource(R.drawable.ic_menu_heart_red);
                ((WallpaperViewHolder) holder).likeText.setText("Liked");

            }

            ((WallpaperViewHolder) holder).favBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (picture.getFav().equals("0")) {
                        picture.setFav("1");
                        ((WallpaperViewHolder) holder).db.updateQuote(picture);
                        ((WallpaperViewHolder) holder).favBtn.setImageResource(R.drawable.ic_menu_heart_red);
                        ((WallpaperViewHolder) holder).likeText.setText("Liked");
                    } else if (picture.getFav().equals("1")) {
                        picture.setFav("0");
                        ((WallpaperViewHolder) holder).db.updateQuote(picture);
                        ((WallpaperViewHolder) holder).favBtn.setImageResource(R.drawable.ic_menu_heart_black);
                        ((WallpaperViewHolder) holder).likeText.setText("Like");
                    }
                    startSound();
                }
            });
//            ((WallpaperViewHolder) holder).favBtn.setOnLikeListener(new OnLikeListener() {
//                @Override
//                public void liked (LikeButton likeButton) {
//
//                    if (picture.getFav().equals("0")) {
//                        picture.setFav("1");
//                        ((WallpaperViewHolder) holder).db.updateQuote(picture);
//                        ((WallpaperViewHolder) holder).favBtn.setLiked(true);
//                        ((WallpaperViewHolder) holder).likeText.setText("Liked");
//                        startSound();
//                    } else if (picture.getFav().equals("1")) {
//                        picture.setFav("0");
//                        ((WallpaperViewHolder) holder).db.updateQuote(picture);
//                        ((WallpaperViewHolder) holder).favBtn.setLiked(false);
//                        ((WallpaperViewHolder) holder).likeText.setText("Like");
//
//                        startSound();
//                    }
//
//                }
//
//                @Override
//                public void unLiked (LikeButton likeButton) {
//
//                    if (picture.getFav().equals("0")) {
//                        picture.setFav("1");
//                        ((WallpaperViewHolder) holder).db.updateQuote(picture);
//                        ((WallpaperViewHolder) holder).favBtn.setLiked(true);
//                        ((WallpaperViewHolder) holder).likeText.setText("Liked");
//                    } else if (picture.getFav().equals("1")) {
//                        picture.setFav("0");
//                        ((WallpaperViewHolder) holder).db.updateQuote(picture);
//                        ((WallpaperViewHolder) holder).favBtn.setLiked(false);
//                        ((WallpaperViewHolder) holder).likeText.setText("Like");
//                        startSound();
//                    }
//
//                }
//            });

            //when you press save button
            ((WallpaperViewHolder) holder).ll_quote_save.setOnClickListener(v -> {


                Bitmap bitmap = Bitmap.createBitmap(((WallpaperViewHolder) holder).relativeLayout.getWidth(), ((WallpaperViewHolder) holder).relativeLayout.getHeight(),
                    Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                ((WallpaperViewHolder) holder).relativeLayout.draw(canvas);
                YmgTools.saveImage(context,bitmap,"best_status_"+System.currentTimeMillis());
                if (ContextCompat.checkSelfPermission(context,
                        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    YmgTools.saveImage(context,bitmap,"best_status_"+System.currentTimeMillis());
                } else {
                    requestStoragePermission();
                }
                adsManager.showInterstitialAd();
            });

            //When You Press copy Botton
            ((WallpaperViewHolder) holder).ll_copy_quote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick (View v) {
                    YmgTools.copyText(context,picture.getQuote());
                    startSound();
                    Toast.makeText(context, "Quotes Copied", Toast.LENGTH_SHORT).show();
                    adsManager.showInterstitialAd();
                }
            });

            //When You Press Share Button
            ((WallpaperViewHolder) holder).ll_quote_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick (View v) {
                    popup();
                    startSound();
                }

                private void popup () {
                    PopupMenu popup = new PopupMenu(context, ((WallpaperViewHolder) holder).ll_quote_share);
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick (MenuItem menuItem) {
                            int itemId = menuItem.getItemId();
                            if (itemId == R.id.sub_text) {
                                YmgTools.shareText(context, picture.getQuote() + "\n https://play.google.com/store/apps/details?id=" + context.getPackageName());
                                Toast.makeText(context, "Share as Text", Toast.LENGTH_SHORT).show();

                                return true;
                            } else if (itemId == R.id.sub_image) {
                                Bitmap bitmap = Bitmap.createBitmap(((WallpaperViewHolder) holder).relativeLayout.getWidth(), ((WallpaperViewHolder) holder).relativeLayout.getHeight(),
                                        Bitmap.Config.ARGB_8888);
                                Canvas canvas = new Canvas(bitmap);
                                ((WallpaperViewHolder) holder).relativeLayout.draw(canvas);
                                YmgTools.shareImageWithText(context, bitmap, "https://play.google.com/store/apps/details?id=" + context.getPackageName());
                                return true;
                            }
                            return false;
                        }
                    });
                    popup.inflate(R.menu.menu_item);

                    popup.show();
                    startSound();
                }
            });

        }else if (holder instanceof ADViewHolder){


            final Quote picture = wallpaperList.get(position);

            if (clickPos == position){
                ((ADViewHolder) holder).watermarkIcon.setVisibility(View.GONE);
                ((ADViewHolder) holder).watermarkRelative.setVisibility(View.GONE);
            }else {
                ((ADViewHolder) holder).watermarkIcon.setVisibility(View.VISIBLE);
                ((ADViewHolder) holder).watermarkRelative.setVisibility(View.VISIBLE);
            }

            ((ADViewHolder) holder).db = new DataBaseHandler(context);
            ((ADViewHolder) holder).prf = new PrefManager(context);
            prf = new PrefManager(context);
            ((ADViewHolder) holder).fav = picture.getFav();

            ((ADViewHolder) holder).txtQuote.setText(picture.getQuote());

            ((ADViewHolder) holder).fontChange.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int numOfImages = 16;
                    font = new int[numOfImages];
                    font[0] = 1;
                    font[1] = 2;
                    font[2] = 3;
                    font[3] = 4;
                    font[4] = 5;
                    font[5] = 6;
                    font[6] = 7;
                    font[7] = 8;
                    font[8] = 9;
                    font[9] = 10;
                    font[10] = 11;
                    font[11] = 12;
                    font[12] = 13;
                    font[13] = 14;
                    font[14] = 15;
                    font[15] = 16;

                    final Typeface fonts = Typeface.createFromAsset(context.getAssets(), "fonts/" + font[fontIndex] +".ttf");
                    ((ADViewHolder) holder).txtQuote.setTypeface(fonts);
                    ++fontIndex;
                    allSound();
                    if (fontIndex == font.length - 1)
                        fontIndex = 0;
                }
            });

            //Remove watermark
            ((ADViewHolder) holder).watermarkRelative.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick (View v) {
                    showRemoveDialog();
                }
                private void showRemoveDialog() {
                    dialog = new Dialog(context, R.style.DialogCustomTheme);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                    dialog.setContentView(R.layout.dialog_watermark);

                    LinearLayout mbtnWatch =dialog.findViewById(R.id.mbtnWatch);
                    LinearLayout mbtnNo = dialog.findViewById(R.id.mbtnNo);
                    mbtnWatch.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            adsManager.showRewardedAd(context, ((ADViewHolder) holder).watermarkRelative,((ADViewHolder) holder).watermarkIcon, dialog);
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
            });


            //Quote Maker Button
            ((ADViewHolder) holder).quote_maker.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick (View v) {
                    Intent favorites = new Intent(context,
                            MakerActivity.class);
                    favorites.putExtra("quote", picture.getQuote());
                    context.startActivity(favorites);
                    adsManager.showInterstitialAd();
                }
            });

            //Change Random Backgrounds
            ((ADViewHolder) holder).relativeLayout.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick (View v) {

                    int numOfImages = 41;
                    images = new int[ numOfImages ];
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


                    Glide.with(context)
                            .load(images[ imagesIndex ])
                            .into(((ADViewHolder) holder).llBackgroundImage);

                    ++imagesIndex;  // update index, so that next time it points to next resource

                    if (imagesIndex == 4 || imagesIndex == 8 || imagesIndex == 12 || imagesIndex == 16 || imagesIndex == 20
                            || imagesIndex == 24 || imagesIndex == 28 || imagesIndex == 32 || imagesIndex == 36 || imagesIndex == 40){
                        ((ADViewHolder) holder).darkView.setVisibility(View.GONE);
                    }else {
                        ((ADViewHolder) holder).darkView.setVisibility(View.VISIBLE);
                    }

                    if (imagesIndex == images.length - 1)
                        imagesIndex = 0; // if we have reached at last index of array, simply restart from beginning
                    allSound();
                }
            });

            if (((ADViewHolder) holder).fav.equals("0")) {
                //finalHolder.favBtn.setImageResource(R.mipmap.not_fav);
                ((ADViewHolder) holder).favBtn.setImageResource(R.drawable.ic_menu_heart_black);
                ((ADViewHolder) holder).likeText.setText("Like");

            }
            if (((ADViewHolder) holder).fav.equals("1")) {
                //finalHolder.favBtn.setImageResource(R.mipmap.fav);
                ((ADViewHolder) holder).favBtn.setImageResource(R.drawable.ic_menu_heart_red);
                ((ADViewHolder) holder).likeText.setText("Liked");

            }

            ((ADViewHolder) holder).favBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (picture.getFav().equals("0")) {
                        picture.setFav("1");
                        ((ADViewHolder) holder).db.updateQuote(picture);
                        ((ADViewHolder) holder).favBtn.setImageResource(R.drawable.ic_menu_heart_red);
                        ((ADViewHolder) holder).likeText.setText("Liked");
                    } else if (picture.getFav().equals("1")) {
                        picture.setFav("0");
                        ((ADViewHolder) holder).db.updateQuote(picture);
                        ((ADViewHolder) holder).favBtn.setImageResource(R.drawable.ic_menu_heart_black);
                        ((ADViewHolder) holder).likeText.setText("Like");
                    }
                    startSound();
                }
            });


//
//            if (((ADViewHolder) holder).fav.equals("0")) {
//                //finalHolder.favBtn.setImageResource(R.mipmap.not_fav);
//                ((ADViewHolder) holder).favBtn.setLiked(false);
//                ((ADViewHolder) holder).likeText.setText("Like");
//
//            }
//            if (((ADViewHolder) holder).fav.equals("1")) {
//                //finalHolder.favBtn.setImageResource(R.mipmap.fav);
//                ((ADViewHolder) holder).favBtn.setLiked(true);
//                ((ADViewHolder) holder).likeText.setText("Liked");
//
//            }
//
//
//            ((ADViewHolder) holder).favBtn.setOnLikeListener(new OnLikeListener() {
//                @Override
//                public void liked (LikeButton likeButton) {
//
//                    if (picture.getFav().equals("0")) {
//                        picture.setFav("1");
//                        ((ADViewHolder) holder).db.updateQuote(picture);
//                        ((ADViewHolder) holder).favBtn.setLiked(true);
//                        ((ADViewHolder) holder).likeText.setText("Liked");
//                        startSound();
//                    } else if (picture.getFav().equals("1")) {
//                        picture.setFav("0");
//                        ((ADViewHolder) holder).db.updateQuote(picture);
//                        ((ADViewHolder) holder).favBtn.setLiked(false);
//                        ((ADViewHolder) holder).likeText.setText("Like");
//
//                        startSound();
//                    }
//
//                }
//
//                @Override
//                public void unLiked (LikeButton likeButton) {
//
//                    if (picture.getFav().equals("0")) {
//                        picture.setFav("1");
//                        ((ADViewHolder) holder).db.updateQuote(picture);
//                        ((ADViewHolder) holder).favBtn.setLiked(true);
//                        ((ADViewHolder) holder).likeText.setText("Liked");
//                    } else if (picture.getFav().equals("1")) {
//                        picture.setFav("0");
//                        ((ADViewHolder) holder).db.updateQuote(picture);
//                        ((ADViewHolder) holder).favBtn.setLiked(false);
//                        ((ADViewHolder) holder).likeText.setText("Like");
//                        startSound();
//                    }
//
//                }
//            });

            //when you press save button

            ((ADViewHolder) holder).ll_quote_save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick (View v) {

                    Bitmap bitmap = Bitmap.createBitmap(((ADViewHolder) holder).relativeLayout.getWidth(), ((ADViewHolder) holder).relativeLayout.getHeight(),
                            Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(bitmap);
                    ((ADViewHolder) holder).relativeLayout.draw(canvas);
                    YmgTools.saveImage(context,bitmap,"best_status_"+System.currentTimeMillis());
                    if (ContextCompat.checkSelfPermission(context,
                            Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        YmgTools.saveImage(context,bitmap,"best_status_"+System.currentTimeMillis());
                    } else {
                        requestStoragePermission();
                    }
                    adsManager.showInterstitialAd();
                }
            });

            //When You Press copy Botton
            ((ADViewHolder) holder).ll_copy_quote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick (View v) {
                    YmgTools.copyText(context,picture.getQuote());
                    startSound();
                    Toast.makeText(context, "Quotes Copied", Toast.LENGTH_SHORT).show();
                    adsManager.showInterstitialAd();
                }
            });

            //When You Press Share Button
            ((ADViewHolder) holder).ll_quote_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick (View v) {
                    popup();
                    startSound();
                }

                private void popup () {
                    PopupMenu popup = new PopupMenu(context, ((ADViewHolder) holder).ll_quote_share);
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick (MenuItem menuItem) {
                            int itemId = menuItem.getItemId();
                            if (itemId == R.id.sub_text) {
                                YmgTools.shareText(context, picture.getQuote() + "\n https://play.google.com/store/apps/details?id=" + context.getPackageName());
                                Toast.makeText(context, "Share as Text", Toast.LENGTH_SHORT).show();
                                return true;
                            } else if (itemId == R.id.sub_image) {
                                Bitmap bitmap = Bitmap.createBitmap(((ADViewHolder) holder).relativeLayout.getWidth(), ((ADViewHolder) holder).relativeLayout.getHeight(),
                                        Bitmap.Config.ARGB_8888);
                                Canvas canvas = new Canvas(bitmap);
                                ((ADViewHolder) holder).relativeLayout.draw(canvas);
                                YmgTools.shareImageWithText(context, bitmap, "https://play.google.com/store/apps/details?id=" + context.getPackageName());
                                return true;
                            }
                            return false;
                        }
                    });
                    popup.inflate(R.menu.menu_item);

                    popup.show();
                    startSound();
                }
            });

            adsManager.loadNativeAdView(((ADViewHolder) holder).ads,true,"default");

            //((ADViewHolder) holder).bindNativeAd(context);

        }else {
        ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
    }
    }

    //Like , Save , Copy , share - Sound Effect
    private void startSound() {
        MediaPlayer likeSound;
        likeSound = MediaPlayer.create(context, R.raw.water);
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        Boolean speaker = sharedPrefs.getBoolean("prefSpeaker", true);

        if (prf.getBoolean("SOUND")==true) {
            if (speaker.equals(true)) {
                likeSound.start();
            } else {
                likeSound.stop();
            }
        }else {
        }
    }

    //Sound Effect
    private void allSound() {
        MediaPlayer likeSound;
        likeSound = MediaPlayer.create(context, R.raw.all);
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        Boolean speaker = sharedPrefs.getBoolean("prefSpeaker", true);

        if (prf.getBoolean("SOUND")==true) {

            if (speaker.equals(true)) {

                likeSound.start();

            } else {

                likeSound.stop();
            }
        }else {
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(context, "Permission ok", Toast.LENGTH_SHORT).show();
            }else
                Toast.makeText(context, "Permission not allow", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return wallpaperList.size();
    }

    public boolean isHeader(int position) {
        return position == wallpaperList.size();
    }

    @Override
    public void onRewardedAdCompleted() {

    }


    class WallpaperViewHolder extends RecyclerView.ViewHolder{

        TextView txtQuote;
        ImageView quotes_watermark, fontChange;
        TextView tv_save_quote;
        TextView likeText;
        TextView txtCategory;
        ImageView iv_save_quote, quote_maker, llBackgroundImage;
        RelativeLayout relativeLayout;
        LinearLayout ll_quote_save, ll_copy_quote, ll_quote_share;
        ImageView imgIcon;
        AppCompatImageView favBtn;
        String fav;
        View darkView;
        private DataBaseHandler db;
        PrefManager prf;
        private RelativeLayout watermarkRelative;
        private ImageView watermarkIcon;


        public WallpaperViewHolder(View itemView) {
            super(itemView);

            txtQuote = itemView.findViewById(R.id.txtQuote);
            relativeLayout = itemView.findViewById(R.id.llBackground);
            imgIcon = itemView.findViewById(R.id.imgIcon);
            likeText = itemView.findViewById(R.id.tv_like_quote_text);
            txtCategory = itemView.findViewById(R.id.txtCategory);
            ll_copy_quote = itemView.findViewById(R.id.ll_copy_quote);
            ll_quote_save = itemView.findViewById(R.id.ll_quote_save);
            ll_quote_share = itemView.findViewById(R.id.ll_quote_share);
            tv_save_quote = itemView.findViewById(R.id.tv_save_quote);
            iv_save_quote = itemView.findViewById(R.id.iv_save_quote);
            quote_maker = itemView.findViewById(R.id.quote_maker);
            darkView = itemView.findViewById(R.id.darkView);
            quotes_watermark = itemView.findViewById(R.id.quotes_watermark);
            favBtn = itemView.findViewById(R.id.favBtn);
            llBackgroundImage = itemView.findViewById(R.id.llBackgroundImage);
            fontChange = itemView.findViewById(R.id.fontChange);
            watermarkRelative = itemView.findViewById(R.id.watermarkRelative);
            watermarkIcon = itemView.findViewById(R.id.watermarkIcon);

        }
    }

    private static class ADViewHolder extends RecyclerView.ViewHolder {

        TextView txtQuote;
        ImageView quotes_watermark, llBackgroundImage, fontChange;
        TextView tv_save_quote;
        TextView likeText;
        TextView txtCategory;
        ImageView iv_save_quote, quote_maker;
        RelativeLayout relativeLayout;
        RelativeLayout watermarkRelative;
        private ImageView watermarkIcon;
        LinearLayout ll_quote_save, ll_copy_quote, ll_quote_share;
        ImageView imgIcon;
        AppCompatImageView favBtn;
        String fav;
        View darkView;
        private DataBaseHandler db;
        PrefManager prf;
        private LinearLayout nativeAdView;
        MediumNativeAdView ads;

        private ADViewHolder(View view) {
            super(view);

            txtQuote = view.findViewById(R.id.txtQuote);
            relativeLayout = view.findViewById(R.id.llBackground);
            imgIcon = view.findViewById(R.id.imgIcon);
            likeText = view.findViewById(R.id.tv_like_quote_text);
            txtCategory = view.findViewById(R.id.txtCategory);
            ll_copy_quote = view.findViewById(R.id.ll_copy_quote);
            ll_quote_save = view.findViewById(R.id.ll_quote_save);
            ll_quote_share = view.findViewById(R.id.ll_quote_share);
            tv_save_quote = view.findViewById(R.id.tv_save_quote);
            iv_save_quote = view.findViewById(R.id.iv_save_quote);
            quote_maker = view.findViewById(R.id.quote_maker);
            darkView = view.findViewById(R.id.darkView);
            quotes_watermark = view.findViewById(R.id.quotes_watermark);
            favBtn = view.findViewById(R.id.favBtn);
            llBackgroundImage = view.findViewById(R.id.llBackgroundImage);
            fontChange = view.findViewById(R.id.fontChange);
            watermarkRelative = view.findViewById(R.id.watermarkRelative);
            watermarkIcon = view.findViewById(R.id.watermarkIcon);
            ads = view.findViewById(R.id.mediumNativeAdView);


        }
    }

    @Override
    public int getItemViewType(int position) {
        if (wallpaperList.get(position) != null) {
            final AdsPref adsPref = new AdsPref(context);
            int LIMIT_NATIVE_AD = (Constant.MAX_NUMBER_OF_NATIVE_AD_DISPLAYED * adsPref.getNativeAdInterval()) + adsPref.getNativeAdIndex();
            for (int i = adsPref.getNativeAdIndex(); i < LIMIT_NATIVE_AD; i += adsPref.getNativeAdInterval()) {
                if (position == i) {
                    return VIEW_AD;
                }
            }
            return VIEW_ITEM;
        } else {
            return VIEW_PROG;
        }
    }


    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = v.findViewById(R.id.load_more);
        }
    }

    private void requestStoragePermission(){
        if (ActivityCompat.shouldShowRequestPermissionRationale((Activity)context,Manifest.permission.READ_EXTERNAL_STORAGE)){

            new AlertDialog.Builder(context)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity)context,new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},STORAGE_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create().show();

        }else {
            ActivityCompat.requestPermissions((Activity)context,new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},STORAGE_PERMISSION_CODE);
        }
    }
}
