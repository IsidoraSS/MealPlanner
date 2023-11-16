package rs.raf.mealPlanner.presentation.view.recycler.viewholder

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import rs.raf.mealPlanner.data.models.utils.Meal
import rs.raf.mealPlanner.databinding.RecyclerItemMealBinding
import rs.raf.mealPlanner.presentation.view.fragments.DialogPlanFilter

class PlanDialogMealViewHolder(private val itemBinding: RecyclerItemMealBinding, private val fragment: Fragment) : RecyclerView.ViewHolder(itemBinding.root) {

    fun bind(meal: Meal) {
        itemBinding.titleTv.text = meal.name
        Glide.with(fragment)
            .load(meal.image+"/preview")
            .fitCenter()
            .circleCrop()
            .into(itemBinding.imageView)
        itemBinding.RWItem.setOnClickListener{
            val myFragment=fragment as DialogPlanFilter
            myFragment.dismissDialogAndSaveMeal(meal)
        }
    }

}