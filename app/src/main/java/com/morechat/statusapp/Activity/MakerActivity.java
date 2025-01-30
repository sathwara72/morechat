package com.morechat.statusapp.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.window.OnBackInvokedDispatcher;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.morechat.statusapp.Utils.AdsManager;
import com.morechat.statusapp.Utils.AdsPref;
import com.morechat.statusapp.Utils.PrefManager;
import com.morechat.statusapp.R;
import com.ymg.ymgdevelopers.YmgTools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import vadiole.colorpicker.ColorModel;
import vadiole.colorpicker.ColorPickerDialog;

public class MakerActivity extends AppCompatActivity {

    RelativeLayout tvSize, tvColor, tvFont, tvAlign, tvPadding, tvStyle, tvShadow;
    RelativeLayout tvGradiant, tvBackgroundColor, tvBackgroundImage, tvOpecity, tvSaveImage, tvShare;
    private final String TAG = MakerActivity.class.getSimpleName();
    private int CAMERA_PERMISSION_CODE = 1;
    private int STORAGE_PERMISSION_CODE = 1;
    CardView tvPrime;
    EditText tvMaker,tvMaker1;
    RelativeLayout rlBackground;
    AppCompatButton btnHeaderTitle,btnLite,btnDark;
//    ImageView backImage;
    CardView tvEsize, tvEfont, cvPadding, cvGradiant, cvOpacity;
    int shade = 0;
    int b0 = 20;
    int currentColor = 0xffffffff;
    int pLeft = 100;
    int pRight = 100;
    int pTop = 100;
    int pBottom = 100;
    int X;
    int Y;
    Uri uri;
    Boolean allreadyLoad = true;
    Boolean interLoad = true;
    View viewLayout;
    private int[] images;
    private int imagesIndex = 0;
    PrefManager prf;
    private AdsManager adsManager;
    private AdsPref adsPref;
//    private RelativeLayout watermarkRelative;
//    private ImageView watermarkIcon;
    private Dialog dialog;

    public static final int REQUEST_IMAGE = 100;
    private final OnBackPressedDispatcher onBackPressedDispatcher = getOnBackPressedDispatcher();

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maker);

        prf = new PrefManager(this);

        adsPref = new AdsPref(this);
        adsManager = new AdsManager(this);
        adsManager.loadBannerAd(true);
        adsManager.loadInterstitialAd(true, adsPref.getInterstitialAdInterval());
        adsManager.loadRewardedAd();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //extra
        tvEsize = findViewById(R.id.textSizeCardView);
        tvEfont = findViewById(R.id.textFontStyleCardView);
        cvPadding = findViewById(R.id.textPaddingCardView);
        cvGradiant = findViewById(R.id.gradientCardView);
        cvOpacity = findViewById(R.id.imageOpacityCardView);


        //main
        rlBackground = findViewById(R.id.idForSaving);
//        backImage = findViewById(R.id.imageView);
        tvMaker = findViewById(R.id.editText);
        tvMaker1 = findViewById(R.id.editText1);
        tvSize = findViewById(R.id.textSizeClickCardView);
        tvColor = findViewById(R.id.textColorClickCardView);
        tvFont = findViewById(R.id.textFontClickCardView);
        tvAlign = findViewById(R.id.textAlignClickCardView);
        tvPadding = findViewById(R.id.textPaddingClickCardView);
        tvStyle = findViewById(R.id.textStyleClickCardView);
        tvShadow = findViewById(R.id.textShadowClickCardView);
        tvGradiant = findViewById(R.id.gradientClickCardView);
        tvBackgroundColor = findViewById(R.id.bgColorClickCardView);
        tvBackgroundImage = findViewById(R.id.imageClickCardView);
        tvOpecity = findViewById(R.id.imageOpacityClickCirdView);
        tvSaveImage = findViewById(R.id.saveClickCardView);
        tvShare = findViewById(R.id.shareClickCardView);
        tvPrime = findViewById(R.id.aboutClickCardView);
        btnHeaderTitle = findViewById(R.id.btnHeaderTitle);
        btnLite = findViewById(R.id.btnLite);
        btnDark = findViewById(R.id.btnDark);
//        watermarkRelative = findViewById(R.id.watermarkRelative);
//        watermarkIcon = findViewById(R.id.watermarkIcon);


