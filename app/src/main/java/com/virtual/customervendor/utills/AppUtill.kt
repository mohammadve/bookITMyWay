package com.virtual.customer_vendor.utill

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.location.Location
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.text.Editable
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TimePicker
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.virtual.customervendor.R
import com.virtual.customervendor.utills.AppLog
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


class AppUtill {
    companion object {
        fun getTime1(textView: EditText, context: Context) {
            val c = Calendar.getInstance()
            val hour = c.get(Calendar.HOUR)
            val minute = c.get(Calendar.MINUTE)
            val timePicker = TimePickerDialog.OnTimeSetListener { timePicker: TimePicker, hour1: Int, min: Int ->
                c.set(Calendar.HOUR_OF_DAY, hour1)
                c.set(Calendar.MINUTE, min)
                c.set(Calendar.SECOND, 0)
                textView.text = Editable.Factory.getInstance().newEditable(SimpleDateFormat("HH:mm").format(c.time))
            }
            TimePickerDialog(context, timePicker, hour, minute, true).show()
        }


        fun compareTime(firstTime: String, secondTime: String): Boolean {
            val firstTime = SimpleDateFormat("HH:mm").parse(firstTime)
            val secondTime = SimpleDateFormat("HH:mm").parse(secondTime)
            AppLog.e("AppUtill", secondTime.after(firstTime).toString())
            return secondTime.after(firstTime)
        }

        fun compareDate(firstTime: String, secondTime: String): Boolean {
            val firstTime = SimpleDateFormat("yyyy-MM-dd").parse(firstTime)
            val secondTime = SimpleDateFormat("yyyy-MM-dd").parse(secondTime)
            AppLog.e("AppUtill", secondTime.after(firstTime).toString())
            return secondTime.after(firstTime) || secondTime.equals(firstTime)
        }

        fun compareDateEquals(firstTime: String, secondTime: String): Boolean {
            val firstTime = SimpleDateFormat("yyyy-MM-dd").parse(firstTime)
            val secondTime = SimpleDateFormat("yyyy-MM-dd").parse(secondTime)
            AppLog.e("AppUtill", secondTime.after(firstTime).toString())
            return secondTime.equals(firstTime)
        }

        fun getDate(textView: EditText, context: Context) {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
            val dpd = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                c.set(Calendar.YEAR, year)
                c.set(Calendar.MONTH, monthOfYear)
                c.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                textView.text = Editable.Factory.getInstance().newEditable(SimpleDateFormat("yyyy-MM-dd").format(c.time))
            }

            val datepicker = DatePickerDialog(context, dpd, year, month, day)
            datepicker.getDatePicker().setMinDate(c.getTimeInMillis())
//            datepicker.datePicker.isSelected()
            datepicker.show()
        }


