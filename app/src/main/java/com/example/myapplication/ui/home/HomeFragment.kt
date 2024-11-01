package com.example.myapplication.ui.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.myapplication.R
import com.google.firebase.firestore.FirebaseFirestore

class HomeFragment : Fragment() {

    private lateinit var blocksContainer: LinearLayout
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home_sound, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        blocksContainer = view.findViewById(R.id.blocksContainer)
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout)

        swipeRefreshLayout.setOnRefreshListener {
            fetchLinksFromFirestore()
        }

        fetchLinksFromFirestore() // Первоначальная загрузка данных
    }

    private fun fetchLinksFromFirestore() {
        swipeRefreshLayout.isRefreshing = true // Показать индикатор загрузки

        db.collection("sound") // Замените на имя вашей коллекции
            .get()
            .addOnSuccessListener { documents ->
                blocksContainer.removeAllViews() // Очистить текущие элементы
                for (document in documents) {
                    val title = document.getString("studio") ?: "Без названия"
                    val link = document.getString("link") ?: ""
                    val type = document.getString("type") ?: ""

                    addBlock(title, link, type)
                }
            }
            .addOnFailureListener { exception ->
                // Обработка ошибки
            }
            .addOnCompleteListener {
                swipeRefreshLayout.isRefreshing = false // Скрыть индикатор загрузки
            }
    }

    private fun addBlock(title: String, link: String, type: String) {
        val textView = TextView(requireContext()).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            //background = resources.getDrawable(R.drawable.custom_selectable_background, null) // Задайте фоновый ресурс
            setPadding(20, 20, 20, 20)
            text = title
            isClickable = true

            // Установка изображения в зависимости от типа
            when (type) {
                "dub" -> setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.dub, 0)
                "sub" -> setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.sub, 0)
                "mvo" -> setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.mvo, 0)
                "vo" -> setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.vo, 0)
                // Добавьте другие типы при необходимости
                //else -> setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.default_icon, 0) // Иконка по умолчанию
            }

            setOnClickListener {
                openLink(link)
            }
        }

        blocksContainer.addView(textView)
    }

    private fun openLink(url: String) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(url)
        }
        startActivity(intent)
    }
}