//        String quote = getIntent().getExtras().getString("quote");
//        imagesIndex = getIntent().getExtras().getInt("image");
//        if (quote != null) {
//            tvMaker.setText(quote);
//            if (imagesIndex == 0) {
////                backImage.setVisibility(View.GONE);
//            } else {
//                loadImages();
//                viewLayout.setVisibility(View.VISIBLE);
//                tvMaker.setTextColor(getResources().getColor(R.color.white));
//                tvMaker1.setTextColor(getResources().getColor(R.color.white));
//            }
//        } else {
//            tvMaker.setText("");
//        }

        btnHeaderTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tvMaker1.getVisibility() == View.VISIBLE){
                    tvMaker1.setVisibility(View.GONE);
                }else {
                    tvMaker1.setVisibility(View.VISIBLE);
                }
            }
        });

        btnLite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rlBackground.setBackgroundColor((Color.parseColor("#FFFFFF")));
                tvMaker.setTextColor((Color.parseColor("#000000")));
                tvMaker1.setTextColor((Color.parseColor("#000000")));
            }
        });

        btnDark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rlBackground.setBackgroundColor((Color.parseColor("#000000")));
                tvMaker.setTextColor((Color.parseColor("#FFFFFF")));
                tvMaker1.setTextColor((Color.parseColor("#FFFFFF")));
            }
        });

        //editText
        tvMaker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvMaker.setCursorVisible(true);
                tvMaker.setHint("");
            }
        });

        //textView size change // app is created by Sathwara InfoTech
        tvSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tvEsize.setVisibility(View.VISIBLE);
                SeekBar seekBar = (SeekBar) findViewById(R.id.textSizeSeekbar);
                seekBar.setProgress(b0);
                tvMaker.setTextSize((float) b0);
                tvMaker1.setTextSize((float) b0);
                seekBar.setOnSeekBarChangeListener(new seekbaralert());
                ((Button) findViewById(R.id.textSizeDoneButton)).setOnClickListener(new n());

            }
        });

        //textView Color // app is created by Sathwara InfoTech
        tvColor.setOnClickListener(v -> {
            //new AmbilWarnaDialog (MakerActivity.this, V, new u()).show();
            chooseTextColor(false);
        });

        //textView fonts
        tvFont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvEfont.setVisibility(View.VISIBLE);

                final Typeface a18 = Typeface.createFromAsset(getAssets(), "fonts/montserrat_bold.ttf");
                final Typeface a2 = Typeface.createFromAsset(getAssets(), "fonts/1.ttf");
                final Typeface a3 = Typeface.createFromAsset(getAssets(), "fonts/montserrat_bold.ttf");
                final Typeface a4 = Typeface.createFromAsset(getAssets(), "fonts/2.ttf");
                final Typeface a5 = Typeface.createFromAsset(getAssets(), "fonts/3.ttf");
                final Typeface a6 = Typeface.createFromAsset(getAssets(), "fonts/4.ttf");
                final Typeface a7 = Typeface.createFromAsset(getAssets(), "fonts/5.ttf");
                final Typeface a8 = Typeface.createFromAsset(getAssets(), "fonts/6.ttf");
                final Typeface a9 = Typeface.createFromAsset(getAssets(), "fonts/7.ttf");
                final Typeface a10 = Typeface.createFromAsset(getAssets(), "fonts/8.ttf");
                final Typeface a11 = Typeface.createFromAsset(getAssets(), "fonts/9.ttf");
                final Typeface a12 = Typeface.createFromAsset(getAssets(), "fonts/10.ttf");
                final Typeface a13 = Typeface.createFromAsset(getAssets(), "fonts/11.ttf");
                final Typeface a14 = Typeface.createFromAsset(getAssets(), "fonts/12.ttf");
                final Typeface a15 = Typeface.createFromAsset(getAssets(), "fonts/13.ttf");
                final Typeface a16 = Typeface.createFromAsset(getAssets(), "fonts/14.ttf");
                final Typeface a17 = Typeface.createFromAsset(getAssets(), "fonts/15.ttf");

                TextView textView = (TextView) findViewById(R.id.fontButton1);
                TextView textView2 = (TextView) findViewById(R.id.fontButton2);
                TextView textView3 = (TextView) findViewById(R.id.fontButton3);
                TextView textView4 = (TextView) findViewById(R.id.fontButton4);
                TextView textView5 = (TextView) findViewById(R.id.fontButton5);
                TextView textView6 = (TextView) findViewById(R.id.fontButton6);
                TextView textView7 = (TextView) findViewById(R.id.fontButton7);
                TextView textView8 = (TextView) findViewById(R.id.fontButton8);
                TextView textView9 = (TextView) findViewById(R.id.fontButton9);
                TextView textView10 = (TextView) findViewById(R.id.fontButton10);
                TextView textView11 = (TextView) findViewById(R.id.fontButton11);
                TextView textView12 = (TextView) findViewById(R.id.fontButton12);
                TextView textView13 = (TextView) findViewById(R.id.fontButton13);
                TextView textView14 = (TextView) findViewById(R.id.fontButton14);
                TextView textView15 = (TextView) findViewById(R.id.fontButton15);
                TextView textView16 = (TextView) findViewById(R.id.fontButton16);
                Button button = (Button) findViewById(R.id.textFontStyleDoneButton);
                textView.setTypeface(a2);
                textView2.setTypeface(a3);
                textView3.setTypeface(a4);
                textView4.setTypeface(a5);
                textView5.setTypeface(a6);
                textView6.setTypeface(a7);
                textView7.setTypeface(a8);
                textView8.setTypeface(a9);
                textView9.setTypeface(a10);
                textView10.setTypeface(a11);
                textView11.setTypeface(a12);
                textView12.setTypeface(a13);
                textView13.setTypeface(a14);
                textView14.setTypeface(a15);
                textView15.setTypeface(a16);
                textView16.setTypeface(a17);
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tvMaker.setTypeface(a2);
                        tvMaker1.setTypeface(a2);
                    }
                });
                textView2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tvMaker.setTypeface(a3);
                        tvMaker1.setTypeface(a3);
                    }
                });
                textView3.setOnClickListener(new View.OnClickListener() {
                    @Override // app is created by Sathwara InfoTech
                    public void onClick(View v) {
                        tvMaker.setTypeface(a4);
                        tvMaker1.setTypeface(a4);
                    }
                });
                textView4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tvMaker.setTypeface(a5);
                        tvMaker1.setTypeface(a5);
                    }
                });
                textView5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tvMaker.setTypeface(a6);
                        tvMaker1.setTypeface(a6);
                    }
                });
                textView6.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tvMaker.setTypeface(a7);
                        tvMaker1.setTypeface(a7);
                    }
                });
                textView7.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tvMaker.setTypeface(a8);
                        tvMaker1.setTypeface(a8);
                    }
                });
                textView8.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tvMaker.setTypeface(a9);
                        tvMaker1.setTypeface(a9);
                    }
                });
                textView9.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tvMaker.setTypeface(a10);
                        tvMaker1.setTypeface(a10);
                    }
                });
                textView10.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tvMaker.setTypeface(a11);
                        tvMaker1.setTypeface(a11);
                    }
                });
                textView11.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tvMaker.setTypeface(a12);
                        tvMaker1.setTypeface(a12);
                    }
                });
                textView12.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tvMaker.setTypeface(a13);
                        tvMaker1.setTypeface(a13);
                    }
                });
                textView13.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tvMaker.setTypeface(a14);
                        tvMaker1.setTypeface(a14);
                    }
                });
                textView14.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tvMaker.setTypeface(a15);
                        tvMaker1.setTypeface(a15);
                    }
                });
                textView15.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tvMaker.setTypeface(a16);
                        tvMaker1.setTypeface(a16);
                    }
                });
                textView16.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tvMaker.setTypeface(a17);
                        tvMaker1.setTypeface(a17);
                    }
                });
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tvEfont.setVisibility(View.INVISIBLE);
                    }
                });

            }
        });

        //textView alignmnet // app is created by Sathwara InfoTech
        tvAlign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i2;
                ImageView imageView = (ImageView) findViewById(R.id.textAligmentImageView);
                if (String.valueOf(tvMaker.getGravity()).equals("17")) {
                    tvMaker.setGravity(16);
                    tvMaker1.setGravity(16);
                    i2 = R.drawable.ic_menu_text_align;
                } else if (String.valueOf(tvMaker.getGravity()).equals("8388627")) {
                    tvMaker.setGravity(21);
                    tvMaker1.setGravity(21);
                    i2 = R.drawable.ic_menu_text_align;
                } else if (String.valueOf(tvMaker.getGravity()).equals("21")) {
                    tvMaker.setGravity(17);
                    tvMaker1.setGravity(17);
                    i2 = R.drawable.ic_menu_text_align;
                } else {
                    return;
                }
                imageView.setImageResource(i2);

            }
        });

        //textView padding
        tvPadding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cvPadding.setVisibility(View.VISIBLE);
                SeekBar seekBar = (SeekBar) findViewById(R.id.leftPaddingSeekbar);
                SeekBar seekBar2 = (SeekBar) findViewById(R.id.rightPaddingSeekbar);
                SeekBar seekBar3 = (SeekBar) findViewById(R.id.topPaddingSeekbar);
                SeekBar seekBar4 = (SeekBar) findViewById(R.id.bottomPaddingSeekbar);
                seekBar.setProgress(pLeft / 2);
                seekBar2.setProgress(pRight / 2);
                seekBar3.setProgress(pTop / 2);
                seekBar4.setProgress(pBottom / 2);
                tvMaker.setPadding(pLeft, pRight, pTop, pBottom);
                tvMaker1.setPadding(pLeft, pRight, pTop, pBottom);
                seekBar.setOnSeekBarChangeListener(new paddingLeft());
                seekBar2.setOnSeekBarChangeListener(new paddingRight());
                seekBar3.setOnSeekBarChangeListener(new paddingTop());
                seekBar4.setOnSeekBarChangeListener(new paddingBottom());
                ((Button) findViewById(R.id.textPaddingDoneButton)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cvPadding.setVisibility(View.INVISIBLE);
                    }
                });

            }

            class paddingLeft implements SeekBar.OnSeekBarChangeListener {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    pLeft = progress * 2;
                    tvMaker.setPadding(pLeft, pRight, pTop, pBottom);
                    tvMaker1.setPadding(pLeft, pRight, pTop, pBottom);

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            }

            class paddingRight implements SeekBar.OnSeekBarChangeListener {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    pRight = progress * 2;
                    tvMaker.setPadding(pLeft, pRight, pTop, pBottom);
                    tvMaker1.setPadding(pLeft, pRight, pTop, pBottom);

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            }

            class paddingTop implements SeekBar.OnSeekBarChangeListener {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    pTop = progress * 2;
                    tvMaker.setPadding(pLeft, pRight, pTop, pBottom);
                    tvMaker1.setPadding(pLeft, pRight, pTop, pBottom);

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            }

            class paddingBottom implements SeekBar.OnSeekBarChangeListener {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    pBottom = progress * 2;
                    tvMaker.setPadding(pLeft, pRight, pTop, pBottom);
                    tvMaker1.setPadding(pLeft, pRight, pTop, pBottom);

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            }
        });

        //watermark

        //textView Style
        tvStyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView = (TextView) findViewById(R.id.textStyleTextView);
                tvMaker.setTypeface(tvMaker.getTypeface(), Typeface.NORMAL);
                if (tvMaker.getTypeface() == null) {
                    textView.setTypeface(tvMaker.getTypeface(), Typeface.BOLD);
                    textView.setText("B");
                    tvMaker.setTypeface(tvMaker.getTypeface(), Typeface.BOLD);
                } else if (tvMaker.getTypeface().getStyle() == Typeface.NORMAL) {
                    textView.setTypeface(null, Typeface.BOLD);
                    textView.setText("B");
                    tvMaker.setTypeface(null, Typeface.BOLD);
                } else if (tvMaker.getTypeface().getStyle() == Typeface.BOLD) {
                    textView.setTypeface(null, Typeface.BOLD_ITALIC);
                    textView.setText("I");
                    tvMaker.setTypeface(null, Typeface.BOLD_ITALIC);
                } else if (tvMaker.getTypeface().getStyle() == Typeface.BOLD_ITALIC) {
                    textView.setTypeface(null, Typeface.NORMAL);
                    textView.setText("N");
                    tvMaker.setTypeface(null, Typeface.NORMAL);
                }

            }
        });

        //textView Shadow
        tvShadow.setOnClickListener(v -> {
            shade++;
            int i = shade % 2;
            EditText editText = tvMaker;
            if (i == 0) {
                editText.setShadowLayer(0.0f, 0.0f, 0.0f, 0);
            } else {
                editText.setShadowLayer(6.5f, -1.0f, -4.0f, -7829368);
            }
        });

        //text background gradiant
        tvGradiant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cvGradiant.setVisibility(View.VISIBLE);
                final Button button = (Button) findViewById(R.id.gradientFirstColorButton);
                final Button button2 = (Button) findViewById(R.id.gradientSecoundColorButton);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        // Create and show Color Picker dialog
                        ColorPickerDialog colorPicker = new ColorPickerDialog.Builder()
                                .setInitialColor(currentColor) // Set the initial selected color
                                .setColorModel(ColorModel.RGB) // Set color model (ARGB, RGB, AHSV, or HSV)
                                .setColorModelSwitchEnabled(true) // Allow switching between color models
                                .setButtonOkText(android.R.string.ok) // OK button text
                                .setButtonCancelText(android.R.string.cancel) // Cancel button text
                                .onColorSelected(color -> {
                                    // Handle selected color
                                    System.out.println("Selected color: #" + Integer.toHexString(color).toUpperCase());
                                    X = color;
                                    button.setBackgroundColor(X);
                                    GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{X, Y});
                                    gradientDrawable.setCornerRadius(0.0f);
                                    rlBackground.setBackground(gradientDrawable);
                                })
                                .create();

                        // Show dialog
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        colorPicker.show(fragmentManager, "color_picker");

