package com.example.employeesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView.layoutManager = LinearLayoutManager(this)

        val queue = Volley.newRequestQueue(this)
        val url = "https://api.jsonbin.io/b/5ecace3ebbaf1f2589465609"
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                val employees = response.getJSONArray("employees")
                recyclerView.adapter = EmployeesAdapter(employees)
            },
            Response.ErrorListener { error ->
                Log.d("JSON",error.toString())
            }
        )
        queue.add(jsonObjectRequest)
    }
}
