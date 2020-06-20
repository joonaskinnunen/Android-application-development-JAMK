package com.joonaskinnunen.coronavirusmonitor
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.country_item.view.*
import org.json.JSONArray
import org.json.JSONObject

class CountriesAdapter(private val countries: ArrayList<JSONObject>, private val context: Context)
    : RecyclerView.Adapter<CountriesAdapter.ViewHolder>(), Filterable {

    private var countryList: List<JSONObject>? = countries
    private var filteredCountryList: List<JSONObject>? = countryList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountriesAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.country_item, parent, false)
        return ViewHolder(view)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.nameTextView
        val casesTextView: TextView = view.casesTextView
        val countryItemImageView: ImageView = view.countryItemImageView

        init {
            itemView.setOnClickListener {
                val intent = Intent(view.context, CountriesActivity::class.java)
                intent.putExtra("country", filteredCountryList?.get(adapterPosition)?.toString())
                view.context.startActivity(intent)
            }
        }
    }

    override fun onBindViewHolder(
        holder: CountriesAdapter.ViewHolder,
        position: Int)
    {
        holder.setIsRecyclable(false)
        val country: JSONObject? = filteredCountryList?.get(position)
        val cases: JSONObject = country?.get("cases") as JSONObject
        val name: String? = country["country"].toString()
        val totalCases: String? = cases["total"].toString()

        holder.nameTextView.text = name
        holder.casesTextView.text = totalCases

        val flagName = name?.replace("-", "_", true)?.toLowerCase()
        val id = context.resources.getIdentifier(flagName, "drawable", context.packageName)

        if(id == 0) {
            holder.countryItemImageView.setVisibility(ImageView.INVISIBLE)
        } else {
            holder.countryItemImageView.setBackgroundResource(id)
        }
    }

    override fun getItemCount(): Int = filteredCountryList!!.size

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence): FilterResults {
                val charSequenceString = constraint.toString()
                if (charSequenceString.isEmpty()) {
                    filteredCountryList = countryList
                } else {
                    val filteredList: MutableList<JSONObject> = ArrayList()
                    countryList?.forEach { country ->
                        if (country["country"].toString().toLowerCase()
                                .contains(charSequenceString.toLowerCase())
                        ) {
                            filteredList.add(country)
                        }
                        filteredCountryList = filteredList
                    }
                }
                val results = FilterResults()
                results.values = filteredCountryList
                return results
            }

            override fun publishResults(
                constraint: CharSequence,
                results: FilterResults
            ) {
                filteredCountryList = results.values as List<JSONObject>
                notifyDataSetChanged()
            }
        }
    }
}