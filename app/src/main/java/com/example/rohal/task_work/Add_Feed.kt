package com.example.rohal.task_work

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.media.Image
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
import kotlinx.android.synthetic.main.activity_edit_activity.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.HashMap
import com.facebook.common.util.UriUtil
import kotlinx.android.synthetic.main.activity_add__feed.*
import kotlinx.android.synthetic.main.dialog_box.view.*
import okhttp3.*
import okio.Okio
import java.io.*
import java.util.regex.Pattern


class Add_Feed : AppCompatActivity() {
    var path: File? = null
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
        setContentView(R.layout.activity_add__feed)
        var b: Bundle = intent.extras
        var p: Person = b.getParcelable("Data")
        val type: String = p.type
        if (type.equals("Facebook")) {
            //user.setText(p.name, TextView.BufferType.EDITABLE)
            //em.text = p.email
            //phone.text=p.phone
            val url: Uri = Uri.parse("${p.ur1}")
            Log.d("url", "${url}")
            //country.text=p.country
            //editpic.setImageURI(url)
            Log.d("saini", "${p.id}")

        }
        get.setOnClickListener {
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
        ok.setOnClickListener {
            val retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            val redditAP = retrofit.create(RedditAPI::class.java)
            //var file:File= File(path)

            // Log.d("Path is ","${query}")
            val headerMap = HashMap<String, RequestBody>()
            headerMap.put("location", RequestBody.create(MediaType.parse("text/plain"), "India"))
            headerMap.put("breed", RequestBody.create(MediaType.parse("text/plain"), ""))
            headerMap.put("caption", RequestBody.create(MediaType.parse("text/plain"), "Punjab"))
            headerMap.put("locationCountry", RequestBody.create(MediaType.parse("text/plain"), "India"))
            headerMap.put("locationCity", RequestBody.create(MediaType.parse("text/plain"), "Ropar"))
            headerMap.put("locationState", RequestBody.create(MediaType.parse("text/plain"), "Punjab"))
            //var bannerPicture1: RequestBody = RequestBody.create(MediaType.parse("image/*"), FilePicture());
            var array= ArrayList<MultipartBody.Part>()
            var file = FilePicture()
                var requestBody:RequestBody= RequestBody.create(MediaType.parse("image/*"), file);
                var filePart: MultipartBody.Part= MultipartBody.Part.createFormData("image", file.getName(),
                        requestBody);
            array.add(filePart)
            array.add(filePart)
            headerMap.put("Image", RequestBody.create(MediaType.parse("${f?.name}"),FilePicture()))
            //var query:String="userPic\";filename=\"${f?.absolutePath}"
            //headerMap.put(query, RequestBody.create(MediaType.parse("/Images"),FilePicture()))           ///

            var call = redditAP.updatefeed(p.id,headerMap,array)
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
            picturePath = getFileStreamPath(mCapturedImageURI.toString()).absolutePath
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
                editpic1.setImageURI(mCapturedImageURI)
                Log.d("path", "${mCapturedImageURI}")
                //pass_pic.setImageURI(mCapturedImageURI)
                url_pic = mCapturedImageURI.toString()
                var bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, mCapturedImageURI)
                Log.d("Rohal", "${url_pic}")
                bmpuri = bitmap
                //picturePath =getFileStreamPath(mCapturedImageURI.toString()).absolutePath
                //newfile = File(picturePath)
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this, "${e}", Toast.LENGTH_LONG).show()
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

        return RequestBody.create(MediaType.parse("/images"), contentUri)
    }
    fun  prepareFilePart(partName: String):MultipartBody.Part
    {
    // https://github.com/iPaulPro/aFileChooser/blob/master/aFileChooser/src/com/ipaulpro/afilechooser/utils/FileUtils.java
    // use the FileUtils to get the actual file by uri
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
        fos.close();// create RequestBody instance from file
     var requestFile:RequestBody=
            RequestBody.create(
                    MediaType.parse(getContentResolver().getType(mCapturedImageURI)),f);

    // MultipartBody.Part is used to send also the actual file name
    return MultipartBody.Part.createFormData(partName,f.name, requestFile);
}


}

