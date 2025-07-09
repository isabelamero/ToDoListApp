package com.example.todolistapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast // Para mensagens de feedback, se necessário
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class BuscaFragment : Fragment() {

    private lateinit var searchView: SearchView
    private lateinit var recyclerView: RecyclerView
    private lateinit var categoriaSumarioAdapter: CategoriaSumarioAdapter
    private lateinit var listaTarefasGlobal: ArrayList<Tarefa> // Referência à lista global da MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_busca, container, false)

        searchView = view.findViewById(R.id.search_view_busca)
        recyclerView = view.findViewById(R.id.recycler_view_busca_categorias)

        // Obter a lista global de tarefas da MainActivity
        if (activity is MainActivity) {
            listaTarefasGlobal = (activity as MainActivity).listaDeTarefasGlobal
        } else {
            listaTarefasGlobal = ArrayList<Tarefa>() // Lista vazia em caso de erro
            Toast.makeText(requireContext(), "Erro: Não foi possível obter a lista de tarefas globais.", Toast.LENGTH_LONG).show()
        }

        // Agrupar e exibir os sumários das categorias
        exibirSumarioPorCategoria()

        // Configurar a SearchView (se for filtrar categorias, a lógica iria aqui)
        configurarSearchView()

        return view
    }

    private fun exibirSumarioPorCategoria() {
        // Agrupa as tarefas pela categoria e conta o total de tarefas em cada grupo
        val sumariosAgrupados = listaTarefasGlobal
            .groupBy { it.categoria } // Agrupa pelo campo 'categoria'
            .map { (categoria, tarefas) -> // Transforma o mapa em uma lista de CategoriaSumario
                CategoriaSumario(categoria, tarefas.size)
            }
            .sortedBy { it.nomeCategoria } // Opcional: Ordena por nome da categoria

        // Converter a lista de Map.Entry para ArrayList<CategoriaSumario> para o Adapter
        val listaDeSumarios = ArrayList(sumariosAgrupados)

        // Configurar o RecyclerView com o adaptador de sumários
        categoriaSumarioAdapter = CategoriaSumarioAdapter(listaDeSumarios)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = categoriaSumarioAdapter
        }
    }

    private fun configurarSearchView() {
        // Por enquanto, o SearchView aqui não faz nada, pois o requisito principal é o agrupamento.
        // Se a busca na tela de busca fosse para filtrar as categorias exibidas, a lógica seria aqui.
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Implementar lógica de busca para categorias aqui, se necessário
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Implementar lógica de filtragem em tempo real das categorias, se necessário
                // Por exemplo, filtrar a listaDeSumarios antes de passá-la ao adapter
                return false
            }
        })
    }
}