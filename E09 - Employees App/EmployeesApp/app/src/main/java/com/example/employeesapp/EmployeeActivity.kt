package com.example.employeesapp

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import kotlinx.android.synthetic.main.activity_employee.*
import org.json.JSONObject

class EmployeeActivity : AppCompatActivity() {

    // EmployeeActivity's onCreate
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_employee)

        val bundle: Bundle? = intent.extras
        if (bundle != null) {
            val employeeString = bundle.getString("employee")
            val employee = JSONObject(employeeString)
            val name = employee["last_name"].toString() + " " + employee["first_name"].toString()
            val title = employee["title"].toString()
            val email = employee["email"].toString()
            val phone = employee["phone"].toString()
            val department = employee["department"].toString()
            val image = employee["image"].toString()

            this.nameTextView.text = name
            this.titleTextView.text = title
            this.emailTextView.text = email
            this.phoneTextView.text = phone
            this.departmentTextView.text = department

            Glide.with(this)
                .load(image).transform(RoundedCorners(R.dimen.corner_radius))
                .into(profileImageView)

            this.sendEmailButton.setOnClickListener {
                val intent = Intent(Intent.ACTION_SENDTO).apply {
                    type = "*/*"
                    data = Uri.parse("mailto:$email")
                    putExtra(Intent.EXTRA_EMAIL, email)
                    putExtra(Intent.EXTRA_SUBJECT, "Message from employees App")
                }
                if (intent.resolveActivity(packageManager) != null) {
                        startActivity(intent)
                }
            }
        }
    }
}
