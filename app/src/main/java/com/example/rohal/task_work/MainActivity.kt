package com.example.rohal.task_work

import android.app.Activity
import android.app.FragmentManager
import android.app.FragmentTransaction
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import kotlinx.android.synthetic.main.activity_main.*
import android.widget.ArrayAdapter
import android.content.Context.INPUT_METHOD_SERVICE
import android.graphics.Bitmap
import android.nfc.Tag
import android.os.AsyncTask
import android.util.Log
import android.view.MotionEvent
import android.view.View.OnTouchListener
import android.widget.Toast
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.ProfilePictureView
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import kotlinx.android.synthetic.main.activity_facebook_activity.*
import kotlinx.android.synthetic.main.activity_gmail_activity.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.*


class MainActivity : AppCompatActivity()
{
    var status=1
    var BASE_URL="http://104.236.194.108:4010"
    //facebook connectivity variables
    var url_pic: String=" "
    var name:String=" "
    var email:String=" "
    var bmpuri: Bitmap?=null
    var bmpString:String=" "
    var bmpByteArray:ByteArray?=null
    val loggedIn = AccessToken.getCurrentAccessToken() == null
    lateinit var callbackManager: CallbackManager
    // End

     //Gmail

    private val RC_SIGN_IN = 7
    // End

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FacebookSdk.sdkInitialize(applicationContext)
        setContentView(R.layout.activity_main)
        //Facebook
        callbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance().registerCallback(callbackManager,
                object : FacebookCallback<LoginResult> {
                    override fun onSuccess(loginResult: LoginResult) {
                        val accessToken = AccessToken.getCurrentAccessToken()
                        val request = GraphRequest.newMeRequest(accessToken)
                        { `object`, response ->
                            setProfileToView(`object`)
                            var p= Person()
                            p.ur1=url_pic
                            p.name=name
                            p.email=email
                            p.type="Facebook"
                            FacebookDetail(p)
                        }
                        val parameters = Bundle()
                        parameters.putString("fields", "id,name,email,picture")
                        request.parameters = parameters
                        request.executeAsync()
                        LoginManager.getInstance().logOut()
                       status=0

                    }


                    override fun onCancel() {

                    }

                    override fun onError(exception: FacebookException) {

                    }
                })

        //Facebook

        fun Gmial()
        {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build()
            var mGoogleSignInClientClient = GoogleSignIn.getClient(this, gso)
            val account = GoogleSignIn.getLastSignedInAccount(this)
            val signInIntent = mGoogleSignInClientClient.getSignInIntent()
            startActivityForResult(signInIntent, RC_SIGN_IN)
           mGoogleSignInClientClient.signOut()
        }

        btn_Sign.setOnClickListener {
            var inte: Intent = Intent(this, Sign_up::class.java)
            startActivity(inte)
        }
        btn_fbk.setOnClickListener {
            //var inte: Intent = Intent(this, facebook_activity::class.java)
            //startActivity(inte)
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"))
        }

        btn_gmail.setOnClickListener {
            //var inte: Intent = Intent(this, gmail_activity::class.java)
            //startActivity(inte)
            Gmial()
        }

        main_page.setOnTouchListener(View.OnTouchListener { view, motionEvent ->hideSoftKeyboard(this)
            return@OnTouchListener true
        })

        btn_Login.setOnClickListener {

            val userName = txt_Username.text.toString()
            val password = txt_Pass.text.toString()
            val loginParams: JSONObject = JSONObject();
            loginParams.put("emial", userName)
            loginParams.put("password",password);
            val retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            val redditAPI = retrofit.create(RedditAPI::class.java)
            val headerMap = HashMap<String, String>()
            //headerMap.put("Content-Type", "application/raw")
            val call = redditAPI.login( userName, password)
            call.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: retrofit2.Response<ResponseBody>?)
                {
                    Log.d("server", "onResponse: Server Response: " + response.toString());

                    try{
                        val json:String = response?.body()!!.string();
                        Log.d("JSON", "onResponse: json: " + json);
                        var data:JSONObject? = null;
                        data =JSONObject(json);
//                            Log.d(TAG, "onResponse: data: " + data.optString("json"));


                        var result= JSONObject(json)
                        val params = result.getJSONObject("user")
                        var emial: String=params.getString("username")
                        Log.d("Detail","${params}")
                        var firstName: String=params.getString("firstName")
                        var phoneNumber: String=params.getString("phoneNumber")
                        var userPic: String=params.getString("userPic")
                        var id:String=params.getString("_id")
                        //var country: String=params.getString("country")
                        var pic: String= "http://104.236.194.108:4010/image/${userPic}"
                        var p= Person()
                        p.id=id
                        p.ur1=pic
                        p.name=firstName
                        p.email=emial
                        p.type="Facebook"
                        FacebookDetail(p)


                        //Log.d("detial","Emial is ${emial} First Name ${firstName} Phone Number  ${phoneNumber} User Pic is ${pic} Country is ${country}")

                    }catch (e: JSONException){
                        Log.e("JSONException", "onResponse: JSONException: ");
                    }catch (e: IOException){
                        Log.e("IOexception", "onResponse: JSONException: " );
                    }

                }
                override  fun  onFailure( call:Call<ResponseBody>,  t:Throwable) {
                    Log.e("OnFailure", "onFailure: Something went wrong: "  );
                    Toast.makeText(baseContext, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            })
        }
            ///
        }






    fun hideSoftKeyboard(activity: Activity) {
        val inputMethodManager = activity.getSystemService(
                Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(
                activity.currentFocus!!.windowToken, 0)
    }
    fun setProfileToView(jsonObject: JSONObject) = try {
        var id=jsonObject.get("id")
        val ur1: String="https://graph.facebook.com/${id}/picture?type=large"
        Log.d("Id ","${ur1}")
        url_pic=ur1
        name=jsonObject.getString("name")
        email=jsonObject.getString("email")

    } catch (e: JSONException) {
        e.printStackTrace();
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(status==1) {
            callbackManager.onActivityResult(requestCode, resultCode, data)
            super.onActivityResult(requestCode, resultCode, data)
        }
        else
        {
            if (requestCode == RC_SIGN_IN) {
                // The Task returned from this call is always completed, no need to attach
                // a listene
                val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
                Toast.makeText(this,result.toString(), Toast.LENGTH_LONG).show()
                val result1 = Auth.GoogleSignInApi.getSignInResultFromIntent(data)

                if(result1.isSuccess)
                {

                    val acct=result1.signInAccount
                    var p= Person()
                    p.ur1=acct?.photoUrl.toString()
                    p.name= acct?.displayName!!
                    p.email=acct.email.toString()
                    FacebookDetail(p)
                    Log.d("rohal","${acct?.email}")
                }

            }
        }
    }
    fun FacebookDetail( detial: Person)
    {

        Log.d("After","yes")
        //var int: Intent= Intent(this,Activity_UserDetail::class.java)
        var int: Intent= Intent(this,Activity_UserDetail::class.java)
        detial.type="Facebook"
        int.putExtra("Data",detial)
        startActivity(int)
    }

}

