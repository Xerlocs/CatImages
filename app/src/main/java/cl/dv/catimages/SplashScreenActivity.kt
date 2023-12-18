package cl.dv.catimages

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        var max: Long = 2500
        var x: Long = 500

        val timer = object : CountDownTimer(max,x) {
            override fun onTick(millisUntilFinished: Long) {
                var x: Long = max - millisUntilFinished
            }

            override fun onFinish() {
                var intent = Intent(this@SplashScreenActivity, MainActivity::class.java)
                startActivity(intent)


            }
        }
        timer.start()
    }
}