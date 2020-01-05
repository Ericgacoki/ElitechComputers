package elitechcomps.com

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity

import kotlinx.android.synthetic.main.activity_location.*

class LocationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Content coming soon ...", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

}
