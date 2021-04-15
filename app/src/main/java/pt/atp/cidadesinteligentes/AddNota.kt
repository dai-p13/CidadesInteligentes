package pt.atp.cidadesinteligentes

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class AddNota : AppCompatActivity() {

    private lateinit var titleText: EditText
    private lateinit var descriptionText: EditText
    private val newNoteActivityRequestCode2 = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_nota)

        titleText = findViewById(R.id.ocotitle)
        descriptionText = findViewById(R.id.ocodescription)

        val button = findViewById<Button>(R.id.save)
        button.setOnClickListener {
            val replyIntent = Intent()
            if (TextUtils.isEmpty(titleText.text) || TextUtils.isEmpty(descriptionText.text)) {
                setResult(Activity.RESULT_CANCELED, replyIntent)
                startActivityForResult(intent, newNoteActivityRequestCode2)
                Toast.makeText(applicationContext, R.string.empty, Toast.LENGTH_LONG).show()
            } else {
                replyIntent.putExtra(EXTRA_REPLY_TITLE, titleText.text.toString())
                replyIntent.putExtra(EXTRA_REPLY_DESCRIPTION, descriptionText.text.toString())
                setResult(Activity.RESULT_OK, replyIntent)
            }
            finish()
        }

        val buttonBack = findViewById<Button>(R.id.cancel)
        buttonBack.setOnClickListener{
            val intent = Intent(this, Notas::class.java)
            startActivity(intent)
        }

    }
    companion object {
        const val EXTRA_REPLY_TITLE = "com.example.android.title"
        const val EXTRA_REPLY_DESCRIPTION = "com.example.android.description"
    }

}