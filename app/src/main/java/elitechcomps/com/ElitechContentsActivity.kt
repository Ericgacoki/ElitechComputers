package elitechcomps.com

import android.annotation.TargetApi
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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

        // Animate cardViews
        about.startAnimation(AnimationUtils.loadAnimation(this, R.anim.animfromtop))
        training.startAnimation(AnimationUtils.loadAnimation(this, R.anim.animfromtop))


        location.startAnimation(AnimationUtils.loadAnimation(this, R.anim.animfrombtm))
        achievements.startAnimation(AnimationUtils.loadAnimation(this, R.anim.animfrombtm))


        contacts.startAnimation(AnimationUtils.loadAnimation(this, R.anim.animfromleft))
        website.startAnimation(AnimationUtils.loadAnimation(this, R.anim.animfromright))


        developer.startAnimation(AnimationUtils.loadAnimation(this, R.anim.animfrombtm))
        rate.startAnimation(AnimationUtils.loadAnimation(this, R.anim.animfrombtm))


        openOtherActivities()
    }

    private fun openOtherActivities() {

        about.setOnClickListener {

            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.SEND_SMS
                )
                == PackageManager.PERMISSION_GRANTED
            ) {

                startActivity(Intent(this, AboutActivity::class.java))
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.SEND_SMS),
                    2
                )
            }
        }

        training.setOnClickListener {

            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.ACCESS_MEDIA_LOCATION
                ) == PackageManager.PERMISSION_GRANTED

            ) {

                startActivity(Intent(this, TrainingActivity::class.java))
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.ACCESS_MEDIA_LOCATION), 2
                )
            }
        }

        location.setOnClickListener {

            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                )
                == PackageManager.PERMISSION_GRANTED
            ) {

                startActivity(Intent(this, LocationActivity::class.java))
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 2
                )
            }
        }


        achievements.setOnClickListener {

            startActivity(Intent(this, AchievementsActivity::class.java))
        }
        contacts.setOnClickListener {

            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.CALL_PHONE
                )
                == PackageManager.PERMISSION_GRANTED
            ) {
                startActivity(Intent(this, ContactsActivity::class.java))
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.CALL_PHONE),
                    2
                )
            }
        }
        //      Links to Internet

        website.setOnClickListener {

            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.INTERNET
                )
                == PackageManager.PERMISSION_GRANTED
            ) {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com")))
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.INTERNET), 2
                )
            }
        }
        developer.setOnClickListener {

            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.INTERNET
                )
                == PackageManager.PERMISSION_GRANTED
            ) {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://sites.google.com/view/www-codewithericg-com/home")
                    )
                )

            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.INTERNET), 2
                )
            }


        }
        rate.setOnClickListener {

            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.INTERNET
                )
                == PackageManager.PERMISSION_GRANTED
            ) {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://googleplay.com")
                    )
                )
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.INTERNET), 2
                )
            }

        }
    }
}