package com.example.jetareader.screen.details

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetareader.data.Resource
import com.example.jetareader.model.Item
import com.example.jetareader.repository.SearchBookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
class DetailsViewModel @Inject constructor(private val repository: SearchBookRepository) :ViewModel(){

    suspend fun getBookById(bookId:String):Resource<Item> {
        return repository.getBookInfo(bookId)
    }


}