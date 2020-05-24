package elitechcomps.com

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color.BLUE
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_reverify_email.view.*
import kotlinx.android.synthetic.main.activity_signin.*


var trials = 0
var elapseTime = 0

private var mAuth: FirebaseAuth? = FirebaseAuth.getInstance()
private var verified: Boolean = false

var userPassword: String? = null
var userEmail: String? = null

@RequiresApi(Build.VERSION_CODES.N)

class SignInActivity : AppCompatActivity() {

    override fun onStart() {
        super.onStart()
        mAuth = FirebaseAuth.getInstance()
        // verified = mAuth!!.currentUser!!.isEmailVerified
        // mAuth!!.currentUser!!.reload()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_signin)

        viewOffline.visibility = View.INVISIBLE
        offlineText.visibility = View.INVISIBLE
        signInProgress.visibility = View.INVISIBLE

        checkOnline()
        enableButtons()
        handleClicks()
    }

    @Suppress("DEPRECATION")
    private fun online(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo?.isConnected == true
    }

    private fun toast(msg: String) {
        Toast.makeText(this, msg, LENGTH_LONG).show()
    }

    private fun checkOnline(): Boolean {
        var isOnline = true

        if (!online(this)) {
            isOnline = false

            viewOffline.visibility = View.VISIBLE
            offlineText.visibility = View.VISIBLE

            Handler().postDelayed({
                viewOffline.visibility = View.INVISIBLE
                offlineText.visibility = View.INVISIBLE
            }, 5000)
        }

        return isOnline
    }

    private fun enableButtons() {
        val views: Array<View> = arrayOf(trialView, tryIn, chronometer, sec)
        for (view in views) {
            view.visibility = View.INVISIBLE
        }
        chronometer.apply {
            base = SystemClock.elapsedRealtime() + 0
            stop()
        }

        btnSignIn.apply {
            background = getDrawable(R.drawable.signinbtn)
            isEnabled = true
        }
        btnCreateAccount.apply {
            background = getDrawable(R.drawable.createaccountbtn)
            isEnabled = true
        }

        btnForgotPassword.isEnabled = true
        btnReportIssue.isEnabled = true
    }

    private fun disableButtons() {
        val views: Array<View> = arrayOf(trialView, tryIn, chronometer, sec)
        for (view in views) {
            view.visibility = View.VISIBLE
        }
        chronometer.apply {
            isCountDown = true
            base = SystemClock.elapsedRealtime() + elapseTime
            start()
        }

        btnSignIn.apply {
            background = getDrawable(R.drawable.signindisabled)
            isEnabled = false
        }

        btnCreateAccount.apply {
            background = getDrawable(R.drawable.signindisabled)
            isEnabled = false
        }

        btnForgotPassword.isEnabled = false
        btnReportIssue.isEnabled = false
        rememberMe.isChecked = false

        Handler().postDelayed({
            kotlin.run {
                enableButtons()
            }
        }, elapseTime.toLong())
    }

    @SuppressLint("InflateParams")
    private fun signInVerifiedUser() {

        // TODO() check if account is suspended

        userEmail = signInEmail!!.text.toString().trim()
        userPassword = signInPassword!!.text.toString().trim()

        if (/*verified && */userEmail!!.isNotEmpty() && userPassword!!.isNotEmpty()) {
            showLoading()
            mAuth!!
                .signInWithEmailAndPassword(userEmail!!, userPassword!!)
                .addOnCompleteListener(this) { task ->

                    if (task.isSuccessful) {
                        toast("Signed in successfully")
                        hideLoading()
                        startNextActivity()
                    } else {
                        toast("Sign in Failed")
                        hideLoading()
                    }
                }
        } else if (userEmail!!.isEmpty() || userPassword!!.isEmpty()) {
            // set an error msg to the empty field

            val inputs: Array<TextInputEditText> = arrayOf(signInEmail, signInPassword)
            for (input in inputs) {
                if (input.text!!.trim().isEmpty()) {
                    input.error = "${input.hint} is required"
                }
            }
        }

        /**
         *  if email is correctly typed and there is an account for that email, then, it's possible that the email is not verified .
         */
        else if (!(mAuth!!.currentUser!!.isEmailVerified)) {
            val customEmailReVerifyLayout =
                LayoutInflater.from(this).inflate(R.layout.activity_reverify_email, null)
            val re_verifyUserDialog = AlertDialog.Builder(this, 0)
            customEmailReVerifyLayout.btnReVerifyEmail.setOnClickListener {
                reVerifyEmail()
                re_verifyUserDialog.create().dismiss()
            }
            re_verifyUserDialog.apply {
                setView(customEmailReVerifyLayout)
                create().show()
            }

            toast("${mAuth!!.currentUser!!.email} is not verified")
        } else {
            toast("unknown error occurred")
        }
    }

    private fun handleClicks() {
        btnSignIn.setOnClickListener {

            trials += 1
            elapseTime += 6000

            when {
                trials % 5 == 0 -> disableButtons()
                trials >= 30 -> {
                    disableButtons()
                    toast("Try again when you're sure")
                }
            }
            checkOnline()
            signInVerifiedUser()
        }

        btnCreateAccount.setOnClickListener {
            val intentCreateAccount = Intent(this, CreateAccount::class.java)
            if (intentCreateAccount.resolveActivity(packageManager) != null) {
                startActivity(intentCreateAccount)
                finish()
            }
        }

        btnForgotPassword.setOnClickListener {
            val intentForgotPassword = Intent(this, ForgotPassword::class.java)
            if (intentForgotPassword.resolveActivity(packageManager) != null) {
                startActivity(intentForgotPassword)
                finish()
            }
        }

        btnReportIssue.setOnClickListener {
        }
    }

    private fun reVerifyEmail() {

        val User: FirebaseUser? = mAuth!!.currentUser
        User!!
            .sendEmailVerification()
            .addOnCompleteListener(this) { task: Task<Void> ->

                if (task.isSuccessful) {
                    // TODO() recreate activity to remove the alert dialog
                    toast("Verification email sent to ${User.email}")
                } else if ((task.isCanceled) || (!task.isSuccessful)) {
                    toast("An error has occurred!")
                }
            }
    }

    private fun showLoading() {
        signInProgress.apply {
            visibility = View.VISIBLE
            setViewColor(getColor(R.color.Orange))
            setRoundColor(BLUE)
            startAnim()
        }
    }

    private fun hideLoading() {
        signInProgress.apply {
            visibility = View.INVISIBLE
            stopAnim()
        }
    }

    private fun startNextActivity() {
        val intent = Intent(this, ElitechContentsActivity::class.java)
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
            finish()
        }
    }

    private var doubleBackToExit: Boolean = false
    override fun onBackPressed() {
        if (doubleBackToExit) {
            super.onBackPressed()
        } else {
            toast("press again to exit")
            doubleBackToExit = true
            Handler().postDelayed({
                kotlin.run { doubleBackToExit = false }
            }, 2000)
        }
    }
}