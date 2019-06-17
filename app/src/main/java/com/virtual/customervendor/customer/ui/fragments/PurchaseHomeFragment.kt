package com.virtual.customervendor.customer.ui.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.virtual.customervendor.R
import com.virtual.customervendor.customer.ui.activity.PurchaseItemsActivity
import com.virtual.customervendor.model.OfferModel
import kotlinx.android.synthetic.main.fragment_purchase_item_home_customer.*
import java.util.*

class PurchaseHomeFragment : Fragment(), View.OnClickListener {

    var datetime: StringBuilder? = null
    var manager: FragmentManager? = null

    val list: ArrayList<OfferModel> = java.util.ArrayList()


    override fun onClick(v: View?) {

        when (v!!.id) {
            R.id.iv_back -> {
                activity?.onBackPressed()
            }
            R.id.btn_store -> {
                (activity as PurchaseItemsActivity).setDisplayFragment(2, activity!!.resources.getString(R.string.store_items),false)
            }
            R.id.btn_excursion -> {
                (activity as PurchaseItemsActivity).setDisplayFragment(2, activity!!.resources.getString(R.string.excursion),false)
            }
            R.id.btn_tickets -> {
                (activity as PurchaseItemsActivity).setDisplayFragment(2, activity!!.resources.getString(R.string.tickes_fpr_events),false)
            }

        }
    }


    companion object {
        fun newInstance(): PurchaseHomeFragment {
            val fragment = PurchaseHomeFragment()
            return fragment
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var mView = inflater.inflate(R.layout.fragment_purchase_item_home_customer, container, false)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
    }

    fun initView(view: View) {
        btn_store.setOnClickListener(this)
        btn_excursion.setOnClickListener(this)
        btn_tickets.setOnClickListener(this)
    }




}