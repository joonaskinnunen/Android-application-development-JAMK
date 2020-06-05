package com.example.golfcoursesinamap

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.clustering.ClusterManager
import kotlinx.android.synthetic.main.info_window.view.*
import org.json.JSONArray

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, ClusterManager.OnClusterItemClickListener<GolfCourseItem> {

    private lateinit var mMap: GoogleMap
    lateinit var clusterManager: ClusterManager<GolfCourseItem>
    lateinit var markerClusterRenderer: MarkerClusterRenderer
    var clickedItem: GolfCourseItem? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        clusterManager = ClusterManager<GolfCourseItem>(this, mMap)
        mMap.setOnCameraIdleListener(clusterManager)
        markerClusterRenderer = MarkerClusterRenderer(this, mMap, clusterManager)
        clusterManager.setRenderer(markerClusterRenderer)
        clusterManager.setOnClusterItemClickListener(this)
        clusterManager.getMarkerCollection().setInfoWindowAdapter(CustomInfoWindowAdapter())
        clusterManager.cluster()
        loadData()
    }

    private fun loadData() {
        val queue = Volley.newRequestQueue(this)
        // 1. code here
        val url = "https://ptm.fi/materials/golfcourses/golf_courses.json"
        var golf_courses: JSONArray
        var course_types: Map<String, Float> = mapOf(
            "?" to BitmapDescriptorFactory.HUE_VIOLET,
            "Etu" to BitmapDescriptorFactory.HUE_BLUE,
            "Kulta" to BitmapDescriptorFactory.HUE_GREEN,
            "Kulta/Etu" to BitmapDescriptorFactory.HUE_YELLOW
        )
        // create JSON request object
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                // JSON loaded successfully
                // 2. code here
                golf_courses = response.getJSONArray("courses")
                // loop through all objects
                for (i in 0 until golf_courses.length()) {
                    // get course data
                    val course = golf_courses.getJSONObject(i)
                    val lat = course["lat"].toString().toDouble()
                    val lng = course["lng"].toString().toDouble()
                    val type = course["type"].toString()
                    val title = course["course"].toString()
                    val address = course["address"].toString()
                    val phone = course["phone"].toString()
                    val email = course["email"].toString()
                    val web_url = course["web"].toString()
                    var list = listOf(title, address, phone, email, web_url)

                    if (course_types.containsKey(type)) {
                        var item = GolfCourseItem(
                            lat,
                            lng,
                            type,
                            list
                        )

                        clusterManager.addItem(item)
                    } else {
                        Log.d("Load data", "This course type does not exist in evaluation ${type}")
                    }
                }
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(65.5, 26.0), 5.0F))
            },
            Response.ErrorListener { error ->
                // Error loading JSON
                Log.d("Volley response", error.message)
            }
        )
        // Add the request to the RequestQueue
        queue.add(jsonObjectRequest)
    }

    internal inner class CustomInfoWindowAdapter : GoogleMap.InfoWindowAdapter {
        private val contents: View = layoutInflater.inflate(R.layout.info_window, null)

        override fun getInfoWindow(marker: Marker?): View? {
            return null
        }

        override fun getInfoContents(marker: Marker): View {
            // UI elements
            val titleTextView = contents.titleTextView
            val addressTextView = contents.addressTextView
            val phoneTextView = contents.phoneTextView
            val emailTextView = contents.emailTextView
            val webTextView = contents.webTextView

                val list: List<String> = clickedItem?.getTag() as List<String>
                titleTextView.text = list[0]
                addressTextView.text = list[1]
                phoneTextView.text = list[2]
                emailTextView.text = list[3]
                webTextView.text = list[4]

            return contents
        }
    }

    override fun onClusterItemClick(item: GolfCourseItem?): Boolean {
        clickedItem = item
        return false
    }
}
