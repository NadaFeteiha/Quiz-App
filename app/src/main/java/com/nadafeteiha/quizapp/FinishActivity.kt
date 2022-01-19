package com.nadafeteiha.quizapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.nadafeteiha.quizapp.databinding.ActivityFinishBinding
import com.nadafeteiha.quizapp.utility.Constant

class FinishActivity : AppCompatActivity() , View.OnClickListener {

    private lateinit var binding: ActivityFinishBinding
    private  var userScore = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFinishBinding.inflate(layoutInflater)
        setContentView(binding.root) //activity_finish

        userScore = intent.getIntExtra(Constant.User_Score,-1)

        binding.tvScore.text = "Your Score is $userScore"

        if (userScore > 0){
            binding.tvMsg.text =  getString(R.string.tv_congrats)
        }else{
            binding.tvMsg.text =  getString(R.string.tv_good_luck)
        }


    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_done -> this@FinishActivity.finish()

        }
    }

}