package com.nadafeteiha.quizapp

import android.R.attr
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.nadafeteiha.quizapp.databinding.ActivityLoginBinding
import com.nadafeteiha.quizapp.utility.Constant
import com.nadafeteiha.quizapp.utility.MyCache
import android.R.attr.password

import android.content.SharedPreferences
import android.provider.MediaStore

import android.graphics.Bitmap
import android.graphics.ImageDecoder

import android.R.attr.bitmap

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
            Toast.makeText(this,"Please Enter Username!!", Toast.LENGTH_LONG).show()
        }
    }

    private fun savePreferences(username: String){
        val sharedPreference =  getSharedPreferences("PREFERENCE_NAME", android.content.Context.MODE_PRIVATE)
        var editor = sharedPreference.edit()
        editor.putString(Constant.username,username)
        editor.commit()
    }

    private fun selectImage(){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                == android.content.pm.PackageManager.PERMISSION_DENIED) {
                //permission denied
                val permission = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                //show popup to request runtime permission
                requestPermissions(permission, com.nadafeteiha.quizapp.utility.Constant.GALLERY_PERMISSION_CODE)
            } else {
                //permission already granted
                pickupImageFromGallery()
            }
        } else {
            //system os is < Marshmallow
            pickupImageFromGallery()
        }
    }

    private fun pickupImageFromGallery() {
        val intent = android.content.Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, com.nadafeteiha.quizapp.utility.Constant.GALLERY)
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<out String>,
                                            grantResults: IntArray)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode) {
            Constant.GALLERY_PERMISSION_CODE -> {
                if (grantResults.size >0 && grantResults[0] == android.content.pm.PackageManager.PERMISSION_GRANTED) {
                    //permission from popup granted
                    pickupImageFromGallery()
                } else {
                    //permission from popup denied
                    Toast.makeText(this,"permission denied",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == android.app.Activity.RESULT_OK && requestCode == Constant.GALLERY) {
            binding.ivUser.setImageURI(data?.data)

            val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, data?.data)
            MyCache.instance.saveBitmapToCahche(Constant.User_Profile_Pic, bitmap)
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_start -> doneAction()
            R.id.iv_user -> selectImage()
        }
    }
}