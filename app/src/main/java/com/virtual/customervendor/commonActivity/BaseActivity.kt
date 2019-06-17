package com.virtual.customervendor.commonActivity

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.virtual.customervendor.MyApp
import com.virtual.customervendor.R
import com.virtual.customervendor.utills.AppLog

public abstract  class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
    }

    private val TAG = "BaseActivity"

    protected override fun attachBaseContext(base: Context) {
        super.attachBaseContext(MyApp.localeManager.setLocale(base))
        AppLog.d(TAG, "attachBaseContext")
    }

}
