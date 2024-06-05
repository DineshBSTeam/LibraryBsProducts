package com.lib.bslibrary

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.lib.bslibrary.databinding.LauncherBanksathiBinding


class BankSathiLauncher : AppCompatActivity() {

    private lateinit var binding: LauncherBanksathiBinding
    private lateinit var navView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = LauncherBanksathiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        navView.setupWithNavController(navController)

    }

    fun selectHomeTab() {
        val view: View = navView.findViewById(R.id.navigation_home)
        view.performClick()
    }
}