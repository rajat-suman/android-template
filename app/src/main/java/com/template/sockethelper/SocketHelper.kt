package com.template.sockethelper

import android.util.Log
import com.template.sockethelper.SocketKeys.BOOKING_CANCEL_ALERT
import com.template.sockethelper.SocketKeys.BOOKING_STATUS_CHANGE
import com.template.sockethelper.SocketKeys.LOGOUT
import com.template.sockethelper.SocketKeys.SYNC_DATA
import com.github.nkzawa.emitter.Emitter
import com.github.nkzawa.socketio.client.IO
import com.github.nkzawa.socketio.client.Socket
import com.template.BuildConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject


import com.google.gson.Gson
import com.template.sockethelper.SocketKeys.NEW_BOOKING_REQUEST
import com.template.utils.AppController

class SocketHelper {

    // For Singleton instantiation
    companion object {
        @Volatile
        private var instance: SocketHelper? = null
        fun getInstance(): SocketHelper {
            return instance ?: synchronized(this) {
                instance ?: buildSocket().also { instance = it }
            }
        }
        private fun buildSocket(): SocketHelper {
            return SocketHelper()
        }

    }

    @Volatile
    var socket: Socket? = null

    @Volatile
    var globalListeners: GlobalListeners? = null

    @Synchronized
    fun initSocket() {
        if (socket == null && !AppController.auth.isNullOrBlank()) {
            val options = IO.Options()
            options.reconnection = true
            options.reconnectionAttempts = Int.MAX_VALUE
            options.reconnectionDelay = 1000
            options.forceNew = true
            options.query = "token=${AppController.auth}"
//            if(!BuildConfig.DEBUG)
//            options.path= "/v2/socket.io"
            socket = IO.socket(BuildConfig.SOCKET_BASE_URL, options)
            socketOn()
        }
        socket?.let {
            if (!it.connected())
                it.connect()
        }
    }

    @Synchronized
    private fun socketOn() {
        listener(Socket.EVENT_CONNECT, onConnect)
        listener(Socket.EVENT_DISCONNECT, onDisconnect)
        listener(Socket.EVENT_CONNECT_ERROR, onConnectError)
        listener(Socket.EVENT_CONNECT_TIMEOUT, onConnectError)
        listener(Socket.EVENT_ERROR, onConnectError)
        listener(Socket.EVENT_RECONNECT_FAILED, onConnectError)
    }

    @Synchronized
    fun disconnectSocket() {
        socket?.disconnect()
        AppController.auth = null
        socket = null
    }

    fun listener(key: String, emitter: Emitter.Listener) {
        initSocket()
        if (socket?.hasListeners(key) != true) {
            socket?.on(key, emitter)
        }
    }

    fun <T> emit(key: String, data: T) {
        if (!isConnected()) {
            initSocket()
            return
        }
        Log.e("SocketHelper", "emit: $key = $data")
        socket?.emit(key, data)
    }

    fun isConnected(): Boolean {
        return socket?.connected() == true
    }

    fun addListeners() {
        try {
            listener(SYNC_DATA, syncData)
            listener(NEW_BOOKING_REQUEST, newBooking)
            listener(BOOKING_STATUS_CHANGE, onStatusChange)
            listener(BOOKING_CANCEL_ALERT, onCancel)
            listener(LOGOUT, logout)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    interface GlobalListeners {
        fun onSyncData(syncData: Any)
        fun onNewBooking(booking: Any)
        fun onBookingStatusChange(booking: Any)
        fun onBookingCancelAlert(booking: Any)
        fun onSessionEnd()
    }

    private val onConnect = Emitter.Listener {
        Log.e("SocketConnection", "======>>>>>>>>  Connected ")
        initSocket()
    }
    private val onDisconnect = Emitter.Listener {
        Log.e("SocketConnection", "======>>>>>>>>  Disconnected ${it[0]}")
        initSocket()
    }
    private val onConnectError = Emitter.Listener { it ->
        Log.e("onConnectError", "======>>>>>>>> onConnectError ${it[0]}")
        initSocket()
    }
    private var syncData = Emitter.Listener {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                Log.e("SocketHelper", "addListeners: $SYNC_DATA = ${it[0]}")
                globalListeners?.onSyncData(JSONObject(it[0].toString()))
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }
    private var newBooking = Emitter.Listener {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                Log.e("SocketHelper", "addListeners: $NEW_BOOKING_REQUEST = ${it[0]}")
                globalListeners?.onNewBooking(JSONObject(it[0].toString()))
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }
    private var onStatusChange = Emitter.Listener {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                Log.e("SocketHelper", "addListeners: $BOOKING_STATUS_CHANGE = ${it[0]}")
                globalListeners?.onBookingStatusChange(JSONObject(it[0].toString()))
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }
    private var onCancel = Emitter.Listener {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                Log.e("SocketHelper", "addListeners: $BOOKING_CANCEL_ALERT = ${it[0]}")
                globalListeners?.onBookingCancelAlert(JSONObject(it[0].toString()))
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }
    private var logout = Emitter.Listener {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                globalListeners?.onSessionEnd()
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

}