package com.example.ktor_jwt_demo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ktor_jwt_demo.data.repo.DataStoreRepository
import com.example.ktor_jwt_demo.ui.auth.Auth
import com.example.ktor_jwt_demo.ui.auth.AuthViewModel
import com.example.ktor_jwt_demo.ui.home.Home
import com.example.ktor_jwt_demo.ui.home.HomeViewModel
import com.example.ktor_jwt_demo.ui.theme.KtorJWTdemoTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var dataStoreRepository: DataStoreRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val authViewModel: AuthViewModel by viewModels()
        val homeViewModel: HomeViewModel by viewModels()

        setContent {
            var startDestination by remember {
                mutableStateOf("home")
            }
            LaunchedEffect(Unit){
                dataStoreRepository.getValue(DataStoreRepository.TOKEN)?: run{
                    startDestination = "auth"
                }
            }

            KtorJWTdemoTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = startDestination) {
                    composable("auth") {
                        Auth(navController, authViewModel)
                    }
                    composable("home") {
                        Home(navController, homeViewModel)
                    }
                }
            }
        }
    }
}