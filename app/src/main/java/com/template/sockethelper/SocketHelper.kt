package com.template.sockethelper

import android.util.Log
import com.template.sockethelper.SocketKeys.BOOKING_CANCEL_ALERT
import com.template.sockethelper.SocketKeys.BOOKING_STATUS_CHANGE
import com.template.sockethelper.SocketKeys.LOGOUT
import com.template.sockethelper.SocketKeys.SYNC_DATA
import com.github.nkzawa.emitter.Emitter
import com.github.nkzawa.socketio.client.IO
import com.github.nkzawa.socketio.client.Socket
import com.template.networkcalls.SOCKET_BASE_URL
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

object SocketHelper {

    var socket: Socket? = null
    var token=""
    var globalListeners: GlobalListeners? = null
    var currentLat :Double?=null
    var currentLong :Double?=null
    private val onConnect = Emitter.Listener {
        Log.e("SocketConnection", "======>>>>>>>>  Connected ")
    }

    private val onDisconnect = Emitter.Listener {
        Log.e("SocketConnection", "======>>>>>>>>  Disconnected ${it[0]}")
        initSocket()
    }

    private val onConnectError = Emitter.Listener { it ->
        Log.e("onConnectError", "======>>>>>>>> onConnectError ${it[0]}")
        initSocket()
    }

    private val onConnectTimeout = Emitter.Listener { ds ->
        Log.e("timeOut", "======>>>>>>>> ${ds[0]}")
        initSocket()
    }

    private var syncData= Emitter.Listener {
        CoroutineScope(Dispatchers.Main).launch {
            Log.e("SocketHelper", "addListeners: $SYNC_DATA = ${it[0]}")
            globalListeners?.onSyncData(JSONObject(it[0].toString()))
        }
    }
    private var onStatusChange= Emitter.Listener {
        CoroutineScope(Dispatchers.Main).launch {
            Log.e("SocketHelper", "addListeners: $BOOKING_STATUS_CHANGE = ${it[0]}")
            try {
                val booking = JSONObject(it[0].toString())
                globalListeners?.onBookingStatusChange(booking)
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }
    private var onCancel= Emitter.Listener {
        CoroutineScope(Dispatchers.Main).launch {
            Log.e("SocketHelper", "addListeners: $BOOKING_CANCEL_ALERT = ${it[0]}")
            globalListeners?.onBookingCancelAlert(JSONObject(it[0].toString()))
        }
    }
    private var logout= Emitter.Listener {
        CoroutineScope(Dispatchers.Main).launch {
            globalListeners?.onSessionEnd()
        }
    }

    fun initSocket() {
        if (socket == null) {
            val options = IO.Options()
            options.reconnection = true
            options.reconnectionAttempts = Int.MAX_VALUE
            options.reconnectionDelay = 1000
//            options.transports = arrayOf(WebSocket.NAME, PollingXHR.NAME)
            options.query = "token=$token"
//            options.path= "/v2/socket.io"
            socket = IO.socket(SOCKET_BASE_URL, options)
            socketOn()
        }
        if(token.isNotBlank())
        socket?.let {
            if (!it.connected() )
                it.connect()
        }
    }

    fun addListeners() {
        try {
            listener(SYNC_DATA, syncData)
            listener(BOOKING_STATUS_CHANGE, onStatusChange)
            listener(BOOKING_CANCEL_ALERT, onCancel)
            listener(LOGOUT,  logout)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun socketOn() {
        listener(Socket.EVENT_CONNECT, onConnect)
        listener(Socket.EVENT_DISCONNECT, onDisconnect)
        listener(Socket.EVENT_CONNECT_ERROR, onConnectError)
    }

    fun disconnectSocket() {
        socket?.disconnect()
        socket = null
        token =""
    }

    fun listener(key: String, emitter: Emitter.Listener) {
        initSocket()
        if (socket?.hasListeners(key) != true) {
            socket?.on(key, emitter)
        }
    }

    fun <T> emit(key: String, data: T) {
        initSocket()
        Log.e("SocketHelper", "emit: $key = $data")
        socket?.emit(key, data)
    }

    fun isConnected(): Boolean {
        return socket?.connected() == true
    }


    interface GlobalListeners {
        fun onSyncData(syncData: Any)
        fun onNewBooking(booking: Any)
        fun onBookingStatusChange(booking: Any)
        fun onBookingCancelAlert(booking: Any)
        fun onSessionEnd()
    }

    fun setAuth(auth: String? = null) {
        try {
            token = auth?:""
            initSocket()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


}