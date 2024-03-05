package com.example.jetareader.navigation

import android.telecom.Call.Details
import java.lang.IllegalArgumentException

enum class  ReaderScreens {
    SplashScreen,
    LoginScreen,
    CreateAcountScreen,
    ReaderHomeScreen,
    SearchScreen,
    DetailScreen,
    UpdateScreen,
    ReaderStatsScreen;

    companion object{
        fun fromRoute(route:String?):ReaderScreens{
            return when(route?.substringBefore("/")){
                SplashScreen.name->SplashScreen
                LoginScreen.name->LoginScreen
                CreateAcountScreen.name->CreateAcountScreen
                ReaderHomeScreen.name->ReaderHomeScreen
                SearchScreen.name->SearchScreen
                DetailScreen.name->DetailScreen
                UpdateScreen.name->UpdateScreen
                ReaderStatsScreen.name->ReaderStatsScreen
                null -> ReaderHomeScreen
                else -> throw IllegalArgumentException("Route $route is not recognized")
            }
        }
    }
}