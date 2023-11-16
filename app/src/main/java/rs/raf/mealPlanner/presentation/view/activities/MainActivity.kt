package rs.raf.mealPlanner.presentation.view.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import rs.raf.mealPlanner.R
import rs.raf.mealPlanner.databinding.ActivityMainBinding
import rs.raf.mealPlanner.presentation.view.adapters.MainPagerAdapter
import rs.raf.mealPlanner.presentation.view.fragments.MainContainerFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    val pagerAdapter = MainPagerAdapter(supportFragmentManager,this)
    val mainContainerFragment=MainContainerFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init() {
        setFragment()
    }

//    private fun initUi() {
//        binding.viewPager.adapter=pagerAdapter
//        binding.tabLayout.setupWithViewPager(binding.viewPager)
//    }
//
    fun slideToFilterTab(){
        mainContainerFragment.slideToFilterTab()
    }

    fun slideToMealTab(){
        mainContainerFragment.slideToMealTab()
    }

    private fun setFragment(){
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, mainContainerFragment)
                .commitAllowingStateLoss()
    }

}
