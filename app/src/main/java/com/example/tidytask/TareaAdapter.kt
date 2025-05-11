package com.example.tidytask

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tidytask.database.TareaDBHelper

class TareaAdapter(
    private var listaTareas: List<Tarea>,
    private val onTareaClick: (Tarea) -> Unit,
    private val onTareaLongClick: (Tarea) -> Unit
) : RecyclerView.Adapter<TareaAdapter.TareaViewHolder>() {

    class TareaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitulo: TextView = itemView.findViewById(R.id.tvTitulo)
        val tvDescripcion: TextView = itemView.findViewById(R.id.tvDescripcion)
        val tvPrioridad: TextView = itemView.findViewById(R.id.tvPrioridad)
        val tvHora: TextView = itemView.findViewById(R.id.tvHora)
        val tvFecha: TextView = itemView.findViewById(R.id.tvFecha)
        val checkBox: CheckBox = itemView.findViewById(R.id.checkBoxCompletada)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TareaViewHolder {
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.item_tarea, parent, false)
        return TareaViewHolder(vista)
    }

    override fun onBindViewHolder(holder: TareaViewHolder, position: Int) {
        val tarea = listaTareas[position]

        holder.tvTitulo.text = tarea.titulo
        holder.tvDescripcion.text = tarea.descripcion
        holder.tvPrioridad.text = "Prioridad: ${tarea.prioridad}"
        holder.tvHora.text = "Hora: ${tarea.hora}"
        holder.tvFecha.text = "Fecha: ${tarea.fecha}"
        holder.checkBox.isChecked = tarea.completada

        aplicarEstiloTachado(holder, tarea.completada)

        holder.checkBox.setOnCheckedChangeListener(null)  // Evita bugs al reciclar la vista
        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            val dbHelper = TareaDBHelper(holder.itemView.context)
            dbHelper.actualizarEstadoCompletada(tarea.idTarea, isChecked)
            tarea.completada = isChecked
            aplicarEstiloTachado(holder, isChecked)
        }

        holder.itemView.setOnClickListener {
            onTareaClick(tarea)
        }

        holder.itemView.setOnLongClickListener {
            onTareaLongClick(tarea)
            true
        }
    }

    override fun getItemCount(): Int = listaTareas.size

    private fun aplicarEstiloTachado(holder: TareaViewHolder, completada: Boolean) {
        if (completada) {
            holder.tvTitulo.paintFlags = holder.tvTitulo.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            holder.tvTitulo.paintFlags = holder.tvTitulo.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }
    }
}
