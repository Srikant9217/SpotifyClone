package com.example.spotifyclone.business.domain.state

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
    private val _shouldDisplayProgressBar: MutableLiveData<Boolean> = MutableLiveData()

    val shouldDisplayProgressBar: LiveData<Boolean>
        get() = _shouldDisplayProgressBar


    fun isStateEventActive(stateEvent: StateEvent): Boolean {
        printLogD(
            "StateEventManager", "is state event active? :" +
                    "${activeStateEvents.containsKey(stateEvent.eventName())}"
        )
        return activeStateEvents.containsKey(stateEvent.eventName())
    }

    fun addStateEvent(stateEvent: StateEvent) {
        printLogD(
            "StateEventManager",
            "-------------------------------- \n" +
                    "launching New State Event: ${stateEvent.eventName()}"
        )
        activeStateEvents[stateEvent.eventName()] = stateEvent
        syncNumActiveStateEvents()
    }

    fun removeStateEvent(stateEvent: StateEvent?) {
        printLogD("StateEventManager", "Removing state event : ${stateEvent?.eventName()}")
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
        var shouldDisplayProgressBar = false
        for (stateEvent in activeStateEvents.values) {
            if (stateEvent.shouldDisplayProgressBar()) {
                shouldDisplayProgressBar = true
            }
        }
        _shouldDisplayProgressBar.value = shouldDisplayProgressBar
        printLogD("StateEventManager", "ProgressBar $shouldDisplayProgressBar ")
    }
}