//                        ColorPickerDialogBuilder
//                                .with(MakerActivity.this)
//                                .setTitle("Choose color")
//                                .initialColor(currentColor)
//                                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
//                                .density(12)
//
//                                .setOnColorSelectedListener(new OnColorSelectedListener() {
//                                    @Override
//                                    public void onColorSelected(int selectedColor) {
//
//
//                                    }
//                                })
//                                .setPositiveButton("ok", new ColorPickerClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
//                                        X = selectedColor;
//                                        button.setBackgroundColor(X);
//                                        GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{X,Y});
//                                        gradientDrawable.setCornerRadius(0.0f);
//                                        rlBackground.setBackground(gradientDrawable);
//                                    }
//                                })
//                                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                    }
//                                })
//                                .build()
//                                .show();
                    }
                });

                button2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        // Create and show Color Picker dialog
                        ColorPickerDialog colorPicker = new ColorPickerDialog.Builder()
                                .setInitialColor(currentColor) // Set the initial selected color
                                .setColorModel(ColorModel.RGB) // Set color model (ARGB, RGB, AHSV, or HSV)
                                .setColorModelSwitchEnabled(true) // Allow switching between color models
                                .setButtonOkText(android.R.string.ok) // OK button text
                                .setButtonCancelText(android.R.string.cancel) // Cancel button text
                                .onColorSelected(color -> {
                                    // Handle selected color
                                    Y = color;
                                    button2.setBackgroundColor(Y);
                                    GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{X, Y});
                                    gradientDrawable.setCornerRadius(0.0f);
                                    rlBackground.setBackground(gradientDrawable);
                                })
                                .create();

                        // Show dialog
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        colorPicker.show(fragmentManager, "color_picker");

