package rs.raf.mealPlanner.presentation.view.recycler.viewholder

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import rs.raf.mealPlanner.data.models.utils.Category
import rs.raf.mealPlanner.databinding.CategoryDialogBinding
import rs.raf.mealPlanner.databinding.RecyclerItemCategoryBinding

import rs.raf.mealPlanner.presentation.view.fragments.CategoriesFragment

class CategoryViewHolder(private val itemBinding: RecyclerItemCategoryBinding, private val fragment:Fragment) : RecyclerView.ViewHolder(itemBinding.root) {
    fun bind(category: Category) {
        itemBinding.titleTv.text = category.name
        Glide.with(fragment)
            .load(category.image)
            .fitCenter()
            .circleCrop()
            .into(itemBinding.imageView)

        itemBinding.infoButton.setOnClickListener {
            showCategoryDialog(category)
        }

        itemBinding.RWItem.setOnClickListener{
            val myFragment=fragment as CategoriesFragment

            myFragment.selectCategoryInMainViewModel(category) //kada se promeni kategorija u view modelu to detektuje filter fragment observerom i radi dalje
            myFragment.slideToFilterTab()
        }
    }

    private fun showCategoryDialog(category: Category){
        val dialog=Dialog(fragment.requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)

        val binding: CategoryDialogBinding = CategoryDialogBinding.inflate(LayoutInflater.from(fragment.context))
        dialog.setContentView(binding.getRoot())

        val width = (fragment.getResources().getDisplayMetrics().widthPixels * 0.9)
        dialog.window!!.setLayout(width.toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.textView.text=category.description
        binding.floatingActionButton.setOnClickListener{
            dialog.dismiss()
        }
        dialog.show()
    }
}