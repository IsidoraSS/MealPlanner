package rs.raf.mealPlanner.presentation.view.recycler.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filterable
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ListAdapter
import rs.raf.mealPlanner.data.models.utils.Filter
import rs.raf.mealPlanner.databinding.RecyclerItemFilterBinding
import rs.raf.mealPlanner.presentation.view.recycler.diff.FilterDiffCallback
import rs.raf.mealPlanner.presentation.view.recycler.viewholder.PlanDialogFilterViewHolder

class PlanDialogFilterAdapter(private val fragment: Fragment) : ListAdapter<Filter, PlanDialogFilterViewHolder>(FilterDiffCallback()), Filterable {
    private var list = listOf<Filter>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlanDialogFilterViewHolder {
        val itemBinding = RecyclerItemFilterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlanDialogFilterViewHolder(itemBinding, fragment)
    }

    override fun onBindViewHolder(holder: PlanDialogFilterViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun setData(list: List<Filter>){
        this.list = list
        submitList(list)
    }

    override fun getFilter(): android.widget.Filter {
        return customFilter
    }

    private val customFilter = object : android.widget.Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filteredList = mutableListOf<Filter>()
            if (constraint == null || constraint.isEmpty()) {
                filteredList.addAll(list)
            } else {
                for (item in list) {
                    if (item.name.toLowerCase().startsWith(constraint.toString().toLowerCase())) {
                        filteredList.add(item)
                    }
                }
            }
            val results = FilterResults()
            results.values = filteredList
            return results
        }

        override fun publishResults(constraint: CharSequence?, filterResults: FilterResults?) {
            submitList(filterResults?.values as MutableList<Filter>)
        }
    }

}