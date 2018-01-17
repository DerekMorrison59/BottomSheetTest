package com.example.bottomsheettest;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

// Background image Created by Freepik.com
// <a href="https://www.freepik.com/free-photos-vectors/food">Food vector created by Freepik</a>

public class MainActivity extends AppCompatActivity {

    private BottomSheetBehavior mBottomSheetBehavior;
    private FloatingActionButton mFloatingActionButton;
    private TextView tvName;
    private TextView tvAddress;
    private TextView tvPhone;
    private TextView tvURL;
    private TextView tvRating;
    private boolean dataLoaded = false;

    private View bottomSheet;
    private ArrayList<ImageView> ivArray;

    @Override
    public void onBackPressed() {
        if (BottomSheetBehavior.STATE_HIDDEN != mBottomSheetBehavior.getState()) {
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        } else {
            super.onBackPressed();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomSheet = findViewById( R.id.bottom_sheet );
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        Button button1 = (Button) findViewById( R.id.button_1 );

        tvName = findViewById(R.id.tvName);
        tvAddress = findViewById(R.id.tvAddress);
        tvPhone = findViewById(R.id.tvPhone);
        tvURL = findViewById(R.id.tvURL);
        tvRating = findViewById(R.id.tvRating);

        ivArray = new ArrayList<ImageView>();
        ivArray.add((ImageView)findViewById(R.id.ivFood1));
        ivArray.add((ImageView)findViewById(R.id.ivFood2));
        ivArray.add((ImageView)findViewById(R.id.ivFood3));
        ivArray.add((ImageView)findViewById(R.id.ivFood4));
        ivArray.add((ImageView)findViewById(R.id.ivFood5));

        mFloatingActionButton = findViewById(R.id.fab);
        mFloatingActionButton.setImageResource(R.drawable.arrow_down_bold_light);
        mFloatingActionButton.hide();
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        });

        // make sure the BottomSheet is initially hidden from view
        mBottomSheetBehavior.setHideable(true);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        mBottomSheetBehavior.setPeekHeight(160);

        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

                // 1 = STATE_DRAGGING
                // 2 = STATE_SETTLING
                // 3 = STATE_EXPANDED
                // 4 = STATE_COLLAPSED
                // 5 = STATE_HIDDEN

                // when the BottomSheet is hidden that means the FAB should be hidden
                if (BottomSheetBehavior.STATE_HIDDEN == newState) {
                    HideTheFAB();

                    bottomSheet.scrollTo(0,0);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });


        button1.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {
                                           if (false == dataLoaded) {
                                               ParseTheLocation();
                                           }
                                           mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                                           ShowTheFAB();
                                           dataLoaded = true;
                                       }
                                   }
        );
    }

    private void ShowTheFAB() {
        CoordinatorLayout.LayoutParams p = new CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.WRAP_CONTENT, CoordinatorLayout.LayoutParams.WRAP_CONTENT);
        p.anchorGravity = Gravity.TOP | Gravity.END;
        p.setAnchorId(R.id.bottom_sheet);
        mFloatingActionButton.setLayoutParams(p);
        mFloatingActionButton.show();
    }

    private void HideTheFAB() {
        CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams) mFloatingActionButton.getLayoutParams();
        p.setAnchorId(View.NO_ID);
        p.height = 0;
        p.width = 0;
        mFloatingActionButton.setLayoutParams(p);
        mFloatingActionButton.hide();
    }

    private void ParseTheLocation() {

        RestaurantData r = new RestaurantData(location);

        // copy basic info to the display elements
        tvName.setText(r.get_name());

        // reference: https://stackoverflow.com/questions/2624649/autolink-for-map-not-working
        SpannableString address = new SpannableString(r.get_address());
        address.setSpan(new UnderlineSpan(), 0, address.length(), 0);
        tvAddress.setText(address);
        tvAddress.setTextColor(getResources().getColor(R.color.primaryColor));

        tvAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent geoIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(
                        "geo:0,0?q=" + tvAddress.getText().toString()));
                startActivity(geoIntent);
            }
        });

        tvPhone.setText(r.get_phone());
        tvURL.setText(r.get_website());
        String ratingLabel = getResources().getString(R.string.rating_label);

        tvRating.setText(ratingLabel + String.valueOf(r.get_rating()));

        ArrayList<String> pUrls = r.get_photoUrls();
        int[] photoIds = r.get_photoIds();

        // limit the number of photos to 5 max
        int photoCount = 5;
        int pUrlsCount = pUrls.size();
        ImageView imageView;

        for (int i = 0; i < photoCount; i++) {
            //String u = pUrls.get(i);
            imageView = ivArray.get(i);
            if (i >= pUrlsCount || photoIds[i] == -1) {
                imageView.setVisibility(View.GONE);
            } else {
                imageView.setVisibility(View.VISIBLE);
                Glide.with(MainActivity.this)
                        .load(pUrls.get(i)).centerCrop()
                        .error(R.drawable.error_image_not_loaded)
                        .into(imageView);
            }
        }

        // create display elements for each weekday hours info
        View linearLayout = findViewById(R.id.days_and_hours);
        LinearLayout.LayoutParams tvLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        tvLayoutParams.setMarginStart(8);
        ArrayList<String> weekdayArray = r.get_weekdayHours();
        for (int i = 0; i < weekdayArray.size(); i++){
            TextView tvDay = new TextView(this);
            tvDay.setText(weekdayArray.get(i));
            tvDay.setLayoutParams(tvLayoutParams);
            ((LinearLayout) linearLayout).addView(tvDay);
        }
    }

    private String location = "{\"formatted_address\" : \"718 17 Ave SW, Calgary, AB T2S 0B7\", \"formatted_phone_number\" : \"(403) 474-4414\", \"name\" : \"MARKET\", \"opening_hours\" : { \"weekday_text\" : [ \"Monday: 11:30 AM – 11:00 PM\", \"Tuesday: 11:30 AM – 11:00 PM\", \"Wednesday: 11:30 AM – 11:00 PM\", \"Thursday: 11:30 AM – 12:00 AM\", \"Friday: 11:30 AM – 12:00 AM\", \"Saturday: 11:30 AM – 12:00 AM\", \"Sunday: 11:30 AM – 11:00 PM\" ] }, \"photos\" : [ { \"photo_id\" : 1, \"photo_url\" : “https://www.cuiseek.com/wp-content/uploads/2017/11/food1.jpg” }, { \"photo_id\" : 2, \"photo_url\" : “https://www.cuiseek.com/wp-content/uploads/2017/11/food2.jpg” }, { \"photo_id\" : 3, \"photo_url\" : “https://www.cuiseek.com/wp-content/uploads/2017/11/food3.jpg” }, { \"photo_id\" : 4, \"photo_url\" : “https://www.cuiseek.com/wp-content/uploads/2017/11/food4.jpg” }, { \"photo_id\" : 5, \"photo_url\" : “https://www.cuiseek.com/wp-content/uploads/2017/11/food5.jpg” }, ], \"rating\" : 4.1, \"website\" : \"http://marketcalgary.ca/\"}";
}
