package com.example.todolistapp

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import android.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HomeFragment : Fragment() {

    private lateinit var searchView: SearchView
    private lateinit var recyclerView: RecyclerView
    private lateinit var tarefaAdapter: TarefaAdapter
    private lateinit var listaDeTarefas: ArrayList<Tarefa>
    private lateinit var themeSwitch: SwitchCompat

    private val PREFS_NAME = "MyThemePrefs" // NOVO
    private val THEME_KEY = "isNightMode" // NOVO
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        searchView = view.findViewById(R.id.search_view_home)
        recyclerView = view.findViewById(R.id.recycler_view_home)
        themeSwitch = view.findViewById(R.id.theme_switch)

        if (activity is MainActivity) {
            listaDeTarefas = (activity as MainActivity).listaDeTarefasGlobal
        } else {
            listaDeTarefas = ArrayList<Tarefa>()
            Toast.makeText(requireContext(), "Erro: Não foi possível obter a lista de tarefas.", Toast.LENGTH_LONG).show()
        }

        if (listaDeTarefas.isEmpty()) {
            popularListaDeTarefasInicial()
        }

        loadThemePreference()
        setupThemeSwitchListener()
        configurarRecyclerView()
        configurarSearchView()

        return view
    }

    private fun popularListaDeTarefasInicial() {
        // Tarefas iniciais com categorias de desenvolvimento
        listaDeTarefas.add(Tarefa("Avaliar Jonathas Leontino", "Descer a lenha no Jonathas Leontino Medina na avaliação docente", "Documentação", "18/07/2025", "5 dias", false, R.drawable.ic_check_circle_green_24dp))
        listaDeTarefas.add(Tarefa("Implementar Login OAuth", "Desenvolver autenticação OAuth 2.0 no backend", "BackEnd", "10/07/2025", "0 dias", true, R.drawable.ic_warning_yellow_24dp))
        listaDeTarefas.add(Tarefa("Criar Componente de Botão", "Desenvolver um componente reutilizável de botão em React", "FrontEnd", "25/07/2025", "12 dias", false, R.drawable.ic_check_circle_green_24dp))
        listaDeTarefas.add(Tarefa("Otimizar Consulta SQL", "Analisar e otimizar query lenta no banco de dados", "Banco de Dados", "05/07/2025", "Atrasado", true, R.drawable.ic_warning_yellow_24dp))
        listaDeTarefas.add(Tarefa("Planejar Sprint 3", "Definir tarefas e estimativas para a próxima sprint", "Desenvolvimento", "11/07/2025", "1 dia", false, R.drawable.ic_check_circle_green_24dp))
    }

    private fun configurarRecyclerView() {
        tarefaAdapter = TarefaAdapter(listaDeTarefas, object : TarefaAdapter.OnItemClickListener {
            override fun onItemClick(tarefa: Tarefa, position: Int) {
                excluirTarefa(tarefa, position)
            }

            override fun onDeleteClick(tarefa: Tarefa, position: Int) {
                excluirTarefa(tarefa, position)
            }
        })

        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = tarefaAdapter
        }
    }

    private fun configurarSearchView() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                tarefaAdapter.filtrar(newText.orEmpty())
                return false
            }
        })
    }

    private fun excluirTarefa(tarefa: Tarefa, position: Int) {
        AlertDialog.Builder(requireContext())
            .setTitle("Confirmar Exclusão")
            .setMessage("Tem certeza que deseja excluir a tarefa '${tarefa.titulo}'?")
            .setPositiveButton("Sim") { dialog, which ->
                (activity as MainActivity).listaDeTarefasGlobal.remove(tarefa)

                tarefaAdapter.notifyItemRemoved(position)
                tarefaAdapter.notifyItemRangeChanged(position, listaDeTarefas.size)
                tarefaAdapter.sincronizarListaOriginal()

                Toast.makeText(requireContext(), "Tarefa '${tarefa.titulo}' excluída!", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Não") { dialog, which ->
                Toast.makeText(requireContext(), "Exclusão cancelada.", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
            .show()
    }
    private fun loadThemePreference() {

        val prefs = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val isNightMode = prefs.getBoolean(THEME_KEY, false)

        themeSwitch.isChecked = isNightMode

        applyTheme(isNightMode)
    }


    private fun setupThemeSwitchListener() {
        themeSwitch.setOnCheckedChangeListener { _, isChecked ->
            applyTheme(isChecked)
            saveThemePreference(isChecked)
        }
    }


    private fun applyTheme(isNightMode: Boolean) {
        if (isNightMode) {
            // Define o tema para modo noturno (escuro)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            // Define o tema para modo diurno (claro)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

    }


    private fun saveThemePreference(isNightMode: Boolean) {

        val prefs = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = prefs.edit()

        editor.putBoolean(THEME_KEY, isNightMode)
        editor.apply()
    }

}