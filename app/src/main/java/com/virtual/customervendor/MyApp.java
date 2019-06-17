package com.virtual.customervendor;

import android.content.Context;
import android.content.res.Configuration;
import android.support.multidex.MultiDexApplication;
import android.util.Log;
import android.widget.EditText;

import com.crashlytics.android.Crashlytics;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.virtual.customervendor.managers.CachingManager;
import io.fabric.sdk.android.Fabric;

public class MyApp extends MultiDexApplication {
    private static final String TAG = MyApp.class.getSimpleName();
    private static MyApp mInstance;

    public static synchronized MyApp getInstance() {
        return mInstance;
    }
    public static LocaleManager localeManager;

    @Override
    protected void attachBaseContext(Context base) {
        localeManager = new LocaleManager(base);
        super.attachBaseContext(localeManager.setLocale(base));
        Log.d(TAG, "attachBaseContext");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        localeManager.setLocale(this);
        Log.d(TAG, "onConfigurationChanged: " + newConfig.locale.getLanguage());
    }
    @Override
    public void onCreate() {
        super.onCreate();

        Fabric.with(this, new Crashlytics());

        mInstance = this;
        Context applicationContext = getApplicationContext();
        CachingManager.cacheApplicationContext(applicationContext);

        Fresco.initialize(getApplicationContext());
    }

}
