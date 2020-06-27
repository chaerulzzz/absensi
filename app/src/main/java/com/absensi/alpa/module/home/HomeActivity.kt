package com.absensi.alpa.module.home

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.absensi.alpa.R
import com.absensi.alpa.module.profile.ProfileActivity
import com.absensi.alpa.module.request.RequestActivity

import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_home)

        rvApproval.setOnClickListener {
            Toast.makeText(this, "Buka Halaman Approval", Toast.LENGTH_SHORT).show()
        }

        rvAttendance.setOnClickListener {
            Toast.makeText(this, "Buka Halaman Attendance", Toast.LENGTH_SHORT).show()
        }

        rvProfile.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        rvRequest.setOnClickListener {
            startActivity(Intent(this, RequestActivity::class.java))
        }
    }
}