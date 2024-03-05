package com.example.jetareader.model


data class Muser(
    val id: String?,
    val userId:String,
    val displayName:String,
    val avaterUrl:String,
    val quote:String,
    val profession:String
){
    fun toMap():MutableMap<String,Any>{
        return mutableMapOf(
            "user_id" to this.userId,
            "display_name" to this.displayName,
            "quote" to this.quote,
            "profession" to this.profession,
            "avater_url" to this.avaterUrl
        )
    }
}
