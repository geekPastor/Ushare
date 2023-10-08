package com.chrinovicmm.tolobelacongo.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.chrinovicmm.tolobelacongo.ui.theme.TolobelaCongoTheme
import com.chrinovicmm.tolobelacongo.util.GoogleAuthUiHelper
import com.google.android.gms.auth.api.identity.SignInClient
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var googleAuthUiHelper: GoogleAuthUiHelper
    @Inject lateinit var oneTapClient : SignInClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TolobelaCongoTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()

                    val viewModel : MainViewModel = hiltViewModel()
                    val uiState = viewModel.uiState.value
                    
                    NavHost(navController = navController, startDestination =  "signin"){
                        composable("signin"){

                        }

                        composable("home"){

                        }
                    }
                }
            }
        }
    }
}