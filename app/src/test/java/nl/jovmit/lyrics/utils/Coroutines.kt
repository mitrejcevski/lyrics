package nl.jovmit.lyrics.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.toCollection
import kotlinx.coroutines.launch

inline fun <T> CoroutineScope.observeFlow(
  stateFlow: StateFlow<T>,
  dispatcher: CoroutineDispatcher = Dispatchers.Unconfined,
  dropInitialState: Boolean = true,
  block: () -> Unit
): List<T> {
  val result = mutableListOf<T>()
  val job = launch(dispatcher) {
    stateFlow.toCollection(result)
  }
  block()
  job.cancel()
  return if (dropInitialState) result.drop(1) else result
}