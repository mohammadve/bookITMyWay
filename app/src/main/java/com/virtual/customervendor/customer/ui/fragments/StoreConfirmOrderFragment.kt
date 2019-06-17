package com.virtual.customervendor.customer.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.virtual.customer_vendor.utill.AppUtill
import com.virtual.customervendor.R
import com.virtual.customervendor.commonActivity.PaymentActivity
import com.virtual.customervendor.customer.ui.activity.PurchaseItemsActivity
import com.virtual.customervendor.customer.ui.adapter.StoreItemsConfirmOrderAdapter
import com.virtual.customervendor.managers.SharedPreferenceManager
import com.virtual.customervendor.model.*
import com.virtual.customervendor.model.response.CustomerBookingResponse
import com.virtual.customervendor.networks.ApiClient
import com.virtual.customervendor.networks.ApiInterface
import com.virtual.customervendor.utills.*
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_store_confirm_order.*


class StoreConfirmOrderFragment : Fragment(), View.OnClickListener {
    //Changes By Himanshu Starts

    private val ACTIVITY_REQUEST_CODE: Int = 1115
    var selectedId: Int = 0

    //Changes By Himanshu ends
    var TAG: String = StoreConfirmOrderFragment::class.java.name;
    var apiService: ApiInterface? = null
    var apirequest: MutableMap<String, String> = mutableMapOf()
    var info: CustomerBookingListModel = CustomerBookingListModel()
    var adpater: StoreItemsConfirmOrderAdapter? = null
    var fieldmap: MutableMap<String, String> = mutableMapOf()
    var addedItemsListNew: ArrayList<ItemPriceStoreModel> = ArrayList<ItemPriceStoreModel>()
    var addedItemsListModel: ArrayList<ItemPriceStoreModel> = ArrayList<ItemPriceStoreModel>()
    var applyOfferModel = ApplyOfferModel()
    var serviceDetail = VendorServiceDetailModel()
    var taxAmt = String()
    var totalAmt = String()

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btn_next -> {
                //Changes By Himanshu Starts

                selectedId = radioGroup_payment_option_purchase.checkedRadioButtonId
                if (selectedId == R.id.rb_pay_now_purchase || selectedId == R.id.rb_pay_later_purchase) {
                    if (selectedId == R.id.rb_pay_now_purchase) {
                        var intent: Intent = Intent(context, PaymentActivity::class.java)
                        (activity as AppCompatActivity).startActivityForResult(intent, ACTIVITY_REQUEST_CODE)
                    } else {
                        apirequest.clear()
                        apirequest.put(AppKeys.PAYMENT_TYPE, "PAYLATER")
                        apirequest.put(AppKeys.OFFER_ID, applyOfferModel.offer_id)
                        apirequest.put(AppKeys.OFFER_PRICE, applyOfferModel.offer_price)

                        if (applyOfferModel.order_price != null && !applyOfferModel.order_price.equals("") && !applyOfferModel.order_price.equals("null", true)) {
                            apirequest.put(AppKeys.ORDER_PRICE, "" + applyOfferModel.order_price)
                        } else {
                            apirequest.put(AppKeys.ORDER_PRICE, "" + fieldmap.get(AppKeys.KEY_TOTAL_PRICE))
                        }

//                        apirequest.put(AppKeys.TOTAL_PRICE, totalAmt)
//                        apirequest.put(AppKeys.TAX, serviceDetail.businessData.business_tax!!)

                        hitApi()
                    }
                } else {
                    UiValidator.displayMsgSnackShort(cons, activity, resources.getString(R.string.please_select_payment_option))
                }
                //Changes By Himanshu ends

            }

        }
    }


    fun hitAPIForConfirm(card_token: String) {
        apirequest.clear()
        apirequest.put(AppKeys.PAYMENT_TYPE, "ONLINE")

        apirequest.put(AppKeys.CARD_TOKEN, "" + card_token)

        apirequest.put(AppKeys.OFFER_ID, "")
        apirequest.put(AppKeys.OFFER_PRICE, "")

        apirequest.put(AppKeys.ORDER_PRICE, "" + fieldmap.get(AppKeys.KEY_TOTAL_PRICE))


        hitApi()


    }

    companion object {
        fun newInstance(): StoreConfirmOrderFragment {
            val fragment = StoreConfirmOrderFragment()
            return fragment
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var mView = inflater.inflate(R.layout.fragment_store_confirm_order, container, false)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_next.setOnClickListener(this)

        apiService = ApiClient.client.create(ApiInterface::class.java)
        info = (activity as PurchaseItemsActivity).getcustomerBookingListModel()
        fieldmap = (activity as PurchaseItemsActivity).getFieldMap()
        addedItemsListNew = (activity as PurchaseItemsActivity).getAddedCartItems()
        applyOfferModel = (activity as PurchaseItemsActivity).applyOfferModel
        serviceDetail = (activity as PurchaseItemsActivity).businessDetailModel

        setData()
    }

    fun setData() {
        txtBusiness.setText(info.business_name)
        txtregion.setText(info.contact_number)
        txtcity.setText(info.cityname)
        if (info.business_image != null && !info.business_image.equals(""))
            Glide.with(activity!!).load(info.business_image).into(imgBusiness)

        adpater = StoreItemsConfirmOrderAdapter(activity!!, addedItemsListNew)
        val manager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        rv_items.layoutManager = manager
        rv_items.adapter = (adpater)

        rv_items.isNestedScrollingEnabled = false



        if (applyOfferModel != null && !applyOfferModel.order_price.isEmpty()) {
            ed_offerprice.setText(AppUtils.getRateWithSymbol(applyOfferModel.offer_price))
            ed_orderprice.text = AppUtils.getRateWithSymbol(applyOfferModel.order_price)

            totalAmt = AppUtill.calculateTotalAmt(serviceDetail.businessData.business_tax!!, applyOfferModel.offer_price)
            taxAmt = AppUtill.calculateTaxAmt(serviceDetail.businessData.business_tax!!, applyOfferModel.offer_price)
        } else {
            ed_offerprice.setText(AppUtils.getRateWithSymbol(fieldmap.get(AppKeys.KEY_TOTAL_PRICE)))
            ed_orderprice.text = AppUtils.getRateWithSymbol(fieldmap.get(AppKeys.KEY_TOTAL_PRICE))


            totalAmt = AppUtill.calculateTotalAmt(serviceDetail.businessData.business_tax!!, fieldmap.get(AppKeys.KEY_TOTAL_PRICE)!!)
            taxAmt = AppUtill.calculateTaxAmt(serviceDetail.businessData.business_tax!!, fieldmap.get(AppKeys.KEY_TOTAL_PRICE)!!)
        }

        ed_tax.setText(AppUtils.getRateWithSymbol(taxAmt))
        ed_toatlprice.setText(AppUtils.getRateWithSymbol(totalAmt))

        if (serviceDetail.store_category_id.equals(AppConstants.STORE_CAT_SEAT_SERVICE)) {
            tv_address1.text = fieldmap.get(AppKeys.KEY_SERVICEAREA)
            tv_address2.text = fieldmap.get(AppKeys.KEY_SECTION)
            tv_city.text = fieldmap.get(AppKeys.KEY_ROW)
            tv_region.text = fieldmap.get(AppKeys.KEY_SEAT)

            tv_zip.visibility = View.GONE
            tv_phone_number.visibility = View.GONE
        } else {
            tv_address1.text = fieldmap.get(AppKeys.KEY_ADDRESS1)
            tv_address2.text = fieldmap.get(AppKeys.KEY_ADDRESS2)
            tv_city.text = fieldmap.get(AppKeys.KEY_CITY)
            tv_region.text = fieldmap.get(AppKeys.KEY_REGION)
            tv_zip.text = fieldmap.get(AppKeys.KEY_ZIP_CODE)
            tv_phone_number.text = fieldmap.get(AppKeys.KEY_PHONE_NUMBER)
        }
    }

    fun hitApi() {
        addedItemsListModel.clear()
        for (item in addedItemsListNew) {
            val storeCartModel: ItemPriceStoreModel = ItemPriceStoreModel()
            storeCartModel.item_id = item.item_id
            storeCartModel.item_name = item.item_name
            storeCartModel.item_price = "" + (item.item_price!!.toInt() * item.quantity!!.toInt())
            storeCartModel.quantity = item.quantity
            if (!item.add_on_one.isEmpty())
                storeCartModel.add__one.add(item.add_on_one)
            if (!item.add_on_two.isEmpty())
                storeCartModel.add__one.add(item.add_on_two)

            addedItemsListModel.add(storeCartModel)
        }

        var model: StorePlaceOrderModel = StorePlaceOrderModel()
        model.business_id = info.business_id.toString()
        model.category_id = AppConstants.CAT_STORE_SELLER.toString()
        model.service_id = info.service_id.toString()
        model.subcategory_id = AppConstants.SUBCAT_STORE_SELLER.toString()
        model.price = fieldmap.get(AppKeys.KEY_TOTAL_PRICE)!!
        model.order_items = addedItemsListModel

        model.payment_type = apirequest.get(AppKeys.PAYMENT_TYPE).toString()
        model.offer_price = apirequest.get(AppKeys.OFFER_PRICE).toString()
        model.order_price = apirequest.get(AppKeys.ORDER_PRICE).toString()
        model.offer_id = apirequest.get(AppKeys.OFFER_ID).toString()
        model.card_token = apirequest.get(AppKeys.CARD_TOKEN).toString()

        model.offer_id = apirequest.get(AppKeys.OFFER_ID).toString()
        model.card_token = apirequest.get(AppKeys.CARD_TOKEN).toString()
        model.store_category_id = serviceDetail.store_category_id!!

        if (serviceDetail.store_category_id.equals(AppConstants.STORE_CAT_SEAT_SERVICE)) {
            model.sevice_area = fieldmap.get(AppKeys.KEY_SERVICEAREA) as String
            model.section = fieldmap.get(AppKeys.KEY_SECTION) as String
            model.row = fieldmap.get(AppKeys.KEY_ROW) as String
            model.seat_number = fieldmap.get(AppKeys.KEY_SEAT) as String
        } else {
            model.delivery_address1 = fieldmap.get(AppKeys.KEY_ADDRESS1) as String
            model.delivery_address2 = fieldmap.get(AppKeys.KEY_ADDRESS2) as String
            model.delivery_city = fieldmap.get(AppKeys.KEY_CITY) as String
            model.delivery_region = fieldmap.get(AppKeys.KEY_REGION) as String
            model.delivery_zipcode = fieldmap.get(AppKeys.KEY_ZIP_CODE) as String
            model.delivery_mobile_no = fieldmap.get(AppKeys.KEY_PHONE_NUMBER) as String
        }





        model.tax = serviceDetail.businessData.business_tax!!
        model.total_price = totalAmt


//        val gson = Gson()
//        val json = gson.toJson(addedItemsListModel)

//        apirequest.put(AppKeys.SERVICE_ID, info.service_id.toString())
//        apirequest.put(AppKeys.BUSINESS_ID, info.business_id.toString())
//        apirequest.put(AppKeys.CATEGORY_ID, AppConstants.CAT_STORE_SELLER.toString())
//        apirequest.put(AppKeys.SUBCATEGORY_ID, AppConstants.SUBCAT_STORE_SELLER.toString())
//        apirequest.put(AppKeys.KEY_TOTAL_PRICE, fieldmap.get(AppKeys.KEY_TOTAL_PRICE)!!)
//        apirequest.put(AppKeys.KEY_ORDER_ITEMS, json)

        if (AppUtils.isInternetConnected(activity)) {
            ProgressDialogLoader.progressDialogCreation(getActivity(), getString(R.string.please_wait))
            AppLog.e(TAG, model.toString())
            apiService!!.initiateOrder("Bearer " + SharedPreferenceManager.getAuthToken(), model)!!.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Observer<CustomerBookingResponse> {
                        override fun onSubscribe(d: Disposable) {
                            AppLog.e(TAG, "onSubscribe")
                        }

                        override fun onNext(apiResponseRestaurant: CustomerBookingResponse) {
                            AppLog.e(TAG, apiResponseRestaurant.toString())
                            ProgressDialogLoader.progressDialogDismiss()
                            if (apiResponseRestaurant.status.equals(AppConstants.KEY_SUCCESS)) {
                                (activity as PurchaseItemsActivity).fieldmap.put(AppKeys.KEY_TRANSACTION_ID, apiResponseRestaurant.data.order_id.toString())
                                (activity as PurchaseItemsActivity).setDisplayFragment(9, resources.getString(R.string.order_placed), false)
                            } else {
                                UiValidator.displayMsgSnack(cons, activity, apiResponseRestaurant.message)
                            }
                        }

                        override fun onError(e: Throwable) {
                            if (e != null) {
                                AppLog.e(TAG, e.message)
                                UiValidator.displayMsgSnack(cons, activity, e.message)
                            }
                            ProgressDialogLoader.progressDialogDismiss()
                        }

                        override fun onComplete() {
                            AppLog.e(TAG, "onComplete: ")
                        }
                    })
        } else {
            UiValidator.displayMsgSnack(cons, activity, getString(R.string.no_internet_connection))
        }


    }
}