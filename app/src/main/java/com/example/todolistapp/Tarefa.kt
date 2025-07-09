package com.example.todolistapp

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Tarefa(
    val titulo: String,
    val descricao: String,
    val categoria: String,
    val dataFinalizacao: String,
    var prazo: String,
    var isAtrasada: Boolean = false,
    var iconeStatusResId: Int = 0
) : Parcelable {


    override fun toString(): String {
        return "Título: $titulo\n" +
                "Descrição: $descricao\n" +
                "Categoria: $categoria\n" +
                "Data Finalização: $dataFinalizacao\n" +
                "Prazo: $prazo\n" +
                "Status: ${if (isAtrasada) "Atrasada" else "Em Dia"}"
    }
}