package com.nadafeteiha.quizapp.data

data class QuestionsResponse(
    var response_code: Int = 0,
    var results: List<Result> = listOf()
)