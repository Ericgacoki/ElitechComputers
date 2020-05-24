package elitechcomps.com

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_splash.*

private const val splashTimeOut = 3000L // 3 sec

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        lightBulb.startAnimation(AnimationUtils.loadAnimation(this, R.anim.animfrombtm))
        textTech.startAnimation(AnimationUtils.loadAnimation(this, R.anim.animfade))

        Handler().postDelayed({
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
        }, splashTimeOut)
    }
}