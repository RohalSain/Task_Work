package com.example.rohal.task_work

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.facebook.drawee.backends.pipeline.Fresco
import kotlinx.android.synthetic.main.activity__user_detail.*
import kotlinx.android.synthetic.main.activity_add__feed.*
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException


class Activity_UserDetail : AppCompatActivity() {
    var BASE_URL = "http://104.236.194.108:4010"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fresco.initialize(this)
        setContentView(R.layout.activity__user_detail)
        var k = Person()
        var b: Bundle = intent.extras
        var p: Person = b.getParcelable("Data")
        k = p
        val type: String = p.type
        if (type.equals("Facebook")) {
            username.text = p.name
            old.text = p.email
            //phone.text=p.phone
            val url: Uri = Uri.parse("${p.ur1}")
            Log.d("url", "${url}")
            //country.text=p.country
            pass_pic.setImageURI(p.ur1)
            Log.d("saini", "${p.id}")

        } else if (type.equals("direct")) {
            username.text = p.name
            old.text = p.email
            //ph.text = p.phone
            val url: Uri = Uri.parse("${p.ur1}")
            Log.d("url", "${url}")
            country.text = p.country
            pass_pic.setImageURI(p.ur1)
            Log.d("saini", "${p.id}")
        }
        edit.setOnClickListener {

            var int: Intent = Intent(this, edit_activity::class.java)
            k.type = "Facebook"
            int.putExtra("Data", k)
            startActivity(int)
        }
        pass.setOnClickListener {

            var int: Intent = Intent(this, Manage_Password::class.java)
            k.type = "Facebook"
            int.putExtra("Data", k)
            startActivity(int)
        }
        feed.setOnClickListener {

            var int: Intent = Intent(this, Add_Feed::class.java)
            k.type = "Facebook"
            int.putExtra("Data", k)
            startActivity(int)
        }
        getfeed.setOnClickListener {
            val retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            val redditAP = retrofit.create(RedditAPI::class.java)
            var call = redditAP.getFeed(p.id)
            call.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: retrofit2.Response<ResponseBody>?) {
                    Log.d("server", "onResponse: Server Response: " + response.toString());

                    try {
                        val json: String = response?.body()!!.string();
                        Log.d("JSON", "onResponse: json: " + json);
                        var data: JSONObject? = null;
                        data = JSONObject(json);
//                            Log.d(TAG, "onResponse: data: " + data.optString("json"));

                        //Log.d("detial","Emial is ${emial} First Name ${firstName} Phone Number  ${phoneNumber} User Pic is ${pic} Country is ${country}")

                    } catch (e: JSONException) {
                        Log.e("JSONException", "onResponse: JSONException: ");
                    } catch (e: IOException) {
                        Log.e("IOexception", "onResponse: JSONException: ");
                    }

                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.e("OnFailure", "onFailure: Something went wrong: ");
                    Toast.makeText(baseContext, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            })
        }

    }
}
