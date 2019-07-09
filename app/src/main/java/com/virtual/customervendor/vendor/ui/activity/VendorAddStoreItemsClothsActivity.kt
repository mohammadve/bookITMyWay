package com.virtual.customervendor.vendor.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.virtual.customer_vendor.utill.AppUtill
import com.virtual.customervendor.R
import com.virtual.customervendor.customer.ui.ViewPagerItemClicked
import com.virtual.customervendor.customer.ui.adapter.HomeSliderAdapter
import com.virtual.customervendor.customer.ui.dialogFragment.ClothColorSelectionDialogFragmentMulti
import com.virtual.customervendor.customer.ui.dialogFragment.ClothSizeSelectionDialogFragmentMulti
import com.virtual.customervendor.listener.ClothSizeRemovedListener
import com.virtual.customervendor.managers.SharedPreferenceManager
import com.virtual.customervendor.model.AddItemClothRequest
import com.virtual.customervendor.model.BusinessImage
import com.virtual.customervendor.model.ProductCategoryModel
import com.virtual.customervendor.model.response.StoreClothColorModel
import com.virtual.customervendor.model.response.StoreClothSizeModel
import com.virtual.customervendor.model.response.StoreListingResponse
import com.virtual.customervendor.networks.ApiClient
import com.virtual.customervendor.networks.ApiInterface
import com.virtual.customervendor.utills.*
import com.virtual.customervendor.vendor.ui.adapter.StoreSelectedSizePArentAdapter
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import com.zfdang.multiple_images_selector.ImagesSelectorActivity
import com.zfdang.multiple_images_selector.SelectorSettings
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_vendor_add_store_items_cloths.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class VendorAddStoreItemsClothsActivity : AppCompatActivity(), View.OnClickListener, ViewPagerItemClicked,
        ClothSizeSelectionDialogFragmentMulti.MultiSizeSelectionInterface,
        ClothColorSelectionDialogFragmentMulti.MultiColorSelectionInterface, ClothSizeRemovedListener, DatePickerDialog.OnDateSetListener {
    override fun onDateSet(view: DatePickerDialog?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        val c = Calendar.getInstance()
        c.set(Calendar.YEAR, year)
        c.set(Calendar.MONTH, monthOfYear)
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        val dat1e = SimpleDateFormat("dd-MM-yyyy").format(c.time)
        tv_selectDate.setText(dat1e)
    }

    override fun doneColor(bean: ArrayList<StoreClothColorModel>, fromWhere: String?) {
        AppLog.e(TAG, bean.toString() + "    -      " + fromWhere)
        var pos: Int = fromWhere!!.toInt()
        allColorBean.clear()
        allColorBean.addAll(bean)
        var selectedBean: ArrayList<StoreClothColorModel> = ArrayList()
        if (bean != null && bean.size > 0) {
            for (item in bean) {

                if (item.isSelected) {
                    selectedBean.add(item)
                }

            }
        }
        selectedSizeBean[pos].variants = selectedBean
        storeSelectedSizePArentAdapter!!.dismisColorSelectionDialog()
        storeSelectedSizePArentAdapter!!.notifyDataSetChanged()
        converttomap(selectedSizeBean[pos])

    }

    private fun converttomap(storeClothSizeModel: StoreClothSizeModel) {

    }


    override fun onSizeRemoved(clothSizeModel: StoreClothSizeModel?) {

        for (item in allSizeBean) {

            if (item.value.equals(clothSizeModel!!.value)) {
                item.isSelected = false
            }
        }
        var size = ""
        if (allSizeBean != null && allSizeBean.size > 0) {
            for (item in allSizeBean) {

                if (item.isSelected) {
                    size = size + "," + item.value
                }

            }

        }

        if (size.length > 0) {
            size = size.substring(1, size.length)
            if (sizeSelection.equals("size")) {
                tv_select_text_size.text = size
                tv_select_size.text = resources.getString(R.string.hint_enter_size_number)
            } else {
                tv_select_size.text = size
                tv_select_text_size.text = resources.getString(R.string.hint_enter_size_text)

            }
        } else {
            tv_select_text_size.text = resources.getString(R.string.hint_enter_size_text)
            tv_select_size.text = resources.getString(R.string.hint_enter_size_number)

            rl_size_text_parent.isEnabled = true
            rl_size_text_parent.isClickable = true
            rl_size_parent.isEnabled = true
            rl_size_parent.isClickable = true
        }


    }

    override fun doneSize(bean: ArrayList<StoreClothSizeModel>, fromWhere: String?) {
        allSizeBean.clear()
        allSizeBean.addAll(bean)
        AppLog.e(TAG, bean.toString())
        if (sizeDialogFragmentMulti != null) {
            sizeDialogFragmentMulti.dismiss()
        }
        selectedSizeBean.clear()
        tv_select_size.text = ""
        var size = ""

        if (bean != null && bean.size > 0) {

            if (sizeSelection.equals("size", true)) {
                rl_size_parent.isEnabled = false
                rl_size_parent.isClickable = false

                rl_size_text_parent.isEnabled = true
                rl_size_text_parent.isClickable = true

            } else {
                rl_size_text_parent.isEnabled = false
                rl_size_text_parent.isClickable = false
                rl_size_parent.isEnabled = true
                rl_size_parent.isClickable = true
            }

            for (item in bean) {

                if (item.isSelected) {
                    size = size + "," + item.value
                    selectedSizeBean.add(item)
                }

            }
            storeSelectedSizePArentAdapter!!.notifyDataSetChanged()

            if (size.length > 0) {
                size = size.substring(1, size.length)

                if (sizeSelection.equals("size")) {
                    tv_select_text_size.text = size
                    tv_select_size.text = resources.getString(R.string.hint_enter_size_number)
                } else {
                    tv_select_size.text = size
                    tv_select_text_size.text = resources.getString(R.string.hint_enter_size_text)

                }
            } else {

                tv_select_text_size.text = resources.getString(R.string.hint_enter_size_text)
                tv_select_size.text = resources.getString(R.string.hint_enter_size_number)
                rl_size_text_parent.isEnabled = true
                rl_size_text_parent.isClickable = true
                rl_size_parent.isEnabled = true
                rl_size_parent.isClickable = true

            }
        } else {

            tv_select_text_size.text = resources.getString(R.string.hint_enter_size_text)
            tv_select_size.text = resources.getString(R.string.hint_enter_size_number)
            rl_size_text_parent.isEnabled = true
            rl_size_text_parent.isClickable = true
            rl_size_parent.isEnabled = true
            rl_size_parent.isClickable = true


        }

    }


    override fun onPagerItemClicked(position: Int) {
    }

    var apiInterface: ApiInterface? = null

    private val selectedSizeBean: ArrayList<StoreClothSizeModel> = ArrayList()
    private val allSizeBean: ArrayList<StoreClothSizeModel> = ArrayList()
    private val allColorBean: ArrayList<StoreClothColorModel> = ArrayList()
    private var fManager: FragmentManager? = null
    private lateinit var sizeDialogFragmentMulti: ClothSizeSelectionDialogFragmentMulti
    var TAG: String = VendorAddStoreItemsClothsActivity::class.java.simpleName

    var storeSelectedSizePArentAdapter: StoreSelectedSizePArentAdapter? = null
    var manager: RecyclerView.LayoutManager? = null
    var storeClothcolorList: ArrayList<StoreClothColorModel> = ArrayList()
    var sizeSelection = ""
    var productModel = ProductCategoryModel()

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
            R.id.rl_size_parent -> {

                if (sizeSelection.equals("size")) {
                    allSizeBean.clear()
                }
                sizeSelection = "number"
                showSizeSelectionDialogMMutli()
            }
            R.id.rl_size_text_parent -> {
                if (sizeSelection.equals("number")) {
                    allSizeBean.clear()
                }
                sizeSelection = "size"

                showSizeSelectionDialogMMutli()
            }
            R.id.tv_selectDate -> {
                var selectedDays = java.util.ArrayList<Int>()
                for (i in 1..7) {
                    selectedDays.add(i)
                }
                val daysArray: Array<Calendar> = AppUtils.getEnabledDays(selectedDays)
                AppUtils.getDate(fragmentManager, this, daysArray)
            }
            R.id.btn_save -> {
               // hitApi(imageFiles)
            }

        }
    }


    private fun showSizeSelectionDialogMMutli() {
        var beanValue = Gson().toJson(allSizeBean, object : TypeToken<List<StoreClothSizeModel>>() {

        }.type)

        sizeDialogFragmentMulti = ClothSizeSelectionDialogFragmentMulti.newInstance("" + sizeSelection, beanValue)
        fManager = supportFragmentManager
        sizeDialogFragmentMulti.show(fManager, "My Dialog")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vendor_add_store_items_cloths)
        apiInterface = ApiClient.client.create(ApiInterface::class.java)
        productModel = intent.extras.get(AppConstants.OREDER_DATA) as ProductCategoryModel
        init()

        cb_add_to_relaese.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                ll_date_parent.visibility = View.VISIBLE
            } else {
                ll_date_parent.visibility = View.GONE

            }
        }
    }

    fun init() {
        toolbar = findViewById<AppBarLayout>(R.id.toolbar)
        mTitle = toolbar!!.findViewById(R.id.tv_toolbarTitleText) as TextView
        mTitle!!.text = getString(R.string.add_item)
        val iv_back = toolbar!!.findViewById(R.id.iv_back) as ImageView
        iv_back.setOnClickListener(this)
        img_camera.setOnClickListener(this)
        rl_size_parent.setOnClickListener(this)
        rl_size_text_parent.setOnClickListener(this)
        tv_selectDate.setOnClickListener(this)
        btn_save.setOnClickListener(this)
        initsizeRecyclerView()
    }


    private fun initsizeRecyclerView() {

        storeSelectedSizePArentAdapter = StoreSelectedSizePArentAdapter(this, AppConstants.FROM_ADDDATA, selectedSizeBean, allColorBean, this)
        manager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_slected_size.layoutManager = manager
        rv_slected_size.adapter = (storeSelectedSizePArentAdapter)
//        rv_slected_size.setNestedScrollingEnabled(false)
        rv_slected_size.setHasFixedSize(false)

    }


    private fun captureMultipleImages() {
// start multiple photos selector
        val intent = Intent(this, ImagesSelectorActivity::class.java)
// max value of images to be selected
        intent.putExtra(SelectorSettings.SELECTOR_MAX_IMAGE_NUMBER, 5)
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
        viewPager!!.adapter = homeSliderAdapter
        AppUtill.handlePager(this, mResults.size, layoutDots, viewPager)
    }


    fun hitApi(addfile: ArrayList<File>) {
        if (AppUtils.isInternetConnected(this)) {
            var additemClothRequest: AddItemClothRequest = AddItemClothRequest()
            additemClothRequest.item_name = ed_itemname.text.toString().trim()
            additemClothRequest.product_description = ""
            additemClothRequest.service_id = SharedPreferenceManager.getServiceId().toString()
            additemClothRequest.action = AppConstants.ACTION_ADD
            additemClothRequest.product_category = productModel.id
            additemClothRequest.sizes = selectedSizeBean
            additemClothRequest.isReleasingSoon = "0"

            var imageList = java.util.ArrayList<MultipartBody.Part>()
//            var deleteimageList = java.util.ArrayList<MultipartBody.Part>()
            if (addfile != null) {

                for (imageFile in addfile) {
                    val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile)
                    imageList.add(MultipartBody.Part.createFormData(AppKeys.STORE_PRODUCT_IMAGES, imageFile.name, requestFile))
                }
            }
            var map = HashMap<String, RequestBody>()

            var action = RequestBody.create(MediaType.parse("text/plain"), AppConstants.ACTION_ADD)
            map.put("action", action)

            var item_name = RequestBody.create(MediaType.parse("text/plain"), additemClothRequest.item_name)
            map.put("item_name", item_name)


            var item_description = RequestBody.create(MediaType.parse("text/plain"), "")
            map.put("product_description", item_description)

            var product_category = RequestBody.create(MediaType.parse("text/plain"), productModel.id)
            map.put("product_category", product_category)

            var service_id = RequestBody.create(MediaType.parse("text/plain"), SharedPreferenceManager.getServiceId().toString())
            map.put("service_id", service_id)

            var beanValue = Gson().toJson(selectedSizeBean, object : TypeToken<List<StoreClothSizeModel>>() {

            }.type)

            var sizes = RequestBody.create(MediaType.parse("text/plain"),beanValue)
            map.put("sizes", sizes)




            apiInterface?.storeItemAddDeleteEditCloth("Bearer " + SharedPreferenceManager.getAuthToken(), map, imageList)
                    ?.subscribeOn(Schedulers.io())
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe(object : io.reactivex.Observer<String> {
                        override fun onSubscribe(d: Disposable) {
                        }

                        override fun onNext(detailResponse: String) {
                            AppLog.e(TAG, detailResponse.toString())
//                            handleResults(detailResponse)
                        }

                        override fun onError(e: Throwable) {
//                            handleError(e)
                        }

                        override fun onComplete() {}
                    })



        } else {
            UiValidator.displayMsgSnack(start_lay, this, getString(R.string.no_internet_connection))
        }
    }
}
