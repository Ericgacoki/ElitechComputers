package elitechcomps.com

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_elitech_contents.*

class ElitechContentsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_elitech_contents)

        openOtherActivities()
    }

    private fun openOtherActivities() {

        about.setOnClickListener {

            val intentAbout = Intent(this@ElitechContentsActivity, AboutActivity::class.java)
            startActivity(intentAbout)
        }

        training.setOnClickListener {

            val intentTraining = Intent(this@ElitechContentsActivity, TrainingActivity::class.java)
            startActivity(intentTraining)
        }

        location.setOnClickListener {

            val intentLocation = Intent(this@ElitechContentsActivity, LocationActivity::class.java)
            startActivity(intentLocation)
        }

        achievements.setOnClickListener {

            val intentAchievements =
                Intent(this@ElitechContentsActivity, AchievementsActivity::class.java)
            startActivity(intentAchievements)
        }
        contacts.setOnClickListener {
            val intentContacts = Intent(this@ElitechContentsActivity, ContactsActivity::class.java)
            startActivity(intentContacts)

        }
        //      Links to Internet

        website.setOnClickListener {

            val webIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("http://www.google.com"))
            startActivity(webIntent)
        }
        developer.setOnClickListener {
            val webAboutDeveloper = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://sites.google.com/view/www-codewithericg-com/home"))
            startActivity(webAboutDeveloper)
        }
        rate.setOnClickListener {
            val rateAction = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://googleplay.com"))
            startActivity(rateAction)
        }

    }
}




















