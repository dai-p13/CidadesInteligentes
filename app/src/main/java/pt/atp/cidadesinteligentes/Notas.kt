package pt.atp.cidadesinteligentes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pt.atp.cidadesinteligentes.adapter.LineAdapter
import pt.atp.cidadesinteligentes.dataclasses.Place

class Notas : AppCompatActivity() {

    private lateinit var myList: ArrayList<Place>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notas)

        myList = ArrayList<Place>()

        for (i in 0 until 500) {
            myList.add(Place("Nota $i"))
        }

        findViewById<RecyclerView>(R.id.recycler_view).adapter = LineAdapter(myList)
        findViewById<RecyclerView>(R.id.recycler_view).layoutManager = LinearLayoutManager(this)
        //recycler_view.setHasFixedSize(true)
    }

}