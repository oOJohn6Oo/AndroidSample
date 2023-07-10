package io.john6.sample.fragmentcommunicate

import android.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlin.random.Random

class DemoViewModel :ViewModel(){
    private val _sharedValue = MutableStateFlow(0)
    val sharedValue:StateFlow<Int> = _sharedValue.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = 0,
    )

    val bgdColor = Color.rgb(
        Random.nextInt(256),
        Random.nextInt(256),
        Random.nextInt(256),
    )

    fun submitValue(value: Int){
        _sharedValue.tryEmit(value)
    }
}