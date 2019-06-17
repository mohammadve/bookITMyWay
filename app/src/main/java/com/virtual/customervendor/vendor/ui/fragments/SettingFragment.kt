package com.virtual.customervendor.vendor.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import com.virtual.customervendor.R
import com.virtual.customervendor.commonActivity.TermsAndConditionActivity
import com.virtual.customervendor.managers.SharedPreferenceManager
import com.virtual.customervendor.utills.AppKeys
import com.virtual.customervendor.utills.AppLog
import com.virtual.customervendor.utills.SlideAnimationUtill
import com.virtual.customervendor.utills.UiValidator
import com.virtual.customervendor.vendor.ui.activity.SetLanguageActivity
import kotlinx.android.synthetic.main.fragment_setting_vendor.*

class SettingFragment : Fragment(), View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.txt_terms -> {
                startActivity(Intent(activity, TermsAndConditionActivity::class.java))
            }

            R.id.txt_change_language -> {

                val intent = Intent(activity, SetLanguageActivity::class.java)
                intent.putExtra("is_coomingFrom_Dash", true)
                startActivity(intent)
                SlideAnimationUtill.slideNextAnimation(activity as AppCompatActivity)
            }
            R.id.txt_logout -> {
                SharedPreferenceManager.setAuthToken("")
                var intent: Intent = Intent(activity, com.virtual.customervendor.vendor.ui.activity.LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(Intent(intent))
                SlideAnimationUtill.slideNextAnimation(activity as AppCompatActivity)
                activity!!.finish()
                UiValidator.displayMsgSnack(cons, activity, resources.getString(R.string.logout_suceesfully))
            }
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var mView = inflater.inflate(R.layout.fragment_setting_vendor, container, false)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
    }

    fun initView(view: View) {
        sw_switch.isChecked = SharedPreferenceManager.getBoolean(AppKeys.IS_NOTIFICTION_SHOW)
        sw_nswitch.isChecked = SharedPreferenceManager.getBoolean(AppKeys.IS_NOTIFICTION_SOUND)
        sw_switch.setOnCheckedChangeListener(this);
        sw_nswitch.setOnCheckedChangeListener(this);
        txt_terms.setOnClickListener(this)
        txt_change_language.setOnClickListener(this)
        txt_logout.setOnClickListener(this)
    }

    override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
        when (buttonView.id) {
            R.id.sw_switch -> {
                AppLog.e("switch_compat", isChecked.toString() + "")
                SharedPreferenceManager.setBoolean(AppKeys.IS_NOTIFICTION_SHOW, isChecked)
            }
            R.id.sw_nswitch -> {
                AppLog.e("switch_compat2", isChecked.toString() + "")
                SharedPreferenceManager.setBoolean(AppKeys.IS_NOTIFICTION_SOUND, isChecked)

            }
        }

    }

}