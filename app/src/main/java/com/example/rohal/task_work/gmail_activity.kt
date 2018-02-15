package com.example.rohal.task_work

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import kotlinx.android.synthetic.main.activity_gmail_activity.*


class gmail_activity : AppCompatActivity() {
    private val RC_SIGN_IN = 7
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gmail_activity)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
        var mGoogleSignInClientClient = GoogleSignIn.getClient(this, gso)
        val account = GoogleSignIn.getLastSignedInAccount(this)
        val signInIntent = mGoogleSignInClientClient.getSignInIntent()
        startActivityForResult(signInIntent, RC_SIGN_IN)

    }


    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listene
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            Toast.makeText(this,result.toString(),Toast.LENGTH_LONG).show()
            val result1 = Auth.GoogleSignInApi.getSignInResultFromIntent(data)

            if(result1.isSuccess)
            {
                val acct=result1.signInAccount
                name.text=acct?.displayName
                email.text=acct?.email
                profile_pic.setImageURI(acct?.getPhotoUrl())

                Log.d("rohal","${acct?.email}")
            }
        }
    }

}