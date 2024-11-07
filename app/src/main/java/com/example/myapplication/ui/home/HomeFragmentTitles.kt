package com.example.myapplication.ui.home
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

        swipeRefreshLayout.setOnRefreshListener {
            fetchLinksFromFirestore()
        }

        fetchLinksFromFirestore() // Initial data load
    }


    private fun fetchLinksFromFirestore() {
        swipeRefreshLayout.isRefreshing = true // Show loading indicator

        db.collection("Title") // Replace with your collection name
            .get()
            .addOnSuccessListener { documents ->
                blocksContainer.removeAllViews() // Clear current views
                for (document in documents) {
                    val imageNotFound = "https://firebasestorage.googleapis.com/v0/b/anihub-64e55.appspot.com/o/Photos%2F404%20not%20found.jpg?alt=media&token=99b0fbda-a13f-4334-a61a-864e8bac914d"
                    val title_name = document.getString("title_name") ?: "No Title"
                    val title_episodes = document.getString("title_episodes") ?: "?"
                    var imageUrl = document.getString("photo_link") ?: imageNotFound // Fetch image URL
                    if (imageUrl == "") { imageUrl = imageNotFound}


                    addBlock(title_name, title_episodes, imageUrl)
                }
            }
            .addOnFailureListener { exception ->
                // Handle error
            }
            .addOnCompleteListener {
                swipeRefreshLayout.isRefreshing = false // Hide loading indicator
            }
    }

    private fun addBlock(title_name: String, title_episodes: String, imageUrl: String) {

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
            layoutParams = LinearLayout.LayoutParams(200, 200)
        }
        // Use Glide to load the image from Firebase URL
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
            text = "Серий: $title_episodes"
            textSize = 14f
        }

        textContainer.addView(titleTextView)
        textContainer.addView(episodesTextView)

        blockLayout.addView(imageView)
        blockLayout.addView(textContainer)

        blocksContainer.addView(blockLayout)
    }

    private fun openLink(title_name: String) {
        val bundle = Bundle().apply {
            putString("title_name", title_name)
        }

        // Используйте NavController для навигации
        findNavController().navigate(R.id.action_homeFragmentTitles_to_homeFragment, bundle)

    }

}
