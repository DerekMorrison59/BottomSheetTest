### BottomSheetTest - Example code for 3DotFive

<p align="center"> 
 <h1>ANDROID DEVELOPER WANTED</h1>
</p>

<p align="center">
 <b>for a flexible software development contract</b>
</p>

<p align="center">
 <img src="https://developer.android.com/_static/images/android/touchicon-180.png">
</p>

Development work is required on an Android application to be released in the coming months.
Applicants should have skills in some or all of the following areas:
* Developing Android applications using **Android Studio**
* Building Android layouts using **Material Design** components and principles.
* Using **JSON** for data transit.
* Developing applications that access services **asynchronously.**
* **Git** repositories

The contract will require effort on a **contract** basis with some flexibility for choice of work location and schedule.

* **Term**: ASAP - month to month basis based on performance
* **Location**: Wherever you like to work.
* **Schedule**: Your choice, with a requirement to work alongside our team some evenings/weekends.
* **How To Apply**: Complete the requirements below to demonstrate some core competencies. Completed submissions can be info@3dotfive.com or pushed to a github repository along with resume and cover letter. Submissions without completed code sample will not be considered.


## **Proof of Skill - Android Design**

Interested applicants,

Please complete the following tasks in Android Studio to showcase your Android development and design skills. There are many solutions that may be deemed “correct” - show us how you would implement the requirements below. Our goal is to identify applicants who have an eye for design and can use accepted techniques for crafting effective interfaces in Android.

Completed submissions may be zipped and emailed or pushed to a github repository. Please include all required project files so we can open the project locally.

**Tasks**

1. Create a new Android Studio project with a single Activity. 
 Fill the Activity’s layout with a color or some filler content of your choice. The layout should fill the window (though the status bar and navigation bar may be retained).
2. Add a single button to the layout, positioned as you choose.
3. Add a bottom sheet to the Activity that slides up from the bottom of the screen. The bottom sheet should be hidden on load and peek when the button (from step 2) is pressed. Swiping up should fully expand the bottom sheet such that all its content is displayed. A partial swipe down on the bottom sheet should return it to a peeked state. A full swipe down should hide it.
4. Add a floating action button to the bottom sheet. The floating action button should anchor to the top edge of the bottom sheet such that half its diameter overlaps the bottom sheet and the other half extends above the bottom sheet. The floating action button should slide with the bottom sheet between the bottom sheet’s collapsed and expanded states, and hide when the bottom sheet hides.
5. Implement a click event for the floating action button. A click on the floating action button should hide the bottom sheet.
6. Using a ConstraintLayout, fill in the bottom sheet with whatever views you feel would best display the provided JSON data (see below). The JSON data contains some basic data about a restaurant. You may import the JSON, or hardcode the values in your Activity. The user should be able to scroll through the images and reviews included with the restaurant data.
7. Pretty up the interface as you desire.
8. Zip and email to 3dotfive@gmail.com, or push to a new github repo and send the link. If you wish to share further information about why you took particular design approaches, include a short description of your implementation in the email.





### JSON Data
```
 {
       "formatted_address" : "718 17 Ave SW, Calgary, AB T2S 0B7",
       "formatted_phone_number" : "(403) 474-4414",
      "name" : "MARKET",
      "opening_hours" : {
         "weekday_text" : [
            "Monday: 11:30 AM – 11:00 PM",
            "Tuesday: 11:30 AM – 11:00 PM",
            "Wednesday: 11:30 AM – 11:00 PM",
            "Thursday: 11:30 AM – 12:00 AM",
            "Friday: 11:30 AM – 12:00 AM",
            "Saturday: 11:30 AM – 12:00 AM",
            "Sunday: 11:30 AM – 11:00 PM"
         ]
      },
      "photos" : [
         {
            "photo_id" : 1,
            "photo_url" : “https://www.cuiseek.com/wp-content/uploads/2017/11/food1.jpg”
         },
         {
            "photo_id" : 2,
            "photo_url" : “https://www.cuiseek.com/wp-content/uploads/2017/11/food2.jpg”
         },
         {
            "photo_id" : 3,
            "photo_url" : “https://www.cuiseek.com/wp-content/uploads/2017/11/food3.jpg”
         },
         {
            "photo_id" : 4,
            "photo_url" : “https://www.cuiseek.com/wp-content/uploads/2017/11/food4.jpg”
         },
         {
            "photo_id" : 5,
            "photo_url" : “https://www.cuiseek.com/wp-content/uploads/2017/11/food5.jpg”
         },
      ],
      "rating" : 4.1,
      "website" : "http://marketcalgary.ca/"
}
```


