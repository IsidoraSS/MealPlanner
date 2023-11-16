package rs.raf.mealPlanner.presentation.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import rs.raf.mealPlanner.R
import rs.raf.mealPlanner.databinding.FragmentMainContainerBinding
import rs.raf.mealPlanner.presentation.contract.MainContract
import rs.raf.mealPlanner.presentation.view.adapters.MainPagerAdapter
import rs.raf.mealPlanner.presentation.viewmodel.MainViewModel


class MainContainerFragment : Fragment(R.layout.fragment_main_container) {

    private val mainViewModel: MainContract.ViewModel by sharedViewModel<MainViewModel>()

    private var _binding: FragmentMainContainerBinding? = null

    lateinit var pagerAdapter:MainPagerAdapter

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainContainerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pagerAdapter= MainPagerAdapter(parentFragmentManager, this.context!!)
        init()
    }

    fun init(){
        initUi()
        initListeners()
    }

    private fun initUi() {
        binding.viewPager.adapter=pagerAdapter
    }

    private fun initListeners(){
        binding.bottomNavigationView.setOnNavigationItemSelectedListener(
                BottomNavigationView.OnNavigationItemSelectedListener { item ->
                    when (item.itemId) {
                        R.id.categories -> binding.viewPager.currentItem=0
                        R.id.filter -> binding.viewPager.currentItem=1
                        R.id.meal -> binding.viewPager.currentItem=2
                        R.id.plan -> binding.viewPager.currentItem=3
                        R.id.misc -> binding.viewPager.currentItem=4
                    }
                    false
                })
    }

    fun slideToFilterTab(){
        binding.viewPager.currentItem=1
    }

    fun slideToMealTab() {
        binding.viewPager.currentItem=2
    }
}