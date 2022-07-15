package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.roomdb.INotesClicked
import com.example.myapplication.roomdb.Note
import com.example.myapplication.roomdb.NoteAdapter
import com.example.myapplication.roomdb.NoteViewModel

class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null
    lateinit var viewModel: NoteViewModel
    private var adapter: NoteAdapter? = null
    private var appBarConfiguration: AppBarConfiguration? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        supportActionBar?.setDisplayShowHomeEnabled(true)


        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(NoteViewModel::class.java)
        navControllerFunction()

    }

    fun navControllerFunction(){

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentbiew) as NavHostFragment
        val navController = navHostFragment.navController

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.secondFragment
            )
        )

        navController?.let { controller ->
            appBarConfiguration?.let { appbar ->
                setupActionBarWithNavController(controller, appbar)
            }
        }

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration!!)

        navController?.addOnDestinationChangedListener { src, destination, bundle ->
            when (destination.id) {
                R.id.secondFragment -> {
                   // supportActionBar?.show()
                }

                R.id.dataShown -> {
                   // supportActionBar?.show()
                }

                R.id.galleryFragment -> {
                    //supportActionBar?.show()
                }
            }
        }
    }
}