package elitechcomps.com

import android.annotation.TargetApi
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import android.widget.Toast.LENGTH_SHORT
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    @TargetApi(23)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Request permissions

        ActivityCompat.requestPermissions(
            this, arrayOf(
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.CALL_PHONE,
                android.Manifest.permission.INTERNET,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_MEDIA_LOCATION,
                android.Manifest.permission.ACCESS_NETWORK_STATE
            ), 2

        )

        // Load Animations

        personIcon.startAnimation(AnimationUtils.loadAnimation(this, R.anim.animfade))
        btn_reset.startAnimation(AnimationUtils.loadAnimation(this, R.anim.animfrombtm))
        btn_login.startAnimation(AnimationUtils.loadAnimation(this, R.anim.animfrombtm))
        remindme.startAnimation(AnimationUtils.loadAnimation(this, R.anim.animfade))

        // Intro Toast

        Toast.makeText(this, " Log in details are required ! ", LENGTH_LONG)
            .show()

        // functions call

        validatingInputs()
        updateUI()
    }

    private fun validatingInputs() {

        btn_login.setOnClickListener {

            // validating the inputs

            if (Name.text.toString() == "Elitech computers" && Password.text.toString() == "Elitech") {
                check_details.text = "correct !"
                welcome_text.text = "Authenticating..."
                Toast.makeText(this, "     correct     ", LENGTH_SHORT).show()

                //open a new activity only if the details are correct -> Main contentsActivity

                val intentMainContents =
                    Intent(this, ElitechContentsActivity::class.java)
                startActivity(intentMainContents)

            } else if (Name.text.toString() == "Elitech computers" && Password.text.toString() == "elitech") {
                check_details.text = "Almost correct!"
                Password.error = " Wrong password !"


            } else if (Name.text.toString() == "" || Password.text.toString() == "") {

                check_details.text = "Fields can't be empty !"
                Name.error = "Required !"
                Password.error = "Required !"

            } else {
                check_details.text = "Wrong details"
            }
        }
    }

    private fun updateUI() {

        // resetting the entered details

        btn_reset.setOnClickListener {

            Name.setText("")
            Password.setText("")
            welcome_text.text = "Welcome back"
            check_details.text = ""

            welcome_text.startAnimation(AnimationUtils.loadAnimation(this, R.anim.animrotate))

            // Toast
            val resetToast = Toast.makeText(this, "   details reset   ", LENGTH_SHORT)
            resetToast.show()

            remindme.setOnClickListener {
                Toast.makeText(this, "Name: Elitech computers     Password: Elitech", LENGTH_LONG)
                    .show()
            }
        }
    }

//Exit toast. confirm exit

    private var doubleBackToExit: Boolean = false
    override fun onBackPressed() {
        if (doubleBackToExit) {
            super.onBackPressed()
        }
        doubleBackToExit = true
        Toast.makeText(this, "Press again to exit ", LENGTH_SHORT).show()
        Handler().postDelayed({
            kotlin.run { doubleBackToExit = false }
        }, 2000)
    }
}
