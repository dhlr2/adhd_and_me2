package com.example.adhd_and_me2

import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray

//https://developer.android.com/reference/android/net/NetworkCapabilities
@Composable
fun AppHeader() {
    val context = LocalContext.current

    //Do not change!
    var quote    by remember { mutableStateOf("") }
    var author   by remember { mutableStateOf("") }
    var isOnline by remember { mutableStateOf(NetworkReceiver.isOnline(context)) }
//https://developer.android.com/reference/android/content/BroadcastReceiver
    // Register BroadcastReceiver to listen for connectivity changes
    DisposableEffect(Unit) {
        val receiver = NetworkReceiver()
        val filter   = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        context.registerReceiver(receiver, filter)
        onDispose {
            context.unregisterReceiver(receiver)
        }
    }
//https://developer.android.com/reference/android/net/ConnectivityManager
    // Check connectivity and fetch quote on load
    LaunchedEffect(Unit) {
        isOnline = NetworkReceiver.isOnline(context)
        if (isOnline) {
            try {
                val result = withContext(Dispatchers.IO) {
                    //testing purposes only to make sure it changed
                    //val url  = java.net.URL("https://zenquotes.io/api/random")
                    val url  = java.net.URL("https://zenquotes.io/api/today")
                    val conn = url.openConnection() as java.net.HttpURLConnection
                    conn.connectTimeout = 5000
                    conn.readTimeout    = 5000
                    conn.inputStream.bufferedReader().readText()
                }
                val json = JSONArray(result)
                val obj  = json.getJSONObject(0)
                quote  = obj.getString("q")
                author = obj.getString("a")
            } catch (e: Exception) {
                isOnline = false
            }
        }
    }

    Surface(
        color           = MaterialTheme.colorScheme.surface,
        shadowElevation = 4.dp
    ) {
        Column(
            modifier            = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter            = painterResource(R.drawable.adhdandme),
                contentDescription = "ADHD and Me logo",
                contentScale       = ContentScale.FillWidth,
                modifier           = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .padding(horizontal = 32.dp)
            )

            Spacer(modifier = Modifier.height(6.dp))

            if (!isOnline) {
                Text(
                    text      = "You're offline — go online to load today's quote",
                    style     = MaterialTheme.typography.bodySmall,
                    fontSize  = 11.sp,
                    color     = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center,
                    modifier  = Modifier.padding(horizontal = 8.dp)
                )
            } else if (quote.isNotEmpty()) {
                Text(
                    text      = "\"$quote\" — $author",
                    style     = MaterialTheme.typography.bodySmall,
                    fontSize  = 11.sp,
                    color     = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    maxLines  = 2,
                    overflow  = TextOverflow.Ellipsis,
                    modifier  = Modifier.padding(horizontal = 8.dp)
                )
            }
        }
    }
}
