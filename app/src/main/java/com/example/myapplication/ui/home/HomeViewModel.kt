package com.example.myapplication.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _blocks = MutableLiveData<List<String>>().apply {
        value = listOf()
    }
    val blocks: LiveData<List<String>> = _blocks
}
