package com.example.myapplication.roomdb

import androidx.lifecycle.LiveData

class NoteRepository(private val noteDao: Dao) {
    val allNotes: LiveData<List<Note>> = noteDao.getAllNotes()

    suspend fun insert(note: Note){
        noteDao.insert(note)
    }

    suspend fun delete(note: Note){
        noteDao.delete(note)
    }
}