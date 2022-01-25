package com.nadafeteiha.quizapp

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.nadafeteiha.quizapp.data.TriviaCategory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.nadafeteiha.quizapp.adapter.CategoryAdapter
import com.nadafeteiha.quizapp.apis.ApiInterface
import com.nadafeteiha.quizapp.data.CategoryResponse
import com.nadafeteiha.quizapp.data.User
import com.nadafeteiha.quizapp.databinding.ActivityMainBinding
import com.nadafeteiha.quizapp.utility.Constant
import com.nadafeteiha.quizapp.utility.OnItemClickListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() , OnItemClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var categoryResponse: CategoryResponse
    private lateinit var  adapter: CategoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setLayout()

        getCategoryAPIs()
    }

    private fun setLayout(){
        binding.tvUsername.text = getString(R.string.tv_greeting,User.userName)
        binding.tvScore.text = getString(R.string.tv_score , User.score)
        binding.ivUser.setImageBitmap(convertStringToImg( User.profileImg))
        binding.recyclerview.visibility = View.GONE
    }

    private fun convertStringToImg(img : String):Bitmap{
        val imageBytes = Base64.decode(img, 0)
       return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }

    private fun setView(){
        binding.loadingIndicator.visibility = View.GONE
        binding.recyclerview.visibility = View.VISIBLE
        binding.recyclerview.layoutManager = LinearLayoutManager(this)
        adapter = CategoryAdapter(categoryResponse.trivia_categories, this)
        binding.recyclerview.adapter = adapter
    }

    private fun getCategoryAPIs(){
        binding.loadingIndicator.visibility = View.VISIBLE
        val apiInterface = ApiInterface.create().getCategory()

        apiInterface.enqueue( object : Callback<CategoryResponse> {
            override fun onResponse(call: Call<CategoryResponse>?, response: Response<CategoryResponse>?) {

                if(response?.body() != null){
                    categoryResponse = response.body()!!
                    setView()
                }
            }

            override fun onFailure(call: Call<CategoryResponse>, t: Throwable) {
                Toast.makeText(this@MainActivity, t.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun onItemClick(item: TriviaCategory) {
        var intent = Intent(this@MainActivity , QuizActivity::class.java)
        intent.putExtra(Constant.TriviaCategoryName, item.name)
        intent.putExtra(Constant.TriviaCategoryID, item.id)
        startActivity(intent)
    }
}