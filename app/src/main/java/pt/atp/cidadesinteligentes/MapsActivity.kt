package pt.atp.cidadesinteligentes

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import pt.atp.cidadesinteligentes.api.EndPoints
import pt.atp.cidadesinteligentes.api.Ocorrencia
import pt.atp.cidadesinteligentes.api.ServiceBuilder
import pt.atp.cidadesinteligentes.api.Tipo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private lateinit var ocorrencia: List<Ocorrencia>
    private lateinit var tipo: List<Tipo>

    // add to implement last known location
    private lateinit var lastLocation: Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val LOCATION_PERMISSION_REQUEST_CODE = 1

    //added to implement location periodic updates
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val float = findViewById<FloatingActionButton>(R.id.floatingActionButton)
        float.setOnClickListener{
            val intent = Intent(this, AddOcorrencia::class.java)
            startActivity(intent)
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        //Pontos da BD
        val request =  ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.getOcorrencias()
        var position: LatLng
        var foto: String
        call.enqueue(object : Callback<List<Ocorrencia>> {
            override fun onResponse(call: Call<List<Ocorrencia>>, response: Response<List<Ocorrencia>>) {
                if(response.isSuccessful){
                    ocorrencia = response.body()!!
                    for (ocorr in ocorrencia) {
                        position = LatLng(ocorr.latitude.toDouble(), ocorr.longitude.toDouble())
                        foto = ocorr.foto
                        val image = MediaStore.Images.Media.getContentUri(foto)
                        Log.d("LOL", image.toString())
                        val bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), image)
                        mMap.addMarker(MarkerOptions().position(position).title(ocorr.titulo + " - " + ocorr.descricao).icon(BitmapDescriptorFactory.fromBitmap(bitmap)))

                    }
                }
            }

            override fun onFailure(call: Call<List<Ocorrencia>>, t: Throwable) {
                Toast.makeText(this@MapsActivity, "${t.message}", Toast.LENGTH_SHORT).show()
            }
        })

        locationCallback = object : LocationCallback() {
            @SuppressLint("SetTextI18n")
            override fun onLocationResult(p0: LocationResult) {
                super.onLocationResult(p0)
                lastLocation = p0.lastLocation
                val loc = LatLng(lastLocation.latitude, lastLocation.longitude)

                //mMap.addMarker(MarkerOptions().position(loc).title("Marker"))
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 15.0f))
                // preenche as coordenadas
                findViewById<TextView>(R.id.txtcoordenadas).setText("Lat: " + loc.latitude + " - Long: " + loc.longitude)

                // reverse geocoding
                /*val address = getAddress(lastLocation.latitude, lastLocation.longitude)
                findViewById<TextView>(R.id.txtmorada).setText("Morada: " + address)*/

                // distancia
                /*findViewById<TextView>(R.id.textView3).setText("Distância: " + calculateDistance(lastLocation.latitude, lastLocation.longitude,
                        position.latitude, position.longitude).toString())*/

                Log.d("**** DIOGO", "new location received - " + loc.latitude + " -" + loc.longitude)
            }
        }

        createLocationRequest()

    }
    //
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        /*val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))*/

        //setUpMap()
    }

    fun calculateDistance(lat1: Double, lng1: Double, lat2: Double, lng2: Double): Float {
        val results = FloatArray(1)
        Location.distanceBetween(lat1, lng1, lat2, lng2, results)
        // distance in meter
        return results[0]
    }

    fun setUpMap(){
        if(ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)

            return
        } else {
            mMap.isMyLocationEnabled = true

            fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
                if (location != null) {
                    lastLocation = location
                    Toast.makeText(this@MapsActivity, lastLocation.toString(), Toast.LENGTH_SHORT).show()
                    val currentLatLng = LatLng(location.latitude, location.longitude)
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f))
                }
            }
        }
    }

    private fun createLocationRequest() {
        locationRequest = LocationRequest()
        // interval specifies the rate at which your app will like to receive updates.
        //locationRequest.interval = 10000
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu1, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId){

            R.id.listarTodas ->{
                val intent = Intent(this, ListaOcorrencias::class.java)
                startActivity(intent)
                true

            }
            R.id.listarAcidentes -> {
                mMap.clear()

                val request = ServiceBuilder.buildService(EndPoints::class.java)
                val call = request.getAcidentes()
                var position: LatLng
                call.enqueue(object : Callback<List<Ocorrencia>> {
                    override fun onResponse(call: Call<List<Ocorrencia>>, response: Response<List<Ocorrencia>>) {
                        if(response.isSuccessful){
                            ocorrencia = response.body()!!
                            for (ocorr in ocorrencia) {
                                position = LatLng(ocorr.latitude.toDouble(), ocorr.longitude.toDouble())
                                mMap.addMarker(MarkerOptions().position(position).title(ocorr.titulo + " - " + ocorr.descricao))
                            }
                        }
                    }

                    override fun onFailure(call: Call<List<Ocorrencia>>, t: Throwable) {
                        Toast.makeText(this@MapsActivity, "${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
                true
            }
            R.id.listarObras -> {
                mMap.clear()

                val request = ServiceBuilder.buildService(EndPoints::class.java)
                val call = request.getObras()
                var position: LatLng
                call.enqueue(object : Callback<List<Ocorrencia>> {
                    override fun onResponse(call: Call<List<Ocorrencia>>, response: Response<List<Ocorrencia>>) {
                        if(response.isSuccessful){
                            ocorrencia = response.body()!!
                            for (ocorr in ocorrencia) {
                                position = LatLng(ocorr.latitude.toDouble(), ocorr.longitude.toDouble())
                                mMap.addMarker(MarkerOptions().position(position).title(ocorr.titulo + " - " + ocorr.descricao + "Distancia: " + calculateDistance(
                                        lastLocation.latitude, lastLocation.longitude, ocorr.latitude.toDouble(), ocorr.longitude.toDouble()).toString()))
                            }
                        }
                    }

                    override fun onFailure(call: Call<List<Ocorrencia>>, t: Throwable) {
                        Toast.makeText(this@MapsActivity, "${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
                true
            }
            R.id.listarSaneamento -> {
                mMap.clear()

                val request = ServiceBuilder.buildService(EndPoints::class.java)
                val call = request.getSaneamento()
                var position: LatLng
                call.enqueue(object : Callback<List<Ocorrencia>> {
                    override fun onResponse(call: Call<List<Ocorrencia>>, response: Response<List<Ocorrencia>>) {
                        if(response.isSuccessful){
                            ocorrencia = response.body()!!
                            for (ocorr in ocorrencia) {
                                position = LatLng(ocorr.latitude.toDouble(), ocorr.longitude.toDouble())
                                mMap.addMarker(MarkerOptions().position(position).title(ocorr.titulo + " - " + ocorr.descricao))
                            }
                        }
                    }

                    override fun onFailure(call: Call<List<Ocorrencia>>, t: Throwable) {
                        Toast.makeText(this@MapsActivity, "${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
                true
            }
            R.id.listarMinhas ->{
                mMap.clear()
                /*val request = ServiceBuilder.buildService(EndPoints::class.java)
                val call = request.getSaneamento()
                var position: LatLng
                call.enqueue(object : Callback<List<Ocorrencia>> {
                    override fun onResponse(call: Call<List<Ocorrencia>>, response: Response<List<Ocorrencia>>) {
                        if(response.isSuccessful){
                            ocorrencia = response.body()!!
                            for (ocorr in ocorrencia) {
                                position = LatLng(ocorr.latitude.toDouble(), ocorr.longitude.toDouble())
                                mMap.addMarker(MarkerOptions().position(position).title(ocorr.titulo + " - " + ocorr.descricao))
                            }
                        }
                    }

                    override fun onFailure(call: Call<List<Ocorrencia>>, t: Throwable) {
                        Toast.makeText(this@MapsActivity, "${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })*/
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}

