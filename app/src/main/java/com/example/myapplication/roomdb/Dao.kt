package com.example.myapplication.roomdb

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Dao

@Dao
interface Dao {
    @Insert
     fun insert(note: Note)

    @Delete
     fun delete(note: Note)

    @Query("Select * from note_table order by id ASC")
    fun getAllNotes(): LiveData<List<Note>>
}