package com.skinective.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.skinective.R
import com.skinective.network.responses.Hospital

class HospitalAdapter(private val hospitals: List<Hospital>) :
    RecyclerView.Adapter<HospitalAdapter.HospitalViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HospitalViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.hospital_item, parent, false)
        return HospitalViewHolder(view)
    }

    override fun onBindViewHolder(holder: HospitalViewHolder, position: Int) {
        val hospital = hospitals[position]
        holder.tvName.text = hospital.name
        holder.tvAddress.text = hospital.address
        holder.tvTime.text = hospital.travelTime
        holder.tvDistance.text = hospital.distance
    }

    override fun getItemCount(): Int = hospitals.size

    class HospitalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tv_hospital_name)
        val tvAddress: TextView = itemView.findViewById(R.id.tv_hospital_address)
        val tvTime: TextView = itemView.findViewById(R.id.tv_hospital_time)
        val tvDistance: TextView = itemView.findViewById(R.id.tv_hospital_distance)
    }
}