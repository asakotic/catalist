package com.example.myapplication.controller

sealed class Screen(val route: String) {
    //samo tu klase mogu da naslede screen, nigde drugo
    object MainScreen: Screen("main_screen")
    object DetailScreen: Screen("detail_screen")

    fun withArgs(vararg args: String):String{
        return buildString {
            append(route)
            args.forEach {arg->
                append("/$arg")
            }
        }
    }
}