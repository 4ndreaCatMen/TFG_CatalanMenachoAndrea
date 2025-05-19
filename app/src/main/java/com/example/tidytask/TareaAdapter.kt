import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.tidytask.R
import com.example.tidytask.Tarea
import com.example.tidytask.database.TareaDBHelper

class TareaAdapter(
    private var listaTareas: List<Tarea>,
    private val onTareaClick: (Tarea) -> Unit,
    private val onTareaLongClick: (Tarea) -> Unit
) : RecyclerView.Adapter<TareaAdapter.TareaViewHolder>() {

    // Nueva lista para saber qué tareas están expandidas
    private val tareasExpandidas = mutableSetOf<Int>()

    class TareaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitulo: TextView = itemView.findViewById(R.id.tvTitulo)
        val tvDescripcion: TextView = itemView.findViewById(R.id.tvDescripcion)
        val tvPrioridad: TextView = itemView.findViewById(R.id.tvPrioridad)
        val tvHora: TextView = itemView.findViewById(R.id.tvHora)
        val tvFecha: TextView = itemView.findViewById(R.id.tvFecha)
        val checkBox: CheckBox = itemView.findViewById(R.id.checkBoxCompletada)
        val contenedorDetalles: View = itemView.findViewById(R.id.contenedorDetalles)
        val btnExpandir: View = itemView.findViewById(R.id.btnExpandir)
        val btnEliminar: ImageView = itemView.findViewById(R.id.btnEliminar)

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

        holder.checkBox.setOnCheckedChangeListener(null)
        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            val dbHelper = TareaDBHelper(holder.itemView.context)
            dbHelper.actualizarEstadoCompletada(tarea.idTarea, isChecked)
            aplicarEstiloTachado(holder, isChecked)

        }

        holder.btnEliminar.setOnClickListener {
            AlertDialog.Builder(holder.itemView.context)
                .setTitle("Eliminar tarea")
                .setMessage("¿Estás segura de que quieres eliminar esta tarea?")
                .setPositiveButton("Sí") { _, _ ->
                    val dbHelper = TareaDBHelper(holder.itemView.context)
                    val eliminada = dbHelper.eliminarTareaPorId(tarea.idTarea)
                    if (eliminada) {
                        listaTareas = listaTareas.toMutableList().apply { removeAt(position) }
                        notifyItemRemoved(position)
                        Toast.makeText(holder.itemView.context, "Tarea eliminada", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(holder.itemView.context, "Error al eliminar", Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }

        // Expandir/colapsar detalles
        val estaExpandida = tareasExpandidas.contains(tarea.idTarea)
        holder.contenedorDetalles.visibility = if (estaExpandida) View.VISIBLE else View.GONE
        holder.btnExpandir.setBackgroundResource(
            if (estaExpandida) android.R.drawable.arrow_up_float
            else android.R.drawable.arrow_down_float
        )

        holder.btnExpandir.setOnClickListener {
            if (tareasExpandidas.contains(tarea.idTarea)) {
                tareasExpandidas.remove(tarea.idTarea)
            } else {
                tareasExpandidas.add(tarea.idTarea)
            }
            notifyItemChanged(position)
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
    fun actualizarLista(nuevaLista: List<Tarea>) {
        listaTareas = nuevaLista
        notifyDataSetChanged()
    }

}
