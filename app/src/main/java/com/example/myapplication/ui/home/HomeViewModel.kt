package com.example.myapplication.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _blocks = MutableLiveData<List<String>>().apply {
        value = listOf(
            "Моя подруга-олениха Нокотан",
            "Block 2",
            "Block 3",
            "Block 4"
        )
    }
    val blocks: LiveData<List<String>> = _blocks
}
