package rs.raf.mealPlanner.presentation.view.adapters

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import rs.raf.mealPlanner.R
import rs.raf.mealPlanner.presentation.view.fragments.*

class MainPagerAdapter(
    fragmentManager: FragmentManager,
    private val context: Context
) : FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    companion object {
        private const val ITEM_COUNT = 5
        const val FRAGMENT_1 = 0
        const val FRAGMENT_2 = 1
        const val FRAGMENT_3 = 2
        const val FRAGMENT_4 = 3
        const val FRAGMENT_5 = 4
    }

    val categoriesFragment = CategoriesFragment()
    val filterFragment=FilterFragment()
    val mealContainerFragment=MealContainerFragment()
    val planFragment=PlanFragment()
    val miscFragment=MiscFragment()

    override fun getItem(position: Int): Fragment {
        return when(position) {
            FRAGMENT_1 -> categoriesFragment
            FRAGMENT_2-> filterFragment
            FRAGMENT_3-> mealContainerFragment
            FRAGMENT_4-> planFragment
            else -> miscFragment
        }
    }

    override fun getCount(): Int {
        return ITEM_COUNT
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when(position) {
            FRAGMENT_1 -> context.getString(R.string.categories)
            FRAGMENT_2-> context.getString(R.string.filter)
            FRAGMENT_3-> context.getString(R.string.meal)
            FRAGMENT_4->context.getString(R.string.plan)
            else -> context.getString(R.string.misc)
        }
    }



}