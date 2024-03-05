package com.example.jetareader.repository

import android.util.Log
import com.example.jetareader.data.Resource
import com.example.jetareader.model.Item
import com.example.jetareader.model.SearchBook
import com.example.jetareader.network.BooksApi
import java.io.IOException
import javax.inject.Inject

class SearchBookRepository @Inject constructor(private val api: BooksApi) {
    suspend fun getBooks(searchQuery:String):Resource<SearchBook>{
       return try {
            Resource.Loading(data=true)
            val itemList = api.getAllBooks(searchQuery)
            if(itemList.totalItems==0) Resource.Empty()
            else Resource.Success(data=itemList)
        }catch (e:Exception){
           Log.e("searcherror", e.message.toString())
            Resource.Error(message = e.message.toString())
        }
    }


    suspend fun getBookInfo(bookId:String):Resource<Item>{
        val response = try {
            Resource.Loading(data=true)
            api.getBookInfo(bookId)
        }catch (e:Exception){
            return Resource.Error(message = e.message.toString())
        }
        Resource.Loading(data=false)
        Log.d("detailsInfo", response.toString())
        return Resource.Success(data = response)
    }
}