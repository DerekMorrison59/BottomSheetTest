package com.example.bottomsheettest;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * Created by Derek on 1/14/2018.
 */


// the following is an example of the kind of JSON that this class parses

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

public class RestaurantData implements Parcelable {

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

    // a known image URL available on the web in case of a problem with the JSON
    private static final String IMAGE_URL = "http://placehold.it/320x180&text=unavailable";
    public static final int BAD_IMAGE_URL = -1;

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

    public RestaurantData(String rawData) {

        JSONObject jsonObject = null;
        JSONArray photosArray = null;
        JSONArray weekdayArray = null;

        String jsonInput = cleanRawJSON(rawData);

        // convert the rawData string into a JSON object
        try {
            jsonObject = new JSONObject(jsonInput);
        } catch (JSONException e) {
            Log.e("JSON Parser jsonInput", "Error parsing data " + e.toString());
        }

        // get all the JSON nodes
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
                // consider expanding the reported details if there are problems with the source JSON
                e.printStackTrace();
            }
        }

        // each item in the photosArray contains a photo ID and a photo URL
        if (null != photosArray) {
            JSONObject photoObject;
            _photoUrls = new ArrayList<>();
            _photoIds = new int[photosArray.length()];

            // get the photo IDs and URLs
            try {
                for (int i = 0; i < photosArray.length(); i++) {

                    // flag missing JSON array data
                    if (photosArray.isNull(i)) {
                        _photoIds[i] = BAD_IMAGE_URL;
                        _photoUrls.add(IMAGE_URL);
                    } else {
                        photoObject = photosArray.getJSONObject(i);
                        _photoIds[i] = photoObject.getInt(TAG_PHOTO_ID);
                        _photoUrls.add(photoObject.getString(TAG_PHOTO_URL));
                    }
                }
            } catch (JSONException e) {
                Log.e("JSON Parser photos", "Error parsing data " + e.toString());
            }
        }

        // weekday array is a list of strings
        if (null != weekdayArray) {
            _weekdayHours = new ArrayList<>(weekdayArray.length());
            try {
                for (int i = 0; i < weekdayArray.length(); i++) {
                    if (!weekdayArray.isNull(i)) {
                        _weekdayHours.add(weekdayArray.getString(i));
                    }
                }
            } catch (JSONException e) {
                Log.e("JSON Parser weekdays ", "Error parsing data " + e.toString());
            }
        }
    }

    // it may be necessary to add more string cleanup steps to make sure the JSON is
    // in good shape before parsing
    private String cleanRawJSON(String jsonInput) {

        // replace any bad opening and closing quotation marks with the standard character
        jsonInput = jsonInput.replace("”", "\"");
        jsonInput = jsonInput.replace("“", "\"");
        return jsonInput;
    }

    // make this class 'Parcelable' so that it can be easily saved and restored when device is rotated
    public static final Parcelable.Creator<RestaurantData> CREATOR = new Parcelable.Creator<RestaurantData>() {
        public RestaurantData createFromParcel(Parcel source) {
            return new RestaurantData(source);
        }

        @Override
        public RestaurantData[] newArray(int size) {
            return new RestaurantData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    private RestaurantData(Parcel in) {
        _address = in.readString();
        _phone = in.readString();
        _name = in.readString();
        _rating = in.readDouble();
        _website = in.readString();

        in.readStringList(_weekdayHours);
        in.readStringList(_photoUrls);
        in.readIntArray(_photoIds);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_address);
        dest.writeString(_phone);
        dest.writeString(_name);
        dest.writeDouble(_rating);
        dest.writeString(_website);

        dest.writeStringList(_weekdayHours);
        dest.writeStringList(_photoUrls);
        dest.writeIntArray(_photoIds);
    }
}
