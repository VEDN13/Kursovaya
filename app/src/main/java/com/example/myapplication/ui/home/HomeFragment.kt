package com.example.myapplication.ui.home

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.R

class HomeFragment : Fragment() {

    private lateinit var viewModel: HomeViewModel
    private lateinit var blocksContainer: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        blocksContainer = view.findViewById(R.id.blocksContainer)

        viewModel.blocks.observe(viewLifecycleOwner) { blocks ->
            updateBlocks(blocks)
        }
    }

    private fun updateBlocks(blocks: List<String>) {
        blocksContainer.removeAllViews()
        for (block in blocks) {
            val textView = TextView(context).apply {
                text = block
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                setPadding(16, 16, 16, 16)
                setBackgroundColor(Color.LTGRAY)
                setOnClickListener {
                    // Установите URL для каждого блока, можно настраивать в зависимости от текста
                    val url = when (block) {
                        "Моя подруга-олениха Нокотан" -> "https://kodik.online/serial/60201/RDXdqRbb60bbd1"
                        "Сайт 2" -> "https://example.com/2"
                        else -> null
                    }
                    url?.let { openLink(it) }
                }
            }
            blocksContainer.addView(textView)
        }
    }

    private fun openLink(url: String) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(url)
        }
        startActivity(intent)
    }
}
