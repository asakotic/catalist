package com.example.myapplication.scnd

import com.example.myapplication.model.Cat

data class SecondScreenState (
    val cat: Cat? = null,
    val loading: Boolean = true,
    val error: String? = null
)