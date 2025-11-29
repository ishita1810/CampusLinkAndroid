package com.example.campuslink;

import android.app.Application;

public class CampusLinkApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // All initialization is now handled by the FirebaseManager.
    }
}
