package com.example.todolistapp

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CadastroFragment : Fragment() {

    interface OnTarefaSalvaListener {
        fun onTarefaSalva(tarefa: Tarefa)
    }

    private var listener: OnTarefaSalvaListener? = null

    private lateinit var editTextTitulo: EditText
    private lateinit var editTextDescricao: EditText
    private lateinit var radioGroupCategoriaCol1: RadioGroup
    private lateinit var radioGroupCategoriaCol2: RadioGroup
    private lateinit var editTextDataFinalizacao: EditText
    private lateinit var buttonSalvarTarefa: Button

    private val calendar = Calendar.getInstance()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnTarefaSalvaListener) {
            listener = context
        } else {
            throw RuntimeException("$context deve implementar OnTarefaSalvaListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cadastro, container, false)

        editTextTitulo = view.findViewById(R.id.edit_text_titulo)
        editTextDescricao = view.findViewById(R.id.edit_text_descricao)
        radioGroupCategoriaCol1 = view.findViewById(R.id.radio_group_categoria_col1)
        radioGroupCategoriaCol2 = view.findViewById(R.id.radio_group_categoria_col2)
        editTextDataFinalizacao = view.findViewById(R.id.edit_text_data_finalizacao)
        buttonSalvarTarefa = view.findViewById(R.id.button_salvar_tarefa)

        editTextDataFinalizacao.setOnClickListener {
            showDatePickerDialog()
        }

        buttonSalvarTarefa.setOnClickListener {
            salvarTarefa()
        }

        // Listener para a primeira coluna: se algo for selecionado aqui, limpa a segunda coluna
        radioGroupCategoriaCol1.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId != -1) {
                radioGroupCategoriaCol2.clearCheck()
            }
        }

        // Listener para a segunda coluna: se algo for selecionado aqui, limpa a primeira coluna
        radioGroupCategoriaCol2.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId != -1) {
                radioGroupCategoriaCol1.clearCheck()
            }
        }

        return view
    }

    private fun showDatePickerDialog() {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                calendar.set(selectedYear, selectedMonth, selectedDayOfMonth)
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                editTextDataFinalizacao.setText(dateFormat.format(calendar.time))
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }

    private fun salvarTarefa() {
        val titulo = editTextTitulo.text.toString().trim()
        val descricao = editTextDescricao.text.toString().trim()
        var categoria = ""

        val selectedIdCol1 = radioGroupCategoriaCol1.checkedRadioButtonId
        if (selectedIdCol1 != -1) {
            val selectedRadioButton = requireView().findViewById<RadioButton>(selectedIdCol1)
            categoria = selectedRadioButton.text.toString()
        }

        if (categoria.isEmpty()) {
            val selectedIdCol2 = radioGroupCategoriaCol2.checkedRadioButtonId
            if (selectedIdCol2 != -1) {
                val selectedRadioButton = requireView().findViewById<RadioButton>(selectedIdCol2)
                categoria = selectedRadioButton.text.toString()
            }
        }

        val dataFinalizacao = editTextDataFinalizacao.text.toString().trim()

        if (titulo.isEmpty()) {
            Toast.makeText(requireContext(), "O título da tarefa não pode estar vazio!", Toast.LENGTH_SHORT).show()
            editTextTitulo.error = "Campo obrigatório"
            return
        }

        if (categoria.isEmpty()) {
            Toast.makeText(requireContext(), "Por favor, selecione uma categoria!", Toast.LENGTH_SHORT).show()
            return
        }

        val novaTarefa = Tarefa(
            titulo = titulo,
            descricao = descricao,
            categoria = categoria,
            dataFinalizacao = dataFinalizacao,
            prazo = "A ser calculado",
            isAtrasada = false,
            iconeStatusResId = 0
        )

        listener?.onTarefaSalva(novaTarefa)

        Toast.makeText(requireContext(), "Tarefa salva: ${novaTarefa.titulo}", Toast.LENGTH_LONG).show()

        editTextTitulo.text.clear()
        editTextDescricao.text.clear()
        radioGroupCategoriaCol1.clearCheck()
        radioGroupCategoriaCol2.clearCheck()
        editTextDataFinalizacao.text.clear()
    }
}