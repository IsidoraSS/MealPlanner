package rs.raf.mealPlanner.presentation.view.recycler.diff

import androidx.recyclerview.widget.DiffUtil
import rs.raf.mealPlanner.data.models.utils.Category

class CategoryDiffCallback : DiffUtil.ItemCallback<Category>(){

    override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem.name == newItem.name
    }
}