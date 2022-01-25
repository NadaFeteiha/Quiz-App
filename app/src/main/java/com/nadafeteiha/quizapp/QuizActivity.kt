package com.nadafeteiha.quizapp

import android.content.Intent
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.nadafeteiha.quizapp.apis.ApiInterface
import com.nadafeteiha.quizapp.data.QuestionsResponse
import com.nadafeteiha.quizapp.data.Result
import com.nadafeteiha.quizapp.databinding.ActivityQuizBinding
import com.nadafeteiha.quizapp.utility.Constant
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class QuizActivity : AppCompatActivity() , View.OnClickListener{
    private lateinit var binding: ActivityQuizBinding
    private lateinit var categoryName:String
    private  var categoryID:Int = 0
    private val apiInterface = ApiInterface.create()
    private lateinit var questions: List<Result>
    private val options = ArrayList<TextView>()
    private var mSelectedOptionPosition = -1
    private var isQuestionSubmitted:Boolean = false
    private var questionPosition = -1
    private var userScore = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initLayout()

        callQuestionsAPIs()
    }

    private fun initLayout(){
        categoryName = intent.getStringExtra(Constant.TriviaCategoryName).toString()
        categoryID = intent.getIntExtra(Constant.TriviaCategoryID,-1)

        binding.toolbarQuiz.title = categoryName
        setSupportActionBar(binding.toolbarQuiz)
        if (supportActionBar!=null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowTitleEnabled(true)
        }

        binding.toolbarQuiz.getNavigationIcon()
            ?.setColorFilter(getResources().getColor(R.color.white),
                PorterDuff.Mode.SRC_ATOP);
        binding.toolbarQuiz.setNavigationOnClickListener {
            onBackPressed()
        }

        binding.progressBar.progress = 0
        binding.progressBar.max = Constant.MaxQuestionsNumber
        binding.progressBar.progress = 1

        // textViews display multiple chose
        options.add(0,binding.tvOptionOne)
        options.add(1,binding.tvOptionTwo)
        options.add(2,binding.tvOptionThree)
        options.add(3,binding.tvOptionFour)
    }

    private fun setData(){
        binding.progressBar.progress = questionPosition
        binding.tvProgress.text = "${questionPosition+1}/${Constant.MaxQuestionsNumber}"

        var optionList= ArrayList<String>()
        optionList.add(questions[questionPosition].correct_answer)
        optionList.addAll(questions[questionPosition].incorrect_answers)
        optionList.shuffle()

        // update question and chooses
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            binding.tvQuestion.text= Html.fromHtml(questions[questionPosition].question, Html.FROM_HTML_MODE_COMPACT)
        } else {
            binding.tvQuestion.text= Html.fromHtml(questions[questionPosition].question)
        }

        binding.tvOptionOne.text = optionList[0]
        binding.tvOptionTwo.text = optionList[1]
        binding.tvOptionThree.text = optionList[2]
        binding.tvOptionFour.text = optionList[3]
    }

    private fun setNextQuestion(){
        questionPosition++
        mSelectedOptionPosition = -1
        isQuestionSubmitted = false
        binding.btnAction.text = getString(R.string.btn_submit_answer)
        setData()
        defaultOptionsView()
    }

    private fun defaultOptionsView(){
        for (option in options){
            option.setTextColor(ContextCompat.getColor(this,R.color.lavender_purple))
            option.typeface = Typeface.DEFAULT_BOLD
            option.background = ContextCompat.getDrawable(this,
                R.drawable.default_option_border)
        }
    }

    /**
     * this function to highlight the selected answer that user select
     * **/
    private fun selectedOptionView(tv: TextView, selectedOptionNum: Int) {
        if (!isQuestionSubmitted){
            defaultOptionsView()
            mSelectedOptionPosition = selectedOptionNum

            tv.setTextColor(ContextCompat.getColor(this, R.color.jagger))
            tv.setTypeface(tv.typeface, Typeface.BOLD)
            tv.background = ContextCompat.getDrawable(
                this,
                R.drawable.selected_option_border_bg
            )
        }
    }

    /**
     * this function to:
     * 1- display the correct and the wrong one ..
     * 2- calculate user score ..
     * **/
    private fun correctOptionsView(){
        val correctAnswer = questions[questionPosition].correct_answer
        var i = 0
        for (option in options){
            if (correctAnswer == option.text){
                if (mSelectedOptionPosition == i){
                    userScore += 10
                }
                option.setTextColor(ContextCompat.getColor(this, android.R.color.holo_green_dark))
                option.background =ContextCompat.getDrawable(this, R.drawable.correct_option_bg)
            }else if (mSelectedOptionPosition!=-1 && mSelectedOptionPosition == i){
                option.setTextColor(ContextCompat.getColor(this, android.R.color.holo_red_dark))
                option.background =ContextCompat.getDrawable(this, R.drawable.wrong_option_bg)
            }
            i++
        }
    }

    private fun callQuestionsAPIs(){
        apiInterface.getQuizQuestions(categoryID,Constant.MaxQuestionsNumber).enqueue(object : Callback<QuestionsResponse>{
            override fun onResponse(
                call: Call<QuestionsResponse>,
                response: Response<QuestionsResponse>
            ) {
                if (response.body() != null) {
                    questions = response.body()!!.results
                    setNextQuestion()
                }
            }
            override fun onFailure(call: Call<QuestionsResponse>, t: Throwable) {
                Toast.makeText(this@QuizActivity, t.message,Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun updateViewSubmitted(){
        binding.progressBar.progress = questionPosition+1
        if (questionPosition+1 < Constant.MaxQuestionsNumber){
            binding.btnAction.text = getString(R.string.btn_next_question)
        }else{
            binding.btnAction.text = getString(R.string.btn_finish_answer)
        }
    }

    private fun actionDo(){
        if (!isQuestionSubmitted  && mSelectedOptionPosition!= -1){
            updateViewSubmitted()
            correctOptionsView()
            isQuestionSubmitted = true
        }else if (!isQuestionSubmitted  && mSelectedOptionPosition == -1) {
            Toast.makeText(this,
                "please select answer!!",
                Toast.LENGTH_LONG)
                .show()
        }else if (binding.btnAction.text.equals(getString(R.string.btn_finish_answer))) {
            val intent = Intent(this,FinishActivity::class.java)
            intent.putExtra(Constant.User_Score , userScore)
            startActivity(intent)
            this@QuizActivity.finish()
        }else{
            setNextQuestion()
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_action -> actionDo()

            R.id.tv_option_one ->{
                selectedOptionView(v as TextView,0)
            }
            R.id.tv_option_two ->{
                selectedOptionView(v as TextView,1)
            }
            R.id.tv_option_three ->{
                selectedOptionView(v as TextView,2)
            }
            R.id.tv_option_four ->{
                selectedOptionView(v as TextView,3)
            }
        }
    }

}
