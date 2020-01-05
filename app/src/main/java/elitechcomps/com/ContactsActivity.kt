package elitechcomps.com

import android.R.drawable
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

import kotlinx.android.synthetic.main.activity_contacts.*

class ContactsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle? ) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contacts)


        callOnClickOfFab()
    }
private fun callOnClickOfFab(){
        fabcall.setOnClickListener {

            val callAlert = AlertDialog.Builder(this)
            callAlert.setTitle("Making a call may incur charges !")
            callAlert.setIcon(drawable.ic_dialog_alert)
            callAlert.setPositiveButton("Ok") { _, _ ->
                // Proceed to make a call
                val callIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:+254745623611"))
                startActivity(callIntent)

                Toast.makeText(this, "  Continue to call Elitech Computers Manager ", Toast.LENGTH_SHORT).show()
            }
            callAlert.setNegativeButton("Cancel") { _, _ ->
                Toast.makeText(this, "cancelled", Toast.LENGTH_SHORT).show()
            }
            val alertDialog: AlertDialog = callAlert.create()
            alertDialog.setCancelable(false)
            alertDialog.show()
        }
    }
}
