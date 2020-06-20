package com.joonaskinnunen.coronavirusmonitor

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    var countriesAdapter: CountriesAdapter? = null

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        val menuItem = menu.findItem(R.id.action_search)
        val search = menuItem.actionView as SearchView
        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(queryString: String): Boolean {
                Log.d("queryString", queryString)
                return false
            }

            override fun onQueryTextChange(queryString: String): Boolean {
                countriesAdapter?.getFilter()!!.filter(queryString)
                Log.d("queryString", queryString)
                return true
            }
        })
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView.layoutManager = LinearLayoutManager(this)

        loadData()
    }

    private fun loadData() {
        val url = "https://covid-193.p.rapidapi.com/statistics"
        val queue = Volley.newRequestQueue(this)
        val strRequest: JsonObjectRequest = object : JsonObjectRequest(Request.Method.GET, url, null,
            Response.Listener { response ->
                val data = response.getJSONArray("response")
                val list: ArrayList<JSONObject>? = ArrayList()
                for (i in 0 until data.length()) {
                    val countryObj = data.getJSONObject(i)
                    Log.d("Country: ", countryObj["country"].toString())
                    Log.d("data", data.toString())
                    Log.d("Country: ", countryObj["country"].toString())
                    list?.add(countryObj)
                }
                countriesAdapter = list?.let { CountriesAdapter(it, this) }
                recyclerView.adapter = countriesAdapter
            },
            Response.ErrorListener { error ->
                Log.d("Error loading data", error.toString()) }) {

            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["x-rapidapi-host"] = "covid-193.p.rapidapi.com"
                params["x-rapidapi-key"] = "74231e2613msh30bc911adf9128ap17f4b3jsn39d962d4974e"
                return params
            }
        }

        queue.add(strRequest)
    }
}
