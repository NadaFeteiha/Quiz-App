package com.nadafeteiha.quizapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.nadafeteiha.quizapp.utility.OnItemClickListener
import com.nadafeteiha.quizapp.R
import com.nadafeteiha.quizapp.data.TriviaCategory

internal class CategoryAdapter (private val categoryList: List<TriviaCategory>,
                                private val onItemClickListener: OnItemClickListener
) :
    RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvCategoryName: TextView
        val categoryItemCard: CardView
        init {
            // Define click listener for the ViewHolder's View.
            tvCategoryName = view.findViewById(R.id.tv_name_category)
            categoryItemCard = view.findViewById(R.id.category_item)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_category, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.tvCategoryName.text = categoryList.get(position).name

        viewHolder.categoryItemCard.setOnClickListener { onItemClickListener.onItemClick(categoryList[position]) }
    }

    override fun getItemCount() = categoryList.size

}