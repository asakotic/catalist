package com.example.myapplication.repository

import com.example.myapplication.model.Cat
import com.example.myapplication.model.CatApi
import com.example.myapplication.networking.retrofit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import retrofit2.create
import java.io.IOException

object Repository {
    //private var currentCatList = SampleData.toMutableList()
    private var catApi = retrofit.create(CatApi::class.java)
    private var currentCatList = emptyList<Cat>()

    suspend fun filter(query: String):List<Cat>{
        fetchAll()
        if(query == "")
            return currentCatList

        return currentCatList.filter { cat->
            (cat.name.startsWith(query,true) || cat.alternativeName?.startsWith(query, true) == true)
        }

    }

    suspend fun findById(id: String):Cat?{
        fetchAll()
        return currentCatList.find { cat -> (cat.id == id) }
    }


    suspend fun findAll():List<Cat> {
      return fetchAll()
    }

    private suspend fun fetchAll(): List<Cat> {
        if (currentCatList.isEmpty()){
            withContext(Dispatchers.IO){
                currentCatList = catApi.getAllBreads()
            }
        }
        return currentCatList
    }

}