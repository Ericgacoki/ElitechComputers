package elitechcomps.com

import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import android.widget.Toast.LENGTH_SHORT
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_authentication.*

class SignInActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)

        // Load Animations

        personIcon.startAnimation(AnimationUtils.loadAnimation(this, R.anim.animfade))
        btn_reset.startAnimation(AnimationUtils.loadAnimation(this, R.anim.animfrombtm))
        btn_login.startAnimation(AnimationUtils.loadAnimation(this, R.anim.animfrombtm))
        remindme.startAnimation(AnimationUtils.loadAnimation(this, R.anim.animfade))

        // Intro Toast

        Toast.makeText(this, " Log in details are required ! ", LENGTH_LONG)
            .show()

        // functions call

        validateToSignIn()
        updateUI()
    }

    private fun validateToSignIn() {

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
            Toast.makeText(this, "   details reset   ", LENGTH_SHORT).show()

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
