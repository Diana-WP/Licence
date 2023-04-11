package com.example.traveler
import android.content.SharedPreferences
import android.content.Context

class PrefManager(context: Context?) {
    //Shared pref mode
    val PRIVATE_MODE =0

    //Shared pref FileName
    private val PREF_NAME = "SharedPreferences"
    private val IS_LOGIN = "is_login"

    val pref: SharedPreferences? = context?.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
    val editor: SharedPreferences.Editor? = pref?.edit()

    fun setLogin(isLogin: Boolean){
    editor?.putBoolean(IS_LOGIN, isLogin)
    editor?.commit()
    }

    fun setEmail(email : String?){
        editor?.putString("email", email)
        editor?.commit()
    }

    fun isLogin() : Boolean?{
        return pref?.getBoolean(IS_LOGIN, false)
    }

    fun getEmail() : String?{
        return pref?.getString("email", "")
    }

    fun removeData(){
        editor?.clear()
        editor?.commit()
    }
}