package com.virtual.customervendor.customer.ui.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.annotation.RequiresApi
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.*
import android.widget.*
import com.virtual.customer_vendor.utill.AppUtill
import com.virtual.customervendor.R
import com.virtual.customervendor.commonActivity.BaseActivity
import com.virtual.customervendor.customer.ui.dialogFragment.UserVendorSwitchDialogFragment
import com.virtual.customervendor.customer.ui.dialogFragment.UserVendorSwitchDialogFragment.vendorSelectionInterface
import com.virtual.customervendor.customer.ui.fragments.*
import com.virtual.customervendor.managers.SharedPreferenceManager
import com.virtual.customervendor.model.BusinessDetail
import com.virtual.customervendor.model.NotificationClass
import com.virtual.customervendor.model.response.BusinessListResponse
import com.virtual.customervendor.networks.ApiClient
import com.virtual.customervendor.networks.ApiInterface
import com.virtual.customervendor.utills.*
import com.virtual.customervendor.vendor.ui.fragments.BecomeVendorHomeFragment
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_dashboard_customer.*
import kotlinx.android.synthetic.main.custom_toolbar_vendor.*


class DashBoardActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, vendorSelectionInterface {
    override fun selectedVendorUpdate(bean: BusinessDetail, fromWhere: String?) {
        AppLog.e(TAG, bean.toString())
        var intent: Intent = Intent(this, com.virtual.customervendor.vendor.ui.activity.DashBoardActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        var bundle = Bundle()
        bundle.putSerializable(AppConstants.BUSINESS_DATA, bean)
        intent.putExtras(bundle)
        startActivity(intent)
        SlideAnimationUtill.slideNextAnimation(this)
        finish()
    }

    override fun addAnotherBusiness() {
        if (!(supportFragmentManager.findFragmentById(R.id.flContent) is BecomeVendorHomeFragment)) {
            iv_edit.visibility = View.GONE
            replaceFragment(BecomeVendorHomeFragment::class.java, getString(R.string.setup_your_bussiness))
        }
    }

    private val mActionHandler = Handler()
    private val CLOSE_DELAY_TIME = 500
    private var toolbar: Toolbar? = null
    private var mTitle: TextView? = null
    private var drawer: DrawerLayout? = null
    private val TAG: String = DashBoardActivity::class.java.name
    private var userSwitchFragment: UserVendorSwitchDialogFragment? = null
    var manager: FragmentManager? = null
    var numbers: Array<String>? = null
    var apiInterface: ApiInterface? = null
    var businesslist: ArrayList<BusinessDetail> = ArrayList();
    //    var userdata = UserDataLogin()
    var notificationData: NotificationClass? = null
    var itemArrayAdapter: ItemArrayAdapter? = null
    var back_pressed: Long = 0

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.ivUserPic -> {
                callEditActivity(ProfileEditActivity())
            }
            R.id.iv_back -> {
                handleHamburg()
            }
            R.id.iv_edit -> {
                var intent: Intent = Intent(this, NotificationActivity::class.java)
                startActivity(intent)
                SlideAnimationUtill.slideNextAnimation(this)
            }
            R.id.iv_switch -> {
                if (businesslist != null && businesslist.size > 0)
                    showVendorSelectionDialog(businesslist)
            }
            R.id.iv_search -> {
                var intent: Intent = Intent(this, SearchActivity::class.java)
                startActivity(intent)
                SlideAnimationUtill.slideNextAnimation(this)
            }
        }
    }


    fun callEditActivity(activity1: AppCompatActivity) {
        var intent: Intent = Intent(this, activity1.javaClass)

        var bundle = Bundle()
//        bundle.putSerializable(AppConstants.BUSINESS_DATA, keyValue)
        intent.putExtras(bundle)
        startActivityForResult(intent, 222)
        SlideAnimationUtill.slideNextAnimation(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard_customer)
        initView()
        setUpDrawer()
        initData()
        if (AppUtils.isInternetConnected(this)) {
            getBusinessList()
        } else {
            UiValidator.displayMsgSnack(drawer_layout, this, getString(R.string.no_internet_connection))
        }
        if (intent.extras != null && intent.extras.getSerializable(AppConstants.NOTIFICATION_DATA) != null) {
            notificationData = intent.extras.getSerializable(AppConstants.NOTIFICATION_DATA) as NotificationClass

            if (notificationData != null) {
                if (notificationData!!.user_type.equals("customer", true)) {
                    AppLog.e(TAG, notificationData.toString())
                    if (notificationData!!.type.equals("neworder")) {
                        var intent: Intent = Intent(this, CustomerOrderDetailActivity::class.java)
                        intent.putExtra("orderid", notificationData!!.object_id)
                        startActivity(intent)
                    } else {
                        var intent: Intent = Intent(this, NotificationActivity::class.java)
                        startActivity(intent)
                    }
                    SlideAnimationUtill.slideNextAnimation(this)
                }
            }
        }
    }


    private fun initData() {

        tvUserName.setText(SharedPreferenceManager.getCustomerName())
        if (SharedPreferenceManager.getCustomerImage() != null && !SharedPreferenceManager.getCustomerImage().equals("")) {
            AppLog.e("SharedPreferenceManager.getCustomerImage()", "" + SharedPreferenceManager.getCustomerImage())
            AppUtill.loadImageRoundProfile(this, SharedPreferenceManager.getCustomerImage(), ivUserPic)
        }

        val rvNumbers = findViewById(R.id.list) as ListView

        numbers = arrayOf(getString(R.string.home), getString(R.string.my_orders), getString(R.string.following), getString(R.string.become_vendor), getString(R.string.settings), getString(R.string.about))



        itemArrayAdapter = ItemArrayAdapter(this, R.layout.checklist_item, numbers!!)
        rvNumbers.adapter = itemArrayAdapter

        rvNumbers.setOnItemClickListener { parent, view, position, id ->
            drawer?.closeDrawer(GravityCompat.START)

            mActionHandler.postDelayed({
                when (position) {
                    0 -> {
                        if (!(supportFragmentManager.findFragmentById(R.id.flContent) is HomeFragment)) {
                            iv_edit.setImageResource(R.drawable.ic_notifications)
                            iv_edit.visibility = View.VISIBLE
                            iv_search.visibility = View.VISIBLE
                            replaceFragment(HomeFragment::class.java, getString(R.string.app_name))
                        }
                    }
                    1 -> {
                        if (!(supportFragmentManager.findFragmentById(R.id.flContent) is CustomerOrderFragment)) {
                            iv_edit.visibility = View.GONE
                            iv_search.visibility = View.GONE
                            replaceFragment(CustomerOrderFragment::class.java, getString(R.string.my_orders))
                        }
                    }

                    2 -> {
                        if (!(supportFragmentManager.findFragmentById(R.id.flContent) is CustomerFollowingFragment)) {
                            iv_edit.visibility = View.GONE
                            iv_search.visibility = View.GONE
                            replaceFragment(CustomerFollowingFragment::class.java, getString(R.string.following))
                        }
                    }
                    3 -> {
                        if (!(supportFragmentManager.findFragmentById(R.id.flContent) is BecomeVendorHomeFragment)) {
                            iv_edit.visibility = View.GONE
                            iv_search.visibility = View.GONE
                            replaceFragment(BecomeVendorHomeFragment::class.java, getString(R.string.setup_your_bussiness))
                        }
                    }

                    4 -> {
                        if (!(supportFragmentManager.findFragmentById(R.id.flContent) is SettingFragment)) {
                            iv_edit.visibility = View.GONE
                            iv_search.visibility = View.GONE
                            replaceFragment(SettingFragment::class.java, getString(R.string.settings))
                        }
                    }
                    5 -> {
                        if (!(supportFragmentManager.findFragmentById(R.id.flContent) is AboutFragment)) {
                            iv_edit.visibility = View.GONE
                            iv_search.visibility = View.GONE
                            replaceFragment(AboutFragment::class.java, getString(R.string.about))
                        }
                    }
                }
            }, CLOSE_DELAY_TIME.toLong())
        }
    }

