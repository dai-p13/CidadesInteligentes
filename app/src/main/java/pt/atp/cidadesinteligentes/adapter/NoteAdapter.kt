package pt.atp.cidadesinteligentes.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pt.atp.cidadesinteligentes.R
import pt.atp.cidadesinteligentes.ententies.Notes

class NoteAdapter internal constructor(
    context: Context
) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var notes = emptyList<Notes>() // Cached copy of cities

    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val noteItemView: TextView = itemView.findViewById(R.id.note)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val itemView = inflater.inflate(R.layout.recyclerline, parent, false)
        return NoteViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val current = notes[position]
        holder.noteItemView.text = current.title
    }

    internal fun setNotes(notes: List<Notes>) {
        this.notes = notes
        notifyDataSetChanged()
    }

    override fun getItemCount() = notes.size
}