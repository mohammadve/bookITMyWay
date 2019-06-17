package com.virtual.customervendor.customer.ui.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.Filterable
import com.virtual.customer_vendor.utill.AppUtill
import com.virtual.customervendor.R
import com.virtual.customervendor.customer.ui.activity.BookRideLimoActivity
import com.virtual.customervendor.customer.ui.activity.BookRideTaxiActivity
import com.virtual.customervendor.customer.ui.activity.BookRideTourBusActivity
import com.virtual.customervendor.customer.ui.activity.OfferListingActivity
import com.virtual.customervendor.managers.SharedPreferenceManager
import com.virtual.customervendor.model.CustomerBookingListModel
import com.virtual.customervendor.model.VenderOffersModel
import com.virtual.customervendor.model.VendorServiceDetailModel
import com.virtual.customervendor.model.response.ApplyOfferResponse
import com.virtual.customervendor.networks.ApiClient
import com.virtual.customervendor.networks.ApiInterface
import com.virtual.customervendor.utills.*
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_booking_infromation.*
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.*

class BookingInfromationFragment : Fragment(), View.OnClickListener, DatePickerDialog.OnDateSetListener/*, TimePickerDialog.OnTimeSetListener*/ {


    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btn_next -> {
                fromWhere = AppConstants.CAL_NEXT
                validateField()
            }
            R.id.offer -> {
                fromWhere = AppConstants.CAL_OFFER
                validateField()
            }

