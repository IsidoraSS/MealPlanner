package rs.raf.mealPlanner.presentation.view.recycler.diff

import androidx.recyclerview.widget.DiffUtil
import rs.raf.mealPlanner.data.models.utils.Filter

class FilterDiffCallback : DiffUtil.ItemCallback<Filter>(){

    override fun areItemsTheSame(oldItem: Filter, newItem: Filter): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: Filter, newItem: Filter): Boolean {
        return oldItem.name == newItem.name
    }
}