//                        ColorPickerDialogBuilder
//                                .with(MakerActivity.this)
//                                .setTitle("Choose color")
//                                .initialColor(currentColor)
//                                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
//                                .density(12)
//
//                                .setOnColorSelectedListener(new OnColorSelectedListener() {
//                                    @Override
//                                    public void onColorSelected(int selectedColor) {
//
//
//                                    }
//                                })
//                                .setPositiveButton("ok", new ColorPickerClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
//                                        Y = selectedColor;
//                                        button2.setBackgroundColor(Y);
//                                        GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{X,Y});
//                                        gradientDrawable.setCornerRadius(0.0f);
//                                        rlBackground.setBackground(gradientDrawable);
//                                    }
//                                })
//                                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                    }
//                                })
//                                .build()
//                                .show();
                    }
                });
                ((Button) findViewById(R.id.gradientDoneButton)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cvGradiant.setVisibility(View.INVISIBLE);
//                        backImage.setVisibility(View.INVISIBLE);

                    }
                });

            }
        });

        //textView Background Color
        tvBackgroundColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseBackgroundColor(false);
            }
        });

        //textView background image
        tvBackgroundImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //CropImage.startPickImageActivity(MakerActivity.this);
                showImagePickerOptions();
            }
        });

        //images opecity
        tvOpecity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cvOpacity.setVisibility(View.VISIBLE);
                ((SeekBar) findViewById(R.id.imageOpacitySeekbar)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                        if (backImage.getVisibility() != View.VISIBLE) {
//                            seekBar.setClickable(false);
//                            Toast.makeText(MakerActivity.this, "select image first", Toast.LENGTH_SHORT).show();
//                        } else if (progress < 5) {
//                            backImage.setAlpha(1.0f);
//                            backImage.setColorFilter((ColorFilter) null);
//                        } else if (progress <= 95) {
//                            float floatValue = Float.valueOf("." + String.valueOf(progress)).floatValue();
//                            backImage.setColorFilter(R.color.black);
//                            backImage.setAlpha(floatValue);
//                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });
                ((Button) findViewById(R.id.imageOpacityDoneButton)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cvOpacity.setVisibility(View.INVISIBLE);
                    }
                });

            }
        });

        //save image
        tvSaveImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvMaker.setCursorVisible(false);
                Bitmap bitmap = Bitmap.createBitmap(rlBackground.getWidth(), rlBackground.getHeight(),
                        Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                rlBackground.draw(canvas);
                YmgTools.saveImage(MakerActivity.this, bitmap, "best_status_" + System.currentTimeMillis());
                if (ContextCompat.checkSelfPermission(MakerActivity.this,
                        Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED) {
                    com.ymg.ymgdevelopers.PrefManager prefManager = new com.ymg.ymgdevelopers.PrefManager(getApplicationContext());
                    prefManager.setString("dev","YMG-Developers");
                    YmgTools.saveImage(MakerActivity.this, bitmap, "best_status_" + System.currentTimeMillis());
                } else {
                    requestStoragePermission();
                    Toast.makeText(MakerActivity.this, "Please Allow Storage Permission", Toast.LENGTH_SHORT).show();
                }
                adsManager.showInterstitialAd();
                tvMaker.setCursorVisible(true);
            }
        });


        //share images
        tvShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvMaker.setCursorVisible(false);
                Bitmap bitmap = Bitmap.createBitmap(rlBackground.getWidth(), rlBackground.getHeight(),
                        Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                rlBackground.draw(canvas);
                YmgTools.shareImageWithText(MakerActivity.this, bitmap, "https://play.google.com/store/apps/details?id=" + getPackageName());
                tvMaker.setCursorVisible(true);
                adsManager.showInterstitialAd();
            }
        });

