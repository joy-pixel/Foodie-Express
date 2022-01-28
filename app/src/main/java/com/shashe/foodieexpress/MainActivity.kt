package com.shashe.foodieexpress

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.shashe.foodieexpress.adapter.RestaurantListAdapter
import com.shashe.foodieexpress.models.RestaurantModel
import java.io.*
import java.lang.Exception
import com.shashe.foodieexpress.R.id.recyclerViewRestaurant as idRecyclerViewRestaurant

class MainActivity : AppCompatActivity(), RestaurantListAdapter.RestaurantListClickListener {
    var recyclerViewRestaurant:RecyclerView?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val actionBar: ActionBar? = supportActionBar
        actionBar?.setTitle("Restaurant List")


       val restaurantModel = getRestaurantData()

        recyclerViewRestaurant = findViewById<RecyclerView>(idRecyclerViewRestaurant)
        initRecyclerView(restaurantModel)
    }


    private fun initRecyclerView(restaurantList: List<RestaurantModel?>?){
        recyclerViewRestaurant!!.layoutManager = LinearLayoutManager(this)
        val adapter = RestaurantListAdapter(restaurantList,this)
        recyclerViewRestaurant!!.adapter = adapter
    }

        private fun getRestaurantData(): List<RestaurantModel?>?{
            val inputStream:InputStream = resources.openRawResource(R.raw.restaurant)
            val writer: Writer = StringWriter()
            val buffer = CharArray(1024)
            try {
                val reader: Reader = BufferedReader(InputStreamReader(inputStream,"UTF-8"))
                var n :Int
                while (reader.read(buffer).also { n = it } != -1){
                    writer.write(buffer, 0, n)
                }
            }catch (e: Exception){}
            val jSonStr: String = writer.toString()
            val gson = Gson()
            val restaurantModel = gson.fromJson<Array<RestaurantModel>>(jSonStr,Array<RestaurantModel>::class.java).toList()

            return restaurantModel
        }

    fun onItem(restaurantModel: RestaurantModel) {
        val intent = Intent(this@MainActivity,RestaurantMenuActivity::class.java)
        intent.putExtra("RestaurantModel", restaurantModel)
        startActivity(intent)
    }

    override fun onItemClick(restaurantModel: RestaurantModel) {
        val intent = Intent(this@MainActivity,RestaurantMenuActivity::class.java)
        intent.putExtra("RestaurantModel", restaurantModel)
        startActivity(intent)
    }


}