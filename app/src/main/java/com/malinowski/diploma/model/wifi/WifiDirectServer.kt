package com.malinowski.diploma.model.wifi

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.ServerSocket

class WifiDirectServer : WifiDirectSocket() {

    private lateinit var serverSocket: ServerSocket

    init {
        launch(Dispatchers.IO) {
            try {
                serverSocket = ServerSocket(PORT)
                socket = serverSocket.accept()
                start()
            } catch (e: Exception) {
                Log.e("RASPBERRY", e.message ?: "server error")
            }
        }
    }

    override fun shutDown() {
        super.shutDown()
        serverSocket.close()
    }
}