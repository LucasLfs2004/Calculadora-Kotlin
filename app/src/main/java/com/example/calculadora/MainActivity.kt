package com.example.calculadora

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import com.example.calculadora.model.Operacao
import com.example.calculadora.utils.formatNumber
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {

    lateinit var themeSwitch: SwitchCompat
    lateinit var sharedPreferences: SharedPreferences

    private lateinit var inputValue1: EditText
    private lateinit var inputValue2: EditText
    private lateinit var btnPlus: Button
    private lateinit var btnSub: Button
    private lateinit var btnMult: Button
    private lateinit var btnDiv: Button
    private lateinit var btnResult: Button
    private lateinit var operationTextView: TextView
    private lateinit var resultTextView: TextView
    private lateinit var btnHistory: Button

    private var selectedOperation: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        // Carregue o tema atual antes da criação da activity
        loadTheme()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        themeSwitch = findViewById(R.id.themeSwitch)
        sharedPreferences = getSharedPreferences("theme_pref", MODE_PRIVATE)

        // Definir o estado inicial do switch
        themeSwitch.isChecked = sharedPreferences.getBoolean("dark_theme", false)

        // Listener para o switch
        themeSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                setTheme(R.style.AppTheme_Dark)
            } else {
                setTheme(R.style.AppTheme_Light)
            }
            with(sharedPreferences.edit()) {
                putBoolean("dark_theme", isChecked)
                apply()
            }
            recreate()
        }

        inputValue1 = findViewById(R.id.InputValue1)
        inputValue2 = findViewById(R.id.InputValue2)
        btnPlus = findViewById(R.id.btnPlus)
        btnSub = findViewById(R.id.btnSub)
        btnMult = findViewById(R.id.btnMult)
        btnDiv = findViewById(R.id.btnDiv)
        btnResult = findViewById(R.id.btnResult)
        operationTextView = findViewById(R.id.OperationTextView)
        resultTextView = findViewById(R.id.TextResult)
        btnHistory = findViewById(R.id.BtnHistory)

        btnPlus.setOnClickListener {
            selectedOperation = "+"
            updateOperationText("Adição")
        }
        btnSub.setOnClickListener {
            selectedOperation = "-"
            updateOperationText("Subtração")
        }
        btnMult.setOnClickListener {
            selectedOperation = "*"
            updateOperationText("Multiplicação")
        }
        btnDiv.setOnClickListener {
            selectedOperation = "÷"
            updateOperationText("Divisão")
        }

        btnResult.setOnClickListener {
            var retorno = calculateResult()
            if (retorno != null) {
                var dados = Operacao(
                    retorno.id,
                    retorno.operation,
                    retorno.value1,
                    retorno.value2,
                    retorno.result,
                    retorno.date
                )
                salvarOperacao(this, dados)
            }
        }

        btnHistory.setOnClickListener {
            val intent = Intent(this, HistoryActivity::class.java)
            startActivity(intent)
        }
    }

    private fun updateOperationText(text: String = "") {
        operationTextView.text = "Operação: $text"
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


    private fun calculateResult(): Operacao? {
        val value1 = inputValue1.text.toString().toDoubleOrNull()
        val value2 = inputValue2.text.toString().toDoubleOrNull()

        if (value1 == null || value2 == null) {
            Toast.makeText(this, "Por favor, insira valores válidos", Toast.LENGTH_SHORT).show()
            return null
        }

        val result = when (selectedOperation) {
            "+" -> value1 + value2
            "-" -> value1 - value2
            "*" -> value1 * value2
            "÷" -> {
                if (value2 == 0.0) {
                    Toast.makeText(this, "Divisão por zero não permitida", Toast.LENGTH_SHORT)
                        .show()
                    return null
                } else {
                    value1 / value2
                }
            }

            else -> {
                Toast.makeText(this, "Por favor, selecione uma operação", Toast.LENGTH_SHORT).show()
                return null
            }
        }

        fun updateResultText(text: String) {
            resultTextView.text = "$text"
        }

        updateResultText(formatNumber(result))
        inputValue1.text = null
        inputValue2.text = null
        operationTextView.text = "Operação:"

        Toast.makeText(this, "Cálculo armazenado com sucesso", Toast.LENGTH_SHORT).show()
        return Operacao(
            System.currentTimeMillis(),
            selectedOperation,
            value1,
            value2,
            result,
            SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(
                Date()
            )
        )
        selectedOperation = ""
    }

    fun salvarOperacao(context: Context, operacao: Operacao) {
        sharedPreferences = context.getSharedPreferences("historico", MODE_PRIVATE)

        val gson = Gson()
        val json = sharedPreferences.getString("historico_operacoes", null)

        val type = object : TypeToken<MutableList<Operacao>>() {}.type
        var listaHistorico: MutableList<Operacao> = gson.fromJson(json, type) ?: mutableListOf()

        listaHistorico.add(operacao)

        val editor = sharedPreferences.edit()
        val jsonAtualizado = gson.toJson(listaHistorico)
        editor.putString("historico_operacoes", jsonAtualizado)
        editor.apply()
    }
}

