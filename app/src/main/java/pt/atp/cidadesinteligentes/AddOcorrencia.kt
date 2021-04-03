package pt.atp.cidadesinteligentes

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import pt.atp.cidadesinteligentes.api.EndPoints
import pt.atp.cidadesinteligentes.api.Ocorrencia
import pt.atp.cidadesinteligentes.api.OutputPost
import pt.atp.cidadesinteligentes.api.ServiceBuilder
import pt.atp.cidadesinteligentes.ententies.Notes
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddOcorrencia : AppCompatActivity() {
    private lateinit var imageView: ImageView
    private lateinit var title: EditText
    private lateinit var description: EditText

    private lateinit var button: Button
    private lateinit var buttonBack: Button
    private lateinit var buttonAdd: Button

    private val newOcorrActivityRequestCode = 1

    private lateinit var lastLocation: Location
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest
    private val LOCATION_PERMISSION_REQUEST_CODE = 1
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val pickImage = 100
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_ocorrencia)

        title = findViewById(R.id.title)
        description = findViewById(R.id.description)

        imageView = findViewById(R.id.imageView)

        button = findViewById(R.id.buttonAddImage)
        button.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)
        }
        buttonBack = findViewById(R.id.cancel)
        buttonBack.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }
        buttonAdd = findViewById(R.id.save)
        buttonAdd.setOnClickListener {
            val replyIntent = Intent()
            if (TextUtils.isEmpty(title.text) && TextUtils.isEmpty(description.text)){
                setResult(Activity.RESULT_CANCELED, replyIntent)
                startActivityForResult(intent, newOcorrActivityRequestCode)
                Toast.makeText(applicationContext, R.string.empty, Toast.LENGTH_LONG).show()
            } else {
                replyIntent.putExtra(EXTRA_REPLY_TITLE, title.text.toString())
                replyIntent.putExtra(EXTRA_REPLY_DESCRIPTION, description.text.toString())
                replyIntent.putExtra(EXTRA_REPLY_LATITUDE, lastLocation.latitude.toString())
                replyIntent.putExtra(EXTRA_REPLY_LONGITUDE, lastLocation.latitude.toString())
                setResult(Activity.RESULT_OK, replyIntent)
            }
            post()
            finish()
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                super.onLocationResult(p0)
                lastLocation = p0.lastLocation
                val loc = LatLng(lastLocation.latitude, lastLocation.longitude)

                Log.d("**** DIOGO", "new location received - " + loc.latitude + " -" + loc.longitude)
            }
        }
        createLocationRequest()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data
            imageView.setImageURI(imageUri)
        }

        /*if (requestCode == newOcorrActivityRequestCode && resultCode == Activity.RESULT_OK){
            val otitulo = data?.getStringExtra(EXTRA_REPLY_TITLE)
            val odescr = data?.getStringExtra(EXTRA_REPLY_DESCRIPTION)
            val olat = data?.getStringExtra(EXTRA_REPLY_LATITUDE)
            val olong = data?.getStringExtra(EXTRA_REPLY_LONGITUDE)

            if(otitulo != null && odescr != null) {
                val ocorrencia = Ocorrencia(titulo = otitulo, descricao = odescr, latitude = olat, longitude = olong)

            }
        }*/

    }

    private fun createLocationRequest() {
        locationRequest = LocationRequest()
        // interval specifies the rate at which your app will like to receive updates.
        locationRequest.interval = 10000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    LOCATION_PERMISSION_REQUEST_CODE)
            return
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null /* Looper */)
    }

    override fun onPause() {
        super.onPause()
        fusedLocationClient.removeLocationUpdates(locationCallback)
        Log.d("**** DIOGO", "onPause - removeLocationUpdates")
    }

    public override fun onResume() {
        super.onResume()
        startLocationUpdates()
        Log.d("**** DIOGO", "onResume - startLocationUpdates")
    }

    fun post(){
        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.addOcorrencias(title.text.toString(), description.text.toString(),
                lastLocation.latitude.toString(), lastLocation.longitude.toString(), "teste android",
                1, 1)

        call.enqueue(object : Callback<OutputPost>{
            override fun onResponse(call: Call<OutputPost>, response: Response<OutputPost>) {
                if(response.isSuccessful){
                    val c: OutputPost = response.body()!!
                    Toast.makeText(applicationContext, c.titulo, Toast.LENGTH_LONG).show()
                    Log.d("**** DIOGO", "ENVIOU PARA A BASE DE DADOS")
                }
            }

            override fun onFailure(call: Call<OutputPost>, t: Throwable) {
                Toast.makeText(applicationContext, "${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    companion object {
        const val EXTRA_REPLY_TITLE = "com.example.android.title"
        const val EXTRA_REPLY_DESCRIPTION = "com.example.android.description"
        const val EXTRA_REPLY_LATITUDE = "LAT"
        const val EXTRA_REPLY_LONGITUDE = "LONG"
    }

}