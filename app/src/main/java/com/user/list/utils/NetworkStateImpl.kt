package com.user.list.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.user.list.model.NetworkState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket
import javax.inject.Inject

class NetworkStateImpl @Inject constructor(private val ctx: Context) : NetworkState {
    override suspend fun hasInternet(): Boolean {
        return if (isConnected())
            withContext(Dispatchers.IO) {
                try {
                    // Connect to api to check for connection
                    val timeoutMs = 1500
                    val socket = Socket()
                    val socketAddress = InetSocketAddress("www.dummyapi.io", 443)

                    socket.connect(socketAddress, timeoutMs)
                    socket.close()
                    true
                } catch (e: IOException) {
                    false
                }
            }
        else false
    }

    @Suppress("DEPRECATION")
    private fun isConnected(): Boolean {
        val cm = ctx.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val network = cm.activeNetwork ?: return false
            val activeNetwork = cm.getNetworkCapabilities(network) ?: return false
            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            val info = cm.activeNetworkInfo ?: return false
            return info.isConnectedOrConnecting
        }
    }
}