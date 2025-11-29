package com.example.campuslink;

import android.content.Context;

import com.google.firebase.FirebaseApp;

public class FirebaseManager {

    public static void initialize(Context context) {
        // The google-services.json file is now used for initialization.
        // This class is kept for future use, in case any other
        // specific Firebase-related initializations are needed.
        FirebaseApp.initializeApp(context);
    }
}
