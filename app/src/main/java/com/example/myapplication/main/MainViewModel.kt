package com.example.myapplication.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class MainViewModel(
    private val repository: Repository = Repository
): ViewModel() {
    //hot flow koji mora da ima jednu vrednost i uvek vraca poslednje stanje
    private val _state = MutableStateFlow(MainScreenState())
    val state = _state.asStateFlow()
    private fun setState(reducer: MainScreenState.()-> MainScreenState) = _state.getAndUpdate(reducer)

    private val events = MutableSharedFlow<MainScreenEvent>()
    fun setEvent(event: MainScreenEvent) = viewModelScope.launch { events.emit(event) }

    private fun observeEvents() {
        viewModelScope.launch {
            events.collect {
                when (it) {
                    is MainScreenEvent.FilterList -> {
                        handleFilterList(query = it.query)
                    }
                }
            }
        }
    }
    private fun handleFilterList(query: String){
        viewModelScope.launch {
            setState { copy(loading = true, query = query) }
            try {
                val data = repository.filter(query)
                setState { copy(listCats = data) }
            }catch (e: IOException){
                setState { copy(error = "ne moze se ucitati, rip :(") }
            }finally {
                setState { copy(loading = false) }
            }
        }
    }

    init {
        observeEvents()
        viewModelScope.launch {
            try {
                val data = repository.findAll()
                setState { copy(listCats = data) }
            }catch (e: IOException){
                setState { copy(error = "ne moze se ucitati, rip :(") }
            }finally {
                setState { copy(loading = false) }
            }

        }
    }
}