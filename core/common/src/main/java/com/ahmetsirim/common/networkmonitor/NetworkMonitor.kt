package com.ahmetsirim.common.networkmonitor

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkMonitor @Inject constructor(
    @param:ApplicationContext
    private val applicationContext: Context,
) {

    /**
     * Checks if the device is currently connected to the internet.
     *
     * This function uses the system's connectivity manager to determine the active network
     * and checks if it has either a Wi-Fi or cellular transport type, indicating an internet connection.
     *
     * @return `true` if the device is connected to the internet, `false` otherwise.
     */
    @SuppressLint("MissingPermission") // Although I declare permission to Manifest, a warning appears. This suppress has been added to close the warning
    fun isInternetConnected(): Boolean {
        val connectivityManager = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val network = connectivityManager.activeNetwork ?: return false

        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> false
        }
    }

    /**
     * Retrieves a flow that emits network connectivity status updates.
     *
     * This function returns a Flow of Boolean values which indicate the current
     * state of network connectivity. The flow will emit updates whenever the network status
     * changes, such as when a network becomes available or is lost.
     * It will also emit the current network status when collection starts.
     *
     * @return A Flow of Boolean that emits true when connected, false otherwise.
     *
     * Example usage in a Fragment:
     * ```
     * class NetworkStatusFragment : Fragment() {
     *
     *     override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
     *         super.onViewCreated(view, savedInstanceState)
     *
     *         // Collect network status updates with flowWithLifecycle
     *         viewLifecycleOwner.lifecycleScope.launch {
     *             getNetworkConnectivityFlow()
     *                 .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
     *                 .collect { isConnected ->
     *                     if (isConnected) {
     *                         // Handle network available
     *                         // Example: show a message or update UI
     *                     } else {
     *                         // Handle network unavailable
     *                         // Example: show a message or update UI
     *                     }
     *                 }
     *         }
     *     }
     * }
     * ```
     */
    @SuppressLint("MissingPermission") // Although I declare permission to Manifest, a warning appears. This suppress has been added to close the warning
    fun getNetworkConnectivityFlow(): Flow<Boolean> {
        return callbackFlow {
            val connectivityManager = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            trySend(isInternetConnected())

            val networkCallback = object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    trySend(true)
                }

                override fun onLost(network: Network) {
                    super.onLost(network)
                    trySend(false)
                }

                override fun onUnavailable() {
                    super.onUnavailable()
                    trySend(false)
                }
            }

            val networkRequest = NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build()

            connectivityManager.registerNetworkCallback(networkRequest, networkCallback)

            awaitClose { connectivityManager.unregisterNetworkCallback(networkCallback) }
        }
    }

}