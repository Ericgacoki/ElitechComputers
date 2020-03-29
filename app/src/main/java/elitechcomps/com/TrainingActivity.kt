package elitechcomps.com

import android.annotation.TargetApi
import android.os.Bundle
//import android.view.View
import androidx.appcompat.app.AppCompatActivity
//import com.google.android.material.snackbar.Snackbar
//import kotlinx.android.synthetic.main.content_training.*

class TrainingActivity : AppCompatActivity() {

    @TargetApi(23)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_training)

        /*fab.setOnClickListener { view: View ->
            Snackbar.make(view, "Content coming soon ...", Snackbar.LENGTH_LONG)
                .setText("This will be in our coming updates. We'll let you know when it's ready")
                .setActionTextColor(getColor(R.color.colorDarkBlue))
                .setAction("Action", null).show()
        }
        */
    }
}
