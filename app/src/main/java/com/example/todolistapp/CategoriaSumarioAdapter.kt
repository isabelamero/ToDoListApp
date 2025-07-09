package com.example.todolistapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CategoriaSumarioAdapter(
    private val listaSumarios: ArrayList<CategoriaSumario>
) : RecyclerView.Adapter<CategoriaSumarioAdapter.SumarioViewHolder>() { // Esta linha estava correta!

    // ViewHolder: representa um item da lista (sumário de categoria)
    class SumarioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nomeCategoria: TextView = itemView.findViewById(R.id.text_view_nome_categoria)
        val totalTarefas: TextView = itemView.findViewById(R.id.text_view_total_tarefas)
    }

    // Cria novos ViewHolders
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SumarioViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_categoria_sumario, parent, false)
        return SumarioViewHolder(view)
    }

    // Vincula dados a um ViewHolder existente
    // AQUI ESTAVA O ERRO! Era 'SumarioAdapter.SumarioViewHolder'
    override fun onBindViewHolder(holder: SumarioViewHolder, position: Int) { // CORRIGIDO!
        val sumario = listaSumarios[position]
        holder.nomeCategoria.text = "Categoria: ${sumario.nomeCategoria}"
        holder.totalTarefas.text = "Total: ${sumario.totalTarefas}"
    }

    // Retorna o número total de itens (sumários de categoria)
    override fun getItemCount(): Int {
        return listaSumarios.size
    }
}