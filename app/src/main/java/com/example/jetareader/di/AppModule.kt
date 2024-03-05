package com.example.jetareader.di

import com.example.jetareader.network.BooksApi
import com.example.jetareader.repository.FireRepository
import com.example.jetareader.repository.SearchBookRepository
import com.example.jetareader.utils.Constants
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    fun provideFireBookRepository() =
        FireRepository(queryBook = FirebaseFirestore.getInstance().collection("books"))


    @Provides
    @Singleton
    fun provideBookApi(): BooksApi {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BooksApi::class.java)
    }

    @Provides
    @Singleton
    fun provideBookRepository(api: BooksApi) = SearchBookRepository(api)

}