package com.example.notemvvmcleanarch.feature_note.domain.use_case

import com.example.notemvvmcleanarch.feature_note.domain.model.InvalidNoteException
import com.example.notemvvmcleanarch.feature_note.domain.model.Note
import com.example.notemvvmcleanarch.feature_note.domain.repository.NoteRepository

class AddNote(private val repository: NoteRepository) {
    @Throws(InvalidNoteException::class)
    suspend operator fun invoke(note: Note){
        if (note.title.isBlank()) {
            throw InvalidNoteException("title of note is empty")
        }

        if (note.content.isBlank()) {
            throw InvalidNoteException("content of note is empty")
        }

        repository.insertNote(note)
    }
}