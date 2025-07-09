package com.example.todolistapp

data class CategoriaSumario(
    val nomeCategoria: String,
    val totalTarefas: Int
) {
    override fun toString(): String {
        return "Categoria: $nomeCategoria\nTotal: $totalTarefas"
    }
}