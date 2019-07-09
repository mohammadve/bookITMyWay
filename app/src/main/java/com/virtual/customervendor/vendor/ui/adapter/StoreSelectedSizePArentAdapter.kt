package com.virtual.customervendor.vendor.ui.adapter

import android.content.Context
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.virtual.customervendor.R
import com.virtual.customervendor.customer.ui.dialogFragment.ClothColorSelectionDialogFragmentMulti
import com.virtual.customervendor.listener.ClothSizeRemovedListener
import com.virtual.customervendor.model.response.StoreClothColorModel
import com.virtual.customervendor.model.response.StoreClothSizeModel
import com.virtual.customervendor.utills.AppConstants
import kotlinx.android.synthetic.main.store_cloth_size_single_row.view.*
import java.util.*


class StoreSelectedSizePArentAdapter(val mContext: AppCompatActivity, val from: String, var offermodel: ArrayList<StoreClothSizeModel>, var colormodels: ArrayList<StoreClothColorModel>,var clothsizeRemovedListemner: ClothSizeRemovedListener) : RecyclerView.Adapter<StoreSelectedSizePArentAdapter.ResultItemViewHolder>() {

    var lisData: ArrayList<StoreClothSizeModel> = offermodel
    private lateinit var colorDialogFragmentMulti: ClothColorSelectionDialogFragmentMulti
    private var fManager: FragmentManager? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultItemViewHolder {
        val view = LayoutInflater.from(mContext.applicationContext).inflate(R.layout.store_cloth_size_single_row, parent, false)
        return ResultItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ResultItemViewHolder, position: Int) {
        holder.bind(mContext, from, lisData[position], position)
    }


    override fun getItemCount(): Int {
        return lisData.size
    }


    inner class ResultItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mcontx: Context? = null
        var datetime: String? = null


        fun bind(mContext: AppCompatActivity, from: String, offModel: StoreClothSizeModel, pos: Int) {


            itemView.hint_colors.text = mContext.resources.getString(R.string.create_colors_of_item) + " " + offModel.value
            itemView.remove_size.setOnClickListener {
                clothsizeRemovedListemner.onSizeRemoved(lisData[pos])
                lisData.removeAt(pos)
                notifyDataSetChanged()

            }
            var storeClothColorQuentityAdapter: StoreClothColorQuentityAdapter? = StoreClothColorQuentityAdapter(mContext, AppConstants.FROM_ADDDATA, offModel.variants)
            var manager = LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
            itemView.rv_selected_colors.layoutManager = manager
            itemView.rv_selected_colors.adapter = storeClothColorQuentityAdapter
//        rv_slected_size.setNestedScrollingEnabled(false)
            itemView.rv_selected_colors.setHasFixedSize(false)
            itemView.rl_color_parent.setOnClickListener {
                showColorSelectionDialogMMutli(pos, offModel.variants)
            }

        }
    }


    private fun showColorSelectionDialogMMutli(pos: Int, selectedColormodels: ArrayList<StoreClothColorModel>) {
        var k = 0
        while (k < colormodels.size) {
            colormodels[k].isSelected = false
            k++
        }
        var i = 0
        var j = 0
        while (i < selectedColormodels.size) {

            var isPresent = false
            var p = 0
            while (j < colormodels.size) {
                if (selectedColormodels[i].name.equals(colormodels[j].name)) {
                    isPresent = true
                    p = j
                    break

                }
                j++


            }

            if (isPresent) {
                colormodels[p].isSelected = true
            }


            i++
        }

        var beanValue = Gson().toJson(colormodels, object : TypeToken<List<StoreClothColorModel>>() {

        }.type)





        colorDialogFragmentMulti = ClothColorSelectionDialogFragmentMulti.newInstance("" + pos, beanValue)
        fManager = mContext.supportFragmentManager
        colorDialogFragmentMulti.show(fManager, "My Dialog")
    }

    public fun dismisColorSelectionDialog() {

        if (colorDialogFragmentMulti != null) {
            colorDialogFragmentMulti.dismiss()
        }
    }

}






