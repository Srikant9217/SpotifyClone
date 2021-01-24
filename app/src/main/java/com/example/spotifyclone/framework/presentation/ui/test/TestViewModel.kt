package com.example.spotifyclone.framework.presentation.ui.test

import com.example.spotifyclone.business.domain.state.StateEvent
import com.example.spotifyclone.framework.presentation.ui.BaseViewModel

class TestViewModel : BaseViewModel<TestViewState>() {

    override fun initNewViewState(): TestViewState {
        return TestViewState()
    }

    override fun setStateEvent(stateEvent: StateEvent) {

    }

    override fun handleNewData(data: TestViewState) {
        // Nothing
    }

    fun setOne(){
        val update = getCurrentViewStateOrNew()
        update.one =+ 1
        setViewState(update)
    }

    fun setTwo(){
        val update = getCurrentViewStateOrNew()
        update.two =+ 1
        setViewState(update)
    }
}