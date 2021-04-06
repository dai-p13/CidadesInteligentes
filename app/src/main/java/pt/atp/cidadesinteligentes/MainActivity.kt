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
import pt.atp.cidadesinteligentes.api.EndPoints
import pt.atp.cidadesinteligentes.api.ServiceBuilder
import pt.atp.cidadesinteligentes.api.Users
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var user: EditText
    private lateinit var pwd: EditText
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var util: List<Users>


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
            finish()
        }

        user = findViewById(R.id.username)
        pwd = findViewById(R.id.password)
        sharedPreferences = getSharedPreferences(getString(R.string.share_preferencees_file), Context.MODE_PRIVATE)

        val nomeUser = sharedPreferences.getString(getString(R.string.username), "")
        val pwd = sharedPreferences.getString(getString(R.string.password), "")
        //
        if(nomeUser.toString().isNotEmpty() && pwd.toString().isNotEmpty()) {
            val intent = Intent(this@MainActivity, MapsActivity::class.java)
            startActivity(intent)
        } else {
            realizarLogin()
        }
    }

    fun realizarLogin() {
        val username =  user.text.toString()
        val password = pwd.text.toString()
        if(username.isNotEmpty() && password.isNotEmpty()){
            val request = ServiceBuilder.buildService(EndPoints::class.java)
            val call = request.getUsers()
            call.enqueue(object : Callback<List<Users>> {
                @SuppressLint("ResourceType")
                override fun onResponse(call: Call<List<Users>>, response: Response<List<Users>>) {
                    if(response.isSuccessful){
                        util = response.body()!!
                        for(utili in util){
                            val usn = utili.username
                            val psw = utili.password
                            val shrePref: SharedPreferences = getSharedPreferences(getString(R.string.share_preferencees_file), Context.MODE_PRIVATE)
                            with(shrePref.edit()) {
                                putString(getString(R.id.username), usn)
                                putString(getString(R.id.password), psw)
                                commit()
                            }
                            Log.d("pref", shrePref.toString())
                        }
                        val intent = Intent(this@MainActivity, MapsActivity::class.java)
                        startActivity(intent)
                    }else {
                        Toast.makeText(this@MainActivity, getString(R.string.error), Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@MainActivity, MainActivity::class.java)
                        startActivity(intent)
                    }
                }

                override fun onFailure(call: Call<List<Users>>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }else {
            Toast.makeText(this@MainActivity, getString(R.string.blank), Toast.LENGTH_SHORT).show()
        }
    }

}