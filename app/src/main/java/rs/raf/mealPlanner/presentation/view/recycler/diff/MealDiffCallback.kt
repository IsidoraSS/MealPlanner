package rs.raf.mealPlanner.presentation.view.recycler.diff

import androidx.recyclerview.widget.DiffUtil
import rs.raf.mealPlanner.data.models.utils.Meal

class MealDiffCallback : DiffUtil.ItemCallback<Meal>() {

    override fun areItemsTheSame(oldItem: Meal, newItem: Meal): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Meal, newItem: Meal): Boolean {
        return oldItem.name == newItem.name
    }

}