//    override fun onResume() {
//        super.onResume()
//        AppLog.e("lifecycle","onResume")
//        var i = 4/0;
//
//    }
//
//    override fun onPause() {
//        super.onPause()
//        AppLog.e("lifecycle","onPause")
//    }
//
//    override fun onStop() {
//        super.onStop()
//        AppLog.e("lifecycle","onStop")
//
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        AppLog.e("lifecycle","onDestroy")
//    }

    private fun initView() {
        AppUtils.hideSoftKeyboard(this)
        apiInterface = ApiClient.client.create(ApiInterface::class.java)
        drawer = findViewById(R.id.drawer_layout) as DrawerLayout
        toolbar = findViewById(R.id.toolbar) as Toolbar
        mTitle = toolbar?.findViewById(R.id.tv_toolbarTitleText) as TextView
        mTitle?.text = getString(R.string.app_name)
        val iv_back = toolbar?.findViewById(R.id.iv_back) as ImageView
        val iv_edit = toolbar?.findViewById(R.id.iv_edit) as ImageView
        iv_back.setImageDrawable(resources.getDrawable(R.drawable.ic_hamburg_blue))
        iv_back.setOnClickListener(this)
        var tvUserName = findViewById(R.id.tvUserName) as TextView
        var ivUserPic = findViewById(R.id.ivUserPic) as ImageView
        ivUserPic.setOnClickListener(this)
        iv_edit.setOnClickListener(this)
        iv_switch.setOnClickListener(this)
        iv_search.setOnClickListener(this)

        left_drawer.setOnClickListener(this)

    }

    private fun setUpDrawer() {
        setDefaultSelectFragment()
    }

    private fun setDefaultSelectFragment() {
        iv_edit.setImageResource(R.drawable.ic_notifications)
        iv_edit.visibility = View.VISIBLE
        iv_search.visibility = View.VISIBLE
        replaceFragment(HomeFragment::class.java, getString(R.string.app_name))
    }

    override fun onBackPressed() {
        if (drawer?.isDrawerOpen(GravityCompat.START)!!) {
            drawer?.closeDrawer(GravityCompat.START)
        } else if (!(supportFragmentManager.findFragmentById(R.id.flContent) is HomeFragment)) {
            iv_edit.setImageResource(R.drawable.ic_notifications)
            iv_edit.visibility = View.VISIBLE
            iv_search.visibility = View.VISIBLE
            replaceFragment(HomeFragment::class.java, resources.getString(R.string.app_name))
        } else {
            if (back_pressed + 1000 > System.currentTimeMillis()) {
                super.onBackPressed();
            } else {
                Toast.makeText(getBaseContext(), "Press once again to exit!", Toast.LENGTH_SHORT).show();
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
        mActionHandler.postDelayed({ selectedDrawerItem(item) }, CLOSE_DELAY_TIME.toLong())
        return true
    }


    /**
     * @param menuItem
     */
    private fun selectedDrawerItem(menuItem: MenuItem?) {
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        AppLog.e(TAG, "actiii");
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
        fragmentManager.beginTransaction().setCustomAnimations(R.anim.slide_from_right, R.anim.slide_to_left1).replace(R.id.flContent, fragment).commit()
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


    class ItemArrayAdapter(context: Context, private val listItemLayout: Int, internal var itemList: Array<String>) : ArrayAdapter<String>(context, listItemLayout, itemList) {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            var convertView = convertView
            val item = getItem(position)

            val viewHolder: ViewHolder
            if (convertView == null) {
                viewHolder = ViewHolder()
                val inflater = LayoutInflater.from(context)
                convertView = inflater.inflate(listItemLayout, parent, false)
                viewHolder.item = convertView!!.findViewById<View>(R.id.tv_text) as TextView
                viewHolder.ll_main = convertView.findViewById<View>(R.id.ll_main) as LinearLayout
                convertView.tag = viewHolder
            } else {
                viewHolder = convertView.tag as ViewHolder
            }

            viewHolder.item!!.text = item

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

    private fun showVendorSelectionDialog(businesslist: ArrayList<BusinessDetail>) {
        userSwitchFragment = UserVendorSwitchDialogFragment.newInstance(businesslist)
        manager = supportFragmentManager
        userSwitchFragment!!.show(manager, "My Dialog")
    }


    fun getBusinessList() {
        if (AppUtils.isInternetConnected(this)) {
            ProgressDialogLoader.progressDialogCreation(this@DashBoardActivity, getString(R.string.please_wait))
            apiInterface!!.getBusinessList("Bearer " + SharedPreferenceManager.getAuthToken(), SharedPreferenceManager.getFCMToken(), AppConstants.DEVICE_TYPE_ANDROID).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Observer<BusinessListResponse> {
                        override fun onSubscribe(d: Disposable) {
                            AppLog.e(TAG, "onSubscribe")
                        }

                        override fun onNext(regionResponse: BusinessListResponse) {
                            ProgressDialogLoader.progressDialogDismiss()
                            AppLog.e(TAG, regionResponse.toString())
                            if (regionResponse.status.equals(AppConstants.KEY_SUCCESS)) {
//                            CachingManager.setUserDetail(loginResponse.success)
                                businesslist = regionResponse.data
//                                initData()
                                manageList()
                            } else {
                                UiValidator.displayMsgSnack(drawer_layout, this@DashBoardActivity, regionResponse.message)
                            }
                        }

                        override fun onError(e: Throwable) {
                            ProgressDialogLoader.progressDialogDismiss()
                            if (e != null) {
                                UiValidator.displayMsgSnack(drawer_layout, this@DashBoardActivity, e.message)
                                AppLog.e(TAG, e.message)
                            }
                        }

                        override fun onComplete() {
                            AppLog.e(TAG, "onComplete: ")
                        }
                    })
        } else {
            UiValidator.displayMsgSnack(drawer_layout, this, getString(R.string.no_internet_connection))
        }
    }

    fun manageList() {
        if (businesslist != null && businesslist.size > 0) {
            iv_switch.visibility = View.VISIBLE
            if (notificationData != null && notificationData?.user_type.equals("vendor")) {
                for (item in businesslist) {

                    if (notificationData?.businesId!!.equals("" + item.business_id)) {
                        var intent: Intent = Intent(this, com.virtual.customervendor.vendor.ui.activity.DashBoardActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        var bundle = Bundle()
                        bundle.putSerializable(AppConstants.BUSINESS_DATA, item)
                        bundle.putSerializable(AppConstants.NOTIFICATION_DATA, notificationData)
                        intent.putExtras(bundle)
                        startActivity(intent)
                        finish()

                    }
                }
            }
        } else {
            iv_switch.visibility = View.GONE
        }

        if (businesslist != null && businesslist.size > 0) {
            numbers = arrayOf(getString(R.string.home), getString(R.string.my_orders), getString(R.string.following), "", getString(R.string.settings), "About")
        }
        itemArrayAdapter?.notifyDataSetChanged()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        AppLog.e(TAG, "checkcall")
        if (requestCode == 222 && resultCode == Activity.RESULT_OK) {
            if (SharedPreferenceManager.getCustomerImage() != null && !SharedPreferenceManager.getCustomerImage().equals("")) {
                AppLog.e("SharedPreferenceManager.getCustomerImage()", "" + SharedPreferenceManager.getCustomerImage())
                AppUtill.loadImageRound(this, SharedPreferenceManager.getCustomerImage(), ivUserPic)
            }
            tvUserName.setText(SharedPreferenceManager.getCustomerName())
        }
    }

    fun updateProfile(){
        AppUtill.loadImageRound(this, SharedPreferenceManager.getCustomerImage(), ivUserPic)
        tvUserName.setText(SharedPreferenceManager.getCustomerName())
    }
}
