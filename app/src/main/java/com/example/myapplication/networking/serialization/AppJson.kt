package com.example.myapplication.networking.serialization

import kotlinx.serialization.json.Json

val AppJson = Json{
    ignoreUnknownKeys = true
    prettyPrint = true
}