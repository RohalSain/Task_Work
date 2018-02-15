package com.example.rohal.task_work

import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_edit_activity.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_manage__password.*
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.HashMap

class Manage_Password : AppCompatActivity() {
    var BASE_URL = "http://104.236.194.108:4010"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage__password)
        var b: Bundle = intent.extras
        var p: Person = b.getParcelable("Data")
        val type: String = p.type
        if (type.equals("Facebook")) {
            username.text = p.name
            val url: Uri = Uri.parse("${p.ur1}")
            Log.d("url", "${url}")
            //country.text=p.country
            pic.setImageURI(url)
            Log.d("saini", "${p.id}")

        }
        Password.setOnClickListener {

            //
            var pass_old =old.text.toString()
            val pass_new = pass.text.toString()
            val retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            val redditAPI = retrofit.create(RedditAPI::class.java)
           var objects=PojoData(pass_old,pass_new)
            Log.d("name","${pass_new} and old one is ${pass_old}")
           val call = redditAPI.updatePassword("${p.id}",objects)
            Log.d("object ","${objects}")
            call.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>?) {
                    Log.d("server", "onResponse: Server Response: " + response.toString());

                    try {
                        val json: String = response?.body()!!.string();
                        Log.d("JSON", "onResponse: json: " + json);
                        var data: JSONObject? = null;
                        data = JSONObject(json)
                    } catch (e: JSONException) {
                        Log.e("JSONException", "onResponse: JSONException: ");
                    } catch (e: IOException) {
                        Log.e("IOexception", "onResponse: JSONException: ");
                    }

                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.e("OnFailure","${t}");
                    Toast.makeText(baseContext, "${t}", Toast.LENGTH_SHORT).show();
                }
            })
        }
    }
}
