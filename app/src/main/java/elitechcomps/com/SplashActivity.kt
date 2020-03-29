package elitechcomps.com

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {
    private val splashTimeOut = 3000L // 3 sec

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

       /* //Hide App Icon
        val componentName = ComponentName(this,SplashActivity::class.java)
        packageManager.setComponentEnabledSetting(componentName,packageManager.)

        TODO()WORK ON THIS
        */

        lightBulb.startAnimation(AnimationUtils.loadAnimation(this, R.anim.animfrombtm))
        textTech.startAnimation(AnimationUtils.loadAnimation(this, R.anim.animfade))


        Handler().postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, splashTimeOut)
    }
}


