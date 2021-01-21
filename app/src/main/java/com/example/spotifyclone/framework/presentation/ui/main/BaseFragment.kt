package com.example.spotifyclone.framework.presentation.ui.main

import android.content.Context
import androidx.fragment.app.Fragment
import com.example.spotifyclone.framework.presentation.ui.UICommunicationListener
import com.example.spotifyclone.util.printLogD

abstract class BaseFragment : Fragment() {

    lateinit var uiCommunicationListener: UICommunicationListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            uiCommunicationListener = context as UICommunicationListener
        } catch (e: ClassCastException) {
            printLogD("BaseFragment", "$context must implement UICommunicationListener")
        }
    }
}