//        watermarkRelative.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showRemoveDialog();
//            }
//        });

        toolbar.setNavigationOnClickListener(v -> onBackPress());

        onBackPressedDispatcher.addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                onBackPress();
            }
        });

    }

    private void showRemoveDialog() {
        dialog = new Dialog(this, R.style.DialogCustomTheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.dialog_watermark);

        LinearLayout mbtnWatch = dialog.findViewById(R.id.mbtnWatch);
        LinearLayout mbtnNo = dialog.findViewById(R.id.mbtnNo);
        mbtnWatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                adsManager.showRewardedAd(MakerActivity.this, watermarkRelative, watermarkIcon, dialog);
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

    private void showImagePickerOptions() {
        ImagePickerActivity.showImagePickerOptions(this, new ImagePickerActivity.PickerOptionListener() {
            @Override
            public void onTakeCameraSelected() {
                launchCameraIntent();
            }

            @Override
            public void onChooseGallerySelected() {
                launchGalleryIntent();
            }
        });
    }

    ActivityResultLauncher<Intent> showImagePickerActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    try {
                        assert result.getData() != null;
                        Uri uri = result.getData().getParcelableExtra("path");
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(MakerActivity.this.getContentResolver(), uri);
                        assert uri != null;
                        loadProfile(uri.toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

    private void launchCameraIntent() {
        if (ContextCompat.checkSelfPermission(MakerActivity.this,
                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {

            Intent intent = new Intent(MakerActivity.this, ImagePickerActivity.class);
            intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);

            // setting aspect ratio
            intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
            intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
            intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);

            // setting maximum bitmap width and height
            intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
            intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000);
            intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000);
            showImagePickerActivityResultLauncher.launch(intent);
        } else {
            requestCameraPermission();
        }
    }

    private void launchGalleryIntent() {
        if (ContextCompat.checkSelfPermission(MakerActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(MakerActivity.this, ImagePickerActivity.class);
            intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);

            // setting aspect ratio
            intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
            intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
            intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
            showImagePickerActivityResultLauncher.launch(intent);
        } else {
            requestStoragePermission();
        }
    }

    private void requestStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 33+
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_MEDIA_IMAGES)) {
                new AlertDialog.Builder(this)
                        .setTitle("Permission needed")
                        .setMessage("This permission is needed to access media files.")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(MakerActivity.this,
                                        new String[]{Manifest.permission.READ_MEDIA_IMAGES,
                                                Manifest.permission.READ_MEDIA_VIDEO,
                                                Manifest.permission.READ_MEDIA_AUDIO},
                                        STORAGE_PERMISSION_CODE);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create().show();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_MEDIA_IMAGES,
                                Manifest.permission.READ_MEDIA_VIDEO,
                                Manifest.permission.READ_MEDIA_AUDIO},
                        STORAGE_PERMISSION_CODE);
            }
        } else { // For Android 32 and below
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                new AlertDialog.Builder(this)
                        .setTitle("Permission needed")
                        .setMessage("This permission is needed to access media files.")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(MakerActivity.this,
                                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                        STORAGE_PERMISSION_CODE);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create().show();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        STORAGE_PERMISSION_CODE);
            }
        }
    }

    private void requestCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) this, Manifest.permission.CAMERA)) {
            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed")
                    .setPositiveButton("Ok", (dialog, which) -> ActivityCompat.requestPermissions((Activity) MakerActivity.this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE))
                    .setNegativeButton("cancel", (dialog, which) -> dialog.dismiss()).create().show();

        } else {
            ActivityCompat.requestPermissions((Activity) this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        }
    }

    private void loadProfile(String url) {
        Log.d(TAG, "Image cache path: " + url);
//        backImage.setVisibility(View.VISIBLE);
//        Glide.with(this)
//                .load(url)
//                .into(backImage);
    }

    private void loadImages() {
        int numOfImages = 41;
        images = new int[numOfImages];
        images[0] = R.drawable.img1;
        images[1] = R.drawable.img2;
        images[2] = R.drawable.img3;
        images[3] = R.drawable.gradient1;
        images[4] = R.drawable.img4;
        images[5] = R.drawable.img5;
        images[6] = R.drawable.img6;
        images[7] = R.drawable.gradient2;
        images[8] = R.drawable.img7;
        images[9] = R.drawable.img8;
        images[10] = R.drawable.img9;
        images[11] = R.drawable.gradient3;
        images[12] = R.drawable.img10;
        images[13] = R.drawable.img11;
        images[14] = R.drawable.img12;
        images[15] = R.drawable.gradient4;
        images[16] = R.drawable.img13;
        images[17] = R.drawable.img14;
        images[18] = R.drawable.img15;
        images[19] = R.drawable.gradient5;
        images[20] = R.drawable.img16;
        images[21] = R.drawable.img17;
        images[22] = R.drawable.img18;
        images[23] = R.drawable.gradient6;
        images[24] = R.drawable.img19;
        images[25] = R.drawable.img20;
        images[26] = R.drawable.img21;
        images[27] = R.drawable.gradient7;
        images[28] = R.drawable.img22;
        images[29] = R.drawable.img23;
        images[30] = R.drawable.img24;
        images[31] = R.drawable.gradient8;
        images[32] = R.drawable.img25;
        images[33] = R.drawable.img26;
        images[34] = R.drawable.img27;
        images[35] = R.drawable.gradient9;
        images[36] = R.drawable.img28;
        images[37] = R.drawable.img29;
        images[38] = R.drawable.img30;
        images[39] = R.drawable.gradient10;
        images[40] = R.drawable.img31;

//        backImage.setBackgroundResource(images[imagesIndex]);
    }


    //Share image tool
    private Uri getLocalBitmapUri(Bitmap bitmap) {
        Uri bmpUri = null;
        try {
            File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                    "wallpaper" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.close();
            bmpUri = FileProvider.getUriForFile(this, getPackageName() + ".provider", file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }


    //choose text colors
    private void chooseTextColor(boolean supportAlpha) {
        // Create and show Color Picker dialog
        ColorPickerDialog colorPicker = new ColorPickerDialog.Builder()
                .setInitialColor(currentColor) // Set the initial selected color
                .setColorModel(ColorModel.RGB) // Set color model (ARGB, RGB, AHSV, or HSV)
                .setColorModelSwitchEnabled(true) // Allow switching between color models
                .setButtonOkText(android.R.string.ok) // OK button text
                .setButtonCancelText(android.R.string.cancel) // Cancel button text
                .onColorSelected(color -> {
                    // Handle selected color
                    currentColor = color;
                    tvMaker.setTextColor(color);
                    tvMaker1.setTextColor(color);
                })
                .create();

        // Show dialog
        FragmentManager fragmentManager = getSupportFragmentManager();
        colorPicker.show(fragmentManager, "color_picker");

//        ColorPickerDialogBuilder
//                .with(this)
//                .setTitle("Choose color")
//                .initialColor(currentColor)
//                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
//                .density(12)
//
//                .setOnColorSelectedListener(new OnColorSelectedListener() {
//                    @Override
//                    public void onColorSelected(int selectedColor) {
//
//
//                    }
//                })
//                .setPositiveButton("ok", new ColorPickerClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
//                        currentColor = selectedColor;
//                        tvMaker.setTextColor(selectedColor);
//                    }
//                })
//                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                    }
//                })
//                .build()
//                .show();
    }

    //choose text colors - app is created by Sathwara InfoTech
    private void chooseBackgroundColor(boolean supportAlpha) {
        // Create and show Color Picker dialog
        ColorPickerDialog colorPicker = new ColorPickerDialog.Builder()
                .setInitialColor(currentColor) // Set the initial selected color
                .setColorModel(ColorModel.RGB) // Set color model (ARGB, RGB, AHSV, or HSV)
                .setColorModelSwitchEnabled(true) // Allow switching between color models
                .setButtonOkText(android.R.string.ok) // OK button text
                .setButtonCancelText(android.R.string.cancel) // Cancel button text
                .onColorSelected(color -> {
                    // Handle selected color
                    currentColor = color;
//                    backImage.setVisibility(View.INVISIBLE);
                    rlBackground.setBackgroundColor(color);
                })
                .create();

        // Show dialog
        FragmentManager fragmentManager = getSupportFragmentManager();
        colorPicker.show(fragmentManager, "color_picker");
//        ColorPickerDialogBuilder
//                .with(this)
//                .setTitle("Choose color")
//                .initialColor(currentColor)
//                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
//                .density(12)
//
//                .setOnColorSelectedListener(new OnColorSelectedListener() {
//                    @Override
//                    public void onColorSelected(int selectedColor) {
//
//
//                    }
//                })
//                .setPositiveButton("ok", new ColorPickerClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
//                        currentColor = selectedColor;
//                        backImage.setVisibility(View.INVISIBLE);
//                        rlBackground.setBackgroundColor(selectedColor);
//                    }
//                })
//                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                    }
//                })
//                .build()
//                .show();
    }

    public class n implements View.OnClickListener {

        public void onClick(View view) {
            tvEsize.setVisibility(View.INVISIBLE);
        }
    }


    public class seekbaralert implements SeekBar.OnSeekBarChangeListener {

        public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
            if (i < 10) {
                tvMaker.setTextSize(15.0f);
                tvMaker1.setTextSize(15.0f);
                return;
            }
            tvMaker.setTextSize((float) i);
            tvMaker1.setTextSize((float) i);
            b0 = i;
        }

        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        public void onStopTrackingTouch(SeekBar seekBar) {
        }
    }

    public class c0 implements View.OnClickListener {

        final Typeface f1979c;
        final Button d;

        c0(Typeface typeface, Button button) {
            this.f1979c = typeface;
            this.d = button;
        }

        public void onClick(View view) {
            tvMaker.setTypeface(this.f1979c);
            this.d.setTypeface(this.f1979c);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }

    public void onBackPress() {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.logo)
                .setTitle(getString(R.string.app_name))
                .setMessage("Are you sure you want to close Quotes Maker")
                .setPositiveButton("Yes", (dialog, which) -> finish())
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initCheck();
    }

    private void initCheck() {
        if (prf.loadNightModeState()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

}