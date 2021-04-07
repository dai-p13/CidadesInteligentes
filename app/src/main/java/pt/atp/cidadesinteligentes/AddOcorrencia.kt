package pt.atp.cidadesinteligentes

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.get
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import pt.atp.cidadesinteligentes.api.EndPoints
import pt.atp.cidadesinteligentes.api.OutputPost
import pt.atp.cidadesinteligentes.api.ServiceBuilder
import pt.atp.cidadesinteligentes.api.Tipo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.sql.Array

class AddOcorrencia : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private lateinit var imageView: ImageView
    private lateinit var title: EditText
    private lateinit var description: EditText

    private lateinit var button: Button
    private lateinit var buttonBack: Button
    private lateinit var buttonAdd: Button
    private lateinit var spinner: Spinner

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
                post()
                finish()
            }
        }

        spinner = findViewById(R.id.spinner)
        ArrayAdapter.createFromResource(this@AddOcorrencia, R.array.tipo,android.R.layout.simple_spinner_item).also { adapter ->
                               // Specify the layout to use when the list of choices appears
                               adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                               // Apply the adapter to the spinner
                               spinner.adapter = adapter
                           }

        spinner.onItemSelectedListener = this

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

            Log.d("IMAGEM", "image " + imageUri.toString() )
        }
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

    fun post(){
        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.addOcorrencias(title.text.toString(), description.text.toString(),
                lastLocation.latitude.toString(), lastLocation.longitude.toString(), imageUri?.let { getRealPathFromURI(it) },
                1, spinner.selectedItemPosition + 1)

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

    private fun getRealPathFromURI(contentUri: Uri): String? {
        val proj = arrayOf<String>(MediaStore.Images.Media.DATA)
        val cursor = managedQuery(contentUri, proj, null, null, null)
                ?: return contentUri.getPath()
        val column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        return cursor.getString(column_index)
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        // An item was selected. You can retrieve the selected item using
        parent.getItemAtPosition(pos)
        Log.d("SPINNER", pos.toString())

    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        // Another interface callback
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
}