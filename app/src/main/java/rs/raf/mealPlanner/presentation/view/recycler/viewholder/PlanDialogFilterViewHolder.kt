package rs.raf.mealPlanner.presentation.view.recycler.viewholder

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import rs.raf.mealPlanner.data.models.utils.Filter
import rs.raf.mealPlanner.databinding.RecyclerItemFilterBinding
import rs.raf.mealPlanner.presentation.view.fragments.DialogPlanFilter


class PlanDialogFilterViewHolder(private val itemBinding: RecyclerItemFilterBinding, private val fragment: Fragment) : RecyclerView.ViewHolder(itemBinding.root)  {
    fun bind(filter: Filter) {
        itemBinding.text1.text=filter.name
        itemBinding.RWItem.setOnClickListener{
            val myFragment=fragment as DialogPlanFilter

            myFragment.selectDialogFilterInMainViewModel(filter)
            myFragment.dismissDialog()
        }
    }
}