package com.example.spotifyclone.framework.presentation.ui.test

import androidx.compose.runtime.MutableState
import com.example.spotifyclone.business.domain.state.StateEvent
import com.example.spotifyclone.framework.presentation.ui.BaseViewModel
import com.example.spotifyclone.util.printLogD

class TestViewModel : BaseViewModel<TestViewState>() {

    val value = viewState.value

    override fun initNewViewState(): TestViewState {
        return TestViewState()
    }

    override fun setStateEvent(stateEvent: StateEvent) {
        //Nothing
    }

    override fun handleNewData(data: TestViewState) {
        // Nothing
    }

    fun setThree() {
        value.three.value = if (value.three.value == null) {
            1
        } else {
            value.three.value!! + 1
        }
    }

    fun getThree(): MutableState<Int?> {
        return value.three
    }
}