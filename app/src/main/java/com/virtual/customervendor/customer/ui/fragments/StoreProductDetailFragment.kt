package com.virtual.customervendor.customer.ui.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import com.bumptech.glide.Glide
import com.virtual.customervendor.R
import com.virtual.customervendor.customer.ui.activity.PurchaseItemsActivity
import com.virtual.customervendor.model.ItemPriceStoreModel
import com.virtual.customervendor.utills.AppLog
import com.virtual.customervendor.utills.AppUtils
import com.virtual.customervendor.utills.UiValidator
import kotlinx.android.synthetic.main.fragment_store_product_detail.*

class StoreProductDetailFragment : Fragment(), View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        AppLog.e(TAG, isChecked.toString())
    }

    override fun onClick(v: View?) {

        when (v!!.id) {
            R.id.plus_img -> {
                clickPlus()
            }
            R.id.minus_img -> {
                clickMinus()
            }
            R.id.btn_save -> {

                itemPriceModelItem = ItemPriceStoreModel()
                itemPriceModelItem?.item_image = itemPriceModel.item_image
                itemPriceModelItem?.item_id = itemPriceModel.item_id
                itemPriceModelItem?.item_name = itemPriceModel.item_name
                itemPriceModelItem?.item_description = itemPriceModel.item_description
                itemPriceModelItem?.item_price = itemPriceModel.item_price
                itemPriceModelItem?.quantity = count

                if (!chkone.isChecked) {
                    itemPriceModelItem?.add_on_one = ""
                } else {
                    itemPriceModelItem?.add_on_one = itemPriceModel.add_on_one
                }
                if (!chktwo.isChecked) {
                    itemPriceModelItem?.add_on_two = ""
                } else {
                    itemPriceModelItem?.add_on_two = itemPriceModel.add_on_two
                }
                AppLog.e(TAG, itemPriceModelItem.toString())
                (activity as PurchaseItemsActivity).addedItemsListNew.add(itemPriceModelItem!!)
                (activity as PurchaseItemsActivity).setCartValueVisible(true)

                UiValidator.displayMsgSnackShort(cons, activity, resources.getString(R.string.item_added))
            }
        }
    }

    companion object {
        fun newInstance(): StoreProductDetailFragment {
            val fragment = StoreProductDetailFragment()
            return fragment
        }
    }

    var TAG: String = StoreProductDetailFragment::class.java.name;
    var itemPriceModel = ItemPriceStoreModel()
    var count = 0
    var itemPriceModelItem: ItemPriceStoreModel? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var mView = inflater.inflate(R.layout.fragment_store_product_detail, container, false)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chkone.setOnCheckedChangeListener(this)
        chktwo.setOnCheckedChangeListener(this)

        itemPriceModel = (activity as PurchaseItemsActivity).itemPriceModel

        if (itemPriceModel != null) {
            if (itemPriceModel.item_image.size > 0)
                Glide.with(this).load(itemPriceModel.item_image.get(0)).into(img_upload)
            ed_itemname.text = itemPriceModel.item_name
            ed_price.text = itemPriceModel.item_price
            ed_desc.text = itemPriceModel.item_description
            count = itemPriceModel.quantity

            tv_price.setText(AppUtils.getRateWithSymbol("" + itemPriceModel.item_price!!.toInt() * count))


            if (!itemPriceModel.add_on_one.isEmpty()) {
                hint_bname.visibility = View.VISIBLE
                chkone.visibility = View.VISIBLE
                chkone.text = itemPriceModel.add_on_one
            } else {
                chkone.visibility = View.GONE
            }


            if (!itemPriceModel.add_on_two.isEmpty()) {
                hint_bname.visibility = View.VISIBLE
                chktwo.visibility = View.VISIBLE
                chktwo.text = itemPriceModel.add_on_two
            } else {
                chktwo.visibility = View.GONE
            }
            chktwo.setChecked(false)

            AppLog.e(TAG, chkone.isChecked.toString())
            AppLog.e(TAG, chktwo.isChecked.toString())
        }

        (activity as PurchaseItemsActivity).setCartVisible(true)
        (activity as PurchaseItemsActivity).setCartValueVisible(true)

        plus_img.setOnClickListener(this)
        minus_img.setOnClickListener(this)
        btn_save.setOnClickListener(this)

        chkone.setChecked(false)
        chktwo.setChecked(false)
        AppLog.e(TAG + "ONE", chkone.isChecked.toString())
        AppLog.e(TAG + "TWO", chktwo.isChecked.toString())
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            fragmentManager!!.beginTransaction().detach(this).attach(this).commit()
            Log.i("IsRefresh", "Yes")
        }
    }

    fun clickMinus() {
        if (count != 1) {
            tv_quantity.setText("" + count.minus(1))
            count--
            tv_price.setText(AppUtils.getRateWithSymbol("" + itemPriceModel.item_price!!.toInt() * count))
        }
    }

    fun clickPlus() {
        tv_quantity.setText("" + count.plus(1))
        count++
        tv_price.setText(AppUtils.getRateWithSymbol("" + (itemPriceModel.item_price!!.toInt() * count)))
    }


}