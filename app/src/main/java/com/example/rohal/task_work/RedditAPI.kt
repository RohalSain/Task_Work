package com.example.rohal.task_work

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*
import okhttp3.RequestBody
import retrofit2.http.PartMap
import retrofit2.http.POST
import retrofit2.http.Multipart
import com.google.gson.JsonObject
import org.json.JSONObject


/**
 * Created by emilence on 2/2/18.
 */
interface RedditAPI {

    @POST("/v1/account/login/")
    fun login(
            //@HeaderMap headers: Map<String, String>,
            @Query("email") user: String, //?user=codingwithmitch
            @Query("password") password: String//&passwd=Mitchtabian1234!
    ): Call<ResponseBody>


    @Multipart
    @POST("/v1/account/register")
    fun uploadFileWithPartMap(
            @PartMap partMap: HashMap<String,RequestBody>
    ): Call<ResponseBody>

    @Multipart
    @PUT("/v1/account/update/{id}")
    fun update(
            @Path ("id") token:String,
            @PartMap  partMap: HashMap<String, RequestBody>
    ): Call<ResponseBody>

    @POST("/v1/account/resetPassword/{id}")
    fun updatePassword(
            @Path ("id") to:String,
            @Body body: PojoData
             ): Call<ResponseBody>
    @Multipart
    @POST("/v1/addFeed/add/{id} ")
    fun updatefeed(
            @Path ("id") to:String,
            @PartMap partMap: HashMap<String, RequestBody>,
            @Part image: ArrayList<MultipartBody.Part>
    ): Call<ResponseBody>

    @GET("/v1/addFeed/read/{id}")
    fun getFeed(
            @Path("id") user:String
    ): Call<ResponseBody>
}