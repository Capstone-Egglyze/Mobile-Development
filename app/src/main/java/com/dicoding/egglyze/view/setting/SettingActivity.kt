package com.dicoding.egglyze.view.setting

import android.content.res.Configuration
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.dicoding.egglyze.databinding.ActivitySettingBinding
import com.example.submissionevent.ui.setting.SettingPreferences
import com.example.submissionevent.ui.setting.SettingViewModel
import com.example.submissionevent.ui.setting.dataStore
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SettingActivity : AppCompatActivity() {
    private lateinit var settingViewModel: SettingViewModel

    private var _binding: ActivitySettingBinding? = null
    private val binding get() = _binding!!

    private var isChangingTheme = false
    private var currentThemeMode: Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = SettingPreferences.getInstance(this.dataStore)
        settingViewModel = ViewModelProvider(this, ViewModelFactory(pref)).get(SettingViewModel::class.java)

        // Check current system theme mode when the activity is created
        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        val isDarkModeActive = when (currentNightMode) {
            Configuration.UI_MODE_NIGHT_YES -> true
            Configuration.UI_MODE_NIGHT_NO -> false
            else -> null
        }

        // Set observer for theme setting
        settingViewModel.getThemeSettings().observe(this) { userPreference ->
            if (userPreference != currentThemeMode) {
                currentThemeMode = userPreference
                binding.switchTheme.isChecked = userPreference
            }
        }

        currentThemeMode = settingViewModel.getThemeSettings().value

        // Set listener for switch theme
        binding.switchTheme.setOnCheckedChangeListener { _, isChecked ->
            if (!isChangingTheme && isChecked != currentThemeMode) {
                isChangingTheme = true
                currentThemeMode = isChecked

                settingViewModel.saveThemeSetting(isChecked)

                // Using coroutine to add a slight delay
                lifecycleScope.launch {
                    delay(200) // debounce with 200 ms delay
                    AppCompatDelegate.setDefaultNightMode(
                        if (isChecked) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
                    )
                    isChangingTheme = false
                }
            }
        }

        // Check current theme and set switch accordingly
        binding.switchTheme.isChecked = (settingViewModel.getThemeSettings().value ?: isDarkModeActive) == true
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
