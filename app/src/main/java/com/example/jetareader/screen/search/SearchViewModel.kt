package com.example.jetareader.screen.search

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetareader.data.Resource
import com.example.jetareader.model.Item
import com.example.jetareader.repository.SearchBookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject


@HiltViewModel
class SearchViewModel @Inject constructor(private val repository: SearchBookRepository) :
    ViewModel() {

    var bookList: List<Item> by mutableStateOf(listOf())
    var isLoading : Boolean by mutableStateOf(true)
    var isError : Boolean by mutableStateOf(false)
    var isEmpty:Boolean by mutableStateOf(false)


    init {
        loadBooks("android")
    }


    fun loadBooks(query: String) {
        viewModelScope.launch {
            isLoading=true
            if(query.isEmpty())return@launch
            try {
                when(val response = repository.getBooks(query)){
                    is Resource.Success->{
                        bookList = response.data!!.items
                        isLoading = false
                        isError=false
                        isEmpty = false
                    }
                    is Resource.Error->{
                        isLoading=false
                        isError=true
                        isEmpty=false
                    }
                    is Resource.Loading->{
                        isLoading=true
                        isError=false
                        isEmpty=false
                    }
                    is Resource.Empty->{
                        isEmpty=true
                        isLoading=false
                        isError=false
                    }
                }
            }catch (e:Exception){
                isLoading=false
                isError=true
                Log.d("searchViewModel", "error: ${e.message.toString()} ")
            }
        }
    }


}