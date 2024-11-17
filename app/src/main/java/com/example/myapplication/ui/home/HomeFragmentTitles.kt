package com.example.myapplication.ui.home
import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.myapplication.R
import com.google.firebase.firestore.FirebaseFirestore
import android.widget.ImageView
import com.bumptech.glide.Glide

class HomeFragmentTitles : Fragment() {

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
        blocksContainer.setPadding(0, 100, 0, 150)
        swipeRefreshLayout.setOnRefreshListener {
            fetchLinksFromFirestore()
        }

        fetchLinksFromFirestore() // Initial data load
    }
    private fun fetchLinksFromFirestore() {
        swipeRefreshLayout.isRefreshing = true

        db.collection("Title")
            .get()
            .addOnSuccessListener { documents ->
                blocksContainer.removeAllViews() // Clear current views
                for (document in documents) {
                    val imageNotFound = "https://firebasestorage.googleapis.com/v0/b/anihub-64e55.appspot.com/o/Photos%2F404%20not%20found.jpg?alt=media&token=99b0fbda-a13f-4334-a61a-864e8bac914d"
                    val title_name = document.getString("title_name") ?: "No Title"
                    val title_episodes = document.getString("title_episodes") ?: "?/?"
                    var imageUrl = document.getString("photo_link") ?: imageNotFound
                    if (imageUrl == "") { imageUrl = imageNotFound}
                    var title_status = document.getString("title_status") ?: ""
                    if (title_status == "ongoing") { title_status = "Онгоинг"}
                    if (title_status == "released") { title_status = "Вышел"}
                    val title_description = document.getString("title_description") ?: ""

                    addBlock(title_name, title_episodes, imageUrl, title_status, title_description)
                }
            }
            .addOnFailureListener {
            }
            .addOnCompleteListener {
                swipeRefreshLayout.isRefreshing = false
            }
    }
    @SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n")
    private fun addBlock(
        title_name: String,
        title_episodes: String,
        imageUrl: String,
        title_status: String,
        title_description: String
    ) {
        val blockLayout = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            background = requireContext().getDrawable(R.drawable.border_background)
            setPadding(10, 50, 80, 50)
            isClickable = true
            setOnClickListener {
                openLink(title_name)
            }
        }

        val imageView = ImageView(requireContext()).apply {
            layoutParams = LinearLayout.LayoutParams(400, 400)
        }

        Glide.with(this).load(imageUrl).into(imageView)

        val textContainer = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1f
            )
        }

        val titleTextView = TextView(requireContext()).apply {
            text = title_name
            textSize = 17f
        }

        val episodesTextView = TextView(requireContext()).apply {
            text = "$title_episodes эп."
            textSize = 14f
        }

        val statusTextView = TextView(requireContext()).apply {
            text = title_status
            textSize = 14f
        }

        val shortDescription = getShortDescription(title_description, 2)
        val isDescriptionShortened = title_description != shortDescription

        val descriptionTextView = TextView(requireContext()).apply {
            text = shortDescription
            setTextColor(Color.GRAY)
            textSize = 11f
            setOnClickListener {
                if (isDescriptionShortened) {
                    if (text == shortDescription) {
                        text = title_description // Показываем полное описание
                    } else {
                        text = shortDescription // Возвращаем сокращённое
                    }
                }
            }
        }

        textContainer.addView(titleTextView)
        textContainer.addView(statusTextView)
        textContainer.addView(episodesTextView)
        textContainer.addView(descriptionTextView)

        blockLayout.addView(imageView)
        blockLayout.addView(textContainer)
        blocksContainer.addView(blockLayout)

    }
    private fun getShortDescription(text: String, maxSentences: Int): String {
        val sentences = text.split(". ").filter { it.isNotEmpty() }
        return if (sentences.size <= maxSentences) {
            text
        } else {
            sentences.take(maxSentences).joinToString(". ") + "..."
        }
    }
    private fun openLink(title_name: String) {
        val bundle = Bundle().apply {
            putString("title_name", title_name)
        }
            // Используйте NavController для навигации
            findNavController().navigate(R.id.action_homeFragmentTitles_to_homeFragment, bundle)
    }
}