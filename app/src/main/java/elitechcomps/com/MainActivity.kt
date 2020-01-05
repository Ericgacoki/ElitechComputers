package elitechcomps.com

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import android.widget.Toast.LENGTH_SHORT
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Intro Toast

        Toast.makeText(this@MainActivity, " Log in details are required ! ", LENGTH_LONG)
            .show()

        // functions call

        validatingInputs()
        resetDetails()
        remindmedetails()


    }


    private fun validatingInputs() {

        btn_login.setOnClickListener {

            // validating the inputs

            if (Name.text.toString() == "Elitech computers" && Password.text.toString() == "Elitech") {
                check_details.text = "correct !"
                welcome_text.text = "Authenticating..."
                Toast.makeText(this@MainActivity, "     correct     ", Toast.LENGTH_SHORT).show()

                //open a new activity only if the details are correct -> Main contents

                val intentMainContents =
                    Intent(this@MainActivity, ElitechContentsActivity::class.java)
                startActivity(intentMainContents)

            } else if (Name.text.toString() == "Elitech compiuters" || Password.text.toString() == "elitech") {
                check_details.text = "Almost correct!"

            } else if (Name.text.toString() == "" || Password.text.toString() == "") {

                check_details.text = "Fields can't be empty !"
                Toast.makeText(
                    this@MainActivity,
                    " Missing some important details",
                    Toast.LENGTH_SHORT
                ).show()

            } else {
                check_details.text = "Wrong details"
            }
        }
    }

    private fun resetDetails() {

        // resetting the entered details

        btn_reset.setOnClickListener {

            Name.setText("")
            Password.setText("")
            welcome_text.text = "Welcome back"
            check_details.text = ""

            // Toast

            val resetToast =
                Toast.makeText(this@MainActivity, "   details reset   ", Toast.LENGTH_SHORT)
            resetToast.show()
        }
    }


    private fun remindmedetails() {

        remindme.setOnClickListener {
            Toast.makeText(
                this@MainActivity,
                "Name: Elitech computers     Password: Elitech",
                LENGTH_LONG
            ).show()
        }
    }

//Exit toast. confirm exit

    private var doubleBackToExit: Boolean = false
    override fun onBackPressed() {
        if (doubleBackToExit) {
            super.onBackPressed()
            return
        }
        this.doubleBackToExit = true
        //displays a toast message when user clicks exit button

        Toast.makeText(this@MainActivity, "Press again to exit ", LENGTH_SHORT).show()
        //displays the toast message for a while

        Handler().postDelayed({
            kotlin.run { doubleBackToExit = false }
        }, 2000)

    }
}
