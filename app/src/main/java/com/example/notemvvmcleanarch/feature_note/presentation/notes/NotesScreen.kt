package com.example.notemvvmcleanarch.feature_note.presentation.notes

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.notemvvmcleanarch.feature_note.presentation.notes.components.NoteItem
import com.example.notemvvmcleanarch.feature_note.presentation.notes.components.OrderSection
import com.example.notemvvmcleanarch.feature_note.presentation.util.Screen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesScreen(navController: NavController, viewModel: NoteViewModel = hiltViewModel()){
    val state:NotesState = viewModel.state.value
    val snackbarHostState = remember{SnackbarHostState()}
    val scope = rememberCoroutineScope()
    
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Screen.AddEditNoteScreen.route)
            },
                contentColor = MaterialTheme.colorScheme.primary,

            ){
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState)},
        topBar = { 
            TopAppBar(
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                title = {
                      Text(text = "Clean Architecture Notes App")
                },
                navigationIcon = {
                IconButton(onClick = { viewModel.onEvent(NotesEvent.ToggleOrderSection) }) {
                   Icon(imageVector = Icons.Default.Menu, contentDescription = null) 
                }
            } )
        }
    ){

        Column(modifier = Modifier.padding(it).background(MaterialTheme.colorScheme.primary)) {
            AnimatedVisibility(visible = state.isOrderSectionVisible,
                    enter = fadeIn() + slideInVertically(),
                    exit = fadeOut() + slideOutVertically()) {
                OrderSection(
                    onOrderChange ={note -> viewModel.onEvent(NotesEvent.Order(note))},
                    modifier = Modifier
                        .fillMaxWidth()
//                        .padding(vertical = 16.dp)
                        .background(MaterialTheme.colorScheme.primary),
                    noteOrder = state.noteOrder
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn(modifier=Modifier.fillMaxSize().padding(8.dp)){
               items(state.notes){note->
                   NoteItem(note = note,
                       modifier = Modifier
                           .fillMaxWidth()
                           .clickable {
                               navController.navigate(
                                   Screen.AddEditNoteScreen.route
                                           + "?noteID=${note.id}&noteColor=${note.color}"
                               )
                           },
                       onDelete = {
                           viewModel.onEvent(NotesEvent.DeleteNote(note))
                           scope.launch {
                               var result = snackbarHostState
                                   .showSnackbar(message = "Note deleted", actionLabel = "Undo")

                               if (result == SnackbarResult.ActionPerformed){
                                   viewModel.onEvent(NotesEvent.RestoreNote)
                               }
                           }
                       }
                   )
                   Spacer(modifier = Modifier.height(16.dp))
               }
            }
        }
    }


}