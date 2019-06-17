package com.virtual.customervendor.vendor.ui.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.virtual.customer_vendor.utill.AppUtill
import com.virtual.customervendor.R
import com.virtual.customervendor.customer.ui.ViewPagerItemClicked
import com.virtual.customervendor.customer.ui.adapter.HomeSliderAdapter
import com.virtual.customervendor.managers.SharedPreferenceManager
import com.virtual.customervendor.model.BusinessImage
import com.virtual.customervendor.model.EventDetail
import com.virtual.customervendor.model.request.VendorEventServiceRequest
import com.virtual.customervendor.model.response.BusinessImagesResponse
import com.virtual.customervendor.networks.ApiClient
import com.virtual.customervendor.networks.ApiInterface
import com.virtual.customervendor.utills.*
import com.virtual.customervendor.vendor.ui.activity.*
import com.zfdang.multiple_images_selector.ImagesSelectorActivity
import com.zfdang.multiple_images_selector.SelectorSettings
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_event_two_vendor.*
import java.io.File
import java.util.ArrayList

class VendorEventTwoFragment : Fragment(), View.OnClickListener, ViewPagerItemClicked {
    var datetime: String? = null
    var TAG: String = VendorEventTwoFragment::class.java.simpleName
    var vendorEventServiceRequest = VendorEventServiceRequest()
    var vendorEventDetail = EventDetail()
    var imageFile: File? = null

