package com.virtual.customervendor.utills

import android.support.v7.app.AppCompatActivity
import com.virtual.customervendor.R


class SlideAnimationUtill {
    companion object {

        fun slideNextAnimation(activity1: AppCompatActivity) {
            activity1.overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left1)
        }

        fun slideBackAnimation(activity1: AppCompatActivity) {
            activity1.overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
        }
    }
}