package com.example.idsystemkoisk

import android.content.Context
import android.content.DialogInterface
import android.content.res.TypedArray
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.os.*
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import com.android.volley.toolbox.Volley
import com.example.idsystemkoisk.databinding.ActivityChangeIdphotoBinding
import com.ttv.segment.TTVException
import com.ttv.segment.TTVSeg
import io.fotoapparat.Fotoapparat
import io.fotoapparat.parameter.Resolution
import io.fotoapparat.preview.Frame
import io.fotoapparat.selector.front
import io.fotoapparat.util.FrameProcessor
import io.fotoapparat.view.CameraView
import java.io.ByteArrayOutputStream


class ChangeIDPhoto : AppCompatActivity() {
    lateinit var binding: ActivityChangeIdphotoBinding
    lateinit var id:String
    lateinit var url:String

    private val TAG = "ChangeIDPhoto"
    private val permissionsDelegate = PermissionsDelegate(this)
    private var hasPermission = false

    private var appCtx: Context? = null
    private var licenseValid = false
    private var humanSegInited = false
    private var cameraView: CameraView? = null
    private var imageView: ImageView? = null

    private var frontFotoapparat: Fotoapparat? = null

    private val mHandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            val i: Int = msg.what
            if (i == 0) {
                imageView!!.setImageBitmap(msg.obj as Bitmap)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangeIdphotoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        url = resources.getString(R.string.app_url)

        appCtx = applicationContext
        cameraView = binding.cameraView
        imageView = binding.imageView

        TTVSeg.createInstance(this)

        hasPermission = permissionsDelegate.hasPermissions()
        if (hasPermission) {
            cameraView!!.visibility = View.VISIBLE
        } else {
            permissionsDelegate.requestPermissions()
        }

        frontFotoapparat = Fotoapparat.with(this)
            .into(cameraView!!)
            .lensPosition(front())
            .frameProcessor(SampleFrameProcessor())
            .previewResolution { Resolution(720,720) }
            .build()

        val ret = TTVSeg.getInstance().setLicenseInfo("")

        if(ret == 0) {
            licenseValid = true
            init()
        }

        Log.e(TAG, "activation: " + ret)

        binding.btnCapture.setOnClickListener{
            binding.btnClear.visibility = View.GONE
            binding.btnCapture.isEnabled = false
            countdown()
        }

        binding.btnClear.setOnClickListener {
            binding.signaturePad.clear()
        }

        binding.btnSend.setOnClickListener {
            val idBitmap = binding.imageID.drawable.toBitmap(
                binding.imageID.drawable.intrinsicWidth,
                binding.imageID.drawable.intrinsicHeight
            )

            val matrix = Matrix()
            matrix.preScale(-1.0f, 1.0f);
            val rotated =
                Bitmap.createBitmap(
                    idBitmap,
                    0,
                    0,
                    binding.imageID.drawable.intrinsicWidth,
                    binding.imageID.drawable.intrinsicHeight,
                    matrix,
                    true
                )

            val sigBitmap = binding.imageSignature.drawable.toBitmap()

            createOrUpdate("$url/changeImageRequests", rotated, sigBitmap)
        }

        binding.btnTryAgain.setOnClickListener {
            binding.imageCapturing.visibility = View.VISIBLE
            binding.imagePreviewing.visibility = View.GONE
            binding.btnCapture.text = "Capture Images"
            binding.btnClear.visibility = View.VISIBLE
        }
    }