    val REQUEST_CODE = 123
    var mResults = ArrayList<String>()
    var SCREEN_COUNT: Int = 0
    var homeSliderAdapter: HomeSliderAdapter? = null
    var imageFiles = ArrayList<File>()
    var images = ArrayList<BusinessImage>()
    var deletefiles = ArrayList<String>()

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.iv_back -> {
                activity?.onBackPressed()
            }
            R.id.ed_starttime -> {
//                AppUtill.getTime( ed_starttime,activity!!)
                AppUtils.getTimeNew(ed_starttime, activity as AppCompatActivity?)
//                AppUtill.getTime(ed_starttime, activity!!)
            }
            R.id.ed_closingtime -> {
                AppUtils.getTimeNew(ed_closingtime, activity as AppCompatActivity?)
//                AppUtill.getTime(ed_closingtime, activity!!)
            }
            R.id.ed_startdate -> {
                AppUtill.getDate(ed_startdate, activity!!)
            }
            R.id.ed_enddate -> {
                AppUtill.getDate(ed_enddate, activity!!)
            }
            R.id.btn_next -> {
                validateField()
            }
            R.id.img_upload -> {
                captureMultipleImages()
            }
            R.id.img_camera -> {
                captureMultipleImages()
            }

        }
    }

    companion object {
        fun newInstance(from: String): VendorEventTwoFragment {
            val args = Bundle()
            args.putString("name", from)
//            args.putInt("value", value)
            val fragment = VendorEventTwoFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var mView = inflater.inflate(R.layout.fragment_event_two_vendor, container, false)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        til_eventprice.hint=""+til_eventprice.hint+"("+AppUtils.getCurrencySymbol()+")"
        if (context is VendorEventsActivity)
            vendorEventServiceRequest = (context as VendorEventsActivity).getEventFieldPojo()
        else if (context is VendorEventsDetailActivity) {
            vendorEventDetail = (context as VendorEventsDetailActivity).getEventFieldPojo()
        }



        getfilledData()
        initView(view)
    }

    fun initView(view: View) {
        btn_next.setOnClickListener(this)
        ed_starttime.setOnClickListener(this)
        ed_closingtime.setOnClickListener(this)
        ed_startdate.setOnClickListener(this)
        ed_enddate.setOnClickListener(this)
        img_camera.setOnClickListener(this)
        img_upload.setOnClickListener(this)
        iv_profile.setOnClickListener(this)
    }

    private fun putAllDataToFieldMap() {
        if (context is VendorEventsActivity) {
            vendorEventServiceRequest.event_name = ed_eventname.text.toString()
            vendorEventServiceRequest.total_tickets = ed_ticketcount.text.toString()
            vendorEventServiceRequest.ticket_price = ed_eventprice.text.toString()
            vendorEventServiceRequest.start_date = ed_startdate.text.toString()
            vendorEventServiceRequest.end_date = ed_enddate.text.toString()
            vendorEventServiceRequest.start_time = ed_starttime.text.toString()
            vendorEventServiceRequest.close_time = ed_closingtime.text.toString()
            vendorEventServiceRequest.description = ed_desc.text.toString()
            vendorEventServiceRequest.venue = ed_venue.text.toString()

        } else if (context is VendorEventsDetailActivity) {
            vendorEventDetail.name = ed_eventname.text.toString()
            vendorEventDetail.total_tickets = ed_ticketcount.text.toString()
            vendorEventDetail.ticket_price = ed_eventprice.text.toString()
            vendorEventDetail.start_date = ed_startdate.text.toString()
            vendorEventDetail.end_date = ed_enddate.text.toString()
            vendorEventDetail.start_time = ed_starttime.text.toString()
            vendorEventDetail.close_time = ed_closingtime.text.toString()
            vendorEventDetail.description = ed_desc.text.toString()
            vendorEventDetail.venue = ed_venue.text.toString()
        }
    }

    private fun getfilledData() {
        try {
            if (context is VendorEventsActivity) {
                ed_eventname.setText(vendorEventServiceRequest.event_name)
                ed_ticketcount.setText(vendorEventServiceRequest.total_tickets)
                ed_eventprice.setText(vendorEventServiceRequest.ticket_price)
                ed_startdate.setText(vendorEventServiceRequest.start_date)
                ed_enddate.setText(vendorEventServiceRequest.end_date)
                ed_starttime.setText(vendorEventServiceRequest.start_time)
                ed_closingtime.setText(vendorEventServiceRequest.close_time)
                ed_venue.setText(vendorEventServiceRequest.venue)
                ed_desc.setText(vendorEventServiceRequest.description)
                if ((activity as VendorEventsActivity).strImages.size > 0) {
                    mResults = (activity as VendorEventsActivity).strImages
                    var imgList = ArrayList<BusinessImage>()
                    for (url in mResults) {
                        imgList.add(BusinessImage(url, ""))
                    }
                    capturedImage(imgList)
                }
            } else if (context is VendorEventsDetailActivity) {
                ed_eventname.setText(vendorEventDetail.name)
                ed_ticketcount.setText(vendorEventDetail.total_tickets)
                ed_eventprice.setText(vendorEventDetail.ticket_price)
                ed_startdate.setText(vendorEventDetail.start_date)
                ed_enddate.setText(vendorEventDetail.end_date)
                ed_starttime.setText(vendorEventDetail.start_time)
                ed_closingtime.setText(vendorEventDetail.close_time)
                ed_venue.setText(vendorEventDetail.venue)
                ed_desc.setText(vendorEventDetail.description)



                mResults.clear()
                for (img in vendorEventDetail.event_images!!) {
                    mResults.add(img.url)
                }
                images.clear()
                images.addAll(vendorEventDetail.event_images!!)

                if (images.size == 0) {
                    viewPager.visibility = View.GONE
                } else {
                    viewPager.visibility = View.VISIBLE
                    homeSliderAdapter = HomeSliderAdapter(activity, images, this, true)
                    viewPager!!.setAdapter(homeSliderAdapter)
                    AppUtill.handlePager(activity!!, mResults.size, layoutDots, viewPager)
                }

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun validateField() {
        if (ed_eventname.getText().toString().isEmpty()) {
            UiValidator.setValidationError(til_eventname, getString(R.string.field_required))
            return
        }
        if (til_eventname.isErrorEnabled()) {
            UiValidator.disableValidationError(til_eventname)
        }

        if (ed_ticketcount.getText().toString().isEmpty()) {
            UiValidator.setValidationError(til_ticketcount, getString(R.string.field_required))
            return
        }
        if (til_ticketcount.isErrorEnabled()) {
            UiValidator.disableValidationError(til_ticketcount)
        }
        if (ed_eventprice.getText().toString().isEmpty()) {
            UiValidator.setValidationError(til_eventprice, getString(R.string.field_required))
            return
        }
        if (til_eventprice.isErrorEnabled()) {
            UiValidator.disableValidationError(til_eventprice)
        }

        if (ed_startdate.getText().toString().isEmpty()) {
            UiValidator.setValidationError(til_startdate, getString(R.string.field_required))
            return
        }
        if (til_startdate.isErrorEnabled()) {
            UiValidator.disableValidationError(til_startdate)
        }

        if (ed_enddate.getText().toString().isEmpty()) {
            UiValidator.setValidationError(til_enddate, getString(R.string.field_required))
            return
        }
        if (til_enddate.isErrorEnabled()) {
            UiValidator.disableValidationError(til_enddate)
        }

        if (!AppUtill.compareDate(ed_startdate.getText().toString(), ed_enddate.getText().toString())) {
            UiValidator.displayMsgSnack(nest,activity, getString(R.string.choose_valid_time_slot))
            return
        }


        if (ed_starttime.getText().toString().isEmpty()) {
            UiValidator.setValidationError(til_starttime, getString(R.string.field_required))
            return
        }
        if (til_starttime.isErrorEnabled()) {
            UiValidator.disableValidationError(til_starttime)
        }

        if (ed_closingtime.getText().toString().isEmpty()) {
            UiValidator.setValidationError(til_closetime, getString(R.string.field_required))
            return
        }
        if (til_closetime.isErrorEnabled()) {
            UiValidator.disableValidationError(til_closetime)
        }

        if (!AppUtill.compareTime(ed_starttime.getText().toString(), ed_closingtime.getText().toString())) {
            UiValidator.displayMsgSnack(nest,activity, getString(R.string.choose_valid_time_slot))
            return
        }

        if (ed_venue.getText().toString().isEmpty()) {
            UiValidator.setValidationError(til_venue, getString(R.string.field_required))
            return
        }
        if (til_venue.isErrorEnabled()) {
            UiValidator.disableValidationError(til_venue)
        }

        if (ed_desc.getText().toString().isEmpty()) {
            UiValidator.setValidationError(til_desc, getString(R.string.field_required))
            return
        }
        if (til_desc.isErrorEnabled()) {
            UiValidator.disableValidationError(til_desc)
        }
        UiValidator.hideSoftKeyboard(context as AppCompatActivity)


        putAllDataToFieldMap()
        if (activity is VendorEventsDetailActivity) {
            if ((activity as VendorEventsDetailActivity).isFromedit()) {
                (activity as VendorEventsDetailActivity).hitApiEdit()
            }
        } else if (activity is VendorEventsActivity) {
            if (activity is VendorEventsActivity)
                (activity as VendorEventsActivity).setDisplayFragment(3, activity!!.resources.getString(R.string.review_your_bussiness), false)

        }
    }

    private fun captureMultipleImages() {
        val intent = Intent(activity, ImagesSelectorActivity::class.java)
        intent.putExtra(SelectorSettings.SELECTOR_MAX_IMAGE_NUMBER, 1)
        intent.putExtra(SelectorSettings.SELECTOR_MIN_IMAGE_SIZE, AppConstants.VENDOR_BANNER_IMG_COMPRESSION_SIZE)
        intent.putExtra(SelectorSettings.SELECTOR_SHOW_CAMERA, false)
        intent.putStringArrayListExtra(SelectorSettings.SELECTOR_INITIAL_SELECTED_LIST, mResults)
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

    fun capturedImage(results: ArrayList<BusinessImage>) {
        images.clear()
        imageFiles.clear()
        images = results
        viewPager.visibility = View.VISIBLE
        if (context is VendorEventsActivity) {
            initViewPager(false)
        } else if (context is VendorEventsDetailActivity) {
            initViewPager(true)
        }


        if (results != null) {
            for (imgUrl in results) {
                imageFiles.add(File(imgUrl.url))
            }
        }

        if (activity is VendorEventsActivity) {
            (activity as VendorEventsActivity).ser_imageFile = imageFiles
            (activity as VendorEventsActivity).strImages = mResults
        } else if (activity is VendorEventsDetailActivity) {
            (activity as VendorEventsDetailActivity).uploadEventPic(imageFiles, vendorEventDetail.service_id!!)
        }
    }

    private fun initViewPager(fromEdit: Boolean) {
        SCREEN_COUNT = mResults.size
        homeSliderAdapter = HomeSliderAdapter(activity, images, this, fromEdit)
        viewPager!!.setAdapter(homeSliderAdapter)
        AppUtill.handlePager(activity!!, mResults.size, layoutDots, viewPager)
    }

    override fun onPagerItemClicked(position: Int) {
        deletefiles.clear()
        deletefiles.add(images.get(position).url)
        hitDeleteImage(deletefiles, vendorEventDetail.service_id.toString()!!, position)
    }

    fun manageFromEdit() {
        if (activity is VendorEventsDetailActivity) {
            if ((activity as VendorEventsDetailActivity).isFromedit()) {
                btn_next.setText((activity as VendorEventsDetailActivity).getString(R.string.save))
            }
        }
    }

    fun hitDeleteImage(deletefiles: ArrayList<String>, service_id: String, position: Int) {
        if (AppUtils.isInternetConnected(activity)) {
        ProgressDialogLoader.progressDialogCreation(activity!!, getString(R.string.please_wait))
        var apiInterface = ApiClient.client.create(ApiInterface::class.java)
        apiInterface?.deleteEventImage("Bearer " + SharedPreferenceManager.getAuthToken(), deletefiles, service_id)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(object : Observer<BusinessImagesResponse> {
                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onNext(userResponse: BusinessImagesResponse) {
                        ProgressDialogLoader.progressDialogDismiss()
                        AppLog.e(TAG, userResponse.toString())
                        if (userResponse.status.equals(AppConstants.KEY_SUCCESS)) {
                            activity!!.finish()
                        } else {
                            UiValidator.displayMsgSnack(nest,activity, userResponse.message)
                        }
                    }

                    override fun onError(e: Throwable) {
                        ProgressDialogLoader.progressDialogDismiss()

                        if (e != null){
                        UiValidator.displayMsgSnack(nest,activity!!, e.message)
                        AppLog.e(TAG, e.message)}
                    }

                    override fun onComplete() {

                    }
                })
        } else {
            UiValidator.displayMsgSnack(nest, activity, getString(R.string.no_internet_connection))
        }
    }
}