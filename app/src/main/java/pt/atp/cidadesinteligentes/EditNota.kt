package pt.atp.cidadesinteligentes

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import pt.atp.cidadesinteligentes.adapter.DESCRICAO
import pt.atp.cidadesinteligentes.adapter.ID
import pt.atp.cidadesinteligentes.adapter.TITULO
import pt.atp.cidadesinteligentes.ententies.Notes
import pt.atp.cidadesinteligentes.viewModel.NoteViewModel

class EditNota : AppCompatActivity() {
    private lateinit var desc: EditText
    private lateinit var title: EditText
    private lateinit var notaViewModel: NoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_nota)

        val editTitl = intent.getStringExtra(TITULO)
        val editDesc = intent.getStringExtra(DESCRICAO)


        findViewById<EditText>(R.id.title).setText(editTitl)
        findViewById<EditText>(R.id.description).setText(editDesc)

        notaViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)


        val buttonCancel = findViewById<Button>(R.id.cancel)
        buttonCancel.setOnClickListener {
            val intent = Intent(this, Notas::class.java)
            startActivity(intent)
        }
    }

    fun Editar(view: View) {
        title = findViewById(R.id.title)
        desc = findViewById(R.id.description)

        var message3 = intent.getIntExtra(ID, 0)
        val replyIntent = Intent()
        if (TextUtils.isEmpty(title.text) || TextUtils.isEmpty(desc.text))  {
            setResult(Activity.RESULT_CANCELED, replyIntent)

        } else {
            val nota = Notes(id = message3, title = title.text.toString(), description = desc.text.toString())
            notaViewModel.updateNote(nota)


        }
        finish()
    }
}