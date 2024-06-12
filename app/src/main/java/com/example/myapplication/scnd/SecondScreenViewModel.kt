package com.example.myapplication.scnd

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.main.MainScreenState
import com.example.myapplication.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class SecondScreenViewModel(
    private val repository: Repository = Repository,
    id: String
): ViewModel() {

    private val _state = MutableStateFlow(SecondScreenState())
    val state = _state.asStateFlow()
    private fun setState(reducer: SecondScreenState.()-> SecondScreenState) = _state.getAndUpdate(reducer)

    init {
        viewModelScope.launch {
            try {
                val data = withContext(Dispatchers.IO){
                    repository.findById(id)
                }
                setState { copy(cat = data) }
            }catch (e: IOException){
                setState { copy(error = "ne moze se ucitati, rip :)") }
            }finally {
                setState { copy(loading = false) }
            }

        }
    }
}