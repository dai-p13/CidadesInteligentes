package pt.atp.cidadesinteligentes

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pt.atp.cidadesinteligentes.adapter.OcorrenciaAdapter
import pt.atp.cidadesinteligentes.api.EndPoints
import pt.atp.cidadesinteligentes.api.Ocorrencia
import pt.atp.cidadesinteligentes.api.ServiceBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListaOcorrenciasUser : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_ocorrencias)

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view_ocorr)
        val request = ServiceBuilder.buildService(EndPoints::class.java)

        sharedPreferences = getSharedPreferences(getString(R.string.share_preferencees_file), Context.MODE_PRIVATE)

        val id = sharedPreferences.getInt(R.string.id_shrpref.toString(), 0)
        val call = request.getOcorrUser(id)
        Log.d("QUALID", id.toString())

        call.enqueue(object : Callback<List<Ocorrencia>>{
            override fun onResponse(call: Call<List<Ocorrencia>>, response: Response<List<Ocorrencia>>) {
                if (response.isSuccessful){
                    recyclerView.apply{
                        setHasFixedSize(true)
                        layoutManager = LinearLayoutManager(this@ListaOcorrenciasUser)
                        adapter = OcorrenciaAdapter(response.body()!!)
                    }
                }
            }

            override fun onFailure(call: Call<List<Ocorrencia>>, t: Throwable) {
                Toast.makeText(this@ListaOcorrenciasUser, "${t.message}", Toast.LENGTH_SHORT).show()
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
                val intent = Intent(this@ListaOcorrenciasUser,MapsActivity::class.java)
                startActivity(intent)
                true

            }
            R.id.listarMinhas -> {

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