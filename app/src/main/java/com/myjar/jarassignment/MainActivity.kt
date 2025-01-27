package com.myjar.jarassignment

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.myjar.jarassignment.ui.composables.AppNavigation
import com.myjar.jarassignment.ui.theme.JarAssignmentTheme
import com.myjar.jarassignment.ui.vm.JarViewModel

class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<JarViewModel>()

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JarAssignmentTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val context = LocalContext.current
                    LaunchedEffect(Unit) {
                        viewModel.checkConnectivity(context)
                        viewModel.fetchData(context = context)
                    }
                    AppNavigation(
                        modifier = Modifier.padding(innerPadding),
                        viewModel = viewModel,
                    )
                }
            }
        }
    }
}