package pt.atp.cidadesinteligentes

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
        //
        if(nomeUser.toString().isNotEmpty() && pwd.toString().isNotEmpty()) {
            TODO("Iniciar a atividade com login iniciado")
        }*/
    }

    fun realizarLogin() {
        val username =  user.text.toString()
        val password = pwd.text.toString()
        if(username.isNotEmpty() && password.isNotEmpty()){
            val request = ServiceBuilder.buildService(EndPoints::class.java)
            val call = request.getUsers()
            call.enqueue(object : Callback<List<Users>> {
                override fun onResponse(call: Call<List<Users>>, response: Response<List<Users>>) {
                    if(response.isSuccessful){
                        val intent = Intent(this@MainActivity, MapsActivity::class.java)
                        startActivity(intent)
                    }else {
                        Toast.makeText(this@MainActivity, getString(R.string.blank), Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<Users>>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }else {
            Toast.makeText(this@MainActivity, getString(R.string.error), Toast.LENGTH_SHORT).show()
        }
    }

}