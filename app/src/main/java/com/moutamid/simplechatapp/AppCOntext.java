package com.moutamid.simplechatapp;

import android.app.Application;

import com.fxn.stash.Stash;

public class AppCOntext extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //Add this line in ApplicationContext.java
        Stash.init(this);
    }
}
