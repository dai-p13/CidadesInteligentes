package pt.atp.cidadesinteligentes

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import pt.atp.cidadesinteligentes.api.EndPoints
import pt.atp.cidadesinteligentes.api.Ocorrencia
import pt.atp.cidadesinteligentes.api.ServiceBuilder
import pt.atp.cidadesinteligentes.api.Users
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var user: EditText
    private lateinit var pwd: EditText
    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.button2)
        button.setOnClickListener{
            val intent = Intent(this, Notas::class.java)
            startActivity(intent)
        }

        val button2 = findViewById<Button>(R.id.buttonLogin)
        button2.setOnClickListener{
            //val intent = Intent(this@MainActivity, MapsActivity::class.java)
            //startActivity(intent)
            realizarLogin()
        }

        user = findViewById(R.id.username)
        pwd = findViewById(R.id.password)

        sharedPreferences = getSharedPreferences(getString(R.string.share_preferencees_file), Context.MODE_PRIVATE)
        val id = sharedPreferences.getInt(R.string.id_shrpref.toString(), 0)

        if(id != 0){
            val intent = Intent(this@MainActivity, MapsActivity::class.java)
            startActivity(intent)
            //realizarLogin()
            finish()
        }

        user = findViewById(R.id.username)
        pwd = findViewById(R.id.password)
        //sharedPreferences = getSharedPreferences(getString(R.string.user_creds_file_key), Context.MODE_PRIVATE)

        //val nomeUser = sharedPreferences.getString(getString(R.string.username), "")
        /*val pwd = sharedPreferences.getString(getString(R.string.password), "")

        if(nomeUser.toString().isNotEmpty() && pwd.toString().isNotEmpty()) {
            TODO("Iniciar a atividade com login iniciado")
        }*/
    }

    fun realizarLogin() {
        val username =  user.text.toString()
        val password = pwd.text.toString()
        if(username.isNotEmpty() && password.isNotEmpty()){
            val request = ServiceBuilder.buildService(EndPoints::class.java)
            val call = request.login(username, password)
            call.enqueue(object : Callback<Users> {
                @SuppressLint("ResourceType")
                override fun onResponse(call: Call<Users>, response: Response<Users>) {
                    if(response.isSuccessful){
                        if(response.body()!!.id != 0){
                            val sharedPreferences = getSharedPreferences(getString(R.string.share_preferencees_file), Context.MODE_PRIVATE)
                            with(sharedPreferences.edit()) {
                                putInt(R.string.id_shrpref.toString(), response.body()!!.id)
                                commit()

                            }
                            Log.d("KOA", sharedPreferences.toString())
                            val intent = Intent(this@MainActivity, MapsActivity::class.java)
                            startActivity(intent)
                            finish()
                            //val jo = sharedPreferences.getInt(R.string.id_shrpref.toString(), 0)
                            //Log.d("DIMITRI", jo.toString())
                        }else {
                            Toast.makeText(this@MainActivity, getString(R.string.error), Toast.LENGTH_SHORT).show()
                        }
                    }else {
                        Toast.makeText(this@MainActivity, getString(R.string.error), Toast.LENGTH_SHORT).show()
                        //val intent = Intent(this@MainActivity, MainActivity::class.java)
                        //startActivity(intent)

                    }
                }

                override fun onFailure(call: Call<Users>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }else {
            Toast.makeText(this@MainActivity, getString(R.string.blank), Toast.LENGTH_SHORT).show()
        }
    }

}