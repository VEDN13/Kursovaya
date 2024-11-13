package com.example.myapplication.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class NotFoundFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createNotFoundView()
    }

    private fun createNotFoundView(): TextView {
        return TextView(requireContext()).apply {
            text = "Кажется на этот тайтл пока нет переводов \n :("
            textSize = 24f
            gravity = android.view.Gravity.CENTER
            setPadding(16, 16, 16, 16)
            // Можно добавить дополнительные настройки, такие как фон, шрифт и прочее
        }
    }
}
