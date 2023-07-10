package com.example.notemvvmcleanarch.feature_note.domain.model

import androidx.compose.ui.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.notemvvmcleanarch.ui.theme.Blue
import com.example.notemvvmcleanarch.ui.theme.BlueSmooth
import com.example.notemvvmcleanarch.ui.theme.BrightOrange
import com.example.notemvvmcleanarch.ui.theme.DeepPurple
import com.example.notemvvmcleanarch.ui.theme.GreenSmooth
import com.example.notemvvmcleanarch.ui.theme.LimeGreen
import com.example.notemvvmcleanarch.ui.theme.Orange
import com.example.notemvvmcleanarch.ui.theme.Pink40
import com.example.notemvvmcleanarch.ui.theme.Pink80
import com.example.notemvvmcleanarch.ui.theme.Purple40
import com.example.notemvvmcleanarch.ui.theme.Purple80
import com.example.notemvvmcleanarch.ui.theme.SkyBlue
import com.example.notemvvmcleanarch.ui.theme.Yellow

@Entity
data class Note (
    val title:String,
    val content:String,
    val timestamp: Long,
    val color: Int,
    @PrimaryKey
    val id : Int? = null
){
    companion object{
        val noteColors : List<Color> = listOf(Purple40,
            Purple80,
            Pink40, Pink80, GreenSmooth, Yellow, DeepPurple,
            Orange, BlueSmooth, Blue, SkyBlue, LimeGreen, BrightOrange
        )
    }
}


class InvalidNoteException(message:String) : Exception(message)