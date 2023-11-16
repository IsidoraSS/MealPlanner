package rs.raf.mealPlanner.presentation.view.recycler.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ListAdapter
import rs.raf.mealPlanner.data.models.utils.Category
import rs.raf.mealPlanner.databinding.RecyclerItemCategoryBinding
import rs.raf.mealPlanner.presentation.view.recycler.diff.CategoryDiffCallback
import rs.raf.mealPlanner.presentation.view.recycler.viewholder.CategoryViewHolder

class CategoryAdapter(private val fragment:Fragment) : ListAdapter<Category, CategoryViewHolder>(CategoryDiffCallback())  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val itemBinding = RecyclerItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(itemBinding, fragment)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}