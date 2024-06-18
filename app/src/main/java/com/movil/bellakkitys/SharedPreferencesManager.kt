package com.movil.bellakkitys

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.movil.bellakkitys.data.model.Artist
import com.movil.bellakkitys.data.model.Song

class SharedPreferencesManager(private val context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("appData", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun saveArtistList(lista: ArrayList<Artist>) {
        val editor = sharedPreferences.edit()
        val listaJson = gson.toJson(lista)
        editor.putString("artistList", listaJson)
        editor.apply()
    }

    fun getArtistList(): ArrayList<Artist> {
        val listaJson = sharedPreferences.getString("artistList", "")
        return if (listaJson.isNullOrEmpty()) {
            ArrayList()
        } else {
            gson.fromJson(listaJson, object : TypeToken<ArrayList<Artist>>() {}.type)
        }
    }

    fun saveSongList(lista: ArrayList<Song>) {
        val editor = sharedPreferences.edit()
        val listaJson = gson.toJson(lista)
        editor.putString("songList", listaJson)
        editor.apply()
    }

    fun getSongList(): ArrayList<Song> {
        val listaJson = sharedPreferences.getString("songList", "")
        return if (listaJson.isNullOrEmpty()) {
            ArrayList()
        } else {
            gson.fromJson(listaJson, object : TypeToken<ArrayList<Song>>() {}.type)
        }
    }
}

