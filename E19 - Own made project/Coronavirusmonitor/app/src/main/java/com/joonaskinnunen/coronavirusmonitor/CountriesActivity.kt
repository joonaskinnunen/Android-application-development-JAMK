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
            val totalDeaths: String = deaths["total"].toString()
            var newDeaths = deaths["new"].toString()
            val deathsPerM = deaths["1M_pop"].toString()
            var updateDate = country["day"].toString()

            if(newCases == "null") {
                newCases = resources.getString(R.string.notCases)
            }

            if(activeCases == "null") {
                activeCases = resources.getString(R.string.notCases)
            }

            if(newDeaths == "null") {
                newDeaths = resources.getString(R.string.notCases)
            }

            if(updateDate == "null") {
                updateDate = resources.getString(R.string.notCases)
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
