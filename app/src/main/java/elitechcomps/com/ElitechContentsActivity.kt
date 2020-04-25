package elitechcomps.com

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_contacts.view.*
import kotlinx.android.synthetic.main.activity_elitech_contents.*

/*
    STARTED:  DEC 2019
         BY:  ERIC G [The CODE STAR]
         TO:  P.DAN -> ELITECH COMPS COLLEGE

 */

class ElitechContentsActivity : AppCompatActivity() {


    @TargetApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_elitech_contents)


        /**
         *  Animate cardViews */

        portal.startAnimation(AnimationUtils.loadAnimation(this, R.anim.animfromtop))
        services.startAnimation(AnimationUtils.loadAnimation(this, R.anim.animfromtop))


        location.startAnimation(AnimationUtils.loadAnimation(this, R.anim.animfrombtm))
        contacts.startAnimation(AnimationUtils.loadAnimation(this, R.anim.animfrombtm))


        achievements.startAnimation(AnimationUtils.loadAnimation(this, R.anim.animfromleft))
        website.startAnimation(AnimationUtils.loadAnimation(this, R.anim.animfromright))


        moreaboutus.startAnimation(AnimationUtils.loadAnimation(this, R.anim.animfrombtm))
        rate.startAnimation(AnimationUtils.loadAnimation(this, R.anim.animfrombtm))


        requestPermissions()
        dealWithAlerts()
        openOtherActivities()
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this, arrayOf(
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.CALL_PHONE,
                android.Manifest.permission.READ_SMS,
                android.Manifest.permission.RECEIVE_SMS,
                android.Manifest.permission.SEND_SMS,
                android.Manifest.permission.READ_CALENDAR,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_NETWORK_STATE
            ), 5

        )
    }

    private fun dealWithAlerts() {

        var alertsCount = 5
        alerts_counter.text = alertsCount.toString()

        /** Animate views */

        val views: Array<View> = arrayOf(alerts_bell, alerts_bell_clicked, alerts_counter)
        for (view in views) {
            view.visibility = View.INVISIBLE
        }

        Handler().postDelayed({
            kotlin.run {
                for (view in views) {
                    view.visibility = View.VISIBLE
                    view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.animfromleft))
                }

                val mediaPlayer = MediaPlayer.create(this, R.raw.onfire)
                mediaPlayer.start()
                Handler().postDelayed({ mediaPlayer.stop() }, 1000)

            }
        }, 3000)


        alerts_bell.setOnClickListener {

            if (intent.resolveActivity(packageManager) != null) {
                Toast.makeText(this, "You have $alertsCount alert(s)", LENGTH_LONG).show()

                startActivity(Intent(this, AlertsActivity::class.java))

                alertsCount = 0
                alerts_counter.text = "0"
            }

            /* This creates a  Toggle-like behavior that lasts for 45 Milliseconds */

            alerts_bell.visibility = View.INVISIBLE
            Handler().postDelayed({
                kotlin.run { alerts_bell.visibility = View.VISIBLE }
            }, 45)

        }// TODO() mark all as read( turn seen to true and update ui ) onLongClickListener by opening a menuView

        alerts_bell.setOnLongClickListener {
            Toast.makeText(this, " LongClicking is not supported ", LENGTH_LONG).show()
            true
        }
    }

    @SuppressLint("InflateParams")
    private fun openOtherActivities() {

        portal.setOnClickListener {

            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.SEND_SMS
                )
                == PackageManager.PERMISSION_GRANTED
            ) {


                if (intent.resolveActivity(packageManager) != null) {

                    //TODO() check if user is a student or not

                    startActivity(Intent(this, StudentPortal::class.java))
                } else {
                    Toast.makeText(this, "Sorry, something went wrong!", LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(
                    this,
                    " Try again after allowing all permissions for smooth usability ",
                    LENGTH_LONG
                ).show()
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.SEND_SMS),
                    5
                )
            }
        }

        services.setOnClickListener {
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(Intent(this, ServicesActivity::class.java))
            }
        }

        location.setOnClickListener {

            //TODO() Open map in a webView

            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                )
                == PackageManager.PERMISSION_GRANTED
            ) {

                if (intent.resolveActivity(packageManager) != null) {
                    startActivity(Intent(this, LocationActivity::class.java))
                }
            } else {
                Toast.makeText(
                    this,
                    " Try again after allowing all permissions for smooth usability ",
                    LENGTH_LONG
                ).show()

                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 5
                )
            }
        }

        achievements.setOnClickListener {

            if (intent.resolveActivity(packageManager) != null) {
                startActivity(Intent(this, AchievementsActivity::class.java))
            }
        }

        contacts.setOnClickListener {

            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.CALL_PHONE
                )
                == PackageManager.PERMISSION_GRANTED
            ) {
/*
 *               inflate View/Layout inside this ClickListener. Otherwise the app will crash on second click
*/
                val customDialogLayout: View =
                    LayoutInflater.from(this).inflate(R.layout.activity_contacts, null)
                val customDialog = AlertDialog.Builder(this, 0)
                customDialog.apply {
                    setView(customDialogLayout)


/*
 *              do some work with the inflated layout
*/
                    customDialogLayout.fabcall.setOnClickListener {

                        // TODO() Implement this on a call/phone icon instead of a fab. This will be among other messaging platforms
                        val callAlertDialog = AlertDialog.Builder(this.context, 0)

                        callAlertDialog.apply {
                            setTitle("Making a call may incur charges !")
                            setMessage("We're always ready for you")
                            setIcon(R.drawable.coin)

                            setNegativeButtonIcon(getDrawable(R.drawable.ic_call_end))
                            setPositiveButtonIcon(getDrawable(R.drawable.ic_call_start))

                            setPositiveButton(" Ok   ") { _, _ ->
                                val callIntent =
                                    Intent(Intent.ACTION_CALL, Uri.parse("tel:+254745623611"))

/*
 *                                Proceed to make a call directly via the default calling App without dialing
*/

                                if (ContextCompat.checkSelfPermission(
                                        this.context,
                                        android.Manifest.permission.CALL_PHONE
                                    )
                                    == PackageManager.PERMISSION_GRANTED
                                ) {

/*
 *                                    make a call only iff there is at least one App that can handle callIntent(s)
*/

                                    if (intent.resolveActivity(packageManager) != null) {
                                        startActivity(callIntent)

                                        Toast.makeText(
                                            this.context,
                                            "Continue to call Eric",
                                            LENGTH_LONG
                                        ).show()
                                    }
                                }
                            }

                            setNegativeButton(" Cancel   ") { _, _ ->
                                Toast.makeText(
                                    this.context, "Cancelled",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                            setCancelable(false)
                            create()
                            show()
                        }
                    }

/*
 *              code other click listeners here     ie -> email , whatsApp , facebook
*/

                    create().show()
                }
            } else {
                Toast.makeText(
                    this,
                    " Try again after allowing all permissions for smooth usability ",
                    LENGTH_LONG
                ).show()

                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.CALL_PHONE),
                    5
                )
            }
        }

/*
 *               Links to Internet
*/
        website.setOnClickListener {

            if (intent.resolveActivity(packageManager) != null) {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com")))
            } else {
                Toast.makeText(this, " Browser NOT detected !", LENGTH_LONG).show()
            }
        }

        moreaboutus.setOnClickListener {

            if (intent.resolveActivity(packageManager) != null) {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://sites.google.com/view/www-codewithericg-com/home")
                    )
                )
            } else {
                Toast.makeText(this, " Browser NOT supported !", LENGTH_LONG).show()
            }
        }

        rate.setOnClickListener {
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://googleplay.com")))
            } else {
                Toast.makeText(this, " Browser NOT found !", LENGTH_LONG).show()
            }
        }
    }

    private var doubleBackToExit: Boolean = false

    override fun onBackPressed() {
        if (doubleBackToExit) {
            super.onBackPressed()
        }
        doubleBackToExit = true
        Toast.makeText(this, "Press again to exit ", Toast.LENGTH_SHORT).show()
        Handler().postDelayed({
            kotlin.run { doubleBackToExit = false }
        }, 2000)
    }
}