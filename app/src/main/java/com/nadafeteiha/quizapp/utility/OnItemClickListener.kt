package com.nadafeteiha.quizapp.utility

import android.graphics.ColorSpace.Model
import com.nadafeteiha.quizapp.data.TriviaCategory


interface OnItemClickListener {
    fun onItemClick(item: TriviaCategory )
}