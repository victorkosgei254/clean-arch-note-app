package com.example.notemvvmcleanarch.feature_note.presentation.add_edit_note

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notemvvmcleanarch.feature_note.domain.model.InvalidNoteException
import com.example.notemvvmcleanarch.feature_note.domain.model.Note
import com.example.notemvvmcleanarch.feature_note.domain.use_case.NoteUseCases
import com.example.notemvvmcleanarch.ui.theme.Pink40
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditNoteViewModel
    @Inject constructor(private val noteUseCases: NoteUseCases,
                        savedStateHandle: SavedStateHandle) : ViewModel() {

        private val _noteTitle = mutableStateOf<NoteTextFieldState>(NoteTextFieldState(
            hint = "Note title here..."
        ))
        val noteTitle : State<NoteTextFieldState> = _noteTitle

        private val _noteContent = mutableStateOf<NoteTextFieldState>(NoteTextFieldState(
            hint = "Note content here..."
        ))
        val noteContent : State<NoteTextFieldState> = _noteContent

        private val _noteColor: MutableState<Int> = mutableStateOf<Int>(Pink40.toArgb())
        val noteColor: State<Int> = _noteColor

        private val _eventFlow = MutableSharedFlow<UiEvent>()
        val eventFlow = _eventFlow.asSharedFlow()

        private var currentNoteID: Int? = null

        init {
            savedStateHandle.get<Int>("noteID")?.let {
                if (it != -1){
                    viewModelScope.launch {
                        noteUseCases.getNote(it)?.also {note->
                            currentNoteID = note.id
                            _noteTitle.value = noteTitle.value.copy(
                                text = note.title,
                                isHintVisible = false
                            )

                            _noteContent.value = noteContent.value.copy(
                                text = note.content,
                                isHintVisible = false
                            )

                            _noteColor.value = note.color

                        }
                    }

                }
            }
        }

        fun onEvent(event:AddEditNoteEvent){
            when(event){
                is AddEditNoteEvent.SaveNote -> {
                    viewModelScope.launch {
                        try {
                            noteUseCases.addNote(Note(title = noteTitle.value.text,
                                content = noteContent.value.text,
                                color = noteColor.value,
                                timestamp = System.currentTimeMillis(),
                                id = currentNoteID
                            ))
                            _eventFlow.emit(UiEvent.SaveNote)
                        }catch (e: InvalidNoteException){
                            _eventFlow.emit(UiEvent.ShowSnackbar(
                                message = e.message ?: "An error occurred"))
                        }

                    }
                }

                is AddEditNoteEvent.ChangeColor -> {
                    _noteColor.value = event.color
                }

                is AddEditNoteEvent.ChangeContentFocus -> {
                    _noteContent.value = noteContent.value.copy(
                        isHintVisible = !event.focusState.isFocused &&
                                        noteContent.value.text.isBlank()
                    )
                }

                is AddEditNoteEvent.ChangeTitleFocus -> {
                    _noteTitle.value = noteTitle.value.copy(
                        isHintVisible = !event.focusState.isFocused
                                && noteTitle.value.text.isBlank()
                    )
                }

                is AddEditNoteEvent.EnteredContent -> {
                    _noteContent.value = noteContent.value.copy(
                        text = event.value
                    )
                }
                is AddEditNoteEvent.EnteredTitle -> {
                    _noteTitle.value = noteTitle.value.copy(
                        text = event.value
                    )
                }
            }
        }
        sealed class UiEvent{
            data class ShowSnackbar(val message:String) : UiEvent()
            object SaveNote: UiEvent()
        }
}