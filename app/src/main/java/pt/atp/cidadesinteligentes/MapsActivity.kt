package pt.atp.cidadesinteligentes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem

import android.widget.Toast

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import pt.atp.cidadesinteligentes.api.EndPoints
import pt.atp.cidadesinteligentes.api.Ocorrencia
import pt.atp.cidadesinteligentes.api.ServiceBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var ocorrencia: List<Ocorrencia>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val request =  ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.getOcorrencias()
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