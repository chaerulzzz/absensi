package com.absensi.alpa.module.request

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.absensi.alpa.R

import kotlinx.android.synthetic.main.activity_request.*;

class RequestActivity : AppCompatActivity() {

    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: RequestItemAdapter
    private var requests: ArrayList<Request> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request)
        supportActionBar?.hide()

        this.setData()
        this.init()
    }

    private fun init() {
        linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = adapter
    }

    private fun setData(){
        val request1 = Request("Pengajuan Sakit", "Disetujui","Periode: 25 Juni 2020 - 28 Juni 2020")
        requests.add(request1)

        val request2 = Request("Pengajuan Izin", "Disetujui","Periode: 22 Juni 2020 - 23 Juni 2020")
        requests.add(request2)

        val request3 = Request("Pengajuan Cuti", "Disetujui","Periode: 17 Juni 2020 - 18 Juni 2020")
        requests.add(request3)

        adapter = RequestItemAdapter(requests)
    }

    override fun onPause() {
        super.onPause()

        requests.clear()
    }
}