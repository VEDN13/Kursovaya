package com.example.myapplication

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.myapplication.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    // Регистрация пермишна для уведомлений (Android 13+)
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            sendNotification("Спасибо!", "Теперь мы можем вам писать!)")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Инициализация binding и NavController
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navController = findNavController(R.id.nav_host_fragment_activity_main)

        val navView: BottomNavigationView = binding.navView

        // Установка `AppBarConfiguration` и `BottomNavigationView`
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.HomeFragmentTitles,
                R.id.navigation_dashboard,
                R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // Создание канала уведомлений
        createNotificationChannel()

        // Проверка разрешения на уведомления для Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        // Скрытие/отображение `BottomNavigationView` при смене фрагментов
        setupNavVisibility(navController, navView)
    }

    // Логика скрытия/показа `BottomNavigationView`
    private fun setupNavVisibility(navController: NavController, navView: BottomNavigationView) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.HomeFragmentTitles,
                R.id.navigation_dashboard,
                R.id.navigation_notifications -> navView.visibility = View.VISIBLE
                else -> navView.visibility = View.GONE
            }
        }
    }

    // Создание канала уведомлений для Android 8.0+
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "default_channel",
                "Default Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "This is the default notification channel"
            }
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    // Отправка уведомления
    private fun sendNotification(title: String, content: String) {
        val notificationManager = getSystemService(NotificationManager::class.java)
        val notification = NotificationCompat.Builder(this, "default_channel")
            .setSmallIcon(R.drawable.frame_1)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()
        notificationManager.notify(1, notification)
    }

    // Обработка навигации "Назад"
    override fun onSupportNavigateUp(): Boolean {
        val currentDestination = navController.currentDestination
        return if (currentDestination?.id == R.id.NotFoundFragment) {
            navController.navigate(R.id.HomeFragmentTitles)
            true
        } else {
            navController.navigateUp() || super.onSupportNavigateUp()
        }
    }
}