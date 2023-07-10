package com.example.notemvvmcleanarch.feature_note.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.notemvvmcleanarch.feature_note.presentation.add_edit_note.AddEditNoteScreen
import com.example.notemvvmcleanarch.feature_note.presentation.notes.NotesScreen
import com.example.notemvvmcleanarch.feature_note.presentation.util.Screen
import com.example.notemvvmcleanarch.ui.theme.NoteMVVMCleanArchTheme
import com.example.notemvvmcleanarch.ui.theme.Pink40
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NoteMVVMCleanArchTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()
                    NavHost(navController = navController,
                        startDestination = Screen.NotesScreen.route ){
                        composable(route = Screen.NotesScreen.route){
                            NotesScreen(navController = navController)
                        }

                        composable(
                            route = "${Screen.AddEditNoteScreen.route}?noteID={noteID}&noteColor={noteColor}",
                            arguments = listOf(
                                navArgument(name = "noteID"){
                                    type= NavType.IntType
                                    defaultValue = -1
                                },
                                navArgument(name = "noteColor"){
                                    type= NavType.IntType
                                    defaultValue = -1
                                }
                            )
                        ){
                            var color = it.arguments?.getInt("noteColor") ?: -1
                            AddEditNoteScreen(navController = navController, noteColor = color )
                        }
                    }

                }
            }
        }
    }
}
