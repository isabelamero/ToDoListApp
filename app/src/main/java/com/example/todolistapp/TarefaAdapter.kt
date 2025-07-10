package com.example.todolistapp

import android.util.TypedValue
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class TarefaAdapter(
    private val listaTarefas: ArrayList<Tarefa>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<TarefaAdapter.TarefaViewHolder>() {

    private var listaOriginal: MutableList<Tarefa> = listaTarefas.toMutableList()

    interface OnItemClickListener {
        fun onItemClick(tarefa: Tarefa, position: Int)
        fun onDeleteClick(tarefa: Tarefa, position: Int)
    }

    inner class TarefaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titulo: TextView = itemView.findViewById(R.id.text_view_titulo)
        val descricao: TextView = itemView.findViewById(R.id.text_view_descricao)
        val categoria: TextView = itemView.findViewById(R.id.text_view_categoria)
        val prazo: TextView = itemView.findViewById(R.id.text_view_prazo)
        val statusIcon: ImageView = itemView.findViewById(R.id.image_view_status)
        val deleteIcon: ImageView = itemView.findViewById(R.id.image_view_delete)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(listaTarefas[position], position)
                }
            }

            deleteIcon.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onDeleteClick(listaTarefas[position], position)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TarefaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_tarefa, parent, false)
        return TarefaViewHolder(view)
    }

    override fun onBindViewHolder(holder: TarefaViewHolder, position: Int) {
        val tarefa = listaTarefas[position]

        holder.titulo.text = tarefa.titulo
        holder.descricao.text = tarefa.descricao
        holder.categoria.text = "Categoria: ${tarefa.categoria}"

        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        try {
            val dataVencimento = dateFormat.parse(tarefa.dataFinalizacao)
            val hoje = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }.time

            if (dataVencimento != null) {
                val diffInMillis = dataVencimento.time - hoje.time
                val diffInDays = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS)

                tarefa.isAtrasada = diffInDays < 0
                tarefa.prazo = when {
                    diffInDays == 0L -> "Hoje"
                    diffInDays == 1L -> "1 dia restante"
                    diffInDays > 1L -> "$diffInDays dias restantes"
                    else -> "Atrasado" // diffInDays < 0
                }

                holder.prazo.text = "Prazo: ${tarefa.prazo}"

                if (tarefa.isAtrasada) {
                    holder.prazo.setTextColor(Color.RED) // Mantém o vermelho para atrasado
                    holder.statusIcon.setImageResource(R.drawable.ic_warning_yellow_24dp)
                } else {
                    // Para prazo normal, use a cor de texto padrão do tema (colorOnSurface, que será branco no escuro e preto no claro)
                    val typedValue = TypedValue()
                    holder.prazo.context.theme.resolveAttribute(com.google.android.material.R.attr.colorOnSurface, typedValue, true) // OU com.google.android.material.R.attr.colorOnSurface
                    holder.prazo.setTextColor(typedValue.data)

                    holder.statusIcon.setImageResource(R.drawable.ic_check_circle_green_24dp)
                }
            } else {
                holder.prazo.text = "Prazo: Data Inválida"
                holder.prazo.setTextColor(Color.GRAY) // Pode ser Color.RED ou outra cor de erro
                holder.statusIcon.setImageResource(R.drawable.ic_warning_yellow_24dp)
            }
        } catch (e: Exception) {
            holder.prazo.text = "Prazo: Erro de Formato"
            holder.prazo.setTextColor(Color.GRAY) // Pode ser Color.RED ou outra cor de erro
            holder.statusIcon.setImageResource(R.drawable.ic_warning_yellow_24dp)
        }

        holder.deleteIcon.setImageResource(R.drawable.ic_delete_black_24dp)
    }

    override fun getItemCount(): Int {
        return listaTarefas.size
    }

    fun filtrar(query: String) {
        listaTarefas.clear()

        if (query.isEmpty()) {
            listaTarefas.addAll(listaOriginal)
        } else {
            val queryLower = query.lowercase(Locale.getDefault())

            for (tarefa in listaOriginal) {
                if (tarefa.titulo.lowercase(Locale.getDefault()).contains(queryLower) ||
                    tarefa.descricao.lowercase(Locale.getDefault()).contains(queryLower) ||
                    tarefa.categoria.lowercase(Locale.getDefault()).contains(queryLower)) {
                    listaTarefas.add(tarefa)
                }
            }
        }
        notifyDataSetChanged()
    }

    fun sincronizarListaOriginal() {
        listaOriginal.clear()
        listaOriginal.addAll(listaTarefas)
    }
}