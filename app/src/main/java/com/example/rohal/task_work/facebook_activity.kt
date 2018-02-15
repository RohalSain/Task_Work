package com.example.rohal.task_work

import android.accounts.Account
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.ProfilePictureView
import kotlinx.android.synthetic.main.activity_facebook_activity.*
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.graphics.ImageFormat
import android.util.Base64
import android.util.Log
import android.widget.ImageView
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.net.URL


class facebook_activity : AppCompatActivity() {
    var url_pic: String=" "
    var name:String=" "
    var email:String=" "
    var bmpuri:Bitmap?=null
    var bmpString:String=" "
    var bmpByteArray:ByteArray?=null


    val loggedIn = AccessToken.getCurrentAccessToken() == null
    lateinit var callbackManager: CallbackManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FacebookSdk.sdkInitialize(applicationContext)
        setContentView(R.layout.activity_facebook_activity)
        callbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance().registerCallback(callbackManager,
                object : FacebookCallback<LoginResult> {
                    override fun onSuccess(loginResult: LoginResult) {
                        val accessToken = AccessToken.getCurrentAccessToken()
                        val request = GraphRequest.newMeRequest(accessToken)
                        { `object`, response ->
                            setProfileToView(`object`)
                        }
                        val parameters = Bundle()
                        parameters.putString("fields", "id,name,email,picture")
                        request.parameters = parameters
                        request.executeAsync()
                        LoginManager.getInstance().logOut()

                    }


                    override fun onCancel() {

                    }

                    override fun onError(exception: FacebookException) {

                    }
                })
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"))
       next.setOnClickListener {
           var int: Intent= Intent(this,Activity_UserDetail::class.java)
           var p= Person()
           p.ur1=url_pic
           p.name=name
           p.email=email
           p.type="Facebook"
           int.putExtra("Data",p)
           startActivity(int)

       }
    }


        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            callbackManager.onActivityResult(requestCode, resultCode, data)
            super.onActivityResult(requestCode, resultCode, data)
        }

        fun setProfileToView(jsonObject: JSONObject) = try {

            //name.setText(jsonObject.getString("name"));
            profile_pic.setPresetSize(ProfilePictureView.NORMAL);
            profile_pic.setProfileId(jsonObject.getString("id"));
            var id=jsonObject.get("id")
            val ur1: String="https://graph.facebook.com/${id}/picture?type=large"
            Log.d("Id ","${ur1}")
            url_pic=ur1
            name=jsonObject.getString("name")
            email=jsonObject.getString("email")

        } catch (e: JSONException) {
            e.printStackTrace();
        }

}
