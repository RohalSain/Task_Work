package com.example.rohal.task_work
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.app.LoaderManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity__user_detail.*
import kotlinx.android.synthetic.main.activity_edit_activity.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.dialog_box.view.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.*
import java.util.HashMap
import java.util.regex.Pattern

class edit_activity : AppCompatActivity() {
    var path: File?=null
    var f: File? = null
    var uri_next: Uri? = null
    var BASE_URL = "http://104.236.194.108:4010"
    var bmpuri: Bitmap? = null
    var Uri_get: Uri? = null
    var bmpString: String = " "
    var bmpByteArray: ByteArray? = null
    var i = 0
    var url_pic: String = " "
    var country: String = " "
    val REQUEST_IMAGE_CAPTURE = 1
    val IMAGEREQUESTCODE = 8242008
    private val PICK_FROM_FILE = 300
    val GALLERY = 100
    private var mCapturedImageURI: Uri? = null
    var picturePath = ""
    var newfile: File? = null
    val EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-    Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
    var mCurrentPhotoPath: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_activity)
        var b: Bundle= intent.extras
        var p:Person=b.getParcelable("Data")
        val type: String=p.type
        if(type.equals("Facebook"))
        {
            user.setText(p.name,TextView.BufferType.EDITABLE)
            em.text=p.email
            //phone.text=p.phone
            val url: Uri = Uri.parse("${p.ur1}")
            Log.d("url","${url}")
            //country.text=p.country
            editpic.setImageURI(url)
            Log.d("saini","${p.id}")

        }
        else if(type.equals("direct"))
        {
            username.text=p.name
            em.text=p.email
            //ph.text=p.phone
            val url: Uri = Uri.parse("${p.ur1}")
            Log.d("url","${url}")
           // country.text=p.country
            editpic.setImageURI(url)
            Log.d("saini","${p.id}")
        }
        //

        //
        save.setOnClickListener {
            val retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            val redditAPI = retrofit.create(RedditAPI::class.java)
            val name = user.text.toString().trim()
            //val email = txt_email.text.toString().trim()
            //val password = txt_password.text.toString().trim()
            //val phoneNumber = txt_phonenumber.text.toString().trim()
            //val redditAP = retrofit.create(RedditAPI::class.java)
            //var file:File= File(path)

            // Log.d("Path is ","${query}")
            //Log.d("Detail is", "name is ${name} emial is ${email} pass is ${password} phone no ${phoneNumber} country ${country}")
            val headerMap = HashMap<String, RequestBody>()
            headerMap.put("firstName", RequestBody.create(MediaType.parse("text/plain"), name))
            headerMap.put("lastName", RequestBody.create(MediaType.parse("text/plain"), name))
            var query:String="userPic\";filename=\"${f?.absolutePath}"
            headerMap.put(query, RequestBody.create(MediaType.parse("/Images"),FilePicture()))
            var call = redditAPI.update("${p.id}",headerMap)
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
    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {

            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_IMAGE_CAPTURE) {
            val extras = data.extras
            val imageBitmap = extras!!.get("data") as Bitmap
            mCapturedImageURI = data?.data
            picturePath =getFileStreamPath(mCapturedImageURI.toString()).absolutePath
            //
            // createImageFile()
            //
            savePicture(imageBitmap, "Rohal.jpg")
            bmpuri = imageBitmap
            val baos: ByteArrayOutputStream = ByteArrayOutputStream();
            bmpuri?.compress(Bitmap.CompressFormat.PNG, 100, baos);
            bmpByteArray = baos.toByteArray();
            bmpString = Base64.encodeToString(bmpByteArray, Base64.DEFAULT);
            url_pic = bmpString


        } else if (requestCode == PICK_FROM_FILE && resultCode == RESULT_OK) {
            try {
                mCapturedImageURI = data?.data
                Log.d("path", "${mCapturedImageURI}")
                editpic.setImageURI(mCapturedImageURI)
                url_pic = mCapturedImageURI.toString()
                val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver,mCapturedImageURI)
                Log.d("Rohal", "${url_pic}")
                bmpuri=bitmap
                //picturePath =getFileStreamPath(mCapturedImageURI.toString()).absolutePath
                //newfile = File(picturePath)
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this, "Image_notfound", Toast.LENGTH_LONG).show()
            }
        }

    }

    fun savePicture(bm: Bitmap, imgName: String) {
        var fOut: OutputStream? = null
        val strDirectory = Environment.getExternalStorageDirectory().toString()
        var f = File(strDirectory, imgName)
        try {
            fOut = FileOutputStream(f)

            /**Compress image */
            bm.compress(Bitmap.CompressFormat.JPEG, 85, fOut)
            fOut.flush()
            fOut.close()

            /**Update image to gallery */
            MediaStore.Images.Media.insertImage(contentResolver,
                    f.absolutePath, f.name, f.name)

        } catch (e: Exception) {
            e.printStackTrace()
        }
        MediaScannerConnection.scanFile(this, arrayOf(f.toString()), null
        ) { path, uri ->
            Log.i("ExternalStorage", "Scanned $path:")
            url_pic = uri.toString()
            uri_next = uri
            Log.d("rohal", "${url_pic}")
            Log.i("ExternalStorage", "-> uri=" + uri)
            editpic.setImageURI(uri)
        }

        //val Image_path = Environment.getExternalStorageDirectory().toString() + "/Pictures/folder_name/" + iname

        Log.d("Path is ", "$path")
    }


    fun FilePicture(): File {
        var f: File = File(cacheDir, "Saii")
        f.createNewFile()
//Convert bitmap to byte array
        var bitmap: Bitmap? = bmpuri
        var bos: ByteArrayOutputStream = ByteArrayOutputStream();
        bitmap?.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
        var bitmapdata = bos.toByteArray();

//write the bytes in file
        var fos: FileOutputStream = FileOutputStream(f);
        fos.write(bitmapdata);
        fos.flush();
        fos.close();

        return f
    }
    fun gallery() {
        val intent = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_FROM_FILE)
    }
    fun Dialog_box(view: View) {
        var builder: AlertDialog.Builder = AlertDialog.Builder(this)
        var inflater: LayoutInflater = layoutInflater
        var view: View = inflater.inflate(R.layout.dialog_box, null)
        builder.setView(view)
        builder.setTitle("Choose Option")
        var Dilog: Dialog = builder.create()
        Dilog.show()
        view.btn_Gallery.setOnClickListener {
            gallery()
            builder.setInverseBackgroundForced(true)
            Dilog.dismiss()
        }
        view.btn_Camera.setOnClickListener {
            dispatchTakePictureIntent()
            Dilog.dismiss()
        }


    }
}
