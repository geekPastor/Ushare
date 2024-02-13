package com.chrinovicmm.tolobelacongo.ui

import android.annotation.SuppressLint
import android.net.Uri
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
import androidx.core.net.UriCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.chrinovicmm.tolobelacongo.ui.screen.BlogDetailsScreen
import com.chrinovicmm.tolobelacongo.ui.screen.HomeScreen
import com.chrinovicmm.tolobelacongo.ui.screen.SignInScreen
import com.chrinovicmm.tolobelacongo.ui.screen.UpdateBlogScreen
import com.chrinovicmm.tolobelacongo.ui.theme.TolobelaCongoTheme
import com.chrinovicmm.tolobelacongo.util.GoogleAuthUiHelper
import com.google.android.gms.auth.api.identity.SignInClient
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.net.URLEncoder
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var googleAuthUiHelper: GoogleAuthUiHelper
    @Inject lateinit var oneTapClient : SignInClient
    @SuppressLint("SuspiciousIndentation")
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
                                blogs = uiState.blogs,
                                isLoading = uiState.isLoading,
                                signOut = {
                                    viewModel.signOut(oneTapClient)
                                },
                                NavigateToBlogDetailsScreen = {blog ->
                                    val imageEncodedUrl = URLEncoder.encode(blog.thumbnail, "UTF-8")
                                    val pdfEncodedUrl = URLEncoder.encode(blog.pdf, "UTF-8")
                                    navController.navigate(
                                        "blog_details?id=${blog.id}?title=${blog.title}" +
                                                "?content=${blog.content}" +
                                                "?username=${uiState.currentUser}" +
                                                "?thumbnail=$imageEncodedUrl?pdf=$pdfEncodedUrl"
                                    )
                                },
                                navigateToUpdateBogScreen = {
                                    navController.navigate("blog_update")
                                },
                                navigateToSigninScreen = {
                                    navController.popBackStack(
                                        route = "signin", inclusive = false
                                    )
                                }

                            )
                        }

                        composable(
                            route= "blog_details?id={id}?title={title}?content={content}" +
                                    "?username={username}?thumbnail={thumbnail}?pdf={pdf}",
                            arguments = listOf(
                                navArgument(name = "id", builder = {nullable = true}),
                                navArgument(name = "title", builder = {nullable = true}),
                                navArgument(name = "content", builder = {nullable = true}),
                                navArgument(name = "username", builder = {nullable = true}),
                                navArgument(name = "thumbnail", builder = {nullable = true}),
                                navArgument(name = "pdf", builder = {nullable = true})
                            )
                        ){backStackEntry->
                            val id = backStackEntry.arguments?.getString("id")
                            val title = backStackEntry.arguments?.getString("title")
                            val content = backStackEntry.arguments?.getString("content")
                            val username = backStackEntry.arguments?.getString("username")
                            val thumbnail = backStackEntry.arguments?.getString("thumbnail")
                            val pdf = backStackEntry.arguments?.getString("pdf")

                            val encodedUrl = URLEncoder.encode(thumbnail, "UTF-8")
                            val encodedPdfUrl = URLEncoder.encode(pdf, "UTF-8")


                                BlogDetailsScreen(
                                    blogTitle = title,
                                    blogContent = content,
                                    blogThumbnail = thumbnail,
                                    blogUser = username!!,
                                    blogPdf = pdf,
                                    onBackPressed = {
                                        navController.popBackStack()
                                    },
                                    onEditClicked = {
                                                    navController.navigate("blog_update?id=$id?title=$title?content=$content?thumbnail=$encodedUrl?pdf=$encodedPdfUrl")
                                    },
                                    onDeleteClicked = {
                                        viewModel.onDeleteBlog(id!!)
                                        navController.popBackStack()
                                    }
                                )

                        }

                        composable(
                            route = "blog_update?id={id}?title={title}?content={content}?thumbnail={thumbnail}?pdf={pdf}",
                            arguments = listOf(
                                navArgument(name = "id", builder = {nullable = true}),
                                navArgument(name = "title", builder = {nullable = true}),
                                navArgument(name = "content", builder = {nullable = true}),
                                navArgument(name = "thumbnail", builder = {nullable = true}),
                                navArgument(name = "pdf", builder = {nullable = true})
                            )
                        ){backStackEntry->
                            val id = backStackEntry.arguments?.getString("id")
                            val title = backStackEntry.arguments?.getString("title")
                            val content = backStackEntry.arguments?.getString("content")
                            val thumbnail = backStackEntry.arguments?.getString("thumbnail")
                            val pdf = backStackEntry.arguments?.getString("pdf")

                            UpdateBlogScreen(
                                blogTitle = title,
                                blogContent = content,
                                thumbnail = thumbnail,
                                blogPdf = pdf,
                                onBackPressed = { navController.popBackStack() },
                                UpdateBlog = {titleCallBack, contentCallBack, thumbnailCallBack, pdfCallBack->
                                    if (id == null){
                                        viewModel.onAddBlog(
                                            title = titleCallBack,
                                            content = contentCallBack,
                                            thumbnails = Uri.parse(thumbnailCallBack),
                                            pdf = Uri.parse(pdfCallBack),
                                            user = uiState.currentUser!!
                                        )
                                    }else{
                                        viewModel.onUpdateBlog(
                                            id = id,
                                            title = titleCallBack,
                                            content = contentCallBack,
                                            thumbnail = Uri.parse(thumbnailCallBack),
                                            pdf = Uri.parse(pdfCallBack)
                                        )

                                    }
                                    navController.navigate("home"){
                                        popUpTo("home")
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