package com.joonaskinnunen.coronavirusmonitor
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import kotlinx.android.synthetic.main.country_item.view.*
import org.json.JSONArray
import org.json.JSONObject

class CountriesAdapter(private val countries: JSONArray, private val context: Context)
    : RecyclerView.Adapter<CountriesAdapter.ViewHolder>() {

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
                intent.putExtra("country",countries[adapterPosition].toString())
                view.context.startActivity(intent)
            }
        }
    }

    override fun onBindViewHolder(
        holder: CountriesAdapter.ViewHolder,
        position: Int)
    {
        holder.setIsRecyclable(false)
        val country: JSONObject? = countries.getJSONObject(position)
        val cases: JSONObject = country?.get("cases") as JSONObject
        val name: String? = country["country"].toString()
        val totalCases: String? = cases["total"].toString()

        holder.nameTextView.text = name
        holder.casesTextView.text = totalCases

        val flagName = name?.replace("-", "_", true)?.toLowerCase()
        Log.d("flagName: ", flagName)
        val id = context.resources.getIdentifier(flagName, "drawable", context.packageName)
        Log.d("drawableId", id.toString())

        if(id == 0) {
            holder.countryItemImageView.setVisibility(ImageView.INVISIBLE)
        } else {
            holder.countryItemImageView.setBackgroundResource(id)
        }
    }

    override fun getItemCount(): Int = countries.length()
}