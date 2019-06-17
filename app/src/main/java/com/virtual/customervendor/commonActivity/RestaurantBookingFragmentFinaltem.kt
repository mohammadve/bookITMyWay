//package com.virtual.customervendor.customer.ui.fragments
//
//import android.content.Intent
//import android.os.Bundle
//import android.support.v4.app.Fragment
//import android.support.v7.app.AppCompatActivity
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.Toast
//import com.bumptech.glide.Glide
//import com.virtual.customervendor.R
//import com.virtual.customervendor.commonActivity.PaymentActivity
//import com.virtual.customervendor.customer.ui.activity.TableBookingActivity
//import com.virtual.customervendor.managers.SharedPreferenceManager
//import com.virtual.customervendor.model.CustomerBookingListModel
//import com.virtual.customervendor.model.response.CustomerBookingResponse
//import com.virtual.customervendor.networks.ApiClient
//import com.virtual.customervendor.networks.ApiInterface
//import com.virtual.customervendor.utill.*
//import io.reactivex.Observer
//import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
//import io.reactivex.disposables.Disposable
//import io.reactivex.schedulers.Schedulers
//import kotlinx.android.synthetic.main.fragment_booking_final_restaurant.*
//
//class RestaurantBookingFragmentFinaltem : Fragment(), View.OnClickListener {
//    //Changes By Himanshu Starts
//    private val ACTIVITY_REQUEST_CODE: Int = 1111
//    var selectedId: Int = 0
//
//    //Changes By Himanshu ends
//    override fun onClick(v: View?) {
//
//        when (v!!.id) {
//            R.id.btn_next -> {
////Changes By Himanshu Starts
//                selectedId = radioGroup_payment_option.checkedRadioButtonId
//                if (selectedId == R.id.rb_pay_now || selectedId == R.id.rb_pay_later) {
//                    hitApi()
//                } else {
//                    Toast.makeText(activity, "Please select payment option", Toast.LENGTH_SHORT).show()
//                }
//                //Changes By Himanshu end
//
//            }
//        }
//
//
////        if (activity is TableBookingActivity)
////            (activity as TableBookingActivity).setDisplayFragment(5, resources.getString(R.string.booking_confirmed), false)
//    }
//
//    var TAG: String = RestaurantBookingFragmentFinaltem::class.java.name
//    var request: MutableMap<String, String> = mutableMapOf()
//    var parkingInfo: CustomerBookingListModel = CustomerBookingListModel()
//    var datetime: StringBuilder? = null
//    var apiService: ApiInterface? = null
//
//
//    companion object {
//        fun newInstance(): RestaurantBookingFragmentFinaltem {
//            val fragment = RestaurantBookingFragmentFinaltem()
//            return fragment
//        }
//    }
//
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//      var  mView = inflater.inflate(R.layout.fragment_booking_final_restaurant, container, false)
//        return mView
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        btn_next.setOnClickListener(this)
//        apiService = ApiClient.getClient().create(ApiInterface::class.java)
//        request = (activity as TableBookingActivity).getFieldMap()
//        parkingInfo = (activity as TableBookingActivity).customerBookingListModel
//        setData()
//    }
//
//    fun setData() {
//        txtBusiness.setText(parkingInfo.business_name)
//        txtDesc.setText(parkingInfo.description)
//        txtCity.setText(parkingInfo.cityname)
//        ed_jou.setText(request.get(AppKeys.KEY_EXPECTEDGUEST))
//        ed_date.setText(request.get(AppKeys.KEY_BOOKDATE) + " | " + request.get(AppKeys.KEY_BOOKTIME))
//
//        if (parkingInfo.business_image != null && !parkingInfo.business_image.equals(""))
//            Glide.with(activity!!).load(parkingInfo.business_image).into(imgBusiness)
//    }
//
//    fun hitApi() {
//        request.put(AppKeys.SERVICE_ID, parkingInfo.service_id.toString())
//        request.put(AppKeys.BUSINESS_ID, parkingInfo.business_id.toString())
//        request.put(AppKeys.CATEGORY_ID, AppConstants.CAT_RESTAURANT_DINNIG.toString())
//        request.put(AppKeys.SUBCATEGORY_ID, AppConstants.SUBCAT_RESTAURANT_DINNIG.toString())
//
//        ProgressDialogLoader.progressDialogCreation(getActivity(), getString(R.string.please_wait))
//        apiService!!.initiateOrder("Bearer " + SharedPreferenceManager.getAuthToken(), request)!!.subscribeOn(Schedulers.io())
//                .observeOn(mainThread())
//                .subscribe(object : Observer<CustomerBookingResponse> {
//                    override fun onSubscribe(d: Disposable) {
//                        AppLog.e(TAG, "onSubscribe")
//                    }
//
//                    override fun onNext(apiResponseRestaurant: CustomerBookingResponse) {
//                        ProgressDialogLoader.progressDialogDismiss()
//                        AppLog.e(TAG, apiResponseRestaurant.toString())
//                        if (apiResponseRestaurant.status.equals(AppConstants.KEY_SUCCESS)) {
////Changes By Himanshu Starts
//                            if (selectedId == R.id.rb_pay_now) {
//                                (activity as TableBookingActivity).orderNo = apiResponseRestaurant.data.order_id!!
//                                var intent: Intent = Intent(context, PaymentActivity::class.java)
//                                (activity as AppCompatActivity).startActivityForResult(intent, ACTIVITY_REQUEST_CODE)
//
//                            } else if (selectedId == R.id.rb_pay_later) {
//                                (activity as TableBookingActivity).orderNo = apiResponseRestaurant.data.order_id!!
//                                (activity as TableBookingActivity).setDisplayFragment(5, resources.getString(R.string.booking_confirmed), false)
//                            }
////Changes By Himanshu end
//                        } else {
//                            UiValidator.displayMsg(activity, apiResponseRestaurant.message)
//                        }
//                    }
//
//                    override fun onError(e: Throwable) {
//                        ProgressDialogLoader.progressDialogDismiss()
//                        AppLog.e(TAG, e.message)
//                        (activity as TableBookingActivity).setDisplayFragment(5, resources.getString(R.string.booking_confirmed), false)
//                    }
//
//                    override fun onComplete() {
//                        AppLog.e(TAG, "onComplete: ")
//                    }
//                })
//
//
//    }
//
//}