            R.id.viewoffer -> {
                var intent: Intent = Intent(activity, OfferListingActivity::class.java)
                intent.putExtra(AppKeys.BUSINESS_ID, serviceDetail.businessData.business_id.toString())
                startActivityForResult(intent, requestCode)
                SlideAnimationUtill.slideNextAnimation(activity as AppCompatActivity)
            }
            R.id.ed_date -> {
                var selectedDays = ArrayList<Int>()
                for (i in 1..7) {
                    if (isValidDate(i)) {
                        selectedDays.add(i)
                    }
                }
                val daysArray: Array<Calendar> = AppUtils.getEnabledDays(selectedDays)
                AppUtils.getDate(activity?.fragmentManager, this, daysArray);

            }
            R.id.ed_time -> {
//                AppUtill.getTime(ed_time, activity!!)
                AppUtils.getTimeNew(ed_time, activity as AppCompatActivity?)
            }
            else -> {
                if (activity is BookRideTaxiActivity)
                    (activity as BookRideTaxiActivity).setDisplayFragment(4, resources.getString(R.string.confirm_booking), false)
                else if (activity is BookRideLimoActivity)
                    (activity as BookRideLimoActivity).setDisplayFragment(4, resources.getString(R.string.confirm_booking), false)
                else if (activity is BookRideTourBusActivity)
                    (activity as BookRideTourBusActivity).setDisplayFragment(4, resources.getString(R.string.confirm_booking), false)
            }
        }

    }

    override fun onDateSet(view: DatePickerDialog?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        val c = Calendar.getInstance()
        c.set(Calendar.YEAR, year)
        c.set(Calendar.MONTH, monthOfYear)
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        val dat1e = SimpleDateFormat("yyyy-MM-dd").format(c.time)
        ed_date.setText(dat1e)

    }

    var TAG: String = BookingInfromationFragment::class.java.name
    var request: MutableMap<String, String> = mutableMapOf()
    var datetime: StringBuilder? = null
    var apiService: ApiInterface? = null
    var placeAdapter: PlacesAutoCompleteAdapter? = null
    val PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place"
    val TYPE_AUTOCOMPLETE = "/autocomplete"
    val OUT_JSON = "/json"
    val LOG_TAG = "MyApp"
    var customerBookingListModel = CustomerBookingListModel()
    var strToLat: String = ""
    var strToLong: String = ""
    var strFromLat: String = ""
    var strFromLong: String = ""
    var strFromTo = ""
    var serviceDetail = VendorServiceDetailModel()
    val requestCode: Int = 101
    var fromWhere = String()


    companion object {
        fun newInstance(): BookingInfromationFragment {
            val fragment = BookingInfromationFragment()
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var mView = inflater.inflate(R.layout.fragment_booking_infromation, container, false)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_next.setOnClickListener(this)
        ed_date.setOnClickListener(this)
        ed_time.setOnClickListener(this)
        viewoffer.setOnClickListener(this)
        offer.setOnClickListener(this)
        apiService = ApiClient.client.create(ApiInterface::class.java)


        ed_time.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (!ed_time.text.toString().isEmpty())
                    if (AppUtill.isTimeGreater(ed_date.text.toString() + " " + ed_time.text.toString())) {
                        UiValidator.displayMsgSnack(cons, activity, getString(R.string.choose_valid_time_slot))
                        ed_time.setText("")
                    }
            }
        })

        if (activity is BookRideTaxiActivity) {
            request = (activity as BookRideTaxiActivity).getFieldMap()
            customerBookingListModel = (activity as BookRideTaxiActivity).getcustomerBookingListModel()
            serviceDetail = (activity as BookRideTaxiActivity).serviceDetailModel
        } else if (activity is BookRideLimoActivity) {
            request = (activity as BookRideLimoActivity).getFieldMap()
            customerBookingListModel = (activity as BookRideLimoActivity).getcustomerBookingListModel()
            serviceDetail = (activity as BookRideLimoActivity).serviceDetailModel
        } else if (activity is BookRideTourBusActivity) {
            request = (activity as BookRideTourBusActivity).getFieldMap()
            customerBookingListModel = (activity as BookRideTourBusActivity).getcustomerBookingListModel()
            serviceDetail = (activity as BookRideTourBusActivity).serviceDetailModel
        }
        ed_bcontact.setText(AppUtils.getRateWithSymbol(serviceDetail.rate_per_km) + " " + resources.getString(R.string.per_km))

        placeAdapter = PlacesAutoCompleteAdapter(activity!!.applicationContext, R.layout.textview_item)
        autoCompleteTextView_from.setAdapter(placeAdapter)
        autoCompleteTextView_from.threshold = 1

        autoCompleteTextView_to.setAdapter(placeAdapter)
        autoCompleteTextView_to.threshold = 1


        autoCompleteTextView_from.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val selectedItem = parent.getItemAtPosition(position).toString()
            strFromLat = ""
            strFromLong = ""
            // Display the clicked item using toast
        }

        autoCompleteTextView_to.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val selectedItem = parent.getItemAtPosition(position).toString()
            // Display the clicked item using toast
            strToLat = ""
            strToLong = ""
        }


    }


    fun bookRideApi(business_id: String, service_id: String) {
        request.put(AppKeys.SERVICE_ID, service_id)
        request.put(AppKeys.BUSINESS_ID, business_id)


        request.put(AppKeys.KEY_FROM_LOCATION, autoCompleteTextView_from.text.toString())
        request.put(AppKeys.KEY_TO_LOCATION, autoCompleteTextView_to.text.toString())

        request.put(AppKeys.KEY_FROM_LAT, strFromLat)
        request.put(AppKeys.KEY_TO_LAT, strToLat)

        request.put(AppKeys.KEY_FROM_LONG, strFromLong)
        request.put(AppKeys.KEY_TO_LONG, strToLong)

        request.put(AppKeys.KEY_FROMDATE, ed_date.text.toString())
        request.put(AppKeys.KEY_FROMTIME, ed_time.text.toString())

        request.put(AppKeys.KEY_SPACIAL_INSTRUCTION, ed_instruct.text.toString())

        var totalDistance = AppUtill.getKmFromLatLong(strFromLat.toDouble(), strFromLong.toDouble(), strToLat.toDouble(), strToLong.toDouble())
        AppLog.e(TAG + "test", totalDistance.toString())
//        AppUtill.getKmFromLatLong(strFromLat.toDouble(), strFromLong.toDouble(), strToLat.toDouble(), strToLong.toDouble())


        if (activity is BookRideTaxiActivity) {

            request.put(AppKeys.CATEGORY_ID, AppConstants.CAT_TRANSPORTATION.toString())
            request.put(AppKeys.SUBCATEGORY_ID, AppConstants.SUBCAT_TRANS_TAXI.toString())

            (activity as BookRideTaxiActivity).totalDistance = totalDistance
            if (fromWhere.equals(AppConstants.CAL_NEXT))
                (activity as BookRideTaxiActivity).setDisplayFragment(4, resources.getString(R.string.booking_information), false)
            else if (fromWhere.equals(AppConstants.CAL_OFFER)) {
                getOfferPrice(totalDistance)
            }
        } else if (activity is BookRideLimoActivity) {

            request.put(AppKeys.CATEGORY_ID, AppConstants.CAT_TRANSPORTATION.toString())
            request.put(AppKeys.SUBCATEGORY_ID, AppConstants.SUBCAT_TRANS_LIMO.toString())

            (activity as BookRideLimoActivity).totalDistance = totalDistance
            if (fromWhere.equals(AppConstants.CAL_NEXT))
                (activity as BookRideLimoActivity).setDisplayFragment(4, resources.getString(R.string.booking_information), false)
            else if (fromWhere.equals(AppConstants.CAL_OFFER)) {
                getOfferPrice(totalDistance)
            }
        } else if (activity is BookRideTourBusActivity) {

            request.put(AppKeys.CATEGORY_ID, AppConstants.CAT_TRANSPORTATION.toString())
            request.put(AppKeys.SUBCATEGORY_ID, AppConstants.SUBCAT_TRANS_TOURBUS.toString())

            (activity as BookRideTourBusActivity).totalDistance = totalDistance
            if (fromWhere.equals(AppConstants.CAL_NEXT))
                (activity as BookRideTourBusActivity).setDisplayFragment(4, resources.getString(R.string.booking_information), false)
            else if (fromWhere.equals(AppConstants.CAL_OFFER)) {
                getOfferPrice(totalDistance)
            }
        }

    }

    fun validateField() {

        UiValidator.hideSoftKeyboard(activity)
        if (autoCompleteTextView_from.getText().toString().isEmpty()) {
            UiValidator.setValidationError(autoCompleteTextView_from, getString(R.string.field_required))
            return
        }

        if (autoCompleteTextView_to.getText().toString().isEmpty()) {
            UiValidator.setValidationError(autoCompleteTextView_to, getString(R.string.field_required))
            return
        }

        if (autoCompleteTextView_to.getText().toString().equals(autoCompleteTextView_from.getText().toString())) {
            UiValidator.setValidationError(autoCompleteTextView_to, getString(R.string.location_valition))
            return
        }

        if (ed_date.getText().toString().isEmpty()) {
            UiValidator.setValidationError(til_date, getString(R.string.field_required))
            return
        }
        if (til_date.isErrorEnabled()) {
            UiValidator.disableValidationError(til_date)
        }

        if (ed_time.getText().toString().isEmpty()) {
            UiValidator.setValidationError(til_time, getString(R.string.field_required))
            return
        }
        if (til_time.isErrorEnabled()) {
            UiValidator.disableValidationError(til_time)
        }
        if (ed_instruct.getText().toString().isEmpty()) {
            UiValidator.setValidationError(til_instruct, getString(R.string.field_required))
            return
        }
        if (til_instruct.isErrorEnabled()) {
            UiValidator.disableValidationError(til_instruct)
        }

        if (fromWhere.equals(AppConstants.CAL_OFFER)) {
//            if (strFromLat != null && !strFromLat.equals("") && strFromLong != null && !strFromLong.equals("") && strToLat != null && !strToLat.equals("") && strToLong != null && !strToLong.equals("")) {
            if (!ed_coupon.text.toString().isEmpty()) {
                strFromTo = "from"
                GetLatLong().execute(autoCompleteTextView_from.getText().toString())
                ProgressDialogLoader.progressDialogCreation(activity, getString(R.string.please_wait))
            } else {
                UiValidator.displayMsgSnack(nest, activity, resources.getString(R.string.nocoupon_code))
            }
        } else if (fromWhere.equals(AppConstants.CAL_NEXT)) {
            if (strFromLat != null && !strFromLat.equals("") && strFromLong != null && !strFromLong.equals("") && strToLat != null && !strToLat.equals("") && strToLong != null && !strToLong.equals("")) {
                bookRideApi(customerBookingListModel.business_id!!, customerBookingListModel.service_id!!)
            } else {
                strFromTo = "from"
                GetLatLong().execute(autoCompleteTextView_from.getText().toString())
                ProgressDialogLoader.progressDialogCreation(activity, getString(R.string.please_wait))

            }
        }
    }

    inner class PlacesAutoCompleteAdapter(context: Context, textViewResourceId: Int) : ArrayAdapter<String>(context, textViewResourceId), Filterable {
        private var resultList = ArrayList<String>()

        override fun getCount(): Int {
            return resultList.size
        }

        override fun getItem(index: Int): String? {

            return if (index < resultList.size) {

                resultList[index]
            } else {
                ""
            }

        }

        override fun getFilter(): Filter {
            return object : Filter() {
                override fun performFiltering(constraint: CharSequence): FilterResults {
                    val filterResults = FilterResults()
                    val places: ArrayList<String>?
                    if (constraint != null) {
                        // Retrieve the autocomplete results.
                        places = autocomplete(constraint.toString())

                        // Assign the data to the FilterResults
                        filterResults.values = places
                        filterResults.count = places!!.size
                    }
                    return filterResults
                }

                override fun publishResults(constraint: CharSequence?,
                                            results: FilterResults?) {
                    if (results?.count!! > 0) {
                        resultList = results?.values as ArrayList<String>
                        notifyDataSetChanged()
                    } else {
                        notifyDataSetInvalidated()
                    }
                }
            }
        }
    }

    private fun autocomplete(input: String): ArrayList<String>? {
        var resultList: ArrayList<String>? = null

        var conn: HttpURLConnection? = null
        val jsonResults = StringBuilder()
        try {
            val sb = StringBuilder(PLACES_API_BASE
                    + TYPE_AUTOCOMPLETE + OUT_JSON)
            sb.append("?key=" + AppConstants.API_KEY)
            // sb.append("&components=country:cr");
            sb.append("&input=" + URLEncoder.encode(input, "utf8"))

            Log.e("Address Url", sb.toString())

            val url = URL(sb.toString())
            conn = url.openConnection() as HttpURLConnection
            val inStream = InputStreamReader(conn.inputStream)

            // Load the results into a StringBuilder
            var read: Int
            val buff = CharArray(1024)
            read = inStream.read(buff)
            while (read != -1) {
                jsonResults.append(buff, 0, read)
                read = inStream.read(buff)
            }
        } catch (e: MalformedURLException) {
            Log.e(LOG_TAG, "Error processing Places API URL", e)
            return resultList
        } catch (e: IOException) {
            Log.e(LOG_TAG, "Error connecting to Places API", e)
            return resultList
        } finally {
            conn?.disconnect()
        }

        try {


            // Create a JSON object hierarchy from the results
            val jsonObj = JSONObject(jsonResults.toString())
            val predsJsonArray = jsonObj.getJSONArray("predictions")
            // Log.e("array","==>"+predsJsonArray.toString());

            // Extract the Place descriptions from the results
            resultList = ArrayList(predsJsonArray.length())
            for (i in 0 until predsJsonArray.length()) {
                resultList.add(predsJsonArray.getJSONObject(i).getString(
                        "description"))
            }
        } catch (e: JSONException) {
            Log.e(LOG_TAG, "Cannot process JSON results", e)
        }

        return resultList
    }

    private inner class GetLatLong : AsyncTask<String, Int, JSONObject>() {

        override fun doInBackground(vararg params: String): JSONObject? {
            try {

                var data = ""

                val url = ("https://maps.googleapis.com/maps/api/geocode/json?address="
                        + URLEncoder.encode(params[0], "utf-8")
                        + "&key=" + AppConstants.API_KEY
                        + "&sensor=false")
                data = downloadUrl(url)


                val jsonObject = JSONObject(data)
                val jresultarr = jsonObject.optJSONArray("results")
                val jobj = jresultarr.optJSONObject(0)
                val jGeomatry = jobj.optJSONObject("geometry")
                val jLocation = jGeomatry.optJSONObject("location")
                val jaddressarray = jobj
                        .optJSONArray("address_components")
                val jcountry = jaddressarray.optJSONObject(jaddressarray
                        .length() - 1)
                Log.e("Country", "==>>>>>>>" + jcountry.optString("short_name"))

                return jLocation
            } catch (e: Throwable) {
                e.printStackTrace()
            }

            return null
        }

        override fun onPostExecute(result: JSONObject?) {
            super.onPostExecute(result)
            if (result != null) {

                try {
                    if (strFromTo.equals("from")) {
                        strFromLat = result.optString("lat")
                        strFromLong = result.optString("lng")

                        strFromTo = "to"
                        GetLatLong().execute(autoCompleteTextView_to.getText().toString())
                    } else if (strFromTo.equals("to")) {
                        strToLat = result.optString("lat")
                        strToLong = result.optString("lng")

                        ProgressDialogLoader.progressDialogDismiss()
                        bookRideApi(customerBookingListModel.business_id!!, customerBookingListModel.service_id!!)
                    }
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            } else {
                ProgressDialogLoader.progressDialogDismiss()
                UiValidator.displayMsgSnack(cons, activity, resources.getString(R.string.choose_valid_location))
                autoCompleteTextView_from.setText("")
                autoCompleteTextView_to.setText("")
                AppLog.e(TAG, "result=  null")
            }
        }
    }

    @Throws(IOException::class)
    private fun downloadUrl(strUrl: String): String {
        var data = ""
        var iStream: InputStream? = null
        var urlConnection: HttpURLConnection? = null
        try {
            val url = URL(strUrl)

            // Creating an http connection to communicate with url
            urlConnection = url.openConnection() as HttpURLConnection

            // Connecting to url
            urlConnection.connect()

            // Reading data from url
            iStream = urlConnection.inputStream

            val br = BufferedReader(InputStreamReader(
                    iStream!!))

            val sb = StringBuffer()

            var line: String? = ""
            line = br.readLine()
            while (line != null) {
                sb.append(line)
                line = br.readLine()
            }

            data = sb.toString()

            br.close()

        } catch (e: Exception) {
            Log.d(TAG, "Exception while downloading url " + e.toString())
        } finally {
            iStream!!.close()
            urlConnection!!.disconnect()
        }
        return data
    }

    private fun isValidDate(day: Int): Boolean {
        when (day) {
            1 -> if (serviceDetail.sunday.equals("1"))
                return true
            2 -> if (serviceDetail?.monday.equals("1"))
                return true
            3 -> if (serviceDetail?.tuesday.equals("1"))
                return true
            4 -> if (serviceDetail?.wednesday.equals("1"))
                return true
            5 -> if (serviceDetail?.thursday.equals("1"))
                return true
            6 -> if (serviceDetail?.friday.equals("1"))
                return true
            7 -> if (serviceDetail?.saturday.equals("1"))
                return true
        }

        return false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == requestCode && resultCode == Activity.RESULT_OK) {
            val result = data?.getSerializableExtra(AppKeys.OFFER_RESULT) as VenderOffersModel
            AppLog.e(TAG, result.toString())
            ed_coupon.setText(result.coupon)
        }
    }


    private fun getOfferPrice(totalDistance: Float) {
        val orderprice = (serviceDetail.rate_per_km?.toDouble()?.times(totalDistance))
        val business_id = serviceDetail.businessData.business_id.toString()
        val coupon = ed_coupon.text.toString()
        ProgressDialogLoader.progressDialogCreation(activity, getString(R.string.please_wait))
        apiService?.getOfferPrice("Bearer " + SharedPreferenceManager.getAuthToken(), business_id, coupon, orderprice.toString())
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(object : Observer<ApplyOfferResponse> {
                    override fun onSubscribe(d: Disposable) {}

                    override fun onNext(userResponse: ApplyOfferResponse) {
                        ProgressDialogLoader.progressDialogDismiss()
                        AppLog.e(TAG, userResponse.toString())
                        if (userResponse.status == (AppConstants.KEY_SUCCESS)) {
                            if (activity is BookRideTaxiActivity) {
                                (activity as BookRideTaxiActivity).applyOfferModel = userResponse.data
                                (activity as BookRideTaxiActivity).setDisplayFragment(4, resources.getString(R.string.booking_information), false)
                            } else if (activity is BookRideLimoActivity) {
                                (activity as BookRideLimoActivity).applyOfferModel = userResponse.data
                                (activity as BookRideLimoActivity).setDisplayFragment(4, resources.getString(R.string.booking_information), false)
                            } else if (activity is BookRideTourBusActivity) {
                                (activity as BookRideTourBusActivity).applyOfferModel = userResponse.data
                                (activity as BookRideTourBusActivity).setDisplayFragment(4, resources.getString(R.string.booking_information), false)
                            }
                        } else {
                            UiValidator.displayMsgSnack(cons, activity, userResponse.message)
                        }
                    }

                    override fun onError(e: Throwable) {
                        ProgressDialogLoader.progressDialogDismiss()
                        if (e != null)
                            UiValidator.displayMsgSnack(cons, activity, e.message)
                    }

                    override fun onComplete() {

                    }
                })
    }

}