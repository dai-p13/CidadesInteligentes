package pt.atp.cidadesinteligentes


import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import pt.atp.cidadesinteligentes.adapter.IDDEL
import pt.atp.cidadesinteligentes.adapter.IDOCO
import pt.atp.cidadesinteligentes.api.EndPoints
import pt.atp.cidadesinteligentes.api.Ocorrencia
import pt.atp.cidadesinteligentes.api.ServiceBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DeleteOcorrencia : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var id_oco_del = intent.getIntExtra(IDDEL, 0)

        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.alert)
        builder.setMessage(R.string.del)
        builder.setPositiveButton(R.string.yes){ dialog, which ->
            Toast.makeText(applicationContext, R.string.delete, Toast.LENGTH_LONG).show()
            val request =  ServiceBuilder.buildService(EndPoints::class.java)
            val call = request.eliminaOcorrencia(id_oco_del)
            call.enqueue(object : Callback<Ocorrencia> {
                override fun onResponse(call: Call<Ocorrencia>, response: Response<Ocorrencia>) {
                    if (response.isSuccessful){
                        Log.d("SIMMMM2", "s")
                        Toast.makeText(applicationContext, R.string.save, Toast.LENGTH_LONG).show()
                        val intent = Intent(this@DeleteOcorrencia, ListaOcorrenciasUser::class.java)
                        startActivity(intent)
                        finish()
                    }
                }

                override fun onFailure(call: Call<Ocorrencia>, t: Throwable) {
                    Log.d("NALOL", "n")
                    Toast.makeText(applicationContext, "${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
            finish()
        }
        builder.setNegativeButton(R.string.no){dialog, which ->
            val intent = Intent(this, ListaOcorrenciasUser::class.java)
            startActivity(intent)
            finish()
        }
        builder.show()

    }

}