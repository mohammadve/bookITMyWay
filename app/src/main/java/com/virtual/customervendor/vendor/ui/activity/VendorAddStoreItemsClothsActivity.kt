package com.virtual.customervendor.vendor.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.virtual.customer_vendor.utill.AppUtill
import com.virtual.customervendor.R
import com.virtual.customervendor.customer.ui.ViewPagerItemClicked
import com.virtual.customervendor.customer.ui.adapter.HomeSliderAdapter
import com.virtual.customervendor.model.BusinessImage
import com.virtual.customervendor.utills.AppConstants
import com.virtual.customervendor.utills.AppLog
import com.zfdang.multiple_images_selector.ImagesSelectorActivity
import com.zfdang.multiple_images_selector.SelectorSettings
import kotlinx.android.synthetic.main.activity_vendor_add_store_items_cloths.*
import java.io.File
import java.util.*

class VendorAddStoreItemsClothsActivity : AppCompatActivity(), View.OnClickListener, ViewPagerItemClicked {
    override fun onPagerItemClicked(position: Int) {
    }

    var TAG: String = VendorAddStoreItemsClothsActivity::class.java.simpleName

    var toolbar: AppBarLayout? = null
    var mTitle: TextView? = null
    val REQUEST_CODE = 123
    var mResults = ArrayList<String>()
    var imageFiles = ArrayList<File>()
    var SCREEN_COUNT: Int = 0
    var homeSliderAdapter: HomeSliderAdapter? = null
    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.iv_back -> {
                onBackPressed()
            }
            R.id.btn_next -> {

            }
            R.id.img_camera -> {
                captureMultipleImages()
            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vendor_add_store_items_cloths)

    }

    fun init() {
        toolbar = findViewById(R.id.toolbar) as AppBarLayout
        mTitle = toolbar!!.findViewById(R.id.tv_toolbarTitleText) as TextView
        mTitle!!.text = getString(R.string.add_item)
        val iv_back = toolbar!!.findViewById(R.id.iv_back) as ImageView
        iv_back.setOnClickListener(this)
        img_camera.setOnClickListener(this)
        initsizeColorRecyclerView()

    }

    private fun initsizeColorRecyclerView() {

    }


    private fun captureMultipleImages() {
// start multiple photos selector
        val intent = Intent(this, ImagesSelectorActivity::class.java)
// max number of images to be selected
        intent.putExtra(SelectorSettings.SELECTOR_MAX_IMAGE_NUMBER, 1)
// min size of image which will be shown; to filter tiny images (mainly icons)
        intent.putExtra(SelectorSettings.SELECTOR_MIN_IMAGE_SIZE, AppConstants.VENDOR_BANNER_IMG_COMPRESSION_SIZE)
// show camera or not
        intent.putExtra(SelectorSettings.SELECTOR_SHOW_CAMERA, false)
// pass current selected images as the initial value
        intent.putStringArrayListExtra(SelectorSettings.SELECTOR_INITIAL_SELECTED_LIST, mResults)
// start the selector
        startActivityForResult(intent, REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === REQUEST_CODE) {
            if (resultCode === Activity.RESULT_OK) {
                mResults = data!!.getStringArrayListExtra(SelectorSettings.SELECTOR_RESULTS)

                var imgList = ArrayList<BusinessImage>()
                for (url in mResults) {
                    imgList.add(BusinessImage(url, ""))
                }
                capturedImage(imgList)

                AppLog.e(TAG, mResults.toString())
            }
        }
    }

    fun capturedImage(mResults: ArrayList<BusinessImage>) {
        initViewPager(mResults, false)
        for (imgUrl in mResults) {
            imageFiles.add(File(imgUrl.url))
        }
    }

    private fun initViewPager(mResults: ArrayList<BusinessImage>, fromEdit: Boolean) {
        SCREEN_COUNT = mResults.size
        homeSliderAdapter = HomeSliderAdapter(this, mResults, this, fromEdit)
        viewPager!!.setAdapter(homeSliderAdapter)
        AppUtill.handlePager(this, mResults.size, layoutDots, viewPager)
    }
}
