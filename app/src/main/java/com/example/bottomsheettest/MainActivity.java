package com.example.bottomsheettest;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.UnderlineSpan;
import android.text.util.Linkify;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

    // Background image Created from Abstract Art Simpler Background Pattern
    // https://androidhdwallpapers.com/abstract-art-simpler-background-pattern/

public class MainActivity extends AppCompatActivity {

    // the 'location' string represents the JSON data for one restaurant returned from a server
    private String location = "{\"formatted_address\" : \"718 17 Ave SW, Calgary, AB T2S 0B7\", \"formatted_phone_number\" : \"(403) 474-4414\", \"name\" : \"MARKET\", \"opening_hours\" : { \"weekday_text\" : [ \"Monday: 11:30 AM – 11:00 PM\", \"Tuesday: 11:30 AM – 11:00 PM\", \"Wednesday: 11:30 AM – 11:00 PM\", \"Thursday: 11:30 AM – 12:00 AM\", \"Friday: 11:30 AM – 12:00 AM\", \"Saturday: 11:30 AM – 12:00 AM\", \"Sunday: 11:30 AM – 11:00 PM\" ] }, \"photos\" : [ { \"photo_id\" : 1, \"photo_url\" : “https://www.cuiseek.com/wp-content/uploads/2017/11/food1.jpg” }, { \"photo_id\" : 2, \"photo_url\" : “https://www.cuiseek.com/wp-content/uploads/2017/11/food2.jpg” }, { \"photo_id\" : 3, \"photo_url\" : “https://www.cuiseek.com/wp-content/uploads/2017/11/food3.jpg” }, { \"photo_id\" : 4, \"photo_url\" : “https://www.cuiseek.com/wp-content/uploads/2017/11/food4.jpg” }, { \"photo_id\" : 5, \"photo_url\" : “https://www.cuiseek.com/wp-content/uploads/2017/11/food5.jpg” }, ], \"rating\" : 4.1, \"website\" : \"http://marketcalgary.ca/\"}";

    private BottomSheetBehavior mBottomSheetBehavior;
    private FloatingActionButton mFloatingActionButton;

    // InstanceState keys
    private static String BOTTOM_SHEET_STATE = "bottomSheetState";
    private static String SELECTED_RESTAURANT = "selected";

    private RestaurantData selectedRestaurant = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View bottomSheet = findViewById( R.id.bottom_sheet );
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        Button rButton = findViewById( R.id.restaurantButton);

