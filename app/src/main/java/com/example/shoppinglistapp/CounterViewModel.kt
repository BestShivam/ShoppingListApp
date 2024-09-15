package com.example.shoppinglistapp

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class CounterViewModel : ViewModel() {
    private var _count = mutableStateOf(0)
    public val count = _count
    fun increment(){
        _count.value++
    }
    fun decrement(){
        _count.value--
    }
    val i = {_count.value++}
    fun print(){
        println(count)
    }
}