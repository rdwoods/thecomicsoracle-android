package com.rwoods.thecomicsoracle.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

abstract class BaseViewModel<Intent : Any, State : Any, Effect : Any>(
    initialState: State,
    private val dispatcher: CoroutineDispatcher = IO
) : ViewModel() {

    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<State> = _state.asStateFlow()

    private val _effect = Channel<Effect>(Channel.BUFFERED)
    val effect: Flow<Effect> = _effect.receiveAsFlow()

    fun onIntent(intent: Intent) {
        viewModelScope.launch { reduce(intent) }
    }

    protected fun updateState(reducer: State.() -> State) {
        _state.update(reducer)
    }

    protected suspend fun sendEffect(effect: Effect) {
        _effect.send(effect)
    }

    protected abstract suspend fun reduce(intent: Intent)

    fun launchCoroutine(handler: CoroutineExceptionHandler, block: suspend CoroutineScope.() -> Unit){
        viewModelScope.launch(dispatcher + handler){
            block()
        }
    }

    fun launchExceptionCoroutine(block: suspend CoroutineScope.() -> Unit){
        viewModelScope.launch(dispatcher){
            block()
        }
    }

    fun getExceptionHandler(handleWith: (Throwable) -> Unit) = CoroutineExceptionHandler {_, throwable ->
        val suppressedExceptions = throwable.suppressedExceptions

        if (suppressedExceptions.isNotEmpty()){
            handleWith(suppressedExceptions.first())
        } else {
            handleWith(throwable)
        }
    }
}
