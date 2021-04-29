package pt.atp.cidadesinteligentes

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import pt.atp.cidadesinteligentes.adapter.DESC
import pt.atp.cidadesinteligentes.adapter.IDOCO
import pt.atp.cidadesinteligentes.adapter.OCORR
import pt.atp.cidadesinteligentes.api.EndPoints
import pt.atp.cidadesinteligentes.api.ServiceBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditOcorrencia : AppCompatActivity() {
    private lateinit var desc: EditText
    private lateinit var tit: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_ocorrencia)

        val desc_edit = intent.getStringExtra(DESC)
        findViewById<EditText>(R.id.ocodescription).setText(desc_edit)

        val tit_edi = intent.getStringExtra(OCORR)
        findViewById<EditText>(R.id.ocotitle).setText(tit_edi)

        val save_Edit = findViewById<Button>(R.id.save)
        save_Edit.setOnClickListener {
            guardarEdit2()
            finish()
        }

        val cancel_edit = findViewById<Button>(R.id.cancel_edit)
        cancel_edit.setOnClickListener {
            val intent = Intent(this, ListaOcorrenciasUser::class.java)
            startActivity(intent)
            finish()
        }


    }

    fun guardarEdit2() {
        tit = findViewById(R.id.ocotitle)
        desc = findViewById(R.id.ocodescription)


        var id_oco_edit: Int = intent.getIntExtra(IDOCO, 0)
        val tituEnv: String = tit.text.toString()
        val descEnv: String = desc.text.toString()

        Log.d("NAOOOOO", id_oco_edit.toString())

        val replyIntent = Intent()
        if (TextUtils.isEmpty(desc.text.toString()) && TextUtils.isEmpty(tit.text.toString())) {
            setResult(Activity.RESULT_CANCELED, replyIntent)
            Toast.makeText(this, R.string.warn, Toast.LENGTH_SHORT).show()

        } else {
            Log.d("TITULOENV", tituEnv)
            Log.d("DESCENV", descEnv)

            val request = ServiceBuilder.buildService(EndPoints::class.java)
            val call = request.editaOcorrencia(id_oco_edit, tituEnv, descEnv)
            Log.d("MOAO", call.toString())
            call.enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    if (response.isSuccessful) {
                        Log.d("SIMMMM2", "s")
                        Toast.makeText(applicationContext, R.string.save, Toast.LENGTH_LONG).show()
                        val intent = Intent(this@EditOcorrencia, ListaOcorrenciasUser::class.java)
                        startActivity(intent)
                        finish()
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.d("NALOL", "n")
                    Toast.makeText(applicationContext, "${t.message}", Toast.LENGTH_SHORT).show()
                    Log.d("NALOL", t.message.toString())
                }
            })


        }
        finish()
    }
}