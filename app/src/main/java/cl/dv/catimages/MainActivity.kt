package cl.dv.catimages

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import cl.dv.catimages.database.CatDatabase
import cl.dv.catimages.database.CatEntity
import com.bumptech.glide.Glide
import org.json.JSONArray
import org.json.JSONException

class MainActivity : AppCompatActivity(), APICallback {

    private lateinit var db: CatDatabase
    private lateinit var gato: ImageView
    private lateinit var getRequestButton : Button
    private lateinit var listData : MutableList<String>
    private lateinit var spinner: Spinner
    private lateinit var breed: String
    private var id: Int = 0
    private var URL : String = "https://api.thecatapi.com/v1/images/search?api_key=live_LeL2KtwWC1nwmPF8actkDkBICIgfKb5AcFNMnF5ojPQsYI8RtptGGeUAij03jNb6breed_ids="

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getRequestButton = findViewById(R.id.generarButton)
        gato = findViewById(R.id.apiURL)
        spinner = findViewById(R.id.breeds_spinner)

        db = Room.databaseBuilder(
            applicationContext,
            CatDatabase::class.java, "database-name"
        ).allowMainThreadQueries().build()

        val listBreeds = listOf(
            "Random",
            "Abyssinian",
            "Aegean",
            "American Bobtail",
            "American Curl",
            "American Shorthair",
            "American Wirehair",
            "Arabian Mau",
            "Autralian Mist",
            "Balinese"
        )

        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listBreeds)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = arrayAdapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = position
                //Toast.makeText(this@MainActivity, "You have selected $selectedItem breed", Toast.LENGTH_SHORT).show()

                if(selectedItem == 0){
                    URL = "https://api.thecatapi.com/v1/images/search?api_key=live_LeL2KtwWC1nwmPF8actkDkBICIgfKb5AcFNMnF5ojPQsYI8RtptGGeUAij03jNb6breed_ids="
                    breed = "random"
                }else{
                    URL = "https://api.thecatapi.com/v1/images/search?api_key=live_LeL2KtwWC1nwmPF8actkDkBICIgfKb5AcFNMnF5ojPQsYI8RtptGGeUAij03jNb6breed_ids=${procesarSegunPosicion(selectedItem)}"
                    breed = procesarSegunPosicion(selectedItem)
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        listData = mutableListOf(

        )

       getRequestButton.setOnClickListener{
           val apiRequestTask = ApiTask(this)
           apiRequestTask.execute(URL)
           listData.clear()
        }
    }

    private fun procesarSegunPosicion(posicion: Int): String {
        val stringDevolver = when (posicion) {
            1 -> "abys"
            2 -> "aege"
            3 -> "abob"
            4 -> "acur"
            5 -> "asho"
            6 -> "awir"
            7 -> "amau"
            8 -> "amis"
            9 -> "bali"
            else -> "Posición no válida"
        }

        return stringDevolver
    }

    override fun onRequestComplete(result: String) {
        listData = processingData(result)
        val imageURL = listData.component1().toString()
        //Toast.makeText(this, imageURL, Toast.LENGTH_LONG).show()
        Glide.with(this).load(imageURL).into(gato)
        //Toast.makeText(this, result, Toast.LENGTH_LONG).show()

        id += 1

        db.getCatDao().insertAll(
            CatEntity(id, imageURL, breed)
        )
    }

    fun processingData(result: String): MutableList<String>{
        var list: MutableList<String> = mutableListOf()
        try{
            val jsonArray = JSONArray(result)

            for (i in 0 until jsonArray.length()){

                val dataObject = jsonArray.getJSONObject(i)

                val image = dataObject.getString("url")
                Log.i("APIRestActivity",image.toString())
                list.add(image)
            }

        }catch(e: JSONException){
            e.printStackTrace()
        }
        return list.toMutableList()
    }


}