package com.example.rohal.task_work

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.facebook.drawee.backends.pipeline.Fresco
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.dialog_box.view.*
import java.util.regex.Pattern
import android.util.Base64
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

class Sign_up : AppCompatActivity() {
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
        Fresco.initialize(this)
        setContentView(R.layout.activity_sign_up)
        btn_back.setOnClickListener {
            //var inte: Intent = Intent(this,MainActivity::class.java)
            //startActivity(inte)
            finish()
        }
        btn_country.setOnClickListener()
        {
            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            builder.setTitle("Select Country")
            val country_Name: Array<String> = resources.getStringArray(R.array.country_arrays)
            builder.setItems(country_Name, DialogInterface.OnClickListener { dialog, which ->
                btn_country.setText("${country_Name[which]}", TextView.BufferType.EDITABLE);
                country = "${country_Name[which]}"
            }
            )
                    .create()
                    .show()

        }

        btn_ok.setOnClickListener {
            var status: Boolean = true

            if (txt_username.text.length < 3) {
                status = false
                Toast.makeText(this, "Invalid Username", Toast.LENGTH_SHORT).show()

            } else if (txt_email.text.isEmpty() || (!validEmail(txt_email.text.toString()))) {
                status = false
                Toast.makeText(this, "Invalid Email", Toast.LENGTH_SHORT).show()
            } else if (txt_password.text.isEmpty()) {
                status = false
                Toast.makeText(this, "Enter Secure password", Toast.LENGTH_SHORT).show()
            } else if (txt_phonenumber.text.length < 10) {
                status = false
                Toast.makeText(this, "Enter Valid Phone Number", Toast.LENGTH_SHORT).show()
            } else if (btn_country.text.length < 2) {
                status = false
                Toast.makeText(this, "Select Country", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Successfully Entered", Toast.LENGTH_LONG).show()

                /*
                var intent: Intent= Intent(this,Activity_UserDetail::class.java)
                var p= Person()
                p.name=txt_username.text.toString()
                p.email=txt_email.text.toString()
                p.phone=txt_phonenumber.text.toString()
                p.country=country
                p.type="direct"
                p.ur1=url_pic
                intent.putExtra("Data",p)
                startActivity(intent)
                */

                //
                Uri_get = Uri.parse(url_pic)

                //
                var fileUri: Uri = Uri.parse("$url_pic")
                val retrofit = Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                val name = txt_username.text.toString().trim()
                val email = txt_email.text.toString().trim()
                val password = txt_password.text.toString().trim()
                val phoneNumber = txt_phonenumber.text.toString().trim()
                val redditAP = retrofit.create(RedditAPI::class.java)
                //var file:File= File(path)

               // Log.d("Path is ","${query}")
                Log.d("Detail is", "name is ${name} emial is ${email} pass is ${password} phone no ${phoneNumber} country ${country}")
                val headerMap = HashMap<String, RequestBody>()
                headerMap.put("firstName", RequestBody.create(MediaType.parse("text/plain"), name))
                headerMap.put("lastName", RequestBody.create(MediaType.parse("text/plain"), "GGGGG"))
                headerMap.put("email", RequestBody.create(MediaType.parse("text/plain"), email))
                headerMap.put("password", RequestBody.create(MediaType.parse("text/plain"), password))
                headerMap.put("phoneNumber", RequestBody.create(MediaType.parse("text/plain"), phoneNumber))
                headerMap.put("country", RequestBody.create(MediaType.parse("text/plain"), "India"))
                var query:String="userPic\";filename=\"${f?.absolutePath}"
                headerMap.put(query, RequestBody.create(MediaType.parse("/Images"),FilePicture()))           ///

                var call = redditAP.uploadFileWithPartMap(headerMap)
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

    fun list_box(view: View) {
        var builder: AlertDialog.Builder = AlertDialog.Builder(this)
        var inflater: LayoutInflater = layoutInflater
        var view: View = inflater.inflate(R.layout.dialog_box, null)
        builder.setView(view)
        builder.setTitle("Choose Option")
        var Dilog: Dialog = builder.create()
        Dilog.show()

    }

    fun gallery() {
        val intent = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_FROM_FILE)
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
                pass_pic.setImageURI(mCapturedImageURI)
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
            pass_pic.setImageURI(uri)
        }

        //val Image_path = Environment.getExternalStorageDirectory().toString() + "/Pictures/folder_name/" + iname

        Log.d("Path is ", "$path")
    }

    fun validEmail(email: String): Boolean {
        var pattern: Pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
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

    fun CreateprepareFilePart(decription: String): RequestBody {
        return RequestBody.create(MultipartBody.FORM, decription)
    }


    fun getTempFile(context: Context, url: String): File? =
            Uri.parse(url)?.lastPathSegment?.let { filename ->
                File.createTempFile(filename, null, context.cacheDir)
            }

    private fun getRealPathFromURI(contentUri: File): RequestBody {

        return RequestBody.create(MediaType.parse("/images"),contentUri)
    }



}