package com.chrinovicmm.tolobelacongo.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.chrinovicmm.tolobelacongo.ui.screen.HomeScreen
import com.chrinovicmm.tolobelacongo.ui.screen.SignInScreen
import com.chrinovicmm.tolobelacongo.ui.theme.TolobelaCongoTheme
import com.chrinovicmm.tolobelacongo.util.GoogleAuthUiHelper
import com.google.android.gms.auth.api.identity.SignInClient
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
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
                            val launcher = rememberLauncherForActivityResult(
                                contract = ActivityResultContracts.StartIntentSenderForResult(),
                                onResult ={result->
                                    if (result.resultCode == RESULT_OK){
                                        lifecycleScope.launch {
                                            val signInResult = googleAuthUiHelper.getSignInResultFromIntentAndSignIn(
                                                intent = result.data ?: return@launch
                                            )

                                            viewModel.onSignInResult(signInResult)
                                        }
                                    }
                                }
                            )

                            LaunchedEffect(key1 = Unit){
                                if (uiState.currentUser != null){
                                    navController.navigate("home")
                                }
                            }
                            
                            LaunchedEffect(key1 = uiState.isSignInSuccessfull){
                                if (uiState.isSignInSuccessfull){
                                    navController.navigate("home")
                                    viewModel.resetSignInState()
                                }
                            }


                            SignInScreen(
                                isLoading = uiState.isLoading,
                                currentUser = uiState.currentUser
                            ) {
                                lifecycleScope.launch {
                                    val signInItentSender = googleAuthUiHelper.actionSignin()
                                    launcher.launch(
                                        IntentSenderRequest.Builder(
                                            signInItentSender ?: return@launch
                                        ).build()
                                    )
                                }
                            }
                        }

                        composable("home"){
                            HomeScreen(
                                currentUser = uiState.currentUser,
                                signOut = {
                                    viewModel.signOut(oneTapClient)
                                    navController.navigate("signin"){
                                        popUpTo(0)
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}