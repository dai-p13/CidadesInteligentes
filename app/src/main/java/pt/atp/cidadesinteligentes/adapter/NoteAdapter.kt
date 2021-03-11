package pt.atp.cidadesinteligentes.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pt.atp.cidadesinteligentes.EditNota
import pt.atp.cidadesinteligentes.R
import pt.atp.cidadesinteligentes.ententies.Notes


const val TITULO="TITULO"
const val DESCRICAO="DESCRICAO"
const val ID="ID"

class NoteAdapter internal constructor(
    context: Context
) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var notes = emptyList<Notes>() // Cached copy of cities
    //private val callbackInterface:CallbackInterface
    interface CallbackInterface {
        fun passResultCallback(id: Int?)
    }

    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val noteItemView: TextView = itemView.findViewById(R.id.note)
        val descr: TextView = itemView.findViewById(R.id.note2)
        val edit: ImageButton = itemView.findViewById(R.id.edit)
        val delete: ImageButton = itemView.findViewById(R.id.delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val itemView = inflater.inflate(R.layout.recyclerline, parent, false)
        return NoteViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val current = notes[position]
        holder.noteItemView.text = current.title
        holder.descr.text = current.description
        val id: Int? = current.id

        /*holder.delete.setOnClickListener {
            callbackInterface.passResultCallback(current.id)
        }*/

        holder.edit.setOnClickListener {
            val context = holder.noteItemView.context
            val titl = holder.noteItemView.text.toString()
            val desc = holder.descr.text.toString()

            val intent = Intent(context, EditNota::class.java).apply {
                putExtra(TITULO, titl)
                putExtra(DESCRICAO, desc )
                putExtra( ID,id)
            }
            context.startActivity(intent)
        }
    }

    internal fun setNotes(notes: List<Notes>) {
        this.notes = notes
        notifyDataSetChanged()
    }

    override fun getItemCount() = notes.size
}