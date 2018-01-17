package com.example.bottomsheettest;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Derek on 1/14/2018.
 */

//{
//      "formatted_address" : "718 17 Ave SW, Calgary, AB T2S 0B7",
//      "formatted_phone_number" : "(403) 474-4414",
//      "name" : "MARKET",
//      "opening_hours" : {
//         "weekday_text" : [
//            "Monday: 11:30 AM – 11:00 PM",
//            "Tuesday: 11:30 AM – 11:00 PM",
//            "Wednesday: 11:30 AM – 11:00 PM",
//            "Thursday: 11:30 AM – 12:00 AM",
//            "Friday: 11:30 AM – 12:00 AM",
//            "Saturday: 11:30 AM – 12:00 AM",
//            "Sunday: 11:30 AM – 11:00 PM"
//         ]
//      },
//      "photos" : [
//         {
//            "photo_id" : 1,
//            "photo_url" : “https://www.cuiseek.com/wp-content/uploads/2017/11/food1.jpg”
//         },
//         {
//            "photo_id" : 2,
//            "photo_url" : “https://www.cuiseek.com/wp-content/uploads/2017/11/food2.jpg”
//         },
//         {
//            "photo_id" : 3,
//            "photo_url" : “https://www.cuiseek.com/wp-content/uploads/2017/11/food3.jpg”
//         },
//         {
//            "photo_id" : 4,
//            "photo_url" : “https://www.cuiseek.com/wp-content/uploads/2017/11/food4.jpg”
//         },
//         {
//            "photo_id" : 5,
//            "photo_url" : “https://www.cuiseek.com/wp-content/uploads/2017/11/food5.jpg”
//         },
//      ],
//      "rating" : 4.1,
//      "website" : "http://marketcalgary.ca/"
//}


public class RestaurantData {

    // JSON Node names
    private static final String TAG_FORMATTED_ADDRESS = "formatted_address";
    private static final String TAG_FORMATTED_PHONE_NUMBER = "formatted_phone_number";
    private static final String TAG_NAME = "name";
    private static final String TAG_PHOTOS = "photos";
    private static final String TAG_PHOTO_ID = "photo_id";
    private static final String TAG_PHOTO_URL = "photo_url";
    private static final String TAG_OPENING_HOURS = "opening_hours";
    private static final String TAG_WEEKDAY_TEXT = "weekday_text";
    private static final String TAG_RATING = "rating";
    private static final String TAG_WEBSITE = "website";

    private String _address = "";
    private String _phone = "";
    private String _name = "";
    private double _rating = 1.0;
    private String _website = "";
    private ArrayList<String> _weekdayHours;
    private ArrayList<String> _photoUrls;
    private int[] _photoIds;

    public String get_address() {
        return _address;
    }

    public String get_phone() {
        return _phone;
    }

    public String get_name() {
        return _name;
    }

    public double get_rating() {
        return _rating;
    }

    public String get_website() {
        return _website;
    }

    public ArrayList<String> get_weekdayHours() {
        return _weekdayHours;
    }

    public ArrayList<String> get_photoUrls() {
        return _photoUrls;
    }

    public int[] get_photoIds() {
        return _photoIds;
    }

    public RestaurantData(String rawData){

        JSONObject jsonObject = null;
        JSONArray photosArray = null;
        JSONArray weekdayArray = null;
        JSONObject photoObject;

        String jsonInput = rawData;

        // replace any bad opening and closing quotation marks with the standard character
        jsonInput = jsonInput.replace("”","\"");
        jsonInput = jsonInput.replace("“","\"");

        // convert the rawData string into a JSON object
        try {
            jsonObject = new JSONObject(jsonInput);
        } catch (JSONException e) {
            Log.e("JSON Parser jsonInput", "Error parsing data " + e.toString());
        }

        if (null != jsonObject) {
            try {
                // get the easy top level items
                _address = jsonObject.getString(TAG_FORMATTED_ADDRESS);
                _phone = jsonObject.getString(TAG_FORMATTED_PHONE_NUMBER);
                _name = jsonObject.getString(TAG_NAME);
                _rating = jsonObject.getDouble(TAG_RATING);
                _website = jsonObject.getString(TAG_WEBSITE);

                // put the photo data into an array
                photosArray = jsonObject.getJSONArray(TAG_PHOTOS);

                // isolate the weekday text array from the opening hours
                String weekdays = jsonObject.getString(TAG_OPENING_HOURS);
                JSONObject weekdaysObject = new JSONObject(weekdays);
                weekdayArray = weekdaysObject.getJSONArray(TAG_WEEKDAY_TEXT);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (null != photosArray) {
            _photoUrls = new ArrayList<>();
            _photoIds = new int[photosArray.length()];

            // get the photo IDs and URLs
            try {
                for (int i = 0; i < photosArray.length(); i++) {
                    // watch out for missing JSON array data
                    if (photosArray.isNull(i)) {
                        _photoIds[i] = -1;
                        _photoUrls.add("https://www.google.ca/favicon.ico");
                    } else {
                        photoObject = photosArray.getJSONObject(i);
                        _photoIds[i] = photoObject.getInt(TAG_PHOTO_ID);
                        _photoUrls.add(photoObject.getString(TAG_PHOTO_URL));

/*
                        if ( i != 2) {
                            _photoUrls.add(photoObject.getString(TAG_PHOTO_URL));
                        } else {
                            _photoUrls.add("https://www.google.ca/favicon.icon");

                        }
*/
                    }
                }
            } catch (JSONException e) {
                Log.e("JSON Parser photos", "Error parsing data " + e.toString());
            }
        }

        if (null != weekdayArray) {
            _weekdayHours = new ArrayList<>(weekdayArray.length());
            try {
                for (int i = 0; i < weekdayArray.length(); i++) {
                    _weekdayHours.add(weekdayArray.getString(i));
                }
            } catch (JSONException e) {
                Log.e("JSON Parser weekdays ", "Error parsing data " + e.toString());
            }
        }
    }

}
