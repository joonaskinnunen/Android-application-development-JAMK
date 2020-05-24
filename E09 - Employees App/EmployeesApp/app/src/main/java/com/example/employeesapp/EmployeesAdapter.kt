package com.example.employeesapp

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import kotlinx.android.synthetic.main.employee_item.view.*
import org.json.JSONArray
import org.json.JSONObject

class EmployeesAdapter(private val employees: JSONArray)
    : RecyclerView.Adapter<EmployeesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeesAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.employee_item, parent, false)
        return ViewHolder(view)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.nameTextView
        val titleTextView: TextView = view.titleTextView
        val emailTextView: TextView = view.emailTextView
        val phoneTextView: TextView = view.phoneTextView
        val departmentTextView: TextView = view.departmentTextView
        val profileImageView: ImageView = view.imageView

        init {
            itemView.setOnClickListener {
                val intent = Intent(view.context, EmployeeActivity::class.java)
                intent.putExtra("employee",employees[adapterPosition].toString())
                view.context.startActivity(intent)
            }
        }
    }

    override fun onBindViewHolder(
        holder: EmployeesAdapter.ViewHolder,
        position: Int)
    {
        val employee: JSONObject = employees.getJSONObject(position)
        holder.nameTextView.text =
            employee["last_name"].toString() + " " + employee["first_name"].toString()
        holder.titleTextView.text = employee["title"].toString()
        holder.emailTextView.text = employee["email"].toString()
        holder.phoneTextView.text = employee["phone"].toString()
        holder.departmentTextView.text = employee["department"].toString()

        Glide.with(holder.profileImageView.context)
            .load(employee["image"].toString()).transform(RoundedCorners(R.dimen.corner_radius))
            .into(holder.profileImageView)
    }

    override fun getItemCount(): Int = employees.length()
}