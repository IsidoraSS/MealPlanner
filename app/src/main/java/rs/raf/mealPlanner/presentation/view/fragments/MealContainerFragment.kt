package rs.raf.mealPlanner.presentation.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import rs.raf.mealPlanner.R
import rs.raf.mealPlanner.databinding.FragmentMealContainerBinding
import rs.raf.mealPlanner.presentation.contract.MainContract
import rs.raf.mealPlanner.presentation.viewmodel.MainViewModel


class MealContainerFragment : Fragment(R.layout.fragment_meal_container) {

    private val mainViewModel: MainContract.ViewModel by sharedViewModel<MainViewModel>()

    private var _binding: FragmentMealContainerBinding? = null

    val mealDetailsFragment=MealFragment()
    val mealEditFragment=EditMealFragment()

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMealContainerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    fun init(){
        childFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, mealDetailsFragment)
            .commitAllowingStateLoss()
    }

    fun replaceDetailsWithEdit(){
        childFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer,mealEditFragment)
            .commitAllowingStateLoss()
    }

    fun replaceEditWithDetails(){
        childFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer,mealDetailsFragment)
            .commitAllowingStateLoss()
    }

}