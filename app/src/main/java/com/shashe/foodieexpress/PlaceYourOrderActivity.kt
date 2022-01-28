package com.shashe.foodieexpress

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.LinearLayoutManager
import com.shashe.foodieexpress.adapter.PlaceYourOrderAdapter
import com.shashe.foodieexpress.models.RestaurantModel
import kotlinx.android.synthetic.main.activity_place_your_order.*

class PlaceYourOrderActivity : AppCompatActivity() {
    var PlaceYourOrderAdapter: PlaceYourOrderAdapter? = null
    var isDeliveryOn: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_your_order)


        val restaurantModel: RestaurantModel? = intent.getParcelableExtra("RestaurantModel")
        val actionbar: ActionBar? = supportActionBar
        actionbar?.setTitle(restaurantModel?.name)
        actionbar?.setSubtitle(restaurantModel?.address)
        actionbar?.setDisplayHomeAsUpEnabled(true)

        buttonPlaceYourOrder.setOnClickListener {
            onPlaceOrderButtonClick(restaurantModel)
        }

        switchDelivery?.setOnCheckedChangeListener { button, isChecked ->
            if (isChecked) {
                inputAddress.visibility = View.VISIBLE
                inputCity.visibility = View.VISIBLE
                inputState.visibility = View.VISIBLE
                inputZip.visibility = View.VISIBLE
                tvDeliveryCharge.visibility = View.VISIBLE
                isDeliveryOn = true
                calculateTotalAmount(restaurantModel)
            } else {
                inputAddress.visibility = View.GONE
                inputCity.visibility = View.GONE
                inputState.visibility = View.GONE
                inputZip.visibility = View.GONE
                tvDeliveryCharge.visibility = View.GONE
                isDeliveryOn = false
                calculateTotalAmount(restaurantModel)
            }

        }

        initRecyclerView(restaurantModel)
        calculateTotalAmount(restaurantModel)
    }

    private fun initRecyclerView(restaurantModel: RestaurantModel?) {
        cartItemsRecyclerView.layoutManager = LinearLayoutManager(this)
        PlaceYourOrderAdapter = PlaceYourOrderAdapter(restaurantModel?.menus)
        cartItemsRecyclerView.adapter = PlaceYourOrderAdapter
    }

    private fun calculateTotalAmount(restaurantModel: RestaurantModel?) {
        var subTotalAmount = 0f
        for (menu in restaurantModel?.menus!!) {
            subTotalAmount += menu?.price!! * menu?.totalInCart!!


        }
        tvSubtotalAmount.text = "$" + String.format("%.2f", subTotalAmount)
        if (isDeliveryOn) {
            tvDeliveryChargeAmount.text =
                "$" + String.format("%.2f", restaurantModel.delivery_charge?.toFloat())
            subTotalAmount += restaurantModel?.delivery_charge?.toFloat()!!
        }

        tvTotalAmount.text = "$" + String.format("%.2f", subTotalAmount)
    }

    private fun onPlaceOrderButtonClick(restaurantModel: RestaurantModel?) {
        if (TextUtils.isEmpty(inputName.text.toString())) {
            inputName.error = "Enter your name"
            return
        } else if (isDeliveryOn && TextUtils.isEmpty(inputAddress.text.toString())) {
            inputAddress.error = "Enter your address"
            return
        } else if (isDeliveryOn && TextUtils.isEmpty(inputCity.text.toString())) {
            inputCity.error = "Enter your City Name"
            return
        } else if (isDeliveryOn && TextUtils.isEmpty(inputZip.text.toString())) {
            inputZip.error = "Enter your Zip code"
            return
        } else if (TextUtils.isEmpty(inputCardNumber.text.toString())) {
            inputCardNumber.error = "Enter your credit card number"
            return
        }else if (TextUtils.isEmpty(inputCardExpiry.text.toString())) {
            inputCardExpiry.error = "Enter your credit card expiry"
            return
        }else if (TextUtils.isEmpty(inputCardPin.text.toString())) {
            inputCardPin.error = "Enter your credit card  pin/cvv"
            return
        }
        var intent = Intent(this@PlaceYourOrderActivity,SuccessOrderActivity::class.java)
        intent.putExtra("RestaurantModel", restaurantModel)
        startActivityForResult(intent,1000)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1000){
            setResult(RESULT_OK)
            finish()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> finish()
            else -> {}
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        setResult(RESULT_CANCELED)
        finish()
    }
}