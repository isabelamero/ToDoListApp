package com.example.todolistapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(), CadastroFragment.OnTarefaSalvaListener {

    private lateinit var bottomNavigationView: BottomNavigationView
    val listaDeTarefasGlobal = ArrayList<Tarefa>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigationView = findViewById(R.id.bottom_navigation)

        if (savedInstanceState == null) {
            loadFragment(HomeFragment())
        }

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    loadFragment(HomeFragment())
                    true
                }
                R.id.nav_cadastro -> {
                    loadFragment(CadastroFragment())
                    true
                }
                R.id.nav_busca -> {
                    loadFragment(BuscaFragment())
                    true
                }
                else -> false
            }
        }
    }

    fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    override fun onTarefaSalva(tarefa: Tarefa) {
        listaDeTarefasGlobal.add(tarefa)
        bottomNavigationView.selectedItemId = R.id.nav_home
    }
}