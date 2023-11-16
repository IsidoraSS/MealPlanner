package rs.raf.mealPlanner.presentation.view.recycler.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ListAdapter
import rs.raf.mealPlanner.data.models.utils.Meal
import rs.raf.mealPlanner.databinding.RecyclerItemMealBinding
import rs.raf.mealPlanner.presentation.view.recycler.diff.MealDiffCallback
import rs.raf.mealPlanner.presentation.view.recycler.viewholder.MealViewHolder

class MealAdapter(private val fragment: Fragment) : ListAdapter<Meal, MealViewHolder>(MealDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealViewHolder {
        val itemBinding = RecyclerItemMealBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MealViewHolder(itemBinding, fragment)
    }
    override fun onBindViewHolder(holder: MealViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}