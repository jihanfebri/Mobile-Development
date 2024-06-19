package com.skinective.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.skinective.R

class WelcomePagerAdapter(private val activity: AppCompatActivity) : RecyclerView.Adapter<WelcomePagerAdapter.WelcomeViewHolder>() {

    private val layouts = intArrayOf(
        R.layout.activity_welcomescreen1,
        R.layout.activity_welcomescreen2,
        R.layout.activity_welcomescreen3
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WelcomeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return WelcomeViewHolder(view)
    }

    override fun onBindViewHolder(holder: WelcomeViewHolder, position: Int) {
        // Bind view data here if needed
    }

    override fun getItemViewType(position: Int): Int {
        return layouts[position]
    }

    override fun getItemCount(): Int {
        return layouts.size
    }

    inner class WelcomeViewHolder(view: View) : RecyclerView.ViewHolder(view)
}