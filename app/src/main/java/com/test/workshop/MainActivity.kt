package com.test.workshop

import android.content.Context
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.test.workshop.databinding.ActivityMainBinding
import com.trusense.components.interceptormanager.AuthenticateManagerClientInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        AuthenticateManagerClientInstance.authorization = "your authorization"
        AuthenticateManagerClientInstance.x_api_key = "your api key"

        val mainScope = CoroutineScope(Dispatchers.Main)

    }

    private fun showInputDialog(context: Context) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_input, null)
        val editText1 = dialogView.findViewById<EditText>(R.id.editText1)

        val alertDialog = AlertDialog.Builder(context)
            .setTitle("Ingresar el product id")
            .setView(dialogView)
            .setPositiveButton("Aceptar") { _, _ ->
                val productId = editText1.text.toString().toInt()

                lifecycleScope.launch {

                }
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
            .create()

        alertDialog.show()
    }
}
