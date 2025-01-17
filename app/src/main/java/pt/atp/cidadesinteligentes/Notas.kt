package pt.atp.cidadesinteligentes

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pt.atp.cidadesinteligentes.adapter.NoteAdapter
import pt.atp.cidadesinteligentes.ententies.Notes
import pt.atp.cidadesinteligentes.viewModel.NoteViewModel

class Notas : AppCompatActivity(), NoteAdapter.CallbackInterface {

    private lateinit var noteViewModel: NoteViewModel
    private val newNoteActivityRequestCode = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notas)

        //recycler view
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        val adapter = NoteAdapter(this, this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        //view model
        noteViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)
        noteViewModel.allNotes.observe(this, Observer { notes ->
            notes?.let { adapter.setNotes(it) }
        })

        val buttaoAd = findViewById<Button>(R.id.buttonAdd)
        buttaoAd.setOnClickListener {
            val intent = Intent(this@Notas, AddNota::class.java)
            startActivityForResult(intent, newNoteActivityRequestCode)
        }
        val buttonBack = findViewById<Button>(R.id.buttonBack)
        buttonBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == newNoteActivityRequestCode && resultCode == Activity.RESULT_OK) {
            val ptitle = data?.getStringExtra(AddNota.EXTRA_REPLY_TITLE)
            val pdescription = data?.getStringExtra(AddNota.EXTRA_REPLY_DESCRIPTION)

            if (ptitle != null && pdescription != null) {
                val city = Notes(title = ptitle, description = pdescription)
                noteViewModel.insert(city)
            }

        } else {
            Toast.makeText(applicationContext, R.string.empty, Toast.LENGTH_LONG).show()
        }
    }

    override fun passResultCallback(id: Int?) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.alert)
        builder.setMessage(R.string.del)
        builder.setPositiveButton(R.string.yes) { dialog, which ->
            Toast.makeText(applicationContext, R.string.delete, Toast.LENGTH_LONG).show()
            noteViewModel.deleteById(id)
        }
        builder.setNegativeButton(R.string.no) { dialog, which ->
        }
        builder.show()


    }
}