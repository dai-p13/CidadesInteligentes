package pt.atp.cidadesinteligentes

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText

class AddNota : AppCompatActivity() {

    private lateinit var titleText: EditText
    private lateinit var descriptionText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_nota)

        titleText = findViewById(R.id.title)
        descriptionText = findViewById(R.id.description)

        val button = findViewById<Button>(R.id.save)
        button.setOnClickListener {
            val replyIntent = Intent()
            if (TextUtils.isEmpty(titleText.text)) {
                setResult(Activity.RESULT_CANCELED, replyIntent)
            } else {
                replyIntent.putExtra(EXTRA_REPLY_TITLE, titleText.text.toString())
                replyIntent.putExtra(EXTRA_REPLY_DESCRIPTION, descriptionText.text.toString())
                setResult(Activity.RESULT_OK, replyIntent)
            }
            finish()
        }

        val buttonBack = findViewById<Button>(R.id.back)
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