package com.example.jetareader.screen.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetareader.data.DataOrException
import com.example.jetareader.model.MBook
import com.example.jetareader.repository.FireRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: FireRepository) :ViewModel(){

    val data : MutableState<DataOrException<List<MBook>,Boolean,Exception>> =
        mutableStateOf(DataOrException(listOf(),true,Exception("")))
    init {
        getAllBooksFromDatabase()
    }


    fun getAllBooksFromDatabase(){
        viewModelScope.launch {
            data.value.loading=true
            data.value = repository.getAllMyBooksFromDatabase()
            if(!data.value.data.isNullOrEmpty()) data.value.loading=false
        }
    }
}