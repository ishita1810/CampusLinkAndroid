package com.example.campuslink;

import android.app.Application;

import com.google.firebase.FirebaseApp;

public class CampusLinkApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
    }
}