        // FAB is initially hidden and onClick hides the BottomSheet
        mFloatingActionButton = findViewById(R.id.fab);
        mFloatingActionButton.hide();
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        });

        // make sure the BottomSheet can be hidden and set the peek height
        mBottomSheetBehavior.setHideable(true);
        mBottomSheetBehavior.setPeekHeight((int) getResources().getDimension(R.dimen.peek_height));

        // when the device has been rotated - restore the BottomSheet State and the Restaurant Data
        if (savedInstanceState != null) {

            int state = savedInstanceState.getInt(BOTTOM_SHEET_STATE, BottomSheetBehavior.STATE_HIDDEN);
            mBottomSheetBehavior.setState(state);

            if (BottomSheetBehavior.STATE_COLLAPSED == state || BottomSheetBehavior.STATE_EXPANDED == state) {
                ShowTheFAB();
            }

            selectedRestaurant = savedInstanceState.getParcelable(SELECTED_RESTAURANT);
            if (null != selectedRestaurant) {
                ShowRestaurantData(selectedRestaurant);
            }
        } else {
            // the BottomSheet is initially hidden from view
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        }

        // there are 2 bottomSheet actions that require some help
        //  - the user slides the BottomSheet out of view at the bottom
        //  - the user clicks the FAB
        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bSheet, int newState) {

                if (BottomSheetBehavior.STATE_HIDDEN == newState) {

                    // when the BottomSheet becomes hidden then the FAB should be hidden manually
                    HideTheFAB();

                    // when the 'back' button is used to hide the bottomSheet the contents may have
                    // been moved to any random position - like a photo at the top instead of the restaurant name
                    // reset the BottomSheet scroll position to be consistent for next 'peek'
                    bSheet.scrollTo(0,0);
                }
            }

            @Override
            public void onSlide(@NonNull View bSheet, float slideOffset) {
            }
        });

        rButton.setOnClickListener(new View.OnClickListener()
           {
               @Override
               public void onClick(View v) {

                   // in a real app the user would have most likely selected a new restaurant
                   // and that data would need to be loaded into 'selectedRestaurant'
                   if (null == selectedRestaurant) {

                       // get the data for the selected restaurant
                       // in this case - convert the raw JSON into a RestaurantData object
                       // in a real app the restaurant data would come from the database
                       selectedRestaurant = new RestaurantData(location);

                       ShowRestaurantData(selectedRestaurant);
                   }

                   // put the BottomSheet at the 'peek' height
                   mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                   ShowTheFAB();
               }
           }
        );
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // save the bottomSheet display state
        outState.putInt(BOTTOM_SHEET_STATE, mBottomSheetBehavior.getState());

        // save the Restaurant selection
        outState.putParcelable(SELECTED_RESTAURANT, selectedRestaurant);

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        // hide the BottomSheet (if visible) when the back button is pressed
        if (BottomSheetBehavior.STATE_HIDDEN != mBottomSheetBehavior.getState()) {
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        } else {
            super.onBackPressed();
        }
    }

    // the FAB does not automatically hide / show when the BottomSheet disappears and appears
    // these methods make sure it does
    private void ShowTheFAB() {
        CoordinatorLayout.LayoutParams p = new CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.WRAP_CONTENT, CoordinatorLayout.LayoutParams.WRAP_CONTENT);
        p.anchorGravity = Gravity.TOP | Gravity.END;
        p.setAnchorId(R.id.bottom_sheet);
        mFloatingActionButton.setLayoutParams(p);
        mFloatingActionButton.show();
    }

    private void HideTheFAB() {
        // 2018-01-13
        // problem - hiding the FAB looks strange because the FAB will jump to the top left corner
        // before disappearing when 'hide' is called - very distracting visually
        //
        // workaround - set the LayoutParams height and width to 0 before hiding
        CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams) mFloatingActionButton.getLayoutParams();
        p.height = 0;
        p.width = 0;
        mFloatingActionButton.setLayoutParams(p);
        mFloatingActionButton.hide();
    }

    private TextView tvAddress;

    // take the data for one restaurant (RestaurantData) and use it to populate the BottomSheet elements
    // in a real app this would likely be using a cursor to populate the UI
    private void ShowRestaurantData(RestaurantData restaurantData) {
        TextView tvName;
        TextView tvPhone;
        TextView tvURL;
        TextView tvRating;

        if (null == restaurantData) {
            return;
        }

        tvName = findViewById(R.id.tvName);
        tvPhone = findViewById(R.id.tvPhone);
        tvAddress = findViewById(R.id.tvAddress);
        tvURL = findViewById(R.id.tvURL);
        tvRating = findViewById(R.id.tvRating);

        // copy basic info to the display elements
        tvName.setText(restaurantData.get_name());
        tvName.setContentDescription(tvName.getContentDescription() + restaurantData.get_name());

        // problem: the TextView 'automap' feature has trouble with address formatting
        // workaround: show the link manually and use 'onClick' to send an Intent to Google maps
        // reference: https://stackoverflow.com/questions/2624649/autolink-for-map-not-working
        SpannableString address = new SpannableString(restaurantData.get_address());
        address.setSpan(new UnderlineSpan(), 0, address.length(), 0);
        tvAddress.setText(address);
        tvAddress.setTextColor(getResources().getColor(R.color.primaryColor));
        tvAddress.setContentDescription(tvAddress.getContentDescription() + address.toString());

        tvAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent geoIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(
                        "geo:0,0?q=" + tvAddress.getText().toString()));
                startActivity(geoIntent);
            }
        });

        tvPhone.setText(restaurantData.get_phone());
        tvPhone.setContentDescription(tvPhone.getContentDescription() + restaurantData.get_phone());

        // create a link to the restaurant website without showing the URL because it might be long string
        String website = getResources().getString(R.string.restaurant_website, restaurantData.get_website());

        // set the text as html and make the link active
        tvURL.setText(Html.fromHtml(website));
        tvURL.setMovementMethod(LinkMovementMethod.getInstance());
        tvURL.setContentDescription(tvURL.getContentDescription() + restaurantData.get_website());

        // format the rating number
        String ratingLabel = getResources().getString(R.string.rating_label, restaurantData.get_rating());
        tvRating.setText(ratingLabel);
        tvRating.setContentDescription(ratingLabel);

        ArrayList<String> pUrls = restaurantData.get_photoUrls();
        int[] photoIds = restaurantData.get_photoIds();

        if (null != pUrls) {

            // limit the number of photos displayed to be the lesser of 'URLs sent from server' or photo_count
            int photoCount = getResources().getInteger(R.integer.photo_count);
            int pUrlsCount = pUrls.size();
            if (pUrlsCount < photoCount) {
                photoCount = pUrlsCount;
            }

            View linearImageLayout = findViewById(R.id.imageBlock);
            LinearLayout.LayoutParams imageLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen.photo_height));
            ImageView imageView;
            String description;

            for (int i = 0; i < photoCount; i++) {

                // don't add any problematic image urls
                if (photoIds[i] != RestaurantData.BAD_IMAGE_URL) {

                    // create a new ImageView and add it to the vertical layout
                    imageView = new ImageView(this);

                    // provide content description like 'Photo Number 2'
                    description = getResources().getString(R.string.photo_description, photoIds[i]);
                    imageView.setContentDescription(description);

                    imageView.setLayoutParams(imageLayoutParams);
                    ((LinearLayout) linearImageLayout).addView(imageView);

                    // centerCrop will make them look good by filling the available space
                    Glide.with(MainActivity.this)
                            .load(pUrls.get(i)).centerCrop()
                            .error(R.drawable.error_image_not_loaded)
                            .into(imageView);
                }
            }
        }

        if (null != restaurantData.get_weekdayHours()) {
            // create a TextView for each weekday hours string and add them to the 'days_and_hours'
            // the order of the strings in the JSON is the order that they are displayed
            View linearLayout = findViewById(R.id.days_and_hours);
            LinearLayout.LayoutParams tvLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            String daysHours;

            for (int i = 0; i < restaurantData.get_weekdayHours().size(); i++) {
                TextView tvDay = new TextView(this);
                daysHours = restaurantData.get_weekdayHours().get(i);
                tvDay.setText(daysHours);

                // use the same string to provide the content description
                tvDay.setContentDescription(daysHours);

                // make the target bigger for accessibility
                tvDay.setPadding(
                        (int) getResources().getDimension(R.dimen.weekday_hours_margin),
                        (int) getResources().getDimension(R.dimen.weekday_hours_margin_top),
                        (int) getResources().getDimension(R.dimen.weekday_hours_margin),
                        (int) getResources().getDimension(R.dimen.weekday_hours_margin_bottom));
                tvDay.setGravity(Gravity.CENTER_HORIZONTAL);
                tvDay.setLayoutParams(tvLayoutParams);
                ((LinearLayout) linearLayout).addView(tvDay);
            }
        }
    }
}
