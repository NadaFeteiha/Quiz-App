package com.nadafeteiha.quizapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.nadafeteiha.quizapp.databinding.ActivityLoginBinding
import com.nadafeteiha.quizapp.utility.Constant
import android.provider.MediaStore
import android.graphics.Bitmap
import android.app.Activity
import android.content.Context
import android.util.Base64
import androidx.activity.result.contract.ActivityResultContracts
import com.nadafeteiha.quizapp.data.User
import java.io.ByteArrayOutputStream

class LoginActivity : AppCompatActivity(), View.OnClickListener{

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    private fun doneAction(){
        if (binding.etName.text!!.isNotEmpty()){
          val username =  binding.etName.text!!.trim().toString()
            savePreferences(username)
            intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            this.finish()
        }else{
            Toast.makeText(this,getString(R.string.mgs_username), Toast.LENGTH_LONG).show()
        }
    }

    private fun savePreferences(username: String){
        val sharedPreference =  getSharedPreferences(Constant.PREFERENCE_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreference.edit()
        editor.putString(Constant.username,username)
        editor.apply()
        User.userName =username
    }

    private fun saveImgPreferences(img: Bitmap){
        val sharedPreference =  getSharedPreferences(Constant.PREFERENCE_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreference.edit()
        User.profileImg = img.toBase64String()
        editor.putString(Constant.imgProfile,  User.profileImg)
        editor.apply()
    }

    //Convert bitmap to String so I can save it
    fun Bitmap.toBase64String():String{
        ByteArrayOutputStream().apply {
            compress(Bitmap.CompressFormat.JPEG,10,this)
            return Base64.encodeToString(toByteArray(),Base64.DEFAULT)
        }
    }

    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val img = data?.data
            binding.ivUser.setImageURI(img)
            val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, img)
            saveImgPreferences(bitmap)
        }
    }

    private fun pickupImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        resultLauncher.launch(intent)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_start -> doneAction()
            R.id.iv_user -> pickupImageFromGallery()
        }
    }
}