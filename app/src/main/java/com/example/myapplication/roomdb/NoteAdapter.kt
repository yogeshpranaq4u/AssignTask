package com.example.myapplication.roomdb

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R

class NoteAdapter(val context: Context, val listner: INotesClicked) :
    RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    var allNotes = ArrayList<Note>()

    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.note_text)
        val delete: Button = itemView.findViewById(R.id.delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val viewHolder =
            NoteViewHolder(LayoutInflater.from(context).inflate(R.layout.item_note, parent, false))
        viewHolder.delete.setOnClickListener {
            listner.onItemClicked(allNotes[viewHolder.absoluteAdapterPosition])
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        Log.d("Hello", "onBindViewHolder: ${allNotes[position].text}")
        holder.textView.text = allNotes[position].text
    }

    override fun getItemCount(): Int {
        return allNotes.size
    }
    fun updateList(newlist:List<Note>){
        allNotes.clear()
        allNotes.addAll(newlist)
    }
}

interface INotesClicked {
    fun onItemClicked(note: Note)
}