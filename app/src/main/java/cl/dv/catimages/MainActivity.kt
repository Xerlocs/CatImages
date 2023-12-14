package cl.dv.catimages

import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray
import org.json.JSONException
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity(), APICallback {

    private lateinit var gato: ImageView
    private lateinit var getRequestButton : Button
    private lateinit var listData : MutableList<String>
    private var URL : String = "https://api.thecatapi.com/v1/images/search?api_key=live_LeL2KtwWC1nwmPF8actkDkBICIgfKb5AcFNMnF5ojPQsYI8RtptGGeUAij03jNb6"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getRequestButton = findViewById(R.id.generarButton)
        gato = findViewById(R.id.apiURL)

        val executor = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())

        var image: Bitmap? = null

        listData = mutableListOf(

        )

       getRequestButton.setOnClickListener{
           val apiRequestTask = ApiTask(this)
           apiRequestTask.execute(URL)
           listData.clear()
        }
    }

    override fun onRequestComplete(result: String) {
        listData = processingData(result)
        val imageURL = listData.component1().toString()
        Toast.makeText(this, imageURL, Toast.LENGTH_LONG).show()
        //Toast.makeText(this, result, Toast.LENGTH_LONG).show()
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