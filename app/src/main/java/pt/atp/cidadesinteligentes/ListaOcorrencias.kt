package pt.atp.cidadesinteligentes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pt.atp.cidadesinteligentes.adapter.OcorrenciaAdapter
import pt.atp.cidadesinteligentes.api.EndPoints
import pt.atp.cidadesinteligentes.api.Ocorrencia
import pt.atp.cidadesinteligentes.api.ServiceBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListaOcorrencias : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_ocorrencias)

        val buttonCancel = findViewById<Button>(R.id.OcorrBack)
        buttonCancel.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view_ocorr)
        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.getOcorrencias()

        call.enqueue(object : Callback<List<Ocorrencia>> {
            override fun onResponse(call: Call<List<Ocorrencia>>, response: Response<List<Ocorrencia>>) {
                if (response.isSuccessful){
                    recyclerView.apply{
                        setHasFixedSize(true)
                        layoutManager = LinearLayoutManager(this@ListaOcorrencias)
                        adapter = OcorrenciaAdapter(response.body()!!)
                    }
                }
            }
            override fun onFailure(call: Call<List<Ocorrencia>>, t: Throwable) {
                Toast.makeText(this@ListaOcorrencias, "${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu1, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId){

            R.id.listarTodas -> {

                true

            }
            R.id.listarAcidentes -> {

                /*val request = ServiceBuilder.buildService(EndPoints::class.java)
                val call = request.getOcorrencias() // estaticamente o valor 2. deverá depois passar a ser dinamico

                call.enqueue(object : Callback<Ocorrencia>{
                    override fun onResponse(call: Call<Ocorrencia>, response: Response<Ocorrencia>) {
                        if (response.isSuccessful){

                            val c: Ocorrencia = response.body()!!
                            Toast.makeText(this@ListaOcorrencias, c.titulo, Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Ocorrencia>, t: Throwable) {
                        Toast.makeText(this@ListaOcorrencias, "${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
                */
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}