    private fun countdown(){
        object : CountDownTimer(6000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.btnCapture.text = (millisUntilFinished / 1000).toString()
            }
            override fun onFinish() {
                val idphoto = imageView!!.drawable.toBitmap()
                val signaturephoto = binding.signaturePad.signatureBitmap

                binding.imageCapturing.visibility = View.GONE
                binding.imagePreviewing.visibility = View.VISIBLE
                binding.btnCapture.isEnabled = true

                binding.imageID.setImageBitmap(idphoto)
                binding.imageSignature.setImageBitmap(signaturephoto)
            }
        }.start()
    }

    private fun init() {
        if (!licenseValid) {
            return
        }

        try {
            if (TTVSeg.getInstance().create(appCtx, 0, 0, 0) == 0) {
                humanSegInited = true
                return
            }
        } catch (e: TTVException) {
            e.printStackTrace()
        }
    }


    override fun onStart() {
        super.onStart()
        if (hasPermission) {
            frontFotoapparat!!.start()
        }
    }


    override fun onStop() {
        super.onStop()
        if (hasPermission) {
            try {
                frontFotoapparat!!.stop()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (permissionsDelegate.hasPermissions() && !hasPermission) {
            hasPermission = true
            cameraView!!.visibility = View.VISIBLE
            frontFotoapparat!!.start()
        } else {
            permissionsDelegate.requestPermissions()
        }
    }


    inner class SampleFrameProcessor : FrameProcessor {
        private var ttvSeg: TTVSeg? = null
        private val bgColor = intArrayOf(255, 255, 255)
        private var bgIdx: Int
        private var bgBitmapArr: ArrayList<Bitmap>? = null

        init {
            ttvSeg = TTVSeg.getInstance()
            bgIdx = 0
            bgBitmapArr = ArrayList<Bitmap>()

            val ids:TypedArray = resources.obtainTypedArray(R.array.scene)

            val len = ids.length() - 1
            for(i in 0..len) {
                val bitmap : Bitmap = BitmapFactory.decodeResource(resources, ids.getResourceId(i, -1))
                bgBitmapArr!!.add(bitmap)
            }

        }

        override fun invoke(frame: Frame) {

            if(!humanSegInited) {
                return
            }

            if(bgIdx >= bgBitmapArr!!.size)
                bgIdx = 0;

            val iArr = IntArray(1)
            val segment: Bitmap = ttvSeg!!.process(
                frame.image,
                frame.size.width,
                frame.size.height,
                frame.rotation,
                1,
                bgColor,
                bgBitmapArr!!.get(bgIdx),
                iArr
            )
            bgIdx ++;

            sendMessage(0, segment)
        }

        /* access modifiers changed from: private */ /* access modifiers changed from: public */
        private fun sendMessage(w: Int, o: Any) {
            val message = Message()
            message.what = w
            message.obj = o
            mHandler.sendMessage(message)
        }
    }

    fun createOrUpdate(url:String,idPhoto: Bitmap, signaturePhoto: Bitmap)
    {
        val extras = intent.extras
        id = extras!!.getString("id", "")

        val req = object : VolleyMultipartRequest(Method.POST,url,{
            Log.d("Response:",it.statusCode.toString())
            if (it.statusCode == 200){
                val alert = AlertDialog.Builder(this)
                alert.setMessage("Images uploaded. You may now proceed to ITU Office for ID Printing.")
                    .setCancelable(false)
                    .setPositiveButton("OK", DialogInterface.OnClickListener { dialog, id ->
                        finish()
                    })
                alert.create().show()
            }
            else{
                Log.d("Error","URL not found")
            }

        },{
        }) {
            override fun getParams(): MutableMap<String, String>? {
                val params: MutableMap<String, String> = HashMap()
                params.put("id", id)
                return params
            }

            override fun getByteData(): MutableMap<String, DataPart> {
                val params:MutableMap<String, DataPart> = HashMap()
                params.put("idphoto",DataPart("ID-$id.jpg",getFileData(idPhoto)))
                params.put("signaturephoto",DataPart("SIG-$id.jpg",getFileData(signaturePhoto)))
                return params
            }
        }

        Volley.newRequestQueue(this).add(req)
    }

    fun getFileData(bitmap: Bitmap) : ByteArray {

        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG,80,bos)
        return bos.toByteArray()
    }
}