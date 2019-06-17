package com.virtual.customervendor.commonActivity

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.stripe.android.Stripe
import com.stripe.android.TokenCallback
import com.stripe.android.model.Card
import com.stripe.android.model.Token
import com.stripe.android.view.CardMultilineWidget
import com.virtual.customervendor.R
import com.virtual.customervendor.utills.*
import kotlinx.android.synthetic.main.activity_payment.*

class PaymentActivity : BaseActivity(), View.OnClickListener {
    private var progressDialog: ProgressDialog? = null
    var toolbar: Toolbar? = null
    var mTitle: TextView? = null
    internal var mCardInputWidget: CardMultilineWidget? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)
        init()
    }

    fun init() {
        toolbar = findViewById<Toolbar>(R.id.toolbar)
        mTitle = toolbar!!.findViewById(R.id.tv_toolbarTitleText) as TextView
        val iv_back = toolbar!!.findViewById(R.id.iv_back) as ImageView
      //  val btn_make_payment = findViewById(R.id.btn_make_payment) as TextView
        mCardInputWidget = findViewById(R.id.card_input_widget) as CardMultilineWidget
        iv_back.setOnClickListener(this)
        btn_make_payment.setOnClickListener(this)
        mTitle!!.text = getString(R.string.make_payment)
    }

    override fun onClick(v: View?) {

        when (v!!.id) {
            R.id.iv_back -> {
                onBackPressed()
            }
            R.id.btn_make_payment -> {
                UiValidator.hideSoftKeyboard(this as AppCompatActivity)
                checkCardDetails()
            }
        }
    }



    private fun checkCardDetails() {

        val cardToSave = mCardInputWidget!!.getCard()
        if (cardToSave == null) {
            Toast.makeText(this, "Invalid Card Details", Toast.LENGTH_SHORT).show()
        } else {

            if (AppUtils.isInternetConnected(this)) {
                showProgress(true)
                generateToken(cardToSave)
            } else {
                Toast.makeText(this, getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun showProgress(isShow: Boolean) {
        if (isShow) {
            ProgressDialogLoader.progressDialogCreation(this, getString(R.string.please_wait))
        } else {
            ProgressDialogLoader.progressDialogDismiss()
        }
    }

    /*
     *Generate Token based on card entered by user
     *
     */

    fun generateToken(card: Card) {
        val stripe = Stripe(this@PaymentActivity, AppConstants.STRIPE_KEY)

        stripe.createToken(
                card,
                object : TokenCallback {
                    override fun onSuccess(token: Token) {
                        // Toast.makeText(PaymentActivity.this, "Token---" + token.getId(), Toast.LENGTH_SHORT).show();
                        AppLog.e("Token123", "-----" + token.id)
                        showProgress(false);
                        val returnIntent = Intent()
                        returnIntent.putExtra("card_token", token.id)
                        setResult(Activity.RESULT_OK,returnIntent)
                        finish()


                    }

                    override fun onError(error: Exception) {
                        showProgress(false);

                        Log.e("@@ Ex", "---" + error.toString())
                        Toast.makeText(this@PaymentActivity, "Token---$error", Toast.LENGTH_SHORT).show()

                    }
                }
        )
    }

    override fun onBackPressed() {
        setResult(Activity.RESULT_CANCELED)
        finish()

    }
}
