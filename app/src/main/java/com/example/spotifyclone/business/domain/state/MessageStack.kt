package com.example.spotifyclone.business.domain.state

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.spotifyclone.util.printLogD
import java.lang.IndexOutOfBoundsException

class MessageStack : ArrayList<StateMessage>() {

    private val _stateMessage: MutableLiveData<StateMessage?> = MutableLiveData()

    val stateMessage: LiveData<StateMessage?>
        get() = _stateMessage

    private fun setStateMessage(stateMessage: StateMessage?) {
        _stateMessage.value = stateMessage
    }

    override fun add(element: StateMessage): Boolean {
        if (this.contains(element)) { // prevent duplicate errors added to stack
            return false
        }
        printLogD(
            "MessageStack",
            "------------------------------------- \n" +
                    "Adding New State message \n" +
                    "message : ${element.response.message} \n" +
                    "type : ${element.response.messageType} \n" +
                    "uiType : ${element.response.uiComponentType} \n" +
                    "-------------------------------------"
        )
        val transaction = super.add(element)
        if (this.size == 1) {
            setStateMessage(stateMessage = element)
        }
        return transaction
    }

    override fun addAll(elements: Collection<StateMessage>): Boolean {
        for (element in elements) {
            add(element)
        }
        return true // always return true. We don't care about result bool.
    }

    override fun removeAt(index: Int): StateMessage {
        try {
            val transaction = super.removeAt(index)
            printLogD("MessageStack", "Removed State message at index $index")
            if (this.size > 0) {
                setStateMessage(stateMessage = this[0])
            } else {
                printLogD("MessageStack", "Message stack is empty")
                setStateMessage(null)
            }
            return transaction
        } catch (e: IndexOutOfBoundsException) {
            setStateMessage(null)
            e.printStackTrace()
        }
        return StateMessage( // this does nothing
            Response(
                message = "does nothing",
                uiComponentType = UIComponentType.None,
                messageType = MessageType.None
            )
        )
    }

    fun isStackEmpty(): Boolean {
        return size == 0
    }
}