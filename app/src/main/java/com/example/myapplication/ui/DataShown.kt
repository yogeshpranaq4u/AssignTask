package com.example.myapplication.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentDataShownBinding
import com.example.myapplication.roomdb.INotesClicked
import com.example.myapplication.roomdb.Note
import com.example.myapplication.roomdb.NoteAdapter
import com.example.myapplication.roomdb.NoteViewModel

class DataShown : Fragment(), INotesClicked {

    private var binding: FragmentDataShownBinding?= null
    private val viewModel: NoteViewModel by activityViewModels()
    private var noteAdapter: NoteAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_data_shown, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDataShownBinding.bind(view)
        noteAdapter = activity?.let { NoteAdapter(it,this) }
        binding?.recyclerview?.adapter = noteAdapter
    }

    override fun onItemClicked(note: Note) {
        viewModel.deleteNote(note)
        Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show()
    }

    fun observe() {
        viewModel.allNotes.observe(viewLifecycleOwner, Observer {list->
            list.let {
                noteAdapter?.updateList(list)
            }

        })
    }

    fun listners(){
        binding?.update?.setOnClickListener {
            var noteText = binding?.editext?.text.toString()
            Log.d("Hello", "text update: $noteText")
            if(noteText.isNotEmpty()){
                viewModel.insertNote(Note(noteText))
            }
            Toast.makeText(context, "Inserted", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        listners()
        observe()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home ->{
                findNavController().navigateUp()
            }
        }
        return super.onOptionsItemSelected(item)
    }

}