package pt.atp.cidadesinteligentes

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import pt.atp.cidadesinteligentes.adapter.DESC
import pt.atp.cidadesinteligentes.adapter.IDOCO
import pt.atp.cidadesinteligentes.adapter.OCORR
import pt.atp.cidadesinteligentes.adapter.TITULO
import pt.atp.cidadesinteligentes.api.EndPoints
import pt.atp.cidadesinteligentes.api.Ocorrencia
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

    }

    fun guardarEdit2(view: View) {
        desc = findViewById(R.id.ocodescription)
        tit = findViewById(R.id.ocotitle)


        val id_oco_edit = intent.getIntExtra(IDOCO,0)

        Log.d("NAOOOOO", id_oco_edit.toString())

        val replyIntent = Intent()
        if (TextUtils.isEmpty(desc.text.toString()) && TextUtils.isEmpty(tit.text.toString()))  {
            setResult(Activity.RESULT_CANCELED, replyIntent)
            //Toast.makeText(this,R.string.sav, Toast.LENGTH_SHORT).show()

        } else {
            Log.d("SIMMMM1", desc.text.toString())

            val request = ServiceBuilder.buildService(EndPoints::class.java)
            val call = request.editaOcorrencia(tit.text.toString(), desc.text.toString(), id_oco_edit)

            call.enqueue(object : Callback<Ocorrencia> {
                override fun onResponse(call: Call<Ocorrencia>, response: Response<Ocorrencia>) {
                    if (response.isSuccessful){
                        Log.d("SIMMMM2", "s")
                        Toast.makeText(
                            applicationContext,
                            R.string.save,
                            Toast.LENGTH_LONG).show()


                    }
                }

                override fun onFailure(call: Call<Ocorrencia>, t: Throwable) {
                    Log.d("SIMMMM2", "n")
                    Toast.makeText(
                        applicationContext,
                        R.string.save,
                        Toast.LENGTH_LONG).show()
                }
            })


        }
        finish()
    }
}