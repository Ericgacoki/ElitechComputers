package elitechcomps.com

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_authentication.*
import kotlinx.android.synthetic.main.activity_create_account.*

//Firebase references
private var mDatabaseReference: DatabaseReference? = null
private var mDatabase: FirebaseDatabase? = null
private var mAuth: FirebaseAuth? = null
private const val TAG = "CreateAccount"

/** global nullable variables */

private var firstName: String? = null
private var secondName: String? = null
private var phoneNumber: String? = null
private var studentEmail: String? = null
private var studentpassword: String? = null
private var confirmpassword: String? = null

class CreateAccount : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_create_account)

        // Animate some views
        profilePicture.startAnimation(AnimationUtils.loadAnimation(this, R.anim.animfromright))
        btnsubmit.startAnimation(AnimationUtils.loadAnimation(this, R.anim.animfrombtm))
        signal.visibility = View.INVISIBLE


        if (!online(this)) {
            Toast.makeText(this, " No internet ", Toast.LENGTH_LONG).show()
        }

        initialise()
        cleaner()

    }

    private fun initialise() {

        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance() //.setPersistenceEnabled(true)
        mDatabaseReference = mDatabase!!.reference.child("Students")
        btnsubmit.setOnClickListener { createNewAccount() }
    }

    private fun cleaner() {

        val ets: Array<TextInputEditText> =
            arrayOf(firstname, secondname, phone, useremail, userpassword, confirmPassword)

        clean.setOnClickListener {
            for (et in ets) {
                et.setText("")
            }
        }
    }

    @Suppress("DEPRECATION")
    private fun online(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo?.isConnected == true
    }

    private fun passwordIsStrong(password: String): Boolean {
        var strong = false

        if (
        // check password's length

            (password.trim().length > 7)
            &&

            // Check if password contains special characters

            ((password.contains("@", true)) ||
                    (password.contains("_", true)) ||
                    (password.contains("#", true)) ||
                    (password.contains("$", true)) ||
                    (password.contains("%", true)) ||
                    (password.contains("&", true)) ||
                    (password.contains("*", true)) ||
                    (password.contains("!", true)) ||
                    (password.contains("?", true)))
            &&

            //check if password contains at least one number

            ((password.contains("0", true)) ||
                    (password.contains("1", true)) ||
                    (password.contains("2", true)) ||
                    (password.contains("3", true)) ||
                    (password.contains("4", true)) ||
                    (password.contains("5", true)) ||
                    (password.contains("6", true)) ||
                    (password.contains("7", true)) ||
                    (password.contains("8", true)) ||
                    (password.contains("9", true)))

        ) {
            strong = true
        }

        return strong
    }


    @SuppressLint("InflateParams")
    private fun createNewAccount() {

        firstName = firstname?.text.toString().trim()
        secondName = secondname?.text.toString().trim()
        phoneNumber = phone?.text.toString().trim()
        studentEmail = useremail?.text.toString().trim()
        studentpassword = userpassword?.text.toString().trim()
        confirmpassword = confirmPassword?.text.toString().trim()


        val inputs: Array<String?> =
            arrayOf(
                firstName, secondName, phoneNumber,
                studentEmail, studentpassword, confirmpassword
            )
        val inputsIDs: Array<TextInputEditText> =
            arrayOf(firstname, secondname, phone, useremail, userpassword, confirmPassword)

        if (inputs[0]!!.isNotEmpty() && inputs[1]!!.isNotEmpty() && inputs[2]!!.isNotEmpty() &&
            inputs[3]!!.isNotEmpty() && inputs[4]!!.isNotEmpty() && (inputs[4]!! == inputs[5]!!)
        ) {

            /**
             *              Password must be strong
             */

            if (passwordIsStrong(studentpassword!!)) {

                // create account here
                //TODO show loading and signal in btnSubmit drawable end
                disableButtons()

                mAuth!!
                    .createUserWithEmailAndPassword(studentEmail!!, studentpassword!!)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {

                            Log.d(TAG, "create user with email: successful")
                            val studentID = mAuth!!.currentUser!!.uid

                            verifyEmail()

                            /**
                             *               update User profile info (save data in a real time database)
                             */

                            val currentStudentDB = mDatabaseReference!!.child(studentID)
                            currentStudentDB.apply {
                                child("Full Name").setValue("$firstName $secondName")
                                child("Phone").setValue(phoneNumber)
                                child("Student's Email").setValue(studentEmail)
                                child("Password").setValue(studentpassword)
                            }

                            startNextActivity()

                        } else {
                            //  TODO() stop loading

                            Log.w(TAG, "create user with email: failed")
                            enableButtons()
                            //snack("Authentication failed")

                        }
                    }
            } else {
                // show custom advice/ warning dialog
                val customWarningView =
                    LayoutInflater.from(this).inflate(R.layout.activity_warning, null)

                val warningDialog = AlertDialog.Builder(this, 0)
                warningDialog.apply {
                    setView(customWarningView)
                    create().show()
                }
            }
        } else if ((inputs[4] != inputs[5]) && (inputs[4]!!.isNotEmpty() && (inputs[5]!!.isNotEmpty()))) {

            inputsIDs[4].error = "passwords mismatch"
            inputsIDs[5].error = "passwords mismatch"

        } else {
            for (id in inputsIDs) {
                if (id.text.toString().trim().isBlank()) {
                    id.error = "${id.hint} is required"
                }
            }
        }
    }

    private fun verifyEmail() {
        val User = mAuth!!.currentUser
        User!!
            .sendEmailVerification()
            .addOnCompleteListener(this) { task: Task<Void> ->

                if (task.isSuccessful) {
                    Toast.makeText(
                        this@CreateAccount,
                        "Verification email sent to   ${User.email}",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Log.w(TAG, "sendEmailVerification", task.exception)
                    Toast.makeText(
                        this@CreateAccount,
                        "Invalid Email, Please try again.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }

    private fun startNextActivity() {

        //start next activity
        val intent = Intent(this@CreateAccount, ElitechContentsActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        finish()
    }
    //    TODO("solve error") work on this snack function

    private fun snack(text: String) {
        view
        Snackbar.make(view, text, Snackbar.LENGTH_LONG)
            .setAction("Action", null).show()
    }


    private fun disableButtons() {
        btnsubmit.apply {
            isEnabled = false
            background = getDrawable(R.drawable.greybtn)
        }

        clean.apply {
            isEnabled = false
            setImageDrawable(getDrawable(R.drawable.ic_clean_disabled))
        }

        signal.visibility = View.VISIBLE
    }

    private fun enableButtons() {
        btnsubmit.apply {
            isEnabled = true
            background = getDrawable(R.drawable.btnselector)
        }

        clean.apply {
            isEnabled = true
            setImageDrawable(getDrawable(R.drawable.ic_clean))
        }
        signal.visibility = View.INVISIBLE
    }
}
