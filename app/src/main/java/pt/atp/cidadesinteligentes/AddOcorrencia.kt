package pt.atp.cidadesinteligentes

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.graphics.drawable.toBitmap
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import pt.atp.cidadesinteligentes.api.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*

class AddOcorrencia : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private lateinit var image: ImageView
    private lateinit var title: EditText
    private lateinit var description: EditText

    private lateinit var location: LatLng

    private lateinit var button: Button
    private lateinit var buttonBack: Button
    private lateinit var buttonAdd: Button
    private lateinit var spinner: Spinner
    private lateinit var sharedPreferences: SharedPreferences

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

        image = findViewById(R.id.imageView)

        button = findViewById(R.id.buttonAddImage)
        button.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK)
            gallery.type = "image/*"
            startActivityForResult(gallery, pickImage)
        }
        buttonBack = findViewById(R.id.cancel_edit)
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
            if (data != null){
                image.setImageURI(data?.data)
            }
            //imageView.setImageURI(imageUri)
            Log.d("IMAGEM", "image " + image.toString() )
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
        sharedPreferences = getSharedPreferences(getString(R.string.share_preferencees_file), Context.MODE_PRIVATE)
        val id_user: Int = sharedPreferences.getInt(R.string.id_shrpref.toString(), 0)

        val imgBitmap: Bitmap = findViewById<ImageView>(R.id.imageView).drawable.toBitmap()
        val imageFile: File = convertBitmapToFile("file", imgBitmap)
        val imgFileRequest: RequestBody = RequestBody.create(MediaType.parse("image/*"), imageFile)
        val foto: MultipartBody.Part = MultipartBody.Part.createFormData("foto", imageFile.name, imgFileRequest)

        val titulo: RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), title.text.toString())
        val descricao: RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), description.text.toString())
        val tipo: RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), spinner.selectedItem.toString())
        val latitude: RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), lastLocation.latitude.toString())
        val longitude: RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), lastLocation.longitude.toString())
        Log.d("FOTO", foto.toString())
        Log.d("TITULO", titulo.toString())
        Log.d("DESC", descricao.toString())
        Log.d("LAT", latitude.toString())
        Log.d("LONG", longitude.toString())
        Log.d("USER", id_user.toString())
        Log.d("TIPO", tipo.toString())

        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.addOcorrencias(titulo, descricao, latitude, longitude, foto, id_user, spinner.selectedItemPosition + 1)

        call.enqueue(object : Callback<OutputPost>{
            override fun onResponse(call: Call<OutputPost>, response: Response<OutputPost>) {
                Log.d("CALL", response.toString())
                if(response.isSuccessful){
                    //val c: OutputPost = response.body()!!
                    Toast.makeText(applicationContext, R.string.save, Toast.LENGTH_LONG).show()
                    Log.d("DIOGOF", "ENVIOU PARA A BASE DE DADOS")
                    val intent = Intent(this@AddOcorrencia, MapsActivity::class.java)
                    //verificar na stack de atividades se já existe esta atividade
                    //se existir limpa e cria de novo
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()
                }
            }

            override fun onFailure(call: Call<OutputPost>, t: Throwable) {
                Toast.makeText(applicationContext, "${t.message}", Toast.LENGTH_SHORT).show()
                Log.d("DIMITRI", t.message.toString())
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

    private fun convertBitmapToFile(fileName: String, bitmap: Bitmap): File {
        //create a file to write bitmap data
        val file = File(this@AddOcorrencia.cacheDir, fileName)
        file.createNewFile()

        //Convert bitmap to byte array
        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 0, bos)
        val bitMapData = bos.toByteArray()

        //write the bytes in file
        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(file)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        try {
            fos?.write(bitMapData)
            fos?.flush()
            fos?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return file
    }
}