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
                    val title_name = document.getString("title_name") ?: "No Title"
                    val title_episodes = document.getString("title_episodes") ?: "?"

                    addBlock(title_name, title_episodes)
                }
            }
            .addOnFailureListener { exception ->
                // Handle error
            }
            .addOnCompleteListener {
                swipeRefreshLayout.isRefreshing = false // Hide loading indicator
            }
    }

    private fun addBlock(title_name: String, title_episodes: String) {
        val textView = TextView(requireContext()).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            setPadding(50, 50, 50, 50)
            text = title_name
            textSize = 17f
            isClickable = true

            setOnClickListener {
                openLink(title_name)
            }
        }

        blocksContainer.addView(textView)
    }

    private fun openLink(title_name: String) {
        val bundle = Bundle().apply {
            putString("title_name", title_name)
        }

        // Используйте NavController для навигации
        findNavController().navigate(R.id.action_homeFragmentTitles_to_homeFragment, bundle)

    }

}
