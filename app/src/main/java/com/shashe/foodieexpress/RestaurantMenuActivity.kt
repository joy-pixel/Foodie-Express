package com.shashe.foodieexpress

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.GridLayoutManager
import com.shashe.foodieexpress.adapter.MenuListAdapter
import com.shashe.foodieexpress.models.Menus
import com.shashe.foodieexpress.models.RestaurantModel
import kotlinx.android.synthetic.main.activity_restaurant_menu.*

class RestaurantMenuActivity : AppCompatActivity(), MenuListAdapter.MenuListClickListener {

    private var itemsInTheCartList: MutableList<Menus?>? = null
    private var totalItemsInCartCount = 0
    private  var menuList: List<Menus?>? = null
    private var menuListAdapter: MenuListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_menu)

        val restaurantModel = intent?.getParcelableExtra<RestaurantModel>("RestaurantModel")

        val actionBarctionBar: ActionBar? = supportActionBar
        actionBar?.setTitle(restaurantModel?.name)
        actionBar?.setSubtitle(restaurantModel?.address)
        actionBar?.setDisplayHomeAsUpEnabled(true)

        menuList = restaurantModel?.menus

        initRecyclerView(menuList)
        checkoutButton.setOnClickListener {
            if (itemsInTheCartList != null && itemsInTheCartList!!.size <= 0){
                Toast.makeText(this@RestaurantMenuActivity, "Please add some items in cart", Toast.LENGTH_LONG).show()
            }
            else{
                restaurantModel?.menus = itemsInTheCartList
            var intent = Intent(this@RestaurantMenuActivity,PlaceYourOrderActivity::class.java)
            intent.putExtra("RestaurantModel",restaurantModel)
                startActivityForResult(intent, 1000)

            }

        }
    }
    private fun initRecyclerView(menus:List<Menus?>?){
        menuRecyclerView.layoutManager = GridLayoutManager(this, 2)
        menuListAdapter = MenuListAdapter(menus,this)

        menuRecyclerView.adapter = menuListAdapter
    }

    override fun addToCartClickListener(menus: Menus) {
        if (itemsInTheCartList == null){
            itemsInTheCartList = ArrayList()

        }
        itemsInTheCartList?.add(menus)
        totalItemsInCartCount = 0
        for (menus in itemsInTheCartList!!){
            totalItemsInCartCount = totalItemsInCartCount + menus?.totalInCart!!
        }
        checkoutButton.text = "Checkout(" + totalItemsInCartCount + ")Items"

    }

    override fun updateCartClickListener(menus: Menus) {
        val index = itemsInTheCartList!!.indexOf(menus)
        itemsInTheCartList?.removeAt(index)
        itemsInTheCartList?.add(menus)
        totalItemsInCartCount = 0
        for (menus in itemsInTheCartList!!){
            totalItemsInCartCount = totalItemsInCartCount + menus?.totalInCart!!
        }
        checkoutButton.text = "Checkout(" + totalItemsInCartCount + ")Items"
    }

    override fun removeFromCartClickListener(menus: Menus) {
       if (itemsInTheCartList!!.contains(menus)){
           itemsInTheCartList?.remove(menus)
           totalItemsInCartCount = 0
           for (menus in itemsInTheCartList!!){
               totalItemsInCartCount = totalItemsInCartCount + menus?.totalInCart!!
           }
           checkoutButton.text = "Checkout(" + totalItemsInCartCount + ")Items"
       }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> finish()
            else ->{}
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1000 && resultCode == RESULT_OK){
            finish()

        }
    }
}