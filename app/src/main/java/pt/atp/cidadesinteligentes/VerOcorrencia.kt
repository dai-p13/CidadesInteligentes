package pt.atp.cidadesinteligentes

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import pt.atp.cidadesinteligentes.api.EndPoints
import pt.atp.cidadesinteligentes.api.Ocorrencia
import pt.atp.cidadesinteligentes.api.ServiceBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VerOcorrencia : AppCompatActivity() {
    private lateinit var ocorrencia: List<Ocorrencia>
    private lateinit var desc: TextView
    private lateinit var tit: TextView
    private lateinit var tipo: TextView
    private lateinit var user: TextView
    private lateinit var foto: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ver_ocorrencia)

        desc = findViewById(R.id.ocodescriptionV)
        tit = findViewById(R.id.ocotitleV)
        foto = findViewById(R.id.imageView2)
        tipo = findViewById(R.id.textView4)
        user = findViewById(R.id.textView6)

        val buttonBack = findViewById<Button>(R.id.buttonBackVer)
        buttonBack.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
            finish()
        }

        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.getOcorrencias()
        call.enqueue(object : Callback<List<Ocorrencia>> {
            override fun onResponse(
                call: Call<List<Ocorrencia>>,
                response: Response<List<Ocorrencia>>
            ) {
                if (response.isSuccessful) {
                    ocorrencia = response.body()!!
                    for (ocorr in ocorrencia) {
                        desc.setText(ocorr.descricao)
                        tit.setText(ocorr.titulo)
                        tipo.setText(ocorr.tipo_id.toString())
                        user.setText(ocorr.users_id.toString())
                        Picasso.with(this@VerOcorrencia)
                            .load("https://cidintdiogo.000webhostapp.com/myslim/API/uploads/" + ocorr.foto + ".png")
                            .into(foto)
                    }
                }
            }

            override fun onFailure(call: Call<List<Ocorrencia>>, t: Throwable) {
                Toast.makeText(this@VerOcorrencia, "${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}