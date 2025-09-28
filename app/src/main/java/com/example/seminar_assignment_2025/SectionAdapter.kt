package com.example.seminar_assignment_2025

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SectionAdapter(private val sections: List<Section>) :
    RecyclerView.Adapter<SectionAdapter.SectionViewHolder>() {

    class SectionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val subtitle: TextView = itemView.findViewById(R.id.itemSubtitle)
        val content: TextView = itemView.findViewById(R.id.itemContent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SectionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_section, parent, false)
        return SectionViewHolder(view)
    }

    override fun onBindViewHolder(holder: SectionViewHolder, position: Int) {
        val section = sections[position]
        holder.subtitle.text = section.subtitle
        holder.content.text = section.content

        holder.subtitle.setTextColor(Color.parseColor("#777777"))
        holder.content.setTextColor(if (section.url != null) Color.parseColor("#5495ff") else Color.parseColor("#777777"))

        holder.itemView.setOnClickListener {
            section.url?.let { link ->
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                holder.itemView.context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int = sections.size
}