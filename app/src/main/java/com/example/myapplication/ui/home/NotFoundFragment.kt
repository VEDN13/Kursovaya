package com.example.myapplication.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.myapplication.R

class NotFoundFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Use a LinearLayout to contain both TextViews
        val layout = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
            gravity = android.view.Gravity.CENTER
            setPadding(16, 16, 16, 16)
            addView(createNotFoundView())
            addView(createBackButton())
        }
        return layout
    }

    private fun createNotFoundView(): TextView {
        return TextView(requireContext()).apply {
            text = "Кажется на этот тайтл пока нет переводов \n :("
            textSize = 20f
            gravity = android.view.Gravity.CENTER
        }
    }
    private fun createBackButton(): TextView {
        return TextView(requireContext()).apply {
            text = "Вернуться на главную"
            textSize = 20f
            gravity = android.view.Gravity.CENTER
            paintFlags = paintFlags or android.graphics.Paint.UNDERLINE_TEXT_FLAG
            isClickable = true
            setOnClickListener {
                findNavController().navigate(R.id.HomeFragmentTitles)
            }
        }
    }
}