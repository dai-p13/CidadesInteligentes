package pt.atp.cidadesinteligentes.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pt.atp.cidadesinteligentes.R
import pt.atp.cidadesinteligentes.api.Ocorrencia

class OcorrenciaAdapter(val ocorrencias: List<Ocorrencia>): RecyclerView.Adapter<OcorenciasViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OcorenciasViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.ocorrenciarecyclerline, parent, false)
        return OcorenciasViewHolder(view)
    }

    override fun getItemCount(): Int {
        return ocorrencias.size
    }

    override fun onBindViewHolder(holder: OcorenciasViewHolder, position: Int) {
        return holder.bind(ocorrencias[position])
    }

}

class OcorenciasViewHolder(itemView : View): RecyclerView.ViewHolder(itemView){
    private val titulo: TextView = itemView.findViewById(R.id.ocorrtit)
    private val descricao:TextView = itemView.findViewById(R.id.ocorrdesc)


    fun bind(ocorrencia: Ocorrencia) {
        titulo.text = ocorrencia.titulo
        descricao.text = ocorrencia.descricao

    }

}