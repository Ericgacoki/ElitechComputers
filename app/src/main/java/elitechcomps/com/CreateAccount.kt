package elitechcomps.com

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_create_account.*
import kotlinx.android.synthetic.main.activity_signin.*
import kotlinx.android.synthetic.main.activity_warning.view.*

private var mDatabaseReference: DatabaseReference? = null
private var mDatabase: FirebaseDatabase? = null
private var mAuth: FirebaseAuth? = null

/**     global nullable variables */

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

        offlineView.visibility = View.INVISIBLE
        textOffline.visibility = View.INVISIBLE


        checkNetConnection()
        initialise()
        cleaner()
        handleButtonClicks()
    }

    private fun initialise() {
        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mDatabase!!.reference.child("Students")
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

    private fun checkNetConnection(): Boolean {
        var isOnline = true

        if (!online(this)) {
            isOnline = false

            offlineView.visibility = View.VISIBLE
            textOffline.visibility = View.VISIBLE

            Handler().postDelayed({
                offlineView.visibility = View.INVISIBLE
                textOffline.visibility = View.INVISIBLE
            }, 5000)
        }
        return isOnline
    }

    private fun handleButtonClicks() {
        btnsubmit.setOnClickListener {
            createNewAccount()
            checkNetConnection()

        }

        btnSignin.setOnClickListener {
            val intentSignIn = Intent(this, SignInActivity::class.java)
            if (intentSignIn.resolveActivity(packageManager) != null) {
                startActivity(intentSignIn)
                finish()
            }
        }

        btnHelp.setOnClickListener {
            //TODO() do something
        }

        reportIssue.setOnClickListener {
            //TODO() do something
        }
    }

    private fun passwordIsStrong(password: String): Boolean {
        var strong = false

        if (
        // check password's length

            (password.trim().length >= 8)
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
             *              Password must be strong to be able to login
             */

            if (passwordIsStrong(studentpassword!!)) {

                /**
                 *     create account here .
                 */
                checkNetConnection()
                disableButtons()

                mAuth!!
                    .createUserWithEmailAndPassword(studentEmail!!, studentpassword!!)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val studentID = mAuth!!.currentUser!!.uid
                            verifyEmail()

                            /**
                             *               update User profile info (save data in a real time database) .
                             */

                            val currentStudentDB = mDatabaseReference!!.child(studentID)
                            currentStudentDB.apply {
                                child("First Name").setValue(firstName)
                                child("Second Name").setValue(secondName)
                                child("Phone").setValue(phoneNumber)
                                child("Student's Email").setValue(studentEmail)
                                child("Password").setValue(studentpassword)
                                child("Suspended").setValue(false)
                            }
                            startNextActivity()
                        } else {
                            enableButtons()
                            //snack("Authentication failed")
                        }
                    }
            } else {
                /**
                 *      show custom advice /warning dialog for entering a weak password .
                 */
                val customWarningView =
                    LayoutInflater.from(this).inflate(R.layout.activity_warning, null)

                val warningDialog = AlertDialog.Builder(this, 0)
                //TODO() work on this exit (X) button

                customWarningView.btnCancel.setOnClickListener {}

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
                        "Verification email sent to ${User.email}",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Toast.makeText(
                        this@CreateAccount,
                        "Invalid Email,PleaseTryAgain.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }

    private fun startNextActivity() {

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
            text = "loading..."
        }
        btnSignin.isEnabled = false
        reportIssue.isEnabled = false
        btnHelp.isEnabled = false

        clean.apply {
            isEnabled = false
            setImageDrawable(getDrawable(R.drawable.ic_clean_disabled))
        }
        showLoading()
    }

    private fun enableButtons() {
        btnsubmit.apply {
            isEnabled = true
            background = getDrawable(R.drawable.btnselector)
            text = "submit"
        }
        btnSignin.isEnabled = true
        reportIssue.isEnabled = true
        btnHelp.isEnabled = true
        clean.apply {
            isEnabled = true
            setImageDrawable(getDrawable(R.drawable.ic_clean))
        }
        hideLoading()
    }

    private fun showLoading() {
        logInProgress.apply {
            visibility = View.VISIBLE
            setViewColor(getColor(R.color.Orange))
            startAnim()
        }
    }

    private fun hideLoading() {
        logInProgress.apply {
            visibility = View.INVISIBLE
            setViewColor(getColor(R.color.Fade))
            stopAnim()
        }
    }

    private var exit = false
    override fun onBackPressed() {
        if (exit) {
            super.onBackPressed()
        }
        Toast.makeText(this, "press again to exit", LENGTH_SHORT).show()
        exit = true
        Handler().postDelayed({
            kotlin.run { exit = false }
        }, 2000)
    }
}