package com.test.workshop

import android.content.Context
import android.os.Bundle
import android.webkit.WebViewClient
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.test.workshop.databinding.ActivityMainBinding
import com.trusense.components.interceptormanager.AuthenticateManagerClientInstance
import com.trusense.components.networkdetection.ConnectivityObserver
import com.trusense.components.networkdetection.NetworkConnectivityObserver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var connectivityObserver: ConnectivityObserver

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        AuthenticateManagerClientInstance.authorization = "QVBJXzU1MUU4QjE1QkU1NDphUGpodXBIcGFoRXVxc2hx"
        AuthenticateManagerClientInstance.x_api_key = "OgA59SKD8RbBbSAA4&jcilub7xYODBk@NFYtv8@cf!VTNh2k@k5sSWWB8ksAgTa6Ranfia92p4nV4u6eaA4RV9pemf8YuLz!9!9Pmr2sgb885fhN3NMbA\$OMOA5NfWiS"

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        connectivityObserver = NetworkConnectivityObserver(applicationContext)
        val networkConnectivityObserver = NetworkConnectivityObserver(applicationContext)

        val mainScope = CoroutineScope(Dispatchers.Main)

        mainScope.launch {
            connectivityObserver.observe().collectLatest {
                // Realiza acciones basadas en los datos del flujo
                when (it) {
                    ConnectivityObserver.Status.Available -> {
                        Toast.makeText(this@MainActivity, "Data Mobile Available", Toast.LENGTH_SHORT).show()
                        binding.btnCheckNetwork.isEnabled = true
                        binding.btnCheckNetwork.setOnClickListener {
                            Thread {
                                runOnUiThread {
                                    networkConnectivityObserver.checkInternetConnectivity { connected ->
                                        runOnUiThread {
                                            if (connected) {
                                                // La conexión a Internet está funcionando
                                                binding.webView.loadUrl("https://www.google.com")
                                                binding.webView.webViewClient = WebViewClient()
                                                binding.btnOpenDialog.isEnabled = true
                                                binding.btnOpenDialog.setOnClickListener {
                                                    showInputDialog(this@MainActivity)
                                                }
                                            } else {
                                                // No se detecta una conexión a Internet
                                                Toast.makeText(this@MainActivity, "Internet connection not detected", Toast.LENGTH_SHORT)
                                                    .show()
                                            }
                                        }
                                    }
                                }
                            }.start()
                        }
                    }
                    ConnectivityObserver.Status.Unavailable -> {
                        binding.btnCheckNetwork.isEnabled = false
                        Toast.makeText(this@MainActivity, "Data Mobile UnAvailable", Toast.LENGTH_SHORT).show()
                    }
                    ConnectivityObserver.Status.Losing -> {
                        binding.btnCheckNetwork.isEnabled = false
                        Toast.makeText(this@MainActivity, "Not network detected", Toast.LENGTH_SHORT).show()
                    }
                    ConnectivityObserver.Status.Lost -> {
                        binding.btnCheckNetwork.isEnabled = false
                        Toast.makeText(this@MainActivity, "Not network on reach", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun showInputDialog(context: Context) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_input, null)
        val editText1 = dialogView.findViewById<EditText>(R.id.editText1)

        val alertDialog = AlertDialog.Builder(context)
            .setTitle("Ingresar el product id")
            .setView(dialogView)
            .setPositiveButton("Aceptar") { _, _ ->
                val productId = editText1.text.toString().toInt()
                viewModel.getAuthenticateToken(productId = productId)
                lifecycleScope.launch {
                    viewModel.authenticateToken.collect { result ->
                        if (result?.message != null) {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(context, "Configura tu Api Key y Authorization", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
            .create()

        alertDialog.show()
    }
}
