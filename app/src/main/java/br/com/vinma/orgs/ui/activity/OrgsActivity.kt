package br.com.vinma.orgs.ui.activity

import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

open class OrgsActivity: AppCompatActivity() {
    companion object{
        val scope = CoroutineScope(Dispatchers.IO)
    }
}