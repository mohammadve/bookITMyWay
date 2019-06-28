package com.virtual.customervendor.vendor.ui.activity

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.*
import android.widget.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.virtual.customervendor.R
import com.virtual.customervendor.commonActivity.BaseActivity
import com.virtual.customervendor.customer.ui.fragments.HelpFragment
import com.virtual.customervendor.customer.ui.fragments.RateUsFragment
import com.virtual.customervendor.managers.CachingManager
import com.virtual.customervendor.managers.SharedPreferenceManager
import com.virtual.customervendor.model.BusinessDetail
import com.virtual.customervendor.model.NotificationClass
import com.virtual.customervendor.networks.ApiClient
import com.virtual.customervendor.networks.ApiInterface
import com.virtual.customervendor.utills.*
import com.virtual.customervendor.vendor.ui.fragments.*
import kotlinx.android.synthetic.main.activity_dashboard_vendor.*
import kotlinx.android.synthetic.main.custom_toolbar_vendor.*
import java.io.Serializable

class DashBoardActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, PopupMenu.OnMenuItemClickListener,
        IssueDialogFragment.issueSelectionInterface {
    private val mActionHandler = Handler()
    private val CLOSE_DELAY_TIME = 500
    private var toolbar: Toolbar? = null
    private var mTitle: TextView? = null
    private var drawer: DrawerLayout? = null
    var apiInterface: ApiInterface? = null
    var TAG: String = DashBoardActivity::class.java.simpleName
    var menuList: Array<String>? = null
    var businessDetail = BusinessDetail()
    var back_pressed: Long = 0

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.iv_back -> {
                handleHamburg()
            }
            R.id.iv_edit -> {
                if (mTitle?.text!!.equals(resources.getString(R.string.app_name))) {
                    val intent: Intent = Intent(this, VendorNotificationActivity::class.java)
                    startActivity(intent)
                    SlideAnimationUtill.slideNextAnimation(this)
                } else {
                    val intent: Intent = Intent(this, SignUpActivity::class.java)
                    intent.putExtra(AppConstants.FROM_EDIT, true)
                    startActivity(intent)
                    SlideAnimationUtill.slideNextAnimation(this)
                }
            }
            R.id.ivUserPic -> {
                callEditProfile(SharedPreferenceManager.getCategoryId(), SharedPreferenceManager.getSubcategoryId())
            }
            R.id.iv_switch -> {
                showPopup(iv_switch)
            }
            R.id.iv_history -> {
                var intent: Intent = Intent(this, VendorHistoryActivity::class.java)
                startActivity(intent)
                SlideAnimationUtill.slideNextAnimation(this as AppCompatActivity)
            }

        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.cut_switch -> {
                val intent: Intent = Intent(this, com.virtual.customervendor.customer.ui.activity.DashBoardActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                SlideAnimationUtill.slideNextAnimation(this)
                finish()
                return true
            }
            else -> return false
        }
    }

    fun callEditProfile(catergoty: Int, subcatergoty: Int) {
        when (catergoty) {
            AppConstants.CAT_TRANSPORTATION -> {
                when (subcatergoty) {
                    AppConstants.SUBCAT_TRANS_TAXI -> callEditActivity(businessDetail, VenTaxiProfileEditActivity())
                    AppConstants.SUBCAT_TRANS_LIMO -> callEditActivity(businessDetail, VenTaxiProfileEditActivity())
                    AppConstants.SUBCAT_TRANS_TOURBUS -> callEditActivity(businessDetail, VenTaxiProfileEditActivity())
                    AppConstants.SUBCAT_TRANS_SIGHTSEEING -> callEditActivity(businessDetail, VenSightSeeingProfileEditActivity())
                }
            }
            AppConstants.CAT_RESTAURANT_DINNIG -> {
                when (subcatergoty) {
                    AppConstants.SUBCAT_RESTAURANT_DINNIG -> callEditActivity(businessDetail, VendorRestaurantProfileActivity())
                }
            }
            AppConstants.CAT_HEALTH_BODYCARE -> {
                when (subcatergoty) {
                    AppConstants.SUBCAT_HEALTH_DOCTOR -> callEditActivity(businessDetail, VendorHealthProfileEditActivity())
                    AppConstants.SUBCAT_HEALTH_HAIR -> callEditActivity(businessDetail, VendorHealthProfileEditActivity())
                    AppConstants.SUBCAT_HEALTH_NAIL -> callEditActivity(businessDetail, VendorHealthProfileEditActivity())
                    AppConstants.SUBCAT_HEALTH_PHYSIO -> callEditActivity(businessDetail, VendorHealthProfileEditActivity())
                }
            }
            AppConstants.CAT_PARKING_VALET -> {
                when (subcatergoty) {
                    AppConstants.SUBCAT_PARKING_VALET -> callEditActivity(businessDetail, VendorParkingProfileEditActivity())
                }
            }
            AppConstants.CAT_EVENT_TICKET -> {
                when (subcatergoty) {
                    AppConstants.SUBCAT_EVENT_TICKET -> callEditActivity(businessDetail, VendorEventProfileEditActivity())
                }
            }
            AppConstants.CAT_STORE_SELLER -> {
                when (subcatergoty) {
                    AppConstants.SUBCAT_STORE_SELLER -> callEditActivity(businessDetail, VendorStoreProfileEditActivity())
                }
            }

        }
    }


    fun callEditActivity(keyValue: BusinessDetail, activity1: AppCompatActivity) {
        var intent: Intent = Intent(this, activity1.javaClass)

        var bundle = Bundle()
        bundle.putSerializable(AppConstants.BUSINESS_DATA, keyValue)
        intent.putExtras(bundle)
        startActivityForResult(intent, 222)
        SlideAnimationUtill.slideNextAnimation(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard_vendor)
        businessDetail = intent.extras.getSerializable(AppConstants.BUSINESS_DATA) as BusinessDetail
        AppLog.e(TAG, businessDetail.toString())
        if (businessDetail != null) {
            apiInterface = ApiClient.client.create(ApiInterface::class.java)
            SharedPreferenceManager.setCategoryId(businessDetail.category_id!!)
            SharedPreferenceManager.setSubcategoryId(businessDetail.subcategory_id!!)
            SharedPreferenceManager.setBussinessID(businessDetail.business_id.toString())
            SharedPreferenceManager.setServiceId(businessDetail.service_id!!)
//            hitApi(businessDetail)
            initView(businessDetail)
            initData()
            setUpDrawer()
        }


        if (intent.extras != null && intent.extras.getSerializable(AppConstants.NOTIFICATION_DATA) != null) {
            var notificationData = intent.extras.getSerializable(AppConstants.NOTIFICATION_DATA) as NotificationClass
            if (notificationData != null) {
                if (!notificationData!!.user_type.equals("customer", true)) {
                    if (notificationData!!.type.equals("newvendororder")) {
                        var intent: Intent = Intent(this, VendorOrderDetailActivity::class.java)
                        intent.putExtra("orderid", notificationData!!.object_id)
                        startActivity(intent)
                    } else {
                        var intent: Intent = Intent(this, VendorNotificationActivity::class.java)
                        startActivity(intent)
                    }
                    SlideAnimationUtill.slideNextAnimation(this)
                }
            }
        }
    }

    private fun initView(businessDetail: BusinessDetail) {
        AppUtils.hideSoftKeyboard(this)
        drawer = findViewById(R.id.drawer_layout) as DrawerLayout
        toolbar = findViewById(R.id.toolbar) as Toolbar
        mTitle = toolbar?.findViewById(R.id.tv_toolbarTitleText) as TextView
        mTitle?.text = getString(R.string.app_name)
        val iv_back = toolbar?.findViewById(R.id.iv_back) as ImageView
        val iv_edit = toolbar?.findViewById(R.id.iv_edit) as ImageView
        val iv_history = toolbar?.findViewById(R.id.iv_history) as ImageView
        iv_back.setImageDrawable(resources.getDrawable(R.drawable.ic_hamburg_blue))
        iv_history.setOnClickListener(this)

        var tvUserName = findViewById(R.id.tvUserName) as TextView
        var ivUserPic = findViewById(R.id.ivUserPic) as ImageView
        ivUserPic.setOnClickListener(this)
        iv_edit.setOnClickListener(this)
        iv_switch.setOnClickListener(this)
        iv_back.setOnClickListener(this)
        tvUserName.setText(businessDetail.business_name)
        if (businessDetail.business_image != null) {
            Glide.with(this).load(businessDetail.business_image).apply(RequestOptions().circleCrop().placeholder(R.drawable.place_holder_round)).into(ivUserPic)
        }
    }

    private fun initData() {
        val rvNumbers = findViewById<ListView>(R.id.list) as ListView
        menuList = arrayOf(getString(R.string.home), getString(R.string.orders), getString(R.string.settings), getString(R.string.about), getString(R.string.help_center), getString(R.string.rate_us))
        if (SharedPreferenceManager.getCategoryId() == AppConstants.CAT_EVENT_TICKET) {
            menuList = arrayOf(getString(R.string.home), getString(R.string.booked_ticket), getString(R.string.my_events), getString(R.string.followers), getString(R.string.settings), getString(R.string.about), getString(R.string.help_center), getString(R.string.rate_us))
        } else if (SharedPreferenceManager.getCategoryId() == AppConstants.CAT_STORE_SELLER) {
            menuList = arrayOf(getString(R.string.home), getString(R.string.orders), getString(R.string.store_items), getString(R.string.followers), getString(R.string.settings), getString(R.string.about), getString(R.string.help_center), getString(R.string.rate_us))
        } else if (SharedPreferenceManager.getCategoryId() == AppConstants.CAT_RESTAURANT_DINNIG) {
            menuList = arrayOf(getString(R.string.home), getString(R.string.orders), getString(R.string.menu), getString(R.string.followers), getString(R.string.settings), getString(R.string.about), getString(R.string.help_center), getString(R.string.rate_us))
        } else if (SharedPreferenceManager.getCategoryId() == AppConstants.CAT_HEALTH_BODYCARE) {
            menuList = arrayOf(getString(R.string.home), getString(R.string.appoint), "", getString(R.string.followers), getString(R.string.settings), getString(R.string.about), getString(R.string.help_center), getString(R.string.rate_us))
        } else if (SharedPreferenceManager.getSubcategoryId() == AppConstants.SUBCAT_TRANS_SIGHTSEEING) {
            menuList = arrayOf(getString(R.string.home), getString(R.string.orders), getString(R.string.packages), getString(R.string.followers), getString(R.string.settings), getString(R.string.about), getString(R.string.help_center), getString(R.string.rate_us))
        } else {
            menuList = arrayOf(getString(R.string.home), getString(R.string.orders), "", getString(R.string.followers), getString(R.string.settings), getString(R.string.about), getString(R.string.help_center), getString(R.string.rate_us))
        }
        val itemArrayAdapter = ItemArrayAdapter(this, R.layout.checklist_item, menuList!!)
        rvNumbers.adapter = itemArrayAdapter

        rvNumbers.setOnItemClickListener { parent, view, position, id ->
            drawer?.closeDrawer(GravityCompat.START)
            mActionHandler.postDelayed({
                when (position) {

                    0 -> {
                        iv_history.visibility = View.GONE
                        iv_edit.setImageResource(R.drawable.ic_notifications)
                        iv_edit.visibility = View.VISIBLE
                        if (!(supportFragmentManager.findFragmentById(R.id.flContent) is HomeFragment)) {
//                            iv_edit.setImageResource(R.drawable.ic_notifications)
//                            iv_edit.visibility = View.VISIBLE
                            replaceFragment(HomeFragment::class.java, getString(R.string.app_name))
                        }
                    }
                    1 -> {
                        iv_edit.visibility = View.GONE
                        iv_history.visibility = View.GONE
                        if (!(supportFragmentManager.findFragmentById(R.id.flContent) is VendorOrdersFragment)) {
                            if (SharedPreferenceManager.getCategoryId() == AppConstants.CAT_HEALTH_BODYCARE) {
                                replaceFragment(VendorOrdersFragment::class.java, getString(R.string.appoint))
                                iv_history.visibility = View.VISIBLE
                            } else {
                                replaceFragment(VendorOrdersFragment::class.java, getString(R.string.my_orders))
                                iv_history.visibility = View.VISIBLE
                            }
                        }
                    }
                    2 -> {
                        if (SharedPreferenceManager.getCategoryId() == AppConstants.CAT_EVENT_TICKET) {
                            callAddActivity(VendorListEventActivity())
                        } else if (SharedPreferenceManager.getCategoryId() == AppConstants.CAT_STORE_SELLER) {
                            callAddActivity(VendorListStoreItemsActivity())
                        } else if (SharedPreferenceManager.getCategoryId() == AppConstants.CAT_RESTAURANT_DINNIG) {
                            callAddActivity(VendorListRestaurantMenuActivity())
                        } else if (SharedPreferenceManager.getSubcategoryId() == AppConstants.SUBCAT_TRANS_SIGHTSEEING) {
                            callAddActivity(VendorSightSeenPackages())
                        }
                    }

                    3 -> {
                        iv_edit.visibility = View.GONE
                        iv_history.visibility = View.GONE
                        if (!(supportFragmentManager.findFragmentById(R.id.flContent) is VendorFollowersFragment)) {
                            replaceFragment(VendorFollowersFragment::class.java, getString(R.string.followers))
                        }
                    }
                    4 -> {
                        iv_edit.visibility = View.GONE
                        iv_history.visibility = View.GONE
                        if (!(supportFragmentManager.findFragmentById(R.id.flContent) is SettingFragment)) {
                            iv_edit.visibility = View.VISIBLE
                            replaceFragment(SettingFragment::class.java, getString(R.string.settings))
//                            UiValidator.displayMsg(this, "In Progress")
                        }
                    }
                    5 -> {
                        iv_edit.visibility = View.GONE
                        iv_history.visibility = View.GONE
                        if (!(supportFragmentManager.findFragmentById(R.id.flContent) is AboutFragment)) {
                            replaceFragment(AboutFragment::class.java, getString(R.string.about))
                        }
                    }
                    6 -> {
                        iv_edit.visibility = View.GONE
                        iv_history.visibility = View.GONE
                        if (!(supportFragmentManager.findFragmentById(R.id.flContent) is HelpFragment)) {
                            replaceFragment(HelpFragment::class.java, getString(R.string.help_center))
                            iv_edit.visibility = View.GONE
//                            UiValidator.displayMsg(this, "In Progress")
                        }
                    }
                    7 -> {
                        iv_edit.visibility = View.GONE
                        iv_history.visibility = View.GONE
                        if (!(supportFragmentManager.findFragmentById(R.id.flContent) is RateUsFragment)) {
                            var uri = Uri.parse("market://details?id=" + getPackageName())
                            var myAppLinkToMarket = Intent(Intent.ACTION_VIEW, uri)
                            try {
                                startActivity(myAppLinkToMarket);
                            } catch (e: ActivityNotFoundException) {
                                Toast.makeText(this, " unable to find market app", Toast.LENGTH_LONG).show();
                            }

//                            UiValidator.displayMsg(this, "In Progress")
                        }
                    }

                }
            }, CLOSE_DELAY_TIME.toLong())
        }
    }

    private fun setUpDrawer() {
        setDefaultSelectFragment()
    }

    private fun setDefaultSelectFragment() {
        iv_edit.setImageResource(R.drawable.ic_notifications)
        iv_edit.visibility = View.VISIBLE
        replaceFragment(HomeFragment::class.java, getString(R.string.app_name))
    }

    override fun onBackPressed() {
        if (drawer?.isDrawerOpen(GravityCompat.START)!!) {
            drawer?.closeDrawer(GravityCompat.START)
        } else if (!(supportFragmentManager.findFragmentById(R.id.flContent) is HomeFragment)) {
            iv_history.visibility = View.GONE
            iv_edit.setImageResource(R.drawable.ic_notifications)
            iv_edit.visibility = View.VISIBLE
            replaceFragment(HomeFragment::class.java, getString(R.string.app_name))

        } else {
            if (back_pressed + 1000 > System.currentTimeMillis()) {
                super.onBackPressed();
            } else {
                Toast.makeText(getBaseContext(),
                        "Press once again to exit!", Toast.LENGTH_SHORT)
                        .show();
            }
            back_pressed = System.currentTimeMillis();
        }
    }

    fun handleHamburg() {
        if (drawer?.isDrawerOpen(GravityCompat.START)!!) {
            drawer?.closeDrawer(GravityCompat.START)
        } else {
            drawer?.openDrawer(Gravity.START)
        }
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        drawer.closeDrawer(GravityCompat.START)
//        mActionHandler.postDelayed({ selectedDrawerItem(item) }, CLOSE_DELAY_TIME.toLong())
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        AppLog.e("checkdtaa---", "actiii");
        return super.onOptionsItemSelected(item)
    }

    /**
     * This method used to replaced the old fragment with current selected fragment
     *
     * @param fragmentClass
     */
    private fun replaceFragment(fragmentClass: Class<*>, titleName: String) {
        setTitleBar(titleName)
        var fragment: Fragment? = null
        try {
            fragment = fragmentClass.newInstance() as Fragment
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // Insert the fragment by replacing any existing fragment
        val fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction().setCustomAnimations(R.anim.slide_from_right, R.anim.slide_to_left1).replace(R.id.flContent, fragment!!).commit()
    }

    /**
     * Change the Title bar according selected fragment
     *
     * @param titleName
     */
    fun setTitleBar(titleName: String) {
        mTitle?.text = titleName
//        iv_notification.visibility = View.GONE
    }

    /**
     * Exit from the App//TODO change it later
     */
    private fun logOut() {
//        startActivity(Intent(this@HomeActivity, JoinActivity::class.java))
//        finish()
    }

    fun showPopup(v: View) {
        val popup = PopupMenu(this, v)
        val inflater = popup.menuInflater
        inflater.inflate(R.menu.vendor_popupmenu, popup.menu)
        popup.setOnMenuItemClickListener(this)
        popup.show()
    }


    private fun handleError(t: Throwable) {
        ProgressDialogLoader.progressDialogDismiss()
        AppLog.e(TAG, t.message)
    }

    fun callAddActivity(activity1: AppCompatActivity) {
        var intent: Intent = Intent(this, activity1.javaClass)
        intent.putExtra(AppConstants.BUSINESS_DATA, businessDetail as Serializable)
        startActivity(intent)
        SlideAnimationUtill.slideNextAnimation(this)
    }


    class ItemArrayAdapter(context: Context, private val listItemLayout: Int, internal var itemList: Array<String>) : ArrayAdapter<String>(context, listItemLayout, itemList) {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            var convertView = convertView
            val item = getItem(position)

            val viewHolder: ViewHolder
            if (convertView == null) {
                viewHolder = ViewHolder()
                val inflater = LayoutInflater.from(context)
                convertView = inflater.inflate(listItemLayout, parent, false)
                viewHolder.item = convertView?.findViewById<View>(R.id.tv_text) as TextView
                viewHolder.ll_main = convertView.findViewById<View>(R.id.ll_main) as LinearLayout
                convertView.tag = viewHolder
            } else {
                viewHolder = convertView.tag as ViewHolder
            }

            if (!item.equals("")) {
                viewHolder.ll_main?.visibility = View.VISIBLE
                viewHolder.item?.visibility = View.VISIBLE
                viewHolder.item?.text = item
            } else {
                viewHolder.ll_main?.visibility = View.GONE
                viewHolder.item?.visibility = View.GONE
            }
            return convertView
        }

        internal inner class ViewHolder {
            var item: TextView? = null
            var ll_main: LinearLayout? = null

        }
    }

    override fun selectedIssueUpdate(selectedText: String, fromWhere: String?) {
        //tv_issue.text=bean
        val fragment = supportFragmentManager.findFragmentById(R.id.flContent)
        if (fragment != null && fragment.isVisible) {
            if (fragment is HelpFragment) fragment.updateText(selectedText)
        }
    }

    fun setUserData(b_name: String, b_img: String) {
        if (b_name != null)
            tvUserName.setText(b_name)
        if (b_img != null) {
            Glide.with(this).load(b_img).apply(RequestOptions().circleCrop().placeholder(R.drawable.place_holder_round)).into(ivUserPic)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 222 && resultCode == Activity.RESULT_OK) {
            when (SharedPreferenceManager.getCategoryId()) {
                AppConstants.CAT_TRANSPORTATION -> {
                    when (SharedPreferenceManager.getSubcategoryId()) {
                        AppConstants.SUBCAT_TRANS_TAXI, AppConstants.SUBCAT_TRANS_LIMO, AppConstants.SUBCAT_TRANS_TOURBUS -> {
                            if (CachingManager.getVendorTaxiInfo().business_images.size > 0)
                                setUserData(CachingManager.getVendorTaxiInfo().business_name!!, CachingManager.getVendorTaxiInfo().business_images.get(0).url)
                            else
                                setUserData(CachingManager.getVendorTaxiInfo().business_name!!, "")

                        }
                        AppConstants.SUBCAT_TRANS_SIGHTSEEING -> {
                            if (CachingManager.getVendorSightSeenInfo().business_images.size > 0)
                                setUserData(CachingManager.getVendorSightSeenInfo().business_name!!, CachingManager.getVendorSightSeenInfo().business_images.get(0).url)
                            else
                                setUserData(CachingManager.getVendorSightSeenInfo().business_name!!, "")
                        }
                    }
                }
                AppConstants.CAT_RESTAURANT_DINNIG -> {
                    when (SharedPreferenceManager.getSubcategoryId()) {
                        AppConstants.SUBCAT_RESTAURANT_DINNIG -> {
                            if (CachingManager.getVendorRestaurantInfo().business_images.size > 0)
                                setUserData(CachingManager.getVendorRestaurantInfo().business_name!!, CachingManager.getVendorRestaurantInfo().business_images.get(0).url)
                            else
                                setUserData(CachingManager.getVendorRestaurantInfo().business_name!!, "")
                        }
                    }
                }
                AppConstants.CAT_HEALTH_BODYCARE -> {
                    when (SharedPreferenceManager.getSubcategoryId()) {
                        AppConstants.SUBCAT_HEALTH_DOCTOR, AppConstants.SUBCAT_HEALTH_HAIR, AppConstants.SUBCAT_HEALTH_NAIL, AppConstants.SUBCAT_HEALTH_PHYSIO -> {
                            if (CachingManager.getVendorDoctorInfo().business_images.size > 0)
                                setUserData(CachingManager.getVendorDoctorInfo().business_name!!, CachingManager.getVendorDoctorInfo().business_images.get(0).url)
                            else
                                setUserData(CachingManager.getVendorDoctorInfo().business_name!!, "")
                        }
                    }
                }

                AppConstants.CAT_PARKING_VALET -> {
                    when (SharedPreferenceManager.getSubcategoryId()) {
                        AppConstants.SUBCAT_PARKING_VALET -> {
                            if (CachingManager.getVendorParkingInfo().business_images.size > 0)
                                setUserData(CachingManager.getVendorParkingInfo().business_name!!, CachingManager.getVendorParkingInfo().business_images.get(0).url)
                            else
                                setUserData(CachingManager.getVendorParkingInfo().business_name!!, "")
                        }
                    }
                }

                AppConstants.CAT_EVENT_TICKET -> {
                    when (SharedPreferenceManager.getSubcategoryId()) {
                        AppConstants.SUBCAT_EVENT_TICKET -> {
                            if (CachingManager.getVendorEventInfo().business_images.size > 0)
                                setUserData(CachingManager.getVendorEventInfo().business_name!!, CachingManager.getVendorEventInfo().business_images.get(0).url)
                            else
                                setUserData(CachingManager.getVendorEventInfo().business_name!!, "")
                        }
                    }
                }
                AppConstants.CAT_STORE_SELLER -> {
                    when (SharedPreferenceManager.getSubcategoryId()) {
                        AppConstants.SUBCAT_STORE_SELLER -> {
                            if (CachingManager.getVendorStoreInfo().business_images.size > 0)
                                setUserData(CachingManager.getVendorStoreInfo().business_name!!, CachingManager.getVendorStoreInfo().business_images.get(0).url)
                            else
                                setUserData(CachingManager.getVendorStoreInfo().business_name!!, "")
                        }
                    }
                }
            }


        }else{
            AppLog.e(TAG,data.toString())
            if ((supportFragmentManager.findFragmentById(R.id.flContent) is VendorOrdersFragment)) {
                (supportFragmentManager.findFragmentById(R.id.flContent) as VendorOrdersFragment).onActivityResult(requestCode, resultCode, data)
            }
        }
    }
}
