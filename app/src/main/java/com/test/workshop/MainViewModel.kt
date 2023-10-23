package com.test.workshop

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trusense.components.authenticatemanager.AuthenticateManagerDataSource
import com.trusense.components.requests.AuthenticateAccessTokenRequest
import com.trusense.components.responses.AuthenticateManagerResponse
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {

    private val authenticateManagerDataSource = AuthenticateManagerDataSource()

    private val _authenticateToken = MutableStateFlow<AuthenticateManagerResponse?>(null)
    val authenticateToken = _authenticateToken.asStateFlow()

    fun getAuthenticateToken(productId: Int) {
        viewModelScope.launch {
            delay(2000)
            _authenticateToken.value = authenticateManagerDataSource.authenticateToken(
                AuthenticateAccessTokenRequest(productId)
            )
        }
    }
}