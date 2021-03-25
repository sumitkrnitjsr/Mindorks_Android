package com.mindorks.notesapp.mynotes

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.Constraints
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.mindorks.notesapp.NotesApp
import com.mindorks.notesapp.R
import com.mindorks.notesapp.addnotes.AddNotesActivity
import com.mindorks.notesapp.blog.BlogActivity
import com.mindorks.notesapp.detail.DetailActivity
import com.mindorks.notesapp.data.local.db.Notes
import com.mindorks.notesapp.data.local.pref.PrefConstant
import com.mindorks.notesapp.data.local.pref.StoreSession
import com.mindorks.notesapp.mynotes.adapter.NotesAdapter
import com.mindorks.notesapp.mynotes.clicklisteners.ItemClickListener
import com.mindorks.notesapp.util.AppConstant
import com.mindorks.notesapp.util.workmanager.MyWorker
import java.util.*
import java.util.concurrent.TimeUnit


class MyNotesActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MyNotesActivity"
        private const val ADD_NOTES_CODE = 100
    }

    private var fullName: String = ""
    private lateinit var buttonAddNotes: FloatingActionButton
    private lateinit var recyclerViewNotes: RecyclerView
    private var listNotes = ArrayList<Notes>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_notes)
        setupSharedPreference()
        bindViews()
        getIntentData()
        getDataFromDataBase()
        setupToolbarText()
        setupClickListeners()
        setupRecyclerView()
        setupWorkManager()
    }

    private fun getIntentData() {
        val intent = intent
        if (intent.hasExtra(AppConstant.FULL_NAME)) {
            fullName = intent.getStringExtra(AppConstant.FULL_NAME)
        }
        if (fullName.isEmpty()) {
            fullName = StoreSession.readString(PrefConstant.FULL_NAME)!!
        }
    }

    private fun setupWorkManager() {
        val constraint = Constraints.Builder()
                .build()
        val request = PeriodicWorkRequest
                .Builder(MyWorker::class.java, 1, TimeUnit.MINUTES)
                .setConstraints(constraint)
                .build()
        WorkManager.getInstance().enqueue(request)
    }

    private fun getDataFromDataBase() {
        val notesApp = applicationContext as NotesApp
        val notesDao = notesApp.getNotesDb().notesDao()
        listNotes.addAll(notesDao.getAll())
    }

    private fun setupSharedPreference() {
        StoreSession.init(this)
    }

    private fun bindViews() {
        buttonAddNotes = findViewById(R.id.buttonAddNotes)
        recyclerViewNotes = findViewById(R.id.recyclerViewNotes)
    }

    private fun setupClickListeners() {
        buttonAddNotes.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                startActivityForResult(Intent(this@MyNotesActivity, AddNotesActivity::class.java), ADD_NOTES_CODE)
            }
        })
    }

    private fun setupToolbarText() {
        if (supportActionBar != null) {
            supportActionBar?.title = fullName
        }
    }


    private fun setupRecyclerView() {
        val itemClickListener = object : ItemClickListener {
            override fun onUpdate(notes: Notes) {
                val notesApp = applicationContext as NotesApp
                val notesDao = notesApp.getNotesDb().notesDao()
                notesDao.updateNotes(notes)
            }

            override fun onClick(notes: Notes) {
                val intent = Intent(this@MyNotesActivity, DetailActivity::class.java)
                intent.putExtra(AppConstant.TITLE, notes.title)
                intent.putExtra(AppConstant.DESCRIPTION, notes.description)
                startActivity(intent)
            }

        }
        val notesAdapter = NotesAdapter(listNotes, itemClickListener)
        val linearLayoutManager = LinearLayoutManager(this@MyNotesActivity)
        linearLayoutManager.orientation = RecyclerView.VERTICAL
        recyclerViewNotes.layoutManager = linearLayoutManager
        recyclerViewNotes.adapter = notesAdapter
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_NOTES_CODE && resultCode == Activity.RESULT_OK) {
            val title = data?.getStringExtra(AppConstant.TITLE);
            val description = data?.getStringExtra(AppConstant.DESCRIPTION)
            val imagePath = data?.getStringExtra(AppConstant.IMAGE_PATH)

            val note = Notes(title = title!!, description = description!!, imagePath = imagePath!!, isTaskCompleted = false)
            addNotesToDb(note)
            listNotes.add(note)
            recyclerViewNotes.adapter?.notifyItemChanged(listNotes.size - 1)
        }
    }

    private fun addNotesToDb(notes: Notes) {
        val notesApp = applicationContext as NotesApp
        val notesDao = notesApp.getNotesDb().notesDao()
        notesDao.insert(notes)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.blog -> startActivity(Intent(this, BlogActivity::class.java))
        }
        return super.onOptionsItemSelected(item);

    }
}
