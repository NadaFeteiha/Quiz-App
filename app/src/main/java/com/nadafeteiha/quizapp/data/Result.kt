package com.nadafeteiha.quizapp.data

data class Result(
    var category: String = "",
    var correct_answer: String = "",
    var difficulty: String = "",
    var incorrect_answers: List<String> = listOf(),
    var question: String = "",
    var type: String = ""
)