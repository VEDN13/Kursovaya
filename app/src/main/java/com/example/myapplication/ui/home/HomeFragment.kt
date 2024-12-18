package com.example.myapplication.ui.home
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
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
        val view = inflater.inflate(R.layout.fragment_home_sound, container, false)

        arguments?.getString("title_name")

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        blocksContainer = view.findViewById(R.id.blocksContainer)
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout)
        blocksContainer.setPadding(0, 80, 0, 150)
        swipeRefreshLayout.setOnRefreshListener {
            fetchLinksFromFirestore()

            requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
                findNavController().navigateUp()
            }
        }
        fetchLinksFromFirestore() // Первоначальная загрузка данных
    }

    private fun fetchLinksFromFirestore() {
        swipeRefreshLayout.isRefreshing = true // Показать индикатор загрузки
        val titleName = arguments?.getString("title_name")

        db.collection("Dubbing") // Замените на имя вашей коллекции
            .whereEqualTo("title", titleName)
            .get()
            .addOnSuccessListener { documents ->
                blocksContainer.removeAllViews() // Очистить текущие элементы

                if (documents.isEmpty) {
                    // Если документов нет, переходим на NotFoundFragment
                    findNavController().navigate(R.id.NotFoundFragment)
                } else {
                    for (document in documents) {
                        val title = document.getString("dub_title") ?: "Без названия"
                        val link = document.getString("link") ?: ""
                        val type = document.getString("type") ?: ""
                        val episodes = document.getString("last_episode") ?: "?"
                        addBlock(title, link, type, episodes)
                    }
                }
            }
            .addOnFailureListener {
                // Обработка ошибки
            }
            .addOnCompleteListener {
                swipeRefreshLayout.isRefreshing = false // Скрыть индикатор загрузки
            }
    }

    private fun addBlock(title: String, link: String, type: String, episodes: String) {
        val textView = TextView(requireContext()).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            setPadding(50, 50, 50, 50)
            text = title + "  •  " + episodes + " эп."
            textSize = 17f
            isClickable = true

            // Установка изображения в зависимости от типа
            when (type.lowercase()) {
                "dub" -> setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.dub, 0)
                "sub", "subtitles" -> setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.sub, 0)
                "mvo" -> setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.mvo, 0)
                "vo" -> setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.vo, 0)
                "voice" -> setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.voice, 0)
            }
            setOnClickListener {
                openLink(link)
            }
        }
        blocksContainer.addView(textView)
    }

    private fun openLink(url: String) {
        val bundle = Bundle().apply {
            putString("url", url)
        }
        findNavController().navigate(R.id.webViewFragment, bundle)
    }
}