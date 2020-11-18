package com.matthbr.recicle.mvi

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

object MVI {

    @InternalCoroutinesApi
    @Suppress("UNCHECKED_CAST")
    @ExperimentalCoroutinesApi
    abstract class Store<DATA, INTENT: Store.Intent> : ViewModel() {

        val state: LiveData<State<DATA>> get() = _state

        private val _state = MutableLiveData<State<DATA>>().apply {
            value = State(data = initialData())
        }

        private val actionsChannel = Channel<suspend ()->Unit>()

        private fun setState(data: State<DATA>) {
            _state.value = data
        }

        fun getState() = state.value!!

        private suspend fun dispatch(intent: Intent) =
                when(intent) {
                    is Intent.LoadingIntent -> {
                        val data = getState()
                        setState(data.copy(loading = intent.loading))
                    }
                    is Intent.MessageIntent -> {
                        val data = getState()
                        setState(data.copy(message = intent.message))
                    }
                    is Intent.ExceptionIntent -> {
                        val data = getState()
                        setState(data.copy(exception = intent.exception))
                    }
                    else -> resolveIntent(intent as INTENT)
                }

        fun produceAction(block: suspend (dispatch: suspend (intent: Intent)->Unit?)->Unit) = viewModelScope.launch {
            actionsChannel.send {
                try {
                    getState().message?.let {
                        dispatch(
                                Intent.MessageIntent(
                                        message = null
                                )
                        )
                    }
                    getState().exception?.let {
                        dispatch(
                                Intent.ExceptionIntent(
                                        exception = null
                                )
                        )
                    }
                    block(::dispatch)
                } catch (e: Exception) {
                    dispatch(
                            Intent.ExceptionIntent(exception = e)
                    )
                } finally {
                    getState().exception?.let {
                        dispatch(
                                Intent.ExceptionIntent(
                                        exception = null
                                )
                        )
                    }
                    if(getState().loading) {
                        dispatch(
                                Intent.LoadingIntent(
                                        loading = false
                                )
                        )
                    }
                }
            }
        }

        suspend fun produceReducer(block: suspend (setState: (data: State<DATA>)->Unit)->Unit) {
            block(::setState)
        }

        abstract fun initialData() : DATA

        abstract suspend fun resolveIntent(intent: INTENT): Unit?

        init {
            viewModelScope.launch {
                actionsChannel.receiveAsFlow().collect { action -> action() }
            }
        }

        data class State<DATA>(val loading: Boolean = false,
                               val message: Message? = null,
                               val exception: Exception? = null,
                               val data: DATA)

        open class Intent {
            class LoadingIntent(val loading: Boolean) : Intent()
            class MessageIntent(val message: Message?) : Intent()
            class ExceptionIntent(val exception: Exception?) : Intent()
        }

        data class Message(val type: MessageType,
                           val code: Int? = null,
                           val message: String? = null,
                           val messageResId: Int? = null)

        enum class MessageType {
            WARN,
            ERROR,
            SUCCESS
        }

    }

}