package pt.atp.cidadesinteligentes.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pt.atp.cidadesinteligentes.R
import pt.atp.cidadesinteligentes.dataclasses.Place

class LineAdapter(val list: ArrayList<Place>):RecyclerView.Adapter<LineViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LineViewHolder {

        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.recyclerline, parent, false);
        return LineViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: LineViewHolder, position: Int) {
        val currentPlace = list[position]

        holder.note.text = currentPlace.note
    }

}

class LineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

    val note = itemView.findViewById<TextView>(R.id.note)

}