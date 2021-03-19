package com.example.spotifyclone.business.domain.state

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.spotifyclone.util.printLogD

/**
 * - Keeps track of active StateEvents in DataChannelManager
 * - Keeps track of whether the progress bar should show or not based on a boolean
 *      value in each StateEvent (shouldDisplayProgressBar)
 */
class StateEventManager {

    private val activeStateEvents: HashMap<String, StateEvent> = HashMap()
    val shouldDisplayProgressBar = mutableStateOf(false)

    fun isStateEventActive(stateEvent: StateEvent): Boolean {
        printLogD(
            "StateEventManager", "Is StateEvent Active? : " +
                    "${activeStateEvents.containsKey(stateEvent.eventName())}"
        )
        return activeStateEvents.containsKey(stateEvent.eventName())
    }

    fun addStateEvent(stateEvent: StateEvent) {
        printLogD(
            "StateEventManager",
            "------------------ Launching New StateEvent : ${stateEvent.eventName()}"
        )
        activeStateEvents[stateEvent.eventName()] = stateEvent
        syncNumActiveStateEvents()
    }

    fun removeStateEvent(stateEvent: StateEvent?) {
        printLogD("StateEventManager", "Removed StateEvent : ${stateEvent?.eventName()}")
        activeStateEvents.remove(stateEvent?.eventName())
        syncNumActiveStateEvents()
    }

    fun getActiveStateEvents(): MutableSet<String> {
        return activeStateEvents.keys
    }

    fun clearActiveStateEvents() {
        printLogD("StateEventManager", "Clearing active state events")
        activeStateEvents.clear()
        syncNumActiveStateEvents()
    }

    private fun syncNumActiveStateEvents() {
        var progressBar = false
        for (stateEvent in activeStateEvents.values) {
            if (stateEvent.shouldDisplayProgressBar()) {
                progressBar = true
            }
        }
        shouldDisplayProgressBar.value = progressBar
        printLogD("StateEventManager", "ProgressBar $progressBar ")
    }
}