        fun getDateText(textView: EditText, context: Context) {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
            val dpd = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                c.set(Calendar.YEAR, year)
                c.set(Calendar.MONTH, monthOfYear)
                c.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                textView.text = Editable.Factory.getInstance().newEditable(SimpleDateFormat("yyyy-MM-dd").format(c.time))
            }
            val datepicker = DatePickerDialog(context, dpd, year, month, day)
            datepicker.getDatePicker().setMinDate(c.getTimeInMillis());
            datepicker.show()
        }


        fun compareTimeSlot(startTime: String, endTime: String, time: String): Boolean {
            val firstTime = SimpleDateFormat("HH:mm:ss").parse(startTime)
            val secondTime = SimpleDateFormat("HH:mm:ss").parse(endTime)
            val time = SimpleDateFormat("HH:mm:ss").parse(time)
            AppLog.e("AppUtill", secondTime.after(firstTime).toString())
            return (time.before(secondTime) && time.after(firstTime)) || time.equals(firstTime) || time.equals(secondTime)
        }


        fun handlePager(context: Context, SCREEN_COUNT: Int, layout: LinearLayout, viewPager: ViewPager) {

            if (SCREEN_COUNT > 1) {
                addBottomDots(context, 0, layout, SCREEN_COUNT)
            }
            viewPager!!.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

                override fun onPageSelected(position: Int) {

                    if (viewPager.adapter?.count!! > 1) {
                        addBottomDots(context, position, layout, viewPager.adapter?.count!!)
                    }
                }

                override fun onPageScrollStateChanged(state: Int) {}
            })
        }

        private fun addBottomDots(context: Context, currentPage: Int, layoutDots: LinearLayout, SCREEN_COUNT: Int) {
            val dots = arrayOfNulls<ImageView>(SCREEN_COUNT)
            val colorsActive = ContextCompat.getDrawable(context, R.drawable.active_home_view_pager_dot)
            val colorsInactive = ContextCompat.getDrawable(context, R.drawable.non_active_home_view_pager_dot)
            layoutDots.removeAllViews()
            for (i in dots.indices) {
                dots[i] = ImageView(context)
                val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                params.setMargins(5, 5, 5, 5)
                dots[i]!!.setImageDrawable(colorsInactive)
                layoutDots.addView(dots[i], params)
            }
            if (dots.size > 0) {
                if (dots.size > 1) {
                    layoutDots.visibility = View.VISIBLE
                    dots[currentPage]!!.setImageDrawable(colorsActive)
                } else {
                    layoutDots.visibility = View.GONE
                }
            }
        }


        fun loadImageList(context: Context, url: String, imageView: ImageView) {
            val options = RequestOptions()
            options.placeholder(R.drawable.place_holder_list)
            options.error(R.drawable.place_holder_list)
            Glide.with(context).load(url).apply(options).into(imageView)
        }

        fun loadImageRound(context: Context, url: String, imageView: ImageView) {
            val options = RequestOptions()
            options.placeholder(R.drawable.place_holder_round)
            options.override(100, 100)
            options.error(R.drawable.place_holder_round)
            Glide.with(context).load(url).apply(options).into(imageView)
        }

        fun loadImageRoundProfile(context: Context, url: String, imageView: ImageView) {
            val options = RequestOptions()
            options.placeholder(R.drawable.place_holder_round)
            options.override(120, 120)

            options.error(R.drawable.place_holder_round)
            options.circleCrop()
            Glide.with(context).load(url).apply(options).into(imageView)
        }


        fun getKmFromLatLong(lat1: Double, lng1: Double, lat2: Double, lng2: Double): Float {
            val loc1 = Location("")
            loc1.setLatitude(lat1)
            loc1.setLongitude(lng1)
            val loc2 = Location("")
            loc2.setLatitude(lat2)
            loc2.setLongitude(lng2)
            val distanceInMeters = loc1.distanceTo(loc2)
            return distanceInMeters / 1000
        }

        val DATE_FORMAT = "d/M/yyyy"  //or use "M/d/yyyy"

        fun getDaysBetweenDates(start: String, end: String): Long {
            val dateFormat = SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH)
            val startDate: Date
            val endDate: Date
            var numberOfDays: Long = 0
            try {
                startDate = dateFormat.parse(start)
                endDate = dateFormat.parse(end)
                numberOfDays = getUnitBetweenDates(startDate, endDate, TimeUnit.DAYS)
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            return numberOfDays
        }

        private fun getUnitBetweenDates(startDate: Date, endDate: Date, unit: TimeUnit): Long {
            val timeDiff = endDate.time - startDate.time
            return unit.convert(timeDiff, TimeUnit.MILLISECONDS)
        }


        fun getDifferenceDays(d1: String, d2: String): Int {
            if (d1.isEmpty())
                return 0

            if (d2.isEmpty())
                return 0

            val sdf = SimpleDateFormat("yyyy-MM-dd")
            var date1 = Date(sdf.parse(d1).getTime())
            var date2 = Date(sdf.parse(d2).getTime())

            var daysdiff = 0
            val diff = date2.time - date1.time
            val diffDays = diff / (24 * 60 * 60 * 1000) + 1
            daysdiff = diffDays.toInt()
            return daysdiff
        }

        fun isTimeGreater(my_date: String): Boolean {
            AppLog.e("isTimeGreater", "isTimeGreater= " + my_date)
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm")
            AppLog.e("isTimeGreater", "isTimeGreater= " + sdf.toString())
            val strDate = sdf.parse(my_date)
            AppLog.e("isTimeGreater", "isTimeGreater= " + strDate.toString() + "----" + strDate.time + "-----" + System.currentTimeMillis())
            if (System.currentTimeMillis() > strDate.time) {
                return true
            } else {
                return false
            }
        }

        fun calculateTotalAmt(tax: String, price: String): String {
            AppLog.e("Calculate", "tax= " + tax + " price" + price)
            var totalPricef: Double
            var tax = tax.toDouble()
            var price = price.toDouble()
            totalPricef = ((price * tax / 100) + price)

            AppLog.e("Calculate %", totalPricef.toString())
            return totalPricef.toString()
        }

        fun calculateTaxAmt(tax: String, price: String): String {
            AppLog.e("Calculate", "tax= " + tax + " price" + price)
            var totalPricef: Double
            var tax = tax.toDouble()
            var price = price.toDouble()
            totalPricef = ((price * tax / 100))

            AppLog.e("Calculate %", totalPricef.toString())
            return totalPricef.toString()
        }


        fun loadImageHomeBanner(context: Context, url: String, imageView: ImageView){
            val options = RequestOptions()
            options.placeholder(R.drawable.place_holder_list)
            options.error(R.drawable.place_holder_list)
            options.override(imageView.width, imageView.height)
            Glide.with(context).load(url).apply(options).into(imageView)

        }

    }
}