package com.absensi.alpa.module.request

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.absensi.alpa.R
import kotlinx.android.synthetic.main.item_request.view.*

class RequestItemAdapter(private val requests: ArrayList<Request>) : RecyclerView.Adapter<RequestItemAdapter.RequestHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestHolder {
        return RequestHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_request, parent, false))
    }

    override fun getItemCount(): Int = requests.size

    override fun onBindViewHolder(holder: RequestHolder, position: Int) {
        val request: Request = requests[position]
        holder.bind(request)
    }

    class RequestHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private var view: View = itemView
        private var request: Request? = null

        companion object{
            private val REQUEST_KEY = "REQUEST"
        }

        fun bind(request: Request) {
            this.request = request
            view.tvType.text = request.requestName
            view.tvStatus.text = request.requestStatus
            view.tvPeriod.text = request.requestPeriod
        }
    }
}