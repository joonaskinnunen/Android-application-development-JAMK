package com.joonaskinnunen.coronavirusmonitor

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_country.*
import kotlinx.android.synthetic.main.activity_country.nameTextView
import org.json.JSONObject


class CountriesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_country)

        val bundle: Bundle? = intent.extras
        if (bundle != null) {
            val countriesString = bundle.getString("country")
            val country = JSONObject(countriesString)
            val cases: JSONObject? = country["cases"] as JSONObject
            val deaths: JSONObject = country["deaths"] as JSONObject
            val name = country["country"].toString()
            var newCases = cases?.get("new")?.toString()
            var activeCases = cases?.get("active")?.toString()
            val totalCases = cases?.get("total")?.toString()
            val casesPerM = cases?.get("1M_pop")?.toString()
            val totalDeaths: String? = deaths["total"].toString()
            var newDeaths: String? = deaths["new"].toString()
            val deathsPerM: String? = deaths["1M_pop"].toString()
            var updateDate: String? = country["day"].toString()

            Log.d("newdeaths", newDeaths)
            if(newCases == "null") {
                newCases = "0"
            }

            if(activeCases == "null") {
                activeCases = "0"
            }

            if(newDeaths == "null") {
                newDeaths = "0"
            }

            if(updateDate == "null") {
                updateDate = ""
            }

            this.nameTextView.text = name
            this.newCasesTextView.text = newCases
            this.activeCasesTextView.text = activeCases
            this.totalCasesTextView.text = totalCases
            this.casesPerMTextView.text = casesPerM
            this.totalDeathsTextView.text = totalDeaths
            this.newDeathsTextView.text = newDeaths
            this.deathsPerMTextView.text = deathsPerM
            this.updateDateTextView.text = updateDate

            val flagName = name.replace("-", "_", true).toLowerCase()
            val id = resources.getIdentifier(flagName, "drawable", packageName)
            if(id == 0) {
                countryImageView.setVisibility(ImageView.INVISIBLE)
            } else {
                countryImageView.setBackgroundResource(id)
            }
        }
    }

}
