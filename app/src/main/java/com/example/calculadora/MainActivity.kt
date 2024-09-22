package com.example.calculadora

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.SwitchCompat
import android.content.SharedPreferences

class MainActivity : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//    }


    lateinit var themeSwitch: SwitchCompat
    lateinit var sharedPreferences: SharedPreferences

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
            // Salvar preferência
            with(sharedPreferences.edit()) {
                putBoolean("dark_theme", isChecked)
                apply()
            }
            // Reinicialize a activity para aplicar o tema
            recreate()
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