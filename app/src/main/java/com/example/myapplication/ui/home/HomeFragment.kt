package com.example.myapplication.ui.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.myapplication.R

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home_sound, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<TextView>(R.id.block1).setOnClickListener {
            openLink("https://kodik.online/serial/58235/RDXdqR5f79f6f5")
        }

        view.findViewById<TextView>(R.id.block2).setOnClickListener {
            openLink("https://kodik.online/serial/60194/RDXdqR39289358")
        }

        // Добавьте остальные обработчики
        view.findViewById<TextView>(R.id.block3).setOnClickListener {
            openLink("https://kodik.online/serial/61140/RDXdqRa1609725")
        }

        view.findViewById<TextView>(R.id.block4).setOnClickListener {
            openLink("https://kodik.online/serial/60204/RDXdqRf206795d")
        }
    }

    private fun openLink(url: String) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(url)
        }
        startActivity(intent)
    }
}



