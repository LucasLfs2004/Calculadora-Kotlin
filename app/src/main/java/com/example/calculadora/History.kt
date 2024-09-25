package com.example.calculadora

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.calculadora.model.Operacao
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class HistoryActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var clearHistoryButton: Button
    private lateinit var textHistorico: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        loadTheme()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)



        supportActionBar?.setDisplayHomeAsUpEnabled(true) // Mostra o botão de voltar
        supportActionBar?.setHomeButtonEnabled(true)

        clearHistoryButton = findViewById(R.id.clearHistory) // Substitua pelo ID correto do botão

        // Definindo o listener para o botão
        clearHistoryButton.setOnClickListener {
            clearHistory()
            val historico = carregarHistorico(this)
            mostrarHistorico(historico)
        }


        // Acessa o SharedPreferences diretamente no onCreate, já com o contexto da Activity
        val historico = carregarHistorico(this)
        mostrarHistorico(historico)

    }

    private fun clearHistory() {
        val sharedPreferences = getSharedPreferences("historico", MODE_PRIVATE)

        with(sharedPreferences.edit()) {
            clear()
            apply()
        }

        Toast.makeText(this, "Histórico limpo!", Toast.LENGTH_SHORT).show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> { // O id do botão de voltar
                onBackPressed() // Navega de volta
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
    private fun carregarHistorico(context: Context): List<Operacao> {
        return try {
            sharedPreferences = context.getSharedPreferences("historico", Context.MODE_PRIVATE)
            val gson = Gson()
            val json = sharedPreferences.getString("historico_operacoes", "")
            println("json: " + json)
            val type = object : TypeToken<MutableList<Operacao>>() {}.type
            return gson.fromJson(json, type) ?: emptyList()
        } catch (e: Exception) {
            println("Error ao carregar histórico: $e")
            return emptyList()
        }
    }

    private fun mostrarHistorico(historico: List<Operacao>) {

            val recyclerView: RecyclerView = findViewById(R.id.rvOperations)
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = OperationAdapter(historico)
        if (historico.size == 0) {
            textHistorico = findViewById(R.id.TextHistory)
            textHistorico.text = "Nenhuma operação no histórico no momento"
        }

    }

    private fun loadTheme() {
        sharedPreferences = getSharedPreferences("theme_pref", MODE_PRIVATE)
        val isDarkTheme = sharedPreferences.getBoolean("dark_theme", false)

        if (isDarkTheme) {
            setTheme(R.style.AppTheme_Dark)
        } else {
            setTheme(R.style.AppTheme_Light)
        }
    }
}
